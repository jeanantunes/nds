package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import br.com.abril.nds.model.cadastro.Cota;
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
@Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE)
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
	public void index(){
		this.carregarComboMotivoStatusCota();
		this.carregarComboSegmento();
	}
	
	@Post
	@Path("/novoAjuste")
    @Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO)
	public void salvarAjuste(AjusteReparteDTO ajusteDTO){
		
		AjusteReparte ajuste = DTOParaModel(ajusteDTO);
		
		evitarCotaRepetida(ajusteDTO);
		
		ajusteService.salvarAjuste(ajuste);
		
        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste incluído com sucesso."),
                "result").recursive().serialize();
	}
	
	@Post
	@Path("/incluirAjusteSegmento")
    @Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO)
	public void salvarAjusteSegmento (AjusteReparteDTO ajusteDTO, BigDecimal [] ajustes, Long [] segmentos){

		validarInsercaoPorSegmento(ajustes, segmentos);
		
		evitarCotaRepetida(ajusteDTO);
		
        // Testar essa inserção deste IF if(seg...)
		
		for (int i = 0; i < segmentos.length; i++) {
			 		if(segmentos[i] != null){
					TipoSegmentoProduto segmento = ajusteService.buscarSegmentoPorID(segmentos[i]);
					ajusteDTO.setAjusteAplicado(ajustes[i]);
					ajusteDTO.setTipoSegmento_Ajuste(segmento);
					evitarSegmentosRepetidos(ajusteDTO, segmento);
					AjusteReparte ajusteModel = DTOParaModel(ajusteDTO);
					ajusteService.salvarAjuste(ajusteModel);
			 		}
		 }
		
        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste incluído com sucesso."),
				"result").recursive().serialize();
	}

	private void validarInsercaoPorSegmento(BigDecimal[] ajustes, Long[] segmentos) {
		if((segmentos.length == 0) || (ajustes.length == 0)){
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe no mínimo 1 segmento com 1 índice para ajuste.");
		}
	}
	
	@Post
	@Path("/buscarCotaAjuste")
	public void buscarCotaAjuste (String sortorder, String sortname, int page, int rp){
		
		AjusteReparteDTO dto = new AjusteReparteDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<AjusteReparteDTO>> tableModel = montarCotasEmAjuste(dto);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<AjusteReparteDTO>> montarCotasEmAjuste(AjusteReparteDTO dto) {
		
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
    @Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO)
	public void alterarAjuste(AjusteReparteDTO ajusteDTO, Long id) {
		ajusteDTO.setIdAjusteReparte(id);
		
		validarEntradaAjuste(ajusteDTO);

		AjusteReparte ajuste = DTOParaModel(ajusteDTO);
		
		ajusteService.alterarAjuste(ajuste);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste alterado com sucesso."), 
				"result").recursive().serialize();
	}
	
	@Post
	@Path("/alterarAjusteSegmento")
    @Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO)
	public void alterarAjusteSegmento(AjusteReparteDTO ajusteDTO, Long id, BigDecimal ajuste, Long [] segmentos) {
		
		TipoSegmentoProduto segmento = ajusteService.buscarSegmentoPorID(segmentos[0]);
		ajusteDTO.setAjusteAplicado(ajuste);
		ajusteDTO.setTipoSegmento_Ajuste(segmento);
		ajusteDTO.setIdAjusteReparte(id);

		validarEntradaAjuste(ajusteDTO);
		evitarSegmentosRepetidos(ajusteDTO, segmento);
		
		AjusteReparte ajusteModel = DTOParaModel(ajusteDTO);
		
		ajusteService.alterarAjuste(ajusteModel);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste alterado com sucesso."), 
				"result").recursive().serialize();
	}

	private AjusteReparte DTOParaModel(AjusteReparteDTO ajusteDTO) {
		
		Date dataEHora = new Date();
		Timestamp data = new Timestamp(dataEHora.getTime());
		
		validarEntradaAjuste(ajusteDTO);
		validarDiferencaData(ajusteDTO.getDataFimCadastro(), ajusteDTO.getDataInicioCadastro());

		AjusteReparte ajuste = new AjusteReparte();
		
		ajuste.setId(ajusteDTO.getIdAjusteReparte());
		ajuste.setCota(this.cotaService.obterPorNumeroDaCota(ajusteDTO.getNumeroCota()));
		ajuste.setAjusteAplicado(ajusteDTO.getAjusteAplicado());
		ajuste.setDataAlteracao(data);
		ajuste.setDataInicio(ajusteDTO.getDataInicioCadastro());
		ajuste.setDataFim(ajusteDTO.getDataFimCadastro());
		ajuste.setFormaAjuste(ajusteDTO.getFormaAjuste());
		ajuste.setMotivo(ajusteDTO.getMotivoAjuste());
		ajuste.setUsuario(this.usuarioService.getUsuarioLogado());
		ajuste.setTipoSegmentoAjuste(ajusteDTO.getTipoSegmento_Ajuste());
		
		return ajuste;
	}
	
	@Post
	@Path("/excluirAjuste")
    @Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO)
	public void excluirAjuste(Long id) {
		
		ajusteService.excluirAjuste(id);

        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Ajuste excluído com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post
	@Path("/buscarAjustePorId")
	public void buscarAjustePorId (Long id){
		
		AjusteReparteDTO ajuste = ajusteService.buscarPorIdAjuste(id);
		
		result.use(Results.json()).withoutRoot().from(ajuste).recursive().serialize();
	}
	
	@Post
	@Path("/qtdAjustesSegmento")
	public void qtdAjustesSegmento (Integer nmCota){

		if (nmCota == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Insira o número da cota.");
		}else{
		
		Cota cota = cotaService.obterPorNumeroDaCota(nmCota );
		
		Long idCota = cota.getId();
		
		int qtdAjustesCadastrados = ajusteService.qtdAjusteSegmento(idCota);
		
		
		result.use(Results.json()).withoutRoot().from(qtdAjustesCadastrados).recursive().serialize();
		}
	}
	
	@Post
	@Path("/carregarVendaMedia")
	public void carregarVendaMedia() {
		Integer vendaMediaMais = ajusteService.buscarVendaMedia();

		result.use(Results.json()).withoutRoot().from(vendaMediaMais).recursive().serialize();
	}
	
	private void validarEntradaAjuste(AjusteReparteDTO ajusteDTO) {
		
		if (ajusteDTO.getNumeroCota() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota é obrigatório.");
		}
		
		if (ajusteDTO.getNomeCota() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nome da cota é obrigatório.");
		}
		
		if (ajusteDTO.getFormaAjuste() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,	"Selecione um tipo de ajuste.");
		}
		
		if (ajusteDTO.getAjusteAplicado() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,	"Informe um indice para ajuste.");
		}
		
		if (ajusteDTO.getMotivoAjuste() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,	"Selecione um motivo de ajuste.");
		}
		
		Cota cota = cotaService.obterPorNumeroDaCota(ajusteDTO.getNumeroCota());
		ajusteDTO.setStatus(cota.getSituacaoCadastro());
		
		if((ajusteDTO.getStatus().toString()) != "Ativo"){
			if(ajusteDTO.getStatus().toString() != "Suspenso"){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Esta cota não pode receber ajuste. (O seu Status não é ATIVO ou SUSPENSO)");
			}
		}

		if (ajusteDTO.getDataInicioCadastro() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inicial é obrigatório.");
		}
		
		if (ajusteDTO.getDataFimCadastro() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Data final é obrigatório.");
		}
		
		if (ajusteDTO.getDataFimCadastro().before(ajusteDTO.getDataInicioCadastro())){
            throw new ValidacaoException(TipoMensagem.WARNING, "A Data Final não pode ser antes que a Data Inicial.");
		}
	}

	private void evitarCotaRepetida(AjusteReparteDTO ajusteDTO) {
		List<AjusteReparteDTO> ajustesJaCadastrados = ajusteService.buscarCotasEmAjuste(null);

		for (AjusteReparteDTO ajusteJaCadastrado : ajustesJaCadastrados) {
			if ((ajusteJaCadastrado.getNumeroCota()).equals(ajusteDTO.getNumeroCota())) {
                throw new ValidacaoException(TipoMensagem.ERROR, "Cota em Ajuste, já cadastrada.");
			}
		}
	}
	
	private void evitarSegmentosRepetidos(AjusteReparteDTO ajusteDTO, TipoSegmentoProduto segmento) {
		Cota cota = cotaService.obterPorNumeroDaCota(ajusteDTO.getNumeroCota());
		Long idCota = cota.getId();
		AjusteReparteDTO ajusteCadastrado = new AjusteReparteDTO();
		
		List<AjusteReparteDTO> cotaEmAjuste = ajusteService.buscarPorIdCota(idCota);
		
		if(ajusteDTO.getIdAjusteReparte() != null){
			ajusteCadastrado = ajusteService.buscarPorIdAjuste(ajusteDTO.getIdAjusteReparte());
		}
		
		if(ajusteCadastrado.getIdSegmento() != ajusteDTO.getTipoSegmento_Ajuste().getId()){
			for (AjusteReparteDTO ajusteReparteDTO : cotaEmAjuste) {
				if ((ajusteReparteDTO.getIdSegmento()) == (segmento.getId())){
                    throw new ValidacaoException(TipoMensagem.ERROR, "Ajuste por segmento já cadastrado.");
				}
			}
		}
	}
	
	private void carregarComboSegmento() {
		List<TipoSegmentoProduto> ListaSegmentos = ajusteService.buscarTodosSegmentos();
		result.include("listaSegmentos", ListaSegmentos);
	}
	
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
            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
			}
			
        FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(this.getNDSFileHeader(), null,
					listaAjustes, AjusteReparteDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	private void validarDiferencaData(Date date1, Date date2) { 
		long diferenca = ((date1.getTime()-date2.getTime())/86400000);
			if (diferenca > 180){
            throw new ValidacaoException(TipoMensagem.WARNING, "Período não pode ser maior que 180 dias.");
			}
		}
}