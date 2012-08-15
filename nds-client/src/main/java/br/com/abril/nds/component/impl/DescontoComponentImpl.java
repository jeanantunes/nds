package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
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
		
		//TODO criar logica de filtragem de produtos passiveis de desconto
		
		return null;
	}
}
