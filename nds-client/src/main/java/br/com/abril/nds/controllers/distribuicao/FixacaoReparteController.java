package br.com.abril.nds.controllers.distribuicao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FixacaoReparteCotaDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.FixacaoReparteHistoricoDTO;
import br.com.abril.nds.dto.FixacaoReparteProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.QuantidadePdvCotaDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/fixacaoReparte")
public class FixacaoReparteController extends BaseController {
	
    private static final String OPERACAO_REALIZADA_COM_SUCESSO = "Operação realizada com sucesso!";
	private static final ValidacaoVO SUCCESS_MSG = new ValidacaoVO(TipoMensagem.SUCCESS, OPERACAO_REALIZADA_COM_SUCESSO);
	private static final String FILTRO_PRODUTO_SESSION_ATTRIBUTE = "filtroPorProduto";
	private static final String FILTRO_COTA_SESSION_ATTRIBUTE = "filtroPorCota";
	private static final int MAX_EDICOES =6;
	private List<String> errosUpload=new ArrayList<String>();
	
	@Autowired
	private ClassificacaoNaoRecebidaService classificacaoNaoRecebidaService;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Autowired
	private ExcecaoSegmentoParciaisService excecaoSegmentoParciaisService;
	

    @Autowired
	private Result result;
	
	@Autowired
	TipoProdutoService tipoProdutoService;
	
	@Autowired
	RepartePdvService repartePdvService;
	
	@Autowired
	ProdutoService produtoService;
	
	@Autowired
	FixacaoReparteService fixacaoReparteService;
	
	@Autowired
	CotaService cotaService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;
	
    @Autowired
	private HttpServletResponse httpResponse;

	@Rules(Permissao.ROLE_DISTRIBUICAO_FIXACAO_REPARTE)
	@Path("/")
	public void fixacaoReparte(){
		result.include("classificacao",fixacaoReparteService.obterClassificacoesProduto());
		
	}


	@Post
	@Path("/pesquisarPorProduto")
	public void pesquisarPorProduto(FiltroConsultaFixacaoProdutoDTO filtro , String sortorder, String sortname, int page, int rp){
		
		if(session.getAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE, filtro);
		}
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		tratarFiltroPorProduto(filtro);

		this.setingIdProduto_produto(filtro);
		
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorProduto(filtro);
		
		if(resultadoPesquisa.isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados resultados para a pesquisa");
		}
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModelProduto = montarTableModelProduto(filtro, resultadoPesquisa);
		
