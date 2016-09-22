package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.dto.RateioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
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

@Resource
@Path("/cotaAusente")
@Rules(Permissao.ROLE_EXPEDICAO_COTA_AUSENTE)
public class CotaAusenteController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotaAusenteController.class);

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroCotaAusente";
	
    private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
	
    private static final String WARNING_COTA_AUSENTE_DUPLICADA = "Esta cota já foi declarada como ausente nesta data.";
	
    private static final String WARNING_DATA_MAIOR_OPERACAO_ATUAL = "A data informada é superior a data de operação atual.";
	
    private static final String WARNING_DATA_INFORMADA_INVALIDA = "A data informada é inválida.";
	
    private static final String WARNING_NUMERO_COTA_NAO_INFORMADO = "O campo \"cota\" é obrigatório.";
	
    private static final String ERRO_ENVIO_SUPLEMENTAR = "Erro não esperado ao realizar envio de suplementar.";
	
	private static final String ERRO_PESQUISAR_COTAS_AUSENTES = "Erro ao pesquisar cotas ausentes.";
	
	private static final String ERRO_CANCELAR_COTA_AUSENTE = "Erro inesperado ao realizar cancelamento de cota ausente.";
	
	private static final String ERRO_RATEIO = "Erro inesperado ao realizar rateio.";
	
	private static final String SUCESSO_ENVIO_SUPLEMENTAR = "Envio de suplementar realizado com sucesso.";
	
	private static final String SUCESSO_CANCELAR_COTA_AUSENTE = "Cancelamento de cota ausente realizado com sucesso.";
	
	private static final String SUCESSO_RATEIO = "Rateio realizado com sucesso.";
	
	@Autowired
	private CotaAusenteService cotaAusenteService;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	private final Result result;
		
	public CotaAusenteController(Result result) {
		this.result=result;
	}
	
	public void cotaAusente() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {
		
		List<Roteiro> roteiros = this.roteirizacaoService.buscarRoteiro(null, null);
		
		List<ItemDTO<Long, String>> listRoteiro = new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros){
			
			listRoteiro.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("roteiros", listRoteiro);
		
		List<Rota> rotas = this.roteirizacaoService.buscarRota(null, null);
		
		List<ItemDTO<Long, String>> listRota = new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas){
			
			listRota.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("rotas", listRota);
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		gerarDataLancamento();
		
		result.forwardTo(CotaAusenteController.class).cotaAusente();
	}
	
	/**
	 * Gera a data de lancamento como data atual
	 */
	private void gerarDataLancamento() {
		
		String data = 
			DateUtil.formatarDataPTBR(
				this.distribuidorService.obterDataOperacaoDistribuidor());
		
		result.include("data", data);			
	}
	
	/**
	 * Realiza pesquisa de Cotas Ausentes
	 * 
	 * @param dataAusente
	 * @param numCota
	 * @param nomeCota
	 * @param box
	 */
	@Post
	public void pesquisarCotasAusentes(String dataAusencia, Integer numCota, 
			Long idRota, Long idRoteiro, String box,String sortorder, 
			String sortname, int page, int rp) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();

		TableModel<CellModelKeyValue<CotaAusenteDTO>> grid = null;
		
		box = (StringUtil.isEmpty(box)) ? null : box;
		
		try {
			Date data = validaData(dataAusencia);
								
			FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO(data, box, numCota, idRota, idRoteiro,
					new PaginacaoVO(page, rp, sortorder), 
					ColunaOrdenacao.valueOf(sortname));
					
			this.tratarFiltro(filtro);
			
			grid = this.efetuarConsulta(filtro);
			
		} catch(ValidacaoException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;	
			
		}catch(Exception e) {
			
			mensagens.clear();
			mensagens.add(ERRO_PESQUISAR_COTAS_AUSENTES);
			status=TipoMensagem.ERROR;
			LOGGER.error(ERRO_PESQUISAR_COTAS_AUSENTES, e);
		}
	
		if(grid==null) {
			grid = new TableModel<CellModelKeyValue<CotaAusenteDTO>>();
		}
		
		Object[] retorno = new Object[3];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
	
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
		
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de CotasAusentes.
	 * @param filtro
	 */
	private TableModel<CellModelKeyValue<CotaAusenteDTO>> efetuarConsulta(FiltroCotaAusenteDTO filtro) {
		
		List<CotaAusenteDTO> listaCotasAusentes = null;
		
		listaCotasAusentes = this.cotaAusenteService.obterCotasAusentes(filtro) ;
		
		if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_PESQUISA_SEM_RESULTADO);
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		String dataOperacaoFormatada = DateUtil.formatarDataPTBR(dataOperacao);
		
		if (filtro.getData() != null) {
			
			for (CotaAusenteDTO cotaAusenteDTO : listaCotasAusentes) {
				
				if (!dataOperacaoFormatada.equals(cotaAusenteDTO.getData())) {
					
					cotaAusenteDTO.setIdCotaAusente(null);
				}
			}
		}
		
		Long totalRegistros = this.cotaAusenteService.obterCountCotasAusentes(filtro);
		
		TableModel<CellModelKeyValue<CotaAusenteDTO>> tableModel = new TableModel<CellModelKeyValue<CotaAusenteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasAusentes));
		
		tableModel.setPage(1);
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		return tableModel;
	}

	
	private Date validaData(String dataAusencia) {

		if ( dataAusencia == null || dataAusencia.isEmpty())
			return null;
		
		Date data = null;
		
		try {
			data = DateUtil.parseDataPTBR(dataAusencia);
		} catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_INFORMADA_INVALIDA);
		}

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
				
		if (data.compareTo(dataOperacao) > 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_MAIOR_OPERACAO_ATUAL);
			
		}
		
		return data;
	}
	
	                /**
     * Executa tratamento de paginação em função de alteração do filtro de
     * pesquisa.
     * 
     * @param filtroResumoExpedicao
     */
	private void tratarFiltro(FiltroCotaAusenteDTO filtro) {

		FiltroCotaAusenteDTO filtroSession = (FiltroCotaAusenteDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	
	                /**
     * Obtém os produtos edição disponíveis para uma cota ausente que foi buscar
     * seu reparte
     * 
     * @param idCotaAusente
     */
	@Post
	public void exibirProdutosSuplementaresDisponiveis(Long idCotaAusente) {
		
        List<ProdutoEdicaoSuplementarDTO> listaProdutosEdicaoDisponíveis =
			this.cotaAusenteService.obterDadosExclusaoCotaAusente(idCotaAusente);
		
        result.use(FlexiGridJson.class).from(listaProdutosEdicaoDisponíveis).page(1).total(
                listaProdutosEdicaoDisponíveis.size()).serialize();
		
		}
		
		
	/**
	 * 
	 * @param idCotaAusente
	 */
	@Rules(Permissao.ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO)
	public void cancelarCotaAusente(Long idCotaAusente) {
				
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
		
			cotaAusenteService.cancelarCotaAusente(idCotaAusente, getUsuarioLogado().getId());
			
			mensagens.add(SUCESSO_CANCELAR_COTA_AUSENTE);
			
		} catch(ValidacaoException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(TipoMovimentoEstoqueInexistenteException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.WARNING;
			
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_CANCELAR_COTA_AUSENTE);
			status=TipoMensagem.ERROR;
			LOGGER.error(ERRO_CANCELAR_COTA_AUSENTE, e);
		}
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status;		
		
		result.use(Results.json()).from(retorno, "result").serialize();
	}
		
	                /**
     * Declara cota como ausente e envia seu reparte para suplementar
     * 
     * @param numCota - Número da Cota
     */
	@Post
	@Rules(Permissao.ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO)
	public void enviarParaSuplementar(List<Integer> numCotas) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
			
			if (numCotas == null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, WARNING_NUMERO_COTA_NAO_INFORMADO);
			}
						
			Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			this.cotaAusenteService.declararCotaAusenteEnviarSuplementar(
				numCotas, dataOperacao, this.getUsuarioLogado().getId());
			
			mensagens.add(SUCESSO_ENVIO_SUPLEMENTAR);
			
		} catch(ValidacaoException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(InvalidParameterException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.add(WARNING_COTA_AUSENTE_DUPLICADA);
			status=TipoMensagem.WARNING;			
		}catch(TipoMovimentoEstoqueInexistenteException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.WARNING;
		} catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_ENVIO_SUPLEMENTAR );
			status=TipoMensagem.ERROR;
			LOGGER.error(ERRO_ENVIO_SUPLEMENTAR, e);
		}
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status;		
		
		result.use(Results.json()).from(retorno, "result").serialize();
	}

	                /**
     * Obtém movimentos para realização do Rateio
     * 
     * @param numCota
     */
	@Post
	public void carregarDadosRateio(List<Integer> numCotas) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		for (Integer numeroCota : numCotas) {
			
			try {
				
				this.cotaAusenteService.verificarExistenciaReparteCota(dataOperacao, numeroCota);
				
			} catch (ValidacaoException e) {
                LOGGER.debug(e.getMessage(), e);
				
				List<String> mensagens = new ArrayList<String>();
				
				mensagens.addAll(e.getValidacao().getListaMensagens());
				
				TipoMensagem tipoMensagem = TipoMensagem.WARNING;
				
				Object[] retorno = new Object[2];
				
				retorno[0] = mensagens;
				retorno[1] = tipoMensagem;
				
				result.use(Results.json()).from(retorno, "result").serialize();
				
				return;
			}
		}

        // TODO: Alterar para não trazer dados já rateados
		
		List<MovimentoEstoqueCotaDTO> movimentos = 
			this.movimentoEstoqueCotaService.obterMovimentoDTOCotaPorTipoMovimento(
				dataOperacao, numCotas, Arrays.asList(GrupoMovimentoEstoque.values()));
		
		if (movimentos == null || movimentos.isEmpty()) {
			
			List<String> mensagens = new ArrayList<String>();
			
            mensagens.add("Não ha reparte para as cotas nesta data.");
			
			TipoMensagem tipoMensagem = TipoMensagem.WARNING;
			
			Object[] retorno = new Object[2];
			
			retorno[0] = mensagens;
			retorno[1] = tipoMensagem;
			
			result.use(Results.json()).from(retorno, "result").serialize();
			
			return;
		}
		
		result.use(Results.json()).from(movimentos, "result").recursive().serialize();
	}
	
	                /**
     * Verifica se a redistribuição esta sendo direcionada para a mesma cota que
     * esta sendo cadastrada como ausente.
     * 
     * @param movimentos
     */
	private void verificarCotaRateios(List<MovimentoEstoqueCotaDTO> movimentos){
		
		for (MovimentoEstoqueCotaDTO mecDTO : movimentos){
			
			Cota cota  = this.cotaService.obterPorId(mecDTO.getIdCota());

			for (RateioDTO rateio : mecDTO.getRateios()){
				
				Cota cotaRateio  = this.cotaService.obterPorNumeroDaCota(rateio.getNumCota());
				
				if(cotaRateio.getTipoCota().equals(TipoCota.A_VISTA)) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Não é permitido redistribuir a vista. ("+cotaRateio.getNumeroCota()+")");
				}
				
			    if (cota.getNumeroCota().equals(rateio.getNumCota())){
                    throw new ValidacaoException(TipoMensagem.WARNING, "Não é permitido redistribuir para a cota ausente.");
		    	}
		    }
		}
	}
	
	/**
	 * Realiza rateio preenchidos na tela
	 * 
	 * @param movimentos
	 * @param numCota
	 */
	@Post
	@Rules(Permissao.ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO)
	public void realizarRateio(List<MovimentoEstoqueCotaDTO> movimentos, 
							   List<Integer> numCotas) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		this.verificarCotaRateios(movimentos);
		
		try {
			
			if (numCotas == null) { 
				
				throw new ValidacaoException(
					TipoMensagem.WARNING, WARNING_NUMERO_COTA_NAO_INFORMADO);
			}
			
			Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			this.cotaAusenteService.declararCotaAusenteRatearReparte(
				numCotas, dataOperacao, this.getUsuarioLogado().getId() , movimentos);
			
			mensagens.add(SUCESSO_RATEIO);
			
		} catch (ValidacaoException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status = TipoMensagem.WARNING;
		
		} catch (InvalidParameterException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.add(WARNING_COTA_AUSENTE_DUPLICADA);
			status = TipoMensagem.WARNING;	
			
		} catch (TipoMovimentoEstoqueInexistenteException e) {
            LOGGER.debug(e.getMessage(), e);
			mensagens.clear();
			mensagens.add(e.getMessage());
			status = TipoMensagem.WARNING;
			
		} catch (Exception e) {
			
			mensagens.clear();
			mensagens.add(ERRO_RATEIO );
			status = TipoMensagem.ERROR;
			LOGGER.error(ERRO_RATEIO, e);
		}
		
		Object[] retorno = new Object[2];
		
		retorno[0] = mensagens;
		retorno[1] = status;		
		
		this.result.use(Results.json()).from(retorno, "result").serialize();
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
		
		FiltroCotaAusenteDTO filtro = getFiltroSessao();
		
		List<CotaAusenteDTO> listaCotaAusente = cotaAusenteService.obterCotasAusentes(filtro) ;
		
        FileExporter.to("cota_ausente", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
				listaCotaAusente, CotaAusenteDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	private FiltroCotaAusenteDTO getFiltroSessao() {
	
		FiltroCotaAusenteDTO filtro = (FiltroCotaAusenteDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "É necessário fazer uma pesquisa primeiro"));
		}
		
		return filtro;
	}

}
