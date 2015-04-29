package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;

@Service
public class EstudoProdutoEdicaoBaseServiceImpl implements EstudoProdutoEdicaoBaseService {

	@Autowired
	private EstudoProdutoEdicaoBaseRepository estudoProdutoEdicaoBaseRepository;
	
	@Override
	@Transactional
	public List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId) {
		return estudoProdutoEdicaoBaseRepository.obterEdicoesBase(estudoId);
	}

	@Override
	@Transactional
	public void copiarEdicoesBase(Long idOrigem, Long estudoDividido) {
		estudoProdutoEdicaoBaseRepository.copiarEdicoesBase(idOrigem, estudoDividido);
		
	}

}