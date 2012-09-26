package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;

public interface ItemNotaEnvioService {

	List<DetalheItemNotaFiscalDTO> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota);
	
}
