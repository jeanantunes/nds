package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuicaoRepository;

@Repository
public class DistribuicaoRepositoryImpl extends AbstractRepositoryModel<Lancamento, Long> implements DistribuicaoRepository {

	public DistribuicaoRepositoryImpl() {
		super(Lancamento.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoDistribuicaoVO> obterMatrizDistribuicao(FiltroDistribuicaoDTO filtro) {
		StringBuilder sql = new StringBuilder();
		
	 sql.append(" select ")
	 
		.append(" lanc.id as idLancamento,")
		.append(" prod.CODIGO as codigoProduto,") 
		.append(" prod.NOME as nomeProduto,")
		.append(" prodEdic.ID as idProdutoEdicao,")
		.append(" prodEdic.NUMERO_EDICAO as numeroEdicao,")
		.append(" plp.NUMERO_PERIODO as periodo, ")
		.append(" prodEdic.PRECO_VENDA as precoVenda,")
		.append(" tpClassProd.DESCRICAO as classificacao,")
		.append(" prodEdic.PACOTE_PADRAO as pctPadrao,")
		.append(" pessoa.NOME_FANTASIA as nomeFornecedor,")
		.append(" estoqueProd.QTDE_JURAMENTADO as juram,")
		.append(" estoqueProd.QTDE_SUPLEMENTAR as suplem,")
//		.append(" estoqueProd.QTDE as estoque,")
        
		.append(" (SELECT sum(lc.REPARTE_PROMOCIONAL) FROM lancamento lc join periodo_lancamento_parcial plcp on plcp.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID ")
        .append(" 		WHERE lc.PRODUTO_EDICAO_ID = prodEdic.id and plcp.NUMERO_PERIODO = plp.NUMERO_PERIODO) AS promo, ")

        .append(" lanc.DATA_LCTO_DISTRIBUIDOR as dataLanctoSemFormatacao,")
		
        .append(" case estudo.liberado when 1 then 'LIBERADO' else '' end as liberado, ")
		
        .append(" estudo.ID as idEstudo,")
		.append(" estudo.data_lancamento as dataLancamentoEstudo,")
		
		.append("  CASE WHEN estoqueProd.QTDE IS NULL OR estoqueProd.QTDE=0 THEN        																					  ") 
        .append("       CASE WHEN plp.NUMERO_PERIODO=1 THEN                     																					  ") 
        .append("       	((SELECT reparte FROM lancamento lc WHERE lc.id = lanc.id and lc.DATA_LCTO_DISTRIBUIDOR = :dataLanctoPrev) - ifnull(lanc.REPARTE_PROMOCIONAL, 0))       								      ") 
        .append("       WHEN plp.numero_periodo>1 THEN       																									  ") 
        .append("           (select (ep.QTDE + ep.QTDE_SUPLEMENTAR + ep.QTDE_DEVOLUCAO_ENCALHE) from estoque_produto ep where ep.PRODUTO_EDICAO_ID = prodEdic.id)") 
        .append("       ELSE          																															  ")   
        .append("           (SELECT sum(lc.REPARTE) FROM lancamento lc WHERE lc.PRODUTO_EDICAO_ID = prodEdic.id AND lc.DATA_LCTO_DISTRIBUIDOR = :dataLanctoPrev)          										  ") 
        .append("       END          																																  ") 
        .append("   ELSE        																																	  ") 
        .append("      estoqueProd.QTDE		  																														  ")
        .append("   END AS reparte,		  																															  ")
		
		.append(" lanc.DATA_FIN_MAT_DISTRIB as dataFinMatDistrib,")

		.append(" (SELECT sum(lc.reparte) FROM lancamento lc join periodo_lancamento_parcial plcp on plcp.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID ")
		.append(" 		WHERE lc.PRODUTO_EDICAO_ID = prodEdic.id and plcp.NUMERO_PERIODO = plp.NUMERO_PERIODO) AS lancto, ")
		
		.append(" floor(estudo.QTDE_REPARTE) as repDistrib")

		.append(" from produto prod")
		
		.append(" join produto_edicao prodEdic on prodEdic.PRODUTO_ID = prod.ID")
		.append(" left join estoque_produto estoqueProd on estoqueProd.PRODUTO_EDICAO_ID = prodEdic.ID ")
		
		.append(" join lancamento lanc on lanc.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" left join estudo_gerado estudo on lanc.ID = estudo.LANCAMENTO_ID and estudo.produto_edicao_id = prodEdic.id ")
			
		.append(" left join tipo_classificacao_produto tpClassProd on prodEdic.TIPO_CLASSIFICACAO_PRODUTO_ID = tpClassProd.ID")
		.append(" join produto_fornecedor prodForn on prodForn.PRODUTO_ID = prod.ID")
		.append(" join fornecedor forn on forn.ID = prodForn.fornecedores_ID")
		.append(" join pessoa ON pessoa.ID = forn.JURIDICA_ID")
		.append(" left join PERIODO_LANCAMENTO_PARCIAL plp ON plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID ")
		
		.append(" where prod.ATIVO = true")
		.append(" and prodEdic.ATIVO = true")
		
		.append(" and lanc.status in ('BALANCEADO', 'PLANEJADO', 'CONFIRMADO', 'EM_BALANCEAMENTO', 'FURO')")
		.append(" and forn.SITUACAO_CADASTRO = 'ATIVO'")
		.append(" and lanc.EXPEDICAO_ID is null")
		.append(" and (lanc.PERIODO_LANCAMENTO_PARCIAL_ID is null or lanc.PERIODO_LANCAMENTO_PARCIAL_ID = plp.id) ");
	 
	 	if(filtro.getEstudoId()!=null){
	 		sql.append(" and estudo.id = :estudoId");
	 	}
		if(filtro.getData()!=null){
			sql.append(" and lanc.DATA_LCTO_DISTRIBUIDOR = :dataLanctoPrev");
		}
		
	 	if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()) {
	 		sql.append(" and forn.id in (:idFornecedores)");
	 	}
	 	
	 	sql.append(" group by lanc.id, estudo.ID ");
	 	
	 	sql.append(" order by liberado, idEstudo ");
	 	sql.append(" , " + filtro.getPaginacao().getSortColumn() + " " + filtro.getPaginacao().getOrdenacao());
//	 	sql.append(" order by codigoProduto, numeroEdicao");
	 	
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		
		if(filtro.getEstudoId()!=null){
			query.setParameter("estudoId",filtro.getEstudoId());
	 	}
		
		if(filtro.getData()!=null){
			query.setParameter("dataLanctoPrev", new java.sql.Date(filtro.getData().getTime()));
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()) {
			
			query.setParameterList("idFornecedores", filtro.getIdsFornecedores());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoDistribuicaoVO.class));
		
		List<ProdutoDistribuicaoVO> result = query.list();
		
//		Collections.sort(result);
		
		return result;
	}
	
	@Override
	public ProdutoDistribuicaoVO obterProdutoDistribuicaoPorEstudo(BigInteger idEstudo) {
		StringBuilder sql = new StringBuilder();
		
	 sql.append(" select ")
		.append(" lanc.ID as idLancamento,")
		.append(" prod.CODIGO as codigoProduto,")
		.append(" prod.NOME as nomeProduto,")
		.append(" prodEdic.NUMERO_EDICAO as numeroEdicao,")
		.append(" lanc.DATA_LCTO_PREVISTA as dataLanctoSemFormatacao,")
		.append(" estudo.QTDE_REPARTE as reparte,")
		.append(" tpClassProd.DESCRICAO as classificacao")
		.append(" from produto prod")
		.append(" join produto_edicao prodEdic on prodEdic.PRODUTO_ID = prod.ID")
		.append(" join lancamento lanc on lanc.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" join estudo_gerado estudo on estudo.LANCAMENTO_ID = lanc.ID")
		.append(" left join tipo_classificacao_produto tpClassProd on prodEdic.TIPO_CLASSIFICACAO_PRODUTO_ID = tpClassProd.ID")
		.append(" where prod.ATIVO = true")
		.append(" and prodEdic.ATIVO = true")
		.append(" and lanc.status = 'BALANCEADO'")
	 	.append(" and estudo.id = :idEstudo");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idEstudo", idEstudo);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoDistribuicaoVO.class));

        return (ProdutoDistribuicaoVO)query.uniqueResult();
	}

	@Override
	public ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo(BigInteger id) {
		StringBuilder sql = new StringBuilder();
		
		 sql.append(" select ")
			.append(" lanc.ID as idLancamento,")
			.append(" prod.CODIGO as codigoProduto,") 
			.append(" prod.NOME as nomeProduto,")
			.append(" prodEdic.ID as idProdutoEdicao,")
			.append(" prodEdic.NUMERO_EDICAO as numeroEdicao,")
			.append(" prod.PERIODICIDADE as periodo, ")
			.append(" prodEdic.PRECO_VENDA as precoVenda,")
			.append(" tpClassProd.DESCRICAO as classificacao,")
			.append(" prod.PACOTE_PADRAO as pctPadrao,")
			.append(" pessoa.NOME_FANTASIA as nomeFornecedor,")
			.append(" estoqueProd.QTDE_JURAMENTADO as juram,")
			.append(" estoqueProd.QTDE_SUPLEMENTAR as suplem,")
			.append(" lanc.REPARTE_PROMOCIONAL as promo,")
			.append(" lanc.DATA_LCTO_PREVISTA as dataLanctoSemFormatacao,")
			.append(" case estudo.liberado when 1 then 'LIBERADO'")
			.append(" else ''")
			.append(" end as liberado,")
			.append(" estudo.ID as idEstudo,")
			.append(" estudo.QTDE_REPARTE as reparte,")
			.append(" lanc.DATA_FIN_MAT_DISTRIB as dataFinMatDistrib,")
			.append(" lanc.REPARTE as lancto")
			.append(" from produto prod")
			.append(" join produto_edicao prodEdic on prodEdic.PRODUTO_ID = prod.ID")
			.append(" left join estoque_produto estoqueProd on estoqueProd.PRODUTO_EDICAO_ID = prodEdic.ID ")
			.append(" join lancamento lanc on lanc.PRODUTO_EDICAO_ID = prodEdic.ID")
			.append(" left join estudo_gerado estudo on lanc.ID = estudo.LANCAMENTO_ID")
			.append(" left join tipo_classificacao_produto tpClassProd on prodEdic.TIPO_CLASSIFICACAO_PRODUTO_ID = tpClassProd.ID")
			.append(" join produto_fornecedor prodForn on prodForn.PRODUTO_ID = prod.ID")
			.append(" join fornecedor forn on forn.ID = prodForn.fornecedores_ID")
			.append(" join pessoa ON pessoa.ID = forn.JURIDICA_ID")
			.append(" where estudo.id = :idEstudo");
			
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			
			query.setParameter("idEstudo", id);
			
			query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoDistribuicaoVO.class));
			
			return (ProdutoDistribuicaoVO)query.uniqueResult();
	}
	
}
