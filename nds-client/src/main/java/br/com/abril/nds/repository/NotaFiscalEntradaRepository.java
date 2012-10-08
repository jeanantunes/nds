package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;

public interface NotaFiscalEntradaRepository extends Repository<NotaFiscalEntrada, Long> {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
		
	List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscalEntrada notaFiscal);
	NotaFiscalEntrada obterNotaFiscalPorNumero(String numero);
	
	List<NotaFiscalEntrada> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<ItemDTO<Long, String>> obterListaFornecedorNotaFiscal(List<Long> listaIdNotaFiscal);
	
}
