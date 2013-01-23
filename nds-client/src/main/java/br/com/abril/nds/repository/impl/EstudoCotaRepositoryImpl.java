package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.planejamento.EstudoCota;
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
	public List<EstudoCota> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
			
		String hql = " select estudoCota from EstudoCota estudoCota "
				   + " join estudoCota.estudo estudo "
				   + " join estudo.produtoEdicao produtoEdicao "
				   + " where estudo.dataLancamento = :dataLancamento " 
				   + " and produtoEdicao.id = :idProdutoEdicao";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
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
	public List<EstudoCota> obterEstudosCotaParaNotaEnvio(Distribuidor distribuidor, 
														  Long idCota, 
														  Intervalo<Date> periodo, 
														  List<Long> listaIdsFornecedores) {
		
		StringBuffer sql = new StringBuffer("SELECT DISTINCT movimentoEstoqueCota ");	
		
		sql.append(" FROM MovimentoEstoqueCota movimentoEstoqueCota ");

		sql.append(" JOIN movimentoEstoqueCota.lancamento lancamento ");
		sql.append(" JOIN movimentoEstoqueCota.cota cota ");
		sql.append(" JOIN movimentoEstoqueCota.produtoEdicao produtoEdicao ");
		
		sql.append(" JOIN produtoEdicao.produto produto ");
		sql.append(" JOIN produto.fornecedores fornecedor ");
		
//		sql.append(" JOIN movimentoEstoqueCota.tipoMovimento tipoMovimento ");
		sql.append(" LEFT JOIN movimentoEstoqueCota.movimentoEstoqueCotaFuro movimentoEstoqueCotaFuro ");
		
		sql.append(" WHERE movimentoEstoqueCota.status = :status ");
		sql.append(" AND cota.id = :idCota ");
		sql.append(" AND movimentoEstoqueCotaFuro.id is null ");
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			sql.append(" AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		}
		
//		if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
//			
//			sql.append(" AND tipoMovimento.grupoMovimentoEstoque IN (:listaGrupoMoviementoEstoque) ");
//		}		
		
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			sql.append(" AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
		}		
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		query.setParameter("idCota", idCota);
	
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			query.setParameterList("listaFornecedores", listaIdsFornecedores);
		}
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			query.setParameter("dataInicio", periodo.getDe());
			query.setParameter("dataFim", periodo.getAte());
		}
		
//		if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
//			
//			query.setParameterList("listaGrupoMoviementoEstoque", listaGrupoMovimentoEstoques);
//		}		
		
		return query.list();
	}
	
}
