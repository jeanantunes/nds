package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.util.QueryUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class DividaRepositoryImpl extends AbstractRepositoryModel<Divida, Long> implements DividaRepository {
    
    public DividaRepositoryImpl() {
        super(Divida.class);
    }
    
    public void excluirDividaConsolidado(final Long idDivida) {
    	
    	Query query = this.getSession().createSQLQuery("DELETE FROM DIVIDA_CONSOLIDADO WHERE DIVIDA_ID = :idDivida");
		
		query.setParameter("idDivida", idDivida);
		
		query.executeUpdate();
		
		getSession().flush();
    	
    }
    
    @Override
    public Divida obterDividaParaAcumuloPorCota(final Long idCota) {
        
        final StringBuilder hql = new StringBuilder("select d ");
        hql.append(" from Divida d ").append(" join d.cota cota ").append(" where cota.id = :idCota ").append(
                " and d.status = :status ").append(
                        " and d.data = (select max(d2.data) from Divida d2 where d2.cota.id = :idCota) ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        query.setParameter("status", StatusDivida.EM_ABERTO);
        
        return (Divida) query.uniqueResult();
    }
    
    @Override
    public Long obterQunatidadeDividaGeradas(final Date dataMovimento) {
        
        final String hql = "select count (divida.id) from Divida divida where divida.data =:data";
        
        final Query query = super.getSession().createQuery(hql);
        
        query.setParameter("data", dataMovimento);
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    public Long obterQuantidadeRegistroDividasGeradas(final FiltroDividaGeradaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(getSqldividas(true, filtro, true));
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosConsultaDividas(filtro, true);
        
        setParameters(query, param);
        
        return (long) query.list().size();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<GeraDividaDTO> obterDividasGeradasSemBoleto(final FiltroDividaGeradaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(getSqldividas(false, filtro, false));
        
        hql.append(getOrdenacaoDivida(filtro));
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosConsultaDividas(filtro, false);
        
        setParameters(query, param);
        
        query.setResultTransformer(Transformers.aliasToBean(GeraDividaDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<GeraDividaDTO> obterDividasGeradas(final FiltroDividaGeradaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(getSqldividas(false, filtro, true));
        
        hql.append(getOrdenacaoDivida(filtro));
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        final Map<String, Object> param = getParametrosConsultaDividas(filtro, true);
        
        setParameters(query, param);
        
        query.setResultTransformer(Transformers.aliasToBean(GeraDividaDTO.class));
        
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
     * @return HashMap<String,Object>
     */
    private Map<String, Object> getParametrosConsultaDividas(final FiltroDividaGeradaDTO filtro, final boolean isBoleto) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        param.put("data", filtro.getDataMovimento());
        param.put("acumulaDivida", Boolean.FALSE);
        param.put("statusCobranca", StatusCobranca.NAO_PAGO);
        param.put("pendenteAcumulada", StatusDivida.PENDENTE_INADIMPLENCIA);
        param.put("tipoRoteiroEspecial", TipoRoteiro.ESPECIAL);
        
        if (!isBoleto) {
            param.put("tipoCobrancaBoleto", Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO));
        }
        
        if (filtro.getNumeroCota() != null) {
            param.put("numeroCota", filtro.getNumeroCota());
        }
        
        if (filtro.getTipoCobranca() != null) {
            param.put("tipoCobranca", filtro.getTipoCobranca());
        }
        
        if (filtro.getIdBox() != null) {
            param.put("box", filtro.getIdBox());
        }
        
        if (filtro.getIdRota() != null) {
            param.put("rota", filtro.getIdRota());
        }
        
        if (filtro.getIdRoteiro() != null) {
            param.put("roteiro", filtro.getIdRoteiro());
        }
        
        param.put("statusDivida", Arrays.asList(
                        StatusDivida.EM_ABERTO, 
                        StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, 
                        StatusDivida.PENDENTE_INADIMPLENCIA));
        
        return param;
    }
    
    /**
     * Retorna o hql da consulta de dividas.
     * 
     * @param count
     * @param filtro
     * @return String
     */
    private String getSqldividas(final boolean count, final FiltroDividaGeradaDTO filtro, final boolean isBoleto) {
        
        final StringBuilder hql = new StringBuilder();
        
        if (count) {
            hql.append(" SELECT count(divida.id)");
        } else {
            hql.append(" select cobranca.id as cobrancaId,")
               .append(" box.codigo || '-'|| box.nome as box,")
               .append(" rota.descricaoRota as rota,")
               .append(" roteiro.descricaoRoteiro as roteiro,")
               .append(" cota.id as idCota, ")
               .append(" cota.numeroCota as numeroCota, ")
               .append(" coalesce(pessoa.nome, pessoa.razaoSocial) as nomeCota,")
               .append(" cobranca.dataVencimento as dataVencimento,")
               .append(" cobranca.dataEmissao as dataEmissao,")
               .append(" cobranca.valor as valor,")
               .append(" cobranca.tipoCobranca as tipoCobranca,")
               .append(" coalesce(cobranca.vias, 0) as vias, ")
               .append(" cobranca.nossoNumero as nossoNumero ");
        }
        
        hql.append(" FROM ")
           .append(" Divida divida ")
           .append(" JOIN divida.cobranca cobranca ")
           .append(" JOIN divida.consolidados consolidado ")
           .append(" JOIN cobranca.cota cota ")
           .append(" left JOIN cota.box box ")
           .append(" left JOIN cota.pdvs pdv ")
           .append(" left JOIN cota.pessoa pessoa ")
           .append(" left JOIN cota.parametroCobranca parametroCobranca ")
           .append(" left JOIN pdv.rotas rotaPdv  ").append(" left JOIN rotaPdv.rota rota  ")
           .append(" left JOIN rota.roteiro roteiro ")
           .append(" WHERE ")
           .append(" divida.data =:data ")
           .append(" AND divida.acumulada =:acumulaDivida ")
           .append(" AND cobranca.statusCobranca=:statusCobranca ")
           .append(" AND pdv.caracteristicas.pontoPrincipal = true ")
           .append(" AND divida.status != :pendenteAcumulada ")
           .append(" AND roteiro.tipoRoteiro != :tipoRoteiroEspecial ")
           .append(" AND cobranca.dataPagamento is null ")
           .append(" AND divida.status in (:statusDivida) ");
        
        if (filtro.getNumeroCota() != null) {
            hql.append(" AND cota.numeroCota =:numeroCota ");
        }
        
        if (filtro.getTipoCobranca() != null) {
            
            hql.append(" AND cobranca.tipoCobranca =:tipoCobranca  ");
        }
        
        if (!isBoleto) {
            hql.append(" AND cobranca.tipoCobranca not in (:tipoCobrancaBoleto ) ");
        }
        
        if (filtro.getIdBox() != null) {
            hql.append(" AND box.id =:box ");
        }
        
        if (filtro.getIdRota() != null) {
            hql.append(" AND rota.id =:rota ");
        }
        
        if (filtro.getIdRoteiro() != null) {
            hql.append(" AND roteiro.id =:roteiro ");
        }
        
        hql.append(" GROUP BY cobranca.id ");
        
        return hql.toString();
    }
    
    /**
     * Retorna a string hql com a oredenação da consulta
     * 
     * @param filtro
     * @return String
     */
    private String getOrdenacaoDivida(final FiltroDividaGeradaDTO filtro) {
    	
        if (FiltroDividaGeradaDTO.ColunaOrdenacao.ROTEIRIZACAO.equals(filtro.getColunaOrdenacao())) {
            return " ORDER BY box.codigo, roteiro.ordem, rota.ordem, rotaPdv.ordem ";
        }
        
        if (filtro.getListaColunaOrdenacao() == null || filtro.getListaColunaOrdenacao().isEmpty()) {
            return "";
        }
        
        String orderByColumn = "";
        
        for (final ColunaOrdenacao ordenacao : filtro.getListaColunaOrdenacao()) {
            
            switch (ordenacao) {
            case BOX:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " box ";
                break;
            case DATA_EMISSAO:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " dataEmissao ";
                break;
            case DATA_VENCIMENTO:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " dataVencimento ";
                break;
            case NOME_COTA:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " nomeCota ";
                break;
            case NUMERO_COTA:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " numeroCota ";
                break;
            case ROTA:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " rota ";
                break;
            case ROTEIRO:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " roteiro ";
                break;
            case TIPO_COBRANCA:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " tipoCobranca ";
                break;
            case VALOR:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " valor ";
                break;
            case VIA:
                orderByColumn += orderByColumn.isEmpty() ? "" : ",";
                orderByColumn += " vias ";
                break;
            default:
                break;

            }
        }
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" ORDER BY ").append(orderByColumn);
        
        if (filtro.getPaginacao().getOrdenacao() != null) {
            
            hql.append(filtro.getPaginacao().getOrdenacao().toString());
        }
        
        return hql.toString();
    }
    
    /**
     * Obtem SQL da consulta de Boletos Antecipados Inadimplentes
     * 
     * @param filtro
     * @return StringBuilder
     */
    private StringBuilder getSqlInadimplenciaBoletosAntecipados(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT ");
        
        sql.append(" BOLETO_ANTECIPADO_.VALOR as dividaAcumulada, ");
        
        sql.append(" BOLETO_ANTECIPADO_.ID as idDivida, ");
        
        sql.append(" COTA_.ID AS idCota, ");
        
        sql.append(" BOLETO_ANTECIPADO_.ID AS idCobranca, ");
        
        sql.append(" null as nameTipoCobranca, ");
        
        sql.append(" BOLETO_ANTECIPADO_.ID as idNegociacao, ");
        
        sql.append(" 0 as comissaoSaldoDivida, ");
        
        sql.append(" COTA_.NUMERO_COTA AS numCota, ");
        
        sql.append(" COTA_.SITUACAO_CADASTRO as status, ");
        
        sql.append(" CASE WHEN PESSOA_.NOME IS NOT NULL ");
        sql.append(" THEN PESSOA_.NOME ");
        sql.append(" ELSE PESSOA_.RAZAO_SOCIAL END AS nome, ");
        
        sql.append(" (select ");
        sql.append(" sum(coalesce(movimentoe0_.PRECO_COM_DESCONTO, ");
        sql.append(" produtoedi4_.PRECO_VENDA, ");
        sql.append(" 0)*movimentoe0_.QTDE) as col_0_0_ ");
        sql.append(" from ");
        sql.append(" MOVIMENTO_ESTOQUE_COTA movimentoe0_ ");
        sql.append(" inner join ");
        sql.append(" COTA cota2_ ");
        sql.append(" on movimentoe0_.COTA_ID=cota2_.ID ");
        sql.append(" inner join ");
        sql.append(" TIPO_MOVIMENTO tipomovime3_ ");
        sql.append(" on movimentoe0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID ");
        sql.append(" inner join ");
        sql.append(" PRODUTO_EDICAO produtoedi4_ ");
        sql.append(" on movimentoe0_.PRODUTO_EDICAO_ID=produtoedi4_.ID ");
        sql.append(" RIGHT join ");
        sql.append(" MOVIMENTO_ESTOQUE_COTA movimentoe11_ ");
        sql.append(" on movimentoe0_.MOVIMENTO_ESTOQUE_COTA_FURO_ID=movimentoe11_.ID and COTA2_.ID = movimentoe11_.COTA_ID");
        sql.append(" where ");
        sql.append(" (tipomovime3_.GRUPO_MOVIMENTO_ESTOQUE in (:gruposMovimentoEstoque)) ");
        sql.append(" and (movimentoe0_.STATUS_ESTOQUE_FINANCEIRO is null ");
        sql.append(" or movimentoe0_.STATUS_ESTOQUE_FINANCEIRO=:statusEstoqueFinanceiro) ");
        sql.append(" and tipomovime3_.OPERACAO_ESTOQUE=:operacaoEstoqueEntrada ");
        sql.append(" and (movimentoe11_.ID is null) ");
        sql.append(" and cota2_.ID=COTA_.ID) AS consignado, ");
        
        sql.append(" BOLETO_ANTECIPADO_.DATA_VENCIMENTO as dataVencimento, ");
        
        sql.append(" BOLETO_ANTECIPADO_.DATA_PAGAMENTO as dataPagamento, ");
        
        sql.append(" BOLETO_ANTECIPADO_.STATUS as situacao ");
        
        sql.append(this.getSqlFromInadimplenciaBoletosAntecipados(filtro));
        
        return sql;
    }
    
    /**
     * Obtem clausula From da consulta de Boletos Antecipados Inadimplentes
     * 
     * @param filtro
     * @return StringBuilder
     */
    private StringBuilder getSqlFromInadimplenciaBoletosAntecipados(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" FROM BOLETO_ANTECIPADO BOLETO_ANTECIPADO_ ");
        
        sql.append(" JOIN CHAMADA_ENCALHE_COTA AS CHAMADA_ENCALHE_COTA_ ON (CHAMADA_ENCALHE_COTA_.ID = BOLETO_ANTECIPADO_.CHAMADA_ENCALHE_COTA_ID) ");
        
        sql.append(" JOIN COTA AS COTA_ ON(COTA_.ID=CHAMADA_ENCALHE_COTA_.COTA_ID) ");
        
        sql.append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ");
        
        sql.append(this.getSqlWhereInadimplenciaBoletosAntecipados(filtro));
        
        return sql;
    }
    
    /**
     * Obtem clausula Where da consulta de Boletos Antecipados Inadimplentes
     * 
     * @param filtro
     * @return StringBuilder
     */
    private StringBuilder getSqlWhereInadimplenciaBoletosAntecipados(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" WHERE BOLETO_ANTECIPADO_.BOLETO_ANTECIPADO_ID IS NULL ");
        
        if (filtro.getPeriodoDe() != null) {
            
            sql.append(" AND BOLETO_ANTECIPADO_.DATA_VENCIMENTO >= :periodoDe ");
        }
        
        if (filtro.getPeriodoAte() != null) {
            
            sql.append(" AND BOLETO_ANTECIPADO_.DATA_VENCIMENTO <= :periodoAte ");
        }
        
        if (filtro.getNumCota() != null) {
            
            sql.append(" AND COTA_.NUMERO_COTA = :numCota ");
        }
        
        if (filtro.getStatusCota() != null) {
            
            sql.append(" AND COTA_.SITUACAO_CADASTRO = :statusCota ");
        }
        
        sql.append(" AND BOLETO_ANTECIPADO_.STATUS IN (:statusDivida) ");
        
        return sql;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<StatusDividaDTO> obterInadimplenciasCota(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder(this.getSqlInadimplenciaClausulaSelect()
        
        		+ this.getSqlInadimplenciaClausulaFrom());
        final Map<String, Object> params = new HashMap<>();
        
        tratarFiltro(sql, params, filtro);
        
        sql.append(" UNION ALL ");
        
        sql.append(this.getSqlInadimplenciaBoletosAntecipados(filtro));
        
        params.put("gruposMovimentoEstoque", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name()));
        
        params.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
        
        params.put("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA.name());
        
        sql.append(obterOrderByInadimplenciasCota(filtro));
        
        final Query query = getSession().createSQLQuery(sql.toString()).addScalar("idDivida", StandardBasicTypes.LONG)
                .addScalar("idCota", StandardBasicTypes.LONG).addScalar("idCobranca", StandardBasicTypes.LONG).addScalar("nameTipoCobranca")
                .addScalar("idNegociacao", StandardBasicTypes.LONG).addScalar("comissaoSaldoDivida").addScalar(
                        "numCota").addScalar("nome").addScalar("status").addScalar("consignado").addScalar(
                                "dataVencimento").addScalar("dataPagamento").addScalar("situacao").addScalar("dividaAcumulada");
        
        setParameters(query, params);
        
        if (filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null
                && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
            
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        query.setResultTransformer(Transformers.aliasToBean(StatusDividaDTO.class));
        
        return query.list();
    }
    
    private void tratarFiltro(final StringBuilder sql, final Map<String, Object> params,
            final FiltroCotaInadimplenteDTO filtro) {
        
        boolean whereUtilizado = false;
        
        if (filtro.getPeriodoDe() != null) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append(" COBRANCA_.DT_VENCIMENTO >= :periodoDe ");
            params.put("periodoDe", filtro.getPeriodoDe());
        }
        
        if (filtro.getPeriodoAte() != null) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append(" COBRANCA_.DT_VENCIMENTO <= :periodoAte ");
            params.put("periodoAte", filtro.getPeriodoAte());
        }
        
        if (filtro.getNumCota() != null) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append(" COTA_.NUMERO_COTA = :numCota ");
            params.put("numCota", filtro.getNumCota());
        }
        
        if (filtro.getNomeCota() != null) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append("  (upper(PESSOA_.NOME) like :nomeCota or upper(PESSOA_.RAZAO_SOCIAL) like :nomeCota)");
            params.put("nomeCota", "%" + filtro.getNomeCota().toUpperCase() + "%");
        }
        
        if (filtro.getStatusCota() != null) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append(" COTA_.SITUACAO_CADASTRO = :statusCota ");
            params.put("statusCota", filtro.getStatusCota());
        }
        
        if (filtro.getStatusDivida() != null && !filtro.getStatusDivida().isEmpty()) {
            
            sql.append(whereUtilizado ? " AND " : " WHERE ");
            whereUtilizado = true;
            
            sql.append(" DIVIDA_.STATUS in (:statusDivida) ");
            params.put("statusDivida", this.parseListaStatusDivida(filtro.getStatusDivida()));
            
            boolean pesquisaVencidosNaoPagos = false;
            
            if (filtro.getStatusDivida().contains(StatusDivida.EM_ABERTO)
                    || filtro.getStatusDivida().contains(StatusDivida.PENDENTE_INADIMPLENCIA)
                    || filtro.getStatusDivida().contains(StatusDivida.POSTERGADA)) {
                
                sql.append(" AND COBRANCA_.DT_VENCIMENTO <= :dataAtual ");
                params.put("dataAtual", filtro.getDataOperacaoDistribuidor());
                pesquisaVencidosNaoPagos = true;
            }
            
            if (filtro.getStatusDivida().contains(StatusDivida.NEGOCIADA)
                    || filtro.getStatusDivida().contains(StatusDivida.QUITADA)) {
                
                if (pesquisaVencidosNaoPagos) {
                    
                    sql.append(" AND (DIVIDA_.STATUS = :statusNegociado OR COBRANCA_.DT_PAGAMENTO IS NULL OR ");
                    sql.append(" COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
                    sql.append("  ) ");
                    
                    params.put("statusNegociado", StatusDivida.NEGOCIADA.name());
                    
                } else {
                    sql.append(" AND COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
                }
                
            }
            
        }
        
    }
    
    private List<String> parseListaStatusDivida(final List<StatusDivida> statusDividas) {
        
        final List<String> parsedStatus = new ArrayList<>();
        
        for (final StatusDivida statusDivida : statusDividas) {
            
            if (statusDivida != null) {
                
                parsedStatus.add(statusDivida.name());
            }
        }
        
        return parsedStatus;
    }
    
    private String obterOrderByInadimplenciasCota(final FiltroCotaInadimplenteDTO filtro) {
        
        if (filtro.getColunaOrdenacao() == null || filtro.getPaginacao() == null) {
            return "";
        }
        
        final String sortColumn = filtro.getColunaOrdenacao().toString();
        final String sortOrder = filtro.getPaginacao().getSortOrder();
        
        String sql = "";
        
        if (sortColumn == null || sortOrder == null) {
            return sql;
        }
        
        sql += " ORDER BY ";
        
        if ("numCota".equalsIgnoreCase(sortColumn)) {
            sql += "numCota";
        } else if ("nome".equalsIgnoreCase(sortColumn)) {
            sql += "nome";
        } else if ("status".equalsIgnoreCase(sortColumn)) {
            sql += "status";
        } else if ("consignado".equalsIgnoreCase(sortColumn)) {
            sql += "consignado";
        } else if ("dataVencimento".equalsIgnoreCase(sortColumn)) {
            sql += "dataVencimento";
        } else if ("dataPagamento".equalsIgnoreCase(sortColumn)) {
            sql += "dataPagamento";
        } else if ("situacao".equalsIgnoreCase(sortColumn)) {
            sql += "situacao";
        } else if ("dividaAcumulada".equalsIgnoreCase(sortColumn)) {
            sql += "dividaAcumulada";
        } else if ("diasAtraso".equalsIgnoreCase(sortColumn)) {
            sql += "diasAtraso";
        } else {
            return "";
        }
        
        sql += "asc".equalsIgnoreCase(sortOrder) ? " ASC " : " DESC ";
        
        return sql;
    }
    
    @Override
    public Long obterTotalInadimplenciasCota(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder(" SELECT ");
        
        sql.append(" ( ");
        
        sql.append(this.getSqlCountInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());
        
        final Map<String, Object> params = new HashMap<String, Object>();
        
        tratarFiltro(sql, params, filtro);
        
        sql.append(" ) ");
        
        sql.append(" + ");
        
        sql.append(" ( ");
        
        sql.append(this.getSqlCountInadimplenciaBoletoAntecipadoClausulaSelect()
                + this.getSqlFromInadimplenciaBoletosAntecipados(filtro));
        
        sql.append(" ) FROM DUAL ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        setParameters(query, params);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @Override
    public Long obterTotalCotasInadimplencias(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder(" SELECT COUNT(DISTINCT CONTAGEM.COTA_ID) FROM ");
        
        sql.append(" ( ");
        
        sql.append(this.getSqlCountCotasInadimplentesClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());
        
        final Map<String, Object> params = new HashMap<String, Object>();
        
        tratarFiltro(sql, params, filtro);
        
        sql.append(" UNION ALL ");
        
        sql.append(this.getSqlCountCotasInadimplentesBoletoAntecipadoClausulaSelect()
                + this.getSqlFromInadimplenciaBoletosAntecipados(filtro));
        
        sql.append(" ) AS CONTAGEM ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        setParameters(query, params);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @Override
    public Double obterSomaDividas(final FiltroCotaInadimplenteDTO filtro) {
        
        final StringBuilder sql = new StringBuilder(" SELECT SUM(SOMATORIO.VALOR) FROM ");
        
        sql.append(" ( ");
        
        sql.append(this.getSqlSumValorInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());
        
        final Map<String, Object> params = new HashMap<String, Object>();
        
        tratarFiltro(sql, params, filtro);
        
        sql.append(" UNION ALL ");
        
        sql.append(this.getSqlSumValorInadimplenciaBoletoAntecipadoClausulaSelect()
                + this.getSqlFromInadimplenciaBoletosAntecipados(filtro));
        
        sql.append(" ) AS SOMATORIO ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        setParameters(query, params);
        final BigDecimal somaDividas = (BigDecimal) query.uniqueResult();
        if (somaDividas == null) {
            return 0.0;
        } else {
            return somaDividas.doubleValue();
        }
    }
    
    @Override
    public Divida obterDividaPorIdConsolidadoNaoNegociado(final Long idConsolidado) {
        
        final StringBuilder hql = new StringBuilder("select d ");
        hql.append(" from Divida d ").append(" join d.consolidados cons ").append(" join d.cobranca cob ").append(
                " where cons.id = :idConsolidado ").append(" and cob.id not in ( ").append("     select c.id ").append(
                        "     from Negociacao neg").append("     join neg.cobrancasOriginarias c) ").append(
                                " and d.origemNegociacao = true ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idConsolidado", idConsolidado);
        
        return (Divida) query.uniqueResult();
    }
    
    @Override
    public Divida obterDividaPorIdConsolidado(final Long idConsolidado) {
        
        final StringBuilder hql = new StringBuilder("select d ");
        hql.append(" from Divida d ").append(" join d.consolidados cons ").append(" where cons.id = :idConsolidado ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idConsolidado", idConsolidado);
        
        return (Divida) query.uniqueResult();
    }
    
    @Override
    public BigDecimal obterTotalDividasAbertoCota(final Long idCota) {
        
        final String hql = "select sum(COALESCE(divida.valor,0)) from Divida divida where divida.cota.id = :idCota and status = :status ";
        final Query query = super.getSession().createQuery(hql);
        query.setParameter("idCota", idCota);
        query.setParameter("status", StatusDivida.EM_ABERTO);
        
        return (BigDecimal) query.uniqueResult();
    }
    
    /**
     * Obtem total de dividas vencidas em aberto da cota
     * 
     * @param idCota
     * @param data
     * @return BigDecimal
     */
    @Override
    public BigDecimal obterTotalDividasVencidasCota(final Long idCota, Date data) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select sum(COALESCE(divida.valor,0)) ");
        
        hql.append(" from Divida divida ");
        
        hql.append(" inner join divida.cobranca cob ");
        
        hql.append(" inner join divida.cota cota ");
        
        hql.append(" where cob.dataVencimento <= :data ");
        
        hql.append(" and cota.id = :idCota ");
        
        hql.append(" and divida.status = :status ");
        
        final Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("idCota", idCota);
        
        query.setParameter("status", StatusDivida.EM_ABERTO);
        
        query.setParameter("data", data);
        
        return (BigDecimal) query.uniqueResult();
    }
    
    @Override
    public BigDecimal obterValorDividasDataOperacao(final boolean dividaVencendo, final boolean dividaAcumulada) {
        
        final StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
        hql.append(" from Divida divida, Distribuidor dist ").append(" where ");
        
        if (dividaVencendo) {
            
            hql.append(" divida.cobranca.dataVencimento = dist.dataOperacao ");
        } else {
            
            hql.append(" divida.cobranca.dataVencimento < dist.dataOperacao ");
        }
        
        if (dividaAcumulada) {
            
            hql.append(" and divida.acumulada = :acumulada ");
        }
        
        hql.append(" and divida.status != :quitada ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (dividaAcumulada) {
            
            query.setParameter("acumulada", true);
        }
        
        query.setParameter("quitada", StatusDivida.QUITADA);
        
        return (BigDecimal) query.uniqueResult();
    }
    
    @Override
    public BigDecimal obterValoresDividasGeradasDataOperacao(final boolean postergada) {
        
        final StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
        hql.append(" from Divida divida, Distribuidor dist ").append(" where divida.data = dist.dataOperacao ");
        
        if (postergada) {
            
            hql.append(" and divida.status = :indPostergada ");
        } else {
            
            hql.append(" and divida.status != :indPostergada ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("indPostergada", StatusDivida.POSTERGADA);
        
        return (BigDecimal) query.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(final Date data) {
        
    	Objects.requireNonNull(data, "Data para sumarização das dívidas a receber EM não pode ser nula!");
        
        StringBuilder sql = new StringBuilder();
        
        sql.append(" select cobranca0_.TIPO_COBRANCA as tipoCobranca, ");
        sql.append(" sum(cobranca0_.VALOR)  + ");
        sql.append("	(select COALESCE(sum(cobranca_.VALOR),0) "); 
        sql.append("		from COBRANCA cobranca_ join DIVIDA divida_ on divida_.ID = cobranca_.DIVIDA_ID "); 
        sql.append("        join baixa_cobranca baixaCobranca on baixaCobranca.COBRANCA_ID = cobranca_.ID ");
        sql.append("		where cobranca_.DT_VENCIMENTO < :dataFechamento ");
        sql.append("		and baixaCobranca.DATA_BAIXA = :dataFechamento ");
        sql.append("		and cobranca_.STATUS_COBRANCA=:statusPago ");
        sql.append("		and cobranca_.TIPO_COBRANCA =  cobranca0_.TIPO_COBRANCA ");
        sql.append("		and divida_.STATUS not in (:pendenteInadimplente))  as total, ");	   	
        sql.append("	  (select COALESCE(sum(cobranca_.VALOR), 0) ");
        sql.append("		from COBRANCA cobranca_ join DIVIDA divida_ on divida_.ID = cobranca_.DIVIDA_ID "); 
        sql.append("        join baixa_cobranca baixaCobranca on baixaCobranca.COBRANCA_ID = cobranca_.ID ");
        sql.append("		where cobranca_.DT_VENCIMENTO < :dataFechamento ");
        sql.append("		and baixaCobranca.DATA_BAIXA = :dataFechamento ");
        sql.append("		and cobranca_.STATUS_COBRANCA = :statusPago ");
        sql.append("		and cobranca_.TIPO_COBRANCA = cobranca0_.TIPO_COBRANCA ");
        sql.append("		and divida_.STATUS not in (:pendenteInadimplente) )  as valorPago, ");
		sql.append("  sum(cobranca0_.VALOR)  as inadimplencia ");
		sql.append("  from ");
		sql.append("        COBRANCA cobranca0_ join DIVIDA divida1_ on cobranca0_.DIVIDA_ID=divida1_.ID "); 
		sql.append("  where ");
		sql.append("       cobranca0_.DT_VENCIMENTO < :dataFechamento ");
		sql.append("       and cobranca0_.STATUS_COBRANCA = :statusNaoPago ");
		sql.append("       and divida1_.STATUS not in (:pendenteInadimplente) ");
		sql.append("  group by cobranca0_.TIPO_COBRANCA ");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        
        query.addScalar("total",StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("valorPago",StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("inadimplencia",StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("tipoCobranca",QueryUtil.obterTypeEnum(TipoCobranca.class));
        
        query.setParameter("dataFechamento", data);
        
        query.setParameter("statusNaoPago", StatusCobranca.NAO_PAGO.name());
        
        query.setParameter("statusPago", StatusCobranca.PAGO.name());
        
        query.setParameterList("pendenteInadimplente", Arrays.asList(StatusDivida.PENDENTE_INADIMPLENCIA.name(), StatusDivida.POSTERGADA.name()));
        
        query.setResultTransformer(Transformers.aliasToBean(SumarizacaoDividasDTO.class));
        
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(final Date data) {
        
    	Objects.requireNonNull(data, "Data para sumarização das dívidas à vencer APÓS não pode ser nula!");
        
    	StringBuilder sql = new StringBuilder();
    	
		sql.append(" select cobranca0_.TIPO_COBRANCA as tipoCobranca, ");
		sql.append(" sum(cobranca0_.VALOR)  + ");
		sql.append("	(select COALESCE(sum(cobranca_.VALOR),0) "); 
		sql.append("		from COBRANCA cobranca_ join DIVIDA divida_ on divida_.ID = cobranca_.DIVIDA_ID "); 
		sql.append("        join baixa_cobranca baixaCobranca on baixaCobranca.COBRANCA_ID = cobranca_.ID ");
		sql.append("		where cobranca_.DT_VENCIMENTO >= :dataFechamento ");
		sql.append("		and baixaCobranca.DATA_BAIXA = :dataFechamento ");
		sql.append("		and cobranca_.STATUS_COBRANCA=:statusPago ");
		sql.append("		and cobranca_.TIPO_COBRANCA =  cobranca0_.TIPO_COBRANCA ");
		sql.append("		and divida_.STATUS<> :pendenteInadimplente)  as total, ");	   	
		sql.append("	  (select COALESCE(sum(cobranca_.VALOR),0) ");
		sql.append("		from COBRANCA cobranca_ join DIVIDA divida_ on divida_.ID = cobranca_.DIVIDA_ID "); 
		sql.append("        join baixa_cobranca baixaCobranca on baixaCobranca.COBRANCA_ID = cobranca_.ID ");
		sql.append("		where cobranca_.DT_VENCIMENTO >= :dataFechamento ");
		sql.append("		and baixaCobranca.DATA_BAIXA = :dataFechamento ");
		sql.append("		and cobranca_.STATUS_COBRANCA = :statusPago ");
		sql.append("		and cobranca_.TIPO_COBRANCA =  cobranca0_.TIPO_COBRANCA ");
		sql.append("		and divida_.STATUS<> :pendenteInadimplente )  as valorPago, ");
 		sql.append("  sum(cobranca0_.VALOR)  as inadimplencia ");
 		sql.append("  from ");
 		sql.append("        COBRANCA cobranca0_ join DIVIDA divida1_ on cobranca0_.DIVIDA_ID=divida1_.ID "); 
 		sql.append("  where ");
 		sql.append("       cobranca0_.DT_VENCIMENTO>=:dataFechamento ");
 		sql.append("       and cobranca0_.STATUS_COBRANCA=:statusNaoPago ");
 		sql.append("       and divida1_.STATUS<> :pendenteInadimplente ");
	 	sql.append("  group by cobranca0_.TIPO_COBRANCA ");
	    	 
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		 
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorPago", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("inadimplencia", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipoCobranca",QueryUtil.obterTypeEnum(TipoCobranca.class));

		query.setParameter("dataFechamento", data);

		query.setParameter("statusNaoPago", StatusCobranca.NAO_PAGO.name());

		query.setParameter("statusPago", StatusCobranca.PAGO.name());

		query.setParameter("pendenteInadimplente",StatusDivida.PENDENTE_INADIMPLENCIA.name());

		query.setResultTransformer(Transformers.aliasToBean(SumarizacaoDividasDTO.class));

		return query.list();
    }
     
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Cobranca> obterDividasReceberEm(final Date data, final PaginacaoVO paginacao) {
        Objects.requireNonNull(data, "Data para consulta das dívidas à receber EM não pode ser nula!");
        final Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, paginacao, false);
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Cobranca> obterDividasVencerApos(final Date data, final PaginacaoVO paginacao) {
        Objects.requireNonNull(data, "Data para consulta das dívidas à vencer APÓS não pode ser nula!");
        final Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, paginacao, false);
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long contarDividasReceberEm(final Date data) {
        Objects.requireNonNull(data, "Data para contagem das dívidas à receber EM não pode ser nula!");
        final Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, null, true);
        return (long) query.uniqueResult();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long contarDividasVencerApos(final Date data) {
        Objects.requireNonNull(data, "Data para contagem das dívidas à vencer APÓS não pode ser nula!");
        final Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, null, true);
        return (long) query.uniqueResult();
    }
    
    /**
     * Cria a query para consulta / contagem de dívidas à receber / à vencer
     * 
     * @param data data base para consulta ou contagem de dívidas
     * @param tipoDivida tipo da dívida para recuperação, dívidas a receber ou
     *            dívidas a vencer
     * @param paginacao no caso de consulta de dívidas, contém os parâmetros de
     *            paginação de dívidas, pode ser {@code null}
     * @param count flag indicando se a query é consulta das dívidas ou contagem
     *            de dívidas
     * @return query criada com os parâmetros recebidos
     */
    private Query queryDividas(final Date data, final TipoDivida tipoDivida, final PaginacaoVO paginacao,
            final boolean count) {
        
        final StringBuilder hql = new StringBuilder("select ");
        hql.append(count ? "count(cobranca.id) " : "cobranca ");
        hql.append("from Cobranca cobranca ");
        hql.append(" join cobranca.cota as cota ");
        
        if (TipoDivida.DIVIDA_A_VENCER.equals(tipoDivida)) {
            
            hql.append(" where cobranca.dataVencimento >= :data and cobranca.statusCobranca = :statusNaoPago   ");
            
        } else {
            
            hql.append(" where cobranca.dataVencimento < :data  and cobranca.statusCobranca = :statusNaoPago ")
            .append("	and cobranca.divida.status not in (:pendenteInadimplente) ");
        }
        
        if (!count) {
            hql.append("order by cota.numeroCota ");
        }
        
        final Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", data);
        query.setParameter("statusNaoPago", StatusCobranca.NAO_PAGO);
        
        if (!TipoDivida.DIVIDA_A_VENCER.equals(tipoDivida)) {
            query.setParameterList("pendenteInadimplente", Arrays.asList(StatusDivida.PENDENTE_INADIMPLENCIA, StatusDivida.POSTERGADA));
        }
        
        if (!count && paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
        return query;
    }
    
    private String getSqlInadimplenciaClausulaFrom() {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" FROM DIVIDA AS DIVIDA_ ");
        sql.append(" JOIN COTA AS COTA_ ON(COTA_.ID=DIVIDA_.COTA_ID) ");
        sql.append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ");
        sql.append(" JOIN COBRANCA AS COBRANCA_ ON(COBRANCA_.DIVIDA_ID=DIVIDA_.ID) ");
        sql.append(" LEFT JOIN NEGOCIACAO_COBRANCA_ORIGINARIA AS NEGOCIACAO_COBRANCA_ORIGINARIA_ ");
        sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.COBRANCA_ID = COBRANCA_.ID) ");
        sql.append(" LEFT JOIN NEGOCIACAO AS NEGOCIACAO_ ");
        sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.NEGOCIACAO_ID=NEGOCIACAO_.ID) ");
        sql.append(" LEFT JOIN FORMA_COBRANCA AS FORMA_COBRANCA_ ");
        sql.append(" ON (FORMA_COBRANCA_.ID = NEGOCIACAO_.FORMA_COBRANCA_ID) ");
        sql.append(" JOIN POLITICA_COBRANCA POLITICA_COBRANCA_ ON (POLITICA_COBRANCA_.PRINCIPAL IS TRUE) ");
        
        
        return sql.toString();
    }
    
    private String getSqlInadimplenciaClausulaSelect() {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT ");
        sql.append(" CASE WHEN DIVIDA_.DIVIDA_RAIZ_ID is null ");
        sql.append(" THEN DIVIDA_.VALOR ");
        sql.append(" ELSE  ");
        sql.append("  (SELECT ");
        sql.append(" SUM(ACUMULADAS_.VALOR) ");
        sql.append(" FROM ");
        sql.append(" DIVIDA ACUMULADAS_ ");
        sql.append(" where ");
        sql.append(" ACUMULADAS_.id = DIVIDA_.DIVIDA_RAIZ_ID) ");
        sql.append(" END as dividaAcumulada, ");
        
        sql.append(" DIVIDA_.ID as idDivida, ");
        
        sql.append(" COTA_.ID AS idCota, ");
        
        sql.append(" COBRANCA_.ID AS idCobranca, ");
        
        sql.append(" FORMA_COBRANCA_.TIPO_COBRANCA nameTipoCobranca, ");
        
        sql.append(" NEGOCIACAO_.ID as idNegociacao, ");
        
        sql.append(" NEGOCIACAO_.COMISSAO_PARA_SALDO_DIVIDA as comissaoSaldoDivida, ");
        
        sql.append(" COTA_.NUMERO_COTA AS numCota, ");
        
        sql.append(" COTA_.SITUACAO_CADASTRO as status, ");
        
        sql.append(" CASE WHEN PESSOA_.NOME IS NOT NULL ");
        sql.append(" THEN PESSOA_.NOME ");
        sql.append(" ELSE PESSOA_.RAZAO_SOCIAL END AS nome, ");
        
        sql.append(" (select ");
        sql.append(" sum(coalesce(movimentoe0_.PRECO_COM_DESCONTO, ");
        sql.append(" produtoedi4_.PRECO_VENDA, ");
        sql.append(" 0)*movimentoe0_.QTDE) as col_0_0_ ");
        sql.append(" from ");
        sql.append(" MOVIMENTO_ESTOQUE_COTA movimentoe0_ ");
        sql.append(" inner join ");
        sql.append(" COTA cota2_ ");
        sql.append(" on movimentoe0_.COTA_ID=cota2_.ID ");
        sql.append(" inner join ");
        sql.append(" TIPO_MOVIMENTO tipomovime3_ ");
        sql.append(" on movimentoe0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID ");
        sql.append(" inner join ");
        sql.append(" PRODUTO_EDICAO produtoedi4_ ");
        sql.append(" on movimentoe0_.PRODUTO_EDICAO_ID=produtoedi4_.ID ");
        sql.append(" RIGHT join ");
        sql.append(" MOVIMENTO_ESTOQUE_COTA movimentoe11_ ");
        sql.append(" on movimentoe0_.MOVIMENTO_ESTOQUE_COTA_FURO_ID=movimentoe11_.ID and COTA2_.ID = movimentoe11_.COTA_ID");
        sql.append(" where ");
        sql.append(" (tipomovime3_.GRUPO_MOVIMENTO_ESTOQUE in (:gruposMovimentoEstoque)) ");
        sql.append(" and (movimentoe0_.STATUS_ESTOQUE_FINANCEIRO is null ");
        sql.append(" or movimentoe0_.STATUS_ESTOQUE_FINANCEIRO=:statusEstoqueFinanceiro) ");
        sql.append(" and tipomovime3_.OPERACAO_ESTOQUE=:operacaoEstoqueEntrada ");
        sql.append(" and (movimentoe11_.ID is null) ");
        sql.append(" and cota2_.ID=COTA_.ID) AS consignado, ");
        
        sql.append(" COBRANCA_.DT_VENCIMENTO as dataVencimento, ");
        
        sql.append(" COBRANCA_.DT_PAGAMENTO as dataPagamento, ");
        
        sql.append(" DIVIDA_.STATUS as situacao ");
        
        return sql.toString();
    }
    
    private String getSqlCountInadimplenciaClausulaSelect() {
        
        return " SELECT COUNT(*) ";
    }
    
    private String getSqlCountInadimplenciaBoletoAntecipadoClausulaSelect() {
        
        return " SELECT COUNT(*) ";
    }
    
    private String getSqlCountCotasInadimplentesClausulaSelect() {
        
        return " SELECT COTA_.ID AS COTA_ID ";
    }
    
    private String getSqlCountCotasInadimplentesBoletoAntecipadoClausulaSelect() {
        
        return " SELECT COTA_.ID AS COTA_ID ";
    }
    
    private String getSqlSumValorInadimplenciaClausulaSelect() {
        
        return " SELECT DIVIDA_.VALOR AS VALOR ";
    }
    
    private String getSqlSumValorInadimplenciaBoletoAntecipadoClausulaSelect() {
        
        return " SELECT BOLETO_ANTECIPADO_.VALOR AS VALOR ";
    }
    
    @Override
    public Long verificarEnvioDeEmail(final GeraDividaDTO dividaGerada) {
        
        final StringBuilder hql = new StringBuilder();
        hql.append("select count(forma_cobranca.recebeCobrancaEmail) " + "from Cobranca as cobranca join "
                + "cobranca.cota as cota join " + "cota.parametroCobranca as parametro_cobranca_cota join "
                + "parametro_cobranca_cota.formasCobrancaCota as forma_cobranca " + "where "
                + "forma_cobranca.recebeCobrancaEmail = true " + "and " + "forma_cobranca.ativa= true " + "and "
                + "cota.numeroCota = :numeroCota " + "and " + "cobranca.id = :cobrancaId");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("numeroCota", dividaGerada.getNumeroCota());
        query.setParameter("cobrancaId", dividaGerada.getCobrancaId());
        
        return (Long) query.uniqueResult();
    }
    
    
    @Override
    public BigInteger obterIdDividaPendente(final ConsolidadoFinanceiroCota consolidado, Date dataOperaçao) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select divida1_.id                                                                                                     ");
        hql.append(" from ACUMULO_DIVIDA acumulodiv0_                                                                                       ");
        hql.append(" inner join DIVIDA divida1_ on acumulodiv0_.DIVIDA_ID=divida1_.ID                                                       ");
        hql.append(" inner join COBRANCA cobranca2_ on divida1_.ID=cobranca2_.DIVIDA_ID                                                     ");
        hql.append(" inner join COTA cota3_ on cobranca2_.COTA_ID=cota3_.ID                                                                 ");
        hql.append(" inner join DIVIDA_CONSOLIDADO consolidad4_  on divida1_.ID=consolidad4_.DIVIDA_ID                                      ");
        hql.append(" inner join CONSOLIDADO_FINANCEIRO_COTA consolidad5_ on consolidad4_.CONSOLIDADO_ID=consolidad5_.ID                     ");
        hql.append(" inner join CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos6_ on consolidad5_.ID=movimentos6_.CONSOLIDADO_FINANCEIRO_ID     ");
        hql.append(" inner join MOVIMENTO_FINANCEIRO_COTA movimentof7_  on movimentos6_.MVTO_FINANCEIRO_COTA_ID=movimentof7_.ID             ");
        hql.append(" inner join DIVIDA_CONSOLIDADO dc on dc.CONSOLIDADO_ID = consolidad5_.ID                                                ");
        hql.append(" where                                                                                                                  ");
        hql.append(" divida1_.STATUS = 'PENDENTE_INADIMPLENCIA'                                                                             ");
        hql.append(" and cota3_.ID = :cotaId                                                                                                ");
        hql.append(" and consolidad5_.DT_CONSOLIDADO < :data                                                                        ");
        hql.append(" group by divida1_.id                                                                                                   ");
        hql.append(" order by divida1_.DATA desc                                                                                            ");
        
        final Query query = this.getSession().createSQLQuery(hql.toString());
        
        query.setParameter("cotaId", consolidado.getCota().getId());
        query.setParameter("data", dataOperaçao);
        query.setMaxResults(1);
        
        return (BigInteger) query.uniqueResult();
    }
}