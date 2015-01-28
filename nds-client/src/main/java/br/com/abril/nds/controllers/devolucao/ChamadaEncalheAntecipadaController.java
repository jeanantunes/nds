package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ChamadaEncalheAntecipadaPesCotaVO;
import br.com.abril.nds.client.vo.ChamadaEncalheAntecipadaVO;
import br.com.abril.nds.client.vo.ResultadoChamadaEncalheAntecipadaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.InfoChamdaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaAntecipadaEncalheService;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
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
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável por controlar as ações da pagina de Chamada de Encalhe Antecipada.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/chamadaEncalheAntecipada")
@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO)
public class ChamadaEncalheAntecipadaController extends BaseController {
	
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
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
    private ChamadaEncalheService chamadaEncalheService;
	
	@Path("/")
	public void index() {
		
		result.include("listaFornecedores",obterFornecedores(null));
		result.include("listaBoxes", obterBoxs(null));
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
		
		List<ItemDTO<String, String>> municipios = enderecoService.buscarMunicipioAssociadasCota();
		
		result.include("listaMunicipios",municipios);
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
	public void pesquisarDataProgramadaEdicao(String codigoProduto, Long numeroEdicao){
		
		Date date  = chamadaAntecipadaEncalheService.obterDataRecolhimentoReal(codigoProduto, numeroEdicao);
		
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
	public void pesquisarCotasPorProduto(String codigoProduto, Long numeroEdicao, Long box, Long fornecedor, 
										 Long rota, Long roteiro, boolean programacaoRealizada, String municipio, String tipoPontoPDV,
										 String sortorder, String sortname, int page, int rp){
		
		validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO(codigoProduto,numeroEdicao,box,fornecedor, rota,roteiro,programacaoRealizada, municipio,tipoPontoPDV);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		ResultadoChamadaEncalheAntecipadaVO vo = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(vo).recursive().serialize();
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
												 boolean programacaoRealizada,String municipio, String tipoPontoPDV){
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setNumeroCota(numeroCota);
		filtro.setCodigoProduto(codigoProduto);
		filtro.setNumeroEdicao(numeroEdicao);
		filtro.setFornecedor(fornecedor);
		filtro.setDescMunicipio(municipio);
		filtro.setCodTipoPontoPDV(tipoPontoPDV);
		
		BigInteger quantidade = BigInteger.ZERO;
		Map<String, Object> mapa = new TreeMap<String, Object>();
		
		if(!programacaoRealizada){
			
			quantidade = chamadaAntecipadaEncalheService.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);
		}
		else{
			
			ChamadaAntecipadaEncalheDTO chamada = chamadaAntecipadaEncalheService.obterChamadaEncalheAntecipada(filtro);
			quantidade = chamada.getQntExemplares();	
			
			mapa.put("idChamadaEncalhe", chamada.getCodigoChamadaEncalhe());
		}
		
		mapa.put("quantidade", quantidade.intValue());
		result.use(CustomJson.class).from(mapa).serialize();
	}
	
	/**
	 * Grava a antecipação de recolhimento de encalhe das cotas do grid de pesquisa de cotas.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 * @param dataRecolhimento
	 */
	@Post
	@Path("/gravarCotasPesquisa")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void gravarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
									String dataRecolhimento,String codigoProduto,
									Long numeroEdicao,String dataProgramada, boolean recolhimentoFinal){
		
		validarDataRecolhimento(dataRecolhimento);
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para chamda antecipada de encalhe!");
		
		gravarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada, recolhimentoFinal);
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
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void gravarCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
							List<ChamadaAntecipadaEncalheDTO> chamadasNaoSelecionadas,
							String dataRecolhimento, String codigoProduto, 
							Long numeroEdicao,String dataProgramada,String gravarTodos, 
							boolean recolhimentoFinal) {
		
		validarDataRecolhimento(dataRecolhimento);
		
		// validar se pode transfererir a data antecipada a matriz de recolhimento. 
		
		
		if (Boolean.parseBoolean(gravarTodos)) {
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			filtro.setDataAntecipacao(DateUtil.parseDataPTBR(dataRecolhimento));
			filtro.setDataProgramada(dataProgramada);
			filtro.setRecolhimentoFinal(recolhimentoFinal);
			filtro.setChamadasNaoSelecionadas(chamadasNaoSelecionadas);
			
			chamadaAntecipadaEncalheService.gravarChamadaAntecipacaoEncalheProduto(filtro);

			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
		
		} else{
			
			gravarChamadaEncalheAnteicipada(
				listaChamadaEncalheAntecipada, dataRecolhimento, codigoProduto, numeroEdicao, 
					dataProgramada, recolhimentoFinal);
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
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void reprogramarCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
								String dataRecolhimento, 
								String codigoProduto, 
								Long numeroEdicao, 
								String dataProgramada, 
								String gravarTodos,
								boolean recolhimentoFinal) {
		
		validarDataRecolhimento(dataRecolhimento);
		
		if (Boolean.parseBoolean(gravarTodos)) {
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			filtro.setDataAntecipacao(DateUtil.parseDataPTBR(dataRecolhimento));
			filtro.setDataProgramada(dataProgramada);
			filtro.setRecolhimentoFinal(recolhimentoFinal);
			chamadaAntecipadaEncalheService.reprogramarChamadaAntecipacaoEncalheProduto(filtro);
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
		}
		else{
		
			reprogramarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada, dataRecolhimento, codigoProduto, numeroEdicao, dataProgramada, recolhimentoFinal);
			
		}	
	}
	
	@Post
	@Path("/cancelarChamdaEncalheCotas")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void cancelarChamdaEncalheCotas(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
											String codigoProduto,Long numeroEdicao,String cancelarTodos, 
											String dataProgramada,
											boolean recolhimentoFinal){
		
		if(Boolean.parseBoolean(cancelarTodos)){
			
			FiltroChamadaAntecipadaEncalheDTO filtro = getFiltroSessionSemPaginacao();
			filtro.setDataAntecipacao((dataProgramada==null) ? null: DateUtil.parseDataPTBR(dataProgramada));
			chamadaAntecipadaEncalheService.cancelarChamadaAntecipadaCota(filtro);
		
		} else {
			
		    if(listaChamadaEncalheAntecipada == null) {
	            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para cancelar!");
	        }
		    
			InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																								  null,codigoProduto,
																								  numeroEdicao, dataProgramada, recolhimentoFinal);
			
			chamadaAntecipadaEncalheService.cancelarChamadaAntecipadaCota(infoChamdaAntecipadaEncalheDTO);
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@Post
	@Path("/cancelarChamdaEncalheCotasPesquisa")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void cancelarChamdaEncalheCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
												   String codigoProduto,Long numeroEdicao, String dataProgramada, boolean recolhimentoFinal){
		

        if(listaChamadaEncalheAntecipada == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para cancelar!");
        }
	    
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																								  null,codigoProduto,
																								  numeroEdicao, dataProgramada, recolhimentoFinal);
			
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
	@Rules(Permissao.ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO)
	public void reprogramarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada,
										String dataRecolhimento,String codigoProduto,
										Long numeroEdicao,String dataProgramada,
										boolean recolhimentoFinal){
		
		validarDataRecolhimento(dataRecolhimento);
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para reprogramação de chamda antecipada de encalhe!");
		
		reprogramarChamadaEncalheAnteicipada(listaChamadaEncalheAntecipada,dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada,recolhimentoFinal);
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
												Long numeroEdicao,String dataProgramada, boolean recolhimentoFinal){
		
		if(listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para gravação!");
		}
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																		dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada,recolhimentoFinal);
		
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
												Long numeroEdicao,String dataProgramada,
												boolean recolhimentoFinal){
		
		if(listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item foi selecionado para reprogramação!");
		}
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = getInfoChamadaEncalhe(listaChamadaEncalheAntecipada,
																		dataRecolhimento,codigoProduto,numeroEdicao,dataProgramada,
																		recolhimentoFinal);
		
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
																	Long numeroEdicao,String dataProgramada,
																	boolean recolhimentoFinal) {
		
		InfoChamdaAntecipadaEncalheDTO infoEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		infoEncalheDTO.setCodigoProduto(codigoProduto);
		infoEncalheDTO.setDataAntecipacao( (dataRecolhimento==null)?null: DateUtil.parseDataPTBR(dataRecolhimento));
		infoEncalheDTO.setNumeroEdicao(numeroEdicao);
		infoEncalheDTO.setDataProgramada( (dataProgramada== null)?null:DateUtil.parseDataPTBR(dataProgramada));
		infoEncalheDTO.setRecolhimentoFinal(recolhimentoFinal);
		
		List<ChamadaAntecipadaEncalheDTO> listaChamadaAntecipadaEncalheDTOs = new ArrayList<ChamadaAntecipadaEncalheDTO>();
		
		for(ChamadaEncalheAntecipadaVO vo : listaChamadaEncalheAntecipada){
			listaChamadaAntecipadaEncalheDTOs.add(new ChamadaAntecipadaEncalheDTO(
							Integer.parseInt(vo.getNumeroCota()),
							vo.getQntExemplares(),
							vo.getCodigoChamadaEncalhe(),
							vo.getIdLancamento()));
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
		
		if (dataRecolhimento == null || dataRecolhimento.trim().isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Data Antecipada] é obrigatório!");
			
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
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagem);
			
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
	public void montarListaPesquisaCota(String codigoProduto, Long numeroEdicao) {
		
		validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
		List<ChamadaEncalheAntecipadaVO> listaPesquisaCota = new ArrayList<ChamadaEncalheAntecipadaVO>();
		
		int qtdeInicialPadrao = 30;
		
		for (int indice = 0; indice < qtdeInicialPadrao; indice++) {
			
			ChamadaEncalheAntecipadaVO encalheAntecipadaVO = new ChamadaEncalheAntecipadaVO();
			encalheAntecipadaVO.setId(Long.valueOf(indice) );
			encalheAntecipadaVO.setCodigoProduto(codigoProduto);
			encalheAntecipadaVO.setNumeroEdicao(numeroEdicao);
			
			listaPesquisaCota.add(encalheAntecipadaVO);
		}
		
		TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel = new TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>>();
		
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

		FiltroChamadaAntecipadaEncalheDTO filtroSession = (FiltroChamadaAntecipadaEncalheDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
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
	private ResultadoChamadaEncalheAntecipadaVO efetuarConsulta(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = chamadaAntecipadaEncalheService.obterInfoChamdaAntecipadaEncalhe(filtro);
		
		if (infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe() == null || infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		}
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipadaVO = getListaChamadaEncalheAntecipadaVO(infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe());
		
		TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel = new TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaChamadaEncalheAntecipadaVO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(infoChamdaAntecipadaEncalheDTO.getTotalRegistros().intValue());
		
		ResultadoChamadaEncalheAntecipadaVO resultadoChamadaEncalheAntecipadaVO = new ResultadoChamadaEncalheAntecipadaVO(tableModel,null,null);

		resultadoChamadaEncalheAntecipadaVO.setRecolhimentoFinal(infoChamdaAntecipadaEncalheDTO.isRecolhimentoFinal());
		
		if(infoChamdaAntecipadaEncalheDTO.getTipoChamadaEncalhe() != null ){
		    resultadoChamadaEncalheAntecipadaVO.setTipoChamadaEncalhe(infoChamdaAntecipadaEncalheDTO.getTipoChamadaEncalhe().name() == null ? "" : infoChamdaAntecipadaEncalheDTO.getTipoChamadaEncalhe().name());		
		    
		}
		
		if(infoChamdaAntecipadaEncalheDTO.getDataRecolhimentoPrevista() != null) {
		    String dataPrevistaFormatada = DateUtil.formatarDataPTBR(infoChamdaAntecipadaEncalheDTO.getDataRecolhimentoPrevista());
		    resultadoChamadaEncalheAntecipadaVO.setDataRecolhimentoPrevista(dataPrevistaFormatada.toString());
		}
		
		return resultadoChamadaEncalheAntecipadaVO;
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
			chamadaEncalheAntecipadaVO.setCodBox(dto.getCodBox());
			chamadaEncalheAntecipadaVO.setNomeCota(dto.getNomeCota());
			chamadaEncalheAntecipadaVO.setNumeroCota( String.valueOf(dto.getNumeroCota()));
			chamadaEncalheAntecipadaVO.setQntExemplares(dto.getQntExemplares());
			chamadaEncalheAntecipadaVO.setCodigoChamadaEncalhe(dto.getCodigoChamadaEncalhe());
			chamadaEncalheAntecipadaVO.setIdLancamento(dto.getIdLancamento());
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
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
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
		
		if (infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe() == null || infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipadaVO = 
				getListaChamadaEncalheAntecipadaVO(infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe());
		
		FileExporter.to("chamada-encalhe-antecipada", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, infoChamdaAntecipadaEncalheDTO, 
					listaChamadaEncalheAntecipadaVO, ChamadaEncalheAntecipadaVO.class, this.httpServletResponse);
		result.nothing();
		
	}
	
	/**
	 * Valida se existe cotas repetidas para exportação das informações.
	 * 
	 * @param listaChamadaEncalheAntecipada
	 */
	@Post
	@Path("/validarCotasPesquisa")
	public void validarCotasPesquisa(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada){
	   
		validarListaNulaVazia(listaChamadaEncalheAntecipada);
		
		validarCotasDuplicadas(listaChamadaEncalheAntecipada,"Existem cotas duplicadas para exportação/impressão!");
		
		session.setAttribute(LISTA_PESQUISA_COTA, listaChamadaEncalheAntecipada);
		
		result.use(Results.json()).from("").recursive().serialize();
	}

    private void validarListaNulaVazia(List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada) {
    	
        if (listaChamadaEncalheAntecipada == null || listaChamadaEncalheAntecipada.isEmpty()){
	        
	        throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
	    }
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
										Long numeroEdicao, Long fornecedor,String municipio, String tipoPontoPDV) throws IOException{
		
		List<ChamadaEncalheAntecipadaVO> listaChamadaEncalheAntecipada = 
				(List<ChamadaEncalheAntecipadaVO>) session.getAttribute(LISTA_PESQUISA_COTA);
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setDataProgramada(dataProgaramada);
		filtro.setCodigoProduto(codigoProduto);
		filtro.setFornecedor(fornecedor);
		filtro.setNumeroEdicao(numeroEdicao);
		filtro.setDescMunicipio(municipio);
		filtro.setCodTipoPontoPDV(tipoPontoPDV);
		
		atribuirValoresFIltro(filtro);
		
		InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		BigInteger totalExemplares = BigInteger.ZERO;
		 
		for(ChamadaEncalheAntecipadaVO vo : listaChamadaEncalheAntecipada){
			totalExemplares = totalExemplares.add(vo.getQntExemplares());
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
			listRetorno.add(new ChamadaEncalheAntecipadaPesCotaVO(vo.getNumeroCota(),vo.getNomeCota(),vo.getQntExemplares().toString()));
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
				filtro.setDescRota(rota.getDescricaoRota());
			}
		}
		
		if(filtro.getRoteiro()!= null){
			Roteiro roteiro = roteirizacaoService.buscarRoteiroPorId(filtro.getRoteiro());
			if(roteiro!= null){
				filtro.setDescRoteiro(roteiro.getDescricaoRoteiro());
			}
		}
		
		filtro.setDescComCE( filtro.isProgramacaoCE()?"Sim":"Não");

		if(filtro.getCodTipoPontoPDV()!= null){
			
			// TipoPontoPDV tipoPontoPDV = pdvService.obterTipoPontoPDVPrincipal(filtro.getCodTipoPontoPDV());
			// if(tipoPontoPDV!= null){
				// filtro.setDescTipoPontoPDV(tipoPontoPDV.getDescricao());
			// }
		}
	}
	
	@Post
	public void recarregarListaRotas(Long roteiro){
		
		List<Rota> rotas = null;
		
		if(roteiro!= null){

			rotas = roteirizacaoService.buscarRotasPorRoteiro(roteiro);
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
		
		Map<String, Object> mapa = new TreeMap<String, Object>();
		
		List<ItemDTO<Long, String>> listaRotas = getRotas(rotas);
		if (listaRotas != null) {
			mapa.put("rotas", listaRotas);
		}

		List<ItemDTO<Long, String>> listaRoteiros = getRoteiros(roteirosBox);
		if (listaRoteiros != null) {
			mapa.put("roteiros", listaRoteiros);
		}
		
		result.use(CustomJson.class).from(mapa).serialize();
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
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
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