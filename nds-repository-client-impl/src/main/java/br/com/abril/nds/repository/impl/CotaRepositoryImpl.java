package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.envio.nota.StatusNotaEnvio;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class CotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements CotaRepository {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CotaRepositoryImpl.class);

	@Value("#{queries.suspensaoCota}")
	public String querySuspensaoCota;

	@Value("#{queries.countSuspensaoCota}")
	public String queryCountSuspensaoCota;
	

	/**
	 * Construtor.
	 */
	public CotaRepositoryImpl() {

		super(Cota.class);
	}

	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		
		Query query = 
				this.getSession().createQuery(
						"select c from Cota c where c.numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		query.setCacheable(true);
		
		return (Cota) query.uniqueResult();
	}
	
	public Cota obterPorNumerDaCota(Integer numeroCota) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		criteria.add(Restrictions.eq("situacaoCadastro", SituacaoCadastro.ATIVO));

		criteria.setMaxResults(1);

		return (Cota) criteria.uniqueResult();
	}
	
	public Cota obterPorPDV(Long idPDV) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pdvs", "pdv");
		
		criteria.add(Restrictions.eq("pdv.id", idPDV));
		
		return (Cota) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorNomePessoa(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.createAlias("pessoa", "pessoa");

		criteria.add(Restrictions.or(Restrictions.ilike("pessoa.nome", nome,
				MatchMode.ANYWHERE), Restrictions.ilike("pessoa.razaoSocial",
				nome, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterPorNome(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.createAlias("pessoa", "pessoa");

		criteria.add(Restrictions.or(Restrictions.like("pessoa.nome", nome, MatchMode.ANYWHERE),
				Restrictions.like("pessoa.razaoSocial", nome, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(
				" select enderecoCota.id as id, enderecoCota.endereco as endereco, ")
				.append(" enderecoCota.principal as enderecoPrincipal, ")
				.append(" enderecoCota.tipoEndereco as tipoEndereco ")
				.append(" from EnderecoCota enderecoCota ")
				.append(" where enderecoCota.cota.id = :idCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = null; 
		
		try {
		    resultTransformer = new AliasToBeanConstructorResultTransformer(
		            EnderecoAssociacaoDTO.class.getConstructor(Long.class, Endereco.class, boolean.class, TipoEndereco.class));
		} catch (Exception e) {
            String message = "Erro criando result transformer para classe: "
                    + EnderecoAssociacaoDTO.class.getName();
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }

		query.setResultTransformer(resultTransformer);

		query.setParameter("idCota", idCota);

		return query.list();
	}
	
	
	@Override
	public TelefoneCota obterTelefonePorTelefoneCota(Long idTelefone, Long idCota){
		
		StringBuilder hql = new StringBuilder("select t from TelefoneCota t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.cota.id   = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (TelefoneCota) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder,
			String sortColumn, Integer inicio, Integer rp, Date dataOperacao) {

		StringBuilder hqlConsignado = new StringBuilder();
	
		hqlConsignado.append(" SELECT SUM(")
					 .append(" MOVIMENTOCOTA.PRECO_COM_DESCONTO ")
					 .append(" *(CASE WHEN TIPOMOVIMENTO.OPERACAO_ESTOQUE='ENTRADA' THEN MOVIMENTOCOTA.QTDE ELSE MOVIMENTOCOTA.QTDE * -1 END)) ")
					 .append(" FROM MOVIMENTO_ESTOQUE_COTA MOVIMENTOCOTA ")
					 .append(" JOIN LANCAMENTO LCTO on (MOVIMENTOCOTA.LANCAMENTO_ID=LCTO.ID AND LCTO.STATUS <> :statusRecolhido) ")
					 .append(" JOIN PRODUTO_EDICAO PRODEDICAO ON(MOVIMENTOCOTA.PRODUTO_EDICAO_ID=PRODEDICAO.ID)  ")
					 .append(" JOIN TIPO_MOVIMENTO TIPOMOVIMENTO ON(MOVIMENTOCOTA.TIPO_MOVIMENTO_ID = TIPOMOVIMENTO.ID)  ")
					 .append(" WHERE MOVIMENTOCOTA.COTA_ID = COTA_.ID  ")
					 .append(" AND MOVIMENTOCOTA.DATA <= :dataOperacao ")
					 .append(" AND (MOVIMENTOCOTA.STATUS_ESTOQUE_FINANCEIRO IS NULL ")
					 .append(" OR MOVIMENTOCOTA.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro) ")
					 .append(" AND TIPOMOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE != :tipoMovimentoEstorno ")
					 .append(" AND MOVIMENTOCOTA.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
		
		StringBuilder hqlDividaAcumulada = new StringBuilder();
		hqlDividaAcumulada.append(" SELECT SUM(round(COALESCE(D.VALOR,0), 2)) ")
						  .append("	FROM DIVIDA D ")
						  .append(" JOIN COBRANCA c on (c.DIVIDA_ID=d.ID) ")
						  .append("	WHERE D.COTA_ID = COTA_.ID ")
						  .append(" AND c.DT_PAGAMENTO is null ")
						  .append("	AND D.STATUS in (:statusDividaEmAbertoPendente) ")
						  .append("	AND C.DT_VENCIMENTO < :dataOperacao ");
				
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COTA_.ID AS IDCOTA, ")
		.append("		COTA_.NUMERO_COTA AS numCota, ")
		.append("		PESSOA_.NOME AS nome, ")
		.append("		PESSOA_.RAZAO_SOCIAL AS RAZAOSOCIAL, ")
		//consignado
		.append("		(")
		.append(hqlConsignado)
		.append("		) AS vlrConsignado, ")
		
		//reparte
		.append("		SUM(ec_.QTDE_EFETIVA * pe_.PRECO_VENDA) as vlrReparte, ")
				
		//divida acumulada
		.append("		(")
		.append(hqlDividaAcumulada)
		.append(" 		) AS dividaAcumulada, ")
		//data abertura
		.append("		(SELECT ")
		.append("			MIN(COBRANCA_.DT_VENCIMENTO) FROM COBRANCA COBRANCA_")
		.append("		WHERE COBRANCA_.COTA_ID=COTA_.ID  ")
		.append("		AND COBRANCA_.STATUS_COBRANCA='NAO_PAGO' ) AS DATAABERTURA, ")
		//faturamento
		.append("		(SELECT SUM(")
		.append("			(EPC.QTDE_RECEBIDA - EPC.QTDE_DEVOLVIDA) * MOVIMENTOCOTA.PRECO_COM_DESCONTO) / :intervalo ")
		.append("		FROM ESTOQUE_PRODUTO_COTA EPC ")
		.append("		JOIN MOVIMENTO_ESTOQUE_COTA MOVIMENTOCOTA ON (MOVIMENTOCOTA.ESTOQUE_PROD_COTA_ID = EPC.ID) ")
		.append("		WHERE EPC.COTA_ID = COTA_.ID ")
		.append("		AND MOVIMENTOCOTA.DATA BETWEEN :dataOperacaoIntervalo AND :dataOperacao ")
		.append("		) AS faturamento, ")
		//percDivida = dividaAcumulada / vlrConsignado
		.append("		((")
		.append(hqlDividaAcumulada)
		.append(" 		) / ")
		.append("		(")
		.append(hqlConsignado)
		.append("		) * 100) as percDivida, ")
		.append("		COALESCE(DATEDIFF(DATE_ADD(:dataOperacao, INTERVAL -1 DAY), ")
		.append("				(SELECT MIN(D.DATA) FROM DIVIDA D JOIN COBRANCA c on (c.DIVIDA_ID=D.ID) WHERE D.COTA_ID = COTA_.ID ")
		.append("												  AND D.STATUS in (:statusDividaEmAbertoPendente) ")
		.append("												  AND D.DATA <= :dataOperacao AND c.DT_PAGAMENTO is null) ")
		.append("		),0) AS diasAberto ");
		
		this.setFromWhereCotasSujeitasSuspensao(sql);
				
		sql.append(obterOrderByCotasSujeitasSuspensao(sortOrder, sortColumn));

		if (inicio != null && rp != null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
		}

		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idCota").addScalar("numCota")
				.addScalar("vlrConsignado").addScalar("vlrReparte")
				.addScalar("dividaAcumulada").addScalar("nome")
				.addScalar("razaoSocial").addScalar("dataAbertura")
				.addScalar("faturamento").addScalar("percDivida")
				.addScalar("diasAberto", IntegerType.INSTANCE);
		
		this.setParametrosCotasSujeitasSuspensao(query, dataOperacao);
		
		query.setParameter("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name());
		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		query.setParameter("statusRecolhido", StatusLancamento.RECOLHIDO.name());
				
		int intervalo = 35;
		query.setParameter("intervalo", intervalo);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataOperacao);
		calendar.add(Calendar.DAY_OF_MONTH, intervalo * -1);
		query.setParameter("dataOperacaoIntervalo", calendar.getTime());
		
		if (inicio != null && rp != null) {
			query.setInteger("inicio", inicio);
			query.setInteger("qtdeResult", rp);
		}

		query.setResultTransformer(Transformers
				.aliasToBean(CotaSuspensaoDTO.class));
 
		
		return query.list();
	}

	private void setParametrosCotasSujeitasSuspensao(Query query, Date dataOperacao) {
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("ativo", SituacaoCadastro.ATIVO.name());
		query.setParameterList("statusDividaEmAbertoPendente", new String[]{StatusDivida.EM_ABERTO.name(), StatusDivida.PENDENTE_INADIMPLENCIA.name(),StatusDivida.PENDENTE.name()});
		
		query.setParameterList("status", new String[]{StatusLancamento.CONFIRMADO.name(), StatusLancamento.EM_BALANCEAMENTO.name()});
		query.setParameterList("statusNaoEmitiveis", new String[]{StatusLancamento.PLANEJADO.name(), StatusLancamento.FECHADO.name(), StatusLancamento.CONFIRMADO.name(), StatusLancamento.EM_BALANCEAMENTO.name(), StatusLancamento.CANCELADO.name()});
		
	}

private void setFromWhereCotasSujeitasSuspensao(StringBuilder sql) {
		
		sql.append(" FROM COTA COTA_ ")
		.append(" LEFT JOIN PARAMETRO_COBRANCA_COTA POLITICACOTA ON(POLITICACOTA.COTA_ID=COTA_.ID) ")
		.append(" JOIN DISTRIBUIDOR AS POLITICADISTRIB ")
		.append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ");
		
		
		sql.append(" left join ESTUDO_COTA ec_ on (ec_.cota_ID=cota_.ID ) ")  
		.append(" join ESTUDO e_ on ec_.ESTUDO_ID = e_.ID ")
		.append(" join LANCAMENTO lancamento_ on (e_.PRODUTO_EDICAO_ID = lancamento_.PRODUTO_EDICAO_ID and e_.DATA_LANCAMENTO = lancamento_.DATA_LCTO_PREVISTA AND lancamento_.DATA_LCTO_DISTRIBUIDOR=:dataOperacao) ")
		.append(" join PRODUTO_EDICAO pe_ on e_.PRODUTO_EDICAO_ID = pe_.ID ");
		
		
		sql.append(" WHERE SITUACAO_CADASTRO = :ativo AND COTA_.SUGERE_SUSPENSAO!=false ")
		
		.append(" AND ((POLITICACOTA.NUM_ACUMULO_DIVIDA IS NOT NULL ")
		.append(" 		AND POLITICACOTA.NUM_ACUMULO_DIVIDA <> 0 ")
		.append("		AND POLITICACOTA.NUM_ACUMULO_DIVIDA <= ( ")
		.append("											SELECT count(DIVIDA.ID) ")
		.append("											FROM DIVIDA DIVIDA ")
		.append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
		.append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
		.append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
		.append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
		.append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao  )")
		.append("	   ) ")
		.append("	   OR ")
		
		
		.append("	   (POLITICACOTA.VALOR_SUSPENSAO IS NOT NULL ")
		.append("       AND POLITICACOTA.VALOR_SUSPENSAO <> 0 ")
		.append("		AND POLITICACOTA.VALOR_SUSPENSAO <= (SELECT SUM(DIVIDA.VALOR) ")
		.append("											FROM DIVIDA DIVIDA ")
		.append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
		.append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
		.append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
		.append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
		.append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao )")
			
		

		.append("	   ) ")
		.append("	   OR ")
		
		.append("	   (POLITICACOTA.NUM_ACUMULO_DIVIDA IS NULL and POLITICACOTA.VALOR_SUSPENSAO IS NULL ")
		.append("	    AND POLITICADISTRIB.NUM_ACUMULO_DIVIDA IS NOT NULL ")
		.append("	    AND POLITICADISTRIB.NUM_ACUMULO_DIVIDA <> 0 ")
		.append("		AND POLITICADISTRIB.NUM_ACUMULO_DIVIDA <= (")
		.append("											SELECT count(DIVIDA.ID) ")
		.append("											FROM DIVIDA DIVIDA ")
		.append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
		.append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
		.append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
		.append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
		.append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao)")
		.append("	   ) ")
		.append("	   OR ")
		
		.append("	   (POLITICACOTA.NUM_ACUMULO_DIVIDA IS NULL and POLITICACOTA.VALOR_SUSPENSAO IS NULL ")
		.append("	    AND POLITICADISTRIB.VALOR_SUSPENSAO IS NOT NULL ")
		.append("	    AND POLITICADISTRIB.VALOR_SUSPENSAO <> 0")
		.append("		AND POLITICADISTRIB.VALOR_SUSPENSAO <= (SELECT SUM(DIVIDA.VALOR) ")
		.append("											FROM DIVIDA DIVIDA ")
		.append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
		.append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
		.append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
		.append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
		.append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao )")
		
		.append("	   ) ")
		.append(") ");
				
		sql.append(" AND (lancamento_.STATUS is null OR lancamento_.STATUS not in (:status))  ")
		   .append(" AND lancamento_.STATUS not in (:statusNaoEmitiveis) ");
		
		sql.append("  group by cota_.ID ");
		
	}

	private String obterOrderByCotasSujeitasSuspensao(String sortOrder,
			String sortColumn) {
		String sql = "";

		if (sortColumn == null || sortOrder == null) {
			return sql;
		}

		sql += " ORDER BY ";

		if (sortColumn.equalsIgnoreCase("numCota")) {
			sql += "numCota";
		} else if (sortColumn.equalsIgnoreCase("nome")) {
			sql += "nome";
		} else if (sortColumn.equalsIgnoreCase("vlrConsignado")) {
			sql += "vlrConsignado";
		} else if (sortColumn.equalsIgnoreCase("vlrReparte")) {
			sql += "vlrReparte";
		} else if (sortColumn.equalsIgnoreCase("dividaAcumulada")) {
			sql += "dividaAcumulada";
		} else if (sortColumn.equalsIgnoreCase("diasAberto")) {
			sql += "dataAbertura";
		} else if (sortColumn.equalsIgnoreCase("faturamento")){
			sql += "faturamento";
		} else if (sortColumn.equalsIgnoreCase("percDivida")){
			sql += "percDivida";
		} else {
			return "";
		}

		sql += sortOrder.equalsIgnoreCase("asc") ? " ASC " : " DESC ";

		return sql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterReparteDaCotaNoDia(Long idCota, Date date) {

		Criteria criteria = getSession().createCriteria(
				MovimentoEstoqueCota.class, "movimento");

		criteria.createAlias("movimento.produtoEdicao", "produtoEdicao");
		criteria.createAlias("movimento.tipoMovimento", "tipoMovimento");

		criteria.add(Restrictions.eq("data", date));
		criteria.add(Restrictions.eq("tipoMovimento.operacaoEstoque",
				OperacaoEstoque.ENTRADA));
		criteria.add(Restrictions.eq("cota.id", idCota));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.alias(Projections.property("qtde"),
				"quantidade"));
		projections.add(Projections.alias(
				Projections.property("produtoEdicao.precoVenda"), "preco"));

		criteria.setProjection(projections);

		criteria.setResultTransformer(Transformers
				.aliasToBean(ProdutoValorDTO.class));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterValorConsignadoDaCota(Long idCota) {

		Criteria criteria = getSession().createCriteria(
				EstoqueProdutoCota.class, "epCota");
		criteria.createAlias("epCota.produtoEdicao", "produtoEdicao");
		criteria.createAlias("epCota.cota", "cota");

		criteria.add(Restrictions.eq("cota.id", idCota));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.alias(
				Projections.property("epCota.qtdeRecebida"), "quantidade"));
		projections.add(Projections.alias(
				Projections.property("produtoEdicao.precoVenda"), "preco"));

		criteria.setProjection(projections);

		criteria.setResultTransformer(Transformers
				.aliasToBean(ProdutoValorDTO.class));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> obterDiasConcentracaoPagamentoCota(Long idCota) {
		StringBuilder hql = new StringBuilder("select cc.codigoDiaSemana ");
		hql.append(" from ConcentracaoCobrancaCota cc ")
		   .append(" join cc.formaCobranca fc ")
		   .append(" join fc.parametroCobrancaCota p ")
		   .append(" join p.cota cota ")
		   .append(" where cota.id = :idCota ");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);

		return query.list();
	}

	@Override
	public Long obterTotalCotasSujeitasSuspensao(Date dataOperacao) {

		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM (SELECT cota_.ID ");
		
		this.setFromWhereCotasSujeitasSuspensao(sql);

		sql.append(") as total ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		this.setParametrosCotasSujeitasSuspensao(query, dataOperacao);

		Long qtde = ((BigInteger) query.uniqueResult()).longValue();

		return qtde == null ? 0 : qtde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotaAssociadaFiador(Long idFiador) {
		Criteria criteria = this.getSession().createCriteria(Cota.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));

		return criteria.list();
	}

	@Override
	public Long obterQntCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		        /*
         * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um
         * PDV associado a mesma cota.
         */
		hql.append("SELECT DISTINCT count ( cota.id ) ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);

		setParameters(query, param);
		return (Long) query.uniqueResult();
	}

	@Override
	public BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);
 
		setParameters(query, param);

		query.setMaxResults(1);

		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		        /*
         * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um
         * PDV associado a mesma cota.
         */
		hql.append("SELECT DISTINCT new ")
				.append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
				.append(" ( box.codigo, box.nome ,cota.numeroCota, estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida, ")
				.append(" lancamento.id ,")
				.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
				.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
				.append(" else null end ").append(" ) ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		hql.append(getOrderByCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);

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

	/**
	 * Retorna os parametros da consulta de dividas.
	 * 
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String, Object> getParametrosCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("codigoProduto", filtro.getCodigoProduto());
		param.put("numeroEdicao", filtro.getNumeroEdicao());
		param.put("status", StatusLancamento.EXPEDIDO);
		param.put("dataAtual", filtro.getDataOperacao());
		param.put("tipoChamadaEncalhe", TipoChamadaEncalhe.ANTECIPADA);

		if (filtro.getNumeroCota() != null) {
			param.put("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getFornecedor() != null) {
			param.put("fornecedor", filtro.getFornecedor());
		}

		if (filtro.getBox() != null) {
			param.put("box", filtro.getBox());
		}
		
		if(filtro.getRota()!= null){
			param.put("rota",filtro.getRota());
		}
		
		if(filtro.getRoteiro()!= null){
			param.put("roteiro",filtro.getRoteiro());
		}
		
		if(filtro.getDescMunicipio()!= null){
			param.put("cidadeCota",filtro.getDescMunicipio());
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			param.put("codigoTipoPontoPDV", filtro.getCodTipoPontoPDV());
		}

		return param;
	}

	private String getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM ").append(" Cota cota  ")
				.append(" JOIN cota.pessoa pessoa ")
				.append(" JOIN cota.box box ")
				.append(" JOIN cota.estoqueProdutoCotas estoqueProdutoCota ")
				.append(" JOIN cota.estudoCotas estudoCota ")
				.append(" JOIN estudoCota.estudo estudo ")
				.append(" JOIN estudo.produtoEdicao produtoEdicao  ")
				.append(" JOIN produtoEdicao.produto produto ")
				.append(" JOIN produtoEdicao.lancamentos lancamento ")
				.append(" JOIN cota.pdvs pdv ")
				.append(" LEFT JOIN pdv.rotas rotaPdv  ")
				.append(" LEFT JOIN rotaPdv.rota rota  ")
				.append(" LEFT JOIN rota.roteiro roteiro ")
				.append(" LEFT JOIN roteiro.roteirizacao roteirizacao ");

		if (filtro.getFornecedor() != null) {
			hql.append(" JOIN produto.fornecedores fornecedor ");
		}
		
		if(filtro.getDescMunicipio()!= null){
			hql.append(" JOIN cota.enderecos enderecoCota ")
				.append(" JOIN enderecoCota.endereco endereco ");
		}
		
		hql.append(" WHERE ")
				.append("NOT EXISTS (")
				.append(" SELECT chamadaEncalheCota.cota FROM ChamadaEncalheCota chamadaEncalheCota ")
				.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
				.append(" WHERE chamadaEncalheCota.cota = cota ")
				.append(" AND chamadaEncalhe.produtoEdicao = produtoEdicao ")
				.append(" AND chamadaEncalhe.tipoChamadaEncalhe=:tipoChamadaEncalhe")
				.append(" ) ")
				.append(" AND estoqueProdutoCota.produtoEdicao.id = produtoEdicao.id ")
				.append(" AND lancamento.status =:status ")
				.append(" AND produto.codigo =:codigoProduto ")
				.append(" AND produtoEdicao.numeroEdicao =:numeroEdicao ")
				.append(" AND lancamento.dataRecolhimentoPrevista >:dataAtual ")
				.append(" AND (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) > 0 ")
				.append(" AND pdv.caracteristicas.pontoPrincipal = true ");


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
			hql.append(" AND endereco.cidade =:cidadeCota ");
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			hql.append(" AND pdv.segmentacao.tipoPontoPDV.codigo =:codigoTipoPontoPDV ");
		}

		return hql.toString();
	}

	    /**
     * Retorna a instrução de ordenação para consulta
     * obterCotasSujeitasAntecipacoEncalhe
     * 
     * @param filtro - filtro de pesquisa com os parâmetros de ordenação.
     * 
     * @return String
     */
	private String getOrderByCotasSujeitasAntecipacoEncalhe(
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
			hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
			break;
		default:
			hql.append(" order by  box.codigo ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
	 */
	@Override
	public Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from Cota cota ").append(" left join fetch cota.pdvs ")
				.append(" where cota.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);
		
		query.setMaxResults(1);

		return (Cota) query.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.repository.CotaRepository#obterEnderecoPrincipal(long)
	 */
	@Override
	public EnderecoCota obterEnderecoPrincipal(long idCota) {
		Criteria criteria = getSession().createCriteria(EnderecoCota.class);
		criteria.add(Restrictions.eq("cota.id", idCota));

		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);

		return (EnderecoCota) criteria.uniqueResult();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> obterCotas(FiltroCotaDTO filtro) {
		


		StringBuilder hql = new StringBuilder();

		hql.append(this.getSqlPesquisaCota(filtro, Boolean.FALSE));

		hql.append(this.ordenarConsultaCota(filtro));

		Query query = getSession().createQuery(hql.toString());


		if (filtro.getCotaId() != null) {
		    query.setParameter("cotaId", filtro.getCotaId());
		}

		if (filtro.getNumeroCota() != null) {
		    query.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getNumeroCpfCnpj() != null
			&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
		    query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() + "%");
		}

		if (filtro.getNomeCota() != null
			&& !filtro.getNomeCota().trim().isEmpty()) {
		    query.setParameter("nomeCota", filtro.getNomeCota() + "%" );
		}

		if (filtro.getLogradouro() != null
			&& !filtro.getLogradouro().trim().isEmpty()) {

		    query.setParameter("logradouro", filtro.getLogradouro() + "%" );
		}

		if (filtro.getBairro() != null
			&& !filtro.getBairro().trim().isEmpty()) {

		    query.setParameter("bairro", filtro.getBairro() + "%" );
		}

		if (filtro.getMunicipio() != null
			&& !filtro.getMunicipio().trim().isEmpty()) {

		    query.setParameter("municipio", filtro.getMunicipio() + "%" );
		}

		if (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty() && !filtro.getStatus().equalsIgnoreCase("TODOS")) {
		    query.setParameter("situacaoCadastro", SituacaoCadastro.valueOf(filtro.getStatus()));
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));

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

	public Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(this.getSqlPesquisaCota(filtro, Boolean.TRUE));

		Query query = getSession().createQuery(hql.toString());

		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getNumeroCpfCnpj() != null
				&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
			query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() +  "%");
		}

		if (filtro.getNomeCota() != null
				&& !filtro.getNomeCota().trim().isEmpty()) {
			query.setParameter("nomeCota", filtro.getNomeCota() + "%");
		}
		
		if (filtro.getLogradouro() != null
				&& !filtro.getLogradouro().trim().isEmpty()) {

			query.setParameter("logradouro", filtro.getLogradouro() + "%" );
		}
		
		if (filtro.getBairro() != null
				&& !filtro.getBairro().trim().isEmpty()) {

			query.setParameter("bairro", filtro.getBairro() + "%" );
		}
		
		if (filtro.getMunicipio() != null
				&& !filtro.getMunicipio().trim().isEmpty()) {

			query.setParameter("municipio", filtro.getMunicipio() + "%" );
		}
		
		if (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty() && !filtro.getStatus().equalsIgnoreCase("TODOS")) {
		    query.setParameter("situacaoCadastro", SituacaoCadastro.valueOf(filtro.getStatus()));
		}

		return (Long) query.uniqueResult();
	}

	private String getSubSqlPesquisaTelefone() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select telefone.ddd || '-' || telefone.numero ");
		
		hql.append(" from  ");
		
		hql.append(" Telefone telefone where telefone.id = 	");
		
		hql.append(" ( select max(telefone.id) from TelefoneCota telefoneCota inner join telefoneCota.telefone as telefone");
		
		hql.append(" where telefoneCota.cota.id = cota.id and ");
		
		hql.append(" telefoneCota.principal = true ) ) as telefone, ");
		
		return hql.toString();
		
	}
	
	private String getSubSqlPesquisaContatoPDV() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select pdv.contato ");
		
		hql.append(" from  ");
		
		hql.append(" PDV pdv ");
		
		hql.append(" where pdv.id =  ");
		
		hql.append(" ( select max(pdv.id) from PDV pdv where pdv.cota.id = cota.id and pdv.caracteristicas.pontoPrincipal = true ) ) as contato,");
		
		return hql.toString();
		
	}
	
	private String getSqlPesquisaCota(FiltroCotaDTO filtro, boolean isCount) {

		StringBuilder hql = new StringBuilder();

		int colunaRazaoSocNomePessoa = 3;
		int colunaNumeroCpfCnpj 	 = 4;
		int colunaContato  = 5;
		int colunaTelefone = 6;

		if (isCount) {

		    hql.append(" select count(distinct cota.numeroCota) ");

		} else {

		    hql.append(
			    " SELECT cota.id as idCota, cota.numeroCota as numeroCota, cota.parametroDistribuicao.recebeComplementar as recebeComplementar, cota.tipoDistribuicaoCota as tipoDistribuicaoCota, ")
			    .append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
			    .append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
			    .append(" else null end as nomePessoa, ")
			    .append(" case when (pessoa.cpf is not null) then ( pessoa.cpf )")
			    .append(" when (pessoa.cnpj is not null) then ( pessoa.cnpj )")
			    .append(" else null end as numeroCpfCnpj, ")


			    .append(getSubSqlPesquisaContatoPDV())
			    .append(getSubSqlPesquisaTelefone())

			    .append(" pessoa.email as email ,")
			    .append(" cota.situacaoCadastro as status, ")
			    .append(" box.nome as descricaoBox ");
		}

		hql.append(" FROM Cota cota 								")
		   .append(" join cota.pessoa pessoa 					")
		   .append(" left join cota.enderecos enderecoCota 	")
		   .append(" left join enderecoCota.endereco endereco 	")
		   .append(" left join cota.box box ");



		if(	filtro.getCotaId() != null || filtro.getNumeroCota() != null ||
			(filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty()) ||
			(filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty()) ||
			(filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty()) ||
			(filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty()) ||
			(filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty()) ||
			(filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty() && !filtro.getStatus().equalsIgnoreCase("TODOS"))) {

		    hql.append(" WHERE ");

		}

		boolean indAnd = false;

		if (filtro.getCotaId() != null) {

		    hql.append(" cota.id = :cotaId ");

		    indAnd = true;
		}

		if (filtro.getNumeroCota() != null) {
		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" cota.numeroCota =:numeroCota ");

		    indAnd = true;
		}


		if (filtro.getNumeroCpfCnpj() != null
			&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" ( upper (pessoa.cpf) like(:numeroCpfCnpj) OR  upper(pessoa.cnpj) like upper (:numeroCpfCnpj) ) ");

		    indAnd = true;
		}

		if (filtro.getNomeCota() != null
			&& !filtro.getNomeCota().trim().isEmpty()) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" ( upper(pessoa.nome) like upper(:nomeCota) OR  upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");

		    indAnd = true;
		}

		if (filtro.getLogradouro() != null
			&& !filtro.getLogradouro().trim().isEmpty()) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" ( upper(endereco.logradouro) like upper(:logradouro) )");

		    indAnd = true;
		}

		if (filtro.getBairro() != null
			&& !filtro.getBairro().trim().isEmpty()) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" ( upper(endereco.bairro) like upper(:bairro) )");

		    indAnd = true;
		}

		if (filtro.getMunicipio() != null
			&& !filtro.getMunicipio().trim().isEmpty()) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" ( upper(endereco.cidade) like upper(:municipio) )");

		    indAnd = true;
		}

		if (filtro.getStatus() != null
			&& !filtro.getStatus().trim().isEmpty() && !filtro.getStatus().equalsIgnoreCase("TODOS")) {

		    if(indAnd) {
			hql.append(" AND ");
		    }

		    hql.append(" cota.situacaoCadastro =:situacaoCadastro ");
		}


		if (!isCount) {

		    hql.append("group by ");
		    hql.append("cota.id, ");
		    hql.append("cota.numeroCota, ");
		    hql.append(colunaRazaoSocNomePessoa + ", ");
		    hql.append(colunaNumeroCpfCnpj 		+ ", ");
		    hql.append(colunaContato 			+ ", ");
		    hql.append(colunaTelefone 			+ ", ");
		    hql.append("pessoa.email, ");
		    hql.append("cota.situacaoCadastro, ");
		    hql.append("box.nome  ");

		} 

		return hql.toString();
	}

	    /**
     * Retorna string sql de ordenação da consulta de cotas
     * 
     * @param filtro - filtro com opção de ordenação escolhida
     * @return String
     */
	private String ordenarConsultaCota(FiltroCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

			switch (filtro.getOrdemColuna()) {

			case NUMERO_COTA:
				hql.append(" ORDER BY numeroCota ");
				break;

			case NOME_PESSOA:
				hql.append(" ORDER BY nomePessoa ");
				break;

			case NUMERO_CPF_CNPJ:
				hql.append(" ORDER BY numeroCpfCnpj ");
				break;

			case CONTATO:
				hql.append(" ORDER BY contato ");
				break;

			case TELEFONE:
				hql.append(" ORDER BY telefone ");
				break;

			case EMAIL:
				hql.append(" ORDER BY email ");
				break;

			case STATUS:
				hql.append(" ORDER BY status ");
				break;
			case BOX:
				hql.append("ORDER BY descricaoBox ");
				break;

			default:
				hql.append(" ORDER BY numeroCota ");
			}

			if (filtro.getPaginacao() != null
					&& filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}

		}

		return hql.toString();
	}
	
	public Integer gerarSugestaoNumeroCota(){
		
		String hql = "select max(cota.numeroCota) from Cota cota where cota.id not in ( select h.pk.idCota from HistoricoNumeroCota h )";
		
		Integer numeroCota =  (Integer) getSession().createQuery(hql).uniqueResult();
		
		return (numeroCota == null ) ? 0 : numeroCota + 1;
	}
	


	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorIDS(List<Long> idsCotas) {
		
		try {
			
			if (idsCotas == null || idsCotas.isEmpty()) {
				return null;
			}
			 
			Criteria criteria = super.getSession().createCriteria(Cota.class);
			
			criteria.add(Restrictions.in("id", idsCotas));
			
			return criteria.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro) {
		
		StringBuilder hql = new StringBuilder(" select count (cota.id) ");
		
		hql.append(" from Cota cota ");
		
		if (situacaoCadastro != null) {
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null) {
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		Object uniqueResult = query.uniqueResult();
		return (Long) uniqueResult;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaResumoDTO> obterCotas(SituacaoCadastro situacaoCadastro) {
		
		StringBuilder hql = new StringBuilder("select coalesce(pessoa.nome, pessoa.razaoSocial) as nome, cota.numeroCota as numero  from Cota cota ");

		hql.append(" join cota.pessoa pessoa ");
		
		if (situacaoCadastro != null) {
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null) {
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
		
		return query.list();
	}
	
	
	public Long countCotas(SituacaoCadastro situacaoCadastro) {
		
		StringBuilder hql = new StringBuilder("select count(cota.id) from Cota cota ");
		
		if (situacaoCadastro != null) {
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null) {
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaResumoDTO> obterCotasComInicioAtividadeEm(Date dataInicioAtividade) {
		
		StringBuilder hql = new StringBuilder("select coalesce(pessoa.nome, pessoa.razaoSocial) as nome, cota.numeroCota as numero from Cota cota ");

		hql.append(" join cota.pessoa pessoa ");
		
		if (dataInicioAtividade != null) {
			
			hql.append(" where cota.inicioAtividade = :dataInicioAtividade ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataInicioAtividade != null) {
			
			query.setParameter("dataInicioAtividade", dataInicioAtividade);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
		
		return query.list();
	}
	
	public Long countCotasComInicioAtividadeEm(Date dataInicioAtividade) {
		
		StringBuilder hql = new StringBuilder("select count(cota.id) from Cota cota ");

		if (dataInicioAtividade != null) {
			
			hql.append(" where cota.inicioAtividade = :dataInicioAtividade ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataInicioAtividade != null) {
			
			query.setParameter("dataInicioAtividade", dataInicioAtividade);
		}
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte) {
		
		StringBuilder hql = new StringBuilder(" select coalesce(pessoa.nome, pessoa.razaoSocial) as nome, cota.numeroCota as numero from CotaAusente cotaAusente ");
		
		hql.append(" join cotaAusente.cota cota ");
		
		hql.append(" join cota.pessoa pessoa ");
				
		
		if (dataExpedicaoReparte != null) {
			
			hql.append(" where cotaAusente.data = :dataExpedicaoReparte ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataExpedicaoReparte != null) {
			
			query.setParameter("dataExpedicaoReparte", dataExpedicaoReparte);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
		
		return query.list();
	}
	
	
	public Long countCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte) {
		
		StringBuilder hql = new StringBuilder(" select count(cotaAusente.id)  from CotaAusente cotaAusente ");
				
		if (dataExpedicaoReparte != null) {
			
			hql.append(" where cotaAusente.data = :dataExpedicaoReparte ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataExpedicaoReparte != null) {
			
			query.setParameter("dataExpedicaoReparte", dataExpedicaoReparte);
		}
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe) {
		
		StringBuilder hql = 
			new StringBuilder(" select coalesce(pessoa.nome, pessoa.razaoSocial) as nome, cota.numeroCota as numero from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" join chamadaEncalheCota.cota cota ");
		
		hql.append(" join cota.pessoa pessoa ");
		
		hql.append(" where chamadaEncalheCota.cota.id not in ( ");
		hql.append(" select cota.id from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append(" join controleConferenciaEncalheCota.cota cota ");
		hql.append(" where controleConferenciaEncalheCota.dataOperacao = :dataRecolhimentoEncalhe ");
		hql.append(" and controleConferenciaEncalheCota.status = :statusControleConferenciaEncalhe) ");
		hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataRecolhimentoEncalhe ");
		hql.append(" group by chamadaEncalheCota.cota.id ");
		hql.append(" order by cota.numeroCota");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimentoEncalhe", dataRecolhimentoEncalhe);
		query.setParameter("statusControleConferenciaEncalhe", StatusOperacao.CONCLUIDO);

		query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
		
		return query.list();
	}
	
	public Long countCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe) {
		
		StringBuilder hql = 
			new StringBuilder(" select count(distinct chamadaEncalheCota.cota.id) from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" join chamadaEncalheCota.cota cota ");
		
		hql.append(" where chamadaEncalheCota.cota.id not in ( ");
		hql.append(" select cota.id from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append(" join controleConferenciaEncalheCota.cota cota ");
		hql.append(" where controleConferenciaEncalheCota.dataOperacao = :dataRecolhimentoEncalhe ");
		hql.append(" and controleConferenciaEncalheCota.status = :statusControleConferenciaEncalhe) ");
		hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataRecolhimentoEncalhe ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimentoEncalhe", dataRecolhimentoEncalhe);
		query.setParameter("statusControleConferenciaEncalhe", StatusOperacao.CONCLUIDO);
		
		return (Long) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterIdCotasEntre(br.com.abril.nds.util.Intervalo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdCotasEntre(Intervalo<Integer> intervaloCota, Intervalo<Integer> intervaloBox, List<SituacaoCadastro> situacoesCadastro
										, Long idRoteiro, Long idRota, String sortName, String sortOrder, Integer maxResults, Integer page) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		criteria.createAlias("box", "box", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pdvs", "pdvs");
		criteria.setProjection(Projections.distinct(Projections.id()));
		
		if (intervaloCota != null && intervaloCota.getDe() != null) {
			
			if (intervaloCota.getAte() != null) {
				criteria.add(Restrictions.between("numeroCota", intervaloCota.getDe(),
						intervaloCota.getAte()));
			} else {
				criteria.add(Restrictions.eq("numeroCota", intervaloCota.getDe()));
			}
		}
		
		if(intervaloBox != null && intervaloBox.getDe() != null &&  intervaloBox.getAte() != null){
			criteria.add(Restrictions.between("box.codigo", intervaloBox.getDe(),
					intervaloBox.getAte()));
		}
		
		if(situacoesCadastro != null && !situacoesCadastro.isEmpty()){
			criteria.add(Restrictions.in("situacaoCadastro", situacoesCadastro));
		}
		
		criteria.createAlias("pdvs.rotas", "rotaPdv", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("rotaPdv.rota", "rota", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("rota.roteiro", "roteiro", JoinType.LEFT_OUTER_JOIN);
				
		
		if (idRoteiro != null){
			
			criteria.add(Restrictions.eq("roteiro.id", idRoteiro));
		}
		
		if (idRota != null){
			
			criteria.add(Restrictions.eq("rota.id", idRota));
		}
		
		criteria.addOrder(Order.asc("box.codigo"));
		criteria.addOrder(Order.asc("rota.id"));
		criteria.addOrder(Order.asc("rota.descricaoRota"));
		criteria.addOrder(Order.asc("numeroCota"));
		
		return criteria.list();
	}

	/**
	 * Obtem cotas por intervalo de numero de cotas
	 * @param cotaDe
	 * @param cotaAte
	 * @param situacoesCadastro
	 * @return List<Cota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotasIntervaloNumeroCota(Integer cotaDe,
			                                        Integer cotaAte,  
			                                        List<SituacaoCadastro> situacoesCadastro) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.setProjection(Projections.distinct(Projections.id()));
		
		if (cotaDe != null) {
			
			if (cotaAte != null) {
				
				criteria.add(Restrictions.between("numeroCota", cotaDe, cotaAte));
			} else {
				
				criteria.add(Restrictions.eq("numeroCota", cotaDe));
			}
		}
		
		if(situacoesCadastro != null && !situacoesCadastro.isEmpty()){
			
			criteria.add(Restrictions.in("situacaoCadastro", situacoesCadastro));
		}

		criteria.addOrder(Order.asc("numeroCota"));
		
		return criteria.list();
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioEmitidasCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro, sql, true, StatusNotaEnvio.EMITIDA);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		BigInteger qtde = (BigInteger) query.uniqueResult();
		
		return (qtde == null) ? 0 : qtde.intValue();
		
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioAEmitirCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro, sql, true, StatusNotaEnvio.NAO_EMITIDA);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		BigInteger qtde = (BigInteger) query.uniqueResult();
		
		return (qtde == null) ? 0 : qtde.intValue();
		
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
		sql.append(" union all ");
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro, sql, true, null);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		BigInteger qtde = (BigInteger) query.uniqueResult();
		
		return (qtde == null) ? 0 : qtde.intValue();
		
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidas(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro, sql, false, StatusNotaEnvio.EMITIDA);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioAEmitir(FiltroConsultaNotaEnvioDTO filtro) {
		
		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro, sql, false, StatusNotaEnvio.NAO_EMITIDA);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	
		
		query.setParameterList("gruposFaltaSobra", this.getGruposSobraFalta());
		query.setParameterList("gruposFalta", this.getGruposFalta());
		query.setParameterList("gruposSobra", this.getGruposSobra());

		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidasEAEmitir(
			FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
		sql.append( " union all ");
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
		sql.append(" union all ");
		montarQueryReparteCotaAusente(filtro,sql, false, null);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	

		query.setParameterList("gruposFaltaSobra", this.getGruposSobraFalta());
		query.setParameterList("gruposFalta", this.getGruposFalta());
		query.setParameterList("gruposSobra", this.getGruposSobra());

		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();
		
	}
	
	private List<String> getGruposSobraFalta() {
		
		return Arrays.asList(
			GrupoMovimentoEstoque.FALTA_DE.name(),
			GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
			GrupoMovimentoEstoque.FALTA_EM.name(),
			GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
			GrupoMovimentoEstoque.SOBRA_DE.name(),
			GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
			GrupoMovimentoEstoque.SOBRA_EM.name(),
			GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
			GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
			GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
			GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name()
		);
	}
	
	private List<String> getGruposFalta() {
		
		return Arrays.asList(
			GrupoMovimentoEstoque.FALTA_DE.name(),
			GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
			GrupoMovimentoEstoque.FALTA_EM.name(),
			GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
			GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
			GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name()
		);
	}
	
	private List<String> getGruposSobra() {
		
		return Arrays.asList(
			GrupoMovimentoEstoque.SOBRA_DE.name(),
			GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
			GrupoMovimentoEstoque.SOBRA_EM.name(),
			GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
			GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name()
		);
	}
	
	private void montarQueryReparteCotaAusente(FiltroConsultaNotaEnvioDTO filtro, 
			StringBuilder sql, boolean isCount, StatusNotaEnvio status) {
		
		if(isCount) {
			sql.append( " select cota_.ID ");
		} else {
			sql.append( " select lancamento_.STATUS as status, "
				+ "	        cota_.ID as idCota, "
				+ "	        cota_.NUMERO_COTA as numeroCota, "
				+ "	        cota_.BOX_ID as box, "
				+ "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
				+ "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, "
				+ "	        SUM(mec.QTDE) as exemplares, "
				+ "	        SUM(mec.QTDE * pe_.PRECO_VENDA) as total, "
				+ "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa,	"
				+ "			roteiro_.ordem ordemRoteiro, "
				+ "			rota_.ordem ordemRota, "
				+ "			rota_pdv_.ordem ordemRotaPdv "); 
		}
		sql.append( "   from "
				+ "	        COTA cota_ " 
				+ "	    left outer join "
				+ "	        BOX box1_  "
				+ "	            on cota_.BOX_ID=box1_.ID  "
				+ "	    inner join "
				+ "	        MOVIMENTO_ESTOQUE_COTA mec  "
				+ "	            on cota_.ID=mec.COTA_ID  "
				+ "	    inner join "
				+ "	        TIPO_MOVIMENTO tm  "
				+ "	            on mec.TIPO_MOVIMENTO_ID=tm.ID  "
				+ "	    inner join "
				+ "	        LANCAMENTO lancamento_  "
				+ "	            on mec.LANCAMENTO_ID=lancamento_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO_EDICAO pe_  "
				+ "	            on mec.PRODUTO_EDICAO_ID=pe_.ID  "
				+ " 			and lancamento_.PRODUTO_EDICAO_ID=pe_.ID "
				+ "	    inner join "
				+ "	        PRODUTO p_  "
				+ "	            on pe_.PRODUTO_ID=p_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO_FORNECEDOR pf_  "
				+ "	            on p_.ID=pf_.PRODUTO_ID  "
				+ "	    inner join "
				+ "	        FORNECEDOR f_  "
				+ "	            on pf_.fornecedores_ID=f_.ID  "
				+ "	    inner join "
				+ "	        PDV pdv_  "
				+ "	            on cota_.ID=pdv_.COTA_ID  "
				+ "	    left outer join "
				+ "        ROTA_PDV rota_pdv_  "
				+ "	            on pdv_.ID=rota_pdv_.PDV_ID    "  
				+ "	    left outer join "
				+ "	        ROTA rota_  "
				+ "	            on rota_pdv_.rota_ID=rota_.ID  "
				+ "	    left outer join "
				+ "	        ROTEIRO roteiro_  "
				+ "	            on rota_.ROTEIRO_ID=roteiro_.ID  "
				+ "	    inner join "
				+ "	        PESSOA pessoa_cota_  "
				+ "	            on cota_.PESSOA_ID=pessoa_cota_.ID  ");
				
				String joinType = " left outer join ";
				
				if(StatusNotaEnvio.EMITIDA.equals(status)){
					joinType = " inner join ";
				}
				
				sql.append(joinType+"   NOTA_ENVIO_ITEM nei " 
		        + "    				on ( nei.SEQUENCIA = mec.NOTA_ENVIO_ITEM_SEQUENCIA " 
		        + "                  	 and nei.NOTA_ENVIO_ID = mec.NOTA_ENVIO_ITEM_NOTA_ENVIO_ID )"
				+ "	   	where "
				+ "	        lancamento_.STATUS in (:status)  "
				+ "	        and mec.ESTUDO_COTA_ID is null  "
				+ "    	and tm.GRUPO_MOVIMENTO_ESTOQUE = 'RATEIO_REPARTE_COTA_AUSENTE'  "
				+ "		and pdv_.ponto_principal = :principal ");
		
				if(StatusNotaEnvio.NAO_EMITIDA.equals(status) || status == null){
					sql.append( "  and mec.NOTA_ENVIO_ITEM_SEQUENCIA is null  ");
				}
				
				if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
					sql.append(
					  "	        and ( "
					+ "	            f_.ID is null  "
					+ "	            or f_.ID in (:idFornecedores) "
					+ "	        )  ");
				}
				
				if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
					if (filtro.getIntervaloCota().getAte() != null) {
						
						sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
						
					} else {
						
						sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
					}
			
				}
				
				if (filtro.getIntervaloBox() != null 
						&& filtro.getIntervaloBox().getDe() != null 
						&&  filtro.getIntervaloBox().getAte() != null) {
					sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
				}
				
				if (filtro.getIdRoteiro() != null){
					sql.append(" and roteiro_.ID=:idRoteiro  ");
				}
				
				if (filtro.getIdRota() != null){
					sql.append(" and rota_.ID=:idRota  ");
				}
				
				if (!filtro.isFiltroEspecial()) {
					sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
				}
				
				if (filtro.getIntervaloMovimento() != null 
						&& filtro.getIntervaloMovimento().getDe() != null 
						&& filtro.getIntervaloMovimento().getAte() != null) {
					sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
					sql.append(" and cota_.ID not in (select COTA_ID from COTA_AUSENTE where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
				}
				
				sql.append(
				  "	    group by cota_.ID ");
		
	}
	
	
		
	private void montarQueryCotasComNotasEnvioEmitidas(FiltroConsultaNotaEnvioDTO filtro, StringBuilder sql, boolean isCount) {
		if(isCount) {
			sql.append( " select cota_.ID ");
		} else {
			sql.append( " select lancamento_.STATUS as status,  "
			+ "	        cota_.ID as idCota, "
			+ "	        cota_.NUMERO_COTA as numeroCota, "
			+ "	        cota_.BOX_ID as box, "
			+ "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
			+ "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, "
			+ "	        SUM(coalesce(nei.reparte, 0)) as exemplares, "
			+ "	        SUM(coalesce(nei.reparte, 0) * pe_.PRECO_VENDA) as total, "
			+ "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa,	"
			+ "			roteiro_.ordem ordemRoteiro, "
			+ "			rota_.ordem ordemRota, "
			+ "			rota_pdv_.ordem ordemRotaPdv ");
		}
		sql.append( 
		  "	    from "
		+ "	        COTA cota_ " 
		+ "	    left outer join "
		+ "	        BOX box1_  "
		+ "	            on cota_.BOX_ID=box1_.ID  "
		+ "	    inner join "
		+ "	        ESTUDO_COTA ec_  "
		+ "	            on cota_.ID=ec_.COTA_ID  "
		+ "	    inner join "
		+ "	        ESTUDO e_  "
		+ "	            on ec_.ESTUDO_ID=e_.ID  "
		+ "	    inner join "
		+ "	        LANCAMENTO lancamento_  "
		+ "	            on e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID  "
		+ "	            and e_.ID=lancamento_.ESTUDO_ID  "
		+ "	    inner join "
		+ "	        PRODUTO_EDICAO pe_  "
		+ "	            on e_.PRODUTO_EDICAO_ID=pe_.ID  "
		+ "	    inner join "
		+ "	        PRODUTO p_  "
		+ "	            on pe_.PRODUTO_ID=p_.ID  "
		+ "	    inner join "
		+ "	        PRODUTO_FORNECEDOR pf_  "
		+ "	            on p_.ID=pf_.PRODUTO_ID  "
		+ "	    inner join "
		+ "	        FORNECEDOR f_  "
		+ "	            on pf_.fornecedores_ID=f_.ID  "
		+ "	    inner join "
		+ "	        PDV pdv_  "
		+ "	            on cota_.ID=pdv_.COTA_ID  "
		+ "	    left outer join "
		+ "        ROTA_PDV rota_pdv_  "
		+ "	            on pdv_.ID=rota_pdv_.PDV_ID    "  
		+ "	    left outer join "
		+ "	        ROTA rota_  "
		+ "	            on rota_pdv_.rota_ID=rota_.ID  "
		+ "	    left outer join "
		+ "	        ROTEIRO roteiro_  "
		+ "	            on rota_.ROTEIRO_ID=roteiro_.ID  "
		+ "	    inner join "
		+ "	        PESSOA pessoa_cota_  "
		+ "	            on cota_.PESSOA_ID=pessoa_cota_.ID  "
		+ "		inner join NOTA_ENVIO_ITEM nei " 
        + "    			on nei.ESTUDO_COTA_ID=ec_.ID "
		+ "	   	where pdv_.ponto_principal = :principal " 
		+ "	    and lancamento_.STATUS not in (:statusNaoEmitiveis) ");
		
		if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			sql.append(
			  "	        and ( "
			+ "	            f_.ID is null  "
			+ "	            or f_.ID in (:idFornecedores) "
			+ "	        )  ");
		}
		
		if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
			if (filtro.getIntervaloCota().getAte() != null) {
				
				sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
				
			} else {
				
				sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
			}
	
		}
		
		if (filtro.getIntervaloBox() != null 
				&& filtro.getIntervaloBox().getDe() != null 
				&&  filtro.getIntervaloBox().getAte() != null) {
			sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
		}
		
		if (filtro.getIdRoteiro() != null){
			sql.append(" and roteiro_.ID=:idRoteiro  ");
		}
		
		if (filtro.getIdRota() != null){
			sql.append(" and rota_.ID=:idRota  ");
		}
		
		if (!filtro.isFiltroEspecial()) {
			sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
		}
		
		if (filtro.getIntervaloMovimento() != null 
				&& filtro.getIntervaloMovimento().getDe() != null 
				&& filtro.getIntervaloMovimento().getAte() != null) {
			sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
		}
		
		sql.append(
		  "	    group by cota_.ID ");
		
		
	}
		
	private void montarQueryCotasComNotasEnvioNaoEmitidas(FiltroConsultaNotaEnvioDTO filtro, StringBuilder sql, boolean isCount) {
		
		if(isCount) {
			sql.append( " select cota_.ID ");
		} else {
			sql.append( " select lancamento_.STATUS as status,  "
				+ "	        cota_.ID as idCota, "
				+ "	        cota_.NUMERO_COTA as numeroCota, "
				+ "	        cota_.BOX_ID as box, "
				+ "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
				+ "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, "
				+ " 		sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE is null,coalesce(ec_.QTDE_EFETIVA,0),if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:gruposFaltaSobra), ec_.QTDE_EFETIVA, 0))) - "
				+ " 		sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposFalta), mec.QTDE,0)) + "			
				+ " 		sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposSobra), mec.QTDE,0)) as exemplares, "	 		
				+ "	        sum(pe_.PRECO_VENDA * ( "
				+ "				if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE is null,coalesce(ec_.QTDE_EFETIVA,0),if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:gruposFaltaSobra), ec_.QTDE_EFETIVA, 0)) - " 
				+ "				if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposFalta), mec.QTDE,0) + 		 "
				+ "				if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposSobra), mec.QTDE,0)) "
				+ "			) as total, " 
				+ "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa,	"
				+ "			roteiro_.ordem ordemRoteiro, "
				+ "			rota_.ordem ordemRota, "
				+ "			rota_pdv_.ordem ordemRotaPdv "); 
		}
		sql.append( "   from "
				+ "	        COTA cota_ " 
				+ "	    left outer join "
				+ "	        BOX box1_  "
				+ "	            on cota_.BOX_ID=box1_.ID  "
				+ "	    inner join "
				+ "	        ESTUDO_COTA ec_  "
				+ "	            on cota_.ID=ec_.COTA_ID  "
				+ "	    inner join "
				+ "	        ESTUDO e_  "
				+ "	            on ec_.ESTUDO_ID=e_.ID  "
				+ "	    inner join "
				+ "	        LANCAMENTO lancamento_  "
				+ "	            on e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID  "
				+ "	            and e_.ID=lancamento_.ESTUDO_ID  "
				+ "	    left join "
				+ "	        MOVIMENTO_ESTOQUE_COTA mec  "
				+ "	            on mec.LANCAMENTO_ID=lancamento_.id "
				+ "	            and mec.COTA_ID=cota_.ID "
				+ "	    left join "
				+ "	        TIPO_MOVIMENTO tipo_mov "
				+ "	            on tipo_mov.ID=mec.TIPO_MOVIMENTO_ID "
				+ "	    inner join "
				+ "	        PRODUTO_EDICAO pe_  "
				+ "	            on e_.PRODUTO_EDICAO_ID=pe_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO p_  "
				+ "	            on pe_.PRODUTO_ID=p_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO_FORNECEDOR pf_  "
				+ "	            on p_.ID=pf_.PRODUTO_ID  "
				+ "	    inner join "
				+ "	        FORNECEDOR f_  "
				+ "	            on pf_.fornecedores_ID=f_.ID  "
				+ "	    inner join "
				+ "	        PDV pdv_  "
				+ "	            on cota_.ID=pdv_.COTA_ID  "
				+ "	    left outer join "
				+ "        ROTA_PDV rota_pdv_  "
				+ "	            on pdv_.ID=rota_pdv_.PDV_ID    "  
				+ "	    left outer join "
				+ "	        ROTA rota_  "
				+ "	            on rota_pdv_.rota_ID=rota_.ID  "
				+ "	    left outer join "
				+ "	        ROTEIRO roteiro_  "
				+ "	            on rota_.ROTEIRO_ID=roteiro_.ID  "
				+ "	    inner join "
				+ "	        PESSOA pessoa_cota_  "
				+ "	            on cota_.PESSOA_ID=pessoa_cota_.ID  "
				+ "		left outer join NOTA_ENVIO_ITEM nei " 
		        + "    			on nei.ESTUDO_COTA_ID=ec_.ID "
				+ "	   	where "
				+ "	    lancamento_.STATUS not in (:statusNaoEmitiveis)  "
				+ "    	and  nei.estudo_cota_id is null "
				+ "		and pdv_.ponto_principal = :principal ");
				
				if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
					sql.append(
					  "	        and ( "
					+ "	            f_.ID is null  "
					+ "	            or f_.ID in (:idFornecedores) "
					+ "	        )  ");
				}
				
				if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
					if (filtro.getIntervaloCota().getAte() != null) {
						
						sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
						
					} else {
						
						sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
					}
			
				}
				
				if (filtro.getIntervaloBox() != null 
						&& filtro.getIntervaloBox().getDe() != null 
						&&  filtro.getIntervaloBox().getAte() != null) {
					sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
				}
				
				if (filtro.getIdRoteiro() != null){
					sql.append(" and roteiro_.ID=:idRoteiro  ");
				}
				
				if (filtro.getIdRota() != null){
					sql.append(" and rota_.ID=:idRota  ");
				}
				
				if (!filtro.isFiltroEspecial()) {
					sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
				}
				
				if (filtro.getIntervaloMovimento() != null 
						&& filtro.getIntervaloMovimento().getDe() != null 
						&& filtro.getIntervaloMovimento().getAte() != null) {
					sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
					sql.append(" and cota_.ID not in (select COTA_ID from COTA_AUSENTE where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
				}
				
				sql.append(" group by cota_.ID ");
	}

	private void montarParametrosFiltroNotasEnvio(
			FiltroConsultaNotaEnvioDTO filtro, Query query, boolean isCount) {
		
		query.setParameter("principal", true);

		query.setParameterList("status", new String[]{StatusLancamento.CONFIRMADO.name(), StatusLancamento.EM_BALANCEAMENTO.name()});
		query.setParameterList("statusNaoEmitiveis", new String[]{StatusLancamento.PLANEJADO.name(), StatusLancamento.FECHADO.name(), StatusLancamento.CONFIRMADO.name(), StatusLancamento.EM_BALANCEAMENTO.name(), StatusLancamento.CANCELADO.name()});
		
		if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			query.setParameterList("idFornecedores", filtro.getIdFornecedores());
		}
		
		
		if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
			
			if (filtro.getIntervaloCota().getAte() != null) {
				query.setParameter("numeroCotaDe", filtro.getIntervaloCota().getDe());
				query.setParameter("numeroCotaAte", filtro.getIntervaloCota().getAte());
			} else {
				query.setParameter("numeroCota", filtro.getIntervaloCota().getDe());
			}
		}
		
		if (filtro.getIntervaloBox() != null 
				&& filtro.getIntervaloBox().getDe() != null 
				&&  filtro.getIntervaloBox().getAte() != null) {
			
			query.setParameter("boxDe", filtro.getIntervaloBox().getDe());
			query.setParameter("boxAte", filtro.getIntervaloBox().getAte());
		}
	
		if (filtro.getIdRoteiro() != null){
			
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
		
		if (filtro.getIdRota() != null){
			
			query.setParameter("idRota", filtro.getIdRota());
		}
		
		if (filtro.getIntervaloMovimento() != null 
				&& filtro.getIntervaloMovimento().getDe() != null 
				&& filtro.getIntervaloMovimento().getAte() != null) {
			query.setParameter("dataDe", filtro.getIntervaloMovimento().getDe());
			query.setParameter("dataAte",filtro.getIntervaloMovimento().getAte());
		}
		
		if (!filtro.isFiltroEspecial()) {
			query.setParameter("roteiroEspecial", TipoRoteiro.ESPECIAL.name());
		}
		
		if(!isCount) {
			
			if (filtro.getPaginacaoVO().getPosicaoInicial()!= null){
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}
			
			if (filtro.getPaginacaoVO().getQtdResultadosPorPagina()!= null){				
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
			
		}
		
	}
	
	private void orderByCotasComNotaEnvioEntre(StringBuilder sql,
			String sortName, String sortOrder) {

		if("numeroCota".equals(sortName)) {
			sql.append(" order by numeroCota " + sortOrder +", notaImpressa ");
		}
		
		if("nomeCota".equals(sortName)) {
			sql.append(" order by  nomeCota " + sortOrder);
		}
				
		if("exemplares".equals(sortName)) {
			sql.append(" order by  exemplares " + sortOrder);
		}
		
		if("total".equals(sortName)) {
			sql.append(" order by  total " + sortOrder);
		}
		
		if("situacaoCadastro".equals(sortName)) {
			sql.append(" order by  cota_.SITUACAO_CADASTRO " + sortOrder);
		}
		
		if("notaImpressa".equals(sortName)) {
			sql.append(" order by  notaImpressa " + sortOrder);
		}
		
		if("roteirizacao".equals(sortName)) {
			
			sql.append(" order by box, ordemRoteiro, ordemRota, ordemRotaPdv " + sortOrder);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<Cota> obterCotasPorFornecedor(Long idFornecedor) {
		
		String queryString = " select cota from Cota cota "
						   + " join fetch cota.fornecedores fornecedores "
						   + " where fornecedores.id = :idFornecedor ";
		
		Query query = this.getSession().createQuery(queryString);
		
		query.setParameter("idFornecedor", idFornecedor);
		
		return new HashSet<Cota>(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaTipoDTO> obterCotaPorTipo(TipoDistribuicaoCota tipoCota, Integer page, Integer rp, String sortname, String sortorder) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.id as idCota, ");
		hql.append(" cota.numeroCota as numCota, ");
		hql.append(" case pessoa.class ");
		hql.append(" when 'F' then pessoa.nome ");
		hql.append(" when 'J' then pessoa.razaoSocial end as nome,");
		hql.append(" endereco.cidade as municipio, ");
		
		hql.append(" concat( ");
		hql.append(" coalesce(endereco.logradouro, ''), "); 
		hql.append(" coalesce(', '  || endereco.numero, ''), ");
		hql.append(" coalesce(' - ' || endereco.bairro, ''), ");
		hql.append(" coalesce(' / ' || endereco.uf, '') ");
		hql.append(" ) as endereco ");
		
		gerarWhereFromObterCotaPorTipo(hql);
		
		hql.append(" group by cota.id ");
		
		gerarOrderByObterCotaPorTipo(hql, sortname, sortorder);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoCota", tipoCota);
		
		query.setParameterList(
			"situacoesCadastro", 
				Arrays.asList(SituacaoCadastro.ATIVO, SituacaoCadastro.SUSPENSO));
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaTipoDTO.class));

		query.setFirstResult( (rp * page) - rp);
		
		query.setMaxResults(rp);
		
		return query.list();
	}
	
	private void gerarWhereFromObterCotaPorTipo(StringBuilder hql) {
		
		hql.append(" from Cota cota ");
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" left join cota.pdvs pdv ");
		hql.append(" left join cota.enderecos enderecoCota ");
		hql.append(" left join enderecoCota.endereco endereco ");
		
		hql.append(" where (pdv.caracteristicas.pontoPrincipal=true or pdv.caracteristicas.pontoPrincipal is null) ");
		hql.append(" and (enderecoCota.principal=true or enderecoCota.principal is null) ");		
		hql.append(" and cota.tipoDistribuicaoCota=:tipoCota ");
		hql.append(" and cota.situacaoCadastro in (:situacoesCadastro) ");
	}

	private void gerarOrderByObterCotaPorTipo(StringBuilder hql,
			String sortname, String sortorder) {
		
		if(sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty())
			return;
		
		if (sortname.equals("numCota")) {
			hql.append(" ORDER BY numCota ");
		} else if (sortname.equals("nome")) {
			hql.append(" ORDER BY nome ");
		} else if (sortname.equals("municipio")) {
			hql.append(" ORDER BY municipio ");
		} else if (sortname.equals("endereco")) {
			hql.append(" ORDER BY endereco ");
		}
		
		if(sortorder.equals("desc"))
			hql.append(" DESC ");
		else 
			hql.append(" ASC ");		
	}

	@Override
	public int obterCountCotaPorTipo(TipoDistribuicaoCota tipoCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct cota.id) ");
		
		gerarWhereFromObterCotaPorTipo(hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoCota", tipoCota);
		
		query.setParameterList(
			"situacoesCadastro", 
				Arrays.asList(SituacaoCadastro.ATIVO, SituacaoCadastro.SUSPENSO));
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp, String sortname, String sortorder) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select  ");
		hql.append(" 		endereco.cidade as municipio, ");
		hql.append(" 		count(cota.id) as qtde ");
				
		gerarWhereFromObterQtdeCotaMunicipio(hql);		
		
		hql.append(" group by endereco.cidade ");	
		
		gerarOrderByObterQtdeCotaMunicipio(hql, sortname, sortorder);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("situacoesCota", Arrays.asList(SituacaoCadastro.PENDENTE, SituacaoCadastro.INATIVO));
				
		query.setResultTransformer(new AliasToBeanResultTransformer(MunicipioDTO.class));

		query.setFirstResult( (rp * page) - rp);
		
		query.setMaxResults(rp);
		
		return query.list();
	}

	private void gerarOrderByObterQtdeCotaMunicipio(StringBuilder hql,
			String sortname, String sortorder) {
		
		if(sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty())
			return;
		
		if (sortname.equals("municipio")) {
			hql.append(" ORDER BY municipio ");
		} else if (sortname.equals("qtde")) {
			hql.append(" ORDER BY qtde ");
		} 
		
		if(sortorder.equals("desc"))
			hql.append(" DESC ");
		else 
			hql.append(" ASC ");	
		
	}
	
	private void gerarWhereFromObterQtdeCotaMunicipio(StringBuilder hql) {
		
		hql.append(" from Cota cota ");
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" left join cota.pdvs pdv ");
		hql.append(" left join cota.enderecos enderecoCota ");
		hql.append(" left join enderecoCota.endereco endereco ");
		
		hql.append(" where pdv.caracteristicas.pontoPrincipal=true ");
		hql.append(" and enderecoCota.principal=true ");
		hql.append(" and cota.situacaoCadastro not in (:situacoesCota) ");
	}
	
	@Override
	public int obterCountQtdeCotaMunicipio() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct endereco.cidade) ");
		
		gerarWhereFromObterQtdeCotaMunicipio(hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("situacoesCota", Arrays.asList(SituacaoCadastro.PENDENTE, SituacaoCadastro.INATIVO));
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCota obterHistoricoTitularidade(Long idCota,
            Long idHistorico) {
        Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico de titularidade não deve ser nulo!");
        String hql = "from HistoricoTitularidadeCota historico where historico.id = :idHistorico and historico.cota.id = :idCota";
        Query query = getSession().createQuery(hql);
        query.setParameter("idHistorico", idHistorico);
        query.setParameter("idCota", idCota);
        return (HistoricoTitularidadeCota) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaFormaPagamento obterFormaPagamentoHistoricoTitularidade(Long idFormaPagto) {
        Validate.notNull(idFormaPagto, "Identificador da forma de pagamento não deve ser nulo!");
        
        String hql = "from HistoricoTitularidadeCotaFormaPagamento where id = :id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id", idFormaPagto);
        return (HistoricoTitularidadeCotaFormaPagamento) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidade(Long idSocio) {
        Validate.notNull(idSocio, "Identificador do sócio não deve ser nulo!");
        
        String hql = "from HistoricoTitularidadeCotaSocio where id = :id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id", idSocio);
        return (HistoricoTitularidadeCotaSocio) query.uniqueResult();
    }

    @Override
	public void ativarCota(Integer numeroCota) {
		
		Query query = 
				this.getSession().createQuery(
						"update Cota set situacaoCadastro = :status where numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("status", SituacaoCadastro.ATIVO);
		
		query.executeUpdate();
	}	
    
    @Override
    public Cota buscarCotaPorID(Long idCota) {
    	
    	String queryString = " select cota from Cota cota "
				   + " left join fetch cota.fornecedores fornecedores "
				   + " where cota.id = :idCota ";

		Query query = this.getSession().createQuery(queryString);
		
		query.setParameter("idCota", idCota);
		
		return (Cota) query.uniqueResult();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemRangeReparte(BigInteger qtdReparteInicial, BigInteger qtdReparteFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.lancamentos as lancamento ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and ");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		
		if (qtdReparteInicial != null && qtdReparteInicial.intValue() >= 0 && qtdReparteFinal != null && qtdReparteFinal.intValue() >= 0 ) {
			//hql.append(" HAVING avg(lancamento.reparte) between :reparteInicial and :reparteFinal");
			
			hql.append("and estoqueProdutoCota.qtdeRecebida >= :reparteInicial and estoqueProdutoCota.qtdeRecebida <= :reparteFinal");
			
			parameters.put("reparteInicial", qtdReparteInicial);
			parameters.put("reparteFinal", qtdReparteFinal);
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemRangeVenda(BigInteger qtdVendaInicial, BigInteger qtdVendaFinal,List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if (qtdVendaInicial != null && qtdVendaInicial.intValue() >= 0 && qtdVendaFinal != null && qtdVendaFinal.intValue() >= 0 ) {
			hql.append(" HAVING avg(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) between :qtdVendaInicial and :qtdVendaFinal");
			parameters.put("qtdVendaInicial", qtdVendaInicial.doubleValue());
			parameters.put("qtdVendaFinal", qtdVendaFinal.doubleValue());
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(BigDecimal percentualVenda, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if (percentualVenda != null && percentualVenda.doubleValue() >= 0) {
			hql.append(" HAVING (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) / sum(estoqueProdutoCota.qtdeRecebida))>= (:percentualVenda / 100)");
			parameters.put("percentualVenda", percentualVenda.intValue());
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasPorNomeOuNumero(CotaDTO cotaDto, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (cotaDto != null ) {
			if (cotaDto.getNumeroCota() != null && !cotaDto.getNumeroCota().equals(0)) {
				hql.append(" cota.numeroCota = :numeroCota and ");
				parameters.put("numeroCota", cotaDto.getNumeroCota());
			}
			else if (cotaDto.getNomePessoa() != null && !cotaDto.getNomePessoa().isEmpty()) {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa and ");
				parameters.put("nomePessoa", cotaDto.getNomePessoa());
			}
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasPorComponentes(ComponentesPDV componente, String elemento, List<ProdutoEdicaoDTO> listProdutoEdicaoDto,	boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		StringBuilder whereParameter = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		switch (componente) {
		case TIPO_PONTO_DE_VENDA:
			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.segmentacao as segmentacao ");
			hql.append(" LEFT JOIN segmentacao.tipoPontoPDV tipoPontoPDV ");

			whereParameter.append(" tipoPontoPDV.id = :codigoTipoPontoPDV AND");
			parameters.put("codigoTipoPontoPDV", Long.parseLong(elemento));

			break;
		case AREA_DE_INFLUENCIA:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.segmentacao as segmentacao ");
			hql.append(" LEFT JOIN segmentacao.areaInfluenciaPDV areaInfluenciaPDV ");

			whereParameter.append(" areaInfluenciaPDV.id = :codigoAreaInfluenciaPDV AND ");
			parameters.put("codigoAreaInfluenciaPDV", Long.parseLong(elemento));
			break;

		case BAIRRO:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.enderecos enderecosPdv ");
			hql.append(" LEFT JOIN enderecosPdv.endereco endereco ");
			
			whereParameter.append(" enderecosPdv.principal = true and endereco.bairro = :bairroPDV AND ");
			parameters.put("bairroPDV", elemento);

			break;
		case DISTRITO:
			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.enderecos enderecosPdv ");
			hql.append(" LEFT JOIN enderecosPdv.endereco endereco ");

			whereParameter.append(" enderecosPdv.principal = true and endereco.uf = :ufSigla AND");
			parameters.put("ufSigla", elemento);

			break;
		case GERADOR_DE_FLUXO:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.geradorFluxoPDV geradorFluxoPdvs ");
			hql.append(" INNER JOIN geradorFluxoPdvs.principal geradorPrincipal ");

			whereParameter.append(" geradorPrincipal.id = :idGeradorFluxoPDV AND ");
			parameters.put("idGeradorFluxoPDV",	Long.parseLong(elemento));

			break;
		case COTAS_A_VISTA:
			hql.append(" LEFT JOIN cota.parametroCobranca as parametroCobranca ");
			
			whereParameter.append(" parametroCobranca.tipoCota = :tipoCota AND");
			parameters.put("tipoCota",TipoCota.A_VISTA);
			
			break;
		case COTAS_NOVAS_RETIVADAS:
			whereParameter.append(" cota.id in (SELECT cotaBase.cota.id FROM CotaBase as cotaBase) AND ");
			
			break;
		case REGIAO:
			whereParameter.append(" cota.id in (SELECT registro.cota.id FROM RegistroCotaRegiao as registro WHERE regiao.id = :regiaoId) AND ");
			parameters.put("regiaoId",Long.parseLong(elemento));
			
			break;
		default:
			break;
		}
		
		
		hql.append(" WHERE ");
		
        // adiciona os parâmetros feitos dentro so switch
		hql.append(whereParameter.toString());
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliseHistoricoDTO> buscarHistoricoCotas(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, List<Cota> cotas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as statusCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
		hql.append(" count(DISTINCT pdvs) as qtdPdv, ");
		hql.append(" avg(estoqueProdutoCota.qtdeRecebida) as reparteMedio, ");
		hql.append(" avg(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) as vendaMedia, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" produto.codigo as codigoProduto ");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		//hql.append(" LEFT JOIN estoqueProdutoCota.movimentos as movimentos ");
		//hql.append(" LEFT JOIN movimentos.tipoMovimento as tipoMovimento");
		//hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pdvs as pdvs ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		//hql.append(" tipoMovimento.id = 21 and ");
		boolean useAnd = false;
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
            // Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

            // Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
			
			useAnd = true;
		}
		
		if (cotas != null && cotas.size() != 0) {
			
			hql.append(useAnd ? " and " : " where ");
			
            // Populando o in ('','') do código produto
			hql.append(" cota.numeroCota in ( ");
			for (int i = 0; i < cotas.size(); i++) {
				
				hql.append(cotas.get(i).getNumeroCota());
				
				if (cotas.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");
			
			useAnd = true;
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseHistoricoDTO.class));
		
		return query.list();
	}

	@Override
	public HistoricoVendaPopUpCotaDto buscarCota(Integer numero) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" rankingFaturamento.id as rankId, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
		hql.append(" cota.tipoDistribuicaoCota as tipoDistribuicaoCota, ");
		hql.append(" rankingFaturamento.faturamento as faturamento, ");
		hql.append(" max(rankingFaturamento.dataGeracaoRank) as  dataGeracao ");
		
		hql.append(" FROM RankingFaturamento rankingFaturamento ");
		hql.append(" RIGHT JOIN rankingFaturamento.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		hql.append(" cota.numeroCota = ");
		hql.append(numero);
		
		hql.append(" GROUP BY rankingFaturamento.cota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistoricoVendaPopUpCotaDto.class));
		
		return (HistoricoVendaPopUpCotaDto) query.uniqueResult();
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer buscarNumeroCotaPorId(Long idCota) {

		Criteria criteria = getSession().createCriteria(Cota.class);
		
		criteria.add(Restrictions.eq("id", idCota));
		
		criteria.setProjection(Projections.property("numeroCota"));
		
		return (Integer) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdsCotasPorMunicipio(String municipio){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.id ");
				
		hql.append(" from Cota cota ");
		hql.append(" join cota.enderecos enderecoCota ");
		hql.append(" join enderecoCota.endereco endereco ");
		
		hql.append(" where enderecoCota.principal=true ");
		hql.append(" and endereco.cidade = :cidade ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("cidade", municipio);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterCotaPorProdutoEdicaoData(FiltroMapaAbastecimentoDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct estudoCota.cota as cota");
				
		hql.append(" from EstudoCota estudoCota ");
		hql.append(" join estudoCota.estudo estudo ");
		
		hql.append(" join estudoCota.cota cota ");
		hql.append(" join estudo.produtoEdicao pe ");
		hql.append(" join pe.produto p ");
		hql.append(" join pe.lancamentos l ");
		hql.append(" where ");
		
		for (int i = 0; i < filtro.getCodigosProduto().size(); i++ ) {
			hql.append(" p.codigo = :codigoProduto_" + i + " and ");
		}
		hql.append(" pe.numeroEdicao = :numeroEdicao and ");
		hql.append(" ( l.status = :balanceado or l.status = :expedido ) and ");
		hql.append(" l.dataLancamentoPrevista = :dataPrevista ");
		
		
		/*
		hql.append(" where estudo.dataLancamento in ( ");
		hql.append(" select lancamento.dataLancamentoPrevista from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" where ");
		
		for (int i = 0; i < filtro.getCodigosProduto().size(); i++ ) {
			hql.append(" produto.codigo = :codigoProduto_" + i + " and ");
		}
		hql.append(" produtoEdicao.numeroEdicao = :numeroEdicao and ");
		hql.append(" ( lancamento.status = :balanceado or lancamento.status = :expedido ) and ");
		hql.append(" lancamento.dataLancamentoPrevista = :dataPrevista ");
		
		hql.append(" )");
		*/
		
		
		Query query = this.getSession().createQuery(hql.toString());
		
		for(int i = 0; i < filtro.getCodigosProduto().size(); i++ ) {
			query.setParameter("codigoProduto_" + i, filtro.getCodigosProduto().get(i));
		}
		query.setParameter("numeroEdicao", filtro.getEdicaoProduto());
		query.setParameter("balanceado", StatusLancamento.BALANCEADO);
		query.setParameter("expedido", StatusLancamento.EXPEDIDO);
		query.setParameter("dataPrevista", filtro.getDataDate());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
		
		return query.list();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> obterCotasSemRoteirizacao(Intervalo<Integer> intervaloCota, Intervalo<Date> intervaloDataLancamento,
			Intervalo<Date> intervaloDateRecolhimento) {
		
		StringBuilder hql = new StringBuilder("")
			.append(" select ")
			.append(" c.NUMERO_COTA as numeroCota, ")
        	.append(" coalesce(p.NOME, p.RAZAO_SOCIAL, p.NOME_FANTASIA) as nomePessoa ")
        	.append(" from COTA c ")
        	.append(" inner join PESSOA p on c.PESSOA_ID=p.ID ")
        	.append(" inner join ESTUDO_COTA ec on c.ID=ec.COTA_ID ")
    		.append(" inner join ESTUDO e on ec.ESTUDO_ID=e.ID ");
        	
    	if (intervaloDataLancamento != null) {
    		hql .append(" inner join LANCAMENTO l on e.PRODUTO_EDICAO_ID=l.PRODUTO_EDICAO_ID and e.DATA_LANCAMENTO=l.DATA_LCTO_PREVISTA ");
    	}
    	
    	if (intervaloDateRecolhimento != null) {
    		hql .append(" inner join chamada_encalhe_cota cec on cec.cota_id = c.id ")
    			.append(" inner join chamada_encalhe ce on ce.id = cec.chamada_encalhe_id and ce.produto_edicao_id = e.produto_edicao_id ");
    	}
    	
    	hql	.append(" where 1=1")
    		.append(" and ( ")
        	.append(" 	c.NUMERO_COTA not in ( ")
        	.append("     	select distinct c.NUMERO_COTA ")
        	.append("     	from ROTEIRIZACAO rot ")
        	.append("     	inner join ROTEIRO r on rot.ID=r.ROTEIRIZACAO_ID ") 
        	.append("     	inner join ROTA rota on r.ID=rota.ROTEIRO_ID ") 
        	.append("     	inner join ROTA_PDV rp on rota.ID=rp.ROTA_ID ") 
        	.append("     	inner join PDV pdv on rp.PDV_ID=pdv.ID ") 
        	.append("     	inner join COTA c on pdv.COTA_ID=c.ID ")
        	.append("     	inner join ESTUDO_COTA ec on c.ID=ec.COTA_ID ") 
        	.append("     	inner join ESTUDO e on ec.ESTUDO_ID=e.ID ");
	    	
    	hql	.append(" 	where rot.box_id is not null ) ")
    		.append(" ) ");
    	
    	if (intervaloCota != null && intervaloCota.getAte() != null && intervaloCota.getDe() != null) {
			hql	.append(" and ( ")
				.append(" 	c.NUMERO_COTA between :de and :ate ")
				.append(" ) ");
		}
    	
    	if (intervaloDataLancamento != null) {
	    	hql	.append(" and ( ")
	    		.append(" 	l.DATA_LCTO_DISTRIBUIDOR between :dataLancamentoDe and :dataLancamentoAte ")
	    		.append(" ) ");
    	}
    	
    	if (intervaloDateRecolhimento != null) {
    		hql	.append(" and ( ")
    			.append(" 	ce.data_recolhimento between :dataRecolhimentoDe and :dataRecolhimentoAte ")
    			.append(" ) ");
    	}
    	
    	hql	.append(" and ( ")
    		.append(" 	c.SITUACAO_CADASTRO not in (:inativo, :pendente) ")
    		.append(" ) ")
    		.append(" group by c.NUMERO_COTA ")
    		.append(" order by c.NUMERO_COTA ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.setParameter("inativo", SituacaoCadastro.INATIVO.name());
		query.setParameter("pendente", SituacaoCadastro.PENDENTE.name());
		
		if (intervaloCota != null && intervaloCota.getAte() != null && intervaloCota.getDe() != null) {
			query.setParameter("de", intervaloCota.getDe());
			query.setParameter("ate", intervaloCota.getAte());
		}
		
		if (intervaloDataLancamento != null) {
			query.setParameter("dataLancamentoDe", intervaloDataLancamento.getDe());
			query.setParameter("dataLancamentoAte", intervaloDataLancamento.getAte());
		}
		
		if (intervaloDateRecolhimento != null) {
			query.setParameter("dataRecolhimentoDe", intervaloDateRecolhimento.getDe());
			query.setParameter("dataRecolhimentoAte", intervaloDateRecolhimento.getAte());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		query.setCacheable(true);
		query.addScalar("nomePessoa", StandardBasicTypes.STRING);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		
		return query.list();
	}

	@Override
	public BigDecimal obterTotalDividaCotasSujeitasSuspensao(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder("SELECT SUM(COALESCE(total.valor,0)) FROM (SELECT  ");
		
		hql.append("( SELECT SUM(round(COALESCE(D.VALOR,0), 2)) ")
						  .append("	FROM DIVIDA D ")
						  .append(" JOIN COBRANCA c on (c.DIVIDA_ID=d.ID) ")
						  .append("	WHERE D.COTA_ID = COTA_.ID ")
						  .append(" AND c.DT_PAGAMENTO is null ")
						  .append("	AND D.STATUS in (:statusDividaEmAbertoPendente) ")
						  .append("	AND C.DT_VENCIMENTO < :dataOperacao ) as valor ");
				
		this.setFromWhereCotasSujeitasSuspensao(hql);
		
		hql.append(") as total ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		this.setParametrosCotasSujeitasSuspensao(query, dataOperacaoDistribuidor);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	
	    /**
     * Obtem Cotas do tipo À Vista, com data de alteração de status menor que a
     * data atual
     * 
     * @param data
     * @return List<Cota>
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotasTipoAVista(Date data){
	    
	    StringBuilder hql = new StringBuilder("select c ")
	
	       .append(" from Cota c ")
	       
	       .append(" where c.tipoCota = :tipoCota ")
	    
	       .append(" and (c.alteracaoTipoCota is null or c.alteracaoTipoCota < :data) ");
	    
	    Query query = this.getSession().createQuery(hql.toString());
	    
	    query.setParameter("tipoCota", TipoCota.A_VISTA);
	    
	    query.setParameter("data", data);
	    
	    return query.list();
	}

	@Override
	public List<CotaDTO> obterCotasPorNomeAutoComplete(String nome) {
		
		List<?> lista = super.getSession().createSQLQuery("select c.ID, c.NUMERO_COTA, p.NOME, c.SITUACAO_CADASTRO from COTA c join PESSOA p on p.ID = c.PESSOA_ID where p.nome like ?")
			.addScalar("ID", LongType.INSTANCE).addScalar("NUMERO_COTA", IntegerType.INSTANCE)
			.addScalar("NOME", StringType.INSTANCE).addScalar("SITUACAO_CADASTRO", StringType.INSTANCE)
			.setParameter(0, "%"+ nome +"%").setMaxResults(10).list();
		Object[] retorno = lista.toArray();
		List<CotaDTO> cotas = new ArrayList<>();
		for (int i = 0; i < retorno.length; i++) {
		    CotaDTO cota = new CotaDTO();
		    cota.setIdCota((Long)((Object[])retorno[i])[0]);
		    cota.setNumeroCota((Integer)((Object[])retorno[i])[1]);
		    cota.setNomePessoa((String)((Object[])retorno[i])[2]);
		    cota.setStatus(SituacaoCadastro.valueOf((String)((Object[])retorno[i])[3]));
		    cotas.add(cota);
		}
		return cotas;
	}

	@Override
	public boolean cotaVinculadaCotaBase(Long idCota) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select count(*) from CotaBase");
		hql.append(" where cota.id = :idCota");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);

		Long count = (Long)query.uniqueResult();

		return (count > 0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> numeroCotaExiste(
			TipoDistribuicaoCota tipoDistribuicaoCota, Integer... cotaIdArray) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("select c.NUMERO_COTA ");
		hql.append("  from cota c ");
		hql.append(" where c.NUMERO_COTA in (:cotaIDList)");
		hql.append("   and c.SITUACAO_CADASTRO in (upper(:situacaoCadastroAtivo), upper(:situacaoCadastroSuspenso)) ");
		hql.append("   and c.TIPO_DISTRIBUICAO_COTA = upper(:tipoDistribuicaoCota) ");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setParameterList("cotaIDList", cotaIdArray);
		query.setParameter("situacaoCadastroAtivo", SituacaoCadastro.ATIVO.toString());
		query.setParameter("situacaoCadastroSuspenso", SituacaoCadastro.SUSPENSO.toString());
		query.setParameter("tipoDistribuicaoCota", tipoDistribuicaoCota.toString());

		return query.list();
	}

	@Override
	public Cota obterCotaComBaseReferencia(Long idCota) {
		
		Cota cota = (Cota)super.getSession().load(Cota.class,idCota);

		BaseReferenciaCota base = (BaseReferenciaCota)super.getSession().createCriteria(BaseReferenciaCota.class).add(Restrictions.eq("cota.id", idCota)).uniqueResult();
		cota.setBaseReferenciaCota(base);

		return cota;
	}

	@Override
	public TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(
			Integer numeroCota) {
		
		StringBuilder query = new StringBuilder();
		query.append("select tipoDistribuicaoCota from Cota where numeroCota = :numeroCota and situacaoCadastro = 'Ativo'");

		Query q = getSession().createQuery(query.toString());

		q.setParameter("numeroCota", numeroCota);

		return (TipoDistribuicaoCota)q.uniqueResult();
	}

	@Override
	public int obterCotasAtivas() {
		
		return ((Number) getSession().createCriteria(Cota.class)
				.add(Restrictions.eq("situacaoCadastro", SituacaoCadastro.ATIVO))
				.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}

	@Override
	public CotaDTO buscarCotaPorNumero(Integer numeroCota, String codigoProduto) {
		
		StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("  cota.numero_cota numeroCota, ");
        sql.append("  coalesce(pe.nome, pe.razao_social, pe.nome_fantasia, '') nomePessoa, ");
        sql.append("  cota.tipo_distribuicao_cota tipoCota, ");
        sql.append("  rks.qtde qtdeRankingSegmento, ");
        sql.append("  rkf.faturamento faturamento, ");
        sql.append("  rkf.data_geracao_rank dataGeracaoRank, ");
        sql.append("  mix.reparte_min mixRepMin, ");
        sql.append("  mix.reparte_max mixRepMax, ");
        sql.append("  u.nome nomeUsuario, ");
        sql.append("  mix.datahora mixDataAlteracao, ");
        sql.append("  fx.data_hora  fxDataAlteracao, ");
        sql.append("  fx.ed_inicial fxEdicaoInicial, ");
        sql.append("  fx.ed_final fxEdicaoFinal, ");
        sql.append("  fx.ed_atendidas fxEdicoesAtendidas, ");
        sql.append("  fx.qtde_edicoes fxQuantidadeEdicoes, ");
        sql.append("  fx.qtde_exemplares fxQuantidadeExemplares ");
        sql.append(" from cota");
        sql.append("   left join pessoa pe on pe.id = cota.pessoa_id");
        sql.append("   left join produto p on p.codigo = :codigoProduto");
        sql.append("   left join ranking_segmento rks on rks.cota_id = cota.id and p.tipo_segmento_produto_id = rks.tipo_segmento_produto_id and rks.data_geracao_rank = (select max(data_geracao_rank) from ranking_segmento)");
        sql.append("   left join ranking_faturamento rkf on rkf.cota_id = cota.id and rkf.data_geracao_rank = (select max(data_geracao_rank) from ranking_faturamento)");
        sql.append("   left join mix_cota_produto mix on mix.id_cota = cota.id and p.codigo_icd = mix.codigo_icd");
        sql.append("   left join usuario u on u.id = mix.id_usuario ");
        sql.append("   left join fixacao_reparte fx on fx.id_cota = cota.id and p.codigo_icd = fx.codigo_icd ");
        sql.append(" where cota.numero_cota = :numeroCota ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("codigoProduto", codigoProduto);
        query.setParameter("numeroCota", numeroCota);

        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        return (CotaDTO) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaEstudo> getInformacoesCotaEstudo(ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct ");
		sql.append("       c.id id, ");
		sql.append("       c.numero_cota numeroCota, ");
		sql.append("       c.situacao_cadastro status, ");
		sql.append("       mcp.reparte_max intervaloMaximo, ");
		sql.append("       mcp.reparte_min intervaloMinimo, ");
		sql.append("       c.tipo_distribuicao_cota tipoDistribuicao, ");
		sql.append("       fr.qtde_exemplares reparteFixado, ");
		sql.append("       (case when mcp.id is not null then 1 else 0 end) mix, ");
		sql.append("       (case when snr.id is not null then 1 else 0 end) cotaNaoRecebeSegmento, ");
		sql.append("       (case when ep.id is not null then 1 else 0 end) cotaExcecaoSegmento, ");
		sql.append("       (case when cnr.id is not null then 1 else 0 end) cotaNaoRecebeClassificacao ");
		sql.append("  from cota c ");
		sql.append("  join produto p ON p.id = :produto_id ");
		sql.append("  join produto_edicao pe ON pe.produto_id = p.id and pe.numero_edicao = :numero_edicao ");
		sql.append("  left join mix_cota_produto mcp ON mcp.id_cota = c.id and mcp.codigo_icd = p.codigo_icd ");
		sql.append("  left join fixacao_reparte fr ON fr.id_cota = c.id and fr.codigo_icd = p.codigo_icd ");
		sql.append("   and ((pe.numero_edicao between fr.ed_inicial and fr.ed_final) ");
		sql.append("    or (coalesce(fr.ed_inicial, 0) = 0 and coalesce(fr.ed_final, 0) = 0 ");
		sql.append("   and coalesce(fr.ed_atendidas, 0) < coalesce(fr.qtde_edicoes, 0))) ");
		sql.append("  left join segmento_nao_recebido snr ON snr.cota_id = c.id ");
		sql.append("   and snr.tipo_segmento_produto_id = p.tipo_segmento_produto_id ");
		sql.append("  left join excecao_produto_cota ep ON ep.cota_id = c.id ");
		sql.append("   and ep.produto_id = p.id and ep.tipo_excecao = 'SEGMENTO' ");
		sql.append("  left join classificacao_nao_recebida cnr ON cnr.cota_id = c.id ");
		sql.append("   and cnr.tipo_classificacao_produto_id = pe.tipo_classificacao_produto_id ");
		sql.append(" where c.situacao_cadastro in ('ATIVO' , 'SUSPENSO') ");
		sql.append(" order by c.id ");

		Query query = getSession().createSQLQuery(sql.toString())
			.addScalar("id", StandardBasicTypes.LONG)
			.addScalar("numeroCota", StandardBasicTypes.INTEGER)
			.addScalar("status", StandardBasicTypes.STRING)
			.addScalar("intervaloMaximo", StandardBasicTypes.BIG_INTEGER)
			.addScalar("intervaloMinimo", StandardBasicTypes.BIG_INTEGER)
			.addScalar("tipoDistribuicao", StandardBasicTypes.STRING)
			.addScalar("reparteFixado", StandardBasicTypes.BIG_INTEGER)
			.addScalar("mix", StandardBasicTypes.BOOLEAN)
			.addScalar("cotaNaoRecebeSegmento", StandardBasicTypes.BOOLEAN)
			.addScalar("cotaExcecaoSegmento", StandardBasicTypes.BOOLEAN)
			.addScalar("cotaNaoRecebeClassificacao", StandardBasicTypes.BOOLEAN);

		query.setParameter("produto_id", produtoEdicao.getProduto().getId());
		query.setParameter("numero_edicao", produtoEdicao.getNumeroEdicao());

		query.setResultTransformer(new AliasToBeanResultTransformer(CotaEstudo.class));
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaDTO> buscarCotasHistorico(
			List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();

    	StringBuilder hql = new StringBuilder();

    	hql.append(" SELECT ");

    	hql.append(" cota.numeroCota as numeroCota, ");
    	hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");

    	hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
    	hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
    	hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
    	hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
    	hql.append(" LEFT JOIN cota.pessoa as pessoa ");

    	hql.append(" WHERE ");

    	if (cotasAtivas) {
    	    hql.append(" cota.situacaoCadastro = :statusCota and");
    	    parameters.put("statusCota", SituacaoCadastro.ATIVO);
    	}

    	if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {

    		hql.append(" produto.codigo in (:produtoCodigoList) and ");
    		hql.append(" produtoEdicao.numeroEdicao in (:produtoEdicaoNumeroList)");
    		
    	//	parameters.put("produtoCodigoList", ListUtils.getValuePathList("codigoProduto", listProdutoEdicaoDto));
    	//	parameters.put("produtoEdicaoNumeroList", ListUtils.getValuePathList("numeroEdicao", listProdutoEdicaoDto));
    		
    	 }

    	hql.append(" GROUP BY cota.numeroCota ");

    	Query query = super.getSession().createQuery(hql.toString());
    	
    	this.setParameters(query, parameters);
    	
    	if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
    		List<String> listCodProduto = new ArrayList<>();
    		List<Long> listNumEdicao = new ArrayList<>();
    		
    		for (ProdutoEdicaoDTO listProduto : listProdutoEdicaoDto) {
				listCodProduto.add(listProduto.getCodigoProduto());
				listNumEdicao.add(listProduto.getNumeroEdicao());
			}
    		
    		query.setParameterList("produtoCodigoList", listCodProduto);
    		query.setParameterList("produtoEdicaoNumeroList", listNumEdicao);
    	}

    	query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));

    	return query.list();
	}
	
	public SituacaoCadastro obterSituacaoCadastroCota(Integer numeroCota) {
		
		Query query = 
			this.getSession().createQuery(
				"select situacaoCadastro from Cota where numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		
		return (SituacaoCadastro) query.uniqueResult();
	}

	@Override
	public Long obterIdPorNumeroCota(Integer numeroCota) {
		
		Query query = this.getSession().createQuery("select id from Cota where numeroCota = :numeroCota");
		query.setParameter("numeroCota", numeroCota);
		
		return (Long) query.uniqueResult();
	}

	@Override
	public CotaVO obterDadosBasicosCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" c.numeroCota as numero, ")
		   .append(" case when p.cnpj is not null then concat(p.razaoSocial, ' (', :siglaPJ, ')') else concat(p.nome, ' (', :siglaPF, ')') end as nome ")
		   .append(" from Cota c ")
		   .append(" join c.pessoa p ")
		   .append(" where c.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("siglaPJ", "PJ");
		query.setParameter("siglaPF", "PF");
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaVO.class));
		
		return (CotaVO) query.uniqueResult();
	}
	
	@Override
	public TipoDistribuicaoCota obterTipoDistribuicao(Long idCota){
		
		Query query = 
			this.getSession().createQuery(
				"select tipoDistribuicaoCota from Cota where id = :idCota");
		
		query.setParameter("idCota", idCota);
		
		return (TipoDistribuicaoCota) query.uniqueResult();
	}
}
