package br.com.abril.nds.controllers;

import java.io.InputStream;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;


@Resource
@Path("/capa")
public class CapaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private CapaService capaService;
	
	
	
	@Get("/{idProdutoEdicao}")
	public Download obtemCapaProdutoEdicao(Long idProdutoEdicao){		
		try {
			InputStream inputStream = capaService.getCapaInputStream(idProdutoEdicao);
			return new InputStreamDownload(inputStream, null,null);
		} catch (Exception e) {			
			
			result.notFound();
			return null;
		}
		
	}	
	
	@Post("salvaCapa")
	public void salvaCapa(Long idProdutoEdicao, UploadedFile image){		
		capaService.saveCapa(idProdutoEdicao, image.getContentType(), image.getFile());	
		
		
		result.use(CustomJson.class).from("OK").serialize();
		
	}
	
	/**
	 * Exclui a capa da Edição.
	 *  
	 * @param idProdutoEdicao
	 */
	@Post("removerCapa")
	public void removerCapa(long idProdutoEdicao) {
		
		try {
			
			capaService.deleteCapa(idProdutoEdicao);
		} catch (Exception e) {
			ValidacaoVO vo = null;
			if (e instanceof ValidacaoException) {
				
				vo = ((ValidacaoException) e).getValidacao();
			} else {
				
				if (e instanceof NoResultException) {
					vo = new ValidacaoVO(TipoMensagem.ERROR, "Não há capa cadastrada!");
				} else {
					vo = new ValidacaoVO(TipoMensagem.ERROR, e.getMessage() + "");
				}
			}
			
			throw new ValidacaoException(vo);
		}
		
		
	}

}
