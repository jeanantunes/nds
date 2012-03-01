package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;

public interface NotaFiscalRepository extends Repository<NotaFiscal, Long> {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
		
	List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscal notaFiscal);
	NotaFiscal obterNotaFiscalPorNumero(String numero);
	
}
