package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ResultadoVendaEncalheVO;
import br.com.abril.nds.client.vo.VendaEncalheVO;
import br.com.abril.nds.client.vo.VendaProdutoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;


/**
 * 
 * Classe responsável pelo controle das ações referentes a
 * tela de venda de encalhe.
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/devolucao/vendaEncalhe")
public class VendaEncalheController extends BaseController {
	

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroVendasencalhe";

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private br.com.caelum.vraptor.Validator validator;
	
	@Autowired
	private VendaEncalheService vendaEncalheService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private DescontoService descontoService;
	
	
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_VENDA_ENCALHE)
	public void index() {}

	/**
	 * Exibe o Slip de Venda de Encalhe em formato PDF.
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @throws Exception
	 */
	@Get("/imprimeSlipVendaEncalhe")
	public Download imprimeSlipVendaEncalhe() throws Exception{
		
		byte[]comprovante = (byte[]) session.getAttribute("COMPROVANTE_VENDA");

		session.removeAttribute("COMPROVANTE_VENDA");
		
		return new ByteArrayDownload(comprovante,"application/pdf", "comprovanteVenda.pdf",true);
	}
	
	@Get("/imprimirSlipVenda")
	public Download imprimirSlipVenda() throws Exception{
		
		FiltroVendaEncalheDTO filtro = this.obterFiltroExportacao();
		
		byte[] comprovate = this.vendaEncalheService.geraImpressaoVenda(filtro);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "slipVenda.pdf", true);
	}
	
	@Post
	public void confirmaNovaVenda(List<VendaEncalheDTO> listaVendas, Long numeroCota, Date dataDebito){
		
		confirmaVenda(listaVendas, numeroCota, dataDebito, Boolean.TRUE);
	}
	
	@Post
	public void confirmaEdicaoVenda(List<VendaEncalheDTO> listaVendas, Long numeroCota, Date dataDebito){
		
		confirmaVenda(listaVendas, numeroCota, dataDebito, Boolean.FALSE);
	}
	
	private void confirmaVenda(List<VendaEncalheDTO> listaVendas, Long numeroCota, Date dataDebito,boolean novaVenda){
		
		validarParametrosVenda(listaVendas,numeroCota, dataDebito);
		
		byte[] comprovanteVenda = null;
		
		if(novaVenda){

			comprovanteVenda = vendaEncalheService.efetivarVendaEncalhe(listaVendas,numeroCota,dataDebito,getUsuarioLogado());
		}
		else{
			comprovanteVenda = vendaEncalheService.alterarVendaEncalhe(listaVendas.get(0),dataDebito,getUsuarioLogado());
		}
		
		session.setAttribute("COMPROVANTE_VENDA",comprovanteVenda);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post
	public void obterBoxCota(Integer numeroCota){
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
		
		String codBox ="";
		
		if(cota!= null && cota.getBox()!= null){
			
			codBox = cota.getBox().getCodigo() + " - " + cota.getBox().getNome(); 
		}
		
		result.use(CustomJson.class).from(codBox).serialize();
	}
	
	@Post
	public void totalizarValorProduto(String precoProduto,Integer qntSolicitada,Integer qntDisponivel){
		
		if(qntSolicitada > qntDisponivel){
			throw new ValidacaoException(TipoMensagem.WARNING,"Quantidade solicitada de produto inválida!");
		}
		
		BigDecimal total = CurrencyUtil.converterValor(precoProduto).multiply((new BigDecimal(qntSolicitada)));
		
		Map<String, Object> mapa = new TreeMap<String, Object>();
		mapa.put("totalFormatado", CurrencyUtil.formatarValor(total));
		mapa.put("total",total);
		
		result.use(CustomJson.class).from(mapa).serialize();
	}
	
	@Post
	public void obterDatavenda(){
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataVencimentoDebito = new Date();
		
		if(distribuidor!= null){
			
			Integer qntDias = distribuidor.getQntDiasVencinemtoVendaEncalhe();
			
			qntDias = (qntDias == null)?0: qntDias;
			
			dataVencimentoDebito = DateUtil.adicionarDias(dataVencimentoDebito,qntDias);
		} 
		
		Map<String, Object> mapa = new TreeMap<String, Object>();
		mapa.put("data", DateUtil.formatarDataPTBR(new Date()));
		mapa.put("dataVencimentoDebito",DateUtil.formatarDataPTBR(dataVencimentoDebito));
		
		result.use(CustomJson.class)
				.from(mapa)
				.serialize();
	}
	
	@Post
	public void obterDadosDoProduto(String codigoProduto, Long numeroEdicao, Long numeroCota){
		
		if(numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Cota deve ser preenchido!");
		}
		
		VendaEncalheDTO encalheDTO = vendaEncalheService.buscarProdutoComEstoque(codigoProduto, numeroEdicao, numeroCota);
		
		if(encalheDTO == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto não possui itens em estoque para venda!");
		}
		
		result.use(CustomJson.class).from(getVendaEncalheVO(encalheDTO)).serialize();
	}
	
	@Post
	public void pesquisarProdutoCodBarra(String codBarra){
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarProdutoPorCodigoBarras(codBarra);
		
		if(produtoEdicao== null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto com o código de barras \""+codBarra+"\" não encontrado!");
		}
		
		Map<String, Object> mapa = new TreeMap<String, Object>();
		
		if (produtoEdicao.getProduto() != null) {
			mapa.put("codigoProduto", produtoEdicao.getProduto().getCodigo());
		}
		mapa.put("nuemroEdicao", produtoEdicao.getNumeroEdicao());
		
		result.use(CustomJson.class).from(mapa).serialize();
	}
	
	@Post
	public void prepararDadosVenda(){
		
		List<VendaProdutoVO> listaPesquisaProduto= new ArrayList<VendaProdutoVO>();
		
		int qtdeInicialPadrao = 30;
		
		for (int indice = 0; indice < qtdeInicialPadrao; indice++) {
			
			VendaProdutoVO produtoVO = new VendaProdutoVO();
			produtoVO.setId(indice);

			listaPesquisaProduto.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<VendaProdutoVO>> tableModel =
						new TableModel<CellModelKeyValue<VendaProdutoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPesquisaProduto));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void prepararDadosEdicaoVenda(Long idVendaEncalhe){
		
		VendaEncalheDTO venda = vendaEncalheService.buscarVendaEncalhe(idVendaEncalhe);
		
		List<VendaProdutoVO> listaPesquisaProduto= new ArrayList<VendaProdutoVO>();
		
		int qtdeInicialPadrao = 1;
			
		VendaProdutoVO produtoVO = new VendaProdutoVO();
		produtoVO.setId(1);
		produtoVO.setCodigoBarras(venda.getCodigoBarras());
		produtoVO.setCodigoProduto(venda.getCodigoProduto());
		produtoVO.setFormaVenda(tratarValor(venda.getFormaVenda()));
		produtoVO.setNomeProduto(venda.getNomeProduto());
		produtoVO.setNumeroEdicao(venda.getNumeroEdicao());
		produtoVO.setPrecoDesconto(CurrencyUtil.formatarValor(venda.getPrecoDesconto()));
		produtoVO.setTotal(CurrencyUtil.formatarValor(venda.getValoTotalProduto()));
		produtoVO.setQntSolicitada(venda.getQntProduto());
		produtoVO.setDataVenda(DateUtil.formatarDataPTBR(venda.getDataVenda()));
		produtoVO.setDataVencimentoDebito(DateUtil.formatarDataPTBR(venda.getDataVencimentoDebito()));
		produtoVO.setCodBox(venda.getCodBox());
		produtoVO.setNumeroCota(tratarValor(venda.getNumeroCota()));
		produtoVO.setNomeCota(venda.getNomeCota());
		produtoVO.setIdVendaEncalhe(venda.getIdVenda());
		produtoVO.setTipoVenda(venda.getTipoVendaEncalhe());
		produtoVO.setValorTotal(venda.getValoTotalProduto());
		produtoVO.setDescTipoVenda(venda.getTipoVendaEncalhe().getVenda());
		produtoVO.setTipoVenda(venda.getTipoVendaEncalhe());
		produtoVO.setFormaComercializacao(venda.getFormaVenda());
		
		BigInteger qntDisponivel = venda.getQntDisponivelProduto();
		
		qntDisponivel = qntDisponivel.add(venda.getQntProduto());
		
		produtoVO.setQntDisponivel(qntDisponivel.intValue());
		
		listaPesquisaProduto.add(produtoVO);
		
		TableModel<CellModelKeyValue<VendaProdutoVO>> tableModel =
						new TableModel<CellModelKeyValue<VendaProdutoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPesquisaProduto));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void recalcularValorDescontoItensVenda(List<VendaProdutoVO> listaVendas, Integer numeroCota){
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
		
		if(cota != null){
			
			for(VendaProdutoVO venda : listaVendas){
				
				ProdutoEdicao produtoEdicao = 
						produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(venda.getCodigoProduto(),venda.getNumeroEdicao().toString());
				
				BigDecimal descontoProduto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota, produtoEdicao);
				
				BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
	    
				BigDecimal valorComDesconto = precoVenda.subtract(MathUtil.calculatePercentageValue(precoVenda, descontoProduto));
				
				BigDecimal valorTotal =  valorComDesconto.multiply(new BigDecimal(venda.getQntSolicitada()));
				
				venda.setPrecoDesconto(CurrencyUtil.formatarValor(valorComDesconto));
				venda.setValorTotal(valorTotal);
				venda.setTotal(CurrencyUtil.formatarValor(valorTotal));
			}
		
		}
			
		for (int i = listaVendas.size(); i < 50; i++) {
			
			VendaProdutoVO produtoVO = new VendaProdutoVO();
			produtoVO.setId(i);
			
			listaVendas.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<VendaProdutoVO>> tableModel =
						new TableModel<CellModelKeyValue<VendaProdutoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaVendas));
		
		tableModel.setTotal(1);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void excluir(Long idVenda){
		
		vendaEncalheService.excluirVendaEncalhe(idVenda);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();

	}
	
	@Post
	@Path("/pesquisarVendas")
	public void pesquisar(Integer numeroCota, TipoVendaEncalhe tipoVenda, Date periodoInicial, 
						  Date periodoFinal,String sortname, String sortorder, int rp, int page){
		
		
		FiltroVendaEncalheDTO filtro = new FiltroVendaEncalheDTO(numeroCota,tipoVenda,periodoInicial, periodoFinal);
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		tratarFiltro(filtro);
		
		validarParametrosFiltro(filtro);
		
		List<VendaEncalheDTO> vendas = vendaEncalheService.buscarVendasProduto(filtro);
		
		if(vendas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long quantidade = vendaEncalheService.buscarQntVendasProduto(filtro);
		
		List<VendaEncalheVO> listaExibicaoGrid = new ArrayList<VendaEncalheVO>();
		
		BigDecimal totalGeral= BigDecimal.ZERO;
		
		VendaEncalheVO vendaEncalheVO = null;
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		for(VendaEncalheDTO dto : vendas){
			
			vendaEncalheVO = getVendaEncalheVO(dto);
			
			if( distribuidor.getDataOperacao().compareTo(DateUtil.removerTimestamp(dto.getDataVenda())) <= 0){
				vendaEncalheVO.setEdicaoExclusaoItem(true);
			}
			else{
				vendaEncalheVO.setEdicaoExclusaoItem(false);
			}
			
			listaExibicaoGrid.add(vendaEncalheVO);
			totalGeral = totalGeral.add(dto.getValoTotalProduto());
		}
		
		TableModel<CellModelKeyValue<VendaEncalheVO>> tableModel = new TableModel<CellModelKeyValue<VendaEncalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaExibicaoGrid));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(quantidade.intValue());
		
		ResultadoVendaEncalheVO resultadoVendaEncalheVO = new ResultadoVendaEncalheVO(tableModel,CurrencyUtil.formatarValor(totalGeral));
		
		result.use(Results.json()).withoutRoot().from(resultadoVendaEncalheVO).recursive().serialize();
			
	}
	
	private VendaEncalheVO getVendaEncalheVO(VendaEncalheDTO dto) {
		
		VendaEncalheVO vendaEncalheVO = new VendaEncalheVO();
		
		vendaEncalheVO.setIdVenda(dto.getIdVenda());
		vendaEncalheVO.setDataVenda(DateUtil.formatarDataPTBR(dto.getDataVenda()));
		vendaEncalheVO.setCodigoProduto(tratarValor(dto.getCodigoProduto()));
		vendaEncalheVO.setNomeCota(tratarValor(dto.getNomeCota()));
		vendaEncalheVO.setNomeProduto(tratarValor(dto.getNomeProduto()));
		vendaEncalheVO.setNumeroCota(tratarValor(dto.getNumeroCota()));
		vendaEncalheVO.setNumeroEdicao(tratarValor(dto.getNumeroEdicao()));
		vendaEncalheVO.setPrecoDesconto(CurrencyUtil.formatarValor((dto.getPrecoDesconto())));
		vendaEncalheVO.setQntProduto( tratarValor(dto.getQntProduto()));
		vendaEncalheVO.setTipoVendaEncalhe( (dto.getTipoVendaEncalhe()!= null)?dto.getTipoVendaEncalhe().getVenda():"");
		vendaEncalheVO.setValoTotalProduto(CurrencyUtil.formatarValor(dto.getValoTotalProduto()));
		vendaEncalheVO.setCodigoBarras(tratarValor(dto.getCodigoBarras()));
		vendaEncalheVO.setQntDisponivelProduto(tratarValor(dto.getQntDisponivelProduto()));
		vendaEncalheVO.setFormaVenda(tratarValor(dto.getFormaVenda()));		
		vendaEncalheVO.setNomeUsuario((dto.getUsuario()!= null)? dto.getUsuario().getNome():"");
		vendaEncalheVO.setTipoVenda(dto.getTipoVendaEncalhe());
		vendaEncalheVO.setFormaComercializacao(dto.getFormaVenda());
	
		return vendaEncalheVO;
	}
	private String tratarValor(Object valor){
		return (valor == null)?"":valor.toString();
	}
	
	private void validarParametrosVenda(List<VendaEncalheDTO> listaVendas,Long numeroCota, Date dataDebito){
	
		if(listaVendas == null || listaVendas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Pelo menos um produto deve ser informado para venda!");
		}
		
		validarFormatoData();
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if(numeroCota == null){
			mensagensValidacao.add("O preenchimento do campo [Cota] é obrigatório!");
		}
		
		if(dataDebito == null){
			mensagensValidacao.add("O preenchimento do campo [Data Vencimento] é obrigatório!");
		}
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	}
	
	private void validarParametrosFiltro(FiltroVendaEncalheDTO filtro){
		
		validarFormatoData();
		
		if(filtro.getPeriodoInicial() != null && filtro.getPeriodoFinal() == null){
			throw new ValidacaoException(TipoMensagem.WARNING,"O período preenchido no campo [Até] está inválido!");
		}
		
		if(filtro.getPeriodoInicial() == null && filtro.getPeriodoFinal() != null){
			throw new ValidacaoException(TipoMensagem.WARNING,"O período preenchido no campo [Período] está inválido!");
		}
		
		if(DateUtil.isDataInicialMaiorDataFinal(filtro.getPeriodoInicial(), filtro.getPeriodoFinal())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O período preenchido nos campos [Período] [Até] está inválido!");
		}

	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroVendaEncalheDTO filtro) {

		FiltroVendaEncalheDTO filtroSession = (FiltroVendaEncalheDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private void configurarPaginacaoPesquisa(FiltroVendaEncalheDTO filtro,String sortorder,String sortname,int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroVendaEncalheDTO.OrdenacaoColuna.values(),sortname));
		}
	}
	
	/**
	 * Valida o formato das datas utilizadas na tela de cadastro de cota
	 */
	private void validarFormatoData(){
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (validator.hasErrors()) {
			
			for (Message message : validator.getErrors()) {
				
				if(message.getCategory().equals("inicioPeriodo")){
					mensagensValidacao.add("O campo [Périodo] está inválido");
				}
				
				if(message.getCategory().equals("fimPeriodo")){
					mensagensValidacao.add("O campo [Até] está inválido");
				}
			}
			
			if (!mensagensValidacao.isEmpty()){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
			}
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
		
		FiltroVendaEncalheDTO filtro = this.obterFiltroExportacao();
		
		List<VendaEncalheDTO> vendas = vendaEncalheService.buscarVendasProduto(filtro);
		
		if(vendas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<VendaEncalheVO> listaExibicaoGrid = new ArrayList<VendaEncalheVO>();
		
		BigDecimal totalGeral= BigDecimal.ZERO;
		
		for(VendaEncalheDTO dto : vendas){
			
			listaExibicaoGrid.add(getVendaEncalheVO(dto));
			totalGeral = totalGeral.add(dto.getValoTotalProduto());
		}
		
		ResultadoVendaEncalheVO resultadoVendaEncalheVO = new ResultadoVendaEncalheVO(null,CurrencyUtil.formatarValor(totalGeral));
		
		FileExporter.to("venda-encalhes", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,resultadoVendaEncalheVO, 
				listaExibicaoGrid, VendaEncalheVO.class, this.httpServletResponse);
	}
	
	private FiltroVendaEncalheDTO obterFiltroExportacao() {
		
		FiltroVendaEncalheDTO filtro = 
			(FiltroVendaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
				filtro.setOrdenacaoColuna(null);
			}
			
			if(filtro.getNumeroCota()!= null){
				
				Cota cota = cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
				
				Pessoa pessoa  = cota.getPessoa();
				
				if(pessoa instanceof PessoaFisica){
					filtro.setNomeCota(((PessoaFisica)pessoa).getNome());
				}
				else if (pessoa instanceof PessoaJuridica){
					filtro.setNomeCota(((PessoaJuridica)pessoa).getRazaoSocial());
				}
			}
		}
		
		return filtro;
	}
	
	@Get("/reimprimirComprovanteVenda/{idVenda}")
	public Download reimprimirComprovanteVenda(Long idVenda){		
		byte[] relatorio =  vendaEncalheService.geraImpressaoComprovanteVenda(idVenda);		
		return new ByteArrayDownload(relatorio,"application/pdf", "comprovanteVenda.pdf",true);
	}
}
	
