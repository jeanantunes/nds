package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;

public interface NotaFiscalRepository extends Repository<NotaFiscal, Long> {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
		
	List<NotaFiscalFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscal notaFiscal);
	NotaFiscal obterNotaFiscalPorNumero(String numero);
	
	List<NotaFiscal> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
}
