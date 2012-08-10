package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroTipoNotaFiscalVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TipoNotaFiscalService;
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
		
		List<ItemDTO<TipoAtividade, String>> listaAtividades = new ArrayList<ItemDTO<TipoAtividade,String>>();
		
		for(TipoAtividade atividade : TipoAtividade.values()){
			listaAtividades.add(new ItemDTO<TipoAtividade, String>(atividade,atividade.getDescTipoDistribuidor()));
		}
		
		result.include("listaAtividades",listaAtividades);
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
		
		List<TipoNotaFiscal> lista = tipoNotaFiscalService.consultarTipoNotaFiscal(filtro);
		
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
		
		FiltroCadastroTipoNotaDTO filtro =  tratarFiltroSessao(operacao, tipoNota, sortname, sortorder, rp, page);
		
		List<TipoNotaFiscal> lista = tipoNotaFiscalService.consultarTipoNotaFiscal(filtro);
		
		if (lista == null || lista.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		
		Integer quantidade = tipoNotaFiscalService.obterQuantidadeTiposNotasFiscais(filtro);
		
		result.use(FlexiGridJson.class).from(getResultadoVO(lista,"<br></br>")).total(quantidade).page(page).serialize();
	}
	
	/**
	 * Grava o filtro na sessão
	 * @param sortname
	 * @param sortname2 
	 * @param tipoNota 
	 */
	private FiltroCadastroTipoNotaDTO tratarFiltroSessao(TipoAtividade operacao, String tipoNota, String sortname, String sortorder, int rp, int page) {
		
		FiltroCadastroTipoNotaDTO filtro = new FiltroCadastroTipoNotaDTO();
		filtro.setTipoNota(tipoNota);
		filtro.setTipoAtividade(operacao);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtro.setPaginacao(paginacao);
		
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroCadastroTipoNotaDTO.OrdenacaoColunaConsulta.values(), sortname));
		session.setAttribute(FILTRO_CADASTRO_TIPO_NOTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
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
	
	/**
	 * Popula uma lista de VO para exibir na view
	 * @param listaTipoNotaFiscal
	 * @return List<RegistroTipoNotaFiscalVO>
	 */
	private List<RegistroTipoNotaFiscalVO> getResultadoVO(List<TipoNotaFiscal> listaTipoNotaFiscal, String tipoQuebraLinha) {
		
		List<RegistroTipoNotaFiscalVO> listaResultado = new ArrayList<RegistroTipoNotaFiscalVO>();
		
		RegistroTipoNotaFiscalVO resultado = null;
		
		for (TipoNotaFiscal tipoNotaFiscal : listaTipoNotaFiscal) {
			
			resultado = new RegistroTipoNotaFiscalVO();
			resultado.setNopDescricao(tipoNotaFiscal.getDescricao());
			resultado.setCfopEstado( (tipoNotaFiscal.getCfopEstado()!= null)?tipoNotaFiscal.getCfopEstado().getCodigo():"");
			resultado.setCfopOutrosEstados((tipoNotaFiscal.getCfopOutrosEstados()!=null)?tipoNotaFiscal.getCfopOutrosEstados().getCodigo():"");
			resultado.setTipoAtividade(tipoNotaFiscal.getTipoAtividade().getDescTipoDistribuidor());
			
			String processo = "";
			
			for(Processo pr : tipoNotaFiscal.getProcesso()){
				processo += pr.getDescricao() + tipoQuebraLinha;
			}
			
			resultado.setProcesso(processo);
			
			listaResultado.add(resultado);
			
		}
		
		return listaResultado;
	}
	
}
