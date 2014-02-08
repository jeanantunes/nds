package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.Util;
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
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(String codigo, String nomeProduto, Long edicao, Date dataLancamento, boolean furado) {
		StringBuilder hql = new StringBuilder();
		
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
		   .append(" lancamento.status = :expedido ) ");   
		
		   if (furado) {
			   hql.append(" and   lancamento.status                     != :statusFuro");
		   }

		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("balanceadoLancamento", StatusLancamento.BALANCEADO);
		query.setParameter("expedido", StatusLancamento.EXPEDIDO);
		
		if (furado) {
			query.setParameter("statusFuro", StatusLancamento.FURO);
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

	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> listProdutoEdicaoPorCodProdutoNumEdicoes(String codigoProduto, Long numeroEdicaoInicial, Long numeroEdicaoFinal) {
		return super.getSession().createCriteria(ProdutoEdicao.class)
				.add(Restrictions.between("numeroEdicao", numeroEdicaoInicial, numeroEdicaoFinal))
				.addOrder(Order.asc("numeroEdicao"))
				.createCriteria("produto")
				.add(Restrictions.eq("codigo", codigoProduto)).list();
	}

    @Override
    @Transactional
    public BigInteger obterReparteDisponivel(Long idProdutoEdicao) {

        StringBuilder sql = new StringBuilder();
        sql.append(" select t.reparte_lcto - t.reparte_est - t.reparte_prom                  ");
        sql.append(" from (select sum(coalesce(l.reparte, 0)) reparte_lcto,                  ");
        sql.append("         sum(coalesce(e.qtde_reparte, 0)) reparte_est,                   ");
        sql.append("         sum(coalesce(l.reparte_promocional, 0)) reparte_prom            ");
        sql.append("         from lancamento l                                               ");
        sql.append("         left join estudo e on e.lancamento_id = l.id ");
        sql.append("         where l.produto_edicao_id = :idProdutoEdicao ) t                ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        return ((BigDecimal) query.uniqueResult()).toBigInteger();
    }

    @Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutoEdicaoPorCodigoBarra(String codigoBarra) {

		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoBarra));

		return criteria.list();
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
	public List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao) {

		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " where produtoEdicao.id in (:idsProdutoEdicao)";

		Query query = super.getSession().createQuery(hql);

		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);

		return query.list();
	}

	public ProdutoEdicao obterProdutoEdicaoPorSequenciaMatriz(Integer sequenciaMatriz, Date dataRecolhimentoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalhe.produtoEdicao from ChamadaEncalhe chamadaEncalhe ");

		hql.append(" where chamadaEncalhe.sequencia = :sequenciaMatriz ");

		hql.append(" and chamadaEncalhe.dataRecolhimento = :dataRecolhimentoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("sequenciaMatriz", sequenciaMatriz);

		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);

		return (ProdutoEdicao) query.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterCodigoMatrizPorProdutoEdicao(java.lang.Long, java.util.Date)
	 */
	public Integer obterCodigoMatrizPorProdutoEdicao(Long idProdutoEdicao, Date dataRecolhimento, Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
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
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("numeroCota", numeroCota);
		
		if(dataRecolhimento!=null) {
			query.setParameter("dataRecolhimento", dataRecolhimento);
		}
		
		return (Integer) query.uniqueResult();
	}

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorSM(Long sm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nome,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int initialResult, int maxResults) {
			
		StringBuilder hql = new StringBuilder()
		
		.append(" SELECT pe.id as id, p.codigo as codigoProduto, p.NOME_COMERCIAL as nomeComercial, ")
		.append("        pe.NUMERO_EDICAO as numeroEdicao, coalesce(pessoa.nome, pessoa.RAZAO_SOCIAL) as nomeFornecedor, ")
		.append("        l.TIPO_LANCAMENTO as statusLancamento, ") 
		.append("        l.status as statusSituacao , ") 
		.append("        pe.possui_brinde as temBrinde, ")
		.append("        pe.parcial as parcial ");
		
		// Corpo da consulta com os filtros:
		SQLQuery query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortname, sortorder);
		
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

	@SuppressWarnings("rawtypes")
	@Override
	public Integer countPesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe.id as total ");
		
		// Corpo da consulta com os filtros:
		SQLQuery query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, nomeProduto, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, null, null);
		
		query.addScalar("total", StandardBasicTypes.LONG);
		
		List idsProdutoEdicao = query.list();
		
		if(idsProdutoEdicao == null) {
			return 0;
		}
		
		return  idsProdutoEdicao.size();
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
	private SQLQuery queryBodyPesquisarEdicoes(StringBuilder hql, String codigoProduto, String nome,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde, String sortname, String sortorder) {
		
		hql.append("   from PRODUTO_EDICAO pe ");
		hql.append("   inner join PRODUTO p on pe.PRODUTO_ID=p.ID "); 
		hql.append("   left join PRODUTO_FORNECEDOR pf on p.ID=pf.PRODUTO_ID "); 
		hql.append("   left join FORNECEDOR f on pf.fornecedores_ID=f.ID ");
		hql.append("   left join PESSOA pessoa on f.JURIDICA_ID=pessoa.ID ");
		hql.append("   join LANCAMENTO l on pe.ID=l.PRODUTO_EDICAO_ID "); 
		hql.append("   where pe.ATIVO = :indAtivo ");
		hql.append("   and l.id=( ");
		hql.append("       select ");
		hql.append("           max(l.id) "); 
		hql.append("       from ");
		hql.append("           LANCAMENTO l "); 
		hql.append("       where ");
		hql.append("           l.PRODUTO_EDICAO_ID=pe.ID ");
		hql.append("   ) ");
		
		                                                        /**
         * Comentado por Eduardo "PunkRock" Castro em 05/12 devido a existencia
         * de dados na tabela de ProdutoEdicao e não eh apresentado no grid
         */
		//hql.append(" AND ln.id = (select max(ln.id) from ln where ln.produtoEdicao.id = pe.id) ");
		
		// Filtros opcionais da pesquisa:
		if (dataLancamento != null) {
			hql.append("  AND (l.DATA_LCTO_DISTRIBUIDOR between :dataLancamentoDe and :dataLancamentoAte OR l.DATA_LCTO_PREVISTA between :dataLancamentoDe and :dataLancamentoAte) ");
		}
		
		if (preco != null) {
			hql.append("  AND (pe.PRECO_VENDA between :precoDe and :precoAte) ");
		}
		if (statusLancamento != null) {
            hql.append("  AND l.status = :situacaoLancamento ");

		}		
		if (!StringUtil.isEmpty(codigoProduto)) {
			hql.append("  AND UPPER(p.codigo) LIKE UPPER(:codigoProduto) ");
		}
		if (!StringUtil.isEmpty(nome)) {
			hql.append("  AND UPPER(p.nome) LIKE UPPER(:nome) ");
		}
		if (!StringUtil.isEmpty(codigoDeBarras)) {
			hql.append("  AND pe.CODIGO_DE_BARRAS LIKE :codigoDeBarras ");
		}
		if (brinde) {
			hql.append("  AND pe.POSSUI_BRINDE = :possuiBrinde ");
		}
		
		hql.append(" GROUP BY pe.id ");
		
		// Ordenacao:
		if (sortname != null && sortorder != null) {
			hql.append(" ORDER BY " + sortname + " " + sortorder);
		}
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
		query.setParameter("indAtivo", true);
		
        // Parâmetros opcionais da pesquisa:
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
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(String codigoProduto,	int qtdEdicoes) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe ");
		
		// Corpo da consulta com os filtros:
		Query query = this.queryBodyPesquisarEdicoes(hql, codigoProduto, null, null, null, null, null, false, "pe.numeroEdicao", "DESC");
		
		query.setMaxResults(qtdEdicoes);
		
		return query.list();
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
    public boolean isNumeroEdicaoCadastrada(Long idProduto,
			Long numeroEdicao, Long idProdutoEdicao) {
        
        Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);
        criteria.setProjection(Projections.rowCount());
        
        criteria.add(Restrictions.eq("numeroEdicao", numeroEdicao));
        criteria.add(Restrictions.eq("produto.id", idProduto));
        if (idProdutoEdicao != null) {
            criteria.add(Restrictions.not(Restrictions.idEq(idProdutoEdicao)));
        }
        Long qtd = (Long) criteria.uniqueResult();
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
     * 
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer obterQuantidadeEdicoesPorCodigoProduto(String codigoProduto) {
		// TODO Auto-generated method stub

		Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);

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
	public List<ProdutoEdicao> obterProdutosEdicoesPorCodigoProdutoLimitado(String codigoProduto, Integer limite) {

		Criteria criteria = getSession().createCriteria(ProdutoEdicao.class);

		criteria.createAlias("produto", "produto");

		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));

		criteria.setMaxResults(limite);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoProdutoDTO> obterProdutosEdicoesPorCodigoProdutoComDesconto(
			String codigoProduto) {

		StringBuilder hql = new StringBuilder();

//		case when pe.desconto is null or pe.desconto = 0 then (case when p.desconto is null then 0 else p.desconto end) else pe.desconto end		
		hql .append("select new ")
			.append(TipoDescontoProdutoDTO.class.getCanonicalName())
			.append("(p.codigo, p.nome, pe.numeroEdicao, d.valor, d.dataAlteracao, u.nome) ")
			.append("\n")
			.append("from ProdutoEdicao pe join pe.produto p join pe.desconto as d join d.usuario u")
			.append("\n")
			.append("where p.codigo = :codigoProduto ")
			.append("\n");

		Query q = getSession().createQuery(hql.toString());

		q.setParameter("codigoProduto", codigoProduto);

		return (List<TipoDescontoProdutoDTO>) q.list();

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
	public Set<ProdutoEdicao> obterProdutosEdicaoPorFornecedor(Long idFornecedor) {

		String queryString = " select produtoEdicao from ProdutoEdicao produtoEdicao "
						   + " join produtoEdicao.produto.fornecedores fornecedores"
				   		   + " where fornecedores.id = :idFornecedor ";

		Query query = this.getSession().createQuery(queryString);

		query.setParameter("idFornecedor", idFornecedor);

		return new HashSet<ProdutoEdicao>(query.list());
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> buscarProdutosLancadosData(Date data) {

		StringBuilder hql = new StringBuilder("select distinct l.produtoEdicao ");
		hql.append(" from Lancamento l ")
		   .append(" where l.dataLancamentoDistribuidor = :data ")
		   .append(" and l.status= '" + StatusLancamento.EXPEDIDO.toString() + "'");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", data);
		query.setMaxResults(6);

		return query.list();
	}
	

	@Override
	public String buscarNome(Long idProdutoEdicao) {

		Query query = this.getSession().createQuery(
				"select produtoEdicao.nomeComercial from ProdutoEdicao produtoEdicao where produtoEdicao.id = :idProdutoEdicao");

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (String) query.uniqueResult();
	}


	@Override
	public Long obterUltimoNumeroEdicao(Long idProduto) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select max(pe.numeroEdicao)	");
		
		hql.append(" from ProdutoEdicao pe			");
		
		hql.append(" inner join pe.produto p		");
		
		hql.append(" where p.id = :idProduto		");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProduto", idProduto);
		
		return (Long) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorDistribuidor(Set<Fornecedor> fornecedores) {

		StringBuilder idsFornecedores = new StringBuilder();
		for (Fornecedor fornecedor : fornecedores) {
			if (!idsFornecedores.toString().isEmpty()) {
				idsFornecedores.append(", ");
			}
			idsFornecedores.append(fornecedor.getId());
		}

		String queryString = "SELECT "
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

		Query query = this.getSession().createQuery(queryString);

		return new HashSet<ProdutoEdicao>(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorCota(Cota cota, Set<Fornecedor> fornecedores) {

		StringBuilder idsFornecedores = new StringBuilder();
		for (Fornecedor fornecedor : fornecedores) {
			if (!idsFornecedores.toString().isEmpty()) {
				idsFornecedores.append(", ");
			}
			idsFornecedores.append(fornecedor.getId());
		}		

		String queryString = "SELECT "
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

		//queryString += whereString;

		Query query = this.getSession().createQuery(queryString);

		return new HashSet<ProdutoEdicao>(query.list());
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<EdicoesProdutosDTO> obterHistoricoEdicoes(FiltroHistogramaVendas filtro) {

		String queryStringProdutoEdicao = 
				" SELECT " +
				" 	numeroEdicao as edicao, " +
				" 	periodicidade as periodo, " +
				" 	sum(venda) as venda, " +
				" 	dataRecolhimento as dtRecolhimento, " +
				" 	dataLancamento as dtLancamento, " +
				" 	sum(reparte) as reparte, " +
				" 	nomeProduto, " +
				" 	codigoProduto, " +
				" 	classificacao as descricaoTipoClassificacao, " +
				" 	segmento as descricaoTipoSegmento, " +
				" 	statusLancamento as status " +
				" FROM " +
				"( SELECT distinct " +
				" 	produtoedi1_.NUMERO_EDICAO as numeroEdicao, " +
				" 	produto5_.PERIODICIDADE as periodicidade, " +
				" 	estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA as venda, " +
				" 	lancamento2_.DATA_REC_DISTRIB as dataRecolhimento, " +
				" 	lancamento2_.DATA_LCTO_DISTRIBUIDOR as dataLancamento, " +
				" 	estoqueProdutoCota.QTDE_RECEBIDA as reparte, " +
				" 	produto5_.NOME as nomeProduto, " +
				" 	produto5_.CODIGO as codigoProduto, " +				
				" 	tipoclassi6_.DESCRICAO as classificacao, " +
				" 	tiposegmen7_.DESCRICAO as segmento,"+
				" 	lancamento2_.STATUS as statusLancamento, " +
				"   estoqueProdutoCota.produto_edicao_id, " +
				" 	estoqueProdutoCota.cota_id " +

				"FROM " +
				 " ESTOQUE_PRODUTO_COTA estoqueProdutoCota " +
				 " inner join PRODUTO_EDICAO produtoedi1_ on estoqueProdutoCota.PRODUTO_EDICAO_ID=produtoedi1_.ID  " +
				 " inner join LANCAMENTO lancamento2_ on produtoedi1_.ID=lancamento2_.PRODUTO_EDICAO_ID  " +	
				 " inner join PRODUTO produto5_ on produtoedi1_.PRODUTO_ID=produto5_.ID  " +
				 " left join TIPO_CLASSIFICACAO_PRODUTO tipoclassi6_ on produtoedi1_.TIPO_CLASSIFICACAO_PRODUTO_ID=tipoclassi6_.ID " +
				 " left join TIPO_SEGMENTO_PRODUTO tiposegmen7_ on produto5_.TIPO_SEGMENTO_PRODUTO_ID=tiposegmen7_.ID " +
				 " inner join COTA cota2_ on estoqueProdutoCota.COTA_ID=cota2_.ID  " +
				 " left join BOX box on cota2_.BOX_ID=box.ID  " +
				 " ";


		List<String> whereList = new ArrayList<String>();
		HashMap<String,Object> parameterMap = new HashMap<String,Object>();

		//Filtro por
		if (StringUtils.isNotEmpty(filtro.getFiltroPor())) {
			whereList.add(" box.tipo_box = :tipoBox ");
			parameterMap.put("tipoBox",TipoBox.values()[Integer.parseInt(filtro.getFiltroPor())]);
		}

		//codigo = codigo do produto
		if (StringUtils.isNotEmpty(filtro.getCodigo())) {

			whereList.add(" produto5_.CODIGO_ICD = :codICD");
			
			parameterMap.put("codICD",filtro.getCodigo());

		}

		//edicao = numero da edicao
		if (StringUtils.isNotEmpty(filtro.getEdicao())) {
			whereList.add(" produtoedi1_.numero_edicao = :numeroEdicao");
			parameterMap.put("numeroEdicao",Long.valueOf(filtro.getEdicao()));
		}

        // classificação = classificação do produto
		if (filtro.getIdTipoClassificacaoProduto() != null) {
			whereList.add(" tipoclassi6_.id = :tipoClassificacaoProdutoId");
			parameterMap.put("tipoClassificacaoProdutoId",Long.valueOf(filtro.getIdTipoClassificacaoProduto()));
		}		

		// check opcao de componente e elemento
		if (StringUtils.isNotEmpty(filtro.getInserirComponentes())
				&& filtro.getInserirComponentes().equalsIgnoreCase("checked")
				&& !filtro.getComponente().equalsIgnoreCase("-1")) {

			queryStringProdutoEdicao += " 	 left outer join PDV pdvs on cota2_.ID = pdvs.COTA_ID "; //" join cota.pdvs pdvs ";

			// JOIN'S Relacionados ao componente/elemento
			/*
			 * " join pdvs.segmetacao segmentacao " +
			 * " join segmetacao.TipoPontoPDV " +
			 * " join segmetacao.areaInfluenciaPDV " +
			 */

			switch (ComponentesPDV.values()[Integer.parseInt(filtro
					.getComponente())]) {
			case TIPO_PONTO_DE_VENDA:
				/*queryStringProdutoEdicao += 
							" join pdvs.segmentacao segmentacao "
						  + " join segmentacao.tipoPontoPDV ";*/

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

				queryStringProdutoEdicao += " left outer join PARAMETRO_COBRANCA_COTA param_cob_cota on cota2_.ID = param_cob_cota.cota_id ";

				whereList.add(" param_cob_cota.tipo_cota = :tipoCota");
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

		queryStringProdutoEdicao += " where "+StringUtils.join(whereList," and ");

		//Group by
		queryStringProdutoEdicao += " ) as base " +
									" GROUP BY base.numeroEdicao  " + 
									this.ordenarConsultaHistogramaVenda(filtro);

		Query query = this.getSession().createSQLQuery(queryStringProdutoEdicao);

		setParameters(query, parameterMap);
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));
		configurarPaginacao(filtro,query);
		List<EdicoesProdutosDTO> resultado = query.list();

		return resultado;
	}

	private String ordenarConsultaHistogramaVenda(FiltroHistogramaVendas filtro) {

		StringBuilder hql = new StringBuilder();

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
	public AnaliseHistogramaDTO obterBaseEstudoHistogramaPorFaixaVenda(FiltroHistogramaVendas filtro,String codigoProduto,Integer de, Integer ate,String[] edicoes) {

		//Criada para EMS 2029
		String queryStringProdutoEdicao = 
				"select 'De "+de+" a "+ate+"' as faixaVenda," +
				" sum(reparteTotal) / :qtdEdicoes as repTotal, " +
				" (sum(reparteTotal) / :qtdEdicoes) / count(distinct COTA_ID) as repMedio, " +
				" sum(HIST.qtde_Recebida - HIST.qtde_Devolvida) / :qtdEdicoes as vdaTotal, " +
				" (sum(HIST.qtde_Recebida - HIST.qtde_Devolvida) / :qtdEdicoes) / count(distinct COTA_ID) as vdaMedio, " +
				" ((sum(HIST.qtde_Recebida - hist.qtde_Devolvida) / :qtdEdicoes) / (sum(reparteTotal) / :qtdEdicoes)) * 100 as percVenda, " +

				// encalheMedio = ((rep total-vda nominal)/qte.cotas)
				// " (sum(reparteTotal)-sum(HIST.qtde_Recebida - HIST.qtde_Devolvida))/count(HIST.COTA_ID) as encalheMedio, " +
				" (avg(HIST.qtde_Devolvida) / :qtdEdicoes) as encalheMedio , " + 

                // Part Reparte – participação do reparte desta faixa de venda
                // em relação ao reparte total da edição
                // O valor dessa coluna é a soma de reparte das cotas que fazem
                // parte desta faixa dividido pelo reparte total da edição.
				" sum(hist.qtde_Recebida)/sum(reparteTotal) as partReparte, " +

                // Part Venda - mesmo critério que Part de Reparte, porém,
                // observando a venda.
                // Resumindo, é a soma de venda das cotas que fazem parte desta
                // faixa dividido pela venda total da edição.
				" sum(HIST.qtde_Recebida - HIST.qtde_Devolvida) / " +
				//subselect para totalizar as vendas de um produto, sem filtro por cota
				" (select sum(qtde_recebida)-sum(qtde_devolvida) from estoque_produto_cota " +
				"	JOIN produto_edicao on produto_edicao.ID = estoque_produto_cota.PRODUTO_EDICAO_ID" +
				"	JOIN PRODUTO ON produto_edicao.PRODUTO_ID=PRODUTO.ID" +
				"	where produto.CODIGO = :produtoCodigo and " +
				"	produto_Edicao.NUMERO_EDICAO in ( :nrEdicoes )) as partVenda," +

				" count(distinct COTA_ID) as qtdeCotas, " +
				" group_concat(distinct COTA_ID) as idCotaStr, " +
				" group_concat(distinct cotasEsmagadas) as idCotasEsmagadas," +
				" count(distinct cotasEsmagadas) as cotasEsmagadas, " +
				" sum(vdEsmag) as vendaEsmagadas, " +
				" sum(cotaAtiva) as qtdeCotasAtivas, " +
				" sum(HIST.qtdeCotasSemVenda) as qtdeCotasSemVenda" +

				//select para totalizar a qtde de cotas ativas para calculo no resumo da tela da EMS 2029
				" from " +
				" ( select DISTINCT " +
				"	case when (sum((estoqueProdutoCota.QTDE_RECEBIDA - estoqueProdutoCota.QTDE_DEVOLVIDA ) / :qtdEdicoes)) between :deMargem and :ate " +
				"	and (sum(estoqueProdutoCota.QTDE_DEVOLVIDA / :qtdEdicoes)) = 0  then cota2_.numero_cota else null end as cotasEsmagadas, " +
				
				"   case when (sum((estoqueProdutoCota.QTDE_RECEBIDA - estoqueProdutoCota.QTDE_DEVOLVIDA ) / :qtdEdicoes)) between :deMargem and :ate " +
				"	and (sum(estoqueProdutoCota.QTDE_DEVOLVIDA / :qtdEdicoes)) = 0 then round(sum(estoqueProdutoCota.QTDE_RECEBIDA - estoqueProdutoCota.QTDE_Devolvida)) else 0 end as vdEsmag," +
				
				"   case when round(sum(estoqueProdutoCota.QTDE_DEVOLVIDA) / :qtdEdicoes) = round(sum(estoqueProdutoCota.QTDE_RECEBIDA) / :qtdEdicoes) then 1 else 0 end as qtdeCotasSemVenda," +
				"   case when cota2_.SITUACAO_CADASTRO='ATIVO' then 1 else 0 end as cotaAtiva," +
				"	  sum(estoqueProdutoCota.QTDE_RECEBIDA) as reparteTotal," +	
					" estoqueProdutoCota.ID as col_2_0_, " +
					" cota2_.numero_cota as COTA_ID, " +
					" estoqueProdutoCota.PRODUTO_EDICAO_ID as PRODUTO6_585_, " +
					" sum(estoqueProdutoCota.QTDE_DEVOLVIDA) as QTDE_DEVOLVIDA, " +
					" sum(estoqueProdutoCota.QTDE_RECEBIDA) as QTDE_RECEBIDA " +

					" from ESTOQUE_PRODUTO_COTA estoqueProdutoCota " +
					" 	left outer join PRODUTO_EDICAO produtoEdicao on estoqueProdutoCota.PRODUTO_EDICAO_ID=produtoEdicao.ID " +
					" 	left outer join LANCAMENTO lancamento on produtoEdicao.ID=lancamento.PRODUTO_EDICAO_ID " +
					" 	left outer join PRODUTO produto4_ on produtoEdicao.PRODUTO_ID=produto4_.ID " +
					" 	left outer join COTA cota2_ on estoqueProdutoCota.COTA_ID=cota2_.ID ";


		// Adicionando filtro

		List<String> whereList = new ArrayList<String>();
		HashMap<String,Object> parameterMap = new HashMap<String,Object>();

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

			// JOIN'S Relacionados ao componente/elemento
			/*
			 * " join pdvs.segmetacao segmentacao " +
			 * " join segmetacao.TipoPontoPDV " +
			 * " join segmetacao.areaInfluenciaPDV " +
			 */

			switch (ComponentesPDV.values()[Integer.parseInt(filtro
					.getComponente())]) {
			case TIPO_PONTO_DE_VENDA:
				/*queryStringProdutoEdicao += 
							" join pdvs.segmentacao segmentacao "
						  + " join segmentacao.tipoPontoPDV ";*/

				whereList.add(" estoqueProdutoCota.cota_id in (select distinct cota.id from cota inner " +
								"join pdv on pdv.cota_id = cota.id where tipo_ponto_pdv_id = :codigoTipoPontoPDV) ");
				parameterMap.put("codigoTipoPontoPDV",
						Long.parseLong(filtro.getElemento()));

				break;
			case AREA_DE_INFLUENCIA:

				/*queryStringProdutoEdicao += " join pdvs.segmentacao segmentacao "
						+ " join segmentacao.areaInfluenciaPDV ";*/
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

				queryStringProdutoEdicao += " left outer join PARAMETRO_COBRANCA_COTA param_cob_cota on cota2_.ID = param_cob_cota.cota_id ";

				whereList.add(" param_cob_cota.tipo_cota = :tipoCota");
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

		queryStringProdutoEdicao += " group by numero_cota " +
									" having round(sum(estoqueProdutoCota.QTDE_RECEBIDA - estoqueProdutoCota.QTDE_DEVOLVIDA) / :qtdEdicoes) >= :de" +  
									" and round(sum(estoqueProdutoCota.QTDE_RECEBIDA - estoqueProdutoCota.QTDE_DEVOLVIDA) / :qtdEdicoes) <= :ate";

		queryStringProdutoEdicao+=") as HIST";

		SQLQuery query = this.getSession().createSQLQuery(queryStringProdutoEdicao);
		query.setParameter("de", de);
		query.setParameter("ate", ate);
		query.setParameter("produtoCodigo", Util.padLeft(codigoProduto, "0", 8));
		query.setParameter("qtdEdicoes", edicoes.length);
		query.setParameterList("nrEdicoes", edicoes);
		query.setParameter("deMargem", de - 0.5);

		setParameters(query, parameterMap);

		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseHistogramaDTO.class));

		AnaliseHistogramaDTO resultado = (AnaliseHistogramaDTO) query.uniqueResult();


		return resultado;
	}

	private void configurarPaginacao(FiltroDTO dto, Query query) {

        PaginacaoVO paginacao = dto.getPaginacao();

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
	public List<ProdutoEdicaoDTO> obterEdicoesProduto(FiltroHistoricoVendaDTO filtro) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT ");
		hql.append(" produto.codigo as codigoProduto, ");
		hql.append(" produto.nome as nomeProduto, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" produto.periodicidade as periodicidade, ");
		hql.append(" lancamento.dataLancamentoPrevista as dataLancamento, ");
		hql.append(" sum(estoqueProduto.qtdeRecebida) as repartePrevisto, ");
		hql.append(" sum(estoqueProduto.qtdeRecebida - coalesce(estoqueProduto.qtdeDevolvida, 0)) as qtdeVendas,");
		hql.append(" lancamento.status as situacaoLancamento, ");
		hql.append(" produtoEdicao.chamadaCapa as chamadaCapa, ");
		hql.append(" produtoEdicao.tipoClassificacaoProduto as tipoClassificacaoProduto ");
		hql.append(" FROM EstoqueProdutoCota estoqueProduto");
		//hql.append(" LEFT JOIN estoqueProduto.movimentos as movimentos");
		//hql.append(" LEFT JOIN movimentos.tipoMovimento as tipoMovimento");
		hql.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao");
		hql.append(" JOIN produtoEdicao.lancamentos as lancamento ");
		hql.append(" JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN produtoEdicao.tipoClassificacaoProduto as tipoClassificacaoProduto ");

		hql.append(" WHERE ");
		//hql.append(" tipoMovimento.id = 13 and ");

		hql.append(" produto.codigo = :codigoProduto ");
		parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());

		if (filtro.getListProdutoEdicaoDTO() != null && !filtro.getListProdutoEdicaoDTO().isEmpty()) {
			hql.append(" and produtoEdicao.numeroEdicao in (");

			for (int i = 0; i < filtro.getListProdutoEdicaoDTO().size(); i++) {
				hql.append(filtro.getListProdutoEdicaoDTO().get(i).getNumeroEdicao());	

				if (filtro.getListProdutoEdicaoDTO().size() != i + 1) {
					hql.append(","); 
				}
			}

			hql.append(")");
		}

		if (filtro.getTipoClassificacaoProdutoId() != null && filtro.getTipoClassificacaoProdutoId() > 0l) {
			hql.append(" and tipoClassificacaoProduto.id = :tipoClassificacaoProdutoId ");
			parameters.put("tipoClassificacaoProdutoId", filtro.getTipoClassificacaoProdutoId());
		}
		if (filtro.getNumeroEdicao() != null && filtro.getNumeroEdicao() > 0l) {
			hql.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ");
			parameters.put("numeroEdicao", filtro.getNumeroEdicao());
		} 

		hql.append(" GROUP BY produtoEdicao.numeroEdicao ");

		if(filtro.getOrdemColuna() != null){
			hql.append(this.ordenarConsultaHistoricoVendaProdutoEdicao(filtro));
		}else{
			hql.append(" ORDER BY lancamento.dataLancamentoPrevista DESC ");			
		}

		Query query = super.getSession().createQuery(hql.toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));

		this.setParameters(query, parameters);

		return query.list();
	}




	private String ordenarConsultaHistoricoVendaProdutoEdicao(FiltroHistoricoVendaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

		    switch (filtro.getOrdemColuna()) {


		    case CODIGO:
		    	hql.append(" ORDER BY codigoProduto ");
		    	break;

		    case CLASSIFICACAO:
		    	hql.append(" ORDER BY tipoClassificacaoProduto ");
		    	break;

		    case EDICAO:
				hql.append(" ORDER BY numeroEdicao ");
				break;

		    case PERIODO:
		    	hql.append(" ORDER BY periodicidade ");
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
		    	hql.append(" ORDER BY situacaoLancamento ");
		    	break;


		    default:
			hql.append(" ORDER BY produtoEdicao.numeroEdicao DESC ");
		    }

		    if (filtro.getPaginacao().getOrdenacao() != null) {
		    	hql.append(filtro.getPaginacao().getOrdenacao().toString());
		    }

		}

		return hql.toString();
	}


	@Override
	public ProdutoEdicaoDTO obterHistoricoProdutoEdicao(String codigoProduto, Long numeroEdicao, Integer numeroCota) {

		if (codigoProduto.isEmpty() || numeroEdicao == 0 || numeroCota == 0) {
			return null;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT ");

		hql.append(" sum(estoqueProdutoCota.qtdeRecebida) as reparte, ");
		hql.append(" sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) as qtdeVendas ");

		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");

		hql.append(" WHERE ");
		hql.append(" produto.codigo = :codigoProduto ");
		parameters.put("codigoProduto", codigoProduto);

		hql.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ");
		parameters.put("numeroEdicao", numeroEdicao);

		hql.append(" and cota.numeroCota = :numeroCota ");
		parameters.put("numeroCota", numeroCota);

		hql.append(" GROUP BY estoqueProdutoCota.cota ");

		Query query = super.getSession().createQuery(hql.toString());

		this.setParameters(query, parameters);

		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));

		return (ProdutoEdicaoDTO) query.uniqueResult();
	}

	/*@Override
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorProduto(Produto produto) {
		String queryString = "SELECT "
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
				+         "WHERE "
				+             "produto.id = " + produto.getId() + " "
				+                 "AND cota.id IN (SELECT cota.id FROM Produto produto JOIN produto.fornecedores fornecedor JOIN fornecedor.cotas cota WHERE produto.id = " + produto.getId() + ") "
				+                 "AND descontoProdutoEdicao.tipoDesconto = ('ESPECIFICO'))"
				+ 	"AND produtoEdicao.id NOT IN (SELECT "
				+             "produtoEdicao.id "
					+         "FROM "
					+             "DescontoProdutoEdicao descontoProdutoEdicao "
					+         "JOIN descontoProdutoEdicao.produtoEdicao produtoEdicao "
				+         "JOIN descontoProdutoEdicao.cota cota "
				+         "JOIN descontoProdutoEdicao.fornecedor fornecedor "
				+         "WHERE "
				+             "produto.id = " + produto.getId() + " "
				+                 "AND fornecedor.id IN (SELECT fornecedor.id FROM Produto produto JOIN produto.fornecedor fornecedor WHERE produto.id = " + produto.getId() + ") "
				+                 "AND descontoProdutoEdicao.tipoDesconto = ('GERAL'))";

			//queryString += whereString;
			
			Query query = this.getSession().createQuery(queryString);
			
			return new HashSet<ProdutoEdicao>(query.list());
	}*/

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorIdLancamento(Long idLancamento) {


		StringBuilder sql = new StringBuilder();

		sql.append(" select lancamento.produtoEdicao from Lancamento lancamento");
		sql.append(" where lancamento.id = :idLancamento");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("idLancamento", idLancamento);

		ProdutoEdicao produtoEdicao = (ProdutoEdicao)query.uniqueResult();

		return produtoEdicao;
	}


	@Override
	public Boolean estudoPodeSerSomado(Long idEstudoBase, String codigoProduto) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT count(*) from EstudoGerado estudoGerado ");
		hql.append(" WHERE estudoGerado.produtoEdicao.produto.codigo = :codigoProduto");
		hql.append(" AND estudoGerado.id = :idEstudo");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto", 	 codigoProduto);		
		query.setParameter("idEstudo", 	 	 idEstudoBase);

		return ((Long)query.uniqueResult() > 0);
	}

	@Override
	public ProdutoEdicaoDTO findReparteEVenda(ProdutoEdicaoDTO dto){
		List<ProdutoEdicaoDTO> produtosEdicao = new ArrayList<>();
		produtosEdicao.add(dto);
		findReparteEVenda(produtosEdicao);
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicaoDTO> findReparteEVenda(List<ProdutoEdicaoDTO> produtosEdicao){
		StringBuffer selectReparte = new StringBuffer();
		StringBuffer selectVenda = new StringBuffer();

		selectReparte.append("SELECT REPARTE,PRODUTO_EDICAO_ID FROM estudo_produto_edicao where PRODUTO_EDICAO_ID in (");

		selectVenda.append("select distinct pe.id,p.qtde_devolucao_fornecedor, m.qtde from produto_edicao pe ")
				.append("join MOVIMENTO_ESTOQUE m on m.produto_edicao_id = pe.id ")
				.append("join ESTOQUE_PRODUTO p on p.produto_edicao_id = pe.id ")
				.append("where m.tipo_movimento_id = 13 and m.qtde and p.qtde_devolucao_fornecedor is not null and pe.id in (");

		for(int i = 0; i < produtosEdicao.size(); i++){
			ProdutoEdicaoDTO dto = produtosEdicao.get(i);
			selectReparte.append(dto.getId());
			selectVenda.append(dto.getId());
			if(i != produtosEdicao.size() - 1){
				selectReparte.append(",");
				selectVenda.append(",");
			}
		}

		selectReparte.append(");");
		selectVenda.append(") ");

		Query queryReparte = super.getSession().createSQLQuery(selectReparte.toString());
		Query queryVenda = super.getSession().createSQLQuery(selectVenda.toString());

		List<Object[]> resultVenda = queryVenda.list();
		List<Object[]> resultReparte = queryReparte.list();

		for(ProdutoEdicaoDTO dto : produtosEdicao){
			preencherCampos(dto, resultReparte, resultVenda);
		}

		return produtosEdicao;
	}

	private void preencherCampos(ProdutoEdicaoDTO dto, List<Object[]> resultadoReparte, List<Object[]> resultadoVenda){
		for(Object[] item : resultadoReparte){
			BigInteger id = (BigInteger)item[1];
			if(id.longValue() == dto.getId()){
				dto.setReparte((BigInteger)item[0]);
				break;
			}
		}
		for(Object[] item : resultadoVenda){
			BigInteger id = (BigInteger)item[0];
			if( id.longValue() == dto.getId() ){
				BigDecimal produto = (BigDecimal) item[1];
				BigDecimal movimento = (BigDecimal) item[2];
				if(produto == null){
					break;
				}
				if(movimento == null){
					movimento = new BigDecimal(0);
				}
				Double venda = produto.doubleValue() - movimento.doubleValue();
				dto.setVenda(venda);
				dto.setPercentualVenda( (venda / produto.doubleValue()) * 100);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void insereVendaRandomica(ProdutoEdicao produtoEdicao) {

	    Session s = getSession();

	    Map<Long, BigInteger> prod = new HashMap<>(); 
	    Criteria esto = s.createCriteria(EstoqueProdutoCota.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    List<EstoqueProdutoCota> temp = esto.list();
	    for (EstoqueProdutoCota x : temp) {
	    	BigInteger venda = BigInteger.valueOf(Math.round((Math.random() * x.getQtdeRecebida().longValue())));
	    	if (prod.get(x.getProdutoEdicao().getId()) == null) {
	    		prod.put(x.getProdutoEdicao().getId(), venda);
	    	} else {
	    		prod.put(x.getProdutoEdicao().getId(), prod.get(x.getProdutoEdicao().getId()).add(venda));
	    	}
	    	x.setQtdeDevolvida(venda);
	    	s.persist(x);

	    	MovimentoEstoqueCota mec = (MovimentoEstoqueCota) s.createCriteria(MovimentoEstoqueCota.class)
		    		.add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()))
		    		.add(Restrictions.eq("tipoMovimento.id", 21L))
		    		.add(Restrictions.eq("cota.id", x.getCota().getId())).uniqueResult();

	    	MovimentoEstoqueCota mec_venda = new MovimentoEstoqueCota();
			BeanUtils.copyProperties(mec, mec_venda, new String[] {"id", "qtde", "tipoMovimento", "listaProdutoServicos"});
	    	mec_venda.setQtde(venda);
	    	TipoMovimento tipoMovimento = (TipoMovimento) s.createCriteria(TipoMovimento.class).add(Restrictions.eq("id", 26L)).uniqueResult();
	    	mec_venda.setTipoMovimento(tipoMovimento);
	    	s.persist(mec_venda);
	    }

	    Criteria espr = s.createCriteria(EstoqueProduto.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    List<EstoqueProduto> temp3 = espr.list();
	    for (EstoqueProduto x : temp3) {
	    	if (prod.get(x.getProdutoEdicao().getId()) != null) {
	    		x.setQtdeDevolucaoFornecedor(prod.get(x.getProdutoEdicao().getId()));
	    		s.persist(x);
	    	}
	    }

	    Criteria lanc = s.createCriteria(Lancamento.class).add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	    List<Lancamento> temp2 = lanc.list();
	    for (Lancamento x : temp2) {
	    	x.setStatus(StatusLancamento.FECHADO);
	    	s.persist(x);
	    }
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean isEdicaoAberta(Long produtoEdicaoId) {
	    StringBuilder sql = new StringBuilder();
	    sql.append("select status ");
	    sql.append("  from lancamento ");
	    sql.append(" where produto_edicao_id = :produtoEdicaoId ");

	    Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("produtoEdicaoId", produtoEdicaoId);
	    List<String> lista = query.list();
	    if (lista.size() > 0 && (lista.get(0).equals("FECHADO") || lista.get(0).equals("RECOLHIDO"))) {
		return false;
	    }
	    return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterNumeroDas6UltimasEdicoesFechadas(Long idProduto){
	
		StringBuilder sql = new StringBuilder();


			sql.append(" select pe.NUMERO_EDICAO from produto_edicao pe left join produto p ON p.ID = pe.PRODUTO_ID ")
				.append(" LEFT join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ") 
				.append(" where p.id= :idProduto ")
				.append(" and l.STATUS='FECHADO' ")
				.append(" order by l.DATA_LCTO_PREVISTA desc ")
				.append(" limit 6  ");
		
	    Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("idProduto", idProduto);
	    
	    List<Long> lista = query.list();
	    return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> obterNumeroDas6UltimasEdicoesFechadasPorICD(String codigoICD){
		
		StringBuilder sql = new StringBuilder();


			sql.append(" select pe.NUMERO_EDICAO from produto_edicao pe left join produto p ON p.ID = pe.PRODUTO_ID ")
				.append(" LEFT join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ") 
				.append(" where p.codigo_icd= :idProduto ")
				.append(" and l.STATUS='FECHADO' ")
				.append(" order by l.DATA_LCTO_PREVISTA desc ")
				.append(" limit 6  ");
		
	    Query query = getSession().createSQLQuery(sql.toString());
	    query.setParameter("idProduto", codigoICD);
	    
	    List<Long> lista = query.list();
	    return lista;
	}
	
	public BigDecimal obterDescontoLogistica(Long idPropdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select pEdicao.descontoLogistica.percentualDesconto from ProdutoEdicao pEdicao ");
		hql.append(" where pEdicao.id = :idPropdutoEdicao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idPropdutoEdicao",idPropdutoEdicao);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemAutoComplete> obterPorCodigoBarraILike(String codigoBarra) {
		
		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		
		Query query = this.getSession().createQuery("select o.id as chave, o.codigoDeBarras as value, o.codigoDeBarras || ' -  ' || o.produto.nome || ' - Ed.:' || o.numeroEdicao as label from ProdutoEdicao o where o.codigoDeBarras like :codigoDeBarras");
		
		criteria.add(Restrictions.ilike("codigoDeBarras", codigoBarra,MatchMode.START));
		query.setString("codigoDeBarras", codigoBarra + "%");
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemAutoComplete.class));
		
		return query.list();
	}
	
	@Override
	public Boolean isEdicaoParcial(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select pEdicao.parcial from ProdutoEdicao pEdicao ");
		hql.append(" where pEdicao.id = :idPropdutoEdicao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idPropdutoEdicao",idProdutoEdicao);
		
		return (Boolean) query.uniqueResult();
		
	}
	
	public ProdutoEdicao obterMaxProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, Long numeroEdicao) {

		StringBuilder hql = new StringBuilder();
		   
		hql.append(" from ProdutoEdicao pe where pe.id =(select max(produtoEdicao.id) from ProdutoEdicao produtoEdicao "); 
		hql.append(" 												join produtoEdicao.produto produto ");
		hql.append("					 							where ");
		hql.append(" 												produto.codigo =:codigoProduto ");
		hql.append(" 												and produtoEdicao.numeroEdicao=:numeroEdicao ) ");	
					
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		
		return (ProdutoEdicao) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto, Integer numeroCota, Integer quantidadeRegisttros) {
		
		StringBuilder hql = new StringBuilder(" select produtoEdicao ");
		   
		hql.append(" from MovimentoEstoqueCota mec 			 	 	")
		
			.append(" inner join mec.tipoMovimento tipoMovimento	")

			.append(" inner join mec.cota cota					 	")
			
			.append(" inner join mec.produtoEdicao produtoEdicao 	")
			
			.append(" inner join produtoEdicao.produto produto		")
			
			.append(" inner join produtoEdicao.lancamentos lancamentos ")
			
			.append(" where 										")
			
			.append(" (produto.nome like :nomeProduto 				")
			
			.append(" or produto.codigo like :codigoProduto) 	and		")
			
			.append(" tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque and	")

			.append(" cota.numeroCota = :numeroCota ")
			
			.append(" and lancamentos.status in (:statusLancamento) ")
			
			.append(" group by produtoEdicao.id 					")
			
			.append(" order by produto.nome asc, 					")
			
			.append(" produtoEdicao.numeroEdicao desc 				");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("nomeProduto", "%" + codigoNomeProduto + "%");
		
		query.setParameter("codigoProduto", "%" + codigoNomeProduto + "%");
		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameterList("statusLancamento", Arrays.asList(StatusLancamento.BALANCEADO_RECOLHIMENTO, StatusLancamento.EM_RECOLHIMENTO));
		
		query.setMaxResults(quantidadeRegisttros);
		
		return query.list();
	}

}
