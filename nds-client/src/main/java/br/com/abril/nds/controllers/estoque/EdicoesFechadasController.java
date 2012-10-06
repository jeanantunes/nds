package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.client.vo.ResultadoEdicoesFechadasVO;
import br.com.abril.nds.dto.filtro.FiltroEdicoesFechadasDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.EdicoesFechadasService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
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

/**
 * @author infoA2
 * Controller de edições fechadas
 */
@Resource
@Path("/estoque/edicoesFechadas")
public class EdicoesFechadasController {
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private EdicoesFechadasService edicoesFechadasService;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private DistribuidorService distribuidorService;

	private static final String FORMATO_DATA = "dd/MM/yyyy";

	private static final String FILTRO_PESQUISA_EDICOES_FECHADAS_SESSION_ATTRIBUTE = "filtroPesquisaEdicoesFechadas";
	
	@Path("/")
	@Rules(Permissao.ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO)
	public void index() {
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO));
	}

	/**
	 * Valida os dados enviados para a pesquisa
	 * @param dataDe
	 * @param dataAte
	 */
	private void validarDadosPesquisa(String dataDe, String dataAte) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null || dataDe.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null || dataAte.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}
	
	/**
	 * Realiza a pesquisa de edições fechadas
	 * @param filtro
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisar")
	public void pesquisarEdicoesFechadas(String dataDe, String dataAte, Long idFornecedor, String sortname, String sortorder, int rp, int page) throws Exception {

		this.validarDadosPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroEdicoesFechadasDTO filtro = carregarFiltro(sortorder, sortname, page, rp, sdf.parse(dataDe), sdf.parse(dataAte), idFornecedor);	

		List<RegistroEdicoesFechadasVO> resultado = null;
		BigDecimal saldoTotal = new BigDecimal("0");
		try {
			resultado = edicoesFechadasService.obterResultadoEdicoesFechadas(sdf.parse(dataDe), sdf.parse(dataAte), idFornecedor);
			saldoTotal = edicoesFechadasService.obterTotalResultadoEdicoesFechadas(sdf.parse(dataDe), sdf.parse(dataAte), idFornecedor);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultado == null || resultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = resultado.size();

			List<RegistroEdicoesFechadasVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
			TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>> tableModel = new TableModel<CellModelKeyValue<RegistroEdicoesFechadasVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
			
			ResultadoEdicoesFechadasVO resultadoEdicoesFechadas = new ResultadoEdicoesFechadasVO();
			resultadoEdicoesFechadas.setTableModel(tableModel);
			resultadoEdicoesFechadas.setSaldoTotal(saldoTotal.toBigInteger());
			
			result.use(Results.json()).withoutRoot().from(resultadoEdicoesFechadas).recursive().serialize();
		}
		
	}

	/**
	 * Carrega os filtros de pesquisa
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @param idFornecedor 
	 * @param dataAte 
	 * @param dataDe 
	 * @return
	 */
	private FiltroEdicoesFechadasDTO carregarFiltro(String sortorder, String sortname, int page, int rp, Date dataDe, Date dataAte, Long idFornecedor) {

		FiltroEdicoesFechadasDTO filtro = new FiltroEdicoesFechadasDTO();
		filtro.setDataDe(dataDe);
		filtro.setDataAte(dataAte);
		filtro.setCodigoFornecedor(idFornecedor);
		
		this.configurarPaginacao(filtro, sortorder, sortname, page, rp);

		FiltroEdicoesFechadasDTO filtroSessao = (FiltroEdicoesFechadasDTO) this.session.getAttribute(FILTRO_PESQUISA_EDICOES_FECHADAS_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_EDICOES_FECHADAS_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	/**
	 * Configura a paginação dos filtros
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacao(FiltroEdicoesFechadasDTO filtro, String sortorder,	String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroEdicoesFechadasDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}	

	/**
	 * Exporta o arquivo para o tipo selecionado
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}

		FiltroEdicoesFechadasDTO filtroSessao = this.obterFiltroParaExportacao();
		List<RegistroEdicoesFechadasVO> lista = edicoesFechadasService.obterResultadoEdicoesFechadas(filtroSessao.getDataDe(), filtroSessao.getDataAte(), filtroSessao.getCodigoFornecedor());
		
		BigDecimal	saldoTotal = edicoesFechadasService.obterTotalResultadoEdicoesFechadas(filtroSessao.getDataDe(), filtroSessao.getDataAte(), filtroSessao.getCodigoFornecedor());
		ResultadoEdicoesFechadasVO resultadoTotalEdicoesFechadas = new ResultadoEdicoesFechadasVO();
		resultadoTotalEdicoesFechadas.setSaldoTotal(saldoTotal.toBigInteger());
		
		FileExporter.to("consulta-edicoes-fechadas-com-saldo", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoTotalEdicoesFechadas, lista, RegistroEdicoesFechadasVO.class, this.httpServletResponse);
	}

	/**
	 * Obtém os dados do cabeçalho de exportação.
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
	 * Obtém o filtro de pesquisa para exportação.
	 * @return
	 */
	private FiltroEdicoesFechadasDTO obterFiltroParaExportacao() {
		
		FiltroEdicoesFechadasDTO filtroSessao =
			(FiltroEdicoesFechadasDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_EDICOES_FECHADAS_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		return filtroSessao;
	}
	
	/**
	 * Retorna o usuário logado
	 * @return
	 */
	// TODO: Implementar quando funcionar
	private Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");
		return usuario;
	}

}
