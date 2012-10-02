package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoPaginacaoVO;
import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoVO;
import br.com.abril.nds.client.vo.EntregadorPessoaFisicaVO;
import br.com.abril.nds.client.vo.EntregadorPessoaJuridicaVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProcuracaoCotaDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoWrapper;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO.OrdenacaoColunaEntregador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.PessoaFisicaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller para a tela de cadastro de entregadores.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/entregador")
public class EntregadorController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EntregadorService entregadorService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String CPF = "CPF";
	
	private static final String CNPJ = "CNPJ";
	
	private static final String NOME_ARQUIVO_PROCURACAO = "procuracao";
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoEntregador";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoEntregador";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoEntregador";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoEntregador";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoEntregador";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoEntregador";
	
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_ENTREGADOR)
	public void index() { }
	
	/**
	 * Método responsável por popular a grid de Entregadores.
	 * 
	 * @param filtroEntregador - Filtro utilizado na pesquisa.
	 *  
	 * @param page - Número da página atual da grid.
	 */
	public void pesquisarEntregadores(
			FiltroEntregadorDTO filtroEntregador, int page, int rp, 
			String sortname, String sortorder) {

		filtroEntregador = prepararFiltroEntregador(filtroEntregador, page, sortname, sortorder, rp);

		List<Entregador> listaEntregador = this.entregadorService.obterEntregadoresPorFiltro(filtroEntregador);
		
		if (listaEntregador == null || listaEntregador.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro foi encontrado.");
		}

		Long quantidadeRegistrosEntregadores = this.entregadorService.obterContagemEntregadoresPorFiltro(filtroEntregador);

		TableModel<CellModel> tableModelEntregador = obterTableModel(listaEntregador);

		tableModelEntregador.setPage(page);

		tableModelEntregador.setTotal(quantidadeRegistrosEntregadores.intValue());

		this.result.use(Results.json()).withoutRoot().from(tableModelEntregador).recursive().serialize();
	}
	
	/**
	 * Método que realiza o cadastro de um entregador com papel de Pessoa Física.
	 * 
	 * @param idEntregador
	 * 
	 * @param codigoEntregador
	 * 
	 * @param isComissionado
	 * 
	 * @param percentualComissao
	 * 
	 * @param procuracao
	 * 
	 * @param cpfEntregador
	 * 
	 * @param procuracaoEntregador
	 */
	public void cadastrarEntregadorPessoaFisica(Long idEntregador,
												Long codigoEntregador,
												boolean isComissionado,
												String percentualComissao,
												String cpfEntregador, 
												PessoaFisica pessoaFisica) {
		
		HashMap<String, String> cpf = new HashMap<String, String>();
		cpf.put(CPF, cpfEntregador);
		
		validarParametrosEntradaCadastroEntregadorPF( idEntregador, 
													  codigoEntregador, 
												      isComissionado, 
												      percentualComissao, 
												      cpf,
												      pessoaFisica.getRg());

		String mensagemSucesso = "Cadastro realizado com sucesso.";

		if (idEntregador != null) {

			mensagemSucesso = "Edição realizada com sucesso.";
		}

		cpfEntregador = cpfEntregador.replaceAll("\\.", "").replaceAll("-", "");

		pessoaFisica.setCpf(cpfEntregador);

		PessoaFisica pessoaFisicaExistente = this.pessoaFisicaService.buscarPorCpf(cpfEntregador);

		if (pessoaFisicaExistente != null) {

			pessoaFisica.setId(pessoaFisicaExistente.getId());
		}

		pessoaFisicaExistente = this.pessoaFisicaService.salvarPessoaFisica(pessoaFisica);

		if (this.entregadorService.isPessoaJaCadastrada(pessoaFisicaExistente.getId(), idEntregador)) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa física já cadastrada para outro entregador.");
		}
		
		Entregador entregador = null;
		
		if (idEntregador == null) {
			
			entregador = new Entregador();

			entregador.setInicioAtividade(new Date());
		
		} else {

			entregador = this.entregadorService.buscarPorId(idEntregador);
		}
		
		entregador.setPessoa(pessoaFisicaExistente);
		entregador.setCodigo(codigoEntregador);
		entregador.setComissionado(isComissionado);
		
		if (percentualComissao != null && !percentualComissao.isEmpty()) {

			entregador.setPercentualComissao(new BigDecimal(getValorSemMascara(percentualComissao)));
		}
		
		entregador = this.entregadorService.salvarEntregadorProcuracao(entregador, null);

		processarEnderecosEntregador(entregador.getId());

		processarTelefonesEntregador(entregador.getId());

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso);

		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	/**
	 * Método que realiza o cadastro de um entregador com papel de Pessoa Juridica.
	 * 
	 * @param idEntregador
	 * 
	 * @param codigoEntregador
	 * 
	 * @param isComissionado
	 * 
	 * @param percentualComissao
	 * 
	 * @param procuracao
	 * 
	 * @param cnpjEntregador
	 * 
	 * @param procuracaoEntregador
	 */
	public void cadastrarEntregadorPessoaJuridica(Long idEntregador,
												  Long codigoEntregador,
												  boolean isComissionado,
												  String percentualComissao,
												  
												  String cnpjEntregador,
												  
												  PessoaJuridica pessoaJuridica) {
		
		HashMap<String, String> cnpj = new HashMap<String, String>();
		cnpj.put(CNPJ, cnpjEntregador);
		
		validarParametrosEntradaCadastroEntregadorPJ(
												   idEntregador,
												   codigoEntregador, 
												   isComissionado, 
												   percentualComissao, 
												   cnpj);

		String mensagemSucesso = "Cadastro realizado com sucesso.";

		if (idEntregador != null) {

			mensagemSucesso = "Edição realizada com sucesso.";
		}
		
		cnpjEntregador = cnpjEntregador.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");

		pessoaJuridica.setCnpj(cnpjEntregador);
		
		PessoaJuridica pessoaJuridicaExistente = this.pessoaJuridicaService.buscarPorCnpj(cnpjEntregador);
		
		if (pessoaJuridicaExistente != null) {
		
			pessoaJuridica.setId(pessoaJuridicaExistente.getId());
		}

		pessoaJuridicaExistente = this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
		
		if (this.entregadorService.isPessoaJaCadastrada(pessoaJuridicaExistente.getId(), idEntregador)) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa jurídica já cadastrada para outro entregador.");
		}
		
		Entregador entregador = null;

		if (idEntregador == null) {

			entregador = new Entregador();
			
			entregador.setInicioAtividade(new Date());

		} else {

			entregador = this.entregadorService.buscarPorId(idEntregador);
		}

		entregador.setCodigo(codigoEntregador);
		entregador.setComissionado(isComissionado);
		
		if (percentualComissao != null && !percentualComissao.isEmpty()) {

			entregador.setPercentualComissao(new BigDecimal(getValorSemMascara(percentualComissao )));
		}

		entregador.setPessoa(pessoaJuridicaExistente);

		entregador = this.entregadorService.salvarEntregadorProcuracao(entregador, null);

		processarEnderecosEntregador(entregador.getId());

		processarTelefonesEntregador(entregador.getId());

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso);

		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
		
		
	}

	/**
	 * 
	 * @param cpf
	 */
	public void obterPessoaFisica(String cpf) {
		
		cpf = cpf.replaceAll("\\.", "").replaceAll("-", "");
		
		PessoaFisica pessoaFisica = this.pessoaFisicaService.buscarPorCpf(cpf);
		
		if (pessoaFisica == null) {
			
			pessoaFisica = new PessoaFisica();
		}
		
		this.result.use(Results.json()).from(pessoaFisica, "result").serialize();
	}

	/**
	 * 
	 * @param cnpj
	 */
	public void obterPessoaJuridica(String cnpj) {
		
		cnpj = cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
		
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
		}
		
		this.result.use(Results.json()).from(pessoaJuridica, "result").serialize();
	}

	/**
	 * Método que prepara um Entregador para ser editado.
	 * 
	 * @param idEntregador - Id do entregador a ser editado.
	 */
	public void editarEntregador(Long idEntregador) {

		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);		

		ProcuracaoEntregador procuracaoEntregador = this.entregadorService.obterProcuracaoEntregadorPorId(idEntregador);
		
		if (procuracaoEntregador == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Entregador não encontrado.");
		}
		
		Entregador entregador = procuracaoEntregador.getEntregador();

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.entregadorService.obterEnderecosPorIdEntregador(idEntregador);

		this.session.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, listaEnderecoAssociacao);

		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = 
				this.entregadorService.buscarTelefonesEntregador(idEntregador);

		Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();

		for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao) {

			map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
		}

		this.session.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, map);

		if (entregador.getPessoa() instanceof PessoaFisica) {

			EntregadorPessoaFisicaVO entregadorPessoaFisica = new EntregadorPessoaFisicaVO();

			entregadorPessoaFisica.setEntregador(entregador);
			
			entregadorPessoaFisica.setDataNascimentoEntregadorFormatada(
					DateUtil.formatarDataPTBR(((PessoaFisica) entregador.getPessoa()).getDataNascimento()));
			
			entregadorPessoaFisica.setInicioAtividadeFormatada(DateUtil.formatarDataPTBR(entregador.getInicioAtividade()));
			
			entregadorPessoaFisica.setProcuracaoEntregador(entregador.getProcuracaoEntregador());

			entregadorPessoaFisica.setPessoaFisica((PessoaFisica) entregador.getPessoa());

			this.result.use(Results.json()).from(
					entregadorPessoaFisica, "result").include(
							"entregador", "procuracaoEntregador", "procuracaoEntregador.cota", "pessoaFisica").serialize();

		} else if (entregador.getPessoa() instanceof PessoaJuridica) {

			EntregadorPessoaJuridicaVO entregadorPessoaJuridica = new EntregadorPessoaJuridicaVO();

			entregadorPessoaJuridica.setEntregador(entregador);
			
			entregadorPessoaJuridica.setInicioAtividadeFormatada(DateUtil.formatarDataPTBR(entregador.getInicioAtividade()));

			entregadorPessoaJuridica.setProcuracaoEntregador(entregador.getProcuracaoEntregador());

			entregadorPessoaJuridica.setPessoaJuridica((PessoaJuridica) entregador.getPessoa());

			this.result.use(Results.json()).from(
					entregadorPessoaJuridica, "result").include(
							"entregador", "procuracaoEntregador", "procuracaoEntregador.cota", "pessoaJuridica").serialize();

		} else {

			this.result.nothing();
		}
	}

	/**
	 * Método responsável por remover um entregador a partir de seu ID.
	 * 
	 * @param idEntregador
	 */
	public void removerEntregador(Long idEntregador) {

		if (idEntregador == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Id do entregador não pode ser nulo.");
		}

		this.entregadorService.removerEntregadorPorId(idEntregador);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Exclusão realizada com sucesso.");

		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}

	/**
	 * Método que prepara a tela para um novo cadastro.
	 */
	public void novoCadastro() {
		
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		boolean utilizaSugestaoIncrementoCodigo = distribuidor.isUtilizaSugestaoIncrementoCodigo();
		
		Long novoCodigoEntregador = null;
		
		if(utilizaSugestaoIncrementoCodigo) {
			novoCodigoEntregador = this.entregadorService.obterMinCodigoEntregadorDisponivel();
		}
		
		this.result.use(CustomMapJson.class).put("data", DateUtil.formatarDataPTBR(new Date())).put("nextCodigo", novoCodigoEntregador).serialize();
		
	}
	
	/**
	 * Método que obtém uma cota através de seu número.
	 * 
	 * @param numeroCota
	 */
	public void obterCotaPorNumero(Integer numeroCota) {

		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inválida!");
		}
		
		EnderecoCota endereco = this.cotaService.obterEnderecoPrincipal(cota.getId());

		ProcuracaoCotaDTO procuracaoCota = parseCotaProcuracao(cota, endereco);

		this.result.use(Results.json()).from(procuracaoCota, "result").recursive().serialize();	
	}

	/**
	 * Renderiza o arquivo de impressão de dividas
	 * 
	 * @param procuracaoImpressao
	 * 
	 * @throws IOException
	 */
	public void imprimirProcuracao(ProcuracaoImpressaoDTO procuracaoImpressao) throws Exception {

//		validarDadosImpressao(procuracaoImpressao);

		List<ProcuracaoImpressaoDTO> list = new ArrayList<ProcuracaoImpressaoDTO>();
		
		list.add(procuracaoImpressao);
		
		ProcuracaoImpressaoWrapper procuracaoImpressaoWrapper = new ProcuracaoImpressaoWrapper();
		
		procuracaoImpressaoWrapper.setListaProcuracaoImpressao(list);
		
		List<ProcuracaoImpressaoWrapper> listaWrapper = new ArrayList<ProcuracaoImpressaoWrapper>();
		
		listaWrapper.add(procuracaoImpressaoWrapper);
		
		byte[] arquivo = this.entregadorService.getDocumentoProcuracao(listaWrapper);
		
		this.httpResponse.setContentType("application/pdf");

		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + NOME_ARQUIVO_PROCURACAO + ".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
	}
	
	public void validarDadosImpressao(ProcuracaoImpressaoDTO procuracaoImpressao) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (procuracaoImpressao.getNomeJornaleiro() == null || procuracaoImpressao.getNomeJornaleiro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Nome do Jornaleiro] é obrigatório.");
		}
		
		if (procuracaoImpressao.getBoxCota() == null || procuracaoImpressao.getBoxCota().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Box] é obrigatório.");
		}
		
		if (procuracaoImpressao.getNacionalidade() == null || procuracaoImpressao.getNacionalidade().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Nacionalidade do Entregador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getEstadoCivil() == null || procuracaoImpressao.getEstadoCivil().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Estado Civil do Entregador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getEnderecoPDV() == null || procuracaoImpressao.getEnderecoPDV().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Endereco do PDV] é obrigatório.");
		}
				
		if (procuracaoImpressao.getBairroPDV() == null || procuracaoImpressao.getBairroPDV().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Bairro do PDV] é obrigatório.");
		}
		
		if (procuracaoImpressao.getCidadePDV() == null || procuracaoImpressao.getCidadePDV().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Cidade do PDV] é obrigatório.");
		}
		
		if (procuracaoImpressao.getNumeroPermissao() == null || procuracaoImpressao.getNumeroPermissao().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Número da permissão] é obrigatório.");
		}

		if (procuracaoImpressao.getRgJornaleiro() == null || procuracaoImpressao.getRgJornaleiro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [RG do Jornaleiro] é obrigatório.");
		}
		
		if (procuracaoImpressao.getCpfJornaleiro() == null || procuracaoImpressao.getCpfJornaleiro().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [CPF do Jornaleiro] é obrigatório.");
		}
		
		if (procuracaoImpressao.getNomeProcurador() == null || procuracaoImpressao.getNomeProcurador().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Nome do Procurador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getRgProcurador() == null || procuracaoImpressao.getRgProcurador().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [RG do Procurador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getEstadoCivilProcurador() == null || procuracaoImpressao.getEstadoCivilProcurador().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Estado civil do procurador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getNacionalidadeProcurador() == null || procuracaoImpressao.getNacionalidadeProcurador().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Nacionalidade do Procurador] é obrigatório.");
		}
		
		if (procuracaoImpressao.getEnderecoDoProcurado() == null || procuracaoImpressao.getEnderecoDoProcurado().isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Endereco do Procurado] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	@Post
	public void carregarAbaCota(Long idEntregador){
		
		Object[] dados = new Object[5];
		
		if (idEntregador != null){
			
			Entregador entregador = this.entregadorService.buscarPorId(idEntregador);
			
			dados[0] = entregador.getPessoa().getNome();
			
			Rota rota = entregador.getRota();
			if (rota != null){
				
				dados[1] = rota.getRoteiro() != null ? rota.getRoteiro().getDescricaoRoteiro() : "";
				dados[2] = rota.getRoteiro() != null && rota.getRoteiro().getRoteirizacao().getBox() != null ? rota.getRoteiro().getRoteirizacao().getBox().getNome() : "";
				dados[3] = entregador.getRota() != null ? entregador.getRota().getDescricaoRota() : "";
			} else {
				
				dados[1] = "";
				dados[2] = "";
				dados[3] = "";
			}
			
			EntregadorCotaProcuracaoPaginacaoVO retorno = 
					this.entregadorService.buscarCotasAtendidas(idEntregador, 1, 15, 
					"numeroCota", "asc");
			
			TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>> tableModel = 
					new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(retorno.getListaVO()));
			
			tableModel.setTotal(retorno.getTotalRegistros());
			
			tableModel.setPage(1);
			
			dados[4] = tableModel;
		} else {
			
			dados[0] = "";
			dados[1] = "";
			dados[2] = "";
			dados[3] = "";
			dados[4] = new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
		}
		
		this.result.use(Results.json()).from(dados, "result").recursive().serialize();
	}
	
	public void atualizarGridCotas(Long idEntregador, int page, int rp, 
			String sortname, String sortorder){
		
		if (idEntregador == null){
			
			this.result.use(Results.json()).withoutRoot().from(new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>()).recursive().serialize();
		} else {
		
			EntregadorCotaProcuracaoPaginacaoVO retorno = 
					this.entregadorService.buscarCotasAtendidas(idEntregador, page, rp, 
					sortname, sortorder);
			
			TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>> tableModel = 
					new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(retorno.getListaVO()));
			
			tableModel.setTotal(retorno.getTotalRegistros());
			
			tableModel.setPage(page);
			
			this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}
	
	private void validarParametrosEntradaCadastroEntregadorPF(
			Long idEntregador,
			Long codigoEntregador,
			boolean isComissionado,
			String percentualComissao,
			HashMap<String, String> cpfCnpj,
			String rg) {
		
		
		List<String> listaMensagens = new ArrayList<String>();
		ValidacaoVO validacao = new ValidacaoVO();
		validacao.setTipoMensagem(TipoMensagem.WARNING);
		validacao.setListaMensagens(listaMensagens);
		
		if (rg != null && rg.length() >  PessoaUtil.RG_QUANTIDADE_DIGITOS) {
			
			listaMensagens.add("Quantidade de caracteres do campo [RG] excede o maximo de " + PessoaUtil.RG_QUANTIDADE_DIGITOS + " dígitos");
		}
		
		validarParametrosEntradaCadastroEntregador(validacao, idEntregador, codigoEntregador, isComissionado, percentualComissao, cpfCnpj);
		
	}
	
	private void validarParametrosEntradaCadastroEntregadorPJ(
			Long idEntregador,
			Long codigoEntregador,
			boolean isComissionado,
			String percentualComissao,
			HashMap<String, String> cpfCnpj) {
		
		List<String> listaMensagens = new ArrayList<String>();
		ValidacaoVO validacao = new ValidacaoVO();
		validacao.setTipoMensagem(TipoMensagem.WARNING);
		validacao.setListaMensagens(listaMensagens);
		
		validarParametrosEntradaCadastroEntregador(validacao, idEntregador, codigoEntregador, isComissionado, percentualComissao, cpfCnpj);
		
	}
	
	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void validarParametrosEntradaCadastroEntregador(ValidacaoVO validacao,
															Long idEntregador,												
															Long codigoEntregador,
															boolean isComissionado,
															String percentualComissao,
															HashMap<String, String> cpfCnpj) {
		
		List<String> listaMensagens = validacao.getListaMensagens();
						
		if (codigoEntregador == null) {
			
			listaMensagens.add("O preenchimento do campo [Código do entregador] é obrigatório.");
			
			
		} else {
			
			Entregador entregador = entregadorService.obterEntregadorPorCodigo(codigoEntregador);

			if(entregador!=null && !entregador.getId().equals(idEntregador)) {
				
				listaMensagens.add(" Valor do campo [Codigo] já esta sendo utilizado.");
				
			}
			
		}
		
		if (cpfCnpj.containsKey(CPF)) {
			
			String cpfEntregador = cpfCnpj.get(CPF);
			
			if (cpfEntregador == null || cpfEntregador.isEmpty()) {
			
				listaMensagens.add("O preenchimento do campo [CPF] é obrigatório.");

			} else {
				
				CPFValidator cpfValidator = new CPFValidator(true);
				
				try {

					cpfValidator.assertValid(cpfEntregador);
					
				} catch(InvalidStateException e) {
				
					listaMensagens.add("CPF inválido.");
				}
			}
			
		} else if (cpfCnpj.containsKey(CNPJ)) {
			
			String cnpjEntregador = cpfCnpj.get(CNPJ);
			
			if (cnpjEntregador == null || cnpjEntregador.isEmpty()) {
			
				listaMensagens.add("O preenchimento do campo [CNPJ] é obrigatório.");
			
			} else {
				
				CNPJValidator cnpjValidator = new CNPJValidator(true);
				
				try {

					cnpjValidator.assertValid(cnpjEntregador);
					
				} catch(InvalidStateException e) {
				
					listaMensagens.add("CNPJ inválido.");
				}
			}
		}
		
		if (isComissionado && percentualComissao == null) {
			
			listaMensagens.add("O preenchimento do campo [Percentual da comissão] é obrigatório.");
		
		} else if (isComissionado && percentualComissao != null) {
			
			try {
				
				new BigDecimal(getValorSemMascara(percentualComissao));
				
			} catch (NumberFormatException e) {
				
				listaMensagens.add("O preenchimento do campo [Percentual da comissão] não é válido.");
			}
		}
			
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_SALVAR_SESSAO);
		
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		if (map.keySet().isEmpty()) {
			
			listaMensagens.add("Pelo menos um telefone deve ser cadastrado para o entregador.");
		
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
				
				listaMensagens.add("Deve haver ao menos um telefone principal para o entregador.");
			}
		}
		
		if (listaEnderecoAssociacaoSalvar == null || listaEnderecoAssociacaoSalvar.isEmpty()) {
			
			listaMensagens.add("Pelo menos um endereço deve ser cadastrado para o entregador.");
		
		} else {
			
			boolean temPrincipal = false;
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacaoSalvar) {
				
				if (enderecoAssociacao.isEnderecoPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}

			if (!temPrincipal) {
				
				listaMensagens.add("Deve haver ao menos um endereço principal para o entregador.");
			}
		}
		
		if (!listaMensagens.isEmpty()) {
			
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * 
	 */
//	private ProcuracaoEntregador obterProcuracaoEntregadorValidada(Integer numeroCotaProcuracao, 
//																   Long idEntregador,
//																   ProcuracaoEntregador procuracaoEntregador) {
//
//		ProcuracaoEntregador procuracaoEntregadorExistente = null;
//		
//		if (idEntregador != null) {
//
//			procuracaoEntregadorExistente = this.entregadorService.obterProcuracaoEntregadorPorId(idEntregador);
//		}
//
//		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCotaProcuracao);
//
//		if (cota == null) {
//
//			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inválida para a procuração.");
//		}
//
//		if (procuracaoEntregadorExistente == null) {
//			
//			procuracaoEntregador.setCota(cota);
//			
//			return procuracaoEntregador;
//		}
//
//		procuracaoEntregadorExistente.setCota(cota);
//		procuracaoEntregadorExistente.setEstadoCivil(procuracaoEntregador.getEstadoCivil());
//		procuracaoEntregadorExistente.setNacionalidade(procuracaoEntregador.getNacionalidade());
//		procuracaoEntregadorExistente.setNumeroPermissao(procuracaoEntregador.getNumeroPermissao());
//		procuracaoEntregadorExistente.setProcurador(procuracaoEntregador.getProcurador());
//		procuracaoEntregadorExistente.setProfissao(procuracaoEntregador.getProfissao());
//		procuracaoEntregadorExistente.setRg(procuracaoEntregador.getRg());
//
//		return procuracaoEntregadorExistente;
//	}
	
	/*
	 * Método responsável por processar os endereços do entregador.
	 */
	@SuppressWarnings("unchecked")
	private void processarEnderecosEntregador(Long idEntregador) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_SALVAR_SESSAO);

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(
						LISTA_ENDERECOS_REMOVER_SESSAO);

		this.entregadorService.processarEnderecos(idEntregador, 
												  listaEnderecoAssociacaoSalvar, 
												  listaEnderecoAssociacaoRemover);

		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
	}

	/*
	 * 
	 */
	private ProcuracaoCotaDTO parseCotaProcuracao(Cota cota, EnderecoCota enderecoCota) {
		
		ProcuracaoCotaDTO cotaProcuracao = new ProcuracaoCotaDTO();
		
		if ((cota == null) || !(cota.getPessoa() instanceof PessoaFisica)) {
			return null;
		}
		
		PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
		
		cotaProcuracao.setNumeroCota(cota.getNumeroCota());
		cotaProcuracao.setBox(cota.getBox().getCodigo() + " - " + cota.getBox().getNome());
		
		cotaProcuracao.setNacionalidade(pessoaFisica.getNacionalidade());
		cotaProcuracao.setNomeJornaleiro(pessoaFisica.getNome());
		cotaProcuracao.setRg(pessoaFisica.getRg());
		cotaProcuracao.setCpf(pessoaFisica.getCpf());
		cotaProcuracao.setEstadoCivil(pessoaFisica.getEstadoCivil());

		if (enderecoCota != null) {
			
			Endereco endereco = enderecoCota.getEndereco();

			cotaProcuracao.setBairro(endereco.getBairro());
			cotaProcuracao.setCep(endereco.getCep());
			cotaProcuracao.setCidade(endereco.getCidade());
			cotaProcuracao.setEnderecoPDVPrincipal(endereco.getLogradouro());
		}
		
		return cotaProcuracao;
	}
	
	/*
	 * Método responsável por processar os telefones do entregador.
	 */
	private void processarTelefonesEntregador(Long idEntregador){

		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();

		List<TelefoneEntregador> lista = new ArrayList<TelefoneEntregador>();

		for (Integer key : map.keySet()){

			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);

			if (telefoneAssociacaoDTO.getTipoTelefone() != null){
				
				TelefoneEntregador telefoneEntregador = new TelefoneEntregador();
				telefoneEntregador.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
				TelefoneDTO dto = telefoneAssociacaoDTO.getTelefone();
				Telefone telefone = new Telefone(dto.getId(), dto.getNumero(), dto.getRamal(), dto.getDdd(), null);
                telefoneEntregador.setTelefone(telefone);
				telefoneEntregador.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());

				if (key > 0) {

					telefoneEntregador.setId(key.longValue());
				}
				
				lista.add(telefoneEntregador);
			}
		}

		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		this.entregadorService.processarTelefones(idEntregador, lista, telefonesRemover);

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

	/*
	 * Método que retorna uma pessoa Juridica com suas devidas validações.
	 */
	private PessoaJuridica validarPessoaJuridica(PessoaJuridica pessoaJuridica) {
		
		if (pessoaJuridica == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa Juridica não pode estar nula.");
		}

		if (pessoaJuridica.getCnpj() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O CNPJ da Pessoa Juridica não pode ser nulo.");
		}
		
		PessoaJuridica pessoaJuridicaValidada = 
				this.pessoaJuridicaService.buscarPorCnpj(pessoaJuridica.getCnpj());
		
		if (pessoaJuridicaValidada == null) {
			
			pessoaJuridicaValidada = this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
		}

		pessoaJuridicaValidada.setEmail(pessoaJuridica.getEmail());
		pessoaJuridicaValidada.setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
		pessoaJuridicaValidada.setNomeFantasia(pessoaJuridica.getNomeFantasia());
		pessoaJuridicaValidada.setRazaoSocial(pessoaJuridica.getRazaoSocial());

		return this.pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridicaValidada);
	}
	
	/*
	 * Método que retorna uma pessoa Juridica com suas devidas validações.
	 */
	private PessoaFisica validarPessoaFisica(PessoaFisica pessoaFisica) {
		
		if (pessoaFisica == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa Juridica não pode estar nula.");
		}
		
		if (pessoaFisica.getCpf() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O CNPJ da Pessoa Física não pode ser nulo.");
		}

		PessoaFisica pessoaFisicaValidada = 
				this.pessoaFisicaService.buscarPorCpf(pessoaFisica.getCpf());

		if (pessoaFisicaValidada == null) {

			pessoaFisicaValidada = this.pessoaFisicaService.salvarPessoaFisica(pessoaFisica);
		}

		pessoaFisicaValidada.setEmail(pessoaFisica.getEmail());
		pessoaFisicaValidada.setApelido(pessoaFisica.getApelido());
		pessoaFisicaValidada.setNome(pessoaFisica.getNome());
		pessoaFisicaValidada.setDataNascimento(pessoaFisica.getDataNascimento());
		pessoaFisicaValidada.setEstadoCivil(pessoaFisica.getEstadoCivil());
		pessoaFisicaValidada.setNacionalidade(pessoaFisica.getNacionalidade());
		pessoaFisicaValidada.setNatural(pessoaFisica.getNatural());
		pessoaFisicaValidada.setOrgaoEmissor(pessoaFisica.getOrgaoEmissor());
		pessoaFisicaValidada.setRg(pessoaFisica.getRg());
		pessoaFisicaValidada.setSexo(pessoaFisica.getSexo());
		pessoaFisicaValidada.setSocioPrincipal(pessoaFisica.isSocioPrincipal());
		pessoaFisicaValidada.setUfOrgaoEmissor(pessoaFisica.getUfOrgaoEmissor());
		
		return this.pessoaFisicaService.salvarPessoaFisica(pessoaFisicaValidada);
	}
	
	/*
	 * Método que cria um table model baseado no retorno da pesquisa de entregadores.
	 */
	private TableModel<CellModel> obterTableModel(List<Entregador> listaEntregador) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Entregador entregador : listaEntregador) {

			String nome = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getRazaoSocial() :
						((PessoaFisica) entregador.getPessoa()).getNome();

			String documento = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getCnpj() :
						((PessoaFisica) entregador.getPessoa()).getCpf(); 

			String apelido = entregador.getPessoa() instanceof PessoaJuridica ? 
					((PessoaJuridica) entregador.getPessoa()).getNomeFantasia() :
						((PessoaFisica) entregador.getPessoa()).getApelido();

			String email = entregador.getPessoa().getEmail();

			String telefone = "";

			for (TelefoneEntregador telefoneEntregador : entregador.getTelefones()) {

				if (telefoneEntregador.isPrincipal()) {
					
					telefone = obterTelefoneFormatado(telefoneEntregador.getTelefone());
					
				}
			}

			CellModel cellModel = new CellModel(
					entregador.getId().intValue(),
					formatField(entregador.getCodigo()),
					nome,
					documento,
					apelido,
					telefone,
					email);

			listaCellModel.add(cellModel);
		}

		tableModel.setRows(listaCellModel);

		return tableModel;
	}

	private String obterTelefoneFormatado(Telefone telefonePrincipal) {
		
		StringBuffer telefoneFormatado = new StringBuffer();
		
		if(telefonePrincipal == null) {
			return "";
		}
		
		if(telefonePrincipal.getDdd()!=null) {
			telefoneFormatado.append("(");
			telefoneFormatado.append(telefonePrincipal.getDdd());
			telefoneFormatado.append(") ");
		}
		
		if(telefonePrincipal.getNumero()!=null) {
			telefoneFormatado.append(telefonePrincipal.getNumero());
		}
		
		return telefoneFormatado.toString();
		
	}
	
	/*
	 * Método que realiza a formatação dos dados a serem apresentados na grid. 
	 */
	private static String formatField(Object obj) {
		
		return obj == null ? "" : String.valueOf(obj);
	}
	
	/*
	 * Método que prepara o filtro para utilização na pesquisa.
	 */
	private FiltroEntregadorDTO prepararFiltroEntregador(
			FiltroEntregadorDTO filtroEntregador, int page, String sortname, String sortorder, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtroEntregador.setPaginacao(paginacao);
		
		filtroEntregador.setOrdenacaoColunaEntregador(
				Util.getEnumByStringValue(OrdenacaoColunaEntregador.values(), sortname));

		return filtroEntregador;
	}
	

	private String getValorSemMascara(String valor) {

		valor = valor.replaceAll("\\.", "");
		valor = valor.replaceAll(",", "\\.");

		return valor;
	}
	
}
