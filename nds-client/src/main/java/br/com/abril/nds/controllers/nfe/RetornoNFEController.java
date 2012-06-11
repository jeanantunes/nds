package br.com.abril.nds.controllers.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.SumarizacaoNotaRetornoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.FileImportUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlar as ações da pagina Retorno NFe.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/nfe/retornoNFe")
public class RetornoNFEController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
//	@Autowired
//	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private HttpSession session;
	
	private static final String LISTA_NOTAS_DE_RETORNO = "listaNotasDeRetorno";
	
	@Path("/")
	public void index() {	
	}

	@Post("/pesquisarArquivos.json")
	public void pesquisarArquivosDeRetorno(Date dataReferencia) {
		
		ParametroSistema pathNFEImportacao = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
				
		List<File> listaNotas = null;
		
		try {
		
			listaNotas = FileImportUtil.importArquivosModificadosEm( pathNFEImportacao.getValor(), dataReferencia, FileType.XML);
		
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O diretório parametrizado não é válido"));
		}
		
		if (listaNotas == null || listaNotas.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foi encontrado nenhuma nota para a data informada"));
		}
		
		Long totalArquivos = (long) listaNotas.size();
		Long notasRejeitadas = 0L;
		Long notasAprovadas = 0L;
		
		for (File nota : listaNotas) {
									
			//TODO: sumarizar notas;
		}
		
		
		//this.session.setAttribute(LISTA_NOTAS_DE_RETORNO, null);
		
		SumarizacaoNotaRetornoVO sumarizacao = new SumarizacaoNotaRetornoVO();
		
		sumarizacao.setNumeroTotalArquivos(totalArquivos);
		sumarizacao.setNumeroNotasRejeitadas(notasRejeitadas);
		sumarizacao.setNumeroNotasAprovadas(notasAprovadas);
		
		this.result.use(Results.json()).from(sumarizacao, "sumarizacao").serialize();
	}
	
	
	@Post("/confirmar.json")
	public void confirmar() {
		
		this.session.getAttribute(LISTA_NOTAS_DE_RETORNO);
		
		//this.notaFiscalService.processarRetornoNotaFiscal(id, retornoComunicacaoEletronica);
		
		this.result.use(Results.json()).from("OK").serialize();
	}
	
}