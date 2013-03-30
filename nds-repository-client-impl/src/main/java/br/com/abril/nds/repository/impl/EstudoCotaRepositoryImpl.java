package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoCotaRepositoryImpl extends AbstractRepositoryModel<EstudoCota, Long> implements EstudoCotaRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoCotaRepositoryImpl() {
		
		super(EstudoCota.class);
	}

	@Override
	public EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		String hql = " from EstudoCota estudoCota"
				   + " where estudoCota.cota.numeroCota = :numeroCota"
				   + " and estudoCota.estudo.dataLancamento >= :dataReferencia";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataReferencia", dataReferencia);
		
		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
			
		String hql = " select estudoCota.id as id, " 
				   + " estudoCota.qtdeEfetiva as qtdeEfetiva, "
				   + " cota.id as idCota "
				   + " from EstudoCota estudoCota "
				   + " join estudoCota.estudo estudo "
				   + " join estudoCota.cota cota "
				   + " join estudo.produtoEdicao produtoEdicao "
				   + " where estudo.dataLancamento = :dataLancamento " 
				   + " and produtoEdicao.id = :idProdutoEdicao";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setResultTransformer(Transformers.aliasToBean(EstudoCotaDTO.class));
		
		return query.list();
	}
	
	@Override
	public EstudoCota obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota) {
			
		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento= :dataLancamento " 
				   + " and estudoCota.estudo.produtoEdicao.id= :idProdutoEdicao " 
				   + " and estudoCota.cota.id = :idCota";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);

		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	public EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, 
																  Long idProdutoEdicao, 
																  Integer numeroCota) {
		
		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento <= :dataLancamentoDistribuidor " 
				   + " and estudoCota.estudo.produtoEdicao.id = :idProdutoEdicao " 
				   + " and estudoCota.cota.numeroCota = :numeroCota "
				   + " order by estudoCota.estudo.dataLancamento desc ";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamentoDistribuidor", dataLancamentoDistribuidor);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("numeroCota", numeroCota);

		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstudoCota> obterEstudosCotaParaNotaEnvio(Long idCota, 
														  Intervalo<Date> periodo, 
														  List<Long> listaIdsFornecedores) {
		
		StringBuffer sql = new StringBuffer("SELECT DISTINCT estudoCota ");	
		
		sql.append(" FROM EstudoCota estudoCota ");

		sql.append(" JOIN estudoCota.estudo estudo ");
		sql.append(" JOIN estudo.lancamentos lancamento ");
		sql.append(" JOIN estudoCota.cota cota ");
		sql.append(" JOIN estudo.produtoEdicao produtoEdicao ");
		
		sql.append(" JOIN produtoEdicao.produto produto ");
		sql.append(" JOIN produto.fornecedores fornecedor ");
		
		sql.append(" WHERE cota.id = :idCota ");
		
		sql.append(" AND lancamento.status IN (:listaStatusLancamento) ");
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			sql.append(" AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		}	
		
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			sql.append(" AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
		}		
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("idCota", idCota);
	
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			query.setParameterList("listaFornecedores", listaIdsFornecedores);
		}
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			query.setParameter("dataInicio", periodo.getDe());
			query.setParameter("dataFim", periodo.getAte());
		}

		query.setParameterList(
			"listaStatusLancamento", 
				new StatusLancamento[] {StatusLancamento.BALANCEADO, StatusLancamento.EXPEDIDO});
		
		return query.list();
	}
	
	

	@Override
	public void removerEstudoCotaPorEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete from EstudoCota estudoCota");
		hql.append(" where estudoCota.estudo.id = :idEstudo");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEstudo", idEstudo);
		
		query.executeUpdate();
	}

	@Override
	public List<EstudoCota> obterEstudosCota(Long idEstudo) {
	    // TODO Auto-generated method stub
	    return null;
	}
}
