package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
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