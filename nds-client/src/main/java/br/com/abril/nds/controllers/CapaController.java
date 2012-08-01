package br.com.abril.nds.controllers;

import java.io.IOException;
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
	
	private final static String JPEG_CONTENT_TYPE = "image/jpeg";
	
	private final static int TAMANHO_MAXIMO =  20 * 1024;
	
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
			
			return null;
		}
		
	}	
	
	@Post("salvaCapa")
	public void salvaCapa(Long idProdutoEdicao, UploadedFile image) throws IOException{
		
		String contentType = image.getContentType();
		
		if(!JPEG_CONTENT_TYPE.equalsIgnoreCase(contentType)){
			result.use(CustomJson.class).from(new ValidacaoVO(TipoMensagem.WARNING, "Apenas Imagens do tipo JPEG")).serialize();
			return;
		}
		
		InputStream inputStream = image.getFile();
		int tamanho = inputStream.available();
		if(TAMANHO_MAXIMO < tamanho){
			result.use(CustomJson.class).from(new ValidacaoVO(TipoMensagem.WARNING, "Apenas Imagens de at&eacute; 20 kbytes")).serialize();
			return;
		}
		capaService.saveCapa(idProdutoEdicao,contentType, inputStream);	
		
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
