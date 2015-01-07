package br.com.abril.nds.controllers.cadastro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO.ColunaOrdenacao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.CanalDistribuicao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TipoFornecedorService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller para a tela de cadastro de fornecedores.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/fornecedor")
@Rules(Permissao.ROLE_CADASTRO_FORNECEDOR)
public class FornecedorController extends BaseController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoFornecedorService tipoFornecedorService;
	
	@Autowired
	private BancoService bancoService;
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoFornecedor";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoFornecedor";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoFornecedor";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoFornecedor";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoFornecedor";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoFornecedor";

	
	
	@Path("/")
	public void index() {

		this.result.include("combo", this.tipoFornecedorService.obterComboTipoFornecedor());
		
		this.result.include("listaBancos", this.bancoService.getComboBancos(true));
		
		this.result.include("canaisDistribuicao", CanalDistribuicao.values());
	}

	/**
	 * 
	 * @param filtroConsultaFornecedor
	 */
	@Post
	public void pesquisarFornecedores(FiltroConsultaFornecedorDTO filtroConsultaFornecedor,
									  int page, int rp, String sortname, String sortorder) {
		
		filtroConsultaFornecedor = prepararFiltroFornecedor(filtroConsultaFornecedor, page, sortname, sortorder, rp);
		
		filtroConsultaFornecedor.setNomeFantasia(PessoaUtil.removerSufixoDeTipo(filtroConsultaFornecedor.getNomeFantasia()));
		filtroConsultaFornecedor.setRazaoSocial(PessoaUtil.removerSufixoDeTipo(filtroConsultaFornecedor.getRazaoSocial()));
		
		Long quantidadeRegistros =
				this.fornecedorService.obterContagemFornecedoresPorFiltro(filtroConsultaFornecedor);		
		
		if (quantidadeRegistros <=0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorService.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		TableModel<CellModelKeyValue<FornecedorDTO>> tableModel = obterTableModelFornecedores(listaFornecedores);
		
		tableModel.setTotal(quantidadeRegistros.intValue());
		
		tableModel.setPage(page);

		this.result.use(Results.json()).from(tableModel).recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_CADASTRO_FORNECEDOR_ALTERACAO)
	public void excluirFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inexistente."); 
		}
		
		this.fornecedorService.removerFornecedor(idFornecedor);
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Exclusão realizada com sucesso.");

		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	private Origem obterOrigemFornecedor(FornecedorDTO fornecedorDTO) {

		Origem origemFornecedor = null;
		
		if(fornecedorDTO.getIdFornecedor()!=null) {
			
			Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(fornecedorDTO.getIdFornecedor());	
			
			if(fornecedor == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Falha na edição do registro de Fornecedor.");
			}
			
			origemFornecedor = fornecedor.getOrigem();
			
		} else {
			
			origemFornecedor = fornecedorDTO.getOrigem() == null ? Origem.MANUAL : fornecedorDTO.getOrigem(); 
		}
		
		return origemFornecedor;
		
	}
	
	@Post
	@Rules(Permissao.ROLE_CADASTRO_FORNECEDOR_ALTERACAO)
	public void cadastrarFornecedor(FornecedorDTO fornecedorDTO) {
		
		Origem origemFornecedor = obterOrigemFornecedor(fornecedorDTO);
		
		this.validarDadosCadastrais(fornecedorDTO, origemFornecedor);
		
		Fornecedor fornecedor = null;
		
		if(Origem.MANUAL.equals(origemFornecedor)) {
			
			fornecedor = criarObjetoFornecedorOrigemManual(fornecedorDTO);
		} else {
			
			fornecedor = criarObjetoFornecedorOrigemInterface(fornecedorDTO);
		}
		
		fornecedor.setCanalDistribuicao(fornecedorDTO.getCanalDistribuicao());
		
		String mensagemSucesso;
		
		if (fornecedorDTO.getIdFornecedor() != null) {
			
			this.fornecedorService.merge(fornecedor);

			mensagemSucesso = "Operação realizada com sucesso.";
		
		} else {
			
			fornecedor.setSituacaoCadastro(SituacaoCadastro.ATIVO);
			
			this.fornecedorService.salvarFornecedor(fornecedor);
			
			mensagemSucesso = "Cadastro realizado com sucesso.";
		}
		
		processarEnderecosFornecedor(fornecedor.getId());
		
		processarTelefonesFornecedor(fornecedor.getId());

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso), "result").recursive().serialize();
	}
	
	private void validarDadosCadastrais(FornecedorDTO fornecedorDTO, Origem origem){
		
		List<String> mensagens = new ArrayList<String>();
		
		if(Origem.MANUAL.equals(origem)) {
			
			if(fornecedorDTO.getInscricaoEstadual() == null || fornecedorDTO.getInscricaoEstadual().isEmpty()) {
				mensagens.add("O preenchimento do campo [Inscrição Estadual] é obrigatório.");
			}
			
			if (fornecedorDTO.getCodigoInterface() == null) {
				
				mensagens.add("O preenchimento do campo [Codigo] é obrigatório.");
			
			} else {
				
				if(	fornecedorDTO.getCodigoInterface() > Constantes.MAX_CODIGO_INTERFACE_FORNCECEDOR_MANUAL) {
					
					mensagens.add(	" Valor do campo [Codigo] não deve exceder "+ Constantes.MAX_CODIGO_INTERFACE_FORNCECEDOR_MANUAL + 
									" para fornecedor de origem MANUAL.");
					
				} else {
					
					Fornecedor fornecedor = fornecedorService.obterFornecedorPorCodigoInterface(fornecedorDTO.getCodigoInterface());
					
					if(fornecedor!=null && !fornecedor.getId().equals(fornecedorDTO.getIdFornecedor())) {
						mensagens.add(" Valor do campo [Codigo] já esta sendo utilizado.");
					}
					
				}
				
			}
			
			if (fornecedorDTO.getRazaoSocial() == null || fornecedorDTO.getRazaoSocial().isEmpty()) {
				
				mensagens.add("O preenchimento do campo [Razao Social] é obrigatório.");
			}
			
			CNPJValidator cnpjValidator = new CNPJValidator(true);
			
			if (fornecedorDTO.getCnpj() == null || fornecedorDTO.getCnpj().isEmpty()) {
				
				mensagens.add("O preenchimento do campo [CNPJ] é obrigatório.");
			
			} else {
				
				try{
					
					cnpjValidator.assertValid(fornecedorDTO.getCnpj());
				
				} catch(InvalidStateException e){
					
					mensagens.add("CNPJ inválido.");
				}
			}
			
			if(fornecedorDTO.getIdBanco() == null) {
				mensagens.add("O preenchimento do [Banco] é obrigatório.");
			}
			
			if (fornecedorDTO.getIdFornecedor() != null) {
				Fornecedor fornecedorCadastrado = fornecedorService.obterFornecedorPorId(fornecedorDTO.getIdFornecedor());
				if (fornecedorCadastrado != null) {
					String cnpjAlterado = fornecedorDTO.getCnpj().replaceAll("[/.-]", "");
					if (!fornecedorCadastrado.getJuridica().getCnpj().equals( cnpjAlterado )) {
						mensagens.add("Não é possível alterar o CNPJ do fornecedor cadastrado!");
					}
				}
			}
			
			if (!StringUtil.isEmpty(fornecedorDTO.getEmailNfe())) {
				if (!Util.validarEmail(fornecedorDTO.getEmailNfe())) {
					mensagens.add("O preenchimento do campo [E-mail NFe] está inválido!");
				}
			}
			
			if (!StringUtil.isEmpty(fornecedorDTO.getEmail())) {
				if (!Util.validarEmail(fornecedorDTO.getEmail())) {
					mensagens.add("O preenchimento do campo [E-mail] está inválido!");
				}
			}
			
			if (fornecedorDTO.isPossuiContrato()) {
				
				if (fornecedorDTO.getValidadeContrato() == null || fornecedorDTO.getValidadeContrato().isEmpty()) {
				
					mensagens.add("O preenchimento do campo [Validade] é obrigatório.");

				} else {
				
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
					
					try {
						
						simpleDateFormat.parse(fornecedorDTO.getValidadeContrato());
						
					} catch (ParseException e) {
		
						mensagens.add("O preenchimento do campo [Validade] não é válido.");
					}
				}
			}

			String cnpj = "";
			
			if (fornecedorDTO.getCnpj() != null && !fornecedorDTO.getCnpj().isEmpty()) {
				cnpj = fornecedorDTO.getCnpj().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
			}
			
			PessoaJuridica juridica = 
					this.pessoaJuridicaService.buscarPorCnpj(cnpj);
			
			if (juridica != null) {
				
				boolean juridicaCadastrada = 
						this.fornecedorService.isPessoaJaCadastrada(juridica.getId(), fornecedorDTO.getIdFornecedor());
				
				if (juridicaCadastrada) {
					
					mensagens.add("Pessoa Jurídica já cadastrada para outro fornecedor.");
				}	
			}
			
			this.validaTelefones(mensagens);

	        this.validarEnderecos(mensagens);
			
		} else {
			
			if(fornecedorDTO.getIdBanco() == null) {
				mensagens.add("O preenchimento do [Banco] é obrigatório.");
			}
		}
		
		if (fornecedorDTO.getCanalDistribuicao() == null){
			
			mensagens.add("O preenchimento do [Canal Distribuição] é obrigatório.");
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	@Post
	public void obterPessoaJuridica(String cnpj) {
		
		cnpj = Util.removerMascaraCnpj( cnpj);
		
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
		}
		
		this.result.use(Results.json()).from(pessoaJuridica, "result").serialize();		
	}
	
	/**
	 * Obtem os endereços referentes ao fornecedor informado e salva em sessao
	 * 
	 * @param idFornecedor - identificador do fornecedor
	 */
	private void obterEndereco(Long idFornecedor){
		
		Boolean enderecoPendente = (Boolean) this.session.getAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		if (enderecoPendente==null || !enderecoPendente){
			
			List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.fornecedorService.obterEnderecosFornecedor(idFornecedor);
		
			this.session.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
		}
	}

	@Post
	public void editarFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inválido.");
		}

		Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(idFornecedor);

		if (fornecedor == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inválido.");
		}
		
		limparSessao();

		obterEndereco(idFornecedor);

		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = 
				this.fornecedorService.obterTelefonesFornecedor(idFornecedor);

		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();

		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao) {

			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}

		this.session.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, map);
		
		FornecedorDTO fornecedorDTO = criarFornecedorDTO(fornecedor);

		this.result.use(Results.json()).from(fornecedorDTO, "result").recursive().serialize();
	}
	
	private void limparSessao() {
		
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);		
	}

	@Post
	@Rules(Permissao.ROLE_CADASTRO_FORNECEDOR_ALTERACAO)
	public void novoCadastro() {

		limparDadosSessao();
		
		boolean utilizaSugestaoIncrementoCodigo = 
				this.distribuidorService.utilizaSugestaoIncrementoCodigo();
		
		Integer novoCodigoInterface = null;
		
		if(utilizaSugestaoIncrementoCodigo) {
			novoCodigoInterface = this.fornecedorService.obterMinCodigoInterfaceDisponivel();
		}
		
		Map<String, Object> mapa = new TreeMap<String, Object>(); 
		
		mapa.put("data", DateUtil.formatarDataPTBR(new Date()));
		
		if (novoCodigoInterface != null) {
			mapa.put("nextCodigo", String.format("%04d", novoCodigoInterface));
		}
		
		this.result.use(CustomJson.class).from(mapa).serialize();
	}
	
	/**
	 * Valida existência de endereço e existência de endereço principal
	 * @param mensagensValidacao
	 */
    private void validarEnderecos(List<String> mensagensValidacao) {
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = (List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecosExibir = (List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_EXIBICAO);

		List<EnderecoAssociacaoDTO> listaEnderecos = new ArrayList<EnderecoAssociacaoDTO>();
		if (listaEnderecosSalvar != null && !listaEnderecosSalvar.isEmpty()) {
			listaEnderecos.addAll(listaEnderecosSalvar);
		}
		
		if (listaEnderecosExibir != null && !listaEnderecosExibir.isEmpty()) {
			listaEnderecos.addAll(listaEnderecosExibir);
		}
		
		if (listaEnderecos.isEmpty()) {
			mensagensValidacao.add("Pelo menos um endereço deve ser cadastrado para o fornecedor.");
		} else {
			boolean temPrincipal = false;
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecos) {
				if (enderecoAssociacao.isEnderecoPrincipal()) {
					temPrincipal = true;
					break;
				}
			}
			if (!temPrincipal) {
				mensagensValidacao.add("Deve haver ao menos um endereço principal para o fornecedor.");
			}
		}
	}
    
    /**
	 * Valida existência de telefone e existência de telefone principal
	 * @param mensagensValidacao
	 */
    private void validaTelefones(List<String> mensagensValidacao){
    
    	Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();

		if (map.isEmpty()) {
			
			mensagensValidacao.add("Pelo menos um telefone deve ser cadastrado para o fornecedor.");
		
		} else {
			
			boolean temPrincipal = false;
			
			for (Entry<Integer, TelefoneAssociacaoDTO> entry : map.entrySet()){

				TelefoneAssociacaoDTO telefoneAssociacaoDTO = entry.getValue();
				
				if (telefoneAssociacaoDTO.isPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}
			
			if (!temPrincipal) {
				
				mensagensValidacao.add("Deve haver ao menos um telefone principal para o fornecedor.");
			}
		}
    }
	
	/*
	 * Método responsável por processar os endereços do entregador.
	 */
	@SuppressWarnings("unchecked")
	private void processarEnderecosFornecedor(Long idFornecedor) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_SALVAR_SESSAO);

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_REMOVER_SESSAO);

		this.fornecedorService.processarEnderecos(idFornecedor, 
												  listaEnderecoAssociacaoSalvar, 
												  listaEnderecoAssociacaoRemover);

		this.session.setAttribute(EnderecoController.ENDERECO_PENDENTE, Boolean.FALSE);
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
	}

	/*
	 * Método responsável por processar os telefones do entregador.
	 */
	private void processarTelefonesFornecedor(Long idFornecedor){

		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();

		List<TelefoneAssociacaoDTO> listaTelefoneAdicionar = new ArrayList<TelefoneAssociacaoDTO>();
		
		if (map != null){
			
			for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : map.values()){
			
				listaTelefoneAdicionar.add(telefoneAssociacaoDTO);
			}
		}

		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		
		this.fornecedorService.processarTelefones(idFornecedor, listaTelefoneAdicionar, telefonesRemover);

		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
	}
	
	/*
	 * Método que obtém os telefones a serem salvos, que estão na sessão.
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.session.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){

			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}
	
	/*
	 * Método que obtém os telefones a serem removidos, que estão na sessão.
	 */
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.session.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);

		if (telefonesSessao == null){
			telefonesSessao = new HashSet<Long>();
		}

		return telefonesSessao;
	}
	
	private Fornecedor criarObjetoFornecedorOrigemInterface(FornecedorDTO fornecedorDTO){
		
		Fornecedor fornecedor = null;
		
		if(Origem.INTERFACE.equals(fornecedorDTO.getOrigem())){
			
			fornecedor = this.fornecedorService.obterFornecedorPorId(fornecedorDTO.getIdFornecedor());
			
			if(fornecedor == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao editar forncedor de origem ["+Origem.INTERFACE.name()+"].");
			}
			
			Banco banco = null;
			
			if (fornecedorDTO.getIdBanco() != null) {
				banco = this.bancoService.obterBancoPorId(fornecedorDTO.getIdBanco());
			}
			
			fornecedor.setBanco(banco);
			
		}
		
		return fornecedor;
		
	}
	
	private Fornecedor criarObjetoFornecedorOrigemManual(FornecedorDTO fornecedorDTO) {
		
		Fornecedor fornecedor = null;
		
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaService.buscarPorCnpj(Util.removerMascaraCnpj( fornecedorDTO.getCnpj()));
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
			
			pessoaJuridica.setCnpj(Util.removerMascaraCnpj( fornecedorDTO.getCnpj()));
		}
		
		pessoaJuridica.setEmail(fornecedorDTO.getEmail());
		
		pessoaJuridica.setInscricaoEstadual(fornecedorDTO.getInscricaoEstadual());
		
		pessoaJuridica.setNomeFantasia(fornecedorDTO.getNomeFantasia());
		
		pessoaJuridica.setRazaoSocial(fornecedorDTO.getRazaoSocial());
		
		pessoaJuridica = this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
		
		if (fornecedorDTO.getIdFornecedor() != null) {
		
			fornecedor = this.fornecedorService.obterFornecedorPorId(fornecedorDTO.getIdFornecedor());

		} else {

			fornecedor = new Fornecedor();
			
			fornecedor.setInicioAtividade(new Date());
		}
		
		TipoFornecedor tipoFornecedor = null;
		
		if (fornecedorDTO.getTipoFornecedor()!=null) {

			tipoFornecedor = this.tipoFornecedorService.obterTipoFornecedorPorId(fornecedorDTO.getTipoFornecedor());
		}
		
		fornecedor.setTipoFornecedor(tipoFornecedor);
		
		Banco banco = null;
		
		if (fornecedorDTO.getIdBanco() != null) {
			banco = this.bancoService.obterBancoPorId(fornecedorDTO.getIdBanco());
		}

		fornecedor.setBanco(banco);
		
		fornecedor.setTipoFornecedor(tipoFornecedor);
		
		fornecedor.setEmailNfe(fornecedorDTO.getEmailNfe());
		
		fornecedor.setCodigoInterface(fornecedorDTO.getCodigoInterface());
		
		fornecedor.setValidadeContrato(DateUtil.parseDataPTBR(fornecedorDTO.getValidadeContrato()));
		
		fornecedor.setResponsavel(fornecedorDTO.getResponsavel());
		
		fornecedor.setPossuiContrato(fornecedorDTO.isPossuiContrato());
		
		fornecedor.setJuridica(pessoaJuridica);
		
		fornecedor.setIntegraGFS(fornecedorDTO.isIntegraGFS());
		
		Origem origem = fornecedorDTO.getOrigem() == null ? Origem.MANUAL : fornecedorDTO.getOrigem(); 
		
		fornecedor.setOrigem(origem);
		
		return fornecedor;
	}
	
	private FornecedorDTO criarFornecedorDTO(Fornecedor fornecedor) {
		
		FornecedorDTO fornecedorDTO = new FornecedorDTO();
		
		fornecedorDTO.setCnpj(fornecedor.getJuridica().getCnpj());
		
		fornecedorDTO.setCodigoInterface(fornecedor.getCodigoInterface());
		
		fornecedorDTO.setEmail(fornecedor.getJuridica().getEmail());
		
		fornecedorDTO.setEmailNfe(fornecedor.getEmailNfe());
		
		fornecedorDTO.setIdFornecedor(fornecedor.getId());
		
		fornecedorDTO.setInicioAtividade(DateUtil.formatarDataPTBR(fornecedor.getInicioAtividade()));
		
		fornecedorDTO.setInscricaoEstadual(fornecedor.getJuridica().getInscricaoEstadual());
		
		fornecedorDTO.setNomeFantasia(fornecedor.getJuridica().getNomeFantasia());
		
		fornecedorDTO.setPossuiContrato(fornecedor.isPossuiContrato());
		
		fornecedorDTO.setRazaoSocial(fornecedor.getJuridica().getRazaoSocial());
		
		fornecedorDTO.setResponsavel(fornecedor.getResponsavel());
		
		fornecedorDTO.setOrigem(fornecedor.getOrigem());
		
		if (fornecedor.getTipoFornecedor() != null) {

			fornecedorDTO.setTipoFornecedor(fornecedor.getTipoFornecedor().getId());
		}
		
		if (fornecedor.getBanco() != null) {

			fornecedorDTO.setIdBanco(fornecedor.getBanco().getId());
		}
		
		fornecedorDTO.setValidadeContrato(DateUtil.formatarDataPTBR(fornecedor.getValidadeContrato()));
		
		fornecedorDTO.setCanalDistribuicao(fornecedor.getCanalDistribuicao());
		
		fornecedorDTO.setIntegraGFS(fornecedor.isIntegraGFS());
		
		return fornecedorDTO;
	}
	
	/*
	 * Método responsável por compor um TableModel para grid de fornecedores. 
	 */
	private TableModel<CellModelKeyValue<FornecedorDTO>> obterTableModelFornecedores(List<FornecedorDTO> listaFornecedores) {
		
		TableModel<CellModelKeyValue<FornecedorDTO>> tableModel = new TableModel<CellModelKeyValue<FornecedorDTO>>();
		
		List<CellModelKeyValue<FornecedorDTO>> listaCellModel = new ArrayList<CellModelKeyValue<FornecedorDTO>>();
		
		for (FornecedorDTO fornecedorDTO : listaFornecedores) {
			
			CellModelKeyValue<FornecedorDTO> cellModelKeyValue = new CellModelKeyValue<FornecedorDTO>(
				fornecedorDTO.getIdFornecedor().intValue(), 
				fornecedorDTO
			);

			listaCellModel.add(cellModelKeyValue);
		}
		
		tableModel.setRows(listaCellModel);
		
		return tableModel;
	}
	
	/*
	 * Método que prepara o filtro para utilização na pesquisa.
	 */
	private FiltroConsultaFornecedorDTO prepararFiltroFornecedor(
					FiltroConsultaFornecedorDTO filtroFornecedor, 
					int page, String sortname, String sortorder, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtroFornecedor.setPaginacao(paginacao);
		
		filtroFornecedor.setColunaOrdenacao(
				Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));

		return filtroFornecedor;
	}
	
	@Post
	public void cancelarCadastro(){
		
		this.limparDadosSessao();
		
		result.use(Results.json()).from("", "result").serialize();
	}

	private void limparDadosSessao() {
		
		this.session.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		this.session.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		this.session.removeAttribute(EnderecoController.ENDERECO_PENDENTE);
	}
	
}
