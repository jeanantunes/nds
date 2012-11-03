package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;

public interface NotaFiscalEntradaService {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	DetalheNotaFiscalDTO obterDetalhesNotaFical(Long idNotaFiscal);

	void inserirNotaFiscal(NotaFiscalEntradaCota notaFiscal, Integer numeroCota, Long idControleConferenciaEncalheCota);
	
	NotaFiscalEntrada obterNotaFiscalPorNumero(String numero);
	
	List<NotaFiscalEntrada> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<ItemDTO<Long, String>> obterFornecedorNotaFiscal(List<Long> listaIdNotaFiscal);
	
	/**
	 * Obtém a Cota pelo número da cota.
	 * 
	 * @param numeroCota
	 * @return
	 */
	Cota obterPorNumerDaCota(Integer numeroCota);
	
	/**
	 * Obtém o Fornecedor pelo ID.
	 * 
	 * @param idFornecedor
	 * @return
	 */
	Fornecedor obterFornecedorPorID(Long idFornecedor);

}
