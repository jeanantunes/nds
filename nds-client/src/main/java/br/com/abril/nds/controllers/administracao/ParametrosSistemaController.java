package br.com.abril.nds.controllers.administracao;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.UfEnum;
import br.com.abril.nds.util.Util;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

@Resource
@Path("/administracao/parametrosSistema")
public class ParametrosSistemaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private ParametroSistemaService psService;
	
	
	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return  
	 */
	@Get
	@Path("/")
	public ParametroSistemaGeralDTO index() {
		
		result.include("listaUf", Util.getUfs(null));
		
		ParametroSistemaGeralDTO dto = psService.buscarParametroSistemaGeral();
		return dto;
	}
	
	
	/**
	 * Salva os parâmetros do sistema.
	 * 
	 * @param dto
	 * @param imgLogoSistema
	 */
	@Post
	@Path("/salvar")
	public void salvar(ParametroSistemaGeralDTO dto, UploadedFile imgLogoSistema) {
		
		// WORKAROUND: para tratar o checkbox (no cenário que ele foi desmarcado):
		dto.setNfeDpec(dto.getNfeDpec());
		
		// Obtém os dados do arquivo:
		InputStream imgLogotipo = null;
		String nomeImgLogotipo = "";
		if (imgLogoSistema != null) {
			imgLogotipo = imgLogoSistema.getFile();
			nomeImgLogotipo = imgLogoSistema.getFileName();
		}
		
		dto.setLogoSistema(nomeImgLogotipo);
		
		
		// TODO: validar imagem
		// TODO: validar CNPJ
		
		// TODO: Validar email:
		/*
		if (!Util.validarEmail(dto.getEmail())) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Formato de email é inválido!");
		}
		*/
		
		// TODO: Validar UF:
		/*
		boolean invalidUf = true;
		for (UfEnum uf : Util.getUfs(null)) {
			if (uf.getSigla().equals(dto.getUf())) {
				invalidUf = false;
				break;
			}
		}
		if (invalidUf) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Unidade Federativa é inválida!");
		}
		*/
		
		
		// Salvar:
		psService.salvar(dto, imgLogotipo);
		
		// deu erro:
		//result.forwardTo(this).index();
				
		// gravou certinho:
		result.redirectTo(this).index();
	}
	
}
