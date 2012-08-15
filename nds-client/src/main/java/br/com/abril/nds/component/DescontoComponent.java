package br.com.abril.nds.component;

import java.math.BigDecimal;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

public interface DescontoComponent {

	public void persistirDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, Set<ProdutoEdicao> produtos, BigDecimal valorDesconto);
	
	public Set<ProdutoEdicao> filtrarProdutosPassiveisDeDesconto(TipoDesconto tipoDesconto,Fornecedor fornecedor,Set<ProdutoEdicao> produtos);
}
