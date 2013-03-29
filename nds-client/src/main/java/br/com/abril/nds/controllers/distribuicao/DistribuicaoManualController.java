package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicaoManual")
@Resource
public class DistribuicaoManualController {

    @Autowired
    private Result result;
    
    @Autowired
    private CotaService cotaService;

    @Autowired
    private EstudoCotaService estudoCotaService;
    
    @Path("/")
    public void index(ProdutoDistribuicaoVO produto) {
	result.include("produto", produto);
    }

    @Path("/carregarGrid")
    public void carregarGrid(EstudoTransient estudo) {
	List<ProdutoDistribuicaoVO> listaProdutos = new ArrayList<>();
	listaProdutos.add(new ProdutoDistribuicaoVO());
	TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>> tableModel = new TableModel<>();
	tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaProdutos));
	tableModel.setTotal(listaProdutos.size());
	result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    @Path("/consultarCota")
    public void consultarCota(Integer numeroCota) {
	Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
	this.result.use(Results.json()).from(cota, "result").recursive().serialize();
    }
}
