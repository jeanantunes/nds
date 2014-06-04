package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupChamadaoRepository;

@Repository
public class FollowupChamadaoRepositoryImpl  extends AbstractRepositoryModel<ConsultaFollowupChamadaoDTO,Long> implements FollowupChamadaoRepository {

	public FollowupChamadaoRepositoryImpl() {
		super(ConsultaFollowupChamadaoDTO.class);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupChamadaoDTO> obterConsignadosParaChamadao(FiltroFollowupChamadaoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("sum(movimentos.valoresAplicados.precoComDesconto * (estoqueProdCota.qtdeRecebida - estoqueProdCota.qtdeDevolvida)) as valorTotalConsignado,  ");
		hql.append("lancamento.dataRecolhimentoPrevista as dataProgramadoChamadao, ");
		hql.append("historico.dataEdicao as dataHistoricoEdicao, ");
		hql.append("datediff((select dist.dataOperacao from Distribuidor dist), historico.dataInicioValidade) as qtdDiasSuspensao");
		
		hql.append(getSqlFromEWhereChamadao(filtro));
		
		Query query =  getSession().createQuery(hql.toString());		

		HashMap<String, Object> param = aplicarParametros(filtro);
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupChamadaoDTO.class));
		
		if(filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		return query.list();
		
	}
	
	
	
	private String getSqlFromEWhereChamadao(FiltroFollowupChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();	

		hql.append(" from EstoqueProdutoCota estoqueProdCota ");
		hql.append("  join estoqueProdCota.movimentos movimentos ");
		hql.append("  join estoqueProdCota.cota cota ");
		hql.append("  join cota.pessoa pessoa ");
		hql.append("  join cota.historicos historico WITH historico.novaSituacao = 'SUSPENSO' ");
		hql.append("  join cota.chamadaEncalheCotas cec ");
		hql.append("  join cec.chamadaEncalhe ce ");
		hql.append("  join estoqueProdCota.produtoEdicao produtoEdicao ");
		hql.append("  join produtoEdicao.produto produto ");
		hql.append("  join produto.fornecedores fornecedor ");
		hql.append("  join produtoEdicao.lancamentos lancamento ");
		
		hql.append(" WHERE cota.situacaoCadastro = 'SUSPENSO' ");
		hql.append(" AND ce.tipoChamadaEncalhe = 'CHAMADAO' ");
		hql.append(" GROUP BY cota.numeroCota, pessoa.nome");		
		hql.append(" ORDER BY cota.numeroCota, pessoa.nome");

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
