package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.caelum.stella.boleto.exception.CriacaoBoletoException;

@Repository
public class DistribuicaoFornecedorRepositoryImpl extends AbstractRepository<DistribuicaoFornecedor, Long> implements
		DistribuicaoFornecedorRepository {

	public DistribuicaoFornecedorRepositoryImpl() {
		super(DistribuicaoFornecedor.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> obterDiasSemanaDistribuicao(String codigoProduto, Long idProdutoEdicao) {
		StringBuilder sql = new StringBuilder();
		sql.append("select d.codigoDiaSemana from DistribuicaoFornecedor d, Produto p, ProdutoEdicao e ")
		   .append("  join p.fornecedores fornecedor  ")
		   .append("  where d.fornecedor.id = fornecedor.id ")
		   .append("  and   e.produto.id    = p.id ")
		   .append("  and   e.id            = :idProdutoEdicao ")
		   .append("  and   p.codigo        = :codigoProduto ")
		   .append("  order by d.codigoDiaSemana");
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
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
	
	private boolean verificarDiaSemana (String codigoProduto,Long idProdutoEdicao, 
										DiaSemana diaSemana,OperacaoDistribuidor operacaoDistribuidor) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(d.codigoDiaSemana) from DistribuicaoFornecedor d, Produto p, ProdutoEdicao e ")
		   .append("  join p.fornecedores fornecedor  ")
		   .append("  where d.fornecedor.id        = fornecedor.id ")
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
	@Override
	public void excluirDadosFornecedor(Fornecedor fornecedor) {
		Criteria criteria =  getSession().createCriteria(DistribuicaoFornecedor.class);
		criteria.add(Restrictions.eq("fornecedor", fornecedor));
		List<DistribuicaoFornecedor> lista = criteria.list();
		for (DistribuicaoFornecedor registro : lista) {
			this.getSession().delete(registro);
		}
	}

}
