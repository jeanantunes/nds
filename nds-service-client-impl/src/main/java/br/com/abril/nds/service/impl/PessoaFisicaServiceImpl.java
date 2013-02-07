package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.PessoaFisicaRepository;
import br.com.abril.nds.service.PessoaFisicaService;

/**
 * Implementação do serviço
 * {@link br.com.abril.nds.service.PessoaFisicaServiceImpl}
 * 
 * @author Discover Technology
 *
 */
@Service
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	/**
	 * @see br.com.abril.nds.service.PessoaFisicaService#salvarPessoaFisica(PessoaFisica)
	 */
	@Override
	@Transactional
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

		return this.pessoaFisicaRepository.merge(pessoaFisica);
	}

	/**
	 * @see br.com.abril.nds.service.PessoaFisicaService#buscarPorCpf(java.lang.String)
	 */
	@Override
	@Transactional
	public PessoaFisica buscarPorCpf(String cpf) {

		return this.pessoaFisicaRepository.buscarPorCpf(cpf);
	}
}
