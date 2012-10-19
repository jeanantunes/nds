package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;
import br.com.abril.nds.repository.EnderecoTransportadorRepository;
import br.com.abril.nds.repository.MotoristaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ParametroCobrancaTransportadorRepository;
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
import br.com.abril.nds.vo.ValidacaoVO;

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
	
	@Autowired
	private ParametroCobrancaTransportadorRepository parametroCobrancaTransportadorRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Transportador buscarTransportadorPorId(Long idTransportador) {
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		Transportador transportador = this.transportadorRepository.buscarPorId(idTransportador);
		
		Hibernate.initialize(transportador.getParametroCobrancaTransportador().getDiasSemanaCobranca());
		
		return transportador;
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
			List<Veiculo> listaVeiculosAdicionar,
			Set<Long> listaVeiculosRemover,
			List<Motorista> listaMotoristasAdicionar,
			Set<Long> listaMotoristasRemover,
			List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar,
			Set<Long> listaAssociacaoRemover) {
		
		this.validarDadosEntrada(transportador, listaVeiculosAdicionar, listaMotoristasAdicionar, listaAssociacaoAdicionar);
		
		this.validarDadosEntradaEnderecos(transportador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.validarDadosEntradaTelefone(transportador, listaTelefoneAdicionar, listaTelefoneRemover);
		
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
		
		if (transportador.getParametroCobrancaTransportador() != null){
			
			if (transportador.getParametroCobrancaTransportador().getId() == null){
				
				this.parametroCobrancaTransportadorRepository.adicionar(
						transportador.getParametroCobrancaTransportador());
			} else {
				
				this.parametroCobrancaTransportadorRepository.alterar(
						transportador.getParametroCobrancaTransportador());
			}
		}
		
		this.processarEnderecos(transportador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.processarTelefones(transportador, listaTelefoneAdicionar, listaTelefoneRemover);
		
		if (listaAssociacaoRemover != null && !listaAssociacaoRemover.isEmpty()){
		
			this.associacaoVeiculoMotoristaRotaRepository.removerAssociacaoPorId(listaAssociacaoRemover);
		}
		
		this.processarVeiculos(transportador, listaVeiculosAdicionar, listaVeiculosRemover);
		
		this.processarMotoristas(transportador, listaMotoristasAdicionar, listaMotoristasRemover);
		
		this.processarAssocicoes(transportador, listaAssociacaoAdicionar);
	}
	
	private void validarDadosEntradaTelefone(Transportador transportador,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover) {
		
		if (listaTelefoneAdicionar != null && !listaTelefoneAdicionar.isEmpty()){
			
			boolean existePrincipal = false;
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (TelefoneAssociacaoDTO dto : listaTelefoneAdicionar){
				
				if (dto.getTelefone() != null && dto.getTelefone().getId() != null){
					
					idsIgnorar.add(dto.getTelefone().getId());
				}
				
				if (dto.isPrincipal()){
					
					existePrincipal = true;
					break;
				}
			}
			
			if (listaTelefoneRemover != null && !listaTelefoneRemover.isEmpty()){
				
				idsIgnorar.addAll(listaTelefoneRemover);
			}
			
			if (existePrincipal){
				
				if (transportador.getId() != null){
					
					if (this.telefoneTransportadorRepositoty.verificarTelefonePrincipalTransportador(transportador.getId(), idsIgnorar)){
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Apenas 1 telefone principal é permitido.");
					}
				}
			} else {
				
				if (transportador.getId() != null){
					
					if (!this.telefoneTransportadorRepositoty.verificarTelefonePrincipalTransportador(transportador.getId(), idsIgnorar)){
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
					}
				} else {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
				}
			}
		} else {
			
			if (!this.telefoneTransportadorRepositoty.verificarTelefonePrincipalTransportador(transportador.getId(), listaTelefoneRemover)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
			}
		}
	}

	private void validarDadosEntradaEnderecos(Transportador transportador,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			Set<Long> listaEnderecosRemover) {
		
		if (listaEnderecosAdicionar != null && !listaEnderecosAdicionar.isEmpty()) {
			
			boolean existePrincipal = false;
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (EnderecoAssociacaoDTO dto : listaEnderecosAdicionar){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					idsIgnorar.add(dto.getEndereco().getId());
				}
				
				if (dto.isEnderecoPrincipal()){
					
					existePrincipal = true;
					break;
				}
			}
			
			if (listaEnderecosRemover != null && !listaEnderecosRemover.isEmpty()){
				
				idsIgnorar.addAll(listaEnderecosRemover);
			}
			
			if (existePrincipal){
				
				if (transportador.getId() != null){
					
					if (this.enderecoTransportadorRepository.verificarEnderecoPrincipalTransportador(transportador.getId(), idsIgnorar)){
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Apenas 1 endereço principal é permitido.");
					}
				}
			} else {
				
				if (transportador.getId() != null){
					
					if (!this.enderecoTransportadorRepository.verificarEnderecoPrincipalTransportador(transportador.getId(), idsIgnorar)){
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
					}
				} else {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
				}
			}
		} else {
			
			Set<Long> idsEnderecosIgnorar = new HashSet<Long>();
			
			if (listaEnderecosRemover != null && !listaEnderecosRemover.isEmpty()){
				
				idsEnderecosIgnorar.addAll(listaEnderecosRemover);
			}
			
			if (!this.enderecoTransportadorRepository.verificarEnderecoPrincipalTransportador(transportador.getId(), idsEnderecosIgnorar)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
			}
		}
	}

	private void processarMotoristas(Transportador transportador,
			List<Motorista> listaMotoristasAdicionar,
			Set<Long> listaMotoristasRemover) {
		
		if (listaMotoristasRemover != null && !listaMotoristasRemover.isEmpty()){
			
			this.motoristaRepository.removerMotoristas(transportador.getId(), listaMotoristasRemover);
		}
		
		if (listaMotoristasAdicionar != null && !listaMotoristasAdicionar.isEmpty()){
			
			for (Motorista motorista : listaMotoristasAdicionar){
				
				motorista.setTransportador(transportador);
				
				if (motorista.getId() == null){
					
					this.motoristaRepository.adicionar(motorista);
				} else {
					
					this.motoristaRepository.alterar(motorista);
				}
			}
		}
	}

	private void processarVeiculos(Transportador transportador,
			List<Veiculo> listaVeiculosAdicionar, Set<Long> listaVeiculosRemover) {
		
		if (listaVeiculosRemover != null && !listaVeiculosRemover.isEmpty()){
			
			this.veiculoRepository.removerVeiculos(transportador.getId(), listaVeiculosRemover);
		}
		
		if (listaVeiculosAdicionar != null && !listaVeiculosAdicionar.isEmpty()){
			
			for (Veiculo veiculo : listaVeiculosAdicionar){
				
				veiculo.setTransportador(transportador);
				
				if (veiculo.getId() == null){
					
					this.veiculoRepository.adicionar(veiculo);
				} else {
					
					this.veiculoRepository.alterar(veiculo);
				}
			}
		}
	}

	private void processarAssocicoes(Transportador transportador, List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar) {
		
		if (listaAssociacaoAdicionar != null && !listaAssociacaoAdicionar.isEmpty()){
			
			for (AssociacaoVeiculoMotoristaRotaDTO dto : listaAssociacaoAdicionar){
				
				AssociacaoVeiculoMotoristaRota assoc = new AssociacaoVeiculoMotoristaRota();
				assoc.setId(dto.getId() != null ? dto.getId() < 0 ? null : dto.getId() : null);
				assoc.setMotorista(dto.getMotorista());
				assoc.setVeiculo(dto.getVeiculo());
				assoc.setRota(this.rotaRepository.buscarPorId(dto.getRota().getIdRota()));
				assoc.setTransportador(transportador);
				
				this.associacaoVeiculoMotoristaRotaRepository.adicionar(assoc);
			}
		}
	}

	private void processarTelefones(Transportador transportador, List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover) {
		
		if (listaTelefoneAdicionar != null && !listaTelefoneAdicionar.isEmpty()){
			
			PessoaJuridica pessoaJuridica = transportador.getPessoaJuridica();
            this.telefoneService.cadastrarTelefone(listaTelefoneAdicionar, pessoaJuridica);
			
			for (TelefoneAssociacaoDTO dto : listaTelefoneAdicionar){
				
				TelefoneDTO telefoneDTO = dto.getTelefone();
				
			
                TelefoneTransportador telefoneTransportador = 
						this.telefoneTransportadorRepositoty.buscarTelefonePorTelefoneTransportador(
								telefoneDTO.getId(), 
								transportador.getId());
				
				if (telefoneTransportador == null){
					
					telefoneTransportador = new TelefoneTransportador();
					telefoneTransportador.setPrincipal(dto.isPrincipal());
					Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), pessoaJuridica);
					telefoneTransportador.setTelefone(telefone);
					telefoneTransportador.setTipoTelefone(dto.getTipoTelefone());
					telefoneTransportador.setTransportador(transportador);
					
					this.telefoneTransportadorRepositoty.adicionar(telefoneTransportador);
				} else {
					Telefone telefone = telefoneTransportador.getTelefone();
					telefone.setDdd(telefoneDTO.getDdd());
					telefone.setNumero(telefoneDTO.getNumero());
					telefone.setRamal(telefoneDTO.getRamal());
					telefoneTransportador.setPrincipal(dto.isPrincipal());
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
			
			PessoaJuridica pessoaJuridica = transportador.getPessoaJuridica();
			
            this.enderecoService.cadastrarEnderecos(listaEnderecosAdicionar, pessoaJuridica);
			
			for (EnderecoAssociacaoDTO dto : listaEnderecosAdicionar){
				
				EnderecoTransportador enderecoTransportador = 
						this.enderecoTransportadorRepository.buscarEnderecoPorEnderecoTransportador(
								dto.getId(), 
								transportador.getId());
				
				EnderecoDTO enderecoDTO = dto.getEndereco();
                Endereco endereco = new Endereco(enderecoDTO.getCodigoBairro(),
                        enderecoDTO.getBairro(), enderecoDTO.getCep(),
                        enderecoDTO.getCodigoCidadeIBGE(),
                        enderecoDTO.getCidade(), enderecoDTO.getComplemento(),
                        enderecoDTO.getTipoLogradouro(),
                        enderecoDTO.getLogradouro(), enderecoDTO.getNumero(),
                        enderecoDTO.getUf(), enderecoDTO.getCodigoUf(),
                        pessoaJuridica);
                endereco.setId(enderecoDTO.getId());
				
				
                if (enderecoTransportador == null){
					
					enderecoTransportador = new EnderecoTransportador();
					enderecoTransportador.setEndereco(endereco);
					enderecoTransportador.setPrincipal(dto.isEnderecoPrincipal());
					enderecoTransportador.setTipoEndereco(dto.getTipoEndereco());
					enderecoTransportador.setTransportador(transportador);
					
					this.enderecoTransportadorRepository.adicionar(enderecoTransportador);
				} else {
					
					enderecoTransportador.setEndereco(endereco);
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

	private void validarDadosEntrada(Transportador transportador, List<Veiculo> listaVeiculosAdicionar, 
			List<Motorista> listaMotoristasAdicionar, 
			List<AssociacaoVeiculoMotoristaRotaDTO> listaAssociacaoAdicionar) {
		
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
		
		if (pessoaJuridica.getCnpj() == null || pessoaJuridica.getCnpj().trim().isEmpty()){
			
			msgs.add("CNPJ é obrigatório");
		}
		
		if (pessoaJuridica.getInscricaoEstadual() == null || pessoaJuridica.getInscricaoEstadual().trim().isEmpty()){
			
			msgs.add("Insc. Estadual é obrigatório.");
		}
		
		if (listaMotoristasAdicionar != null && !listaMotoristasAdicionar.isEmpty()){
			
			for (Motorista motorista : listaMotoristasAdicionar){
				
				if (motorista == null){
					
					msgs.add("Motorista é obrigatório.");
				} else {
				
					if (motorista.getNome() == null || motorista.getNome().trim().isEmpty()){
						
						msgs.add("Nome motorista é obrigatório.");
					}
					
					if (motorista.getCnh() == null || motorista.getCnh().trim().isEmpty()){
						
						msgs.add("CNH é obrigatório.");
					}
				}
			}
		}
		
		if (listaVeiculosAdicionar != null && !listaVeiculosAdicionar.isEmpty()){
			
			for (Veiculo veiculo : listaVeiculosAdicionar){
				
				if (veiculo == null){
					
					msgs.add("Veículo é obrigatório.");
				} else {
				
					if (veiculo.getTipoVeiculo() == null || veiculo.getTipoVeiculo().trim().isEmpty()){
						
						msgs.add("Tipo de Veículo é obrigatório.");
					}
					
					if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
						
						msgs.add("Placa é obrigatório.");
					}
				}
			}
		}
		
		if (listaAssociacaoAdicionar != null && !listaAssociacaoAdicionar.isEmpty()){
			
			for (AssociacaoVeiculoMotoristaRotaDTO dto : listaAssociacaoAdicionar){
				
				if (dto == null){
					
					msgs.add("Associação é obrigatório.");
				} else {
					
					if (dto.getMotorista() == null){
						
						msgs.add("Motorista da associação é obrigatório.");
					}
					
					if (dto.getVeiculo() == null){
						
						msgs.add("Veiculo da associação é obrigatório.");
					}
					
					if (dto.getRota() == null || dto.getRota().getIdRota() == null){
						
						msgs.add("Rota/Roteiro da associação é obrigatório.");
					}
				}
			}
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
			
			this.enderecoTransportadorRepository.excluirEnderecosPorIdTransportador(transportador.getId());
			
			this.telefoneTransportadorRepositoty.excluirTelefonesTransportador(transportador.getId());
			
			this.associacaoVeiculoMotoristaRotaRepository.removerAssociacaoTransportador(transportador.getId());
			
			this.veiculoRepository.removerVeiculos(transportador.getId(), null);
			
			this.motoristaRepository.removerMotoristas(transportador.getId(), null);
			
			Set<Long> idsTelefone = new HashSet<Long>();
			for (Telefone telefone : transportador.getPessoaJuridica().getTelefones()){
				
				idsTelefone.add(telefone.getId());
			}
			
			this.telefoneService.removerTelefones(idsTelefone);
			
			Set<Long> idsEndereco = new HashSet<Long>();
			for (Endereco endereco : transportador.getPessoaJuridica().getEnderecos()){
				
				idsEndereco.add(endereco.getId());
			}
			
			this.enderecoService.removerEnderecos(idsEndereco);
			
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
	public List<Veiculo> buscarVeiculosPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder) {
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.veiculoRepository.buscarVeiculosPorTransportador(idTransportador, idsIgnorar, sortname, sortorder);
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
	public List<Motorista> buscarMotoristasPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder) {
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.motoristaRepository.buscarMotoristasPorTransportador(idTransportador, idsIgnorar, sortname, sortorder);
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
	public List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(String sortname, String sortorder){
		
		return this.rotaRepository.buscarRotasRoteiroAssociacao(sortname, sortorder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TelefoneAssociacaoDTO> buscarTelefonesTransportador(Long id, Set<Long> idsIgnorar) {
		
		if (id == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.telefoneTransportadorRepositoty.buscarTelefonesTransportador(id, idsIgnorar);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> buscarEnderecosTransportador(Long id, Set<Long> idsIgnorar) {
		
		if (id == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.enderecoTransportadorRepository.buscarEnderecosTransportador(id, idsIgnorar);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AssociacaoVeiculoMotoristaRota> buscarAssociacoesTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder){
		
		if (idTransportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id transportador é obrigatório.");
		}
		
		return this.associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(
				idTransportador, idsIgnorar, sortname, sortorder);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Long> buscarIdsRotasPorAssociacao(Set<Long> assocRemovidas) {
		
		if (assocRemovidas == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Ids de associação são obrigatórios.");
		}
		
		return this.associacaoVeiculoMotoristaRotaRepository.buscarIdsRotasPorAssociacao(assocRemovidas);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean verificarAssociacaoMotorista(Long idMotorista, Set<Long> idsIgnorar) {
		
		if (idMotorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id motorista é obrigatório.");
		}
		
		return this.associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoMotorista(idMotorista, idsIgnorar);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean verificarAssociacaoVeiculo(Long idVeiculo, Set<Long> idsIgnorar) {
		
		if (idVeiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id veículo é obrigatório.");
		}
		
		return this.associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoVeiculo(idVeiculo, idsIgnorar);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean verificarAssociacaoRotaRoteiro(Long idRota) {
		
		if (idRota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id rota é obrigatório.");
		}
		
		return this.associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoRotaRoteiro(idRota);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaAtendidaTransportadorVO> buscarCotasAtendidadas(
			Long idTransportador, String sortorder, String sortname) {
		
		return this.transportadorRepository.buscarCotasAtendidadas(idTransportador, sortorder, sortname);
	}

	@Override
	@Transactional
	public List<CotaTransportadorDTO> obterResumoTransportadorCota(Date dataDe,
			Date dataAte, Long idTransportador, PaginacaoVO paginacaoVO) {
		
		return movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(dataDe, dataAte, idTransportador, paginacaoVO);
	}

	@Override
	@Transactional
	public List<MovimentoFinanceiroDTO> obterDetalhesTrasportadorPorCota(
			Date dataDe, Date dataAte, Long idTransportador, Long idCota) {
		
		return movimentoFinanceiroCotaRepository.obterDetalhesTrasportadorPorCota(dataDe, dataAte, idTransportador, idCota);
	}
}