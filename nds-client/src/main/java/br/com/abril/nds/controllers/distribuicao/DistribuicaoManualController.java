package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicaoManual")
@Resource
public class DistribuicaoManualController extends BaseController {

    @Autowired
    private Result result;
    
    @Autowired
    private CotaService cotaService;

    @Autowired
    private EstudoService estudoService;

    @Autowired
    private EstudoCotaService estudoCotaService;
    
    @Path("/")
    public void index(ProdutoDistribuicaoVO produto) {
	estudoService.criarNovoEstudo(produto);
	result.include("produto", produto);
    }

    @Post
    @Path("/carregarGrid")
    public void carregarGrid() {
	List<EstudoCota> estudosCota = new ArrayList<>();
	estudosCota.add(new EstudoCota());
	TableModel<CellModelKeyValue<EstudoCota>> tableModel = new TableModel<>();
	tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(estudosCota));
	tableModel.setTotal(estudosCota.size());
	result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    @Post
    @Path("/consultarCota")
    public void consultarCota(Integer numeroCota) {
	CotaDTO cotaDTO = new CotaDTO();
	Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
	cotaDTO.setIdCota(cota.getId());
	cotaDTO.setNomePessoa(cota.getPessoa().getNome());
	result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
    }
}
