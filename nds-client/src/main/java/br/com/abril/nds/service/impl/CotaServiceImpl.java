package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CotaCobrancaVO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;

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
	private BancoRepository bancoRepository;
	
	@Autowired
	private ConcentracaoCobrancaCotaRepository concentracaoCobrancaCotaRepository;

	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCota(numeroCota);
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
		
		this.telefoneService.cadastrarTelefone(listaTelefonesCota);
		
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

		List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario);
		
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
	public List<Cota> suspenderCotas(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas = new ArrayList<Cota>();
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		for(Long id:idCotas) {	
			cotasSuspensas.add(suspenderCota(id, usuario));			
		}		
		return cotasSuspensas;
	}

	@Override
	@Transactional
	public Cota suspenderCota(Long idCota, Usuario usuario) {
				
		Cota cota = obterPorId(idCota);
		
		if(SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
			throw new InvalidParameterException();
		}
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setDataEdicao(new Date());
		historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuario);
		historico.setMotivo(MotivoAlteracaoSituacao.INADIMPLENCIA);
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

	
	
	@Override
	@Transactional(readOnly = true)
	public CotaCobrancaVO obterDadosCotaCobranca(Cota cota) {
		ParametroCobrancaCota parametrosCobranca = cota.getParametroCobranca();
		
		CotaCobrancaVO cotaCobrancaVO = null;
		if (cota!=null){
			cotaCobrancaVO = new CotaCobrancaVO(); 
			
			cotaCobrancaVO.setNumCota(cota.getNumeroCota());
			
			//combo !!
			cotaCobrancaVO.setTipoCobranca(parametrosCobranca.getFormaCobranca().getTipoCobranca());
			
			//combo !!
			
			cotaCobrancaVO.setIdBanco(parametrosCobranca.getFormaCobranca().getBanco().getId());
			cotaCobrancaVO.setRecebeEmail(parametrosCobranca.isRecebeCobrancaEmail());
			
			//verificar se existem estes campos no banco de dados.
			cotaCobrancaVO.setNumBanco("000000");//!!!
			cotaCobrancaVO.setNomeBanco("NOME BANCO");//!!!
			cotaCobrancaVO.setAgencia("AGENCIA");//!!!
			cotaCobrancaVO.setAgenciaDigito("0");//!!!
			cotaCobrancaVO.setConta("00000");//!!!
			cotaCobrancaVO.setContaDigito("0");//!!!
			
			cotaCobrancaVO.setFatorVencimento(parametrosCobranca.getFatorVencimento());
			cotaCobrancaVO.setSugereSuspensao(cota.isSugereSuspensao());
			cotaCobrancaVO.setContrato(cota.getContratoCota()==null);
			
			Set<ConcentracaoCobrancaCota> concentracaoCobrancas = parametrosCobranca.getConcentracaoCobrancaCota();
			if ((concentracaoCobrancas!=null)&&(concentracaoCobrancas.size() > 0)){
				for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracaoCobrancas){
					
					DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
					cotaCobrancaVO.setDomingo(dia == DiaSemana.DOMINGO);
					cotaCobrancaVO.setSegunda(dia == DiaSemana.SEGUNDA_FEIRA);
					cotaCobrancaVO.setTerca(dia == DiaSemana.TERCA_FEIRA);
				    cotaCobrancaVO.setQuarta(dia == DiaSemana.QUARTA_FEIRA);
					cotaCobrancaVO.setQuinta(dia == DiaSemana.QUINTA_FEIRA);
					cotaCobrancaVO.setSexta(dia == DiaSemana.SEXTA_FEIRA);
					cotaCobrancaVO.setSabado(dia == DiaSemana.SABADO);
					
				}
			}
			
			cotaCobrancaVO.setValorMinimo(parametrosCobranca.getFormaCobranca().getValorMinimoEmissao());
			
			cotaCobrancaVO.setComissao(BigDecimal.ZERO);//!!!
			
			cotaCobrancaVO.setQtdDividasAberto(parametrosCobranca.getPoliticaSuspensao().getNumeroAcumuloDivida());
			
			cotaCobrancaVO.setVrDividasAberto(BigDecimal.ZERO);//!!!
		}
		
		return cotaCobrancaVO;
	}
	
	
	
	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = bancoRepository.obterBancosPorTipoDeCobranca(tipoCobranca);
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+" - "+itemBanco.getNome()));
		}
		return comboBancos;
	}
	
	
	 /**
	  * Método responsável por obter tipos de cobrança para preencher combo da camada view
	  * @return comboTiposPagamento: Tipos de cobrança padrão.
	  */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<TipoCobranca, String>> getComboTiposCobranca() {
		List<ItemDTO<TipoCobranca,String>> comboTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
		for (TipoCobranca itemTipoCobranca: TipoCobranca.values()){
			comboTiposCobranca.add(new ItemDTO<TipoCobranca,String>(itemTipoCobranca, itemTipoCobranca.getDescTipoCobranca()));
		}
		return comboTiposCobranca;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public void postarDadosCotaCobranca(CotaCobrancaVO cotaCobranca) {
		
		

		//COTA
		Cota cota = this.obterPorNumeroDaCota(cotaCobranca.getNumCota());
		

		
		//PARAMETROS DE COBRANCA DA COTA
		ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
		PoliticaSuspensao politicaSuspensao=null;
		FormaCobranca formaCobranca = null;
		
		
		
		
		if (parametroCobranca==null){
			parametroCobranca = new ParametroCobrancaCota();
		}
		else{
			
			
			
			
			//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
			politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			if (politicaSuspensao==null){
				politicaSuspensao = new PoliticaSuspensao();
			}
			politicaSuspensao.setNumeroAcumuloDivida(cotaCobranca.getQtdDividasAberto());
			
			
			
			
			//FORMA DE COBRANCA DO PARAMETRO DE COBRANCA DA COTA
			formaCobranca = parametroCobranca.getFormaCobranca();
			if (formaCobranca==null){
				formaCobranca=new FormaCobranca();
			}
			formaCobranca.setValorMinimoEmissao(cotaCobranca.getValorMinimo());
			formaCobranca.setTipoCobranca(cotaCobranca.getTipoCobranca());
			formaCobranca.setBanco(this.bancoRepository.buscarPorId(cotaCobranca.getIdBanco()));
			//BigDecimal comissao;
			//BigDecimal vrDividasAberto;
			
			
			
		}

		
		
		
		
		parametroCobranca.setRecebeCobrancaEmail(cotaCobranca.isRecebeEmail());
		parametroCobranca.setFatorVencimento((int) cotaCobranca.getFatorVencimento());
		//parametroCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		
		parametroCobranca.setFormaCobranca(formaCobranca);
		parametroCobranca.setPoliticaSuspensao(politicaSuspensao);
		parametroCobranca.setValorMininoCobranca(cotaCobranca.getValorMinimo());
		
		//String numBanco;
		//String nomeBanco;
		//String agencia;
		//String agenciaDigito;
		//String conta;
		//String contaDigito;
		

		
		
		

		cota.setParametroCobranca(parametroCobranca);
		cota.setSugereSuspensao(cotaCobranca.isSugereSuspensao());
		//boolean contrato;
		this.cotaRepository.merge(cota);

		
		
		
		
		
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		ConcentracaoCobrancaCota concentracaoCobranca;
		if (cotaCobranca.isDomingo()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isSegunda()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isTerca()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isQuarta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isQuinta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isSexta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		if (cotaCobranca.isSabado()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
		}
		
	}
	

}
