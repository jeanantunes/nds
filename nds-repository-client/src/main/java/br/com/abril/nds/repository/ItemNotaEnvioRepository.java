package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;

public interface ItemNotaEnvioRepository  extends Repository<ItemNotaEnvio, ItemNotaEnvioPK>{
	
	/**
	 * Obtém Itens Nota de Envio de acordo com os parâmetros informados.
	 * 
	 * @param dataEmissao - data de emissão da nota
	 * @param numeroCota - número da cota
	 * 
	 * @return List<DetalheItemNotaFiscalDTO>
	 */
	List<DetalheItemNotaFiscalDTO> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota);
	
	/**
	 * Obtém Item da Nota de Envio de acordo com os parâmetros informados.
	 * 
	 * @param dataEmissao - data de emissão da nota
	 * @param numeroCota - número da cota
	 * @param idProdutoEdicao - identificador do produto edição
	 * 
	 * @return DetalheItemNotaFiscalDTO
	 */
	DetalheItemNotaFiscalDTO obterItemNotaEnvio(Date dataEmissao,
												Integer numeroCota,
												Long idProdutoEdicao);
	
	List<ItemNotaEnvio> obterItemNotaEnvio(Long idLancamento);
	
	/**
	 * Obtém Item da Nota de Envio de acordo com os parâmetros informados.
	 * 
	 * @param dataLancamento - data de lançamento do produto
	 * @param numeroCota - número da cota
	 * @param idProdutoEdicao - identificador do produto edição
	 * 
	 * @return DetalheItemNotaFiscalDTO
	 */
	DetalheItemNotaFiscalDTO obterItemNotaEnvioLancamentoProduto(Date dataLancamento,
																		Integer numeroCota,
																		Long idProdutoEdicao);
	
	/**
	 * Obtém Itens Nota de Envio de acordo com os parâmetros informados.
	 * 
	 * @param dataLancamento - data de lançamento do produto
	 * @param numeroCota - número da cota
	 * 
	 * @return List<DetalheItemNotaFiscalDTO>
	 */
	List<DetalheItemNotaFiscalDTO> obterItensNotaEnvioLancamentoProduto(Date dataLancamento, Integer numeroCota);

	/**
	 * 
	 * @param idEstudo
	 */
	public abstract void removerItemNotaEnvioPorEstudo(Long idEstudo);
	
}
