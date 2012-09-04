package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VendaProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/vendaProduto")
public class VendaProdutoController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroVendaProduto";
	
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;
	
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
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<VendaProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaVendaProdutoDTO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaVendaProdutoDTO.size());
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarLancamentoEdicao")
	public void pesquisarLancamentoEdicao(FiltroVendaProdutoDTO filtro,String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> tableModel = efetuarConsultaLancamentoEdicao(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> efetuarConsultaLancamentoEdicao(FiltroVendaProdutoDTO filtro) {
		 
		List<LancamentoPorEdicaoDTO> listaAux =  this.vendaProdutoService.buscaLancamentoPorEdicao(filtro);		
		List<LancamentoPorEdicaoDTO> listaLancamentoPorEdicao = new ArrayList<LancamentoPorEdicaoDTO>();
		int cont = 1;
		if(!listaAux.isEmpty()){
			for(LancamentoPorEdicaoDTO dto: listaAux){
				dto.setPeriodo(cont+"º");
				listaLancamentoPorEdicao.add(dto);
				cont++;
			}			
		}
		
		TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>> tableModel = new TableModel<CellModelKeyValue<LancamentoPorEdicaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentoPorEdicao));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
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
		
		FiltroVendaProdutoDTO filtro = (FiltroVendaProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if(tipoExportacao.equals("principal")){
			List<VendaProdutoDTO> listaDTOParaExportacao = vendaProdutoService.buscaVendaPorProduto(filtro);
			
			if(listaDTOParaExportacao.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("venda_produto", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaDTOParaExportacao, VendaProdutoDTO.class, this.httpResponse);
			
		}else if(tipoExportacao.equals("popup")){
			List<LancamentoPorEdicaoDTO> lista = this.vendaProdutoService.buscaLancamentoPorEdicao(filtro);
			
			if(lista.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("lancamento_edicao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					lista, LancamentoPorEdicaoDTO.class, this.httpResponse);
		}
		
		result.nothing();
	}
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
	}
	
	private void tratarFiltro(FiltroVendaProdutoDTO filtroAtual) {

		FiltroVendaProdutoDTO filtroSession = (FiltroVendaProdutoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

}
