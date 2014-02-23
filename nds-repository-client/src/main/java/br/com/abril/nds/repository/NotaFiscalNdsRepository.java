package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;

public interface NotaFiscalNdsRepository extends Repository<NotaFiscalNds, Long>  {

	/**
	 * Metodos para nota fiscal
	 */
	public abstract List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro);
	
	public abstract List<Cota> obterConjuntoCotasNotafiscal(FiltroNFeDTO filtro);

	/**
	 * Obtem naturezas de operacao pelo tipo de destinatario
	 * @param tipoDestinatario
	 */
	public abstract List<ItemDTO<Long, String>> obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario);

	public abstract Long consultaCotaExemplaresSumarizadosQtd(FiltroNFeDTO filtro);
	
	public abstract List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroNFeDTO filtro);
	
	public abstract List<EstoqueProduto> obterConjuntoFornecedorNotafiscal(FiltroNFeDTO filtro);

	public abstract List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro);
	
	public abstract List<EstoqueProduto> obterEstoques(FiltroNFeDTO filtro);

	public abstract Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro);
}