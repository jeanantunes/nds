package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface DescontoRepository extends Repository<Desconto, Long> {

	List<Fornecedor> buscarFornecedoresQueUsamDescontoGeral(Desconto desconto);
	
	List<Cota> buscarCotasQueUsamDescontoEspecifico(Desconto desconto);
	
	List<Fornecedor> buscarFornecedoresQueUsamDescontoEspecifico(Desconto desconto);
	
	List<Produto> buscarProdutosQueUsamDescontoProduto(Desconto desconto);
	
	List<ProdutoEdicao> buscarProdutosEdicoesQueUsamDescontoProduto(Desconto desconto);
	
	Desconto buscarUltimoDescontoValido(Long idDesconto, Fornecedor fornecedor);
	
}
