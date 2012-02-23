package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.service.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Transactional
	@Override
	public List<Pessoa> buscaPorCnpj(String cnpj) {
		return pessoaRepository.buscarPorCnpj(cnpj);
	}
	
}
