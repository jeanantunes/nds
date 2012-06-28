package br.com.abril.nds.controllers.administracao;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.UfEnum;
import br.com.abril.nds.util.Util;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

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
	public void salvar(ParametroSistemaGeralDTO dto, UploadedFile imgLogoSistema) {
		
		// WORKAROUND: para tratar o checkbox (no cenário que ele foi desmarcado):
		dto.setNfeDpec(dto.getNfeDpec());
		
		//
		this.validarParametrosSistema(dto, imgLogoSistema);
		
		// Obtém os dados do arquivo:
		InputStream imgLogotipo = null;
		String contentType = "";
		if (imgLogoSistema != null) {
			imgLogotipo = imgLogoSistema.getFile();
			contentType = imgLogoSistema.getContentType();
		}
		
		// Salvar:
		psService.salvar(dto, imgLogotipo, contentType);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetros salvos com sucesso."), "result").recursive().serialize();
	}
	
	
	/**
	 * Realiza as validações dos campos da tela.
	 *  
	 * @param dto
	 * @param imgLogoSistema
	 * 
	 * @exception
	 */
	private void validarParametrosSistema(ParametroSistemaGeralDTO dto, UploadedFile imgLogoSistema) 
				throws ValidacaoException {
		
		// Validar imagem:
		if (imgLogoSistema != null) {
			String contentType = imgLogoSistema.getContentType().toLowerCase();
			if (!contentType.matches("image/(gif|png)")) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione um formato de arquivo 'gif' ou 'png'!");
			}
		}
		
		// Validar CNPJ
		CNPJValidator vld = new CNPJValidator(true);
		if (!vld.invalidMessagesFor(dto.getCnpj()).isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, preencha com um CNPJ válido!");
		}
		
		// Validar email:
		if (!Util.validarEmail(dto.getEmail())) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, preencha com um email válido!");
		}
		
		// Validar UF:
		try {
			UfEnum.valueOf(dto.getUf().toUpperCase());
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Unidade Federativa válida!");
		}
	}
	
	/**
	 * Obtém o logotipo do distribuidor, caso exista.
	 * 
	 * @return
	 */
	public Download getImageLogotipo() {
		
		try {
			InputStream inputStream = psService.getLogotipoDistribuidor();
			return new InputStreamDownload(inputStream, null, null);
		} catch (Exception e) {
			result.use(CustomJson.class).from(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage())).serialize();
		}

		return null;
	}
	
}
