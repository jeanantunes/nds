package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.service.TipoSegmentoProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/segmentoNaoRecebido")
@Resource
@Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO)
public class SegmentoNaoRecebidoController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroSegmentoNaoRecebido";
	private static final String COTAS_NAO_RECEBEM_SEGMENTO = "cotas_nao_recebem_segmento";
	private static final String SEGMENTOS_NAO_RECEBEM_COTA = "segmentos_nao_recebem_cota";
	private static final String PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO = "pesquisar_cotas_nao_estao_no_segmento";
	
	@Autowired
	private Result result;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Autowired
	private TipoSegmentoProdutoService tipoSegmentoProdutoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
    @Path("/")
	public void index(){
		// POPULANDO FILTROS
		List<TipoSegmentoProduto> listaTipoSegmentoProduto = tipoSegmentoProdutoService.obterTipoSegmentoProdutoOrdenados(Ordenacao.ASC);
		this.carregarComboSegmento(listaTipoSegmentoProduto, "listaTipoSegmentoProduto");
		
		session.removeAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO);
	}
	
	@Get
	@Path("/chamarTelaExcecaoSegmentoParcias")
	public void chamarTelaExcecaoSegmentoParcias(){
		result.forwardTo(ExcecaoSegmentoParciaisController.class).index();
	}

	@Post("/pesquisarCotasNaoRecebemSegmento")
	public void pesquisarCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, String sortname, int page, int rp, boolean isReload){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		validarEntradaFiltroSegmento(filtro);
		
		tratarFiltro(filtro);
		
		List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO = segmentoNaoRecebidoService.obterCotasNaoRecebemSegmento(filtro);
		
		if (!isReload) {
			if (listaCotaNaoRecebeSegmentoDTO == null || listaCotaNaoRecebeSegmentoDTO.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
		}
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = montarTableModelCotasNaoRecebemSegmento(filtro, listaCotaNaoRecebeSegmentoDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> montarTableModelCotasNaoRecebemSegmento(
			FiltroSegmentoNaoRecebidoDTO filtro, 
			List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO) {
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaNaoRecebeSegmentoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	@Post("/pesquisarSegmentosCadastradosNaCota")
	public void pesquisarSegmentosCadastradosNaCota(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, String sortname, int page, int rp, boolean isReload){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		}
		
		List<SegmentoNaoRecebeCotaDTO> listaSegmentoNaoRecebeCotaDTO = segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosNaCota(filtro);
		
		tratarFiltro(filtro);
		
		if (!isReload) {
			if (listaSegmentoNaoRecebeCotaDTO == null || listaSegmentoNaoRecebeCotaDTO.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
		}
		
		TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> tableModel = montarTableModelSegmentosNaoRecebemCota(filtro, listaSegmentoNaoRecebeCotaDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> montarTableModelSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro, List<SegmentoNaoRecebeCotaDTO> listaSegmentoNaoRecebeCotaDTO) {
		
		TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> tableModel = new TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaSegmentoNaoRecebeCotaDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	@Post("/excluirSegmentoNaoRecebido")
    @Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO)
	public void excluirSegmentoNaoRecebido(Long segmentoNaoRecebidoId){
		
		segmentoNaoRecebidoService.excluirSegmentoNaoRecebido(segmentoNaoRecebidoId);
		
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Get("/limparPesquisarCotasNaoEstaoNoSegmento")
	public void limparPesquisarCotasNaoEstaoNoSegmento() {
		session.removeAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO);
		result.nothing();
	}
	
	@SuppressWarnings("unchecked")
	@Post("/pesquisarCotasNaoEstaoNoSegmento")
	public void pesquisarCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, int page, int rp, boolean isReload) {
		
	    if (!isReload || filtro.getNomeCota() != null || filtro.getNumeroCota() != null || filtro.getTipoSegmentoProdutoId() != null) {
		this.validarEntradaFiltroSegmento(filtro);
		this.validarEntradaFiltroCota(filtro);
		
		filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
		
		List<CotaDTO> listaCotaDTO = new ArrayList<>();
		listaCotaDTO.addAll(segmentoNaoRecebidoService.obterCotasNaoEstaoNoSegmento(filtro));
		
		if (filtro.getNumeroCota() != null) {
			List<CotaDTO> sessionListaCotaDTO = (List<CotaDTO>) session.getAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO);
			if (sessionListaCotaDTO != null) {
				listaCotaDTO.addAll(sessionListaCotaDTO);
			}
			session.setAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO, listaCotaDTO);
		} else {
			session.removeAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO);
		}
		
		Collections.sort(listaCotaDTO, new Comparator<CotaDTO>() {
			@Override
			public int compare(CotaDTO o1, CotaDTO o2) {
				return o1.getNumeroCota().compareTo(o2.getNumeroCota());
			}
		});
		
		if (!isReload) {
			if (listaCotaDTO == null || listaCotaDTO.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
		}
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = montarTableModelCotasParaInclusaoSegmento(filtro, listaCotaDTO);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	    } else {
		result.nothing();
	    }
	}
	
	@Post("/pesquisarSegmentosElegiveisParaInclusao")
	public void pesquisarSegmentosElegiveisParaInclusao(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, int page, int rp, boolean isReload){
		
        // fazer validação do numero e nome da cota;
		this.validarEntradaFiltroCota(filtro);

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
		
		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		}
		
		List<TipoSegmentoProduto> listaTipoSegmentoProduto = segmentoNaoRecebidoService.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
		
		if(!isReload){
			if (listaTipoSegmentoProduto == null || listaTipoSegmentoProduto.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
		}
		
		TableModel<CellModelKeyValue<TipoSegmentoProduto>> tableModel = montarTableModelSegmentosParaInclusaoCota(filtro, listaTipoSegmentoProduto);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post("/incluirCotasSegmentoNaoRecebido")
    @Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO)
	public void incluirCotasSegmentoNaoRecebido(Long[] idCotas, Long idTipoSegmento){
	
        // Valida o filtro (Seleção de cotas)
		if (idCotas.length == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota foi selecionada, por favor selecione uma cota."); 
		}
		
		List<SegmentoNaoRecebido> listaSegmentoNaoRecebido = new ArrayList<SegmentoNaoRecebido>();
		
		for (Long idCota : idCotas) {
			SegmentoNaoRecebido segmentoNaoRecebido = new SegmentoNaoRecebido();
			segmentoNaoRecebido.setCota(cotaService.obterPorId(idCota));
			segmentoNaoRecebido.setTipoSegmentoProduto(tipoSegmentoProdutoService.obterTipoProdutoSegmentoPorId(idTipoSegmento));
			segmentoNaoRecebido.setUsuario(usuarioService.getUsuarioLogado());
			segmentoNaoRecebido.setDataAlteracao(new Date());
			
			listaSegmentoNaoRecebido.add(segmentoNaoRecebido);
		}
		
		segmentoNaoRecebidoService.inserirCotasSegmentoNaoRecebido(listaSegmentoNaoRecebido);
		
		session.removeAttribute(PESQUISAR_COTAS_NAO_ESTAO_NO_SEGMENTO);
		
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
		
	}
	
	@Post("/incluirSegmentosNaCota")
    @Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO)
	public void incluirSegmentosNaCota(Integer numeroCota, String nomeCota, Long[] idTipoSegmentos){
	
        // Criando o filtro a partir dos parametros passados na requisição
		FiltroSegmentoNaoRecebidoDTO filtro = new FiltroSegmentoNaoRecebidoDTO();
		filtro.setNomeCota(nomeCota);
		filtro.setNumeroCota(numeroCota);
		
		this.validarEntradaFiltroCota(filtro);
		
		if (idTipoSegmentos.length == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma segmento foi selecionado, por favor selecione um segmento."); 
		}
		
		List<SegmentoNaoRecebido> listaSegmentoNaoRecebido = new ArrayList<SegmentoNaoRecebido>();
		
		for (Long idTipoSegmento : idTipoSegmentos) {
			SegmentoNaoRecebido segmentoNaoRecebido = new SegmentoNaoRecebido();
			
			if (numeroCota != null && numeroCota > 0) {
				segmentoNaoRecebido.setCota(cotaService.obterPorNumeroDaCota(numeroCota));	
			}else{
				segmentoNaoRecebido.setCota(cotaService.obterPorNome(PessoaUtil.removerSufixoDeTipo(nomeCota)).get(0));
			}
			
			segmentoNaoRecebido.setTipoSegmentoProduto(tipoSegmentoProdutoService.obterTipoProdutoSegmentoPorId(idTipoSegmento));
			segmentoNaoRecebido.setUsuario(usuarioService.getUsuarioLogado());
			segmentoNaoRecebido.setDataAlteracao(new Date());
			
			listaSegmentoNaoRecebido.add(segmentoNaoRecebido);
		}
		
		segmentoNaoRecebidoService.inserirCotasSegmentoNaoRecebido(listaSegmentoNaoRecebido);
		
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
		
	}
	
	        /**
     * utilizado para auto complete da tela onde o usuário utiliza o nome do
     * Segmento
     */
	@Post
	public void autoCompletarPorNome(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		List<TipoSegmentoProduto> listaTipoSegmentoProduto = segmentoNaoRecebidoService.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
		
		List<ItemAutoComplete> listaTipoSegmentoProdutoAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaTipoSegmentoProduto != null && !listaTipoSegmentoProduto.isEmpty()) {
			
			for (TipoSegmentoProduto tipoSegmentoProduto : listaTipoSegmentoProduto) {
				
				listaTipoSegmentoProdutoAutoComplete.add(new ItemAutoComplete(tipoSegmentoProduto.getDescricao(), null, tipoSegmentoProduto));
			}
		}
		
		this.result.use(Results.json()).from(listaTipoSegmentoProdutoAutoComplete, "result").include("value", "chave").serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaDTO>> montarTableModelCotasParaInclusaoSegmento(FiltroSegmentoNaoRecebidoDTO filtro, List<CotaDTO> listaCotaDTO) {
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(listaCotaDTO.size());
		
		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<TipoSegmentoProduto>> montarTableModelSegmentosParaInclusaoCota(FiltroSegmentoNaoRecebidoDTO filtro, List<TipoSegmentoProduto> listaTipoSegmentoProduto) {
		
		TableModel<CellModelKeyValue<TipoSegmentoProduto>> tableModel = new TableModel<CellModelKeyValue<TipoSegmentoProduto>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoSegmentoProduto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(listaTipoSegmentoProduto.size());
		
		return tableModel;
	}
	
	@Post("/carregarComboboxInclusaoDeSegmentoNaCota")
	public void carregarComboboxInclusaoDeSegmentoNaCota(FiltroSegmentoNaoRecebidoDTO filtro){
		
		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		}
		
		List<TipoSegmentoProduto> listaTipoSegmentoProduto = segmentoNaoRecebidoService.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
		
		result.use(Results.json()).from(listaTipoSegmentoProduto, "listaTipoSegmentoProduto").recursive().serialize();
		
	}
	
	@Get
	public void exportar(FileType fileType, String tipoExportacao) throws IOException {
		
		FiltroSegmentoNaoRecebidoDTO filtro = (FiltroSegmentoNaoRecebidoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (tipoExportacao.equals(COTAS_NAO_RECEBEM_SEGMENTO)) {
		
			List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO = this.segmentoNaoRecebidoService.obterCotasNaoRecebemSegmento(filtro);
			
			if(listaCotaNaoRecebeSegmentoDTO.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
			}
			
            FileExporter.to(COTAS_NAO_RECEBEM_SEGMENTO, fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
					listaCotaNaoRecebeSegmentoDTO, CotaNaoRecebeSegmentoDTO.class, this.httpResponse);
			
			result.nothing();
			
		} else if(tipoExportacao.equals(SEGMENTOS_NAO_RECEBEM_COTA)) {
			
			List<SegmentoNaoRecebeCotaDTO> listaSegmentoNaoRecebeCotaDTO = this.segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosNaCota(filtro);
			
			if(listaSegmentoNaoRecebeCotaDTO.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
			}
			
            FileExporter.to(SEGMENTOS_NAO_RECEBEM_COTA, fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
					listaSegmentoNaoRecebeCotaDTO, SegmentoNaoRecebeCotaDTO.class, this.httpResponse);
			
			result.nothing();
			
		}else{
			List<TipoSegmentoProduto> listaTipoSegmentoProduto = this.segmentoNaoRecebidoService.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
			
			if(listaTipoSegmentoProduto.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
			}
			
            FileExporter.to("segmentos_elegiveis", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
					listaTipoSegmentoProduto, TipoSegmentoProduto.class, this.httpResponse);
			
			result.nothing();
		}
		
	}
	
	private void carregarComboSegmento(List<TipoSegmentoProduto> listaTipoSegmentoProduto, String key) {

		// Lista usada para popular o combobox
		List<ItemDTO<Long, String>> listaTipoSegmentoProdutoCombobox = new ArrayList<ItemDTO<Long, String>>();

		for (TipoSegmentoProduto tipoSegmentoProduto : listaTipoSegmentoProduto) {

            // Preenchendo a lista que irá representar o combobox
            // TipoSegmentoProduto
			listaTipoSegmentoProdutoCombobox.add(new ItemDTO<Long, String>(tipoSegmentoProduto.getId(), tipoSegmentoProduto.getDescricao()));
		}

		result.include(key, listaTipoSegmentoProdutoCombobox);
	}
	
	private void validarEntradaFiltroSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		if((filtro.getTipoSegmentoProdutoId() == null || filtro.getTipoSegmentoProdutoId() == 0))
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um segmento.");
	}
	
	private void tratarFiltro(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		FiltroSegmentoNaoRecebidoDTO filtroSession = (FiltroSegmentoNaoRecebidoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) 
			filtro.getPaginacao().setPaginaAtual(1);
		
		// else if(filtroSession != null) 
		// filtro.getPaginacao().setQtdResultadosTotal(filtroSession.getPaginacao().getQtdResultadosTotal());
		
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private void validarEntradaFiltroCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		if((filtro.getNumeroCota() == null || filtro.getNumeroCota() == 0) && (filtro.getNomeCota() == null || filtro.getNomeCota().trim().isEmpty()))
            throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");
	}

}

