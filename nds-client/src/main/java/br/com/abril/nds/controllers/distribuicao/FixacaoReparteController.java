package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/fixacaoReparte")
public class FixacaoReparteController extends BaseController {
	@Autowired
	private Result result;
	
	@Autowired
	TipoProdutoService tipoProdutoService;
	
	FiltroConsultaFixacaoCotaDTO filtroPorCota;
	
	FiltroConsultaFixacaoProdutoDTO filtroPorProduto;
	

	@Rules(Permissao.ROLE_DISTRIBUICAO_FIXACAOREPARTE)
	public void index(){
		result.include("listaTiposProduto", getListaTipoProduto());
	}
	
	
	public List<TipoProduto> getListaTipoProduto(){
		return tipoProdutoService.obterTodosTiposProduto();
	}
	
	@Post
	@Path("/fixacaoReparte/pesquisar")
	public void pesquisar(FiltroConsultaFixacaoProdutoDTO filtro){
//	TableModel<CellModelKeyValue<ProdutoDTO>> tableModel = efetuarConsultaVendaProduto(filtro);
		
//		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
}
