package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		   .append("(produto.codigo, produto.nome, produtoEdicao.numeroEdicao, estudo.qtdeReparte, ")
		   .append("   lancamento.dataLancamentoDistribuidor, parametroSistema.valor, lancamento.id, produtoEdicao.id)")
		   .append(" from Produto produto, ProdutoEdicao produtoEdicao, Lancamento lancamento, ParametroSistema parametroSistema, ")
		   .append("      Estudo estudo ")
		   .append(" where produtoEdicao.produto.id              = produto.id ")
		   .append(" and   produtoEdicao.id                      = lancamento.produtoEdicao.id ")
		   .append(" and   estudo.dataLancamento                 = lancamento.dataLancamentoPrevista ")
		   .append(" and   estudo.produtoEdicao                  = lancamento.produtoEdicao ")
		   .append(" and   produto.codigo                        = :codigo ")
		   .append(" and   produtoEdicao.numeroEdicao            = :edicao")
		   .append(" and   lancamento.dataLancamentoDistribuidor = :dataLancamento ")
		   .append(" and   parametroSistema.tipoParametroSistema = :pathCapas ")
		   .append(" and   lancamento.status                     != :statusFuro");
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			hql.append(" and produto.nome = :nomeProduto ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("pathCapas", TipoParametroSistema.PATH_IMAGENS_CAPA);
		query.setParameter("statusFuro", StatusLancamento.FURO);
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			query.setParameter("nomeProduto", nomeProduto);
		}
		
		query.setMaxResults(1);
		
		try {
			return (FuroProdutoDTO) query.uniqueResult();
		} catch (NoResultException e) {
			return null;
		}
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
		
		//TODO: no banco nao deveria retornar mais de um registro
		query.setMaxResults(1);
		
		return (ProdutoEdicao) query.uniqueResult();
	}

}
