package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
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
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EnderecoFiadorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.FiadorRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.TelefoneFiadorRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.service.GarantiaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;

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
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaFiadorDTO obterFiadores(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO) {
		
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
			Set<Integer> listaCotasDesassociar) {
		
		this.validarDadosFiador(fiador, sociosAdicionar);
		
		if (fiador.getId() != null && fiador.getPessoa().getId() == null){
		
			fiador.getPessoa().setId(this.fiadorRepository.buscarIdPessoaFiador(fiador.getId()));
		}
		
		List<Endereco> listaEnderecos = new ArrayList<Endereco>();
		if (listaEnderecosAdicionar != null){
			for (EnderecoAssociacaoDTO enderecoAssociacaoDTO : listaEnderecosAdicionar){
				listaEnderecos.add(enderecoAssociacaoDTO.getEndereco());
			}
		}
		
		if (!listaEnderecos.isEmpty()){
			fiador.getPessoa().setEnderecos(listaEnderecos);
		}
		
		List<Telefone> listaTelefones = new ArrayList<Telefone>();
		if (listaTelefoneAdicionar != null){
			for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAdicionar){
				listaTelefones.add(telefoneAssociacaoDTO.getTelefone());
			}
		}
		
		if (!listaTelefones.isEmpty()){
			fiador.getPessoa().setTelefones(listaTelefones);
		}
		
		if (!listaGarantiaAdicionar.isEmpty()){
			fiador.setGarantias(listaGarantiaAdicionar);
		}
		
		List<Cota> listaCotas = new ArrayList<Cota>();
		if (listaCotasAssociar != null){
			for (Integer numeroCota : listaCotasAssociar){
				listaCotas.add(this.cotaRepository.obterPorNumerDaCota(numeroCota));
			}
		}
		
		if (!listaCotas.isEmpty()){
			fiador.setCotasAssociadas(listaCotas);
		}
		
		if (fiador.getPessoa() instanceof PessoaFisica){
			PessoaFisica conjuge = ((PessoaFisica)fiador.getPessoa()).getConjuge();
			
			if (conjuge != null){
				
				Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(conjuge.getCpf());
				conjuge.setId(idPessoa);
				
				if (conjuge.getId() == null){
					
					this.pessoaRepository.adicionar(conjuge);
				} else {
					
					this.pessoaRepository.alterar(conjuge);
				}
			}
		}
		
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
							
							this.pessoaRepository.alterar(conjuge);
						}
					}
					
					Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(((PessoaFisica) socio).getCpf());
					socio.setId(idPessoa);
				}
				
				if (socio.getId() == null){
					
					this.pessoaRepository.adicionar(socio);
				} else {
					
					this.pessoaRepository.alterar(socio);
				}
			}
		}
		
		if (fiador.getId() != null){
			
			List<Pessoa> sociosBanco = this.fiadorRepository.buscarSociosFiador(fiador.getId());
			
			if (sociosAdicionar != null && !sociosAdicionar.isEmpty()){
				
				for (Pessoa socio : sociosAdicionar){
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
			
			fiador.setSocios(sociosAdicionar);
		} else {
			
			fiador.setSocios(sociosAdicionar);
		}
		
		if (fiador.getPessoa() instanceof PessoaFisica){
			
			Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCPF(((PessoaFisica) fiador.getPessoa()).getCpf());
			fiador.getPessoa().setId(idPessoa);
		} else {
			
			Long idPessoa = this.pessoaRepository.buscarIdPessoaPorCNPJ(((PessoaJuridica) fiador.getPessoa()).getCnpj());
			fiador.getPessoa().setId(idPessoa);
		}
		
		if (fiador.getPessoa() instanceof PessoaFisica){
			if (((PessoaFisica) fiador.getPessoa()).getConjuge() != null){
				((PessoaFisica) fiador.getPessoa()).getConjuge().setConjuge((PessoaFisica) fiador.getPessoa());
			}
		}
		
		if (fiador.getPessoa().getId() == null){
			
			this.pessoaRepository.adicionar(fiador.getPessoa());
		} else {
			
			this.pessoaRepository.alterar(fiador.getPessoa());
		}
		
		if (fiador.getId() == null){
			
			fiador.setInicioAtividade(new Date());
			
			this.fiadorRepository.adicionar(fiador);
		} else {
			
			fiador.setInicioAtividade(this.fiadorRepository.buscarDataInicioAtividadeFiadorPorId(fiador.getId()));
			
			this.fiadorRepository.alterar(fiador);
		}
		
		this.processarEnderecos(fiador, listaEnderecosAdicionar, listaEnderecosRemover);
		
		this.processarTelefones(fiador, listaTelefoneAdicionar, listaTelefoneRemover);
		
		this.processarGarantias(fiador, listaGarantiaAdicionar, listaGarantiaRemover);
		
		this.processarCotasAssociadas(fiador, listaCotas, listaCotasDesassociar);
	}

	private void processarEnderecos(Fiador fiador,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosFiador(fiador, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosFiador(fiador, listaEnderecoAssociacaoRemover);
		}
	}
	
	private void salvarEnderecosFiador(Fiador fiador, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		this.enderecoService.cadastrarEnderecos(listaEnderecoAssociacao);
		
		if (listaEnderecoAssociacao != null){
		
			boolean isEnderecoPrincipal = false;
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao){
				
				if (isEnderecoPrincipal && enderecoAssociacao.isEnderecoPrincipal()){
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um endereço principal é permitido.");
				}
				
				if (enderecoAssociacao.isEnderecoPrincipal()){
					isEnderecoPrincipal = enderecoAssociacao.isEnderecoPrincipal();
				}
			}
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
	
				EnderecoFiador enderecoFiador = this.enderecoFiadorRepository.buscarPorId(enderecoAssociacao.getId());
	
				if (enderecoFiador == null) {
	
					enderecoFiador = new EnderecoFiador();
					enderecoFiador.setFiador(fiador);
					
					enderecoFiador.setEndereco(enderecoAssociacao.getEndereco());
					enderecoFiador.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());
					enderecoFiador.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
					
					this.enderecoFiadorRepository.adicionar(enderecoFiador);
				} else {
					
					enderecoFiador.setEndereco(enderecoAssociacao.getEndereco());
					enderecoFiador.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());
					enderecoFiador.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
					
					this.enderecoFiadorRepository.alterar(enderecoFiador);
				}
			}
		}
	}

	private void removerEnderecosFiador(Fiador fiador, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.getEndereco() != null){
				listaEndereco.add(enderecoAssociacao.getEndereco());
			}

			EnderecoFiador enderecoFiador = this.enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(enderecoAssociacao.getId(), fiador.getId());
			
			if (enderecoFiador != null && enderecoFiador.getEndereco() != null){
				idsEndereco.add(enderecoFiador.getEndereco().getId());
				
				this.enderecoFiadorRepository.remover(enderecoFiador);
			}
		}
		
		if (listaEndereco != null && !listaEndereco.isEmpty()){
		
			this.enderecoRepository.removerEnderecos(idsEndereco);
		}
	}
	
	private void processarTelefones(Fiador fiador, List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover){
		
		this.salvarTelefonesFiador(fiador, listaTelefoneAdicionar);
		
		this.removerTelefonesFiador(listaTelefoneRemover);
	}

	private void salvarTelefonesFiador(Fiador fiador, List<TelefoneAssociacaoDTO> listaTelefones) {
		
		this.telefoneService.cadastrarTelefone(listaTelefones);
		
		if (listaTelefones != null){
			boolean isTelefonePrincipal = false;
			
			for (TelefoneAssociacaoDTO dto : listaTelefones){
				
				if (isTelefonePrincipal && dto.isPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
				}
				
				if (dto.isPrincipal()){
					isTelefonePrincipal = dto.isPrincipal();
				}
			}
			
			for (TelefoneAssociacaoDTO dto : listaTelefones){
				
				TelefoneFiador telefoneFiador = this.telefoneFiadorRepository.obterTelefonePorTelefoneFiador(dto.getTelefone().getId(), fiador.getId());
				
				if (telefoneFiador == null){
					telefoneFiador = new TelefoneFiador();
					
					telefoneFiador.setFiador(fiador);
					telefoneFiador.setPrincipal(dto.isPrincipal());
					telefoneFiador.setTelefone(dto.getTelefone());
					telefoneFiador.setTipoTelefone(dto.getTipoTelefone());
					
					this.telefoneFiadorRepository.adicionar(telefoneFiador);
				} else {
					
					telefoneFiador.setPrincipal(dto.isPrincipal());
					telefoneFiador.setTipoTelefone(dto.getTipoTelefone());
					
					this.telefoneFiadorRepository.alterar(telefoneFiador);
				}
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
	
	private void processarCotasAssociadas(Fiador fiador,
			List<Cota> listaCotasAssociar, Set<Integer> listaCotasDesassociar) {
		
		if (listaCotasDesassociar != null){
			for (Integer numeroCota : listaCotasDesassociar){
				Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
				
				if (cota != null){
					cota.setFiador(null);
					
					this.cotaRepository.alterar(cota);
				}
			}
		}
		
		if (listaCotasAssociar != null){
			for (Cota cota : listaCotasAssociar){
				
				if (cota != null){
					
					cota.setFiador(fiador);
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
		if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()){
			msgsValidacao.add("Nome é obrigatório.");
		}
		
		if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
			msgsValidacao.add("E-mail é obrigatório.");
		}
		
		if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()){
			msgsValidacao.add("CPF é obrigatório.");
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

		
		if (fiador != null){
			
			if (fiador.getCotasAssociadas() != null){
				
				for (Cota cota : fiador.getCotasAssociadas()){
					
					cota.setFiador(null);
					this.cotaRepository.alterar(cota);
				}
			}
			
			this.fiadorRepository.remover(fiador);
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
	public List<Cota> obterCotasAssociadaFiador(Long idFiador) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		return this.fiadorRepository.obterCotasAssociadaFiador(idFiador);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean verificarAssociacaoFiadorCota(Long idFiador, Integer numeroCota) {
		
		if (idFiador == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Id fiador é obrigatório.");
		}
		
		if (numeroCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		return this.fiadorRepository.verificarAssociacaoFiadorCota(idFiador, numeroCota);
	}
}