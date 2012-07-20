package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.service.MovimentoEstoqueService;
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
	
	private static final int POSICAO_CODIGO_PRODUTO = 2;
	private static final int POSICAO_NUMERO_COTA = 3;
	private static final int POSICAO_QNT_RECEBIDA_PRODUTO = 5;
	private static final int POSICAO_QNT_DEVOLVIDA_PRODUTO = 8;
	

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
		
		Object[] dados = linha.split(IDENTIFICADOR_LINHA);
		
		HistoricoVendaInput historicoVenda = new HistoricoVendaInput();

		/*historicoVenda.setCodigoProduto(codigoProduto);
		historicoVenda.setNumeroEdicao(numeroEdicao);
		historicoVenda.setNumeroCota(numeroCota);
		historicoVenda.setQuantidadeDevolvidaProduto(quantidadeDevolvidaProduto);
		historicoVenda.setQuantidadeRecebidaProduto(quantidadeRecebidaProduto);
		*/
		return historicoVenda;
		
	}
	
	@Override
	protected void processarDados(Object input) {
		
		HistoricoVendaInput vendaInput = (HistoricoVendaInput) input;
		
		// FIXME implementar a logica de negocio de importação
		
	}

	

}
