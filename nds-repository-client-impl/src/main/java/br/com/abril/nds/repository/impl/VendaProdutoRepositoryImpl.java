package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
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
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		        
        hql.append(" SELECT ");
        hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
        hql.append(" 	  			THEN movimentoEstoque.QTDE ");
        hql.append("         	ELSE 0 ");
        hql.append("     		END ");
        hql.append(" 	 ) as reparte, ");
		    
        hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
        hql.append(" 	  			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 				ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 			END ");
        hql.append(" 	 ) AS venda, ");
		    
        hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
        hql.append(" 	  			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 				ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 			END ");
        hql.append(" 	 ) * 100 / ");
        hql.append(" 	 SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
        hql.append(" 	  			THEN movimentoEstoque.QTDE ");
        hql.append("         	ELSE 0 ");
        hql.append("     		END ");
        hql.append(" 	 ) AS percentualVenda, ");
		    
        hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
        hql.append(" 	  			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 				ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoque.QTDE ELSE 0 END) ");
        hql.append(" 			END ");
        hql.append(" 	 ) * produtoEdicao.PRECO_VENDA AS total, ");
		    
        hql.append(" 	 produtoEdicao.NUMERO_EDICAO AS numEdicao, ");
        hql.append("     lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
        hql.append("     lancamento.DATA_REC_DISTRIB AS dataRecolhimento, ");
        hql.append("     produtoEdicao.PRECO_VENDA AS precoCapa, ");
        hql.append("     produtoEdicao.CHAMADA_CAPA AS chamadaCapa, ");
        hql.append("     produtoEdicao.PARCIAL AS parcial, ");
        hql.append("     produto.CODIGO AS codigoProduto ");
        
		hql.append(this.getSqlFromEWhereVendaPorProduto(filtro));
		
		hql.append(" 	 GROUP BY produtoEdicao.ID ");		
		
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
		
		HashMap<String, Object> paramList = this.buscarParametrosListVendaProduto();
		
		setParameters(query, param);
		setParameters(query, paramList);
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaProdutoDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
	}
	
	private String getSqlFromEWhereVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM ");
		hql.append("     MOVIMENTO_ESTOQUE movimentoEstoque ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  TIPO_MOVIMENTO tipoMovimento ");
		hql.append(" 			ON movimentoEstoque.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_EDICAO produtoEdicao ");          
		hql.append("         ON movimentoEstoque.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto ");
		hql.append("         ON produtoEdicao.PRODUTO_ID = produto.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor ");          
		hql.append("         ON produto.ID = produtoFornecedor.PRODUTO_ID ");  
		hql.append(" INNER JOIN ");
		hql.append("     FORNECEDOR fornecedor ");          
		hql.append("         ON produtoFornecedor.fornecedores_ID = fornecedor.ID ");
		hql.append(" INNER JOIN ");
		hql.append(" LANCAMENTO lancamento ");
		hql.append("     ON lancamento.ID = ( SELECT ID FROM LANCAMENTO WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID ORDER BY ID ASC LIMIT 1 ) ");
		hql.append(" LEFT JOIN ");
		hql.append("  	  FECHAMENTO_ENCALHE fechamentoEncalhe ");
		hql.append(" 			ON (fechamentoEncalhe.DATA_ENCALHE = lancamento.DATA_REC_DISTRIB ");
		hql.append(" 				AND fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID) ");
		
		hql.append(" WHERE tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN ( ");
		hql.append("   		:gruposMovimentoEstoque ");
		hql.append(" ) ");
		
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
		
		hql.append(" FROM ");
		hql.append("     MOVIMENTO_ESTOQUE movimentoEstoque ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  TIPO_MOVIMENTO tipoMovimento ");
		hql.append(" 			ON movimentoEstoque.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_EDICAO produtoEdicao ");          
		hql.append("         ON movimentoEstoque.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto ");
		hql.append("         ON produtoEdicao.PRODUTO_ID = produto.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor ");          
		hql.append("         ON produto.ID = produtoFornecedor.PRODUTO_ID ");  
		hql.append(" INNER JOIN ");
		hql.append("     FORNECEDOR fornecedor ");          
		hql.append("         ON produtoFornecedor.fornecedores_ID = fornecedor.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     LANCAMENTO lancamento ");
		hql.append("         ON lancamento.ID = ( ");
		hql.append(" 	  			SELECT ID FROM LANCAMENTO ");
		hql.append(" 	  				WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" 					AND movimentoEstoque.DATA between DATA_LCTO_DISTRIBUIDOR and DATA_REC_DISTRIB ");
		hql.append(" 					ORDER BY DATA_REC_DISTRIB LIMIT 1 ");
		hql.append(" 	  		) ");
		hql.append("  LEFT JOIN ");
		hql.append("  	  FECHAMENTO_ENCALHE fechamentoEncalhe ");
		hql.append(" 			ON (fechamentoEncalhe.DATA_ENCALHE = lancamento.DATA_REC_DISTRIB ");
		hql.append(" 				AND fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID) ");
		
		hql.append("  WHERE produtoEdicao.PARCIAL = :produtoParcial ");
		hql.append("  	  AND produto.CODIGO = :codigo ");
		hql.append("      AND produtoEdicao.NUMERO_EDICAO = :edicao ");
		hql.append("      AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN ( ");
		hql.append("   		:gruposMovimentoEstoque ");
		hql.append(" 	  ) ");
		
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
		
		param.put("operacaoEstoqueSaida", OperacaoEstoque.SAIDA.name());
		
		return param;
	}
	
	private HashMap<String,Object> buscarParametrosListVendaProduto() {
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		List<String> lista = new ArrayList<>();
		
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name());
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name());
		lista.add(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name());				
		
		param.put("gruposMovimentoEstoque", lista);
		
		return param;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
		hql.append(" 	  			THEN movimentoEstoque.QTDE ");
		hql.append("         	ELSE 0 ");
		hql.append("     		END ");
		hql.append(" 	 ) as reparte, ");
			  
		hql.append(" 	 SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueEntrada) ");
		hql.append(" 	  			THEN movimentoEstoque.QTDE ");
		hql.append("         	ELSE 0 ");
		hql.append("     		END ");
		hql.append(" 	 ) as encalhe, ");
	        
		hql.append("     SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = :operacaoEstoqueSaida) ");
		hql.append(" 	  			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoque.QTDE ELSE 0 END) ");
		hql.append(" 				ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoque.QTDE ELSE 0 END) ");
		hql.append(" 			END ");
		hql.append(" 	 ) AS venda, ");
			  
			  
		hql.append(" 	 produtoEdicao.NUMERO_EDICAO AS numEdicao, ");
		hql.append("     lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
		hql.append("     lancamento.DATA_REC_DISTRIB AS dataRecolhimento ");
		
		hql.append(this.getSqlFromEWhereDetalheVendaPorProduto());
		
		hql.append(" GROUP BY lancamento.ID ");
		hql.append(" ORDER BY dataLancamento ");
		
		SQLQuery query =  getSession().createSQLQuery(hql.toString());
		
		query.addScalar("dataLancamento");
		query.addScalar("dataRecolhimento");
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		
		HashMap<String, Object> param = this.buscarParametrosDetalhesVendaProduto(filtro);
		
		HashMap<String, Object> paramList = this.buscarParametrosListVendaProduto();
		
		setParameters(query, param);
		setParameters(query, paramList);
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(LancamentoPorEdicaoDTO.class));
				
		return query.list();
	}

}
