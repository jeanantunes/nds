package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	public void persistirDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, Set<ProdutoEdicao> produtos, BigDecimal valorDesconto){
		
		DescontoProdutoEdicao descontoProdutoEdicao = null;
		
		for(ProdutoEdicao produto : produtos){
			
			descontoProdutoEdicao =  descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(fornecedor,cota,produto);
			
			if(descontoProdutoEdicao == null){
				descontoProdutoEdicao = new DescontoProdutoEdicao();
				descontoProdutoEdicao.setCota(cota);
				descontoProdutoEdicao.setFornecedor(fornecedor);
				descontoProdutoEdicao.setProdutoEdicao(produto);
			}
			
			descontoProdutoEdicao.setDesconto(valorDesconto);
			descontoProdutoEdicao.setTipoDesconto(tipoDesconto);
			
			descontoProdutoEdicaoRepository.merge(descontoProdutoEdicao);
		}
	}
	
	@Override
	
	@Transactional
	public void removerDescontos(Fornecedor fornecedor,Cota cota,TipoDesconto tipoDesconto){
		
		Set<DescontoProdutoEdicao> descontosParaExclusao = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(tipoDesconto, fornecedor, cota);
		
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
