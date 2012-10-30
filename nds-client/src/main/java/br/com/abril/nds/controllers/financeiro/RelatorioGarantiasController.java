package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.RelatorioDetalheGarantiaVO;
import br.com.abril.nds.client.vo.RelatorioGarantiasVO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.RelatorioGarantiasService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/relatorioGarantias")
public class RelatorioGarantiasController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private RelatorioGarantiasService relatorioGarantiasService;
	
	private static List<ItemDTO<TipoGarantia,String>> listaTiposGarantia =  new ArrayList<ItemDTO<TipoGarantia,String>>();
	
	private static List<ItemDTO<TipoStatusGarantia,String>> listaTiposStatusGarantia =  new ArrayList<ItemDTO<TipoStatusGarantia,String>>();
	
	private static final String FILTRO_RELATORIO_GARANTIAS = "FILTRO_RELATORIO_GARANTIAS";
	
	public RelatorioGarantiasController(Result result){
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS)
	public void index() {
		
		listaTiposGarantia = distribuidorService.getComboTiposGarantia();
		listaTiposStatusGarantia = distribuidorService.getComboTiposStatusGarantia();
		
		result.include("listaTiposGarantia" , listaTiposGarantia);
		result.include("listaTiposStatusGarantia" , listaTiposStatusGarantia);


	}
	@Path("/pesquisarTodasGarantias.json")
	public void pesquisarTodasGarantias(FiltroRelatorioGarantiasDTO filtro, String sortname, String sortorder, int rp, int page){
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacao(paginacaoVO);
		filtro.setDataBaseCalculo(distribuidorService.obter().getDataOperacao());
		
		this.session.setAttribute(FILTRO_RELATORIO_GARANTIAS, filtro);
		
		if (filtro.getTipoGarantia().equalsIgnoreCase("Selecione...")) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O tipo de garantia deve ser informado."));
		}
		else if(filtro.getStatusGarantia().equalsIgnoreCase("Selecionar...")){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O status de garantia deve ser informado."));
			
		}
		
		List<RelatorioGarantiasVO> garantiasVO = new ArrayList<RelatorioGarantiasVO>();
		FlexiGridDTO<RelatorioGarantiasDTO> flexDTO = relatorioGarantiasService.gerarTodasGarantias(filtro);
		
		if (flexDTO == null || flexDTO.getGrid().size() == 0) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		for(RelatorioGarantiasDTO dto : flexDTO.getGrid()){
			
			garantiasVO.add(new RelatorioGarantiasVO(dto));
			
		}
		
		result.use(FlexiGridJson.class).from(garantiasVO).total(garantiasVO.size()).serialize();
		
	}
	
	
	@Path("/pesquisarGarantia.json")
	public void pesquisarGarantia(FiltroRelatorioGarantiasDTO filtro, String sortname, String sortorder, int rp, int page){
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacao(paginacaoVO);
		filtro.setDataBaseCalculo(distribuidorService.obter().getDataOperacao());
		
		this.session.setAttribute(FILTRO_RELATORIO_GARANTIAS, filtro);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM/yy",new Locale("pt", "BR"));
		String data = sdf.format(distribuidorService.obter().getDataOperacao());
		
		if (filtro.getTipoGarantia().equalsIgnoreCase("Selecione...")) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O tipo de garantia deve ser informado."));
		}
		else if(filtro.getStatusGarantia().equalsIgnoreCase("Selecionar...")){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O status de garantia deve ser informado."));
			
		}
		
		List<RelatorioDetalheGarantiaVO> garantiasVO = new ArrayList<RelatorioDetalheGarantiaVO>();
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> flexDTO = relatorioGarantiasService.gerarPorTipoGarantia(filtro);
		 
		if (flexDTO == null || flexDTO.getGrid().size() == 0) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		for(RelatorioDetalheGarantiaDTO dto : flexDTO.getGrid()){
			
			garantiasVO.add(new RelatorioDetalheGarantiaVO(dto,data));
		}
		
		result.use(FlexiGridJson.class).from(garantiasVO).total(garantiasVO.size()).serialize();

	}
	
	
	@Path("/exportPesquisarTodasGarantias")
	public void exportPesquisarTodasGarantias(FileType fileType) throws IOException{
		
		FiltroRelatorioGarantiasDTO filtro = (FiltroRelatorioGarantiasDTO) this.session.getAttribute(FILTRO_RELATORIO_GARANTIAS);
		
		List<RelatorioGarantiasVO> garantiasVO = new ArrayList<RelatorioGarantiasVO>();
		FlexiGridDTO<RelatorioGarantiasDTO> flexDTO = relatorioGarantiasService.gerarTodasGarantias(filtro);
		
		for(RelatorioGarantiasDTO dto : flexDTO.getGrid()){
			garantiasVO.add(new RelatorioGarantiasVO(dto));
		}
		
		FileExporter.to("relatorio-garantias", fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null,
				garantiasVO, RelatorioGarantiasVO.class, this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	@Path("/exportPesquisarGarantia")
	public void exportPesquisarGarantia(FileType fileType) throws IOException{
		
		FiltroRelatorioGarantiasDTO filtro = (FiltroRelatorioGarantiasDTO) this.session.getAttribute(FILTRO_RELATORIO_GARANTIAS);
		
		List<RelatorioDetalheGarantiaVO> garantiasVO = new ArrayList<RelatorioDetalheGarantiaVO>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM/yy",new Locale("pt", "BR"));
		String data = sdf.format(distribuidorService.obter().getDataOperacao());
		
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> flexDTO = relatorioGarantiasService.gerarPorTipoGarantia(filtro);
		
		for(RelatorioDetalheGarantiaDTO dto : flexDTO.getGrid()){
			garantiasVO.add(new RelatorioDetalheGarantiaVO(dto,data));
		}
		
		FileExporter.to("relatorio-garantias", fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null,
				garantiasVO, RelatorioDetalheGarantiaVO.class, this.httpServletResponse);
		
		result.use(Results.nothing());

	}

	private NDSFileHeader getNDSFileHeader(Date data) {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(data);
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
