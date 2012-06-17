package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
@Path("/administracao/cadastroTipoNota")
public class CadastroTipoNotaController {

	@Autowired
	TipoNotaFiscalService tipoNotaFiscalService;

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
		
		FiltroCadastroTipoNotaDTO filtro = obterFiltroParaExportacao();
		
		Distribuidor distribuidor = distribuidorService.obter();

		List<TipoNotaFiscal> lista = tipoNotaFiscalService.obterTiposNotasFiscais(filtro.getCfop(), filtro.getTipoNota(), distribuidor.getTipoAtividade());
		
		FileExporter.to("consulta-edicoes-fechadas-com-saldo", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, lista, TipoNotaFiscal.class, this.httpServletResponse);
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
	public void pesquisar(String cfop, String tipoNota, String sortname, String sortorder, int rp, int page) throws Exception {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		gravarFiltroSessao(sortname, cfop, tipoNota);
		
		List<TipoNotaFiscal> lista = tipoNotaFiscalService.obterTiposNotasFiscais(cfop, tipoNota, distribuidor.getTipoAtividade(), sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		
		if (lista == null || lista.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		
		Long quantidade = tipoNotaFiscalService.obterQuantidadeTiposNotasFiscais(cfop, tipoNota, distribuidor.getTipoAtividade());
		result.use(FlexiGridJson.class).from(lista).total(quantidade.intValue()).page(page).serialize();
	}
	
	/**
	 * Grava o filtro na sessão
	 * @param sortname
	 * @param sortname2 
	 * @param tipoNota 
	 */
	private void gravarFiltroSessao(String sortname, String tipoNota, String cfop) {
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota(tipoNota);
		filtro.setCfop(cfop);
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.values(), sortname));
		session.setAttribute(FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * @return
	 */
	private FiltroCadastroTipoNotaDTO obterFiltroParaExportacao() {
		
		FiltroCadastroTipoNotaDTO filtroSessao = (FiltroCadastroTipoNotaDTO) this.session.getAttribute(FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		return filtroSessao;
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
