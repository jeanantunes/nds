package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.component.DescontoComponent;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

@Component
public class DescontoComponentImpl implements DescontoComponent {

	@Override
	@Transactional
	public void persistirDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, Set<ProdutoEdicao> produtos, BigDecimal valorDesconto){
		
		//TODO criar logica de persistencia de dados do desconto produto edição
	}
	
	@Override
	@Transactional(readOnly=true)
	public Set<ProdutoEdicao> filtrarProdutosPassiveisDeDesconto(TipoDesconto tipoDesconto,Fornecedor fornecedor,Set<ProdutoEdicao> produtos) {
		
		//TODO criar logica de filtragem de produtos passiveis de desconto
		
		return null;
	}
}
