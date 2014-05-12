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
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;

public interface NotaFiscalRepository extends Repository<NotaFiscal, Long>  {

	
	/**
	 * Obtém notas fiscais que possuem o status de processamento interno igual ao informado no parâmetro .
	 * 
	 * @param statusProcessamentoInterno
	 * @return lista de notas fiscais.
	 */
	List<NotaFiscal> obterListaNotasFiscaisPor(StatusProcessamento statusProcessamentoInterno);
	
	public Integer obterQtdeRegistroNotaFiscal(FiltroMonitorNfeDTO filtro);
	
	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro);

	List<Long> obterNumerosNFePorLancamento(Long idLancamento);

	NotaFiscal buscarNotaFiscalNumeroSerie(RetornoNFEDTO dadosRetornoNFE);
	
	List<NotaFiscal> obterListaNotasFiscaisNumeroSerie(FiltroMonitorNfeDTO filtro);
	
	NotaFiscal obterChaveAcesso(RetornoNFEDTO dadosRetornoNFE);
	
	/**
	 * Metodos para nota fiscal
	 */
	List<CotaExemplaresDTO> consultaCotaExemplaresMECSumarizados(FiltroNFeDTO filtro);
	
	List<Cota> obterConjuntoCotasNotafiscal(FiltroNFeDTO filtro);

	/**
	 * Obtem naturezas de operacao pelo tipo de destinatario
	 * @param tipoDestinatario
	 */
	List<ItemDTO<Long, String>> obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario);

	Long consultaCotaExemplaresMECSumarizadosQtd(FiltroNFeDTO filtro);
	
	List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroNFeDTO filtro);
	
	List<EstoqueProduto> obterConjuntoFornecedorNotafiscal(FiltroNFeDTO filtro);

	List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro);
	
	List<EstoqueProduto> obterEstoques(FiltroNFeDTO filtro);

	Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro);
	
	Long consultaCotaExemplaresMFFSumarizadosQtd(FiltroNFeDTO filtro);
	
	List<CotaExemplaresDTO> consultaCotaExemplaresMFFSumarizados(FiltroNFeDTO filtro);

	List<MovimentoFechamentoFiscal> obterMovimentosFechamentosFiscaisFornecedor(FiltroNFeDTO filtro);

	List<FornecedorExemplaresDTO> consultaFornecedorExemplaresMFFSumarizados(FiltroNFeDTO filtro);

	Long consultaFornecedorExemplaresMFFSumarizadosQtd(FiltroNFeDTO filtro);

	List<FornecedorExemplaresDTO> consultaFornecedorExemplaresMESumarizados(FiltroNFeDTO filtro);
	
}