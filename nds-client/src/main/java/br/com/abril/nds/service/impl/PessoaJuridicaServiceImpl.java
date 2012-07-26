package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.service.PessoaJuridicaService;

@Service
public class PessoaJuridicaServiceImpl implements PessoaJuridicaService {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Transactional
	public PessoaJuridica buscarPorCnpj(String cnpj) {
		return pessoaJuridicaRepository.buscarPorCnpj(cnpj);
	}
	
	@Transactional
	public PessoaJuridica buscarCnpjPorFornecedor(String nomeFantasia){
		return pessoaJuridicaRepository.buscarCnpjPorFornecedor(nomeFantasia);
	}

	/**
	 * @see br.com.abril.nds.service.PessoaJuridicaService#salvarPessoaJuridica(br.com.abril.nds.model.cadastro.PessoaJuridica)
	 */
	@Override
	@Transactional
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {
		
		return this.pessoaJuridicaRepository.merge(pessoaJuridica);
	}

	@Override
	@Transactional
	public PessoaJuridica buscarPorId(Long id) {		 
		return this.pessoaJuridicaRepository.buscarPorId(id);
	}

}
