package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;

/**
 * Estratégia de importação de arquivos referente a Histórico Financeiro
 * 
 *  */
@Component("importacaoDeArquivoHistoricoFinanceiroStrategy")
public class ImportacaoDeArquivoHistoricoFinanceiroStrategy extends ImportacaoAbstractStrategy<HistoricoFinanceiroInput> implements ImportacaoArquivoStrategy {

	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
		
	@Override
	protected void processarDados(Object input) {
		
		HistoricoFinanceiroInput vendaInput = (HistoricoFinanceiroInput) input;
		
		movimentoFinanceiroCotaService.processarRegistrohistoricoFinanceiro(vendaInput);
	}

	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {		
		return processarArquivo(arquivo, HistoricoFinanceiroInput.class);
	}
	
}
