package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.util.Intervalo;

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
	Map<Cota, QuantidadePrecoItemNotaDTO> obterTotalItensNotaFiscalPorCotaEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal);
	
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
	 * @param dadosRetornoNFE
	 */
	void cancelarNotaFiscal(RetornoNFEDTO dadosRetornoNFE);
	
	/**
	 * Denega uma nota fiscal.
	 * 
	 * @param dadosRetornoNFE
	 */
	void denegarNotaFiscal(RetornoNFEDTO dadosRetornoNFE);

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
	void exportarNotasFiscais(List<NotaFiscal> notasFiscaisParaExportacao) throws FileNotFoundException, IOException; 

	public abstract Long emitiNotaFiscal(long idTipoNotaFiscal, Date dataEmissao,
			Long idCota, List<ItemNotaFiscal> listItemNotaFiscal, InformacaoTransporte transporte, InformacaoAdicional informacaoAdicional, List<NotaFiscalReferenciada> listNotaFiscalReferenciada); 
	
	
	/**
	 * Obtém itens para nota fiscal respeitando os parametros.
	 * 
	 * @param periodo periodo de lançamento de um movimento
	 * @param listaIdFornecedores id dos fornecedores que serão pesquisados(se for null, busca todos)
	 * @param listaIdProdutos id dos produtos que serão pesquisados(se for null, busca todoso)
	 * @param tipoNotaFiscal TODO
	 * @param Cota cota
	 * @return lista de itens para nota fiscal
	 */
	List<ItemNotaFiscal> obterItensNotaFiscalPor(Distribuidor distribuidor, 
			Cota cota, Intervalo<Date> periodo, List<Long> listaIdFornecedores, List<Long> listaIdProdutos, TipoNotaFiscal tipoNotaFiscal);
	
	/**
	 * Cria uma nota fiscal referenciada a partir de uma nota fiscal
	 * 
	 * @param notaFiscal
	 * @return notaFiscaReferenciada
	 */
	NotaFiscalReferenciada converterNotaFiscalToNotaFiscalReferenciada(NotaFiscal notaFiscal);
	
	/**
	 * Obtém informações de transpote pela Cota
	 * 
	 * @param idCota id da cota
	 * @return InformacaoTransporte
	 */
	InformacaoTransporte obterTransporte(Long idCota);
	
	/**
	 * Obtem Notas Referenciadas por itens da nota.
	 * 
	 * @param listaItensNotaFiscal 
	 * @return lista de notas referenciadas.
	 */
	List<NotaFiscalReferenciada> obterNotasReferenciadas(List<ItemNotaFiscal> listaItensNotaFiscal); 
}
