package br.com.abril.nds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.PessoaFisicaRepository;

@Service
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Override
	@Transactional
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

		return this.pessoaFisicaRepository.merge(pessoaFisica);
	}
}
