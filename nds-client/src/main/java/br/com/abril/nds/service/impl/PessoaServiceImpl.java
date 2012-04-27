package br.com.abril.nds.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

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
	public List<PessoaFisica> obterSociosPorFiador(Long idFiador, Set<Long> idsIgnorar, Set<String> cpfsIgnorar) {
		
		return this.pessoaRepository.obterSociosPorFiador(idFiador, idsIgnorar, cpfsIgnorar);
	}
	

	/**
	 * @see br.com.abril.nds.service.PessoaService#salvarPessoa(br.com.abril.nds.model.cadastro.Pessoa)
	 */
	@Override
	@Transactional
	public void salvarPessoa(Pessoa pessoa) {

		this.pessoaRepository.adicionar(pessoa);
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
	
	@Transactional(readOnly = true)
	@Override
	public PessoaFisica buscarPessoaPorCPF(String cpf, boolean isFiador, String cpfConjuge){
		
		if (cpf == null || cpf.trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "CPF é obrigatório.");
		}
		
		cpf = cpf.trim();
		
		PessoaFisica pessoaFisica = this.pessoaRepository.buscarPorCPF(cpf);
		
		if (pessoaFisica != null && !isFiador && pessoaFisica.getConjuge() != null && !pessoaFisica.getConjuge().getCpf().equals(cpfConjuge)){
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					"A pessoa de CPF " + Util.adicionarMascaraCPF(pessoaFisica.getCpf()) + " já é conjuge de outra pessoa.");
		}
		
		return pessoaFisica;
	}

	@Transactional(readOnly = true)
	@Override
	public PessoaJuridica buscarPessoaPorCNPJ(String cnpj) {
		
		if (cnpj == null || cnpj.trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "CNPJ é obrigatório.");
		}
		
		cnpj = cnpj.trim();
		
		return this.pessoaRepository.buscarPorCNPJ(cnpj);
	}
}
