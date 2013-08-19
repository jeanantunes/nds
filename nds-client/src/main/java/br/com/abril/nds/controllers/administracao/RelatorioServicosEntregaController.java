package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.RelatorioServicosEntregaDetalheVO;
import br.com.abril.nds.client.vo.RelatorioServicosEntregaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/relatorioServicosEntrega")
@Rules(Permissao.ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA)
public class RelatorioServicosEntregaController extends BaseController {
	
	@Autowired
	private TransportadorService transportadorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	private Result result;
	
	private final String FILTRO_PESQUISA = "FILTRO_PESQUISA";
	

	public RelatorioServicosEntregaController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		
		List<Transportador> listTransportadores = this.transportadorService.buscarTransportadores();
		result.include("listTransportadores", listTransportadores);
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroRelatorioServicosEntregaDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		this.session.setAttribute(this.FILTRO_PESQUISA, filtro);
		
		FlexiGridDTO<CotaTransportadorDTO> flexiDTO = this.transportadorService.obterResumoTransportadorCota(filtro);
		
		if (flexiDTO == null || flexiDTO.getGrid() == null || flexiDTO.getGrid().isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		List<RelatorioServicosEntregaVO> listVO = new ArrayList<RelatorioServicosEntregaVO>();
		for (CotaTransportadorDTO dto : flexiDTO.getGrid()) {
			listVO.add(new RelatorioServicosEntregaVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(flexiDTO.getTotalGrid()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhe.json")
	public void pesquisarDetalhe(FiltroRelatorioServicosEntregaDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<MovimentoFinanceiroDTO> listDTO = this.transportadorService.obterDetalhesTrasportadorPorCota(filtro);
		
		List<RelatorioServicosEntregaDetalheVO> listVO = new ArrayList<RelatorioServicosEntregaDetalheVO>();
		for (MovimentoFinanceiroDTO dto : listDTO) {
			listVO.add(new RelatorioServicosEntregaDetalheVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).page(1).serialize();
	}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroRelatorioServicosEntregaDTO filtro = (FiltroRelatorioServicosEntregaDTO) this.session.getAttribute(this.FILTRO_PESQUISA);
		FlexiGridDTO<CotaTransportadorDTO> flexiDTO = this.transportadorService.obterResumoTransportadorCota(filtro);
		
		List<RelatorioServicosEntregaVO> listVO = new ArrayList<RelatorioServicosEntregaVO>();
		for (CotaTransportadorDTO dto : flexiDTO.getGrid()) {
			listVO.add(new RelatorioServicosEntregaVO(dto));
		}
		
		FileExporter.to("visao-estoque-conferencia-cega", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listVO, RelatorioServicosEntregaVO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
}
