package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BaseEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.HistogramaPosEstudoDadoInicioDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/histogramaPosEstudo")
@Resource
public class HistogramaPosEstudoController extends BaseController{
	
	private String[] faixaReparteInicial = {"0-10","11-20","21-50","51-90","91-99999999"}; 
	
	@Autowired
	private Result result;
	
	@Autowired
	private EstudoProdutoEdicaoBaseRepository estudoProdutoEdicaoBaseRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService;
	
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
	
	@Post
	public void carregarGridAnalise(String[] faixasReparte, int estudoId){

		List<BaseEstudoAnaliseFaixaReparteDTO> base = new ArrayList<>();
		
		String[] faixaIterator = faixasReparte;
		
		if (faixaIterator == null || faixaIterator.length == 0) {
			faixaIterator = faixaReparteInicial;
		}
		
		for (String faixas : faixaIterator) {
			int faixaDe = Integer.parseInt(faixas.split("-")[0]);
			int faixaAte = Integer.parseInt(faixas.split("-")[1]);
			BaseEstudoAnaliseFaixaReparteDTO baseEstudoAnaliseFaixaReparteDTO = histogramaPosEstudoFaixaReparteService.obterHistogramaPosEstudo(faixaDe, faixaAte, estudoId);
			base.add(baseEstudoAnaliseFaixaReparteDTO);
		}
		
		TableModel<CellModelKeyValue<BaseEstudoAnaliseFaixaReparteDTO>> tableModel = new TableModel<>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(base));

		tableModel.setPage(1);

		tableModel.setTotal(faixaIterator.length);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void carregarGridBaseSugerida(){
		
	}
	
	@Post
	public void carregarGridBaseEstudo(long estudoId){

		List<EdicaoBaseEstudoDTO> estudoProdutoEdicaoBaseDTO = estudoProdutoEdicaoBaseRepository.obterEdicoesBase(estudoId);
		
		TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>> tableModel = new TableModel<>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(estudoProdutoEdicaoBaseDTO));

		tableModel.setPage(1);

		tableModel.setTotal(6);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
}
