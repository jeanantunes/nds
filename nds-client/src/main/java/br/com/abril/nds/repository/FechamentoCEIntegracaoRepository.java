package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface FechamentoCEIntegracaoRepository {
	
	List<FechamentoCEIntegracaoDTO> buscarConferenciaEncalhe(FiltroFechamentoCEIntegracaoDTO filtro);
	
	void fecharCE(Long encalhe, ProdutoEdicao produtoEdicao);

	boolean verificarStatusSemana();
	
}
