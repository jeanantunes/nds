package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroEdicoesFechadasDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.LogExecucaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * @author infoA2
 * Controller de painel de processamento
 */
@Resource
@Path("/administracao/painelProcessamento")
public class PainelProcessamentoController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	private static final String FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE = "filtroPesquisaInterfaces";

	@Autowired
	private LogExecucaoService logExecucaoService;
	
	@Path("/")
	public void index() {
	}

	@Path("/pesquisarInterfaces")
	public void pesquisarInterfaces(String sortname, String sortorder, int rp, int page) throws Exception {

		List<LogExecucao> resultado = null;
		try {
			resultado = logExecucaoService.buscaPaginada(sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
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
			result.use(FlexiGridJson.class).from(resultado).total(resultado.size()).page(page).serialize();
		}
	
	}

	@Path("/pesquisarProcessos")
	public void pesquisarProcessos(String sortname, String sortorder, int rp, int page) {
	
		/*List<RegistroProcessosPainelProcessamentoVO> resultado = null;
		BigDecimal saldoTotal = new BigDecimal("0");
		try {
			resultado = edicoesFechadasService.obterResultadoEdicoesFechadas();
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}*/
	}

	/*private FiltroPainelProcessamentoDTO carregarFiltro(String sortorder, String sortname, int page, int rp) {

		FiltroPainelProcessamentoDTO filtro = new FiltroPainelProcessamentoDTO();

		this.configurarPaginacao(filtro, sortorder, sortname, page, rp);

		FiltroPainelProcessamentoDTO filtroSessao = (FiltroPainelProcessamentoDTO) this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}*/
	
	/**
	 * Configura a paginação dos filtros
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	/*private void configurarPaginacao(FiltroPainelProcessamentoDTO filtro, String sortorder,	String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroPainelProcessamentoDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}*/	

	/**
	 * Exporta o arquivo para o tipo selecionado
	 * @param fileType
	 * @param tipoDesconto
	 * @throws IOException
	 */
	@Get
	public void exportar(FileType fileType, String tipoDesconto) throws IOException {
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}

		/*FiltroEdicoesFechadasDTO filtroSessao = this.obterFiltroParaExportacao();
		List<RegistroEdicoesFechadasVO> lista = edicoesFechadasService.obterResultadoEdicoesFechadas(filtroSessao.getDataDe(), filtroSessao.getDataAte(), filtroSessao.getCodigoFornecedor());
		
		BigDecimal	saldoTotal = edicoesFechadasService.obterTotalResultadoEdicoesFechadas(filtroSessao.getDataDe(), filtroSessao.getDataAte(), filtroSessao.getCodigoFornecedor());
		ResultadoEdicoesFechadasVO resultadoTotalEdicoesFechadas = new ResultadoEdicoesFechadasVO();
		resultadoTotalEdicoesFechadas.setSaldoTotal(saldoTotal.toBigInteger());
		
		FileExporter.to("consulta-edicoes-fechadas-com-saldo", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoTotalEdicoesFechadas, lista, RegistroEdicoesFechadasVO.class, this.httpServletResponse);*/
	}

	/**
	 * Obtém os dados do cabeçalho de exportação.
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		/*NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;*/
		return null;
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * @return
	 */
	private FiltroEdicoesFechadasDTO obterFiltroParaExportacao() {
		
		FiltroEdicoesFechadasDTO filtroSessao =
			(FiltroEdicoesFechadasDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
		
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
