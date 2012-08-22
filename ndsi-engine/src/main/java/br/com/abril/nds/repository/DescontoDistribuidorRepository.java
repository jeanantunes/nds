package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface DescontoDistribuidorRepository extends Repository<DescontoDistribuidor, Long> {

	/**
	 * Retorna os descontos do distribuidor dado aos fornecedores
	 * @param filtro - filtro de cosnulta
	 * @return List<DescontoDistribuidor> 
	 */
	List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro);
	
	/**
	 * Retorna a quantidade de descontos do distribuidor dado aos fornecedores
	 * @param filtro - filtro de cosnulta
	 * @return Integer
	 */
	Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro);
		
	/**
	 * Retorna o ultimo desconto valido do distribuidor a um fornecedor
	 * 
	 * @param idUltimoDesconto - id do desconto a ser excluido
	 * @param fornecedor - fornecedor
	 * 
	 * @return DescontoDistribuidor
	 */
	DescontoDistribuidor buscarUltimoDescontoValido(Long idUltimoDesconto, Fornecedor fornecedor);
	
	/**
	 * Retorna o ultimo desconto valido do distribuidor a um fornecedor
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return DescontoDistribuidor
	 */
	DescontoDistribuidor buscarUltimoDescontoValido(Fornecedor fornecedor);
}
