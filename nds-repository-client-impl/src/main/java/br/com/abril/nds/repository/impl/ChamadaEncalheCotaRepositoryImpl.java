package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;

@Repository
public class ChamadaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ChamadaEncalheCota, Long> implements
		ChamadaEncalheCotaRepository {
	
	private static final String REPARTE_COM_DESCONTO = "reparte";
	
	private static final String REPARTE_SEM_DESCONTO = "reparteSemDesconto";
	
	private static final String DESCONTO = "desconto";
	
	@Autowired
	private DataSource dataSource;

	public ChamadaEncalheCotaRepositoryImpl() {
		super(ChamadaEncalheCota.class);
	}
	
	public BigInteger quantidadeCotaAusenteFechamentoEncalhe(Integer numeroCota, Date dataRecolhimento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT COUNT(*) "); 
		sql.append(" FROM CHAMADA_ENCALHE ce, CHAMADA_ENCALHE_COTA cec, COTA c ");
		sql.append(" WHERE ce.ID = cec.CHAMADA_ENCALHE_ID ");
		sql.append(" AND cec.COTA_ID = c.ID ");
		sql.append(" AND ce.DATA_RECOLHIMENTO = :dataRecolhimento ");
		sql.append(" AND NUMERO_COTA = :numeroCota ");
		sql.append(" AND cec.FECHADO = :fechado " );

		Query query = getSession().createSQLQuery(sql.toString());

		query.setParameter("dataRecolhimento", dataRecolhimento);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("fechado", true);

		return (BigInteger) query.uniqueResult();
	}

	@Override
	public BigDecimal obterReparteDaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {

		Query query = this.getSession().createSQLQuery(this.getSqlValor(conferido, postergado, REPARTE_COM_DESCONTO));

		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido!=null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado!=null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameterList("grupoMovimentoEstoque", this.grupoMovimentoEstoqueCota());
		
		return (BigDecimal) query.uniqueResult();
	}
	
	private List<String> grupoMovimentoEstoqueCota(){
		
		return Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
				GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA.name(),
				GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
				GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
				GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
				GrupoMovimentoEstoque.SOBRA_EM_COTA.name());
	}
	
	@Override
	public BigDecimal obterTotalDescontoDaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {

		Query query = this.getSession().createSQLQuery(this.getSqlValor(conferido, postergado, DESCONTO));

		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido != null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado != null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterTotalDaChamaEncalheCotaSemDesconto(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {
		
		Query query = 
				this.getSession().createSQLQuery(
						this.getSqlValor(conferido, postergado, REPARTE_SEM_DESCONTO));

		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido!=null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado!=null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		return (BigDecimal) query.uniqueResult();
	}

	private String getSqlValor(Boolean conferido, Boolean postergado, String valor) {
		
		StringBuilder sql = new StringBuilder();
		
		StringBuilder subSelePrecoComDesconto = new StringBuilder();
		
		subSelePrecoComDesconto.append(" (SELECT ");
		subSelePrecoComDesconto.append("		 MEC_SUB.PRECO_COM_DESCONTO ");    
		subSelePrecoComDesconto.append("	FROM ");
		subSelePrecoComDesconto.append("		MOVIMENTO_ESTOQUE_COTA MEC_SUB, ");
		subSelePrecoComDesconto.append("		TIPO_MOVIMENTO TIPO_MOV ");
		subSelePrecoComDesconto.append(" WHERE ");
		subSelePrecoComDesconto.append("		MEC_SUB.COTA_ID = CH_ENCALHE_COTA.COTA_ID ");   
		subSelePrecoComDesconto.append("		AND     MEC_SUB.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");   
		subSelePrecoComDesconto.append("		AND  MEC_SUB.TIPO_MOVIMENTO_ID = TIPO_MOV.ID    ");
		subSelePrecoComDesconto.append("		AND     TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE IN( :grupoMovimentoEstoque ) ");
		subSelePrecoComDesconto.append("	order by MEC_SUB.DATA desc limit 1) ");
		
		StringBuilder subSelectDataMovimento = new StringBuilder();
		
		subSelectDataMovimento.append(" (SELECT ");
		subSelectDataMovimento.append("		 MAX(MEC_SUB.DATA) ");    
		subSelectDataMovimento.append("	FROM ");
		subSelectDataMovimento.append("		MOVIMENTO_ESTOQUE_COTA MEC_SUB, ");
		subSelectDataMovimento.append("		TIPO_MOVIMENTO TIPO_MOV ");
		subSelectDataMovimento.append(" WHERE ");
		subSelectDataMovimento.append("		MEC_SUB.COTA_ID = CH_ENCALHE_COTA.COTA_ID ");   
		subSelectDataMovimento.append("		AND     MEC_SUB.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");   
		subSelectDataMovimento.append("		AND  MEC_SUB.TIPO_MOVIMENTO_ID = TIPO_MOV.ID    ");
		subSelectDataMovimento.append("		AND     TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE IN( :grupoMovimentoEstoque ) )");
		
		StringBuilder sqlValor = new StringBuilder();
		
        if (REPARTE_COM_DESCONTO.equals(valor)){
			
        	sqlValor.append(subSelePrecoComDesconto).append(", PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		} else if (DESCONTO.equals(valor)){
			
			sqlValor.append("  MEC_SUB.PRECO_VENDA - ").append(subSelePrecoComDesconto).append(", 0 ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		} else {
			
			sqlValor.append(" MEC_SUB.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		}
	
		sql.append(" SELECT ");
		
		sql.append(" SUM( ");
		
		sql.append("     ( ");
		
		sql.append("      CASE WHEN COTA.TIPO_COTA = 'A_VISTA' THEN CASE WHEN COTA.ALTERACAO_TIPO_COTA >= ").append(subSelectDataMovimento).append(" THEN ");
		
        sql.append("         ( COALESCE( " +sqlValor+ " ) ");
		
		sql.append("      ELSE 0 END ELSE ");
		
		sql.append("         ( COALESCE( " +sqlValor+ " ) ");
		
		sql.append("      END ");
		
		sql.append("     ) ");
		
		sql.append("    ) ");
		
		sql.append("    FROM    ");
		
		sql.append("    CHAMADA_ENCALHE_COTA AS CH_ENCALHE_COTA 				");
		sql.append("	inner join COTA AS COTA ON 								");
		sql.append("	(COTA.ID = CH_ENCALHE_COTA.COTA_ID)						");
		sql.append("	inner join CHAMADA_ENCALHE AS CH_ENCALHE ON 			");
		sql.append("	(CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)	");
		sql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON 			");
		sql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)			");
		sql.append("	inner join PRODUTO as PROD ON 							");
		sql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)						");
		
		sql.append("	WHERE   ");
		
		sql.append("	COTA.NUMERO_COTA = :numeroCota  ");
		
		sql.append("	AND CH_ENCALHE.DATA_RECOLHIMENTO = :dataOperacao	");
		
		if(conferido!=null) {
			sql.append(" AND	CH_ENCALHE_COTA.FECHADO = :conferido		");
		}
		
		if(postergado!=null) {
			sql.append(" AND CH_ENCALHE_COTA.POSTERGADO = :postergado		");
		}

		return sql.toString();
	}

	public BigDecimal obterReparteDaChamaEncalheCotaNoPeriodo(
			Long cotaId, 
			Date dataOperacaoDe,
			Date dataOperacaoAte,
			Boolean postergado) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT SUM(PRODUTOS_DESCONTO.PRECO_COM_DESCONTO * PRODUTOS_DESCONTO.QTDE_PREVISTA) as precoTotalComDesconto ");
		
		sql.append(" FROM	");
		
		sql.append(" ( ");
		
		sql.append("  SELECT	");
		sql.append("  COALESCE(MEC.PRECO_COM_DESCONTO, MEC.PRECO_VENDA) AS PRECO_COM_DESCONTO, ");
		sql.append("  CH_ENCALHE_COTA.QTDE_PREVISTA AS QTDE_PREVISTA  ");
		sql.append("    FROM    ");
		sql.append("    CHAMADA_ENCALHE_COTA AS CH_ENCALHE_COTA 				");
		
		sql.append("	inner join COTA AS COTA ON 								");
		sql.append("	(COTA.ID = CH_ENCALHE_COTA.COTA_ID)						");
		
		sql.append("	inner join CHAMADA_ENCALHE AS CH_ENCALHE ON 			");
		sql.append("	(CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)	");
		
		sql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON 			");
		sql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)			");
		
		sql.append("	inner join PRODUTO as PROD ON 							");
		sql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)						");
		
		sql.append("	left join CHAMADA_ENCALHE_LANCAMENTO as CEL ON 	");
		sql.append("	(CEL.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)		");

		sql.append("	left join LANCAMENTO as LANCAMENTO ON 		");
		sql.append("	(CEL.LANCAMENTO_ID = LANCAMENTO.ID)			");
		
		sql.append("	left join MOVIMENTO_ESTOQUE_COTA as MEC ON 	( ");
		sql.append("		MEC.LANCAMENTO_ID = LANCAMENTO.ID AND ");
		sql.append("		MEC.COTA_ID = COTA.ID AND ");
		sql.append("		MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		sql.append("	)														");
		
		sql.append("	inner join TIPO_MOVIMENTO ON (	");
		sql.append("	MEC.TIPO_MOVIMENTO_ID = TIPO_MOVIMENTO.ID AND	");
		sql.append("	TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque	");
		sql.append(" 	) ");
		
		sql.append("	WHERE   ");

		sql.append("	( CH_ENCALHE.DATA_RECOLHIMENTO >= :dataOperacaoDe AND  CH_ENCALHE.DATA_RECOLHIMENTO <= :dataOperacaoAte )  ");
		
		sql.append(" AND CH_ENCALHE_COTA.QTDE_PREVISTA > 0 ");
		
		sql.append(" AND MEC.DATA = ");
		
		sql.append(" ( SELECT MAX(MV.DATA) FROM MOVIMENTO_ESTOQUE_COTA MV WHERE MV.LANCAMENTO_ID = LANCAMENTO.ID AND MV.ID = MEC.ID ) ");
		
		if(cotaId!=null) {
			sql.append(" AND COTA.ID = :cotaId ");
		}
		
		if(postergado!=null) {
			sql.append(" AND CH_ENCALHE_COTA.POSTERGADO = :postergado		");
		}

		sql.append("  GROUP BY CH_ENCALHE.ID ");
		
		sql.append(" ) AS PRODUTOS_DESCONTO ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(cotaId!=null) {
			parameters.put("cotaId", cotaId);
		}

		if(postergado!=null) {
			parameters.put("postergado", postergado);
		}
		
		parameters.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		parameters.put("dataOperacaoDe", dataOperacaoDe);
		parameters.put("dataOperacaoAte", dataOperacaoAte);

		Object precoTotalComDesconto = namedParameterJdbcTemplate.queryForMap(sql.toString(), parameters).get("precoTotalComDesconto");

		return (BigDecimal) (precoTotalComDesconto == null ? BigDecimal.ZERO : precoTotalComDesconto);
	}
	
	@SuppressWarnings("unchecked")
	public List<ChamadaEncalheCota> obterListaChamadaEncalheCota(Long idCota, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cec from ChamadaEncalheCota cec ");
		
		hql.append(" where cec.cota.id = :idCota and ");
		
		hql.append(" cec.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");

		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return query.list();
		
	}
	
	public Long obterQtdListaChamaEncalheCota(Integer numeroCota,
			Date dataOperacao, Long idProdutoEdicao,
			boolean indPesquisaCEFutura, boolean conferido, boolean postergado) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select count(chamadaEncalheCota.id) ");

		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");

		hql.append(" where ");

		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");

		hql.append(" and chamadaEncalheCota.fechado = :conferido ");

		hql.append(" and chamadaEncalheCota.postergado = :postergado ");

		if (indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}

		if (idProdutoEdicao != null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		query.setParameter("conferido", conferido);

		query.setParameter("dataOperacao", dataOperacao);

		query.setParameter("postergado", postergado);

		if (idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}

		Long qtde = (Long) query.uniqueResult();

		return (qtde == null) ? 0 : qtde;

	}

	public ChamadaEncalheCota obterUltimaChamaEncalheCota(Cota cota,Long idProdutoEdicao,
														 boolean postergado,Date dataOperacao) {

		return obterUltimaChamadaEncalheCota(cota, idProdutoEdicao,postergado, null);

	}

	private ChamadaEncalheCota obterUltimaChamadaEncalheCota(Cota cota, Long idProdutoEdicao, boolean postergado,
															 Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalheCota ");

		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");

		hql.append(" where ");

		hql.append(" chamadaEncalheCota.cota.id = :idCota ");

		hql.append(" and chamadaEncalheCota.postergado = :postergado ");
		
		if(dataOperacao!= null){
			
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento  = :dataOperacao ");
		}
		
		if (idProdutoEdicao != null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}

		hql.append(" order by chamadaEncalheCota.chamadaEncalhe.dataRecolhimento desc ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setMaxResults(1);
		
		query.setParameter("idCota", cota.getId());

		query.setParameter("postergado", postergado);

		if(dataOperacao!= null){
			query.setParameter("dataOperacao", dataOperacao);
		}
		
		if (idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		return (ChamadaEncalheCota) query.uniqueResult();
	}
	
	public ChamadaEncalheCota obterChamadaEncalheCotaNaData(Cota cota, Long idProdutoEdicao,
			 													 boolean postergado, Date dataOperacao) {
		
		return obterUltimaChamadaEncalheCota(cota, idProdutoEdicao, postergado, dataOperacao);
	}
	

	public ChamadaEncalheCota buscarPorChamadaEncalheECota(
			Long idChamadaEncalhe, Long idCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select o from ChamadaEncalheCota o ")
				.append(" WHERE o.cota.id =:idCota ")
				.append(" AND o.chamadaEncalhe.id=:idChamadaEncalhe ");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idChamadaEncalhe", idChamadaEncalhe);
		query.setParameter("idCota", idCota);

		return (ChamadaEncalheCota) query.uniqueResult();

	}

	@Override
	public BigDecimal obterQntExemplaresComProgramacaoAntecipadaEncalheCota(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append(" SELECT DISTINCT chamadaEncalheCota.qtdePrevista ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		setParameters(query, param);

		query.setMaxResults(1);

		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public Long obterQntCotasProgramadaParaAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT count ( cota.id ) ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		setParameters(query, param);
		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ChamadaAntecipadaEncalheDTO> obterCotasProgramadaParaAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT new ")
				.append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
				.append(" (box.codigo, ")
				.append(" box.nome, ")
				.append(" cota.numeroCota, ")
				.append(" chamadaEncalheCota.qtdePrevista,")
				.append(" lancamento.id, ")
				.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
				.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
				.append(" else null end ,").append(" chamadaEncalheCota.id ")
				.append(" ) ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		hql.append(getOrderByCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		setParameters(query, param);

		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	private HashMap<String, Object> getParametrosCotasProgramadaParaAntecipacaoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("codigoProduto", filtro.getCodigoProduto());
		param.put("numeroEdicao", filtro.getNumeroEdicao());
		param.put("tipoChamadaEncalhe", TipoChamadaEncalhe.ANTECIPADA);
		param.put("dataOperacao", filtro.getDataOperacao());

		if (filtro.getNumeroCota() != null) {
			param.put("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getFornecedor() != null) {
			param.put("fornecedor", filtro.getFornecedor());
		}

		if (filtro.getBox() != null) {
			param.put("box", filtro.getBox());
		}

		if (filtro.getRota() != null) {
			param.put("rota", filtro.getRota());
		}

		if (filtro.getRoteiro() != null) {
			param.put("roteiro", filtro.getRoteiro());
		}

		if (filtro.getDescMunicipio() != null) {
			param.put("cidade", filtro.getDescMunicipio());
		}

		if (filtro.getCodTipoPontoPDV() != null) {
			param.put("codigoTipoPontoPDV", filtro.getCodTipoPontoPDV());
		}

		return param;
	}

	private Object getOrderByCotasProgramadaParaAntecipacaoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
		case BOX:
			hql.append(" order by box.codigo ");
			break;
		case NOME_COTA:
			hql.append(" order by   ")
					.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
					.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
					.append(" else null end ");
			break;
		case NUMERO_COTA:
			hql.append(" order by cota.numeroCota ");
			break;
		case QNT_EXEMPLARES:
			hql.append(" order by  chamadaEncalheCota.qtdePrevista ");
			break;
		default:
			hql.append(" order by  box.codigo, cota.numeroCota ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

	private Object getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("  FROM ChamadaEncalheCota chamadaEncalheCota ")
			.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			.append(" JOIN chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" JOIN produtoEdicao.lancamentos lancamento ")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN chamadaEncalheCota.cota cota ")
			.append(" JOIN cota.pessoa pessoa ")
			.append(" JOIN cota.box box ")
			.append(" JOIN cota.pdvs pdv ")
			.append(" LEFT JOIN pdv.rotas rotaPdv  ")
			.append(" LEFT JOIN rotaPdv.rota rota  ")
			.append(" LEFT JOIN rota.roteiro roteiro ");

			
		if (filtro.getFornecedor() != null) {
			hql.append(" JOIN produto.fornecedores fornecedor ");
		}
		
		if(filtro.getDescMunicipio()!= null){
			hql.append(" JOIN cota.enderecos enderecoCota ")
				.append(" JOIN enderecoCota.endereco endereco ");
		}
			
		hql.append(" WHERE ")
			.append(" chamadaEncalhe.tipoChamadaEncalhe=:tipoChamadaEncalhe")
			.append(" AND produto.codigo =:codigoProduto ")
			.append(" AND produtoEdicao.numeroEdicao =:numeroEdicao ")
			.append(" AND chamadaEncalheCota.fechado = false ")
			.append(" AND chamadaEncalheCota.postergado = false ")
			.append(" AND pdv.caracteristicas.pontoPrincipal = true ")
			.append(" AND chamadaEncalhe.dataRecolhimento > :dataOperacao ");
		
		if (filtro.getNumeroCota() != null) {

			hql.append(" AND cota.numeroCota =:numeroCota ");
		}

		if (filtro.getFornecedor() != null) {

			hql.append(" AND fornecedor.id =:fornecedor ");
		}

		if (filtro.getBox() != null) {

			hql.append(" AND box.id =:box ");
		}
		
		if(filtro.getRota()!= null){
			hql.append(" AND rota.id =:rota ");
		}
		
		if(filtro.getRoteiro()!= null ){
			hql.append(" AND roteiro.id =:roteiro ");
		}
		
		if(filtro.getDescMunicipio()!= null){
			hql.append(" AND endereco.cidade =:cidade ");
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			hql.append(" AND pdv.segmentacao.tipoPontoPDV.codigo =:codigoTipoPontoPDV ");
		}
		
		return hql;
	}

	@Override
	public Long obterQntChamadaEncalheCota(Long idChamadaEncalhe) {

		String hql = " select count(chamadaEncalheCota.id) from ChamadaEncalheCota chamadaEncalheCota where chamadaEncalheCota.chamadaEncalhe.id=:idChamada ";

		Query query = getSession().createQuery(hql);

		query.setParameter("idChamada", idChamadaEncalhe);

		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChamadaEncalheCota> obterListChamadaEncalheCota(Long cotaID, Date dataRecolhimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cec from ChamadaEncalheCota cec ");
		hql.append(" inner join cec.chamadaEncalhe ce ");
		hql.append(" where cec.cota.id = :cotaID ");
		hql.append(" and ce.dataRecolhimento = :dataRecolhimento ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("cotaID", cotaID);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		return query.list();
	}
	
	@Override
	public Boolean existeChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Boolean fechado, Date dataRecolhimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when(count(cec) > 0) then true else false end ");
		
		hql.append(" from ChamadaEncalheCota cec ");
		
		hql.append(" join cec.chamadaEncalhe chamadaEncalhe ");
		
		hql.append(" where cec.cota.id = :idCota ");
		
		hql.append(" and chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		
		if (fechado != null) {
			hql.append(" and cec.fechado = :fechado ");
		}
		
		if (dataRecolhimento != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento >= :dataRecolhimento ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		if (fechado != null) {
			query.setParameter("fechado", fechado);
		}
		
		if (dataRecolhimento != null) {
			query.setParameter("dataRecolhimento", dataRecolhimento);
		}
		
		return (Boolean) query.uniqueResult();
	}
	
	@Override
	public Boolean existeChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Date dataRecolhimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when(count(cec) > 0) then true else false end ");
		
		hql.append(" from ChamadaEncalheCota cec ");
		
		hql.append(" join cec.chamadaEncalhe chamadaEncalhe ");
		
		hql.append(" where cec.cota.id = :idCota ");
		
		hql.append(" and chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		
		if (dataRecolhimento != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento = :dataRecolhimento ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		if (dataRecolhimento != null) {
			query.setParameter("dataRecolhimento", dataRecolhimento);
		}
		
		return (Boolean) query.uniqueResult();
	}
	
	public ChamadaEncalheCota obterChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Date dataRecolhimento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalheCota from ChamadaEncalheCota chamadaEncalheCota ")
			.append(" join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			.append(" WHERE chamadaEncalheCota.cota.id = :idCota ")
			.append(" AND chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ")
			.append(" and chamadaEncalhe.dataRecolhimento >= :dataRecolhimento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataRecolhimento", dataRecolhimento);

		query.setMaxResults(1);
		
		return (ChamadaEncalheCota) query.uniqueResult();
	}
	
	public void fecharChamadasEncalheDaCota(Long idCota, Date data) {
		
		
	}
	
}
