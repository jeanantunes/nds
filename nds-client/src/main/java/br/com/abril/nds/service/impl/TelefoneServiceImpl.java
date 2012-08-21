package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.TelefoneEntregadorRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class TelefoneServiceImpl implements TelefoneService {

	@Autowired
	private TelefoneEntregadorRepository telefoneEntregadorRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Transactional
	@Override
	public void cadastrarTelefone(List<TelefoneAssociacaoDTO> listaTelefones, Pessoa pessoa){
		
		if (listaTelefones == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Lista de telefones é obrigatória.");
		}
		
		validarTelefonePrincipal(listaTelefones);
		
		for (TelefoneAssociacaoDTO associacaoTelefone : listaTelefones){
			this.valiadarTelefone(associacaoTelefone.getTelefone(), associacaoTelefone.getTipoTelefone());
			
			associacaoTelefone.getTelefone().setPessoa(pessoa);
			
			if (associacaoTelefone.getTelefone().getId() == null){
				this.telefoneRepository.adicionar(associacaoTelefone.getTelefone());
			} else {
				this.telefoneRepository.alterar(associacaoTelefone.getTelefone());
			}
		}
	}
	
	@Transactional
	@Override
	public void cadastrarTelefone(TelefoneAssociacaoDTO associacaoTelefone){
		
		this.valiadarTelefone(associacaoTelefone.getTelefone(), associacaoTelefone.getTipoTelefone());
		
		if (associacaoTelefone.getTelefone().getId() == null){
			this.telefoneRepository.adicionar(associacaoTelefone.getTelefone());
		} else {
			this.telefoneRepository.alterar(associacaoTelefone.getTelefone());
		}
	}
	
	private void valiadarTelefone(Telefone telefone, TipoTelefone tipoTelefone){
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (telefone == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Telefone é obrigatório.");
		}
		
		if (telefone.getDdd() == null || telefone.getDdd().trim().isEmpty()){
			mensagensValidacao.add("DDD é obrigatório.");
		} else if (telefone.getDdd().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo DDD.");
		}
		telefone.setDdd(telefone.getDdd() != null ? telefone.getDdd().trim() : null);
		
		if (telefone.getNumero() == null || telefone.getNumero().trim().isEmpty()){
			mensagensValidacao.add("Número é obrigatório.");
		} else if (telefone.getNumero().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo Número.");
		}
		telefone.setNumero(telefone.getNumero() != null ? telefone.getNumero().trim() : null);
		
		if (tipoTelefone == null){
			mensagensValidacao.add("Tipo Telefone é obrigatório.");
		}
		
		if (telefone.getRamal() != null && telefone.getRamal().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo Ramal.");
		}
		telefone.setRamal(telefone.getRamal() != null ? telefone.getRamal().trim() : null);
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	}
	
	@Override
	public void removerTelefones(Collection<Long> listaTelefones) {
		try {
			
			if (listaTelefones != null && !listaTelefones.isEmpty()){
				
				this.telefoneRepository.removerTelefones(listaTelefones);
			}
		} catch (RuntimeException e) {
			//caso o telefone esteja associado a outra pessoa na base de dados não pode ser apagado.
			//nem todas as associações estão definidas, por isso é melhor tratar dessa maneira do que fazer
			//selects que terão que ser alterados até que todas as associações estejam definidas.
			
			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
				throw new ValidacaoException(TipoMensagem.ERROR,"Exclusão de telefone não permitida, registro possui dependências!");
			}
		}
	}

	/**
	 * @see br.com.abril.nds.service.TelefoneService#salvarTelefonesEntregador(java.util.List)
	 */
	@Override
	@Transactional
	public void salvarTelefonesEntregador(List<TelefoneEntregador> listaTelefonesEntregador) {
		
		if (listaTelefonesEntregador != null) {
			
			boolean isTelefonePrincipal = false;
			
			for (TelefoneEntregador telefoneEntregador : listaTelefonesEntregador){
				
				if (telefoneEntregador == null) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone entregador é obrigatório.");
				}
				
				this.valiadarTelefone(telefoneEntregador.getTelefone(), telefoneEntregador.getTipoTelefone());
				
				if (isTelefonePrincipal && telefoneEntregador.isPrincipal()) {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
				}
				
				if (telefoneEntregador.isPrincipal()) {
					
					isTelefonePrincipal = telefoneEntregador.isPrincipal();
				}
				
				if (telefoneEntregador.getEntregador() == null || telefoneEntregador.getEntregador().getId() == null) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Entregador é obrigatório.");
				}
				
				if (telefoneEntregador.getTelefone().getId() == null) {
					
					this.telefoneRepository.adicionar(telefoneEntregador.getTelefone());
					
				} else {
					
					this.telefoneRepository.alterar(telefoneEntregador.getTelefone());
				}
				
				if (telefoneEntregador.getId() == null) {
					
					this.telefoneEntregadorRepository.adicionar(telefoneEntregador);
					
				} else {
					
					this.telefoneEntregadorRepository.alterar(telefoneEntregador);
				}
			}
		}
	}

	/**
	 * @see br.com.abril.nds.service.TelefoneService#removerTelefonesEntregador(java.util.Collection)
	 */
	@Override
	@Transactional
	public void removerTelefonesEntregador(Collection<Long> listaTelefonesEntregador) {
		
		if (listaTelefonesEntregador != null && !listaTelefonesEntregador.isEmpty()){
			
			this.telefoneEntregadorRepository.removerTelefonesEntregador(listaTelefonesEntregador);
			
			this.removerTelefones(listaTelefonesEntregador);
		}
	}
	
	@Override
	@Transactional
	public List<TelefoneAssociacaoDTO> buscarTelefonesPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar){
		
		List<Telefone> lista = this.telefoneRepository.buscarTelefonesPessoa(idPessoa, idsIgnorar);
		
		List<TelefoneAssociacaoDTO> ret = new ArrayList<TelefoneAssociacaoDTO>();
		
		if (lista != null && !lista.isEmpty()){
			
			for (Telefone telefone : lista){
				ret.add(new TelefoneAssociacaoDTO(false, null, null, telefone));
			}
		}
		
		return ret;
	}

	@Override
	@Transactional
	public Telefone buscarTelefonePorId(Long longValue) {
		
		return this.telefoneRepository.buscarPorId(longValue);
	}
	
	/**
	 * Valida se a lista tem pelo menos um e somente um telefone principal
	 * 
	 * @param listaTelefones lista de telefones para serem validados
	 */
	private void validarTelefonePrincipal(List<TelefoneAssociacaoDTO> listaTelefones) {
		boolean isTelefonePrincipal = false;
		boolean hasTelefonePrincipal = false;
		for (TelefoneAssociacaoDTO dto : listaTelefones){
			
			if (isTelefonePrincipal && dto.isPrincipal()){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
			}
			
			if (dto.isPrincipal()){
				isTelefonePrincipal = dto.isPrincipal();
				hasTelefonePrincipal = dto.isPrincipal();
			}
		}
		
		if (!hasTelefonePrincipal)
			throw new ValidacaoException(TipoMensagem.WARNING, "É necessario cadastrar pelo menos um telefone principal.");
		
	}
}