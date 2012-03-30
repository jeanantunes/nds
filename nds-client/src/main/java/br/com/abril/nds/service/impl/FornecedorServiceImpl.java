package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.service.FornecedorService;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Transactional
	public Fornecedor obterFornecedorUnico(String codigoProduto) {
		
		List<Fornecedor> fornecedores =  fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
		
		if(fornecedores == null || fornecedores.size()!=1) {
			throw new IllegalStateException("O produto não possui um único fornecedor");
		}
		
		return fornecedores.get(0);
		
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedores() {
		return fornecedorRepository.obterFornecedores();
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedoresAtivos() {
		return fornecedorRepository.obterFornecedoresAtivos();
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedores(String cnpj) {
		return fornecedorRepository.obterFornecedores(cnpj);
	}

	@Transactional(readOnly = true)
	public List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes) {
		return fornecedorRepository.obterFornecedores(permiteBalanceamento,
				situacoes);
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedoresPorProduto(String codigoProduto,
														GrupoFornecedor grupoFornecedor) {
		
		return fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, grupoFornecedor);
	}
	
	@Transactional
	public Fornecedor obterFornecedorPorId(Long id) {
		
		return this.fornecedorRepository.buscarPorId(id);
	}
	
}
