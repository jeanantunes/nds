package br.com.abril.nds.controllers.distribuicao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.FixacaoReparteCotaDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.FixacaoReparteHistoricoDTO;
import br.com.abril.nds.dto.FixacaoReparteProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.QuantidadePdvCotaDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
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
	
	private static final String FILTRO_PRODUTO_SESSION_ATTRIBUTE = "filtroPorProduto";
	private static final String FILTRO_COTA_SESSION_ATTRIBUTE = "filtroPorCota";
	private static final int MAX_EDICOES =6;
	private static final String IMPORT_INCONSISTENTE="importInconsistente";
	private List<String> errosUpload=new ArrayList<String>();
	
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
	
	FiltroConsultaFixacaoCotaDTO filtroPorCota;
	
	FiltroConsultaFixacaoProdutoDTO filtroPorProduto;
	
	FixacaoReparteDTO fixacaoReparteDTO;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	
	

	@Rules(Permissao.ROLE_DISTRIBUICAO_FIXACAO_REPARTE)
	@Path("/")
	public void index(){
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
		
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorProduto(filtro);
		
		if(resultadoPesquisa.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não Foram encontrados resultados para a pesquisa");	
		}
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModelProduto = montarTableModelProduto(filtro);
		
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Não Foram encontrados resultados para a pesquisa");	
		}
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModelCota = montarTableModelCota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModelCota).recursive().serialize();
	}
	
	
	
	private TableModel<CellModelKeyValue<FixacaoReparteDTO>> montarTableModelProduto(FiltroConsultaFixacaoProdutoDTO filtro) {
		
		List<FixacaoReparteDTO> resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorProduto(filtro);
		
		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
		
	private TableModel<CellModelKeyValue<FixacaoReparteDTO>> montarTableModelCota(FiltroConsultaFixacaoCotaDTO filtro) {
		
		List<FixacaoReparteDTO> resultadoPesquisa = fixacaoReparteService.obterFixacoesRepartePorCota(filtro);
		
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
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		List<FixacaoReparteDTO>	resultadoPesquisa = fixacaoReparteService.obterHistoricoLancamentoPorCota(filtro);
		
		
		if(resultadoPesquisa.size()>0){
			FixacaoReparteDTO fixacaoReparteDTO = resultadoPesquisa.get(0);
			this.result.include("ultimaEdicao",fixacaoReparteDTO);
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
		
		if(!isCotaValida(fixacaoReparteDTO)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Informe uma cota ativa para realizar fixação!");
		}else if(!isStatusEdicaoValido(fixacaoReparteDTO)){
			throw new ValidacaoException(TipoMensagem.WARNING,"O status da edição não permite fixação!");
		} else {
			if(fixacaoReparteDTO.isQtdeEdicoesMarcado()){
				if(fixacaoReparteDTO.getQtdeEdicoes() != null ){
					if(fixacaoReparteDTO.getQtdeEdicoes() > MAX_EDICOES){
						throw new ValidacaoException(TipoMensagem.WARNING,"O numero de edições fixadas não pode ser maior que 6");
					}else if(fixacaoReparteDTO.getQtdeExemplares() == null || fixacaoReparteDTO.getQtdeExemplares() == 0){
						throw new ValidacaoException(TipoMensagem.WARNING,"Quantidade de exemplares não pode ser vazia ou 0.");
					}
				}
			} else {
				if( fixacaoReparteDTO.getEdicaoInicial() ==null ||  fixacaoReparteDTO.getEdicaoInicial() == 0){
					throw new ValidacaoException(TipoMensagem.WARNING,"Edição inicial não pode ser vazia ou 0.");
				}else if( fixacaoReparteDTO.getEdicaoFinal() ==null ||  fixacaoReparteDTO.getEdicaoFinal() == 0){
					throw new ValidacaoException(TipoMensagem.WARNING,"Edição final não pode ser vazia ou 0.");
				}else if( fixacaoReparteDTO.getEdicaoFinal() < fixacaoReparteDTO.getEdicaoInicial()){
					throw new ValidacaoException(TipoMensagem.WARNING,"Edição final não pode ser inferior a inicial.");
				}else if(fixacaoReparteDTO.getEdicaoFinal() - fixacaoReparteDTO.getEdicaoInicial() > MAX_EDICOES){
					throw new ValidacaoException(TipoMensagem.WARNING,"O intervalo não deve ultrapassar 6 edições!");
				}else if(fixacaoReparteDTO.getQtdeExemplares() == null || fixacaoReparteDTO.getQtdeExemplares() == 0){
					throw new ValidacaoException(TipoMensagem.WARNING,"Quantidade de exemplares não pode ser vazia ou 0.");
				}
			}
		}
		
		fixacaoReparteService.adicionarFixacaoReparte(fixacaoReparteDTO);
		throw new ValidacaoException(TipoMensagem.SUCCESS,"Operação realizada com sucesso!");
	}
	
	
	@Post
	@Path("/removerFixacaoReparte")
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO){
		fixacaoReparteService.removerFixacaoReparte(fixacaoReparteDTO);
		throw new ValidacaoException(TipoMensagem.SUCCESS,"Operaão realizada com sucesso!"); 
	}
	
	@Post
	@Path("/carregarGridPdv")
	public void carregarGridPdv(FiltroConsultaFixacaoProdutoDTO filtro , String sortorder, String sortname, int page, int rp){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<PdvDTO> listaPDVDTO = fixacaoReparteService.obterListaPdvPorFixacao(filtro.getIdFixacao());
		Produto produto = produtoService.obterProdutoPorCodigo(filtro.getCodigoProduto());
		
		for (PdvDTO pdvDTO : listaPDVDTO) {
			RepartePDV reparteEncontrado = repartePdvService.obterRepartePorPdv(filtro.getIdFixacao(), produto.getId(), pdvDTO.getId());
			if(reparteEncontrado !=null)
				pdvDTO.setReparte( reparteEncontrado.getReparte());
			}
		
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
		repartePdvService.salvarRepartesPDV(listPDV,codProduto, codCota, idFixacao, manterFixa);
		throw new ValidacaoException(TipoMensagem.SUCCESS,"Operação realizada com sucesso!");
	}
	
	@Post
	@Path("/verificaCotaPossuiPdvs")
	public void verificaCotaPossuiPdvs(Long idFixacao){
		QuantidadePdvCotaDTO quantidadePdvCotaDTO  = new QuantidadePdvCotaDTO();
		quantidadePdvCotaDTO.setCotaPossuiVariosPdvs(fixacaoReparteService.isCotaPossuiVariosPdvs(idFixacao));
		result.use(Results.json()).withoutRoot().from(quantidadePdvCotaDTO).recursive().serialize();
	}
	
	@Get
	public void exportar(FileType fileType, String tipoExportacao) throws IOException {
		List dto = new ArrayList<>();
		Class clazz = null;
		FiltroConsultaFixacaoCotaDTO filtroPorCota = null;
		FiltroConsultaFixacaoProdutoDTO filtroPorProduto = null;
		List<FixacaoReparteDTO> resultadoPesquisa = null;
		
		filtroPorCota = (FiltroConsultaFixacaoCotaDTO) session.getAttribute(FILTRO_COTA_SESSION_ATTRIBUTE);
		filtroPorProduto = (FiltroConsultaFixacaoProdutoDTO) session.getAttribute(FILTRO_PRODUTO_SESSION_ATTRIBUTE);
		
		if (tipoExportacao.equals("historicoCota")) {
			clazz = FixacaoReparteHistoricoDTO.class;
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
					dto.add(fixacaoReparteHistoricoDTO);
				}
			}
		}else if (tipoExportacao.equals("historicoProduto")) {
			clazz = FixacaoReparteHistoricoDTO.class;
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
					dto.add(fixacaoReparteHistoricoDTO);
					}
				}
		} else if (tipoExportacao.equals("cota")) {
			clazz = FixacaoReparteCotaDTO.class;
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
				
				dto.add(fixacaoReparteCotaDTO);
			}
		}else if (tipoExportacao.equals("produto")) {
			clazz = FixacaoReparteProdutoDTO.class;
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
				
				dto.add(fixacaoReparteProdutoDTO);
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
	
	private boolean isRangeEdicoesValido(FixacaoReparteDTO fixacaoReparteDTO) {
		boolean rangeEdicoesOK =(fixacaoReparteDTO.getEdicaoFinal() >= fixacaoReparteDTO.getEdicaoInicial());
		return rangeEdicoesOK ;
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
	
		if(!isListaVazia(listaFixacaoExcel)){
			 listaRegistrosInvalidosExcel = obterListaInvalidos(listaFixacaoExcel);
			 listaFixacaoExcel.removeAll(listaRegistrosInvalidosExcel);
			 for (FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel) {
				 fixacaoReparteService.adicionarFixacaoReparte(fixacaoReparteDTO);
			}
			 if(listaRegistrosInvalidosExcel.size() ==0){
				 this.result.use(Results.json()).from(
							new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso!"), 
							"result").recursive().serialize();
			 }else{
				 this.result.use(Results.json()).from(
							new ValidacaoVO(TipoMensagem.WARNING, getMsgErroUpload()), 
							"result").recursive().serialize();
			 }
			
			 
		}else{
			 this.result.use(Results.json()).from(
						new ValidacaoVO(TipoMensagem.WARNING, "Arquivo esta vazio"), 
						"result").recursive().serialize();
		}
		
		
		
	}


	private String getMsgErroUpload() {
		StringBuilder builderErros = new StringBuilder("");
		builderErros.append("Alguns registros nao foram adicionados \n");
		for(String msg: getErrosUpload()){
			builderErros.append(msg).append("\n");
		}
		return builderErros.toString();
	}


	private List<FixacaoReparteDTO> obterListaInvalidos(List<FixacaoReparteDTO> listaFixacaoExcel) {
		List<FixacaoReparteDTO>invalidos = new ArrayList<FixacaoReparteDTO>();
		for(FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel){
		//  validar se a cota existe  
			  Integer[] cotaIdArray = new Integer[listaFixacaoExcel.size()];
			  for (int i = 0; i < listaFixacaoExcel.size(); i++) {
			   cotaIdArray[i] = listaFixacaoExcel.get(i).getCotaFixada();
			  }
			  List<Integer> verificarNumeroCotaExiste = this.cotaService.verificarNumeroCotaExiste(cotaIdArray);
			  
			  for (FixacaoReparteDTO fixacaoDTO : listaFixacaoExcel) {
			   if(!verificarNumeroCotaExiste.contains(fixacaoDTO.getCotaFixada())){
			    invalidos.add(fixacaoDTO);
			    getErrosUpload().add("-Cota \t" + fixacaoDTO.getCotaFixada().toString() +  "\t nao encontrada");
			   }
			  }

			  /*
			  validar se o produto é um produtoValido
			  */
			  String[] codigoProdutoArray = new String[listaFixacaoExcel.size()];
			  for (int i = 0; i < listaFixacaoExcel.size(); i++) {
			   codigoProdutoArray[i]=listaFixacaoExcel.get(i).getProdutoFixado();
			  }
			  
			  List<String> verificarProdutoExiste = this.produtoService.verificarProdutoExiste(codigoProdutoArray);
			  for (FixacaoReparteDTO fixacaoDTO : listaFixacaoExcel) {
			   if(!verificarProdutoExiste.contains(fixacaoDTO.getProdutoFixado())){
			    invalidos.add(fixacaoDTO);
			    getErrosUpload().add("-Produto \t" + fixacaoDTO.getProdutoFixado() +  "\t nao encontrado");
			   }
			  }
			  
			  // Validacao preenchimento campos
			  for (FixacaoReparteDTO fixacaoDTO : listaFixacaoExcel) {
				  if(isPreechimentoInvalido(fixacaoReparteDTO)){
					  invalidos.add(fixacaoDTO);
					  getErrosUpload().add("-Impossivel identificar fixação por cota ou produto");
				  }
			  }
			  
			  //validacao existe registro salvo
			  for (FixacaoReparteDTO fixacaoDTO : listaFixacaoExcel) {
				  if(fixacaoReparteService.isFixacaoExistente(fixacaoReparteDTO)){
					  invalidos.add(fixacaoDTO);
					  getErrosUpload().add("-Registro existente para o codigo:\t" + fixacaoDTO.getProdutoFixado() + "\t e cota:" + fixacaoDTO.getCotaFixada().toString()) ;
				  }
			  }
			  
		}
		return invalidos;
	}


	private boolean isPreechimentoInvalido(FixacaoReparteDTO fixacaoReparteDTO) {
		boolean edIniOk= (fixacaoReparteDTO.getEdicaoInicial()!=null && fixacaoReparteDTO.getEdicaoInicial() > 0);
		boolean edFinalOk= (fixacaoReparteDTO.getEdicaoFinal()!=null && fixacaoReparteDTO.getEdicaoFinal() > 0 && fixacaoReparteDTO.getEdicaoFinal() > fixacaoReparteDTO.getEdicaoFinal());
		boolean qtdeEdicoesOk= (fixacaoReparteDTO.getQtdeEdicoes()!=null && fixacaoReparteDTO.getQtdeEdicoes()>0);		
		return ( edIniOk  && qtdeEdicoesOk) || (edFinalOk && qtdeEdicoesOk);
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

	
	
}
