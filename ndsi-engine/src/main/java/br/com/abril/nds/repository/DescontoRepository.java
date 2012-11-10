package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface DescontoRepository extends Repository<Desconto, Long> {

	List<Fornecedor> buscarFornecedoresQueUsam(Desconto desconto);

	Desconto buscarUltimoDescontoValido(Long idDesconto, Fornecedor fornecedor);
	
}
