package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;
import br.com.abril.nds.repository.EnderecoTransportadorRepository;
import br.com.abril.nds.repository.MotoristaRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.TelefoneTransportadorRepositoty;
import br.com.abril.nds.repository.TransportadorRepository;
import br.com.abril.nds.repository.VeiculoRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class TransportadorServiceImpl implements TransportadorService {
	
	@Autowired
	private TransportadorRepository transportadorRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private TelefoneTransportadorRepositoty telefoneTransportadorRepositoty;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private EnderecoTransportadorRepository enderecoTransportadorRepository;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private MotoristaRepository motoristaRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private AssociacaoVeiculoMotoristaRotaRepository associacaoVeiculoMotoristaRotaRepository;
	
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
	public void cadastrarTransportador(Transportador transportador,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			Set<Long> listaEnderecosRemover,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover,
			List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover) {
		
		this.validarDadosEntrada(transportador);
		
		transportador.getPessoaJuridica().setCnpj(transportador.getPessoaJuridica().getCnpj().replace(".", "").replace("-", "").replace("/", ""));
		
		transportador.getPessoaJuridica().setId(
				this.pessoaRepository.buscarIdPessoaPorCNPJ(transportador.getPessoaJuridica().getCnpj()));
		
		if (transportador.getPessoaJuridica().getId() == null){
			
			this.pessoaRepository.adicionar(transportador.getPessoaJuridica());
		} else {
			
			this.pessoaRepository.alterar(transportador.getPessoaJuridica());
		}
		
		if (transportador.getId() == null){
			
			this.transportadorRepository.adicionar(transportador);
		} else {
			
			this.transportadorRepository.alterar(transportador);
		}
		
		this.processarEnderecos(transportador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.processarTelefones(transportador, listaTelefoneAdicionar, listaTelefoneRemover);
		
		this.processarAssocicoes(transportador, listaAssociacaoAdicionar, listaAssociacaoRemover);
	}
	
	private void processarAssocicoes(Transportador transportador, List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover) {
		
		if (listaAssociacaoRemover != null && !listaAssociacaoRemover.isEmpty()){
			
			this.associacaoVeiculoMotoristaRotaRepository.removerAssociacaoPorId(listaAssociacaoRemover);
		}
		
		if (listaAssociacaoAdicionar != null && !listaAssociacaoAdicionar.isEmpty()){
			
			Map<AssociacaoVeiculoMotoristaRota, List<Rota>> mapAssoc = 
					new HashMap<AssociacaoVeiculoMotoristaRota, List<Rota>>();
			
			for (AssociacaoVeiculoMotoristaRotaDTO dto : listaAssociacaoAdicionar){
				
				AssociacaoVeiculoMotoristaRota assoc = new AssociacaoVeiculoMotoristaRota();
				assoc.setId(dto.getId());
				assoc.setMotorista(dto.getMotorista());
				assoc.setVeiculo(dto.getVeiculo());
				
				if (mapAssoc.containsKey(assoc)){
					
					List<Rota> lista = mapAssoc.get(dto.getId());
					
					Rota rota = new Rota();
					rota.setId(dto.getRota().getIdRota());
					
					lista.add(rota);
					
				} else {
					
					List<Rota> lista = mapAssoc.get(dto.getId());
					
					Rota rota = new Rota();
					rota.setId(dto.getRota().getIdRota());
					
					mapAssoc.put(assoc, lista);
				}
			}
			
			for (AssociacaoVeiculoMotoristaRota assoc : mapAssoc.keySet()){
				
				assoc.setRotas(mapAssoc.get(assoc.getId()));
				assoc.setId(null);
				
				this.associacaoVeiculoMotoristaRotaRepository.adicionar(assoc);
			}
		}
	}

	private void processarTelefones(Transportador transportador, List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover) {
		
		if (listaTelefoneAdicionar != null && !listaTelefoneAdicionar.isEmpty()){
			
			this.telefoneService.cadastrarTelefone(listaTelefoneAdicionar, transportador.getPessoaJuridica());
			
			for (TelefoneAssociacaoDTO dto : listaTelefoneAdicionar){
				
				TelefoneTransportador telefoneTransportador = 
						this.telefoneTransportadorRepositoty.buscarTelefonePorTelefoneTransportador(
								dto.getTelefone().getId(), 
								transportador.getId());
				
				if (telefoneTransportador == null){
					
					telefoneTransportador = new TelefoneTransportador();
					telefoneTransportador.setPrincipal(dto.isPrincipal());
					telefoneTransportador.setTelefone(dto.getTelefone());
					telefoneTransportador.setTipoTelefone(dto.getTipoTelefone());
					telefoneTransportador.setTransportador(transportador);
					
					this.telefoneTransportadorRepositoty.adicionar(telefoneTransportador);
				} else {
					
					telefoneTransportador.setPrincipal(dto.isPrincipal());
					telefoneTransportador.setTelefone(dto.getTelefone());
					telefoneTransportador.setTipoTelefone(dto.getTipoTelefone());
					
					this.telefoneTransportadorRepositoty.alterar(telefoneTransportador);
				}
			}
		}
		
		if (listaTelefoneRemover != null && !listaTelefoneRemover.isEmpty()){
			
			this.telefoneTransportadorRepositoty.removerTelefones(listaTelefoneRemover);
			
			this.telefoneService.removerTelefones(listaTelefoneRemover);
		}
	}

	private void processarEnderecos(Transportador transportador, List<EnderecoAssociacaoDTO> listaEnderecosAdicionar, 
			Set<Long> listaEnderecosRemover) {
		
		if (listaEnderecosAdicionar != null && !listaEnderecosAdicionar.isEmpty()){
			
			this.enderecoService.cadastrarEnderecos(listaEnderecosAdicionar, transportador.getPessoaJuridica());
			
			for (EnderecoAssociacaoDTO dto : listaEnderecosAdicionar){
				
				EnderecoTransportador enderecoTransportador = 
						this.enderecoTransportadorRepository.buscarEnderecoPorEnderecoTransportador(
								dto.getId(), 
								transportador.getId());
				
				if (enderecoTransportador == null){
					
					enderecoTransportador = new EnderecoTransportador();
					enderecoTransportador.setEndereco(dto.getEndereco());
					enderecoTransportador.setPrincipal(dto.isEnderecoPrincipal());
					enderecoTransportador.setTipoEndereco(dto.getTipoEndereco());
					enderecoTransportador.setTransportador(transportador);
					
					this.enderecoTransportadorRepository.adicionar(enderecoTransportador);
				} else {
					
					enderecoTransportador.setEndereco(dto.getEndereco());
					enderecoTransportador.setPrincipal(dto.isEnderecoPrincipal());
					enderecoTransportador.setTipoEndereco(dto.getTipoEndereco());
					
					this.enderecoTransportadorRepository.alterar(enderecoTransportador);
				}
			}
		}
		
		if (listaEnderecosRemover != null && !listaEnderecosRemover.isEmpty()){
			
			this.enderecoTransportadorRepository.removerEnderecosTransportador(listaEnderecosRemover);
			
			this.enderecoService.removerEnderecos(listaEnderecosRemover);
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

	@Override
	@Transactional(readOnly = true)
	public Transportador obterTransportadorPorCNPJ(String cnpj) {
		
		if (cnpj == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "CNPJ é obrigatório.");
		}
		
		return this.transportadorRepository.buscarTransportadorPorCNPJ(cnpj);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Veiculo> buscarVeiculos() {
		
		return this.veiculoRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Veiculo buscarVeiculoPorId(Long idVeiculo){
		
		if (idVeiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Veículo é obrigatório.");
		}
		
		return this.veiculoRepository.buscarPorId(idVeiculo);
	}

	@Override
	@Transactional
	public void cadastrarVeiculo(Veiculo veiculo) {
		
		this.validarDadosEntradaVeiculo(veiculo);
		
		if (veiculo.getId() == null){
			
			this.veiculoRepository.adicionar(veiculo);
		} else {
			
			this.veiculoRepository.alterar(veiculo);
		}
	}
	
	@Override
	@Transactional
	public void cadastrarVeiculos(List<Veiculo> veiculos){
		
		for (Veiculo veiculo : veiculos){
			
			this.cadastrarVeiculo(veiculo);
		}
	}

	private void validarDadosEntradaVeiculo(Veiculo veiculo) {
		
		if (veiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Veículo é obrigatório");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (veiculo.getTipoVeiculo() == null || veiculo.getTipoVeiculo().trim().isEmpty()){
			
			msgs.add("Tipo veículo é obrigatório.");
		} else {
			
			veiculo.setTipoVeiculo(veiculo.getTipoVeiculo().trim());
		}
		
		if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
			
			msgs.add("Placa é obrigatório.");
		} else {
			veiculo.setPlaca(veiculo.getPlaca().trim());
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	@Override
	@Transactional
	public void excluirVeiculo(Long idVeiculo) {
		
		if (idVeiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Veículo é obrigatório.");
		}
		
		this.veiculoRepository.removerPorId(idVeiculo);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Motorista> buscarMotoristas() {
		
		return this.motoristaRepository.buscarTodos();
	}
	
	@Override
	@Transactional
	public void cadastrarMotoristas(List<Motorista> motoristas){
		
		for (Motorista motorista : motoristas){
			
			this.cadastarMotorista(motorista);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Motorista buscarMotoristaPorId(Long idMotorista){
		
		if (idMotorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Motorista é obrigatório.");
		}
		
		return this.motoristaRepository.buscarPorId(idMotorista);
	}

	@Override
	@Transactional
	public void cadastarMotorista(Motorista motorista) {
		
		this.validarDadosEntradaMotorista(motorista);
		
		if (motorista.getId() == null){
			
			this.motoristaRepository.adicionar(motorista);
		} else {
			
			this.motoristaRepository.alterar(motorista);
		}
	}

	private void validarDadosEntradaMotorista(Motorista motorista) {
		
		if (motorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Motorista é obrigatório");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (motorista.getNome() == null || motorista.getNome().isEmpty()){
			
			msgs.add("Nome é obrigatório");
		} else {
			
			motorista.setNome(motorista.getNome().trim());
		}
		
		if (motorista.getCnh() == null || motorista.getCnh().isEmpty()){
			
			msgs.add("CNH é obrigatório");
		} else {
			
			motorista.setCnh(motorista.getCnh().trim());
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	@Override
	@Transactional
	public void excluirMotorista(Long idMotorista) {
		
		if (idMotorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Motorista é obrigatório");
		}
		
		this.motoristaRepository.removerPorId(idMotorista);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Rota> buscarRotas(){
		
		return this.rotaRepository.buscarTodos();
	}
}