package br.com.abril.nds.controllers.nfe;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;


@Resource
@Path(value="/nfe/certificadoNFE")
@Rules(Permissao.ROLE_NFE_CERTICADO_NFE)
public class CertificadoNFEController extends BaseController {
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private Result result;
		
	@Autowired
	private HttpSession session;
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_CERTICADO_NFE)
	public void index() {	
		
	} 
	
	@Post
	@Transactional
	public void confirmar(CertificadoNFEDTO filtro) {
		
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Upload do certificado efetuado com sucesso."), "result").recursive().serialize();
	}
}