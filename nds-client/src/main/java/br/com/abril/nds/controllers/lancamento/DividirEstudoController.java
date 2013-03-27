package br.com.abril.nds.controllers.lancamento;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.estudocomplementar.BaseEstudoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/dividirEstudo")
public class DividirEstudoController extends BaseController {

    @Autowired
    private Result result;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private EstudoService estudoService;

    @Path("/index")
    public void index() {

	String data = DateUtil.formatarDataPTBR(new Date());
	result.include("data", data);
    }

    @Path("/confirmar")
    @Post
    public void confirmar(DivisaoEstudoDTO divisaoEstudo) {

	Estudo estudoOriginal = estudoService.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudo);

	if (estudoOriginal != null) {
	    
	    Estudo primeiroEstudo = new Estudo();
	    Estudo segundoEstudo = new Estudo();
	    
	    primeiroEstudo.setDataLancamento(estudoOriginal.getDataLancamento());
	    
//	    result.use(json()).from().serialize();
	    
	}
    }

}
