package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;

public interface FechamentoCEIntegracaoRepository {
	
	List<FechamentoCEIntegracaoDTO> buscarConferenciaEncalhe(FiltroFechamentoCEIntegracaoDTO filtro);
	
	Long obterDesconto(Long idCota, Long idProdutoEdica, Long idFornecedor);

}
