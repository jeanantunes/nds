package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.service.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Override
	@Transactional
	public void removerEndereco(Endereco endereco) {

		this.enderecoRepository.remover(endereco);
	}

	@Override
	@Transactional
	public Endereco salvarEndereco(Endereco endereco) {

		return this.enderecoRepository.merge(endereco);
	}

	@Override
	@Transactional
	public Endereco obterEnderecoPorId(Long idEndereco) {

		return this.enderecoRepository.buscarPorId(idEndereco);
	}
}
