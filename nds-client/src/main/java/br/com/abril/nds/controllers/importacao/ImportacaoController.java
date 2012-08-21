package br.com.abril.nds.controllers.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TipoImportacaoArquivo;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

/**
 * 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/importacao")
public class ImportacaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private ImportacaoArquivoService importacaoArquivoService;
	
	/**
	 * 
	 */
	@Path("/")
	public void index() { 

		TipoImportacaoArquivo[] tipos = TipoImportacaoArquivo.values();
		
		this.result.include("tiposImportacao", tipos);
	}

	/**
	 * 
	 * @param arquivoImportacao
	 * @param tipoImportacaoArquivo
	 */
	@Post
	@Path("upload")
	public void upload(UploadedFile arquivoImportacao, TipoImportacaoArquivo tipoImportacaoArquivo) {

		validarParametros(arquivoImportacao, tipoImportacaoArquivo);
		
		File arquivo = new File(arquivoImportacao.getFileName());

		try {
			
			IOUtils.copyLarge(arquivoImportacao.getFile(), new FileOutputStream(arquivo));
			
		} catch (FileNotFoundException e) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não encontrado.");
			
		} catch (IOException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Problema ao ler arquivo.");
		}

		RetornoImportacaoArquivoVO retorno = importacaoArquivoService.processarImportacaoArquivo(arquivo, tipoImportacaoArquivo);
		
		tratarRetornoImportacao(retorno);
	}

	/**
	 * 
	 * @param arquivoImportacao
	 * @param tipoImportacaoArquivo
	 */
	private void validarParametros(UploadedFile arquivoImportacao, TipoImportacaoArquivo tipoImportacaoArquivo) {

		List<String> listaErros = new ArrayList<String>();
		
		if (arquivoImportacao == null) {
			
			listaErros.add("Campo [Arquivo] é obrigatório.");
		}
		
		if (tipoImportacaoArquivo == null) {
			
			listaErros.add("Campo [Tipo Importação] é obrigatório.");
		}
		
		if (!listaErros.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaErros);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * 
	 * @param retorno
	 */
	private void tratarRetornoImportacao(RetornoImportacaoArquivoVO retorno) {
	
		List<String> listaErros = new ArrayList<String>();
		
		if (retorno == null) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Ocorreu um erro na importação.");
			
			result.use(PlainJSONSerialization.class).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();
			
			return;
		}
		
		if (retorno.getMotivoDoErro() != null && retorno.getMotivoDoErro().length > 0 && !retorno.isSucessoNaImportacao())  {
			
			listaErros.add("Ocorreu um ou mais erros na linha: " + retorno.getNumeroDaLinha());

			listaErros.add("");
			
			listaErros.add("- Conteúdo da linha: "+retorno.getConteudoLinha());
			
			listaErros.add("");
			
			if(retorno.getMotivoDoErro().length > 0){
				
				listaErros.add(" Motivo(s) do erro: ");
				listaErros.add("");
			}
			
			for (String motivo : retorno.getMotivoDoErro()) {
				
				listaErros.add("- " + motivo);
			}
		}

		if (listaErros != null && !listaErros.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, listaErros);
			
			result.use(PlainJSONSerialization.class).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();
		
		} else {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Importação realizada com sucesso.");
			
			result.use(PlainJSONSerialization.class).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();
		}
	}
	
}
