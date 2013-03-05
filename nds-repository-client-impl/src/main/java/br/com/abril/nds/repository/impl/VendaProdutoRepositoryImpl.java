package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	private StringBuilder getMovimentosReparte(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT COALESCE(sum(COALESCE(mec.qtde,0)),0) ");
		hql.append(" FROM MovimentoEstoqueCota mec ");
		hql.append(" WHERE mec = movEstCota ");
		
		return hql;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		
		hql.append(" sum(case when (tipoMovimento.operacaoEstoque = 'ENTRADA') ");
		hql.append(" then movimentoEstoqueCota.qtde else 0 end) as reparte, ");
		
		hql.append(" sum(case when (tipoMovimento.operacaoEstoque = 'ENTRADA') ");
		hql.append(" then movimentoEstoqueCota.qtde else - movimentoEstoqueCota.qtde end) as venda, ");

		hql.append(" cast (sum(case when (tipoMovimento.operacaoEstoque = 'ENTRADA') ");
		hql.append(" then movimentoEstoqueCota.qtde else - movimentoEstoqueCota.qtde end) as big_decimal ) ");
		hql.append(" * 100 / cast(sum(case when (tipoMovimento.operacaoEstoque = 'ENTRADA') ");
		hql.append(" then movimentoEstoqueCota.qtde else 0 end) as big_decimal ) as percentagemVenda, ");
		
		hql.append(" produtoEdicao.precoVenda  as precoCapa, ");
		hql.append(" produtoEdicao.chamadaCapa as chamadaCapa, ");
		
		hql.append(" sum(case when (tipoMovimento.operacaoEstoque = 'ENTRADA') ");
		hql.append(" then movimentoEstoqueCota.qtde else - movimentoEstoqueCota.qtde end) ");
		hql.append(" * produtoEdicao.precoVenda as total ");
		
//		hql.append(" SELECT ");
//		hql.append(" produtoEdicao.NUMERO_EDICAO AS numEdicao, ");
//		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
//		hql.append(" lancamento.DATA_REC_DISTRIB AS dataRecolhimento, ");
//		  
//		hql.append(" SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
//		hql.append(" THEN movimentoEstoqueCota.QTDE ELSE 0 END) as reparte, ");
//        
//		hql.append(" SUM(CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
//		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
//		hql.append(" ELSE 0 END) AS venda, ");
//        
//		hql.append(" SUM( ");
//		hql.append(" (CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
//		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
//		hql.append(" ELSE 0 END) * 100 / ");
//		hql.append(" (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
//		hql.append(" ) / count(*) AS percentualVenda, ");
//        
//		hql.append(" produtoEdicao.PRECO_VENDA AS precoCapa, ");
//		hql.append(" produtoEdicao.CHAMADA_CAPA AS chamadaCapa, ");
//        
//		hql.append(" SUM(CASE WHEN (chamadaEncalheCota.ID IS NOT NULL) ");
//		hql.append(" THEN (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') THEN movimentoEstoqueCota.QTDE ELSE - movimentoEstoqueCota.QTDE END) ");
//		hql.append(" ELSE 0 END) * produtoEdicao.PRECO_VENDA AS total, ");
//		  
//		hql.append(" produtoEdicao.PARCIAL AS isParcial, ");
		
		hql.append(this.getSqlFromEWhereVendaPorProduto(filtro));
		
		//hql.append(getOrderByPorEdicoes(filtro));
		
		hql.append(" group by produtoEdicao ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = this.buscarParametrosVendaProduto(filtro);
		
		HashMap<String, Object> paramList = this.buscarParametrosListVendaProduto(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, (List) paramList.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaProdutoDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
	}
	
	private String getSqlFromEWhereVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
		hql.append(" JOIN movimentoEstoqueCota.produtoEdicao as produtoEdicao ");
		hql.append(" JOIN produtoEdicao.produto as produto ");
		hql.append(" JOIN produtoEdicao.lancamentos as lancamento ");
		hql.append(" JOIN movimentoEstoqueCota.tipoMovimento as tipoMovimento ");
		
//		hql.append(" FROM  MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
//	    
//		hql.append(" INNER JOIN ");
//		hql.append("     PRODUTO_EDICAO produtoEdicao ");
//		hql.append("         ON movimentoEstoqueCota.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
//		hql.append(" LEFT JOIN ");
//		hql.append("     LANCAMENTO lancamento "); 
//		hql.append("         ON movimentoEstoqueCota.LANCAMENTO_ID=lancamento.ID "); 
//		hql.append(" INNER JOIN ");
//		hql.append("     TIPO_MOVIMENTO tipoMovimento "); 
//		hql.append("         ON movimentoEstoqueCota.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
//		hql.append(" INNER JOIN ");
//		hql.append("     PRODUTO produto "); 
//		hql.append("         ON produtoEdicao.PRODUTO_ID=produto.ID "); 
//		hql.append(" INNER JOIN ");
//		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor "); 
//		hql.append("         ON produto.ID=produtoFornecedor.PRODUTO_ID "); 
//		hql.append(" INNER JOIN ");
//		hql.append("     FORNECEDOR fornecedor "); 
//		hql.append("         ON produtoFornecedor.fornecedores_ID=fornecedor.ID ");
//		hql.append(" left JOIN CHAMADA_ENCALHE chamadaEncalhe ");
//		hql.append("  		ON chamadaEncalhe.ID = ");
//		hql.append(" 			 (SELECT ID FROM CHAMADA_ENCALHE WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID LIMIT 1) ");
//		hql.append(" left JOIN CHAMADA_ENCALHE_COTA chamadaEncalheCota ");
//		hql.append("  		ON (chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID ");
//		hql.append(" 			 	AND chamadaEncalheCota.COTA_ID = movimentoEstoqueCota.COTA_ID ");
//		hql.append(" 				AND chamadaEncalheCota.FECHADO=TRUE) ");
//		
//		hql.append(" WHERE produto.CODIGO='19359001' ");
//		hql.append("     AND produtoEdicao.NUMERO_EDICAO=35 ");
//		hql.append("     AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE in ('RECEBIMENTO_REPARTE', 'ENVIO_ENCALHE') ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		if(filtro.getIdFornecedor() !=null && filtro.getIdFornecedor() != -1){
			hql.append( (usarAnd ? " and ":" where ") + " produto.fornecedor.id = :idFornecedor ");
			usarAnd = true;
		}
		
		hql.append("  AND movimentoEstoqueCota.movimentoEstoqueCotaFuro is null ");
		
		hql.append("  AND tipoMovimento.grupoMovimentoEstoque in :gruposMovimentoEstoque ");
		
		return hql.toString();
	}
	
	private String getOrderByPorEdicoes(FiltroVendaProdutoDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" group by lancamento ");
		
		switch (coluna) {
			case EDICAO:	
				hql.append(" order by estoqueProduto.produtoEdicao.numeroEdicao ");
				break;
				
			case CHAMADA_CAPA:	
				hql.append(" order by chamadaCapa ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
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
		
		return param;
	}
	
	private HashMap<String,Object> buscarParametrosListVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
				
		param.put("gruposMovimentoEstoque", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE,
														  GrupoMovimentoEstoque.ENVIO_ENCALHE));
		
		return param;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LancamentoPorEdicaoDTO> buscarLancamentoPorEdicao(
			FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT DISTINCT lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		
		hql.append(" ( ");
		hql.append( this.getMovimentosReparte() );
		hql.append(" ) as reparte, ");		
		
		hql.append(" estoqueProduto.qtdeDevolucaoEncalhe  as encalhe, ");
		hql.append(" (("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" (("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe)  as vendaAcumulada, ");
		hql.append(" ROUND(((("+this.getMovimentosReparte()+") - estoqueProduto.qtdeDevolucaoEncalhe) / ("+this.getMovimentosReparte()+"))) as percentualVenda ");
		
		hql.append(getSqlFromEWhereLancamentoPorEdicao(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentoPorEdicao(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				LancamentoPorEdicaoDTO.class));
		
		return query.list();
	}
	
	private String getSqlFromEWhereLancamentoPorEdicao(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from Lancamento lancamento ");
		hql.append(" JOIN lancamento.produtoEdicao as produtoEdicao ");
		hql.append(" JOIN produtoEdicao.produto as produto ");
		hql.append(" JOIN lancamento.produtoEdicao.movimentoEstoques as movimentoEstoque ");
		hql.append(" JOIN movimentoEstoque.estoqueProduto as estoqueProduto ");
		hql.append(" JOIN lancamento.movimentoEstoqueCotas as movEstCota ");
		hql.append(" JOIN movEstCota.tipoMovimento tipoMovimento ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		
        hql.append("  AND movEstCota.movimentoEstoqueCotaFuro is null ");
		
		hql.append("  AND tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");

		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosLancamentoPorEdicao(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){ 
			param.put("codigo", filtro.getCodigo());
		}
		if(filtro.getEdicao() != null){ 
			param.put("edicao", filtro.getEdicao());
		}		
		
		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
	
		return param;
	}

}
