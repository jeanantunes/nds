package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.RegistroTipoNotaFiscalVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNaturezaOperacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * @author InfoA2
 * Controller de Cadastro de Tipo de Notas
 */
@Resource
@Path("/administracao/naturezaOperacao")
@Rules(Permissao.ROLE_ADMINISTRACAO_NATUREZA_OPERACAO)
public class NaturezaOperacaoController extends BaseController {

	@Autowired
	NaturezaOperacaoService naturezaOperacaoService;

	@Autowired
	DistribuidorService distribuidorService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Path("/")
	public void index() {
		
		List<ItemDTO<TipoAtividade, String>> listaAtividades = new ArrayList<ItemDTO<TipoAtividade, String>>();
		
		listaAtividades.add(new ItemDTO<TipoAtividade, String>(distribuidorService.obter().getTipoAtividade(), distribuidorService.obter().getTipoAtividade().getDescricao()));
		result.include("listaAtividades", listaAtividades);
	}

	private static final String FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE = "filtroCadastroTipoNota";
	
	/**
	 * Exporta o arquivo para o tipo selecionado
	 * @param fileType
	 * @param tipoDesconto
	 * @throws IOException
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroNaturezaOperacaoDTO filtro = obterFiltroParaExportacao();
		
		List<NaturezaOperacao> lista = naturezaOperacaoService.consultarNaturezasOperacoes(filtro);
		
		FileExporter.to("consulta-edicoes-fechadas-com-saldo", fileType)
					.inHTTPResponse(this.getNDSFileHeader(), 
									filtro, 
									null, 
									getResultadoVO(lista,"\n"), 
									RegistroTipoNotaFiscalVO.class, 
									this.httpServletResponse);
	}
	
	/**
	 * Realiza a pesquisa dos dados
	 * @param cfop
	 * @param tipoNota
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisar")
	public void pesquisar(TipoAtividade operacao, String tipoNota, String sortname, String sortorder, int rp, int page) throws Exception {
		
		FiltroNaturezaOperacaoDTO filtro =  tratarFiltroSessao(operacao, tipoNota, sortname, sortorder, rp, page);
		
		List<NaturezaOperacao> lista = naturezaOperacaoService.consultarNaturezasOperacoes(filtro);
		
		if (lista == null || lista.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		
		Integer quantidade = naturezaOperacaoService.obterQuantidadeNaturezasOperacoes(filtro);
		
		result.use(FlexiGridJson.class).from(getResultadoVO(lista,"<br></br>")).total(quantidade).page(page).serialize();
	}
	
	/**
	 * Grava o filtro na sessão
	 * @param sortname
	 * @param sortname2 
	 * @param tipoNota 
	 */
	private FiltroNaturezaOperacaoDTO tratarFiltroSessao(TipoAtividade operacao, String tipoNota, String sortname, String sortorder, int rp, int page) {
		
		FiltroNaturezaOperacaoDTO filtro = new FiltroNaturezaOperacaoDTO();
		filtro.setTipoNota(tipoNota);
		filtro.setTipoAtividade(operacao);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtro.setPaginacao(paginacao);
		
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroNaturezaOperacaoDTO.OrdenacaoColunaConsulta.values(), sortname));
		session.setAttribute(FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * @return
	 */
	private FiltroNaturezaOperacaoDTO obterFiltroParaExportacao() {
		
		FiltroNaturezaOperacaoDTO filtroSessao = (FiltroNaturezaOperacaoDTO) this.session.getAttribute(FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		return filtroSessao;
	}	
	
	/**
	 * Popula uma lista de VO para exibir na view
	 * @param listaTipoNotaFiscal
	 * @return List<RegistroTipoNotaFiscalVO>
	 */
	private List<RegistroTipoNotaFiscalVO> getResultadoVO(List<NaturezaOperacao> listaTipoNotaFiscal, String tipoQuebraLinha) {
		
		List<RegistroTipoNotaFiscalVO> listaResultado = new ArrayList<RegistroTipoNotaFiscalVO>();
		
		RegistroTipoNotaFiscalVO resultado = null;
		
		for (NaturezaOperacao tipoNotaFiscal : listaTipoNotaFiscal) {
			
			resultado = new RegistroTipoNotaFiscalVO();
			resultado.setNopDescricao(tipoNotaFiscal.getDescricao());
			resultado.setCfopEstado( (tipoNotaFiscal.getCfopEstado()!= null) ? tipoNotaFiscal.getCfopEstado() : "");
			resultado.setCfopOutrosEstados((tipoNotaFiscal.getCfopOutrosEstados()!=null) ? tipoNotaFiscal.getCfopOutrosEstados() : "");
			resultado.setTipoAtividade(tipoNotaFiscal.getTipoAtividade().getDescricao());
			
			String processo = "";
			
			for(Processo pr : tipoNotaFiscal.getProcesso()){
				processo += pr.getDescricao() + tipoQuebraLinha;
			}
			
			resultado.setProcesso(processo);
			
			listaResultado.add(resultado);
			
		}
		
		return listaResultado;
	}
	
	@Post
	public void obterNaturezasOperacoesPorEmitenteDestinatario(TipoEmitente tipoEmitente, TipoDestinatario tipoDestinatario) {
		
	    List<ItemDTO<Long, String>> naturezasOperacoes = null;
	    
	    if(TipoEmitente.COTA.equals(tipoEmitente) &&  TipoDestinatario.DISTRIBUIDOR.equals(tipoDestinatario)){
	        naturezasOperacoes = naturezaOperacaoService.obterNaturezasOperacoesPorEmitenteDestinatario(tipoEmitente, tipoDestinatario, true, true);
	    } else{
	        naturezasOperacoes = naturezaOperacaoService.obterNaturezasOperacoesPorEmitenteDestinatario(tipoEmitente, tipoDestinatario, false, true);
	    }
	
		result.use(FlexiGridJson.class).from(naturezasOperacoes).serialize();
	}
	
}