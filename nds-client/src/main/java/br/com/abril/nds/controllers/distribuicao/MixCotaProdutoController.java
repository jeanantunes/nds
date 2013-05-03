package br.com.abril.nds.controllers.distribuicao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.UsuarioService;
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
@Path("/distribuicao/mixCotaProduto")
public class MixCotaProdutoController extends BaseController {

	private static final String OPERACAO_REALIZADA_COM_SUCESSO = "Operação realizada com sucesso!";
	private static final ValidacaoVO SUCCESS_MSG = new ValidacaoVO(TipoMensagem.SUCCESS, OPERACAO_REALIZADA_COM_SUCESSO);
	private static final int REPARTE_MAXIMO = 99999;
	private static final String FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE = "filtroMixPorProduto";
	private static final String FILTRO_MIX_COTA_SESSION_ATTRIBUTE = "filtroMixPorCota";
	private static final String COTA_IMPORT_INCONSISTENTE="cotaImportInconsistente";

	@Autowired
	private Result result;

	@Autowired
	TipoProdutoService tipoProdutoService;

	@Autowired
	ProdutoService produtoService;

	@Autowired
	FixacaoReparteService fixacaoReparteService;

	@Autowired
	MixCotaProdutoService mixCotaProdutoService;
	
	@Autowired
	PdvService pdvService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RepartePdvService repartePdvService;

	FiltroConsultaMixPorCotaDTO filtroMixPorCota;

	FiltroConsultaFixacaoProdutoDTO filtroMixPorProduto;

