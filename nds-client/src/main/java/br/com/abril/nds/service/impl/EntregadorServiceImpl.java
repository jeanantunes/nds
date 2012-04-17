package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.repository.EnderecoEntregadorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.repository.TelefoneEntregadorRepository;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Implementação do serviço responsável pela lógica de negócios que envolve a entidade 
 * {@link br.com.abril.nds.model.cadastro.Entregador} 
 * 
 * @author Discover Technology
 * 
 */
@Service 
public class EntregadorServiceImpl implements EntregadorService {

	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private EntregadorRepository entregadorRepository;

	@Autowired
	private EnderecoEntregadorRepository enderecoEntregadorRepository;

	@Autowired
	private TelefoneEntregadorRepository telefoneEntregadorRepository;
	
	/**
	 * @see br.com.abril.nds.service.EntregadorService#obterTodosEntregadores()
	 */
	@Override
	@Transactional
	public List<Entregador> obterEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador) {

		return this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#obterContagemEntregadoresPorFiltro(br.com.abril.nds.dto.filtro.FiltroEntregadorDTO)
	 */
	@Override
	@Transactional
	public Long obterContagemEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador) {

		return this.entregadorRepository.obterContagemEntregadoresPorFiltro(filtroEntregador);
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#salvarEntregador(br.com.abril.nds.model.cadastro.Entregador)
	 */
	@Override
	@Transactional
	public void salvarEntregador(Entregador entregador) {

		if (entregador.getId() == null) {

			this.entregadorRepository.adicionar(entregador);

		} else {

			this.entregadorRepository.alterar(entregador);
		}
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#removerEntregadorPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removerEntregadorPorId(Long idEntregador) {

		Entregador entregador = this.entregadorRepository.buscarPorId(idEntregador);

		if (entregador == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Entregador não encontrado.");
		}
		
		if (entregador.getEnderecos() != null && !entregador.getEnderecos().isEmpty()) {
			
			this.enderecoEntregadorRepository.removerEnderecosEntregadorPorIdEntregador(idEntregador);
		}
		
		if (entregador.getTelefones() != null && !entregador.getTelefones().isEmpty()) {
			
			this.telefoneEntregadorRepository.removerTelefoneEntregadorPorIdEntregador(idEntregador);
		}

		this.entregadorRepository.remover(entregador);
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#obterEntregadorPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public Entregador obterEntregadorPorId(Long idEntregador) {

		return this.entregadorRepository.buscarPorId(idEntregador);
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#obterEnderecosPorIdEntregador(java.lang.Long)
	 */
	@Override
	@Transactional
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdEntregador(Long idEntregador) {

		return this.entregadorRepository.obterEnderecosPorIdEntregador(idEntregador);
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#processarEnderecos(java.lang.Long, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public void processarEnderecos(Long idEntregador,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {

		if (idEntregador == null){

			throw new ValidacaoException(TipoMensagem.ERROR, "Id do entregador é obrigatório.");
		}
		
		Entregador entregador = this.entregadorRepository.buscarPorId(idEntregador);
		
		if (entregador == null){

			throw new ValidacaoException(TipoMensagem.ERROR, "Entregador não encontrado.");
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosEntregador(entregador, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosEntregador(entregador, listaEnderecoAssociacaoRemover);
		}
	}

	/**
	 * @see br.com.abril.nds.service.EntregadorService#processarTelefones(java.lang.Long, java.util.List, java.util.Collection)
	 */
	@Override
	@Transactional
	public void processarTelefones(Long idEntregador, 
								   List<TelefoneEntregador> listaTelefonesAdicionar, 
								   Collection<Long> listaTelefonesRemover) {
		
		if (idEntregador == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Entregador entregador = this.entregadorRepository.buscarPorId(idEntregador);
		
		if (entregador == null){
		
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		for (TelefoneEntregador telefoneEntregador : listaTelefonesAdicionar){
			
			telefoneEntregador.setEntregador(entregador);
		}
		
		this.telefoneService.salvarTelefonesEntregador(listaTelefonesAdicionar);

		this.telefoneService.removerTelefonesEntregador(listaTelefonesRemover);
		
		List<Telefone> listaTelefone = new ArrayList<Telefone>();
		
		for (TelefoneEntregador telefoneEntregador : listaTelefonesAdicionar){
			
			listaTelefone.add(telefoneEntregador.getTelefone());
		}
		
		entregador.getPessoa().setTelefones(listaTelefone);
		
		this.entregadorRepository.alterar(entregador);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TelefoneAssociacaoDTO> buscarTelefonesEntregador(Long idEntregador) {
		
		if (idEntregador == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Id do Entregador é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneEntregadorRepository.buscarTelefonesEntregador(idEntregador);
		
		return listaTelAssoc;
	}
	
	/*
	 * Método responsável por salvar os endereços referentes ao entregador em questão.
	 */
	private void salvarEnderecosEntregador(Entregador entregador, 
										   List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoEntregador enderecoEntregador = this.enderecoEntregadorRepository.buscarPorId(enderecoAssociacao.getId());

			if (enderecoEntregador == null) {

				enderecoEntregador = new EnderecoEntregador();

				enderecoEntregador.setEntregador(entregador);
			}

			enderecoEntregador.setEndereco(enderecoAssociacao.getEndereco());

			enderecoEntregador.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoEntregador.setTipoEndereco(enderecoAssociacao.getTipoEndereco());

			this.enderecoEntregadorRepository.merge(enderecoEntregador);
		}
	}

	/*
	 * Método responsável por remover os endereços referentes ao entregador em questão.
	 */
	private void removerEnderecosEntregador(Entregador entregador, 
			   								List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			listaEndereco.add(enderecoAssociacao.getEndereco());

			EnderecoEntregador enderecoEntregador = this.enderecoEntregadorRepository.buscarPorId(enderecoAssociacao.getId());
			
			idsEndereco.add(enderecoAssociacao.getEndereco().getId());

			this.enderecoEntregadorRepository.remover(enderecoEntregador);
		}

		this.enderecoRepository.removerEnderecos(idsEndereco);
	}

}
