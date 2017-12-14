package br.com.abril.nds.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;

public interface EstudoProdutoEdicaoBaseService {

	public List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId);

	public void copiarEdicoesBase(Long id, Long estudoDividido);

	EdicaoBaseEstudoDTO obterEdicoesBaseEstudoOrigemCopiaEstudo(Long estudoId);
	
}