	FixacaoReparteDTO fixacaoReparteDTO;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private CotaService cotaService;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO)
	@Path("/")
	public void index() {
		result.include("classificacao",
				fixacaoReparteService.obterClassificacoesProduto());
	}

	@Post
	@Path("/pesquisarPorProduto")
	public void pesquisarPorProduto(FiltroConsultaMixPorProdutoDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		if (session.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE, filtro);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		tratarFiltroPorProduto(filtro);

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService.pesquisarPorProduto(filtro);

		TableModel<CellModelKeyValue<MixProdutoDTO>> tableModelProduto = montarTableModelProduto(filtro);

		result.use(Results.json()).withoutRoot().from(tableModelProduto)
				.recursive().serialize();
	}

	@Post
	@Path("/pesquisarPorCota")
	public void pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		if (session.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtro);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		tratarFiltroPorCota(filtro);

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService.pesquisarPorCota(filtro);

		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Não foram encontrados resultados para a pesquisa.");
		}

		TableModel<CellModelKeyValue<MixCotaDTO>> tableModelCota = montarTableModelCota(filtro);

		result.use(Results.json()).withoutRoot().from(tableModelCota)
				.recursive().serialize();
	}

	@Post
	@Path("/removerMixCotaProduto")
	public void removerMixCotaProduto(FiltroConsultaMixPorCotaDTO filtro) {
		mixCotaProdutoService.removerMixCotaProduto(filtro);
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}

	private TableModel<CellModelKeyValue<MixCotaDTO>> montarTableModelCota(
			FiltroConsultaMixPorCotaDTO filtro) {

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorCota(filtro);

		TableModel<CellModelKeyValue<MixCotaDTO>> tableModel = new TableModel<CellModelKeyValue<MixCotaDTO>>();

		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	private TableModel<CellModelKeyValue<MixProdutoDTO>> montarTableModelProduto(
			FiltroConsultaMixPorProdutoDTO filtro) {

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorProduto(filtro);

		TableModel<CellModelKeyValue<MixProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<MixProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	@Post
	@Path("/carregarGridHistoricoCota")
	public void carregarGridHistoricoCota(FiltroConsultaFixacaoCotaDTO filtro, String codigoProduto, String sortorder, String sortname,
			int page, int rp) {
		
		if (session.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtro);
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		List<FixacaoReparteDTO> resultadoPesquisa = fixacaoReparteService
				.obterHistoricoLancamentoPorCota(filtro);

		if (resultadoPesquisa.size() > 0) {
			FixacaoReparteDTO fixacaoReparteDTO = resultadoPesquisa.get(0);
			this.result.include("ultimaEdicao", fixacaoReparteDTO);
		}

		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();
		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(resultadoPesquisa.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive()
				.serialize();
	}

	

	@Post
	@Path("/editarRepartePorPdv")
	public void editarRepartePDV(FiltroConsultaMixPorCotaDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		MixCotaProdutoDTO resultadoPesquisa = mixCotaProdutoService.obterPorId(filtro.getId());
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa)
				.recursive().serialize();
	}
	
	@Post
	@Path("/carregarGridPdv")
	public void carregarGridPdv(FiltroConsultaMixPorCotaDTO filtro , String sortorder, String sortname, int page, int rp){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<PdvDTO> listaPDVDTO = mixCotaProdutoService.obterListaPdvPorMix(filtro.getId());
		Produto produto = produtoService.obterProdutoPorID(filtro.getProdutoId());
		
		for (PdvDTO pdvDTO : listaPDVDTO) {
			RepartePDV reparteEncontrado = repartePdvService.obterRepartePorPdvMix(filtro.getId(), produto.getId(), pdvDTO.getId());
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
	@Path("/salvarGridPdvReparte")
	public void salvarGridPdvReparte(List<RepartePDVDTO> listPDV, String codProduto, Long idMix){
		repartePdvService.salvarRepartesPDVMix(listPDV,codProduto, idMix);
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	@Post
	@Path("/excluirTodos")
	public void excluirTodos() {
		mixCotaProdutoService.excluirTodos();
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	@Post
	public void validarProduto(MixCotaProdutoDTO mixCotaProduto, String produtoId) {
		
		mixCotaProduto.setCodigoProduto(produtoId);
		
		String mensagem = mixCotaProdutoService.obterValidacaoLinha(mixCotaProduto);
		
		if (!StringUtils.isEmpty(mensagem)) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagem));
		}
		
		result.nothing();
	}
	
	@Post
	public void validarCota(MixCotaProdutoDTO mixCotaProduto, String numeroCota) {
		
		mixCotaProduto.setNumeroCota(numeroCota);
		
		String mensagem = mixCotaProdutoService.obterValidacaoLinha(mixCotaProduto);
		
		if (!StringUtils.isEmpty(mensagem)) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagem));
		}
		
		result.nothing();
	}
	
	@Post
	@Path("/adicionarMixProduto")
	public void adicionarMixProduto(List<MixCotaProdutoDTO>listaNovosMixProduto,String produtoId) {
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO: listaNovosMixProduto) {
			
			mixCotaProdutoDTO.setCodigoProduto(produtoId);
		}
		
		List<String> mensagens = mixCotaProdutoService.adicionarListaMixPorProduto(listaNovosMixProduto,produtoId);
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}
	
	@Post
	@Path("/adicionarMixCota")
	public void adicionarMixCota(List<MixCotaProdutoDTO>listaNovosMixCota, Integer cotaId){
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO: listaNovosMixCota) {
			
			mixCotaProdutoDTO.setNumeroCota(cotaId.toString());
		}
		
		List<String> mensagens = mixCotaProdutoService.adicionarListaMixPorCota(listaNovosMixCota,cotaId);
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
		result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
	}

	
	@Get
	public void exportarGridCota(FileType fileType, String tipoExportacao)
			throws IOException {

		FiltroConsultaMixPorCotaDTO filtroPorCota = (FiltroConsultaMixPorCotaDTO) session
				.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE);

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorCota(filtroPorCota);
		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A última pesquisa realizada não obteve resultado.");
		} else {

			FileExporter.to("mix", fileType).inHTTPResponse(
					this.getNDSFileHeader(), filtroPorCota, null,
					resultadoPesquisa, MixCotaDTO.class, this.httpResponse);
		}

		result.nothing();
	}

	@Get
	public void exportarGridProduto(FileType fileType, String tipoExportacao)
			throws IOException {

		FiltroConsultaMixPorProdutoDTO filtroPorProduto = (FiltroConsultaMixPorProdutoDTO) session
				.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE);

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorProduto(filtroPorProduto);
		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A última pesquisa realizada não obteve resultado.");
		} else {

			FileExporter.to("mix", fileType).inHTTPResponse(
					this.getNDSFileHeader(), filtroPorProduto, null,
					resultadoPesquisa, MixProdutoDTO.class, this.httpResponse);
		}

		result.nothing();
	}

	private void tratarFiltroPorCota(FiltroConsultaMixPorCotaDTO filtroAtual) {

		FiltroConsultaMixPorCotaDTO filtroSession = (FiltroConsultaMixPorCotaDTO) session
				.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtroAtual);
	}

	private void tratarFiltroPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroAtual) {

		FiltroConsultaMixPorProdutoDTO filtroSession = (FiltroConsultaMixPorProdutoDTO) session
				.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE, filtroAtual);
	}

	@Post
	@Path("/uploadArquivoLote")
	public void uploadExcel(UploadedFile excelFile) throws FileNotFoundException, IOException{

		List<MixCotaDTO> listMixExcel = XlsUploaderUtils.getBeanListFromXls(MixCotaDTO.class, excelFile);
		
		List<MixCotaDTO> mixCotaDTOInconsistente = validaMixEmLote(listMixExcel);
		
		//salvar lista listMixExcel 
		List<MixCotaProdutoDTO> mixCotaProdutoDTOList = new ArrayList<MixCotaProdutoDTO>();
		for (MixCotaDTO mixCotaDTO : listMixExcel) {
			MixCotaProdutoDTO mix = new MixCotaProdutoDTO();
			mix.setCodigoProduto(mixCotaDTO.getCodigoProduto());
			mix.setNumeroCota(mixCotaDTO.getNumeroCota().toString());
			mix.setReparteMinimo(mixCotaDTO.getReparteMinimo().longValue());
			mix.setReparteMaximo(mixCotaDTO.getReparteMaximo().longValue());
			mixCotaProdutoDTOList.add(mix);
		}
		
		List<String> mensagens = this.mixCotaProdutoService.adicionarMixEmLote(mixCotaProdutoDTOList);
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
		//salvar em sessao mixCotaDTOIconsistente para posteriormente mostrar na tela
		session.setAttribute(COTA_IMPORT_INCONSISTENTE,mixCotaDTOInconsistente );
		
		this.result.use(Results.json()).from(mixCotaDTOInconsistente, "mixCotaDTOInconsistente").recursive().serialize();
	}

	private List<MixCotaDTO> validaMixEmLote(List<MixCotaDTO> listMixExcel) {
		List<MixCotaDTO> listCotaInconsistente = new ArrayList<MixCotaDTO>();
		for (MixCotaDTO mixCotaDTO : listMixExcel) {
			if ( StringUtils.isEmpty(mixCotaDTO.getCodigoProduto())
					|| ( mixCotaDTO.getNumeroCota() == null    || mixCotaDTO.getNumeroCota().equals(0) )
					|| ( mixCotaDTO.getReparteMinimo() == null || mixCotaDTO.getReparteMinimo().compareTo(BigInteger.ZERO) < 0 )
					|| ( mixCotaDTO.getReparteMaximo() == null || mixCotaDTO.getReparteMaximo().compareTo(BigInteger.ZERO) <= 0
						|| mixCotaDTO.getReparteMaximo().compareTo(BigInteger.valueOf(REPARTE_MAXIMO)) > 0 ) 
					) {
				listCotaInconsistente.add(mixCotaDTO);
			}
		}
		listMixExcel.removeAll(listCotaInconsistente);
		
		if (listMixExcel.isEmpty()) {
			
			return listCotaInconsistente;
		}	
		
		//validar se o reparteMaximo é maior que o reparteMinimo
		for (MixCotaDTO mixCotaDTO : listMixExcel) {
			if(mixCotaDTO.getReparteMinimo().compareTo(mixCotaDTO.getReparteMaximo())==1){
				listCotaInconsistente.add(mixCotaDTO);
			}
		}
		listMixExcel.removeAll(listCotaInconsistente);
		
		if (listMixExcel.isEmpty()) {
			
			return listCotaInconsistente;
		}	
		
		//validar se a cota existe		
		Integer[] cotaIdArray = new Integer[listMixExcel.size()];
		for (int i = 0; i < listMixExcel.size(); i++) {
			cotaIdArray[i] = listMixExcel.get(i).getNumeroCota();
		}
		List<Integer> verificarNumeroCotaExiste = cotaService.numeroCotaExiste(TipoDistribuicaoCota.ALTERNATIVO, cotaIdArray);
		
		for (MixCotaDTO mixCotaDTO : listMixExcel) {
			if(!verificarNumeroCotaExiste.contains(mixCotaDTO.getNumeroCota())){
				listCotaInconsistente.add(mixCotaDTO);
			}
		}
		
		listMixExcel.removeAll(listCotaInconsistente);

		if (listMixExcel.isEmpty()) {
			
			return listCotaInconsistente;
		}	
		
			// validar se o produto é um produtoValido
			String[] codigoProdutoArray = new String[listMixExcel.size()];
			for (int i = 0; i < listMixExcel.size(); i++) {
				codigoProdutoArray[i]=listMixExcel.get(i).getCodigoProduto();
			}
			
			List<String> verificarProdutoExiste = this.produtoService.verificarProdutoExiste(codigoProdutoArray);
			for (MixCotaDTO mixCotaDTO : listMixExcel) {
				if(!verificarProdutoExiste.contains(mixCotaDTO.getCodigoProduto())){
					listCotaInconsistente.add(mixCotaDTO);
				}
			}
			
			listMixExcel.removeAll(listCotaInconsistente);
		
		
		return listCotaInconsistente;

	}

	@Post
	@Path("/gerarCopiaMix")
	public void gerarCopiaMix(CopiaMixFixacaoDTO copiaMix){
		
		TipoMensagem tipoMsg = TipoMensagem.WARNING;
		List<String> msg = new ArrayList<String>();

		switch (copiaMix.getTipoCopia()) {
		case COTA:
			Cota cotaOrigem = cotaService.obterPorNumeroDaCota(copiaMix.getCotaNumeroOrigem());
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaMix.getCotaNumeroDestino());

			if(cotaOrigem.getTipoDistribuicaoCota()==null || cotaDestino.getTipoDistribuicaoCota()==null
					|| !cotaOrigem.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO) 
					|| !cotaDestino.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)){
				msg.add( "Cotas não são do tipo ALTERNATIVO.");
			} 
			
			break;
		case PRODUTO:
			
			break;
		}
		
		
		if(msg.isEmpty()){
			try {
				
				boolean gerarCopiaMix = this.mixCotaProdutoService.gerarCopiaMix(copiaMix);
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

}
