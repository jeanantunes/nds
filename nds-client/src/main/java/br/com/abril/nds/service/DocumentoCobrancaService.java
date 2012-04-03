package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface DocumentoCobrancaService {

	byte[] gerarDocumentoCobranca(String nossoNumero);
	
	void enviarDocumentoCobrancaPorEmail(String nossoNumero);
	
	byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas,TipoCobranca tipoCobranca);
	
}