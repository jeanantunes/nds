package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto da cota
 * 
 * @author Discover Technology
 */
public interface DescontoCotaRepository extends Repository<DescontoCota, Long> {
	
	/**
	 * Obtem lista de dados de Tipo de Desconto Específico da Cota
	 * @param filtro
	 * @return List<TipoDescontoCotaDTO>
	 */
	List<TipoDescontoCotaDTO> obterDescontoCota(FiltroTipoDescontoCotaDTO filtro);
	
	/**
	 * Obtem quantidade de dados de Tipo de Desconto Específico da Cota
	 * @param filtro
	 * @return int
	 */
	int obterQuantidadeDescontoCota(FiltroTipoDescontoCotaDTO filtro);

	/**
	 * Retorna o ultimo desconto válido da cota de um forncedor
	 * 
	 * @param idDesconto - identificador do ultimo desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @return DescontoCota
	 */
	DescontoCota buscarUltimoDescontoValido(Long idDesconto,Fornecedor fornecedor, Cota cota);

}
