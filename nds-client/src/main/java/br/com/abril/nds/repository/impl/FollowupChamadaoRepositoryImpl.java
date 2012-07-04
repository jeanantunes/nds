package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.repository.FollowupChamadaoRepository;

@Repository
public class FollowupChamadaoRepositoryImpl  extends AbstractRepositoryModel<ConsultaFollowupChamadaoDTO,Long> implements FollowupChamadaoRepository {

	public FollowupChamadaoRepositoryImpl() {
		super(ConsultaFollowupChamadaoDTO.class);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupChamadaoDTO> obterConsignadosParaChamadao(
			FiltroFollowupChamadaoDTO filtro) {

		StringBuilder myqrystr = new StringBuilder();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("sum((produtoEdicao.preco_venda - produtoEdicao.desconto) * (estoqueProdCota.qtde_recebida - estoqueProdCota.qtde_devolvida)) as valorTotalConsignado ");
		hql.append("lancamento.dataRecolhimentoPrevista as dataProgramadoChamadao, ");
		hql.append("historico.dataEdicao as dataHistoricoEdicao");
		
		hql.append(getSqlFromEWhereChamadao(filtro));
		
		
		myqrystr.append("select ifnull(c.id,0) as numeroCota, ");
		myqrystr.append(" ifnull(p.nome,'<NULL NAME>') as nomeJornaleiro,");
		myqrystr.append(" ifnull(sum((produtoEdicao.preco_venda - produtoEdicao.desconto) * "); 
		myqrystr.append(" (estoqueProdCota.qtde_recebida - estoqueProdCota.qtde_devolvida)),0) "); 
		myqrystr.append("  as valorTotalConsignado, ");
		myqrystr.append(" ifnull(l.data_rec_prevista,str_to_date('01,01,1900','%d,%m,%Y')) as dataProgramadoChamadao, ");
		myqrystr.append(" ifnull(datediff(sysdate(),hsc.data_edicao),0) as qtdDiasCotaSuspensa ");

		myqrystr.append(" from cota c, ");
		myqrystr.append(" pessoa p, ");
		myqrystr.append(" produto_edicao produtoEdicao, estoque_produto_cota estoqueProdCota, ");
		myqrystr.append(" historico_situacao_cota hsc, ");
		myqrystr.append(" lancamento l, ");
		myqrystr.append(" chamada_encalhe_cota cec, ");
		myqrystr.append(" chamada_encalhe ce ");
		myqrystr.append(" where c.situacao_cadastro = 'SUSPENSO' ");
		myqrystr.append(" and p.id = c.pessoa_id ");
		myqrystr.append(" and estoqueProdCota.cota_id = c.id ");
		myqrystr.append(" and estoqueProdCota.produto_edicao_id = produtoEdicao.id ");
		myqrystr.append(" and hsc.cota_id = c.id ");
		// quoteds...
		//myqrystr.append(" and str_to_date( ':dataoperacao' ,'%d,%m,%Y') between hsc.data_inicio_validade and hsc.data_fim_validade ");
		myqrystr.append(" and str_to_date( '"+filtro.getDataOperacao().charAt(8)+filtro.getDataOperacao().charAt(9)+","
				                             +filtro.getDataOperacao().charAt(5)+filtro.getDataOperacao().charAt(6)+","
				                             +filtro.getDataOperacao().charAt(0)+filtro.getDataOperacao().charAt(1)+filtro.getDataOperacao().charAt(2)+filtro.getDataOperacao().charAt(3) 
				                             +"' ,'%d,%m,%Y') between hsc.data_inicio_validade and hsc.data_fim_validade ");		
		myqrystr.append(" and l.produto_edicao_id = estoqueProdCota.produto_edicao_id ");
		myqrystr.append(" and l.produto_edicao_id = produtoEdicao.id ");
		myqrystr.append(" and cec.cota_id = c.id ");
		myqrystr.append(" and cec.chamada_encalhe_id = ce.id ");
		myqrystr.append(" and ce.produto_edicao_id = produtoEdicao.id ");
		myqrystr.append(" and ce.tipo_chamada_encalhe = 'CHAMADAO' ");
		myqrystr.append(" group by c.id, p.nome, l.data_rec_prevista, datediff(sysdate(),hsc.data_edicao) ");
		myqrystr.append(" having qtdDiasCotaSuspensa > :qtddiassuspenso  ");
        myqrystr.append("    and valorTotalConsignado > :valorconsignadolimite ");

		Query qry2db = getSession().createSQLQuery(myqrystr.toString())
		  .addScalar("numeroCota", StandardBasicTypes.LONG)
		  .addScalar("nomeJornaleiro", StandardBasicTypes.STRING )
		  .addScalar("valorTotalConsignado", StandardBasicTypes.BIG_DECIMAL)
		  .addScalar("dataProgramadoChamadao", StandardBasicTypes.DATE )
		  .addScalar("qtdDiasCotaSuspensa", StandardBasicTypes.LONG);

		HashMap<String, Object> param = aplicarParametros(filtro);
		
		for(String key : param.keySet()){
			qry2db.setParameter(key, param.get(key));
		}
		
		qry2db.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupChamadaoDTO.class));

		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			qry2db.setFirstResult(filtro.getPaginacao().getPosicaoInicial());

