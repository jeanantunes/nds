package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ItemAutoComplete;
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
@Path("/distribuicao/classificacaoNaoRecebida")
@Rules(Permissao.ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA)
public class ClassificacaoNaoRecebidaController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroClassificacaoNaoRecebidaDTO";
	
    private static final ValidacaoVO VALIDACAO_VO_MENSAGEM_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS,
            "Operação realizada com sucesso.");
	
	@Autowired
	private Result result;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ClassificacaoNaoRecebidaService classificacaoNaoRecebidaService; 
	
	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse; 
	
    @Path("/")
	public void index(){
		this.carregarComboClassificacao();
	}
	
	@Post
	public void pesquisarCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro, String sortorder, String sortname, int page, int rp ){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroClassificacao(filtro);
		
		List<CotaQueNaoRecebeClassificacaoDTO> listaCotaQueRecebeClassificacaoDTO = this.classificacaoNaoRecebidaService.obterCotasQueNaoRecebemClassificacao(filtro);

		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<CotaQueNaoRecebeClassificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueNaoRecebeClassificacaoDTO>>();
		
		configurarTableModelComPaginacao(listaCotaQueRecebeClassificacaoDTO, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	public void pesquisarCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro, String sortorder, String sortname, int page, int rp ){
		
		validarEntradaFiltroClassificacao(filtro);
		
		List<CotaQueRecebeClassificacaoDTO> listaCotaQueRecebeClassificacaoDTO = this.classificacaoNaoRecebidaService.obterCotasQueRecebemClassificacao(filtro);

		TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>>();
		
		configurarTableModelSemPaginacao(listaCotaQueRecebeClassificacaoDTO, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
    @Rules(Permissao.ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO)
	public void inserirCotaNaClassificacaoNaoRecebida(Integer[] numerosCota, FiltroClassificacaoNaoRecebidaDTO filtro){
		validarEntradaFiltroClassificacao(filtro);
		
		TipoClassificacaoProduto tipoClassificacaoProduto = this.tipoClassificacaoProdutoService.buscarPorId(filtro.getIdTipoClassificacaoProduto());
		Usuario usuarioLogado = getUsuarioLogado();
		
		List<ClassificacaoNaoRecebida> listaClassificacaoNaoRecebida = new ArrayList<>();
		for (Integer numeroCota : numerosCota) {
			ClassificacaoNaoRecebida classificacaoNaoRecebida = new ClassificacaoNaoRecebida();
			classificacaoNaoRecebida.setTipoClassificacaoProduto(tipoClassificacaoProduto);
			classificacaoNaoRecebida.setCota(cotaService.obterPorNumeroDaCota(numeroCota));
			classificacaoNaoRecebida.setUsuario(usuarioLogado);
			classificacaoNaoRecebida.setDataAlteracao(new Date());
			
			listaClassificacaoNaoRecebida.add(classificacaoNaoRecebida);
		}
		
		this.classificacaoNaoRecebidaService.inserirListaClassificacaoNaoRecebida(listaClassificacaoNaoRecebida);
		
		result.use(Results.json()).from(VALIDACAO_VO_MENSAGEM_SUCESSO,"result").recursive().serialize();
	}
	
	@Post
    @Rules(Permissao.ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO)
	public void inserirClassificacaoNaCota(Long[] idsTipoClassificacaoProduto, FiltroClassificacaoNaoRecebidaDTO filtro){
		validarEntradaFiltroCota(filtro);

		Cota cota = null;
		Usuario usuarioLogado = getUsuarioLogado();
		
		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			cota = (cotaService.obterPorNumeroDaCota(filtro.getCotaDto().getNumeroCota()));
		}else {
			cota =(cotaService.obterPorNome(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()))).get(0);
		}
		
		List<ClassificacaoNaoRecebida> listaClassificacaoNaoRecebida = new ArrayList<>();
		for (Long idTipoClassificacaoProduto : idsTipoClassificacaoProduto) {
			ClassificacaoNaoRecebida classificacaoNaoRecebida = new ClassificacaoNaoRecebida();
			classificacaoNaoRecebida.setTipoClassificacaoProduto(tipoClassificacaoProdutoService.buscarPorId(idTipoClassificacaoProduto));
			classificacaoNaoRecebida.setCota(cota);
			classificacaoNaoRecebida.setUsuario(usuarioLogado);
			classificacaoNaoRecebida.setDataAlteracao(new Date());
			
			listaClassificacaoNaoRecebida.add(classificacaoNaoRecebida);
		}
		
		this.classificacaoNaoRecebidaService.inserirListaClassificacaoNaoRecebida(listaClassificacaoNaoRecebida);
		
		result.use(Results.json()).from(VALIDACAO_VO_MENSAGEM_SUCESSO,"result").recursive().serialize();
	}
	
	@Post
	public void pesquisarClassificacoesNaoRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroCota(filtro);
		
		filtro.getCotaDto().setNomePessoa(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()));
		
		List<ClassificacaoNaoRecebidaDTO> listaClassificacaoNaoRecebidaDTO = this.classificacaoNaoRecebidaService.obterClassificacoesNaoRecebidasPelaCota(filtro);

		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<CotaQueNaoRecebeClassificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueNaoRecebeClassificacaoDTO>>();
		
		configurarTableModelComPaginacao(listaClassificacaoNaoRecebidaDTO, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	public void pesquisarClassificacoesRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro){
		validarEntradaFiltroCota(filtro);
		
		List<TipoClassificacaoProduto> listaTipoClassificacaoProduto = this.classificacaoNaoRecebidaService.obterClassificacoesRecebidasPelaCota(filtro);

		TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>>();
		
		configurarTableModelSemPaginacao(listaTipoClassificacaoProduto, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
    @Rules(Permissao.ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO)
	public void excluirClassificacaoNaoRecebida(Long id){
		this.classificacaoNaoRecebidaService.excluirClassificacaoNaoRecebida(id);
		
		result.use(Results.json()).from(VALIDACAO_VO_MENSAGEM_SUCESSO,"result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Get
	public void exportar(FileType fileType, boolean porCota) throws IOException {
		
		List listaDto = null;
		Class classDto = null;
		String fileName = null;
		
		FiltroClassificacaoNaoRecebidaDTO filtro = (FiltroClassificacaoNaoRecebidaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (porCota) {
			listaDto = this.classificacaoNaoRecebidaService.obterClassificacoesNaoRecebidasPelaCota(filtro);
			classDto = ClassificacaoNaoRecebidaDTO.class;
            fileName = "Classificações_não_recebidas";
		}else {
			listaDto = this.classificacaoNaoRecebidaService.obterCotasQueNaoRecebemClassificacao(filtro);
			classDto = CotaQueNaoRecebeClassificacaoDTO.class;
            fileName = "Cotas_que_não_recebem_classificação";
		}
        FileExporter.to(fileName, fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, listaDto,
				classDto, this.httpResponse);
		
		result.nothing();
	}
	
	@Post
	public void autoCompletarPorNomeCotaQueRecebeClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro){
		List<CotaQueRecebeClassificacaoDTO> listaCotaQueRecebeClassificacaoDTO = this.classificacaoNaoRecebidaService.obterCotasQueRecebemClassificacao(filtro);
		
		List<ItemAutoComplete> listaCotaQueNaoRecebeClassificacaoDTOAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotaQueRecebeClassificacaoDTO != null && !listaCotaQueRecebeClassificacaoDTO.isEmpty()) {
			
			for (CotaQueRecebeClassificacaoDTO cotaQueRecebeClassificacaoDTO : listaCotaQueRecebeClassificacaoDTO) {
				listaCotaQueNaoRecebeClassificacaoDTOAutoComplete.add(new ItemAutoComplete(cotaQueRecebeClassificacaoDTO.getNomePessoa(), null, cotaQueRecebeClassificacaoDTO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotaQueNaoRecebeClassificacaoDTOAutoComplete, "result").include("value", "chave").serialize();
	}
	
	private void validarEntradaFiltroCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
            throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");
	}
	
	private void validarEntradaFiltroClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		if(filtro.getIdTipoClassificacaoProduto() == null || filtro.getIdTipoClassificacaoProduto() == 0)
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma Classificação foi selecionada.");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void guardarFiltroNaSession(FiltroClassificacaoNaoRecebidaDTO filtro) {
		
		FiltroClassificacaoNaoRecebidaDTO filtroSession = (FiltroClassificacaoNaoRecebidaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private void carregarComboClassificacao() {

		List<TipoClassificacaoProduto> listaTipoClassificacaoProduto = tipoClassificacaoProdutoService.obterTodos();

		List<ItemDTO<Long, String>> listaTipoClassificacaoProdutoCombo = new ArrayList<ItemDTO<Long, String>>();

		for (TipoClassificacaoProduto tipoClassificacaoProduto : listaTipoClassificacaoProduto) {

            // Preenchendo a lista que irá representar o combobox de área de
            // influência na view
			listaTipoClassificacaoProdutoCombo.add(new ItemDTO<Long, String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}

		result.include("listaTipoClassificacao", listaTipoClassificacaoProdutoCombo);
	}
	
}
