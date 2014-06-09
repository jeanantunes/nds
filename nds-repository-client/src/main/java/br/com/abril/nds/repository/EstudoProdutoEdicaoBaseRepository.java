package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;

public interface EstudoProdutoEdicaoBaseRepository {

	List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId);

	void copiarEdicoesBase(Long idOrigem, Long estudoDividido);

}
