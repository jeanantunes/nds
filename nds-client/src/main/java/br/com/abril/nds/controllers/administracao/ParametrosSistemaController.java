package br.com.abril.nds.controllers.administracao;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.UfEnum;
import br.com.abril.nds.util.Util;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
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
		
		// Obtém os dados do arquivo:
		InputStream imgLogotipo = null;
		String nomeImgLogotipo = "";
		if (imgLogoSistema != null) {
			imgLogotipo = imgLogoSistema.getFile();
			nomeImgLogotipo = imgLogoSistema.getFileName();
		}
		
		//dto.setLogoSistema(nomeImgLogotipo);
		
		//
		this.validar(dto, imgLogoSistema);
		
		
		// Salvar:
		//psService.salvar(dto, imgLogotipo);
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
	private void validar(ParametroSistemaGeralDTO dto, UploadedFile imgLogoSistema) 
				throws ValidacaoException {
		
		// TODO: validar imagem
		if (imgLogoSistema == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Escolha um arquivo para ser enviado!");
		}
		String fileName = imgLogoSistema.getFileName().toLowerCase();
		if (!fileName.matches("([A-Za-z0-9]|_|.|-)+\\.(png|gif)")) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione um formato de arquivo 'gif' ou 'png'!");
		}
		
		// TODO: validar CNPJ
		CNPJValidator vld = new CNPJValidator(true);
		for (ValidationMessage error : vld.invalidMessagesFor(dto.getCnpj())) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, preencha com um CNPJ válido!");
		}
		
		// TODO: Validar email:
		if (!Util.validarEmail(dto.getEmail())) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, preencha com um email válido!");
		}
		
		// TODO: Validar UF:
		try {
			UfEnum.valueOf(dto.getUf().toUpperCase());
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Unidade Federativa válida!");
		}
	}
	
	public Download getImageLogotipo() {		
		/*
		byte[] buff = cotaGarantiaService.getImageCheque(idCheque);
		
		if (buff == null) {
			buff = new byte[0];
		}
		
		return new ByteArrayDownload(buff, "image/jpeg", "cheque.jpg");
		*/
		return null;
	}
	
}
