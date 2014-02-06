package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;

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

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca);

	FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro, BigDecimal qntVenda);
	
	void atualizarItemChamadaEncalheFornecedor(Long idItemChamadaFornecedor, BigInteger encalhe, BigInteger venda);

	String reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);

}
