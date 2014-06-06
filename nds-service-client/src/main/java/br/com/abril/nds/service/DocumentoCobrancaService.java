package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface DocumentoCobrancaService {

	/**
	 * BOLETO
	 * 
	 * Gera documento de cobrança
	 * @param nossoNumero
	 */
	byte[] gerarDocumentoCobranca(String nossoNumero);
	
	byte[] gerarDocumentoCobranca(String nossoNumero, boolean incrementarVias);
	
	/**
	 * BOLETO
	 * 
	 * Envia Cobranca por Email
	 * @param nossoNumero
	 */
	void enviarDocumentoCobrancaPorEmail(String nossoNumero);
	
	/**
	 * BOLETO
	 * 
	 * Gerar documento de Cobranca
	 * @param dividas
	 * @param tipoCobranca
	 */
	byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas,TipoCobranca tipoCobranca);

	/**
	 * SLIP
	 * 
	 * Gera Slip da Cobrança
	 * @param idControleConferenciaEncalheCota
	 * @param incluirNumeroSlip
	 * @param tpArquivo
	 * @return byte[]
	 */
	byte[] gerarSlipCobranca(Long idControleConferenciaEncalheCota,boolean incluirNumeroSlip, TipoArquivo tpArquivo);
	
	/**
	 * SLIP
	 * 
	 * Gera Slip da Cobrança
	 * @param nossoNumero
	 * @param incluirNumeroSlip
	 * @param tpArquivo
	 * @return byte[]
	 */
	byte[] gerarSlipCobranca(String nossoNumero, boolean incluirNumeroSlip,
			TipoArquivo tpArquivo);

	/**
     * SLIP
     * 
     * Gera Slip da Cobrança para Impressão em Impressora Matricial
     * @param idControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @return byte[]
     */
	byte[] gerarSlipCobrancaMatricial(Long idControleConferenciaEncalheCota,boolean incluirNumeroSlip);

	/**
	 * RECIBO
	 * 
	 * Gera Recibo da Cobrança
	 * @param nossoNumero
	 * @return byte[]
	 */
    byte[] gerarReciboCobranca(String nossoNumero);

    /**
     * SLIP DTO
     * 
     * Gera Slip da Cobrança
     * 
     * @param nossoNumero
     * @param incluirNumeroSlip
     * @param tpArquivo
     * @return SlipDTO
     */
	SlipDTO gerarSlipDTOCobranca(Long idControleConferenciaEncalheCota,boolean incluirNumeroSlip);

	/**
     * SLIP DTO
     * 
     * Gera lista de SlipDTO por lista de controle de conferencia de encalhe
     * 
     * @param idsControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @return SlipDTO
     */
	List<SlipDTO> gerarListaSlipDTOCobranca(List<Long> idsControleConferenciaEncalheCota,boolean incluirNumeroSlip);

	/**
     * SLIP
     * 
     * Gera lista de Slip da Cobranças
     * 
     * @param slipDTO
     * @param tpArquivo
     * @return List<byte[]>
     */
	List<byte[]> gerarListaSlipCobranca(List<SlipDTO> listaSlipDTO, TipoArquivo tpArquivo);
	
}