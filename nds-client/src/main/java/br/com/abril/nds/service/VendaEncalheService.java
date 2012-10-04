package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;


/**
 * 
 * Interface de serviços referentes a venda de produtos de encalhe
 * 
 * @author Discover Technology
 *
 */
public interface VendaEncalheService {

	 /**
	 * Retorna os dados de uma determinada venda de encalhe
	 * 
	 * @param idVendaEncalhe - identificador da venda de encalhe 
	 * 
	 * @return VendaEncalheDTO
	 */
	VendaEncalheDTO buscarVendaEncalhe(Long idVendaEncalhe);
	
	/**
	 * Efetiva uma venda de encalhe, e retorna o comprovante da venda.
	 * 
	 * @param vendaEncalheDTO - dados referente a vernda de encalhe 
	 * @param numeroCota - número da cota
	 * @param dataVencimentoDebito - data de vencimento do débito
	 * @param usuario - usúario que efetivou a venda
	 */
	  byte[] efetivarVendaEncalhe(List<VendaEncalheDTO> vendaEncalheDTO, Long numeroCota,Date dataVencimentoDebito,Usuario usuario);
	
	/**
	 * Exclui uma venda de encalhe efetivada
	 * 
	 * @param idVendaEncalhe - identificador da venda de encalhe
	 */
	void excluirVendaEncalhe(Long idVendaEncalhe);
	
	/**
	 * Altera uma venda de encalhe efetivada, e retorna o comprovante da venda..
	 * 
	 * @param vendaEncalheDTO - dados referente a vernda de encalhe 
	 */
	 byte[] alterarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO,Date dataVencimentoDebito, Usuario usuario);

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
	
	byte[] geraImpressaoVenda(FiltroVendaEncalheDTO filtro);
	
	/**
	 * Gera a compravante de impressão apartir do id da venda.
	 * @param idVenda
	 * @return
	 */
	public abstract byte[] geraImpressaoComprovanteVenda(Long idVenda);
	
}
