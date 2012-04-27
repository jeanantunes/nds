package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;


public interface ItemNotaFiscalSaidaRepository extends Repository<ItemNotaFiscalSaida, Long> {
	
	public List<ItemDanfe> obterListaItemNotaFiscalSaidaDadosDanfe(Long idNotaFiscal);
	
}
