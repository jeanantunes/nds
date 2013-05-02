package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
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

	@Override
	public BigDecimal obterReparteDaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {

		Query query = 
				this.getSession().createSQLQuery(
						this.getSqlValor(conferido, postergado, REPARTE_COM_DESCONTO));

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
	
	@Override
	public BigDecimal obterTotalDescontoDaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao,
			Boolean conferido, Boolean postergado) {

		Query query = 
				this.getSession().createSQLQuery(
						this.getSqlValor(conferido, postergado, DESCONTO));

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
		StringBuffer subSqlWhereDesconto = new StringBuffer();
		
		subSqlWhereDesconto.append(" (SELECT ");
		
		if (REPARTE_COM_DESCONTO.equals(valor)){
			
			subSqlWhereDesconto.append("	MEC.PRECO_COM_DESCONTO ");
		} else if (DESCONTO.equals(valor)){
			
			subSqlWhereDesconto.append(" MEC.PRECO_VENDA - MEC.PRECO_COM_DESCONTO ");
		} else {
			
			subSqlWhereDesconto.append(" MEC.PRECO_VENDA ");
		}
		
		subSqlWhereDesconto.append(" FROM 	");
		subSqlWhereDesconto.append(" MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV	");
		subSqlWhereDesconto.append(" WHERE  	");
		subSqlWhereDesconto.append(" MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND 					");
		subSqlWhereDesconto.append(" MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND 				");
		subSqlWhereDesconto.append(" MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND ");
		subSqlWhereDesconto.append(" TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		subSqlWhereDesconto.append(" ORDER BY MEC.DATA DESC ");
		subSqlWhereDesconto.append(" LIMIT 1) ");

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT SUM( COALESCE( ");
		sql.append(subSqlWhereDesconto.toString());
		
		if (REPARTE_COM_DESCONTO.equals(valor)){
			
			sql.append(", PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		} else if (DESCONTO.equals(valor)){
			
			sql.append(", 0 ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		} else {
			
			sql.append(", PROD_EDICAO.PRECO_VENDA ) * CH_ENCALHE_COTA.QTDE_PREVISTA ");
		}
		
		sql.append(" ) ");
		
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
			Boolean conferido, Boolean postergado) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT SUM(PRODUTOS_DESCONTO.PRECO_COM_DESCONTO * PRODUTOS_DESCONTO.QTDE_PREVISTA) as precoTotalComDesconto ");
		
		sql.append(" FROM	");
		
		sql.append(" ( ");
		
		sql.append("  SELECT	");
		sql.append("  COALESCE(MEC.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA) AS PRECO_COM_DESCONTO, ");
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

		sql.append("	( CH_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataOperacaoDe AND  :dataOperacaoAte )  ");
		
		if(cotaId!=null) {
			sql.append(" AND COTA.ID = :cotaId ");
		}
		
		if(conferido!=null) {
			sql.append(" AND	CH_ENCALHE_COTA.FECHADO = :conferido		");
		}
		
		if(postergado!=null) {
			sql.append(" AND CH_ENCALHE_COTA.POSTERGADO = :postergado		");
		}

		sql.append("  GROUP BY PRECO_COM_DESCONTO, QTDE_PREVISTA ");
		
		sql.append(" ) AS PRODUTOS_DESCONTO ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(cotaId!=null) {
			parameters.put("cotaId", cotaId);
		}
		if(conferido!=null) {
			parameters.put("conferido", conferido);
		}
		
		if(postergado!=null) {
			parameters.put("postergado", postergado);
		}
		
		parameters.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		parameters.put("dataOperacaoDe", dataOperacaoDe);
		parameters.put("dataOperacaoAte", dataOperacaoAte);

		Map<String, Object> queryForMap = namedParameterJdbcTemplate.queryForMap(sql.toString(), parameters);
		Object precoTotalComDesconto = queryForMap.get("precoTotalComDesconto");

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

	@SuppressWarnings("unchecked")
	public List<ChamadaEncalheCota> obterListaChamaEncalheCota(Integer numeroCota,Long idProdutoEdicao,
															   boolean postergado,Date dataOperacao, Date... dataRecolhimento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalheCota ");

		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");

		hql.append(" where ");

		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");

		hql.append(" and chamadaEncalheCota.postergado = :postergado ");

		if (dataRecolhimento!= null && dataRecolhimento.length > 0) {
			hql.append(" and (chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
			hql.append(" or chamadaEncalheCota.chamadaEncalhe.dataRecolhimento IN (:dataRecolhimento ))");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}

		if (idProdutoEdicao != null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		query.setParameter("postergado", postergado);

		query.setParameter("dataOperacao", dataOperacao);
		
		if(dataRecolhimento!= null && dataRecolhimento.length > 0){
			query.setParameterList("dataRecolhimento", dataRecolhimento);
		}

		if (idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}

		return query.list();

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

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

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

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
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

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

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

		if (filtro.getCodMunicipio() != null) {
			param.put("codigoCidadeIBGE", filtro.getCodMunicipio());
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
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" JOIN pdv.enderecos enderecoPDV ")
				.append(" JOIN enderecoPDV.endereco endereco ");
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
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" AND endereco.codigoCidadeIBGE =:codigoCidadeIBGE ");
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
	public List<ChamadaEncalheCota> obterListChamadaEncalheCota(
			Long chamadaEncalheID, Long cotaID) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cec from ChamadaEncalheCota cec ");
		hql.append(" where cec.chamadaEncalhe.id = :chamadaEncalheID ");
		hql.append(" and cec.cota.id = :cotaID ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("chamadaEncalheID", chamadaEncalheID);
		query.setParameter("cotaID", cotaID);
		
		return query.list();
	}

}