		result.use(Results.json()).withoutRoot().from(tableModelProduto).recursive().serialize();
	}
	
	@Post
	@Path("/pesquisarPorCota")
	public void pesquisarPorCota(FiltroConsultaFixacaoCotaDTO filtro , String sortorder, String sortname, int page, int rp){
		if(session.getAttribute(FILTRO_COTA_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_COTA_SESSION_ATTRIBUTE, filtro);
		}
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		tratarFiltroPorCota(filtro);
		//remove tipo cota adicionado no autocomplete	
		filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorCota(filtro);
		
		if(resultadoPesquisa.isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados resultados para a pesquisa.");
		}
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModelCota = montarTableModelCota(filtro, resultadoPesquisa);
		
		result.use(Results.json()).withoutRoot().from(tableModelCota).recursive().serialize();
	}
	
	@Post
	@Path("/validarTipoCota")
	public void validarTipoCota (String numeroCota){
		
		Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(numeroCota));
		
		if(cota.getTipoDistribuicaoCota() != null && cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Não é possivel fixar reparte para cota [ " + numeroCota
                + " ] tipo[" + TipoDistribuicaoCota.ALTERNATIVO.toString() + "].");
	}else{
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
}


	private TableModel<CellModelKeyValue<FixacaoReparteDTO>> montarTableModelProduto(FiltroConsultaFixacaoProdutoDTO filtro, List<FixacaoReparteDTO> resultadoPesquisa) {
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
		
	private TableModel<CellModelKeyValue<FixacaoReparteDTO>> montarTableModelCota(FiltroConsultaFixacaoCotaDTO filtro, List<FixacaoReparteDTO> resultadoPesquisa) {
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	@Post
	@Path("/carregarGridHistoricoProduto")
	public void carregarGridHistoricoProduto(FiltroConsultaFixacaoProdutoDTO filtro , String sortorder, String sortname, int page, int rp){
		if(session.getAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE, filtro);
			this.session.removeAttribute(FILTRO_COTA_SESSION_ATTRIBUTE);
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		tratarFiltroPorProduto(filtro);
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterHistoricoLancamentoPorProduto(filtro);
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(resultadoPesquisa.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/carregarGridHistoricoCota")
	public void carregarGridHistoricoCota(FiltroConsultaFixacaoCotaDTO filtro ,String codigoProduto, String sortorder, String sortname, int page, int rp){
		if(session.getAttribute(FILTRO_COTA_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_COTA_SESSION_ATTRIBUTE, filtro);
			this.session.removeAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE);
			
		}
        Produto produto = produtoService.obterProdutoPorCodigo(filtro.getCodigoProduto());
        filtro.setCodigoProduto(produto.getCodigoICD());

        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterHistoricoLancamentoPorCota(filtro);

		if(resultadoPesquisa.size()>0){
			FixacaoReparteDTO fixacaoReparteDTO = resultadoPesquisa.get(0);
			this.result.include("ultimaEdicao",fixacaoReparteDTO);
		}

		for (FixacaoReparteDTO fix : resultadoPesquisa) {
			fix.setCodigoProduto(produto.getCodigoICD());
		}
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(resultadoPesquisa.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/adicionarFixacaoReparte")
	public void adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO){
		
		String fixacaoInvalida = validaFixacao(fixacaoReparteDTO);
		if (fixacaoInvalida != null) {
			throw new ValidacaoException(TipoMensagem.WARNING, fixacaoInvalida);
		}
		
		FixacaoReparte fixacaoReparte = fixacaoReparteService.adicionarFixacaoReparte(fixacaoReparteDTO);
		
		if(fixacaoReparte.getCotaFixada().getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
			throw new ValidacaoException(TipoMensagem.WARNING, OPERACAO_REALIZADA_COM_SUCESSO +
					"<br>Status da Cota: " + SituacaoCadastro.SUSPENSO.toString());
		}
		
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	
	@Post
	@Path("/removerFixacaoReparte")
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO){
		fixacaoReparteService.removerFixacaoReparte(fixacaoReparteDTO);
		
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarGridPdv")
	public void carregarGridPdv(FiltroConsultaFixacaoProdutoDTO filtro , String sortorder, String sortname, int page, int rp){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<PdvDTO> listaPDVDTO = fixacaoReparteService.obterListaPdvPorFixacao(filtro.getIdFixacao());

 		TableModel<CellModelKeyValue<PdvDTO>> tableModel = new TableModel<CellModelKeyValue<PdvDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPDVDTO));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(listaPDVDTO.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/editarFixacao")
	public void editarFixacaoReparte(FiltroConsultaFixacaoProdutoDTO filtro , String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		FixacaoReparteDTO fixacaoReparteDTO = fixacaoReparteService.obterFixacaoDTO(filtro.getIdFixacao());
		result.use(Results.json()).withoutRoot().from(fixacaoReparteDTO).recursive().serialize();
	}
	
	@Post
	@Path("/salvarGridPdvReparte")
	public void salvarGridPdvReparte(List<RepartePDVDTO> listPDV, String codProduto, String codCota, Long idFixacao, boolean manterFixa){
		repartePdvService.salvarRepartesPDV(listPDV,codProduto, idFixacao, manterFixa);
		
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	@Post
	@Path("/verificaCotaPossuiPdvs")
	public void verificaCotaPossuiPdvs(Long idFixacao){
		QuantidadePdvCotaDTO quantidadePdvCotaDTO  = new QuantidadePdvCotaDTO();
		quantidadePdvCotaDTO.setCotaPossuiVariosPdvs(fixacaoReparteService.isCotaPossuiVariosPdvs(idFixacao));
		result.use(Results.json()).withoutRoot().from(quantidadePdvCotaDTO).recursive().serialize();
	}
	
	//FIXME refatorar
	@SuppressWarnings({ "unchecked" })
	@Get
	public <T> void exportar(FileType fileType, String tipoExportacao) throws IOException {
		List<T> dto = new ArrayList<>();
		Class<T> clazz = null;
		FiltroConsultaFixacaoCotaDTO filtroPorCota = null;
		FiltroConsultaFixacaoProdutoDTO filtroPorProduto = null;
		List<FixacaoReparteDTO> resultadoPesquisa = null;
		
		filtroPorCota = (FiltroConsultaFixacaoCotaDTO) session.getAttribute(FILTRO_COTA_SESSION_ATTRIBUTE);
		filtroPorProduto = (FiltroConsultaFixacaoProdutoDTO) session.getAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE);
		
		if (tipoExportacao.equals("historicoCota")) {
			clazz = (Class<T>) FixacaoReparteHistoricoDTO.class;
				resultadoPesquisa = fixacaoReparteService.obterHistoricoLancamentoPorProduto(filtroPorProduto);
			if(resultadoPesquisa!=null ){	
				for (FixacaoReparteDTO fixacaoReparteDTO : resultadoPesquisa) {
					FixacaoReparteHistoricoDTO fixacaoReparteHistoricoDTO = new FixacaoReparteHistoricoDTO();
					fixacaoReparteHistoricoDTO.setEdicaoString(fixacaoReparteDTO.getEdicaoString());
					fixacaoReparteHistoricoDTO.setReparteString(fixacaoReparteDTO.getReparteString());
					fixacaoReparteHistoricoDTO.setVenda(fixacaoReparteDTO.getVenda());
					fixacaoReparteHistoricoDTO.setDataLancamentoString(fixacaoReparteDTO.getDataLancamentoString());
					fixacaoReparteHistoricoDTO.setDataRecolhimentoString(fixacaoReparteDTO.getDataRecolhimentoString());
					fixacaoReparteHistoricoDTO.setStatus(fixacaoReparteDTO.getStatus());
					dto.add((T) fixacaoReparteHistoricoDTO);
				}
			}
		}else if (tipoExportacao.equals("historicoProduto")) {
			clazz = (Class<T>) FixacaoReparteHistoricoDTO.class;
				resultadoPesquisa = fixacaoReparteService.obterHistoricoLancamentoPorCota(filtroPorCota);
				if(resultadoPesquisa!=null ){	
				for (FixacaoReparteDTO fixacaoReparteDTO : resultadoPesquisa) {
					FixacaoReparteHistoricoDTO fixacaoReparteHistoricoDTO = new FixacaoReparteHistoricoDTO();
					fixacaoReparteHistoricoDTO.setEdicaoString(fixacaoReparteDTO.getEdicaoString());
					fixacaoReparteHistoricoDTO.setReparteString(fixacaoReparteDTO.getReparteString());
					fixacaoReparteHistoricoDTO.setVenda(fixacaoReparteDTO.getVenda());
					fixacaoReparteHistoricoDTO.setDataLancamentoString(fixacaoReparteDTO.getDataLancamentoString());
					fixacaoReparteHistoricoDTO.setDataRecolhimentoString(fixacaoReparteDTO.getDataRecolhimentoString());
					fixacaoReparteHistoricoDTO.setStatus(fixacaoReparteDTO.getStatus());
					dto.add((T) fixacaoReparteHistoricoDTO);
					}
				}
		} else if (tipoExportacao.equals("cota")) {
			clazz = (Class<T>) FixacaoReparteCotaDTO.class;
			resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorCota(filtroPorCota);
			
			for (FixacaoReparteDTO fixacaoReparteDTO : resultadoPesquisa) {
				FixacaoReparteCotaDTO fixacaoReparteCotaDTO = new FixacaoReparteCotaDTO();
				fixacaoReparteCotaDTO.setProdutoFixado(fixacaoReparteDTO.getProdutoFixado());
				fixacaoReparteCotaDTO.setNomeProduto(fixacaoReparteDTO.getNomeProduto());
				fixacaoReparteCotaDTO.setClassificacaoProduto(fixacaoReparteDTO.getClassificacaoProduto());
				fixacaoReparteCotaDTO.setEdicaoInicial(fixacaoReparteDTO.getEdicaoInicial());
				fixacaoReparteCotaDTO.setEdicaoFinal(fixacaoReparteDTO.getEdicaoFinal());
				fixacaoReparteCotaDTO.setEdicoesAtendidas(fixacaoReparteDTO.getEdicoesAtendidas());
				fixacaoReparteCotaDTO.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
				fixacaoReparteCotaDTO.setQtdeExemplares(fixacaoReparteDTO.getQtdeExemplares());
				fixacaoReparteCotaDTO.setUsuario(fixacaoReparteDTO.getUsuario());
				fixacaoReparteCotaDTO.setData(fixacaoReparteDTO.getData());
				fixacaoReparteCotaDTO.setHora(fixacaoReparteDTO.getHora());
				
				dto.add((T) fixacaoReparteCotaDTO);
			}
		}else if (tipoExportacao.equals("produto")) {
			clazz = (Class<T>) FixacaoReparteProdutoDTO.class;
			resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorProduto(filtroPorProduto);
			
			for (FixacaoReparteDTO fixacaoReparteDTO : resultadoPesquisa) {
				FixacaoReparteProdutoDTO fixacaoReparteProdutoDTO = new FixacaoReparteProdutoDTO();
				fixacaoReparteProdutoDTO.setCotaFixada(fixacaoReparteDTO.getCotaFixada());
				fixacaoReparteProdutoDTO.setNomeCota(fixacaoReparteDTO.getNomeCota());
				fixacaoReparteProdutoDTO.setEdicaoInicial(fixacaoReparteDTO.getEdicaoInicial());
				fixacaoReparteProdutoDTO.setEdicaoFinal(fixacaoReparteDTO.getEdicaoFinal());
				fixacaoReparteProdutoDTO.setEdicoesAtendidas(fixacaoReparteDTO.getEdicoesAtendidas());
				fixacaoReparteProdutoDTO.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
				fixacaoReparteProdutoDTO.setQtdeExemplares(fixacaoReparteDTO.getQtdeExemplares());
				fixacaoReparteProdutoDTO.setUsuario(fixacaoReparteDTO.getUsuario());
				fixacaoReparteProdutoDTO.setData(fixacaoReparteDTO.getData());
				fixacaoReparteProdutoDTO.setHora(fixacaoReparteDTO.getHora());
				
				dto.add((T) fixacaoReparteProdutoDTO);
			}
		} 
			
		if(resultadoPesquisa!=null && !resultadoPesquisa.isEmpty()) {
			FileExporter.to("fixacao_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, dto, clazz, this.httpResponse);
		}
		result.nothing();
	}
	

	private void tratarFiltroPorCota(FiltroConsultaFixacaoCotaDTO filtroAtual) {

		FiltroConsultaFixacaoCotaDTO filtroSession = (FiltroConsultaFixacaoCotaDTO) session
				.getAttribute(FILTRO_COTA_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		
		session.setAttribute(FILTRO_COTA_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void tratarFiltroPorProduto(FiltroConsultaFixacaoProdutoDTO filtroAtual) {

		FiltroConsultaFixacaoProdutoDTO filtroSession = (FiltroConsultaFixacaoProdutoDTO) session
				.getAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private boolean isStatusEdicaoValido(FixacaoReparteDTO fixacaoReparteDTO) {
		List<String>edicoesInvalidas =new ArrayList<String>();
        edicoesInvalidas.add("lançada");
		edicoesInvalidas.add("gerada");
		edicoesInvalidas.add("liberada");
		edicoesInvalidas.add("liberada");
		boolean statusOK =true;
		for(String edicao:edicoesInvalidas){
			if(edicao.equalsIgnoreCase(fixacaoReparteDTO.getStatus())){
				return false;
			};
		}
		
		return statusOK ;
	}
	
	@Post
	@Path("/uploadArquivoLoteFixacao")
	public void uploadArquivoEmLote(UploadedFile excelFileFixacao) throws FileNotFoundException, IOException{
		List<FixacaoReparteDTO> listaRegistrosInvalidosExcel=null;
		List<FixacaoReparteDTO> listaFixacaoExcel = XlsUploaderUtils.getBeanListFromXls(FixacaoReparteDTO.class, excelFileFixacao);

		if (!isListaVazia(listaFixacaoExcel)) {
			
			listaRegistrosInvalidosExcel = obterListaInvalidos(listaFixacaoExcel);
			listaFixacaoExcel.removeAll(listaRegistrosInvalidosExcel);
			
			for (FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel) {
				FixacaoReparte fixacaoReparte = fixacaoReparteService.adicionarFixacaoReparte(fixacaoReparteDTO);
				if(fixacaoReparte.getCotaFixada().getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
                    getErrosUpload().add(
                            "- Fixação inserida, status da Cota: " + SituacaoCadastro.SUSPENSO.toString() + ". (Cota["
                                + fixacaoReparteDTO.getCotaFixadaString() + "] Produto["
                                + fixacaoReparteDTO.getProdutoFixado() + "]).");
				}
			}
			
			if (listaRegistrosInvalidosExcel.isEmpty() && getErrosUpload().isEmpty()) {
				result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
			} else {
				result.use(Results.json()).from(
						new ValidacaoVO(TipoMensagem.WARNING, getMsgErroUpload()), 
						"result").recursive().serialize();
			}
			
		} else {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Arquivo está vazio."),"result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/gerarCopiaFixacao")
	public void gerarCopiaFixacao(CopiaMixFixacaoDTO copiaDTO){
		
		TipoMensagem tipoMsg = TipoMensagem.WARNING;
		List<String> msg = new ArrayList<String>();

		switch (copiaDTO.getTipoCopia()) {
		case COTA:
			Cota cotaOrigem = cotaService.obterPorNumeroDaCota(copiaDTO.getCotaNumeroOrigem());
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaDTO.getCotaNumeroDestino());

			if(cotaOrigem.getTipoDistribuicaoCota()==null || cotaDestino.getTipoDistribuicaoCota()==null
					|| !cotaOrigem.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL) 
					|| !cotaDestino.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL)){
                msg.add("Cotas não são do tipo CONVENCIONAL.");
			} 
			
			break;
		case PRODUTO:
			
			break;
		}
		
		if(msg.isEmpty()){
			try {
				boolean gerarCopiaMix = this.fixacaoReparteService.gerarCopiafixacao(copiaDTO);
				if (gerarCopiaMix) {
					tipoMsg = TipoMensagem.SUCCESS;
                    msg.add("Cópia executada com sucesso.");
				} else {
					tipoMsg = TipoMensagem.ERROR;
                    msg.add("Houve um erro ao gerar a cópia");
				}
			} 
			catch(ValidacaoException e) {
				tipoMsg = TipoMensagem.WARNING;
				msg=e.getValidacao().getListaMensagens();
			}
			catch(Exception e) {
				tipoMsg = TipoMensagem.ERROR;
                msg.add("Houve um erro ao gerar a cópia");
			}
			
		}
		
		result.use(Results.json()).from(new ValidacaoVO(tipoMsg, msg),"result").recursive().serialize();
		
	}

	private String getMsgErroUpload() {
		StringBuilder builderErros = new StringBuilder("");
        builderErros.append("Os registros a seguir não foram adicionados:<br>");
		for(String msg: getErrosUpload()){
			builderErros.append(msg).append("<br>");
		}
		return builderErros.toString();
	}

	private List<FixacaoReparteDTO> obterListaInvalidos (List<FixacaoReparteDTO> listaFixacaoExcel) {
		List<FixacaoReparteDTO>invalidos = new ArrayList<>();
		
		List<Integer> cotaIds = new ArrayList<>();
		List<String> codigoProdutos = new ArrayList<>();
		List<Integer> cotasExistentes = null;
		List<String> produtosExistentes = null;
		
		for (FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel) {
			
			if (isPreechimentoInvalido(fixacaoReparteDTO)) {
				invalidos.add(fixacaoReparteDTO);
                getErrosUpload().add(
                        "- Selecione Ed. Inicial / Ed. Final ou Qtde. de Edições (Cota["
                            + fixacaoReparteDTO.getCotaFixadaString() + "] Produto["
                            + fixacaoReparteDTO.getProdutoFixado() + "]).");
				continue;
			}
			
			if (isQuantidadeEdicoesMaiorQueSeis(fixacaoReparteDTO)) {
				invalidos.add(fixacaoReparteDTO);
                getErrosUpload().add(
                        "- O numero de edições fixadas não pode ser maior que 6 (Cota["
                            + fixacaoReparteDTO.getCotaFixadaString() + "] Produto["
                            + fixacaoReparteDTO.getProdutoFixado() + "]).");
				continue;
			}

            if (StringUtils.isBlank(fixacaoReparteDTO.getClassificacaoProduto())) {
                invalidos.add(fixacaoReparteDTO);
                getErrosUpload().add(
                        "- Classificação não pode estar vazia. [" + fixacaoReparteDTO.getProdutoFixado() + "]");
                continue;
            }

            if (fixacaoReparteService.isFixacaoExistente(fixacaoReparteDTO)) {
				invalidos.add(fixacaoReparteDTO);
				getErrosUpload().add("- Registro existente para o produto[" + fixacaoReparteDTO.getProdutoFixado() + "] e cota[" + fixacaoReparteDTO.getCotaFixadaString() + "].") ;
				continue;
			}
            
            if(fixacaoReparteDTO.getProdutoFixado() == null){
            	invalidos.add(fixacaoReparteDTO);
        		getErrosUpload().add("Há Produtos com Código ICD inválido, ajuste-os no Cadastro de Produto.");
        		continue;
            }
            
            
            if(fixacaoReparteDTO.getProdutoFixado().length() >= 8){ 
            	Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());
            	
            	if(!produtoService.isIcdValido(produto.getCodigo())){
            		invalidos.add(fixacaoReparteDTO);
            		getErrosUpload().add("Produto ["+produto.getNomeComercial()+"]: Código ICD inválido, ajuste-o no Cadastro de Produto.");
            		continue;
            	}
            }

			cotaIds.add(fixacaoReparteDTO.getCotaFixada());
			codigoProdutos.add(fixacaoReparteDTO.getProdutoFixado());
		}
		
		if (!cotaIds.isEmpty()) {
			cotasExistentes = cotaService.numeroCotaExiste(TipoDistribuicaoCota.CONVENCIONAL, cotaIds.toArray(new Integer[0]));
		}
		if (!codigoProdutos.isEmpty()) {
			produtosExistentes = produtoService.verificarProdutoExiste(codigoProdutos.toArray(new String[0]));
		}
			
		listaFixacaoExcel.removeAll(invalidos);
		for (FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel) {
			
			if (cotasExistentes != null && !cotasExistentes.contains(fixacaoReparteDTO.getCotaFixada())) {
				invalidos.add(fixacaoReparteDTO);
                getErrosUpload().add(
                        "- Cota[" + fixacaoReparteDTO.getCotaFixada().toString()
                            + "] não encontrada, inativa ou alternativa.");
			} else if (produtosExistentes != null && !produtosExistentes.contains(fixacaoReparteDTO.getProdutoFixado())) {
				invalidos.add(fixacaoReparteDTO);
                getErrosUpload().add("- Produto[" + fixacaoReparteDTO.getProdutoFixado() + "] não encontrado.");
			}
		}
		
		return invalidos;
	}

	private boolean isQuantidadeEdicoesMaiorQueSeis(FixacaoReparteDTO fixacaoReparteDTO) {	
		if (fixacaoReparteDTO.getQtdeEdicoes() != null) {
			return fixacaoReparteDTO.getQtdeEdicoes() > MAX_EDICOES;
		}
		return fixacaoReparteDTO.getEdicaoInicial() == null || fixacaoReparteDTO.getEdicaoFinal() == null
                || ((fixacaoReparteDTO.getEdicaoFinal() - fixacaoReparteDTO.getEdicaoInicial()) > MAX_EDICOES);
	}


	private String validaFixacao(FixacaoReparteDTO fixacaoReparteDTO) {
		
		if(!isCotaValida(fixacaoReparteDTO)) {
            return "Informe uma cota ativa/suspensa e convencional para realizar fixação!";
		} else if(!isStatusEdicaoValido(fixacaoReparteDTO)) {
            return "O status da edição não permite fixação!";
		} else {
			if(fixacaoReparteDTO.isQtdeEdicoesMarcado()) {
				if(fixacaoReparteDTO.getQtdeEdicoes() != null ){
					if(fixacaoReparteDTO.getQtdeEdicoes() > MAX_EDICOES) {
                        return "O numero de edições fixadas não pode ser maior que 6";
					} else if(fixacaoReparteDTO.getQtdeExemplares() == null || fixacaoReparteDTO.getQtdeExemplares() == 0) {
                        return "Quantidade de exemplares não pode ser vazia ou 0.";
					}
				}
			} else {
				if(fixacaoReparteDTO.getEdicaoInicial() == null || fixacaoReparteDTO.getEdicaoInicial() == 0) {
                    return "Edição inicial não pode ser vazia ou 0.";
				} else if( fixacaoReparteDTO.getEdicaoFinal() == null ||  fixacaoReparteDTO.getEdicaoFinal() == 0){
                    return "Edição final não pode ser vazia ou 0.";
				} else if( fixacaoReparteDTO.getEdicaoFinal() < fixacaoReparteDTO.getEdicaoInicial()) {
                    return "Edição final não pode ser inferior a inicial.";
				} else if(fixacaoReparteDTO.getEdicaoFinal() - fixacaoReparteDTO.getEdicaoInicial() > MAX_EDICOES) {
                    return "O intervalo não deve ultrapassar 6 edições!";
				} else if(fixacaoReparteDTO.getQtdeExemplares() == null || fixacaoReparteDTO.getQtdeExemplares() == 0) {
                    return "Quantidade de exemplares não pode ser vazia ou 0.";
				}
			}
			
			//tratamento para restrigir Classificacao nao recebida
			Cota cota = this.cotaService.obterPorNumeroDaCota(fixacaoReparteDTO.getCotaFixada());
			
			List<ClassificacaoNaoRecebidaDTO> classificacoesNaoRecebidasPelaCotaList = this.classificacaoNaoRecebidaService.obterClassificacoesNaoRecebidasPelaCota(cota);
			
			if(classificacoesNaoRecebidasPelaCotaList!=null){
				for (ClassificacaoNaoRecebidaDTO classificacaoNaoRecebidaDTO : classificacoesNaoRecebidasPelaCotaList) {
					if(classificacaoNaoRecebidaDTO.getNomeClassificacao().equals(fixacaoReparteDTO.getClassificacaoProduto()))
                        return "Cota de número " + cota.getNumeroCota() + " não recebe classificação do tipo "
                            + classificacaoNaoRecebidaDTO.getNomeClassificacao();
				}
			}
			
			if(fixacaoReparteDTO.getProdutoFixado().length() >= 8){
				Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());
				
				if(!produtoService.isIcdValido(produto.getCodigo())){
					return "Produto ["+produto.getNomeComercial()+"]: Código ICD inválido, ajuste no Cadastro de Produto.";
				}
			}
			
			
			//tratamento para segmento
			List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota = segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosNaCota(cota);
			
			FiltroExcecaoSegmentoParciaisDTO filtroExcecaoSeg = new FiltroExcecaoSegmentoParciaisDTO() ;
			CotaDTO cotadto = new CotaDTO();
			cotadto.setNumeroCota(cota.getNumeroCota());
			filtroExcecaoSeg.setExcecaoSegmento(true);
			filtroExcecaoSeg.setCotaDto(cotadto);
			 
			List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCotaList = this.excecaoSegmentoParciaisService.obterProdutosRecebidosPelaCota(filtroExcecaoSeg);
			
			Produto prd = this.produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());
			TipoSegmentoProduto tipoSegProd = prd.getTipoSegmentoProduto();
            if (tipoSegProd == null) {
                return "Produto [" + prd.getNome() + "] não possui Tipo Segmento cadastrado.";
            }

            loopSeg:for (SegmentoNaoRecebeCotaDTO seg : obterSegmentosNaoRecebidosCadastradosNaCota) {
				if(seg.getNomeSegmento().equals(tipoSegProd.getDescricao())){
					
					for (ProdutoRecebidoDTO prodRecebidoDTO : obterProdutosRecebidosPelaCotaList) {
						if(prodRecebidoDTO.getCodigoProduto().equals(fixacaoReparteDTO.getProdutoFixado()))
							continue loopSeg;
					}
					
                    return "Cota [" + cota.getNumeroCota() + "] não recebe segmento " + tipoSegProd.getDescricao()
                        + " do produto " + fixacaoReparteDTO.getProdutoFixado();

				}
				
			}
			
			
		}
		
		return null;
	}

	private boolean isPreechimentoInvalido(FixacaoReparteDTO fixacaoReparteDTO) {
        boolean edIniOk = (fixacaoReparteDTO.getEdicaoInicial() != null && fixacaoReparteDTO.getEdicaoInicial() > 0);
        boolean edFinalOk = (fixacaoReparteDTO.getEdicaoFinal() != null && fixacaoReparteDTO.getEdicaoFinal() > 0 && fixacaoReparteDTO.getEdicaoFinal() > fixacaoReparteDTO.getEdicaoInicial());
        boolean qtdeEdicoesOk = (fixacaoReparteDTO.getQtdeEdicoes() != null && fixacaoReparteDTO.getQtdeEdicoes() > 0);
        return !((edIniOk && edFinalOk) || qtdeEdicoesOk);
    }

    private boolean isCotaValida(FixacaoReparteDTO fixacaoReparteDTO) {
		return fixacaoReparteService.isCotaValida(fixacaoReparteDTO) ;
	}

	private boolean isListaVazia(List<FixacaoReparteDTO> listaFixacaoExcel) {
		return (listaFixacaoExcel == null || listaFixacaoExcel.isEmpty());
	}

	public List<String> getErrosUpload() {
		return errosUpload;
	}

	public void setErrosUpload(List<String> errosUpload) {
		this.errosUpload = errosUpload;
	}
	
	private void setingIdProduto_produto(FiltroConsultaFixacaoProdutoDTO filtro) {
		Produto produto = produtoService.obterProdutoPorCodigo(filtro.getCodigoProduto());
		filtro.setIdProduto(produto.getId());
	}
	
}
