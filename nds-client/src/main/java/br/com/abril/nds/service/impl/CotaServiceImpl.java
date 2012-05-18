package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCota;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCotaPK;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametrosCotaNotaFiscalEletronica;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaseReferenciaCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.HistoricoNumeroCotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.ReferenciaCotaRepository;
import br.com.abril.nds.repository.SocioCotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.repository.TipoEntregaRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class CotaServiceImpl implements CotaService {
	
	@Autowired
	private SituacaoCotaService situacaoCotaService; 
	
	@Autowired
	private CotaRepository cotaRepository;
		
	@Autowired
	private EnderecoCotaRepository enderecoCotaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
	
	@Autowired
	private BaseReferenciaCotaRepository baseReferenciaCotaRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private ReferenciaCotaRepository referenciaCotaRepository;
	
	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Autowired
	private SocioCotaRepository socioCotaRepository;
	
	@Autowired
	private HistoricoNumeroCotaRepository historicoNumeroCotaRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> obterCotas(FiltroCotaDTO filtro) {

		return cotaRepository.obterCotas(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro) {

		return cotaRepository.obterQuantidadeCotasPesquisadas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCota(numeroCota);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCotaAtiva(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
	}

	@Transactional(readOnly = true)
	public List<Cota> obterCotasPorNomePessoa(String nome) {
		
		return this.cotaRepository.obterCotasPorNomePessoa(nome);
	}

	@Transactional(readOnly = true)
	public Cota obterPorNome(String nome) {
		
		List<Cota> listaCotas = this.cotaRepository.obterPorNome(nome);
		
		if  (listaCotas == null || listaCotas.isEmpty()) {
			
			return null;
		}
		
		if (listaCotas.size() > 1) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Mais de um resultado encontrado para a cota com nome \"" + nome + "\"");
		}
		
		return listaCotas.get(0);
	}

	/**
	 * @see br.com.abril.nds.service.CotaService#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		return this.cotaRepository.obterEnderecosPorIdCota(idCota);
	}	
	
	
	/**
	 * @see br.com.abril.nds.service.CotaService#obterPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public Cota obterPorId(Long idCota) {

		if (idCota == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Id da cota não pode ser nulo.");
		}
		
		return this.cotaRepository.buscarPorId(idCota);
	}

	/**
	 * @see br.com.abril.nds.service.CotaService#processarEnderecos(java.util.Long, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public void processarEnderecos(Long idCota,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {

		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosCota(cota, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosCota(cota, listaEnderecoAssociacaoRemover);
		}
	}
	
	private void salvarEnderecosCota(Cota cota, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());

			if (enderecoCota == null) {

				enderecoCota = new EnderecoCota();

				enderecoCota.setCota(cota);
			}

			enderecoCota.setEndereco(enderecoAssociacao.getEndereco());

			enderecoCota.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoCota.setTipoEndereco(enderecoAssociacao.getTipoEndereco());

			this.enderecoCotaRepository.merge(enderecoCota);
		}
	}

	private void removerEnderecosCota(Cota cota,
									  List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			listaEndereco.add(enderecoAssociacao.getEndereco());

			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
			
			idsEndereco.add(enderecoAssociacao.getEndereco().getId());

			this.enderecoCotaRepository.remover(enderecoCota);
		}
		
		this.enderecoRepository.removerEnderecos(idsEndereco);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
		
		return listaTelAssoc;
	}
	
	@Transactional
	public void processarTelefones(Long idCota, 
			List<TelefoneAssociacaoDTO> listaTelefonesAdicionar, 
			Collection<Long> listaTelefonesRemover){
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		this.salvarTelefonesCota(cota, listaTelefonesAdicionar);
		
		this.removerTelefonesCota(listaTelefonesRemover);
		
		List<Telefone> listaTelefone = new ArrayList<Telefone>();
		
		for (TelefoneAssociacaoDTO telefoneCota : listaTelefonesAdicionar){
			listaTelefone.add(telefoneCota.getTelefone());
		}
		
		cota.getPessoa().setTelefones(listaTelefone);
		
		this.cotaRepository.alterar(cota);
		
	}

	private void salvarTelefonesCota(Cota cota, List<TelefoneAssociacaoDTO> listaTelefonesCota) {
		
		this.telefoneService.cadastrarTelefone(listaTelefonesCota, cota.getPessoa());
		
		if (listaTelefonesCota != null){
			boolean isTelefonePrincipal = false;
			
			for (TelefoneAssociacaoDTO dto : listaTelefonesCota){
				
				if (isTelefonePrincipal && dto.isPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
				}
				
				if (dto.isPrincipal()){
					isTelefonePrincipal = dto.isPrincipal();
				}
				
				TelefoneCota telefoneCota = new TelefoneCota();
				telefoneCota.setCota(cota);
				telefoneCota.setPrincipal(dto.isPrincipal());
				telefoneCota.setTelefone(dto.getTelefone());
				telefoneCota.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneCotaRepository.adicionar(telefoneCota);
			}
		}
	}

	private void removerTelefonesCota(Collection<Long> listaTelefonesCota) {
		
		if (listaTelefonesCota != null && !listaTelefonesCota.isEmpty()){
			this.telefoneCotaRepository.removerTelefonesCota(listaTelefonesCota);
			
			this.telefoneService.removerTelefones(listaTelefonesCota);
		}
	}

	@Transactional
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota) {	
		return cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota);
	}

	@Override
	@Transactional
	public List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario, MotivoAlteracaoSituacao.INADIMPLENCIA);
		
		List<CotaSuspensaoDTO> cotasDTO = new ArrayList<CotaSuspensaoDTO>();
		
		for(Cota cota : cotasSuspensas) {
			
			Pessoa pessoa = cota.getPessoa();
			
			String nome = pessoa instanceof PessoaFisica ? 
					((PessoaFisica)pessoa).getNome() : ((PessoaJuridica)pessoa).getRazaoSocial();
			
			if(cota.getContratoCota().isExigeDocumentacaoSuspencao()) {
				
				cotasDTO.add(new CotaSuspensaoDTO(
					cota.getId(), 
					cota.getNumeroCota(),
					nome, 
					null,
					null,
					null,
					null,
					null));	
			}
		}
		
		return cotasDTO;
	}
	
	
	@Override
	@Transactional
	public List<Cota> suspenderCotas(List<Long> idCotas, Long idUsuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {

		List<Cota> cotasSuspensas = new ArrayList<Cota>();
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		for(Long id:idCotas) {	
			
			cotasSuspensas.add(suspenderCota(id, usuario, motivoAlteracaoSituacao));
		}	
		
		return cotasSuspensas;
	}

	@Override
	@Transactional
	public Cota suspenderCota(Long idCota, Usuario usuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {
				
		Cota cota = obterPorId(idCota);
		
		if(SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A cota já está suspensa!");
		}
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setDataEdicao(new Date());
		historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuario);
		historico.setMotivo(motivoAlteracaoSituacao);
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		
		historicoSituacaoCotaRepository.adicionar(historico);
		
		cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
		
		cotaRepository.alterar(cota);
		
		situacaoCotaService.removerAgendamentosAlteracaoSituacaoCota(cota.getId());
		
		return cota;
	}
	
	@Override
	@Transactional
	public List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer inicio, Integer rp) {
		
		return cotaRepository.obterCotasSujeitasSuspensao(sortOrder,sortColumn, inicio, rp);		
	}

	@Override
	@Transactional
	public Long obterTotalCotasSujeitasSuspensao() {
		return cotaRepository.obterTotalCotasSujeitasSuspensao();
	}
	
	@Override
	@Transactional
	public String obterNomeResponsavelPorNumeroDaCota(Integer numeroCota){
		
		if (numeroCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório");
		}
		
		Cota cota = this.obterPorNumeroDaCota(numeroCota);
		
		if (cota != null){
			if (cota.getPessoa() instanceof PessoaFisica){
				return ((PessoaFisica) cota.getPessoa()).getNome();
			} else {
				return ((PessoaJuridica) cota.getPessoa()).getRazaoSocial();
			}
		}
		
		return null;
	}


	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotaAssociadaFiador(Long idFiador){
		return this.cotaRepository.obterCotaAssociadaFiador(idFiador);
	}

	/**
	 * @see br.com.abril.nds.service.CotaService#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
	 */
	@Override
	@Transactional
	public Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota) {
		return this.cotaRepository.obterCotaPDVPorNumeroDaCota(numeroCota);
	}

	@Override
	@Transactional
	public DistribuicaoDTO obterDadosDistribuicaoCota(Long idCota) {
		
		if(idCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Id da cota não informado.");
		}
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if(cota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		ParametroDistribuicaoCota parametro = cota.getParametroDistribuicao();
		
		boolean qtdePDVAutomatico = distribuidorService.obter().isPreenchimentoAutomaticoPDV();
				
		DistribuicaoDTO dto = new DistribuicaoDTO();
		
		dto.setNumCota(cota.getNumeroCota());
		
		if(cota.getBox()!= null){
			dto.setBox(cota.getBox().getNome());
		}
		
		if(qtdePDVAutomatico) {	
			dto.setQtdePDV( (cota.getPdvs()!=null) ? cota.getPdvs().size() : 0);
			dto.setQtdeAutomatica(true);
		}
		
		if(parametro == null) {
			return dto;
		}
		
		if(!qtdePDVAutomatico) {
			dto.setQtdePDV(parametro.getQtdePDV());
		}
		
		dto.setAssistComercial(parametro.getAssistenteComercial());
		dto.setTipoEntrega( (parametro.getTipoEntrega()==null) ? null : parametro.getTipoEntrega().getId());
		dto.setArrendatario(parametro.getArrendatario());
		dto.setObservacao(parametro.getObservacao());
		dto.setRepPorPontoVenda(parametro.getRepartePorPontoVenda());
		dto.setSolNumAtras(parametro.getSolicitaNumAtras());
		dto.setRecebeRecolhe(parametro.getRecebeRecolheParcias());
		dto.setNeImpresso(parametro.getNotaEnvioImpresso());
		dto.setNeEmail(parametro.getNotaEnvioEmail());
		dto.setCeImpresso(parametro.getChamadaEncalheImpresso());
		dto.setCeEmail(parametro.getChamadaEncalheEmail());
		dto.setSlipImpresso(parametro.getSlipImpresso());
		dto.setSlipEmail(parametro.getSlipEmail());
		
		return dto;
	}

	@Override
	@Transactional
	public List<Fornecedor> obterFornecedoresCota(Long idCota) {
		Cota cota = this.obterPorId(idCota);
		Set<Fornecedor> fornecedores = cota.getFornecedores();
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		for(Fornecedor itemFornecedor:fornecedores){
			listaFornecedores.add(itemFornecedor);
		}
		return listaFornecedores;
	}

	@Override
	@Transactional
	public void salvarDistribuicaoCota(DistribuicaoDTO dto) {
		
		if(dto==null || dto.getNumCota()==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota = cotaRepository.obterPorNumerDaCota(dto.getNumCota());		
		
		if( cota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		ParametroDistribuicaoCota parametros = new ParametroDistribuicaoCota();
		parametros.setQtdePDV(dto.getQtdePDV());
		parametros.setAssistenteComercial(dto.getAssistComercial());
		parametros.setTipoEntrega(tipoEntregaRepository.buscarPorId(dto.getTipoEntrega()));
		parametros.setArrendatario(dto.getArrendatario());
		parametros.setObservacao(dto.getObservacao());
		parametros.setRepartePorPontoVenda(dto.getRepPorPontoVenda());
		parametros.setSolicitaNumAtras(dto.getSolNumAtras());
		parametros.setRecebeRecolheParcias(dto.getRecebeRecolhe());
		parametros.setNotaEnvioImpresso(dto.getNeImpresso());
		parametros.setNotaEnvioEmail(dto.getNeEmail());
		parametros.setChamadaEncalheImpresso(dto.getCeImpresso());
		parametros.setChamadaEncalheEmail(dto.getCeEmail());
		parametros.setSlipImpresso(dto.getSlipImpresso());
		parametros.setSlipEmail(dto.getSlipEmail());
		
		cota.setParametroDistribuicao(parametros);
		
		cotaRepository.merge(cota);
	}
	

	@Override
	@Transactional(readOnly = true)
	public CotaDTO obterDadosCadastraisCota(Long idCota){
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		CotaDTO cotaDTO = new CotaDTO();
		cotaDTO.setIdCota(cota.getId());
		cotaDTO.setNumeroCota(cota.getNumeroCota());
		cotaDTO.setClassificacaoSelecionada(cota.getClassificacaoEspectativaFaturamento());
		cotaDTO.setDataInclusao(cota.getInicioAtividade());
		cotaDTO.setEmailNF(cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica());
		cotaDTO.setEmiteNFE(cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica());
		cotaDTO.setStatus(cota.getSituacaoCadastro());
		
		this.atribuirDadosPessoaCota(cotaDTO, cota.getPessoa());
		this.atribuirDadosBaseReferencia(cotaDTO, cota.getBaseReferenciaCota());
		
		return cotaDTO;
	}
	
	private void atribuirDadosPessoaCota(CotaDTO cotaDTO, Pessoa pessoa){
		
		if(pessoa == null){
			return;
		}
		
		if(pessoa instanceof PessoaJuridica){
			
			PessoaJuridica pessoaJ = (PessoaJuridica) pessoa;
			cotaDTO.setInscricaoEstadual(pessoaJ.getInscricaoEstadual());
			cotaDTO.setInscricaoMunicipal(pessoaJ.getInscricaoMunicipal());
			cotaDTO.setEmail(pessoaJ.getEmail());
			cotaDTO.setNumeroCnpj(pessoaJ.getCnpj());
			cotaDTO.setNomeFantasia(pessoaJ.getNomeFantasia());
			cotaDTO.setRazaoSocial(pessoaJ.getRazaoSocial());
			cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);
		}
	}
	
	private void atribuirDadosBaseReferencia(CotaDTO cotaDTO, BaseReferenciaCota baseReferenciaCota){
		
		if(baseReferenciaCota == null){
			return;
		}
		
		cotaDTO.setInicioPeriodo(baseReferenciaCota.getInicioPeriodo());
		cotaDTO.setFimPeriodo(baseReferenciaCota.getFinalPeriodo());
		
		if(baseReferenciaCota.getReferenciasCota()!= null && !baseReferenciaCota.getReferenciasCota().isEmpty()){
			
			List<ReferenciaCota> referenicasCota = new ArrayList<ReferenciaCota>();
			referenicasCota.addAll(baseReferenciaCota.getReferenciasCota());
			
			if(referenicasCota.size() > 0){
				cotaDTO.setHistoricoPrimeiraCota(referenicasCota.get(0).getCota().getNumeroCota());
				cotaDTO.setHistoricoPrimeiraPorcentagem(referenicasCota.get(0).getPercentual());
			}
			
			if(referenicasCota.size() > 1){
				cotaDTO.setHistoricoSegundaCota(referenicasCota.get(1).getCota().getNumeroCota());
				cotaDTO.setHistoricoSegundaPorcentagem(referenicasCota.get(1).getPercentual());
			}
			
			if(referenicasCota.size() > 2){
				cotaDTO.setHistoricoTerceiraCota(referenicasCota.get(2).getCota().getNumeroCota());
				cotaDTO.setHistoricoTerceiraPorcentagem(referenicasCota.get(2).getPercentual());
			}
		}
	}
	
	@Override
	@Transactional
	public void excluirCota(Long idCota){
		
		if(idCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada para exclusão invalida!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
	
		try{	
			
			cotaRepository.remover(cota);
			
		}catch (RuntimeException e) {
			
			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
				throw new ValidacaoException(TipoMensagem.ERROR,"Exclusão não permitida, registro possui dependências!");
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer gerarNumeroSugestaoCota(){
	
		return cotaRepository.gerarSugestaoNumeroCota();
	}
	
	@Override
	@Transactional
	public Long salvarCota(CotaDTO cotaDto){
		
		validarParametrosObrigatoriosCota(cotaDto);
		
		validarFormatoDados(cotaDto);
		
		validarHistoricoCotaBase(cotaDto);
		
		Cota cota  = null;
		
		if(cotaDto.getIdCota()!= null){
			cota = cotaRepository.buscarPorId(cotaDto.getIdCota());
		}
				
		boolean incluirPDV = false;
		
		if(cota == null){
			cota = new Cota();
			cota.setInicioAtividade(new Date());
			cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
			incluirPDV = true;
		}
		
		processarNovoNumeroCota(cotaDto.getNumeroCota(),cota.getId());
		
	    cota.setNumeroCota(cotaDto.getNumeroCota());
	    
	    cota.setParametrosCotaNotaFiscalEletronica(getParamNFE(cota, cotaDto));
	    
	    cota.setClassificacaoEspectativaFaturamento(cotaDto.getClassificacaoSelecionada());
	    
	    cota.setPessoa(getPessoaCota(cota, cotaDto));
	    
	    cota  = cotaRepository.merge(cota);
	    
	    BaseReferenciaCota baseReferenciaCota = processarDadosBaseReferenciaCota(cota, cotaDto);
	    
	    processarDadosReferenciaCota(baseReferenciaCota, cotaDto);
	    
	    if(incluirPDV){
	    	processarDadosPDV(cota, cotaDto);
	    }
	
	    return cota.getId();
	}
	
	private void validarHistoricoCotaBase(CotaDTO cotaDto) {
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
			
			if(DateUtil.removerTimestamp(cotaDto.getInicioPeriodo()).compareTo(DateUtil.removerTimestamp(cotaDto.getDataInclusao()))!=0){

				throw new ValidacaoException(TipoMensagem.WARNING,"Campo [Período] referente à cota base deve ser igual ao campo [Início de Atividade]!");
			}
		}
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
			return;
		}
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"Campo [Até] referente à  cota base deve ser informado!");
		}
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() != null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"Campo [Período] referente à  cota base deve ser informado!");
		}
		
		if(DateUtil.isDataInicialMaiorDataFinal(cotaDto.getInicioPeriodo(), cotaDto.getFimPeriodo())){
			throw new ValidacaoException(TipoMensagem.WARNING,"Período  cota base invalido!");
		}
	}

	private void validarFormatoDados(CotaDTO cotaDto) {
		
		if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
			
			CNPJValidator cnpjValidator = new CNPJValidator(true);
			try{
				cnpjValidator.assertValid(cotaDto.getNumeroCnpj());
				
			}catch(InvalidStateException e){
				throw new ValidacaoException(TipoMensagem.WARNING,"Número CNPJ inválido!");
			}
		}
		
		if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
			
			CPFValidator cpfValidator = new CPFValidator();
			try{
				cpfValidator.assertValid(cotaDto.getNumeroCPF());
				
			}catch(InvalidStateException e){
				throw new ValidacaoException(TipoMensagem.WARNING,"Número CPF inválido!");
			}
		}
		
		if( cotaDto.getEmail()!= null && !cotaDto.getEmail().isEmpty() && !Util.validarEmail(cotaDto.getEmail())){
			throw new ValidacaoException(TipoMensagem.WARNING,"E-mail inválido!");
		}
		
		if( cotaDto.getEmailNF()!= null && !cotaDto.getEmailNF().isEmpty() && !Util.validarEmail(cotaDto.getEmailNF())){
			throw new ValidacaoException(TipoMensagem.WARNING,"E-mail NF-e inválido!");
		}
		
	}

	private void processarDadosPDV(Cota cota,CotaDTO cotaDTO) {
		
		if(cota.getPdvs() == null || cota.getPdvs().isEmpty()){
			
			PDV pdv = new PDV();
			pdv.setCaracteristicas(new CaracteristicasPDV());
			pdv.getCaracteristicas().setPontoPrincipal(Boolean.TRUE);
			pdv.setDataInclusao(new Date());
			pdv.setCota(cota);
			
			if( TipoPessoa.JURIDICA.equals(cotaDTO.getTipoPessoa())){
				
				pdv.setNome(cotaDTO.getRazaoSocial());
			}
			else {
				
				pdv.setNome(cotaDTO.getNomePessoa());
			}
			
			pdvRepository.adicionar(pdv);
		}

	}

	private ParametrosCotaNotaFiscalEletronica getParamNFE(Cota cota, CotaDTO cotaDto){
		
		ParametrosCotaNotaFiscalEletronica paramNFE = cota.getParametrosCotaNotaFiscalEletronica(); 
	    
	    if(paramNFE == null){
	    	paramNFE = new ParametrosCotaNotaFiscalEletronica();
	    }
	    		
	    paramNFE.setEmailNotaFiscalEletronica(cotaDto.getEmailNF());
	    paramNFE.setEmiteNotaFiscalEletronica(cotaDto.isEmiteNFE());
	    
	    return paramNFE;
	}
	
	private void validarParametrosObrigatoriosCota(CotaDTO cotaDto) {
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if(cotaDto.getNumeroCota() == null){
			mensagensValidacao.add("O campo Cota é obrigatório.");
		}
	
		if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
			
			if(cotaDto.getRazaoSocial() == null || cotaDto.getRazaoSocial().isEmpty()){
				mensagensValidacao.add("O campo Razão Social é obrigatório.");
			}
			
			if(cotaDto.getNumeroCnpj() == null || cotaDto.getNumeroCnpj().trim().isEmpty()){
				mensagensValidacao.add("O campo CNPJ é obrigatório.");
	    	}
			
			if(cotaDto.getInscricaoEstadual() == null || cotaDto.getInscricaoEstadual().isEmpty() ){
				mensagensValidacao.add("O campo Inscrição Estadual é obrigatório.");
			}
		}
				
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	}
	
	private Pessoa getPessoaCota(Cota cota, CotaDTO cotaDto){
		
		Pessoa pessoa = cota.getPessoa();
	    
	    if(cota.getPessoa() == null){
	    	
	    	if( TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
	    		
	    		pessoa  = pessoaJuridicaRepository.buscarPorCnpj(cotaDto.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
	    		
	    		if(pessoa == null){
	    			pessoa = new PessoaJuridica();
	    		}
	    	}
	    }
	    
	    pessoa.setEmail(cotaDto.getEmail());
	    
    	if ( pessoa instanceof  PessoaJuridica  ){
    		
    		((PessoaJuridica) pessoa).setCnpj(cotaDto.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
    		((PessoaJuridica) pessoa).setInscricaoEstadual(cotaDto.getInscricaoEstadual());
    		((PessoaJuridica) pessoa).setInscricaoMunicipal(cotaDto.getInscricaoMunicipal());
    		((PessoaJuridica) pessoa).setNomeFantasia(cotaDto.getNomeFantasia());
    		((PessoaJuridica) pessoa).setRazaoSocial(cotaDto.getRazaoSocial());
    		
    		pessoa = pessoaJuridicaRepository.merge(((PessoaJuridica) pessoa));
    	}
	    
    	return pessoa;
	}
	
	private BaseReferenciaCota processarDadosBaseReferenciaCota(Cota cota , CotaDTO cotaDto){
		
		BaseReferenciaCota baseReferenciaCota  = cota.getBaseReferenciaCota();
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
			
			if(baseReferenciaCota!= null){
				baseReferenciaCotaRepository.remover(baseReferenciaCota);
				return null;
			}
		}
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
			
			if (baseReferenciaCota == null ){
				baseReferenciaCota = new BaseReferenciaCota();    
			}
			
			baseReferenciaCota.setInicioPeriodo(cotaDto.getInicioPeriodo());
		    baseReferenciaCota.setFinalPeriodo(cotaDto.getFimPeriodo());
		    baseReferenciaCota.setCota(cota);
		    
		    baseReferenciaCota = baseReferenciaCotaRepository.merge(baseReferenciaCota);
		}
		
	    return  baseReferenciaCota;
		
	}
	
	private void processarDadosReferenciaCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
		if(baseReferenciaCota == null){
			return;
		}
		
		validarPorcentagemCotaBase(cotaDto);
		
		Set<ReferenciaCota> referenciasCota = baseReferenciaCota.getReferenciasCota();
		
		if(referenciasCota != null && !referenciasCota.isEmpty()){
	    	
			referenciaCotaRepository.excluirReferenciaCota(baseReferenciaCota.getId());
	    }
		
		referenciasCota = getReferenciasCota(baseReferenciaCota, cotaDto);
		
		if(!referenciasCota.isEmpty()){

			for(ReferenciaCota ref : referenciasCota){
	    		referenciaCotaRepository.merge(ref);
	    	}
		}
	}
	
	
	private Set<ReferenciaCota> getReferenciasCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
		Set<ReferenciaCota> referenciasCota = new HashSet<ReferenciaCota>();
	    
	    if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(), cotaDto.getHistoricoPrimeiraPorcentagem())){
			
	    	referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoPrimeiraCota(),
					   							   cotaDto.getHistoricoPrimeiraPorcentagem(),
					   							   baseReferenciaCota) );
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(), cotaDto.getHistoricoSegundaPorcentagem())){
			
			referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoSegundaCota(),
					   							   cotaDto.getHistoricoSegundaPorcentagem(),
					   							   baseReferenciaCota) );
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(), cotaDto.getHistoricoTerceiraPorcentagem())){
			
			referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoTerceiraCota(),
												   cotaDto.getHistoricoTerceiraPorcentagem(),
												   baseReferenciaCota) );

		}
		
		return referenciasCota;
	}
	
	private ReferenciaCota getReferencaiCota(Integer numeroCota,BigDecimal porcentagem, BaseReferenciaCota baseReferenciaCota){
		
		Cota cota = null;
		
		if(numeroCota != null){
			cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		}
		
		ReferenciaCota referenciaCota = new ReferenciaCota();
		referenciaCota.setCota(cota);
		referenciaCota.setPercentual(porcentagem);
		referenciaCota.setBaseReferenciaCota(baseReferenciaCota);
		
		return referenciaCota;
	}
	
	private void validarPorcentagemCotaBase(CotaDTO cotaDto) {
		
		boolean existeValor = false;
		BigDecimal valor  = BigDecimal.ZERO;
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(),
				cotaDto.getHistoricoPrimeiraPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoPrimeiraPorcentagem());
			existeValor = true;
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(),
				cotaDto.getHistoricoSegundaPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoSegundaPorcentagem());
			existeValor = true;
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(),
				cotaDto.getHistoricoTerceiraPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoTerceiraPorcentagem());
			existeValor = true;
		}
		
		if(existeValor && (valor.intValue() != 100)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Porcentagem histórica cota base invalido! A porcentagem do histórico das cotas base deve ser 100%  ");
		}
		
	}

	private boolean tratarValorReferenciaCota(Integer numeroCota, BigDecimal porcentagem){
		return (numeroCota != null && porcentagem != null);
	}
	
	/**
	 * Verifica se o número da cota existente pode ser utilizado por uma nova cota
	 * 
	 * @param numeroCota - número da nova cota
	 */
	private void processarNovoNumeroCota(Integer numeroCota, Long idCota){
		
		Cota cota  = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		if(cota!= null){
			
			if(SituacaoCadastro.INATIVO.equals(cota.getSituacaoCadastro())){
				
				if(!isParametroDistribuidoNumeroCotaValido(numeroCota)){

					throw new ValidacaoException(TipoMensagem.WARNING,"Número da cota está inativo mas não pode ser usado.");
				}
				else{
				   //Alterar Numero Cota e registra histoico
					alteraNumeroCota(cota);
				}
			}
			else{
				//Verifica se é edicao da cota
				if(!cota.getId().equals(idCota)){
					throw new ValidacaoException(TipoMensagem.WARNING,"Número da cota não pode ser utilizado.");
				}
			}	
		}
	}
	
	private void alteraNumeroCota(Cota cota){
		
		Integer novoNumeroCota = getNovoNumeroCota(cota.getNumeroCota(), null,1);
		
		Integer numeroCotaAntigo = cota.getNumeroCota();
		
		cota.setNumeroCota(novoNumeroCota);
		cotaRepository.merge(cota);
		
		HistoricoNumeroCotaPK pk = new HistoricoNumeroCotaPK();
		pk.setDataAlteracao(new Date());
		pk.setIdCota(cota.getId());
		

		HistoricoNumeroCota historicoNumeroCota = new HistoricoNumeroCota();
		historicoNumeroCota.setNumeroCota(numeroCotaAntigo);
		historicoNumeroCota.setPk(pk);
		
		historicoNumeroCotaRepository.merge(historicoNumeroCota);
		
	}
	
	private Integer getNovoNumeroCota(Integer numeroCota, Integer novoNumeroCota ,Integer numero){
		
		Cota cota  = cotaRepository.obterPorNumerDaCota( (novoNumeroCota == null) ?numeroCota :novoNumeroCota);
		
		if(cota != null){
			novoNumeroCota = numero * 10000 + numeroCota;
			return getNovoNumeroCota(numeroCota,novoNumeroCota, ++numero);
		}
		
		return novoNumeroCota;
	}
	
	/**
	 * Verifica se o numero da cota informado pode ser reaproveitado por outra cota em um novo cadastro
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	private boolean isParametroDistribuidoNumeroCotaValido(Integer  numeroCota){
		
		HistoricoSituacaoCota histCota  = historicoSituacaoCotaRepository.obterUltimoHistoricoInativo(numeroCota);
		
		if(histCota == null){
			return true;
		}
		
	    Distribuidor distribuidor = distribuidorService.obter();
	    
	    Long qntDiasInativo =  DateUtil.obterDiferencaDias(histCota.getDataInicioValidade(), new Date());
	    
	    Long qntDiasDistribuidor =  (distribuidor.getQntDiasReutilizacaoCodigoCota() == null)?0:distribuidor.getQntDiasReutilizacaoCodigoCota();
	    
		return (qntDiasInativo > qntDiasDistribuidor );
	}

	
	/**
	 * Método responsável por obter tipos de cota para preencher combo da camada view
	 * @return comboTiposCota: Tipos de cota padrão.
	 */
	@Override
	public List<ItemDTO<TipoCota, String>> getComboTiposCota() {
		List<ItemDTO<TipoCota,String>> comboTiposCota =  new ArrayList<ItemDTO<TipoCota,String>>();
		for (TipoCota itemTipoCota: TipoCota.values()){
			comboTiposCota.add(new ItemDTO<TipoCota,String>(itemTipoCota, itemTipoCota.getDescTipoCota()));
		}
		return comboTiposCota;
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoDesconto> obterDescontos(Long idCota){
		
		return tipoDescontoRepository.obterTipoDescontoNaoReferenciadosComCota(idCota);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoDesconto> obterDescontosCota(Long idCota){
		
		return tipoDescontoRepository.obterTiposDescontoCota(idCota);
	}
	
	@Transactional
	@Override
	public void salvarDescontosCota(List<Long> descontos, Long idCota){
		
		if(idCota == null ){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro Cota invalido!");
		}
		
		Set<TipoDesconto> listaDescontos = new HashSet<TipoDesconto>();
		
		if(descontos != null && !descontos.isEmpty()){
			
			TipoDesconto tipoDesconto = null;
			
			for(Long  td :  descontos ){
				
				tipoDesconto = tipoDescontoRepository.buscarPorId(td) ;
				
				if(tipoDesconto != null){
					listaDescontos.add( tipoDesconto );
				}

			}
		}

		Cota cota = cotaRepository.buscarPorId(idCota);
		cota.setTiposDescontoCota(listaDescontos);
		
		cotaRepository.alterar(cota);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<SocioCota> obterSociosCota(Long idCota){
		
		return socioCotaRepository.obterSocioCotaPorIdCota(idCota);
		
	}
	
	@Override
	@Transactional
	public void salvarSociosCota(List<SocioCota> sociosCota, Long idCota ){
		
		if(idCota == null ){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro Cota invalido!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if(cota == null ){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro Cota invalido!");
		}
		
		if( cota.getSociosCota() != null && !cota.getSociosCota().isEmpty()){
	    	
			socioCotaRepository.removerSociosCota(idCota);
	    }
		
		if( sociosCota!= null && !sociosCota.isEmpty()){
			
			if(!isSocioPrincipal(sociosCota)){
				throw new ValidacaoException(TipoMensagem.WARNING,"Deve ser informado um sócio principal!");
			}
	
			for(SocioCota ref : sociosCota){
	    		ref.setCota(cota);
				socioCotaRepository.merge(ref);
	    	}
		}
	}

	private boolean isSocioPrincipal(List<SocioCota> sociosCota) {
		
		for(SocioCota socio : sociosCota){
			if(socio.getPrincipal())
				return true;
		}
		
		return false;
	}
}
