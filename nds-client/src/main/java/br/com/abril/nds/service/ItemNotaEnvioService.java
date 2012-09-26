package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;

public interface ItemNotaEnvioService {

	List<ItemNotaEnvio> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota);
	
}
