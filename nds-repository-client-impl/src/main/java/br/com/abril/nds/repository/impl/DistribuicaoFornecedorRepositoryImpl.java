package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;

/**
 * Implementação da Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.DistribuicaoFornecedor}  
 * @author InfoA2
 */
@Repository
public class DistribuicaoFornecedorRepositoryImpl extends AbstractRepositoryModel<DistribuicaoFornecedor, Long> implements
		DistribuicaoFornecedorRepository {

	public DistribuicaoFornecedorRepositoryImpl() {
		super(DistribuicaoFornecedor.class);
	}

	
	@Override
	public List<Integer> obterDiasSemanaDistribuicao(String codigoProduto, Long idProdutoEdicao) {
		return obterDiasSemanaDistribuicao(codigoProduto, idProdutoEdicao,
				null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> obterDiasSemanaDistribuicao(String codigoProduto, Long idProdutoEdicao, OperacaoDistribuidor operacaoDistribuidor) {
		StringBuilder sql = new StringBuilder();
		sql.append("select d.codigoDiaSemana from DistribuicaoFornecedor d, Produto p, ProdutoEdicao e ")
		   .append("  join p.fornecedores fornecedor  ")
		   .append("  where d.fornecedor.id = fornecedor.id ")
		   .append("  and   e.produto.id    = p.id ")
		   .append("  and   e.id            = :idProdutoEdicao ")
		   .append("  and   p.codigo        = :codigoProduto ");
		if(operacaoDistribuidor != null){
			sql.append("  and   d.operacaoDistribuidor = :operacaoDistribuidor ");
		}
		
		
		   sql.append("  order by d.codigoDiaSemana");
		
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		if(operacaoDistribuidor != null){
			query.setParameter("operacaoDistribuidor", operacaoDistribuidor);
		}
		
		return query.list();
	}

	@Override
	public boolean verificarDistribuicaoDiaSemana(String codigoProduto,
			Long idProdutoEdicao, DiaSemana diaSemana) {
		
		return verificarDiaSemana(codigoProduto, idProdutoEdicao, diaSemana, OperacaoDistribuidor.DISTRIBUICAO);
	}
	
	@Override
	public boolean verificarRecolhimentoDiaSemana(String codigoProduto,
			Long idProdutoEdicao, DiaSemana diaSemana) {
		
		return verificarDiaSemana(codigoProduto, idProdutoEdicao, diaSemana, OperacaoDistribuidor.RECOLHIMENTO);
	}
	
	private boolean verificarDiaSemana(String codigoProduto,
                                       Long idProdutoEdicao, 
			                           DiaSemana diaSemana,
			                           OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("select count(d.codigoDiaSemana) ")
		
		   .append("  from DistribuicaoFornecedor d, Produto p, ProdutoEdicao e  ")
		 
		   .append("  join p.fornecedores f ")
		
		   .append("  where d.fornecedor.id        = f.id ")
		
		   .append("  and   e.produto.id           = p.id ")
		
		   .append("  and   e.id                   = :idProdutoEdicao ")
		
		   .append("  and   p.codigo               = :codigoProduto ")
		
		   .append("  and   d.codigoDiaSemana      = :diaSemana ")
		
		   .append("  and   d.operacaoDistribuidor = :opeDis");
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("diaSemana", diaSemana.getCodigoDiaSemana());
		
		query.setParameter("opeDis", operacaoDistribuidor);
		
		query.setMaxResults(1);
		
		try {
			
			return ((Long)query.uniqueResult()) > 0;
		} catch (NoResultException e) {
			
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuicaoFornecedorRepository#obterTodosOrdenadoId()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DistribuicaoFornecedor> obterTodosOrdenadoId() {
		Criteria criteria =  getSession().createCriteria(DistribuicaoFornecedor.class);
		criteria.addOrder(Order.asc("fornecedor"));
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuicaoFornecedorRepository#excluirDadosFornecedor(br.com.abril.nds.model.cadastro.Fornecedor)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void excluirDadosFornecedor(Fornecedor fornecedor) {
		Criteria criteria =  getSession().createCriteria(DistribuicaoFornecedor.class);
		criteria.add(Restrictions.eq("fornecedor", fornecedor));
		List<DistribuicaoFornecedor> lista = criteria.list();
		for (DistribuicaoFornecedor registro : lista) {
			this.getSession().delete(registro);
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuicaoFornecedorRepository#gravarAtualizarDadosFornecedor(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void gravarAtualizarDadosFornecedor(List<DistribuicaoFornecedor> listaDistribuicaoFornecedor) {

		Fornecedor fornecedor = null;
		Criteria criteria = null;
		
		List<DistribuicaoFornecedor> listaExclusao = null;
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			criteria = this.getSession().createCriteria(DistribuicaoFornecedor.class);

			// Antes de inserir, exclui todos os registros do fornecedor, para sobrescrever os dias de lançamento e recolhimento
			if (fornecedor == null || !fornecedor.getId().equals(distribuicaoFornecedor.getFornecedor().getId())) {
				fornecedor = distribuicaoFornecedor.getFornecedor();
				criteria.add(Restrictions.eq("fornecedor.id", fornecedor.getId()));
				listaExclusao = criteria.list();
				for (DistribuicaoFornecedor registro : listaExclusao) {
					this.getSession().delete(registro);
				}
			}
			
			this.getSession().save(distribuicaoFornecedor);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> obterCodigosDiaDistribuicaoFornecedor(OperacaoDistribuidor operacaoDistribuidor, Long ...idFornecedor) {

		Criteria criteria = getSession().createCriteria(DistribuicaoFornecedor.class);
		
		criteria.setProjection(Projections.property("codigoDiaSemana"));
		criteria.setProjection(Projections.distinct(Projections.property("codigoDiaSemana")));
		
		if (idFornecedor != null && idFornecedor.length > 0 && idFornecedor[0] != null) {
		
			criteria.add(Restrictions.in("fornecedor.id", idFornecedor));
		}
		
		if (operacaoDistribuidor != null) {
			
			criteria.add(Restrictions.eq("operacaoDistribuidor", operacaoDistribuidor));
		}
		
		return criteria.list();
	}
	
}
