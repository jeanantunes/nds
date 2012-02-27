package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;

public interface NotaFiscalService {

	List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal);
	
}
