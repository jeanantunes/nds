package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT estoqueProduto.produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataRecolhimento, ");
		hql.append(" (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) as reparte, ");
		hql.append(" ((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) / lancamento.reparte)  as percentagemVenda, ");
		hql.append(" produtoEdicao.precoVenda  as precoCapa, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) * produtoEdicao.precoVenda)  as total ");
		
		
		hql.append(getSqlFromEWhereVendaPorProduto(filtro));
		
		hql.append(getOrderByPorEdicoes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosVendaProduto(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
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
	

		hql.append(" from EstoqueProduto estoqueProduto ");
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao.lancamentos as lancamento ");		
		hql.append(" LEFT JOIN estoqueProduto.produtoEdicao.produto.fornecedores as fornecedor ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produtoEdicao.produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}
		if(filtro.getIdFornecedor() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " fornecedor.id = :idFornecedor ");
			usarAnd = true;
		}


		return hql.toString();
	}
	
	private String getOrderByPorEdicoes(FiltroVendaProdutoDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		switch (coluna) {
			case EDICAO:	
				hql.append(" order by estoqueProduto.produtoEdicao.numeroEdicao desc ");
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
		if(filtro.getIdFornecedor() != null){ 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
	
		return param;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaProdutoDTO> buscarLancamentoPorEdicao(
			FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataRecolhimento, ");
		hql.append(" (estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) as reparte, ");
		hql.append(" estoqueProduto.qtdeDevolucaoEncalhe  as encalhe, ");
		hql.append(" ((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe)  as venda, ");
		hql.append(" (((estoqueProduto.qtde + estoqueProduto.qtdeSuplementar) - estoqueProduto.qtdeDevolucaoEncalhe) * produtoEdicao.precoVenda)  as total ");
		
		
		hql.append(getSqlFromEWhereLancamentoPorEdicao(filtro));
		
		//hql.append(getOrderByPorEdicoes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentoPorEdicao(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaProdutoDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
		
	}
	
	private String getSqlFromEWhereLancamentoPorEdicao(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from Lancamento lancamento ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN lancamento.produtoEdicao.movimentoEstoques as movimentoEstoque ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto as estoqueProduto ");
		
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			hql.append( (usarAnd ? " and ":" where ") + "produto.codigo = :codigo ");
			usarAnd = true;
		}
		if(filtro.getEdicao() !=null){
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao = :edicao ");
			usarAnd = true;
		}

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
	
		return param;
	}

	
}
