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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ParametroDistribuicaoEntregaCotaDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
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
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.QueryUtil;

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
    
    
    public Cota selectForUpdate(Long numeroCota) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT C.* ");
		
		hql.append(" FROM COTA C ");
		
		hql.append(" WHERE C.NUMERO_COTA = :numeroCota FOR UPDATE ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		((org.hibernate.SQLQuery)query).addEntity(Cota.class);
		
		return (Cota) query.uniqueResult();
		
	}
    
    @Override
    public Cota obterPorNumeroDaCota(final Integer numeroCota) {
        
        final Query query = this.getSession().createQuery("select c from Cota c join fetch c.pessoa where c.numeroCota = :numeroCota");
        
        query.setParameter("numeroCota", numeroCota);
        
        return (Cota) query.uniqueResult();
    }
    
    @Override
    public Cota obterPorNumerDaCota(final Integer numeroCota) {
        
        return this.obterPorNumerDaCota(numeroCota, Arrays.asList(SituacaoCadastro.ATIVO, SituacaoCadastro.SUSPENSO));
    }
    
    @Override
    public Cota obterPorNumerDaCota(final Integer numeroCota, List<SituacaoCadastro> situacaoCadastro) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        
        criteria.add(Restrictions.eq("numeroCota", numeroCota));
        criteria.add(Restrictions.in("situacaoCadastro", situacaoCadastro));
        
        criteria.setMaxResults(1);
        
        return (Cota) criteria.uniqueResult();
    }
    
    @Override
    public Cota obterPorPDV(final Long idPDV) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        
        criteria.createAlias("pdvs", "pdv");
        
        criteria.add(Restrictions.eq("pdv.id", idPDV));
        
        return (Cota) criteria.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Cota> obterCotasPorNomePessoa(final String nome) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        
        criteria.createAlias("pessoa", "pessoa");
        
        criteria.add(Restrictions.or(Restrictions.ilike("pessoa.nome", nome, MatchMode.ANYWHERE), Restrictions.ilike(
                "pessoa.razaoSocial", nome, MatchMode.ANYWHERE)));
        
        return criteria.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Cota> obterPorNome(final String nome) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        
        criteria.createAlias("pessoa", "pessoa");
        
        criteria.add(Restrictions.or(Restrictions.like("pessoa.nome", nome, MatchMode.ANYWHERE), Restrictions.like(
                "pessoa.razaoSocial", nome, MatchMode.ANYWHERE)));
        
        return criteria.list();
    }
    
    /**
     * @see br.com.abril.nds.repository.CotaRepository#obterEnderecosPorIdCota(java.lang.Long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(final Long idCota) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select enderecoCota.id as id, enderecoCota.endereco as endereco, ").append(
                " enderecoCota.principal as enderecoPrincipal, ").append(" enderecoCota.tipoEndereco as tipoEndereco ")
                .append(" from EnderecoCota enderecoCota ").append(" where enderecoCota.cota.id = :idCota ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        ResultTransformer resultTransformer = null;
        
        try {
            resultTransformer = new AliasToBeanConstructorResultTransformer(EnderecoAssociacaoDTO.class.getConstructor(
                    Long.class, Endereco.class, boolean.class, TipoEndereco.class));
        } catch (final Exception e) {
            final String message = "Erro criando result transformer para classe: "
                    + EnderecoAssociacaoDTO.class.getName();
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
        
        query.setResultTransformer(resultTransformer);
        
        query.setParameter("idCota", idCota);
        
        return query.list();
    }
    
    @Override
    public TelefoneCota obterTelefonePorTelefoneCota(final Long idTelefone, final Long idCota) {
        
        final StringBuilder hql = new StringBuilder("select t from TelefoneCota t ");
        hql.append(" where t.telefone.id = :idTelefone ").append(" and   t.cota.id   = :idCota");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idTelefone", idTelefone);
        query.setParameter("idCota", idCota);
        query.setMaxResults(1);
        
        return (TelefoneCota) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(final String sortOrder, final String sortColumn,
            final Integer inicio, final Integer rp, final Date dataOperacao) {
        
        final StringBuilder hqlConsignado = new StringBuilder();
        hqlConsignado.append(" SELECT SUM(")
                     .append(" COALESCE(MOVIMENTOCOTA.PRECO_COM_DESCONTO, PRODEDICAO.PRECO_VENDA, 0) ")
                     .append(" *(CASE WHEN TIPOMOVIMENTO.OPERACAO_ESTOQUE='ENTRADA' THEN MOVIMENTOCOTA.QTDE ELSE MOVIMENTOCOTA.QTDE * -1 END)) ")
                     .append(" FROM MOVIMENTO_ESTOQUE_COTA MOVIMENTOCOTA ")
                     .append(" LEFT JOIN LANCAMENTO LCTO on (MOVIMENTOCOTA.LANCAMENTO_ID=LCTO.ID AND LCTO.STATUS <> :statusRecolhido) ")
                     .append(" JOIN PRODUTO_EDICAO PRODEDICAO ON(MOVIMENTOCOTA.PRODUTO_EDICAO_ID=PRODEDICAO.ID)  ")
                     .append(" JOIN TIPO_MOVIMENTO TIPOMOVIMENTO ON(MOVIMENTOCOTA.TIPO_MOVIMENTO_ID = TIPOMOVIMENTO.ID)  ")
                     .append(" WHERE MOVIMENTOCOTA.COTA_ID = COTA_.ID ")
                     .append(" AND (MOVIMENTOCOTA.STATUS_ESTOQUE_FINANCEIRO IS NULL ")
                     .append(" OR MOVIMENTOCOTA.STATUS_ESTOQUE_FINANCEIRO =:statusEstoqueFinanceiro) ")
                     .append(" AND TIPOMOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE not in (:tipoMovimentoEstorno) ")
                     .append(" AND MOVIMENTOCOTA.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
        
        final StringBuilder hqlDividaAcumulada = new StringBuilder();
        hqlDividaAcumulada.append(" SELECT SUM(COALESCE(D.VALOR,0)) ")
                          .append("	FROM DIVIDA D ")
                          .append(" JOIN COBRANCA c on (c.DIVIDA_ID=d.ID) ")
                          .append("	WHERE D.COTA_ID = COTA_.ID ")
                          .append(" AND c.DT_PAGAMENTO is null ")
                          .append("	AND D.STATUS in (:statusDividaEmAbertoPendente) ")
                          .append("	AND C.DT_VENCIMENTO < :dataOperacao ");
        
        final StringBuilder sql = new StringBuilder();
        sql.append(" SELECT geral.IDCOTA, ")
           .append(" geral.numCota, ")
           .append(" geral.nome, ")
           .append(" geral.RAZAOSOCIAL, ")
           .append(" geral.DATAABERTURA, ")
           .append(" geral.dividaAcumulada, ")
           .append(" ( " + hqlConsignado.toString().replaceAll("COTA_.ID","geral.IDCOTA") + ") AS vlrConsignado, ")
           .append(" (select SUM(ec_.QTDE_EFETIVA * pe_.PRECO_VENDA) from ")
           .append(" ESTUDO_COTA ec_ ")
           .append(" join ESTUDO e_ on ec_.ESTUDO_ID = e_.ID ")
           .append(" join LANCAMENTO lancamento_ force index (ndx_status) on (e_.PRODUTO_EDICAO_ID = lancamento_.PRODUTO_EDICAO_ID and e_.ID = lancamento_.ESTUDO_ID AND lancamento_.DATA_LCTO_DISTRIBUIDOR=:dataOperacao) ")
           .append(" join PRODUTO_EDICAO pe_ on e_.PRODUTO_EDICAO_ID = pe_.ID ")
           .append(" WHERE ec_.cota_ID=geral.IDCOTA ")
           .append(" AND (lancamento_.STATUS is null OR lancamento_.STATUS not in (:statusNaoEmitiveis)) ")
           .append(") as vlrReparte, ")
           // faturamento
           .append("       (SELECT SUM(")
           .append("           (EPC.QTDE_RECEBIDA - EPC.QTDE_DEVOLVIDA) * MOVIMENTOCOTA.PRECO_COM_DESCONTO) / :intervalo ")
           .append("       FROM ESTOQUE_PRODUTO_COTA EPC ")
           .append("       JOIN MOVIMENTO_ESTOQUE_COTA MOVIMENTOCOTA ON (MOVIMENTOCOTA.ESTOQUE_PROD_COTA_ID = EPC.ID) ")
           .append("       WHERE EPC.COTA_ID = geral.IDCOTA ")
           .append("       AND MOVIMENTOCOTA.DATA BETWEEN :dataOperacaoIntervalo AND :dataOperacao ")
           .append("       ) AS faturamento, ")
           // percDivida = dividaAcumulada / vlrConsignado
           .append("       ((")
           .append(hqlDividaAcumulada.toString().replaceAll("COTA_.ID","geral.IDCOTA"))
           .append("       ) / ")
           .append("       (")
           .append(hqlConsignado.toString().replaceAll("COTA_.ID","geral.IDCOTA"))
           .append("       ) * 100) as percDivida, ")
           .append("       COALESCE(DATEDIFF(DATE_ADD(:dataOperacao, INTERVAL -1 DAY), ")
           .append("               (SELECT MIN(D.DATA) FROM DIVIDA D JOIN COBRANCA c on (c.DIVIDA_ID=D.ID) WHERE D.COTA_ID = geral.IDCOTA ")
           .append("                                                 AND D.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("                                                 AND D.DATA <= :dataOperacao AND c.DT_PAGAMENTO is null) ")
           .append("       ),0) AS diasAberto ")
           
           .append(" from ( ")
           
           .append("SELECT COTA_.ID AS IDCOTA, ")
           .append("		COTA_.NUMERO_COTA AS numCota, ")
           .append("		PESSOA_.NOME AS nome, ")
           .append("		PESSOA_.RAZAO_SOCIAL AS RAZAOSOCIAL, ")
           
           // divida acumulada
           .append("		(")
           .append(hqlDividaAcumulada)
           .append(" 		) AS dividaAcumulada, ")
           // data abertura
           .append("		(SELECT ")
           .append("			MIN(COBRANCA_.DT_VENCIMENTO) FROM COBRANCA COBRANCA_")
           .append("		WHERE COBRANCA_.COTA_ID=COTA_.ID  ")
           .append("		AND COBRANCA_.STATUS_COBRANCA= :statusCobrancaNaoPago ) AS DATAABERTURA ");
        
        this.setFromWhereCotasSujeitasSuspensao(sql);
        
        sql.append(obterOrderByCotasSujeitasSuspensao(sortOrder, sortColumn));
                
        if (inicio != null && rp != null) {
            sql.append(" LIMIT :inicio,:qtdeResult");
        }
        sql.append(") as geral");
        
        sql.append(" GROUP BY geral.IDCOTA ");
        
        sql.append(obterOrderByCotasSujeitasSuspensao(sortOrder, sortColumn));
        
        final Query query = getSession().createSQLQuery(sql.toString()).addScalar("idCota").addScalar("numCota")
                .addScalar("vlrConsignado").addScalar("vlrReparte").addScalar("dividaAcumulada").addScalar("nome")
                .addScalar("razaoSocial").addScalar("dataAbertura").addScalar("faturamento").addScalar("percDivida")
                .addScalar("diasAberto", IntegerType.INSTANCE);
        
        this.setParametrosCotasSujeitasSuspensao(query, dataOperacao);
        
        query.setParameterList("statusNaoEmitiveis", new String[] { 
                StatusLancamento.CONFIRMADO.name(),
                StatusLancamento.EM_BALANCEAMENTO.name(),
                StatusLancamento.PLANEJADO.name(),
                StatusLancamento.FECHADO.name(), 
                StatusLancamento.CONFIRMADO.name(),
                StatusLancamento.EM_BALANCEAMENTO.name(), 
                StatusLancamento.CANCELADO.name() });
        
        query.setParameterList("tipoMovimentoEstorno", new String[]{GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name()});
        query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
        query.setParameter("statusRecolhido", StatusLancamento.RECOLHIDO.name());
        
        final int intervalo = 35;
        query.setParameter("intervalo", intervalo);
        
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataOperacao);
        calendar.add(Calendar.DAY_OF_MONTH, intervalo * -1);
        query.setParameter("dataOperacaoIntervalo", calendar.getTime());
        
        if (inicio != null && rp != null) {
            query.setInteger("inicio", inicio);
            query.setInteger("qtdeResult", rp);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(CotaSuspensaoDTO.class));
        
        return query.list();
    }
    
    private void setParametrosCotasSujeitasSuspensao(final Query query, final Date dataOperacao) {
        
        query.setParameter("dataOperacao", dataOperacao);
        query.setParameter("ativo", SituacaoCadastro.ATIVO.name());
        query.setParameterList("statusDividaEmAbertoPendente", new String[] { StatusDivida.EM_ABERTO.name(),
                StatusDivida.PENDENTE_INADIMPLENCIA.name()});
        query.setParameter("statusCobrancaNaoPago", StatusCobranca.NAO_PAGO.name());
    }
    
    private void setFromWhereCotasSujeitasSuspensao(final StringBuilder sql) {
        
        sql.append(" FROM COTA COTA_ ")
           .append(" LEFT JOIN PARAMETRO_COBRANCA_COTA POLITICACOTA ON(POLITICACOTA.COTA_ID=COTA_.ID) ")
           .append(" JOIN DISTRIBUIDOR AS POLITICADISTRIB ")
           .append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ")
           
           .append(" WHERE COTA_.SITUACAO_CADASTRO = :ativo AND COTA_.SUGERE_SUSPENSAO!=false ")
           .append(" AND ((COTA_.NUM_ACUMULO_DIVIDA IS NOT NULL ")
           .append(" 		AND COTA_.NUM_ACUMULO_DIVIDA <> 0 ")
           .append("		AND COTA_.NUM_ACUMULO_DIVIDA <= ( ")
           .append("											SELECT count(DIVIDA.ID) ")
           .append("											FROM DIVIDA DIVIDA ")
           .append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
           .append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
           .append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
           .append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao ")
           .append("                                                               AND COBRANCA_.STATUS_COBRANCA = :statusCobrancaNaoPago)")
           .append("	   ) ")
           .append("	   OR ")
           .append("	   (COTA_.VALOR_SUSPENSAO IS NOT NULL ")
           .append("       AND COTA_.VALOR_SUSPENSAO <> 0 ")
           .append("		AND COTA_.VALOR_SUSPENSAO <= (SELECT SUM(DIVIDA.VALOR) ")
           .append("											FROM DIVIDA DIVIDA ")
           .append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
           .append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
           .append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
           .append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao ")
           .append("                                                               AND COBRANCA_.STATUS_COBRANCA = :statusCobrancaNaoPago)")
           .append("	   ) ")
           .append("	   OR ")
           .append("	   ((COTA_.NUM_ACUMULO_DIVIDA IS NULL or COTA_.VALOR_SUSPENSAO IS NULL) ")
           .append("	    AND COTA_.NUM_ACUMULO_DIVIDA IS NOT NULL ")
           .append("	    AND COTA_.NUM_ACUMULO_DIVIDA <> 0 ")
           .append("		AND COTA_.NUM_ACUMULO_DIVIDA <= (")
           .append("											SELECT count(DIVIDA.ID) ")
           .append("											FROM DIVIDA DIVIDA ")
           .append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
           .append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
           .append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
           .append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao ")
           .append("                                                               AND COBRANCA_.STATUS_COBRANCA = :statusCobrancaNaoPago)")
           .append("	   ) ")
           .append("	   OR ")
           .append("	   ((COTA_.NUM_ACUMULO_DIVIDA IS NULL or COTA_.VALOR_SUSPENSAO IS NULL) ")
           .append("	    AND COTA_.VALOR_SUSPENSAO IS NOT NULL ")
           .append("	    AND COTA_.VALOR_SUSPENSAO <> 0")
           .append("		AND COTA_.VALOR_SUSPENSAO <= (SELECT SUM(DIVIDA.VALOR) ")
           .append("											FROM DIVIDA DIVIDA ")
           .append("											JOIN COBRANCA COBRANCA_ ON (COBRANCA_.DIVIDA_ID=DIVIDA.ID) ")
           .append("															   WHERE DIVIDA.COTA_ID=COTA_.ID ")
           .append("                            								   AND COBRANCA_.DT_PAGAMENTO is null ")
           .append("															   AND DIVIDA.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("															   AND COBRANCA_.DT_VENCIMENTO < :dataOperacao ")
           .append("                                                               AND COBRANCA_.STATUS_COBRANCA = :statusCobrancaNaoPago)")
           .append("	   ) ")
           .append("           OR  ")
           .append("           	COTA_.ID in ")
           .append("           	(select ")
           .append("           		dv.COTA_ID ")
           .append("         	 from negociacao ng ")
           .append("         join parcela_negociacao pn ")
           .append("         	on pn.NEGOCIACAO_ID = ng.ID ")
           .append("         join movimento_financeiro_cota mfc ")
           .append("    	     ON  mfc.ID = pn.MOVIMENTO_FINANCEIRO_ID ")
           .append("         join divida dv ")
           .append("        	 ON dv.cota_id = mfc.cota_id ")
           .append("         join cobranca cb ")
           .append("         	on cb.DIVIDA_ID = dv.ID ")
           .append("         where ")
           .append("         dv.STATUS in ('EM_ABERTO', 'PENDENTE_INADIMPLENCIA') ")
           .append("         	and cb.DT_VENCIMENTO < :dataOperacao ")
           .append("         group by dv.id) ")
           
           .append(" ) ")
           .append("  group by cota_.ID ");
    }
    
    private String obterOrderByCotasSujeitasSuspensao(final String sortOrder, final String sortColumn) {
        String sql = "";
        
        if (sortColumn == null || sortOrder == null) {
            return sql;
        }
        
        sql += " ORDER BY ";
        
        if ("numCota".equalsIgnoreCase(sortColumn)) {
            sql += "numCota";
        } else if ("nome".equalsIgnoreCase(sortColumn)) {
            sql += "nome";
        } else if ("vlrConsignado".equalsIgnoreCase(sortColumn)) {
            sql += "vlrConsignado";
        } else if ("vlrReparte".equalsIgnoreCase(sortColumn)) {
            sql += "vlrReparte";
        } else if ("dividaAcumulada".equalsIgnoreCase(sortColumn)) {
            sql += "dividaAcumulada";
        } else if ("diasAberto".equalsIgnoreCase(sortColumn)) {
            sql += "dataAbertura";
        } else if ("faturamento".equalsIgnoreCase(sortColumn)) {
            sql += "faturamento";
        } else if ("percDivida".equalsIgnoreCase(sortColumn)) {
            sql += "percDivida";
        } else {
            return "";
        }
        
        sql += "asc".equalsIgnoreCase(sortOrder) ? " ASC " : " DESC ";
        
        return sql;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoValorDTO> obterReparteDaCotaNoDia(final Long idCota, final Date date) {
        
        final Criteria criteria = getSession().createCriteria(MovimentoEstoqueCota.class, "movimento");
        
        criteria.createAlias("movimento.produtoEdicao", "produtoEdicao");
        criteria.createAlias("movimento.tipoMovimento", "tipoMovimento");
        
        criteria.add(Restrictions.eq("data", date));
        criteria.add(Restrictions.eq("tipoMovimento.operacaoEstoque", OperacaoEstoque.ENTRADA));
        criteria.add(Restrictions.eq("cota.id", idCota));
        
        final ProjectionList projections = Projections.projectionList();
        projections.add(Projections.alias(Projections.property("qtde"), "quantidade"));
        projections.add(Projections.alias(Projections.property("produtoEdicao.precoVenda"), "preco"));
        
        criteria.setProjection(projections);
        
        criteria.setResultTransformer(Transformers.aliasToBean(ProdutoValorDTO.class));
        
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoValorDTO> obterValorConsignadoDaCota(final Long idCota) {
        
        final Criteria criteria = getSession().createCriteria(EstoqueProdutoCota.class, "epCota");
        criteria.createAlias("epCota.produtoEdicao", "produtoEdicao");
        criteria.createAlias("epCota.cota", "cota");
        
        criteria.add(Restrictions.eq("cota.id", idCota));
        
        final ProjectionList projections = Projections.projectionList();
        projections.add(Projections.alias(Projections.property("epCota.qtdeRecebida"), "quantidade"));
        projections.add(Projections.alias(Projections.property("produtoEdicao.precoVenda"), "preco"));
        
        criteria.setProjection(projections);
        
        criteria.setResultTransformer(Transformers.aliasToBean(ProdutoValorDTO.class));
        
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> obterDiasConcentracaoPagamentoCota(final Long idCota) {
        final StringBuilder hql = new StringBuilder("select cc.codigoDiaSemana ");
        hql.append(" from ConcentracaoCobrancaCota cc ").append(" join cc.formaCobranca fc ").append(
                " join fc.parametroCobrancaCota p ").append(" join p.cota cota ").append(" where cota.id = :idCota ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        
        return query.list();
    }
    
    @Override
    public Long obterTotalCotasSujeitasSuspensao(final Date dataOperacao) {
        
        final StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM (SELECT cota_.ID ");
        
        this.setFromWhereCotasSujeitasSuspensao(sql);
        
        sql.append(") as total ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        this.setParametrosCotasSujeitasSuspensao(query, dataOperacao);
        
        final Long qtde = ((BigInteger) query.uniqueResult()).longValue();
        
        return qtde == null ? 0L : qtde;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Cota> obterCotaAssociadaFiador(final Long idFiador) {
        final Criteria criteria = this.getSession().createCriteria(Cota.class);
        criteria.add(Restrictions.eq("fiador.id", idFiador));
        
        return criteria.list();
    }
    
    @Override
    public Long obterQntCotasSujeitasAntecipacoEncalhe(final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        /*
         * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um
         * PDV associado a mesma cota.
         */
        hql.append("SELECT count(cota.id) ");

        hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

        hql.append(" group by ")
           .append(" box.id, ")
		   .append(" cota.id, ")
		   .append(" estoqueProdutoCota.id, ")
		   .append(" produtoEdicao.id, ")
		   .append(" lancamento ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);
        
        setParameters(query, param);

        return ((Integer) query.list().size()).longValue();
    }
    
    @Override
    public BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" SELECT estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
        
        hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);
        
        setParameters(query, param);
        
        query.setMaxResults(1);
        
        return (BigInteger) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        /*
         * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um
         * PDV associado a mesma cota.
         */
        hql.append("SELECT DISTINCT new ")
        .append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
        .append(" ( box.codigo,")
        .append(" box.nome,")
        .append(" cota.numeroCota, ")
        .append(" estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida,")
        .append(" lancamento.id ,")
        .append(" case when (pessoa.class = 'F') then ( pessoa.nome )")
        .append(" when (pessoa.class = 'J' ) then ( pessoa.razaoSocial )").append(" else null end ")
        .append(" ) ");
        
        hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));
        
        hql.append(getOrderByCotasSujeitasAntecipacoEncalhe(filtro));
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);
        
        setParameters(query, param);
        
        if (filtro.getPaginacao() != null) {
            
            if (filtro.getPaginacao().getPosicaoInicial() != null) {
                query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
            }
            
            if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
                query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
            }
        }
        
        return query.list();
    }
    
    /**
     * Retorna os parametros da consulta de dividas.
     * 
     * @param filtro
     * @return Map<String,Object>
     */
    private Map<String, Object> getParametrosCotasSujeitasAntecipacoEncalhe(
            final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        param.put("codigoProduto", filtro.getCodigoProduto());
        param.put("numeroEdicao", filtro.getNumeroEdicao());
        param.put("status", Arrays.asList(StatusLancamento.EXPEDIDO, StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO));
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
        
        if (filtro.getRota() != null) {
            param.put("rota", filtro.getRota());
        }
        
        if (filtro.getRoteiro() != null) {
            param.put("roteiro", filtro.getRoteiro());
        }
        
        if (filtro.getDescMunicipio() != null) {
            param.put("cidadeCota", filtro.getDescMunicipio());
        }
        
        if (filtro.getCodTipoPontoPDV() != null) {
        	
        	if(filtro.getCodTipoPontoPDV().equals("ALTERNATIVO")) {
        		param.put("codigoTipoPontoPDV", TipoDistribuicaoCota.ALTERNATIVO);
        	} else {
        		param.put("codigoTipoPontoPDV", TipoDistribuicaoCota.CONVENCIONAL);
        	}
        }
        
        return param;
    }
    
    private String getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" FROM ").append(" Cota cota  ").append(" JOIN cota.pessoa pessoa ").append(" JOIN cota.box box ")
        .append(" JOIN cota.estoqueProdutoCotas estoqueProdutoCota ")
        .append(" JOIN estoqueProdutoCota.produtoEdicao produtoEdicao  ").append(" JOIN produtoEdicao.produto produto ")
                        .append(" JOIN produtoEdicao.lancamentos lancamento force index (ndx_status) ").append(" JOIN cota.pdvs pdv ").append(
                                " LEFT JOIN pdv.rotas rotaPdv  ").append(" LEFT JOIN rotaPdv.rota rota  ").append(
                                        " LEFT JOIN rota.roteiro roteiro ").append(" LEFT JOIN roteiro.roteirizacao roteirizacao ");
        
        if (filtro.getFornecedor() != null) {
            hql.append(" JOIN produto.fornecedores fornecedor ");
        }
        
        if (filtro.getDescMunicipio() != null) {
            hql.append(" JOIN cota.enderecos enderecoCota ").append(" JOIN enderecoCota.endereco endereco ");
        }
        
        hql.append(" WHERE ").append("NOT EXISTS (").append(
                " SELECT chamadaEncalheCota.cota FROM ChamadaEncalheCota chamadaEncalheCota ").append(
                        " JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ").append(
                                " WHERE chamadaEncalheCota.cota = cota ").append(" AND chamadaEncalhe.produtoEdicao = produtoEdicao ")
                                .append(" AND chamadaEncalhe.tipoChamadaEncalhe=:tipoChamadaEncalhe").append(" ) ").append(
                                        " AND estoqueProdutoCota.produtoEdicao.id = produtoEdicao.id ").append(
                                                " AND lancamento.status in (:status) ").append(" AND produto.codigo =:codigoProduto ").append(
                                                        " AND produtoEdicao.numeroEdicao =:numeroEdicao ").append(
                                                                " AND lancamento.dataRecolhimentoPrevista >:dataAtual ").append(
                                                                        " AND (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) > 0 ").append(
                                                                                " AND pdv.caracteristicas.pontoPrincipal = true ");
        
        if (filtro.getNumeroCota() != null) {
            
            hql.append(" AND cota.numeroCota =:numeroCota ");
        }
        
        if (filtro.getFornecedor() != null) {
            
            hql.append(" AND fornecedor.id =:fornecedor ");
        }
        
        if (filtro.getBox() != null) {
            
            hql.append(" AND box.id =:box ");
        }
        
        if (filtro.getRota() != null) {
            hql.append(" AND rota.id =:rota ");
        }
        
        if (filtro.getRoteiro() != null) {
            hql.append(" AND roteiro.id =:roteiro ");
        }
        
        if (filtro.getDescMunicipio() != null) {
            hql.append(" AND endereco.cidade =:cidadeCota ");
        }
        
        if (filtro.getCodTipoPontoPDV() != null) {
        	hql.append(" AND cota.tipoDistribuicaoCota =:codigoTipoPontoPDV ");
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
    private String getOrderByCotasSujeitasAntecipacoEncalhe(final FiltroChamadaAntecipadaEncalheDTO filtro) {
        
        if (filtro == null || filtro.getOrdenacaoColuna() == null) {
            return "";
        }
        
        final StringBuilder hql = new StringBuilder();
        
        switch (filtro.getOrdenacaoColuna()) {
        case BOX:
            hql.append(" order by box.codigo ");
            break;
        case NOME_COTA:
            hql.append(" order by   ").append(" case when (pessoa.class = 'F') then ( pessoa.nome )").append(
                    " when (pessoa.class = 'J') then ( pessoa.razaoSocial )").append(" else null end ");
            break;
        case NUMERO_COTA:
            hql.append(" order by cota.numeroCota ");
            break;
        case QNT_EXEMPLARES:
            hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
            break;
        default:
            hql.append(" order by  box.codigo ");
            break;
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
    public Cota obterCotaPDVPorNumeroDaCota(final Integer numeroCota) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" from Cota cota ").append(" left join fetch cota.pdvs ").append(
                " where cota.numeroCota = :numeroCota ");
        
        final Query query = getSession().createQuery(hql.toString());
        
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
    public EnderecoCota obterEnderecoPrincipal(final long idCota) {
        final Criteria criteria = getSession().createCriteria(EnderecoCota.class);
        criteria.add(Restrictions.eq("cota.id", idCota));
        
        criteria.add(Restrictions.eq("principal", true));
        criteria.setMaxResults(1);
        
        return (EnderecoCota) criteria.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> obterCotas(final FiltroCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(this.getSqlPesquisaCota(filtro, Boolean.FALSE));
        
        hql.append(this.ordenarConsultaCota(filtro));
        
        final Query query = getSession().createQuery(hql.toString());
        
        if (filtro.getCotaId() != null) {
            query.setParameter("cotaId", filtro.getCotaId());
        }
        
        if (filtro.getNumeroCota() != null) {
            query.setParameter("numeroCota", filtro.getNumeroCota());
        }
        
        if (filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
            query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() + "%");
        }
        
        if (filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty()) {
            query.setParameter("nomeCota", filtro.getNomeCota() + "%");
        }
        
        if (filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty()) {
            
            query.setParameter("logradouro", filtro.getLogradouro() + "%");
        }
        
        if (filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty()) {
            
            query.setParameter("bairro", filtro.getBairro() + "%");
        }
        
        if (filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty()) {
            
            query.setParameter("municipio", filtro.getMunicipio() + "%");
        }
        
        if (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty()
                && !"TODOS".equalsIgnoreCase(filtro.getStatus())) {
            query.setParameter("situacaoCadastro", SituacaoCadastro.valueOf(filtro.getStatus()));
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        if (filtro.getPaginacao() != null) {
            
            if (filtro.getPaginacao().getPosicaoInicial() != null) {
                query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
            }
            
            if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
                query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
            }
        }
        
        return query.list();
        
    }
    
    @Override
    public Long obterQuantidadeCotasPesquisadas(final FiltroCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(this.getSqlPesquisaCota(filtro, Boolean.TRUE));
        
        final Query query = getSession().createQuery(hql.toString());
        
        if (filtro.getNumeroCota() != null) {
            query.setParameter("numeroCota", filtro.getNumeroCota());
        }
        
        if (filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
            query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() + "%");
        }
        
        if (filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty()) {
            query.setParameter("nomeCota", filtro.getNomeCota() + "%");
        }
        
        if (filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty()) {
            
            query.setParameter("logradouro", filtro.getLogradouro() + "%");
        }
        
        if (filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty()) {
            
            query.setParameter("bairro", filtro.getBairro() + "%");
        }
        
        if (filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty()) {
            
            query.setParameter("municipio", filtro.getMunicipio() + "%");
        }
        
        if (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty()
                && !"TODOS".equalsIgnoreCase(filtro.getStatus())) {
            query.setParameter("situacaoCadastro", SituacaoCadastro.valueOf(filtro.getStatus()));
        }
        
        return (Long) query.uniqueResult();
    }
    
    private String getSubSqlPesquisaTelefone() {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" ( select telefone.ddd || '-' || telefone.numero ");
        
        hql.append(" from  ");
        
        hql.append(" Telefone telefone where telefone.id = 	");
        
        hql.append(" ( select max(telefone.id) from TelefoneCota telefoneCota inner join telefoneCota.telefone as telefone");
        
        hql.append(" where telefoneCota.cota.id = cota.id and ");
        
        hql.append(" telefoneCota.principal = true ) ) as telefone, ");
        
        return hql.toString();
        
    }
    
    private String getSubSqlPesquisaContatoPDV() {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" ( select pdv.contato ");
        
        hql.append(" from  ");
        
        hql.append(" PDV pdv ");
        
        hql.append(" where pdv.id =  ");
        
        hql.append(" ( select max(pdv.id) from PDV pdv where pdv.cota.id = cota.id and pdv.caracteristicas.pontoPrincipal = true ) ) as contato,");
        
        return hql.toString();
        
    }
    
    private String getSqlPesquisaCota(final FiltroCotaDTO filtro, final boolean isCount) {
        
        final StringBuilder hql = new StringBuilder();
        
        final int colunaRazaoSocNomePessoa = 3;
        final int colunaNumeroCpfCnpj = 4;
        final int colunaContato = 5;
        final int colunaTelefone = 6;
        
        if (isCount) {
            
            hql.append(" select count(distinct cota.numeroCota) ");
            
        } else {
            
            hql.append(
                    " SELECT cota.id as idCota, cota.numeroCota as numeroCota, cota.parametroDistribuicao.recebeComplementar as recebeComplementar, cota.tipoDistribuicaoCota as tipoDistribuicaoCota, ")
                    .append(" case when (pessoa.class = 'F') then ( pessoa.nome )").append(
                            " when (pessoa.class = 'J') then ( pessoa.razaoSocial )").append(
                                    " else null end as nomePessoa, ").append(
                                            " case when (pessoa.class = 'F') then ( pessoa.cpf )").append(
                                                    " when (pessoa.class = 'J') then ( pessoa.cnpj )").append(
                                                            " else null end as numeroCpfCnpj, ")
                                                            
                                                            .append(getSubSqlPesquisaContatoPDV()).append(getSubSqlPesquisaTelefone())
                                                            
                                                            .append(" pessoa.email as email ,").append(" cota.situacaoCadastro as status, ").append(
                                                                    " box.nome as descricaoBox ");
        }
        
        hql.append(" FROM Cota cota 								").append(" join cota.pessoa pessoa 					").append(
                " left join cota.enderecos enderecoCota 	").append(" left join enderecoCota.endereco endereco 	")
                .append(" left join cota.box box ");
        
        if (filtro.getCotaId() != null
                || filtro.getNumeroCota() != null
                || (filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty())
                || (filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty())
                || (filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty())
                || (filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty())
                || (filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty())
                || (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty() && !"TODOS".equalsIgnoreCase(filtro
                        .getStatus()))) {
            
            hql.append(" WHERE ");
            
        }
        
        boolean indAnd = false;
        
        if (filtro.getCotaId() != null) {
            
            hql.append(" cota.id = :cotaId ");
            
            indAnd = true;
        }
        
        if (filtro.getNumeroCota() != null) {
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" cota.numeroCota =:numeroCota ");
            
            indAnd = true;
        }
        
        if (filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" ( upper (pessoa.cpf) like(:numeroCpfCnpj) OR  upper(pessoa.cnpj) like upper (:numeroCpfCnpj) ) ");
            
            indAnd = true;
        }
        
        if (filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty()) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" ( upper(pessoa.nome) like upper(:nomeCota) OR  upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");
            
            indAnd = true;
        }
        
        if (filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty()) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" ( upper(endereco.logradouro) like upper(:logradouro) )");
            
            indAnd = true;
        }
        
        if (filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty()) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" ( upper(endereco.bairro) like upper(:bairro) )");
            
            indAnd = true;
        }
        
        if (filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty()) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" ( upper(endereco.cidade) like upper(:municipio) )");
            
            indAnd = true;
        }
        
        if (filtro.getStatus() != null && !filtro.getStatus().trim().isEmpty()
                && !"TODOS".equalsIgnoreCase(filtro.getStatus())) {
            
            if (indAnd) {
                hql.append(" AND ");
            }
            
            hql.append(" cota.situacaoCadastro =:situacaoCadastro ");
        }
        
        if (!isCount) {
            
            hql.append("group by ");
            hql.append("cota.id, ");
            hql.append("cota.numeroCota, ");
            hql.append(colunaRazaoSocNomePessoa + ", ");
            hql.append(colunaNumeroCpfCnpj + ", ");
            hql.append(colunaContato + ", ");
            hql.append(colunaTelefone + ", ");
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
    private String ordenarConsultaCota(final FiltroCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
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
                break;
            }
            
            if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
                hql.append(filtro.getPaginacao().getOrdenacao().toString());
            }
            
        }
        
        return hql.toString();
    }
    
    @Override
    public Integer gerarSugestaoNumeroCota() {
        
        final String hql = "select max(cota.numeroCota) from Cota cota where cota.id not in ( select h.pk.idCota from HistoricoNumeroCota h )";
        
        final Integer numeroCota = (Integer) getSession().createQuery(hql).uniqueResult();
        
        return (numeroCota == null) ? 0 : numeroCota + 1;
    }
    
    

    @Override
    public Long obterQuantidadeCotas(final SituacaoCadastro situacaoCadastro) {
        
        final StringBuilder hql = new StringBuilder(" select count (cota.id) ");
        
        hql.append(" from Cota cota ");
        
        if (situacaoCadastro != null) {
            
            hql.append(" where cota.situacaoCadastro = :situacao ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (situacaoCadastro != null) {
            
            query.setParameter("situacao", situacaoCadastro);
        }
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaResumoDTO> obterCotas(final SituacaoCadastro situacaoCadastro) {
        
        final StringBuilder hql = new StringBuilder(
                "select "+
                " case pessoa.class " +
                " when 'F' then pessoa.nome " +
                " when 'J' then pessoa.razaoSocial end as nome," +
                " cota.numeroCota as numero  from Cota cota ");
        
        hql.append(" join cota.pessoa pessoa ");
        
        if (situacaoCadastro != null) {
            
            hql.append(" where cota.situacaoCadastro = :situacao ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (situacaoCadastro != null) {
            
            query.setParameter("situacao", situacaoCadastro);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
        
        return query.list();
    }
    
    @Override
    public Long countCotas(final SituacaoCadastro situacaoCadastro) {
        
        final StringBuilder hql = new StringBuilder("select count(cota.id) from Cota cota ");
        
        if (situacaoCadastro != null) {
            
            hql.append(" where cota.situacaoCadastro = :situacao ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (situacaoCadastro != null) {
            
            query.setParameter("situacao", situacaoCadastro);
        }
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaResumoDTO> obterCotasComInicioAtividadeEm(final Date dataInicioAtividade) {
        
        final StringBuilder hql = new StringBuilder(
                "select " +
        		" case pessoa.class " +
                " when 'F' then pessoa.nome " +
                " when 'J' then pessoa.razaoSocial end as nome," +
                "cota.numeroCota as numero from Cota cota ");
        
        hql.append(" join cota.pessoa pessoa ");
        
        if (dataInicioAtividade != null) {
            
            hql.append(" where cota.inicioAtividade = :dataInicioAtividade ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (dataInicioAtividade != null) {
            
            query.setParameter("dataInicioAtividade", dataInicioAtividade);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
        
        return query.list();
    }
    
    @Override
    public Long countCotasComInicioAtividadeEm(final Date dataInicioAtividade) {
        
        final StringBuilder hql = new StringBuilder("select count(cota.id) from Cota cota ");
        
        if (dataInicioAtividade != null) {
            
            hql.append(" where cota.inicioAtividade = :dataInicioAtividade ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (dataInicioAtividade != null) {
            
            query.setParameter("dataInicioAtividade", dataInicioAtividade);
        }
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(final Date dataExpedicaoReparte) {
        
        final StringBuilder hql = new StringBuilder(
                " select "+
        		" case pessoa.class " +
                " when 'F' then pessoa.nome " +
                " when 'J' then pessoa.razaoSocial end as nome," +
                " cota.numeroCota as numero from CotaAusente cotaAusente ");
        
        hql.append(" join cotaAusente.cota cota ");
        
        hql.append(" join cota.pessoa pessoa ");
        
        if (dataExpedicaoReparte != null) {
            
            hql.append(" where cotaAusente.data = :dataExpedicaoReparte ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (dataExpedicaoReparte != null) {
            
            query.setParameter("dataExpedicaoReparte", dataExpedicaoReparte);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
        
        return query.list();
    }
    
    @Override
    public Long countCotasAusentesNaExpedicaoDoReparteEm(final Date dataExpedicaoReparte) {
        
        final StringBuilder hql = new StringBuilder(" select count(cotaAusente.id)  from CotaAusente cotaAusente ");
        
        if (dataExpedicaoReparte != null) {
            
            hql.append(" where cotaAusente.data = :dataExpedicaoReparte ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (dataExpedicaoReparte != null) {
            
            query.setParameter("dataExpedicaoReparte", dataExpedicaoReparte);
        }
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(final Date dataRecolhimentoEncalhe) {
        
        final StringBuilder hql = new StringBuilder(
                " select " +
                " case pessoa.class " +
                " when 'F' then pessoa.nome " +
                " when 'J' then pessoa.razaoSocial end as nome," +
                " cota.numeroCota as numero from ChamadaEncalheCota chamadaEncalheCota ");
        
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
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("dataRecolhimentoEncalhe", dataRecolhimentoEncalhe);
        query.setParameter("statusControleConferenciaEncalhe", StatusOperacao.CONCLUIDO);
        
        query.setResultTransformer(Transformers.aliasToBean(CotaResumoDTO.class));
        
        return query.list();
    }
    
    @Override
    public Long countCotasAusentesNoRecolhimentoDeEncalheEm(final Date dataRecolhimentoEncalhe) {
        
        final StringBuilder hql = new StringBuilder(
                " select count(distinct chamadaEncalheCota.cota.id) from ChamadaEncalheCota chamadaEncalheCota ");
        
        hql.append(" join chamadaEncalheCota.cota cota ");
        
        hql.append(" where chamadaEncalheCota.cota.id not in ( ");
        hql.append(" select cota.id from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
        hql.append(" join controleConferenciaEncalheCota.cota cota ");
        hql.append(" where controleConferenciaEncalheCota.dataOperacao = :dataRecolhimentoEncalhe ");
        hql.append(" and controleConferenciaEncalheCota.status = :statusControleConferenciaEncalhe) ");
        hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataRecolhimentoEncalhe ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("dataRecolhimentoEncalhe", dataRecolhimentoEncalhe);
        query.setParameter("statusControleConferenciaEncalhe", StatusOperacao.CONCLUIDO);
        
        return (Long) query.uniqueResult();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * br.com.abril.nds.repository.CotaRepository#obterIdCotasEntre(br.com.abril
     * .nds.util.Intervalo)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> obterIdCotasEntre(final Intervalo<Integer> intervaloCota, final Intervalo<Integer> intervaloBox,
            final List<SituacaoCadastro> situacoesCadastro, final Long idRoteiro, final Long idRota,
            final String sortName, final String sortOrder, final Integer maxResults, final Integer page) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        criteria.createAlias("box", "box", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("pdvs", "pdvs");
        criteria.setProjection(Projections.distinct(Projections.id()));
        
        if (intervaloCota != null && intervaloCota.getDe() != null) {
            
            if (intervaloCota.getAte() != null) {
                criteria.add(Restrictions.between("numeroCota", intervaloCota.getDe(), intervaloCota.getAte()));
            } else {
                criteria.add(Restrictions.eq("numeroCota", intervaloCota.getDe()));
            }
        }
        
        if (intervaloBox != null && intervaloBox.getDe() != null && intervaloBox.getAte() != null) {
            criteria.add(Restrictions.between("box.codigo", intervaloBox.getDe(), intervaloBox.getAte()));
        }
        
        if (situacoesCadastro != null && !situacoesCadastro.isEmpty()) {
            criteria.add(Restrictions.in("situacaoCadastro", situacoesCadastro));
        }
        
        criteria.createAlias("pdvs.rotas", "rotaPdv", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("rotaPdv.rota", "rota", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("rota.roteiro", "roteiro", JoinType.LEFT_OUTER_JOIN);
        
        if (idRoteiro != null) {
            
            criteria.add(Restrictions.eq("roteiro.id", idRoteiro));
        }
        
        if (idRota != null) {
            
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
     * 
     * @param cotaDe
     * @param cotaAte
     * @param situacoesCadastro
     * @return List<Cota>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Cota> obterCotasIntervaloNumeroCota(final Integer cotaDe, final Integer cotaAte,
            final List<SituacaoCadastro> situacoesCadastro) {
        
        final Criteria criteria = super.getSession().createCriteria(Cota.class);
        
        criteria.setProjection(Projections.distinct(Projections.id()));
        
        if (cotaDe != null) {
            
            if (cotaAte != null) {
                
                criteria.add(Restrictions.between("numeroCota", cotaDe, cotaAte));
            } else {
                
                criteria.add(Restrictions.eq("numeroCota", cotaDe));
            }
        }
        
        if (situacoesCadastro != null && !situacoesCadastro.isEmpty()) {
            
            criteria.add(Restrictions.in("situacaoCadastro", situacoesCadastro));
        }
        
        criteria.addOrder(Order.asc("numeroCota"));
        
        return criteria.list();
    }
    
    @Override
    public Integer obterDadosCotasComNotaEnvioEmitidasCount(final FiltroConsultaNotaEnvioDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(*) from ( ");
        
        montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
        sql.append(" union all ");
        montarConsultaFuroProdutoNotaEmitida(filtro, sql, true);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, true, StatusNotaEnvio.EMITIDA);
        
        sql.append(" ) rs1 ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, true);
        
        final BigInteger qtde = (BigInteger) query.uniqueResult();
        
        return (qtde == null) ? 0 : qtde.intValue();
        
    }
    
    @Override
    public Integer obterDadosCotasComNotaEnvioAEmitirCount(final FiltroConsultaNotaEnvioDTO filtro) {

        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(*) from ( ");
        
        montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, true, StatusNotaEnvio.NAO_EMITIDA);
        
        sql.append(" ) rs1 ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, true);
        
        final BigInteger qtde = (BigInteger) query.uniqueResult();
        
        return (qtde == null) ? 0 : qtde.intValue();
        
    }
    
    @Override
    public Integer obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(final FiltroConsultaNotaEnvioDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(*) from ( ");
        
        montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
        sql.append(" union all ");
        montarConsultaFuroProdutoNotaEmitida(filtro, sql, true);
        sql.append(" union all ");
        montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, true, null);
        
        sql.append(" ) rs1 ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, true);
        
        final BigInteger qtde = (BigInteger) query.uniqueResult();
        
        return (qtde == null) ? 0 : qtde.intValue();
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidas(final FiltroConsultaNotaEnvioDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
        sql.append(" union all ");
        montarConsultaFuroProdutoNotaEmitida(filtro, sql, false);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, false, StatusNotaEnvio.EMITIDA);
        
        orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(), filtro.getPaginacaoVO()
                .getSortOrder() == null ? "" : filtro.getPaginacaoVO().getSortOrder());
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, false);
        
        query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
        
        return query.list();
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioAEmitir(final FiltroConsultaNotaEnvioDTO filtro) {

        final StringBuilder sql = new StringBuilder();
        
        montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, false, StatusNotaEnvio.NAO_EMITIDA);
        
        orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(), filtro.getPaginacaoVO()
                .getSortOrder() == null ? "" : filtro.getPaginacaoVO().getSortOrder());
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, false);
        
        query.setParameterList("gruposFaltaSobra", this.getGruposSobraFalta());
        query.setParameterList("gruposFalta", this.getGruposFalta());
        query.setParameterList("gruposSobra", this.getGruposSobra());
        
        query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidasEAEmitir(final FiltroConsultaNotaEnvioDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
        sql.append(" union all ");
        montarConsultaFuroProdutoNotaEmitida(filtro, sql, false);
        sql.append(" union all ");
        montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
        sql.append(" union all ");
        montarQueryReparteCotaAusente(filtro, sql, false, null);
        
        orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(), filtro.getPaginacaoVO()
                .getSortOrder() == null ? "" : filtro.getPaginacaoVO().getSortOrder());
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        montarParametrosFiltroNotasEnvio(filtro, query, false);
        
        query.setParameterList("gruposFaltaSobra", this.getGruposSobraFalta());
        query.setParameterList("gruposFalta", this.getGruposFalta());
        query.setParameterList("gruposSobra", this.getGruposSobra());
        
        query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
        
        return query.list();
        
    }
    
    private List<String> getGruposSobraFalta() {
        
        List<String> grupoMovimentosSobrasFaltas = new ArrayList<String>();
        grupoMovimentosSobrasFaltas.addAll(this.getGruposFalta());
        grupoMovimentosSobrasFaltas.addAll(this.getGruposSobra());
        
        return grupoMovimentosSobrasFaltas;
    }
    
    private List<String> getGruposFalta() {
        
        return Arrays.asList(
        		GrupoMovimentoEstoque.FALTA_DE.name(), 
        		GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
        		GrupoMovimentoEstoque.FALTA_EM.name(), 
        		GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
                GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(), 
                GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA.name());
    }
    
    private List<String> getGruposSobra() {
        
        return Arrays.asList(
        		GrupoMovimentoEstoque.SOBRA_DE.name(), 
        		GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
                GrupoMovimentoEstoque.SOBRA_EM.name(), 
                GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
                GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.SOBRA_ENVIO_PARA_COTA.name(),
                GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name());
    }
    
    private void montarQueryReparteCotaAusente(final FiltroConsultaNotaEnvioDTO filtro, final StringBuilder sql,
            final boolean isCount, final StatusNotaEnvio status) {
        
        if (isCount) {
            sql.append(" select cota_.ID ");
        } else {
            sql.append(" select lancamento_.STATUS as status, " + "	        cota_.ID as idCota, "
                    + "	        cota_.NUMERO_COTA as numeroCota, " + "	        cota_.BOX_ID as box, "
                    + "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
                    + "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, " + "	        SUM(mec.QTDE) as exemplares, "
                    + "	        SUM(mec.QTDE * pe_.PRECO_VENDA) as total, "
                    + "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa,	"
                    + "			roteiro_.ordem ordemRoteiro, " + "			rota_.ordem ordemRota, "
                    + "			rota_pdv_.ordem ordemRotaPdv ");
        }
        sql.append("   from " + "	        COTA cota_ " + "	    left outer join " + "	        BOX box1_  "
                + "	            on cota_.BOX_ID=box1_.ID  " + "	    inner join " + "	        MOVIMENTO_ESTOQUE_COTA mec  "
                + "	            on cota_.ID=mec.COTA_ID  " + "	    inner join " + "	        TIPO_MOVIMENTO tm  "
                + "	            on mec.TIPO_MOVIMENTO_ID=tm.ID  " + "	    inner join "
                + "	        LANCAMENTO lancamento_  force index (ndx_status) " + "	            on mec.LANCAMENTO_ID=lancamento_.ID  "
                + "	    inner join " + "	        PRODUTO_EDICAO pe_  " + "	            on mec.PRODUTO_EDICAO_ID=pe_.ID  "
                + " 			and lancamento_.PRODUTO_EDICAO_ID=pe_.ID " + "	    inner join " + "	        PRODUTO p_  "
                + "	            on pe_.PRODUTO_ID=p_.ID  " + "	    inner join " + "	        PRODUTO_FORNECEDOR pf_  "
                + "	            on p_.ID=pf_.PRODUTO_ID  " + "	    inner join " + "	        FORNECEDOR f_  "
                + "	            on pf_.fornecedores_ID=f_.ID  " + "	    inner join " + "	        PDV pdv_  "
                + "	            on cota_.ID=pdv_.COTA_ID  " + "	    left outer join " + "        ROTA_PDV rota_pdv_  "
                + "	            on pdv_.ID=rota_pdv_.PDV_ID    " + "	    left outer join " + "	        ROTA rota_  "
                + "	            on rota_pdv_.rota_ID=rota_.ID  " + "	    left outer join " + "	        ROTEIRO roteiro_  "
                + "	            on rota_.ROTEIRO_ID=roteiro_.ID  " + "	    inner join " + "	        PESSOA pessoa_cota_  "
                + "	            on cota_.PESSOA_ID=pessoa_cota_.ID  ");
        
        String joinType = " left outer join ";
        
        if (StatusNotaEnvio.EMITIDA.equals(status)) {
            joinType = " inner join ";
        }
        
        sql.append(joinType + "   NOTA_ENVIO_ITEM nei " + "    				on ( nei.SEQUENCIA = mec.NOTA_ENVIO_ITEM_SEQUENCIA "
                + "                  	 and nei.NOTA_ENVIO_ID = mec.NOTA_ENVIO_ITEM_NOTA_ENVIO_ID )" + "	   	where "
                + "	        lancamento_.STATUS in (:status)  " + "	        and mec.ESTUDO_COTA_ID is null  "
                + "    	and tm.GRUPO_MOVIMENTO_ESTOQUE = 'RATEIO_REPARTE_COTA_AUSENTE'  "
                + "		and pdv_.ponto_principal = :principal ");
        
        if (StatusNotaEnvio.NAO_EMITIDA.equals(status) || status == null) {
            sql.append("  and mec.NOTA_ENVIO_ITEM_SEQUENCIA is null  ");
        }
        
        if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
            sql.append("	        and ( " + "	            f_.ID is null  "
                    + "	            or f_.ID in (:idFornecedores) " + "	        )  ");
        }
        
        if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
            if (filtro.getIntervaloCota().getAte() != null) {
                
                sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
                
            } else {
                
                sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
            }
            
        }
        
        if (filtro.getIntervaloBox() != null && filtro.getIntervaloBox().getDe() != null
                && filtro.getIntervaloBox().getAte() != null) {
            sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            sql.append(" and roteiro_.ID=:idRoteiro  ");
        }
        
        if (filtro.getIdRota() != null) {
            sql.append(" and rota_.ID=:idRota  ");
        }
        
        if (!filtro.isFiltroEspecial()) {
            sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
        }
        
        if (filtro.getIntervaloMovimento() != null && filtro.getIntervaloMovimento().getDe() != null
                && filtro.getIntervaloMovimento().getAte() != null) {
            sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
            sql.append(" and cota_.ID not in (select COTA_ID from COTA_AUSENTE where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
        }
        
        sql.append("	    group by cota_.ID ");
        
    }
    
    private void montarQueryCotasComNotasEnvioEmitidas(final FiltroConsultaNotaEnvioDTO filtro,
            final StringBuilder sql, final boolean isCount) {
        if (isCount) {
            sql.append(" select cota_.ID ");
        } else {
          
            sql.append(" select lancamento_.STATUS AS status, ");	 
            sql.append(" cota_.ID AS idCota,"); 	 
            sql.append(" cota_.NUMERO_COTA AS numeroCota,"); 	 
            sql.append(" cota_.BOX_ID AS box, ");
            sql.append(" COALESCE(pessoa_cota_.nome,pessoa_cota_.razao_social) AS nomeCota,"); 	 
            sql.append(" cota_.SITUACAO_CADASTRO AS situacaoCadastro, ");
            sql.append(" SUM(COALESCE(nei.reparte, 0)) AS exemplares, ");
            sql.append(" SUM(COALESCE(nei.reparte, 0) * pe_.PRECO_VENDA) AS total,"); 
            sql.append(" CASE WHEN COUNT(nei.NOTA_ENVIO_ID)>0 THEN TRUE ELSE FALSE END notaImpressa,");
            sql.append(" roteiro_.ordem ordemRoteiro, ");			
            sql.append(" rota_.ordem ordemRota, ");			
            sql.append(" rota_pdv_.ordem ordemRotaPdv ");
        }
      
        sql.append(" FROM COTA cota_ ");
        sql.append(" LEFT  JOIN BOX box1_  ON cota_.BOX_ID=box1_.ID ");
        sql.append(" INNER JOIN ESTUDO_COTA ec_ ON cota_.ID=ec_.COTA_ID ");
        sql.append(" INNER JOIN ESTUDO e_  ON ec_.ESTUDO_ID=e_.ID ");
        sql.append(" INNER JOIN LANCAMENTO lancamento_  force index (ndx_status) ON e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID AND e_.ID=lancamento_.ESTUDO_ID ");        									
        sql.append(" INNER JOIN PRODUTO_EDICAO pe_  ON e_.PRODUTO_EDICAO_ID=pe_.ID ");
        sql.append(" INNER JOIN PRODUTO p_  ON pe_.PRODUTO_ID=p_.ID ");
        sql.append(" INNER JOIN PRODUTO_FORNECEDOR pf_  ON p_.ID=pf_.PRODUTO_ID ");
        sql.append(" INNER JOIN FORNECEDOR f_ ON pf_.fornecedores_ID=f_.ID ");
        sql.append(" INNER JOIN PDV pdv_ ON cota_.ID=pdv_.COTA_ID ");
        sql.append(" LEFT  JOIN ROTA_PDV rota_pdv_ ON pdv_.ID=rota_pdv_.PDV_ID ");
        sql.append(" LEFT  JOIN ROTA rota_ ON rota_pdv_.rota_ID=rota_.ID ");
        sql.append(" LEFT  JOIN ROTEIRO roteiro_ ON rota_.ROTEIRO_ID=roteiro_.ID ");
        sql.append(" INNER JOIN PESSOA pessoa_cota_ ON cota_.PESSOA_ID=pessoa_cota_.ID ");
        sql.append(" INNER JOIN NOTA_ENVIO_ITEM nei ON nei.ESTUDO_COTA_ID = ec_.ID and nei.PRODUTO_EDICAO_ID = pe_.ID "); 
        sql.append(" WHERE pdv_.ponto_principal = true ");
        sql.append(" AND nei.ESTUDO_COTA_ID is not null");
        sql.append(" AND lancamento_.STATUS NOT IN (:statusNaoEmitiveis) ");
        
        if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
            sql.append(" and ( f_.ID is null  or f_.ID in (:idFornecedores) )  ");
        }
        
        if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
            if (filtro.getIntervaloCota().getAte() != null) {
                
                sql.append(" and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
                
            } else {
                
                sql.append(" and cota_.NUMERO_COTA=:numeroCota ");
            }
            
        }
        
        if (filtro.getIntervaloBox() != null && filtro.getIntervaloBox().getDe() != null
                && filtro.getIntervaloBox().getAte() != null) {
            sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            sql.append(" and roteiro_.ID=:idRoteiro  ");
        }
        
        if (filtro.getIdRota() != null) {
            sql.append(" and rota_.ID=:idRota  ");
        }
        
        if (!filtro.isFiltroEspecial()) {
            sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
        }
        
        if (filtro.getIntervaloMovimento() != null && filtro.getIntervaloMovimento().getDe() != null
                && filtro.getIntervaloMovimento().getAte() != null) {
        		
        	sql.append(" AND lancamento_.DATA_LCTO_DISTRIBUIDOR BETWEEN :dataDe AND :dataAte ");
        	
        }
        
        sql.append(" group by cota_.ID ");
        
    }
    
    private void montarQueryCotasComNotasEnvioNaoEmitidas(final FiltroConsultaNotaEnvioDTO filtro,
            final StringBuilder sql, final boolean isCount) {
        
        if (isCount) {
            sql.append(" select cota_.ID ");
        } else {
            sql.append(" select lancamento_.STATUS as status,  ");
            sql.append(" cota_.ID as idCota, ");
            sql.append(" cota_.NUMERO_COTA as numeroCota, ");
            sql.append(" cota_.BOX_ID as box, ");
            sql.append(" coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  ");
            sql.append(" cota_.SITUACAO_CADASTRO as situacaoCadastro, ");
            sql.append(" sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE is null,coalesce(ec_.QTDE_EFETIVA,0),if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:gruposFaltaSobra), ec_.QTDE_EFETIVA, 0))) - ");
            sql.append(" sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposFalta), mec.QTDE,0)) + ");
            sql.append(" sum(if(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposSobra), mec.QTDE,0)) as exemplares, ");
            sql.append(" SUM(pe_.PRECO_VENDA * (IF(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IS NULL, COALESCE(ec_.QTDE_EFETIVA,0), "); 
            sql.append(" 	IF(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:gruposFaltaSobra), ec_.QTDE_EFETIVA, 0)) "); 
            sql.append("	- IF(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposFalta), mec.QTDE,0) ");
            sql.append("	+ IF(tipo_mov.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposSobra), mec.QTDE,0))) AS total, "); 
            sql.append(" CASE WHEN COUNT(nei.NOTA_ENVIO_ID)>0 THEN TRUE ELSE FALSE END notaImpressa,");
            sql.append(" roteiro_.ordem ordemRoteiro, rota_.ordem ordemRota, ");
            sql.append(" rota_pdv_.ordem ordemRotaPdv ");
        }
        
        sql.append(" FROM COTA cota_ ");
        sql.append(" LEFT  JOIN  BOX box1_ ON cota_.BOX_ID=box1_.ID ");
        sql.append(" INNER JOIN  ESTUDO_COTA ec_ ON cota_.ID=ec_.COTA_ID ");
        sql.append(" INNER JOIN  ESTUDO e_ ON ec_.ESTUDO_ID=e_.ID ");
        sql.append(" INNER JOIN  LANCAMENTO lancamento_ force index (ndx_status) ON e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID AND e_.ID=lancamento_.ESTUDO_ID ");
        sql.append(" LEFT  JOIN  MOVIMENTO_ESTOQUE_COTA mec ON mec.LANCAMENTO_ID=lancamento_.id AND mec.COTA_ID=cota_.ID ");
        sql.append(" LEFT  JOIN  TIPO_MOVIMENTO tipo_mov ON tipo_mov.ID=mec.TIPO_MOVIMENTO_ID ");
        sql.append(" INNER JOIN  PRODUTO_EDICAO pe_ ON e_.PRODUTO_EDICAO_ID=pe_.ID ");
        sql.append(" INNER JOIN  PRODUTO p_ ON pe_.PRODUTO_ID=p_.ID ");
        sql.append(" INNER JOIN  PRODUTO_FORNECEDOR pf_ ON p_.ID=pf_.PRODUTO_ID ");
        sql.append(" INNER JOIN  FORNECEDOR f_ ON pf_.fornecedores_ID=f_.ID ");
        sql.append(" INNER JOIN  PDV pdv_ ON cota_.ID=pdv_.COTA_ID ");
        sql.append(" LEFT  JOIN  ROTA_PDV rota_pdv_ ON pdv_.ID=rota_pdv_.PDV_ID ");
        sql.append(" LEFT  JOIN  ROTA rota_ ON rota_pdv_.rota_ID=rota_.ID ");
        sql.append(" LEFT  JOIN  ROTEIRO roteiro_ ON rota_.ROTEIRO_ID=roteiro_.ID ");
        sql.append(" INNER JOIN  PESSOA pessoa_cota_ ON cota_.PESSOA_ID=pessoa_cota_.ID ");
        sql.append(" LEFT  JOIN  NOTA_ENVIO_ITEM nei on nei.ESTUDO_COTA_ID = ec_ .ID  ");
        sql.append(" WHERE lancamento_.STATUS NOT IN (:statusNaoEmitiveis)"); 
        sql.append(" AND pdv_.ponto_principal = true "); 
        sql.append(" AND nei.ESTUDO_COTA_ID is null ");
        sql.append(" AND mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
        
        
        if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
            sql.append("and ( f_.ID is null or f_.ID in (:idFornecedores) ) ");
        }
        
        if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
            if (filtro.getIntervaloCota().getAte() != null) {
                
                sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
                
            } else {
                
                sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
            }
            
        }
        
        if (filtro.getIntervaloBox() != null && filtro.getIntervaloBox().getDe() != null
                && filtro.getIntervaloBox().getAte() != null) {
            sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            sql.append(" and roteiro_.ID=:idRoteiro  ");
        }
        
        if (filtro.getIdRota() != null) {
            sql.append(" and rota_.ID=:idRota  ");
        }
        
        if (!filtro.isFiltroEspecial()) {
            sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
        }
        
        if (filtro.getIntervaloMovimento() != null && filtro.getIntervaloMovimento().getDe() != null
                && filtro.getIntervaloMovimento().getAte() != null) {
            sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
            sql.append(" and cota_.ID not in (select COTA_ID from COTA_AUSENTE where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
        }
        
        sql.append(" group by cota_.ID ");
    }
    
    private void montarConsultaFuroProdutoNotaEmitida(final FiltroConsultaNotaEnvioDTO filtro,
            final StringBuilder sql, final boolean isCount){
    	
    	if (isCount) {
            sql.append(" select cota_.ID ");
    	} else {
    		
    		sql.append(" select lancamento_.STATUS AS status, ");	 
    		sql.append(" cota_.ID AS idCota, ");	 
    		sql.append(" cota_.NUMERO_COTA AS numeroCota, "); 	 
    		sql.append(" cota_.BOX_ID AS box, ");
    		sql.append(" COALESCE(pessoa_cota_.nome,pessoa_cota_.razao_social) AS nomeCota, "); 	 
    		sql.append(" cota_.SITUACAO_CADASTRO AS situacaoCadastro, ");
    		sql.append(" SUM(COALESCE(nei.reparte, 0)) AS exemplares, "); 
    		sql.append(" SUM(COALESCE(nei.reparte, 0) * pe_.PRECO_VENDA) AS total, "); 
    		sql.append(" CASE WHEN COUNT(nei.NOTA_ENVIO_ID)>0 THEN TRUE ELSE FALSE END notaImpressa, ");
    		sql.append(" roteiro_.ordem ordemRoteiro, ");			
    		sql.append(" rota_.ordem ordemRota, ");			
    		sql.append(" rota_pdv_.ordem ordemRotaPdv ");
    	}
    
    	sql.append(" FROM COTA cota_ ");
    	sql.append(" LEFT  JOIN BOX box1_  ON cota_.BOX_ID=box1_.ID ");
    	sql.append(" INNER JOIN ESTUDO_COTA ec_ ON cota_.ID=ec_.COTA_ID ");
    	sql.append(" INNER JOIN ESTUDO e_  ON ec_.ESTUDO_ID=e_.ID ");
    	sql.append(" INNER JOIN LANCAMENTO lancamento_ force index (ndx_status) ON e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID AND e_.ID=lancamento_.ESTUDO_ID ");
    	sql.append(" INNER JOIN PRODUTO_EDICAO pe_  ON e_.PRODUTO_EDICAO_ID=pe_.ID ");
    	sql.append(" INNER JOIN PRODUTO p_  ON pe_.PRODUTO_ID=p_.ID ");
    	sql.append(" INNER JOIN PRODUTO_FORNECEDOR pf_  ON p_.ID=pf_.PRODUTO_ID ");
    	sql.append(" INNER JOIN FORNECEDOR f_ ON pf_.fornecedores_ID=f_.ID ");
    	sql.append(" INNER JOIN PDV pdv_ ON cota_.ID=pdv_.COTA_ID ");
    	sql.append(" LEFT  JOIN ROTA_PDV rota_pdv_ ON pdv_.ID=rota_pdv_.PDV_ID ");
    	sql.append(" LEFT  JOIN ROTA rota_ ON rota_pdv_.rota_ID=rota_.ID ");
    	sql.append(" LEFT  JOIN ROTEIRO roteiro_ ON rota_.ROTEIRO_ID=roteiro_.ID ");
    	sql.append(" INNER JOIN PESSOA pessoa_cota_ ON cota_.PESSOA_ID=pessoa_cota_.ID ");
    	sql.append(" INNER JOIN FURO_PRODUTO furoProduto ON furoProduto.LANCAMENTO_ID = lancamento_.ID ");
    	sql.append(" INNER JOIN NOTA_ENVIO_ITEM nei ON nei.FURO_PRODUTO_ID  = furoProduto.ID ");
    	sql.append(" WHERE pdv_.ponto_principal = true ");
        sql.append(" AND nei.ESTUDO_COTA_ID is null");
        sql.append(" AND lancamento_.STATUS NOT IN (:statusNaoEmitiveis) ");
        
        
    	if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
            sql.append(" and ( f_.ID is null  or f_.ID in (:idFornecedores) )  ");
        }
        
        if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
            if (filtro.getIntervaloCota().getAte() != null) {
                
                sql.append(" and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
                
            } else {
                
                sql.append(" and cota_.NUMERO_COTA=:numeroCota ");
            }
        }
        
        if (filtro.getIntervaloBox() != null && filtro.getIntervaloBox().getDe() != null
                && filtro.getIntervaloBox().getAte() != null) {
            sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            sql.append(" and roteiro_.ID=:idRoteiro  ");
        }
        
        if (filtro.getIdRota() != null) {
            sql.append(" and rota_.ID=:idRota  ");
        }
        
        if (!filtro.isFiltroEspecial()) {
            sql.append(" and roteiro_.TIPO_ROTEIRO!=:roteiroEspecial  ");
        }
        
        if (filtro.getIntervaloMovimento() != null && filtro.getIntervaloMovimento().getDe() != null
                && filtro.getIntervaloMovimento().getAte() != null) {
        		
        	sql.append(" AND furoProduto.DATA_LCTO_DISTRIBUIDOR BETWEEN :dataDe AND :dataAte ");
        }
       
        sql.append(" group by cota_.ID ");
    }
    
    private void montarParametrosFiltroNotasEnvio(final FiltroConsultaNotaEnvioDTO filtro, final Query query,
            final boolean isCount) {
        
        query.setParameter("principal", true);
        
        query.setParameterList("status", new String[] { StatusLancamento.CONFIRMADO.name(),
                StatusLancamento.EM_BALANCEAMENTO.name() });
        
        query.setParameterList("statusNaoEmitiveis", new String[] {StatusLancamento.PLANEJADO.name(),
                StatusLancamento.FECHADO.name(), StatusLancamento.CONFIRMADO.name(),
                StatusLancamento.EM_BALANCEAMENTO.name(), StatusLancamento.CANCELADO.name() });
        
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
        
        if (filtro.getIntervaloBox() != null && filtro.getIntervaloBox().getDe() != null
                && filtro.getIntervaloBox().getAte() != null) {
            
            query.setParameter("boxDe", filtro.getIntervaloBox().getDe());
            query.setParameter("boxAte", filtro.getIntervaloBox().getAte());
        }
        
        if (filtro.getIdRoteiro() != null) {
            
            query.setParameter("idRoteiro", filtro.getIdRoteiro());
        }
        
        if (filtro.getIdRota() != null) {
            
            query.setParameter("idRota", filtro.getIdRota());
        }
        
        if (filtro.getIntervaloMovimento() != null && filtro.getIntervaloMovimento().getDe() != null
                && filtro.getIntervaloMovimento().getAte() != null) {
            query.setParameter("dataDe", filtro.getIntervaloMovimento().getDe());
            query.setParameter("dataAte", filtro.getIntervaloMovimento().getAte());
        }
        
        if (!filtro.isFiltroEspecial()) {
            query.setParameter("roteiroEspecial", TipoRoteiro.ESPECIAL.name());
        }
        
        if (!isCount) {
            
            if (filtro.getPaginacaoVO().getPosicaoInicial() != null) {
                query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
            }
            
            if (filtro.getPaginacaoVO().getQtdResultadosPorPagina() != null) {
                query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
            }
            
        }
        
    }
    
    private void orderByCotasComNotaEnvioEntre(final StringBuilder sql, final String sortName, final String sortOrder) {
        
        if ("numeroCota".equals(sortName)) {
            sql.append(" order by numeroCota " + sortOrder + ", notaImpressa ");
        }
        
        if ("nomeCota".equals(sortName)) {
            sql.append(" order by  nomeCota " + sortOrder);
        }
        
        if ("exemplares".equals(sortName)) {
            sql.append(" order by  exemplares " + sortOrder);
        }
        
        if ("total".equals(sortName)) {
            sql.append(" order by  total " + sortOrder);
        }
        
        if ("situacaoCadastro".equals(sortName)) {
            sql.append(" order by  cota_.SITUACAO_CADASTRO " + sortOrder);
        }
        
        if ("notaImpressa".equals(sortName)) {
            sql.append(" order by  notaImpressa " + sortOrder);
        }
        
        if ("roteirizacao".equals(sortName)) {
            
            sql.append(" order by box, ordemRoteiro, ordemRota, ordemRotaPdv " + sortOrder);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<Cota> obterCotasPorFornecedor(final Long idFornecedor) {
        
        final String queryString = " select cota from Cota cota " + " join fetch cota.fornecedores fornecedores "
                + " where fornecedores.id = :idFornecedor ";
        
        final Query query = this.getSession().createQuery(queryString);
        
        query.setParameter("idFornecedor", idFornecedor);
        
        return new HashSet<Cota>(query.list());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaTipoDTO> obterCotaPorTipo(final TipoDistribuicaoCota tipoCota, final Integer page,
            final Integer rp, final String sortname, final String sortorder) {
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("tipoCota", tipoCota);
        
        query.setParameterList("situacoesCadastro", Arrays.asList(SituacaoCadastro.ATIVO, SituacaoCadastro.SUSPENSO));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaTipoDTO.class));
        
        query.setFirstResult((rp * page) - rp);
        
        query.setMaxResults(rp);
        
        return query.list();
    }
    
    private void gerarWhereFromObterCotaPorTipo(final StringBuilder hql) {
        
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
    
    private void gerarOrderByObterCotaPorTipo(final StringBuilder hql, final String sortname, final String sortorder) {
        
        if (sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty()) {
            return;
        }
        
        if ("numCota".equals(sortname)) {
            hql.append(" ORDER BY numCota ");
        } else if ("nome".equals(sortname)) {
            hql.append(" ORDER BY nome ");
        } else if ("municipio".equals(sortname)) {
            hql.append(" ORDER BY municipio ");
        } else if ("endereco".equals(sortname)) {
            hql.append(" ORDER BY endereco ");
        }
        
        if ("desc".equals(sortorder)) {
            hql.append(" DESC ");
        } else {
            hql.append(" ASC ");
        }
    }
    
    @Override
    public int obterCountCotaPorTipo(final TipoDistribuicaoCota tipoCota) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select count(distinct cota.id) ");
        
        gerarWhereFromObterCotaPorTipo(hql);
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("tipoCota", tipoCota);
        
        query.setParameterList("situacoesCadastro", Arrays.asList(SituacaoCadastro.ATIVO, SituacaoCadastro.SUSPENSO));
        
        return ((Long) query.uniqueResult()).intValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<MunicipioDTO> obterQtdeCotaMunicipio(final Integer page, final Integer rp, final String sortname,
            final String sortorder) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select  ");
        hql.append(" 		endereco.cidade as municipio, ");
        hql.append(" 		count(cota.id) as qtde ");
        
        gerarWhereFromObterQtdeCotaMunicipio(hql);
        
        hql.append(" group by endereco.cidade ");
        
        gerarOrderByObterQtdeCotaMunicipio(hql, sortname, sortorder);
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameterList("situacoesCota", Arrays.asList(SituacaoCadastro.PENDENTE, SituacaoCadastro.INATIVO));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(MunicipioDTO.class));
        
        query.setFirstResult((rp * page) - rp);
        
        query.setMaxResults(rp);
        
        return query.list();
    }
    
    private void gerarOrderByObterQtdeCotaMunicipio(final StringBuilder hql, final String sortname,
            final String sortorder) {
        
        if (sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty()) {
            return;
        }
        
        if ("municipio".equals(sortname)) {
            hql.append(" ORDER BY municipio ");
        } else if ("qtde".equals(sortname)) {
            hql.append(" ORDER BY qtde ");
        }
        
        if ("desc".equals(sortorder)) {
            hql.append(" DESC ");
        } else {
            hql.append(" ASC ");
        }
        
    }
    
    private void gerarWhereFromObterQtdeCotaMunicipio(final StringBuilder hql) {
        
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
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select count(distinct endereco.cidade) ");
        
        gerarWhereFromObterQtdeCotaMunicipio(hql);
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameterList("situacoesCota", Arrays.asList(SituacaoCadastro.PENDENTE, SituacaoCadastro.INATIVO));
        
        return ((Long) query.uniqueResult()).intValue();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCota obterHistoricoTitularidade(final Long idCota, final Long idHistorico) {
        Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico de titularidade não deve ser nulo!");
        final String hql = "from HistoricoTitularidadeCota historico where historico.id = :idHistorico and historico.cota.id = :idCota";
        final Query query = getSession().createQuery(hql);
        query.setParameter("idHistorico", idHistorico);
        query.setParameter("idCota", idCota);
        return (HistoricoTitularidadeCota) query.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaFormaPagamento obterFormaPagamentoHistoricoTitularidade(final Long idFormaPagto) {
        Validate.notNull(idFormaPagto, "Identificador da forma de pagamento não deve ser nulo!");
        
        final String hql = "from HistoricoTitularidadeCotaFormaPagamento where id = :id";
        final Query query = getSession().createQuery(hql);
        query.setParameter("id", idFormaPagto);
        return (HistoricoTitularidadeCotaFormaPagamento) query.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidade(final Long idSocio) {
        Validate.notNull(idSocio, "Identificador do sócio não deve ser nulo!");
        
        final String hql = "from HistoricoTitularidadeCotaSocio where id = :id";
        final Query query = getSession().createQuery(hql);
        query.setParameter("id", idSocio);
        return (HistoricoTitularidadeCotaSocio) query.uniqueResult();
    }
    
    @Override
    public void ativarCota(final Integer numeroCota) {
        
        final Query query = this.getSession().createQuery(
                "update Cota set situacaoCadastro = :status where numeroCota = :numeroCota");
        
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("status", SituacaoCadastro.ATIVO);
        
        query.executeUpdate();
    }
    
    @Override
    public Cota buscarCotaPorID(final Long idCota) {
        
        final String queryString = " select cota from Cota cota " + " left join fetch cota.fornecedores fornecedores "
                + " where cota.id = :idCota ";
        
        final Query query = this.getSession().createQuery(queryString);
        
        query.setParameter("idCota", idCota);
        
        return (Cota) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> buscarCotasQuePossuemRangeReparte(final BigInteger qtdReparteInicial,
            final BigInteger qtdReparteFinal, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto,
            final boolean cotasAtivas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        if (qtdReparteInicial != null && qtdReparteInicial.intValue() >= 0 && qtdReparteFinal != null
                && qtdReparteFinal.intValue() >= 0) {
            
            hql.append("and estoqueProdutoCota.qtdeRecebida >= :reparteInicial and estoqueProdutoCota.qtdeRecebida <= :reparteFinal");
            
            parameters.put("reparteInicial", qtdReparteInicial);
            parameters.put("reparteFinal", qtdReparteFinal);
        }
        
        hql.append(" GROUP BY cota.numeroCota ");
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> buscarCotasQuePossuemRangeVenda(final BigInteger qtdVendaInicial,
            final BigInteger qtdVendaFinal, final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        if (qtdVendaInicial != null && qtdVendaInicial.intValue() >= 0 && qtdVendaFinal != null
                && qtdVendaFinal.intValue() >= 0) {
            hql.append(" HAVING avg(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) between :qtdVendaInicial and :qtdVendaFinal");
            parameters.put("qtdVendaInicial", qtdVendaInicial.doubleValue());
            parameters.put("qtdVendaFinal", qtdVendaFinal.doubleValue());
        }
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(final BigDecimal percentualVenda,
            final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> buscarCotasPorNomeOuNumero(final CotaDTO cotaDto,
            final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        if (cotaDto != null) {
            if (cotaDto.getNumeroCota() != null && !cotaDto.getNumeroCota().equals(0)) {
                hql.append(" cota.numeroCota = :numeroCota and ");
                parameters.put("numeroCota", cotaDto.getNumeroCota());
            } else if (cotaDto.getNomePessoa() != null && !cotaDto.getNomePessoa().isEmpty()) {
                hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa and ");
                parameters.put("nomePessoa", cotaDto.getNomePessoa());
            }
        }
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> buscarCotasPorComponentes(final ComponentesPDV componente, final String elemento,
            final List<ProdutoEdicaoDTO> listProdutoEdicaoDto, final boolean cotasAtivas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        final StringBuilder whereParameter = new StringBuilder();
        
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
            parameters.put("idGeradorFluxoPDV", Long.parseLong(elemento));
            
            break;
        case COTAS_A_VISTA:
            hql.append(" LEFT JOIN cota.parametroCobranca as parametroCobranca ");
            
            whereParameter.append(" parametroCobranca.tipoCota = :tipoCota AND");
            parameters.put("tipoCota", TipoCota.A_VISTA);
            
            break;
        case COTAS_NOVAS_RETIVADAS:
            whereParameter.append(" cota.id in (SELECT cotaBase.cota.id FROM CotaBase as cotaBase) AND ");
            
            break;
        case REGIAO:
            whereParameter
            .append(" cota.id in (SELECT registro.cota.id FROM RegistroCotaRegiao as registro WHERE regiao.id = :regiaoId) AND ");
            parameters.put("regiaoId", Long.parseLong(elemento));
            
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
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<AnaliseHistoricoDTO> buscarHistoricoCotas(final List<ProdutoEdicaoDTO> listProdutoEdicaoDto,
            final List<Integer> numeroCotas) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" SELECT ");
        hql.append(" cota.numeroCota as numeroCota, ");
        hql.append(" cota.situacaoCadastro as statusCota, ");
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
        hql.append(" count(DISTINCT pdvs) as qtdPdv, ");
        hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
        hql.append(" produto.codigo as codigoProduto ");
        
        hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
        hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
        hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
        hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
        hql.append(" LEFT JOIN cota.pdvs as pdvs ");
        hql.append(" LEFT JOIN cota.pessoa as pessoa ");
        
        hql.append(" WHERE ");
        boolean useAnd = false;
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            // Populando o in ('','') do código produto
            hql.append(" produto.codigo in ( ");
            for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
                
                hql.append("'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
                
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
        
        if (numeroCotas != null && !numeroCotas.isEmpty()) {
            
            hql.append(useAnd ? " and " : " where ");
            
            hql.append(" cota.numeroCota in (:numeroCotas)");
            parameters.put("numeroCotas", numeroCotas);
            
            useAnd = true;
        }
        
        hql.append(" GROUP BY cota.numeroCota ");
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseHistoricoDTO.class));
        
        return query.list();
    }
    
    @Override
    public HistoricoVendaPopUpCotaDto buscarCota(final Integer numero) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT                                                                                  ")
	       .append("     cota.NUMERO_COTA as numeroCota,                                                     ")
	       .append("     coalesce(pessoa.NOME_FANTASIA, pessoa.RAZAO_SOCIAL, pessoa.NOME, '') as nomePessoa, ")
	       .append("     cota.TIPO_DISTRIBUICAO_COTA as tipoDistribuicaoCota,                                ")
	       .append("     rankingFaturamento.FATURAMENTO as faturamento,                                      ")
	       .append("     max(rankingFaturamento.DATA_GERACAO_RANK) as  dataGeracao,                          ")
	       .append("                                                                                         ")
	       .append("       (SELECT T.rank                                                                    ")
	       .append("         FROM (SELECT  rf.COTA_ID as cota,                                               ")
	       .append("           @curRank /*'*/:=/*'*/ @curRank + 1 AS rank                                    ")
	       .append("           FROM ranking_faturamento rf, (SELECT @curRank /*'*/:=/*'*/ 0) r               ")
	       .append("           where rf.COTA_ID                                                              ")
	       .append("           ORDER BY  rf.FATURAMENTO desc) T where  T.cota = cota.ID) as rankId           ")
           .append("                                                                                         ")
           .append(" FROM ranking_faturamento rankingFaturamento                                             ")
           .append(" join cota ON rankingFaturamento.COTA_ID = cota.ID                                       ")
           .append(" join pessoa ON cota.PESSOA_ID = pessoa.ID                                               ")
           .append("                                                                                         ")
           .append(" WHERE cota.NUMERO_COTA = :numeroCota                                                    ")
           .append(" GROUP BY rankingFaturamento.COTA_ID                                                     ");
        
        final SQLQuery query = super.getSession().createSQLQuery(sql.toString());
        
        query.setParameter("numeroCota", numero);
        
        query.addScalar("nomePessoa", StandardBasicTypes.STRING);
        query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
        query.addScalar("tipoDistribuicaoCota", StandardBasicTypes.STRING);
        query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("dataGeracao", StandardBasicTypes.DATE);
        query.addScalar("rankId", StandardBasicTypes.LONG);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(HistoricoVendaPopUpCotaDto.class));
        
        return (HistoricoVendaPopUpCotaDto) query.uniqueResult();
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer buscarNumeroCotaPorId(final Long idCota) {
        
        final Criteria criteria = getSession().createCriteria(Cota.class);
        
        criteria.add(Restrictions.eq("id", idCota));
        
        criteria.setProjection(Projections.property("numeroCota"));
        
        return (Integer) criteria.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Long> obterIdsCotasPorMunicipio(final String municipio) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select cota.id ");
        
        hql.append(" from Cota cota ");
        hql.append(" join cota.enderecos enderecoCota ");
        hql.append(" join enderecoCota.endereco endereco ");
        
        hql.append(" where enderecoCota.principal=true ");
        hql.append(" and endereco.cidade = :cidade ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("cidade", municipio);
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterCotaPorProdutoEdicaoData(final FiltroMapaAbastecimentoDTO filtro) {
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select distinct estudoCota.cota as cota");
        
        hql.append(" from EstudoCota estudoCota ");
        hql.append(" join estudoCota.estudo estudo ");
        
        hql.append(" join estudoCota.cota cota ");
        hql.append(" join estudo.produtoEdicao pe ");
        hql.append(" join pe.produto p ");
        hql.append(" join pe.lancamentos l ");
        hql.append(" where ");
        
        hql.append(" ( l.status = :balanceado or l.status = :expedido ) and ");
        hql.append(" l.dataLancamentoPrevista = :dataPrevista ");
        
        if (filtro.getCodigosProduto() != null && !filtro.getCodigosProduto().isEmpty()){
            
            hql.append(" and p.codigo in (:codigoProduto) ");
        }
        
        if (filtro.getNumerosEdicao() != null && !filtro.getNumerosEdicao().isEmpty()){
            
            hql.append(" and pe.numeroEdicao in (:numeroEdicao) ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameterList("codigoProduto", filtro.getCodigosProduto());
        query.setParameterList("numeroEdicao", filtro.getNumerosEdicao());
        query.setParameter("balanceado", StatusLancamento.BALANCEADO);
        query.setParameter("expedido", StatusLancamento.EXPEDIDO);
        query.setParameter("dataPrevista", filtro.getDataDate());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        return query.list();
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaDTO> obterCotasSemRoteirizacao(final Intervalo<Integer> intervaloCota,
            final Intervalo<Date> intervaloDataLancamento, final Intervalo<Date> intervaloDateRecolhimento) {
        
        final StringBuilder hql = new StringBuilder("").append(" select ").append(" c.NUMERO_COTA as numeroCota, ")
                .append(" coalesce(p.NOME, p.RAZAO_SOCIAL, p.NOME_FANTASIA) as nomePessoa ").append(" from COTA c ")
                .append(" inner join PESSOA p on c.PESSOA_ID=p.ID ").append(
                        " inner join ESTUDO_COTA ec on c.ID=ec.COTA_ID ").append(
                                " inner join ESTUDO e on ec.ESTUDO_ID=e.ID ");
        
        if (intervaloDataLancamento != null) {
            hql.append(" inner join LANCAMENTO l on e.PRODUTO_EDICAO_ID=l.PRODUTO_EDICAO_ID and e.DATA_LANCAMENTO=l.DATA_LCTO_PREVISTA ");
        }
        
        if (intervaloDateRecolhimento != null) {
            hql.append(" inner join chamada_encalhe_cota cec on cec.cota_id = c.id ")
            .append(" inner join chamada_encalhe ce on ce.id = cec.chamada_encalhe_id and ce.produto_edicao_id = e.produto_edicao_id ");
        }
        
        hql.append(" where 1=1").append(" and ( ").append(" 	c.NUMERO_COTA not in ( ").append(
                "     	select distinct c.NUMERO_COTA ").append("     	from ROTEIRIZACAO rot ").append(
                        "     	inner join ROTEIRO r on rot.ID=r.ROTEIRIZACAO_ID ").append(
                                "     	inner join ROTA rota on r.ID=rota.ROTEIRO_ID ").append(
                                        "     	inner join ROTA_PDV rp on rota.ID=rp.ROTA_ID ").append(
                                                "     	inner join PDV pdv on rp.PDV_ID=pdv.ID ").append("     	inner join COTA c on pdv.COTA_ID=c.ID ")
                                                .append("     	inner join ESTUDO_COTA ec on c.ID=ec.COTA_ID ").append(
                                                        "     	inner join ESTUDO e on ec.ESTUDO_ID=e.ID ");
        
        hql.append(" 	where rot.box_id is not null ) ").append(" ) ");
        
        if (intervaloCota != null && intervaloCota.getAte() != null && intervaloCota.getDe() != null) {
            hql.append(" and ( ").append(" 	c.NUMERO_COTA between :de and :ate ").append(" ) ");
        }
        
        if (intervaloDataLancamento != null) {
            hql.append(" and ( ")
            .append(" 	l.DATA_LCTO_DISTRIBUIDOR between :dataLancamentoDe and :dataLancamentoAte ").append(
                    " ) ");
        }
        
        if (intervaloDateRecolhimento != null) {
            hql.append(" and ( ")
            .append(" 	ce.data_recolhimento between :dataRecolhimentoDe and :dataRecolhimentoAte ").append(
                    " ) ");
        }
        
        hql.append(" and ( ").append(" 	c.SITUACAO_CADASTRO not in (:inativo, :pendente) ").append(" ) ").append(
                " group by c.NUMERO_COTA ").append(" order by c.NUMERO_COTA ");
        
        final SQLQuery query = this.getSession().createSQLQuery(hql.toString());
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
    public BigDecimal obterTotalDividaCotasSujeitasSuspensao(final Date dataOperacaoDistribuidor) {
        
        final StringBuilder hql = new StringBuilder("SELECT SUM(COALESCE(total.valor,0)) FROM (SELECT  ");
        
        hql.append("( SELECT SUM(round(COALESCE(D.VALOR,0), 2)) ")
           .append("	FROM DIVIDA D ")
           .append(" JOIN COBRANCA c on (c.DIVIDA_ID=d.ID) ")
           .append("	WHERE D.COTA_ID = COTA_.ID ")
           .append("    AND c.DT_PAGAMENTO is null ")
           .append("	AND D.STATUS in (:statusDividaEmAbertoPendente) ")
           .append("	AND C.DT_VENCIMENTO < :dataOperacao ")
           .append("    AND c.STATUS_COBRANCA = :statusCobrancaNaoPago ")
           .append(") as valor ");
        
        this.setFromWhereCotasSujeitasSuspensao(hql);
        
        hql.append(") as total ");
        
        final Query query = this.getSession().createSQLQuery(hql.toString());
        
        this.setParametrosCotasSujeitasSuspensao(query, dataOperacaoDistribuidor);
        
        return (BigDecimal) query.uniqueResult();
    }
    
    /**
     * Obtem Cotas do tipo À Vista, com data de alteração de status menor que a
     * data atual
     * 
     * @return List<Cota>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Cota> obterCotasTipoAVista() {
        
        final StringBuilder hql = new StringBuilder("select c ")
        
        .append(" from Cota c ")
        
        .append(" where c.tipoCota = :tipoCota ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("tipoCota", TipoCota.A_VISTA);
        
        return query.list();
    }
    
    @Override
    public List<CotaDTO> obterCotasPorNomeAutoComplete(final String nome) {
        
        final List<?> lista = super
                .getSession()
                .createSQLQuery(
                        "select c.ID, c.NUMERO_COTA, p.NOME, c.SITUACAO_CADASTRO from COTA c join PESSOA p on p.ID = c.PESSOA_ID where p.nome like ?")
                        .addScalar("ID", LongType.INSTANCE).addScalar("NUMERO_COTA", IntegerType.INSTANCE).addScalar("NOME",
                                StringType.INSTANCE).addScalar("SITUACAO_CADASTRO", StringType.INSTANCE).setParameter(0,
                                        "%" + nome + "%").setMaxResults(10).list();
        final Object[] retorno = lista.toArray();
        final List<CotaDTO> cotas = new ArrayList<>();
        for (int i = 0; i < retorno.length; i++) {
            final CotaDTO cota = new CotaDTO();
            cota.setIdCota((Long) ((Object[]) retorno[i])[0]);
            cota.setNumeroCota((Integer) ((Object[]) retorno[i])[1]);
            cota.setNomePessoa((String) ((Object[]) retorno[i])[2]);
            cota.setStatus(SituacaoCadastro.valueOf((String) ((Object[]) retorno[i])[3]));
            cotas.add(cota);
        }
        return cotas;
    }
    
    @Override
    public boolean cotaVinculadaCotaBase(final Long idCota) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select count(*) from CotaBase");
        hql.append(" where cota.id = :idCota");
        
        final Query query = getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        
        final Long count = (Long) query.uniqueResult();
        
        return count > 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> numeroCotaExiste(final TipoDistribuicaoCota tipoDistribuicaoCota, final Integer... cotaIdArray) {
        
        final StringBuilder hql = new StringBuilder();
        hql.append("select c.NUMERO_COTA ");
        hql.append("  from cota c ");
        hql.append(" where c.NUMERO_COTA in (:cotaIDList)");
        hql.append("   and c.SITUACAO_CADASTRO in (upper(:situacaoCadastroAtivo), upper(:situacaoCadastroSuspenso)) ");
        hql.append("   and c.TIPO_DISTRIBUICAO_COTA = upper(:tipoDistribuicaoCota) ");
        
        final SQLQuery query = getSession().createSQLQuery(hql.toString());
        query.setParameterList("cotaIDList", cotaIdArray);
        query.setParameter("situacaoCadastroAtivo", SituacaoCadastro.ATIVO.toString());
        query.setParameter("situacaoCadastroSuspenso", SituacaoCadastro.SUSPENSO.toString());
        query.setParameter("tipoDistribuicaoCota", tipoDistribuicaoCota.toString());
        
        return query.list();
    }
    
    @Override
    public Cota obterCotaComBaseReferencia(final Long idCota) {
        
        final Cota cota = (Cota) super.getSession().load(Cota.class, idCota);
        
        final BaseReferenciaCota base = (BaseReferenciaCota) super.getSession()
                .createCriteria(BaseReferenciaCota.class).add(Restrictions.eq("cota.id", idCota)).uniqueResult();
        cota.setBaseReferenciaCota(base);
        
        return cota;
    }
    
    @Override
    public TipoDistribuicaoCota obterTipoDistribuicaoCotaPorNumeroCota(final Integer numeroCota) {
        
        final StringBuilder query = new StringBuilder();
        query.append("select tipoDistribuicaoCota from Cota where numeroCota = :numeroCota and situacaoCadastro = 'Ativo'");
        
        final Query q = getSession().createQuery(query.toString());
        
        q.setParameter("numeroCota", numeroCota);
        
        return (TipoDistribuicaoCota) q.uniqueResult();
    }
    
    @Override
    public int obterCotasAtivas() {
        
        return ((Number) getSession().createCriteria(Cota.class).add(
                Restrictions.eq("situacaoCadastro", SituacaoCadastro.ATIVO)).setProjection(Projections.rowCount())
                .uniqueResult()).intValue();
    }
    
    @Override
    public CotaDTO buscarCotaPorNumero(final Integer numeroCota, final String codigoProduto, final Long idClassifProdEdicao) {
        
        final StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("  cota.numero_cota numeroCota, ");
        sql.append("  coalesce(pe.nome, pe.razao_social, pe.nome_fantasia, '') nomePessoa, ");
        sql.append("  cota.tipo_distribuicao_cota tipoCota, ");
//        sql.append("  rks.qtde qtdeRankingSegmento, "); Trocar para posição do ranking FATURAMENTO
        
        sql.append("                                                                                         ");
	    sql.append("       (SELECT CAST(T.rank AS UNSIGNED INTEGER)                                          ");
	    sql.append("         FROM (SELECT  rf.COTA_ID as cota,                                               ");
	    sql.append("           @curRank /*'*/:=/*'*/ @curRank + 1 AS rank                                    ");
	    sql.append("           FROM ranking_faturamento rf, (SELECT @curRank /*'*/:=/*'*/ 0) r               ");
	    sql.append("           where rf.COTA_ID                                                              ");
	    sql.append("           ORDER BY  rf.FATURAMENTO desc) T where T.cota = cota.ID) as qtdeRankingSegmento, ");
        sql.append("                                                                                         ");
        
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
        sql.append("   left join produto p on p.codigo_icd = :codigoProduto");
        sql.append("   left join ranking_segmento rks on rks.cota_id = cota.id and p.tipo_segmento_produto_id = rks.tipo_segmento_produto_id ");
        sql.append("   left join ranking_faturamento rkf on rkf.cota_id = cota.id ");
        sql.append("   left join mix_cota_produto mix on mix.id_cota = cota.id and p.codigo_icd = mix.codigo_icd and mix.TIPO_CLASSIFICACAO_PRODUTO_ID = :idClassificacao ");
        sql.append("   left join usuario u on u.id = mix.id_usuario ");
        sql.append("   left join fixacao_reparte fx on fx.id_cota = cota.id and p.codigo_icd = fx.codigo_icd and fx.ID_CLASSIFICACAO_EDICAO = :idClassificacao ");
        sql.append(" where cota.numero_cota = :numeroCota ");
        sql.append(" Group By cota.numero_cota ");
        
        final SQLQuery query = getSession().createSQLQuery(sql.toString());
        
        query.setParameter("codigoProduto", codigoProduto);
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("idClassificacao", idClassifProdEdicao);
        
        query.addScalar("numeroCota",StandardBasicTypes.INTEGER)
        .addScalar("nomePessoa",StandardBasicTypes.STRING)
        .addScalar("tipoCota",StandardBasicTypes.STRING)
        .addScalar("qtdeRankingSegmento",StandardBasicTypes.INTEGER)
        .addScalar("faturamento",StandardBasicTypes.BIG_DECIMAL)
        .addScalar("dataGeracaoRank",StandardBasicTypes.DATE)
        .addScalar("mixRepMin",StandardBasicTypes.BIG_INTEGER)
        .addScalar("mixRepMax",StandardBasicTypes.BIG_INTEGER)
        .addScalar("nomeUsuario",StandardBasicTypes.STRING)
        .addScalar("mixDataAlteracao",StandardBasicTypes.DATE)
        .addScalar("fxDataAlteracao",StandardBasicTypes.DATE)
        .addScalar("fxEdicaoInicial",StandardBasicTypes.INTEGER)
        .addScalar("fxEdicaoFinal",StandardBasicTypes.INTEGER)
        .addScalar("fxEdicoesAtendidas",StandardBasicTypes.INTEGER)
        .addScalar("fxQuantidadeEdicoes",StandardBasicTypes.INTEGER)
        .addScalar("fxQuantidadeExemplares",StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        return (CotaDTO) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaEstudo> getInformacoesCotaEstudo(final ProdutoEdicao produtoEdicao) {
        
        final StringBuilder sql = new StringBuilder();
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
        
        final Query query = getSession().createSQLQuery(sql.toString()).addScalar("id", StandardBasicTypes.LONG)
                .addScalar("numeroCota", StandardBasicTypes.INTEGER).addScalar("status", StandardBasicTypes.STRING)
                .addScalar("intervaloMaximo", StandardBasicTypes.BIG_INTEGER).addScalar("intervaloMinimo",
                        StandardBasicTypes.BIG_INTEGER).addScalar("tipoDistribuicao", StandardBasicTypes.STRING)
                        .addScalar("reparteFixado", StandardBasicTypes.BIG_INTEGER)
                        .addScalar("mix", StandardBasicTypes.BOOLEAN).addScalar("cotaNaoRecebeSegmento",
                                StandardBasicTypes.BOOLEAN).addScalar("cotaExcecaoSegmento", StandardBasicTypes.BOOLEAN)
                                .addScalar("cotaNaoRecebeClassificacao", StandardBasicTypes.BOOLEAN);
        
        query.setParameter("produto_id", produtoEdicao.getProduto().getId());
        query.setParameter("numero_edicao", produtoEdicao.getNumeroEdicao());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaEstudo.class));
        return query.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CotaDTO> buscarCotasHistorico(final List<ProdutoEdicaoDTO> listProdutoEdicaoDto,
            final boolean cotasAtivas) {
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final StringBuilder hql = new StringBuilder();
        
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
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            
            hql.append(" produto.codigo in (:produtoCodigoList) and ");
            hql.append(" produtoEdicao.numeroEdicao in (:produtoEdicaoNumeroList)");
            
        }
        
        hql.append(" GROUP BY cota.numeroCota ");
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        this.setParameters(query, parameters);
        
        if (listProdutoEdicaoDto != null && !listProdutoEdicaoDto.isEmpty()) {
            final List<String> listCodProduto = new ArrayList<>();
            final List<Long> listNumEdicao = new ArrayList<>();
            
            for (final ProdutoEdicaoDTO listProduto : listProdutoEdicaoDto) {
                listCodProduto.add(listProduto.getCodigoProduto());
                listNumEdicao.add(listProduto.getNumeroEdicao());
            }
            
            query.setParameterList("produtoCodigoList", listCodProduto);
            query.setParameterList("produtoEdicaoNumeroList", listNumEdicao);
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
        
        return query.list();
    }
    
    @Override
    public SituacaoCadastro obterSituacaoCadastroCota(final Integer numeroCota) {
        
        final Query query = this.getSession().createQuery(
                "select situacaoCadastro from Cota where numeroCota = :numeroCota");
        
        query.setParameter("numeroCota", numeroCota);
        
        return (SituacaoCadastro) query.uniqueResult();
    }
    
    @Override
    public Long obterIdPorNumeroCota(final Integer numeroCota) {
        
        final Query query = this.getSession().createQuery("select id from Cota where numeroCota = :numeroCota");
        query.setParameter("numeroCota", numeroCota);
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    public CotaVO obterDadosBasicosCota(final Integer numeroCota) {
        
        final StringBuilder hql = new StringBuilder("select ");
        hql.append(" c.numeroCota as numero, ")
        .append(" case when p.cnpj is not null then concat(p.razaoSocial, ' (', :siglaPJ, ')') else concat(p.nome, ' (', :siglaPF, ')') end as nome ")
        .append(" from Cota c ").append(" join c.pessoa p ").append(" where c.numeroCota = :numeroCota ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("siglaPJ", "PJ");
        query.setParameter("siglaPF", "PF");
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaVO.class));
        
        return (CotaVO) query.uniqueResult();
    }
    
    @Override
    public TipoDistribuicaoCota obterTipoDistribuicao(final Long idCota) {
        
        final Query query = this.getSession().createQuery("select tipoDistribuicaoCota from Cota where id = :idCota");
        
        query.setParameter("idCota", idCota);
        
        return (TipoDistribuicaoCota) query.uniqueResult();
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterConjuntoCota(FiltroNFeDTO filtro) {
	    final StringBuilder sql = new StringBuilder();
	    
	    sql.append("select cota FROM MovimentoEstoqueCota mec ")
		.append(" JOIN mec.tipoMovimento tipoMovimento ")
		.append(" JOIN mec.lancamento lancamento ")
		.append(" JOIN mec.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" LEFT JOIN cota.box box ")
		.append(" LEFT JOIN box.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.roteiros roteiro ")
		.append(" LEFT JOIN roteiro.rotas rota ")
		.append(" JOIN mec.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mec.data BETWEEN :dataDe AND :dataAte ")
		.append(" AND mec.movimentoEstoqueCotaEstorno is null ")
		.append(" AND mec.movimentoEstoqueCotaFuro is null ")
		.append(" AND mec.notaFiscalEmitida = false ");
	    
        if (filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()) {
            sql.append(" and ( fornecedor.id in (:idFornecedores) ) ");
        }
        
        if (filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
            sql.append(" and cota.numeroCota between :numeroCotaDe and :numeroCotaAte ");
        }
        
        if (filtro.getIntervaloBoxInicial() != null && filtro.getIntervalorCotaFinal() != null) {
            sql.append(" and box.CODIGO between :boxDe and :boxAte  ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            sql.append(" and roteiro.id=:idRoteiro  ");
        }
        
        if (filtro.getIdRota() != null) {
            sql.append(" and rota.id=:idRota  ");
        }
        
        if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
            sql.append(" and lancamento.dataLancamentoDistribuidor between :dataDe and :dataAte  ");
            // sql.append(" and cota.id not in (select id from CotaAusentem ca where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
        }
        
        sql.append(" GROUP BY cota ");
        
        Query query = this.getSession().createQuery(sql.toString());
        
        if (filtro.getListIdFornecedor() != null && !filtro.getListIdFornecedor().isEmpty()) {
            query.setParameterList("idFornecedores", filtro.getListIdFornecedor());
        }
        
        if (filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
            query.setParameter("numeroCotaDe", filtro.getIntervalorCotaInicial());
            query.setParameter("numeroCotaAte", filtro.getIntervalorCotaFinal());
        }
        
        if (filtro.getIntervaloBoxInicial() != null && filtro.getIntervaloBoxFinal() != null){
            query.setParameter("boxDe", filtro.getIntervaloBoxInicial());
            query.setParameter("boxAte", filtro.getIntervaloBoxFinal());
        }
        
        if (filtro.getIdRoteiro() != null) {
            
            query.setParameter("idRoteiro", filtro.getIdRoteiro());
        }
        
        if (filtro.getIdRota() != null) {
            
            query.setParameter("idRota", filtro.getIdRota());
        }
        
        if (filtro.getDataInicial() != null && filtro.getDataFinal() != null){
            query.setParameter("dataDe", filtro.getDataInicial());
            query.setParameter("dataAte", filtro.getDataFinal());
        }
        
        // query.setParameterList("statusNaoEmitiveis", new String[] {StatusLancamento.PLANEJADO.name(),
        //        StatusLancamento.FECHADO.name(), StatusLancamento.CONFIRMADO.name(),
        //        StatusLancamento.EM_BALANCEAMENTO.name(), StatusLancamento.CANCELADO.name()});
        
        return query.list();
	}
	
    @Override
    public String obterEmailCota(Integer numeroCota) {
        
        Query query = this.getSession().createQuery(
                "select c.pessoa.email from Cota c where c.numeroCota = :numeroCota ");
        
        query.setParameter("numeroCota", numeroCota);
        
        return (String) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<ParametroDistribuicaoEntregaCotaDTO> obterParametrosDistribuicaoEntregaCota(){
    	
    	final StringBuilder hql = new StringBuilder();
        
    	hql.append(" select distinct cota.ID as idCota , ")
    		.append(" 	cota.DESCRICAO_TIPO_ENTREGA as tipoEntrega, ")
		    .append("	cota.INICIO_PERIODO_CARENCIA as inicioCarencia,  ")
		    .append("	cota.FIM_PERIODO_CARENCIA as fimCarencia, ")
		    .append("	parametroCob.BASE_CALCULO as baseCalculo, ")
		    .append("	parametroCob.DIA_COBRANCA as diaCobranca, ")
		    .append("	parametroCob.DIA_SEMANA as diaSemana, ")
		    .append("	parametroCob.MODELIDADE_COBRANCA as modalidadeCobranca, ")
		    .append("	parametroCob.PERCENTUAL_FATURAMENTO percentualFaturamento, ")
		    .append("	parametroCob.PERIODICIDADE_COBRANCA periodicidade, ")
		    .append("	parametroCob.POR_ENTREGA  as porEntrega, ")
		    .append("	parametroCob.TAXA_FIXA as taxaFixa ")
		    .append(" from cota cota ")
		    .append("	join parametro_cobranca_distribuicao_cota parametroCob on parametroCob.COTA_ID = cota.ID ")
		    .append("   join pdv pdv on pdv.COTA_ID = cota.ID ")
		    .append("   join rota_pdv rotaPdv on rotaPDV.PDV_ID  = pdv.ID ")
		    .append("   join rota rota on rota.ID = rotaPdv.ROTA_ID ")
		    .append(" where cota.SITUACAO_CADASTRO IN (:statusCadastro) ")
		    .append(" and cota.TIPO_DISTRIBUICAO_COTA <> :cotaRetira ");
		
        final SQLQuery query = this.getSession().createSQLQuery(hql.toString());
        
        query.setParameter("cotaRetira", DescricaoTipoEntrega.COTA_RETIRA.name());
        query.setParameterList("statusCadastro", Arrays.asList(SituacaoCadastro.ATIVO.name(),SituacaoCadastro.SUSPENSO.name()));
         
        query.addScalar("idCota",StandardBasicTypes.LONG)
	        .addScalar("inicioCarencia",StandardBasicTypes.DATE)
	        .addScalar("fimCarencia",StandardBasicTypes.DATE)
	        .addScalar("baseCalculo", QueryUtil.obterTypeEnum(BaseCalculo.class))
	        .addScalar("diaCobranca",StandardBasicTypes.INTEGER)
	        .addScalar("diaSemana",QueryUtil.obterTypeEnum(DiaSemana.class))
	        .addScalar("modalidadeCobranca", QueryUtil.obterTypeEnum(ModalidadeCobranca.class))
	        .addScalar("percentualFaturamento",StandardBasicTypes.BIG_DECIMAL)
	        .addScalar("periodicidade",QueryUtil.obterTypeEnum(PeriodicidadeCobranca.class))
	        .addScalar("taxaFixa",StandardBasicTypes.BIG_DECIMAL)
	        .addScalar("porEntrega",StandardBasicTypes.BOOLEAN)
	        .addScalar("tipoEntrega",QueryUtil.obterTypeEnum(DescricaoTipoEntrega.class));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ParametroDistribuicaoEntregaCotaDTO.class));
        
        return query.list();
    }
    
    @Override
    public Fornecedor obterFornecedorPadrao(Long idCota) {
    	
    	Query query = getSession().createQuery(" select c.parametroCobranca.fornecedorPadrao from Cota c where c.id =:idCota ");
    	
    	query.setParameter("idCota", idCota);
    	query.setMaxResults(1);
    	
    	return (Fornecedor) query.uniqueResult();
    }
    
    @Override
    public Boolean validarNumeroCota(Integer numCota, TipoDistribuicaoCota tipoDistribuicaoCota){
    	
    	 final StringBuilder sql = new StringBuilder();
         
    	 sql.append("select count(*) ");
         sql.append("  from cota c ");
         sql.append(" where c.NUMERO_COTA in (:numCota)");
         sql.append("   and c.SITUACAO_CADASTRO in (upper(:situacaoCadastroAtivo), upper(:situacaoCadastroSuspenso)) ");
         sql.append("   and c.TIPO_DISTRIBUICAO_COTA = upper(:tipoDistribuicaoCota) ");
         
         final SQLQuery query = getSession().createSQLQuery(sql.toString());
         
         query.setParameter("numCota", numCota);
         query.setParameter("situacaoCadastroAtivo", SituacaoCadastro.ATIVO.toString());
         query.setParameter("situacaoCadastroSuspenso", SituacaoCadastro.SUSPENSO.toString());
         query.setParameter("tipoDistribuicaoCota", tipoDistribuicaoCota.toString());
    	
    	
    	return (BigIntegerUtil.isMaiorQueZero((BigInteger)query.uniqueResult()));
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemDTO<String, String>> obterCotasSemRoterizacao(List<Long> listaIdCotas) {
		StringBuilder hql = new StringBuilder();
			
		hql.append(" SELECT cota.numeroCota as key, ")
		   .append(" case pessoa.class " )
           .append(     " when 'F' then pessoa.nome " )
           .append(    " when 'J' then pessoa.razaoSocial else '' end as value," )
		   .append(" FROM ").append(" Cota cota  ").append(" JOIN cota.pessoa pessoa ").append(" LEFT JOIN cota.box box ")
	       .append(" WHERE cota.id in (:ids) ")
		   .append(" AND cota.box is null ");

		Query query = getSession().createQuery(hql.toString());
		
		query.setParameterList("ids", listaIdCotas);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		return query.list();
	}
	
	@Override
	public HistoricoSituacaoCota obterSituacaoCota(Long idCota){
		
		final Criteria criteria = super.getSession().createCriteria(HistoricoSituacaoCota.class);
        
		criteria.add(Restrictions.eq("cota.id", idCota));
		
        criteria.addOrder(Order.desc("dataFimValidade"));
        criteria.setMaxResults(1);
        
        return (HistoricoSituacaoCota) criteria.uniqueResult();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AbastecimentoBoxCotaDTO> obterCotasExpedicao(final Intervalo<Date> intervaloDataLancamento) {
        
		StringBuilder sql = new StringBuilder();
		
        sql.append(" select pe_.ID as idProdutoEdicao, "); 
        sql.append(" pe_.NUMERO_EDICAO as numeroEdicao, ");
        sql.append(" cota_.NUMERO_COTA as numeroCota, ");
        sql.append(" p_.NOME as nomeProduto, ");
        sql.append(" p_.CODIGO as codigoProduto, ");
        sql.append(" sum(COALESCE(ec_.REPARTE, mec.QTDE, 0)) as reparte" );
        sql.append(" FROM COTA cota_ ");
        sql.append(" LEFT  JOIN  BOX box1_ ON cota_.BOX_ID=box1_.ID ");
        sql.append(" INNER JOIN  ESTUDO_COTA ec_ ON cota_.ID=ec_.COTA_ID ");
        sql.append(" INNER JOIN  ESTUDO e_ ON ec_.ESTUDO_ID=e_.ID ");
        sql.append(" INNER JOIN  LANCAMENTO lancamento_ force index (ndx_status) ON e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID AND e_.ID=lancamento_.ESTUDO_ID ");
        sql.append(" LEFT  JOIN  MOVIMENTO_ESTOQUE_COTA mec ON mec.LANCAMENTO_ID=lancamento_.id AND mec.COTA_ID=cota_.ID ");
        sql.append(" LEFT  JOIN  TIPO_MOVIMENTO tipo_mov ON tipo_mov.ID=mec.TIPO_MOVIMENTO_ID ");
        sql.append(" INNER JOIN  PRODUTO_EDICAO pe_ ON e_.PRODUTO_EDICAO_ID=pe_.ID ");
        sql.append(" INNER JOIN  PRODUTO p_ ON pe_.PRODUTO_ID=p_.ID ");
        sql.append(" INNER JOIN  PRODUTO_FORNECEDOR pf_ ON p_.ID=pf_.PRODUTO_ID ");
        sql.append(" INNER JOIN  FORNECEDOR f_ ON pf_.fornecedores_ID=f_.ID ");
        sql.append(" INNER JOIN  PDV pdv_ ON cota_.ID=pdv_.COTA_ID ");
        sql.append(" LEFT  JOIN  ROTA_PDV rota_pdv_ ON pdv_.ID=rota_pdv_.PDV_ID ");
        sql.append(" LEFT  JOIN  ROTA rota_ ON rota_pdv_.rota_ID=rota_.ID ");
        sql.append(" LEFT  JOIN  ROTEIRO roteiro_ ON rota_.ROTEIRO_ID=roteiro_.ID ");
        sql.append(" INNER JOIN  PESSOA pessoa_cota_ ON cota_.PESSOA_ID=pessoa_cota_.ID ");
        sql.append(" LEFT  JOIN  NOTA_ENVIO_ITEM nei on nei.ESTUDO_COTA_ID = ec_ .ID  ");
        sql.append(" WHERE lancamento_.STATUS NOT IN (:statusNaoEmitiveis)"); 
        sql.append(" AND pdv_.ponto_principal = true "); 
        sql.append(" AND nei.ESTUDO_COTA_ID is null ");
        
        if (intervaloDataLancamento != null && intervaloDataLancamento.getDe() != null) {
            sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
            sql.append(" and cota_.ID not in (select COTA_ID from COTA_AUSENTE where COTA_ID = cota_.ID and DATA between :dataDe and :dataAte)  ");
        }
        
        sql.append(" group by cota_.ID");
        
        sql.append(" order by cota_.NUMERO_COTA ");
		
        Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataDe", intervaloDataLancamento.getDe());
		
		query.setParameter("dataAte", intervaloDataLancamento.getDe());
		
		query.setParameterList("statusNaoEmitiveis", new String[] {StatusLancamento.PLANEJADO.name(),
                StatusLancamento.FECHADO.name(), StatusLancamento.CONFIRMADO.name(),
                StatusLancamento.EM_BALANCEAMENTO.name(), StatusLancamento.CANCELADO.name() });
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AbastecimentoBoxCotaDTO.class));
		
		return query.list();
    }
}
