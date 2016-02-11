package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.client.vo.ContaCorrenteVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends
AbstractRepositoryModel<ConsolidadoFinanceiroCota, Long> implements
ConsolidadoFinanceiroRepository {
    
    public ConsolidadoFinanceiroRepositoryImpl() {
        
        super(ConsolidadoFinanceiroCota.class);
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(
            final Integer numeroCota) {
        
        final StringBuilder hql = new StringBuilder();
        
        hql.append(" select ");
        
        hql.append("    consolidadoFinanceiroCota.dataConsolidado as dataConsolidado,                 ");
        hql.append("         consolidadoFinanceiroCota.valorPostergado as valorPostergado,                ");
        hql.append("         consolidadoFinanceiroCota.numeroAtrasados as numeroAtradao,                    ");
        hql.append("         consolidadoFinanceiroCota.consignado as consignado,                       ");
        hql.append("          consolidadoFinanceiroCota.encalhe as encalhe,                               ");
        hql.append("          consolidadoFinanceiroCota.vendaEncalhe as vendaEncalhe,                     ");
        hql.append("          consolidadoFinanceiroCota.debitoCredito as debCred,                            ");
        hql.append("         consolidadoFinanceiroCota.encargos as encargos,                             ");
        hql.append("         consolidadoFinanceiroCota.pendente as pendente,                                 ");
        hql.append("         consolidadoFinanceiroCota.total as total ,                                          ");
        hql.append("         consolidadoFinanceiroCota.id as id                                                    ");
        
        hql.append(" from ");
        
        hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");
        
        hql.append(" where ");
        
        hql.append(" consolidadoFinanceiroCota.cota.numeroCota = :numeroCota ");
        
        final Query query = getSession().createQuery(hql.toString());
        
        final ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
                ViewContaCorrenteCotaDTO.class);
        
        query.setResultTransformer(resultTransformer);
        
        query.setParameter("numeroCota", numeroCota);
        
        return query.list();
    }
    
    /**
     * Método que obtém uma lista de encalhe por produto e cota
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(
            final FiltroConsolidadoEncalheCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder("select STRAIGHT_JOIN");
        
        hql.append("     produto8_.CODIGO as codigoProduto, ")
        .append("        produto8_.NOME as nomeProduto, ")
        .append("        pessoajuri11_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi7_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi7_.PRECO_VENDA as precoCapa, ")
        .append("        movimentos4_.VALOR_DESCONTO as desconto, ")
        .append("        coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi7_.PRECO_VENDA) as precoComDesconto, ")
        .append("        sum(movimentos4_.QTDE) * coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi7_.PRECO_VENDA) as total,")
        .append("        sum(movimentos4_.QTDE) as encalhe,  ")
        .append("        chamadaEncalhe.sequencia as sequencia ")
        .append("from ")
        .append("        CONSOLIDADO_FINANCEIRO_COTA consolidad0_  ")
        .append("left outer join ")
        .append("        COTA cota1_  ")
        .append("                on consolidad0_.COTA_ID=cota1_.ID  ")
        .append("left outer join ")
        .append("        CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos2_  ")
        .append("                on consolidad0_.ID=movimentos2_.CONSOLIDADO_FINANCEIRO_ID  ")
        .append("left outer join ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof3_  ")
        .append("                on movimentos2_.MVTO_FINANCEIRO_COTA_ID=movimentof3_.ID  ")
        .append("left outer join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos4_  ")
        .append("                on movimentof3_.ID=movimentos4_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        .append("left outer join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro6_  ")
        .append("                on movimentos4_.ESTOQUE_PROD_COTA_ID=estoquepro6_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO_EDICAO produtoedi7_  ")
        .append("                on estoquepro6_.PRODUTO_EDICAO_ID=produtoedi7_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO produto8_  ")
        .append("                on produtoedi7_.PRODUTO_ID=produto8_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO_FORNECEDOR fornecedor9_  ")
        .append("                on produto8_.ID=fornecedor9_.PRODUTO_ID  ")
        .append("left outer join ")
        .append("        FORNECEDOR fornecedor10_  ")
        .append("                on fornecedor9_.fornecedores_ID=fornecedor10_.ID  ")
        .append("left outer join ")
        .append("        PESSOA pessoajuri11_  ")
        .append("                on fornecedor10_.JURIDICA_ID=pessoajuri11_.ID  ")
        .append("left outer join ")
        .append("        TIPO_MOVIMENTO tipomovime5_  ")
        .append("                on movimentof3_.TIPO_MOVIMENTO_ID=tipomovime5_.ID  ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota ")
        .append("                on cota1_.ID = chamadaEncalheCota.COTA_ID ")
        .append("inner join conferencia_encalhe conf on conf.chamada_encalhe_cota_id = chamadaEncalheCota.ID and conf.DATA = movimentos4_.data ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE chamadaEncalhe ")
        .append("                on (produtoedi7_.ID = chamadaEncalhe.PRODUTO_EDICAO_ID ")
        .append("                and chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID) ")
        .append("where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
        .append("        and tipomovime5_.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinanceiro) ")
        .append("    and movimentos4_.QTDE != 0 ")
        .append("        and chamadaEncalheCota.postergado = :naoPostergado ");
        
        if (filtro.getIdConsolidado()!=null){
        	
       	    hql.append(" and consolidad0_.id = :idConsolidado ");
        }
        
        hql.append("group by ")
        .append("        produto8_.CODIGO , ")
        .append("        produto8_.NOME , ")
        .append("        produtoedi7_.NUMERO_EDICAO , ")
        .append("        produtoedi7_.PRECO_VENDA , ")
        .append("        pessoajuri11_.RAZAO_SOCIAL ")
        
        .append("union all ")
        
        .append("select STRAIGHT_JOIN ")
        .append("        produto6_.CODIGO as codigoProduto, ")
        .append("        produto6_.NOME as nomeProduto, ")
        .append("        pessoajuri9_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi5_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi5_.PRECO_VENDA as precoCapa, ")
        .append("        movimentos2_.VALOR_DESCONTO as desconto, ")
        .append("        coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi5_.PRECO_VENDA) as precoComDesconto, ")
        .append("        sum(movimentos2_.QTDE) * coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi5_.PRECO_VENDA) as total,")
        .append("        sum(movimentos2_.QTDE) as encalhe,  ")
        .append("        chamadaEncalhe.sequencia as sequencia ")
        .append("from ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
        .append("left outer join ")
        .append("        COTA cota1_  ")
        .append("                on movimentof0_.COTA_ID=cota1_.ID  ")
        .append("left outer join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos2_  ")
        .append("                on movimentof0_.ID=movimentos2_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        .append("left outer join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro4_  ")
        .append("                on movimentos2_.ESTOQUE_PROD_COTA_ID=estoquepro4_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO_EDICAO produtoedi5_  ")
        .append("                on estoquepro4_.PRODUTO_EDICAO_ID=produtoedi5_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO produto6_  ")
        .append("                on produtoedi5_.PRODUTO_ID=produto6_.ID  ")
        .append("left outer join ")
        .append("        PRODUTO_FORNECEDOR fornecedor7_  ")
        .append("                on produto6_.ID=fornecedor7_.PRODUTO_ID  ")
        .append("left outer join ")
        .append("        FORNECEDOR fornecedor8_  ")
        .append("                on fornecedor7_.fornecedores_ID=fornecedor8_.ID  ")
        .append("left outer join ")
        .append("        PESSOA pessoajuri9_  ")
        .append("                on fornecedor8_.JURIDICA_ID=pessoajuri9_.ID  ")
        .append("left outer join ")
        .append("        TIPO_MOVIMENTO tipomovime3_  ")
        .append("                on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID  ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota ")
        .append("                on cota1_.ID = chamadaEncalheCota.COTA_ID ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE chamadaEncalhe ")
        .append("                on (produtoedi5_.ID = chamadaEncalhe.PRODUTO_EDICAO_ID ")
        .append("                and chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID) ")
        .append("inner join conferencia_encalhe conf on conf.chamada_encalhe_cota_id = chamadaEncalheCota.ID and conf.DATA = movimentos2_.data ")
        .append("where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and movimentof0_.DATA = :dataConsolidado ")
        .append("        and tipomovime3_.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinanceiro) ")
        .append("        and ( ")
        .append("                movimentof0_.ID not in  ( ")
        .append("                        select ")
        .append("                                movimentof12_.ID  ")
        .append("                        from ")
        .append("                                CONSOLIDADO_FINANCEIRO_COTA consolidad10_  ")
        .append("                        inner join ")
        .append("                                CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos11_  ")
        .append("                                        on consolidad10_.ID=movimentos11_.CONSOLIDADO_FINANCEIRO_ID  ")
        .append("                        inner join ")
        .append("                                MOVIMENTO_FINANCEIRO_COTA movimentof12_  ")
        .append("                                        on movimentos11_.MVTO_FINANCEIRO_COTA_ID=movimentof12_.ID ")
        .append("                        ) ")
        .append("                )  ")
        .append("    and movimentos2_.QTDE != 0 ")
        .append("        and chamadaEncalheCota.postergado = :naoPostergado ")

        .append("group by ")
        .append("        produto6_.CODIGO , ")
        .append("        produto6_.NOME , ")
        .append("        produtoedi5_.NUMERO_EDICAO , ")
        .append("        produtoedi5_.PRECO_VENDA , ")
        .append("        pessoajuri9_.RAZAO_SOCIAL ");
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (filtro.getOrdenacaoColuna() != null) {
            
            hql.append(" order by ");
            
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
                orderByColumn = " precoCapa ";
                break;
            case PRECO_COM_DESCONTO:
                orderByColumn = " precoComDesconto ";
                break;
            case ENCALHE:
                orderByColumn = " encalhe ";
                break;
            case FORNECEDOR:
                orderByColumn = " nomeFornecedor ";
                break;
            case TOTAL:
                orderByColumn = " total ";
                break;
            case SEQUENCIA:
                orderByColumn = " sequencia ";
                break;
            default:
                orderByColumn = " sequencia ";
                break;
            }
            
            hql.append(orderByColumn);
            
            if (paginacao.getOrdenacao() != null) {
                
                hql.append(paginacao.getOrdenacao().toString());
                
            }
        }
        else{
            
            hql.append(" order by sequencia ");
        }
        
        final SQLQuery query = getSession().createSQLQuery(hql.toString());
        
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("sequencia", StandardBasicTypes.LONG);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(EncalheCotaDTO.class));
        
        query.setParameter("numeroCota", filtro.getNumeroCota());
        query.setParameter("dataConsolidado", filtro.getDataConsolidado());
        query.setParameterList("grupoMovimentoFinanceiro",
                Arrays.asList(
                        GrupoMovimentoFinaceiro.ENVIO_ENCALHE.toString()
                        ));
        
        if (filtro.getIdConsolidado()!=null){
        	
	        query.setParameter("idConsolidado", filtro.getIdConsolidado());
        }
        
        query.setParameter("naoPostergado", false);
        
        if (paginacao != null &&
                paginacao.getQtdResultadosPorPagina() != null &&
                paginacao.getPaginaAtual() != null){
            
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
            query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(
            final FiltroConsolidadoVendaCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder("select STRAIGHT_JOIN");
        hql.append("        produto9_.CODIGO as codigoProduto, ")
        .append("        produto9_.NOME as nomeProduto, ")
        .append("        pessoajuri12_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi8_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi8_.PRECO_VENDA as precoCapa, ")
        .append("        vendaProduto.VALOR_DESCONTO as desconto, ")
        .append("        produtoedi8_.PRECO_VENDA-(produtoedi8_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100) as precoComDesconto, ")
        .append("        sum(vendaProduto.QNT_PRODUTO*(produtoedi8_.PRECO_VENDA-(produtoedi8_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100))) as total, ")
        .append("        vendaProduto.QNT_PRODUTO as exemplares ")
        .append("from ")
        .append("        CONSOLIDADO_FINANCEIRO_COTA consolidad0_ ")
        
        .append("join ")
        .append("        COTA cota1_  ")
        .append("                on consolidad0_.COTA_ID=cota1_.ID ")
        
        .append("join ")
        .append("        CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos3_  ")
        .append("                on consolidad0_.ID=movimentos3_.CONSOLIDADO_FINANCEIRO_ID  ")
        
        .append("join ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof4_  ")
        .append("                on movimentos3_.MVTO_FINANCEIRO_COTA_ID=movimentof4_.ID  ")
        
        .append("join ")
        .append("        VENDA_PRODUTO_MOVIMENTO_FINANCEIRO vendaProdutoMF  ")
        .append("                on vendaProdutoMF.ID_MOVIMENTO_FINANCEIRO = movimentof4_.ID ")
        
        .append("join ")
        .append("        VENDA_PRODUTO vendaProduto  ")
        .append("                on vendaProduto.ID = vendaProdutoMF.ID_VENDA_PRODUTO ")
        
        .append("join ")
        .append("        PRODUTO_EDICAO produtoedi8_  ")
        .append("                on vendaProduto.ID_PRODUTO_EDICAO=produtoedi8_.ID  ")
        
        .append("join ")
        .append("        PRODUTO produto9_  ")
        .append("                on produtoedi8_.PRODUTO_ID=produto9_.ID  ")
        
        .append("join ")
        .append("        PRODUTO_FORNECEDOR fornecedor10_  ")
        .append("                on produto9_.ID=fornecedor10_.PRODUTO_ID  ")
        
        .append("join ")
        .append("        FORNECEDOR fornecedor11_ ")
        .append("                on fornecedor10_.fornecedores_ID=fornecedor11_.ID  ")
        
        .append("join ")
        .append("        PESSOA pessoajuri12_ ")
        .append("                on fornecedor11_.JURIDICA_ID=pessoajuri12_.ID  ")
        
        .append("join ")
        .append("        TIPO_MOVIMENTO tipomovime13_ ")
        .append("                on movimentof4_.TIPO_MOVIMENTO_ID=tipomovime13_.ID ")
        
        .append("where   cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
        .append("        and tipomovime13_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
        
        .append("group by ")
        .append("        produto9_.CODIGO , ")
        .append("        produto9_.NOME , ")
        .append("        produtoedi8_.NUMERO_EDICAO , ")
        .append("        produtoedi8_.PRECO_VENDA , ")
        .append("        pessoajuri12_.RAZAO_SOCIAL ")
        
        .append("union all ")
        
        .append("select STRAIGHT_JOIN ")
        .append("        produto7_.CODIGO as codigoProduto, ")
        .append("        produto7_.NOME as nomeProduto, ")
        .append("        pessoajuri10_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi6_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi6_.PRECO_VENDA as precoCapa, ")
        .append("        vendaProduto.VALOR_DESCONTO as desconto, ")
        .append("        CASE WHEN (produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100)) IS NOT NULL THEN (produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100)) ELSE (vendaProduto.VALOR_TOTAL_VENDA / vendaProduto.QNT_PRODUTO) END as precoComDesconto, ")
        .append("        sum(CASE WHEN movimentos3_.QTDE*(produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100)) IS NOT NULL THEN movimentos3_.QTDE*(produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*vendaProduto.VALOR_DESCONTO/100)) ELSE vendaProduto.VALOR_TOTAL_VENDA END) as total, ")
        .append("        CASE WHEN movimentos3_.QTDE IS NOT NULL THEN movimentos3_.QTDE ELSE vendaProduto.QNT_PRODUTO END as exemplares  ")
        
        .append("from ")
        .append("        VENDA_PRODUTO vendaProduto  ")
        
        .append("join ")
        .append("        VENDA_PRODUTO_MOVIMENTO_FINANCEIRO vendaProdutoMF  ")
        .append("                on vendaProdutoMF.ID_VENDA_PRODUTO = vendaProduto.ID ")
        
        .append("join ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
        .append("                on movimentof0_.ID = vendaProdutoMF.ID_MOVIMENTO_FINANCEIRO  ")
        
        .append("join ")
        .append("        VENDA_PRODUTO_MOVIMENTO_ESTOQUE vendaProdutoME  ")
        .append("                on vendaProdutoME.ID_VENDA_PRODUTO = vendaProduto.ID ")
        
        .append("join ")
        .append("        MOVIMENTO_ESTOQUE movimentos3_  ")
        .append("                on movimentos3_.ID = vendaProdutoME.ID_MOVIMENTO_ESTOQUE  ")
        
        .append("join ")
        .append("        COTA cota1_  ")
        .append("                on movimentof0_.COTA_ID=cota1_.ID  ")
        
        .append("join ")
        .append("        PRODUTO_EDICAO produtoedi6_  ")
        .append("                on vendaProduto.ID_PRODUTO_EDICAO=produtoedi6_.ID  ")
        
        .append("join ")
        .append("        TIPO_MOVIMENTO tipomovime4_  ")
        .append("                on movimentos3_.TIPO_MOVIMENTO_ID=tipomovime4_.ID  ")
        
        .append("join ")
        .append("        PRODUTO produto7_  ")
        .append("                on produtoedi6_.PRODUTO_ID=produto7_.ID  ")
        
        .append("join ")
        .append("        PRODUTO_FORNECEDOR fornecedor8_  ")
        .append("                on produto7_.ID=fornecedor8_.PRODUTO_ID  ")
        
        .append("join ")
        .append("        FORNECEDOR fornecedor9_  ")
        .append("                on fornecedor8_.fornecedores_ID=fornecedor9_.ID  ")
        
        .append("join ")
        .append("        PESSOA pessoajuri10_  ")
        .append("                on fornecedor9_.JURIDICA_ID=pessoajuri10_.ID ")
        
        .append("join ")
        .append("        TIPO_MOVIMENTO tipomovime11_  ")
        .append("                on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime11_.ID ")
        
        .append("where   cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and movimentof0_.DATA= :dataConsolidado ")
        .append("        and tipomovime11_.GRUPO_MOVIMENTO_FINANCEIRO= :grupoMovimentoFinanceiro ")
        
        .append(" and movimentof0_.ID not in ( select MVTO_FINANCEIRO_COTA_ID ")
        .append("                              from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("                              inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("                              inner join COTA on COTA.ID = CON.COTA_ID ) ")
        
        .append("group by ")
        .append("        produto7_.CODIGO , ")
        .append("        produto7_.NOME , ")
        .append("        produtoedi6_.NUMERO_EDICAO , ")
        .append("        produtoedi6_.PRECO_VENDA , ")
        .append("        pessoajuri10_.RAZAO_SOCIAL  ");
        
        if (filtro.getOrdenacaoColuna() != null) {
            hql.append(" order by ");
            hql.append(filtro.getOrdenacaoColuna().toString());
            
            if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
                hql.append(" ").append(filtro.getPaginacao().getOrdenacao().toString());
            }
            
        }
        
        final SQLQuery query = getSession().createSQLQuery(hql.toString());
        
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("exemplares", StandardBasicTypes.BIG_INTEGER);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaVendaEncalheDTO.class));
        
        query.setParameter("numeroCota", filtro.getNumeroCota());
        query.setParameter("dataConsolidado", filtro.getDataConsolidado());
        query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR.name());
        
        if(filtro.getPaginacao() != null &&
                filtro.getPaginacao().getPosicaoInicial() != null &&
                filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
            
            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
        }
        
        return query.list();
    }
    
    /**
     * Obtem ordenacao da consulta do detalhe do consignado da conta corrente da
     * cota
     * 
     * @param filtro
     * @return StringBuilder
     */
    private StringBuilder getOrdenacaoConsignado(final FiltroConsolidadoConsignadoCotaDTO filtro) {
        
        final StringBuilder hql = new StringBuilder();
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (filtro.getOrdenacaoColuna() != null) {
            
            hql.append(" order by ");
            
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
                orderByColumn = " precoCapa ";
                break;
            case PRECO_COM_DESCONTO:
                orderByColumn = " precoComDesconto ";
                break;
            case REPARTE_SUGERIDO:
                orderByColumn = " reparteSugerido ";
                break;
            case REPARTE_FINAL:
                orderByColumn = " reparteFinal ";
                break;
            case DIFERENCA:
                orderByColumn = " diferenca ";
                break;
            case MOTIVO:
                orderByColumn = " motivoTexto ";
                break;
            case FORNECEDOR:
                orderByColumn = " nomeFornecedor ";
                break;
            case TOTAL:
                orderByColumn = " total ";
                break;
            case SEQUENCIA:
                orderByColumn = " sequencia ";
                break;
            default:
                orderByColumn = " sequencia ";
                break;
            }
            
            hql.append(orderByColumn);
            
            if (paginacao.getOrdenacao() != null) {
                
                hql.append(paginacao.getOrdenacao().toString());
                
            }
        }
        else{
            
            hql.append(" order by sequencia ");
        }
        
        return hql;
    }
    
    /**
     * Obtem query do detalhe do consignado da conta corrente da cota
     * @param hql
     * @param filtro
     * @return SQLQuery
     */
    private SQLQuery getSQLQueryConsignado(final StringBuilder hql, final FiltroConsolidadoConsignadoCotaDTO filtro) {
        
        final Session session = getSession();
        
        final SQLQuery query = session.createSQLQuery(hql.toString());
        
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeProduto", StandardBasicTypes.STRING);
        query.addScalar("codigoBarras", StandardBasicTypes.STRING);
        query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
        query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
        query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("reparteSugerido", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("reparteFinal", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("diferenca", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("motivoTexto", StandardBasicTypes.STRING);
        query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("sequencia", StandardBasicTypes.STRING);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaDTO.class));
        
        query.setParameter("numeroCota", filtro.getNumeroCota());
        query.setParameter("dataConsolidado", filtro.getDataConsolidado());
        query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE.toString());
        
        if (filtro.getIdConsolidado()!=null){
        	
        	query.setParameter("idConsolidado", filtro.getIdConsolidado());
        }
        
        return query;
    }
    
    /**
     * Obtem paginacao do detalhe do consignado da conta corrente da cota
     * @param query
     * @param filtro
     * @return SQLQuery
     */
    private SQLQuery getPaginacaoConsignado(final SQLQuery query, final FiltroConsolidadoConsignadoCotaDTO filtro){
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if (paginacao != null &&
                paginacao.getQtdResultadosPorPagina() != null &&
                paginacao.getPaginaAtual() != null){
            
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
            query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
        }
        
        return query;
    }
    
    /**
     * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para
     * conta corrente da Cota do tipo À Vista
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaAVistaConsignado(final FiltroConsolidadoConsignadoCotaDTO filtro){
        
        final StringBuilder hql = new StringBuilder("select STRAIGHT_JOIN ");
        
        hql.append("        produto11_.CODIGO as codigoProduto, ")
        
        .append("        produto11_.NOME as nomeProduto, ")
        
        .append("        pessoajuri14_.RAZAO_SOCIAL as nomeFornecedor, ")
        
        .append("        produtoedi8_.NUMERO_EDICAO as numeroEdicao, ")
        
        .append("        produtoedi8_.PRECO_VENDA as precoCapa, ")
        
        .append("        movimentos4_.VALOR_DESCONTO as desconto, ")
        
        .append("        coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as precoComDesconto, ")
        
        .append("        coalesce(movimentos4_.QTDE,0) as reparteSugerido, ")
        
        .append("        coalesce(movimentos4_.QTDE,0) as reparteFinal, ")
        
        .append("        coalesce(0,0) as diferenca, ")
        
        .append("        '' as motivoTexto, ")
        
        .append("        sum(movimentos4_.QTDE) * coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as total, ")
        
        
        .append("        ( ")
        
        .append("      case when cota1_.alteracao_tipo_cota is not null then ")
        
        .append("          case when movimentos4_.data <= cota1_.alteracao_tipo_cota then ")
        
        .append("          coalesce( ")
        
        .append("                      ( ")
        
        .append("                           select ce.sequencia ")
        
        .append("                           from chamada_encalhe ce, chamada_encalhe_cota cec ")
        
        .append("                               where cec.cota_id = cota1_.ID ")
        
        .append("                           and cec.chamada_encalhe_id = ce.id ")
        
        .append("                           and ce.produto_edicao_id = produtoedi8_.ID ")
        
        .append("                       ), ")
        
        .append("                       'Postergado' ")
        
        .append("                  ) ")
        
                .append("          else 'À Vista' end ")
        
                .append("      else 'À Vista' end ")
        
        .append("    ) as sequencia, ")
        .append("       produtoedi8_.codigo_de_barras as codigoBarras, ")
        .append("       produtoedi8_.chamada_capa as chamadaCapa ")
        
        
        .append("from CONSOLIDADO_FINANCEIRO_COTA consolidad0_  ")
        
        .append("inner join ")
        .append("        COTA cota1_  ")
        .append("                on consolidad0_.COTA_ID=cota1_.ID  ")
        
        .append("inner join ")
        .append("        CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos2_  ")
        .append("                on consolidad0_.ID=movimentos2_.CONSOLIDADO_FINANCEIRO_ID  ")
        
        .append("inner join ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof3_  ")
        .append("                on movimentos2_.MVTO_FINANCEIRO_COTA_ID=movimentof3_.ID  ")
        
        .append("inner join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos4_  ")
        .append("                on movimentof3_.ID=movimentos4_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        
        .append("inner join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro6_  ")
        .append("                on movimentos4_.ESTOQUE_PROD_COTA_ID=estoquepro6_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO_EDICAO produtoedi8_  ")
        .append("                on estoquepro6_.PRODUTO_EDICAO_ID=produtoedi8_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO produto11_  ")
        .append("                on produtoedi8_.PRODUTO_ID=produto11_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO_FORNECEDOR fornecedor12_  ")
        .append("                on produto11_.ID=fornecedor12_.PRODUTO_ID  ")
        
        .append("inner join ")
        .append("        FORNECEDOR fornecedor13_  ")
        .append("                on fornecedor12_.fornecedores_ID=fornecedor13_.ID  ")
        
        .append("inner join ")
        .append("        PESSOA pessoajuri14_  ")
        .append("                on fornecedor13_.JURIDICA_ID=pessoajuri14_.ID  ")
        
        .append("inner join ")
        .append("        TIPO_MOVIMENTO tipomovime5_  ")
        .append("                on movimentof3_.TIPO_MOVIMENTO_ID=tipomovime5_.ID  ")
        
        .append("where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
        .append("        and tipomovime5_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ");
        
        if (filtro.getIdConsolidado()!=null){
        	
        	 hql.append(" and consolidad0_.id = :idConsolidado ");
        }
        
        hql.append("group by ")
        .append("        produto11_.CODIGO , ")
        .append("        produto11_.NOME , ")
        .append("        produtoedi8_.NUMERO_EDICAO , ")
        .append("        produtoedi8_.PRECO_VENDA , ")
        .append("        pessoajuri14_.RAZAO_SOCIAL ")
        
        
        .append(" UNION ALL ")
        
        
        .append("select STRAIGHT_JOIN ")
        
        .append("        produto9_.CODIGO as codigoProduto, ")
        
        .append("        produto9_.NOME as nomeProduto, ")
        
        .append("        pessoajuri12_.RAZAO_SOCIAL as nomeFornecedor, ")
        
        .append("        produtoedi6_.NUMERO_EDICAO as numeroEdicao, ")
        
        .append("        produtoedi6_.PRECO_VENDA as precoCapa, ")
        
        .append("        movimentos2_.VALOR_DESCONTO as desconto, ")
        
        .append("        coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as precoComDesconto, ")
        
        .append("        coalesce(movimentos2_.QTDE,0) as reparteSugerido, ")
        
        .append("        coalesce(movimentos2_.QTDE,0) as reparteFinal, ")
        
        .append("        coalesce(0,0) as diferenca, ")
        
        .append("        '' as motivoTexto, ")
        
        .append("        sum(movimentos2_.QTDE) * coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as total, ")
        
        
        .append("        ( ")
        
        .append("      case when cota1_.alteracao_tipo_cota is not null then ")
        
        .append("          case when movimentos2_.data <= cota1_.alteracao_tipo_cota then ")
        
        .append("          coalesce( ")
        
        .append("                      ( ")
        
        .append("                           select ce.sequencia ")
        
        .append("                           from chamada_encalhe ce, chamada_encalhe_cota cec ")
        
        .append("                               where cec.cota_id = cota1_.ID ")
        
        .append("                           and cec.chamada_encalhe_id = ce.id ")
        
        .append("                           and ce.produto_edicao_id = produtoedi6_.ID ")
        
        .append("                       ), ")
        
        .append("                       'Postergado' ")
        
        .append("                  ) ")
        
                .append("          else 'À Vista' end ")
        
                .append("      else 'À Vista' end ")
        
        .append("    ) as sequencia, ")
        
        .append("       produtoedi6_.codigo_de_barras as codigoBarras, ")
        .append("       produtoedi6_.chamada_capa as chamadaCapa ")
      //  .append("       produto11_.chamada_capa as nomeEditor ")
        
        
        .append("from MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
        
        .append("inner join ")
        .append("        COTA cota1_  ")
        .append("                on movimentof0_.COTA_ID=cota1_.ID  ")
        
        .append("inner join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos2_  ")
        .append("                on movimentof0_.ID=movimentos2_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        
        .append("inner join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro4_  ")
        .append("                on movimentos2_.ESTOQUE_PROD_COTA_ID=estoquepro4_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO_EDICAO produtoedi6_  ")
        .append("                on estoquepro4_.PRODUTO_EDICAO_ID=produtoedi6_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO produto9_  ")
        .append("                on produtoedi6_.PRODUTO_ID=produto9_.ID  ")
        
        .append("inner join ")
        .append("        PRODUTO_FORNECEDOR fornecedor10_  ")
        .append("                on produto9_.ID=fornecedor10_.PRODUTO_ID  ")
        
        .append("inner join ")
        .append("        FORNECEDOR fornecedor11_  ")
        .append("                on fornecedor10_.fornecedores_ID=fornecedor11_.ID  ")
        
        .append("inner join ")
        .append("        PESSOA pessoajuri12_  ")
        .append("                on fornecedor11_.JURIDICA_ID=pessoajuri12_.ID  ")
        
        .append("inner join ")
        .append("        TIPO_MOVIMENTO tipomovime3_  ")
        .append("                on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID  ")
        
        .append("where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and movimentof0_.DATA = :dataConsolidado ")
        .append("        and tipomovime3_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
        .append("        and ( ")
        .append("                movimentof0_.ID not in  ( ")
        .append("                        select ")
        .append("                                movimentof15_.ID  ")
        .append("                        from ")
        .append("                                CONSOLIDADO_FINANCEIRO_COTA consolidad13_  ")
        .append("                        inner join ")
        .append("                                CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos14_  ")
        .append("                                        on consolidad13_.ID=movimentos14_.CONSOLIDADO_FINANCEIRO_ID  ")
        .append("                        inner join ")
        .append("                                MOVIMENTO_FINANCEIRO_COTA movimentof15_  ")
        .append("                                        on movimentos14_.MVTO_FINANCEIRO_COTA_ID=movimentof15_.ID ")
        .append("                        ) ")
        .append("                )  ")
        .append("group by ")
        .append("        produto9_.CODIGO , ")
        .append("        produto9_.NOME , ")
        .append("        produtoedi6_.NUMERO_EDICAO , ")
        .append("        produtoedi6_.PRECO_VENDA , ")
        .append("        pessoajuri12_.RAZAO_SOCIAL ");
        
        hql.append(this.getOrdenacaoConsignado(filtro));
        
        SQLQuery query = this.getSQLQueryConsignado(hql, filtro);
        
        query = this.getPaginacaoConsignado(query, filtro);
        
        return query.list();
    }
    
    /**
     * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para
     * conta corrente da Cota
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(final FiltroConsolidadoConsignadoCotaDTO filtro){
        
        final StringBuilder hql = new StringBuilder("select STRAIGHT_JOIN ");
        
        hql.append(" consignados.*, reparteFinal * preco as total from ( ")
        .append("        select ")
        .append("        produtoedi8_.ID as idProdutoEdicao, ")
        .append("		 movimentos4_.ID as idMovimentoEstoqueCota, ")
        .append("        produto11_.CODIGO as codigoProduto, ")
        .append("        produto11_.NOME as nomeProduto, ")
        .append("        pessoajuri14_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi8_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi8_.PRECO_VENDA as precoCapa, ")
        .append("        movimentos4_.VALOR_DESCONTO as desconto, ")
        .append("        coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as precoComDesconto, ")
        .append("        sum(coalesce(estudocota7_.QTDE_PREVISTA, 0)) as reparteSugerido, ")
        //.append("        sum(coalesce(movimentos4_.QTDE, 0)) as reparteFinal, ")
        .append("        coalesce(chamadaEncalheCota.QTDE_PREVISTA,0) as reparteFinal, ")
        .append("        coalesce(estudocota7_.QTDE_PREVISTA-estudocota7_.QTDE_EFETIVA,0) as diferenca, ")
        .append("        case when diferenca10_.TIPO_DIFERENCA is null then '' else diferenca10_.TIPO_DIFERENCA end as motivoTexto, ")
        .append("        movimentos4_.QTDE as qtde, ")
        .append(" 		 coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as preco, ")
        .append("        chamadaEncalhe.sequencia as sequencia, ")
        .append("       produtoedi8_.codigo_de_barras as codigoBarras, ")
        .append("       produtoedi8_.chamada_capa as chamadaCapa ")
      //  .append("       produto11_.chamada_capa as nomeEditor ")
        .append(" from ")
        .append("        CONSOLIDADO_FINANCEIRO_COTA consolidad0_  ")
        .append(" inner join ")
        .append("        COTA cota1_  ")
        .append("                on consolidad0_.COTA_ID=cota1_.ID  ")
        .append(" inner join ")
        .append("        CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos2_  ")
        .append("                on consolidad0_.ID=movimentos2_.CONSOLIDADO_FINANCEIRO_ID  ")
        .append(" inner join ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof3_  ")
        .append("                on movimentos2_.MVTO_FINANCEIRO_COTA_ID=movimentof3_.ID  ")
        .append(" inner join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos4_  ")
        .append("                on movimentof3_.ID=movimentos4_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        .append(" inner join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro6_  ")
        .append("                on movimentos4_.ESTOQUE_PROD_COTA_ID=estoquepro6_.ID  ")
        .append(" inner join ")
        .append("        PRODUTO_EDICAO produtoedi8_  ")
        .append("                on estoquepro6_.PRODUTO_EDICAO_ID=produtoedi8_.ID  ")
        .append(" inner join ")
        .append("        PRODUTO produto11_  ")
        .append("                on produtoedi8_.PRODUTO_ID=produto11_.ID  ")
        .append(" inner join ")
        .append("        PRODUTO_FORNECEDOR fornecedor12_  ")
        .append("                on produto11_.ID=fornecedor12_.PRODUTO_ID  ")
        .append(" inner join ")
        .append("        FORNECEDOR fornecedor13_  ")
        .append("                on fornecedor12_.fornecedores_ID=fornecedor13_.ID  ")
        .append(" inner join ")
        .append("        PESSOA pessoajuri14_  ")
        .append("                on fornecedor13_.JURIDICA_ID=pessoajuri14_.ID  ")
        .append(" left outer join ")
        .append("        ESTUDO_COTA estudocota7_  ")
        .append("                on movimentos4_.ESTUDO_COTA_ID=estudocota7_.ID  ")
        .append(" left outer join ")
        .append("        RATEIO_DIFERENCA rateiosdif9_  ")
        .append("                on estudocota7_.ID=rateiosdif9_.ESTUDO_COTA_ID  ")
        .append(" left outer join ")
        .append("        DIFERENCA diferenca10_  ")
        .append("                on rateiosdif9_.DIFERENCA_ID=diferenca10_.id  ")
        .append(" inner join ")
        .append("        TIPO_MOVIMENTO tipomovime5_  ")
        .append("                on movimentof3_.TIPO_MOVIMENTO_ID=tipomovime5_.ID  ")
        .append(" inner join ")
        .append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota ")
        .append("                on cota1_.ID = chamadaEncalheCota.COTA_ID ")
        .append(" inner join ")
        .append("        CHAMADA_ENCALHE chamadaEncalhe ")
        .append("                on (produtoedi8_.ID = chamadaEncalhe.PRODUTO_EDICAO_ID ")
        .append("                and chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID) ")
        .append(" where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
        .append("        and chamadaEncalhe.DATA_RECOLHIMENTO = consolidad0_.DT_CONSOLIDADO ")
        .append("        and tipomovime5_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
        .append("        and chamadaEncalheCota.postergado = :naoPostergado ");
        
        if (filtro.getIdConsolidado()!=null){
       	    hql.append(" and consolidad0_.id = :idConsolidado ");
        }

        hql.append(" group by ").append("        idProdutoEdicao ")
        .append("union all ")
        .append("select STRAIGHT_JOIN ")
        .append("        produtoedi6_.ID as idProdutoEdicao, ")
        .append("        movimentos2_.ID as idMovimentoEstoqueCota, ")
        .append("        produto9_.CODIGO as codigoProduto, ")
        .append("        produto9_.NOME as nomeProduto, ")
        .append("        pessoajuri12_.RAZAO_SOCIAL as nomeFornecedor, ")
        .append("        produtoedi6_.NUMERO_EDICAO as numeroEdicao, ")
        .append("        produtoedi6_.PRECO_VENDA as precoCapa, ")
        .append("        movimentos2_.VALOR_DESCONTO as desconto, ")
        .append("        coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as precoComDesconto, ")
        .append("        sum(coalesce(estudocota5_.QTDE_PREVISTA, 0)) as reparteSugerido, ")
        .append("        sum(coalesce(estudocota5_.QTDE_EFETIVA, 0)) as reparteFinal, ")
        .append("        estudocota5_.QTDE_PREVISTA-estudocota5_.QTDE_EFETIVA as diferenca, ")
        .append("        diferenca8_.TIPO_DIFERENCA as motivoTexto, ")
        .append("        movimentos2_.QTDE as qtde, ")
        .append("   	 coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as preco, ")
        .append("        chamadaEncalhe.sequencia as sequencia ,")
        .append("        produtoedi6_.codigo_de_barras as codigoBarras, ")
        .append("        produtoedi6_.chamada_capa as chamadaCapa ")
      //  .append("       produto11_.chamada_capa as nomeEditor ")
        .append("from ")
        .append("        MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
        .append("inner join ")
        .append("        COTA cota1_  ")
        .append("                on movimentof0_.COTA_ID=cota1_.ID  ")
        .append("inner join ")
        .append("        MOVIMENTO_ESTOQUE_COTA movimentos2_  ")
        .append("                on movimentof0_.ID=movimentos2_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
        .append("inner join ")
        .append("        ESTOQUE_PRODUTO_COTA estoquepro4_  ")
        .append("                on movimentos2_.ESTOQUE_PROD_COTA_ID=estoquepro4_.ID  ")
        .append("inner join ")
        .append("        PRODUTO_EDICAO produtoedi6_  ")
        .append("                on estoquepro4_.PRODUTO_EDICAO_ID=produtoedi6_.ID  ")
        .append("inner join ")
        .append("        PRODUTO produto9_  ")
        .append("                on produtoedi6_.PRODUTO_ID=produto9_.ID  ")
        .append("inner join ")
        .append("        PRODUTO_FORNECEDOR fornecedor10_  ")
        .append("                on produto9_.ID=fornecedor10_.PRODUTO_ID  ")
        .append("inner join ")
        .append("        FORNECEDOR fornecedor11_  ")
        .append("                on fornecedor10_.fornecedores_ID=fornecedor11_.ID  ")
        .append("inner join ")
        .append("        PESSOA pessoajuri12_  ")
        .append("                on fornecedor11_.JURIDICA_ID=pessoajuri12_.ID  ")
        .append("left outer join ")
        .append("        ESTUDO_COTA estudocota5_  ")
        .append("                on movimentos2_.ESTUDO_COTA_ID=estudocota5_.ID  ")
        .append("left outer join ")
        .append("        RATEIO_DIFERENCA rateiosdif7_  ")
        .append("                on estudocota5_.ID=rateiosdif7_.ESTUDO_COTA_ID  ")
        .append("left outer join ")
        .append("        DIFERENCA diferenca8_  ")
        .append("                on rateiosdif7_.DIFERENCA_ID=diferenca8_.id  ")
        .append("inner join ")
        .append("        TIPO_MOVIMENTO tipomovime3_  ")
        .append("                on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID  ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE_COTA chamadaEncalheCota ")
        .append("                on cota1_.ID = chamadaEncalheCota.COTA_ID ")
        .append("inner join ")
        .append("        CHAMADA_ENCALHE chamadaEncalhe ")
        .append("                on (produtoedi6_.ID = chamadaEncalhe.PRODUTO_EDICAO_ID ")
        .append("                and chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID) ")
        .append("where ")
        .append("        cota1_.NUMERO_COTA = :numeroCota ")
        .append("        and movimentof0_.DATA = :dataConsolidado ")
        .append("        and tipomovime3_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
        .append("        and chamadaEncalheCota.postergado = :naoPostergado ")
        .append("        and ( ")
        .append("                movimentof0_.ID not in  ( ")
        .append("                        select ")
        .append("                                movimentof15_.ID  ")
        .append("                        from ")
        .append("                                CONSOLIDADO_FINANCEIRO_COTA consolidad13_  ")
        .append("                        inner join ")
        .append("                                CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos14_  ")
        .append("                                        on consolidad13_.ID=movimentos14_.CONSOLIDADO_FINANCEIRO_ID  ")
        .append("                        inner join ")
        .append("                                MOVIMENTO_FINANCEIRO_COTA movimentof15_  ")
        .append("                                        on movimentos14_.MVTO_FINANCEIRO_COTA_ID=movimentof15_.ID ")
        .append("                        ) ")
        .append("                )  ")        
        .append("        and chamadaEncalhe.DATA_RECOLHIMENTO = :dataConsolidado ")
        .append("group by ")
        .append("        idProdutoEdicao ");
        
        hql.append(" ) as consignados ");
        hql.append(" group by idProdutoEdicao ");
        
        hql.append(this.getOrdenacaoConsignado(filtro));
        
        SQLQuery query = this.getSQLQueryConsignado(hql, filtro);
        
        query.setParameter("naoPostergado", false);
        
        query = this.getPaginacaoConsignado(query, filtro);
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsolidadoFinanceiroCota> obterConsolidadoPorIdMovimentoFinanceiro(final Long idMovimentoFinanceiro) {
        
        final StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c join c.movimentos mov ");
        hql.append(" where mov.id = :idMovimentoFinanceiro ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
        
        return query.list();
    }
    
    /* (non-Javadoc)
     * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarUltimaBaixaAutomaticaDia(java.util.Date)
     */
    @Override
    public Date buscarUltimaDividaGeradaDia(final Date dataOperacao) {
        final Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
        criteria.add(Restrictions.eq("dataConsolidado", dataOperacao));
        criteria.setProjection(Projections.max("dataConsolidado"));
        return (Date) criteria.uniqueResult();
    }
    
    /* (non-Javadoc)
     * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarDiaUltimaBaixaAutomatica()
     */
    @Override
    public Date buscarDiaUltimaDividaGerada() {
        final Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
        criteria.setProjection(Projections.max("dataConsolidado"));
        return (Date) criteria.uniqueResult();
    }
    
    @Override
    public Long obterQuantidadeDividasGeradasData(final List<Long> idsCota) {
        
        final StringBuilder hql = new StringBuilder("select count(consolidado.id) ");
        hql.append(" from ConsolidadoFinanceiroCota consolidado ")
        .append(" join consolidado.cota cota ")
        .append(" where consolidado.dataConsolidado = (select d.dataOperacao from Distribuidor d) ")
        .append(" and consolidado.id not in (")
        .append(" select c.id from Divida d join d.consolidados c where c.id = consolidado.id ")
        .append(" and d.origemNegociacao = false ")
        .append(")");
        
        if (idsCota != null) {
            
            hql.append("and cota.id in (:idsCota)");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (idsCota != null) {
            
            query.setParameterList("idsCota", idsCota);
        }
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    public Long obterQuantidadeDividasGeradasData(final Date dataVencimentoDebito, final Long... idsCota) {
        
        final StringBuilder hql = new StringBuilder("select count(c.id) ");
        hql.append(" from ConsolidadoFinanceiroCota c, Divida divida ")
        .append(" join divida.consolidados cons ")
        .append(" where c.dataConsolidado = :dataVencimentoDebito ")
        .append(" and c.id = cons.id ")
        .append(" and divida.data = :dataVencimentoDebito ");
        
        if (idsCota != null) {
            
            hql.append("and c.cota.id in (:idsCota)");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        if (idsCota != null) {
            
            query.setParameterList("idsCota", idsCota);
        }
        
        query.setParameter("dataVencimentoDebito", dataVencimentoDebito);
        
        return (Long) query.uniqueResult();
    }
    
    @Override
    public ConsolidadoFinanceiroCota buscarPorCotaEData(final Long idCota, final Date data) {
        
        final StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c ");
        hql.append(" join c.cota cota ");
        hql.append(" where c.dataConsolidado = :data ");
        hql.append(" and cota.id = :idCota ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("data", data);
        query.setParameter("idCota", idCota);
        
        query.setMaxResults(1);
        
        return (ConsolidadoFinanceiroCota) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<DebitoCreditoCota> buscarMovFinanPorCotaEData(final Long idCota, final List<Date> datas, final Long idFornecedor) {
        
        final StringBuilder hql = new StringBuilder("select new ");
        hql.append(DebitoCreditoCota.class.getCanonicalName())
           .append(" (movs.valor as valor, ")
           .append(" movs.observacao as observacoes, ")
           .append(" tpMov.operacaoFinaceira as tipoLancamento, ")
           .append(" tpMov.grupoMovimentoFinaceiro as tipoMovimento, ")
           .append(" movs.data as dataLancamento, " )
           .append(" movs.dataCriacao as dataVencimento) ")
           .append(" from ConsolidadoFinanceiroCota c ")
           .append(" join c.movimentos movs ")
           .append(" join movs.fornecedor fornecedor ")
           .append(" join movs.tipoMovimento tpMov ")
           .append(" join c.cota cota ")
           .append(" where c.dataConsolidado IN (:datas) ")
           .append(" and cota.id = :idCota ")
           .append(" and tpMov.grupoMovimentoFinaceiro not in (:grupoIgnorar) ");
        
        if (idFornecedor != null){
            
            hql.append(" and fornecedor.id = :idFornecedor ");
        }
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameterList("datas", datas);
        
        query.setParameter("idCota", idCota);
        
        query.setParameterList("grupoIgnorar", Arrays.asList(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE,
                                                             GrupoMovimentoFinaceiro.ENVIO_ENCALHE));
        
        if (idFornecedor != null){
            
            query.setParameter("idFornecedor", idFornecedor);
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ConsolidadoFinanceiroCota> obterConsolidadosDataOperacao(final Long idCota, Date dataOperacao) {
        
        final StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c ");
        
        hql.append(" join c.cota cota ");
        
        hql.append(" where c.dataConsolidado = :dataOperacao ");
            
        hql.append(" and cota.id = :idCota ");
        
        final Query query = this.getSession().createQuery(hql.toString());
        
        query.setParameter("idCota", idCota);
        
        query.setParameter("dataOperacao", dataOperacao);
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ContaCorrenteVO> obterContaCorrenteExtracao(final FiltroViewContaCorrenteDTO filtro) {
    	final StringBuilder sql = new StringBuilder("select ");      
    	sql.append(" DT_CONSOLIDADO as dataConsolidado,");
    	sql.append(" numero_cota as numeroCota, ");
		sql.append(" consignado as consignado,");
		sql.append(" encalhe as encalhe, ");
		sql.append(" 	(consignado - encalhe) as valorVendaDia,");
		sql.append(" valor_postergado *-1 as valorPostergado, ");
		sql.append(" venda_encalhe as vendaEncalhe,");
		sql.append(" debito_credito*-1 as debitoCredito,");
		sql.append(" encargos as encargos,");
		sql.append(" pendente as pendente, ");
		sql.append(" (total *-1) total, ");
		sql.append(" 	b.situacao_cadastro as situacaoCadastro, ");
		sql.append(" 	d.status as legenda ");
		sql.append(" 	from consolidado_financeiro_cota a, cota b, divida d ");
		sql.append(" 	where DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo");
		sql.append(" 	and a.cota_id = b.id ");
		sql.append(" 	and d.cota_id = b.id ");
		sql.append(" 	group by dataConsolidado, numeroCota ");
	    sql.append(" 	order by 1,2");
         
        final Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
        query.setParameter("fimPeriodo", filtro.getFimPeriodo());
      
        query.setResultTransformer(new AliasToBeanResultTransformer(ContaCorrenteVO.class));
        return  query.list();
    }
  
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ContaCorrenteCotaVO> obterContaCorrente(final FiltroViewContaCorrenteCotaDTO filtro,
            final List<Long> tiposMovimentoCredito, final List<Long> tiposMovimentoDebito,
            final List<Long> tipoMovimentoEncalhe, final List<Long> tiposMovimentoEncargos,
            final List<Long> tiposMovimentoPostergadoCredito, final List<Long> tiposMovimentoPostergadoDebito,
            final List<Long> tipoMovimentoVendaEncalhe, final List<Long> tiposMovimentoConsignado,
            final List<Long> tiposMovimentoPendente, final List<Long> tiposMovimentoNegociacaoComissao) {
        
        final StringBuilder sql = new StringBuilder("select ");
        sql.append(" cfc.ID as id, ")
        .append(" cfc.COTA_ID as cotaId, ")
        .append(" COTA.NUMERO_COTA as numeroCota, ")
                .append(" COALESCE(box.NOME, 'Box não Cadastrado para essa Cota') as nomeBox, ")
        .append(" cfc.CONSIGNADO as consignado, ")
        
        .append(" cfc.DT_CONSOLIDADO as dataConsolidado, ")
        
        .append(" (select max(mf_ant.DATA) ")
        .append(" from consolidado_financeiro_cota cfc_data ")
        .append(" join consolidado_mvto_financeiro_cota cmfc_data_data on cmfc_data_data.CONSOLIDADO_FINANCEIRO_ID=cfc_data.ID ")
        .append(" join movimento_financeiro_cota mfc_data on  mfc_data.ID=cmfc_data_data.MVTO_FINANCEIRO_COTA_ID ")
        .append(" join tipo_movimento tm on tm.ID=mfc_data.TIPO_MOVIMENTO_ID ")
        .append(" join acumulo_divida ad_data on ad_data.MOV_PENDENTE_ID=mfc_data.ID ")
        .append(" join divida d_data on d_data.ID=ad_data.DIVIDA_ID ")
        .append(" join divida_consolidado d_cons on d_data.ID = d_cons.DIVIDA_ID ")
        .append(" join consolidado_financeiro_cota cfc_ant on cfc_ant.ID=d_cons.CONSOLIDADO_ID ")
        .append(" join consolidado_mvto_financeiro_cota cmfc_data_data_ant on cmfc_data_data_ant.CONSOLIDADO_FINANCEIRO_ID=cfc_ant.ID ")
        .append(" join movimento_financeiro_cota mf_ant on mf_ant.ID=cmfc_data_data_ant.MVTO_FINANCEIRO_COTA_ID ")
        .append(" join tipo_movimento tm_ant on tm_ant.ID=mf_ant.TIPO_MOVIMENTO_ID ")
        .append(" where tm.GRUPO_MOVIMENTO_FINANCEIRO=:grupoMovPendente ")
        .append(" and cfc_data.ID=cfc.ID limit 1) as dataPendente, ")
        
        .append(" (select max(ad_data.NUMERO_ACUMULO) ")
        .append(" from consolidado_financeiro_cota cfc_data ")
        .append(" join consolidado_mvto_financeiro_cota cmfc_data_data on cmfc_data_data.CONSOLIDADO_FINANCEIRO_ID=cfc_data.ID ")
        .append(" join movimento_financeiro_cota mfc_data on  mfc_data.ID=cmfc_data_data.MVTO_FINANCEIRO_COTA_ID ")
        .append(" join tipo_movimento tm on tm.ID=mfc_data.TIPO_MOVIMENTO_ID ")
        .append(" join acumulo_divida ad_data on ad_data.MOV_PENDENTE_ID=mfc_data.ID ")
        .append(" where tm.GRUPO_MOVIMENTO_FINANCEIRO=:grupoMovPendente ")
        .append(" and cfc_data.ID=cfc.ID ")
        .append(" and COTA.ID=cfc_data.COTA_ID limit 1) as numeroAcumulo, ")
        
        .append(" cfc.DEBITO_CREDITO as debitoCredito, ")
        
        //detalharDebitoCredito
        .append(" if(cfc.DEBITO_CREDITO = 0,false,(select case when count(m.id) > 0 then true else false end ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and (m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) or m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ) ")
        .append(" and m.ID in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA <= cfc.DT_CONSOLIDADO ")
        .append("))")
        .append(" as detalharDebitoCredito, ")
        
        .append(" cfc.ENCALHE as encalhe, ")
        .append(" cfc.ENCARGOS as encargos, ")
        .append(" ROUND(cfc.PENDENTE,2) as pendente, ")
        .append(" cfc.VALOR_POSTERGADO as valorPostergado, ")
        .append(" cfc.VENDA_ENCALHE as vendaEncalhe, ")
        
        //cobrado/postergado
        .append(" ((select cob.ID from COBRANCA cob where cob.DIVIDA_ID = divida.ID) is not null ) AS cobrado, ")
        
        //status divida
        .append(" (divida.status) as statusDivida,")
        
        //data raiz postergado
        .append(" (select max(mfp.DATA_CRIACAO)  ")
        .append(" from  MOVIMENTO_FINANCEIRO_COTA mfp ")
        .append(" join consolidado_mvto_financeiro_cota cmfc on cmfc.MVTO_FINANCEIRO_COTA_ID=mfp.id ")
        .append(" where mfp.COTA_ID = cfc.COTA_ID ")
        .append(" AND mfp.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoCredito,:tiposMovimentoPostergadoDebito) ")
      //  .append(" OR mfp.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoDebito) ")
        .append(" AND cmfc.CONSOLIDADO_FINANCEIRO_ID=cfc.id ")
        .append(" limit 1 )")
        .append(" AS dataRaiz, ")

        //Valor Pago
        .append(" ROUND(")
               
        .append("    COALESCE( ")
        //divida paga em cota unificadora: valor pago = total na cota unificada
        .append("    (SELECT CF.TOTAL ")
        .append("     FROM DIVIDA D, DIVIDA_CONSOLIDADO DC, CONSOLIDADO_FINANCEIRO_COTA CF ")
        .append("     WHERE DC.DIVIDA_ID = D.ID ")
        .append("     AND DC.CONSOLIDADO_ID = CF.ID ")
        .append("     AND CF.ID = cfc.ID ")
        .append("     AND D.COTA_ID <> cota.ID ")
        .append("     AND D.STATUS = :statusDividaQuitada) * (-1) ")
        
        .append("    ,")
        
        .append("    (select SUM( coalesce(bc.VALOR_PAGO, 0) ) - ")
        //consolidados de cotas unificadas subtraidos do valor pago na cota unificadora
        .append("    COALESCE(((SELECT SUM(ROUND(CF.TOTAL,2)) ")
        .append("               FROM DIVIDA_CONSOLIDADO DC, CONSOLIDADO_FINANCEIRO_COTA CF ")
        .append("               WHERE DC.DIVIDA_ID = divida.ID ")
        .append("               AND DC.CONSOLIDADO_ID = CF.ID ")
        .append("               AND CF.ID <> cfc.ID ")
        .append("               AND CF.COTA_ID <> cota.ID) * (-1)),0) ")

        .append("     from BAIXA_COBRANCA bc ")
        .append("     inner join COBRANCA cobranca ON cobranca.ID = bc.COBRANCA_ID ")
        .append("     inner join DIVIDA divida ON divida.ID = cobranca.DIVIDA_ID ")
        .append("     inner join DIVIDA_CONSOLIDADO d_cons ON divida.ID = d_cons.DIVIDA_ID ")
        .append("     where bc.STATUS not in (:statusBaixaCobranca) ")
        .append("     and cota.ID = cobranca.COTA_ID ")
        .append("     and d_cons.CONSOLIDADO_ID = cfc.ID ")
        .append("     and cfc.ID) ")
 
        .append("    ,0)")
        
        .append(" ,2) as valorPago, ")

        //total
        .append(" ROUND(cfc.TOTAL, 2) as total, ")
        
        //CALCULO DO SALDO = total - valorPago
        .append(" ROUND(")
        .append(" ( ")
        .append("          SELECT CASE WHEN bc.status = :naoPagoPostergado THEN 0 ")
        .append("          else ") 
        
        .append("              round(cfc.TOTAL, 2) + SUM(coalesce(bc.VALOR_PAGO, 0)) - SUM(coalesce(bc.VALOR_JUROS, 0) + coalesce(bc.VALOR_MULTA, 0) - coalesce(bc.VALOR_DESCONTO,0)) ") 
        
        .append("              - ")
        //consolidados de cotas unificadas subtraidos do saldo da cota unificadora
        .append("              COALESCE(((SELECT SUM(ROUND(CF.TOTAL,2)) ")
        .append("                         FROM DIVIDA_CONSOLIDADO DC, CONSOLIDADO_FINANCEIRO_COTA CF ")
        .append("                         WHERE DC.DIVIDA_ID = divida.ID ")
        .append("                         AND DC.CONSOLIDADO_ID = CF.ID ")
        .append("                         AND CF.ID <> cfc.ID ")
        .append("                         AND CF.COTA_ID <> cota.ID) * (-1)),0) ")
        
        .append("          end ")
        .append("          FROM BAIXA_COBRANCA bc ")
        .append("          INNER JOIN COBRANCA cobranca ON cobranca.ID = bc.COBRANCA_ID ")
        .append("          INNER JOIN DIVIDA divida ON divida.ID = cobranca.DIVIDA_ID ")
        .append("		   INNER JOIN DIVIDA_CONSOLIDADO d_cons ON divida.ID = d_cons.DIVIDA_ID ")
        .append("          WHERE bc.STATUS NOT IN (:statusBaixaCobranca)  ")
        .append("          AND cota.ID = cobranca.COTA_ID AND d_cons.CONSOLIDADO_ID = cfc.ID AND cfc.ID  ")
        .append(" ),2) as saldo, ")
        
        .append(" coalesce(cfc.CONSIGNADO,0) - coalesce(cfc.ENCALHE,0) ")
        .append(" as valorVendaDia, ")
        
        .append(" case when divida.STATUS = :statusPendenteInadimplencia then 1 else 0 end as inadimplente ")
        
        .append(", mf.DATA as dataMovimento ")
        
        .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
        .append(" inner join consolidado_mvto_financeiro_cota cmfc on cmfc.CONSOLIDADO_FINANCEIRO_ID = cfc.ID")
        .append(" inner join movimento_financeiro_cota mf on mf.ID = cmfc.MVTO_FINANCEIRO_COTA_ID")
        .append(" inner join COTA cota on cota.ID = cfc.COTA_ID")
        .append(" left join DIVIDA_CONSOLIDADO d_cons on d_cons.CONSOLIDADO_ID = cfc.ID ")
        .append(" left join DIVIDA divida on divida.ID = d_cons.DIVIDA_ID ")
        .append(" left join BOX box on cota.BOX_ID = box.ID")
        .append(" left join DIVIDA dividaRaiz on divida.DIVIDA_RAIZ_ID = dividaRaiz.ID ")
        .append(" where cota.NUMERO_COTA = :numeroCota ");
        
        if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
            
            sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
        }

        sql.append(" group by cfc.ID ")
        
        .append(" union all ")
        
        .append(" select ")
        .append(" null as id, ")
        .append(" mfc.COTA_ID as cotaId, ")
        .append(" null as numeroCota, ")
                .append(" COALESCE(box.NOME, 'Box não Cadastrado para essa Cota') as nomeBox, ")
        
        //consignado
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoConsignado) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) as consignado, ")
        
        .append(" mfc.DATA as dataConsolidado, ")
        
        .append(" (select max(mf_ant.DATA) ")
        .append(" from movimento_financeiro_cota mfc_data ")
        .append(" join tipo_movimento tm_data on tm_data.ID=mfc_data.TIPO_MOVIMENTO_ID ")
        .append(" join acumulo_divida ad_data on ad_data.MOV_PENDENTE_ID=mfc_data.ID ")
        .append(" join divida d_data on d_data.ID=ad_data.DIVIDA_ID ")
        .append(" join divida_consolidado d_cons on d_cons.DIVIDA_ID = d_data.ID ")
        .append(" join consolidado_financeiro_cota cfc_ant on cfc_ant.ID=d_cons.CONSOLIDADO_ID ")
        .append(" join consolidado_mvto_financeiro_cota cmfc_data_ant on cmfc_data_ant.CONSOLIDADO_FINANCEIRO_ID=cfc_ant.ID ")
        .append(" join movimento_financeiro_cota mf_ant on mf_ant.ID=cmfc_data_ant.MVTO_FINANCEIRO_COTA_ID ")
        .append(" join tipo_movimento tm_ant on tm_ant.ID=mf_ant.TIPO_MOVIMENTO_ID ")
        .append(" where tm_data.GRUPO_MOVIMENTO_FINANCEIRO=:grupoMovPendente ")
        .append(" and mfc_data.ID=mfc.ID limit 1) as dataPendente, ")
        
        .append(" (select max(ad_data.NUMERO_ACUMULO) ")
        .append(" from movimento_financeiro_cota mfc_data ")
        .append(" join tipo_movimento tm on tm.ID=mfc_data.TIPO_MOVIMENTO_ID ")
        .append(" join acumulo_divida ad_data on ad_data.MOV_PENDENTE_ID=mfc_data.ID ")
        .append(" where tm.GRUPO_MOVIMENTO_FINANCEIRO=:grupoMovPendente ")
        .append(" and mfc_data.ID=mfc.ID ")
        .append(" and COTA.ID=mfc_data.COTA_ID limit 1) as numeroAcumulo, ")
        
                // crédito
        .append("(coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) - ")
                // débito
        .append(" coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)")
        .append(") as debitoCredito, ")
        
        //detalharDebitoCredito
        .append("(select case when count(m.id) > 0  then true else false end ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and (m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) or m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append(")")
        .append(" as detalharDebitoCredito, ")
        
        //encalhe
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) as encalhe, ")
        
        //encargos
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoEncargos) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) as encargos, ")
        
        //pendente
        .append("ROUND(")
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPendente) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0),2) as pendente, ")
        
        //valorPostergado
        //valorPostergado credito
        .append("(coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoCredito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) - ")
                // valorPostergado débito
        .append(" coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoDebito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)")
        .append(") as valorPostergado, ")
        
        //vendaEncalhe
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoVendaEncalhe) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) as vendaEncalhe, ")
        
        //cobrado
        .append(" false as cobrado, ")
        
        //status divida
        .append(" ( select divida.status from cobranca cobranca ")
        .append("   join divida divida on divida.id = cobranca.divida_id  ")
        .append("   join cota cota on cota.id = divida.cota_id ")
        .append("   where cota.id = mfc.COTA_ID ")
        .append("   and  divida.data = mfc.data limit 1 ) as statusDivida,")
        
        //data raiz
        .append(" (select ")
        .append("  max(mfp.DT_CONSOLIDADO) ")
        .append("  from ")
        .append("  CONSOLIDADO_FINANCEIRO_COTA mfp ")
        .append("  left join consolidado_mvto_financeiro_cota cmfc on cmfc.CONSOLIDADO_FINANCEIRO_ID=mfp.id ")
        .append("  where ")
        .append("  mfp.COTA_ID = mfc.COTA_ID ")
        .append("  and mfp.DT_CONSOLIDADO < mfc.DATA limit 1) ")
        .append("  as dataRaiz, ")
        
        //valor pago
        .append(" 0 as valorPago, ")
        
        //total
        .append("ROUND(")
        .append("(")
        //.append(" (consignado - encalhe + valorPostergado + vendaEncalhe + debitoCredito + encargos + pendente) ")
        //.append("coalesce((select sum(m.VALOR) * -1 ")
        .append("- coalesce((select sum(m.VALOR)  ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoConsignado) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
         .append(" + ")
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
        .append(" + ")
        .append("(coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoCredito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)")
        .append(" - ")
        .append(" coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoDebito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)")
        .append(") ")
        .append(" - ")
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoVendaEncalhe) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
        .append(" + ")
        .append("(coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
        .append(" - ")
        .append(" coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)")
        .append(") ")
        .append(" + ")
        .append("coalesce((select sum(m.VALOR) * -1 ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoEncargos) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
        .append(" + ")
        .append("coalesce((select sum(m.VALOR) * -1 ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPendente) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0)), 2) as total, ")
        //.append("),0)), 2) as total, ")
        
        //saldo
        .append(" 0 as saldo, ")
        
        //valorVendaDia (consignado - encalhe)
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoConsignado) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) ")
        .append(" - ")
        .append("coalesce((select sum(m.VALOR) ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
        .append(" inner join COTA on COTA.ID = m.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
        .append(" and m.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(") and m.DATA = mfc.DATA ")
        .append("),0) as valorVendaDia, ")
        
        .append(" 0 as inadimplente ")
        .append(", null as dataMovimento ")
        
        .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
        .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
        .append(" left join BOX box on COTA.BOX_ID = box.ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and mfc.ID not in (")
        .append("     select MVTO_FINANCEIRO_COTA_ID ")
        .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
        .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
        .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
        .append(")");
        
        if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
            
            sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
        }
        
        sql.append(" group by mfc.DATA ");
        
        if (filtro.getColunaOrdenacao() != null) {
            sql.append(" order by ").append(filtro.getColunaOrdenacao()).append(" ");
            if (filtro.getPaginacao() != null) {
                
                sql.append(filtro.getPaginacao().getOrdenacao());
            }else{
                
                sql.append("asc");
            }
            sql.append(", dataMovimento desc");
        }
        
        final SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("cotaId", StandardBasicTypes.LONG);
        query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
        query.addScalar("consignado", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("dataConsolidado", StandardBasicTypes.DATE);
        query.addScalar("debitoCredito", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("encalhe", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("encargos", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("pendente", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("valorPostergado", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("vendaEncalhe", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("cobrado", StandardBasicTypes.BOOLEAN);
        query.addScalar("dataRaiz", StandardBasicTypes.DATE);
        query.addScalar("dataPendente", StandardBasicTypes.DATE);
        query.addScalar("valorPago", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("saldo", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("valorVendaDia", StandardBasicTypes.BIG_DECIMAL);
        query.addScalar("numeroAcumulo", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("inadimplente", StandardBasicTypes.BOOLEAN);
        
        query.addScalar("statusDivida", StandardBasicTypes.STRING);
        
        query.addScalar("nomeBox");
        query.addScalar("detalharDebitoCredito", StandardBasicTypes.BOOLEAN);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ContaCorrenteCotaVO.class));
        
        query.setParameter("numeroCota", filtro.getNumeroCota());
        
        final List<String> statusBaixaCobranca =
                Arrays.asList(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA.name(),
                        StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA.name(),
                        StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR.name());
        
        query.setParameterList("statusBaixaCobranca", statusBaixaCobranca);
        
        if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
            
            query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
            query.setParameter("fimPeriodo", filtro.getFimPeriodo());
        }
        
        query.setParameterList("tiposMovimentoCredito", tiposMovimentoCredito);
        query.setParameterList("tiposMovimentoDebito", tiposMovimentoDebito);
        query.setParameterList("tipoMovimentoEncalhe", tipoMovimentoEncalhe);
        query.setParameterList("tiposMovimentoEncargos", tiposMovimentoEncargos);
        query.setParameterList("tiposMovimentoPostergadoCredito", tiposMovimentoPostergadoCredito);
        query.setParameterList("tiposMovimentoPostergadoDebito", tiposMovimentoPostergadoDebito);
        query.setParameterList("tipoMovimentoVendaEncalhe", tipoMovimentoVendaEncalhe);
        query.setParameterList("tiposMovimentoConsignado", tiposMovimentoConsignado);
        query.setParameterList("tiposMovimentoPendente", tiposMovimentoPendente);
        
        query.setParameter("naoPagoPostergado", StatusBaixa.NAO_PAGO_POSTERGADO.name());
        
        query.setParameter("grupoMovPendente", GrupoMovimentoFinaceiro.PENDENTE.name());
        
        query.setParameter("statusPendenteInadimplencia", StatusDivida.PENDENTE_INADIMPLENCIA.name());
        
        query.setParameter("statusDividaQuitada", StatusDivida.QUITADA.name());
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        if (paginacao != null) {
            
            if (paginacao.getPosicaoInicial() != null) {
                
                query.setFirstResult(paginacao.getPosicaoInicial());
            }
            
            if (paginacao.getQtdResultadosPorPagina() != null) {
                
                query.setMaxResults(paginacao.getQtdResultadosPorPagina());
            }
        }
        
        return query.list();
    }
    
    @Override
    public BigInteger countObterContaCorrente(final FiltroViewContaCorrenteCotaDTO filtro){
        
        final StringBuilder sql = new StringBuilder("select count(cotaId) from (");
        sql.append(" select cfc.COTA_ID as cotaId, ")
        .append(" cfc.DT_CONSOLIDADO as dataConsolidado ")
        .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
        .append(" inner join COTA on COTA.ID = cfc.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ");
        
        if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
            
            sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
        }
        
        sql.append(" union all ")
        
        .append(" select mfc.COTA_ID as cotaId, ")
        .append(" mfc.DATA as dataConsolidado ")
        .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
        .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
        .append(" where COTA.NUMERO_COTA = :numeroCota ")
        .append(" and mfc.DATA not in (")
        .append("     select DT_CONSOLIDADO ")
        .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
        .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
        .append(")");
        
        if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
            
            sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
        }
        
        sql.append(" group by dataConsolidado ")
        .append(") as tmp ");
        
        final Query query = this.getSession().createSQLQuery(sql.toString());
        
        query.setParameter("numeroCota", filtro.getNumeroCota());
        
        if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
            
            query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
            query.setParameter("fimPeriodo", filtro.getFimPeriodo());
        }
        
        return (BigInteger) query.uniqueResult();
    }
    
    @Override
    public Date obterDataAnteriorImediataPostergacao(final ConsolidadoFinanceiroCota consolidadoFinanceiroCota) {
        
        
        final String hql = " select max(cfc.dataConsolidado) " +
                " from ConsolidadoFinanceiroCota cfc " +
                " where cfc.dataConsolidado < :dataConsolidado " +
                " and cfc.cota = :cotaConsolidado ";
        
        final Query query = this.getSession().createQuery(hql);
        
        query.setParameter("dataConsolidado", consolidadoFinanceiroCota.getDataConsolidado());
        query.setParameter("cotaConsolidado", consolidadoFinanceiroCota.getCota());
        
        return (Date) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DebitoCreditoCota> obterConsolidadosDataOperacaoSlip(Long idCota, Date dataOperacao) {
        
        final String hql = "select " +
        		" ABS(con.TOTAL) as valor, " +
                " concat('Cobrança ', " +
                " CASE WHEN d.STATUS in (:statusNegociada) THEN 'negociada' " +
                "      WHEN d.STATUS = :statusInadimplencia THEN 'acumulada' " +
                "      WHEN cob.ID IS NOT NULL THEN " +
                "               concat('efetuada', ' - venc. ', " +
                "               (SELECT EXTRACT(DAY FROM cob.DT_VENCIMENTO)), '/', (SELECT EXTRACT(MONTH FROM cob.DT_VENCIMENTO)), '/', (SELECT EXTRACT(YEAR FROM cob.DT_VENCIMENTO))) " +
                "      ELSE 'postergada' "+
                " END " +
                " ) as observacoes" +
                " from CONSOLIDADO_FINANCEIRO_COTA con "+
                " join COTA c ON (c.ID = con.COTA_ID) " +
                " left join DIVIDA_CONSOLIDADO dc ON (dc.CONSOLIDADO_ID = con.ID) " +
                " left join DIVIDA d ON (d.ID = dc.DIVIDA_ID) "+
                " left join COBRANCA cob ON (cob.DIVIDA_ID = d.ID) " +
        		" where con.DT_CONSOLIDADO = :dataOperacao " +
        		" and c.ID = :idCota ";
        
        final SQLQuery query = this.getSession().createSQLQuery(hql.toString());
        query.setParameter("idCota", idCota);
        query.setParameter("dataOperacao", dataOperacao);
        query.setParameterList("statusNegociada", 
                Arrays.asList(StatusDivida.NEGOCIADA, StatusDivida.POSTERGADA));
        query.setParameter("statusInadimplencia", StatusDivida.PENDENTE_INADIMPLENCIA);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCota.class));
        
        return query.list();
    }  
}