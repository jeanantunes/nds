package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.vo.RetornoImportacaoArquivoVO;

/**
 * Estratégia de importação de arquivos referente a Histórico de Vendas
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoHistoricoVendaStrategy")
public class ImportacaoDeArquivoHistoricoVendaStrategy extends ImportacaoAbstractStrategy<HistoricoVendaInput> implements ImportacaoArquivoStrategy {


	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		 
		return processarArquivo(arquivo, HistoricoVendaInput.class);
	}
		
	@Override
	protected void processarDados(Object input, Date dataOperacao) {
		
		HistoricoVendaInput vendaInput = (HistoricoVendaInput) input;
		vendaInput.setIdUsuario(2L); //Usuario de Importacao
		movimentoEstoqueService.processarRegistroHistoricoVenda(vendaInput, dataOperacao);
	}
	
}
