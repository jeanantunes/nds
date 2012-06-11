package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.SlipVendaEncalheDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;


/**
 * 
 * Interface de serviços referentes a venda de produtos de encalhe
 * 
 * @author Discover Technology
 *
 */
public interface VendaEncalheService {
	

	/**
	 * Obtém dados da venda encalhe por id
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return List<SlipVendaEncalheDTO>
	 */
	 List<SlipVendaEncalheDTO> obtemDadosSlip(long idCota, Date dataInicio, Date dataFim);
		
	 
	 /**
	 * Gera Array de Bytes do Slip de Venda de Encalhe
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	  * @return byte[]
	 */
	 byte[] geraImpressaoVendaEncalhe(long idCota, Date dataInicio, Date dataFim);

	 
	 
	 /**
	 * Gera Array de Bytes do Slip de Venda de Suplementar
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	  * @return byte[]
	 */
	 byte[] geraImpressaoVendaSuplementar(long idCota, Date dataInicio, Date dataFim);


	 /**
	 * Retorna os dados de uma determinada venda de encalhe
	 * 
	 * @param idVendaEncalhe - identificador da venda de encalhe 
	 * 
	 * @return VendaEncalheDTO
	 */
	VendaEncalheDTO buscarVendaEncalhe(Long idVendaEncalhe);
	
	/**
	 * Efetiva uma venda de encalhe
	 * 
	 * @param vendaEncalheDTO - dados referente a vernda de encalhe 
	 */
	void efetivarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO);
	
	/**
	 * Exclui uma venda de encalhe efetivada
	 * 
	 * @param idVendaEncalhe - identificador da venda de encalhe
	 */
	void excluirVendaEncalhe(Long idVendaEncalhe);
	
	/**
	 * Altera uma venda de encalhe efetivada.
	 * 
	 * @param vendaEncalheDTO - dados referente a vernda de encalhe 
	 */
	void alterarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO);
	
	/**
	 * Retorna o slip no formato PDF referente a venda de encalhe.
	 *  
	 * @param idVendaEncalhe - identificador da venda de encalhe
	 * 
	 * @return  byte[]
	 * 
	 */
	byte[] gerarSlipVendaEncalhe(Long idVendaEncalhe);
	
	/**
	 * 
	 * Retorna as informações de um produto edição, candidato a venda de encalhe.
	 * 
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - número da edição do produto
	 * @param tipoVendaEncalhe - tipo de venda referente ao produto em estoque
	 * @return VendaEncalheDTO
	 */
	VendaEncalheDTO buscarProdutoComEstoque(String codigoProduto,Long numeroEdicao, TipoVendaEncalhe tipoVendaEncalhe);
	
	List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro);
	
	Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro);
	
}
