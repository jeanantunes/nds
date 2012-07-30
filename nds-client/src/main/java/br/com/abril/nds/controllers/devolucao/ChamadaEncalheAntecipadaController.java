package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ChamadaEncalheAntecipadaPesCotaVO;
import br.com.abril.nds.client.vo.ChamadaEncalheAntecipadaVO;
import br.com.abril.nds.client.vo.ResultadoChamadaEncalheAntecipadaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.InfoChamdaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaAntecipadaEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável por controlar as ações da pagina de Chamada de Encalhe Antecipada.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/chamadaEncalheAntecipada")
public class ChamadaEncalheAntecipadaController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroChamadaAntecipadaEncalhe";
	
	private static final String LISTA_PESQUISA_COTA = "listaPesquisaCotas";

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ChamadaAntecipadaEncalheService chamadaAntecipadaEncalheService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private PdvService pdvService;
	
	@Path("/")
	public void index(){
		
		result.include("listaFornecedores",obterFornecedores(null));
		result.include("listaBoxes",obterBoxs(null));
		carregarRota();
		carregarRoteiro();
		carregarMunicipios();
		carregarTipoPonto();
	}
	
	private void carregarTipoPonto(){
		
		List<TipoPontoPDV> listaTipoPontoPDV = pdvService.obterTiposPontoPDVPrincipal();
		
		List<ItemDTO<Long, String>> tipoPonto = new ArrayList<ItemDTO<Long,String>>();
		
		for (TipoPontoPDV tp : listaTipoPontoPDV) {
			tipoPonto.add(new ItemDTO<Long, String>(tp.getCodigo(), tp.getDescricao()));
		}
		
		result.include("listaTipoPonto",tipoPonto);
	}
	
	private void carregarMunicipios(){
		
		List<Endereco> enderecoPdvPrincipal = pdvService.buscarMunicipiosPdvPrincipal();
		
		List<ItemDTO<Integer, String>> municipios = new ArrayList<ItemDTO<Integer,String>>();
		
		for (Endereco tp : enderecoPdvPrincipal) {
			municipios.add(new ItemDTO<Integer, String>(tp.getCodigoCidadeIBGE(),tp.getCidade()));
		}
		
		result.include("listaMunicipios",municipios);
	}
	
	/**
	 * Pesquisa fornecedores relacionados a um determinado produto
	 * 
	 * @param codigoProduto
	 */
	@Post
	@Path("/pesquisarFornecedor")
	public void pesquisarFornecedorPorProduto(String codigoProduto){
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = obterFornecedores(codigoProduto);
		
		result.use(Results.json()).from(listaFornecedoresCombo, "result").recursive().serialize();
	}
	
	/**
	 * Pesquisa boxs relacionados a um determinado produto 
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 */
	@Post
	@Path("/pesquisarBox")
	public void pesquisarBoxPoProduto(String codigoProduto,Long numeroEdicao ){
			
		List<ItemDTO<Long, String>> listaBoxCombo = obterBoxs(codigoProduto);
		
		result.use(Results.json()).from(listaBoxCombo, "result").recursive().serialize();
	}
	
	/**
	 * Pesquisa a data programada para recolhimento de encalhe de um produto edição
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 */
	@Post
	@Path("/pesquisarDataProgramada")
	public void pesquisarpesquisarDataProgramadaEdicao(String codigoProduto, Long numeroEdicao){
		
		Date date  = chamadaAntecipadaEncalheService.obterDataRecolhimentoPrevista(codigoProduto, numeroEdicao);
		
		String dataFormatada = (date == null)?"":DateUtil.formatarDataPTBR(date);
		
		result.use(Results.json()).from(dataFormatada, "result").recursive().serialize();
	}
	
	/**
	 * Pesquisa as cotas relacionadas a um produto que seja candidato a antecipação de colhimento de encalhe.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param box
	 * @param fornecedor
	 */
	@Post
	@Path("/pesquisar")
	public void pesquisarCotasPorProduto(String codigoProduto,Long numeroEdicao,Long box,Long fornecedor, 
										 Long rota,Long roteiro,boolean programacaoRealizada,Integer municipio,Long tipoPontoPDV,
										 String sortorder, String sortname, int page, int rp){
		
		validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
		FiltroChamadaAntecipadaEncalheDTO filtro = 
				new FiltroChamadaAntecipadaEncalheDTO(codigoProduto,numeroEdicao,box,fornecedor,
													  rota,roteiro,programacaoRealizada, municipio,tipoPontoPDV);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
	}
	
	/**
	 * Obtem o valor sumarizado das cotas e exemplares referente o filtro de pesquisa da tela.
	 */
	@Post
	@Path("/obterTotalCotaExemplar")
	public void obterTotalDeCotaEExemplar(){
		
		FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
		
		InfoChamdaAntecipadaEncalheDTO dto = chamadaAntecipadaEncalheService.obterInfoChamdaAntecipadaEncalheSumarizado(filtro);
		
		ResultadoChamadaEncalheAntecipadaVO resultado = new ResultadoChamadaEncalheAntecipadaVO();
		
		if(dto!= null){
			resultado.setQntTotalExemplares(dto.getTotalExemplares());
			resultado.setQtdeTotalCotas(dto.getTotalCotas());
		}
		else{
			resultado.setQntTotalExemplares(BigDecimal.ZERO);
			resultado.setQtdeTotalCotas(BigDecimal.ZERO);
		}
		
		result.use(Results.json()).from(resultado, "result").recursive().serialize();
	}
	
	/**
	 * Retorna o filtro da sessão sem as informações de paginação e ordenação. 
	 * 
	 * @return
	 */
	private FiltroChamadaAntecipadaEncalheDTO getFiltroSessionSemPaginacao() {
		
		FiltroChamadaAntecipadaEncalheDTO filtro = 
				(FiltroChamadaAntecipadaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
				filtro.setOrdenacaoColuna(null);
			}
		}
		
		return filtro;
	}
	
	/**
	 * Obtem a quantidade de exemplares de uma determinada cota, que esteja sujeita a antecipação de recolhimento de encalhe.
	 * 
	 * @param numeroCota
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param fornecedor
	 */
	@Post
	@Path("/obterQuantidadeExemplares")
	public void obterQuantidadeExemplaresPorCota(Integer numeroCota, String codigoProduto, Long numeroEdicao,Long fornecedor,
												 boolean programacaoRealizada){
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO(codigoProduto,numeroEdicao,null,fornecedor,numeroCota);
		
		BigDecimal quantidade = BigDecimal.ZERO;
		
		if(!programacaoRealizada){
			
			quantidade = chamadaAntecipadaEncalheService.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);
			
			result.use(CustomMapJson.class).put("quantidade", quantidade.intValue()).serialize();
		}
		else{
			
			ChamadaAntecipadaEncalheDTO chamada = chamadaAntecipadaEncalheService.obterChamadaEncalheAntecipada(filtro);
			quantidade = chamada.getQntExemplares();	
			
			result.use(CustomMapJson.class).put("quantidade", quantidade.intValue())
										   .put("idChamadaEncalhe", chamada.getCodigoChamadaEncalhe()).serialize();
		}
	}
	
	/**
	 * Grava a antecipação de recolhimento de encalhe das cotas do grid de pesquisa de cotas.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 */
	@Post
	@Path("/gravarCotasPesquisa")
	public void gravarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
									String dataRecolhimento,String codigoProduto,
									Long numeroEdicao,String dataProgramada){
		
		validarDataRecolhimento(dataRecolhimento);
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para chamda antecipada de encalhe!");
		
		gravarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
	}
	
	
	
	/**
	 * 
	 * Grava a antecipação de recolhimento de encalhe das cotas do grid de pesquisa.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 * @param gravarTodos
	 */
	@Post
	@Path("/gravarCotas")
	public void gravarCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
							String dataRecolhimento, String codigoProduto, 
							Long numeroEdicao,String dataProgramada,String gravarTodos){
		
		validarDataRecolhimento(dataRecolhimento);
		
		if(!gravarTodos.isEmpty()){
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			filtro.setDataAntecipacao(DateUtil.parseDataPTBR(dataRecolhimento));
			filtro.setDataProgramada(dataProgramada);
			
			chamadaAntecipadaEncalheService.gravarChamadaAntecipacaoEncalheProduto(filtro);
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
											"result").recursive().serialize();
		}
		else{
			
			gravarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
			
		}	
	}
	
	
	/**
	 * 
	 * Reprogramar a antecipação de recolhimento de encalhe das cotas do grid de pesquisa.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 * @param gravarTodos
	 */
	@Post
	@Path("/reprogramarCotas")
	public void reprogramarCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
								String dataRecolhimento, String codigoProduto, 
								Long numeroEdicao,String dataProgramada,String gravarTodos){
		
		validarDataRecolhimento(dataRecolhimento);
		
		if(!gravarTodos.isEmpty()){
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			filtro.setDataAntecipacao(DateUtil.parseDataPTBR(dataRecolhimento));
			filtro.setDataProgramada(dataProgramada);
			
			chamadaAntecipadaEncalheService.reprogramarChamadaAntecipacaoEncalheProduto(filtro);
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
											"result").recursive().serialize();
		}
		else{
		
			reprogramarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
			
		}	
	}
	
	@Post
	@Path("/cancelarChamdaEncalheCotas")
	public void cancelarChamdaEncalheCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
											String codigoProduto,Long numeroEdicao,String cancelarTodos){
		
		if(!cancelarTodos.isEmpty()){
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			
			chamadaAntecipadaEncalheService.cancelarChamadaAntecipadaCota(filtro);
		
		}
		else{
			
			InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																								  null,codigoProduto,
																								  numeroEdicao,null);
			
			chamadaAntecipadaEncalheService.cancelarChamadaAntecipadaCota(infoChamdaAntecipadaEncalheDTO);
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post
	@Path("/cancelarChamdaEncalheCotasPesquisa")
	public void cancelarChamdaEncalheCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
												   String codigoProduto,Long numeroEdicao){
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																								  null,codigoProduto,
																								  numeroEdicao,null);
			
		chamadaAntecipadaEncalheService.cancelarChamadaAntecipadaCota(infoChamdaAntecipadaEncalheDTO);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();
	}
	
	/**
	 * Reprogramar a antecipação de recolhimento de encalhe das cotas do grid de pesquisa de cotas.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 */
	@Post
	@Path("/reprogramarCotasPesquisa")
	public void reprogramarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
										String dataRecolhimento,String codigoProduto,
										Long numeroEdicao,String dataProgramada){
		
		validarDataRecolhimento(dataRecolhimento);
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para reprogramação de chamda antecipada de encalhe!");
		
		reprogramarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
	}
	
	/**
	 * Grava informações referentes a chamada antecipada de encalhe de um produto.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 * @param numeroEdicao 
	 * @param codigoProduto 
	 */
	private void gravarChamadaEncalheAnteicipada(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
												String dataRecolhimento, String codigoProduto, 
												Long numeroEdicao,String dataProgramada){
		
		if(listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para gravação!");
		}
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																		dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
		
		chamadaAntecipadaEncalheService.gravarChamadaAntecipacaoEncalheProduto(infoChamdaAntecipadaEncalheDTO);
		
		result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();
	}
	
	
	/**
	 * Grava informações referentes a chamada antecipada de encalhe de um produto.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 * @param numeroEdicao 
	 * @param codigoProduto 
	 */
	private void reprogramarChamadaEncalheAnteicipada(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
												String dataRecolhimento, String codigoProduto, 
												Long numeroEdicao,String dataProgramada){
		
		if(listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para reprogramação!");
		}
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																		dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada);
		
		chamadaAntecipadaEncalheService.reprogramarChamadaAntecipacaoEncalheProduto(infoChamdaAntecipadaEncalheDTO);
		
		result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				"result").recursive().serialize();
	}
	
	/**
	 * Monta os dados para gravação de chamada de encalhe antecipada.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @return InfoChamdaAntecipadaEncalheDTO
	 */
	private InfoChamdaAntecipadaEncalheDTO getInfoChamadaEncalhe(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
																	String dataRecolhimento, String codigoProduto, 
																	Long numeroEdicao,String dataProgramada) {
		
		InfoChamdaAntecipadaEncalheDTO infoEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		infoEncalheDTO.setCodigoProduto(codigoProduto);
		infoEncalheDTO.setDataAntecipacao( (dataRecolhimento==null)?null: DateUtil.parseDataPTBR(dataRecolhimento));
		infoEncalheDTO.setNumeroEdicao(numeroEdicao);
		infoEncalheDTO.setDataProgramada( (dataProgramada== null)?null:DateUtil.parseDataPTBR(dataProgramada));
		
		List<ChamadaAntecipadaEncalheDTO> listaChamadaAntecipadaEncalheDTOs = 
				new ArrayList<ChamadaAntecipadaEncalheDTO>();
		
		for(ChamadaEncalheAntecipadaVO vo : listaChamadaEncalheAntecipada){
			listaChamadaAntecipadaEncalheDTOs.add(
					new ChamadaAntecipadaEncalheDTO(
							Integer.parseInt(vo.getNumeroCota()),
							new BigDecimal(vo.getQntExemplares()),
							vo.getCodigoChamdaEncalhe()));
		}
		
		infoEncalheDTO.setChamadasAntecipadaEncalhe(listaChamadaAntecipadaEncalheDTOs);
		
		return infoEncalheDTO;
	}

	/**
	 * Valida a data informada para antecipação de encalhe
	 * 
	 * @param dataRecolhimento
	 */
	private void validarDataRecolhimento(String dataRecolhimento){
		
		if (dataRecolhimento == null 
				|| dataRecolhimento.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data Antecipada] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataRecolhimento)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Antecipada inválida");
		}
	}
	
	/**
	 * Verifica se existe alguma cota duplicada para antecipação de encalhe.
	 * 
	 * @param listaCotas
	 */
	@SuppressWarnings("unchecked")
	private void validarCotasDuplicadas(List<ChamadaEncalheAntecipadaVO> listaCotas, String mensagem) {
		
		if (listaCotas == null) {
			
			return;
		}
		
		Collections.sort(listaCotas, new BeanComparator("numeroCota"));
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		ChamadaEncalheAntecipadaVO ultimoChamadaEncalheAntecipadaVO = null;
		
		for (ChamadaEncalheAntecipadaVO antecipadaVO : listaCotas) {
			
			String numeroCota = antecipadaVO.getNumeroCota();
			
			if (numeroCota == null) {
				
				continue;
			}
			
			if (ultimoChamadaEncalheAntecipadaVO != null) {
				
				if (numeroCota.equals(ultimoChamadaEncalheAntecipadaVO.getNumeroCota())) {
					
					linhasComErro.add(ultimoChamadaEncalheAntecipadaVO.getId());
					linhasComErro.add(antecipadaVO.getId());
				}
			}
			
			ultimoChamadaEncalheAntecipadaVO = antecipadaVO;
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING,mensagem);
			
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * Monta a estrutura de dados para renderização do grid de pesquisa de Cotas.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 */
	@Post
	@Path("/montarPesquisaCotas")
	public void montarListaPesquisaCota(String codigoProduto,Long numeroEdicao) {
		
		validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
		List<ChamadaEncalheAntecipadaVO> listaPesquisaCota= new ArrayList<ChamadaEncalheAntecipadaVO>();
		
		int qtdeInicialPadrao = 30;
		
		for (int indice = 0; indice < qtdeInicialPadrao; indice++) {
			
			ChamadaEncalheAntecipadaVO encalheAntecipadaVO = new ChamadaEncalheAntecipadaVO();
			encalheAntecipadaVO.setId(Long.valueOf(indice) );
			encalheAntecipadaVO.setCodigoProduto(codigoProduto);
			encalheAntecipadaVO.setNumeroEdicao(numeroEdicao);
			
			listaPesquisaCota.add(encalheAntecipadaVO);
		}
		
		TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel =
						new TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPesquisaCota));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroChamadaAntecipadaEncalheDTO filtro) {

		FiltroChamadaAntecipadaEncalheDTO filtroSession = (FiltroChamadaAntecipadaEncalheDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de dividas geradas.
	 * 
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = 
				chamadaAntecipadaEncalheService.obterInfoChamdaAntecipadaEncalhe(filtro);
		
		if (infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe() == null 
				|| infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipadaVO = 
				getListaChamadaEncalheAntecipadaVO(infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe());

		TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel = new TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaChamadaEncalheAntecipadaVO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(infoChamdaAntecipadaEncalheDTO.getTotalRegistros().intValue());
		
		ResultadoChamadaEncalheAntecipadaVO resultadoChamadaEncalheAntecipadaVO = new ResultadoChamadaEncalheAntecipadaVO(tableModel,null,null);
		
		result.use(Results.json()).withoutRoot().from(resultadoChamadaEncalheAntecipadaVO).recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de ChamadaEncalheAntecipadaVO para parse do grid.
	 * 
	 * @param listaChamdaAntecipadaEncalheDTO
	 * 
	 * @return List<ChamadaEncalheAntecipadaVO>
	 */
	private List<ChamadaEncalheAntecipadaVO> getListaChamadaEncalheAntecipadaVO(List<ChamadaAntecipadaEncalheDTO> listaChamdaAntecipadaEncalheDTO){
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipadaVO = new LinkedList<ChamadaEncalheAntecipadaVO>();
		
		ChamadaEncalheAntecipadaVO chamadaEncalheAntecipadaVO = null;
		
		for (ChamadaAntecipadaEncalheDTO dto : listaChamdaAntecipadaEncalheDTO) {
			
			chamadaEncalheAntecipadaVO =  new ChamadaEncalheAntecipadaVO();
			chamadaEncalheAntecipadaVO.setBox(dto.getCodBox() + " - " + dto.getNomeBox());
			chamadaEncalheAntecipadaVO.setNomeCota(dto.getNomeCota());
			chamadaEncalheAntecipadaVO.setNumeroCota( String.valueOf(dto.getNumeroCota()));
			chamadaEncalheAntecipadaVO.setQntExemplares(String.valueOf(dto.getQntExemplares().intValue()));
			chamadaEncalheAntecipadaVO.setCodigoChamdaEncalhe(dto.getCodigoChamadaEncalhe());
			
			listaChamadaEncalheAntecipadaVO.add(chamadaEncalheAntecipadaVO);
		}
		
		return listaChamadaEncalheAntecipadaVO;
	}
	
	
	/**
	 * Configura paginação do grid de pesquisa.
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroChamadaAntecipadaEncalheDTO filtro,String sortorder,String sortname,int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroChamadaAntecipadaEncalheDTO.OrdenacaoColuna.values(),sortname));
		}
	}
	
	
	/**
	 * Valida os parâmetros obrigatórios do filtro de pesquisa 
	 * @param codigoProduto
	 * @param numeroEdicao
	 */
	private void validarParametrosPesquisa(String codigoProduto,Long numeroEdicao){
		
		if(codigoProduto == null || codigoProduto.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Código] é obrigatório.");
		}
		
		if(numeroEdicao == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Edição] é obrigatório.");
		}
	}
	
	/**
	 * Retorna uma lista de fornecedores relacionados com um produto.
	 * @param codigoProduto
	 * @return  List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> obterFornecedores(String codigoProduto){
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorProduto(codigoProduto, GrupoFornecedor.PUBLICACAO);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return listaFornecedoresCombo;
	}
	
	/**
	 * Retorna uma lista de boxes relacionadas com um produto.
	 * @param codigoProduto
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> obterBoxs(String codigoProduto){
		
		List<Box> listaBox = boxService.obterBoxPorProduto(codigoProduto);
		
		List<ItemDTO<Long, String>> listaBoxCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : listaBox) {
			listaBoxCombo.add(new ItemDTO<Long, String>(box.getId(), box.getCodigo() + " - " + box.getNome()));
		}
		
		return listaBoxCombo;
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType, String dataProgaramada) throws IOException {
		
		FiltroChamadaAntecipadaEncalheDTO filtro = this.obterFiltroExportacao();
		filtro.setDataProgramada(dataProgaramada);
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = 
				chamadaAntecipadaEncalheService.obterInfoChamdaAntecipadaEncalhe(filtro);
		
		if (infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe() == null 
				|| infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipadaVO = 
				getListaChamadaEncalheAntecipadaVO(infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe());
		
		FileExporter.to("chamada-encalhe-antecipada", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, infoChamdaAntecipadaEncalheDTO, 
					listaChamadaEncalheAntecipadaVO, ChamadaEncalheAntecipadaVO.class, this.httpServletResponse);
		
	}
	
	/**
	 * Valida se existe cotas repetidas para exportação das informações.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 */
	@Post
	@Path("/validarCotasPesquisa")
	public void validarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada){
		
		if (listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para exportação/impressão!");
		
		session.setAttribute(LISTA_PESQUISA_COTA, listaChamadaEncalheAntecipada);
		
		result.use(Results.json()).from("").recursive().serialize();
	}
	
	/**
	 * Realiza a exportação do grid de pesquisa de cotas
	 * 
	 * @param fileType
	 * @param dataProgaramada
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param fornecedor
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Get
	public void exportarPesquisaCotas(FileType fileType,String dataProgaramada,String codigoProduto, 
										Long numeroEdicao, Long fornecedor ) throws IOException{
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada = 
				(List<ChamadaEncalheAntecipadaVO>) session.getAttribute(LISTA_PESQUISA_COTA);
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setDataProgramada(dataProgaramada);
		filtro.setCodigoProduto(codigoProduto);
		filtro.setFornecedor(fornecedor);
		filtro.setNumeroEdicao(numeroEdicao);
		
		atribuirValoresFIltro(filtro);
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		Integer totalExemplares = 0;
		 
		for(ChamadaEncalheAntecipadaVO vo : listaChamadaEncalheAntecipada){
			totalExemplares += Integer.parseInt(vo.getQntExemplares());
		}
		
		infoChamdaAntecipadaEncalheDTO.setTotalCotas(new BigDecimal(listaChamadaEncalheAntecipada.size()));
		infoChamdaAntecipadaEncalheDTO.setTotalExemplares(new BigDecimal(totalExemplares));
		
		session.removeAttribute(LISTA_PESQUISA_COTA);
		
		FileExporter.to("chamada-encalhe-antecipada", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), 
							filtro, infoChamdaAntecipadaEncalheDTO, 
							montarListaChamadaEncalheAntecipadaPesCotaVO(listaChamadaEncalheAntecipada), 
							ChamadaEncalheAntecipadaPesCotaVO.class, this.httpServletResponse);
		
	}
	
	/**
	 * Retorna uma lista de ChamadaEncalheAntecipadaPesCotaVO para exportação dos dados
	 * 
	 * @param list
	 * 
	 * @return List<ChamadaEncalheAntecipadaPesCotaVO>
	 */
	private List<ChamadaEncalheAntecipadaPesCotaVO> montarListaChamadaEncalheAntecipadaPesCotaVO(List<ChamadaEncalheAntecipadaVO> list){
		
		List<ChamadaEncalheAntecipadaPesCotaVO> listRetorno = new ArrayList<ChamadaEncalheAntecipadaPesCotaVO>();
		
		for(ChamadaEncalheAntecipadaVO vo : list){
			listRetorno.add(new ChamadaEncalheAntecipadaPesCotaVO(vo.getNumeroCota(),vo.getNomeCota(),vo.getQntExemplares()));
		}
		
		return listRetorno;
	}
	
	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroChamadaAntecipadaEncalheDTO obterFiltroExportacao() {
		
		FiltroChamadaAntecipadaEncalheDTO filtro = 
			(FiltroChamadaAntecipadaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
				filtro.setOrdenacaoColuna(null);
			}
			
			atribuirValoresFIltro(filtro);
		}
		
		return filtro;
	}
	
	/**
	 * Atribui valores do filtro para exportação dos grids
	 * 
	 * @param filtro
	 */
	private void atribuirValoresFIltro(FiltroChamadaAntecipadaEncalheDTO filtro){
		
		if (filtro.getCodigoProduto() != null) {
			
			String produto = produtoService.obterNomeProdutoPorCodigo(filtro.getCodigoProduto());
			filtro.setNomeProduto(produto);
		}
		
		if(filtro.getFornecedor()!= null){
			
			Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(filtro.getFornecedor());
			filtro.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
		}
		
		if(filtro.getBox()!= null){
			Box box = boxService.buscarPorId(filtro.getBox());
			
			if(box!= null){
				filtro.setDescBox(box.getCodigo() + " - " + box.getNome());
			}
		}
		
		if(filtro.getRota()!= null){
			Rota rota = roteirizacaoService.buscarRotaPorId(filtro.getRota());
			if(rota!= null){
				filtro.setDescRota(rota.getCodigoRota());
			}
		}
		
		if(filtro.getRoteiro()!= null){
			Roteiro roteiro = roteirizacaoService.buscarRoteiroPorId(filtro.getRoteiro());
			if(roteiro!= null){
				filtro.setDescRoteiro(roteiro.getDescricaoRoteiro());
			}
		}
		
		if(filtro.isProgramacaoCE()){
			filtro.setDescComCE("Sim");
		}
		else{
			filtro.setDescComCE("Não");
		}
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	@Post
	public void recarregarListaRotas(Long roteiro){
		
		List<Rota> rotas = null;
		
		if(roteiro!= null){

			rotas = roteirizacaoService.buscarRotaPorRoteiro(roteiro);
		}
		else{
			
			rotas = roteirizacaoService.buscarRotas();
		}
	
		result.use(Results.json()).from(getRotas(rotas), "result").recursive().serialize();
	}
	
	@Post
	public void recarregarRoteiroRota(Long idBox){
		
		List<Roteiro> roteirosBox = null;
		
		List<Rota> rotas = null;
		
		if(idBox!= null){
			
			roteirosBox = roteirizacaoService.buscarRoteiroDeBox(idBox);
			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		}
		else{
			
			roteirosBox = roteirizacaoService.buscarRoteiros();
			rotas = roteirizacaoService.buscarRotas();
		}
		
		result.use(CustomMapJson.class).put("rotas", getRotas(rotas)).put("roteiros", getRoteiros(roteirosBox)).serialize();
	}
	
	/**
	 * Carrega a lista de Rotas
	 */
	private void carregarRota(){
		
		List<Rota> rotas = roteirizacaoService.buscarRotas();
			
		result.include("listaRotas",getRotas(rotas));
	} 
	
	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getRotas(List<Rota> rotas){
		
		List<ItemDTO<Long, String>> listaRotas = new ArrayList<ItemDTO<Long,String>>();
		
		for(Rota rota : rotas){
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(),rota.getCodigoRota()+" - "+ rota.getDescricaoRota()));
		}
		
		return listaRotas;
	}
	
	/**
	 * Carrega as listas de Roteiros
	 */
	private void carregarRoteiro(){
		
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiros();
			
		result.include("listaRoteiros",getRoteiros(roteiros));
	}
	
	/**
	 * Retorna uma lista de roteiros no formato ItemDTO
	 * @param roteiros - lista de roteiros
	 * @return List<ItemDTO<Long, String>> 
	 */
	private List<ItemDTO<Long, String>> getRoteiros(List<Roteiro> roteiros){
		
		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long,String>>();
		
		for(Roteiro roteiro : roteiros){
			
			listaRoteiros.add(new ItemDTO<Long, String>(roteiro.getId(),roteiro.getDescricaoRoteiro()));
		}
		return listaRoteiros;
	}
	
}
