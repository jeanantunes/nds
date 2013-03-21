package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface FechamentoCEIntegracaoService {
	
	List<ItemFechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro);
	
	void fecharCE(String[] listaEncalhePronta, String[] listaIdProdutoEdicaoPronta, String idFornecedor, Integer numeroSemana);

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca);

	FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoConsolidadoDTO buscarFechamentoEncalheTotal(FiltroFechamentoCEIntegracaoDTO filtro);

	void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);

}
