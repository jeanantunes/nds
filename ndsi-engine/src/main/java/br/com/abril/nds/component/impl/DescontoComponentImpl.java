package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.read.ListAppender;

import br.com.abril.nds.component.DescontoComponent;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;

@Component
public class DescontoComponentImpl implements DescontoComponent {

	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Override
	@Transactional
	public void persistirDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, Set<ProdutoEdicao> produtos, BigDecimal valorDesconto, Boolean descontoPredominante){
		
		DescontoProdutoEdicao descontoProdutoEdicao = null;
		
		List<DescontoProdutoEdicao> listaDescontosProdutoEdicao = new ArrayList<DescontoProdutoEdicao>();
		
		for(ProdutoEdicao produto : produtos){
			
			descontoProdutoEdicao =  descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(null, fornecedor,cota,produto);
			
			if (!this.aplicarDesconto(
					valorDesconto, descontoProdutoEdicao, descontoPredominante)) {
					
				continue;
			}
			
			if (descontoProdutoEdicao == null) {
				
				descontoProdutoEdicao = new DescontoProdutoEdicao();
				descontoProdutoEdicao.setCota(cota);
				descontoProdutoEdicao.setFornecedor(fornecedor);
				descontoProdutoEdicao.setProdutoEdicao(produto);
			
			}
			
			descontoProdutoEdicao.setDesconto(valorDesconto);
			descontoProdutoEdicao.setTipoDesconto(tipoDesconto);
			
			listaDescontosProdutoEdicao.add(descontoProdutoEdicao);
			
		}
		
		descontoProdutoEdicaoRepository.salvarListaDescontoProdutoEdicao(listaDescontosProdutoEdicao);
		
	}
	
	/*
	 * Verifica se o dever ser aplicado o desconto de acordo com o valor do desconto e a predominância.
	 */
	private boolean aplicarDesconto(BigDecimal novoDesconto,
									DescontoProdutoEdicao descontoProdutoEdicao,
									Boolean descontoPredominante) {
		
		if (descontoPredominante == null || descontoPredominante) {
			
			return true;
		}
		
		if (descontoProdutoEdicao == null) {
		
			return true;
		}
		
		if (descontoProdutoEdicao.getTipoDesconto().equals(TipoDesconto.PRODUTO)) {
			
			return true;
		}
		
		BigDecimal descontoExistente = descontoProdutoEdicao.getDesconto();
		
		if (descontoExistente.compareTo(novoDesconto) <= 0) {
			
			return true;
		}
		
		return false;
	}
	
	@Override
	@Transactional
	public void removerDescontos(Fornecedor fornecedor,Cota cota,TipoDesconto tipoDesconto){
		
		Set<DescontoProdutoEdicao> descontosParaExclusao = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(tipoDesconto, fornecedor, cota);
		
		processarRemocaoDesconto(descontosParaExclusao);
	}
	
	@Override
	@Transactional
	public void removerDescontos(Fornecedor fornecedor,Cota cota,ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Set<DescontoProdutoEdicao> descontosParaExclusao = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(tipoDesconto, fornecedor, cota, produtoEdicao);
		
		processarRemocaoDesconto(descontosParaExclusao);
	}

	private void processarRemocaoDesconto(Set<DescontoProdutoEdicao> descontosParaExclusao) {
		
		for(DescontoProdutoEdicao desconto : descontosParaExclusao){
			
			descontoProdutoEdicaoRepository.remover(desconto);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Set<ProdutoEdicao> filtrarProdutosPassiveisDeDesconto(TipoDesconto tipoDesconto,Fornecedor fornecedor,Cota cota,Set<ProdutoEdicao> produtos) {
		
		switch (tipoDesconto) {
			
			case PRODUTO:
				return produtos;
			
			case ESPECIFICO:
				return filtrarProdutosCota(tipoDesconto,fornecedor,produtos,cota);
			
			case GERAL:
				return filtrarProdutoDistribuidor(tipoDesconto,fornecedor,cota,produtos);
			
			default:
				throw new RuntimeException("Tipo de Desconto inválido!");
		}
	}

	private Set<ProdutoEdicao> filtrarProdutoDistribuidor(TipoDesconto tipoDesconto, Fornecedor fornecedor,Cota cota,Set<ProdutoEdicao> produtos) {
		
		List<DescontoProdutoEdicao> descontosProduto = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(tipoDesconto, fornecedor,cota);
		
		if(descontosProduto.isEmpty()){
			return produtos;
		}
		
		Set<ProdutoEdicao> produtosCandidatosDesconto = new HashSet<ProdutoEdicao>();
		produtosCandidatosDesconto.addAll(produtos);
		
		for(ProdutoEdicao prd : produtos){
			
			if(this.contains(prd, descontosProduto,null)){
				produtosCandidatosDesconto.remove(prd);
			}
		}
		
		return produtosCandidatosDesconto;
	}

	private Set<ProdutoEdicao> filtrarProdutosCota(TipoDesconto tipoDesconto, Fornecedor fornecedor,Set<ProdutoEdicao> produtos, Cota cota) {
		
		List<DescontoProdutoEdicao> descontosProduto = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(tipoDesconto, fornecedor,cota);
		
		if(descontosProduto.isEmpty()){
			return produtos;
		}
		
		Set<ProdutoEdicao> produtosCandidatosDesconto = new HashSet<ProdutoEdicao>();
		produtosCandidatosDesconto.addAll(produtos);
		
		for(ProdutoEdicao prd : produtos){
			
			if(!this.contains(prd, descontosProduto,TipoDesconto.GERAL)){
				produtosCandidatosDesconto.remove(prd);
			}
		}
		
		return produtosCandidatosDesconto;
	}
	
	private boolean contains(ProdutoEdicao produtoEdicao, List<DescontoProdutoEdicao> descontos,TipoDesconto tipoDesconto){
		
		for(DescontoProdutoEdicao desc : descontos){
			
			if(desc.getProdutoEdicao().equals(produtoEdicao)){	
				
				return (tipoDesconto!= null) ? (desc.getTipoDesconto().equals(tipoDesconto)):true; 
			}
		}
		return false;
	}
}
