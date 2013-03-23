package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface FechamentoCEIntegracaoService {
	
	List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro);

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca);

	FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);

	void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	void atualizarItemChamadaEncalheFornecedor(Long idItemChamadaFornecedor, BigInteger encalhe, BigInteger venda);

}
