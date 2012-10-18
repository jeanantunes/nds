package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;

/**
 * Estratégia de importação de arquivos referente a Histórico Financeiro
 * 
 *  */
@Component("importacaoDeArquivoHistoricoFinanceiroStrategy")
public class ImportacaoDeArquivoHistoricoFinanceiroStrategy extends ImportacaoAbstractStrategy implements ImportacaoArquivoStrategy {

	private static final String IDENTIFICADOR_LINHA = ";";
	
	private static final int POSICAO_CODIGO_PRODUTO = 3;
	private static final int POSICAO_NUMERO_COTA = 2;
	private static final int POSICAO_QNT_RECEBIDA_PRODUTO = 11;
	private static final int POSICAO_QNT_DEVOLVIDA_PRODUTO = 7;
	

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
	protected HistoricoFinanceiroInput parseDados(String linha) {
		
		String[] dados = linha.split(IDENTIFICADOR_LINHA);
		
		if(dados == null){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		HistoricoFinanceiroInput historicoFinanceiro = new HistoricoFinanceiroInput();
		
		try{
			
			atribuirValorCodigoProdutoNumeroEdicao(historicoFinanceiro, dados);
			atribuirValorNumeroCota(historicoFinanceiro, dados);
			atribuirValorQuantidadeDevolvidaProduto(historicoFinanceiro, dados);
			atribuirValorQuantidadeRecebidaProduto(historicoFinanceiro, dados);
			
		}catch ( java.lang.NumberFormatException e) {
			
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		catch (java.lang.StringIndexOutOfBoundsException e) {
			
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
	
		return historicoFinanceiro;
	}
	/**
	 * 
	 * Valida e atribui os valores de código do produto e número edição
	 * 
	 * @param historicoFinanceiro
	 * @param dados
	 */
	private void atribuirValorCodigoProdutoNumeroEdicao(HistoricoFinanceiroInput historicoFinanceiro, String[]dados){
		
		if(dados[POSICAO_CODIGO_PRODUTO] == null || dados[POSICAO_CODIGO_PRODUTO].isEmpty() ){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		String codigoPublicacao = dados[POSICAO_CODIGO_PRODUTO];
		
		if(codigoPublicacao == null || codigoPublicacao.isEmpty() || codigoPublicacao.length() < 12){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		atribuirValorCodigoProduto(historicoFinanceiro, codigoPublicacao);
		atribuirValorNumeroEdicao(historicoFinanceiro, codigoPublicacao);
	}
	
	/**
	 * Valida e atribui o valor do código do produto
	 * 
	 * @param historicoFinanceiro
	 * @param codigoPublicacao
	 */
	private void atribuirValorCodigoProduto(HistoricoFinanceiroInput historicoFinanceiro,String codigoPublicacao){
		
		Integer codigoProduto = Integer.parseInt(codigoPublicacao.trim().substring(0,8));
		
		if(codigoProduto == null ){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		//historicoFinanceiro.setCodigoProduto(codigoPublicacao.trim().substring(0,8));
	}
	
	/**
	 * Valida e atribui o valor do número de edição
	 * 
	 * @param historicoFinanceiro
	 * @param codigoPublicacao
	 */
	private void atribuirValorNumeroEdicao(HistoricoFinanceiroInput historicoFinanceiro, String codigoPublicacao){
		
		Integer numeroEdicao = Integer.parseInt(codigoPublicacao.trim().substring(8,12));
		
		if(numeroEdicao == null){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		//historicoFinanceiro.setNumeroEdicao(numeroEdicao);
	}
	
	/**
	 * Valida e atribui o valor do número da cota
	 * 
	 * @param historicoFinanceiro
	 * @param dados
	 */
	private void atribuirValorNumeroCota(HistoricoFinanceiroInput historicoFinanceiro, String[]dados){
		
		if(dados[POSICAO_NUMERO_COTA] == null || dados[POSICAO_NUMERO_COTA].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer numeroCota = Integer.parseInt(dados[POSICAO_NUMERO_COTA].trim());
		
		historicoFinanceiro.setNumeroCota(numeroCota);
	}
	/**
	 * Valida e atribui o valor da quantidade devolvida de produto
	 * 
	 * @param historicoFinanceiro
	 * @param dados
	 */
	private void atribuirValorQuantidadeDevolvidaProduto(HistoricoFinanceiroInput historicoFinanceiro,String[]dados){
		
		if(dados[POSICAO_QNT_DEVOLVIDA_PRODUTO] == null || dados[POSICAO_QNT_DEVOLVIDA_PRODUTO].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer quantidadeDevolvidaProduto = Integer.parseInt(dados[POSICAO_QNT_DEVOLVIDA_PRODUTO].trim());
	
		//historicoFinanceiro.setQuantidadeDevolvidaProduto(quantidadeDevolvidaProduto);
	}
	
	/**
	 * 
	 * Valida e atribui o valor da quantidade recebida de produto
	 * 
	 * @param historicoFinanceiro
	 * @param dados
	 */
	private void atribuirValorQuantidadeRecebidaProduto(HistoricoFinanceiroInput historicoFinanceiro,String[]dados){
		
		if(dados[POSICAO_QNT_RECEBIDA_PRODUTO] == null || dados[POSICAO_QNT_RECEBIDA_PRODUTO].trim().isEmpty()){
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
		
		Integer quantidadeRecebidaProduto = Integer.parseInt(dados[POSICAO_QNT_RECEBIDA_PRODUTO].trim());
	
		//historicoFinanceiro.setQuantidadeRecebidaProduto(quantidadeRecebidaProduto);
	}
	
	@Override
	protected void processarDados(Object input) {
		
		HistoricoFinanceiroInput vendaInput = (HistoricoFinanceiroInput) input;
		
//		movimentoEstoqueService.processarRegistrohistoricoFinanceiro(vendaInput);
	}
	
}
