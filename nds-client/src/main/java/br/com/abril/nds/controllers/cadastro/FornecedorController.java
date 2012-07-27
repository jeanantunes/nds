package br.com.abril.nds.controllers.cadastro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO.ColunaOrdenacao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TipoFornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
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
public class FornecedorController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private TipoFornecedorService tipoFornecedorService;
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoFornecedor";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoFornecedor";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoFornecedor";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoFornecedor";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoFornecedor";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoFornecedor";

	@Path("/")
	public void index() {

		obterTiposFornecedor();
	}

	/**
	 * 
	 * @param filtroConsultaFornecedor
	 */
	@Post
	public void pesquisarFornecedores(FiltroConsultaFornecedorDTO filtroConsultaFornecedor,
									  int page, int rp, String sortname, String sortorder) {
		
		filtroConsultaFornecedor = prepararFiltroFornecedor(filtroConsultaFornecedor, page, sortname, sortorder, rp);
		
		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorService.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		if (listaFornecedores == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		Long quantidadeRegistros =
				this.fornecedorService.obterContagemFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		TableModel<CellModelKeyValue<FornecedorDTO>> tableModel = obterTableModelFornecedores(listaFornecedores);
		
		tableModel.setTotal(quantidadeRegistros.intValue());
		
		tableModel.setPage(page);

		this.result.use(Results.json()).from(tableModel).recursive().serialize();
	}
	
	@Post
	public void excluirFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inexistente."); 
		}
		
		this.fornecedorService.removerFornecedor(idFornecedor);
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Exclusão realizada com sucesso.");

		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	@Post
	public void cadastrarFornecedor(FornecedorDTO fornecedorDTO) {

		validarFornecedorDTO(fornecedorDTO);
		
		Fornecedor fornecedor = criarFornecedor(fornecedorDTO);
		
		String mensagemSucesso = "Cadastro realizado com sucesso.";
		
		if (fornecedorDTO.getIdFornecedor() != null) {
			
			this.fornecedorService.merge(fornecedor);

			mensagemSucesso = "Edição realizada com sucesso.";
		
		} else {
			
			fornecedor.setSituacaoCadastro(SituacaoCadastro.ATIVO);
			
			this.fornecedorService.salvarFornecedor(fornecedor);
		}
		
		processarEnderecosFornecedor(fornecedor.getId());
		
		processarTelefonesFornecedor(fornecedor.getId());

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso), "result").recursive().serialize();
	}
	
	@Post
	public void obterPessoaJuridica(String cnpj) {
		
		cnpj = cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
		
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
		}
		
		this.result.use(Results.json()).from(pessoaJuridica, "result").serialize();		
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
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.fornecedorService.obterEnderecosFornecedor(idFornecedor);

		this.session.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, listaEnderecoAssociacao);

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
	
	@Post
	public void novoCadastro() {

		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.result.use(Results.json()).from(DateUtil.formatarDataPTBR(new Date()), "result").serialize();
	}
	
	private void obterTiposFornecedor() {

		List<ComboTipoFornecedorDTO> combo = this.tipoFornecedorService.obterComboTipoFornecedor();

		this.result.include("combo", combo);
	}
	
	private void validarFornecedorDTO(FornecedorDTO fornecedorDTO) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if (fornecedorDTO.getCodigoInterface() == null) {
			
			mensagens.add("O preenchimento do campo [Codigo] é obrigatório.");
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
		
		if (fornecedorDTO.getTipoFornecedor() == null) {
			
			mensagens.add("O preenchimento do campo [Tipo Fornecedor] é obrigatório.");
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

		String cnpj = fornecedorDTO.getCnpj().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
		
		PessoaJuridica juridica = 
				this.pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if (juridica != null) {
			
			boolean juridicaCadastrada = 
					this.fornecedorService.isPessoaJaCadastrada(juridica.getId(), fornecedorDTO.getIdFornecedor());
			
			if (juridicaCadastrada) {
				
				mensagens.add("Pessoa Jurídica já cadastrada para outro fornecedor.");
			}	
		}
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_SALVAR_SESSAO);
		
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		if (map.keySet().isEmpty()) {
			
			mensagens.add("Pelo menos um telefone deve ser cadastrado para o entregador.");
		
		} else {
			
			boolean temPrincipal = false;
			
			for (Integer key : map.keySet()){

				TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
				
				if (telefoneAssociacaoDTO.isPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}
			
			if (!temPrincipal) {
				
				mensagens.add("Deve haver ao menos um telefone principal para o entregador.");
			}
		}
		
		if (listaEnderecoAssociacaoSalvar == null || listaEnderecoAssociacaoSalvar.isEmpty()) {
			
			mensagens.add("Pelo menos um endereço deve ser cadastrado para o entregador.");
		
		} else {
			
			boolean temPrincipal = false;
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacaoSalvar) {
				
				if (enderecoAssociacao.isEnderecoPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}

			if (!temPrincipal) {
				
				mensagens.add("Deve haver ao menos um endereço principal para o entregador.");
			}
		}

		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
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

		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
	}

	/*
	 * Método responsável por processar os telefones do entregador.
	 */
	private void processarTelefonesFornecedor(Long idFornecedor){

		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();

		List<TelefoneFornecedor> lista = new ArrayList<TelefoneFornecedor>();

		for (Integer key : map.keySet()){

			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);

			if (telefoneAssociacaoDTO.getTipoTelefone() != null){
				
				TelefoneFornecedor telefoneFornecedor = new TelefoneFornecedor();
				telefoneFornecedor.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
				telefoneFornecedor.setTelefone(telefoneAssociacaoDTO.getTelefone());
				telefoneFornecedor.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());

				if (key > 0) {

					telefoneFornecedor.setId(key.longValue());
				}
				
				lista.add(telefoneFornecedor);
			}
		}

		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		this.fornecedorService.processarTelefones(idFornecedor, lista, telefonesRemover);

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
	
	private Fornecedor criarFornecedor(FornecedorDTO fornecedorDTO) {
		
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaService.buscarPorCnpj(fornecedorDTO.getCnpj());
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
			
			pessoaJuridica.setCnpj(fornecedorDTO.getCnpj());
		}
		
		pessoaJuridica.setEmail(fornecedorDTO.getEmail());
		
		pessoaJuridica.setInscricaoEstadual(fornecedorDTO.getInscricaoEstadual());
		
		pessoaJuridica.setNomeFantasia(fornecedorDTO.getNomeFantasia());
		
		pessoaJuridica.setRazaoSocial(fornecedorDTO.getRazaoSocial());
		
		pessoaJuridica = this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
		
		Fornecedor fornecedor = null;
		
		if (fornecedorDTO.getIdFornecedor() != null) {
		
			fornecedor = this.fornecedorService.obterFornecedorPorId(fornecedorDTO.getIdFornecedor());

		} else {

			fornecedor = new Fornecedor();
			
			fornecedor.setInicioAtividade(new Date());
		}
		
		TipoFornecedor tipoFornecedor = 
				this.tipoFornecedorService.obterTipoFornecedorPorId(fornecedorDTO.getTipoFornecedor());

		fornecedor.setTipoFornecedor(tipoFornecedor);
		
		fornecedor.setEmailNfe(fornecedorDTO.getEmailNfe());
		
		fornecedor.setCodigoInterface(fornecedorDTO.getCodigoInterface());
		
		fornecedor.setValidadeContrato(DateUtil.parseDataPTBR(fornecedorDTO.getValidadeContrato()));
		
		fornecedor.setResponsavel(fornecedorDTO.getResponsavel());
		
		fornecedor.setPossuiContrato(fornecedorDTO.isPossuiContrato());
		
		fornecedor.setJuridica(pessoaJuridica);
		
		Origem origem = fornecedorDTO.getOrigem() == null ? Origem.MANUAL : fornecedorDTO.getOrigem(); 
		
		fornecedor.setOrigem(origem);
		
		return fornecedor;
	}
	
	private FornecedorDTO criarFornecedorDTO(Fornecedor fornecedor) {
		
		FornecedorDTO fornecedorDTO = new FornecedorDTO();
		
		fornecedorDTO.setCnpj(fornecedor.getJuridica().getCnpj());
		
		fornecedorDTO.setCodigoInterface(fornecedor.getCodigoInterface());
		
		fornecedorDTO.setEmail(fornecedor.getJuridica().getEmail());
		
		fornecedorDTO.setIdFornecedor(fornecedor.getId());
		
		fornecedorDTO.setInicioAtividade(DateUtil.formatarDataPTBR(fornecedor.getInicioAtividade()));
		
		fornecedorDTO.setInscricaoEstadual(fornecedor.getJuridica().getInscricaoEstadual());
		
		fornecedorDTO.setNomeFantasia(fornecedor.getJuridica().getNomeFantasia());
		
		fornecedorDTO.setPossuiContrato(fornecedor.isPossuiContrato());
		
		fornecedorDTO.setRazaoSocial(fornecedor.getJuridica().getRazaoSocial());
		
		fornecedorDTO.setResponsavel(fornecedor.getResponsavel());
		
		fornecedorDTO.setTipoFornecedor(fornecedor.getTipoFornecedor().getId());
		
		fornecedorDTO.setValidadeContrato(DateUtil.formatarDataPTBR(fornecedor.getValidadeContrato()));
		
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
}
