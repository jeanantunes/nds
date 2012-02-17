package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;

@Repository
public class ProdutoEdicaoRepositoryImpl extends AbstractRepository<ProdutoEdicao, Long> 
										 implements ProdutoEdicaoRepository {

	public ProdutoEdicaoRepositoryImpl() {
		super(ProdutoEdicao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where upper(produtoEdicao.produto.nome) like upper(:nomeProduto)";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nomeProduto", nomeProduto + "%");
		
		return query.list();
	}

	@Override
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new ")
		   .append(FuroProdutoDTO.class.getCanonicalName())
		   .append("(produto.codigo, produto.nome, produtoEdicao.numeroEdicao, lancamento.reparte) ")
		   .append(" from Produto produto, ProdutoEdicao produtoEdicao, Lancamento lancamento ")
		   .append(" where produtoEdicao.produto.id          = produto.id ")
		   .append(" and   produtoEdicao.id                  = lancamento.produtoEdicao.id ")
		   .append(" and   produto.codigo                    = :codigo ")
		   .append(" and   produtoEdicao.numeroEdicao        = :edicao")
		   .append(" and   lancamento.dataLancamentoPrevista = :dataLancamento ");
		
		if (nomeProduto != null){
			hql.append(" and produto.nome = :nomeProduto ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		
		if (nomeProduto != null){
			query.setParameter("nomeProduto", nomeProduto);
		}
		
		query.setMaxResults(1);
		
		try {
			return (FuroProdutoDTO) query.uniqueResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
