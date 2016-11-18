package br.com.abril.nds.controllers.lancamento;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaFuroDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.spring.VRaptorRequestHolder;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/furoProduto")
@Rules(Permissao.ROLE_LANCAMENTO_FURO_PRODUTO)
public class FuroProdutoController extends BaseController {

	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private FuroProdutoService furoProdutoService;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;

	@Autowired
	private LancamentoService lancamentoService;
	
	private Result result;
	
	public FuroProdutoController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){
		
	}
	
	@Post
	public void pesquisar(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
		
		codigo = obterCodigoProdutoFormatado(codigo);
		
		this.validarDadosEntradaPesquisa(codigo, edicao, dataLancamento);
		
		FuroProdutoDTO furoProdutoDTO = null;
		try {
			furoProdutoDTO = produtoEdicaoService.obterProdutoEdicaoPorCodigoEdicaoDataLancamento(codigo, produto, edicao, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(dataLancamento), false);
		} catch (Exception e) {
			
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		if (furoProdutoDTO == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		try {
			furoProdutoDTO = produtoEdicaoService.obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
					codigo, produto, edicao, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(dataLancamento), true);
			
		} catch (Exception e) {
			
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		if (furoProdutoDTO == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto já furado.");
		}
		
		String path = furoProdutoDTO.getPathImagem();
		path = path.replace("\\", "/");
		furoProdutoDTO.setPathImagem(null);
		File imagem = null;
		for (String ext : Constantes.EXTENSOES_IMAGENS){
			imagem = new File(path + ext);
			
			String raizApp = VRaptorRequestHolder.currentRequest().getRequest().getContextPath();
			if (imagem.exists()){
				furoProdutoDTO.setPathImagem(path.substring(
								path.indexOf(raizApp)) + ext);
				break;
			}
		}
		result.use(Results.json()).from(furoProdutoDTO, "result").serialize();
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	private void validarDadosEntradaPesquisa(String codigo, Long edicao, String dataLancamento) {
		
		codigo = obterCodigoProdutoFormatado(codigo);
		
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()) {
			
			listaMensagemValidacao.add("Código é obrigatório.");
		}
		
		if (edicao == null) {
			
			listaMensagemValidacao.add("Edição é obrigatório.");
		}
		
		if (dataLancamento == null || dataLancamento.isEmpty()) {
			
			listaMensagemValidacao.add("Data Lançamento é obrigatório.");
			
		} else if (!DateUtil.isValidDatePTBR(dataLancamento)) {
			
			listaMensagemValidacao.add("Valor inválido: Data Lançamento.");
		}

		if (dataLancamento != null) {
		
			Date data = DateUtil.parseDataPTBR(dataLancamento);
			
			// Não permite que a data de lançamento seja menor que a data de operação 
			//na pesquisa (conforme solicitado pelo Rodrigo Winter na trac 586)
			if (data.before(distribuidorService.obter().getDataOperacao())) {
				
				listaMensagemValidacao.add(
					"Data de lançamento não pode ser menor que a data de operação.");
			}
		}
		
		if (!listaMensagemValidacao.isEmpty()) {
			
			ValidacaoVO validacaoVO = 
				new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) throws Exception{
		
		List<Produto> listaProdutoEdicao = null;
		try {
			listaProdutoEdicao = this.produtoService.obterProdutoLikeNome(nomeProduto, Constantes.QTD_MAX_REGISTROS_AUTO_COMPLETE);
		} catch (Exception e) {
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			for (Produto produto : listaProdutoEdicao){
				listaProdutos.add(
						new ItemAutoComplete(
								produto.getNome(), 
								null,
								new FuroProdutoDTO(
										produto.getCodigo())));
			}
			
			result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
		} else {
		
			result.use(Results.json()).from("", "result").serialize();
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	/**
     * Verifica se data é operante
     * 
     * @param data
     */
    private void validarData(String codigoProduto, Long idProdutoEdicao, Date data){
    	
    	final Calendar c = Calendar.getInstance();
         
        c.setTime(data);
         
        boolean diaOperante = this.furoProdutoService.isDiaOperante(codigoProduto, idProdutoEdicao, c);
		
		if (!diaOperante) {
	        	
			throw new ValidacaoException(TipoMensagem.WARNING, DateUtil.formatarDataPTBR(data)+" não é uma data em que o distribuidor realiza operação! ");
	    }
    }

	@Post
	public void validarFuro(String codigoProduto, Long idProdutoEdicao, String novaData, Long idLancamento) throws Exception {
		
		this.validarData(codigoProduto, idProdutoEdicao, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(novaData));

		codigoProduto = obterCodigoProdutoFormatado(codigoProduto);
		
		validarDadosEntradaConfirmarFuro(codigoProduto, idProdutoEdicao, novaData, idLancamento);
		
		this.furoProdutoService.validarFuroProduto(codigoProduto, idProdutoEdicao, idLancamento, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(novaData), getUsuarioLogado().getId());

		boolean produtoExpedido = this.furoProdutoService.verificarProdutoExpedido(idLancamento);
		
		if (produtoExpedido) {
			
			result.use(Results.json()).from(true, "result").serialize();
			
		} else {
			
			confirmarFuro(codigoProduto, idProdutoEdicao, novaData, idLancamento);
		}		
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_FURO_PRODUTO_ALTERACAO)
	public void confirmarFuro(String codigoProduto, Long idProdutoEdicao, String novaData, Long idLancamento) throws Exception{
		
		verificarExecucaoInterfaces();
		
		codigoProduto = obterCodigoProdutoFormatado(codigoProduto);
		
		try {
			this.furoProdutoService.efetuarFuroProduto(codigoProduto, 
			        idProdutoEdicao, 
			        idLancamento, 
			        new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(novaData), 
			        getUsuarioLogado().getId());
			
		} catch (Exception e){
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao efetuar furo: " + e.getMessage());
			}
		}
		
		Lancamento lancamento = this.lancamentoService.buscarPorId(idLancamento);
		
		List<CotaFuroDTO> listaCotaAvista = this.furoProdutoService.obterCobrancaRealizadaParaCotaVista(idProdutoEdicao, lancamento.getDataLancamentoDistribuidor(), idLancamento);
		
		debitoCreditoCotaAvista(listaCotaAvista);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), Constantes.PARAM_MSGS).recursive().serialize();
		
		result.forwardTo(FuroProdutoController.class).index();
	}

	private void debitoCreditoCotaAvista(List<CotaFuroDTO> listaCotaAvista) {
		
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (!listaCotaAvista.isEmpty()){
			listaMensagemValidacao.add("Verifique possíveis debito e crédito, pois a cobrança já foi gerada para as cotas a vista abaixo:");
			for(CotaFuroDTO cotaDTO : listaCotaAvista) {
				listaMensagemValidacao.add("Cota: "+ cotaDTO.getNumeroCota() + " - " + cotaDTO.getNome() + " - " + "Nosso Numero: " + cotaDTO.getNossoNumero());
			}
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	@Post
	public void buscarNomeProduto(String codigoProduto){
		
		codigoProduto = obterCodigoProdutoFormatado(codigoProduto);
		
		String nomeProduto = this.produtoService.obterNomeProdutoPorCodigo(codigoProduto);
		
		if (nomeProduto == null){
			this.result.use(Results.json()).from("").serialize();
		} else {
			this.result.use(Results.json()).from(nomeProduto, "result").serialize();
		}
	}
	
	private void validarDadosEntradaConfirmarFuro(String codigoProduto, Long idProdutoEdicao, 
			String novaData, Long idLancamento) {
		
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()){
			listaMensagemValidacao.add("Código produto é obrigatório.");
		}
		
		if (idProdutoEdicao == null){
			listaMensagemValidacao.add("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			listaMensagemValidacao.add("Lançamento é obrigatório");
		}
		
		if (novaData == null || novaData.isEmpty()){
			listaMensagemValidacao.add("Nova Data é obrigatório.");
		} else if (!DateUtil.isValidDatePTBR(novaData)){
			listaMensagemValidacao.add("Valor inválido: Nova Data.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	private String obterCodigoProdutoFormatado(String codigoProduto) {

		return StringUtils.leftPad(codigoProduto, 8, '0');
		
	}

	private void verificarExecucaoInterfaces() {
		if (distribuidorService.verificaDesbloqueioProcessosLancamentosEstudos()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "As interfaces encontram-se em processamento. Aguarde o termino da execução para continuar!");
		}
	}
}
