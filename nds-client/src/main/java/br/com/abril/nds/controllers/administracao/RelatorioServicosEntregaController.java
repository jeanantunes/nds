package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.RelatorioServicosEntregaVO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioServicosEntregaDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.RelatorioServicosEntregaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/relatorioServicosEntrega")
public class RelatorioServicosEntregaController {
	
	@Autowired
	private TransportadorService transportadorService;
	
	@Autowired
	private RelatorioServicosEntregaService relatorioServicosEntregaService;
	
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
	@Rules(Permissao.ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA)
	public void index(){
		
		List<Transportador> listTransportadores = this.transportadorService.buscarTransportadores();
		result.include("listTransportadores", listTransportadores);
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroRelatorioServicosEntregaDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		this.session.setAttribute(this.FILTRO_PESQUISA, filtro);
		
		FlexiGridDTO<RelatorioServicosEntregaDTO> flexiDTO = this.relatorioServicosEntregaService.pesquisar(filtro);
		
		List<RelatorioServicosEntregaVO> listVO = new ArrayList<RelatorioServicosEntregaVO>();
		for (RelatorioServicosEntregaDTO dto : flexiDTO.getGrid()) {
			listVO.add(new RelatorioServicosEntregaVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(flexiDTO.getTotalGrid()).page(page).serialize();
	}
	
	
	
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroRelatorioServicosEntregaDTO filtro = (FiltroRelatorioServicosEntregaDTO) this.session.getAttribute(this.FILTRO_PESQUISA);
		FlexiGridDTO<RelatorioServicosEntregaDTO> flexiDTO = this.relatorioServicosEntregaService.pesquisar(filtro);
		
		List<RelatorioServicosEntregaVO> listVO = new ArrayList<RelatorioServicosEntregaVO>();
		for (RelatorioServicosEntregaDTO dto : flexiDTO.getGrid()) {
			listVO.add(new RelatorioServicosEntregaVO(dto));
		}
		
		FileExporter.to("visao-estoque-conferencia-cega", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listVO, RelatorioServicosEntregaVO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
		return ndsFileHeader;
	}
	
	
	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
}
