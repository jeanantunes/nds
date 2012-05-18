package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.util.TipoMensagem;

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
	
	@Override
	@Transactional
	public void cadastrarEnderecos(List<EnderecoAssociacaoDTO> listaEnderecos, Pessoa pessoa){
		
		if (listaEnderecos != null){
		
			boolean isEnderecoPrincipal = false;
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecos){
				
				if (isEnderecoPrincipal && enderecoAssociacao.isEnderecoPrincipal()){
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um endereço principal é permitido.");
				}
				
				if (enderecoAssociacao.isEnderecoPrincipal()){
					isEnderecoPrincipal = enderecoAssociacao.isEnderecoPrincipal();
				}
			}
			
			for (EnderecoAssociacaoDTO enderecoAssociacaoDTO : listaEnderecos){
				
				this.validarEndereco(enderecoAssociacaoDTO.getEndereco(), enderecoAssociacaoDTO.getTipoEndereco());
				
				enderecoAssociacaoDTO.getEndereco().setPessoa(pessoa);
				
				if (enderecoAssociacaoDTO.getEndereco().getId() == null){
					
					this.enderecoRepository.adicionar(enderecoAssociacaoDTO.getEndereco());
				} else {
					
					this.enderecoRepository.alterar(enderecoAssociacaoDTO.getEndereco());
				}
			}
		}
	}

	private void validarEndereco(Endereco endereco, TipoEndereco tipoEndereco) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (endereco == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Endereço é obrigatório.");
		}
		
		if (tipoEndereco == null){
			
			listaMensagens.add("O preenchimento do campo [Tipo Endereço] é obrigatório.");
		}
		
		if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [CEP] é obrigatório.");
		}

		if (endereco.getTipoLogradouro() == null || endereco.getTipoLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Tipo Logradouro] é obrigatório.");
		}
		
		if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Logradouro] é obrigatório.");
		}

		if (endereco.getNumero() <= 0) {
			
			listaMensagens.add("O preenchimento do campo [Número] é obrigatório.");
		}
		
		if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Bairro] é obrigatório.");
		}		

		if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Cidade] é obrigatório.");
		}
		
		if (endereco.getUf() == null || endereco.getUf().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [UF] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
		
	}

	@Override
	@Transactional
	public void removerEnderecos(Collection<Long> idsEndereco) {
		
		if (idsEndereco != null && !idsEndereco.isEmpty()){
			
			try{
				
				this.enderecoRepository.removerEnderecos(idsEndereco);
				
			}catch (RuntimeException e) {
				
				if( e instanceof org.springframework.dao.DataIntegrityViolationException){
					throw new ValidacaoException(TipoMensagem.ERROR,"Exclusão de endereço não permitida, registro possui dependências!");
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Endereco buscarEnderecoPorId(Long idEndereco) {
		
		return this.enderecoRepository.buscarPorId(idEndereco);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> buscarEnderecosPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar){
		
		List<EnderecoAssociacaoDTO> ret = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<Endereco> lista = this.enderecoRepository.buscarEnderecosPessoa(idPessoa, idsIgnorar);
		
		for (Endereco endereco : lista){
			
			EnderecoAssociacaoDTO dto = new EnderecoAssociacaoDTO(false, null, null, endereco);
			ret.add(dto);
		}
		
		return ret;
	}
}
