package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.seguranca.Usuario;


/**
 * Interface que define serviços referentes aos desconto do sistema
 * 
 */
public interface DescontoService {

	List<TipoDescontoDTO> buscarTipoDesconto(FiltroTipoDescontoDTO filtro);
	
	Integer buscarQntTipoDesconto(FiltroTipoDescontoDTO filtro);
	
	List<TipoDescontoCotaDTO> buscarTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro);
	
	Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro);
	
	List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
	
	Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
	
	List<TipoDesconto> obterTodosTiposDescontos();
	
	TipoDesconto obterTipoDescontoPorID(Long id);
	
	/**
	 * Exclui um tipo de desconto, respeitando a data vigente desse desconto.
	 * 
	 * @param idDesconto - identificador do desconto
	 * @param tipoDesconto - tipo de desconto selecionado
	 */
	void excluirDesconto(Long idDesconto, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto);
	
	/**
	 * Inclui um desconto geral para diversos fornecedores.
	 * 
	 * @param desconto - valor do desconto
	 * @param fornecedores - fornecedores associados
	 * @param usuario - usuario
	 */
	void incluirDesconto(BigDecimal desconto, List<Long> fornecedores,Usuario usuario);
	
	/**
	 * Inclui um desconto especifico para uma determindad cota.
	 * 
	 * @param valorDesconto - valor do desconto
	 * @param fornecedores - fornecedores associados
	 * @param numeroCota - número da cota
	 * @param usuario - usuario
	 */
	void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario);
	
	void incluirDesconto(DescontoProdutoDTO desconto);

	/**
	 * Retorna os fornecedores associados a um desconto.
	 * 
	 * @param idDesconto - identificador do desconto
	 * @param tipoDesconto - tipo de desconto
	 * @return List<Fornecedor>
	 */
	List<Fornecedor> busacarFornecedoresAssociadosADesconto(Long idDesconto, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto);
}
	
