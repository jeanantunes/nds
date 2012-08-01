package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.StringUtil;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<ProdutoEdicao, Long> 
										 implements ProdutoEdicaoRepository {

	/**
	 * Construtor padrão.
	 */
	public ProdutoEdicaoRepositoryImpl() {
		super(ProdutoEdicao.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterPercentualComissionamento(java.lang.Long, java.lang.Integer, java.lang.Long)
	 */
	public BigDecimal obterFatorDesconto(Long idProdutoEdicao, Integer numeroCota, Long idDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.numeroCota = :numeroCota and ");

		hql.append(" pe.id = :idProdutoEdicao and ");

		hql.append(" distribuidor.id = :idDistribuidor ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return (BigDecimal) query.uniqueResult();
		
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
	

	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto) {
		
		Criteria criteria = super.getSession().createCriteria(ProdutoEdicao.class);
		
		criteria.createAlias("produto", "produto");
		
		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
				
		return  criteria.list();
				
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto) {
		
		StringBuilder hql = new StringBuilder("select pe ");
		hql.append(" from ProdutoEdicao pe ")
		   .append(" where pe.produto.nome like :nomeProduto ")
		   .append(" or pe.produto.codigo = :codigoProduto ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeProduto", "%" + codigoNomeProduto + "%");
		query.setParameter("codigoProduto", codigoNomeProduto);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " where produtoEdicao.id in (:idsProdutoEdicao)";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		
		return query.list();
	}
	
	public ProdutoEdicao obterProdutoEdicaoPorSequenciaMatriz(Integer sequenciaMatriz) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento.produtoEdicao from Lancamento lancamento ");
		
		hql.append(" where lancamento.sequenciaMatriz = :sequenciaMatriz ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("sequenciaMatriz", sequenciaMatriz);
		
		return (ProdutoEdicao) query.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterCodigoMatrizPorProdutoEdicao(java.lang.Long)
	 */
	public Integer obterCodigoMatrizPorProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento.sequenciaMatriz from Lancamento lancamento ");
		
		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ");

		hql.append(" and lancamento.dataLancamentoDistribuidor = ");

		hql.append(" ( ");
		
		hql.append(" select max(lancamento.dataLancamentoDistribuidor) from Lancamento lancamento  ");
		
		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao   ");
		
		hql.append(" ) ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (Integer) query.uniqueResult();
	}

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorSM(Long sm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int initialResult, int maxResults) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe.id as id, pr.codigo as codigoProduto, pe.nomeComercial as nomeProduto, ");
		hql.append("        pe.numeroEdicao as numeroEdicao, jr.razaoSocial as nomeFornecedor, ");
		hql.append("        ln.tipoLancamento as statusLancamento, ln.status as statusSituacao, ");
		hql.append("        pe.possuiBrinde as temBrinde ");
		
		// Corpo da consulta com os filtros:
		Query query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, nomeProduto, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortname, sortorder);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));
		query.setFirstResult(initialResult);
		query.setMaxResults(maxResults);
		
		try {
			return query.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Long countPesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT count(pr.codigo) ");
		
		// Corpo da consulta com os filtros:
		Query query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, nomeProduto, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, null, null);
		
		try {
			return (Long) query.uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Corpo com a consulta HQL para pesquisar e ordenar as edições já 
	 * cadatradas.<br>
	 * Possui como opções de filtro:<br>
	 * <ul>
	 * <li>Código do Produto;</li>
	 * <li>Nome do Produto;</li>
	 * <li>Data de Lançamento;</li>
	 * <li>Situação do Lançamento;</li>
	 * <li>Código de Barra da Edição;</li>
	 * <li>Contém brinde;</li>
	 * </ul>
	 * 
	 * @param hql
	 * @param dto
	 * @param sortname
	 * @param sortorder
	 * 
	 * @return
	 */
	private Query queryBodyPesquisarEdicoes(StringBuilder hql, String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde, String sortname, String sortorder) {
		
		hql.append("   FROM ProdutoEdicao pe ");
		hql.append("        JOIN pe.produto pr ");
		hql.append("        JOIN pr.fornecedores fr JOIN fr.juridica jr ");
		hql.append("        JOIN pe.lancamentos ln ");
		hql.append("  WHERE 1=1 ");
		
		// Filtros opcionais da pesquisa:
		if (dataLancamento != null) {
			hql.append("  AND (ln.dataLancamentoDistribuidor between :dataLancamentoDe and :dataLancamentoAte OR ln.dataLancamentoPrevista between :dataLancamentoDe and :dataLancamentoAte) ");
		}
		
		if (preco != null) {
			hql.append("  AND (ln.precoVenda between :precoDe and :precoAte) ");
		}
		if (statusLancamento != null) {
			hql.append("  AND ln.status = :situacaoLancamento ");
		}		
		if (StringUtil.isEmpty(codigoProduto)) {
			hql.append("  AND UPPER(pr.codigo) LIKE UPPER(:codigoProduto) ");
		}
		if (StringUtil.isEmpty(nomeProduto)) {
			hql.append("  AND UPPER(pr.descricao) LIKE UPPER(:nomeProduto) ");
		}
		if (StringUtil.isEmpty(codigoDeBarras)) {
			hql.append("  AND pe.codigoDeBarras LIKE :codigoDeBarras ");
		}
		if (brinde) {
			hql.append("  AND pe.possuiBrinde = :possuiBrinde ");
		}
		
		// Ordenacao:
		if (sortname != null && sortorder != null) {
			hql.append(" ORDER BY " + sortname + " " + sortorder);
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		// Parâmetros opcionais da pesquisa:
		if (dataLancamento != null) {
			query.setDate("dataLancamentoDe", dataLancamento.getDe());
			query.setDate("dataLancamentoAte", dataLancamento.getAte());
		}
		if (preco != null) {
			query.setBigDecimal("precoDe", preco.getDe());
			query.setBigDecimal("precoAte", preco.getAte());
		}
		
		if (statusLancamento != null) {
			query.setParameter("situacaoLancamento", statusLancamento);
		}		
		if (StringUtil.isEmpty(codigoProduto)) {
			query.setString("codigoProduto", codigoProduto);
		}
		if (StringUtil.isEmpty(nomeProduto))  {
			query.setString("nomeProduto", nomeProduto);
		}
		if (StringUtil.isEmpty(codigoDeBarras)){
			query.setString("codigoDeBarras", codigoDeBarras);
		}
		if (brinde) {
			query.setBoolean("possuiBrinde", brinde);
		}
		
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(String codigoProduto,
			int qtdEdicoes) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe ");
		
		// Corpo da consulta com os filtros:
		Query query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, null, null, null, null, null, false, "numeroEdicao", "DESC");
		
		query.setMaxResults(qtdEdicoes);
		
		try {
			return query.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean hasProdutoEdicao(Produto produto) {
		
		String hql = 
				"SELECT count(pe.id) FROM ProdutoEdicao pe WHERE pe.produto = :produto ";
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("produto", produto);
		
		try {
			Long qtd = (Long) query.uniqueResult(); 
			return qtd == null || Long.valueOf(0).equals(qtd) ? false : true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isProdutoEdicaoJaPublicada(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT CASE WHEN (ln.dataLancamentoDistribuidor > sysdate()) THEN false ELSE true END ");
		hql.append("   FROM Lancamento ln ");
		hql.append("  WHERE ln.dataLancamentoDistribuidor = ");
		hql.append("        (SELECT MIN(lnMinDate.dataLancamentoDistribuidor) ");
		hql.append("           FROM Lancamento lnMinDate ");
		hql.append("          WHERE lnMinDate.produtoEdicao.id = :idProdutoEdicao ) ");
		hql.append("    AND ln.produtoEdicao.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setLong("idProdutoEdicao", 
				idProdutoEdicao == null ? Long.valueOf(0) : idProdutoEdicao);
		
		Boolean isPublicado = (Boolean) query.uniqueResult(); 
		return (isPublicado == null ? false : isPublicado.booleanValue());
	}
	
	@Override
	public boolean isNumeroEdicaoCadastrada(String codigoProduto, 
			Long numeroEdicao, Long idProdutoEdicao) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT COUNT(pe.id) ");
		hql.append("  FROM ProdutoEdicao pe JOIN pe.produto as pr ");
		hql.append(" WHERE pe.numeroEdicao = :numeroEdicao ");
		hql.append("   AND pe.id != :idProdutoEdicao ");
		hql.append("   AND pr.codigo = :codigoProduto ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setString("codigoProduto", codigoProduto);
		query.setLong("numeroEdicao", numeroEdicao);
		query.setLong("idProdutoEdicao", 
				idProdutoEdicao == null ? Long.valueOf(0) : idProdutoEdicao);
		
		Long qtd = (Long) query.uniqueResult();
		return qtd.intValue() > 0 ? true : false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutoEdicaoPorCodigoDeBarra(String codigoDeBarras,
			Long idProdutoEdicao) {
		
		Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoDeBarras));
		criteria.add(Restrictions.ne("id", 
				idProdutoEdicao == null ? Long.valueOf(0) : idProdutoEdicao));
		
		return criteria.list();
	}
	
	/**
	 * Obtém produtoEdicao por (produto e numeroEdicao) ou nome
	 * @param idProduto
	 * @param numeroEdicao
	 * @param nome
	 * @return ProdutoEdicao
	 */
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorProdutoEEdicaoOuNome(Long idProduto,
																   Long numeroEdicao,
																   String nome) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where ((produtoEdicao.produto.id = :idProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao)"
				   + " or 	 (produtoEdicao.nomeComercial  = :nome))";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("idProduto", idProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("nome", nome);
		
		query.setMaxResults(1);
		
		return (ProdutoEdicao) query.uniqueResult();
	}
}
 