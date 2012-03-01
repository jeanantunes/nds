package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;

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
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(d.codigoDiaSemana) from DistribuicaoFornecedor d, Produto p, ProdutoEdicao e ")
		   .append("  join p.fornecedores fornecedor  ")
		   .append("  where d.fornecedor.id   = fornecedor.id ")
		   .append("  and   e.produto.id      = p.id ")
		   .append("  and   e.id              = :idProdutoEdicao ")
		   .append("  and   p.codigo          = :codigoProduto ")
		   .append("  and   d.codigoDiaSemana = :diaSemana ");
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("diaSemana", diaSemana.getCodigoDiaSemana());
		query.setMaxResults(1);
		
		try {
			return ((Long)query.uniqueResult()) > 0;
		} catch (NoResultException e) {
			return false;
		}
	}
}
