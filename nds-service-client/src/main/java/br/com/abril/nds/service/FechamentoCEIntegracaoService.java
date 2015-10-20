package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;

public interface FechamentoCEIntegracaoService {
	
	List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	/**
	 * Fecha C.E. Integração
	 * @param filtro
	 * @param diferencas
	 */
	void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro, Map<Long,ItemFechamentoCEIntegracaoDTO> diferencas);
	
	/**
	 * Salva C.E. Integração
	 * @param filtro
	 * @param diferencas
	 */
	void salvarCE(List<ItemFechamentoCEIntegracaoDTO> itens);
	
	/**
     * Realiza estorno de movimentos de estoque gerados no fechamento da CE Fornecedor
     * Cancela a diferença (falta/sobra)
     * 
     * @param cef
     * @param dataOperacao
     */
	void estornarCeIntegracao(ChamadaEncalheFornecedor cef, Date dataOperacao);

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca);

	FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro, BigDecimal qntVenda);

	String reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarImpressaoChamadaEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoDTO obterDiferencaCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtroCE,Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlteradosFechamento);
	
	FechamentoCEIntegracaoConsolidadoDTO obterConsolidadoCE(FiltroFechamentoCEIntegracaoDTO filtro);

	void fecharSemCE(List<ItemFechamentoCEIntegracaoDTO> itens);
	

	 public List <ChamadaEncalheFornecedor> obterChamadasEncalheFornecedorCE(FiltroFechamentoCEIntegracaoDTO filtro);
}
