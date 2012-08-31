package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;

public interface FechamentoCEIntegracaoService {
	
	List<FechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro);
	
	Long obterDesconto(Long idCota, Long idProdutoEdica, Long idFornecedor);
	
	List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento);

}
