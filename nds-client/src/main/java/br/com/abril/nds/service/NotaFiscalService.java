package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

/**
 * Inteface do serviço de Nota Fiscal.
 * 
 * @author Discover Technology
 *
 */
public interface NotaFiscalService {

	/**
	 * Obtém um mapa do total de itens (produtos/serviços) de notas fiscais a serem geradas por cota.
	 * 
	 * @param consultaLoteNotaFiscal - dados da consulta em lote de notas fiscais
	 * 
	 * @return {@link Map} de Total de Itens (produtos/serviços) por Cota
	 */
	Map<Long, Integer> obterTotalItensNotaFiscalPorCotaEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal);
	
	/**
	 * Gera os dados de notas fiscais em lote.
	 * 
	 * @param consultaLoteNotaFiscal - dados da consulta em lote de notas fiscais
	 * 
	 * @return {@link List} de {@link NotaFiscal}
	 */
	List<NotaFiscal> gerarDadosNotaFicalEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal);
	
	/**
	 * Processa o retorno de uma nota fiscal. Validando os arquivos de notas que ja foram retornados.
	 *  
	 * @param listaDadosRetornoNFE - informações de retorno da comunicação eletrônica
	 */
	List<RetornoNFEDTO> processarRetornoNotaFiscal(List<RetornoNFEDTO> listaDadosRetornoNFE);
	
	/**
	 * Autoriza uma nota fiscal.
	 * 
	 * @param dadosRetornoNFE - dados de retorno da nota fiscal
	 */
	void autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE);
	
	/**
	 * Cancela uma nota fiscal.
	 * 
	 * @param id - id da nota fiscal
	 */
	void cancelarNotaFiscal(Long id);
	
	/**
	 * Denega uma nota fiscal.
	 * 
	 * @param id - id da nota fiscal
	 */
	void denegarNotaFiscal(Long id);

	/**
	 * Envia uma nota fiscal.
	 * 
	 * @param id - id da nota fiscal
	 */
	void enviarNotaFiscal(Long id);
	
	/**
	 * Exporta um arquivo contendo as NFes que serão enviadas para o Software Emissor Gratuito do Governo
	 * 
	 * @throws FileNotFoundException caso o diretório parametrizado de exportação das seja inválido. 
	 * @throws IOException caso ocarra erros durante a gravação do arquivo no diretório
	 */
	void exportarNotasFiscais() throws FileNotFoundException, IOException; 

	public abstract void emitiNotaFiscal(long idTipoNotaFiscal, Date dataEmissao,
			Long idCota, List<ItemNotaFiscal> listItemNotaFiscal, InformacaoTransporte transporte, InformacaoAdicional informacaoAdicional); 
	
}
