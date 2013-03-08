package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" produtoEdicao.NUMERO_EDICAO AS numEdicao, ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
		hql.append(" lancamento.DATA_REC_DISTRIB AS dataRecolhimento, ");
		  
		hql.append(" SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) ");
		hql.append(" THEN movimentoEstoqueCota.QTDE ELSE 0 END) as reparte, ");
        
		hql.append(" SUM(CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
		hql.append(" ELSE 0 END) AS venda, ");
        
		hql.append(" SUM( ");
		hql.append(" (CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
		hql.append(" ELSE 0 END) * 100 / ");
		hql.append(" (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" ) / count(*) AS percentualVenda, ");
        
		hql.append(" produtoEdicao.PRECO_VENDA AS precoCapa, ");
		hql.append(" produtoEdicao.CHAMADA_CAPA AS chamadaCapa, ");
        
		hql.append(" SUM(CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
		hql.append(" ELSE 0 END) * produtoEdicao.PRECO_VENDA AS total, ");
		  
		hql.append(" produtoEdicao.PARCIAL AS parcial, ");
		hql.append(" produto.CODIGO AS codigoProduto ");
		
		hql.append(this.getSqlFromEWhereVendaPorProduto(filtro));
		
		hql.append(" GROUP BY produtoEdicao.ID ");
		hql.append(" HAVING numEdicao IS NOT NULL ");		
		
		hql.append(this.getOrderByVenda(filtro));
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
		query.addScalar("numEdicao", StandardBasicTypes.LONG);
		query.addScalar("dataLancamento");
		query.addScalar("dataRecolhimento");
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("percentualVenda");
		query.addScalar("precoCapa");
		query.addScalar("chamadaCapa");
		query.addScalar("total");
		query.addScalar("parcial", StandardBasicTypes.BOOLEAN);
		query.addScalar("codigoProduto");
		
		HashMap<String, Object> param = this.buscarParametrosVendaProduto(filtro);
		
		HashMap<String, Object> paramList = this.buscarParametrosListVendaProduto(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, (List) paramList.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaProdutoDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
	}
	
	private String getSqlFromEWhereVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM  MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
	    
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_EDICAO produtoEdicao ");
		hql.append("         ON movimentoEstoqueCota.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
		hql.append(" LEFT JOIN ");
		hql.append("     LANCAMENTO lancamento "); 
		hql.append("         ON movimentoEstoqueCota.LANCAMENTO_ID=lancamento.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     TIPO_MOVIMENTO tipoMovimento "); 
		hql.append("         ON movimentoEstoqueCota.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto "); 
		hql.append("         ON produtoEdicao.PRODUTO_ID=produto.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor "); 
		hql.append("         ON produto.ID=produtoFornecedor.PRODUTO_ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     FORNECEDOR fornecedor "); 
		hql.append("         ON produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		hql.append(" LEFT JOIN CHAMADA_ENCALHE chamadaEncalhe ");
		hql.append("  		ON chamadaEncalhe.ID = ");
		hql.append(" 			 (SELECT ID FROM CHAMADA_ENCALHE WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID LIMIT 1) ");
		hql.append(" LEFT JOIN CHAMADA_ENCALHE_COTA chamadaEncalheCota ");
		hql.append("  		ON (chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID ");
		hql.append(" 			 	AND chamadaEncalheCota.COTA_ID = movimentoEstoqueCota.COTA_ID ");
		hql.append(" 				AND chamadaEncalheCota.FECHADO=:chamadaEncalheFechada) ");
		
		hql.append(" WHERE tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in (:gruposMovimentoEstoque) ");
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append(" AND produto.CODIGO = :codigo ");
		}
		if(filtro.getEdicao() !=null){
			hql.append(" AND produtoEdicao.NUMERO_EDICAO = :edicao ");
		}
		if(filtro.getIdFornecedor() !=null && filtro.getIdFornecedor() != -1){
			hql.append(" AND fornecedor.ID = :idFornecedor ");
		}
		
		return hql.toString();
	}
	
	private String getSqlFromEWhereDetalheVendaPorProduto() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM  MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
	    
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_EDICAO produtoEdicao ");
		hql.append("         ON movimentoEstoqueCota.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     LANCAMENTO lancamento "); 
		hql.append("         ON movimentoEstoqueCota.LANCAMENTO_ID=lancamento.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     TIPO_MOVIMENTO tipoMovimento "); 
		hql.append("         ON movimentoEstoqueCota.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto "); 
		hql.append("         ON produtoEdicao.PRODUTO_ID=produto.ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor "); 
		hql.append("         ON produto.ID=produtoFornecedor.PRODUTO_ID "); 
		hql.append(" INNER JOIN ");
		hql.append("     FORNECEDOR fornecedor "); 
		hql.append("         ON produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		 
		hql.append(" WHERE tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in (:gruposMovimentoEstoque) ");
		hql.append(" 	 AND produtoEdicao.PARCIAL = :produtoParcial ");
		
		hql.append(" AND produto.CODIGO = :codigo ");
		
		hql.append(" AND produtoEdicao.NUMERO_EDICAO = :edicao ");
		
		return hql.toString();
	}
	
	private String getOrderByVenda(FiltroVendaProdutoDTO filtro) {
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ORDER BY " + coluna);
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(" " + filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosDetalhesVendaProduto(FiltroDetalheVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("codigo", filtro.getCodigo());
		param.put("edicao", filtro.getEdicao());
		param.put("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA.name());
		param.put("operacaoEstoqueSaida", OperacaoEstoque.SAIDA.name());
		param.put("chamadaEncalheFechada", true);
		param.put("produtoParcial", true);
		
		return param;
	}
	
	private HashMap<String,Object> buscarParametrosVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){ 
			param.put("codigo", filtro.getCodigo());
		}
		if(filtro.getEdicao() != null){ 
			param.put("edicao", filtro.getEdicao());
		}
		if(filtro.getIdFornecedor() != null && filtro.getIdFornecedor() != -1){ 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		param.put("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA.name());
		
		param.put("chamadaEncalheFechada", true);
		
		return param;
	}
	
	private HashMap<String,Object> buscarParametrosListDetalheVendaProduto(FiltroDetalheVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
				
		param.put("gruposMovimentoEstoque", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
														  GrupoMovimentoEstoque.ENVIO_ENCALHE.name()));
		
		return param;
	}
	
	private HashMap<String,Object> buscarParametrosListVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
				
		param.put("gruposMovimentoEstoque", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
														  GrupoMovimentoEstoque.ENVIO_ENCALHE.name()));
		
		return param;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
		hql.append(" lancamento.DATA_REC_DISTRIB AS dataRecolhimento, ");
        
		hql.append(" SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) ");
		hql.append(" THEN movimentoEstoqueCota.QTDE ELSE 0 END) AS reparte, ");
		  
		hql.append(" SUM(case when (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
		hql.append(" THEN movimentoEstoqueCota.QTDE ELSE 0 END) AS encalhe, ");
        
		hql.append(" SUM(CASE WHEN ((SELECT chamadaCota.ID ");
		hql.append(" 		  FROM CHAMADA_ENCALHE_LANCAMENTO chamadaEncLancamento, ");
		hql.append(" 		  		 CHAMADA_ENCALHE chamadaEncalhe, CHAMADA_ENCALHE_COTA chamadaCota ");
		hql.append(" 		  WHERE chamadaEncLancamento.LANCAMENTO_ID=lancamento.ID ");
		hql.append(" 		  AND chamadaEncalhe.ID=chamadaEncLancamento.CHAMADA_ENCALHE_ID ");
		hql.append(" 		  AND chamadaCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID ");
		hql.append(" 		  AND chamadaCota.COTA_ID=movimentoEstoqueCota.COTA_ID ");
		hql.append(" 	     AND chamadaCota.FECHADO=:chamadaEncalheFechada LIMIT 1) IS NOT NULL) ");
		hql.append("   THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
		hql.append("   ELSE 0 END) AS venda, ");
		  
		hql.append(" SUM((CASE WHEN ( ");
		hql.append("   		(SELECT chamadaCota.ID ");
		hql.append(" 		  FROM CHAMADA_ENCALHE_LANCAMENTO chamadaEncLancamento, ");
		hql.append(" 		  		 CHAMADA_ENCALHE chamadaEncalhe, CHAMADA_ENCALHE_COTA chamadaCota ");
		hql.append(" 		  WHERE chamadaEncLancamento.LANCAMENTO_ID=lancamento.ID ");
		hql.append(" 		  AND chamadaEncalhe.ID=chamadaEncLancamento.CHAMADA_ENCALHE_ID ");
		hql.append(" 		  AND chamadaCota.CHAMADA_ENCALHE_ID=chamadaEncalhe.ID ");
		hql.append(" 		  AND chamadaCota.COTA_ID=movimentoEstoqueCota.COTA_ID ");
		hql.append(" 	     AND chamadaCota.FECHADO=:chamadaEncalheFechada LIMIT 1) IS NOT NULL) ");
		hql.append("   THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
		hql.append("   ELSE 0 END) * 100 / ");
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   ) / count(*) AS percentualVenda ");
		
		hql.append(this.getSqlFromEWhereDetalheVendaPorProduto());
		
		hql.append(" GROUP BY lancamento.ID ");
		hql.append(" HAVING dataLancamento IS NOT NULL ");
		hql.append(" ORDER BY dataLancamento ");
		
		SQLQuery query =  getSession().createSQLQuery(hql.toString());
		
		query.addScalar("dataLancamento");
		query.addScalar("dataRecolhimento");
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("percentualVenda");
		
		HashMap<String, Object> param = this.buscarParametrosDetalhesVendaProduto(filtro);
		
		HashMap<String, Object> paramList = this.buscarParametrosListDetalheVendaProduto(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, (List) paramList.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(LancamentoPorEdicaoDTO.class));
				
		return query.list();
	}

}
