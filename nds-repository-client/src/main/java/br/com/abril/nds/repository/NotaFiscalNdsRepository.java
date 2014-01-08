package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nfe.model.NotaFiscalBase;

public interface NotaFiscalNdsRepository extends Repository<NotaFiscalNds, Long>  {

	/**
	 * Metodos para nota fiscal
	 */
	public abstract List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroViewNotaFiscalDTO filtro);
	
	public abstract List<Cota> obterConjuntoCotasNotafiscal(FiltroViewNotaFiscalDTO filtro);

	/**
	 * Obtem naturezas de operacao pelo tipo de destinatario
	 * @param tipoDestinatario
	 */
	public abstract List<ItemDTO<Long, String>> obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario);

	public abstract Long consultaCotaExemplaresSumarizadosQtd(FiltroViewNotaFiscalDTO filtro);
	
	public abstract List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroViewNotaFiscalDTO filtro);

	void salvarNotasFiscais(List<NotaFiscalNds> notasFiscais);

	public abstract List<MovimentoEstoque> obterConjuntoDistribuidorNotafiscal(FiltroViewNotaFiscalDTO filtro);

	public abstract List<NotaFiscalBase> obterNotafiscal(FiltroViewNotaFiscalDTO filtro);
}