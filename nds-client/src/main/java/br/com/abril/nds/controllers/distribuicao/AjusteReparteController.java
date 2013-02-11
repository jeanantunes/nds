package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.AjusteReparteService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
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
@Path("/distribuicao/ajusteReparte")
public class AjusteReparteController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private AjusteReparteService ajusteService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public AjusteReparteController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE)
	public void index(){
		this.carregarComboMotivoStatusCota();
	}
	
	@Post
	@Path("/novoAjuste")
	public void salvarAjuste (AjusteReparteDTO ajusteDTO){
		
		AjusteReparte ajuste = DTOParaModel(ajusteDTO);
		
		ajusteService.salvarAjuste(ajuste);
		
//		this.validarEntradaRegiao(nome);
		
		result.use(Results.json()).from(ajuste, "ajuste").recursive().serialize();
	}
	
	@Post
	@Path("/buscarCotaAjuste")
	public void buscarCotaAjuste (String sortorder, String sortname, int page, int rp){
		
		AjusteReparteDTO dto = new AjusteReparteDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<AjusteReparteDTO>> tableModel = montarCotasEmAjuste(dto);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<AjusteReparteDTO>> montarCotasEmAjuste (AjusteReparteDTO dto) {
		
		List<AjusteReparteDTO> cotasEmAjuste = ajusteService.buscarCotasEmAjuste(dto);
		
		if (cotasEmAjuste == null || cotasEmAjuste.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<AjusteReparteDTO>> tableModel = new TableModel<CellModelKeyValue<AjusteReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cotasEmAjuste));

		tableModel.setPage(dto.getPaginacao().getPaginaAtual());

		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	@Post
	@Path("/alterarAjuste")
	public void alterarAjuste(AjusteReparteDTO ajusteDTO) {
		
		AjusteReparte ajuste = DTOParaModel(ajusteDTO);
		
		ajusteService.alterarAjuste(ajuste);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste alterado com sucesso."), 
				"result").recursive().serialize();
	}

	private AjusteReparte DTOParaModel(AjusteReparteDTO ajusteDTO) {
		
		Date dataEHora = new Date();
		Timestamp data = new Timestamp(dataEHora.getTime());

		AjusteReparte ajuste = new AjusteReparte();
		
		ajuste.setCota(this.cotaService.obterPorNumeroDaCota(ajusteDTO.getNumeroCota()));
		ajuste.setAjusteAplicado(ajusteDTO.getAjusteAplicado());
		ajuste.setDataAlteracao(data);
		ajuste.setDataInicio(ajusteDTO.getDataInicioCadastro());
		ajuste.setDataFim(ajusteDTO.getDataFimCadastro());
		
		if (ajuste.getDataFim().before(ajuste.getDataInicio())){
			throw new ValidacaoException(TipoMensagem.WARNING, "Data final não pode ser menor que data inicial.");
		}
		
		
		String dias = this.calcularDiasPeriodo(ajusteDTO.getDataInicioCadastro(), ajusteDTO.getDataFimCadastro());
		
		/*
		int qtdDias = this.calcularDiasPeriodo(ajusteDTO.getDataInicioCadastro(), ajusteDTO.getDataFimCadastro());
		if (qtdDias > 180){
			throw new ValidacaoException(TipoMensagem.WARNING, "Período não pode ser maior que 180 dias.");
		}
		*/
		
		ajuste.setFormaAjuste(ajusteDTO.getFormaAjuste());
		ajuste.setMotivo(ajusteDTO.getMotivoAjuste());
		ajuste.setUsuario(this.usuarioService.getUsuarioLogado());
		
		return ajuste;
	}
	
	@Post
	@Path("/excluirAjuste")
	public void excluirAjuste(Long id) {
		
		ajusteService.excluirAjuste(id);

		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste excluído com sucesso."), 
				"result").recursive().serialize();
	}
	
	@Post
	@Path("/buscarAjustePorId")
	public void buscarAjustePorId (Long id){
		
		AjusteReparteDTO ajuste = ajusteService.buscarPorIdAjuste(id);
		
		result.use(Results.json()).withoutRoot().from(ajuste).recursive().serialize();
	}
	
	/*
	private void validarEntradaRegiao(AjusteReparte ajuste) {
//		if (nomeRegiao == null || (nomeRegiao.isEmpty())) {
//			throw new ValidacaoException(TipoMensagem.WARNING,
//					"Nome da regiao � obrigat�rio.");
//		}

		
		
////		List<RegiaoDTO> listaRegiaoDTO = regiaoService.buscarRegiao();
//
//		for (RegiaoDTO regiaoDTO : listaRegiaoDTO) {
//			if (regiaoDTO.getNomeRegiao().equalsIgnoreCase(nomeRegiao)) {
//				throw new ValidacaoException(TipoMensagem.WARNING, "Regi�o j� cadastrada.");
//			}
//		}
	}
	
	*/
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboMotivoStatusCota() {
		
		List<ItemDTO<MotivoAlteracaoSituacao, String>> listaMotivosStatusCota =
			new ArrayList<ItemDTO<MotivoAlteracaoSituacao, String>>();
		
		for (MotivoAlteracaoSituacao motivoAlteracaoSituacao : MotivoAlteracaoSituacao.values()) {
			
			listaMotivosStatusCota.add(new ItemDTO<MotivoAlteracaoSituacao, String>(
					motivoAlteracaoSituacao, motivoAlteracaoSituacao.toString())
			);
		}
		result.include("listaMotivosStatusCota", listaMotivosStatusCota);
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		List<AjusteReparteDTO> listaAjustes = ajusteService.buscarCotasEmAjuste (null);
			
			if(listaAjustes.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A pesquisa realizada n�o obteve resultado.");
			}
			
			FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaAjustes, AjusteReparteDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	private String calcularDiasPeriodo (Date dataInicial, Date dataFinal){
		
		Calendar dtFinal = Calendar.getInstance();
		dtFinal.setTime(dataFinal);
		
		Calendar dtInicial = Calendar.getInstance();
		dtFinal.setTime(dataInicial);
		
		long m1 = dtFinal.getTimeInMillis();
		long m2 = dtInicial.getTimeInMillis();
		
		Integer diasPeriodo = (int) ((m1 - m2) / (24*60*60*1000));
		
		return diasPeriodo.toString();
		
	}
	
	@Post
	@Path("/carregarSegmento")
	public void carregarSegmento (){
		TableModel<CellModelKeyValue<TipoSegmentoProduto>> tableModel = montarSegmento();
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
		private TableModel<CellModelKeyValue<TipoSegmentoProduto>> montarSegmento() {
		
		List<TipoSegmentoProduto> segmentos = ajusteService.buscarTodosSegmentos();
		
		TableModel<CellModelKeyValue<TipoSegmentoProduto>> tableModel = new TableModel<CellModelKeyValue<TipoSegmentoProduto>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(segmentos));
		
		tableModel.setPage(1);

		tableModel.setTotal(segmentos.size());

		return tableModel;
	}
}
