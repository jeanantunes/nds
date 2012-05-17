package br.com.abril.nds.controllers.lancamento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/relatorioVendas")
public class RelatorioVendasController {

	@Autowired
	private Result result;

	@Autowired
	private Validator validator;

	@Autowired 
	private Localization localization;

	@Autowired 
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EditorService editorService;  
	
	@Autowired 
	private CotaService cotaService;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";

	private static final String CAMPO_REQUERIDO_KEY = "required_field";
	private static final String CAMPO_MAIOR_IGUAL_KEY = "validator.must.be.greaterEquals";

	public RelatorioVendasController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){
	}

	private void validarDadosEntradaPesquisa(String dataDe, String dataAte) {
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (dataDe == null || dataDe.isEmpty()){
			listaMensagemValidacao.add("Erro");
		}

		if (dataAte == null || dataAte.isEmpty()){
			listaMensagemValidacao.add("Erro");
		}

		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}
	
	@Post
	@Path("/pesquisarCurvaABCDistribuidor")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte) throws Exception{
		pesquisarCurvaABCDistribuidor(dataDe, dataAte, "", "", "", "", "", "", "", "");
	}

	@Post
	@Path("/pesquisarCurvaABCDistribuidorAvancada")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte, String codigoFornecedor, String codigoProduto, String nomeProduto, String edicaoProduto, String codigoEditor, String codigoCota, String nomeCota, String municipio) throws Exception{

		this.validarDadosEntradaPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		
		FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO = new FiltroCurvaABCDistribuidorDTO(
				sdf.parse(dataDe), 
				sdf.parse(dataAte), 
				codigoFornecedor,
				codigoProduto, 
				nomeProduto, 
				edicaoProduto,
				codigoEditor, 
				codigoCota, 
				nomeCota,
				municipio);
		
		List<RegistroCurvaABCDistribuidorVO> resultadoCurvaABCDistribuidor = null;
		try {
			resultadoCurvaABCDistribuidor = distribuidorService.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		} catch (Exception e) {
			
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		
		if (resultadoCurvaABCDistribuidor == null || resultadoCurvaABCDistribuidor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			int qtdeTotalRegistros = resultadoCurvaABCDistribuidor.size();
			
			/*PaginacaoUtil.armazenarQtdRegistrosPesquisa(
				this.session,
				QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE,
				listaMovimentoAprovacaoDTO.size());
						
			this.processarAprovacoes(listaMovimentoAprovacaoDTO, filtro,
									 qtdeTotalRegistros.intValue());*/


			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel =
					new TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCDistribuidor));
			//tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
			
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();		
		
		}

	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarProdutosPorEditor(String codigo, String dataDe, String dataAte) throws Exception{
	}

}
