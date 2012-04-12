package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.service.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Transactional
	@Override
	public List<PessoaJuridica> buscarPorCnpj(String cnpj) {
		return pessoaRepository.buscarPorCnpj(cnpj);
	}

	@Transactional(readOnly = true)
	@Override
	public List<PessoaFisica> obterSociosPorFiador(Long idFiador) {
		
		return this.pessoaRepository.obterSociosPorFiador(idFiador);
	}
	
	@Transactional(readOnly = true)
	@Override
	public PessoaFisica buscarPessoaFisicaPorId(Long idPessoa){
		
		Pessoa pessoa = this.pessoaRepository.buscarPorId(idPessoa);
		
		if (pessoa instanceof PessoaFisica){
			return (PessoaFisica) pessoa;
		} else {
			return null;
		}
	}
}
