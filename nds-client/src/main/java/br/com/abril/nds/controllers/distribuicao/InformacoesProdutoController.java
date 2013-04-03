package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.InformacoesBaseProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/informacoesProduto")
public class InformacoesProdutoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private InformacoesProdutoService infoProdService;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroInfoProdutos";
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	public InformacoesProdutoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO)
	public void index(){
		this.carregarComboClassificacao();
	}
	
	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		List<TipoClassificacaoProduto> classificacoes = infoProdService.buscarClassificacao();
		
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}
	
	@Post
	@Path("/buscarProduto")
	public void buscarProduto (FiltroInformacoesProdutoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		this.tratarArgumentosFiltro(filtro);
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<InformacoesProdutoDTO>> tableModel = gridProdutos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<InformacoesProdutoDTO>> gridProdutos (FiltroInformacoesProdutoDTO filtro) {
		
		List<InformacoesProdutoDTO> produtos = infoProdService.buscarProduto(filtro);
		
		for (InformacoesProdutoDTO informacoesProdutoDTO : produtos) {
			if((informacoesProdutoDTO.getHora()) == null){
				informacoesProdutoDTO.setHora("");
			}
			
			if((informacoesProdutoDTO.getNomeUsuario()) == null){
				informacoesProdutoDTO.setNomeUsuario("");
			}
			
			if((informacoesProdutoDTO.getDataInsercao()) == null){
				informacoesProdutoDTO.setDataInser("");
			}
			
		}
		
		if (produtos == null || produtos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<InformacoesProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<InformacoesProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(produtos));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	
	@Post
	@Path("/buscarBaseSugerida")
	public void baseSugerida (String codProd){
		
		TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> tableModel = gridBaseSugerida(codProd);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> gridBaseSugerida (String codProd) {
		
		List<InformacoesBaseProdDTO> baseSugerida = infoProdService.buscarBases(codProd);
		
		TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> tableModel = new TableModel<CellModelKeyValue<InformacoesBaseProdDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(baseSugerida));

		tableModel.setPage(1);

		tableModel.setTotal(baseSugerida.size());

		return tableModel;
	}

	@Post
	@Path("/buscarBaseEstudo")
	public void baseEstudo (String codProd){
		
		TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> tableModel = gridBaseEstudo(codProd);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	
	}
	
	private TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> gridBaseEstudo (String codProd) {
		
		List<InformacoesBaseProdDTO> baseEstudo = infoProdService.buscarBases(codProd);
		
		TableModel<CellModelKeyValue<InformacoesBaseProdDTO>> tableModel = new TableModel<CellModelKeyValue<InformacoesBaseProdDTO>>();
	
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(baseEstudo));
	
		tableModel.setPage(1);
	
		tableModel.setTotal(baseEstudo.size());
	
		return tableModel;
	}
	
	@Post
	@Path("/buscarItemRegiao")
	public void buscarItemRegiao (){
		
		TableModel<CellModelKeyValue<InfoProdutosItemRegiaoEspecificaDTO>> tableModel = gridItemRegiao();
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	
	}
	
	private TableModel<CellModelKeyValue<InfoProdutosItemRegiaoEspecificaDTO>> gridItemRegiao () {
		
		List<InfoProdutosItemRegiaoEspecificaDTO> itensRegiao = infoProdService.buscarItemRegiao();
		
		TableModel<CellModelKeyValue<InfoProdutosItemRegiaoEspecificaDTO>> tableModel = new TableModel<CellModelKeyValue<InfoProdutosItemRegiaoEspecificaDTO>>();
	
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(itensRegiao));
	
		tableModel.setPage(1);
	
		tableModel.setTotal(itensRegiao.size());
	
		return tableModel;
	}
	
	@Post
	@Path("/buscarCaracteristicaProduto")
	public void buscarCaracteristicaProduto(String codProd, Long numEdicao){

		InformacoesCaracteristicasProdDTO caracteristicas = infoProdService.buscarCaracteristicas(codProd, numEdicao);
		
		result.use(Results.json()).from(caracteristicas, "result").serialize();

	}
	
	private void tratarFiltro(FiltroInformacoesProdutoDTO filtroAtual) {
		
		FiltroInformacoesProdutoDTO filtroSession = (FiltroInformacoesProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void tratarArgumentosFiltro (FiltroInformacoesProdutoDTO filtro){
		
		if(filtro.getCodProduto() == null || filtro.getCodProduto().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Informe o Código e o Nome do produto.");
		}
		
		if(filtro.getNomeProduto() == null || filtro.getNomeProduto().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Informe o Código e o Nome do produto.");
		}
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroInformacoesProdutoDTO filtro = (FiltroInformacoesProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<InformacoesProdutoDTO> produtos = infoProdService.buscarProduto(filtro);
		
		
			if(produtos.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A pesquisa realizada n�o obteve resultado.");
			}
			
			FileExporter.to("Informações_Produto", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					produtos, InformacoesProdutoDTO.class, this.httpResponse);
		
		result.nothing();
	}
	

}