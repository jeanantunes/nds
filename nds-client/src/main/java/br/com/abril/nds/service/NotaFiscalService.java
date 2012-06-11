package br.com.abril.nds.service;

import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;

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
	 * Processa o retorno de uma nota fiscal.
	 * 
	 * @param id - id da nota fiscal
	 * @param retornoComunicacaoEletronica - informações de retorno da comunicação eletrônica
	 */
	void processarRetornoNotaFiscal(Long id, RetornoComunicacaoEletronica retornoComunicacaoEletronica);
	
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

}
