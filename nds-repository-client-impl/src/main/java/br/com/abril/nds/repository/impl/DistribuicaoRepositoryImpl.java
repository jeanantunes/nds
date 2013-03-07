package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
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
	public List<ProdutoDistribuicaoVO> obterMatrizDistribuicao(FiltroLancamentoDTO filtro) {
		StringBuilder sql = new StringBuilder();
		
	 sql.append(" select ")
		.append(" lanc.ID as idLancamento,")
		.append(" prod.CODIGO as codigoProduto,") 
		.append(" prod.NOME as nomeProduto,")
		.append(" prodEdic.NUMERO_EDICAO as numeroEdicao,")
		.append(" prod.PERIODICIDADE as periodo, ")
		.append(" prodEdic.PRECO_VENDA as precoVenda,")
		.append(" tpClassProd.DESCRICAO as classificacao,")
		.append(" prod.PACOTE_PADRAO as pctPadrao,")
		.append(" pessoa.NOME_FANTASIA as nomeFornecedor,")
		.append(" estoqueProdJuram.QTDE as juram,")
		.append(" estoqueProd.QTDE_SUPLEMENTAR as suplem,")
		.append(" lanc.REPARTE_PROMOCIONAL as promo,")
		.append(" DATE_FORMAT(lanc.DATA_LCTO_DISTRIBUIDOR,'%d/%m/%Y') as dataLancto,")
		.append(" case estudo.liberado when 1 then 'LIBERADO'")
		.append(" else ''")
	    .append(" end as liberado,")
	    .append(" estudo.ID as idEstudo,")
	    .append(" prodEdic.REPARTE_DISTRIBUIDO as reparte,")
	    .append(" lanc.DATA_FIN_MAT_DISTRIB as dataFinMatDistrib")
	    .append(" from produto prod")
		.append(" join produto_edicao prodEdic on prodEdic.PRODUTO_ID = prod.ID")
		.append(" left join estoque_produto estoqueProd on estoqueProd.PRODUTO_EDICAO_ID = prodEdic.ID ")
		.append(" left join estoque_produto_cota_juramentado estoqueProdJuram on estoqueProdJuram.PRODUTO_EDICAO_ID = prodEdic.ID ")
		.append(" join lancamento lanc on lanc.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" left join estudo estudo on estudo.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" left join tipo_classificacao_produto tpClassProd on prod.TIPO_CLASSIFICACAO_PRODUTO_ID = tpClassProd.ID")
		.append(" join produto_fornecedor prodForn on prodForn.PRODUTO_ID = prod.ID")
		.append(" join fornecedor forn on forn.ID = prodForn.fornecedores_ID")
		.append(" join pessoa ON pessoa.ID = forn.JURIDICA_ID")
		.append(" where prod.ATIVO = true")
		.append(" and prodEdic.ATIVO = true")
		.append(" and lanc.status = 'BALANCEADO'")
		.append(" and forn.SITUACAO_CADASTRO = 'ATIVO'");
	 /*	.append(" and lanc.DATA_LCTO_PREVISTA = :dataLanctoPrev");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()) {
			sql.append(" and forn.ID in (:idsFornecedores)");
		}
	*/
		sql.append(" order by liberado");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
//		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()) {
//			query.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
//		}
//		
//		query.setParameter("dataLanctoPrev", filtro.getData());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoDistribuicaoVO.class));
		
		List<ProdutoDistribuicaoVO> result = query.list();
		
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
		.append(" DATE_FORMAT(lanc.DATA_LCTO_DISTRIBUIDOR,'%d/%m/%Y') as dataLancto,")
	    .append(" prodEdic.REPARTE_DISTRIBUIDO as reparte,")
	    .append(" tpClassProd.DESCRICAO as classificacao")
	    .append(" from produto prod")
		.append(" join produto_edicao prodEdic on prodEdic.PRODUTO_ID = prod.ID")
		.append(" join lancamento lanc on lanc.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" join estudo estudo on estudo.PRODUTO_EDICAO_ID = prodEdic.ID")
		.append(" left join tipo_classificacao_produto tpClassProd on prod.TIPO_CLASSIFICACAO_PRODUTO_ID = tpClassProd.ID")
		.append(" where prod.ATIVO = true")
		.append(" and prodEdic.ATIVO = true")
		.append(" and lanc.status = 'BALANCEADO'")
	 	.append(" and estudo.id = :idEstudo");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idEstudo", idEstudo);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoDistribuicaoVO.class));
		
		ProdutoDistribuicaoVO result = (ProdutoDistribuicaoVO)query.uniqueResult();
		
		return result;
	}
	
}
