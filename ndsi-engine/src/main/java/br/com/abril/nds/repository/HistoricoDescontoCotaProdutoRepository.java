package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface HistoricoDescontoCotaProdutoRepository extends Repository<HistoricoDescontoCotaProdutoExcessao, Long> {

	/**
	 * Retorna os descontos da cota com os fornecedores
	 * @param filtro - filtro de cosnulta
	 * @return List<DescontoDistribuidor> 
	 */
	List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro);
	
	/**
	 * Retorna a quantidade de descontos da cota com os fornecedores
	 * @param filtro - filtro de cosnulta
	 * @return Integer
	 */
	Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro);
		
	/**
	 * Retorna o ultimo desconto valido da cota com um fornecedor
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return DescontoDistribuidor
	 */
	HistoricoDescontoCotaProdutoExcessao buscarUltimoDescontoValido(Cota cota, Fornecedor fornecedor);
	
}
