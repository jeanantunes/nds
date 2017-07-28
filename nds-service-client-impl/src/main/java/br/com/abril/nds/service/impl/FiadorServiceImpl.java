package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO.OrdenacaoColunaFiador;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EnderecoFiadorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.FiadorRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.TelefoneFiadorRepository;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.service.GarantiaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

@Service
public class FiadorServiceImpl implements FiadorService {

	@Autowired
	private FiadorRepository fiadorRepository;
	
	@Autowired
	private EnderecoFiadorRepository enderecoFiadorRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private TelefoneFiadorRepository telefoneFiadorRepository;
	
	@Autowired
	private GarantiaService garantiaService;
	
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaFiadorDTO obterFiadores(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO) {
		
		if (filtroConsultaFiadorDTO == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro para pesquisa inválido.");
		}
		
		if (filtroConsultaFiadorDTO.getOrdenacaoColunaFiador() == null){
			
			filtroConsultaFiadorDTO.setOrdenacaoColunaFiador(OrdenacaoColunaFiador.CODIGO);
		}
		
		if (filtroConsultaFiadorDTO.getPaginacaoVO() == null){
			
			filtroConsultaFiadorDTO.setPaginacaoVO(new PaginacaoVO(1, 15, PaginacaoVO.Ordenacao.ASC.getOrdenacao()));
		} else {
			
			if (filtroConsultaFiadorDTO.getPaginacaoVO().getPaginaAtual() == null){
				
				filtroConsultaFiadorDTO.getPaginacaoVO().setPaginaAtual(1);
			}
			
			if (filtroConsultaFiadorDTO.getPaginacaoVO().getQtdResultadosPorPagina() == null){
				
				filtroConsultaFiadorDTO.getPaginacaoVO().setQtdResultadosPorPagina(15);
			}
			
			if (filtroConsultaFiadorDTO.getPaginacaoVO().getOrdenacao() == null){
				
				filtroConsultaFiadorDTO.getPaginacaoVO().setOrdenacao(Ordenacao.ASC);
			}
		}
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorRepository.obterFiadoresCpfCnpj(filtroConsultaFiadorDTO);
		
		for (Fiador fiador : consultaFiadorDTO.getListaFiadores()){
		
			List<Telefone> telefones = new ArrayList<Telefone>();
			
			Telefone telefone = this.telefoneFiadorRepository.pesquisarTelefonePrincipalFiador(fiador.getId());
			
			if (telefone != null){
				telefones.add(telefone);
			}
			
			fiador.getPessoa().setTelefones(telefones);
		}
		
		return consultaFiadorDTO;
	}

