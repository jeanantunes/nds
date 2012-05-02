package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TelefoneTransportadorRepositoty;
import br.com.abril.nds.repository.TransportadorRepository;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class TransportadorServiceImpl implements TransportadorService {
	
	@Autowired
	private TransportadorRepository transportadorRepository;
	
	@Autowired
	private TelefoneTransportadorRepositoty telefoneTransportadorRepositoty;
	
	@Override
	@Transactional(readOnly = true)
	public Transportador buscarTransportadorPorId(Long idTransportador) {
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.transportadorRepository.buscarPorId(idTransportador);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Transportador> buscarTransportadores() {
		
		return this.transportadorRepository.buscarTodos();
	}

	@Override
	@Transactional
	public void cadastrarTransportador(Transportador transportador) {
		
		this.validarDadosEntrada(transportador);
		
		if (transportador.getId() == null){
			
			this.transportadorRepository.adicionar(transportador);
		} else {
			
			this.transportadorRepository.alterar(transportador);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaTransportadorDTO consultarTransportadores(FiltroConsultaTransportadorDTO filtro){
		
		if (filtro == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro de pesquisa inválido.");
		}
		
		if (filtro.getOrdenacaoColunaTransportador() == null){
			
			filtro.setOrdenacaoColunaTransportador(OrdenacaoColunaTransportador.CODIGO);
		}
		
		if (filtro.getPaginacaoVO() == null){
			
			filtro.setPaginacaoVO(new PaginacaoVO(1, 15, Ordenacao.ASC.getOrdenacao()));
		} else {
			
			if (filtro.getPaginacaoVO().getPaginaAtual() == null){
				
				filtro.getPaginacaoVO().setPaginaAtual(1);
			}
			
			if (filtro.getPaginacaoVO().getQtdResultadosPorPagina() == null){
				
				filtro.getPaginacaoVO().setQtdResultadosPorPagina(15);
			}
			
			if (filtro.getPaginacaoVO().getOrdenacao() == null){
				
				filtro.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
			}
		}
		
		ConsultaTransportadorDTO consultaTransportadorDTO = this.transportadorRepository.pesquisarTransportadoras(filtro);
		
		for (Transportador transportador : consultaTransportadorDTO.getTransportadores()){
			
			List<Telefone> telefones = new ArrayList<Telefone>();
			
			Telefone telefone = this.telefoneTransportadorRepositoty.pesquisarTelefonePrincipalTransportador(transportador.getId());
			
			if (telefone != null){
				telefones.add(telefone);
			}
			
			transportador.getPessoaJuridica().setTelefones(telefones);
		}
		
		return consultaTransportadorDTO;
	}

	private void validarDadosEntrada(Transportador transportador) {
		
		if (transportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Transportador é obrigatório.");
		}
		
		if (transportador.getPessoaJuridica() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa jurídica é obrigatório.");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		PessoaJuridica pessoaJuridica = transportador.getPessoaJuridica();
		
		if (pessoaJuridica.getRazaoSocial() == null || pessoaJuridica.getRazaoSocial().trim().isEmpty()){
			
			msgs.add("Razão Social é obrigatório.");
		}
		
		if (pessoaJuridica.getNomeFantasia() == null || pessoaJuridica.getNomeFantasia().trim().isEmpty()){
			
			msgs.add("Nome Fantasia é obrigatório.");
		}
		
		if (pessoaJuridica.getEmail() == null || pessoaJuridica.getEmail().trim().isEmpty()){
			
			msgs.add("Email é obrigatório");
		}
		
		if (transportador.getResponsavel() == null || transportador.getResponsavel().trim().isEmpty()){
			
			msgs.add("Responsável é obrigatório.");
		}
		
		if (pessoaJuridica.getCnpj() == null || pessoaJuridica.getCnpj().trim().isEmpty()){
			
			msgs.add("CNPJ é obrigatório");
		}
		
		if (pessoaJuridica.getInscricaoEstadual() == null || pessoaJuridica.getInscricaoEstadual().trim().isEmpty()){
			
			msgs.add("Insc. Estadual é obrigatório.");
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	@Override
	@Transactional
	public void excluirTransportador(Long idTransportador) {
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		Transportador transportador = this.transportadorRepository.buscarPorId(idTransportador);
		
		if (transportador != null){
			
			this.transportadorRepository.remover(transportador);
		}
	}

}