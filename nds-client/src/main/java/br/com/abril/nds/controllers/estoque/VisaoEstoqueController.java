package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.VisaoEstoqueConferenciaCegaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("estoque/visaoEstoque")
@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
public class VisaoEstoqueController extends BaseController {
	
	private static final String FILTRO_VISAO_ESTOQUE = "FILTRO_VISAO_ESTOQUE";
	private static final String LISTA_CONFERENCIA_CEGA = "LISTA_CONFERENCIA_CEGA";
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private VisaoEstoqueService visaoEstoqueService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	
	private Result result;
	
	public VisaoEstoqueController(Result result) {
		super();
		this.result = result;
	}

	
	@Path("/")
	public void index()
	{
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		result.include("listFornecedores", listFornecedores);
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
	}
	
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaVisaoEstoque filtro) {
		tratarErro(validarDadosConsulta(filtro));
		
		this.atualizarDataMovimentacao(filtro);
		
		this.session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		result.use(FlexiGridJson.class).from(listVisaoEstoque).total(listVisaoEstoque.size()).serialize();
	}
	
	@Path("/pesquisarDetalhe.json")
	public void pesquisarDetalhe(FiltroConsultaVisaoEstoque filtro, String sortname, String sortorder, int rp, int page) {		
		
		if("undefined".equalsIgnoreCase(sortorder)){
			sortorder = null;
		}
		
		if("undefined".equalsIgnoreCase(sortname)){
			sortname = null;
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp,sortorder,sortname));
		
		if(filtro.getPaginar()!=null && !filtro.getPaginar()) {
			filtro.getPaginacao().setQtdResultadosPorPagina(null);
			filtro.getPaginacao().setPaginaAtual(null);
		}
		
		this.atualizarDataMovimentacao(filtro);
		
		this.session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
		
		Long count = visaoEstoqueService.obterCountVisaoEstoqueDetalhe(filtro);
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
				
		result.use(FlexiGridJson.class).from(listDetalhe).total(count.intValue()).page(page).serialize();
	}
	
	
	@Path("/transferir")
	public void transferir(FiltroConsultaVisaoEstoque filtro) {
		
		this.atualizarDataMovimentacao(filtro);
		
		TipoEstoque entrada  = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoqueSelecionado());
		TipoEstoque saida = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoque());

		filtro.setGrupoMovimentoEntrada(this.getGrupoMovimentoTransferencia(entrada, true));
		filtro.setGrupoMovimentoSaida(this.getGrupoMovimentoTransferencia(saida, false));

		this.visaoEstoqueService.transferirEstoque(filtro, this.getUsuarioLogado());
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Transferência realizada com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	private GrupoMovimentoEstoque getGrupoMovimentoTransferencia(TipoEstoque tipoEstoque, boolean isEntrada) {
		
		switch(tipoEstoque) {
		
		case LANCAMENTO:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO : 
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
		case SUPLEMENTAR:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
		case RECOLHIMENTO:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
		case PRODUTOS_DANIFICADOS:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
		default:
			return null;
		}
	}
	
	
	@Path("/inventario")
	public void inventario(FiltroConsultaVisaoEstoque filtro) {

		this.atualizarDataMovimentacao(filtro);
		
		TipoEstoque tipoEstoque = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoque());
		
		this.visaoEstoqueService.atualizarInventarioEstoque(
			filtro.getListaTransferencia(), tipoEstoque, this.getUsuarioLogado()
		);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Atualização de inventário concluída com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").serialize();
	}

	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) this.session.getAttribute(FILTRO_VISAO_ESTOQUE);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		
		FileExporter.to("visao-estoque", fileType).inHTTPResponse(
				this.getNDSFileHeader(filtro.getDataMovimentacao()), null, null,
				listVisaoEstoque, VisaoEstoqueDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/exportarDetalhe")
	public void exportarDetalhe(FileType fileType) throws IOException {
		
		FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) this.session.getAttribute(FILTRO_VISAO_ESTOQUE);
		
		if (filtro.getPaginacao() == null){
			filtro.setPaginacao(new PaginacaoVO());
		}
		
		filtro.getPaginacao().setPaginaAtual(null);
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setQtdResultadosTotal(null);
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
		Class clazz = VisaoEstoqueDetalheDTO.class;
		
		if (listDetalhe != null && !listDetalhe.isEmpty() && listDetalhe.get(0) instanceof VisaoEstoqueDetalheJuramentadoDTO) {
			clazz = VisaoEstoqueDetalheJuramentadoDTO.class;
		}
		
		FileExporter.to("visao-estoque", fileType).inHTTPResponse(
				this.getNDSFileHeader(filtro.getDataMovimentacao()), null, null,
				listDetalhe, clazz,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/gerarDadosConferenciaCega")
	public void gerarDadosConferenciaCega(FiltroConsultaVisaoEstoque filtro) throws IOException {
		
		this.atualizarDataMovimentacao(filtro);
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
		
		List<VisaoEstoqueConferenciaCegaVO> listaExport = new ArrayList<VisaoEstoqueConferenciaCegaVO>();
		
		for(VisaoEstoqueDetalheDTO dto : listDetalhe) {
			
			VisaoEstoqueConferenciaCegaVO vo = new VisaoEstoqueConferenciaCegaVO(dto);
			
			
			
			listaExport.add(vo);
		}
		
		this.session.setAttribute(LISTA_CONFERENCIA_CEGA, listaExport);
		result.use(Results.nothing());
	}
	
	
	@SuppressWarnings("unchecked")
	@Path("/exportarConferenciaCega")
	public void exportarConferenciaCega(FileType fileType) throws IOException {
		
		List<VisaoEstoqueConferenciaCegaVO> listaExport = (List<VisaoEstoqueConferenciaCegaVO>) this.session.getAttribute(LISTA_CONFERENCIA_CEGA);
		
		if (listaExport == null || listaExport.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		FileExporter.to("visao-estoque-conferencia-cega", fileType).inHTTPResponse(
				getNDSFileHeader(), null, null,
				listaExport, VisaoEstoqueConferenciaCegaVO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
		
	private void atualizarDataMovimentacao(FiltroConsultaVisaoEstoque filtro) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (filtro.getDataMovimentacao() == null
				|| DateUtil.isDataInicialMaiorDataFinal(filtro.getDataMovimentacao(), dataOperacao)) {
			
			filtro.setDataMovimentacao(dataOperacao);
		}
	}

	private List<String> validarDadosConsulta(FiltroConsultaVisaoEstoque filtro){
		
		List<String> mensagens = new ArrayList<String>();
		if(filtro != null && filtro.getDataMovimentacaoStr() != null && !"".equals(filtro.getDataMovimentacaoStr())){
			
			if (!DateUtil.isValidDate(filtro.getDataMovimentacaoStr(), "dd/MM/yyyy")) {
				
				mensagens.add("O campo Data Movimento de é inválido");
			}else{
				filtro.setDataMovimentacao(DateUtil.parseDataPTBR(filtro.getDataMovimentacaoStr()));
			}
		}
		
		return mensagens;
	}
	
	private void tratarErro(List<String> mensagensErro){
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		if(!mensagensErro.isEmpty()){
			
			validacao.setListaMensagens(mensagensErro);
			
			throw new ValidacaoException(validacao);
		}
	}
}
