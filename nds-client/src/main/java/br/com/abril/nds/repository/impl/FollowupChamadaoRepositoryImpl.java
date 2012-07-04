package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
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

		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("sum((produtoEdicao.precoVenda - produtoEdicao.desconto) * (estoqueProdCota.qtdeRecebida - estoqueProdCota.qtdeDevolvida)) as valorTotalConsignado,  ");
		hql.append("lancamento.dataRecolhimentoPrevista as dataProgramadoChamadao, ");
		hql.append("historico.dataEdicao as dataHistoricoEdicao");
		
		hql.append(getSqlFromEWhereChamadao(filtro));
		
		Query query =  getSession().createQuery(hql.toString());		

		HashMap<String, Object> param = aplicarParametros(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupChamadaoDTO.class));

		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		return query.list();
		
	}
	
	
	
	private String getSqlFromEWhereChamadao(FiltroFollowupChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();	

		hql.append(" from EstoqueProdutoCota estoqueProdCota,  HistoricoSituacaoCota as historico, Lancamento as lancamento, ChamadaEncalheCota as cec, ");
		hql.append(" Cota as cota, ChamadaEncalhe as ce, Pessoa as pessoa, ProdutoEdicao produtoEdicao ");		
		
		hql.append(" WHERE cota.situacaoCadastro = 'SUSPENSO' ");
		hql.append(" AND ce.tipoChamadaEncalhe = 'CHAMADAO' ");
		hql.append(" AND historico.cota.id = cota.id ");
		hql.append(" AND lancamento.produtoEdicao.id = produtoEdicao.id ");
		hql.append(" AND cec.cota.id = cota.id ");
		hql.append(" AND estoqueProdCota.cota.id = cota.id ");
		hql.append(" AND cec.chamadaEncalhe.id = ce.id ");
		hql.append(" AND cota.pessoa.id = pessoa.id ");
		hql.append(" AND estoqueProdCota.produtoEdicao.id = produtoEdicao.id ");
		
		hql.append(" GROUP BY cota.id  ");		

		return hql.toString();
	}


   private HashMap<String,Object> aplicarParametros(FiltroFollowupChamadaoDTO filtro) {
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		//if(filtro.getDataOperacao() != null ) { 
		//	param.put("dataoperacao", filtro.getDataOperacao());
		//}
//		if(filtro.getValorConsignadoLimite() != null){
//			param.put("valorconsignadolimite", filtro.getValorConsignadoLimite());
//		}
//		if(filtro.getQuantidadeDiasSuspenso() >= 0) {
//			param.put("qtddiassuspenso", filtro.getQuantidadeDiasSuspenso());
//		}
		
		return param;
   }	
}
