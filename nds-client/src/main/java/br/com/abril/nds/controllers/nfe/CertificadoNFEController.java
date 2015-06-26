package br.com.abril.nds.controllers.nfe;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CertificadoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CerfiticadoService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
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
    
	@Autowired
	private CerfiticadoService cerfiticadoService;
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_CERTICADO_NFE)
	public void index() {	
		
	}
	
	@Post
    public void uploadCertificado(final UploadedFile uploadedFile) {
		
		try {
			
			String nomeArquivo = this.cerfiticadoService.upload(uploadedFile, TipoParametroSistema.NFE_PATH_CERTIFICADO);
			
			session.setAttribute("nomeArquivo", nomeArquivo);
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao realizar upload do certificado");
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Upload do certificado efetuado com sucesso."), "result").recursive().serialize();
	}
	
	@Post("/confirmar")
	public void confirmar(CertificadoNFEDTO filtro) {
		
		this.cerfiticadoService.confirmar(filtro, getUsuarioLogado().getId());
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Upload do certificado efetuado com sucesso."), "result").recursive().serialize();
	}

    @Post("buscar")
    public void busca(final CertificadoNFEDTO filtro) {
        
        final List<CertificadoNFEDTO> certificados = this.cerfiticadoService.obterCertificado(filtro);
        
        final Long quantidade = this.cerfiticadoService.quantidade(filtro);
        
        if(quantidade == 0) {
        	throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado!");
        }
        
        result.use(FlexiGridJson.class).from(certificados).total(quantidade.intValue()).page(1).serialize();
        
    }
	
	@Post("/obterCertificado")
	public void obterCertificado() {
		
		
	}
}