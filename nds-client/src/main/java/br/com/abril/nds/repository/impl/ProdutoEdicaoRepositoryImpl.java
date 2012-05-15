package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoEdicaoRepositoryImpl extends AbstractRepository<ProdutoEdicao, Long> 
										 implements ProdutoEdicaoRepository {

	/**
	 * Construtor padrão.
	 */
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterListaProdutoEdicao(Produto produto, ProdutoEdicao produtoEdicao ) {
		
		String codigoProduto = produto.getCodigo();
		Long numeroEdicao = produtoEdicao.getNumeroEdicao();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p from ProdutoEdicao p ");
		
		hql.append(" where ");
		
		hql.append(" p.numeroEdicao = :numeroEdicao and ");
		
		hql.append(" p.produto.codigo = :codigoProduto ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto",  codigoProduto );

		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
	}
	
	
	@Override
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new ")
		   .append(FuroProdutoDTO.class.getCanonicalName())
		   .append("(produto.codigo, produto.nome, produtoEdicao.numeroEdicao, estudo.qtdeReparte, lancamento.reparte, ")
		   .append("   lancamento.dataLancamentoDistribuidor, lancamento.id, produtoEdicao.id)")
		   .append(" from Produto produto, ProdutoEdicao produtoEdicao, ")
		   .append("      Lancamento lancamento ")
		   .append(" left join lancamento.estudo as estudo ")
		   .append(" where produtoEdicao.produto.id              = produto.id ")
		   .append(" and   produtoEdicao.id                      = lancamento.produtoEdicao.id ")
		   .append(" and   produto.codigo                        = :codigo ")
		   .append(" and   produtoEdicao.numeroEdicao            = :edicao")
		   .append(" and   lancamento.dataLancamentoDistribuidor = :dataLancamento ")
		   .append(" and   lancamento.status                     != :statusFuro");
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			hql.append(" and produto.nome = :nomeProduto ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("statusFuro", StatusLancamento.FURO);
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			query.setParameter("nomeProduto", nomeProduto);
		}
		
		query.setMaxResults(1);
		
		return (FuroProdutoDTO) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto,
																  Long numeroEdicao) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where produtoEdicao.produto.codigo = :codigoProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		
		query.setMaxResults(1);
		
		return (ProdutoEdicao) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorCodigoBarra(String codigoBarra){
		
		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoBarra));
		
		criteria.setMaxResults(1);
		
		return (ProdutoEdicao) criteria.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorSM(Long sm){
		
		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoSM", sm));
		
		criteria.setMaxResults(1);
		
		return (ProdutoEdicao) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto) {
		
		Criteria criteria = super.getSession().createCriteria(ProdutoEdicao.class);
		
		criteria.createAlias("produto", "produto");
		
		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
				
		return  criteria.list();
				
	}

}
