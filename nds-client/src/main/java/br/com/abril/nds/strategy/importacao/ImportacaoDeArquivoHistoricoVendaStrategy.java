package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;

/**
 * Estratégia de importação de arquivos referente a Histórico de Vendas
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoHistoricoVendaStrategy")
public class ImportacaoDeArquivoHistoricoVendaStrategy extends ImportacaoAbstractStrategy implements ImportacaoArquivoStrategy {

	private static final String IDENTIFICADOR_LINHA = ";";
	
	private static final int POSICAO_CODIGO_PRODUTO = 3;
	private static final int POSICAO_NUMERO_COTA = 2;
	private static final int POSICAO_QNT_RECEBIDA_PRODUTO = 7;
	private static final int POSICAO_QNT_DEVOLVIDA_PRODUTO = 11;
	

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		 
		return processarArquivo(arquivo);
	}
	
	@Override
	public void processarImportacaoDados(Object input){
		processarDados(input);
	}

	@Override
	protected HistoricoVendaInput parseDados(String linha) {
		
		String[] dados = linha.split(IDENTIFICADOR_LINHA);
		
		if(dados == null){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		HistoricoVendaInput historicoVenda = new HistoricoVendaInput();
		
		try{
			
			atribuirValorCodigoProdutoNumeroEdicao(historicoVenda, dados);
			atribuirValorNumeroCota(historicoVenda, dados);
			atribuirValorQuantidadeDevolvidaProduto(historicoVenda, dados);
			atribuirValorQuantidadeRecebidaProduto(historicoVenda, dados);
			
		}catch ( java.lang.NumberFormatException e) {
			
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		catch (java.lang.StringIndexOutOfBoundsException e) {
			
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
	
		return historicoVenda;
	}
	/**
	 * 
	 * Valida e atribui os valores de código do produto e número edição
	 * 
	 * @param historicoVenda
	 * @param dados
	 */
	private void atribuirValorCodigoProdutoNumeroEdicao(HistoricoVendaInput historicoVenda, String[]dados){
		
		if(dados[POSICAO_CODIGO_PRODUTO] == null || dados[POSICAO_CODIGO_PRODUTO].isEmpty() ){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		String codigoPublicacao = dados[POSICAO_CODIGO_PRODUTO];
		
		if(codigoPublicacao == null || codigoPublicacao.isEmpty() || codigoPublicacao.length() < 12){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		atribuirValorCodigoProduto(historicoVenda, codigoPublicacao);
		atribuirValorNumeroEdicao(historicoVenda, codigoPublicacao);
	}
	
	/**
	 * Valida e atribui o valor do código do produto
	 * 
	 * @param historicoVenda
	 * @param codigoPublicacao
	 */
	private void atribuirValorCodigoProduto(HistoricoVendaInput historicoVenda,String codigoPublicacao){
		
		Integer codigoProduto = Integer.parseInt(codigoPublicacao.trim().substring(0,8));
		
		if(codigoProduto == null ){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		historicoVenda.setCodigoProduto(codigoProduto);
	}
	
	/**
	 * Valida e atribui o valor do número de edição
	 * 
	 * @param historicoVenda
	 * @param codigoPublicacao
	 */
	private void atribuirValorNumeroEdicao(HistoricoVendaInput historicoVenda, String codigoPublicacao){
		
		Integer numeroEdicao = Integer.parseInt(codigoPublicacao.trim().substring(8,12));
		
		if(numeroEdicao == null){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		historicoVenda.setNumeroEdicao(numeroEdicao);
	}
	
	/**
	 * Valida e atribui o valor do número da cota
	 * 
	 * @param historicoVenda
	 * @param dados
	 */
	private void atribuirValorNumeroCota(HistoricoVendaInput historicoVenda, String[]dados){
		
		if(dados[POSICAO_NUMERO_COTA] == null || dados[POSICAO_NUMERO_COTA].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer numeroCota = Integer.parseInt(dados[POSICAO_NUMERO_COTA].trim());
		
		historicoVenda.setNumeroCota(numeroCota);
	}
	/**
	 * Valida e atribui o valor da quantidade devolvida de produto
	 * 
	 * @param historicoVenda
	 * @param dados
	 */
	private void atribuirValorQuantidadeDevolvidaProduto(HistoricoVendaInput historicoVenda,String[]dados){
		
		if(dados[POSICAO_QNT_DEVOLVIDA_PRODUTO] == null || dados[POSICAO_QNT_DEVOLVIDA_PRODUTO].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer quantidadeDevolvidaProduto = Integer.parseInt(dados[POSICAO_QNT_DEVOLVIDA_PRODUTO].trim());
	
		historicoVenda.setQuantidadeDevolvidaProduto(quantidadeDevolvidaProduto);
	}
	
	/**
	 * 
	 * Valida e atribui o valor da quantidade recebida de produto
	 * 
	 * @param historicoVenda
	 * @param dados
	 */
	private void atribuirValorQuantidadeRecebidaProduto(HistoricoVendaInput historicoVenda,String[]dados){
		
		if(dados[POSICAO_QNT_RECEBIDA_PRODUTO] == null || dados[POSICAO_QNT_RECEBIDA_PRODUTO].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer quantidadeRecebidaProduto = Integer.parseInt(dados[POSICAO_QNT_RECEBIDA_PRODUTO].trim());
	
		historicoVenda.setQuantidadeRecebidaProduto(quantidadeRecebidaProduto);
	}
	
	@Override
	protected void processarDados(Object input) {
		
		HistoricoVendaInput vendaInput = (HistoricoVendaInput) input;
		
		movimentoEstoqueService.processarRegistroHistoricoVenda(vendaInput);
	}
	
}
