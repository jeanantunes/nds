package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ResultadoRomaneioVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/romaneio")
public class RomaneioController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_ROMANEIOS = "filtroRomaneio";
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private RomaneioService romaneioService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	
	@Path("/")
	@Rules(Permissao.ROLE_EXPEDICAO_ROMANEIOS)
	public void index() {
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS, null);
		carregarComboBox();
		carregarComboRoteiro();
		carregarComboRota(); 
	}
	@Post
	@Path("/pesquisarRomaneio")
	public void pesquisarRomaneio(FiltroRomaneioDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<RomaneioDTO>> tableModel = efetuarConsultaRomaneio(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<RomaneioDTO>> efetuarConsultaRomaneio(FiltroRomaneioDTO filtro) {
		
		List<RomaneioDTO> listaRomaneios = this.romaneioService.buscarRomaneio(filtro, "limitar");
		
		TableModel<CellModelKeyValue<RomaneioDTO>> tableModel = new TableModel<CellModelKeyValue<RomaneioDTO>>();
		
		Integer totalRegistros = this.romaneioService.buscarTotalDeRomaneios(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRomaneios));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	


	
	
	public void exportar(FileType fileType) throws IOException {
		
		FiltroRomaneioDTO filtro = (FiltroRomaneioDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS);
		
		List<RomaneioDTO> listaDTOParaExportacao = this.romaneioService.buscarRomaneio(filtro, "naoLimitar");
		
		if(listaDTOParaExportacao.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		Integer totalRegistros = this.romaneioService.buscarTotalDeRomaneios(filtro);
		ResultadoRomaneioVO romaneioVO = new ResultadoRomaneioVO();
		romaneioVO.setTotalCotas(totalRegistros);
		
		FileExporter.to("romaneios", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, romaneioVO, 
				listaDTOParaExportacao, RomaneioDTO.class, this.httpResponse);
			
		
		result.nothing();
	}
	
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
	
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
	}

	private void validarEntrada(FiltroRomaneioDTO filtro) {
		boolean validar = false;
		
		if(filtro.getIdBox()==null && filtro.getIdRoteiro()==null && filtro.getIdRota()==null){
			validar = true;
		}
		
		if(validar){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");
		}
		
	}
	
	private void tratarFiltro(FiltroRomaneioDTO filtroAtual) {

		FiltroRomaneioDTO filtroSession = (FiltroRomaneioDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS, filtroAtual);
	}

	private void carregarComboBox() {
		List<Box> boxs = boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : boxs) {
			
			lista.add(
				new ItemDTO<Long, String>(box.getId(), box.getNome()));
		}
		
		result.include("listaBox", lista);
	}
	
	private void carregarComboRoteiro() {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiro("descricaoRoteiro", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros) {
			try{
				roteiro.getRotas();
			} catch (Exception e) {
				e.getMessage();
			}
			
			lista.add(
				new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("listaRoteiro", lista);
	}
	
	private void carregarComboRota() {
		List<Rota> rotas = roteirizacaoService.buscarRota("descricaoRota", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas) {
			
			lista.add(
				new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("listaRota", lista);
	}

}
