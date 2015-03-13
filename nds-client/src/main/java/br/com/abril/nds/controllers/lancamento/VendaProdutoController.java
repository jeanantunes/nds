package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.VendaProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
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
@Path("/lancamento/vendaProduto")
@Rules(Permissao.ROLE_LANCAMENTO_VENDA_PRODUTO)
public class VendaProdutoController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroVendaProduto";
	
	private static final String FILTRO_DETALHE_SESSION_ATTRIBUTE = "filtroDetalheVendaProduto";
	
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private VendaProdutoService vendaProdutoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	public VendaProdutoController(Result result) {
		this.result = result;
	}
	
	
	@Path("/")
	@Rules(Permissao.ROLE_LANCAMENTO_VENDA_PRODUTO)
	public void index(){
		this.carregarComboFornecedores();
		this.carregarComboClassificacao();
	}
	
	
	@Post
	@Path("/pesquisarVendaProduto")
	public void pesquisarVendaProduto(FiltroVendaProdutoDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = efetuarConsultaVendaProduto(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<VendaProdutoDTO>> efetuarConsultaVendaProduto(FiltroVendaProdutoDTO filtro) {
		
		List<VendaProdutoDTO> listaVendaProdutoDTO =  this.vendaProdutoService.buscaVendaPorProduto(filtro);				

		if (listaVendaProdutoDTO == null || listaVendaProdutoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<VendaProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaVendaProdutoDTO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarLancamentoEdicao")
	public void pesquisarLancamentoEdicao(FiltroDetalheVendaProdutoDTO filtro,String sortorder, String sortname, int page, int rp){
		
		this.tratarFiltroDetalhe(filtro);
		
		TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> tableModel = efetuarConsultaLancamentoEdicao(filtro);
		
		if (tableModel.getRows() == null || tableModel.getRows().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro foi encontrado.");
		}
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> efetuarConsultaLancamentoEdicao(FiltroDetalheVendaProdutoDTO filtro) {
		 
		List<LancamentoPorEdicaoDTO> listaLancamentoPorEdicao =  this.vendaProdutoService.buscaLancamentoPorEdicao(filtro);
		
		TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> tableModel = new TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentoPorEdicao));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(listaLancamentoPorEdicao.size());
		
		return tableModel;
	}

	private void validarEntrada(FiltroVendaProdutoDTO filtro) {
		
		if(filtro.getCodigo()==null || filtro.getCodigo().trim().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Código é obrigatório.");		
		if(filtro.getNomeProduto()==null || filtro.getNomeProduto().trim().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		
	}
	
	@Get
	public void exportar(FileType fileType, String tipoExportacao) throws IOException {
		
		FiltroVendaProdutoDTO filtroVenda = (FiltroVendaProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		FiltroDetalheVendaProdutoDTO filtroDetalhe = (FiltroDetalheVendaProdutoDTO) session.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
		
		if(tipoExportacao.equals("principal")){
			List<VendaProdutoDTO> listaDTOParaExportacao = this.vendaProdutoService.buscaVendaPorProduto(filtroVenda);
			
			if(listaDTOParaExportacao.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("venda_produto", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroVenda, null, 
					listaDTOParaExportacao, VendaProdutoDTO.class, this.httpResponse);
			
		}else if(tipoExportacao.equals("popup")){
			List<LancamentoPorEdicaoDTO> lista = this.vendaProdutoService.buscaLancamentoPorEdicao(filtroDetalhe);
			
			if(lista.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("lancamento_edicao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroDetalhe, null, 
					lista, LancamentoPorEdicaoDTO.class, this.httpResponse);
		}
		
		result.nothing();
	}
	
	private void montarFiltroVendaProduto(FiltroVendaProdutoDTO filtro) {
		
		if (filtro != null) {
			
			if (filtro.getIdFornecedor() != null) {
				
				Fornecedor fornecedor =
					this.fornecedorService.obterFornecedorPorId(filtro.getIdFornecedor());
				
				if (fornecedor != null) {
					
					filtro.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
				}
			}

			if (filtro.getCodigo() != null) {
				
				String nomeProduto =
					this.produtoService.obterNomeProdutoPorCodigo(filtro.getCodigo());
				
				filtro.setNomeProduto(nomeProduto);
			}
		}
	}
	
	private void montarFiltroDetalheVendaProduto(FiltroDetalheVendaProdutoDTO filtro) {
		
		if (filtro != null) {
			
			if (filtro.getCodigo() != null) {
				
				String nomeProduto =
					this.produtoService.obterNomeProdutoPorCodigo(filtro.getCodigo());
				
				filtro.setNomeProduto(nomeProduto);
			}
		}
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	private void tratarFiltro(FiltroVendaProdutoDTO filtroAtual) {

//		FiltroVendaProdutoDTO filtroSession = (FiltroVendaProdutoDTO) session
//				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
//		
//		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
//
//			filtroAtual.getPaginacao().setPaginaAtual(1);
//		}
		
		this.montarFiltroVendaProduto(filtroAtual);
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void tratarFiltroDetalhe(FiltroDetalheVendaProdutoDTO filtroAtual) {
		
		this.montarFiltroDetalheVendaProduto(filtroAtual);
		
		session.setAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		
		List<TipoClassificacaoProduto> classificacoes = vendaProdutoService.buscarClassificacaoProduto();
		
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}

}