	@Override
	@Transactional
	public void cadastrarFiador(Fiador fiador,
			List<Pessoa> sociosAdicionar,
			Set<Long> sociosRemover,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			List<EnderecoAssociacaoDTO> listaEnderecosRemover,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover,
			List<Garantia> listaGarantiaAdicionar,
			Set<Long> listaGarantiaRemover, 
			List<Integer> listaCotasAssociar,
			Set<Long> listaCotasDesassociar) {
		
		this.validarDadosFiador(fiador, sociosAdicionar);
		
		this.validarDadosEntradaEnderecos(fiador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.validarDadosEntradaTelefone(fiador, listaTelefoneAdicionar, listaTelefoneRemover);
		
		List<Cota> listaCotas = new ArrayList<Cota>();
		if (listaCotasAssociar != null){
			for (Integer numeroCota : listaCotasAssociar){
				listaCotas.add(this.cotaRepository.obterPorNumeroDaCota(numeroCota));
			}
		}
		
		try{
			
			processarFiador(fiador, sociosAdicionar, sociosRemover);
			
		}catch(org.springframework.dao.DataIntegrityViolationException e ){
			
			if (fiador.getPessoa() instanceof PessoaFisica){
				throw new ValidacaoException(TipoMensagem.WARNING,"CPF já está sendo utilizado por outro fiador.");
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING,"CNPJ já está sendo utilizado por outro fiador.");
		}
	
		this.processarEnderecos(fiador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.processarTelefones(fiador, listaTelefoneAdicionar, listaTelefoneRemover);
		
		this.processarGarantias(fiador, listaGarantiaAdicionar, listaGarantiaRemover);
		
		this.processarCotasAssociadas(fiador, listaCotas, listaCotasDesassociar);
	}

	private void processarFiador(Fiador fiador, List<Pessoa> sociosAdicionar,
			Set<Long> sociosRemover) {
		if (fiador.getPessoa() instanceof PessoaFisica){
			PessoaFisica conjuge = ((PessoaFisica)fiador.getPessoa()).getConjuge();
			
			if (conjuge != null){
				
				Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(conjuge.getCpf());
				conjuge.setId(idPessoa);
				
				if (conjuge.getId() == null){
					
					this.pessoaRepository.adicionar(conjuge);
				} else {
					
					this.pessoaRepository.merge(conjuge);
				}
			}
			
			Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(((PessoaFisica) fiador.getPessoa()).getCpf());
			fiador.getPessoa().setId(idPessoa);
		} else {
			
			PessoaJuridica pessoaJuridica = (PessoaJuridica)fiador.getPessoa();
			
			pessoaJuridica.setCnpj(pessoaJuridica.getCnpj().replace(".", "").replace("-", "").replace("/", ""));
			
			Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCNPJ(((PessoaJuridica) fiador.getPessoa()).getCnpj());
			fiador.getPessoa().setId(idPessoa);
		}
		
		if (fiador.getPessoa() instanceof PessoaFisica){
			if (((PessoaFisica) fiador.getPessoa()).getConjuge() != null){
				((PessoaFisica) fiador.getPessoa()).getConjuge().setConjuge((PessoaFisica) fiador.getPessoa());
			}
		}
		
		if (fiador.getPessoa() instanceof PessoaJuridica){
			this.processarSocios(fiador, sociosAdicionar, sociosRemover);
		}
		
		if (fiador.getPessoa().getId() == null){
			
			this.pessoaRepository.adicionar(fiador.getPessoa());
		} else {
			
			this.pessoaRepository.merge(fiador.getPessoa());
		}
		
		if (fiador.getId() == null){
			
			fiador.setInicioAtividade(new Date());
			this.fiadorRepository.adicionar(fiador);
		} else {
			
			fiador.setInicioAtividade(this.fiadorRepository.buscarDataInicioAtividadeFiadorPorId(fiador.getId()));
			this.fiadorRepository.merge(fiador);
		}
	}

	private void validarDadosEntradaTelefone(Fiador fiador,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar, Set<Long> idsTelefonesRemover) {
		
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
			
			if (idsTelefonesRemover != null && !idsTelefonesRemover.isEmpty()){
				
				idsIgnorar.addAll(idsTelefonesRemover);
			}
			
			if (fiador.getId() != null && !existePrincipal) {
				
				if (!this.telefoneFiadorRepository.verificarTelefonePrincipalFiador(fiador.getId(), idsIgnorar)) {
					
					if(!existePrincipal) {
						throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
					}
				}

			} else {
				if (fiador.getId() != null && !existePrincipal) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
				}
			}
			
		} else {
			
			if (!this.telefoneFiadorRepository.verificarTelefonePrincipalFiador(fiador.getId(), idsTelefonesRemover)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 telefone principal.");
			}
		}
	}

