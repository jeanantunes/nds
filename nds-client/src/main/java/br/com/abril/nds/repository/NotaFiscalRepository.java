package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;

public interface NotaFiscalRepository extends Repository<NotaFiscal, Long>  {

	
	/**
	 * Obtém notas fiscais que possuem o status de processamento interno igual ao informado no parâmetro .
	 * 
	 * @param statusProcessamentoInterno
	 * @return lista de notas fiscais.
	 */
	public abstract List<NotaFiscal> obterListaNotasFiscaisPor(StatusProcessamentoInterno statusProcessamentoInterno);
	
}
