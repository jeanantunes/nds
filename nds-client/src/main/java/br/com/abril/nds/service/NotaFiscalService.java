package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.vo.estoque.DetalheNotaFiscalVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalDTO;

public interface NotaFiscalService {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheNotaFiscalVO> obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscal notaFiscal);
	
	NotaFiscal obterNotaFiscalPorNumero(String numero);


}
