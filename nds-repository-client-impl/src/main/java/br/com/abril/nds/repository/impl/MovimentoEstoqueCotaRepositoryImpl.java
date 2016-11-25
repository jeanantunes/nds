package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.ContagemDevolucaoAgregationValuesDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.CotaReparteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.MovimentosEstoqueEncalheDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacaoDetalhes;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacaoEntregador;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.TipoConsulta;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {
    
    @Autowired
    private DataSource dataSource;
    
    public MovimentoEstoqueCotaRepositoryImpl() {
        super(MovimentoEstoqueCota.class);
    }
    
    /**
     * FROM: Consignado da cota com chamada de encalhe ou produto conta firme
     * @param paramIdCota
     * @return String
     */
    @Override
    public String getFromConsignadoCotaAVista(final String paramIdCota){
        
        final StringBuilder hql = new StringBuilder("")
        
        .append("  from MovimentoEstoqueCota mec ")

        .append("  join mec.cota c1 ")
        
        .append("  join mec.produtoEdicao pe ")
        
        .append("  join mec.lancamento l ")
        
        .append("  join l.produtoEdicao peLanc ")
         
        .append("  join l.chamadaEncalhe ce ")
        
        .append("  join ce.chamadaEncalheCotas cec ")
        
        .append("  join cec.cota c1Cec ")
        
        .append("  join pe.produto produto ")
        
        .append("  join mec.tipoMovimento tipoMovimento ")
        
        .append("  left join mec.movimentoEstoqueCotaFuro mecF ")
        
        .append("  where ((mec.statusEstoqueFinanceiro is null) or (mec.statusEstoqueFinanceiro = :statusEstoqueFinanceiro)) ")
        
        .append("  and tipoMovimento.grupoMovimentoEstoque in (:gruposMovimentoReparte) ")
        
        .append("  and mec.status = :statusAprovacao ")
        
        .append("  and c1.id = ").append(paramIdCota)
        
        .append("  and c1.id = c1Cec.id ")
        
        .append("  and pe.id = peLanc.id ")
        
        .append("  and mec.data <= :data")
        
        .append("  and mecF is null ")
        
        .append("  and ( ")
        
        .append("            ((produto.formaComercializacao = :formaComercializacaoProduto) OR ")
        
        .append("             ( l.id in (")
        
        .append("                           select l.id ")
        
        .append("                           from Lancamento lcto ")
        
        .append("                           join lcto.produtoEdicao proed ")
        
        .append("                           join lcto.chamadaEncalhe c ")
        
        .append("                           join c.chamadaEncalheCotas cc ")
        
        .append("                           join cc.cota cota ")
          
        .append("                           left join cc.conferenciasEncalhe conferencia ")
        
        .append("                           left join conferencia.controleConferenciaEncalheCota as controleConferencia with controleConferencia.status = :statusOperacaoConferencia ")
        
        .append("                           where c.dataRecolhimento = :data ")
        
        .append("                           and cota.id = c1.id ")
        
        .append("                           and proed.id = pe.id ")
        
        .append("                           and controleConferencia is null ")
        
        .append("                           group by cota.numeroCota, proed.id ")
        
        .append("                       ) ")
        
        .append("             ) ")
        
        .append("            ) ")
        
        .append("      ) ");

        return hql.toString();
    }
    
	/**
     * FROM: À Vista da cota sem chamada de encalhe e produtos diferentes de
     * conta firme
     * 
     * @param paramIdCota
     * @return String
     */
    @Override
    public String getFromAVistaCotaAVista(final String paramIdCota){
        
        final StringBuilder hql = new StringBuilder("")
        
        .append("  from MovimentoEstoqueCota mec ")
        
        .append("    join mec.lancamento l ")
        
        .append("  join mec.produtoEdicao pe ")
        
        .append("  join pe.produto produto ")
        
        .append("  join mec.cota c1 ")
        
        .append("  join mec.tipoMovimento tipoMovimento ")
        
        .append("  left join mec.movimentoEstoqueCotaFuro mecF ")
        
        .append("  where ((mec.statusEstoqueFinanceiro is null) or (mec.statusEstoqueFinanceiro = :statusEstoqueFinanceiro )) ")
        
        .append("  and tipoMovimento.grupoMovimentoEstoque in (:gruposMovimentoReparte) ")
        
        .append("  and mec.status = :statusAprovacao ")
        
        .append("  and c1.id = ").append(paramIdCota)
        
        .append("  and l.dataLancamentoDistribuidor <= :data")
        
        .append("  and mecF is null ");
        
        return hql.toString();
    }
    
    /**
     * FROM: Movimentos de Estorno da cota
     * @param paramIdCota
     * @return String
     */
    @Override
    public String getFromEstornoCotaAVista(final String paramIdCota){
        
        final StringBuilder hql = new StringBuilder("")
        
        .append("  from MovimentoEstoqueCota mec ")
        
        .append("  join mec.produtoEdicao pe ")
        
        .append("  join mec.cota c2 ")
        
        .append("  join mec.tipoMovimento tipoMovimento ")
        
        .append("  where ((mec.statusEstoqueFinanceiro is null) or (mec.statusEstoqueFinanceiro = :statusEstoqueFinanceiro )) ")
        
        .append("  and tipoMovimento.grupoMovimentoEstoque in (:gruposMovimentoEstorno) ")
        
        .append("  and mec.status = :statusAprovacao ")
        
        .append("  and c2.id = ").append(paramIdCota)
        
        .append("  and mec.data <= :data");
        
        return hql.toString();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCotaGenericoDTO> obterListaMovimentoEstoqueCotaDevolucaoJuramentada(final Date dataOperacao) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select   ");
        
        hql.append(" produtoEdicao.id as idProdutoEdicao,	");
        hql.append(" cota.id as idCota,						");
        hql.append(" sum(conferenciaEncalhe.qtde) as qtde,	");
        hql.append(" chamadaEncalhe.id as idChamadaEncalhe, ");
        hql.append(" movimentoEstoqueCota.id as movimentoEstoqueCotaId ");
        
        hql.append(" from ConferenciaEncalhe conferenciaEncalhe	");
        
        hql.append(" inner join conferenciaEncalhe.produtoEdicao produtoEdicao 				");
        hql.append(" inner join conferenciaEncalhe.controleConferenciaEncalheCota controlConfEncalheCota ");
        hql.append(" inner join conferenciaEncalhe.chamadaEncalheCota chamadaEncalheCota ");
        hql.append(" inner join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
        hql.append(" inner join controlConfEncalheCota.cota cota ");
        hql.append(" inner join conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ");
        
        hql.append(" where ");
        
        hql.append(" controlConfEncalheCota.dataOperacao = :dataOperacao and 	");
        hql.append(" conferenciaEncalhe.juramentada = :juramentada  			");
        
        hql.append(" group by ");
        
        hql.append(" produtoEdicao.id,	");
        hql.append(" cota.id			");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoEstoqueCotaGenericoDTO.class));
        
        query.setParameter("dataOperacao", dataOperacao);
        
        query.setParameter("juramentada", true);
        
        return query.list();
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(java.lang.Long)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentosEstoqueEncalheDTO> obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(final Long idControleConferenciaEncalheCota) {
        
        final StringBuilder hql = new StringBuilder();
        hql.append("SELECT movimentoe1_.ID AS idMovimentoEstoqueCota,");
        hql.append("movimentoe1_.COTA_ID AS idCota,");
        hql.append("edicao.id as idProdutoEdicao,");
        hql.append("forn.id as idFornecedor,");
        hql.append("movimentoe1_.QTDE AS qtde,");
        hql.append("movimentoe1_.PRECO_COM_DESCONTO AS precoComDesconto,");
        hql.append("edicao.PRECO_VENDA AS precoVenda,");
        hql.append("movimentoe1_.VALOR_DESCONTO AS valorDesconto ");
        hql.append("FROM CONFERENCIA_ENCALHE conferenci0_ ");
        hql.append("INNER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe1_ ON conferenci0_.MOVIMENTO_ESTOQUE_COTA_ID=movimentoe1_.ID ");
        hql.append("INNER JOIN produto_edicao edicao ON movimentoe1_.produto_edicao_id = edicao.id ");
        hql.append("INNER JOIN PRODUTO prod ON edicao.PRODUTO_ID = prod.id ");
        hql.append("INNER JOIN produto_fornecedor prodForn ON prod.id = prodForn.PRODUTO_ID ");
        hql.append("INNER JOIN fornecedor forn ON prodForn.fornecedores_ID = forn.id ");
        hql.append("WHERE conferenci0_.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID=:idControleConferenciaEncalheCota ");
        hql.append("AND movimentoe1_.FORMA_COMERCIALIZACAO = :formaComercializacao ");
        
        final Query query = getSession().createSQLQuery(hql.toString())
        		.addScalar("idMovimentoEstoqueCota",LongType.INSTANCE)
        		.addScalar("idCota",LongType.INSTANCE)
        		.addScalar("idProdutoEdicao",LongType.INSTANCE)
        		.addScalar("idFornecedor",LongType.INSTANCE)
        		
        		.addScalar("qtde",BigIntegerType.INSTANCE)
        		.addScalar("precoComDesconto",BigDecimalType.INSTANCE)
        		.addScalar("precoVenda",BigDecimalType.INSTANCE)
        		.addScalar("valorDesconto",BigDecimalType.INSTANCE);
        
        query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
        query.setParameter("formaComercializacao", FormaComercializacao.CONSIGNADO.name());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(MovimentosEstoqueEncalheDTO.class));
        
        
        return query.list();
    }
    
	/**
     * Obtém movimentos de estoque da cota que ainda não geraram movimento
     * financeiro Considera movimentos de estoque provenientes dos fluxos de
     * Expedição - movimentos à vista
     * 
     * @param idCota
     * @param dataControleConferencia
     * @return List<MovimentoEstoqueCota>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoEstoqueCota> obterMovimentosAVistaPendentesGerarFinanceiro(final Long idCota, final Date dataLancamento) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select mec ")
        
        .append(this.getFromAVistaCotaAVista(":idCota"));
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameterList("gruposMovimentoReparte", Arrays.asList(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR,
                GrupoMovimentoEstoque.COMPRA_ENCALHE,
                GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
        
        query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO);
        query.setParameter("idCota", idCota);
        query.setParameter("data", dataLancamento);
        
        query.setCacheable(true);
        
        return query.list();
    }
    
	/**
     * Obtém movimentos de estoque da cota que ainda não geraram movimento
     * financeiro Considera movimentos de estoque provenientes dos fluxos de
     * Expedição - movimentos à vista ou consignados com conferencia prevista no
     * dia
     * 
     * @param idCota
     * @param dataControleConferencia
     * @return List<MovimentoEstoqueCota>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoEstoqueCota> obterMovimentosConsignadosCotaAVistaPrevistoDia(final Long idCota, final Date dataLancamento) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select mec ")
        
        .append(this.getFromConsignadoCotaAVista(":idCota"));
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameterList("gruposMovimentoReparte", Arrays.asList(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR,
                GrupoMovimentoEstoque.COMPRA_ENCALHE,
                GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
        
        query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO);
        query.setParameter("formaComercializacaoProduto", FormaComercializacao.CONTA_FIRME);
        query.setParameter("statusOperacaoConferencia", StatusOperacao.CONCLUIDO);
        query.setParameter("idCota", idCota);
        query.setParameter("data", dataLancamento);
        
        query.setCacheable(true);
        
        return query.list();
    }
    
	/**
     * Obtém movimentos de estoque da cota que ainda não geraram movimento
     * financeiro Considera movimentos de estoque provenientes dos fluxos de
     * Expedição e Conferência de Encalhe ou com Produtos Conta Firme
     * 
     * @param idCota
     * @param datas
     * @return List<MovimentoEstoqueCota>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentosEstoqueEncalheDTO> obterMovimentosPendentesGerarFinanceiroComChamadaEncalheOuProdutoContaFirme(final Long idCota, final List<Date> datas, List<Long> idTiposMovimentoEstoque) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT mec.ID AS idMovimentoEstoqueCota,");
        sql.append("mec.COTA_ID AS idCota,");
        sql.append("edicao.id AS idProdutoEdicao,");
        sql.append("forn.id AS idFornecedor,");
        sql.append("mec.QTDE AS qtde,");
        sql.append("mec.PRECO_COM_DESCONTO AS precoComDesconto,");
        sql.append("edicao.PRECO_VENDA AS precoVenda,");
        sql.append("mec.VALOR_DESCONTO AS valorDesconto ");
        sql.append("FROM movimento_estoque_cota mec ");
        sql.append("INNER JOIN produto_edicao edicao ON mec.produto_edicao_id = edicao.id ");
        sql.append("INNER JOIN PRODUTO prod ON edicao.PRODUTO_ID = prod.id ");
        sql.append("INNER JOIN produto_fornecedor prodForn ON prod.id = prodForn.PRODUTO_ID ");
        sql.append("INNER JOIN fornecedor forn ON prodForn.fornecedores_ID = forn.id ");
		sql.append("INNER JOIN chamada_encalhe ce on ce.PRODUTO_EDICAO_ID = edicao.id ");
		sql.append("INNER JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.id and cec.cota_id = mec.cota_id ");
		sql.append("INNER JOIN chamada_encalhe_lancamento cel on cel.CHAMADA_ENCALHE_ID = ce.id ");
		sql.append("INNER JOIN lancamento l on l.id = cel.LANCAMENTO_ID and mec.LANCAMENTO_ID = l.id ");
        sql.append("WHERE ");
        sql.append("mec.STATUS = :statusAprovacao AND "); 
        sql.append("mec.cota_id = :idCota AND ");
        sql.append("mec.FORMA_COMERCIALIZACAO = :formaComercializacao AND ");
        sql.append("(mec.STATUS_ESTOQUE_FINANCEIRO IS NULL OR mec.STATUS_ESTOQUE_FINANCEIRO = :statusFinanceiro) AND ");
        sql.append("mec.TIPO_MOVIMENTO_ID IN :idTiposMovimentoEstoque AND ");
        sql.append("mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL AND ");
        sql.append("(prod.FORMA_COMERCIALIZACAO = :formaComercializacaoProduto OR cec.COTA_ID = :idCota) AND ");
        sql.append("ce.DATA_RECOLHIMENTO IN (:datas) "); 
        sql.append("GROUP BY mec.ID");
        
        final Query query = getSession().createSQLQuery(sql.toString())
        		.addScalar("idMovimentoEstoqueCota",LongType.INSTANCE)
        		.addScalar("idCota",LongType.INSTANCE)
        		.addScalar("idProdutoEdicao",LongType.INSTANCE)
        		.addScalar("idFornecedor",LongType.INSTANCE)		
        		.addScalar("qtde",BigIntegerType.INSTANCE)
        		.addScalar("precoComDesconto",BigDecimalType.INSTANCE)
        		.addScalar("precoVenda",BigDecimalType.INSTANCE)
        		.addScalar("valorDesconto",BigDecimalType.INSTANCE);
        
        query.setParameterList("idTiposMovimentoEstoque", idTiposMovimentoEstoque);
        
        query.setParameter("statusFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
        query.setParameter("statusAprovacao", StatusAprovacao.APROVADO.name());
        query.setParameter("idCota", idCota);
        query.setParameterList("datas", datas);
        query.setParameter("formaComercializacaoProduto", FormaComercializacao.CONTA_FIRME.name());
        query.setParameter("formaComercializacao", FormaComercializacao.CONSIGNADO.name());
        query.setResultTransformer(new AliasToBeanResultTransformer(MovimentosEstoqueEncalheDTO.class));
        
        return query.list();
    }
    
	/**
     * Obtém movimentos de estoque da cota que forão estornados Considera
     * movimentos de estoque provenientes dos fluxos de Venda de Encalhe e
     * Suplementar
     * 
     * @param idCota
     * @return List<MovimentoEstoqueCota>
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<MovimentoEstoqueCota> obterMovimentosEstornadosPorChamadaEncalhe(final Long idCota, final List<Long> idsTipoMovimentoEstorno, List<Date> datas) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select mec.* ");
        sql.append(" from ");
        sql.append(" movimento_estoque_cota mec ");
        sql.append(" inner join chamada_encalhe_cota cec on cec.cota_id = mec.cota_id ");
        sql.append(" inner join chamada_encalhe ce on ce.id = cec.chamada_encalhe_id and ce.produto_edicao_id = mec.produto_edicao_id ");
        sql.append(" inner join chamada_encalhe_lancamento cel on cel.CHAMADA_ENCALHE_ID = ce.id ");
        sql.append(" inner join lancamento l on l.id = mec.lancamento_id and l.id = cel.lancamento_id ");
        sql.append(" where mec.TIPO_MOVIMENTO_ID in (:idsTipoMovimentoEstorno) ");
        sql.append(" and mec.cota_id = :idCota ");
        // sql.append(" and mec.STATUS_ESTOQUE_FINANCEIRO = 'FINANCEIRO_NAO_PROCESSADO' ");
        
        if(datas != null && !datas.isEmpty()) {
        	
        	sql.append(" and ce.data_recolhimento IN (:datas) ");
        	
        }
        
        sql.append(" group by mec.id ");
        
        final Query query = getSession().createSQLQuery(sql.toString()).addEntity(MovimentoEstoqueCota.class);
        
        query.setParameterList("idsTipoMovimentoEstorno", idsTipoMovimentoEstorno);
        
        if(datas != null && !datas.isEmpty()) {
        	
        	query.setParameterList("datas", datas);
        }
        
        query.setParameter("idCota", idCota);
        
        return query.list();
        
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date, br.com.abril.nds.model.estoque.OperacaoEstoque)
     */
    @Override
    public BigInteger obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
            final Long idCota,
            final Long idProdutoEdicao,
            final Date dataInicial,
            final Date dataFinal,
            final OperacaoEstoque operacaoEstoque) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select sum(movimentoEstoqueCota.qtde) ");
        
        hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
        
        hql.append(" where movimentoEstoqueCota.data between :dataInicial and :dataFinal and ");
        
        hql.append(" movimentoEstoqueCota.cota.id = :idCota and ");
        
        hql.append(" movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao and ");
        
        hql.append(" movimentoEstoqueCota.tipoMovimento.operacaoEstoque = :operacaoEstoque ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        query.setParameter("idCota", idCota);
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("operacaoEstoque", operacaoEstoque);
        
        return (BigInteger) query.uniqueResult();
        
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date, br.com.abril.nds.model.estoque.OperacaoEstoque)
     */
    @Override
    public BigDecimal obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
            final Long idCota,
            final Long idFornecedor,
            final Long idProdutoEdicao,
            final Date dataInicial,
            final Date dataFinal,
            final OperacaoEstoque operacaoEstoque) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select sum(movimentoEstoqueCota.qtde * produtoEdicao.precoVenda) ");
        
        hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
        
        hql.append(" join movimentoEstoqueCota.produtoEdicao produtoEdicao  ");
        
        hql.append(" join produtoEdicao.produto produto  ");
        
        hql.append(" join produto.fornecedores fornecedor  ");
        
        hql.append(" where movimentoEstoqueCota.data between :dataInicial and :dataFinal and ");
        
        hql.append(" movimentoEstoqueCota.cota.id = :idCota and ");
        
        hql.append(" fornecedor.id = :idFornecedor and ");
        
        hql.append(" movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao and ");
        
        hql.append(" movimentoEstoqueCota.tipoMovimento.operacaoEstoque = :operacaoEstoque ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        query.setParameter("idCota", idCota);
        query.setParameter("idFornecedor", idFornecedor);
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("operacaoEstoque", operacaoEstoque);
        
        return (BigDecimal) query.uniqueResult();
        
    }
    
    private StringBuilder obterQueryListaConsultaEncalhe(final FiltroConsultaEncalheDTO filtro, final boolean counting) {
    	
		final StringBuilder subSqlVendaProduto = new StringBuilder();
        
		subSqlVendaProduto.append(" select COALESCE(sum( vp.QNT_PRODUTO ),0) ");
        subSqlVendaProduto.append(" from venda_produto vp ");
        subSqlVendaProduto.append(" where vp.ID_PRODUTO_EDICAO = PRODUTO_EDICAO.ID ");
        subSqlVendaProduto.append(" and vp.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
        subSqlVendaProduto.append(" and vp.TIPO_VENDA_ENCALHE = :tipoVendaProduto");
        
        if (filtro.getIdCota() != null) {
            subSqlVendaProduto.append(" and vp.ID_COTA = :idCota ");
           // subSqlVendaProduto.append(" and vp.TIPO_COMERCIALIZACAO_VENDA <> 'CONTA_FIRME' ");
        }
        
        final StringBuilder qtdeInformadaEncalhe = new StringBuilder("coalesce(sum(COALESCE(CONFERENCIA_ENCALHE.QTDE, 0)), 0)");
        
        final StringBuilder subSqlValoresDesconto = new StringBuilder();
        
        if (filtro.getIdCota() != null) {
            
            subSqlValoresDesconto.append("  COALESCE(TBL_PRECO_VENDA.PRECO_COM_DESCONTO, PRODUTO_EDICAO.PRECO_VENDA, 0) as precoComDesconto, ");
            subSqlValoresDesconto.append("	COALESCE(TBL_PRECO_VENDA.VALOR_DESCONTO, 0) as valorDesconto, ");
            subSqlValoresDesconto.append("	( ( "+ qtdeInformadaEncalhe +") * COALESCE(TBL_PRECO_VENDA.PRECO_COM_DESCONTO , PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valorComDesconto, ");
            
        } else {
        	
            subSqlValoresDesconto.append("  CASE  ");
            subSqlValoresDesconto.append("  WHEN PRODUTO_EDICAO.ORIGEM = :origemInterface THEN ( ");
            subSqlValoresDesconto.append("   COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) - 		");
            subSqlValoresDesconto.append("   (COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) *  	");
            subSqlValoresDesconto.append("   COALESCE(DESCONTO_LOGISTICA.PERCENTUAL_DESCONTO,0) / 100)  	");
            subSqlValoresDesconto.append("  ) ");
            subSqlValoresDesconto.append("  ELSE ( ");
            subSqlValoresDesconto.append("   COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) - ");
            subSqlValoresDesconto.append("   (COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) * COALESCE(PRODUTO_EDICAO.DESCONTO, PRODUTO.DESCONTO, 0) / 100) ");
            subSqlValoresDesconto.append("  ) END AS precoComDesconto, ");

			subSqlValoresDesconto.append("  CASE  ");
			subSqlValoresDesconto.append("  WHEN PRODUTO_EDICAO.ORIGEM = :origemInterface THEN (	");
			subSqlValoresDesconto.append("  COALESCE(DESCONTO_LOGISTICA.PERCENTUAL_DESCONTO, 0)		");
			subSqlValoresDesconto.append("  ) ");
			subSqlValoresDesconto.append("  ELSE ( ");
			subSqlValoresDesconto.append("  COALESCE(PRODUTO_EDICAO.DESCONTO, PRODUTO.DESCONTO, 0) ");
			subSqlValoresDesconto.append("  ) END AS valorDesconto, ");
			
			subSqlValoresDesconto.append("  CASE  ");
			subSqlValoresDesconto.append("  WHEN PRODUTO_EDICAO.ORIGEM = :origemInterface THEN ( ");

	        subSqlValoresDesconto.append("  COALESCE(( "+ qtdeInformadaEncalhe +" ), 0) * COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) - ");
	        subSqlValoresDesconto.append("  (COALESCE(( "+ qtdeInformadaEncalhe +" ), 0) * COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) * " );
	        subSqlValoresDesconto.append("  COALESCE(DESCONTO_LOGISTICA.PERCENTUAL_DESCONTO, 0) / 100 )   ");
	          
	        subSqlValoresDesconto.append("  ) ");
	        subSqlValoresDesconto.append("  ELSE ( ");
	          
	        subSqlValoresDesconto.append("  COALESCE(( "+ qtdeInformadaEncalhe +" ), 0) * COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) - ");
	        subSqlValoresDesconto.append("  (COALESCE(( "+ qtdeInformadaEncalhe +" ), 0) * COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) * " );
	        subSqlValoresDesconto.append("  COALESCE(PRODUTO_EDICAO.DESCONTO, PRODUTO.DESCONTO, 0) / 100 )   ");
	          
	        subSqlValoresDesconto.append("  ) END AS valorComDesconto, ");
        }
        
        final StringBuilder subSqlIndObservacao = new StringBuilder();
        
        subSqlIndObservacao.append(" SELECT count(*) > 0 ");
        subSqlIndObservacao.append(" 	FROM CONFERENCIA_ENCALHE CONFERENCIA_ENCALHE_0 ");
        
        if (filtro.getIdCota() != null) {
        	subSqlIndObservacao.append(" INNER JOIN CHAMADA_ENCALHE_COTA on CONFERENCIA_ENCALHE_0.CHAMADA_ENCALHE_COTA_ID = CHAMADA_ENCALHE_COTA.id ");
        }
        
        subSqlIndObservacao.append(" WHERE CONFERENCIA_ENCALHE_0.PRODUTO_EDICAO_ID = PRODUTO_EDICAO.ID ");
        
        if (filtro.getIdCota() != null) {
            subSqlIndObservacao.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota ");
        }
        
        subSqlIndObservacao.append(" 	and (CONFERENCIA_ENCALHE_0.OBSERVACAO is not null OR CONFERENCIA_ENCALHE_0.JURAMENTADA = 1) ");
        
        final StringBuilder sql = new StringBuilder();
        
        if(counting) {
            
            sql.append("	select CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID, CONFERENCIA_ENCALHE.DIA_RECOLHIMENTO ");
            
        } else {
            
            sql.append("	select	");
            
            sql.append("	PRODUTO.CODIGO 							as codigoProduto, 					");
            sql.append("	PRODUTO.NOME 							as nomeProduto,   					");
            sql.append("	PRODUTO_EDICAO.ID 						as idProdutoEdicao,  				");
            sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO 			as numeroEdicao,  					");
            
            sql.append("	COALESCE( TBL_PRECO_VENDA.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0)	as precoVenda, ");
            
            sql.append(subSqlValoresDesconto);
            
            if (filtro.getIdCota() != null) {
                sql.append("	( ( "+ qtdeInformadaEncalhe +") * COALESCE(TBL_PRECO_VENDA.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valor, ");
            } else {
                sql.append("	( ( "+ qtdeInformadaEncalhe +" ) * COALESCE(PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valor, ");
            }
            
            sql.append(" TBL_REPARTE.REPARTE as reparte, ");
            
            sql.append("( ( ").append(qtdeInformadaEncalhe).append(" ) - ( ").append(subSqlVendaProduto).append(") ) as encalhe, ");
            
            sql.append("	FORNECEDOR.ID						as idFornecedor,		");
            
            sql.append(" 	PESSOA.RAZAO_SOCIAL 				as fornecedor,  		");
            
            sql.append("    CCEC.DATA_OPERACAO as dataMovimento,		");
            
            sql.append(" (" + subSqlIndObservacao + ") AS indObservacaoConferenciaEncalhe, ");
            
            sql.append(" coalesce(CONFERENCIA_ENCALHE.DIA_RECOLHIMENTO,1) AS diaRecolhimento ");
            
        }

        sql.append(getFromWhereConsultaEncalhe(filtro));
        
        return sql;
    }  
    
    @Override
    public Integer obterQtdeConsultaEncalhe(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        final FiltroConsultaEncalheDTO f = new FiltroConsultaEncalheDTO();
        
        BeanUtils.copyProperties(filtro, f);
        f.setPaginacao(null);
        
        sql.append("	SELECT COUNT(*) FROM ( ");
        sql.append(obterQueryListaConsultaEncalhe(f, true));
        sql.append(" )	as encalhes ");
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
        if(filtro.getIdCota() != null) {
            parameters.put("idCota", filtro.getIdCota());
        } else {
        	parameters.put("origemInterface", Origem.INTERFACE.name());
        }
        
        parameters.put("grupoMovimentoEstoqueConsignado", Arrays.asList(
					GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name()
					, GrupoMovimentoEstoque.COMPRA_ENCALHE.name()
					, GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name())
				);
        
        if(filtro.getIdFornecedor() != null) {
            parameters.put("idFornecedor", filtro.getIdFornecedor());
        }

        if(filtro.getCodigoProduto() != null) {
        	parameters.put("codigoProduto", filtro.getCodigoProduto());
        }

        if(filtro.getIdProdutoEdicao() != null) {
            parameters.put("idProdutoEdicao", filtro.getIdProdutoEdicao());
        }
        
        if(filtro.getIdBoxEncalhe() != null) {
            parameters.put("boxEncalhe", filtro.getIdBoxEncalhe());
        }
        
        if(filtro.getIdBox() != null) {
            parameters.put("box", filtro.getIdBox());
        }
        
        if(filtro.getIdRota() != null) {
            parameters.put("rota", filtro.getIdRota());
        }
        
        if(filtro.getIdRoteiro() != null) {
            parameters.put("roteiro", filtro.getIdRoteiro());
        }
        
        parameters.put("grupoMovimentoEstoqueEncalhe", GrupoMovimentoEstoque.ENVIO_ENCALHE.name());
        parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        parameters.put("isPostergado", false);
        parameters.put("tipoVendaProduto",TipoVendaEncalhe.ENCALHE.name());
        
        final Integer qtde = namedParameterJdbcTemplate.queryForInt(sql.toString(), parameters);
        
        return qtde == null ? 0 : qtde;
    }
    
    public BigDecimal obterSaldoEntradaNoConsignado(Date dataRecolhimento, TipoCota tipoCota) {
    	
	    final StringBuilder sql = new StringBuilder();
	    
	    sql.append("SELECT");
	    sql.append("	coalesce(SUM(	");
	    sql.append("		CASE WHEN REPARTES.DIARECOLHIMENTO <> 1 THEN 0        ");
	    sql.append("		ELSE (REPARTES.PRECOVENDA * REPARTES.QTDREPARTE) END  ");
	    sql.append("	),0) AS totalReparte ");
	    sql.append("FROM ( ");
	    sql.append(" SELECT ");
	    sql.append("	SUM( IF(TM.OPERACAO_ESTOQUE='SAIDA', MEC.QTDE*-1, MEC.QTDE) ) AS QTDREPARTE,                              ");
	    sql.append("	COALESCE(MEC.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0) AS PRECOVENDA,                                   ");
	    sql.append("	COALESCE(CONFERENCIA_ENCALHE.DIA_RECOLHIMENTO,1) AS DIARECOLHIMENTO                                       ");
	    sql.append(" FROM                                                                                                          ");
	    sql.append("	CHAMADA_ENCALHE                                                                                           ");
	    sql.append("	INNER JOIN CHAMADA_ENCALHE_COTA ON ( CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CHAMADA_ENCALHE.ID )       ");
	    sql.append("	INNER JOIN PRODUTO_EDICAO ON ( PRODUTO_EDICAO.ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID )                    ");
	    sql.append("	INNER JOIN PRODUTO PRODUTO ON ( PRODUTO.ID = PRODUTO_EDICAO.PRODUTO_ID )                                  ");
	    
	    sql.append("	INNER JOIN MOVIMENTO_ESTOQUE_COTA MEC ON ( MEC.COTA_ID = CHAMADA_ENCALHE_COTA.COTA_ID 	");
	    sql.append("		AND MEC.PRODUTO_EDICAO_ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID ) 					");
	    
	    sql.append("	INNER JOIN TIPO_MOVIMENTO TM ON ( MEC.TIPO_MOVIMENTO_ID = TM.ID )                       ");
	    sql.append("	INNER JOIN CHAMADA_ENCALHE_LANCAMENTO cel on cel.CHAMADA_ENCALHE_ID = CHAMADA_ENCALHE.ID AND cel.LANCAMENTO_ID = MEC.LANCAMENTO_ID ");
	    sql.append("  	LEFT JOIN CONFERENCIA_ENCALHE ON (                                                      ");
	    sql.append("		CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID = CHAMADA_ENCALHE_COTA.ID               ");
	    sql.append("        AND CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID       ");
	    sql.append("    )                                                                                       ");
	    sql.append("    INNER JOIN COTA ON COTA.ID = CHAMADA_ENCALHE_COTA.COTA_ID                               ");
	    sql.append(" WHERE                                                                                      ");
	    sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento    			                    ");
	    sql.append("    AND TM.GRUPO_MOVIMENTO_ESTOQUE <> :grupoMovimentoEstoqueEncalhe   	                    ");
	    sql.append("	AND MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL                                          ");
	    sql.append("	AND MEC.FORMA_COMERCIALIZACAO = :consignado                                             ");
	    sql.append("	AND MEC.LANCAMENTO_ID IS NOT NULL                                                       ");
	    sql.append("	AND CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_COTA_POSTERGADA_ID IS NULL                     ");
	    
	    if(tipoCota != null) {
    		sql.append("	AND COTA.TIPO_COTA = :tipoCota                  ");
	    }
	    
	    sql.append(" GROUP BY ");
	    sql.append("	CHAMADA_ENCALHE.PRODUTO_EDICAO_ID,                  ");
	    sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO                   ");
	    sql.append(") REPARTES                                              ");
    	
	    Query query = getSession().createSQLQuery(sql.toString());
	    
	    query.setParameter("dataRecolhimento", dataRecolhimento);
	    query.setParameter("grupoMovimentoEstoqueEncalhe", GrupoMovimentoEstoque.ENVIO_ENCALHE.name());
	    query.setParameter("consignado", FormaComercializacao.CONSIGNADO.name());
	    
	    if(tipoCota != null) {
    		query.setParameter("tipoCota", tipoCota.name());
	    }
	    
	    ((SQLQuery) query).addScalar("totalReparte", StandardBasicTypes.BIG_DECIMAL);
	    
	    return (BigDecimal) query.uniqueResult();
	    
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public ConsultaEncalheDTO obterValorTotalReparteEncalheDataCotaFornecedor(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        final FiltroConsultaEncalheDTO f = new FiltroConsultaEncalheDTO();
        
        boolean indUtilizaPrecoCapa = filtro.isUtilizaPrecoCapa();
        
        BeanUtils.copyProperties(filtro, f);
        f.setPaginacao(null);
        f.setDesconsiderarCotaAVista(true);
        
        sql.append("select ");

        sql.append(" sum(case when diaRecolhimento = 1 then ");
        sql.append(indUtilizaPrecoCapa ? " a.precoVenda " : " a.precoComDesconto ");
        sql.append(" * a.reparte else 0 end ) as totalReparte, ");
        
        sql.append(" sum( ");
        sql.append(indUtilizaPrecoCapa ? "  a.precoVenda " : " a.precoComDesconto ");
        sql.append(" * a.encalhe) as totalEncalhe   ");
        
        sql.append(" from ( ");
        
        sql.append(obterQueryListaConsultaEncalhe(f, false));
        
        sql.append(") a ");
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
        if(filtro.getIdCota() != null) {
            parameters.put("idCota", filtro.getIdCota());
        } else {
            parameters.put("origemInterface", Origem.INTERFACE.name());
        }
        
        if(filtro.getIdFornecedor() != null) {
            parameters.put("idFornecedor", filtro.getIdFornecedor());
        }

        if(filtro.getCodigoProduto() != null) {
            parameters.put("codigoProduto", filtro.getCodigoProduto());
        }
        
        if(filtro.getIdProdutoEdicao() != null) {
            parameters.put("idProdutoEdicao", filtro.getIdProdutoEdicao());
        }

        if(filtro.getIdBoxEncalhe() != null) {
            parameters.put("boxEncalhe", filtro.getIdBoxEncalhe());
        }

        if(filtro.getIdBox() != null) {
            parameters.put("box", filtro.getIdBox());
        }
        
        if(filtro.getIdRota() != null) {
            parameters.put("rota", filtro.getIdRota());
        }
        
        if(filtro.getIdRoteiro() != null) {
            parameters.put("roteiro", filtro.getIdRoteiro());
        }
        
        parameters.put("grupoMovimentoEstoqueEncalhe", GrupoMovimentoEstoque.ENVIO_ENCALHE.name());
        parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        parameters.put("isPostergado", false);
        parameters.put("tipoVendaProduto",TipoVendaEncalhe.ENCALHE.name());
        
        @SuppressWarnings("rawtypes")
        final
        RowMapper cotaRowMapper = new RowMapper() {
            
            @Override
            public Object mapRow(final ResultSet rs, final int arg1) throws SQLException {
                
                final ConsultaEncalheDTO dto = new ConsultaEncalheDTO();
                dto.setReparte(rs.getBigDecimal("totalReparte"));
                dto.setEncalhe(rs.getBigDecimal("totalEncalhe"));
                
                return dto;
            }
        };
        
        return (ConsultaEncalheDTO) namedParameterJdbcTemplate.queryForObject(sql.toString(), parameters, cotaRowMapper);
    }
    
    @Override
    public BigDecimal obterValorTotalEncalhe(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append("	SELECT	");
        
        sql.append("	SUM( MOVIMENTO_ESTOQUE_COTA.QTDE * COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO, PRODUTO_EDICAO.PRECO_VENDA, 0) ) as totalEncalhe ");
        
        sql.append("	from	");
        
        sql.append("	CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA, MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
        sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");
        
        sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
        
        sql.append("	inner join COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
        
        
        sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
        sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
        
        sql.append("	inner join CHAMADA_ENCALHE on ");
        sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
        
        sql.append("	inner join PRODUTO_EDICAO on ");
        sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
        
        sql.append("	inner join PRODUTO on ");
        sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
        
        sql.append("	inner join PRODUTO_FORNECEDOR on ");
        sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
        
        sql.append("	inner join FORNECEDOR on ");
        sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
        
        sql.append("	inner join PESSOA on                   	");
        sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
        
        sql.append("	where	");
        
        sql.append("	CONTROLE_CONF_ENC_COTA.ID = CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID ");
        
        sql.append("	AND (CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
        
        sql.append("	AND CHAMADA_ENCALHE_COTA.FECHADO = :isPostergado ");
        
        if(filtro.getIdCota()!=null) {
            sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
        }
        
        if(filtro.getIdFornecedor() != null) {
            sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
        }
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
        if(filtro.getIdCota()!=null) {
            parameters.put("idCota", filtro.getIdCota());
        }
        
        if(filtro.getIdFornecedor() != null) {
            parameters.put("idFornecedor", filtro.getIdFornecedor());
        }
        
        parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        parameters.put("isPostergado", false);
        
        final Map<String, Object> queryForMap = namedParameterJdbcTemplate.queryForMap(sql.toString(), parameters);
        final Object totalEncalhe = queryForMap.get("totalEncalhe");
        
        return (BigDecimal) (totalEncalhe == null ? 
        		BigDecimal.ZERO : totalEncalhe);
    }

    public StringBuilder getFromWhereConsultaEncalhe(final FiltroConsultaEncalheDTO filtro) {
		
		final StringBuilder sqlTblPrecoVenda = new StringBuilder();
		
		sqlTblPrecoVenda.append(" SELECT ");
		sqlTblPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID,    ");
		sqlTblPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA.PRECO_VENDA AS PRECO_VENDA,                ");
		sqlTblPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO AS PRECO_COM_DESCONTO,  ");
		sqlTblPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA.VALOR_DESCONTO AS VALOR_DESCONTO			");
		sqlTblPrecoVenda.append(" FROM ");
		sqlTblPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA 	");
		sqlTblPrecoVenda.append(" INNER JOIN				");
		sqlTblPrecoVenda.append(" (SELECT					");
		sqlTblPrecoVenda.append(" 	MEC.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		sqlTblPrecoVenda.append(" 	MAX(MEC.DATA) AS DATA_REPARTE				");
		sqlTblPrecoVenda.append(" FROM							");
		sqlTblPrecoVenda.append(" 	MOVIMENTO_ESTOQUE_COTA MEC  ");
		if(filtro.getIdBox() != null) {
			sqlTblPrecoVenda.append(" INNER JOIN COTA COTA ON (COTA.ID = MEC.COTA_ID) ");
			sqlTblPrecoVenda.append(" INNER JOIN BOX BLANC ON (COTA.BOX_ID = BLANC.ID) ");
			sqlTblPrecoVenda.append(" INNER JOIN PDV on (PDV.COTA_ID = COTA.ID and PDV.PONTO_PRINCIPAL = true) ");
			sqlTblPrecoVenda.append(" INNER JOIN ROTA_PDV on (ROTA_PDV.PDV_ID = PDV.ID) ");
			sqlTblPrecoVenda.append(" INNER JOIN ROTA on (ROTA.ID = ROTA_PDV.ROTA_ID) ");
			sqlTblPrecoVenda.append(" INNER JOIN ROTEIRO on (ROTEIRO.ID = ROTA.ROTEIRO_ID) ");
			sqlTblPrecoVenda.append(" INNER JOIN ROTEIRIZACAO on (ROTEIRIZACAO.ID = ROTEIRO.ROTEIRIZACAO_ID) ");
			sqlTblPrecoVenda.append(" AND (BLANC.ID = ROTEIRIZACAO.BOX_ID) ");
			
		}
		sqlTblPrecoVenda.append(" INNER JOIN                    ");
		sqlTblPrecoVenda.append(" (SELECT PRODUTO_EDICAO.ID AS ID ");
		sqlTblPrecoVenda.append(" FROM CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ");
		sqlTblPrecoVenda.append(" INNER JOIN CONFERENCIA_ENCALHE ON (CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID) ");
		
		if(filtro.getIdBoxEncalhe() != null) {
			sqlTblPrecoVenda.append(" INNER JOIN BOX ON  (CCEC.BOX_ID = BOX.ID) ");
        }
		
		sqlTblPrecoVenda.append(" INNER JOIN PRODUTO_EDICAO ON (PRODUTO_EDICAO.ID = CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID)  ");
		sqlTblPrecoVenda.append(" INNER JOIN PRODUTO ON (PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID)                            ");
		sqlTblPrecoVenda.append(" INNER JOIN PRODUTO_FORNECEDOR ON (PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID)             ");
		sqlTblPrecoVenda.append(" INNER JOIN FORNECEDOR ON (PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID)             ");
		sqlTblPrecoVenda.append(" INNER JOIN PESSOA ON (PESSOA.ID = FORNECEDOR.JURIDICA_ID)                                 ");
		sqlTblPrecoVenda.append(" WHERE CCEC.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal		");
		sqlTblPrecoVenda.append(filtro.getIdCota() !=null ? " AND CCEC.COTA_ID = :idCota " : "");
		sqlTblPrecoVenda.append(filtro.getCodigoProduto() != null ? " AND PRODUTO.CODIGO = :codigoProduto " : "");
		sqlTblPrecoVenda.append(filtro.getIdProdutoEdicao() != null ? " AND PRODUTO_EDICAO.ID = :idProdutoEdicao " : "");
		sqlTblPrecoVenda.append(filtro.getIdBoxEncalhe() != null ? " AND BOX.ID = :boxEncalhe " : "");
		sqlTblPrecoVenda.append(" GROUP BY CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID) AS EDICAO_ENCALHADA ON  					");
		sqlTblPrecoVenda.append(" ( MEC.PRODUTO_EDICAO_ID = EDICAO_ENCALHADA.ID AND MEC.TIPO_MOVIMENTO_ID ) ");
		
		sqlTblPrecoVenda.append(" INNER JOIN TIPO_MOVIMENTO ON ( TIPO_MOVIMENTO.ID = MEC.TIPO_MOVIMENTO_ID ) ");
		
		sqlTblPrecoVenda.append(" WHERE 1=1 ");
		sqlTblPrecoVenda.append(filtro.getIdBox() != null ? " AND BLANC.CODIGO = :box " : "");
		// sqlTblPrecoVenda.append(" WHERE TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE in (:grupoMovimentoEstoqueConsignado) ");
		sqlTblPrecoVenda.append(filtro.getIdCota()!=null ? " AND MEC.COTA_ID = :idCota " : "");
		sqlTblPrecoVenda.append(filtro.getIdRota() != null ? " AND ROTA.ID = :rota " : "");
		sqlTblPrecoVenda.append(filtro.getIdRoteiro() != null ? " AND ROTEIRO.ID = :roteiro " : "");
		sqlTblPrecoVenda.append(" GROUP BY MEC.PRODUTO_EDICAO_ID ");
		sqlTblPrecoVenda.append(" ) AS PRECO_VENDA_DE_REPARTE ON (MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID = PRECO_VENDA_DE_REPARTE.PRODUTO_EDICAO_ID AND ");
		sqlTblPrecoVenda.append(" 								MOVIMENTO_ESTOQUE_COTA.DATA = PRECO_VENDA_DE_REPARTE.DATA_REPARTE)                       ");
		sqlTblPrecoVenda.append(" INNER JOIN TIPO_MOVIMENTO ON (TIPO_MOVIMENTO.ID = MOVIMENTO_ESTOQUE_COTA.TIPO_MOVIMENTO_ID) ");
		sqlTblPrecoVenda.append(" WHERE 1=1 ");
		//sqlTblPrecoVenda.append(" WHERE TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE in (:grupoMovimentoEstoqueConsignado) ");
		sqlTblPrecoVenda.append(filtro.getIdCota()!=null ? " AND MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota " : "");
		sqlTblPrecoVenda.append(" GROUP BY MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID	                                                                         ");
		
		StringBuilder sqlTblReparte = new StringBuilder();
		
		sqlTblReparte.append(" SELECT	");
		sqlTblReparte.append(" MEC.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		
		if(filtro.isDesconsiderarCotaAVista()){
			
			sqlTblReparte.append(" if(MEC.COTA_ID in (select cota_a_vista.ID from cota cota_a_vista where cota_a_vista.TIPO_COTA = 'A_VISTA'),0, SUM(	");
			sqlTblReparte.append(" 	COALESCE(if(tm.OPERACAO_ESTOQUE = 'SAIDA', MEC.qtde*-1, MEC.qtde),0)                                    ");
			sqlTblReparte.append(" )) AS REPARTE ");
		}
		else{
			
			sqlTblReparte.append(" SUM(	");
			sqlTblReparte.append(" 	COALESCE(if(tm.OPERACAO_ESTOQUE = 'SAIDA', MEC.qtde*-1, MEC.qtde),0)                                 ");
			sqlTblReparte.append(" ) AS REPARTE ");
		}
		
		sqlTblReparte.append(" FROM  ");
		sqlTblReparte.append(" MOVIMENTO_ESTOQUE_COTA MEC	");
		
		if(filtro.getIdBoxEncalhe() != null) {
			sqlTblReparte.append(" INNER JOIN CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ON (CCEC.COTA_ID = MEC.COTA_ID)   ");
			sqlTblReparte.append(" INNER JOIN BOX ON  (CCEC.BOX_ID = BOX.ID) ");
			sqlTblReparte.append(" INNER JOIN CONFERENCIA_ENCALHE ON (CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID ");
			sqlTblReparte.append(" 	AND CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID = MEC.PRODUTO_EDICAO_ID )   ");
        }
		sqlTblReparte.append(" INNER JOIN                   	");
		sqlTblReparte.append(" (SELECT PRODUTO_EDICAO.ID AS ID  ");
		sqlTblReparte.append(" FROM CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ");
		sqlTblReparte.append(" INNER JOIN CONFERENCIA_ENCALHE ON (CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID)   ");
		
		if(filtro.getIdBoxEncalhe() != null) {
			sqlTblReparte.append(" INNER JOIN BOX ON  (CCEC.BOX_ID = BOX.ID) ");
        }
		
		sqlTblReparte.append(" INNER JOIN PRODUTO_EDICAO ON (PRODUTO_EDICAO.ID = CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID)                 ");
		sqlTblReparte.append(" INNER JOIN PRODUTO ON (PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID)                                           ");
		sqlTblReparte.append(" INNER JOIN PRODUTO_FORNECEDOR ON (PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID)                            ");
		sqlTblReparte.append(" INNER JOIN FORNECEDOR ON (PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID)                            ");
		sqlTblReparte.append(" INNER JOIN PESSOA ON (PESSOA.ID = FORNECEDOR.JURIDICA_ID)                                                ");
		sqlTblReparte.append(" WHERE CCEC.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal                                           ");
		sqlTblReparte.append(filtro.getIdCota() != null ? " AND CCEC.COTA_ID = :idCota " : "");
		sqlTblReparte.append(filtro.getCodigoProduto() != null ? " AND PRODUTO.CODIGO = :codigoProduto " : "");
		sqlTblReparte.append(filtro.getIdProdutoEdicao() != null ? " AND PRODUTO_EDICAO.ID = :idProdutoEdicao " : "");
		sqlTblReparte.append(filtro.getIdBoxEncalhe() != null ? " AND BOX.ID = :boxEncalhe " : "");
		
		sqlTblReparte.append(" GROUP BY CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID ");
		sqlTblReparte.append(" ) AS EDICAO_ENCALHADA ON ( MEC.PRODUTO_EDICAO_ID = EDICAO_ENCALHADA.ID ) ");
		
		sqlTblReparte.append(" INNER JOIN cota c on c.id = MEC.COTA_ID ");
		
		if(filtro.getIdBox() != null) {
			sqlTblReparte.append(" INNER JOIN BOX BLANC ON (c.BOX_ID = BLANC.ID) ");
			sqlTblReparte.append(" INNER JOIN PDV on (PDV.COTA_ID = c.ID and PDV.PONTO_PRINCIPAL = true) ");
			sqlTblReparte.append(" INNER JOIN ROTA_PDV on (ROTA_PDV.PDV_ID = PDV.ID) ");
			sqlTblReparte.append(" INNER JOIN ROTA on (ROTA.ID = ROTA_PDV.ROTA_ID) ");
			sqlTblReparte.append(" INNER JOIN ROTEIRO on (ROTEIRO.ID = ROTA.ROTEIRO_ID) ");
			sqlTblReparte.append(" INNER JOIN ROTEIRIZACAO on (ROTEIRIZACAO.ID = ROTEIRO.ROTEIRIZACAO_ID) ");
			sqlTblReparte.append(" AND (BLANC.ID = ROTEIRIZACAO.BOX_ID) ");
		}
		
		sqlTblReparte.append(" INNER JOIN TIPO_MOVIMENTO TM ON (TM.ID = MEC.TIPO_MOVIMENTO_ID) ");
		
		sqlTblReparte.append(" INNER JOIN chamada_encalhe ce on ce.PRODUTO_EDICAO_ID = EDICAO_ENCALHADA.ID ");
				
		sqlTblReparte.append(" INNER JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.id and cec.COTA_ID = c.id "); 
		
		sqlTblReparte.append(" INNER JOIN conferencia_encalhe conf on conf.CHAMADA_ENCALHE_COTA_ID =  cec.id "); 
		
		sqlTblReparte.append(" INNER JOIN chamada_encalhe_lancamento cel on cel.CHAMADA_ENCALHE_ID = ce.id ");
		
		sqlTblReparte.append(" INNER JOIN lancamento l on l.id = mec.LANCAMENTO_ID and l.id = cel.LANCAMENTO_ID and l.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID"); 
		
		sqlTblReparte.append(" LEFT OUTER JOIN PERIODO_LANCAMENTO_PARCIAL PLP ON PLP.ID = L.PERIODO_LANCAMENTO_PARCIAL_ID  ");
		
		sqlTblReparte.append(" LEFT OUTER JOIN LANCAMENTO_PARCIAL LP ON PLP.LANCAMENTO_PARCIAL_ID = LP.ID ");
		
		sqlTblReparte.append(" WHERE MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
		
		sqlTblReparte.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE <> :grupoMovimentoEstoqueEncalhe ");
		
		sqlTblReparte.append(" AND conf.DATA BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
		
		// sqlTblReparte.append(" AND case when l.periodo_lancamento_parcial_id is not null then CE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal else 1 = 1 end ");
		
		sqlTblReparte.append(filtro.getIdCota()!=null ? " AND MEC.COTA_ID = :idCota " : "");
		
		sqlTblReparte.append(filtro.getIdBox() != null ? " AND BLANC.CODIGO = :box " : "");
		
		sqlTblReparte.append(filtro.getIdRota() != null ? " AND ROTA.ID = :rota " : "");
		
		sqlTblReparte.append(filtro.getIdRoteiro() != null ? " AND ROTEIRO.ID = :roteiro " : "");
		
		if(filtro.getIdBoxEncalhe() != null) {
			
			sqlTblReparte.append(" AND BOX.ID = :boxEncalhe ");
			sqlTblReparte.append(" AND CCEC.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal "); 
		}
		
		sqlTblReparte.append(" AND MEC.LANCAMENTO_ID is not null ");
		
		// sqlTblReparte.append(" AND MEC.FORMA_COMERCIALIZACAO <> 'CONTA_FIRME' ");
		
		sqlTblReparte.append(" GROUP BY MEC.PRODUTO_EDICAO_ID                                                                           ");
		
		
		final StringBuilder sql = new StringBuilder();

		sql.append(" FROM CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC                                                               ");
		sql.append(" INNER JOIN BOX ON (CCEC.BOX_ID = BOX.ID) 																   ");
		sql.append(" INNER JOIN CONFERENCIA_ENCALHE ON (CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID)    ");
		sql.append(" INNER JOIN PRODUTO_EDICAO ON (PRODUTO_EDICAO.ID = CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID)                  ");
		sql.append(" INNER JOIN PRODUTO ON (PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID)                                            ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR ON (PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID)                             ");
		sql.append(" INNER JOIN FORNECEDOR ON (PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID)                             ");
		sql.append(" INNER JOIN PESSOA ON (PESSOA.ID = FORNECEDOR.JURIDICA_ID)                                                 ");

		if(filtro.getIdCota() == null) {
			
			sql.append(" LEFT JOIN DESCONTO_LOGISTICA ON (                                   ");
			sql.append(" CASE WHEN PRODUTO_EDICAO.DESCONTO_LOGISTICA_ID IS NOT NULL          ");
			sql.append(" THEN (DESCONTO_LOGISTICA.ID = PRODUTO_EDICAO.DESCONTO_LOGISTICA_ID) ");
			sql.append(" ELSE DESCONTO_LOGISTICA.ID = PRODUTO.DESCONTO_LOGISTICA_ID END )    ");

		}

		sql.append(" INNER JOIN (  ");
		sql.append( sqlTblPrecoVenda );
		sql.append(" ) AS TBL_PRECO_VENDA ON (TBL_PRECO_VENDA.PRODUTO_EDICAO_ID = PRODUTO_EDICAO.ID) ");

		sql.append(" INNER JOIN ( ");
		sql.append(	sqlTblReparte );
		sql.append(" ) AS TBL_REPARTE ON (TBL_REPARTE.PRODUTO_EDICAO_ID = PRODUTO_EDICAO.ID) ");

		sql.append(" WHERE CCEC.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal	");
		
		if(filtro.getIdCota() != null) {
	    	sql.append(" AND CCEC.COTA_ID = :idCota  ");
	    }
		
		if(filtro.getIdBoxEncalhe() != null) {
	    	sql.append(" AND BOX.ID = :boxEncalhe  ");
	    }
	    
		if(filtro.getCodigoProduto() != null) {
	    	sql.append(" AND PRODUTO.CODIGO = :codigoProduto  ");
	    }
		
		if(filtro.getIdProdutoEdicao() != null) {
	    	sql.append(" AND PRODUTO_EDICAO.ID = :idProdutoEdicao ");
	    }
		
	    if(filtro.getIdFornecedor() != null) {
	    	sql.append(" AND FORNECEDOR.ID = :idFornecedor ");
	    }
	    
		sql.append(" GROUP BY ");
		
		sql.append(" CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID,     ");
		
		sql.append(" CONFERENCIA_ENCALHE.DIA_RECOLHIMENTO 		");
		
		return sql;
	}    
    
    @SuppressWarnings("unchecked")
	public List<ConsultaEncalheDTO> obterListaConsultaEncalhe(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = obterQueryListaConsultaEncalhe(filtro, false);
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (filtro.getOrdenacaoColuna() != null) {
            
            sql.append(" order by ");
            
            String orderByColumn = "";
            
            switch (filtro.getOrdenacaoColuna()) {
            
            case CODIGO_PRODUTO:
                orderByColumn = " codigoProduto ";
                break;
            case NOME_PRODUTO:
                orderByColumn = " nomeProduto ";
                break;
            case NUMERO_EDICAO:
                orderByColumn = " numeroEdicao ";
                break;
            case PRECO_CAPA:
                orderByColumn = " precoVenda ";
                break;
            case PRECO_COM_DESCONTO:
                orderByColumn = " precoComDesconto ";
                break;
            case REPARTE:
                orderByColumn = " reparte ";
                break;
            case ENCALHE:
                orderByColumn = " encalhe ";
                break;
            case FORNECEDOR:
                orderByColumn = " fornecedor ";
                break;
            case VALOR:
                orderByColumn = " valor ";
                break;
            case VALOR_COM_DESCONTO:
                orderByColumn = " valorComDesconto ";
                break;
            case RECOLHIMENTO:
                orderByColumn = " diaRecolhimento ";
                break;
            default:
                break;
            }
            
            sql.append(orderByColumn).append(", nomeProduto ");
            
            if (paginacao.getOrdenacao() != null) {
                sql.append(paginacao.getOrdenacao().toString());
            }
        } else {
            sql.append(" order by nomeProduto ");
        }
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
        if(filtro.getIdCota() != null) {
        	parameters.put("idCota", filtro.getIdCota());
        } else {
        	parameters.put("origemInterface", Origem.INTERFACE.name());
        }
        
        if(filtro.getIdFornecedor() != null) {
            parameters.put("idFornecedor", filtro.getIdFornecedor());
        }
        
        if(filtro.getCodigoProduto() != null) {
        	parameters.put("codigoProduto", filtro.getCodigoProduto());
        }
        
        if(filtro.getIdProdutoEdicao() != null) {
            parameters.put("idProdutoEdicao", filtro.getIdProdutoEdicao());
        }
        
        if(filtro.getIdBoxEncalhe() != null) {
            parameters.put("boxEncalhe", filtro.getIdBoxEncalhe());
        }
        
        if(filtro.getIdBox() != null) {
            parameters.put("box", filtro.getIdBox());
        }
        
        if(filtro.getIdRota() != null) {
            parameters.put("rota", filtro.getIdRota());
        }
        
        if(filtro.getIdRoteiro() != null) {
            parameters.put("roteiro", filtro.getIdRoteiro());
        }
        
        parameters.put("grupoMovimentoEstoqueEncalhe", GrupoMovimentoEstoque.ENVIO_ENCALHE.name());
        parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        parameters.put("isPostergado", false);
        parameters.put("tipoVendaProduto",TipoVendaEncalhe.ENCALHE.name());
        
        if (filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            sql.append(" limit :posicaoInicial, :posicaoFinal");
            parameters.put("posicaoInicial",filtro.getPaginacao().getPosicaoInicial());
            parameters.put("posicaoFinal",filtro.getPaginacao().getQtdResultadosPorPagina());
            
        }
        
        @SuppressWarnings("rawtypes")
        final RowMapper cotaRowMapper = new RowMapper() {
            
            @Override
            public Object mapRow(final ResultSet rs, final int arg1) throws SQLException {
                
                final ConsultaEncalheDTO dto = new ConsultaEncalheDTO();
                dto.setCodigoProduto(rs.getString("codigoProduto"));
                dto.setNomeProduto(rs.getString("nomeProduto"));
                dto.setIdProdutoEdicao(rs.getLong("idProdutoEdicao"));
                dto.setNumeroEdicao(rs.getLong("numeroEdicao"));
                dto.setPrecoVenda(rs.getBigDecimal("precoVenda"));
                dto.setPrecoComDesconto(rs.getBigDecimal("precoComDesconto"));
                dto.setValor(rs.getBigDecimal("valor"));
                dto.setValorComDesconto(rs.getBigDecimal("valorComDesconto"));
                dto.setReparte(rs.getBigDecimal("reparte"));
                dto.setEncalhe(rs.getBigDecimal("encalhe"));
                dto.setIdFornecedor(rs.getLong("idFornecedor"));
                dto.setFornecedor(rs.getString("fornecedor"));
                dto.setDataMovimento(rs.getDate("dataMovimento"));
                dto.setIndPossuiObservacaoConferenciaEncalhe(rs.getBoolean("indObservacaoConferenciaEncalhe"));
                dto.setRecolhimento(rs.getObject("diaRecolhimento") != null ? (Long) rs.getObject("diaRecolhimento") : null);
                
                return dto;
            }
        };
        
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
        
    }
    
    
    
   @SuppressWarnings("unchecked")
	public List<ConsultaEncalheDTO> obterListaConsultaReparte(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = obterQueryListaConsultaReparte(filtro, false);
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        
           sql.append(" order by cota_id ");
        
       
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
       
        if(filtro.getIdCota() != null) {
        	parameters.put("idCota", filtro.getIdCota());
        } 
      
        if(filtro.getIdFornecedor() != null) {
            parameters.put("idFornecedor", filtro.getIdFornecedor());
        }
        
        
        if(filtro.getIdProdutoEdicao() != null) {
            parameters.put("idProdutoEdicao", filtro.getIdProdutoEdicao());
        }
        
        if(filtro.getIdBox() != null) {
            parameters.put("idBox", filtro.getIdBox());
        }
         parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        parameters.put("tipoVendaProduto",TipoVendaEncalhe.ENCALHE.name());
        
        @SuppressWarnings("rawtypes")
        final RowMapper cotaRowMapper = new RowMapper() {
            
            @Override
            public Object mapRow(final ResultSet rs, final int arg1) throws SQLException {
                
                final ConsultaEncalheDTO dto = new ConsultaEncalheDTO();
                dto.setEncalhe(rs.getBigDecimal("encalhe"));
                dto.setReparte(rs.getBigDecimal("reparte"));
                dto.setIdCota(rs.getLong("idCota"));
                dto.setNomeCota(rs.getString("nomeCota"));
                dto.setIdBox(rs.getLong("idBox"));
                dto.setNomeBox(rs.getString("nomeBox"));
                     
                return dto;
            }
        };
        
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
        
    }
    
    private String montarSqlEncalheDetalhe(final FiltroConsultaEncalheDetalheDTO filtro){
    	
    	final StringBuilder sql = new StringBuilder();
        
        sql.append("	select distinct	");
        
        sql.append("	COTA.NUMERO_COTA						  as numeroCota,  ");
        sql.append("	coalesce(if(PESSOA.TIPO='F',PESSOA.NOME,PESSOA.RAZAO_SOCIAL),PESSOA.NOME_FANTASIA) as nomeCota,    ");
        sql.append(" 	CONFERENCIA_ENCALHE.OBSERVACAO			  as observacao  ");
        
        montarSqlFromDetalhesEncalhe(sql);
        
        sql.append("	where CONFERENCIA_ENCALHE.OBSERVACAO IS NOT NULL	");
        
        montarParametrosDetalhesEncalhe(filtro, sql);
        
        return sql.toString();
    }
    
    private String montarSqlEncalheDetalheJuramentado(final FiltroConsultaEncalheDetalheDTO filtro){
    	
    	final StringBuilder sql = new StringBuilder();
        
        sql.append("	select distinct	");
        
        sql.append("	COTA.NUMERO_COTA						  as numeroCota,  ");
        sql.append("	coalesce(IF(PESSOA.TIPO='F',PESSOA.NOME,PESSOA.RAZAO_SOCIAL),PESSOA.NOME_FANTASIA) as nomeCota,    ");
        sql.append(" 	concat(format(CONFERENCIA_ENCALHE.QTDE,0),' exes. Juramentado') as observacao  ");
        
        montarSqlFromDetalhesEncalhe(sql);
        
        sql.append("	where CONFERENCIA_ENCALHE.JURAMENTADA = true	");
        
        montarParametrosDetalhesEncalhe(filtro, sql);
        
        return sql.toString();
    }

	private void montarParametrosDetalhesEncalhe(
			final FiltroConsultaEncalheDetalheDTO filtro,
			final StringBuilder sql) {
		
		if(filtro.getDataMovimento() != null) {
            sql.append("	AND MOVIMENTO_ESTOQUE_COTA.DATA = :dataMovimento ");
        }
        
        if(filtro.getDataRecolhimento() != null) {
            sql.append("	AND CHAMADA_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento ");
        }
        
        if(filtro.getNumeroCota()!=null) {
            sql.append(" AND COTA.NUMERO_COTA = :numeroCota  ");
        }
        
        if(filtro.getIdFornecedor() != null) {
            sql.append(" AND PRODUTO_FORNECEDOR.FORNECEDORES_ID = :idFornecedor ");
        }
        
        sql.append(" AND PRODUTO_EDICAO.ID = :idProdutoEdicao ");
	}

	private void montarSqlFromDetalhesEncalhe(final StringBuilder sql) {
		
		sql.append("	from	");
        
        sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
        sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");
        
        sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
        
        sql.append("	inner join COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
        
        sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
        sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
        
        sql.append("	inner join CHAMADA_ENCALHE on ");
        sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
        
        sql.append("	inner join PRODUTO_EDICAO on ");
        sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
        
        sql.append("	inner join PRODUTO_FORNECEDOR on ");
        sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO_EDICAO.PRODUTO_ID ) ");
        
        sql.append("	inner join PESSOA on                   	");
        sql.append("	( PESSOA.ID = COTA.PESSOA_ID )	");
	}
    
    @Override
    @SuppressWarnings("unchecked")
    public List<ConsultaEncalheDetalheDTO> obterListaConsultaEncalheDetalhe(final FiltroConsultaEncalheDetalheDTO filtro) {
     	
    	final StringBuilder sql = new StringBuilder();
        
    	sql.append(this.montarSqlEncalheDetalhe(filtro));
        
        sql.append(" union ");
        
        sql.append(this.montarSqlEncalheDetalheJuramentado(filtro));
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (filtro.getOrdenacaoColunaDetalhe() != null) {
            
            sql.append(" order by ");
            
            String orderByColumn = "";
            
            switch (filtro.getOrdenacaoColunaDetalhe()) {
            
            case NUMERO_COTA:
                orderByColumn = " numeroCota ";
                break;
            case NOME_COTA:
                orderByColumn = " nomeCota ";
                break;
            case OBSERVACAO:
                orderByColumn = " observacao ";
                break;
            default:
            	 orderByColumn = " numeroCota ";
            	break;
            }
            
            sql.append(orderByColumn);
            
            if (paginacao.getOrdenacao() != null) {
                
                sql.append(paginacao.getOrdenacao().toString());
                
            }
            
        }
        else{
        
        	  sql.append(" order by observacao,nomeCota,numeroCota ");
        }
        
        final SQLQuery sqlquery = getSession().createSQLQuery(sql.toString())
                
                .addScalar("numeroCota")
                .addScalar("nomeCota")
                .addScalar("observacao");
        
        sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheDetalheDTO.class));
        
        this.atribuirValoresAosParametrosEncalheDetalhe(filtro, sqlquery);
        
        if(filtro.getPaginacao()!=null) {
            
            if(filtro.getPaginacao().getPosicaoInicial()!=null) {
                sqlquery.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
            }
            
            if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
                sqlquery.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
            }
            
        }
        
        return sqlquery.list();
        
    }
    
    @Override
    public Integer obterQtdeConsultaEncalheDetalhe(final FiltroConsultaEncalheDetalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select count(1) from ( ");
        
        sql.append(this.montarSqlEncalheDetalhe(filtro));
        
        sql.append(" union ");
        
        sql.append(this.montarSqlEncalheDetalheJuramentado(filtro));
        
        sql.append(" ) as consultaEncalheDetalhe ");
        
        final SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
        
        this.atribuirValoresAosParametrosEncalheDetalhe(filtro, sqlquery);
        
        final BigInteger qtde = (BigInteger) sqlquery.uniqueResult();
        
        return qtde == null ? 0 : qtde.intValue();
        
    }

	private void atribuirValoresAosParametrosEncalheDetalhe(
			final FiltroConsultaEncalheDetalheDTO filtro,
			final SQLQuery sqlquery) {
		
		if(filtro.getNumeroCota() != null) {
            sqlquery.setParameter("numeroCota", filtro.getNumeroCota());
        }
        
        if(filtro.getIdFornecedor() != null) {
            sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
        }
        
        sqlquery.setParameter("idProdutoEdicao", filtro.getIdProdutoEdicao());
        
        if(filtro.getDataMovimento() != null) {
            sqlquery.setParameter("dataMovimento", filtro.getDataMovimento());
        }
        
        if(filtro.getDataRecolhimento() != null) {
            sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());
        }
	}
    
    @Override
    public ConsultaEncalheRodapeDTO obterValoresTotais(final FiltroConsultaEncalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append("	select ");
        
        sql.append("	coalesce(sum( ESTOQUE_PRODUTO_COTA.QTDE_RECEBIDA * ");
        
        sql.append("    (COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO,PRODUTO_EDICAO.PRECO_VENDA)) ), 0) as valorReparte,");
        
        sql.append("	coalesce(sum( ESTOQUE_PRODUTO_COTA.QTDE_DEVOLVIDA * ");
        
        sql.append("    (COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO,PRODUTO_EDICAO.PRECO_VENDA)) ), 0) as valorEncalhe ");
        
        sql.append("	from	");
        
        sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
        sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");
        
        sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
        
        sql.append("	inner join COTA on                                         ");
        sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
        
        sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
        sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
        
        sql.append("	inner join CHAMADA_ENCALHE on ");
        sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
        
        sql.append("	inner join PRODUTO_EDICAO on ");
        sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
        
        sql.append("	inner join PRODUTO_FORNECEDOR on ");
        sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO_EDICAO.PRODUTO_ID ) ");
        
        sql.append("	where	");
        
        sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
        
        if(filtro.getIdCota()!=null) {
            sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
        }
        
        if(filtro.getIdFornecedor() != null) {
            sql.append(" and PRODUTO_FORNECEDOR.FORNECEDORES_ID =  :idFornecedor ");
        }
        
        final SQLQuery sqlquery = getSession().createSQLQuery(sql.toString())
                
                .addScalar("valorReparte")
                .addScalar("valorEncalhe");
        
        sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheRodapeDTO.class));
        
        if(filtro.getIdCota()!=null) {
            sqlquery.setParameter("idCota", filtro.getIdCota());
        }
        
        if(filtro.getIdFornecedor() != null) {
            sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
        }
        
        sqlquery.setParameter("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
        
        sqlquery.setParameter("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
        
        return (ConsultaEncalheRodapeDTO) sqlquery.uniqueResult();
        
    }
    
	                                                    /**
     * Obtém hql para pesquisa de ContagemDevolucao.
     * 
     * @param filtro
     * @param indBuscaTotalParcial
     * @param indBuscaQtd
     * 
     * @return String - hql
     */
    private String getConsultaListaContagemDevolucao(final FiltroDigitacaoContagemDevolucaoDTO filtro, final boolean indBuscaTotalParcial, final boolean indBuscaQtd) {
        
        final StringBuilder sql = new StringBuilder("");
        
        if (indBuscaQtd){
            
            sql.append("select count(quantidadeTotal) as quantidadeTotal, sum(valorTotalGeral) as valorTotalGeral from (");
        } else {
            
            sql.append(" SELECT ");
        }
        
        final StringBuilder qtdDevolucaoSubQuery = new StringBuilder();
        qtdDevolucaoSubQuery.append(" ( SELECT SUM(  		");
        qtdDevolucaoSubQuery.append(" COALESCE(ESTOQUE_PROD.QTDE, 0) +	");
        qtdDevolucaoSubQuery.append(" COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR, 0) + 		");
        qtdDevolucaoSubQuery.append(" COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_ENCALHE, 0))	");
        qtdDevolucaoSubQuery.append(" FROM ESTOQUE_PRODUTO ESTOQUE_PROD ");
        qtdDevolucaoSubQuery.append(" WHERE ESTOQUE_PROD.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ) ");
        
        final StringBuilder qtdNotaSubQuery = new StringBuilder();
        qtdNotaSubQuery.append(" ( SELECT SUM( COALESCE(PARCIAL.QTDE, 0)) ");
        qtdNotaSubQuery.append(" FROM CONFERENCIA_ENC_PARCIAL PARCIAL ");
        qtdNotaSubQuery.append(" WHERE PROD_EDICAO.ID = PARCIAL.PRODUTOEDICAO_ID AND  ");
        qtdNotaSubQuery.append(" PARCIAL.STATUS_APROVACAO = :statusAprovacao  ");
        qtdNotaSubQuery.append(" ) ");
        
        if(indBuscaQtd) {
            
            sql.append(" SELECT COUNT(PROD_EDICAO.ID) as quantidadeTotal, ");
            sql.append(" SUM(PROD_EDICAO.PRECO_VENDA * ");
            
            sql.append(" CASE WHEN ( ");
            sql.append(qtdNotaSubQuery).append(" = 0 ) ");
            sql.append(" THEN ").append(qtdNotaSubQuery);
            sql.append(" ELSE ");
            sql.append(qtdDevolucaoSubQuery).append(" END ) AS valorTotalGeral ");
            
        } else {
            
            sql.append(" PROD_EDICAO.ID, ");
            
            sql.append(qtdDevolucaoSubQuery);
            sql.append(" AS qtdDevolucao, ");
            
            if(indBuscaTotalParcial) {
                
                sql.append(qtdNotaSubQuery);
                sql.append(" AS qtdNota, ");
                
            } else {
                
                sql.append(" NULL AS QTDE_PARCIAL, ");
            }
            
            sql.append(" PROD.CODIGO as codigoProduto,  				");
            sql.append(" PROD.NOME as nomeProduto, 						");
            sql.append(" PROD_EDICAO.NUMERO_EDICAO as numeroEdicao, 	");
            sql.append(" PROD_EDICAO.PRECO_VENDA as precoVenda, 		");
            
            sql.append(" case when PROD_EDICAO.origem ='MANUAL' then ");
            sql.append(" case when PROD_EDICAO.desconto is not null then PROD_EDICAO.desconto  ");
            sql.append(" else PROD.desconto end  ");
            sql.append(" else (  ");
            sql.append(" case when desconto_logistica_pe.ID is not null then  ");			  		
            sql.append(" desconto_logistica_pe.PERCENTUAL_DESCONTO   ");          	  
            sql.append(" else	 ");											 		 	 		
            sql.append(" case when desconto_logistica_prod.ID is not null then ");		 
            sql.append(" desconto_logistica_prod.PERCENTUAL_DESCONTO   ");    	         
            sql.append(" else 0 end         	   end) end as desconto,  "); 
            
            /*
            sql.append(" (case when desconto_logistica_pe.ID is not null then 			");
            sql.append("  		desconto_logistica_pe.PERCENTUAL_DESCONTO            	");
            sql.append("  else												 		 	");
            sql.append(" 		case when desconto_logistica_prod.ID is not null then 	");
            sql.append(" 			 desconto_logistica_prod.PERCENTUAL_DESCONTO      	");
            sql.append("         else 0 end         	");
            sql.append("   end) as desconto,           	");
            */
            
            sql.append(" CE.DATA_RECOLHIMENTO as dataMovimento ");
        }
        
        sql.append(" FROM ");
        
        sql.append(" (	              ");
        
        sql.append(" SELECT CE.PRODUTO_EDICAO_ID AS CE_PRODUTO_EDICAO_ID		");
        sql.append(" FROM CHAMADA_ENCALHE CE		");
        sql.append(" WHERE CE.DATA_RECOLHIMENTO BETWEEN :dataInicial and :dataFinal		");
        sql.append(" GROUP BY CE.PRODUTO_EDICAO_ID		");
        
        sql.append(" ) AS RECOLHIMENTOS	              ");
        
        sql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( 					");
        sql.append(" 	RECOLHIMENTOS.CE_PRODUTO_EDICAO_ID = PROD_EDICAO.ID 	");
        sql.append(" ) ");
        
        sql.append("  LEFT JOIN  ");
        sql.append("  	desconto_logistica desconto_logistica_pe  ");
        sql.append("  	on ( PROD_EDICAO.DESCONTO_LOGISTICA_ID = desconto_logistica_pe.id ) ");
        
        sql.append(" INNER JOIN PRODUTO PROD ON (			");
        sql.append(" 	PROD_EDICAO.PRODUTO_ID = PROD.ID	");
        sql.append(" ) 	");
        
        sql.append("  LEFT JOIN  ");
        sql.append("  	desconto_logistica desconto_logistica_prod  ");
        sql.append("  	on ( PROD.DESCONTO_LOGISTICA_ID = desconto_logistica_prod.id ) ");
        
        sql.append(" LEFT JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (	");
        sql.append(" 	PROD.ID = PROD_FORNEC.PRODUTO_ID 			");
        sql.append(" ) ");
        
        sql.append(" INNER JOIN CHAMADA_ENCALHE CE ON (");
        sql.append("	PROD_EDICAO.ID = CE.PRODUTO_EDICAO_ID ");
        sql.append(" ) ");
        
        sql.append(" JOIN ESTOQUE_PRODUTO EP ON (");
        sql.append(" 	EP.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
        
        sql.append(") ");
        
        sql.append(" WHERE (EP.QTDE != 0 ");
        sql.append(" OR EP.QTDE_SUPLEMENTAR != 0 ");
        sql.append(" OR EP.QTDE_DEVOLUCAO_ENCALHE != 0) ");
        
        if(filtro.getIdFornecedor() != null){
        	sql.append(" AND PROD_FORNEC.FORNECEDORES_ID IN ( :idFornecedor )");
        } else {
        	sql.append(" AND PROD_FORNEC.FORNECEDORES_ID NOT IN ( ")
        	.append(" SELECT ID FROM FORNECEDOR forn ")
        	.append(" WHERE forn.SITUACAO_CADASTRO = 'ATIVO' ")
        	.append(" AND forn.FORNECEDOR_UNIFICADOR_ID IS NOT NULL ")
        	.append(" UNION ")
        	.append(" SELECT FORNECEDOR_UNIFICADOR_ID FROM FORNECEDOR forn ")
        	.append(" WHERE forn.SITUACAO_CADASTRO = 'ATIVO' ")
        	.append(" AND FORNECEDOR_UNIFICADOR_ID IS NOT NULL ) ");

        }
           
        sql.append(" GROUP BY PROD_EDICAO.ID ");

        sql.append(" HAVING ("+qtdDevolucaoSubQuery+" > 0) ");

        if (indBuscaQtd){
            sql.append(") as temp ");
        }
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (!indBuscaQtd && filtro.getOrdenacaoColuna() != null) {
            
            sql.append(" order by ");
            
            String orderByColumn = "";
            
            switch (filtro.getOrdenacaoColuna()) {
            
            case CODIGO_PRODUTO:
                orderByColumn = "codigoProduto ";
                break;
            case NOME_PRODUTO:
                orderByColumn = "nomeProduto ";
                break;
            case NUMERO_EDICAO:
                orderByColumn = "numeroEdicao ";
                break;
            case PRECO_CAPA:
                orderByColumn = "precoVenda ";
                break;
            case QTD_DEVOLUCAO:
                orderByColumn = "qtdDevolucao ";
                break;
            case QTD_NOTA:
                orderByColumn = "qtdNota ";
                break;
                
            default:
                break;
            }
            
            sql.append(orderByColumn);
            
            if (paginacao != null && paginacao.getOrdenacao() != null) {
                
                sql.append(paginacao.getOrdenacao().toString());
                
            }
            
        }
        
        return sql.toString();
        
    }
    
	                                                    /**
     * Carrega os parâmetros da pesquisa de ContagemDevolucao e retorna o objeto
     * Query.
     * 
     * @param hql
     * @param filtro
     * @param tipoMovimentoEstoque
     * @param indBuscaTotalParcial
     * @param indBuscaQtd
     * 
     * @return Query
     */
    private Query criarQueryComParametrosObterListaContagemDevolucao(final String hql, final FiltroDigitacaoContagemDevolucaoDTO filtro, final boolean indBuscaTotalParcial, final boolean indBuscaQtd) {
        
        Query query = null;
        
        if(indBuscaQtd) {
            
            query = getSession().createSQLQuery(hql)
                    .addScalar("quantidadeTotal", StandardBasicTypes.INTEGER)
                    .addScalar("valorTotalGeral", StandardBasicTypes.BIG_DECIMAL);
            
        } else {
            
            query = getSession().createSQLQuery(hql)
                    .addScalar("codigoProduto", StandardBasicTypes.STRING)
                    .addScalar("nomeProduto", StandardBasicTypes.STRING)
                    .addScalar("numeroEdicao", StandardBasicTypes.LONG)
                    .addScalar("precoVenda", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("desconto", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("qtdDevolucao", StandardBasicTypes.BIG_INTEGER)
                    .addScalar("dataMovimento", StandardBasicTypes.DATE);
            
            
            if(indBuscaTotalParcial) {
                
                ((SQLQuery) query).addScalar("qtdNota", StandardBasicTypes.BIG_INTEGER);
            }
            
            query.setResultTransformer(Transformers.aliasToBean(ContagemDevolucaoDTO.class));
        }
        
        query.setParameter("dataInicial", filtro.getPeriodo().getDe());
        
        query.setParameter("dataFinal", filtro.getPeriodo().getAte());
        
        if (indBuscaQtd || indBuscaTotalParcial) {
            
            query.setParameter("statusAprovacao", StatusAprovacao.PENDENTE.name());
        }
       
        if(filtro.getIdFornecedor() != null){
        	query.setParameterList("idFornecedor", filtro.getFornecedores());
        }
        
        return query;
        
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, boolean)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
            final FiltroDigitacaoContagemDevolucaoDTO filtro,
            final boolean indBuscaTotalParcial) {
        
        final String hql = getConsultaListaContagemDevolucao(filtro, indBuscaTotalParcial, false);
        
        final Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, indBuscaTotalParcial, false);
        
        return query.list();
        
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterValorTotalGeralContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, br.com.abril.nds.model.estoque.TipoMovimentoEstoque)
     */
    @Override
    public BigDecimal obterValorTotalGeralContagemDevolucao(
            final FiltroDigitacaoContagemDevolucaoDTO filtro) {
        
        final StringBuilder sql = new StringBuilder("");
        
        sql.append(" SELECT ")
        
        .append(" SUM(MOV_ESTOQUE_COTA.QTDE * PRODUTO_EDICAO.PRECO_VENDA) ")
        
        .append(" FROM ")
        
        .append(" MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA ")
        
        .append(" INNER JOIN CONFERENCIA_ENCALHE ON ( ")
        .append("	CONFERENCIA_ENCALHE.movimento_estoque_cota_id = MOV_ESTOQUE_COTA.ID ")
        .append(" ) ")
        
        .append(" INNER JOIN PRODUTO_EDICAO ON (	")
        .append("	PRODUTO_EDICAO.ID = MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID ")
        .append(" ) ")
        
        .append(" INNER JOIN PRODUTO PROD ON (			")
        .append(" 	PRODUTO_EDICAO.PRODUTO_ID = PROD.ID	")
        .append(" ) 	")
        
        .append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (	")
        .append(" 	PROD.ID = PROD_FORNEC.PRODUTO_ID 				")
        .append(" ) ")
        
        .append(" LEFT JOIN CONTROLE_CONTAGEM_DEVOLUCAO ON ( 		")
        .append(" 	CONTROLE_CONTAGEM_DEVOLUCAO.PRODUTO_EDICAO_ID  	=  MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID AND ")
        .append("   CONTROLE_CONTAGEM_DEVOLUCAO.DATA     			=  MOV_ESTOQUE_COTA.DATA AND ")
        .append("   CONTROLE_CONTAGEM_DEVOLUCAO.STATUS = :statusOperacao ) ")
        
        .append(" WHERE ")
        
        .append(" MOV_ESTOQUE_COTA.DATA BETWEEN :dataInicial AND :dataFinal AND ")
        
        .append(" CONTROLE_CONTAGEM_DEVOLUCAO.ID IS NULL  ")
        
        .append(" AND MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID IN ( ")
        
        .append("	SELECT ")
        .append("	ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID  ")
        
        .append("	from ")
        .append("	ITEM_CHAMADA_ENCALHE_FORNECEDOR ITEM_CH_ENC_FORNECEDOR ")
        
        .append("	where ");
        
        sql.append(" PROD_FORNEC.FORNECEDORES_ID = :idFornecedor AND ");
        
        sql.append(" ITEM_CH_ENC_FORNECEDOR.DATA_RECOLHIMENTO BETWEEN :dataInicial AND :dataFinal ")
        
        .append("	GROUP BY ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID   ) ");
        
        final Query query = getSession().createSQLQuery(sql.toString());
        
        query.setParameter("dataInicial", filtro.getPeriodo().getDe());
        
        query.setParameter("dataFinal", filtro.getPeriodo().getAte());
        
        query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
        
        query.setParameter("idFornecedor", filtro.getIdFornecedor());
        
        final BigDecimal valor = (BigDecimal) query.uniqueResult();
        
        return valor == null ? BigDecimal.ZERO : valor;
        
    }
    
    @Override
    public ContagemDevolucaoAgregationValuesDTO obterQuantidadeContagemDevolucao(final FiltroDigitacaoContagemDevolucaoDTO filtro) {
        
        final String hql = getConsultaListaContagemDevolucao(filtro, false, true);
        
        final Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, false, true);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ContagemDevolucaoAgregationValuesDTO.class));
        
        return (ContagemDevolucaoAgregationValuesDTO) query.uniqueResult();
    }
    
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterMovimentoCotaPorTipoMovimento(java.util.Date, java.lang.Long, br.com.abril.nds.model.estoque.GrupoMovimentoEstoque)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(final Date data, final Long idCota, final GrupoMovimentoEstoque grupoMovimentoEstoque){
        
        final StringBuilder hql = new StringBuilder("");
        
        hql.append(" from MovimentoEstoqueCota movimento");
        
        hql.append(" where movimento.cota.id = :idCota ");
        
        hql.append(" and movimento.data = :data ");
        
        hql.append(" and movimento.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("data", data);
        
        query.setParameter("idCota", idCota);
        
        query.setParameter("grupoMovimentoEstoque", grupoMovimentoEstoque);
        
        return query.list();
        
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdMovimentoCotaPorTipoMovimento(java.util.Date, java.lang.Long, br.com.abril.nds.model.estoque.GrupoMovimentoEstoque)
     */
    @Override
    public Map<Long, BigInteger> obterQtdMovimentoCotaPorTipoMovimento(final Intervalo<Date> periodo,
            final Long idCota,
            final String... gruposMovimentoEstoque){
       
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select sum(rs.quantidade) as quantidade, ");
        
        hql.append(" rs.produtoEdicao as produtoEdicao  ");
        
        hql.append(" from ( ");
        
        hql.append(" SELECT "); 
        
        hql.append("	SUM(CASE WHEN tipoMovimento.OPERACAO_ESTOQUE=:operacaoEntrada ");
        
        hql.append("		THEN movimentoEstoque.QTDE "); 
        
        hql.append("		ELSE -movimentoEstoque.QTDE END) AS quantidade, "); 
        
        hql.append("	produtoEdicao.ID AS produtoEdicao ");

        hql.append(" FROM MOVIMENTO_ESTOQUE_COTA movimentoEstoque ");
	
        hql.append(" INNER JOIN TIPO_MOVIMENTO tipoMovimento ON movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
	
        hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
	
        hql.append(" LEFT OUTER JOIN LANCAMENTO lancamento ON movimentoEstoque.LANCAMENTO_ID=lancamento.ID ");

        hql.append(" WHERE  movimentoEstoque.COTA_ID= :idCota ");
	    
        hql.append(" AND (movimentoEstoque.DATA BETWEEN :inicio  AND :fim  ) "); 
	    
        hql.append(" AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimento) ");
        
        hql.append(" GROUP BY produtoEdicao.ID ");
        
        hql.append("  union ");
        
        hql.append(" SELECT "); 
        
        hql.append("  (case when (movimentoEstoque.DATA BETWEEN :inicio AND :fim ) then ");
        
        hql.append("	0 else SUM(CASE WHEN tipoMovimento.OPERACAO_ESTOQUE=:operacaoEntrada "); 
        
        hql.append("		THEN movimentoEstoque.QTDE "); 
        
        hql.append("		ELSE -movimentoEstoque.QTDE END)  end) AS quantidade, "); 
        
        hql.append("	produtoEdicao.ID AS produtoEdicao ");

        hql.append(" FROM MOVIMENTO_ESTOQUE_COTA movimentoEstoque ");
	
        hql.append(" INNER JOIN TIPO_MOVIMENTO tipoMovimento ON movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
	
        hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
	
        hql.append(" LEFT OUTER JOIN LANCAMENTO lancamento ON movimentoEstoque.LANCAMENTO_ID=lancamento.ID ");

        hql.append(" WHERE  movimentoEstoque.COTA_ID= :idCota ");
	    
        hql.append(" AND ( lancamento.DATA_LCTO_DISTRIBUIDOR BETWEEN :inicio  AND :fim ) "); 
	    
        hql.append(" AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimento) ");
        
        hql.append(" GROUP BY produtoEdicao.ID ");
 
        hql.append("  )rs ");
		
        hql.append(" GROUP BY rs.produtoEdicao  ");
 
        final Query query = getSession().createSQLQuery(hql.toString());
        
        query.setParameter("inicio", periodo.getDe());
        
        query.setParameter("fim", periodo.getAte());
        
        query.setParameter("idCota", idCota);
        
        query.setParameter("operacaoEntrada", OperacaoEstoque.ENTRADA.name());
        
        query.setParameterList("gruposMovimento", gruposMovimentoEstoque);
        
        @SuppressWarnings("unchecked")
        final
        List<Object[]> listaResultados = query.list();
        
        final Map<Long, BigInteger> mapResult = new HashMap<>();
        
        for (final Object[] item : listaResultados) {
            
            final BigInteger quantidade = BigInteger.valueOf(((Number) item[0]).intValue());
            
            final Long id = Long.valueOf(item[1].toString());
            
            mapResult.put(id,quantidade);
        }
        
        return mapResult;
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCota> obterMovimentoCotaLancamentoPorTipoMovimento(final Date dataLancamento,
            final Long idCota,
            final List<GrupoMovimentoEstoque> gruposMovimentoEstoque) {
        
        final StringBuilder hql = new StringBuilder("");
        
        hql.append(" select movimento from MovimentoEstoqueCota movimento ");
        
        hql.append(" inner join movimento.lancamento lancamento ");
        
        hql.append(" where movimento.cota.id = :idCota ");
        
     //   hql.append(" and movimento.data = :data ");
        
        hql.append(" and movimento.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ");
        
        hql.append(" and lancamento.dataLancamentoDistribuidor = :data ");
        
        hql.append(" and movimento.tipoMovimento.grupoMovimentoEstoque in (:gruposMovimentoEstoque) ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("data", dataLancamento);
        
        query.setParameter("idCota", idCota);
        
        query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        
        query.setParameterList("gruposMovimentoEstoque", gruposMovimentoEstoque);
        
        return query.list();
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoEstoqueCotaDTO> obterMovimentoCotasPorTipoMovimento(final Date data, final List<Integer> numCotas, final List<GrupoMovimentoEstoque> gruposMovimentoEstoque){
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select  cota.id as idCota, ");
        
        hql.append(" 		 produtoEdicao.id as idProdEd, ");
        
        hql.append(" 		 produto.codigo as codigoProd, ");
        
        hql.append(" 		 produtoEdicao.numeroEdicao as edicaoProd, ");
        
        hql.append(" 		 produto.nomeComercial as nomeProd, ");
        
        hql.append(" 		 sum( case tipoMovimento.operacaoEstoque when 'ENTRADA' then (movimento.qtde) else (movimento.qtde * -1) end) as qtdeReparte ");
        
        hql.append(" from MovimentoEstoqueCota movimento");
        
        hql.append(" join movimento.produtoEdicao produtoEdicao ");
        
        hql.append(" join produtoEdicao.produto produto ");
        
        hql.append(" join movimento.cota cota ");
        
        hql.append(" join movimento.tipoMovimento tipoMovimento ");
        
        hql.append(" join movimento.lancamento lancamento ");
        
        hql.append(" where cota.numeroCota in (:numCotas) ");
        
   //     hql.append(" and movimento.data = :data ");
        
        hql.append(" and lancamento.dataLancamentoDistribuidor = :data ");
        
        hql.append(" and tipoMovimento.grupoMovimentoEstoque in (:gruposMovimentoEstoque) ");
        
        hql.append(" and tipoMovimento.grupoMovimentoEstoque not in (:gruposMovimentoEstoqueEstorno) ");
        
        hql.append(" and movimento.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ");
        
        hql.append(" group by produtoEdicao.id ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("data", data);
        
        query.setParameterList("numCotas", numCotas);
        
        query.setParameterList("gruposMovimentoEstoque", gruposMovimentoEstoque);
        
        query.setParameterList("gruposMovimentoEstoqueEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO));
        
        query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        
        query.setResultTransformer(Transformers.aliasToBean(MovimentoEstoqueCotaDTO.class));
        
        return query.list();
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<AbastecimentoDTO> obterDadosAbastecimento(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.ID as idBox, ");
        hql.append(" 		concat(box.CODIGO, '-', box.NOME) as box, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota, ");
        hql.append("        coalesce(if(pessoa.tipo = 'F',pessoa.NOME, pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia, '') as nomeCota, ");
        hql.append(" 		count(distinct  produtoEdicao.ID) as totalProduto, ");
        hql.append(" 		sum(estudoCota.QTDE_EFETIVA) as totalReparte, ");
        hql.append(" 		sum(estudoCota.QTDE_EFETIVA * produtoEdicao.PRECO_VENDA) as totalBox ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by box.ID ");
        
        if (filtro.getQuebraPorCota()) {
            hql.append(" , cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("idBox", StandardBasicTypes.LONG);
        query.addScalar("box", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("totalProduto", StandardBasicTypes.LONG);
        query.addScalar("totalReparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(AbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return  query.list();
        
    }
    
    private void gerarFromWhereDadosAbastecimento(final FiltroMapaAbastecimentoDTO filtro, final StringBuilder hql,
            final Map<String, Object> param, final List<String> statusLancamento) {
        
        hql.append(" from ESTUDO_COTA estudoCota ");
        hql.append("	join ESTUDO estudo ON (estudo.ID = estudoCota.ESTUDO_ID) ");
        hql.append("	join COTA cota ON (cota.ID = estudoCota.COTA_ID) ");
        hql.append("	join PRODUTO_EDICAO produtoEdicao ON (produtoEdicao.ID = estudo.PRODUTO_EDICAO_ID) ");
        hql.append("	join PRODUTO produto ON (produto.ID = produtoEdicao.PRODUTO_ID) ");
        hql.append("    join PESSOA pessoa ON (pessoa.ID = cota.PESSOA_ID) ");
        hql.append(" 	join LANCAMENTO lancamento ON (lancamento.ESTUDO_ID = estudo.ID) ");
        hql.append("    join PDV pdv ON (pdv.COTA_ID = cota.ID) ");
        hql.append("    join ROTA_PDV rotaPDV ON (rotaPDV.PDV_ID = pdv.ID) ");
        hql.append("    join ROTA rota ON (rotaPDV.ROTA_ID = rota.ID) ");
        hql.append("    join ROTEIRO roteiro ON (roteiro.ID = rota.ROTEIRO_ID) ");
        hql.append("    join ROTEIRIZACAO rtz ON (rtz.ID = roteiro.ROTEIRIZACAO_ID) ");
        
        hql.append("    join BOX box ON (box.ID = rtz.BOX_ID) ");
        
        
        if(filtro.getIdEntregador() != null || TipoConsulta.ENTREGADOR.equals(filtro.getTipoConsulta())){
            
            hql.append(" join ENTREGADOR entregador ON (entregador.ROTA_ID = rota.ID) ");
            hql.append(" join PESSOA pessoaEnt ON (entregador.PESSOA_ID = pessoaEnt.ID) ");
            hql.append(" join ROTA rotaEnt ON (rotaEnt.ID = entregador.ROTA_ID) ");
            hql.append(" join ROTEIRO roteiroEnt ON (roteiroEnt.ID = rotaEnt.ROTEIRO_ID) ");
            hql.append(" join ROTEIRIZACAO roteirizacaoEnt ON (roteiroEnt.ROTEIRIZACAO_ID = roteirizacaoEnt.ID) ");
            hql.append(" join BOX boxEnt ON (boxEnt.ID = roteirizacaoEnt.BOX_ID) ");
        }
        
        hql.append(" where lancamento.STATUS in (:status) ");
        
        hql.append(" and estudoCota.TIPO_ESTUDO = :tipoEstudo ");
        param.put("tipoEstudo", TipoEstudoCota.NORMAL.name());
        
        if(filtro.getDataDate() != null) {
            // Criado pelo Eduardo Punk Rock - Comentado para realizar a busca
            // através da data de lançamento do distribuidor e não a data de
            // movimento que foi gerada
            hql.append(" and lancamento.DATA_LCTO_DISTRIBUIDOR = :data ");
            param.put("data", filtro.getDataDate());
        }
        
        if(filtro.getBox() != null) {
            
            hql.append(" and box.ID = :box ");
            param.put("box", filtro.getBox());
          
        } else {
        	
			hql.append(" and  box.tipo_box <> 'ESPECIAL' ");
			hql.append(" and roteiro.TIPO_ROTEIRO <> 'ESPECIAL' ");
			
		}
        
        if(filtro.getRota() != null) {
            
            hql.append(" and rota.ID = :rota ");
            param.put("rota", filtro.getRota());
        }
        
        if(filtro.getRoteiro() != null) {
            
            hql.append(" and roteiro.ID = :roteiro ");
            param.put("roteiro", filtro.getRoteiro());
        }
        
        if(filtro.getCodigosProduto() != null && !filtro.getCodigosProduto().isEmpty()) {
            
            hql.append(" and produto.CODIGO in (:codigosProduto) ");
            param.put("codigosProduto", filtro.getCodigosProduto());
        }
        
        if(filtro.getNumerosEdicao() != null && !filtro.getNumerosEdicao().isEmpty()) {
            
            hql.append(" and produtoEdicao.NUMERO_EDICAO in (:numeroEdicao) ");
            param.put("numeroEdicao", filtro.getNumerosEdicao());
        }
        
        if(filtro.getCodigoCota() != null ) {
            
            hql.append(" and cota.NUMERO_COTA = :codigoCota ");
            param.put("codigoCota", filtro.getCodigoCota());
        }
        
        if(filtro.getIdEntregador() != null ) {
            
            hql.append(" and entregador.ID = :idEntregador ");
            param.put("idEntregador", filtro.getIdEntregador());
        }
        
        statusLancamento.add(StatusLancamento.BALANCEADO.name());
        statusLancamento.add(StatusLancamento.EXPEDIDO.name());
        
    }
    
    private void gerarOrdenacaoDadosAbastecimento(final FiltroMapaAbastecimentoDTO filtro, final StringBuilder hql) {
        
        if (filtro.getExcluirProdutoSemReparte()) {
            
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
    	
        if (filtro.getPaginacao() == null){
            
            hql.append(" order by nomeProduto asc ");
            
            return;
            
        }
        
        final String sortOrder = filtro.getPaginacao().getOrdenacao().name();
        final ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
        
        if (coluna != null) {
        
	        String nome = null;
	        
	        switch(coluna) {
	        case BOX:
	            nome = " box.CODIGO ";
	            break;
	        case TOTAL_PRODUTO:
	            nome = " totalProduto ";
	            break;
	        case TOTAL_REPARTE:
	            nome = " totalReparte ";
	            break;
	        case TOTAL_BOX:
	            nome = " totalBox ";
	            break;
	        case CODIGO_COTA:
	            nome = " codigoCota ";
	            break;
	        case CODIGO_ROTA:
	            nome = " codigoRota ";
	            break;
	        case NOME_COTA:
	            nome = " nomeCota ";
	            break;
	        case CODIGO_PRODUTO:
	            nome = " codigoProduto ";
	            break;
	        case NOME_PRODTO:
	            nome = " nomeProduto ";
	            break;
	        case NUMERO_EDICAO:
	            nome = " numeroEdicao ";
	            break;
	        case REPARTE:
	            nome = " reparte ";
	            break;
	        case PROMOCIONAL:
	            nome = " materialPromocional ";
	            break;
	        case PRECO_CAPA:
	            nome = " precoCapa ";
	            break;
	        case TOTAL:
	            nome = " total ";
	            break;
	        case CODIGO_BOX:
	            nome = " codigoBox ";
	            break;
	        case PRODUTO_ESPECIFICO:
	            nome = " codigoBox, nomeProduto, roteiro.ordem, rota.ordem, rotaPDV.ORDEM ";
	            break;
	            
	        case NOME_EDICAO:
	            nome = " nomeProduto, numeroEdicao ";
	            break;
	        
	        case PRODUTO_COTA:
	        	if (filtro.getPaginacao() == null){
	        		nome = " nomeProduto, numeroEdicao, codigoCota ";
	        		break;
	        		
	        	} else {
	        		nome = " codigoBox, nomeProduto, roteiro.ordem, rota.ordem, rotaPDV.ORDEM, codigoCota ";
	        		break;
	        		
	        	}
	        
	        case SEQUENCIA_MATRIZ:
	         //   nome = " codigoBox, roteiro.ordem, rota.ordem, rotaPDV.ORDEM, sequenciaMatriz, nomeProduto ";
	            nome = " codigoBox, sequenciaMatriz, nomeProduto,  roteiro.ordem, rota.ordem, rotaPDV.ORDEM, codigoCota,  descRota ";
	            break;  
	            
	        default:
	            nome = "";
	            break;
	            
	        }
	        hql.append( " order by " + nome + sortOrder + " ");
        }
    }
    
    
    private void gerarOrdenacaoEntregador(final FiltroMapaAbastecimentoDTO filtro, final StringBuilder hql) {
        
        final String sortOrder = filtro.getPaginacao().getOrdenacao().name();
        
        final ColunaOrdenacaoEntregador coluna = ColunaOrdenacaoEntregador.getPorDescricao(filtro.getPaginacao().getSortColumn());
        
        String nome = null;
        
        switch(coluna) {
        case CODIGO_PRODUTO:
            nome = " codigoProduto ";
            break;
        case NOME_PRODTO:
            nome = " nomeProduto ";
            break;
        case NUMERO_EDICAO:
            nome = " numeroEdicao ";
            break;
        case CODIGO_BARRA:
            nome = " codigoBarra ";
            break;
        case PACOTE_PADRAO:
            nome = " pacotePadrao ";
            break;
        case REPARTE:
            nome = " reparte ";
            break;
        case PRECO_CAPA:
            nome = " precoCapa ";
            break;
        case CODIGO_COTA:
            nome = " codigoCota ";
            break;
        case NOME_COTA:
            nome = " nomeCota ";
            break;
        case QTDE_EXEMPLARES:
            nome = " qtdeExms ";
            break;
        default:
            nome = " codigoProduto ";
            break;
        }
        
        hql.append( " order by " + nome + sortOrder + " ");
    }
    
    @Override
    public Long countObterDadosAbastecimento(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from (");
        
        hql.append("select count(estudoCota.ID) as c ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by box.ID ");
        
        if (filtro.getQuebraPorCota()) {
            hql.append(" , cota.ID ");
        }
        
        if (filtro.getExcluirProdutoSemReparte()) {
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(
            final Long idBox, final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as total, ");
        hql.append("		box.ID as boxId ");
        
        filtro.setBox(idBox);
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by produto.CODIGO,produtoEdicao.NUMERO_EDICAO ");
        
        gerarOrdenacaoDetalhesAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        return query.list();
    }
    
    private void gerarOrdenacaoDetalhesAbastecimento(final FiltroMapaAbastecimentoDTO filtro, final StringBuilder hql) {
        
        final String sortOrder = filtro.getPaginacaoDetalhes().getOrdenacao().name();
        final ColunaOrdenacaoDetalhes coluna = ColunaOrdenacaoDetalhes.getPorDescricao(filtro.getPaginacaoDetalhes().getSortColumn());
        
        String nome = null;
        
        switch(coluna) {
        case CODIGO_PRODUTO:
            nome = " codigoProduto ";
            break;
        case NOME_PRODTO:
            nome = " nomeProduto ";
            break;
        case NUMERO_EDICAO:
            nome = " numeroEdicao ";
            break;
        case REPARTE:
            nome = " reparte ";
            break;
        case PRECO_CAPA:
            nome = " precoCapa ";
            break;
        case TOTAL:
            nome = " total ";
            break;
        case CODIGO_BOX:
            nome = " codigoBox ";
            break;
        default:
            break;

        }
        hql.append( " order by " + nome + sortOrder + " ");
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBox(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.CODIGO as codigoBox, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		coalesce(if(pessoa.tipo='F',pessoa.NOME, pessoa.RAZAO_SOCIAL), pessoa.NOME_FANTASIA, '') as nomeCota, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by box.ID, produtoEdicao.ID ");
        
        if (filtro.getQuebraPorCota()) {
            hql.append(" , cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoBoxRota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.CODIGO as codigoBox, ");
        hql.append("        box.NOME as nomeBox, ");
        hql.append(" 		rota.DESCRICAO_ROTA as codigoRota, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produtoEdicao.NOME_COMERCIAL as nomeProduto, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		pessoa.NOME as nomeCota, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota, ");
        hql.append("        roteiro.DESCRICAO_ROTEIRO as descRoteiro ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by produtoEdicao.ID, box.ID, rota.ID ");
        
        if (filtro.getQuebraPorCota()) {
            hql.append(" , cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoRota", StandardBasicTypes.STRING);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("descRoteiro", StandardBasicTypes.STRING);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        return query.list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoBoxRoteiro(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.CODIGO as codigoBox, ");
        hql.append("        box.NOME as nomeBox, ");
        hql.append(" 		roteiro.DESCRICAO_ROTEIRO as codigoRota, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produtoEdicao.NOME_COMERCIAL as nomeProduto, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		pessoa.NOME as nomeCota, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota, ");
        hql.append("        roteiro.DESCRICAO_ROTEIRO as descRoteiro ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by produtoEdicao.ID, box.ID, rota.ID ");
        
        if (filtro.getQuebraPorCota()) {
            hql.append(" , cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoRota", StandardBasicTypes.STRING);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("descRoteiro", StandardBasicTypes.STRING);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        return query.list();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBoxRota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.CODIGO as codigoBox, ");
        hql.append("        box.NOME as nomeBox, ");
        hql.append(" 		rota.DESCRICAO_ROTA as codigoRota, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		coalesce(if(pessoa.tipo = 'F',pessoa.NOME, pessoa.RAZAO_SOCIAL), pessoa.nome_fantasia, '') as nomeCota, ");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append("        count(distinct produtoEdicao.ID) as totalProduto, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by box.ID, rota.ID ");
        
        if (filtro.getQuebraPorCota()){
            
            hql.append(", cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoRota", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("totalProduto", StandardBasicTypes.LONG);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    @Override
    public Long countObterMapaAbastecimentoPorBoxRota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from ( ");
        
        hql.append("select count(estudoCota.ID) as c ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by box.ID, rota.ID ");
        
        if (filtro.getQuebraPorCota()){
            
            hql.append(", cota.ID ");
        }
        
        if (filtro.getExcluirProdutoSemReparte()) {
            
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoEdicao(
            final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select TRIM(box.CODIGO) as codigoBox, ");
        hql.append("        TRIM(box.NOME) as nomeBox, ");
        hql.append(" 		TRIM(rota.DESCRICAO_ROTA) as codigoRota, ");
        hql.append(" 		TRIM(produto.CODIGO) as codigoProduto, ");
        hql.append(" 		TRIM(produto.NOME) as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		TRIM(produtoEdicao.CODIGO_DE_BARRAS) as codigoBarra, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append("		coalesce(lancamento.REPARTE_PROMOCIONAL, 0) as materialPromocional ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by ");
        
        if (filtro.isProdutoEspecifico()){
            
            hql.append(" box.ID, rota.ID, produtoEdicao.ID  ");
        } else {
            
            hql.append(" produto.CODIGO  ");
        }
        
        if (filtro.getPaginacao() == null){
            
            hql.append(", rota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoRota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("materialPromocional", StandardBasicTypes.BIG_INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long countObterMapaAbastecimentoPorProdutoEdicao(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from (");
        
        hql.append("select count(estudoCota.ID) as c ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by ");
        
        if (filtro.isProdutoEspecifico()){
            
            hql.append(" box.ID, produtoEdicao.ID ");
        } else {
            
            hql.append(" produto.CODIGO ");
        }
        
        if (filtro.getExcluirProdutoSemReparte()) {
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger)query.uniqueResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorCota(
            final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		lancamento.SEQUENCIA_MATRIZ as sequenciaMatriz, ");
        hql.append(" 		lancamento.REPARTE_PROMOCIONAL as materialPromocional, ");
        hql.append("		box.id as boxId, ");
        hql.append("		box.CODIGO as codigoBox ");
        
        
        filtro.setUseSM(true);
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by ");
        
        if (filtro.isPorRepartePromocional()){
            
            hql.append(" produtoEdicao.ID ");
        } else {
            
            hql.append(" estudoCota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
        query.addScalar("materialPromocional", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long countObterMapaAbastecimentoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from ( ");
        
        hql.append("select count(estudoCota.ID) as c ");
        
        filtro.setUseSM(true);
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by produtoEdicao.ID ");
        
        if (filtro.getExcluirProdutoSemReparte()) {
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorProdutoQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select box.CODIGO as codigoBox, ");
        hql.append("  		box.NOME as nomeBox, ");
        hql.append("  		cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		coalesce(if(pessoa.tipo='F',pessoa.nome, pessoa.razao_social),pessoa.nome_fantasia) as nomeCota,");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by ");
        
        if (filtro.getPaginacao() == null){
            
            hql.append(" estudoCota.ID, produtoEdicao.ID ");
        } else {
            
            hql.append(" cota.ID ");
        }
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from (");
        
        hql.append("select count(estudoCota.ID) as c ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by cota.ID ");
        
        if (filtro.getExcluirProdutoSemReparte()) {
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(final ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
            final Long idCota, final GrupoNotaFiscal grupoNotaFiscal, final List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques,
            final Intervalo<Date> periodo, final List<Long> listaFornecedores, final List<Long> listaProdutos) {
        
        final List<MovimentoEstoqueCota> result = new ArrayList<MovimentoEstoqueCota>();
        
        final int qtdeIteracao =
                GrupoNotaFiscal.NF_DEVOLUCAO_SIMBOLICA.equals(grupoNotaFiscal)
                || GrupoNotaFiscal.NF_VENDA.equals(grupoNotaFiscal)
                ? 2 : 1 ;
        int i = 0;
        while (i < qtdeIteracao) {
            final StringBuilder sql = new StringBuilder("");
            
            sql.append(" SELECT DISTINCT movimentoEstoqueCota ")
            .append(" FROM Lancamento lancamento ");
            
            if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
                // MovimentoEstoqueCota dos lançamentos relacionados ao
                // ChamadaEncalhe
                sql.append("   INNER JOIN lancamento.chamadaEncalhe chamadaEncalhe ")
                .append("   INNER JOIN chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCota ")
                .append("   INNER JOIN chamadaEncalheCota.conferenciasEncalhe conferenciaEncalhe ")
                .append("   INNER JOIN conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ");
            } else {
                // MovimentoEstoqueCota dos lançamentos relacionados ao Estudo
                sql.append("   INNER JOIN lancamento.movimentoEstoqueCotas movimentoEstoqueCota ");
            }
            
            sql.append("   LEFT JOIN movimentoEstoqueCota.cota.fornecedores fornecedor ")
            .append("   LEFT JOIN movimentoEstoqueCota.listaProdutoServicos produtoServico ")
            .append("   LEFT JOIN produtoServico.produtoServicoPK.notaFiscal notaFiscal ")
            .append("   LEFT JOIN notaFiscal.informacaoEletronica informacaoEletronica ")
            .append("   LEFT JOIN informacaoEletronica.retornoComunicacaoEletronica retornoComunicacaoEletronica ")
            .append("   LEFT JOIN notaFiscal.identificacao.tipoNotaFiscal tipoNotaFiscal ");
            
            sql.append(" WHERE movimentoEstoqueCota.status = :status ")
            .append("   AND (retornoComunicacaoEletronica IS NULL OR retornoComunicacaoEletronica.status = :statusNFe)")
            .append("   AND (notaFiscal IS NULL OR notaFiscal.statusProcessamentoInterno != :statusInterno)")
            .append("   AND movimentoEstoqueCota.cota.id = :idCota ")
            .append("   AND (tipoNotaFiscal IS NULL OR tipoNotaFiscal.grupoNotaFiscal != :grupoNotaFiscal) ");
            
            if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
                sql.append("   AND (chamadaEncalhe.dataRecolhimento + :diasAMais) >= :diaAtual ")
                .append("   AND (chamadaEncalheCota.fechado IS NULL OR chamadaEncalheCota.fechado = :fechado) ")
                .append("   AND (chamadaEncalheCota.postergado IS NULL OR chamadaEncalheCota.postergado = :postergado) ");
            }
            
            if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
                sql.append("   AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
            }
            
            if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
                sql.append("   AND movimentoEstoqueCota.tipoMovimento.grupoMovimentoEstoque IN (:listaGrupoMoviementoEstoque) ");
            }
            
            if (listaProdutos != null && !listaProdutos.isEmpty()) {
                sql.append("   AND movimentoEstoqueCota.produtoEdicao.produto.id IN (:listaProdutos) ");
            }
            
            if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
                sql.append("   AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
            }
            
            final Query query = getSession().createQuery(sql.toString());
            
            query.setParameter("status", StatusAprovacao.APROVADO);
            query.setParameter("statusNFe", StatusRetornado.CANCELAMENTO_HOMOLOGADO);
            query.setParameter("statusInterno", StatusProcessamento.NAO_GERADA);
            query.setParameter("idCota", idCota);
            query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
            
            if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
                double diasAMais;
                if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoQuinto()) {
                    diasAMais = 4;
                } else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoQuarto()) {
                    diasAMais = 3;
                } else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoTerceiro()) {
                    diasAMais = 2;
                } else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoSegundo()) {
                    diasAMais = 1;
                } else {
                    diasAMais = 0;
                }
                query.setParameter("diasAMais", diasAMais);
                query.setParameter("diaAtual", new Date());
                query.setParameter("fechado", false);
                query.setParameter("postergado", false);
            }
            
            if (listaProdutos != null && !listaProdutos.isEmpty()) {
                query.setParameterList("listaProdutos", listaProdutos);
            }
            
            if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
                query.setParameterList("listaFornecedores", listaFornecedores);
            }
            
            if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
                query.setParameter("dataInicio", periodo.getDe());
                query.setParameter("dataFim", periodo.getAte());
            }
            
            if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
                query.setParameterList("listaGrupoMoviementoEstoque", listaGrupoMovimentoEstoques);
            }
            
            result.addAll(query.list());
            
            i++;
        }
        
        return result;
    }
    
    @Override
    public Long obterQuantidadeProdutoEdicaoMovimentadoPorCota(final Long idCota, final Long idProdutoEdicao, Lancamento lancamento) {

        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select sum(case when tipoMovimento.grupoMovimentoEstoque.operacaoEstoque = :entrada then movimentoEstoqueCota.qtde ");
        hql.append(" else (-movimentoEstoqueCota.qtde) end) ");
        hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
        hql.append(" join movimentoEstoqueCota.tipoMovimento tipoMovimento ");
        hql.append(" where movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao ");
        hql.append(" and movimentoEstoqueCota.movimentoEstoqueCotaFuro is null "); 
        
        if(idCota != null) {
            hql.append(" and movimentoEstoqueCota.cota.id = :idCota ");
        }
        
        if(lancamento != null) {
            hql.append(" and movimentoEstoqueCota.lancamento.dataRecolhimentoDistribuidor = :dataRecolhimentoDistribuidor ");
        }
        
        final Query query = getSession().createQuery(hql.toString());
        
        if(idCota != null) {
            query.setParameter("idCota", idCota);
        }
        
        if(lancamento != null) {
        	query.setParameter("dataRecolhimentoDistribuidor", lancamento.getDataRecolhimentoDistribuidor());
        }
        
        query.setParameter("idProdutoEdicao", idProdutoEdicao);

        query.setParameter("entrada", OperacaoEstoque.ENTRADA);
        
        final BigInteger sum = (BigInteger) (query.uniqueResult() == null ? BigInteger.ZERO : query.uniqueResult());
        
        return sum.longValue();
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public  List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(final Distribuidor distribuidor, final Long idCota, final List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, final Intervalo<Date> periodo, final List<Long> listaFornecedores){
        final StringBuilder sql = new StringBuilder("SELECT DISTINCT movimentoEstoqueCota ");
        sql.append(" FROM MovimentoEstoqueCota movimentoEstoqueCota ");
        
        sql.append("   JOIN movimentoEstoqueCota.lancamento lancamento ");
        sql.append("   JOIN movimentoEstoqueCota.cota cota ");
        
        
        sql.append("   JOIN movimentoEstoqueCota.produtoEdicao produtoEdicao ");
        sql.append("   JOIN produtoEdicao.produto produto ");
        sql.append("   JOIN produto.fornecedores fornecedor ");
        
        
        sql.append("   JOIN movimentoEstoqueCota.tipoMovimento tipoMovimento ");
        sql.append("   LEFT JOIN movimentoEstoqueCota.movimentoEstoqueCotaFuro movimentoEstoqueCotaFuro ");
        
        sql.append(" WHERE movimentoEstoqueCota.status = :status ")
        .append("   AND cota.id = :idCota ")
        .append(" AND movimentoEstoqueCotaFuro.id is null ");
        
        if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
            sql.append("   AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
        }
        
        if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
            sql.append("   AND tipoMovimento.grupoMovimentoEstoque IN (:listaGrupoMoviementoEstoque) ");
        }
        
        if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
            sql.append("   AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
        }
        
        final Query query = getSession().createQuery(sql.toString());
        query.setParameter("status", StatusAprovacao.APROVADO);
        query.setParameter("idCota", idCota);
        
        
        if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
            query.setParameterList("listaFornecedores", listaFornecedores);
        }
        
        if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
            query.setParameter("dataInicio", periodo.getDe());
            query.setParameter("dataFim", periodo.getAte());
        }
        
        if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
            query.setParameterList("listaGrupoMoviementoEstoque", listaGrupoMovimentoEstoques);
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorEntregador(
            final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		coalesce(produtoEdicao.CODIGO_DE_BARRAS, '') as codigoBarra, ");
        hql.append(" 		produtoEdicao.PACOTE_PADRAO as pacotePadrao, ");
        hql.append(" 		estudo.QTDE_REPARTE as reparte, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append(" 		cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		coalesce(if(pessoa.tipo = 'F',pessoa.nome, pessoa.RAZAO_SOCIAL),pessoa.nome_fantasia,'') as nomeCota,");
        hql.append(" 		estudoCota.REPARTE as qtdeExms ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by entregador.ID, produtoEdicao.ID ");
        
        gerarOrdenacaoEntregador(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("pacotePadrao", StandardBasicTypes.INTEGER);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("qtdeExms", StandardBasicTypes.BIG_INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null){
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null){
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    @Override
    public Long countObterMapaDeAbastecimentoPorEntregador(
            final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder("select count(sub.c) from (");
        
        hql.append("select count(estudoCota.id) as c ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by entregador.ID, produtoEdicao.ID ");
        
        if (filtro.getExcluirProdutoSemReparte()) {
        	
        	if (FiltroMapaAbastecimentoDTO.TipoConsulta.PROMOCIONAL.equals(filtro.getTipoConsulta())){
            	
            	hql.append(" having sum(lancamento.REPARTE_PROMOCIONAL) > 0 ");
            } else {
        	
            	hql.append(" having sum(estudoCota.QTDE_EFETIVA) > 0 ");
            }
        }
        
        hql.append(") as sub ");
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        return ((BigInteger) query.uniqueResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorEntregador(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		pessoa.NOME as nomeCota,");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append("        entregador.ID as idEntregador, ");
        hql.append("        coalesce(if(pessoaEnt.tipo = 'F',pessoaEnt.NOME, pessoaEnt.RAZAO_SOCIAL),pessoaEnt.nome_fantasia, '') as nomeEntregador, ");
        hql.append("        rotaEnt.DESCRICAO_ROTA as descRota, ");
        hql.append("        roteiroEnt.DESCRICAO_ROTEIRO as descRoteiro, ");
        hql.append("        boxEnt.CODIGO as codigoBox ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by entregador.ID, produtoEdicao.ID, cota.ID ");
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("idEntregador", StandardBasicTypes.LONG);
        query.addScalar("nomeEntregador", StandardBasicTypes.STRING);
        query.addScalar("descRota", StandardBasicTypes.STRING);
        query.addScalar("descRoteiro", StandardBasicTypes.STRING);
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null){
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null){
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorEntregadorQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		pessoa.NOME as nomeCota,");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produtoEdicao.NOME_COMERCIAL as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append(" 		produtoEdicao.PACOTE_PADRAO as pacotePadrao, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append("        entregador.ID as idEntregador, ");
        hql.append("        coalesce(if(pessoaEnt.tipo='F',pessoaEnt.NOME, pessoaEnt.RAZAO_SOCIAL),pessoaEnt.nome_fantasia, '') as nomeEntregador, ");
        hql.append("        rotaEnt.DESCRICAO_ROTA as descRota, ");
        hql.append("        roteiroEnt.DESCRICAO_ROTEIRO as descRoteiro, ");
        hql.append(" 		lancamento.SEQUENCIA_MATRIZ as sequenciaMatriz, ");
        hql.append("        boxEnt.CODIGO as codigoBox ");
        
        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
        
        hql.append(" group by entregador.ID, produtoEdicao.ID, produto.CODIGO, cota.id ");
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("pacotePadrao", StandardBasicTypes.INTEGER);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("idEntregador", StandardBasicTypes.LONG);
        query.addScalar("nomeEntregador", StandardBasicTypes.STRING);
        query.addScalar("descRota", StandardBasicTypes.STRING);
        query.addScalar("descRoteiro", StandardBasicTypes.STRING);
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null){
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null){
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoEstoqueCota> obterPorLancamento(final Long idLancamento) {
        

        final String hql = " select movimento from MovimentoEstoqueCota movimento "
                + " join movimento.lancamento lancamento "
                + " where lancamento.id = :idLancamento ";
        
        final Query query = super.getSession().createQuery(hql);
        
        query.setParameter("idLancamento", idLancamento);
        
        return query.list();
    }
    
    @Override
    public ValoresAplicados obterValoresAplicadosProdutoEdicao(final Integer numeroCota,
            final Long idProdutoEdicao, final Date dataOperacao) {
        
    	if(numeroCota == null) {
    		throw new ValidacaoException(TipoMensagem.WARNING, "Cota não informada ao obter Valores de Expedição.");
    	}
    	
    	if(idProdutoEdicao == null) {
    		throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não informada ao obter Valores de Expedição.");
    	}
    	
    	if(dataOperacao == null) {
    		throw new ValidacaoException(TipoMensagem.WARNING, "Data de Operacao não informada ao obter Valores de Expedição.");
    	}
    	
    	final StringBuilder sql = new StringBuilder();
    	
    	sql.append(" SELECT	");
    	sql.append(" MEC.PRECO_COM_DESCONTO AS precoComDesconto, ");
        sql.append(" MEC.PRECO_VENDA AS precoVenda,              ");
        sql.append(" MEC.VALOR_DESCONTO AS valorDesconto         ");
        sql.append(" FROM MOVIMENTO_ESTOQUE_COTA MEC             ");
		sql.append(" WHERE ");
		sql.append(" MEC.COTA_ID = ( SELECT ID FROM COTA WHERE NUMERO_COTA = :numeroCota )	");
		sql.append(" AND MEC.PRODUTO_EDICAO_ID= :idProdutoEdicao                            ");
		sql.append(" AND MEC.TIPO_MOVIMENTO_ID IN (                                         ");
		sql.append(" 		SELECT ID FROM TIPO_MOVIMENTO WHERE GRUPO_MOVIMENTO_ESTOQUE IN (:grupoMovimentoEstoque) ");
		sql.append(" )	");
    	sql.append(" AND MEC.DATA <= :dataOperacao ");
    	sql.append(" ORDER BY MEC.DATA DESC LIMIT 1		");
    	
    	Query query = getSession().createSQLQuery(sql.toString());
    	((SQLQuery) query).setResultTransformer(new AliasToBeanResultTransformer(ValoresAplicados.class));
    	
        query.setParameter("numeroCota", numeroCota);
        
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        
        query.setParameterList("grupoMovimentoEstoque", 
    		Arrays.asList(
    			GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
    			GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
    			GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
    			GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
    			GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name(),
    			GrupoMovimentoEstoque.COMPRA_ENCALHE.name(),
    			GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name()
    		)
    	);
        
        query.setParameter("dataOperacao", dataOperacao);
        
        return (ValoresAplicados) query.uniqueResult();
        
    }
    
    @Override
    public Long obterIdProdutoEdicaoPorControleConferenciaEncalhe(final Long idControleConferenciaEncalheCota){
        
        final StringBuilder hql = new StringBuilder(" select produtoEdicao.id  ");
        
        hql.append(" from ConferenciaEncalhe conferenciaEncalhe ")
        .append(" join conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ")
        .append(" join movimentoEstoqueCota.produtoEdicao produtoEdicao ")
        .append(" WHERE conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        query.setMaxResults(1);
        
        query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaSemEstudoPor(final List<Long> idCota, 
    		final Intervalo<Date> periodo,
            final List<Long> listaIdFornecedores,
            final List<GrupoMovimentoEstoque> listaGruposMovimentoEstoqueCota) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append("   select mec from MovimentoEstoqueCota mec   ");
        sql.append("   join mec.estudoCota e ");
        sql.append("   join mec.cota cota   ");
        sql.append("   join mec.lancamento lancamento   ");
        sql.append("   join mec.tipoMovimento tm   ");
        sql.append("   join mec.produtoEdicao pe   ");
        sql.append("   join pe.produto p   ");
        sql.append("   join p.fornecedores f   ");
        sql.append("   where 1=1 ");
        //sql.append("   mec.estudoCota is null ");  
        sql.append("  and lancamento.estudo is not null "); 
        sql.append("   and mec.itemNotaEnvio is null ");
        
        
        if(listaGruposMovimentoEstoqueCota != null && !listaGruposMovimentoEstoqueCota.isEmpty()) {
            sql.append("  and tm.grupoMovimentoEstoque in (:gruposMovimentosEstoque)  ");
        }
        
        if(idCota != null) {
            sql.append("  and mec.cota.id IN( :cotaID ) ");
        }
        
        if(periodo != null) {
            sql.append("  and lancamento.dataLancamentoDistribuidor between :dataInicial and :dataFinal ");
        }
        
        if(listaIdFornecedores != null && !listaIdFornecedores.isEmpty()) {
            sql.append(" and f.id in (:fornecedoresID) ");
        }
        
        sql.append(" order by mec.cota.id ");
        
        final Query query = this.getSession().createQuery(sql.toString());
        
        query.setParameterList("gruposMovimentosEstoque", listaGruposMovimentoEstoqueCota);
        
        query.setParameterList("cotaID", idCota);
        
        if (periodo != null) {
            query.setParameter("dataInicial", periodo.getDe());
            query.setParameter("dataFinal", periodo.getAte());
        }
        query.setParameterList("fornecedoresID", listaIdFornecedores);
        
        return query.list();
    }
    
    @Override
    public Date obterDataUltimaMovimentacaoReparteExpedida(final Integer numeroCota,
            final Long idProdutoEdicao) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select max(mec.DATA) as data ")
        .append(" from ")
        .append(" movimento_estoque_cota mec ")
        .append(" inner join ")
        .append(" tipo_movimento tm on (tm.ID = mec.TIPO_MOVIMENTO_ID) ")
        .append(" inner join ")
        .append(" lancamento on (lancamento.id = mec.LANCAMENTO_ID) ")
        .append(" inner join ")
        .append(" cota on (cota.ID = mec.COTA_ID) ")
        .append(" where ")
        .append(" tm.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ")
        .append(" and mec.PRODUTO_EDICAO_ID = :idProdutoEdicao ")
        .append(" and cota.NUMERO_COTA = :numeroCota ")
        .append(" and lancamento.STATUS = :statusLancamento ");
        
        final SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        
        query.addScalar("data", StandardBasicTypes.DATE);
        
        query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("statusLancamento", StatusLancamento.EXPEDIDO.name());
        
        return (Date) query.uniqueResult();
    }
    
    @Override
    public void adicionarEmLoteDTO(final List<MovimentoEstoqueCotaDTO> movimentosEstoqueCota) {
        
        if (movimentosEstoqueCota == null || movimentosEstoqueCota.isEmpty()) {
            return;
        }
        
        final Session session = this.getSession();
        
        session.doWork(new Work() {
            @Override
            public void execute(final Connection conn) throws SQLException {
                
                final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
                
                final StringBuilder sqlQry = new StringBuilder()
                .append("insert ")
                .append("into MOVIMENTO_ESTOQUE_COTA ")
                .append("(APROVADO_AUTOMATICAMENTE, APROVADOR_ID, DATA_APROVACAO, MOTIVO, STATUS, DATA, DATA_CRIACAO, ")
                .append("DATA_INTEGRACAO, STATUS_INTEGRACAO, TIPO_MOVIMENTO_ID, USUARIO_ID, PRODUTO_EDICAO_ID, ")
                .append("QTDE, COTA_ID, DATA_LANCAMENTO_ORIGINAL, ESTOQUE_PROD_COTA_ID, ")
                .append("ESTUDO_COTA_ID, NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, NOTA_ENVIO_ITEM_SEQUENCIA, LANCAMENTO_ID, ")
                .append("MOVIMENTO_ESTOQUE_COTA_FURO_ID, MOVIMENTO_FINANCEIRO_COTA_ID, STATUS_ESTOQUE_FINANCEIRO, ")
                .append("PRECO_COM_DESCONTO, PRECO_VENDA, VALOR_DESCONTO, FORMA_COMERCIALIZACAO, COTA_CONTRIBUINTE_EXIGE_NF, GERAR_COTA_EXIGE_NFE, ID) ")
                .append("values ")
                .append("(:aprovadoAutomaticamente, :usuarioAprovadorId, :dataAprovacao, :motivo, :status, :data, :dataCriacao, ")
                .append(":dataIntegracao, :statusIntegracao, :tipoMovimentoId, :usuarioId, :idProdEd, ")
                .append(":qtde, :idCota, :dataLancamentoOriginal, :estoqueProdutoEdicaoCotaId, ")
                .append(":estudoCotaId, :notaEnvioItemNotaEnvioId, :notaEnvioItemSequencia, :lancamentoId, ")
                .append(":movimentoEstoqueCotaFuroId, :movimentoFinanceiroCotaId, :statusEstoqueFinanceiro, ")
                .append(":precoComDesconto, :precoVenda, :valorDesconto, :formaComercializacao, :cotaContribuinteExigeNF, true, -1) ");
                
                final SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(movimentosEstoqueCota.toArray());
                
                namedParameterJdbcTemplate.batchUpdate(sqlQry.toString(), params);
                
            }
        });
        
    }
    
    @Override
    public void removerMovimentoEstoqueCotaPorEstudo(final Long idEstudo) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" update MovimentoEstoqueCota movEst set movEst.estudoCota = null");
        hql.append(" where movEst.estudoCota.id in (");
        hql.append(" select id from EstudoCota");
        hql.append(" where estudo.id = :idEstudo)");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("idEstudo", idEstudo);
        
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaReparteDTO> obterRepartePeriodosAnteriores(Lancamento lancamento) {
        
        final StringBuilder sql = new StringBuilder();
       
        sql.append(" select mec.cota as cota, ");
        sql.append(" mec.cotaContribuinteExigeNF as cotaContribuinteExigeNF, ");
        sql.append(" sum(mec.qtde) as reparte, ");
        sql.append(" lancamento.id as idLancamento ");
		sql.append("  from MovimentoEstoqueCota mec ");
		sql.append("	join mec.tipoMovimento tipo ");
		sql.append("	join mec.lancamento lancamento ");
		sql.append("    join  lancamento.periodoLancamentoParcial periodoLancamentoParcial  ");
		sql.append(" where  mec.tipoMovimento.id in (21,26) and periodoLancamentoParcial.tipo = :parcial ");
		sql.append("        and mec.produtoEdicao.id = :produtoEdicaoId ");
		sql.append(" group by mec.cota.id ");
		 sql.append("      having sum(mec.qtde)  > 0 ");
        
        final Query query = this.getSession().createQuery(sql.toString());
        
      
        
        query.setParameter("produtoEdicaoId", lancamento.getProdutoEdicao().getId());
        query.setParameter("parcial", TipoLancamentoParcial.PARCIAL);   
       
        
        query.setResultTransformer(Transformers.aliasToBean(CotaReparteDTO.class));
        
        return query.list();
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CotaReparteDTO> obterReparte(final Set<Long> idsLancamento, Long cotaId) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" select cota as cota, ");
        sql.append(" mec.cotaContribuinteExigeNF as cotaContribuinteExigeNF, ");
        sql.append(" sum( ");
        sql.append(" 	case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque = 'SAIDA') ");
        sql.append(" 	then (-mec.qtde) ");
        sql.append(" 	else (mec.qtde) end ");
        sql.append(" ) as reparte, ");
        sql.append(" lancamento.id as idLancamento ");
        
        sql.append(" from MovimentoEstoqueCota mec ");
        
        sql.append(" join mec.cota cota ");
        
        sql.append(" join mec.lancamento lancamento ");
        
        sql.append(" join mec.tipoMovimento tipoMovimento ");
        
        sql.append(" join mec.produtoEdicao produtoEdicao ");
        
        sql.append(" join lancamento.produtoEdicao produtoEdicaoLcto ");
        
        sql.append(" left join mec.movimentoEstoqueCotaFuro mecFuro ");
        
        sql.append(" where lancamento.id IN (:idsLancamento) ");
        
        if(cotaId != null) {
        	sql.append(" and mec.cota.id = :cotaId ");
        }
        
        sql.append(" and produtoEdicao.id = produtoEdicaoLcto.id ");
        
        sql.append(" and mecFuro.id is null ");
        
        sql.append(" and tipoMovimento.grupoMovimentoEstoque not in (:gruposMovimentoReparte) ");
        
        sql.append(" and (mec.statusEstoqueFinanceiro is null ");
        
        sql.append(" or (mec.statusEstoqueFinanceiro = :processado and cota.devolveEncalhe = true and cota.tipoCota = 'A_VISTA') ");
        
        sql.append(" or ( mec.statusEstoqueFinanceiro != :processado and cota.tipoCota = 'CONSIGNADO'  ))" );
        // ajustando o group by
        sql.append(" group by mec.cota.id, produtoEdicao.id, lancamento.id ");
        
        sql.append(" order by mec.cota.numeroCota ");
        
        final Query query = this.getSession().createQuery(sql.toString());
        
        query.setParameterList("idsLancamento", idsLancamento);

        query.setParameterList("gruposMovimentoReparte", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO));
        
        query.setParameter("processado", StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
        
        if(cotaId != null) {
        	query.setParameter("cotaId", cotaId);
        }
        
        query.setResultTransformer(Transformers.aliasToBean(CotaReparteDTO.class));
        
        return query.list();
    }
    
    @Override
	public void updateByIdConsolidadoAndGrupos(Long idConsolidado, List<String> grupoMovimentoFinaceiros,  String motivo, Long movimentoFinanceiroCota, StatusEstoqueFinanceiro statusEstoqueFinanceiro ){
      	
    	final StringBuilder sql =  new StringBuilder();
    	sql.append("UPDATE MOVIMENTO_ESTOQUE_COTA AS estoque ");
    	sql.append("join MOVIMENTO_FINANCEIRO_COTA movi on movi.id = estoque.MOVIMENTO_FINANCEIRO_COTA_ID AND movi.COTA_ID = estoque.COTA_ID ");
    	sql.append("join TIPO_MOVIMENTO tipo on movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' ");
    	sql.append("join CONSOLIDADO_MVTO_FINANCEIRO_COTA con on con.MVTO_FINANCEIRO_COTA_ID = movi.id ");

    	sql.append("SET estoque.MOTIVO = :motivo, ");
    	sql.append("estoque.MOVIMENTO_FINANCEIRO_COTA_ID = :movimentoFinanceiroCota, ");
    	sql.append("estoque.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro ");
    	sql.append("where con.CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");
    	sql.append("and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros)");
    	
    	 this.getSession().createSQLQuery(sql.toString())
	        .setParameter("motivo", motivo)
	        .setParameter("movimentoFinanceiroCota", null)
	        .setParameter("statusEstoqueFinanceiro", statusEstoqueFinanceiro.name())
	        .setParameter("idConsolidado", idConsolidado)
	        .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
	        .executeUpdate();

    }
    
    @Override
	public Long findIdByIdConferenciaEncalhe(Long idConferenciaEncalhe){
    	final StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT confe.movimentoEstoqueCota.id FROM ConferenciaEncalhe confe ");
    	sql.append("WHERE confe.id = :idConferenciaEncalhe");
    	
    	Query query =  getSession().createQuery(sql.toString());
    	
    	query.setParameter("idConferenciaEncalhe", idConferenciaEncalhe);
    	
    	return (Long) query.uniqueResult();
    }
    
	@Override
	public void updateById(Long id, ValoresAplicados valoresAplicados,
			BigInteger qtde) {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE MovimentoEstoqueCota estoque ");
		sql.append("SET estoque.valoresAplicados.precoVenda = :precoVenda ");
		sql.append(",estoque.valoresAplicados.precoComDesconto = :precoComDesconto ");
		sql.append(",estoque.valoresAplicados.valorDesconto = :valorDesconto ");
		sql.append(",estoque.qtde = :qtde ");
		sql.append("WHERE estoque.id = :id");

		this.getSession()
				.createQuery(sql.toString())
				.setParameter("precoVenda", valoresAplicados.getPrecoVenda())
				.setParameter("precoComDesconto",
						valoresAplicados.getPrecoComDesconto())
				.setParameter("valorDesconto",
						valoresAplicados.getValorDesconto())
				.setParameter("qtde", qtde).setParameter("id", id)
				.executeUpdate();
	}

    @Override
	public BigInteger loadQtdeById(Long id){
    	Query query =  getSession().createQuery("SELECT o.qtde FROM MovimentoEstoqueCota o WHERE o.id = :id");
    	
    	query.setParameter("id", id);
    	
    	return (BigInteger) query.uniqueResult();
    }
    
	@Override
	public void updateById(Long id, MovimentoFinanceiroCota movimentoFinanceiroCota) {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE MovimentoEstoqueCota estoque ");
		sql.append("SET estoque.movimentoFinanceiroCota = :movimentoFinanceiroCota ");
		sql.append("WHERE estoque.id = :id");

		this.getSession()
				.createQuery(sql.toString())
				.setParameter("movimentoFinanceiroCota", movimentoFinanceiroCota)
				.setParameter("id", id)
				.executeUpdate();
	}
	
	@Override
	public void updateById(Long id, StatusEstoqueFinanceiro statusEstoqueFinanceiro) {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE MovimentoEstoqueCota estoque ");
		sql.append("SET estoque.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ");
		sql.append("WHERE estoque.id = :id");

		this.getSession()
				.createQuery(sql.toString())
				.setParameter("statusEstoqueFinanceiro", statusEstoqueFinanceiro)
				.setParameter("id", id)
				.executeUpdate();
	}

    @Override
    public void updateByCotaAndDataOpAndGrupos(Long idCota, Date dataOperacao, List<String> grupoMovimentoFinaceiros,
            String motivo, StatusEstoqueFinanceiro statusEstoqueFinanceiro) {
        
        final String sql = "UPDATE MOVIMENTO_ESTOQUE_COTA AS estoque "+
            "join MOVIMENTO_FINANCEIRO_COTA movi on "+
            "movi.id = estoque.MOVIMENTO_FINANCEIRO_COTA_ID "+
            "join TIPO_MOVIMENTO tipo on "+
            "movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' "+
            "SET estoque.MOTIVO = :motivo "+
            ",estoque.MOVIMENTO_FINANCEIRO_COTA_ID = :movimentoFinanceiroCota "+
            ",estoque.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro "+
            "where movi.COTA_ID = :idCota "+
            "and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros)"+
            "and movi.DATA = :dataOperacao ";
        
         this.getSession().createSQLQuery(sql)
            .setParameter("motivo", motivo)
            .setParameter("movimentoFinanceiroCota", null)
            .setParameter("statusEstoqueFinanceiro", statusEstoqueFinanceiro.name())
            .setParameter("idCota", idCota)
            .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
            .setParameter("dataOperacao", dataOperacao)
            .executeUpdate();
    }
    
	@Override
	@SuppressWarnings("unchecked")
    public List<MovimentoEstoqueCota> obterMovimentosComProdutoContaFirme(final Long idLancamento){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select m from MovimentoEstoqueCota m join m.lancamento lancamento join m.tipoMovimento tipoMovimento  ");
    	hql.append(" where lancamento.id =:idLancamento ");
    	hql.append(" and tipoMovimento.grupoMovimentoEstoque =:grupoMovimentoEstoque ");
    	hql.append(" and m.movimentoEstoqueCotaFuro is null ");
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("idLancamento", idLancamento);
    	query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE_CONTA_FIRME);
    	
    	return query.list();
    }

	@Override
    public void atualizarPrecoProdutoExpedido(final Long idProdutoEdicao, final BigDecimal precoProduto){
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" update movimento_estoque_cota mc ")
	    	.append(" join lancamento l on mc.LANCAMENTO_ID = l.ID ")
	    	.append(" join tipo_movimento tm on tm.ID = mc.TIPO_MOVIMENTO_ID ")
	    	.append(" set mc.PRECO_VENDA = :precoProduto ,")
	    	.append(" mc.PRECO_COM_DESCONTO = :precoProduto - ((mc.valor_desconto/100)* :precoProduto) ")
	    	.append(" where l.STATUS in (:statusLancamento) ")
	    	.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE in (:statusRecebimentoReparte) ")
	    	.append(" and mc.PRODUTO_EDICAO_ID =:idProdutoEdicao ");
	   
    	Query query = getSession().createSQLQuery(sql.toString());
    	
    	query.setParameterList("statusLancamento", 
    			Arrays.asList(StatusLancamento.EXPEDIDO.name(),
    						  StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name()));
    	query.setParameter("idProdutoEdicao", idProdutoEdicao);
    	query.setParameterList("statusRecebimentoReparte", 
    			Arrays.asList(
    					GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
    					GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
    					GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
    					GrupoMovimentoEstoque.FALTA_DE_COTA.name(), 
    					GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
    					GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA.name(),
    					GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
    					GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
    					GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name(),
    					GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name(),
    					GrupoMovimentoEstoque.COMPRA_ENCALHE.name(),
    					GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name(),
    					GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR.name()
					)
				);
    	query.setParameter("precoProduto", precoProduto);
    	
    	query.executeUpdate();
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentosEstoqueCotaPorIds(List<Long> idsMEC) {
		
		StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select mec from MovimentoEstoqueCota mec ")
    		.append(" where mec.id in (:idsMEC) ");
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameterList("idsMEC", idsMEC);
    	
    	return query.list();
	}
    
    public BigDecimal obterValorExpedicaoCotaAVista(final Date dataMovimentacao, Boolean devolveEncalhe, boolean precoCapaHistoricoAlteracao){
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select SUM(CONSIGNADO.total) as TOTAL ");
    	sql.append(" from ( ");
    	sql.append("	SELECT  C.ID AS cotaId,  PE.ID AS produtoEdicaoId, ");  
    	sql.append("	(COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * SUM(CASE WHEN TM.OPERACAO_ESTOQUE=:opSaida THEN MEC.QTDE ELSE MEC.QTDE * -1 END)) AS total ");
    	sql.append(" FROM MOVIMENTO_ESTOQUE_COTA MEC ");
    	sql.append(" INNER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID) ");
    	sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
    	sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
    	sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
    	sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
    	sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
    	sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
    	sql.append(" INNER JOIN FORNECEDOR fornecedor8_ ON F.fornecedores_ID=fornecedor8_.ID ");
    	sql.append(" INNER JOIN PESSOA PJ ON fornecedor8_.JURIDICA_ID=PJ.ID ");
    	sql.append(" WHERE MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
    	sql.append(" AND LCTO.STATUS NOT IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') "); 
    	sql.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:grupoEstornoReparteCotaFuro) ");
    	
    	if(devolveEncalhe != null) {
    		
    		sql.append(" AND (");
    		sql.append("        ((c.TIPO_COTA = :tipoCotaAVista ");
    		
    		if(devolveEncalhe) {
    			sql.append(" AND c.DEVOLVE_ENCALHE = TRUE ");
    		} else {
    			sql.append(" AND c.DEVOLVE_ENCALHE = FALSE ");
    		}
    			   		
    		sql.append(" )       	AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusFinanceiroNaoProcessado)) ");
    		sql.append("    )");
    	} else {
    		
    		sql.append(" AND (");
    		sql.append("        c.TIPO_COTA = :tipoCotaAVista AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusFinanceiroNaoProcessado) ");
    		sql.append("    )");
    	}
		
    	sql.append(" AND LCTO.DATA_LCTO_DISTRIBUIDOR =:dataMovimentacao ");
    	sql.append(" GROUP BY PE.ID, C.ID ");
    	sql.append(" HAVING ");
    	sql.append("    SUM(if(TM.OPERACAO_ESTOQUE=:opEntrada,MEC.QTDE,0) - if(TM.OPERACAO_ESTOQUE=:opSaida,MEC.QTDE,0 ))<>0 ");
    	sql.append(") as CONSIGNADO  ");
    	
    	Query query = getSession().createSQLQuery(sql.toString()).addScalar("TOTAL", StandardBasicTypes.BIG_DECIMAL);
    	
    	query.setParameter("opEntrada", OperacaoEstoque.ENTRADA.name());
    	
    	query.setParameter("opSaida", OperacaoEstoque.SAIDA.name());
    	
    	query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
    	
    	query.setParameter("grupoEstornoReparteCotaFuro",GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name());
    	
    	query.setParameter("statusFinanceiroNaoProcessado", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
    	
    	query.setParameter("dataMovimentacao", dataMovimentacao);
    	
    	return (BigDecimal) query.uniqueResult();
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoAbastecimentoDTO> obterDadosAbastecimentoBoxVersusCota(FiltroMapaAbastecimentoDTO filtro) {

        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		pessoa.NOME as nomeCota,");
        hql.append(" 		produto.CODIGO as codigoProduto, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
        hql.append("        count(distinct produtoEdicao.ID) as totalProduto, ");
        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
        hql.append("        box.NOME as nomeBox, ");
        hql.append("        box.CODIGO as codigoBox ");
        
        gerarFromWhereDadosAbastecimentoVersusBoxCota(filtro, hql, param, statusLancamento);
        
        // hql.append(" group by produtoEdicao.ID, cota.ID ");
        hql.append(" group by box.ID, cota.ID  ");
        
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
        query.addScalar("nomeCota", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("totalProduto", StandardBasicTypes.LONG);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("nomeBox", StandardBasicTypes.STRING);
        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null){
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null){
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public List<AbastecimentoBoxCotaDTO> obterMapaDeImpressaoPorBoxVersusCotaQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
        
        final Map<String, Object> param = new HashMap<String, Object>();
        
        final List<String> statusLancamento = new ArrayList<String>();
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select cota.NUMERO_COTA as codigoCota, ");
        hql.append(" 		produto.NOME as nomeProduto, ");
        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
        hql.append(" 		estudoCota.REPARTE as reparte ");
        
        gerarFromWhereDadosAbastecimentoVersusBoxCota(filtro, hql, param, statusLancamento);
        
        hql.append(" group by ");
        
        if (filtro.getPaginacao() == null){
            
            hql.append(" estudoCota.ID, produtoEdicao.ID ");
        } else {
            
            hql.append(" cota.ID, produtoEdicao.ID ");
        }
              
        gerarOrdenacaoDadosAbastecimento(filtro, hql);
        
        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
        
        setParameters(query, param);
        
        query.setParameterList("status", statusLancamento);
        
        query.addScalar("codigoCota", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("reparte", StandardBasicTypes.BIG_DECIMAL);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(AbastecimentoBoxCotaDTO.class));
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) {
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
        }
        
        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
	
	private void gerarFromWhereDadosAbastecimentoVersusBoxCota(final FiltroMapaAbastecimentoDTO filtro, final StringBuilder hql, final Map<String, Object> param, final List<String> statusLancamento) {
        
        hql.append(" from ESTUDO_COTA estudoCota ");
        hql.append("	join ESTUDO estudo ON (estudo.ID = estudoCota.ESTUDO_ID) ");
        hql.append("	join COTA cota ON (cota.ID = estudoCota.COTA_ID) ");
        hql.append("	join PRODUTO_EDICAO produtoEdicao ON (produtoEdicao.ID = estudo.PRODUTO_EDICAO_ID) ");
        hql.append("	join PRODUTO produto ON (produto.ID = produtoEdicao.PRODUTO_ID) ");
        hql.append("    join PESSOA pessoa ON (pessoa.ID = cota.PESSOA_ID) ");
        hql.append(" 	join LANCAMENTO lancamento ON (lancamento.ESTUDO_ID = estudo.ID) ");
        hql.append("    join PDV pdv ON (pdv.COTA_ID = cota.ID) ");
        hql.append("    join ROTA_PDV rotaPDV ON (rotaPDV.PDV_ID = pdv.ID) ");
        hql.append("    join ROTA rota ON (rotaPDV.ROTA_ID = rota.ID) ");
        hql.append("    join ROTEIRO roteiro ON (roteiro.ID = rota.ROTEIRO_ID) ");
        hql.append("    join ROTEIRIZACAO rtz ON (rtz.ID = roteiro.ROTEIRIZACAO_ID) ");   
        hql.append("    join BOX box ON (box.ID = rtz.BOX_ID) ");
        hql.append(" where lancamento.STATUS in (:status) ");
        
        hql.append(" and estudoCota.TIPO_ESTUDO = :tipoEstudo ");
        param.put("tipoEstudo", TipoEstudoCota.NORMAL.name());
        
        if(filtro.getDataDate() != null) {
            // Criado pelo Eduardo Punk Rock - Comentado para realizar a busca
            // através da data de lançamento do distribuidor e não a data de
            // movimento que foi gerada
            hql.append(" and lancamento.DATA_LCTO_DISTRIBUIDOR = :data ");
            param.put("data", filtro.getDataDate());
        }
        
       
        if(filtro.getBox() != null) {
            
            hql.append(" and box.ID = :box ");
            param.put("box", filtro.getBox());
        }  else {
			
			hql.append(" and  box.tipo_box <> 'ESPECIAL' ");
			hql.append(" and roteiro.TIPO_ROTEIRO <> 'ESPECIAL' ");
			
		}
        
        if(filtro.getRota() != null) {
            
            hql.append(" and rota.ID = :rota ");
            param.put("rota", filtro.getRota());
        }
        
        if(filtro.getRoteiro() != null) {
            
            hql.append(" and roteiro.ID = :roteiro ");
            param.put("roteiro", filtro.getRoteiro());
        }
        
        if(filtro.getCodigosProduto() != null && !filtro.getCodigosProduto().isEmpty()) {
            
            hql.append(" and produto.CODIGO in (:codigosProduto) ");
            param.put("codigosProduto", filtro.getCodigosProduto());
        }
        
        if(filtro.getNumerosEdicao() != null && !filtro.getNumerosEdicao().isEmpty()) {
            
            hql.append(" and produtoEdicao.NUMERO_EDICAO in (:numeroEdicao) ");
            param.put("numeroEdicao", filtro.getNumerosEdicao());
        }
        
        if(filtro.getCodigoCota() != null ) {
            
            hql.append(" and cota.NUMERO_COTA = :codigoCota ");
            param.put("codigoCota", filtro.getCodigoCota());
        }
        
        if(filtro.getIdEntregador() != null ) {
            
            hql.append(" and entregador.ID = :idEntregador ");
            param.put("idEntregador", filtro.getIdEntregador());
        }
        
        statusLancamento.add(StatusLancamento.BALANCEADO.name());
        statusLancamento.add(StatusLancamento.EXPEDIDO.name());
        
    }
	
	 @SuppressWarnings("unchecked")
	    @Override
	    public List<ProdutoAbastecimentoDTO> obterCotasEntregadorQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
	        
	        final Map<String, Object> param = new HashMap<String, Object>();
	        
	        final List<String> statusLancamento = new ArrayList<String>();
	        
	        final StringBuilder hql = new StringBuilder();
	        
	        hql.append(" select cota.NUMERO_COTA as codigoCota, ");
	        hql.append(" 		pessoa.NOME as nomeCota,");
	        hql.append(" 		produto.CODIGO as codigoProduto, ");
	        hql.append(" 		produtoEdicao.NOME_COMERCIAL as nomeProduto, ");
	        hql.append(" 		produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
	        hql.append(" 		produtoEdicao.CODIGO_DE_BARRAS as codigoBarra, ");
	        hql.append(" 		produtoEdicao.ID as idProdutoEdicao, ");
	        hql.append(" 		produtoEdicao.PACOTE_PADRAO as pacotePadrao, ");
	        hql.append(" 		sum(estudoCota.REPARTE) as reparte, ");
	        hql.append(" 		sum(estudoCota.REPARTE * produtoEdicao.PRECO_VENDA) as totalBox, ");
	        hql.append(" 		produtoEdicao.PRECO_VENDA as precoCapa, ");
	        hql.append("        entregador.ID as idEntregador, ");
	        hql.append("        coalesce(if(pessoaEnt.tipo = 'F',pessoaEnt.NOME, pessoaEnt.RAZAO_SOCIAL),pessoaEnt.nome_fantasia, '') as nomeEntregador, ");
	        hql.append("        rotaEnt.DESCRICAO_ROTA as descRota, ");
	        hql.append("        roteiroEnt.DESCRICAO_ROTEIRO as descRoteiro, ");
	        hql.append(" 		lancamento.SEQUENCIA_MATRIZ as sequenciaMatriz, ");
	        hql.append("        boxEnt.CODIGO as codigoBox ");
	        
	        gerarFromWhereDadosAbastecimento(filtro, hql, param, statusLancamento);
	        
	        hql.append(" group by cota.id, boxEnt.CODIGO, entregador.ID, produtoEdicao.ID ");
	        
	        gerarOrdenacaoDadosAbastecimento(filtro, hql);
	        
	        final SQLQuery query =  getSession().createSQLQuery(hql.toString());
	        
	        setParameters(query, param);
	        
	        query.setParameterList("status", statusLancamento);
	        
	        query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
	        query.addScalar("nomeCota", StandardBasicTypes.STRING);
	        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
	        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
	        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
	        query.addScalar("codigoBarra", StandardBasicTypes.STRING);
	        query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
	        query.addScalar("pacotePadrao", StandardBasicTypes.INTEGER);
	        query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
	        query.addScalar("totalBox", StandardBasicTypes.BIG_DECIMAL);
	        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
	        query.addScalar("idEntregador", StandardBasicTypes.LONG);
	        query.addScalar("nomeEntregador", StandardBasicTypes.STRING);
	        query.addScalar("descRota", StandardBasicTypes.STRING);
	        query.addScalar("descRoteiro", StandardBasicTypes.STRING);
	        query.addScalar("codigoBox", StandardBasicTypes.INTEGER);
	        query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
	        
	        query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
	        
	        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null){
	            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
	        }
	        
	        if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null){
	            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
	        }
	        
	        return query.list();
	    }
	 
	 
	    private StringBuilder obterQueryListaConsultaReparte(final FiltroConsultaEncalheDTO filtro, final boolean counting) {
	    	
			StringBuilder sql = new StringBuilder();
			
			final StringBuilder subSqlVendaProduto = new StringBuilder();
	        
			subSqlVendaProduto.append(" select COALESCE(sum( vp.QNT_PRODUTO ),0) ");
	        subSqlVendaProduto.append(" from venda_produto vp ");
	        subSqlVendaProduto.append(" where vp.ID_PRODUTO_EDICAO =  :idProdutoEdicao ");
	        subSqlVendaProduto.append(" and vp.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
	        subSqlVendaProduto.append(" and vp.TIPO_VENDA_ENCALHE = :tipoVendaProduto");
	        subSqlVendaProduto.append(" and vp.TIPO_COMERCIALIZACAO_VENDA <> 'CONTA_FIRME' ");
	        subSqlVendaProduto.append(" and vp.ID_COTA = mec.cota_id ");
	           
			 final StringBuilder qtdeInformadaEncalhe = new StringBuilder("select coalesce(sum(COALESCE(conf1.QTDE, 0)), 0) ");
			 
			 qtdeInformadaEncalhe.append(" from conferencia_encalhe conf1 ");
			 qtdeInformadaEncalhe.append("  inner join chamada_encalhe_cota   cec on cec.id = conf1.chamada_encalhe_cota_id ");
			 qtdeInformadaEncalhe.append(" inner join chamada_encalhe ce on ce.id = cec.CHAMADA_ENCALHE_ID ");
			 qtdeInformadaEncalhe.append(" 		            where conf1.DATA BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
			 qtdeInformadaEncalhe.append(" and ce.PRODUTO_EDICAO_ID = mec.produto_edicao_id ");  		
			 qtdeInformadaEncalhe.append(" and cec.cota_id = mec.cota_id ");
		      
			sql.append(" 			SELECT  coalesce(if(pessoa.tipo = 'F',pessoa.nome, pessoa.razao_social), pessoa.nome_fantasia, '') as nomeCota ,");
			sql.append(" 			boxid as idBox,boxnome as nomeBox,tipobox as tipobox,");
			sql.append(" 		       c.numero_cota as idCota,");
			sql.append(" 		        MEC.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID,MEC.cota_id ,");
			sql.append(" 		            SUM(COALESCE(if(tm.OPERACAO_ESTOQUE = 'SAIDA', MEC.qtde * - 1, MEC.qtde), 0)) AS REPARTE,");
			sql.append("( ( ").append(qtdeInformadaEncalhe).append(" ) - ( ").append(subSqlVendaProduto).append(") ) as encalhe ");
		       
			sql.append(" 		    FROM");
			sql.append(" 		        MOVIMENTO_ESTOQUE_COTA MEC");
			sql.append(" 		    INNER JOIN (SELECT ");
			sql.append(" 		        distinct PRODUTO_EDICAO.ID AS ID, b.id as boxid,b.tipo_box as tipobox, b.nome as boxnome,CCEC.COTA_ID as COTA_ID");
			sql.append(" 		    FROM");
			sql.append(" 		        CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC");
			sql.append(" 		    INNER JOIN CONFERENCIA_ENCALHE ON (CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID)");
			sql.append(" 		    INNER JOIN PRODUTO_EDICAO ON (PRODUTO_EDICAO.ID = CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID)");
			sql.append(" 		    INNER JOIN PRODUTO ON (PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID)");
			sql.append(" 		    INNER JOIN PRODUTO_FORNECEDOR ON (PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID)");
			sql.append(" 		    INNER JOIN FORNECEDOR ON (PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID)");
			sql.append(" 		    INNER JOIN BOX B ON (B.id  = ccec.box_id )");
			sql.append(" 		    INNER JOIN COTA c ON (c.id = CCEC.cota_id )");
			sql.append(" 		    INNER JOIN PESSOA ON (PESSOA.ID = FORNECEDOR.JURIDICA_ID)");
			sql.append(" 		    WHERE ");
			sql.append(" 		      CCEC.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");

			if(filtro.getIdCota() != null) {
	           sql.append(" 		AND CCEC.COTA_ID = :idCota" );
	        }
			if(filtro.getIdBox() != null) {
	        	sql.append("  and c.box_id = :idBox" );
	        }
			sql.append(" 		    AND PRODUTO_EDICAO.ID = :idProdutoEdicao ");
			sql.append(" 		    GROUP BY  CCEC.COTA_ID) AS EDICAO_ENCALHADA ON (MEC.PRODUTO_EDICAO_ID = EDICAO_ENCALHADA.ID AND MEC.COTA_ID = EDICAO_ENCALHADA.COTA_ID and MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null)");
			sql.append(" 		    INNER JOIN cota c ON c.id = MEC.COTA_ID");
			sql.append(" 		    INNER JOIN PESSOA ON (PESSOA.ID = c.PESSOA_ID)");
			sql.append(" 		    INNER JOIN BOX ON (BOX.ID = c.BOX_ID)");
			sql.append(" 		    INNER JOIN TIPO_MOVIMENTO TM ON (TM.ID = MEC.TIPO_MOVIMENTO_ID)");
			sql.append(" 		    INNER JOIN chamada_encalhe ce ON ce.PRODUTO_EDICAO_ID = EDICAO_ENCALHADA.ID");
			sql.append(" 		    INNER JOIN chamada_encalhe_cota cec ON cec.CHAMADA_ENCALHE_ID = ce.id");
			sql.append(" 		        and cec.COTA_ID = c.id");
			sql.append(" 		    INNER JOIN conferencia_encalhe conf ON conf.CHAMADA_ENCALHE_COTA_ID = cec.id");
			sql.append(" 		    INNER JOIN chamada_encalhe_lancamento cel ON cel.CHAMADA_ENCALHE_ID = ce.id");
			sql.append(" 		    INNER JOIN lancamento l ON l.id = mec.LANCAMENTO_ID");
			sql.append(" 		        and l.id = cel.LANCAMENTO_ID");
			sql.append(" 		        and l.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID");
			sql.append(" 		    LEFT OUTER JOIN PERIODO_LANCAMENTO_PARCIAL PLP ON PLP.ID = L.PERIODO_LANCAMENTO_PARCIAL_ID");
			sql.append(" 		    LEFT OUTER JOIN LANCAMENTO_PARCIAL LP ON PLP.LANCAMENTO_PARCIAL_ID = LP.ID");
			sql.append(" 		    WHERE");
			sql.append(" 		        MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null");
			sql.append(" 		        AND MEC.PRODUTO_EDICAO_ID = :idProdutoEdicao ");
			sql.append(" 		            AND TM.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE'");
			sql.append(" 		            AND conf.DATA BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
			if(filtro.getIdCota() != null) {
	        	sql.append("  and mec.cota_id = :idCota" );
	        }
			if(filtro.getIdBox() != null) {
	        	sql.append("  and c.box_id = :idBox" );
	        }
			sql.append("  and tipobox = 'ENCALHE' " );  // FILTRO PARA NAO PEGAR COTA AUSENTE
			sql.append(" 		            AND MEC.LANCAMENTO_ID is not null");
			sql.append(" 		            AND MEC.FORMA_COMERCIALIZACAO <> 'CONTA_FIRME'");
			sql.append(" 		    GROUP BY MEC.COTA_ID");
			sql.append(" ");
			
	        return sql;
	    } 
}