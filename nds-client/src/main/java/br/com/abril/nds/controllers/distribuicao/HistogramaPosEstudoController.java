package br.com.abril.nds.controllers.distribuicao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.HistogramaPosEstudoDadoInicioDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/histogramaPosEstudo")
@Resource
public class HistogramaPosEstudoController extends BaseController{
	
	@Autowired
	private Result result;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EstudoService estudoService;
	
	@Path("/index")
	public void histogramaPosEstudo(){
	}
	
	@Post
	public void carregarGridHistogramaPosEstudo(){
		
	}
	
	@Post
	@Path("/carregarDadosFieldsetHistogramaPreAnalise")
	public void carregarDadosFieldsetHistogramaPreAnalise(HistogramaPosEstudoDadoInicioDTO selecionado ){
		Produto produto = produtoService.obterProdutoPorCodigo(selecionado.getCodigoProduto());
		Estudo estudo = estudoService.obterEstudo(Long.parseLong(selecionado.getEstudo()));
		
		selecionado.setTipoSegmentoProduto(produto.getTipoSegmentoProduto());
		selecionado.setPeriodicidadeProduto(produto.getPeriodicidade().getOrdem());
		
		if (estudo.getLiberado() != null && estudo.getLiberado() == 1) {
			selecionado.setEstudoLiberado(Boolean.TRUE);
		}else{
			selecionado.setEstudoLiberado(Boolean.FALSE);
		}
		
		result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
		
	}
	
}
