package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/regiao")
public class RegiaoController extends BaseController {
	
	
	private Result result;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroCotasRegiao";
	
	@Autowired
	private RegiaoService regiaoService; // interface
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public RegiaoController(Result result) {
		this.result = result;
	}
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO)
	public void index(){
		this.carregarComboRegiao();
	}
	@Post
	@Path("/salvarRegiao")
	public void salvarRegiao (String nome, boolean isFixa){
		
		Regiao regiao = new Regiao();
		regiao.setNomeRegiao(nome);
		regiao.setRegiaoIsFixa(isFixa);
		regiao.setIdUsuario(this.usuarioService.getUsuarioLogado());
		
		Date dataEHora = new Date();
		Timestamp data = new Timestamp(dataEHora.getTime());

		regiao.setDataRegiao(data);
		
		regiaoService.salvarRegiao(regiao);
	}
	
	@Post
	@Path("/addCotaNaRegiao")
	public void addCotaNaRegiao (int[] numeroCota, Long idRegiao){

		for (int numCota : numeroCota) {
			RegistroCotaRegiao registro = new RegistroCotaRegiao();
			
			Date dataEHora = new Date();
			Timestamp data = new Timestamp(dataEHora.getTime());
			
			registro.setRegiao(this.regiaoService.obterRegiaoPorId(idRegiao));
			registro.setCota(this.cotaService.obterPorNumeroDaCota(numCota));
			registro.setUsuario(this.usuarioService.getUsuarioLogado());
			registro.setDataAlteracao(data);
			
			regiaoService.addCotaNaRegiao(registro);
		}
	}
	
	@Post
	@Path("/excluirRegistroCotaRegiao")
	public void excluirRegistroCotaRegiao(Long id) {
		
		this.regiaoService.excluirRegistroCotaRegiao(id);
			
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Cota removida da região com sucesso!"), 
				"result").recursive().serialize();
	}
	
	/**
	 * Exclui uma Regiao.
	 * 
	 * @param id
	 */
	@Post
	public void excluirRegiao(Long id) {
		
		this.regiaoService.excluirRegiao(id);
			
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Regiao excluída com sucesso!"), 
				"result").recursive().serialize();
	}
	
	
	private void carregarComboRegiao() {
		
		List<ItemDTO<Long,String>> comboRegiao =  new ArrayList<ItemDTO<Long,String>>();
		List<RegiaoDTO> regioes = regiaoService.buscarRegiao();
		
		for (RegiaoDTO itemRegiao : regioes) {
			comboRegiao.add(new ItemDTO<Long,String>(itemRegiao.getIdRegiao() , itemRegiao.getNomeRegiao()));
		}
		result.include("listaRegiao",comboRegiao );
	}
	
	@Post
	@Path("/carregarCotasRegiao")
	public void carregarCotasRegiao (FiltroCotasRegiaoDTO filtro, String sortorder, String sortname, int page, int rp){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
//		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = efetuarConsultaCotasDaRegiao(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/carregarRegiao")
	public void carregarRegiao (String sortorder, String sortname, int page, int rp){
		
		List<RegiaoDTO> listaRegiaoDTO = regiaoService.buscarRegiao();
		
		TableModel<CellModelKeyValue<RegiaoDTO>> tableModel = efetuarConsultaRegiao(listaRegiaoDTO);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<RegiaoDTO>> efetuarConsultaRegiao(List<RegiaoDTO> listaRegiao) {

		TableModel<CellModelKeyValue<RegiaoDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRegiao));		

		tableModel.setTotal(listaRegiao.size());

		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<RegiaoCotaDTO>> efetuarConsultaCotasDaRegiao(FiltroCotasRegiaoDTO filtro) {

		List<RegiaoCotaDTO> listaCotasRegiaoDTO = regiaoService.carregarCotasRegiao(filtro.getId());

		if (listaCotasRegiaoDTO == null || listaCotasRegiaoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasRegiaoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(listaCotasRegiaoDTO.size());

		return tableModel;
	}
	
	
	@Post
	@Path("/buscarPorCep")
	public void buscarPorCep(FiltroCotasRegiaoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		List<RegiaoCotaDTO> listaCotasCep = regiaoService.buscarPorCEP(filtro);
		
		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = montarTableModelBuscaCep(filtro, listaCotasCep);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<RegiaoCotaDTO>> montarTableModelBuscaCep (FiltroCotasRegiaoDTO filtro, List<RegiaoCotaDTO> listaCotaDoCep) {

		if (listaCotaDoCep == null || listaCotaDoCep.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaDoCep));

		tableModel.setPage(1);

		tableModel.setTotal(listaCotaDoCep.size());

		return tableModel;
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroCotasRegiaoDTO filtro = (FiltroCotasRegiaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<RegiaoCotaDTO> listaCotasRegiaoDTO = regiaoService.carregarCotasRegiao(filtro.getId());
			
			if(listaCotasRegiaoDTO.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("Cotas_Cadastradas_Na_Região", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaCotasRegiaoDTO, RegiaoCotaDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	private void tratarFiltro(FiltroCotasRegiaoDTO filtroAtual) {

		FiltroCotasRegiaoDTO filtroSession = (FiltroCotasRegiaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

}
