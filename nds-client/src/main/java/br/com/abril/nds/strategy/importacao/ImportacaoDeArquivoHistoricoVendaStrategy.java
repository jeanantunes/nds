package br.com.abril.nds.strategy.importacao;

import java.io.File;


import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MappingIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;

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
	protected void processarDados(Object input) {
		
		HistoricoVendaInput vendaInput = (HistoricoVendaInput) input;
		vendaInput.setIdUsuario(2L); //Usuario de Importacao
		movimentoEstoqueService.processarRegistroHistoricoVenda(vendaInput);
	}
	
}