	private void validarDadosEntradaEnderecos(Fiador fiador, List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
			List<EnderecoAssociacaoDTO> listaEnderecosRemover) {
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {
			
			boolean existePrincipal = false;
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (EnderecoAssociacaoDTO dto : listaEnderecoAssociacaoSalvar){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					idsIgnorar.add(dto.getEndereco().getId());
				}
				
				if (dto.isEnderecoPrincipal()){
					
					existePrincipal = true;
					break;
				}
			}
			
			if (listaEnderecosRemover != null && !listaEnderecosRemover.isEmpty()){
				
				for (EnderecoAssociacaoDTO dto : listaEnderecosRemover){
					
					if (dto != null && dto.getEndereco() != null && dto.getEndereco().getId() != null){
						idsIgnorar.add(dto.getEndereco().getId());
					}
				}
			}
			
			/*if (existePrincipal){
				
				if (fiador.getId() != null){
					
					if (this.enderecoFiadorRepository.verificarEnderecoPrincipalFiador(fiador.getId(), idsIgnorar)){
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Apenas 1 endereço principal é permitido.");
					}
				}
			} else {*/
				
				if (fiador.getId() != null && !existePrincipal) {
					
					if (!this.enderecoFiadorRepository.verificarEnderecoPrincipalFiador(fiador.getId(), idsIgnorar)) {
						
						throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
					}
				} else {
					if (!existePrincipal) {
						throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
					}
				}
			//}
		} else {
			
			Set<Long> idsEnderecosIgnorar = new HashSet<Long>();
			
			if (listaEnderecosRemover != null && !listaEnderecosRemover.isEmpty()){
				
				for (EnderecoAssociacaoDTO dto : listaEnderecosRemover){
					
					if (dto.getId() != null){
						idsEnderecosIgnorar.add(dto.getId());
					}
				}
			}
			
			if (!this.enderecoFiadorRepository.verificarEnderecoPrincipalFiador(fiador.getId(), idsEnderecosIgnorar)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre 1 endereço principal.");
			}
		}
	}

	private void processarSocios(Fiador fiador, List<Pessoa> sociosAdicionar, Set<Long> sociosRemover) {
		
		if (sociosAdicionar != null && !sociosAdicionar.isEmpty()){
			
			for (Pessoa socio : sociosAdicionar){
				
				if (socio instanceof PessoaFisica){
					
					if (((PessoaFisica) socio).getConjuge() != null){
						
						PessoaFisica conjuge = ((PessoaFisica) socio).getConjuge();
						
						Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(conjuge.getCpf());
						conjuge.setId(idPessoa);
						
						if (conjuge.getId() == null){
							
							this.pessoaRepository.adicionar(conjuge);
						} else {
							
							this.pessoaRepository.merge(conjuge);
						}
					}
					
					Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(((PessoaFisica) socio).getCpf());
					socio.setId(idPessoa);
				}
				
				if (socio.getId() == null){
					
					this.pessoaRepository.adicionar(socio);
				} else {
					
					this.pessoaRepository.merge(socio);
				}
			}
		}
		
		if (fiador.getId() != null){
			
			List<Pessoa> sociosBanco = this.fiadorRepository.buscarSociosFiador(fiador.getId());
			
			if (sociosAdicionar != null && !sociosAdicionar.isEmpty()){
				
				for (Pessoa socio : sociosAdicionar){
					
					if(((PessoaFisica)socio).isSocioPrincipal()){
						for(Pessoa item : sociosBanco){
							if(((PessoaFisica)item).isSocioPrincipal()){
								if(!item.getId().equals(socio.getId())){
									((PessoaFisica)item).setSocioPrincipal(false);
								}
							}
						}
					}
					
					if (!sociosBanco.contains(socio)){
						sociosBanco.add(socio);
					}
				}
			}
			
			if (sociosRemover != null && !sociosRemover.isEmpty()){
				
				for (int index = 0 ; index < sociosBanco.size() ; index++){
					
					for (Long idRemover : sociosRemover){
						
						if (sociosBanco.get(index).getId().equals(idRemover)){
							sociosBanco.remove(index);
						}
					}
				}
			}
			
			fiador.setSocios(sociosBanco);
		} else {
			
			fiador.setSocios(sociosAdicionar);
		}
	}

	private void processarEnderecos(Fiador fiador,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {
		
		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosFiador(fiador, listaEnderecoAssociacaoRemover);
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {
			
			this.salvarEnderecosFiador(fiador, listaEnderecoAssociacaoSalvar);
		}
	}

	private void salvarEnderecosFiador(Fiador fiador, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		Pessoa pessoa = fiador.getPessoa();
		
        for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoFiador enderecoFiador = 
					this.enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(enderecoAssociacao.getEndereco().getId(), fiador.getId());

			EnderecoDTO dto = enderecoAssociacao.getEndereco();
			
			this.enderecoService.validarEndereco(dto, enderecoAssociacao.getTipoEndereco());
			
			Endereco endereco = null;
			
			if (enderecoFiador == null) {
				
				endereco = new Endereco();
				
				enderecoFiador = new EnderecoFiador();
				enderecoFiador.setFiador(fiador);
				
			} else {
				
				endereco = enderecoFiador.getEndereco();				
			}
			
			endereco.setBairro(dto.getBairro());
			endereco.setCep(dto.getCep());
			endereco.setCodigoCidadeIBGE(dto.getCodigoCidadeIBGE());
			endereco.setCidade(dto.getCidade());
			endereco.setComplemento(dto.getComplemento());
			endereco.setTipoLogradouro(dto.getTipoLogradouro());
			endereco.setLogradouro(dto.getLogradouro());
			endereco.setNumero(dto.getNumero());
			endereco.setUf(dto.getUf());
			endereco.setCodigoUf(dto.getCodigoUf());
			endereco.setPessoa(pessoa);
			
			enderecoFiador.setEndereco(endereco);
			enderecoFiador.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());
			enderecoFiador.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
			
			if (enderecoFiador.getId() == null) {
				
				this.enderecoFiadorRepository.adicionar(enderecoFiador);
				
			} else {
				
				this.enderecoFiadorRepository.alterar(enderecoFiador);	
			}
		}
	}

	private void removerEnderecosFiador(Fiador fiador, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<Long> idsEndereco = new ArrayList<Long>();
		
		List<Long> idsEnderecoFiador = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
			if (enderecoAssociacao.getEndereco().getId() != null){
				idsEndereco.add(enderecoAssociacao.getEndereco().getId());
			}

			EnderecoFiador enderecoFiador = this.enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(enderecoAssociacao.getEndereco().getId(), fiador.getId());
			
			if (enderecoFiador != null && enderecoFiador.getEndereco() != null){
				idsEnderecoFiador.add(enderecoFiador.getId());
			}
		}
		
		if (!idsEnderecoFiador.isEmpty()){
			
			this.enderecoFiadorRepository.excluirEnderecosFiador(idsEnderecoFiador);
		}
		
		if (!idsEndereco.isEmpty()){
			
			this.enderecoRepository.removerEnderecos(idsEndereco);
		}
	}
	
	private void processarTelefones(Fiador fiador, List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover){
		
		this.removerTelefonesFiador(listaTelefoneRemover);
		
		this.salvarTelefonesFiador(fiador, listaTelefoneAdicionar);
	}

	private void salvarTelefonesFiador(Fiador fiador, List<TelefoneAssociacaoDTO> listaTelefones) {
		
		Pessoa pessoa = fiador.getPessoa();
		
        for (TelefoneAssociacaoDTO dto : listaTelefones){
			
			TelefoneDTO telefoneDTO = dto.getTelefone();
			
			this.telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
			
            TelefoneFiador telefoneFiador = this.telefoneFiadorRepository.obterTelefonePorTelefoneFiador(telefoneDTO.getId(), fiador.getId());
			
			if (telefoneFiador == null){
				telefoneFiador = new TelefoneFiador();
				
				telefoneFiador.setFiador(fiador);
				telefoneFiador.setPrincipal(dto.isPrincipal());
				Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), pessoa);
				telefoneFiador.setTelefone(telefone);
				telefoneFiador.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneFiadorRepository.adicionar(telefoneFiador);
			} else {
				Telefone telefone = telefoneFiador.getTelefone();
				telefone.setDdd(telefoneDTO.getDdd());
				telefone.setNumero(telefoneDTO.getNumero());
				telefone.setRamal(telefone.getRamal());
				telefoneFiador.setPrincipal(dto.isPrincipal());
				telefoneFiador.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneFiadorRepository.alterar(telefoneFiador);
			}
		}
	}

	private void removerTelefonesFiador(Collection<Long> listaTelefones) {
		
		if (listaTelefones != null && !listaTelefones.isEmpty()){
			this.telefoneFiadorRepository.removerTelefonesFiador(listaTelefones);
			
			this.telefoneService.removerTelefones(listaTelefones);
		}
	}
	
	private void processarGarantias(Fiador fiador, List<Garantia> listaGarantias, Set<Long> idsGarantiasRemover){
		
		this.garantiaService.salvarGarantias(listaGarantias, fiador);
		
		this.garantiaService.removerGarantias(idsGarantiasRemover);
	}
	
	/**
	 * Remove relacionamento de garantia entre cota e fiador da associacao
	 * 
	 * @param idFiador
	 * 
	 * @param idCota
	 */
	private void removerCotaGarantiaAssociacao(Long idFiador, Long idCota){
		
		CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia != null){
			
			if (cotaGarantia instanceof CotaGarantiaFiador) {
			
				Fiador f = ((CotaGarantiaFiador) cotaGarantia).getFiador();
				
				if (idFiador.equals(f.getId())){
					
					cotaGarantiaRepository.remover(cotaGarantia);
				}
			}
		}
	}
	
	/**
	 * Adiciona ou altera relacionamento de garantia entre cota e fiador da associacao.
	 * 
	 * Apenas adiciona ou altera caso a cota não tenha garantia ou tenha garantia Fiador
	 * 
	 * @param fiador
	 * 
	 * @param cota
	 */
	private void adicionarCotaGarantiaAssociacao(Fiador fiador, Cota cota) {
		
		CotaGarantia cotaGarantia = cota.getCotaGarantia();
		
		if (cotaGarantia == null) {
			
			CotaGarantiaFiador cotaGarantiaFiador = new CotaGarantiaFiador();
			cotaGarantiaFiador.setData(Calendar.getInstance().getTime());
			cotaGarantiaFiador.setFiador(fiador);
			cotaGarantiaFiador.setCota(cota);
			
			cotaGarantiaRepository.adicionar(cotaGarantiaFiador);
			cota.setCotaGarantia(cotaGarantiaFiador);
			cotaRepository.merge(cota);
		} else {
			
			if (cotaGarantia instanceof CotaGarantiaFiador) {
			
				CotaGarantiaFiador cotaGarantiaFiador = (CotaGarantiaFiador) cotaGarantia;
				cotaGarantiaFiador.setData(Calendar.getInstance().getTime());
				cotaGarantiaFiador.setFiador(fiador);
				
				cotaGarantiaRepository.alterar(cotaGarantiaFiador);
				cota.setCotaGarantia(cotaGarantiaFiador);
				cotaRepository.merge(cota);
			} else {
				
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
						"A cota ["+cota.getNumeroCota()+"] já possui uma garantia diferente de [Fiador] cadastrada.\nDirija-se ao cadastro da [Cota] caso queira alterar o [Tipo] da garantia da cota para [Fiador]."));
			}
		}
	}
	
	private void processarCotasAssociadas(Fiador fiador, List<Cota> listaCotasAssociar, Set<Long> listaCotasDesassociar) {
		
		if (listaCotasDesassociar != null) {
			
			for (Long idCota : listaCotasDesassociar) {
				
				Cota cota = this.cotaRepository.buscarPorId(idCota);
				
				if (cota != null){
					cota.setFiador(null);
					
					this.removerCotaGarantiaAssociacao(fiador.getId(), idCota);
					
					this.cotaRepository.alterar(cota);
				}
			}
		}
		
		if (listaCotasAssociar != null){
			for (Cota cota : listaCotasAssociar){
				
				if (cota != null){
					
					cota.setFiador(fiador);
					
					this.adicionarCotaGarantiaAssociacao(fiador, cota);
					
					this.cotaRepository.alterar(cota);
				}
			}
		}
	}
	
	private void validarDadosFiador(Fiador fiador, List<Pessoa> socios){
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		if (fiador == null || fiador.getPessoa() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "CPF/CNPJ é obrigatório");
		}
		
		if (fiador.getPessoa() instanceof PessoaFisica){
			PessoaFisica pessoa = (PessoaFisica) fiador.getPessoa();
			
			this.validarDadosPessoaFisica(pessoa, msgsValidacao);
		} else {
			
			PessoaJuridica pessoa = (PessoaJuridica) fiador.getPessoa();
			
			CNPJValidator cnpjValidator = new CNPJValidator(false);
			
			if (pessoa == null){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "CNPJ é obrigatório"));
			}
			
			if (pessoa.getRazaoSocial() == null || pessoa.getRazaoSocial().trim().isEmpty()){
				msgsValidacao.add("Razão social é obrigatório");
			}
			
			if (pessoa.getNomeFantasia() == null || pessoa.getNomeFantasia().trim().isEmpty()){
				msgsValidacao.add("Nome fantasia é obrigatório");
			}
			
			if (pessoa.getInscricaoEstadual() == null || pessoa.getInscricaoEstadual().trim().isEmpty()){
				msgsValidacao.add("Inscrição estadual é obrigatório");
			}
			
			if (pessoa.getCnpj() == null || pessoa.getCnpj().trim().isEmpty()){
				msgsValidacao.add("CNPJ é obrigatório");
			} else {
				
				try{
					
					cnpjValidator.assertValid(pessoa.getCnpj());
				} catch(InvalidStateException e){
					
					msgsValidacao.add("CNPJ inválido.");
				}
			}
			
			if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
				msgsValidacao.add("E-mail é obrigatório");
			}
		}
		
		if (socios != null){
			for (Pessoa pessoa : socios){
				
				if (pessoa instanceof PessoaFisica){
					
					this.validarDadosPessoaFisica((PessoaFisica) pessoa, msgsValidacao);
				}
			}
		}
			
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}

	private void validarDadosPessoaFisica(PessoaFisica pessoa, List<String> msgsValidacao) {
		
		CPFValidator cpfValidator = new CPFValidator(false);
		
		if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()){
			msgsValidacao.add("Nome é obrigatório.");
		}
		
		if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
			msgsValidacao.add("E-mail é obrigatório.");
		}
		
		if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()){
			msgsValidacao.add("CPF é obrigatório.");
		} else {
			
			try{
				
				cpfValidator.assertValid(pessoa.getCpf());
			} catch(InvalidStateException e){
				
				msgsValidacao.add("CPF inválido.");
			}
		}
		
		if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
			msgsValidacao.add("R.G. é obrigatório.");
		}
		
		if (pessoa.getDataNascimento() == null){
			msgsValidacao.add("Data Nascimento é obrigatório.");
		}
		
		if (pessoa.getEstadoCivil() == null){
			msgsValidacao.add("Estado Civil é obrigatório.");
		}
		
		if (pessoa.getSexo() == null){
			msgsValidacao.add("Sexo é obrigatório.");
		}
		
		//dados do conjuge
		if (pessoa.getConjuge() != null){
			
			if (pessoa.getCpf().equals(pessoa.getConjuge().getCpf())){
				msgsValidacao.add("Fiador e conjuge devem ser pessoas diferentes.");
			}
			
			pessoa = pessoa.getConjuge();
			
			if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()){
				msgsValidacao.add("Nome do conjuge é obrigatório.");
			}
			
			if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
				msgsValidacao.add("E-mail do conjuge é obrigatório.");
			}
			
			if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()){
				msgsValidacao.add("CPF do conjuge é obrigatório.");
			} else {
				
				try{
					
					cpfValidator.assertValid(pessoa.getCpf());
				} catch(InvalidStateException e){
					
					msgsValidacao.add("CPF conjuge inválido.");
				}
			}
			
			if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
				msgsValidacao.add("R.G. do conjuge é obrigatório.");
			}
			
			if (pessoa.getDataNascimento() == null){
				msgsValidacao.add("Data Nascimento do conjuge é obrigatório.");
			}
			
			if (pessoa.getSexo() == null){
				msgsValidacao.add("Sexo do conjuge é obrigatório.");
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Pessoa buscarPessoaFiadorPorId(Long idFiador) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		Pessoa pessoa = this.fiadorRepository.buscarPessoaFiadorPorId(idFiador);
		
		if (pessoa instanceof PessoaFisica){
			((PessoaFisica) pessoa).getConjuge();
		}
		
		return pessoa;
	}
	
	@Override
	@Transactional
	public void excluirFiador(Long idFiador){
		
		Fiador fiador = this.fiadorRepository.buscarPorId(idFiador);
		
		if((fiador.getCotasAssociadas() != null && !fiador.getCotasAssociadas().isEmpty()) ||
				(this.cotaGarantiaRepository.obterCotaGarantiaFiadorPorIdFiador(idFiador) != null)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Fiador está associado a uma cota, não pode ser excluído.");
		}
			
		if (fiador != null){
			
			if (fiador.getCotasAssociadas() != null){
				
				for (Cota cota : fiador.getCotasAssociadas()){
					
					cota.setFiador(null);
					this.cotaRepository.alterar(cota);
				}
			}
			
			this.enderecoFiadorRepository.excluirEnderecosPorIdFiador(fiador.getId());
			
			this.telefoneFiadorRepository.excluirTelefonesFiador(fiador.getId());
			
			this.garantiaService.removerGarantiasPorFiador(fiador.getId());
			
			this.fiadorRepository.remover(fiador);
			
			Set<Long> idsTelefone = new HashSet<Long>();
			for (Telefone telefone : fiador.getPessoa().getTelefones()){
				
				idsTelefone.add(telefone.getId());
			}
			
			this.telefoneService.removerTelefones(idsTelefone);
			
			Set<Long> idsEndereco = new HashSet<Long>();
			for (Endereco endereco : fiador.getPessoa().getEnderecos()){
				
				idsEndereco.add(endereco.getId());
			}
			
			this.enderecoService.removerEnderecos(idsEndereco);
		}
	}

	@Transactional
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFiador(Long idFiador,	Set<Long> idsIgnorar) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFiador é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneFiadorRepository.buscarTelefonesFiador(idFiador, idsIgnorar);
		
		return listaTelAssoc;
	}
	
	@Transactional
	@Override
	public List<EnderecoAssociacaoDTO> buscarEnderecosFiador(Long idFiador,	Set<Long> idsIgnorar) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFiador é obrigatório");
		}
		
		List<EnderecoAssociacaoDTO> listaEndAssoc =
				this.enderecoFiadorRepository.buscaEnderecosFiador(idFiador, idsIgnorar);
		
		return listaEndAssoc;
	}

	@Transactional(readOnly = true)
	@Override
	public TelefoneFiador buscarTelefonePorTelefoneFiador(Long idFiador, Long idTelefone) {
		
		return this.telefoneFiadorRepository.obterTelefonePorTelefoneFiador(idTelefone, idFiador);
	}
	
	@Transactional(readOnly = true)
	@Override
	public EnderecoFiador buscarEnderecoPorEnderecoFiador(Long idFiador, Long idEndereco) {
		
		return this.enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(idEndereco, idFiador);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Fiador obterFiadorPorId(Long idFiador){
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		return this.fiadorRepository.buscarPorId(idFiador);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotasAssociadaFiador(Long idFiador, Set<Long> cotasIgnorar) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		return this.fiadorRepository.obterCotasAssociadaFiador(idFiador, cotasIgnorar);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean verificarAssociacaoFiadorCota(Long idFiador, Integer numeroCota, Set<Long> idsIgnorar) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		if (numeroCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		return this.fiadorRepository.verificarAssociacaoFiadorCota(idFiador, numeroCota, idsIgnorar);
	}

	@Override
	@Transactional(readOnly = true)
	public Fiador obterFiadorPorCPF(String cpf) {
		
		return this.fiadorRepository.obterFiadorPorCpf(cpf);
	}

	@Override
	@Transactional(readOnly = true)
	public PessoaFisica buscarSocioFiadorPorCPF(Long idFiador, String cpf) {
		
		return this.fiadorRepository.buscarSocioFiadorPorCPF(idFiador, cpf);
	}

	@Override
	@Transactional(readOnly = true)
	public Fiador obterFiadorPorCNPJ(String cnpj) {
		
		return this.fiadorRepository.obterFiadorPorCnpj(cnpj);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Pessoa> obterFiadorPorNome(String nomeFiador) {
		
		return fiadorRepository.obterFiadorPorNome(nomeFiador);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Pessoa> obterFiadorPorNome(String nomeFiador, Integer qtdMaxResul) {
		
		return fiadorRepository.obterFiadorPorNome(nomeFiador, qtdMaxResul);
	}
}