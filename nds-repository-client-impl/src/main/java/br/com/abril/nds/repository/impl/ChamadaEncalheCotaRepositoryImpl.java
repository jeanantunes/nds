package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
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
			List<Date> datas,
			Boolean conferido, Boolean postergado) {

		Query query = this.getSession().createSQLQuery(this.getSqlValor(conferido, postergado, REPARTE_COM_DESCONTO));

		query.setParameterList("datas", datas);
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido!=null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado!=null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameter("grupoMovimentoEnvioReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		return (BigDecimal) query.uniqueResult();
	}
	
	
	@Override
	public BigDecimal obterTotalDescontoDaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {

		Query query = this.getSession().createSQLQuery(this.getSqlValor(conferido, postergado, DESCONTO));

		query.setParameterList("datas", Arrays.asList(dataOperacao));
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido != null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado != null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameter("grupoMovimentoEnvioReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
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

		query.setParameterList("datas", Arrays.asList(dataOperacao));
		
		query.setParameter("numeroCota", numeroCota);
		
		if(conferido!=null) {
			query.setParameter("conferido", conferido);
		}
		
		if(postergado!=null) {
			query.setParameter("postergado", postergado);
		}
		
		query.setParameter("grupoMovimentoEnvioReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		return (BigDecimal) query.uniqueResult();
	}

	private String getSqlValor(Boolean conferido, Boolean postergado, String valor) {
		
		StringBuilder sql = new StringBuilder();
		
		StringBuilder sqlValor = new StringBuilder();
		
        if (REPARTE_COM_DESCONTO.equals(valor)){
        	sqlValor.append(" COALESCE( PRECOS_DE_REPARTE.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
        	
		} else if (DESCONTO.equals(valor)){
			sqlValor.append(" COALESCE( PRECOS_DE_REPARTE.VALOR_DESCONTO, 0 ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
			
		} else {
			sqlValor.append(" COALESCE ( PRECOS_DE_REPARTE.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		}
	
		sql.append(" SELECT ");
		
		sql.append(" SUM( ");
		
		sql.append("     ( ");
		
		sql.append("      CASE WHEN (COTA.TIPO_COTA = 'A_VISTA' AND COTA.DEVOLVE_ENCALHE = false) THEN ");
		
        sql.append("      0 ELSE ");
		
		sql.append("         ( " +sqlValor+ " ) ");
		
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
		
		
		sql.append(" LEFT JOIN ( ");
		
		// QUERY ABAIXO QUE OBTEM UMA LISTA DOS PRODUTOS EDICAO E                                                           
		// O VALOR DE PRECO COM DESCONTO MAIS ATUAL DESTE APLICADO NO MOV. EST. COTA DE REPARTE.                     
		
		sql.append(" 		SELECT ");
		
		sql.append(" 				MEC.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID,                  ");
		sql.append(" 				MEC.PRECO_COM_DESCONTO AS PRECO_COM_DESCONTO,                ");
		sql.append(" 				MEC.PRECO_VENDA AS PRECO_VENDA,                              ");
		sql.append(" 				(MEC.PRECO_VENDA - MEC.PRECO_COM_DESCONTO) AS VALOR_DESCONTO ");
		
		sql.append(" 		FROM                                                                                                         ");
		sql.append(" 				MOVIMENTO_ESTOQUE_COTA MEC                                                                           ");
		sql.append(" 		INNER JOIN (	SELECT                                                                                       ");
		sql.append(" 							MEC.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID,                                              ");
		sql.append(" 							MEC.COTA_ID, MAX(MEC.DATA) AS DATA                                                       ");
		sql.append(" 						FROM                                                                                         ");
		sql.append(" 							CHAMADA_ENCALHE_COTA                                                                     ");
		sql.append(" 						INNER JOIN CHAMADA_ENCALHE ON (CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID) ");
		sql.append(" 						INNER JOIN MOVIMENTO_ESTOQUE_COTA MEC ON (                                                   ");
		sql.append(" 								CHAMADA_ENCALHE_COTA.COTA_ID = MEC.COTA_ID AND                                       ");
		sql.append(" 								CHAMADA_ENCALHE.PRODUTO_EDICAO_ID = MEC.PRODUTO_EDICAO_ID)                           ");
		
		sql.append(" 						INNER JOIN TIPO_MOVIMENTO ON ( TIPO_MOVIMENTO.ID = MEC.TIPO_MOVIMENTO_ID AND ");
		sql.append(" 						TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEnvioReparte ) ");
		
		sql.append(" 						WHERE ");
		sql.append(" 							CHAMADA_ENCALHE_COTA.COTA_ID = (SELECT ID FROM COTA WHERE NUMERO_COTA = :numeroCota) AND ");
		sql.append(" 							CHAMADA_ENCALHE.DATA_RECOLHIMENTO IN (:datas) AND   ");
		sql.append(" 							CHAMADA_ENCALHE_COTA.FECHADO = :conferido AND 		");
		sql.append(" 							CHAMADA_ENCALHE_COTA.POSTERGADO = :postergado 		");
		sql.append(" 						GROUP BY                                                                                     ");
		sql.append(" 							MEC.PRODUTO_EDICAO_ID                                                                    ");
		sql.append(" 					) AS MOVCOTA                                                                                     ");
		sql.append(" 		ON (                                                                                                         ");
		sql.append(" 			MEC.PRODUTO_EDICAO_ID = MOVCOTA.PRODUTO_EDICAO_ID AND                                                    ");
		sql.append(" 			MEC.DATA = MOVCOTA.DATA AND MEC.COTA_ID = MOVCOTA.COTA_ID ");
		sql.append(" 		)                                                                                                            ");
		sql.append(" 		INNER JOIN TIPO_MOVIMENTO ON ( TIPO_MOVIMENTO.ID = MEC.TIPO_MOVIMENTO_ID AND ");
		sql.append(" 		TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEnvioReparte ) ");
		sql.append(" 		GROUP BY MEC.PRODUTO_EDICAO_ID                                                                               ");
		sql.append(" 	)                                                                                                                ");
		sql.append(" AS PRECOS_DE_REPARTE ON (CH_ENCALHE.PRODUTO_EDICAO_ID = PRECOS_DE_REPARTE.PRODUTO_EDICAO_ID)                        ");
		
		sql.append("	WHERE   ");
		
		sql.append("	COTA.NUMERO_COTA = :numeroCota  ");
		
		sql.append("	AND CH_ENCALHE.DATA_RECOLHIMENTO IN (:datas)	");
		
		if(conferido!=null) {
			sql.append(" AND	CH_ENCALHE_COTA.FECHADO = :conferido		");
		}
		
		if(postergado!=null) {
			sql.append(" AND CH_ENCALHE_COTA.POSTERGADO = :postergado		");
		}

		return sql.toString();
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
	
	public Long obterIdChamadaEncalheCotaNaData(Long idCota, Long idProdutoEdicao, Date dataRecolhimento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select CEC.ID as idChamadaEncalheCota ");
		
		sql.append(" from CHAMADA_ENCALHE_COTA CEC ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE AS CE ON (CEC.CHAMADA_ENCALHE_ID = CE.ID) ");
		
		sql.append(" where");
		
		sql.append(" CE.DATA_RECOLHIMENTO = :dataRecolhimento AND ");
		sql.append(" CEC.COTA_ID = :idCota AND ");
		sql.append(" CE.PRODUTO_EDICAO_ID = :idProdutoEdicao AND ");
		sql.append(" CEC.POSTERGADO = false ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("idChamadaEncalheCota", StandardBasicTypes.LONG);
		
		query.setParameter("idCota", idCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		return (Long) query.uniqueResult();
		
	}


	
	@Override
	public Date obterDataChamadaEncalheCotaProximaDataOperacao(
			Cota cota, Long idProdutoEdicao, boolean postergado,
			Date dataOperacao) {

		StringBuffer sqlCEAnteriorDataOperacao = new StringBuffer("");
		
		sqlCEAnteriorDataOperacao.append(" ( SELECT	");
		sqlCEAnteriorDataOperacao.append(" MAX(CE.DATA_RECOLHIMENTO) AS DATAENCALHE	");
		sqlCEAnteriorDataOperacao.append(" FROM CHAMADA_ENCALHE_COTA CEC            ");
		sqlCEAnteriorDataOperacao.append(" INNER JOIN CHAMADA_ENCALHE CE ON (CE.ID = CEC.CHAMADA_ENCALHE_ID)   ");
		sqlCEAnteriorDataOperacao.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = CE.PRODUTO_EDICAO_ID)      ");
		sqlCEAnteriorDataOperacao.append(" INNER JOIN PRODUTO P ON (P.ID = PE.PRODUTO_ID) ");
		sqlCEAnteriorDataOperacao.append(" WHERE ");
		sqlCEAnteriorDataOperacao.append(" PE.ID = :idProdutoEdicao AND		");
		sqlCEAnteriorDataOperacao.append(" CEC.COTA_ID = :idCota AND		");
		sqlCEAnteriorDataOperacao.append(" CEC.POSTERGADO = :postergado AND			");
		sqlCEAnteriorDataOperacao.append(" CE.DATA_RECOLHIMENTO <= :dataOperacao 	");
		sqlCEAnteriorDataOperacao.append(" ORDER BY CE.DATA_RECOLHIMENTO DESC )		");
		
		StringBuffer sqlCEPosteriorDataOperacao = new StringBuffer("");
		
		sqlCEPosteriorDataOperacao.append(" ( SELECT ");
		sqlCEPosteriorDataOperacao.append(" MIN(CE.DATA_RECOLHIMENTO) AS DATAENCALHE ");
		sqlCEPosteriorDataOperacao.append(" FROM CHAMADA_ENCALHE_COTA CEC			 ");
		sqlCEPosteriorDataOperacao.append(" INNER JOIN CHAMADA_ENCALHE CE ON (CE.ID = CEC.CHAMADA_ENCALHE_ID)   ");
		sqlCEPosteriorDataOperacao.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = CE.PRODUTO_EDICAO_ID)      ");
		sqlCEPosteriorDataOperacao.append(" INNER JOIN PRODUTO P ON (P.ID = PE.PRODUTO_ID) ");
		sqlCEPosteriorDataOperacao.append(" WHERE ");
		sqlCEPosteriorDataOperacao.append(" PE.ID = :idProdutoEdicao AND	");
		sqlCEPosteriorDataOperacao.append(" CEC.COTA_ID = :idCota AND		");
		sqlCEPosteriorDataOperacao.append(" CEC.POSTERGADO = :postergado AND		");
		sqlCEPosteriorDataOperacao.append(" CE.DATA_RECOLHIMENTO > :dataOperacao	");
		sqlCEPosteriorDataOperacao.append(" ORDER BY CE.DATA_RECOLHIMENTO DESC ) 	");
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append("	SELECT MIN(DATAENCALHE) AS dataEncalhe	");
		sql.append("	FROM	");
		sql.append("	(	");
		sql.append(sqlCEAnteriorDataOperacao);
		sql.append("	UNION ALL	");
		sql.append(sqlCEPosteriorDataOperacao);
		sql.append("	) AS dataEncalheProximaDataOperacao ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("dataEncalhe",StandardBasicTypes.DATE);
		
		query.setParameter("idCota", cota.getId());
		query.setParameter("postergado", postergado);
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (Date) query.uniqueResult();
		
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
		hql.append("SELECT  count (DISTINCT cota.id ) ");

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
				.append(" else null end,")
				.append(" chamadaEncalheCota.id,")
				.append(" chamadaEncalhe.tipoChamadaEncalhe, ")
				.append(" chamadaEncalhe.dataRecolhimento, ")
				.append(" lancamento.dataRecolhimentoDistribuidor, ")
				.append(" lancamento.dataRecolhimentoPrevista ")
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
	
	public Long quantidadeChamadasEncalheParaCota(Long idCota, Date periodoInicial, Date periodoFinal){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(chamadas.data) as quantidade from ( ")
		.append(" select chamadaEncalhe.DATA_RECOLHIMENTO as data ")
		.append(" from chamada_encalhe_cota chamadaCota ")
		.append(" join chamada_encalhe chamadaEncalhe on chamadaEncalhe.ID = chamadaCota.CHAMADA_ENCALHE_ID ") 
		.append(" where chamadaCota.COTA_ID =:idCota ")
		.append(" and chamadaEncalhe.DATA_RECOLHIMENTO  between :dataInicio and :dataFim ") 
		.append(" group by chamadaEncalhe.DATA_RECOLHIMENTO) as chamadas "); 
		
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataInicio", periodoInicial);
		query.setParameter("dataFim", periodoFinal);
		query.setParameter("idCota", idCota);
		
		query.addScalar("quantidade",StandardBasicTypes.LONG);
		
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Remove Chamadas de Encalhe de Cota por lista de ID da chamada de encalhe
	 * 
	 * @param ids
	 */
	@Override
	public void removerChamadaEncalheCotaPorIdsChamadaEncalhe(List<Long> ids) {
		
        String hql  = "DELETE FROM ChamadaEncalheCota WHERE chamadaEncalhe.id in (:ids)" ;
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("ids", ids);	
		
		query.executeUpdate();
		
		getSession().flush();
		
	}
}
