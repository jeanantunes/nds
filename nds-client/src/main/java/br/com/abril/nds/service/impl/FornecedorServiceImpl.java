package br.com.abril.nds.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
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
	
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		
		if (idFornecedor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFornecedor é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor, idsIgnorar);
		
		List<Telefone> listaTel = this.telefoneFornecedorRepository.buscarTelefonesPessoaPorFornecedor(idFornecedor);
		
		for (TelefoneAssociacaoDTO tDto : listaTelAssoc){
			listaTel.remove(tDto.getTelefone());
		}
		
		for (Telefone telefone : listaTel){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = new TelefoneAssociacaoDTO(false, telefone, null);
			listaTelAssoc.add(telefoneAssociacaoDTO);
		}
		
		return listaTelAssoc;
	}
}