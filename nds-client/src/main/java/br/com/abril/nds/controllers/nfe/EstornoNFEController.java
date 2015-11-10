package br.com.abril.nds.controllers.nfe;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EstornoNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/estornoNFE")
@Rules(Permissao.ROLE_NFE_ESTORNO_NFE)
public class EstornoNFEController extends BaseController {
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private Result result;
		
	@Autowired
	private HttpSession session;
    
	@Autowired
	private EstornoNFEService estornoNFEService;
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_ESTORNO_NFE)
	public void index() {	
		
	}
	
	@Post("/estornoNotaFiscal")
	@Rules(Permissao.ROLE_NFE_ESTORNO_NFE)
	public void estornoNotaFiscal(Long id) {
		
		this.estornoNFEService.estornoNotaFiscal(id);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Estorno Efetuado Com Sucesso."), "result").recursive().serialize();
	}

    @Post("pesquisar")
    @Rules(Permissao.ROLE_NFE_ESTORNO_NFE)
    public void pesquisar(FiltroMonitorNfeDTO filtro) {
        
    	Long quantidade =  this.estornoNFEService.quantidade(filtro);
    	
    	if(quantidade.intValue() == 0) {
    		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado");
    	}
    	
    	List<NotaFiscalDTO> notas = this.estornoNFEService.pesquisar(filtro);
         
    	result.use(FlexiGridJson.class).from(notas).total(quantidade.intValue()).page(1).serialize();
        
    }
}