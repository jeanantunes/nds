package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ParcialDTO;
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
	public List<VendaProdutoDTO> buscarLancamentosParciais(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataRecolhimento, ");
		hql.append(" lancamento.reparte as reparte, ");
		hql.append(" (lancamento.reparte - chamadaEncalheCota.qtdePrevista)  as venda, ");
		hql.append(" ((lancamento.reparte - chamadaEncalheCota.qtdePrevista) / lancamento.reparte)  as percentagemVenda, ");
		hql.append(" produtoEdicao.precoVenda  as precoCapa, ");
		hql.append(" ((lancamento.reparte - chamadaEncalheCota) * produtoEdicao.precoVenda)  as total ");
		
		
		hql.append(getSqlFromEWhereLancamentosParciais(filtro));
		
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
	
	private String getSqlFromEWhereLancamentosParciais(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from MovimentoEstoque movimentoEstoque ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao.lancamentos as lancamento ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao.chamadaEncalhes as chamadaEncalhe ");
		hql.append(" LEFT JOIN chamadaEncalhe.chamadaEncalheCotas as chamadaEncalheCota ");
		hql.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao.produto.fornecedores as fornecedor ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigo() != null) { 
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
				hql.append(" order by movimentoEstoque.estoqueProduto.produtoEdicao.numeroEdicao ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> buscarParametrosVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null){ 
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

	
}
