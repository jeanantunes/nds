package br.com.abril.nds.component;

import java.math.BigDecimal;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

public interface DescontoComponent {
	
	/**
	 * Persiste os dados de um tipo de desconto para N produtos associados a uma cota e fornecedor.
	 * 
	 * @param tipoDesconto - tipo de desconto (GERAL,ESPECIFICO,PRODUTO)
	 * @param fornecedor - fornecedor associado aos produtos informados
	 * @param cota - cota associada ao fornecedor informado
	 * @param produtos - produtos candidatos a receberer o desconto
	 * @param valorDesconto - valor do desconto atribuido aos produtos
	 */
	void persistirDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota, Set<ProdutoEdicao> produtos, BigDecimal valorDesconto);
	
	/**
	 * Filtra os produtos para atibuição de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto (GERAL,ESPECIFICO,PRODUTO)
	 * @param fornecedor -  fornecedor associado aos produtos informados
	 * @param produtos - produtos candidatos a receberer o desconto
	 * @return Set<ProdutoEdicao> - produtos que receberão desconto
	 */
	Set<ProdutoEdicao> filtrarProdutosPassiveisDeDesconto(TipoDesconto tipoDesconto,Fornecedor fornecedor,Cota cota,Set<ProdutoEdicao> produtos);
	
	/**
	 * Remove so desconto produto edição conforme parâmentros informados.
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @param tipoDesconto - tipo de desconto
	 */
	void removerDescontos(Fornecedor fornecedor,Cota cota,TipoDesconto tipoDesconto);
	
	/**
	 * Remove so desconto produto edição conforme parâmentros informados.
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param produtoEdicao - produto edição
	 */
	void removerDescontos(Fornecedor fornecedor,Cota cota,ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto);
}
