package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;

public interface ImpressaoDividaService {
	
	/**
	 * Retorna um arquivo no formato pdf de uma divida para ser impresso.
	 * @param nossoNumero
	 * @return byte[]
	 */
	byte[] gerarArquivoImpressao(String nossoNumero);
	
	/**
	 * Retorna um arquivo no formato pdf de todas as dividas para ser impresso.
	 * @param filtro
	 * @return byte[]
	 */
	byte[] gerarArquivoImpressao(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Envia uma divida por e-mail
	 * @param nossoNumero
	 */
	void enviarArquivoPorEmail(String nossoNumero);
	
	/**
	 * Retorna uma lista de dividas geradas.
	 * @param filtro
	 * @return List<GeraDividaDTO>
	 */
	List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Retorna a quantidade de dividas geradas.
	 * @param filtro
	 * @return Long
	 */
	Long obterQuantidadeDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Verifica se ja foi gerado divida para data de movimento informada.
	 * @param dataMovimento
	 * @return Boolean
	 */
	Boolean validarDividaGerada(Date dataMovimento);

}