		/**
		 * regular test.
		 *
		 */
		List<ConsultaFollowupChamadaoDTO> m23 = qry2db.list();		
		  System.out.println("\n======================================================================================================================================"+
				  "\n===== REGULAR TEST ===== RETORNO DA LISTA FOLLOW UP CHAMADAO =====" +
				  "\n======================================================================================================================================\n");
		for (Iterator it = m23.iterator(); it.hasNext(); ) {
		 ConsultaFollowupChamadaoDTO cfdto = (ConsultaFollowupChamadaoDTO) it.next();
		 System.out.println("numeroCota[" + cfdto.getNumeroCota() +
		      "]  nomeJornaleiro["  + cfdto.getNomeJornaleiro() +
		      "]  valorTotalConsignado[" + cfdto.getValorTotalConsignado() + 
		      "]  dataProgramadoChamadao[" + cfdto.getDataProgramadoChamadao()+
		      "]  qtdDiasCotaSuspensa["  + cfdto.getDataProgramadoChamadao()  );
		System.out.println("\n======================================================================================================================================\n");
		}
		return qry2db.list();
		
	}
	
	private String getSqlFromEWhereChamadao(FiltroFollowupChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from EstoqueProdutoCota estoqueProdCota ");
		hql.append(" LEFT JOIN estoqueProdCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN estoqueProdCota.produtoEdicao as produtoEdicao");
		hql.append(" LEFT JOIN HistoricoSituacaoCota as historico WITH historico.cota.id = cota.id");
		hql.append(" LEFT JOIN Lancamento as lancamento WITH lancamento.produtoEdicao.id = produtoEdicao.id");
		hql.append(" LEFT JOIN ChamadaEncalheCota as cec WITH cec.cota.id = cota.id");
		hql.append(" LEFT JOIN cec.chamadaEncalhe as ce ");
		
		hql.append(" WHERE cota.situacaoCadastro = 'PENDENTE' ");
		hql.append(" WHERE ce.tipoChamadaEncalhe = 'CHAMADAO' ");
		

//		boolean usarAnd = false;
//		
//		if(filtro.getIdBox() != null ) { 
//			hql.append( (usarAnd ? " and ":" where ") +" box.id = :idBox ");
//			usarAnd = true;
//		}
//		if(filtro.getIdRoteiro() != null){
//			hql.append( (usarAnd ? " and ":" where ") + " roteiro.id = :idRoteiro ");
//			usarAnd = true;
//		}
//		if(filtro.getIdRota() != null){
//			hql.append( (usarAnd ? " and ":" where ") + " rota.id = :idRota ");
//			usarAnd = true;
//		}


		return hql.toString();
	}


   private HashMap<String,Object> aplicarParametros(FiltroFollowupChamadaoDTO filtro) {
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		//if(filtro.getDataOperacao() != null ) { 
		//	param.put("dataoperacao", filtro.getDataOperacao());
		//}
		if(filtro.getValorConsignadoLimite() != null){
			param.put("valorconsignadolimite", filtro.getValorConsignadoLimite());
		}
		if(filtro.getQuantidadeDiasSuspenso() >= 0) {
			param.put("qtddiassuspenso", filtro.getQuantidadeDiasSuspenso());
		}
		
		return param;
   }	
	
}
