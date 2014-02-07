package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/matrizLancamento")
@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ)
public class MatrizLancamentoController extends BaseController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;

	
	@Autowired
	private LancamentoRepository lancamentoRepositoryService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Value("${data_cabalistica}")
	private String dataCabalistica;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO = "balanceamentoLancamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
	
	private static final String DATA_ATUAL_SELECIONADA = "dataAtualSelecionada";
	
	
	@Path("/")
	public void index() {
		
		removerAtributoAlteracaoSessao();
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarDataPTBR(new Date());
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}
	
	@Post
	public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {
		
		validarDadosPesquisa(dataLancamento, idsFornecedores);
		
		removerAtributoAlteracaoSessao();

		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
			
		BalanceamentoLancamentoDTO balanceamentoLancamento = this.obterBalanceamentoLancamento(filtro);
		        
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this.obterResultadoResumoLancamento(balanceamentoLancamento);
						
		this.result.use(CustomJson.class).put("resultado", resultadoResumoBalanceamento).serialize();
		
	}
	
	@Post
	@Path("/salvar")
	public void salvar() {
		
		this.verificarExecucaoInterfaces();
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
	
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
			balanceamentoLancamento.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			this.cloneObject(matrizLancamentoSessao);
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno =
			this.matrizLancamentoService.salvarMatrizLancamento(matrizLancamento, getUsuarioLogado());
		
		matrizLancamento =
			this.atualizarMatizComProdutosConfirmados(matrizLancamento, matrizLancamentoRetorno);
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamento);
		
		this.removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de lancamento salvo com sucesso!"), "result").recursive().serialize();
	}
	
	/**
	 * Obtem lista de produtos do balanceamento
	 * 
	 * @param data
	 * @param balanceamentoLancamento
	 * @return List<ProdutoLancamentoDTO>
	 */
	private List<ProdutoLancamentoDTO> getListaProdutoBalanceamento(Date data, BalanceamentoLancamentoDTO balanceamentoLancamento){
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = new ArrayList<>();
		
		if (data != null) {
			
			listaProdutoBalanceamento =
				balanceamentoLancamento.getMatrizLancamento().get(data);
			
		} else {
			
			for (Map.Entry<Date,List<ProdutoLancamentoDTO>> entry :
					balanceamentoLancamento.getMatrizLancamento().entrySet()) {
			
				listaProdutoBalanceamento.addAll(entry.getValue());
			}
		}
		
		return listaProdutoBalanceamento;
	}
	
	@Post
	public void obterGridMatrizLancamento(String dataLancamentoFormatada, String sortorder, String sortname, int page, int rp) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
	
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		session.setAttribute(DATA_ATUAL_SELECIONADA, dataLancamentoFormatada);
		
		Date data = null;
		
 		if (dataLancamentoFormatada != null && !dataLancamentoFormatada.trim().isEmpty()) {
			
			data = DateUtil.parseDataPTBR(dataLancamentoFormatada);
		}

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(data, balanceamentoLancamento);

		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamentoNaoAgrupados =
			this.obterListaLancamentosDTONaoAgrupados(listaProdutoBalanceamento);
		
		if (listaProdutoBalanceamentoNaoAgrupados != null) {
			filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamentoNaoAgrupados.size());
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
		
		if (listaProdutoBalanceamentoNaoAgrupados != null && !listaProdutoBalanceamentoNaoAgrupados.isEmpty()) {	
			
			processarBalanceamento(listaProdutoBalanceamentoNaoAgrupados, filtro);
			
		} else {
			
			listaProdutoBalanceamentoNaoAgrupados = new ArrayList<>();
			result.use(FlexiGridJson.class).from(listaProdutoBalanceamentoNaoAgrupados).page(page).total(0).serialize();
		}
	}
	
	private List<ProdutoLancamentoDTO> obterListaLancamentosDTONaoAgrupados(List<ProdutoLancamentoDTO> listaProdutoBalanceamento) {

		List<ProdutoLancamentoDTO> listaProdutoBalanceamentoClone = 
			this.cloneObject(listaProdutoBalanceamento);
		
		this.removerProdutosAgrupados(listaProdutoBalanceamentoClone);
		
		return listaProdutoBalanceamentoClone;
	}

	/**
	 * Obtem lista de todos os produtos de lançamento
	 * 
	 * @return List<ProdutoLancamentoDTO>
	 */
	private List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao() {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(null, balanceamentoLancamento);

		return listaProdutoBalanceamento;
	}
	
	/**
	 * Obtem lista de produtos de lançamento por data
	 * 
	 * @param dataSelecionada
	 * 
	 * @return List<ProdutoLancamentoDTO>
	 */
	private List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao(Date dataSelecionada) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(dataSelecionada, balanceamentoLancamento);
		
		return listaProdutoBalanceamento;
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void confirmarMatrizLancamento(List<Date> datasConfirmadas) {
		
		this.verificarExecucaoInterfaces();
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		this.validarDatasConfirmacao(datasConfirmadas.toArray(new Date[]{}));
	
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
			balanceamentoLancamento.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			this.cloneObject(matrizLancamentoSessao);
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno =
			matrizLancamentoService.confirmarMatrizLancamento(matrizLancamento,
															  datasConfirmadas, getUsuarioLogado());
		
		matrizLancamento =
			this.atualizarMatizComProdutosConfirmados(matrizLancamento, matrizLancamentoRetorno);
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamento);
		
		this.verificarLancamentosConfirmados();
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de lançamento confirmado com sucesso!"), "result").recursive().serialize();
	}

	private void validarDatasConfirmacao(Date... datasConfirmadas) {
		
		if (datasConfirmadas == null || datasConfirmadas.length <= 0){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos uma data!");
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		for (Date dataConfirmada : datasConfirmadas) {
			
			if (dataConfirmada.compareTo(dataOperacao) < 0) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Não é possível confirmar uma data menor que a data de operação [ "
						+ DateUtil.formatarDataPTBR(dataOperacao) + " ]");
			}
		}
	}

	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void voltarConfiguracaoOriginal() {
		
		this.verificarExecucaoInterfaces();
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		this.matrizLancamentoService.voltarConfiguracaoInicial(
			filtro.getData(), 
			balanceamentoLancamento, getUsuarioLogado());
		
		//F2
		//filtro.getData(), balanceamentoLancamento.getMatrizLancamento(), getUsuarioLogado());
		
		filtro.setPaginacao(null);
		filtro.setTotalRegistrosEncontrados(null);
		
		balanceamentoLancamento =
			this.obterBalanceamentoLancamento(filtro);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void perguntarDataConfirmadaOuNao( ProdutoLancamentoDTO produtoLancamento ){
		
		boolean retornoDataConfirmada = this.matrizLancamentoService.isDataConfirmada(produtoLancamento);
		this.result.use(Results.json()).from(retornoDataConfirmada).serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void reprogramarLancamentosSelecionados(List<ProdutoLancamentoVO> produtosLancamento,
												   String novaDataFormatada) {
		
		this.verificarExecucaoInterfaces();
		
		this.validarDadosReprogramar(novaDataFormatada);
	
		adicionarAtributoAlteracaoSessao();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		this.validarDatasConfirmacao(novaData);
		
		this.validarListaParaReprogramacao(produtosLancamento);
		
		this.validarDataReprogramacao(produtosLancamento, novaData);
		
		this.atualizarMapaLancamento(produtosLancamento, novaData);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	private void removerProdutosAgrupados(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		if(produtosLancamento == null) return;
		
		Iterator<ProdutoLancamentoDTO> iterator = produtosLancamento.iterator();
		
		while (iterator.hasNext()) {
			
			ProdutoLancamentoDTO produtoLancamento = iterator.next();
			
			if (produtoLancamento.isLancamentoAgrupado()) {
				
				iterator.remove();
			}
		}
	}

	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void reprogramarLancamentoUnico(ProdutoLancamentoVO produtoLancamento) {
		
		this.verificarExecucaoInterfaces();
		
		String novaDataFormatada = produtoLancamento.getNovaDataLancamento();
		
		this.validarDadosReprogramar(novaDataFormatada);

		adicionarAtributoAlteracaoSessao();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		this.validarDatasConfirmacao(novaData);
		
		List<ProdutoLancamentoVO> produtosLancamento = new ArrayList<ProdutoLancamentoVO>();
		
		if (produtoLancamento != null){
			
			produtosLancamento.add(produtoLancamento);
		}
		
		this.validarListaParaReprogramacao(produtosLancamento);
		
		this.validarDataReprogramacao(produtosLancamento, novaData);
		
		this.atualizarMapaLancamento(produtosLancamento, novaData);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	/**
	 * Método que atualiza a matriz de lançamento de acordo com os produtos da matriz de retorno
	 * 
	 * @param matrizLancamento - matriz de lançamento
	 * @param matrizLancamentoRetorno - matriz de lançamento de retorno
	 * 
	 * @return matriz atualizada
	 */
	private TreeMap<Date, List<ProdutoLancamentoDTO>> atualizarMatizComProdutosConfirmados(
								TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
								TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno) {
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry
				: matrizLancamentoRetorno.entrySet()) {
			
			Date novaData = entry.getKey();
			
			List<ProdutoLancamentoDTO> produtosLancamentoRetorno =
					matrizLancamentoRetorno.get(novaData);
			
			matrizLancamento.put(novaData, produtosLancamentoRetorno);
		}
		
		return matrizLancamento;
	}
	
	/**
	 * Método que verifica se todos os lançamentos estão confirmados
	 * para remover a flag de alteração de dados da sessão.
	 */
	private void verificarLancamentosConfirmados() {
		
		List<ConfirmacaoVO> listaConfirmacao = montarListaDatasConfirmacao();
		
		boolean lancamentosConfirmados = true;
		
		for (ConfirmacaoVO confirmacao : listaConfirmacao) {
			
			if (!confirmacao.isConfirmado()) {
				
				lancamentosConfirmados = false;
				
				break;
			}
		}
		
		if (lancamentosConfirmados) {
			
			removerAtributoAlteracaoSessao();
		}
	}
	
	/**
	 * Valida os dados para reprogramação.
	 * 
	 * @param data - data para reprogramação
	 */
	private void validarDadosReprogramar(String data) {
		
		if (data == null || data.trim().isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
		}
		
		if (!DateUtil.isValidDatePTBR(data)) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Data inválida!"));
		}
	}
	
	/**
	 * Valida a data de reprogramação de lançamento.
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 * @param novaData - nova data de recolhimento
	 */
	private void validarDataReprogramacao(List<ProdutoLancamentoVO> produtosLancamento, Date novaData) {
		
		this.matrizLancamentoService.verificaDataOperacao(novaData);
		
		this.matrizLancamentoService.validarDiaSemanaDistribuicaoFornecedores(novaData);
		
		List<String> listaMensagens = new ArrayList<String>();
		
		String produtos = "";
		
		Integer qtdDiasLimiteParaReprogLancamento =
				this.distribuidorService.qtdDiasLimiteParaReprogLancamento();
		
		for (ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
		
			String dataRecolhimentoPrevistaFormatada = produtoLancamento.getDataRecolhimentoPrevista();
			
			if (dataRecolhimentoPrevistaFormatada == null
					|| dataRecolhimentoPrevistaFormatada.trim().isEmpty()) {
				
				continue;
			}
			
			Date dataRecolhimentoPrevista =
				DateUtil.parseDataPTBR(produtoLancamento.getDataRecolhimentoPrevista());
			
			Date dataLimiteReprogramacao =
				calendarioService.subtrairDiasUteisComOperacao(dataRecolhimentoPrevista,
															   qtdDiasLimiteParaReprogLancamento);
			
			if (novaData.compareTo(dataLimiteReprogramacao) == 1) {
				
				if (produtos.isEmpty()) {
					produtos += "<table>";
				}
				
				produtos +=
					"<tr>"
					+ "<td><u>Produto:</u> " + produtoLancamento.getNomeProduto() + "</td>"
					+ "<td><u>Edição:</u> " + produtoLancamento.getNumeroEdicao() + "</td>"
					+ "<td><u>Data recolhimento:</u> " + dataRecolhimentoPrevistaFormatada + "</td>"
					+ "</tr>";
			}
		}
		
		if (!produtos.isEmpty()) {
		
			listaMensagens.add(
				"A nova data de lançamento não deve ultrapassar "
				+ "a data de recolhimento prevista menos a quantidade de dias limite [" 
				+ qtdDiasLimiteParaReprogLancamento + "] para o(s) produto(s):"
			);
			
			listaMensagens.add(produtos + "</table> "
				+ "Para lançar o produto na data informada, é necessário alterar a data de recolhimento da(s) edição(ões).");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * Valida a lista de produtos informados na tela para reprogramação.
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 */
	private void validarListaParaReprogramacao(List<ProdutoLancamentoVO> produtosLancamento) {
		
		if (produtosLancamento == null || produtosLancamento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar ao menos um produto para realizar a reprogramação!");
		}
	}
	
	/**
	 * Método que atualiza o mapa de lançamento de acordo com as escolhas do usuário
	 * 
	 * @param produtosLancamento - lista de produtos a serem alterados
	 * @param novaData - nova data de lançamento
	 */
	private void atualizarMapaLancamento(List<ProdutoLancamentoVO> produtosLancamento,
										 Date novaData) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamentoSessao =
			(BalanceamentoLancamentoDTO)
				this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamentoSessao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
			balanceamentoLancamentoSessao.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			this.cloneObject(matrizLancamentoSessao);
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar =
			new ArrayList<ProdutoLancamentoDTO>();
		
		this.montarListasParaAlteracaoMapa(produtosLancamento,
										   listaProdutoLancamentoAlterar);
		
		this.removerEAdicionarMapa(matrizLancamento,
							  	   listaProdutoLancamentoAlterar,
							  	   novaData);
		
		balanceamentoLancamentoSessao.setMatrizLancamento(matrizLancamento);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO,
								  balanceamentoLancamentoSessao);
	}
	
	/**
	 * Cria uma cópia do objeto informado.
	 * Isso é necessário pois se houver alterações na cópia,
	 * não altera os valores do objeto original por referência.
	 * 
	 * @param object - objeto a ser clonado
	 * 
	 * @return objeto clonado
	 */
	@SuppressWarnings("unchecked")
	private <T extends Object> T cloneObject(T object) {
		
		byte[] serialized =
			SerializationUtils.serialize(object);

		T objectCloned =
			(T) SerializationUtils.deserialize(serialized);
		
		return objectCloned;
	}
	
	/**
	 * Monta as listas para alteração do mapa da matriz de lançamento
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 * @param listaProdutoLancamentoAlterar - lista de produtos que serão alterados
	 */
	private void montarListasParaAlteracaoMapa(List<ProdutoLancamentoVO> produtosLancamento,									 
											   List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar) {
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoSessao = this.getProdutoLancamentoDTOFromMatrizSessao();
		
		for (ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoSessao) {
				
				if (produtoLancamentoDTO.getIdLancamento().equals(
						Long.valueOf(produtoLancamento.getId()))) {
					
					listaProdutoLancamentoAlterar.add(produtoLancamentoDTO);
					
					List<ProdutoLancamentoDTO> produtosLancamentoAgrupados =
							produtoLancamentoDTO.getProdutosLancamentoAgrupados();
					
					if (!produtosLancamentoAgrupados.isEmpty()) {
						
						listaProdutoLancamentoAlterar.addAll(produtosLancamentoAgrupados);
					}
					
					break;
				}
			}
		}
	}
	
	/**
	 * Remove e adiona os produtos no mapa da matriz de lançamento.
	 * 
	 * @param matrizLancamento - mapa da matriz de lançamento
	 * @param listaProdutoLancamentoAlterar - lista de produtos que serão alterados
	 * @param novaData - nova data de lançamento
	 */
	private void removerEAdicionarMapa(Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,   									 
		     						   List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar,
		     						   Date novaData) {
		
		//Remover do mapa
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoAlterar) {
		
			List<ProdutoLancamentoDTO> produtosLancamentoDTO =
				matrizLancamento.get(produtoLancamentoDTO.getNovaDataLancamento());
			
			produtosLancamentoDTO.remove(produtoLancamentoDTO);
			
			if (produtosLancamentoDTO.isEmpty()) {
				
				matrizLancamento.remove(produtoLancamentoDTO.getNovaDataLancamento());
				
			} else {
				
				matrizLancamento.put(produtoLancamentoDTO.getNovaDataLancamento(),
									 produtosLancamentoDTO);
			}
		}
		
		//Adicionar no mapa
		for (ProdutoLancamentoDTO produtoLancamentoAdicionar : listaProdutoLancamentoAlterar) {
			
			List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(novaData);
			
			if (produtosLancamento == null) {
				
				produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
			}
			
			produtoLancamentoAdicionar.setNovaDataLancamento(novaData);
			
			matrizLancamentoService.tratarAgrupamentoPorProdutoDataLcto(
				produtoLancamentoAdicionar, produtosLancamento);
			
			produtosLancamento.add(produtoLancamentoAdicionar);
			
			matrizLancamento.put(novaData, produtosLancamento);
		}
	}
	
	private void processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										FiltroLancamentoDTO filtro) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		Double valorTotal = this.getValorTotal(listaProdutoLancamento);
		
		List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = getProdutosLancamentoVO(listaProdutoLancamento);
		
		listaProdutoLancamento =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoLancamento, paginacao, paginacao.getSortColumn());
		
		List<ProdutoLancamentoVO> listaProdutoBalanceamentoPaginacaoVO = getProdutosLancamentoVO(listaProdutoLancamento);

		TableModel<CellModelKeyValue<ProdutoLancamentoVO>> tm = new TableModel<CellModelKeyValue<ProdutoLancamentoVO>>();
		List<CellModelKeyValue<ProdutoLancamentoVO>> cells = CellModelKeyValue
				.toCellModelKeyValue(listaProdutoBalanceamentoPaginacaoVO);
		
		List<Object> resultado = new ArrayList<Object>();
		
		tm.setRows(cells);
		
		tm.setPage(paginacao.getPaginaAtual());
		
		tm.setTotal(filtro.getTotalRegistrosEncontrados());

		resultado.add(tm);
		resultado.add(CurrencyUtil.formatarValor(valorTotal));
		resultado.add(listaProdutoBalanceamentoVO);
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		
	}
	
	private Double getValorTotal(List<ProdutoLancamentoDTO> listaProdutoLancamento) {

		Double valorTotal = 0.0;
		
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {

			valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
		}
		
		return valorTotal;
	}

	private List<ProdutoLancamentoVO> getProdutosLancamentoVO(List<ProdutoLancamentoDTO> listaProdutoLancamento) {

		List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = new LinkedList<ProdutoLancamentoVO>();
		
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {
			
			listaProdutoBalanceamentoVO.add(getVoProdutoBalanceamento(produtoLancamentoDTO));
		}

		return listaProdutoBalanceamentoVO;
	}

	private ProdutoLancamentoVO getVoProdutoBalanceamento(
			ProdutoLancamentoDTO produtoLancamentoDTO) {

		ProdutoLancamentoVO produtoBalanceamentoVO = new ProdutoLancamentoVO();
		
		produtoBalanceamentoVO.setCodigoProduto(produtoLancamentoDTO.getCodigoProduto());
		
		produtoBalanceamentoVO.setNovaDataLancamento(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getNovaDataLancamento()));
		
		produtoBalanceamentoVO.setDataLancamentoPrevista(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataLancamentoPrevista()));
		
		produtoBalanceamentoVO.setDataRecolhimentoPrevista(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataRecolhimentoPrevista()));
		
		produtoBalanceamentoVO.setId(produtoLancamentoDTO.getIdLancamento());
		
		produtoBalanceamentoVO.setDescricaoLancamento(produtoLancamentoDTO.getDescricaoLancamento());
		
		produtoBalanceamentoVO.setNomeProduto(produtoLancamentoDTO.getNomeProduto());
		produtoBalanceamentoVO.setNumeroEdicao(produtoLancamentoDTO.getNumeroEdicao());
		
		produtoBalanceamentoVO.setPrecoVenda(CurrencyUtil.formatarValor(produtoLancamentoDTO.getPrecoVenda()));
		
		produtoBalanceamentoVO.setRepartePrevisto(produtoLancamentoDTO.getRepartePrevisto().toString());
		
		produtoBalanceamentoVO.setValorTotal(CurrencyUtil.formatarValor(produtoLancamentoDTO.getValorTotal()));
		
		if(produtoLancamentoDTO.getReparteFisico()==null)
			produtoBalanceamentoVO.setReparteFisico("0");
		else
			produtoBalanceamentoVO.setReparteFisico(produtoLancamentoDTO.getReparteFisico().toString());
		
		if(produtoLancamentoDTO.getDistribuicao() == null) {
			produtoBalanceamentoVO.setDistribuicao("");
		} else {
			produtoBalanceamentoVO.setDistribuicao(produtoLancamentoDTO.getDistribuicao().toString());
		}
		
		produtoBalanceamentoVO.setBloquearData(
			this.matrizLancamentoService.isProdutoConfirmado(produtoLancamentoDTO));
		
		produtoBalanceamentoVO.setIdProdutoEdicao(produtoLancamentoDTO.getIdProdutoEdicao());
		
		produtoBalanceamentoVO.setPossuiFuro(produtoLancamentoDTO.isPossuiFuro());
		
		String reparteFisico = produtoBalanceamentoVO.getReparteFisico();
		String distribuicao = produtoBalanceamentoVO.getDistribuicao();
		
		if((reparteFisico.equals("0") || reparteFisico.equals("")
				|| distribuicao.equals("0") || distribuicao.equals(""))&& !produtoLancamentoDTO.getStatus().equals(StatusLancamento.EXPEDIDO)) {
			
			produtoBalanceamentoVO.setDestacarLinha(true);
		}
				
		produtoBalanceamentoVO.setPeb(produtoLancamentoDTO.getPeb());
		
		if (validarStatusParaExclusaoLancamento(produtoLancamentoDTO.getStatus())) {

		  produtoBalanceamentoVO.setCancelado(true);
		  
		} else {
			
		  produtoBalanceamentoVO.setCancelado(false);	
		}
		
		return produtoBalanceamentoVO;
	}
	
	private boolean validarStatusParaExclusaoLancamento(StatusLancamento status) {
		
		return (StatusLancamento.CONFIRMADO.equals(status)
					|| StatusLancamento.EM_BALANCEAMENTO.equals(status)
					|| StatusLancamento.PLANEJADO.equals(status)
					|| StatusLancamento.FURO.equals(status));
	}
	
	@Exportable
	public class RodapeDTO {
		@Export(label="Valor Total R$:")
		private String total;
		
		public RodapeDTO(String total) {
			this.total = total;
		}
		
		public String getTotal() {
			return total;
		}
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		String dataSelecionada = (String) session.getAttribute(DATA_ATUAL_SELECIONADA);
		
        Date data = null;
		
 		if (dataSelecionada != null && !dataSelecionada.trim().isEmpty()) {
			
			data = DateUtil.parseDataPTBR(dataSelecionada);
		}

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao(data);

		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			Double valorTotal = getValorTotal(listaProdutoBalanceamento);
			
			List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO =
				getProdutosLancamentoVO(listaProdutoBalanceamento);
			
			RodapeDTO rodape = new RodapeDTO(CurrencyUtil.formatarValor(valorTotal));
			
			FileExporter.to("matriz_balanceamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
					listaProdutoBalanceamentoVO, ProdutoLancamentoVO.class, this.httpResponse);
		}
		
		result.nothing();
	}
	
	private String montarNomeFornecedores(List<Long> idsFornecedores) {
		
		String nomeFornecedores = "";
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorId(idsFornecedores);
		
		if (listaFornecedor != null && !listaFornecedor.isEmpty()) {
			
			for (Fornecedor fornecedor : listaFornecedor) {
				
				if (!nomeFornecedores.isEmpty()) {
					
					nomeFornecedores += " / ";
				}
				
				nomeFornecedores += fornecedor.getJuridica().getRazaoSocial();
			}
		}
		
		return nomeFornecedores;
	}
	
	/**
	 * Obtém a matriz de balanceamento de balanceamento.
	 * 
	 * @param dataBalanceamento - data de balanceamento
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * 
	 * @return - objeto contendo as informações do balanceamento
	 */
	private BalanceamentoLancamentoDTO obterBalanceamentoLancamento(FiltroLancamentoDTO filtro) {
		
		BalanceamentoLancamentoDTO balanceamento = this.matrizLancamentoService.obterMatrizLancamento(filtro);
					
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamento);
		
		if (balanceamento == null
				|| balanceamento.getMatrizLancamento() == null
				|| balanceamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamento;
	}
	
	
	/**
	 * Configura o filtro informado na tela e o armazena na sessão.
	 * 
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de identificadores de fornecedores
	 */
	private FiltroLancamentoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		FiltroLancamentoDTO filtro =
			new FiltroLancamentoDTO(dataPesquisa, listaIdsFornecedores);
		
		filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE,filtro);
		
		return filtro;
	}
	
	
	/**
	 * Valida os dados da pesquisa.
	 *  
	 * @param numeroSemana - número da semana
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 */
	private void validarDadosPesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");
			
		}
		
		if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	/**
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private ResultadoResumoBalanceamentoVO obterResultadoResumoLancamento(
											BalanceamentoLancamentoDTO balanceamentoBalanceamento) {
		
		if (balanceamentoBalanceamento == null
				|| balanceamentoBalanceamento.getMatrizLancamento() == null
				|| balanceamentoBalanceamento.getMatrizLancamento().isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoVO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoBalanceamento.getMatrizLancamento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = 0L;
				Long qtdeTitulosParciais = 0L;
				
				Long pesoTotal = 0L;
				BigInteger qtdeExemplares = BigInteger.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
					
					if (produtoBalanceamento.isLancamentoAgrupado()) {
						
						continue;
					}
					
					if (produtoBalanceamento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoBalanceamento.getPeso() != null) {
						
						pesoTotal += produtoBalanceamento.getPeso();
					}
					
					if (produtoBalanceamento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoBalanceamento.getValorTotal());
					}
					
					if (produtoBalanceamento.getRepartePrevisto() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoBalanceamento.getRepartePrevisto());
					}
					
					qtdeTitulos++;
				}
				
				boolean excedeCapacidadeDistribuidor = false;
				
				if (balanceamentoBalanceamento.getCapacidadeDistribuicao() != null) {
				
					excedeCapacidadeDistribuidor =
						(balanceamentoBalanceamento.getCapacidadeDistribuicao()
							.compareTo(qtdeExemplares) == -1);
				}
				
				itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(
					excedeCapacidadeDistribuidor);
				
				itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
				itemResumoPeriodoBalanceamento.setPesoTotal(new BigDecimal(pesoTotal/1000));
				itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares);
				itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
				
				itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
				
				itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
			}
			
			resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
		
		resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
		
		resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(
			balanceamentoBalanceamento.getCapacidadeDistribuicao());
		
		resultadoResumoBalanceamento.setListaProdutosLancamentosCancelados(balanceamentoBalanceamento.getProdutosLancamentosCancelados());
		
		return resultadoResumoBalanceamento;
	}
	
	/**
	 * Obtém o filtro para pesquisa da sessão.
	 * 
	 * @return filtro
	 */
	private FiltroLancamentoDTO obterFiltroSessao() {
		
		FiltroLancamentoDTO filtro = (FiltroLancamentoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		return filtro;
	}

	/**
	 * Obtem agrupamento diário para confirmação de Balanceamento
	 */
	@Post
	public void obterAgrupamentoDiarioBalanceamento() {

		List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
		List<ConfirmacaoVO> confirmacoesAuxVO =  new ArrayList<>();
		
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
		Date data = new Date();
		
		for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
			
			try{
			 
				data = format.parse(confirmacaoVO.getMensagem());
			
			}catch(ParseException ex){
				
			}
			
			if (!confirmacaoVO.isConfirmado()) {
				
				if(this.distribuidorService.obterDataOperacaoDistribuidor().getTime() <= data.getTime()){
					
					confirmacoesAuxVO.add(confirmacaoVO);
				}
			}
		}
		
		if (confirmacoesAuxVO != null) {
		
			result.use(Results.json()).from(confirmacoesAuxVO, "result").serialize();
		}
	}

	/**
	 * Obtem a concentração ordenada e agrupada por data para a Matriz de Lançamento
	 * 
	 * @return List<ConfirmacaoVO>: confirmacoesVO
	 */
	private List<ConfirmacaoVO> montarListaDatasConfirmacao() {

		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		List<ConfirmacaoVO> confirmacoesVO =
			this.matrizLancamentoService.obterDatasConfirmacao(balanceamentoLancamento);

		return confirmacoesVO;
	}
	
	@Post
	public void verificarBalanceamentosAlterados() {
		
		Boolean balanceamentoAlterado =
			(Boolean) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
		
		if (balanceamentoAlterado == null) {
			
			balanceamentoAlterado = false;
		}
		
		this.result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
	}
	
	/**
	 * Adiciona um indicador, que informa se houve reprogramação de produtos, na sessão.
	 */
	private void adicionarAtributoAlteracaoSessao() {
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
	}
	
	/**
	 * Remove um indicador, que informa se houve reprogramação de produtos, da sessão.
	 */
	private void removerAtributoAlteracaoSessao() {
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
	}

	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void atualizarResumoBalanceamento() {
				
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO)
				this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
		
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}

	private void verificarExecucaoInterfaces() {
		if (distribuidorService.verificaDesbloqueioProcessosLancamentosEstudos()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "As interfaces encontram-se em processamento. Aguarde o termino da execução para continuar!");
		}
	}
	
	@Get
	public void obterDatasConfirmadasReabertura() {
		
		List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();

		List<String> datasConfirmadasReabertura = new ArrayList<>();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
		Date data = new Date();
		
		
		
		for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
			
			try{
			 
				data = format.parse(confirmacaoVO.getMensagem());
			
			}catch(ParseException ex){
				
			}
			
			if (confirmacaoVO.isConfirmado()) {
				
				if(this.distribuidorService.obterDataOperacaoDistribuidor().before(data)){
					
				  datasConfirmadasReabertura.add(confirmacaoVO.getMensagem());
				}
			}
		}
		
		this.result.use(Results.json()).from(datasConfirmadasReabertura, "result").serialize();
	}
	
	@Post
	public void reabrirMatriz(List<Date> datasReabertura, Date dataLancamento, List<Long> idsFornecedores) {

		if (datasReabertura == null || datasReabertura.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhuma data foi selecionada!"));
		}
		this.matrizLancamentoService.reabrirMatriz(datasReabertura, getUsuarioLogado());
		
		validarDadosPesquisa(dataLancamento, idsFornecedores);
		
		removerAtributoAlteracaoSessao();

		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
		
		// Recarrega o objeto na sessao
		this.obterBalanceamentoLancamento(filtro);
		
		this.result.use(PlainJSONSerialization.class).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Reabertura realizada com sucesso!"), "result").recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
	public void excluirLancamento(ProdutoLancamentoVO produtoLancamento) {

		Date data = DateUtil.parseDataPTBR(this.dataCabalistica);
		
		Lancamento lancamento = this.lancamentoRepositoryService.buscarPorId(produtoLancamento.getId());
		
		lancamento.setDataLancamentoDistribuidor(data);
		lancamento.voltarStatusOriginal();
		//atualizarLancamento(produtoLancamento.getId(),data);

		this.lancamentoRepositoryService.merge(lancamento);

		this.result.use(PlainJSONSerialization.class).from(
					new ValidacaoVO(TipoMensagem.SUCCESS, "Excluido com sucesso!"), "result").recursive().serialize();
		
	}

}
