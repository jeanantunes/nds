package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
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
	
	void excluirDesconto(Long idDesconto, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto);
	
	void incluirDesconto(BigDecimal desconto, List<Long> fornecedores,Usuario usuario);
	
	void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario);
	
	/**
	 * Método que realiza a inclusão de um tipo de desconto para produto.
	 * 
	 * @param desconto - Desconto Produto
	 * 
	 * @param usuario - Usuário.
	 */
	void incluirDesconto(DescontoProdutoDTO desconto, Usuario usuario);
	
	/**
	 * Método que retorna uma coleção com as cotas relacionadas ao tipo de desconto especificado.
	 * 
	 * @param idDescontoProduto - ID do Tipo de desconto.
	 * 
	 * @return - Set<Cota> - As cotas relacionadas.
	 */
	Set<Cota> obterCotasDoTipoDescontoProduto(Long idDescontoProduto);
}
	
