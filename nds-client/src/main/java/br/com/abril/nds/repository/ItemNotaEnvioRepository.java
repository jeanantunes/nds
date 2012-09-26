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
}
