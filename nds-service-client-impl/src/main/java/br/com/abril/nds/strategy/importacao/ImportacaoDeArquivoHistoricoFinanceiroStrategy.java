package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;
import br.com.abril.nds.vo.RetornoImportacaoArquivoVO;

/**
 * Estratégia de importação de arquivos referente a Histórico Financeiro
 * 
 *  */
@Component("importacaoDeArquivoHistoricoFinanceiroStrategy")
public class ImportacaoDeArquivoHistoricoFinanceiroStrategy extends ImportacaoAbstractStrategy<HistoricoFinanceiroInput> implements ImportacaoArquivoStrategy {

	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
		
	@Override
	protected void processarDados(Object input, Date dataOperacao) {
		
		HistoricoFinanceiroInput vendaInput = (HistoricoFinanceiroInput) input;
		
		movimentoFinanceiroCotaService.processarRegistrohistoricoFinanceiro(vendaInput, dataOperacao);
	}

	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {		
		return processarArquivo(arquivo, HistoricoFinanceiroInput.class);
	}
	
}
