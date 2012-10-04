package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
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
	private HttpServletResponse httpResponse;
	
	
	@Path("/")
	@Rules(Permissao.ROLE_EXPEDICAO_ROMANEIOS)
	public void index() {
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS, null);
		carregarComboBox();
		carregarComboRoteiro();
		carregarComboRota();
		
		Date dataAtual = new Date();
		
		result.include("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(dataAtual));
		
		result.include("produtos", this.pesquisarProdutosDataLancamento(dataAtual));
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
	
	@Post
	public void carregarProdutosDataLancamento(Date data){
		
		result.use(Results.json()).from(this.pesquisarProdutosDataLancamento(data), "result").recursive().serialize();
	}
	
	private List<ItemDTO<Long, String>> pesquisarProdutosDataLancamento(Date data){
		
		List<ProdutoEdicao> produtos = this.romaneioService.buscarProdutosLancadosData(data);
		
		List<ItemDTO<Long, String>> listaItens = new ArrayList<ItemDTO<Long,String>>();
		for (ProdutoEdicao produto : produtos){
			
			listaItens.add(new ItemDTO<Long, String>(produto.getId(), produto.getNomeComercial()));
		}
		
		return listaItens;
	}
	
	private TableModel<CellModelKeyValue<RomaneioDTO>> efetuarConsultaRomaneio(FiltroRomaneioDTO filtro) {
		
		List<RomaneioDTO> listaRomaneios = this.romaneioService.buscarRomaneio(filtro, true);
		
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
	

	@Post
	public void pesquisarQuantidadeCotasEntrega(){
		FiltroRomaneioDTO filtro = (FiltroRomaneioDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS);
		
		this.result.use(Results.json()).from(this.romaneioService.buscarTotalDeCotas(filtro)).serialize();
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException, URISyntaxException, JRException {
		
		FiltroRomaneioDTO filtro = (FiltroRomaneioDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_ROMANEIOS);
		
		byte[] arquivo = this.romaneioService.gerarRelatorio(filtro, "", fileType);
		
		if (FileType.PDF == fileType){
		
			this.httpResponse.setContentType("application/pdf");
		} else if (FileType.XLS == fileType) {
			
			this.httpResponse.setContentType("application/xls");
		}
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=romaneio" + fileType.getExtension());

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.nothing();
	}
	
	private void validarEntrada(FiltroRomaneioDTO filtro) {
		
		if (filtro.getData() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha a data!");
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
