package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;

public interface NotaFiscalService {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<NotaFiscalFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	DetalheNotaFiscalDTO obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscal notaFiscal);
	
	NotaFiscal obterNotaFiscalPorNumero(String numero);


}
