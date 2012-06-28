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
		      "]  qtdDiasCotaSuspensa["  + cfdto.getQtdDiasCotaSuspensa()  );
		}
		System.out.println("\n======================================================================================================================================\n");
		
		return qry2db.list();
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
