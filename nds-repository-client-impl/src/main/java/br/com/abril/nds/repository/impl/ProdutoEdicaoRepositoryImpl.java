package br.com.abril.nds.repository.impl;

import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<ProdutoEdicao, Long> implements ProdutoEdicaoRepository {
	    
    /**
     * Construtor padrão.
     */
	public ProdutoEdicaoRepositoryImpl() {
		super(ProdutoEdicao.class);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(final String nomeProduto) {
		final String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where upper(produtoEdicao.produto.nome) like upper(:nomeProduto)";

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("nomeProduto", nomeProduto + "%");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterListaProdutoEdicao(final Produto produto, final ProdutoEdicao produtoEdicao ) {

		final String codigoProduto = produto.getCodigo();
		final Long numeroEdicao = produtoEdicao.getNumeroEdicao();

		final StringBuilder hql = new StringBuilder();

		hql.append(" select p from ProdutoEdicao p ");

		hql.append(" where ");

		hql.append(" p.numeroEdicao = :numeroEdicao and ");

		hql.append(" p.produto.codigo = :codigoProduto ");

		final Query query = getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto",  codigoProduto );

		query.setParameter("numeroEdicao", numeroEdicao);

		return query.list();
	}


	@Override
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(final String codigo, final String nomeProduto, final Long edicao, final Date dataLancamento, final boolean furado) {
		final StringBuilder hql = new StringBuilder();
		
		// Corrigido para obter o saldo real do produto. Implementado em conjunto com Eduardo Punk Rock.
		hql.append("select new ")
		   .append(FuroProdutoDTO.class.getCanonicalName())
		   .append("(produto.codigo, produto.nome, produtoEdicao.numeroEdicao, ")
		   .append("coalesce((select ep.qtde from EstoqueProduto ep where ep.produtoEdicao.id = produtoEdicao.id), lancamento.reparte, 0) as quantidadeExemplares, ")
		   .append("   lancamento.dataLancamentoDistribuidor, lancamento.id, produtoEdicao.id)")
		   .append(" from Produto produto, Lancamento lancamento, ProdutoEdicao produtoEdicao ")
		   .append(" where produtoEdicao.produto.id              = produto.id ")
		   .append(" and   produtoEdicao.id                      = lancamento.produtoEdicao.id ")
		   .append(" and   produto.codigo                        = :codigo ")
		   .append(" and   produtoEdicao.numeroEdicao            = :edicao")
		   .append(" and   lancamento.dataLancamentoDistribuidor = :dataLancamento ")
		   .append(" and   ( lancamento.status = :balanceadoLancamento or ")
		   .append(" lancamento.status = :expedido or (lancamento.status = :recolhidoProdutoContaFirme and produto.formaComercializacao =:produtoContaFimre) ) ");   
		
		   if (furado) {
			   hql.append(" and   lancamento.status                     != :statusFuro");
		   }

		final Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("balanceadoLancamento", StatusLancamento.BALANCEADO);
		query.setParameter("expedido", StatusLancamento.EXPEDIDO);
		query.setParameter("recolhidoProdutoContaFirme", StatusLancamento.RECOLHIDO);
		query.setParameter("produtoContaFimre",FormaComercializacao.CONTA_FIRME);
		
		if (furado) {
			query.setParameter("statusFuro", StatusLancamento.FURO);
		}
		
		query.setMaxResults(1);
		
		return (FuroProdutoDTO) query.uniqueResult();
	}

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(final String codigoProduto,
																  final Long numeroEdicao) {

		final String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where produtoEdicao.produto.codigo = :codigoProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao";

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto",  leftPad(codigoProduto, 8, "0"));
		query.setParameter("numeroEdicao", numeroEdicao);

		query.setMaxResults(1);

		return (ProdutoEdicao) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> listProdutoEdicaoPorCodProdutoNumEdicoes(final String codigoProduto, final Long numeroEdicaoInicial, final Long numeroEdicaoFinal) {
		return super.getSession().createCriteria(ProdutoEdicao.class)
				.add(Restrictions.between("numeroEdicao", numeroEdicaoInicial, numeroEdicaoFinal))
				.addOrder(Order.asc("numeroEdicao"))
				.createCriteria("produto")
				.add(Restrictions.eq("codigo", codigoProduto)).list();
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutoEdicaoPorCodigoBarra(final String codigoBarra) {

		final Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoBarra));

		return criteria.list();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(final String codigoProduto) {

		final Criteria criteria = super.getSession().createCriteria(ProdutoEdicao.class);

		criteria.createAlias("produto", "produto");

		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));

		return  criteria.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutosEdicaoPorId(final Set<Long> idsProdutoEdicao) {

		final String hql = "from ProdutoEdicao produtoEdicao " 
				   + " where produtoEdicao.id in (:idsProdutoEdicao)";

		final Query query = super.getSession().createQuery(hql);

		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterCodigoMatrizPorProdutoEdicao(java.lang.Long, java.util.Date)
	 */
	@Override
	public Integer obterCodigoMatrizPorProdutoEdicao(final Long idProdutoEdicao, final Date dataRecolhimento, final Integer numeroCota) {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe.sequencia from ChamadaEncalhe chamadaEncalhe ");
		hql.append(" join chamadaEncalhe.chamadaEncalheCotas cec ");
		hql.append(" join cec.cota cota ");
		hql.append(" where chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		hql.append(" and cota.numeroCota = :numeroCota ");

		if(dataRecolhimento != null) {
			
			hql.append(" and chamadaEncalhe.dataRecolhimento = :dataRecolhimento ");
					
		} else {
			
			hql.append(" and chamadaEncalhe.dataRecolhimento = ");
			
			hql.append(" ( ");
			
			hql.append(" select max(ce.dataRecolhimento) from ChamadaEncalhe ce  ");
			
			hql.append(" where ce.produtoEdicao.id = :idProdutoEdicao   ");
			
			hql.append(" ) ");			
		}
		
		final Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("numeroCota", numeroCota);
		
		if(dataRecolhimento!=null) {
			query.setParameter("dataRecolhimento", dataRecolhimento);
		}
		
		return (Integer) query.uniqueResult();
	}

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorSM(final Long sm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(final String codigoProduto, final String nome,
			final Intervalo<Date> dataLancamento, final Intervalo<Double> preco , final StatusLancamento statusLancamento,
			final String codigoDeBarras, final boolean brinde,
			final String sortorder, final String sortname, final int initialResult, final int maxResults) {
			
		final StringBuilder hql = new StringBuilder()
		
		.append(" SELECT pe.id as id, p.codigo as codigoProduto, p.NOME_COMERCIAL as nomeComercial, ")
		.append("        pe.NUMERO_EDICAO as numeroEdicao, coalesce(pessoa.nome, pessoa.RAZAO_SOCIAL) as nomeFornecedor, ")
		.append("        l.TIPO_LANCAMENTO as statusLancamento, ") 
		.append("        l.status as statusSituacao , ") 
		.append("        pe.possui_brinde as temBrinde, ")
		.append("        pe.parcial as parcial ");
		
		// Corpo da consulta com os filtros:
		final SQLQuery query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortname, sortorder);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));
		
		query.setFirstResult(initialResult);
		query.setMaxResults(maxResults);
		
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeComercial", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		query.addScalar("statusLancamento", StandardBasicTypes.STRING);
		query.addScalar("statusSituacao", StandardBasicTypes.STRING);
		query.addScalar("temBrinde", StandardBasicTypes.BOOLEAN);
		query.addScalar("parcial", StandardBasicTypes.BOOLEAN);
		
		return query.list();
	}
	
	/**
	 * Obtem tuplas para pesquisas de edições
	 * 
	 * @param codigoProduto
	 * @param nome
	 * @param dataLancamento
	 * @param preco
	 * @param statusLancamento
	 * @param codigoDeBarras
	 * @param brinde
	 * @return StringBuilder
	 */
	private StringBuilder obterTuplasPesquisarEdicoes(final String codigoProduto, 
			                                          final String nome,
			                                          final Intervalo<Date> dataLancamento, 
			                                          final Intervalo<Double> preco , 
			                                          final StatusLancamento statusLancamento,
			                                          final String codigoDeBarras, 
			                                          final boolean brinde){
		
		StringBuilder sql = new StringBuilder();

		if (dataLancamento != null) {
			
			sql.append("  AND (l.DATA_LCTO_DISTRIBUIDOR between :dataLancamentoDe and :dataLancamentoAte) ");
		}
		
		if (preco != null) {
			
			sql.append("  AND (pe.PRECO_VENDA between :precoDe and :precoAte) ");
		}
		if (statusLancamento != null) {
			
			sql.append("  AND l.status = :situacaoLancamento ");

		}		
		if (!StringUtil.isEmpty(codigoProduto)) {
			
			sql.append("  AND UPPER(p.codigo) LIKE UPPER(:codigoProduto) ");
		}
		if (!StringUtil.isEmpty(nome)) {
			
			sql.append("  AND UPPER(p.nome) LIKE UPPER(:nome) ");
		}
		if (!StringUtil.isEmpty(codigoDeBarras)) {
			
			sql.append("  AND pe.CODIGO_DE_BARRAS LIKE :codigoDeBarras ");
		}
		if (brinde) {
			
			sql.append("  AND pe.POSSUI_BRINDE = :possuiBrinde ");
		}
		
		return sql;
	}
	
	/**
	 * Define parâmetros para consulta de edições
	 * 
	 * @param query
	 * @param codigoProduto
	 * @param nome
	 * @param dataLancamento
	 * @param preco
	 * @param statusLancamento
	 * @param codigoDeBarras
	 * @param brinde
	 * @return SQLQuery
	 */
	private SQLQuery setParametrosPerquisarEdicoes(final SQLQuery query,
			                                       final String codigoProduto, 
										           final String nome,
										           final Intervalo<Date> dataLancamento, 
										           final Intervalo<Double> preco , 
										           final StatusLancamento statusLancamento,
										           final String codigoDeBarras, 
										           final boolean brinde){
		
        query.setParameter("indAtivo", true);
		
		if (dataLancamento != null) {
			
			query.setDate("dataLancamentoDe", dataLancamento.getDe());
			
			query.setDate("dataLancamentoAte", dataLancamento.getAte());
		}
		
		if (preco != null) {
			
			query.setDouble("precoDe", preco.getDe());
			
			query.setDouble("precoAte", preco.getAte());
		}
		
        if (statusLancamento != null) {
        	
			query.setParameter("situacaoLancamento", statusLancamento.name());
			query.setParameter("situacaoLancamentoInner", statusLancamento.name());
		}	
		
		if (!StringUtil.isEmpty(codigoProduto)) {
			
			query.setString("codigoProduto", codigoProduto);
		}
		
		if (!StringUtil.isEmpty(nome))  {
			
            query.setString("nome", "%" + nome + "%");
		}
		
		if (!StringUtil.isEmpty(codigoDeBarras)) {
			
			query.setString("codigoDeBarras", codigoDeBarras);
		}
		
		if (brinde) {
			
			query.setBoolean("possuiBrinde", brinde);
		}
		
		return query;
	}

	@Override
	public Integer countPesquisarEdicoes(final String codigoProduto, 
			                             final String nomeProduto,
			                             final Intervalo<Date> dataLancamento, 
			                             final Intervalo<Double> preco , 
			                             final StatusLancamento statusLancamento,
			                             final String codigoDeBarras, 
			                             final boolean brinde) {
		
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT count(Q.produtoEdicaoId) as total FROM ");
		
		sql.append(" ( ");

		sql.append("   SELECT pe.id as produtoEdicaoId ");
		
		sql.append("   from PRODUTO_EDICAO pe ");
		
		sql.append("   inner join PRODUTO p on pe.PRODUTO_ID=p.ID "); 
		
		sql.append("   left join PRODUTO_FORNECEDOR pf on p.ID=pf.PRODUTO_ID "); 
		
		sql.append("   left join FORNECEDOR f on pf.fornecedores_ID=f.ID ");
		
		sql.append("   left join PESSOA pessoa on f.JURIDICA_ID=pessoa.ID ");
		
		sql.append("   join LANCAMENTO l on pe.ID=l.PRODUTO_EDICAO_ID "); 
		
		sql.append("   where pe.ATIVO = :indAtivo ");
		
		sql.append("   and l.id=( ");
		
		sql.append("       select ");
		
		sql.append("           min(l.id) ");
		
		sql.append("       from ");
		
		sql.append("           LANCAMENTO l "); 
		
		sql.append("       where ");
		
		sql.append("           l.PRODUTO_EDICAO_ID=pe.ID ");
		if (statusLancamento != null  )
			 sql.append("   and l.STATUS = :situacaoLancamentoInner");
		
		sql.append("   ) ");
		
        sql.append(this.obterTuplasPesquisarEdicoes(codigoProduto, nomeProduto, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde));
		
		sql.append(" GROUP BY pe.id ");
		
		sql.append(" ) AS Q ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());

        query = this.setParametrosPerquisarEdicoes(query, codigoProduto, nomeProduto, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde);
		
        query.addScalar("total", StandardBasicTypes.INTEGER);
        
        return  (Integer) query.uniqueResult();

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
	private SQLQuery queryBodyPesquisarEdicoes(final StringBuilder hql, 
			                                   final String codigoProduto, 
			                                   final String nome,
			                                   final Intervalo<Date> dataLancamento, 
			                                   final Intervalo<Double> preco , 
			                                   final StatusLancamento statusLancamento,
			                                   final String codigoDeBarras, 
			                                   final boolean brinde, 
			                                   final String sortname, 
			                                   final String sortorder) {
		
		hql.append("   from PRODUTO_EDICAO pe ");
		
		hql.append("   inner join PRODUTO p on pe.PRODUTO_ID=p.ID "); 
		
		hql.append("   left join PRODUTO_FORNECEDOR pf on p.ID=pf.PRODUTO_ID "); 
		
		hql.append("   left join FORNECEDOR f on pf.fornecedores_ID=f.ID ");
		
		hql.append("   left join PESSOA pessoa on f.JURIDICA_ID=pessoa.ID ");
		
		hql.append("   join LANCAMENTO l on pe.ID=l.PRODUTO_EDICAO_ID "); 
		
		hql.append("   where pe.ATIVO = :indAtivo ");
		
		hql.append("   and l.id=( ");
		
		hql.append("       select ");
		
		hql.append("           MAX(l.id) ");
		
		hql.append("       from ");
		
		hql.append("           LANCAMENTO l "); 
		
		hql.append("       where ");
		
		hql.append("           l.PRODUTO_EDICAO_ID=pe.ID ");
		
		if (statusLancamento != null  )
			 hql.append("   and l.STATUS = :situacaoLancamentoInner");
		
		hql.append("   ) ");
		
        hql.append(this.obterTuplasPesquisarEdicoes(codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde));
		
		hql.append(" GROUP BY pe.id ");
		
		if (sortname != null && sortorder != null) {
			
			hql.append(" ORDER BY " + sortname + " " + sortorder);
		}
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());

        query = this.setParametrosPerquisarEdicoes(query, codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde);
		
		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(final String codigoProduto,	final int qtdEdicoes) {
		
		final StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe ");
		
		// Corpo da consulta com os filtros:
		final Query query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, null, null, null, null, null, false, "pe.numeroEdicao", "DESC");
		
		query.setMaxResults(qtdEdicoes);
		
		return query.list();
	}

	@Override
	public boolean hasProdutoEdicao(final Produto produto) {

		final String hql = 
				"SELECT count(pe.id) FROM ProdutoEdicao pe WHERE pe.produto = :produto ";

		final Query query = this.getSession().createQuery(hql);
		query.setParameter("produto", produto);

		try {
			final Long qtd = (Long) query.uniqueResult(); 
			return qtd == null || Long.valueOf(0).equals(qtd) ? false : true;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isProdutoEdicaoJaPublicada(final Long idProdutoEdicao) {

		final StringBuilder hql = new StringBuilder();

		hql.append(" SELECT CASE WHEN (ln.dataLancamentoDistribuidor > sysdate()) THEN false ELSE true END ");
		hql.append("   FROM Lancamento ln ");
		hql.append("  WHERE ln.dataLancamentoDistribuidor = ");
		hql.append("        (SELECT MIN(lnMinDate.dataLancamentoDistribuidor) ");
		hql.append("           FROM Lancamento lnMinDate ");
		hql.append("          WHERE lnMinDate.produtoEdicao.id = :idProdutoEdicao ) ");
		hql.append("    AND ln.produtoEdicao.id = :idProdutoEdicao ");

		final Query query = this.getSession().createQuery(hql.toString());
		query.setLong("idProdutoEdicao", 
				idProdutoEdicao == null ? Long.valueOf(0) : idProdutoEdicao);

		final Boolean isPublicado = (Boolean) query.uniqueResult(); 
		return (isPublicado == null ? false : isPublicado.booleanValue());
	}

	@Override
    public boolean isNumeroEdicaoCadastrada(final Long idProduto,
			final Long numeroEdicao, final Long idProdutoEdicao) {
        
        final Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);
        criteria.setProjection(Projections.rowCount());
        
        criteria.add(Restrictions.eq("numeroEdicao", numeroEdicao));
        criteria.add(Restrictions.eq("produto.id", idProduto));
        if (idProdutoEdicao != null) {
            criteria.add(Restrictions.not(Restrictions.idEq(idProdutoEdicao)));
        }
        final Long qtd = (Long) criteria.uniqueResult();
		return qtd.intValue() > 0 ? true : false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutoEdicaoPorCodigoDeBarra(final String codigoDeBarras,
			final Long idProdutoEdicao) {

		final Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoDeBarras));
		criteria.add(Restrictions.ne("id", 
				idProdutoEdicao == null ? Long.valueOf(0) : idProdutoEdicao));

		return criteria.list();
	}

	                            /**
     * Obtém produtoEdicao por (produto e numeroEdicao) ou nome
     * 
     * @param idProduto
     * @param numeroEdicao
     * @param nome
     * @return ProdutoEdicao
     */
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorProdutoEEdicaoOuNome(final Long idProduto,
																   final Long numeroEdicao,
																   final String nome) {

		final String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where ((produtoEdicao.produto.id = :idProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao)"
				   + " or 	 (produtoEdicao.nomeComercial  = :nome))";

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("idProduto", idProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("nome", nome);

		query.setMaxResults(1);

		return (ProdutoEdicao) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer obterQuantidadeEdicoesPorCodigoProduto(final String codigoProduto) {
		// TODO Auto-generated method stub

		final Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);

		criteria.createAlias("produto", "produto");

		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));

		criteria.setProjection(Projections.rowCount());

		return ((Long) criteria.uniqueResult()).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutosEdicoesPorCodigoProdutoLimitado(final String codigoProduto, final Integer limite) {

		final Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);

		criteria.createAlias("produto", "produto");

		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));

		criteria.setMaxResults(limite);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoProdutoDTO> obterProdutosEdicoesPorCodigoProdutoComDesconto(
			final String codigoProduto) {

		final StringBuilder hql = new StringBuilder();

//		case when pe.desconto is null or pe.desconto = 0 then (case when p.desconto is null then 0 else p.desconto end) else pe.desconto end		
		hql .append("select new ")
			.append(TipoDescontoProdutoDTO.class.getCanonicalName())
			.append("(p.codigo, p.nome, pe.numeroEdicao, d.valor, d.dataAlteracao, u.nome) ")
			.append("\n")
			.append("from ProdutoEdicao pe join pe.produto p join pe.desconto as d join d.usuario u")
			.append("\n")
			.append("where p.codigo = :codigoProduto ")
			.append("\n");

		final Query q = getSession().createQuery(hql.toString());

		q.setParameter("codigoProduto", codigoProduto);

		return q.list();

		/*Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);
		
		criteria.createAlias("produto", "produto");
		
		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
		
		criteria.add(Restrictions.isNotNull("desconto"));
		
		criteria.add(Restrictions.not(Restrictions.eq("desconto", BigDecimal.ZERO)));
		
		return criteria.list();*/
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<ProdutoEdicao> obterProdutosEdicaoPorFornecedor(final Long idFornecedor) {

		final String queryString = " select produtoEdicao from ProdutoEdicao produtoEdicao "
						   + " join produtoEdicao.produto.fornecedores fornecedores"
				   		   + " where fornecedores.id = :idFornecedor ";

		final Query query = this.getSession().createQuery(queryString);

		query.setParameter("idFornecedor", idFornecedor);

		return new HashSet<ProdutoEdicao>(query.list());
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> buscarProdutosLancadosData(final Date data) {

		final StringBuilder hql = new StringBuilder("select distinct l.produtoEdicao ");
		hql.append(" from Lancamento l ")
		   .append(" join l.estudo.estudoCotas estudoCotas ")
		   .append(" where l.dataLancamentoDistribuidor = :data ")
		   .append(" and l.status IN (:status) ")
		   .append(" and estudoCotas.tipoEstudo != :juramentado ")
		   .append(" order by l.produtoEdicao.produto.nome ");

		final Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", data);
		query.setParameter("juramentado", TipoEstudoCota.JURAMENTADO);
		query.setParameterList("status",Arrays.asList(StatusLancamento.EXPEDIDO, StatusLancamento.BALANCEADO));
		return query.list();
	}
	

	@Override
	public String buscarNome(final Long idProdutoEdicao) {

		final Query query = this.getSession().createQuery(
				"select produtoEdicao.nomeComercial from ProdutoEdicao produtoEdicao where produtoEdicao.id = :idProdutoEdicao");

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (String) query.uniqueResult();
	}


	@Override
	public Long obterUltimoNumeroEdicao(final Long idProduto) {

		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select max(pe.numeroEdicao)	");
		
		hql.append(" from ProdutoEdicao pe			");
		
		hql.append(" inner join pe.produto p		");
		
		hql.append(" where p.id = :idProduto		");
		
		final Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProduto", idProduto);
		
		return (Long) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorDistribuidor(final Set<Fornecedor> fornecedores) {

		final StringBuilder idsFornecedores = new StringBuilder();
		for (final Fornecedor fornecedor : fornecedores) {
			if (!idsFornecedores.toString().isEmpty()) {
				idsFornecedores.append(", ");
			}
			idsFornecedores.append(fornecedor.getId());
		}

		final String queryString = "SELECT "
			+ 	"produtoEdicao "
			+ "FROM "
			+ 	"ProdutoEdicao produtoEdicao "
			+ "WHERE "
			+ 	"produtoEdicao.id NOT IN (SELECT "
			+         "produtoEdicao.id "
			+         "FROM "
			+             "DescontoProdutoEdicao descontoProdutoEdicao "
			+         "JOIN descontoProdutoEdicao.produtoEdicao produtoEdicao "
			+         "JOIN produtoEdicao.produto produto "
			+         "JOIN descontoProdutoEdicao.fornecedor fornecedor "
			+         "WHERE "
			+             "fornecedor.id IN (" + idsFornecedores + ") "
			+                 "AND produto.id IN (SELECT produto.id FROM Produto produto JOIN produto.fornecedores fornecedor WHERE fornecedor.id IN (" + idsFornecedores + ") ) "
			+                 "AND descontoProdutoEdicao.tipoDesconto = ('PRODUTO')) "
			+ 	"AND produtoEdicao.id NOT IN (SELECT "
			+             "produtoEdicao.id "
			+         "FROM "
			+             "DescontoProdutoEdicao descontoProdutoEdicao "
			+         "JOIN descontoProdutoEdicao.produtoEdicao produtoEdicao "
			+         "JOIN descontoProdutoEdicao.cota cota "
			+         "JOIN descontoProdutoEdicao.fornecedor fornecedor "
			+         "WHERE "
			+             "fornecedor.id IN (" + idsFornecedores + ") "
			+                 "AND cota.id IN (SELECT cota.id FROM Cota cota JOIN cota.fornecedores fornecedor WHERE fornecedor.id IN (" + idsFornecedores + ")) "
			+                 "AND descontoProdutoEdicao.tipoDesconto = ('ESPECIFICO'))";

		//queryString += whereString;

		final Query query = this.getSession().createQuery(queryString);

		return new HashSet<ProdutoEdicao>(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorCota(final Cota cota, final Set<Fornecedor> fornecedores) {

		final StringBuilder idsFornecedores = new StringBuilder();
		for (final Fornecedor fornecedor : fornecedores) {
			if (!idsFornecedores.toString().isEmpty()) {
				idsFornecedores.append(", ");
			}
			idsFornecedores.append(fornecedor.getId());
		}		

		final String queryString = "SELECT "
			+ 	"produtoEdicao "
			+ "FROM "
			+ 	"ProdutoEdicao produtoEdicao "
			+ "WHERE "
			+ 	"produtoEdicao.id NOT IN (SELECT "
			+         "produtoEdicao.id "
			+         "FROM "
			+             "DescontoProdutoEdicao descontoProdutoEdicao "
			+         "JOIN descontoProdutoEdicao.produtoEdicao produtoEdicao "
			+         "JOIN produtoEdicao.produto produto "
			+         "JOIN descontoProdutoEdicao.cota cota "
			+         "JOIN descontoProdutoEdicao.fornecedor fornecedor "
			+         "WHERE "
			+             "cota.id = " + cota.getId() + " "
			+             "AND fornecedor.id IN (" + idsFornecedores + ") "
			+                 "AND produto.id IN (SELECT produto.id FROM Produto produto JOIN produto.fornecedores fornecedor JOIN fornecedor.cotas cota WHERE cota.id = " + cota.getId() + " AND fornecedor.id IN (" + idsFornecedores + ")) "
			+                 "AND descontoProdutoEdicao.tipoDesconto = ('PRODUTO')) "
			+ 	"AND produtoEdicao.id NOT IN (SELECT "
			+             "produtoEdicao.id "
			+         "FROM "
			+             "DescontoProdutoEdicao descontoProdutoEdicao "
			+         "JOIN descontoProdutoEdicao.produtoEdicao produtoEdicao "
			+         "JOIN descontoProdutoEdicao.cota cota "
			+         "JOIN descontoProdutoEdicao.fornecedor fornecedor "
			+         "WHERE "
			+             "cota.id = " + cota.getId() + " "
			+             "AND fornecedor.id IN (" + idsFornecedores + ") "
			+                 "AND fornecedor.id IN (SELECT fornecedor.id FROM Fornecedor fornecedor JOIN fornecedor.cotas cota WHERE cota.id = " + cota.getId() + " AND fornecedor.id IN (" + idsFornecedores + ")) "
			+                 "AND descontoProdutoEdicao.tipoDesconto = ('GERAL'))";


		final Query query = this.getSession().createQuery(queryString);

		return new HashSet<ProdutoEdicao>(query.list());
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<EdicoesProdutosDTO> obterHistoricoEdicoes(final FiltroHistogramaVendas filtro) {

		String queryStringProdutoEdicao = 
			"SELECT                                                                            "+
			"    pe.NUMERO_EDICAO AS edicao,                                                   "+
			"    pe.id AS idProdutoEdicao,                                                   "+
			"    l.DATA_REC_DISTRIB AS dtRecolhimento,                                         "+
			"    l.DATA_LCTO_DISTRIBUIDOR AS dtLancamento,                                     "+
			"    p.NOME AS nomeProduto,                                                        "+
			"    p.CODIGO AS codigoProduto,                                                    "+
			"    tcp.DESCRICAO AS descricaoTipoClassificacao,                                  "+
			"    tsp.DESCRICAO AS descricaoTipoSegmento,                                       "+
			"    l.STATUS AS status,                                                           "+
            "                                                                                  "+
			"    sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0)      "+
			"			  else if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) end) AS reparte,          "+
			"    case                                                                          "+
			"        when l.STATUS IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO')              "+
			"          then sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA'              "+
			"							then if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) else if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) end ) - (   "+
			"                    select sum(mecEncalhe.qtde)                                   "+
			"                    from                                                          "+
			"                        lancamento lanc                                           "+
			"                    LEFT JOIN                                                     "+
			"                        chamada_encalhe_lancamento cel                            "+
			"                            on cel.LANCAMENTO_ID = lanc.ID                        "+
			"                    LEFT JOIN                                                     "+
			"                        chamada_encalhe ce                                        "+
			"                            on ce.id = cel.CHAMADA_ENCALHE_ID                     "+
			"                    LEFT JOIN                                                     "+
			"                        chamada_encalhe_cota cec                                  "+
			"                            on cec.CHAMADA_ENCALHE_ID = ce.ID                     "+
			"                    LEFT JOIN                                                     "+
			"                        conferencia_encalhe confEnc                               "+
			"                            on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID           "+
			"                    LEFT JOIN                                                     "+
			"                        movimento_estoque_cota mecEncalhe                         "+
			"                            on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  "+    
			"                    WHERE                                                         "+
			"                        lanc.id = l.id)                      "+
			"        else null                                                                 "+
			"    end as venda                                                                  "+
            "                                                                                  "+
			"FROM lancamento l                                                                 "+
			"    JOIN                                                                          "+
			"        produto_edicao pe                                                         "+
			"          ON pe.id = l.produto_edicao_id                                          "+
			"    LEFT JOIN                                                                     "+
			"        periodo_lancamento_parcial plp                                            "+
			"          ON plp.id = l.periodo_lancamento_parcial_id                             "+
			"    JOIN                                                                          "+
			"        produto p                                                                 "+
			"          ON p.id = pe.produto_id                                                 "+
			"    LEFT JOIN                                                                     "+
			"        tipo_classificacao_produto tcp                                            "+
			"          ON tcp.id = pe.tipo_classificacao_produto_id                            "+
			"    LEFT JOIN                                                                     "+
			"        movimento_estoque_cota mecReparte                                         "+
			"          on mecReparte.LANCAMENTO_ID = l.id                                      "+
			"    LEFT JOIN                                                                     "+
			"        tipo_movimento tipo                                                       "+
			"          ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID                               "+
			"    LEFT JOIN                                                                     "+
			"        tipo_segmento_produto tsp                                                 "+
			"          ON p.TIPO_SEGMENTO_PRODUTO_ID = tsp.ID                                  "+
			"    LEFT Join cota                                                                "+
			"          ON mecReparte.COTA_ID = cota.ID                                         "+
			"    Left join box                                                                 "+
			"          ON cota.BOX_ID = box.ID                                                 "+
			" ";

		final List<String> whereList = new ArrayList<String>();
		final HashMap<String,Object> parameterMap = new HashMap<String,Object>();

		//Filtro por
		if (StringUtils.isNotEmpty(filtro.getFiltroPor())) {
			whereList.add(" box.tipo_box = :tipoBox ");
			parameterMap.put("tipoBox",TipoBox.values()[Integer.parseInt(filtro.getFiltroPor())]);
		}

		//codigo = codigo do produto
		if (StringUtils.isNotEmpty(filtro.getCodigo())) {

			whereList.add(" p.CODIGO_ICD = :codICD");
			
			parameterMap.put("codICD",filtro.getCodigo());

		}

		//edicao = numero da edicao
		if (StringUtils.isNotEmpty(filtro.getEdicao())) {
			whereList.add(" pe.numero_edicao = :numeroEdicao");
			parameterMap.put("numeroEdicao",Long.valueOf(filtro.getEdicao()));
		}

        // classificação = classificação do produto
		if (filtro.getIdTipoClassificacaoProduto() != null) {
			whereList.add(" tcp.id = :tipoClassificacaoProdutoId");
			parameterMap.put("tipoClassificacaoProdutoId",Long.valueOf(filtro.getIdTipoClassificacaoProduto()));
		}		

		// check opcao de componente e elemento
		if (StringUtils.isNotEmpty(filtro.getInserirComponentes())
				&& "checked".equalsIgnoreCase(filtro.getInserirComponentes())
				&& !"-1".equalsIgnoreCase(filtro.getComponente())) {

			queryStringProdutoEdicao += " 	 left outer join PDV pdvs on cota.ID = pdvs.COTA_ID "; //" join cota.pdvs pdvs ";

			switch (ComponentesPDV.values()[Integer.parseInt(filtro
					.getComponente())]) {
			case TIPO_PONTO_DE_VENDA:

				whereList.add(" pdvs.TIPO_PONTO_PDV_ID = :codigoTipoPontoPDV");
				parameterMap.put("codigoTipoPontoPDV",
						Long.parseLong(filtro.getElemento()));

				break;
			case AREA_DE_INFLUENCIA:

				whereList
						.add(" pdvs.AREA_INFLUENCIA_PDV_ID = :codigoAreaInfluenciaPDV");
				parameterMap.put("codigoAreaInfluenciaPDV",
						Long.parseLong(filtro.getElemento()));
				break;

			case BAIRRO:

				queryStringProdutoEdicao += 
						  " left outer join ENDERECO_PDV enderecoPDV on enderecoPDV.pdv_id=pdvs.id"
					      +" left outer join ENDERECO endereco on endereco.id=enderecoPDV.endereco_id";

				whereList
						.add(" enderecoPDV.principal = true and endereco.bairro = :bairroPDV ");
				parameterMap.put("bairroPDV", filtro.getElemento());

				break;
			case DISTRITO:
				queryStringProdutoEdicao += 
									" left outer join ENDERECO_PDV enderecoPDV on enderecoPDV.pdv_id=pdvs.id"
							      +" left outer join ENDERECO endereco on endereco.id=enderecoPDV.endereco_id";

				whereList.add(" enderecoPDV.principal = true and endereco.uf = :ufSigla");
				parameterMap.put("ufSigla", filtro.getElemento());

				break;
			case GERADOR_DE_FLUXO:

				queryStringProdutoEdicao += " left outer join GERADOR_FLUXO_PDV geradorFluxoPDV on pdvs.ID = geradorFluxoPDV.PDV_ID ";

				whereList.add(" geradorFluxoPDV.tipo_gerador_fluxo_id = :idGeradorFluxoPDV");
				parameterMap.put("idGeradorFluxoPDV",
						Long.parseLong(filtro.getElemento()));

				break;
			case COTAS_A_VISTA:

				whereList.add(" cota.tipo_cota = :tipoCota");
				parameterMap.put("tipoCota",TipoCota.A_VISTA);

				break;
			case COTAS_NOVAS_RETIVADAS:

				break;
			case REGIAO:

				whereList.add(" cota.id in (SELECT registro.cota_id FROM registro_cota_regiao as registro WHERE regiao_id = :regiaoId )");
				parameterMap.put("regiaoId",Long.parseLong(filtro.getElemento()));
				break;
			default:
				break;
			}

		}

		whereList.add("l.status in ('EXPEDIDO','EM_BALANCEAMENTO_RECOLHIMENTO','BALANCEADO_RECOLHIMENTO','EM_RECOLHIMENTO','RECOLHIDO','FECHADO')");
		whereList.add(" tipo.GRUPO_MOVIMENTO_ESTOQUE not in ('ENVIO_ENCALHE') ");
		
		queryStringProdutoEdicao += " where "+StringUtils.join(whereList," and ");

		//Group by
		queryStringProdutoEdicao += " GROUP BY pe.numero_edicao " +
									this.ordenarConsultaHistogramaVenda(filtro);

		final SQLQuery query = this.getSession().createSQLQuery(queryStringProdutoEdicao);

		setParameters(query, parameterMap);
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));
		configurarPaginacao(filtro,query);
		
		query.addScalar("edicao", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("dtRecolhimento", StandardBasicTypes.DATE);
		query.addScalar("dtLancamento", StandardBasicTypes.DATE);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("descricaoTipoClassificacao", StandardBasicTypes.STRING);
		query.addScalar("descricaoTipoSegmento", StandardBasicTypes.STRING);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("venda", StandardBasicTypes.BIG_DECIMAL);
		
		final List<EdicoesProdutosDTO> resultado = query.list();

		return resultado;
	}

	private String ordenarConsultaHistogramaVenda(final FiltroHistogramaVendas filtro) {

		final StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

		    switch (filtro.getOrdemColuna()) {


		    case CODIGO:
		    	hql.append(" ORDER BY codigoProduto ");
		    	break;

		    case CLASSIFICACAO:
		    	hql.append(" ORDER BY tipoClassificacaoProduto ");
		    	break;

		    case EDICAO:
				hql.append(" ORDER BY edicao ");
				break;

		    case PERIODO:
		    	hql.append(" ORDER BY periodo ");
		    	break;

		    case REPARTE:
		    	hql.append(" ORDER BY reparte ");
		    	break;

		    case VENDA:
		    	hql.append(" ORDER BY venda ");
		    	break;

		    case DT_LANCAMENTO:
		    	hql.append("ORDER BY dtLancamento ");
		    	break;

		    case DT_RECOLHIMENTO:
		    	hql.append("ORDER BY dtRecolhimento ");
		    	break;

		    case STATUS:
		    	hql.append(" ORDER BY status ");
		    	break;


		    default:
			hql.append(" ORDER BY base.numeroEdicao DESC ");
		    }

		    if (filtro.getPaginacao().getOrdenacao() != null) {
		    	hql.append(filtro.getPaginacao().getOrdenacao().toString());
		    }

		}

		return hql.toString();
	}


	@Override
	public AnaliseHistogramaDTO obterBaseEstudoHistogramaPorFaixaVenda(final FiltroHistogramaVendas filtro,final String codigoProduto,final Integer de, final Integer ate,final String[] edicoes) {

		final String queryQtdEdicoesPresentes = " (select count(distinct _pe.ID) " +
				" from ESTOQUE_PRODUTO_COTA _epc " +
				" join PRODUTO_EDICAO _pe on (_pe.ID = _epc.PRODUTO_EDICAO_ID) " +
				" join PRODUTO _p on (_p.ID = _pe.PRODUTO_ID) " +
				" join COTA _c on (_c.ID = _epc.COTA_ID) " +
				" where _p.CODIGO = :produtoCodigo " +
				" and _pe.NUMERO_EDICAO in (:nrEdicoes)) ";
		
		final String queryQtdEdicoesPresentesPorCota = " (select count(distinct _pe.ID) " +
				" from ESTOQUE_PRODUTO_COTA _epc " +
				" join PRODUTO_EDICAO _pe on (_pe.ID = _epc.PRODUTO_EDICAO_ID) " +
				" join PRODUTO _p on (_p.ID = _pe.PRODUTO_ID) " +
				" join COTA _c on (_c.ID = _epc.COTA_ID) " +
				" where _p.CODIGO = :produtoCodigo " +
				" and _pe.NUMERO_EDICAO in (:nrEdicoes)" +
				" and _c.NUMERO_COTA = cota2_.NUMERO_COTA) ";
		
		//Criada para EMS 2029
		String queryStringProdutoEdicao = 
				"select concat('De ', :de, ' a ', :ate) as faixaVenda," +
				" (avg(HIST.qtde_Devolvida) / "+ queryQtdEdicoesPresentes +") as encalheMedio , " + 
				" count(distinct COTA_ID) as qtdeCotas, " +
				" group_concat(distinct COTA_ID) as idCotaStr, " +
				" sum(cotaAtiva) as qtdeCotasAtivas, " +
				" sum(HIST.qtdeCotasSemVenda) as qtdeCotasSemVenda, " +
				" :de as faixaDe, " +
				" :ate as faixaAte, " +
				" sum(hist.mediaEdPresente) as mediaEdPresente, " +
				" group_concat(distinct hist.cotaEsmagada) as idCotasEsmagadas, " +
				" count(distinct hist.cotaEsmagada) as cotasEsmagadas, " +
				" sum(hist.vendaEsmagada) as vendaEsmagadas " +

				//select para totalizar a qtde de cotas ativas para calculo no resumo da tela da EMS 2029
				" from " +
				" ( select  " +
				
				" 	case when sum(estoqueProdutoCota.QTDE_DEVOLVIDA) = sum(estoqueProdutoCota.QTDE_RECEBIDA) then 1 else 0 end as qtdeCotasSemVenda," +
				"   case when cota2_.SITUACAO_CADASTRO='ATIVO' then 1 else 0 end as cotaAtiva," +
				"	  sum(estoqueProdutoCota.QTDE_RECEBIDA) as reparteTotal," +	
				" 	estoqueProdutoCota.ID as col_2_0_, " +
				" 	cota2_.numero_cota as COTA_ID, " +
				" 	sum(estoqueProdutoCota.QTDE_DEVOLVIDA) as QTDE_DEVOLVIDA, " +
				" 	sum(estoqueProdutoCota.QTDE_RECEBIDA) as QTDE_RECEBIDA, " +
				
				" 	(sum(estoqueProdutoCota.QTDE_RECEBIDA) - sum(estoqueProdutoCota.QTDE_DEVOLVIDA)) / " + queryQtdEdicoesPresentesPorCota +" as mediaEdPresente, " +
				
				" 	case when (round((sum(estoqueProdutoCota.QTDE_RECEBIDA) - sum(estoqueProdutoCota.QTDE_DEVOLVIDA)) / "+ queryQtdEdicoesPresentesPorCota +") = " +
				" 	round(sum(estoqueProdutoCota.QTDE_RECEBIDA) / "+ queryQtdEdicoesPresentesPorCota +")) then cota2_.NUMERO_COTA else null end as cotaEsmagada, " +
				
				" 	case when (round((sum(estoqueProdutoCota.QTDE_RECEBIDA) - sum(estoqueProdutoCota.QTDE_DEVOLVIDA)) / "+ queryQtdEdicoesPresentesPorCota +") = " +
				" 	round(sum(estoqueProdutoCota.QTDE_RECEBIDA) / "+ queryQtdEdicoesPresentesPorCota +")) then round(sum(estoqueProdutoCota.QTDE_RECEBIDA) /"+ queryQtdEdicoesPresentesPorCota +") else 0 end as vendaEsmagada " +
				
				" from ESTOQUE_PRODUTO_COTA estoqueProdutoCota " +
				" 	join PRODUTO_EDICAO produtoEdicao on estoqueProdutoCota.PRODUTO_EDICAO_ID=produtoEdicao.ID " +
				" 	join PRODUTO produto4_ on produtoEdicao.PRODUTO_ID=produto4_.ID " +
				" 	join COTA cota2_ on estoqueProdutoCota.COTA_ID=cota2_.ID ";


		// Adicionando filtro

		final List<String> whereList = new ArrayList<String>();
		final HashMap<String,Object> parameterMap = new HashMap<String,Object>();

		// Filtro por
		if (StringUtils.isNotEmpty(filtro.getFiltroPor())) {
			queryStringProdutoEdicao += 
					  " left outer join BOX box on cota2_.box_id=box.id";

			whereList.add(" box.tipo_box = :tipoBox ");
			parameterMap.put("tipoBox",TipoBox.values()[Integer.parseInt(filtro.getFiltroPor())].toString());
		}

		// check opcao de componente e elemento
		if (StringUtils.isNotEmpty(filtro.getInserirComponentes())
				&& filtro.getInserirComponentes().equalsIgnoreCase("checked")
				&& !filtro.getComponente().equalsIgnoreCase("-1")) {

			switch (ComponentesPDV.values()[Integer.parseInt(filtro
					.getComponente())]) {
			case TIPO_PONTO_DE_VENDA:
				
				whereList.add(" estoqueProdutoCota.cota_id in (select distinct cota.id from cota inner " +
								"join pdv on pdv.cota_id = cota.id where tipo_ponto_pdv_id = :codigoTipoPontoPDV) ");
				parameterMap.put("codigoTipoPontoPDV",
						Long.parseLong(filtro.getElemento()));

				break;
			case AREA_DE_INFLUENCIA:

				whereList.add(" estoqueProdutoCota.cota_id in (select distinct cota.id from cota inner " +
								" join pdv on pdv.cota_id = cota.id where AREA_INFLUENCIA_PDV_ID = :codigoAreaInfluenciaPDV) ");
				parameterMap.put("codigoAreaInfluenciaPDV",
						Long.parseLong(filtro.getElemento()));
				break;

			case BAIRRO:
				whereList.add(
						  " estoqueProdutoCota.cota_id in (select distinct cota.id from cota  " +
						  " inner join pdv on pdv.cota_id = cota.id " +
						  " left outer join ENDERECO_PDV enderecoPDV on enderecoPDV.pdv_id=pdv.id " +
					      " left outer join ENDERECO endereco on endereco.id=enderecoPDV.endereco_id " +
					      " WHERE enderecoPDV.principal = true and endereco.bairro = :bairroPDV ) ");

				parameterMap.put("bairroPDV", filtro.getElemento());

				break;
			case DISTRITO:

				whereList.add(
						  " estoqueProdutoCota.cota_id in (select distinct cota.id from cota  " +
						  " inner join pdv on pdv.cota_id = cota.id " +
						  " left outer join ENDERECO_PDV enderecoPDV on enderecoPDV.pdv_id=pdv.id " +
					      " left outer join ENDERECO endereco on endereco.id=enderecoPDV.endereco_id " +
					      " WHERE enderecoPDV.principal = true and endereco.uf = :ufSigla ) ");

				parameterMap.put("ufSigla", filtro.getElemento());

				break;
			case GERADOR_DE_FLUXO:

				whereList.add(
						  " estoqueProdutoCota.cota_id in (select distinct cota.id from cota  " +
						  " inner join pdv on pdv.cota_id = cota.id " +
						  " left outer join GERADOR_FLUXO_PDV geradorFluxoPDV on pdv.ID = geradorFluxoPDV.pdv_id " +
						  " where geradorFluxoPDV.tipo_gerador_fluxo_id = :idGeradorFluxoPDV) ");

				parameterMap.put("idGeradorFluxoPDV",
						Long.parseLong(filtro.getElemento()));

				break;
			case COTAS_A_VISTA:

				whereList.add(" cota2_.tipo_cota = :tipoCota");
				parameterMap.put("tipoCota",TipoCota.A_VISTA);

				break;
			case COTAS_NOVAS_RETIVADAS:

				break;
			case REGIAO:

				whereList.add(" estoqueProdutoCota.COTA_ID in (SELECT registro.cota_id FROM registro_cota_regiao as registro WHERE regiao_id = :regiaoId )");
				parameterMap.put("regiaoId",Long.parseLong(filtro.getElemento()));
				break;
			default:
				break;
			}

		}

		queryStringProdutoEdicao += " where " +
									"	produto4_.CODIGO= :produtoCodigo " +
									"	and ( produtoEdicao.NUMERO_EDICAO in ( :nrEdicoes ))";

		if(!whereList.isEmpty()){
			queryStringProdutoEdicao += " and "+StringUtils.join(whereList, " and ");

		}

		queryStringProdutoEdicao += " group by numero_cota "
								   +" having round(mediaEdPresente) between :de and :ate ";

		queryStringProdutoEdicao+=") as HIST";

		final SQLQuery query = this.getSession().createSQLQuery(queryStringProdutoEdicao);
		query.setParameter("de", de);
		query.setParameter("ate", ate);
		query.setParameter("produtoCodigo", codigoProduto);
		query.setParameterList("nrEdicoes", edicoes);

		setParameters(query, parameterMap);

		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseHistogramaDTO.class));

		final AnaliseHistogramaDTO resultado = (AnaliseHistogramaDTO) query.uniqueResult();


		return resultado;
	}

	private void configurarPaginacao(final FiltroDTO dto, final Query query) {

        final PaginacaoVO paginacao = dto.getPaginacao();

        if (paginacao.getQtdResultadosTotal().equals(0)) {
         paginacao.setQtdResultadosTotal(query.list().size());
        }

        if(paginacao.getQtdResultadosPorPagina() != null) {
         query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }

        if (paginacao.getPosicaoInicial() != null) {
         query.setFirstResult(paginacao.getPosicaoInicial());
        }
}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicaoDTO> obterEdicoesProduto(final FiltroHistoricoVendaDTO filtro) {

		final Map<String, Object> parameters = new HashMap<String, Object>();

		final StringBuilder sql = new StringBuilder();

		sql.append(" SELECT   ");
		sql.append("         T.codigoProduto AS codigoProduto,  ");
		sql.append("         T.id as id,  ");
		sql.append("         T.nomeProduto AS nomeProduto,  ");
		sql.append("         T.numeroEdicao AS numeroEdicao,  ");
		
		if(filtro.getNumeroEdicao() != null){
			sql.append("     T.periodo as periodo, ");
			sql.append("     T.dataLancamento AS dataLancamento,  ");
		}else{
			sql.append("     MAX(T.periodo) as periodo, ");
			sql.append("     MAX(T.dataLancamento) AS dataLancamento,  ");
		}
		
		sql.append("         T.descricaoSituacaoLancamento AS descricaoSituacaoLancamento,  ");
		sql.append("         T.chamadaCapa AS chamadaCapa,  ");
		sql.append("         T.descricaoClassificacao as descricaoClassificacao,  ");
		sql.append("         sum(T.repartePrevisto) as repartePrevisto,  ");
		sql.append("         sum(T.qtdeVendas) as qtdeVendas  ");
		
		sql.append("   FROM  ");
		sql.append("     (SELECT  ");
		sql.append("         produto.CODIGO AS codigoProduto,  ");
		sql.append("         pe.id as id,  ");
		sql.append("         produto.NOME AS nomeProduto,  ");
		sql.append("         pe.NUMERO_EDICAO AS numeroEdicao,  ");
		sql.append("     	 plp.NUMERO_PERIODO as periodo, ");
		sql.append("         lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento,  ");
		sql.append("         lancamento.STATUS AS descricaoSituacaoLancamento,  ");
		sql.append("         pe.CHAMADA_CAPA AS chamadaCapa,  ");
		sql.append("         tipoClassificacaoProduto.descricao as descricaoClassificacao,  ");
		sql.append("           ");
		sql.append("         round(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then   ");
		sql.append("                         mecReparte.QTDE   ");
		sql.append("                   else -mecReparte.QTDE   ");
		sql.append("                   end),0) AS repartePrevisto,  ");
		sql.append("           ");
		sql.append("         coalesce(round(case  ");
		sql.append("             when lancamento.STATUS IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') then   ");
		sql.append("                 sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then   ");
		sql.append("                       mecReparte.QTDE   ");
		sql.append("                 else -mecReparte.QTDE   ");
		sql.append("             end)   ");
		sql.append("             - (select sum(mecEncalhe.qtde)  ");
		sql.append("                   from  ");
		sql.append("                       lancamento lanc  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                   chamada_encalhe_lancamento cel  ");
		sql.append("                           on cel.LANCAMENTO_ID = lanc.ID  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       chamada_encalhe ce  ");
		sql.append("                           on ce.id = cel.CHAMADA_ENCALHE_ID  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       chamada_encalhe_cota cec  ");
		sql.append("                           on cec.CHAMADA_ENCALHE_ID = ce.ID  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       conferencia_encalhe confEnc  ");
		sql.append("                           on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       movimento_estoque_cota mecEncalhe  ");
		sql.append("                           on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
		sql.append("                   WHERE  ");
		sql.append("                       lanc.id = lancamento.id and ce.DATA_RECOLHIMENTO = lanc.DATA_REC_DISTRIB)  ");
		sql.append("                   else null end,0),0) as qtdeVendas  ");
		sql.append("     FROM  ");
		sql.append("         lancamento lancamento  ");
		sql.append("     JOIN  ");
		sql.append("         produto_edicao pe  ");
		sql.append("             ON pe.id = lancamento.produto_edicao_id  ");
		sql.append("     LEFT JOIN  ");
		sql.append("         periodo_lancamento_parcial plp  ");
		sql.append("             ON plp.id = lancamento.periodo_lancamento_parcial_id  ");
		sql.append("     JOIN  ");
		sql.append("         produto produto  ");
		sql.append("             ON produto.id = pe.produto_id  ");
		sql.append("     LEFT JOIN  ");
		sql.append("         tipo_classificacao_produto tipoClassificacaoProduto  ");
		sql.append("             ON tipoClassificacaoProduto.id = pe.tipo_classificacao_produto_id  ");
		sql.append("     JOIN  ");
		sql.append("         movimento_estoque_cota mecReparte  ");
		sql.append("             on mecReparte.LANCAMENTO_ID = lancamento.id  ");
		sql.append("     LEFT JOIN  ");
		sql.append("         tipo_movimento tipo  ");
		sql.append("             ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID  ");
			
		sql.append(" WHERE ");

		if(filtro.getProdutoDto().getCodigoProduto().length() > 6){
			sql.append(" produto.codigo = :codigoProduto ");
		}else{
			sql.append(" produto.codigo_icd = :codigoProduto ");
		}
		
		parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
		
		sql.append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE not in ('ENVIO_ENCALHE') ");
		sql.append(" and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");

		if (filtro.getListProdutoEdicaoDTO() != null && !filtro.getListProdutoEdicaoDTO().isEmpty()) {
			sql.append(" and pe.numero_Edicao in (");

			for (int i = 0; i < filtro.getListProdutoEdicaoDTO().size(); i++) {
				sql.append(filtro.getListProdutoEdicaoDTO().get(i).getNumeroEdicao());	

				if (filtro.getListProdutoEdicaoDTO().size() != i + 1) {
					sql.append(","); 
				}
			}

			sql.append(")");
		}

		if (filtro.getTipoClassificacaoProdutoId() != null && filtro.getTipoClassificacaoProdutoId() > 0l) {
			sql.append(" and tipoClassificacaoProduto.id = :tipoClassificacaoProdutoId ");
			parameters.put("tipoClassificacaoProdutoId", filtro.getTipoClassificacaoProdutoId());
		}
		if (filtro.getNumeroEdicao() != null && filtro.getNumeroEdicao() > 0l) {
			sql.append(" and pe.numero_Edicao = :numeroEdicao ");
			parameters.put("numeroEdicao", filtro.getNumeroEdicao());
		} 

		sql.append(" GROUP BY pe.numero_Edicao, produto.codigo, plp.NUMERO_PERIODO ");

		if(filtro.getNumeroEdicao() != null){
			sql.append(" ) T group by T.codigoProduto, T.numeroEdicao, T.periodo ");
		}else{
			sql.append(" ) T group by T.codigoProduto, T.numeroEdicao ");
		}
		
		if(filtro.getOrdemColuna() != null){
			sql.append(this.ordenarConsultaHistoricoVendaProdutoEdicao(filtro));
		}else{
			sql.append(" ORDER BY T.dataLancamento DESC ");			
		}
		
		final SQLQuery query = super.getSession().createSQLQuery(sql.toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));

		this.setParameters(query, parameters);

		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("periodo", StandardBasicTypes.INTEGER);
		query.addScalar("dataLancamento", StandardBasicTypes.DATE);
		query.addScalar("repartePrevisto", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeVendas", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("descricaoSituacaoLancamento", StandardBasicTypes.STRING);
		query.addScalar("chamadaCapa", StandardBasicTypes.STRING);
		query.addScalar("descricaoClassificacao", StandardBasicTypes.STRING);
		
		return query.list();
	}

	private String ordenarConsultaHistoricoVendaProdutoEdicao(final FiltroHistoricoVendaDTO filtro) {

		final StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

		    switch (filtro.getOrdemColuna()) {


		    case CODIGO:
		    	hql.append(" ORDER BY codigoProduto ");
		    	break;

		    case CLASSIFICACAO:
		    	hql.append(" ORDER BY descricaoClassificacao ");
		    	break;

		    case EDICAO:
				hql.append(" ORDER BY numeroEdicao ");
				break;

		    case PERIODO:
		    	hql.append(" ORDER BY periodo ");
		    	break;

		    case DT_LANCAMENTO:
		    	hql.append("ORDER BY dataLancamento ");
		    	break;

		    case REPARTE:
		    	hql.append(" ORDER BY repartePrevisto ");
		    	break;

		    case VENDA:
		    	hql.append(" ORDER BY qtdeVendas ");
		    	break;

		    case STATUS:
		    	hql.append(" ORDER BY descricaoSituacaoLancamento ");
		    	break;


		    default:
			hql.append(" ORDER BY T.dataLancamento  DESC ");
		    }

		    if (filtro.getPaginacao().getOrdenacao() != null) {
		    	hql.append(filtro.getPaginacao().getOrdenacao().toString());
		    }

		}

		return hql.toString();
	}


	@Override
	public ProdutoEdicaoDTO obterHistoricoProdutoEdicao(final String codigoProduto, final Long numeroEdicao, final Integer numeroCota) {

		if (codigoProduto.isEmpty() || numeroEdicao == null || numeroCota == null) {
			return null;
		}
		
		if(numeroEdicao == 0 || numeroCota == 0){
			return null;
		}

		StringBuilder sql = new StringBuilder();
		
		sql.append(" Select sum(T.REPARTE) AS reparte, sum(T.qtdeVendas) AS qtdeVendas, T.statusLancamento AS statusLancamento, T.ID AS id from (SELECT ");
        
        sql.append(" cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else -mecReparte.QTDE end ) as unsigned int) AS reparte, ");
        
        sql.append(" (case when l.status IN (:statusLancFechadoRecolhido) ");
        sql.append("   then  ");
        sql.append("    cast(sum(  ");
        sql.append("      CASE  ");
        sql.append("          WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA'  ");
        sql.append("          THEN  ");
        sql.append("            mecReparte.QTDE  ");
        sql.append("          ELSE  ");
        sql.append("            -mecReparte.QTDE  ");
        sql.append("      END)  ");
        sql.append("      - IF(mecReparte.TIPO_MOVIMENTO_ID = 9, 0,(select sum(mecEncalhe.qtde)  ");
        sql.append("          from lancamento lanc  ");
        sql.append("          LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID  ");
        sql.append("          LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID  ");
        sql.append("          LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID  ");
        sql.append("          LEFT JOIN cota cota on cota.id = cec.COTA_ID  ");
        sql.append("          LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID  ");
        sql.append("          LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
	    sql.append("          WHERE lanc.id = l.id and cota.id = c.id and ce.DATA_RECOLHIMENTO = lanc.data_rec_distrib)) AS SIGNED INT)  ");
	    sql.append("    else  ");
        sql.append("    null  ");
        sql.append("    end) as qtdeVendas,");
        
        sql.append("    l.status as statusLancamento,");
        sql.append("    pe.id as id ");
        
        sql.append(" FROM lancamento l ");

        sql.append("     JOIN produto_edicao pe ON pe.id = l.produto_edicao_id ");
        sql.append("     LEFT JOIN periodo_lancamento_parcial plp ON plp.id = l.periodo_lancamento_parcial_id ");
        sql.append("     JOIN produto p ON p.id = pe.produto_id   ");
        sql.append("     LEFT JOIN movimento_estoque_cota mecReparte on mecReparte.LANCAMENTO_ID = l.id ");
        sql.append("     LEFT JOIN tipo_movimento tipo ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
        sql.append("     JOIN cota c on mecReparte.COTA_ID = c.ID ");

        sql.append(" where l.status in (:statusLancamento) ");
        
        sql.append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
        
        sql.append(" and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
		
       // sql.append("   and p.codigo = :codigo_produto ");
        
	   // sql.append("   and pe.numero_edicao = :numero_edicao ");
	    
        sql.append("   and pe.id = :produto_edicao_id ");
	    sql.append("   and c.NUMERO_COTA = :numeroCota ");
		
		sql.append(" group by pe.numero_edicao, pe.id, mecReparte.cota_id, plp.numero_periodo ORDER BY l.ID desc) as T");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
	  
		SQLQuery query1 = getSession().createSQLQuery("select pe.id as produto_edicao_id from produto_edicao pe inner join produto p on pe.produto_id = p.id where "+
		" p.codigo = :codigo_produto and pe.numero_edicao = :numero_edicao");
		 query1.addScalar("produto_edicao_id", StandardBasicTypes.BIG_INTEGER);
		 query1.setParameter("numero_edicao", numeroEdicao);
		 query1.setParameter("codigo_produto", codigoProduto);
		
		  BigInteger produtoEdicaoId = (BigInteger) query1.uniqueResult();
		
		query.setParameter("produto_edicao_id", produtoEdicaoId);
		query.setParameter("numeroCota", numeroCota);
		
		List<String> statusLancamento = new ArrayList<>();
        statusLancamento.add(StatusLancamento.EXPEDIDO.name());
        statusLancamento.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.EM_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.RECOLHIDO.name());
        statusLancamento.add(StatusLancamento.FECHADO.name());
        
        query.setParameterList("statusLancamento", statusLancamento);
        
        query.setParameterList("statusLancFechadoRecolhido", 
                Arrays.asList(StatusLancamento.FECHADO.name(), StatusLancamento.RECOLHIDO.name(), StatusLancamento.EM_RECOLHIMENTO.name()));
		
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("qtdeVendas", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("statusLancamento", StandardBasicTypes.STRING);
        query.addScalar("id", StandardBasicTypes.LONG);
        
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));

		return (ProdutoEdicaoDTO) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicaoDTO obterHistoricoProdutoEdicaoParcial(Long idProdutoEdicao, Integer numeroPeriodo, Integer numeroCota, Long numeroEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		
		sql.append("       pe.id as id, ");
		sql.append("       l.STATUS as statusLancamento,        ");

		sql.append("       cast(sum( case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.QTDE else -mec.QTDE end ) as unsigned int) AS reparte, ");

		sql.append("       (case when l.status IN (:statusLancFechadoRecolhido) ");
		sql.append("            then   ");
		sql.append("             cast(sum(   ");
		sql.append("               CASE   ");
		sql.append("                   WHEN tm.OPERACAO_ESTOQUE = 'ENTRADA'   ");
		sql.append("                   THEN   ");
		sql.append("                     mec.QTDE   ");
		sql.append("                   ELSE   ");
		sql.append("                     -mec.QTDE   ");
		sql.append("               END)   ");
		sql.append("               - IF(mec.TIPO_MOVIMENTO_ID = 9, 0,(select sum(mecEncalhe.qtde)   ");
		sql.append("                   from lancamento lanc   ");
		sql.append("                   LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID   ");
		sql.append("                   LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID   ");
		sql.append("                   LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID   ");
		sql.append("                   LEFT JOIN cota cota on cota.id = cec.COTA_ID   ");
		sql.append("                   LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID   ");
		sql.append("                   LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID   ");
		sql.append(" 	              WHERE lanc.id = l.id and cota.id = c.id)) AS SIGNED INT)   ");
		sql.append(" 	        else   ");
		sql.append("             null   ");
		sql.append("             end) as qtdeVendas ");

		sql.append("   FROM movimento_estoque_cota mec ");
		sql.append("        JOIN tipo_movimento tm ");
		sql.append("           ON tm.id = mec.tipo_movimento_id ");
		sql.append("        JOIN lancamento l ");
		sql.append("           ON l.id = mec.lancamento_id ");
		sql.append("        JOIN produto_edicao pe  ");
		sql.append("           ON l.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append("        JOIN produto p  ");
		sql.append("           ON pe.PRODUTO_ID = p.ID ");
		sql.append("        JOIN periodo_lancamento_parcial plp ");
		sql.append("           ON plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append("              AND plp.numero_periodo = :numPeriodo  ");
		sql.append("        JOIN cota c ON c.id = mec.cota_id  ");

		sql.append("        where mec.produto_edicao_id = :idProdutoEdicao ");
		sql.append("             and pe.NUMERO_EDICAO = :numEdicao ");
		sql.append("             and c.NUMERO_COTA = :numCota ");
		sql.append("             and tm.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
		sql.append("             and mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
		sql.append("        group by mec.cota_id  ");
		sql.append("        order by mec.cota_id ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("numPeriodo", numeroPeriodo);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("numEdicao", numeroEdicao);
		query.setParameter("numCota", numeroCota);
		
        query.setParameterList("statusLancFechadoRecolhido", 
                Arrays.asList(StatusLancamento.FECHADO.name(), StatusLancamento.RECOLHIDO.name(), StatusLancamento.EM_RECOLHIMENTO.name()));
		
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("statusLancamento", StandardBasicTypes.STRING);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("qtdeVendas", StandardBasicTypes.BIG_INTEGER);
        
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));

		return (ProdutoEdicaoDTO) query.uniqueResult();
		
	}
	
	@Override
	public String obterStatusLancamentoPeriodoParcial(Long idProdutoEdicao, Integer numeroPeriodo, Long numeroEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select l.STATUS from lancamento l ");
		sql.append(" join produto_edicao pe  ");
		sql.append("   ON l.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" join periodo_lancamento_parcial plp  ");
		sql.append("   ON plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" where l.PRODUTO_EDICAO_ID = :idProdEdicao  ");
		sql.append("   and pe.NUMERO_EDICAO = :numEdicao ");
		sql.append("   and plp.NUMERO_PERIODO = :numPeriodo ");
		sql.append("   order by l.id desc limit 1 ");
				
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idProdEdicao", idProdutoEdicao);
		query.setParameter("numEdicao", numeroEdicao);
		query.setParameter("numPeriodo", numeroPeriodo);
		
		return (String) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorIdLancamento(final Long idLancamento) {


		final StringBuilder sql = new StringBuilder();

		sql.append(" select lancamento.produtoEdicao from Lancamento lancamento");
		sql.append(" where lancamento.id = :idLancamento");

		final Query query = getSession().createQuery(sql.toString());
		query.setParameter("idLancamento", idLancamento);

		final ProdutoEdicao produtoEdicao = (ProdutoEdicao)query.uniqueResult();

		return produtoEdicao;
	}


	@Override
	public Boolean estudoPodeSerSomado(final Long idEstudoBase, final String codigoProduto) {

		final StringBuilder hql = new StringBuilder();

		hql.append(" SELECT count(*) from EstudoGerado estudoGerado ");
		hql.append(" WHERE estudoGerado.produtoEdicao.produto.codigo = :codigoProduto");
		hql.append(" AND estudoGerado.id = :idEstudo");

		final Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto", 	 codigoProduto);		
		query.setParameter("idEstudo", 	 	 idEstudoBase);

		return ((Long)query.uniqueResult() > 0);
	}

	@Override
	public ProdutoEdicaoDTO findReparteEVenda(final ProdutoEdicaoDTO dto){
		final List<ProdutoEdicaoDTO> produtosEdicao = new ArrayList<>();
		produtosEdicao.add(dto);
		findReparteEVenda(produtosEdicao);
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicaoDTO> findReparteEVenda(final List<ProdutoEdicaoDTO> produtosEdicao){
		final StringBuffer selectReparte = new StringBuffer();
		final StringBuffer selectVenda = new StringBuffer();

		selectReparte.append("SELECT REPARTE,PRODUTO_EDICAO_ID FROM estudo_produto_edicao where PRODUTO_EDICAO_ID in (");

		selectVenda.append("select distinct pe.id,p.qtde_devolucao_fornecedor, m.qtde from produto_edicao pe ")
				.append("join MOVIMENTO_ESTOQUE m on m.produto_edicao_id = pe.id ")
				.append("join ESTOQUE_PRODUTO p on p.produto_edicao_id = pe.id ")
				.append("where m.tipo_movimento_id = 13 and m.qtde and p.qtde_devolucao_fornecedor is not null and pe.id in (");

		for(int i = 0; i < produtosEdicao.size(); i++){
			final ProdutoEdicaoDTO dto = produtosEdicao.get(i);
			selectReparte.append(dto.getId());
			selectVenda.append(dto.getId());
			if(i != produtosEdicao.size() - 1){
				selectReparte.append(",");
				selectVenda.append(",");
			}
		}

		selectReparte.append(");");
		selectVenda.append(") ");

		final Query queryReparte = super.getSession().createSQLQuery(selectReparte.toString());
		final Query queryVenda = super.getSession().createSQLQuery(selectVenda.toString());

		final List<Object[]> resultVenda = queryVenda.list();
		final List<Object[]> resultReparte = queryReparte.list();

		for(final ProdutoEdicaoDTO dto : produtosEdicao){
			preencherCampos(dto, resultReparte, resultVenda);
		}

		return produtosEdicao;
	}

	private void preencherCampos(final ProdutoEdicaoDTO dto, final List<Object[]> resultadoReparte, final List<Object[]> resultadoVenda){
		for(final Object[] item : resultadoReparte){
			final BigInteger id = (BigInteger)item[1];
			if(id.longValue() == dto.getId()){
				dto.setReparte((BigInteger)item[0]);
				break;
			}
		}
		for(final Object[] item : resultadoVenda){
			final BigInteger id = (BigInteger)item[0];
			if( id.longValue() == dto.getId() ){
				final BigDecimal produto = (BigDecimal) item[1];
				BigDecimal movimento = (BigDecimal) item[2];
				if(produto == null){
					break;
				}
				if(movimento == null){
					movimento = new BigDecimal(0);
				}
				final Double venda = produto.doubleValue() - movimento.doubleValue();
				dto.setVenda(venda);
				dto.setPercentualVenda( (venda / produto.doubleValue()) * 100);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void insereVendaRandomica(final ProdutoEdicao produtoEdicao) {

	    final Session s = getSession();

	    final Map<Long, BigInteger> prod = new HashMap<>(); 
	    final Criteria esto = s.createCriteria(EstoqueProdutoCota.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    final List<EstoqueProdutoCota> temp = esto.list();
	    for (final EstoqueProdutoCota x : temp) {
	    	final BigInteger venda = BigInteger.valueOf(Math.round((Math.random() * x.getQtdeRecebida().longValue())));
	    	if (prod.get(x.getProdutoEdicao().getId()) == null) {
	    		prod.put(x.getProdutoEdicao().getId(), venda);
	    	} else {
	    		prod.put(x.getProdutoEdicao().getId(), prod.get(x.getProdutoEdicao().getId()).add(venda));
	    	}
	    	x.setQtdeDevolvida(venda);
	    	s.persist(x);

	    	final MovimentoEstoqueCota mec = (MovimentoEstoqueCota) s.createCriteria(MovimentoEstoqueCota.class)
		    		.add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()))
		    		.add(Restrictions.eq("tipoMovimento.id", 21L))
		    		.add(Restrictions.eq("cota.id", x.getCota().getId())).uniqueResult();

	    	final MovimentoEstoqueCota mec_venda = new MovimentoEstoqueCota();
			BeanUtils.copyProperties(mec, mec_venda, new String[] {"id", "qtde", "tipoMovimento", "listaProdutoServicos"});
	    	mec_venda.setQtde(venda);
	    	final TipoMovimento tipoMovimento = (TipoMovimento) s.createCriteria(TipoMovimento.class).add(Restrictions.eq("id", 26L)).uniqueResult();
	    	mec_venda.setTipoMovimento(tipoMovimento);
	    	s.persist(mec_venda);
	    }

	    final Criteria espr = s.createCriteria(EstoqueProduto.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    final List<EstoqueProduto> temp3 = espr.list();
	    for (final EstoqueProduto x : temp3) {
	    	if (prod.get(x.getProdutoEdicao().getId()) != null) {
	    		x.setQtdeDevolucaoFornecedor(prod.get(x.getProdutoEdicao().getId()));
	    		s.persist(x);
	    	}
	    }

	    final Criteria lanc = s.createCriteria(Lancamento.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    final List<Lancamento> temp2 = lanc.list();
	    for (final Lancamento x : temp2) {
	    	x.setStatus(StatusLancamento.FECHADO);
	    	s.persist(x);
	    }
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean isEdicaoAberta(final Long produtoEdicaoId) {
	    final StringBuilder sql = new StringBuilder();
	    sql.append("select status ");
	    sql.append("  from lancamento ");
	    sql.append(" where produto_edicao_id = :produtoEdicaoId ");

	    final Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("produtoEdicaoId", produtoEdicaoId);
	    final List<String> lista = query.list();
	    if (lista.size() > 0 && (lista.get(0).equals("FECHADO") || lista.get(0).equals("RECOLHIDO"))) {
		return false;
	    }
	    return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterNumeroDas6UltimasEdicoesFechadas(final Long idProduto){
	
		final StringBuilder sql = new StringBuilder();


			sql.append(" select pe.NUMERO_EDICAO from produto_edicao pe left join produto p ON p.ID = pe.PRODUTO_ID ")
				.append(" LEFT join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ") 
				.append(" where p.id= :idProduto ")
				.append(" and l.STATUS='FECHADO' ")
				.append(" order by l.DATA_LCTO_PREVISTA desc ")
				.append(" limit 6  ");
		
	    final Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("idProduto", idProduto);
	    
	    final List<Long> lista = query.list();
	    return lista;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> obterNumeroDas6UltimasEdicoesFechadasPorICD(final String codigoICD){
		
		final StringBuilder sql = new StringBuilder();


			sql.append(" select pe.NUMERO_EDICAO from produto_edicao pe left join produto p ON p.ID = pe.PRODUTO_ID ")
				.append(" LEFT join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ") 
				.append(" where p.codigo_icd= :idProduto ")
				.append(" and l.STATUS='FECHADO' ")
				.append(" order by l.DATA_LCTO_PREVISTA desc ")
				.append(" limit 6  ");
		
	    final Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("idProduto", codigoICD);
	    
	    final List<Long> lista = query.list();
	    return lista;
	}
	
	@Override
	public BigDecimal obterDescontoLogistica(final Long idPropdutoEdicao){
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select pEdicao.descontoLogistica.percentualDesconto from ProdutoEdicao pEdicao ");
		hql.append(" where pEdicao.id = :idPropdutoEdicao ");
		
		final Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idPropdutoEdicao",idPropdutoEdicao);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemAutoComplete> obterPorCodigoBarraILike(final String codigoBarra) {
		
		final Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		
		final Query query = this.getSession().createQuery("select o.id as chave, o.codigoDeBarras as value, o.codigoDeBarras || ' -  ' || o.produto.nome || ' - Ed.:' || o.numeroEdicao as label from ProdutoEdicao o where o.codigoDeBarras like :codigoDeBarras");
		
		criteria.add(Restrictions.ilike("codigoDeBarras", codigoBarra,MatchMode.START));
		query.setString("codigoDeBarras", codigoBarra + "%");
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemAutoComplete.class));
		
		return query.list();
	}
	
	@Override
	public Boolean isEdicaoParcial(final Long idProdutoEdicao) {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select pEdicao.parcial from ProdutoEdicao pEdicao ");
		hql.append(" where pEdicao.id = :idPropdutoEdicao ");
		
		final Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idPropdutoEdicao",idProdutoEdicao);
		
		return (Boolean) query.uniqueResult();
		
	}
	
	@Override
	public ProdutoEdicao obterMaxProdutoEdicaoPorCodProdutoNumEdicao(final String codigoProduto, final Long numeroEdicao) {

		final StringBuilder hql = new StringBuilder();
		   
		hql.append(" from ProdutoEdicao pe where pe.id =(select max(produtoEdicao.id) from ProdutoEdicao produtoEdicao "); 
		hql.append(" 												join produtoEdicao.produto produto ");
		hql.append("					 							where ");
		hql.append(" 												produto.codigo =:codigoProduto ");
		hql.append(" 												and produtoEdicao.numeroEdicao=:numeroEdicao ) ");	
					
		final Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		
		return (ProdutoEdicao) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoPorCodigoNomeCodigoSM(
			final Integer codigoSM,
			final String codigoNomeProduto, 
			final Integer numeroCota, 
			final Integer quantidadeRegisttros,
			final Map<Long, DataCEConferivelDTO> mapaDataCEConferivel,
			final Date dataOperacao,
			final boolean indAceitaRecolhimentoParcialAtraso) {
		
		final StringBuilder hql = new StringBuilder(" select produtoEdicao ");
		   
		hql.append(" from ChamadaEncalheCota cec		")
			.append(" inner join cec.chamadaEncalhe ce	")
			.append(" inner join cec.cota cota					 		")
			.append(" inner join ce.produtoEdicao produtoEdicao 		")
			.append(" inner join produtoEdicao.produto produto			")
			.append(" inner join produto.fornecedores fornecedor  		")
			.append(" inner join produtoEdicao.lancamentos lancamentos 	")
			
			.append(" where ");

			hql.append(" cota.numeroCota = :numeroCota 				");
			
			hql.append(" and lancamentos.status != :lancamentoFechado ");
			
			if(codigoNomeProduto!=null && !codigoNomeProduto.trim().isEmpty()) {
				hql.append(" and ( produto.nome like :nomeProduto 	 ");
				hql.append(" or produto.codigo like :codigoProduto ) ");
			}
			
			if(codigoSM != null) {
				hql.append(" and ce.sequencia = :codigoSM ");
			}
			
			if(dataOperacao!=null){
				hql.append(" and ce.dataRecolhimento = :dataOperacao ");
			} else {
				carregarHQLParametrosFornecedorDatasEncalhe(hql, null, mapaDataCEConferivel, indAceitaRecolhimentoParcialAtraso);
			}
		
			hql.append(" group by produtoEdicao.id			")
			   .append(" order by produto.nome asc,			")
			   .append(" produtoEdicao.numeroEdicao desc	");
		
		final Query query = this.getSession().createQuery(hql.toString());

		
		
		if(codigoNomeProduto!=null && !codigoNomeProduto.trim().isEmpty()) {
			query.setParameter("nomeProduto", "%"+codigoNomeProduto+"%" );
			query.setParameter("codigoProduto", "%"+codigoNomeProduto+"%" );
		}

		if(codigoSM != null) {
			query.setParameter("codigoSM", codigoSM);
		}
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("lancamentoFechado", StatusLancamento.FECHADO);
		
		if(dataOperacao!=null){
			query.setParameter("dataOperacao", dataOperacao);
		} else {
			carregarHQLParametrosFornecedorDatasEncalhe(null, query, mapaDataCEConferivel, indAceitaRecolhimentoParcialAtraso);
		}
		
		query.setMaxResults(quantidadeRegisttros);
		
		return query.list();
	}
	
	
	private void carregarHQLParametrosFornecedorDatasEncalhe(
			final StringBuilder hql,
			final Query query,
			final Map<Long, DataCEConferivelDTO> mapaDataCEConferivel, 
			final boolean indAceitaRecolhimentoParcialAtraso) {
		
		if(query == null) {

			int quantidadeEntradas = mapaDataCEConferivel.size();
			
			hql.append(" and ");
			
			final Set<Long> listaIdFornecedor = mapaDataCEConferivel.keySet();
			
			hql.append(" ( ");
			
			for(final Long idFornecedor : listaIdFornecedor) {
				
				final List<Date> datasCEProdutoParcial = mapaDataCEConferivel.get(idFornecedor).getListaDataConferivelProdutoParcial();
				final List<Date> datasCEProdutoNaoParcial = mapaDataCEConferivel.get(idFornecedor).getListaDataConferivelProdutoNaoParcial();
				final boolean criaHqlParcial = !datasCEProdutoParcial.isEmpty();
				final boolean criaHqlNaoParcial = !datasCEProdutoNaoParcial.isEmpty();
				
				hql.append(" (( fornecedor.id = :idFornecedor_"+idFornecedor );
				hql.append(" and "); 
				
				hql.append(" 	( ");
				
				boolean indOr = false;
				
				
				if(criaHqlParcial && !indAceitaRecolhimentoParcialAtraso) {
					hql.append(" ((produtoEdicao.parcial = true and ce.dataRecolhimento in (:datasRecolhimentoParcial_"+idFornecedor+")))" );
					indOr = true;
				}
				
				if(criaHqlNaoParcial) {
					
					hql.append(indOr ? " or " : "");
					
					if(indAceitaRecolhimentoParcialAtraso) {
						hql.append(" ((ce.dataRecolhimento in (:datasRecolhimentoNaoParcial_"+idFornecedor+")))" );
					} else {
						hql.append(" ((produtoEdicao.parcial = false and ce.dataRecolhimento in (:datasRecolhimentoNaoParcial_"+idFornecedor+")))" );
					}
					
				}
				
				hql.append(" 	) ");
				
				hql.append(" )) ");
				
				hql.append( (--quantidadeEntradas > 0) ? " or " : "");
				
			}
			
			hql.append(" ) ");
			
		} else {
			
			final Set<Entry<Long, DataCEConferivelDTO>> entradasMapa = mapaDataCEConferivel.entrySet();
			
			for(final Entry<Long, DataCEConferivelDTO> entrada : entradasMapa) {
				
				query.setParameter("idFornecedor_"+entrada.getKey(), entrada.getKey());
				
				final List<Date> listaDataConferivelProdutoParcial = entrada.getValue().getListaDataConferivelProdutoParcial();
				final List<Date> listaDataConferivelProdutoNaoParcial = entrada.getValue().getListaDataConferivelProdutoNaoParcial();
				
				if(!listaDataConferivelProdutoParcial.isEmpty() && !indAceitaRecolhimentoParcialAtraso) {
					query.setParameterList("datasRecolhimentoParcial_"+entrada.getKey(), listaDataConferivelProdutoParcial);
				}

				if(!listaDataConferivelProdutoNaoParcial.isEmpty()) {
					query.setParameterList("datasRecolhimentoNaoParcial_"+entrada.getKey(), listaDataConferivelProdutoNaoParcial);
				}
				
				
			}
			
			
		}
		
		
	}
	
	

	@Override
	public BigDecimal obterVendaEsmagadaMedia(final String codigoProduto, final Integer numeroEdicao, 
			final Integer numeroCotaEsmagada) {
		
		final SQLQuery query = this.getSession().createSQLQuery("select sum(epc.QTDE_RECEBIDA) " +
				" from ESTOQUE_PRODUTO_COTA epc " +
				" join PRODUTO_EDICAO pe on (pe.ID = epc.PRODUTO_EDICAO_ID) " +
				" join PRODUTO p on (p.ID = pe.PRODUTO_ID) " +
				" join COTA c on (c.ID = epc.COTA_ID) " +
				" where p.CODIGO = :codigoProduto " +
				" and pe.NUMERO_EDICAO = :numeroEdicao " +
				" and epc.QTDE_DEVOLVIDA = 0 " +
				" and c.NUMERO_COTA = :numeroCotaEsmagada ");
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("numeroCotaEsmagada", numeroCotaEsmagada);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public int obterQtdEdicoesPresentes(final String codigoProduto, final String[] edicoes, 
			final Integer numeroCotaEsmagada) {
		
		final SQLQuery query = this.getSession().createSQLQuery("select count(distinct pe.ID) " +
				" from ESTOQUE_PRODUTO_COTA epc " +
				" join PRODUTO_EDICAO pe on (pe.ID = epc.PRODUTO_EDICAO_ID) " +
				" join PRODUTO p on (p.ID = pe.PRODUTO_ID) " +
				" join COTA c on (c.ID = epc.COTA_ID) " +
				" where p.CODIGO = :codigoProduto " +
				" and pe.NUMERO_EDICAO in (:edicoes) " +
				" and c.NUMERO_COTA = :numeroCotaEsmagada ");
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameterList("edicoes", edicoes);
		query.setParameter("numeroCotaEsmagada", numeroCotaEsmagada);
		
		return ((BigInteger) query.uniqueResult()).intValue();
	}


	@Override
	public boolean cotaTemProduto(final String codigoProduto, final Integer numeroEdicao,
			final Integer numeroCota) {
		
		final SQLQuery query = this.getSession().createSQLQuery(
				"select count(pe.ID) " +
				" from ESTOQUE_PRODUTO_COTA epc " +
				" join PRODUTO_EDICAO pe on (pe.ID = epc.PRODUTO_EDICAO_ID) " +
				" join PRODUTO p on (p.ID = pe.PRODUTO_ID) " +
				" join COTA c on (c.ID = epc.COTA_ID) " +
				" where p.CODIGO = :codigoProduto " +
				" and pe.NUMERO_EDICAO = :numeroEdicao " +
				" and c.NUMERO_COTA = :numeroCota ");
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("numeroCota", numeroCota);
		
		return ((BigInteger) query.uniqueResult()).compareTo(BigInteger.ZERO) > 0;
	}


    @SuppressWarnings("unchecked")
    @Override
    public List<ItemDTO<String, String>> obterProdutosBalanceados(Date dataLancamento) {
        
        String hql = " select "
                + " new "
                + ItemDTO.class.getCanonicalName()
                + " (concat(produto.codigo, '_', produtoEdicao.numeroEdicao), concat(produto.codigo, ' - ', produto.nome, ' - ', produtoEdicao.numeroEdicao)) "
                + " from Lancamento lancamento "
                + " join lancamento.produtoEdicao produtoEdicao "
                + " join produtoEdicao.produto produto "
                + " join lancamento.estudo.estudoCotas estudoCotas "
                + " where lancamento.status in (:status) "
                + " and lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor "
                + " and estudoCotas.tipoEstudo != :juramentado "
                + " group by produtoEdicao.id "
                + " order by produto.nome ";
        
        Query query = super.getSession().createQuery(hql);
    
        query.setParameterList("status", Arrays.asList(StatusLancamento.BALANCEADO,StatusLancamento.EXPEDIDO));
        
        query.setParameter("dataLancamentoDistribuidor", dataLancamento);
        query.setParameter("juramentado", TipoEstudoCota.JURAMENTADO);
        
        return query.list();
    }
    
    @Override
    public BigDecimal obterPrecoProdutoEdicao(String codigoProduto, Long numeroEdicao){
    	
    	String hql = "select coalesce(pe.precoVenda,pe.precoPrevisto) from ProdutoEdicao pe where pe.numeroEdicao=:numeroEdicao and pe.produto.codigo=:codigo";
    	
    	Query query  = getSession().createQuery(hql);
    	
    	query.setParameter("numeroEdicao", numeroEdicao);
    	query.setParameter("codigo",codigoProduto);
    	
    	return (BigDecimal) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutosEdicaoComVendaEntreDatas(FiltroCurvaABCDistribuidorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		sql.append(" pe.ID as id, pe.NUMERO_EDICAO as numeroEdicao ");
		sql.append(" from produto_edicao pe  ");
		sql.append(" join lancamento lcmt ");
		sql.append("   on lcmt.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" join produto pdt ");
		sql.append("   on pe.PRODUTO_ID = pdt.ID ");
		sql.append("   where lcmt.status IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') ");
		sql.append("         and lcmt.DATA_REC_DISTRIB BETWEEN DATE_FORMAT(:dataDe,'%Y-%m-%d') AND DATE_FORMAT(:dataAte,'%Y-%m-%d')  ");
		sql.append("         AND pdt.CODIGO = :codProduto  ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("codProduto", filtro.getCodigoProduto());
		query.setParameter("dataDe", filtro.getDataDe());
		query.setParameter("dataAte", filtro.getDataAte());
		
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicao.class));
		
		
		
		return query.list();
	}
}
