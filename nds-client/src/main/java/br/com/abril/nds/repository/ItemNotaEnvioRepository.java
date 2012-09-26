package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;

public interface ItemNotaEnvioRepository  extends Repository<ItemNotaEnvio, ItemNotaEnvioPK>{
	
	/**
	 * 
	 * Retorna Itens Nota de Envio dado sua data de emissão e pessoa a qual foi emitida.
	 * 
	 * @param pessoa - pessoa a qual a nota foi emitida
	 * 
	 * @param dataEmissao - data de emissão da nota
	 * 
	 * @return List<ItemNotaEnvio>
	 */
	List<ItemNotaEnvio> obterItensNotaEnvioPorDataEmissao(Date dataEmissao, Pessoa pessoa);
}
