package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/estoqueProdutosRecolhimento")
@Rules(Permissao.ROLE_ESTOQUE_PRODUTOS_EM_RECOLHIMENTO)
public class EstoqueProdutosRecolhimentoController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession sessao;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private String FILTRO_PESQUISA = "filtroEstoqueProdutosRecolhimento";
	
	public void estoqueProdutosRecolhimento(){}
	
	@Path("/")
	public void index(){
		
		result.include("dataRecolhimento", 
			new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(
				this.distribuidorService.obterDataOperacaoDistribuidor()));
		
		result.forwardTo(EstoqueProdutosRecolhimentoController.class).estoqueProdutosRecolhimento();
	}
	
	@Post
	public void pesquisar(Date dataRecolhimento, 
			int page, int rp, String sortname, String sortorder){
		
		FiltroEstoqueProdutosRecolhimento filtro = 
			new FiltroEstoqueProdutosRecolhimento(dataRecolhimento, 
				page, rp, sortname, sortorder);
		
		this.sessao.setAttribute(FILTRO_PESQUISA, filtro);
		
		Long qtd = 
			this.estoqueProdutoService.buscarEstoqueProdutoRecolhimentoCount(filtro);
		
		if (qtd > 0){
		
			List<EstoqueProdutoRecolimentoDTO> lista = 
				this.estoqueProdutoService.buscarEstoqueProdutoRecolhimento(filtro);
			
			result.use(FlexiGridJson.class).from(lista).page(page).total(qtd.intValue()).serialize();
			
			return;
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException{
		
		FiltroEstoqueProdutosRecolhimento filtro = 
			(FiltroEstoqueProdutosRecolhimento) this.sessao.getAttribute(FILTRO_PESQUISA);
		
		if (filtro != null){
			
			filtro.getPaginacaoVO().setPaginaAtual(null);
			
			List<EstoqueProdutoRecolimentoDTO> lista = 
					this.estoqueProdutoService.buscarEstoqueProdutoRecolhimento(filtro);
			
			FileExporter.to("estoqueProdutosRecolhimento", fileType).inHTTPResponse(this.getNDSFileHeader(), 
					filtro, null, lista, EstoqueProdutoRecolimentoDTO.class, this.response);
		}
		
		result.nothing();
	}
}