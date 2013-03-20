package br.com.abril.nds.controllers.distribuicao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao")
public class DistribuicaoManualController extends BaseController {

    @Autowired
    private Result result;
    
    @Path("/distribuicaoManual")
    @Rules(Permissao.ROLE_DISTRIBUICAO_MANUAL)
    public void index(Produto produto) {
	System.out.println("teste >>>>>>>> "+ produto.getCodigo());
    }
}
