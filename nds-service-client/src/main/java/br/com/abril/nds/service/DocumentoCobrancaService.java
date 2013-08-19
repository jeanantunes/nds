package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface DocumentoCobrancaService {

	/**
	 * Retorna um arquivo de uma cobrança segundo seu número.
	 * @param nossoNumero -nosso número referente a cobrança.
	 * @return byte[] 
	 */
	byte[] gerarDocumentoCobranca(String nossoNumero);
	
	/**
	 * Envia um documento de cobrança por e-mail dado seu número
	 * @param nossoNumero - nosso número referente a cobrança.
	 */
	void enviarDocumentoCobrancaPorEmail(String nossoNumero);
	
	/**
	 * Retorna um arquivo com um grupo de cobranças segundo os parâmetros informados.
	 * @param dividas - objetos com os dados da divida gerada
	 * @param tipoCobranca - tipo de cobrança 
	 * @return byte[]
	 */
	byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas,TipoCobranca tipoCobranca);
	
}