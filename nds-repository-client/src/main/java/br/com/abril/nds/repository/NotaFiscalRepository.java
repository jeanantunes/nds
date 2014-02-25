package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
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
	
	public Integer obterQtdeRegistroNotaFiscal(FiltroMonitorNfeDTO filtro);
	
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro);

	List<Long> obterNumerosNFePorLancamento(Long idLancamento);

	public abstract NotaFiscal buscarNotaFiscalNumeroSerie(RetornoNFEDTO dadosRetornoNFE);
	
	public abstract List<NotaFiscal> obterListaNotasFiscaisNumeroSerie(FiltroMonitorNfeDTO filtro);
	
	public abstract NotaFiscal obterChaveAcesso(RetornoNFEDTO dadosRetornoNFE);
}