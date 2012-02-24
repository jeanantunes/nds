package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;

public interface NotaFiscalDAO extends Repository<NotaFiscal, Long> {

	List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal);
	
}
