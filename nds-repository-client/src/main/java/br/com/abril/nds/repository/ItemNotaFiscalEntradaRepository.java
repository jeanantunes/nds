package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


public interface ItemNotaFiscalEntradaRepository extends Repository<ItemNotaFiscalEntrada, Long> {
	
	List<ItemNotaFiscalEntrada> buscarItensPorIdNota(Long idNotaFiscal);
	
	List<ItemDanfe> obterListaItemNotaFiscalEntradaDadosDanfe(Long idNotaFiscal);

	public abstract Long quantidadePorControleConferenciaEncalheCota(
			long idControleConferencia);

	public abstract List<ItemNotaFiscalEntrada> obtemPorControleConferenciaEncalheCota(long idControleConferencia,
			String  orderBy, Ordenacao ordenacao, Integer firstResult, Integer maxResults);

	
}
