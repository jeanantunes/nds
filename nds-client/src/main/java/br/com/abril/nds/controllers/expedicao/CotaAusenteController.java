package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.dto.ProdutoServicoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
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
public class CotaAusenteController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroCotaAusente";
	
	private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
	
	private static final String WARNING_COTA_AUSENTE_DUPLICADA =  "Esta cota já foi declarada como ausente nesta data.";
	
	private static final String WARNING_DATA_MAIOR_OPERACAO_ATUAL = "A data informada é inferior a data de operação atual.";
	
	private static final String WARNING_DATA_INFORMADA_INVALIDA = "A data informada é inválida.";
	
	private static final String WARNING_NUMERO_COTA_NAO_INFORMADO =  "O campo \"cota\" é obrigatório.";
	
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
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private static final Logger LOG = LoggerFactory
			.getLogger(CotaAusenteController.class);
	
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
	@Rules(Permissao.ROLE_EXPEDICAO_COTA_AUSENTE)
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
		
		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		
		result.include("data",data);			
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
		
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;	
			
		}catch(Exception e) {
			
			mensagens.clear();
			mensagens.add(ERRO_PESQUISAR_COTAS_AUSENTES);
			status=TipoMensagem.ERROR;
			LOG.error(ERRO_PESQUISAR_COTAS_AUSENTES, e);
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
		
		listaCotasAusentes = cotaAusenteService.obterCotasAusentes(filtro) ;
		
		if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_PESQUISA_SEM_RESULTADO);
		}
		
		if(filtro.getData() != null) {
			for(CotaAusenteDTO cotaAusenteDTO : listaCotasAusentes) {
				if(!DateUtil.isHoje(DateUtil.parseDataPTBR(cotaAusenteDTO.getData())))
						cotaAusenteDTO.setIdCotaAusente(null);
			}
		}
		
		Long totalRegistros = cotaAusenteService.obterCountCotasAusentes(filtro);
		
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
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_INFORMADA_INVALIDA);
		}

		if ( data.getTime() > (new Date().getTime()) )
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_MAIOR_OPERACAO_ATUAL );
		
		return data;
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
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
	 * Obtém os produtos edição disponíveis para uma cota ausente que 
	 * foi buscar seu reparte
	 * 
	 * @param idCotaAusente
	 */
	@Post
	public void exibirProdutosSuplementaresDisponiveis(Long idCotaAusente) {
		
		FiltroCotaAusenteDTO filtro = this.getFiltroSessao();
		
		List<ProdutoEdicaoSuplementarDTO> listaProdutosEdicaoDisponíveis = 
				this.estoqueProdutoService.obterProdutosEdicaoSuplementarDisponivel(filtro.getData(), idCotaAusente);
		
		result.use(FlexiGridJson.class).from(listaProdutosEdicaoDisponíveis).page(1).total(listaProdutosEdicaoDisponíveis.size()).serialize();
	}
	
	
	/**
	 * 
	 * @param idCotaAusente
	 */
	public void cancelarCotaAusente(Long idCotaAusente) {
				
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
		
			cotaAusenteService.cancelarCotaAusente(idCotaAusente, getUsuario().getId());
			
			mensagens.add(SUCESSO_CANCELAR_COTA_AUSENTE);
			
		} catch(ValidacaoException e) {
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(TipoMovimentoEstoqueInexistenteException e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.WARNING;
			
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_CANCELAR_COTA_AUSENTE);
			status=TipoMensagem.ERROR;
			LOG.error(ERRO_CANCELAR_COTA_AUSENTE, e);
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
	public void enviarParaSuplementar(Integer numCota) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
			
			if(numCota == null) 
				throw new ValidacaoException(TipoMensagem.WARNING, WARNING_NUMERO_COTA_NAO_INFORMADO);
						
			cotaAusenteService.declararCotaAusenteEnviarSuplementar(numCota, new Date(), this.getUsuario().getId());
			
			mensagens.add(SUCESSO_ENVIO_SUPLEMENTAR);
			
		} catch(ValidacaoException e) {
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(InvalidParameterException e) {
			mensagens.clear();
			mensagens.add(WARNING_COTA_AUSENTE_DUPLICADA);
			status=TipoMensagem.WARNING;			
		}catch(TipoMovimentoEstoqueInexistenteException e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.WARNING;
		} catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_ENVIO_SUPLEMENTAR );
			status=TipoMensagem.ERROR;
			LOG.error(ERRO_ENVIO_SUPLEMENTAR, e);
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
	public void carregarDadosRateio(Integer numCota) {
		
		List<MovimentoEstoqueCotaDTO> movimentos = 
				movimentoEstoqueCotaService.obterMovimentoDTOCotaPorTipoMovimento(new Date(), numCota, GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		result.use(Results.json()).from(movimentos, "result").recursive().serialize();
	}
	
	/**
	 * Realiza rateio preenchidos na tela
	 * 
	 * @param movimentos
	 * @param numCota
	 */
	@Post
	public void realizarRateio(List<MovimentoEstoqueCotaDTO> movimentos, Integer numCota) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
			
			if(numCota == null) 
				throw new ValidacaoException(TipoMensagem.WARNING, WARNING_NUMERO_COTA_NAO_INFORMADO);
			
			cotaAusenteService.declararCotaAusenteRatearReparte(numCota, new Date(), this.getUsuario().getId() , movimentos);
			
			mensagens.add(SUCESSO_RATEIO);
			
		} catch(ValidacaoException e) {
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(InvalidParameterException e) {
			mensagens.clear();
			mensagens.add(WARNING_COTA_AUSENTE_DUPLICADA);
			status=TipoMensagem.WARNING;			
		}catch(TipoMovimentoEstoqueInexistenteException e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.WARNING;
		} catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_RATEIO );
			status=TipoMensagem.ERROR;
			LOG.error(ERRO_RATEIO, e);
		}
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status;		
		
		result.use(Results.json()).from(retorno, "result").serialize();
	}
	
	//TODO getRealUsuario
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("fakeUsuario");
		usuario.setNome("Fake Usuario");
		return usuario;
	}
	
	/**
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
		
		FileExporter.to("cota_ausente", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaCotaAusente, CotaAusenteDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	private FiltroCotaAusenteDTO getFiltroSessao() {
	
		FiltroCotaAusenteDTO filtro = (FiltroCotaAusenteDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "É necessário fazer uma pesquisa primeiro"));
		}
		
		return filtro;
	}
}
