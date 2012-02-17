package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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

}
