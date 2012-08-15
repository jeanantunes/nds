package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
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
	@Transactional(readOnly=true)
	public Set<ProdutoEdicao> filtrarProdutosPassiveisDeDesconto(TipoDesconto tipoDesconto,Fornecedor fornecedor,Set<ProdutoEdicao> produtos) {
		
		Cota cota = null;
		
		switch (tipoDesconto) {
			
			case PRODUTO:
				return produtos;
			
			case ESPECIFICO:
				return filtrarProdutosCota(tipoDesconto,fornecedor,produtos,cota);
			
			case GERAL:
				return filtrarProdutoDistribuidor(tipoDesconto,fornecedor,produtos);
			
			default:
				throw new RuntimeException("Tipo de Desconto inválido!");
		}
	}

	private Set<ProdutoEdicao> filtrarProdutoDistribuidor(TipoDesconto tipoDesconto, Fornecedor fornecedor,Set<ProdutoEdicao> produtos) {
		
		List<DescontoProdutoEdicao> descontosProduto = descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicaoNotInTipoDesconto(tipoDesconto, fornecedor);
		
		if(descontosProduto.isEmpty()){
			return produtos;
		}
		
		for(ProdutoEdicao prd : produtos){
			
			if(this.contains(prd, descontosProduto,null)){
				produtos.remove(prd);
			}
		}
		
		return produtos;
	}

	private Set<ProdutoEdicao> filtrarProdutosCota(TipoDesconto tipoDesconto, Fornecedor fornecedor,Set<ProdutoEdicao> produtos, Cota cota) {
		
		List<DescontoProdutoEdicao> descontosProduto = descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicaoNotInTipoDesconto(tipoDesconto, fornecedor,cota);
		
		if(descontosProduto.isEmpty()){
			return produtos;
		}
		
		for(ProdutoEdicao prd : produtos){
			
			if(!this.contains(prd, descontosProduto,TipoDesconto.GERAL)){
				produtos.remove(prd);
			}
		}
		
		return produtos;
	}
	
	private boolean contains(ProdutoEdicao produtoEdicao, List<DescontoProdutoEdicao> descontos,TipoDesconto tipoDesconto){
		
		for(DescontoProdutoEdicao desc : descontos){
			
			if(desc.getProdutoEdicao().equals(produtoEdicao)){	
				
				if(tipoDesconto!= null){
					
					if(desc.getTipoDesconto().equals(tipoDesconto)){	
						return true;
					}
					else{
						return false;
					}
				}
				else{
					return true;
				}
			}
		}
		return false;
	}
}
