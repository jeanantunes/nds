package br.com.abril.nds.controllers.nfe;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.NFEImportUtil;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

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
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Path("/")
	public void index(){	
	}

	
	
	@Post("/pesquisarArquivos.json")
	public void pesquisarArquivosDeRetorno(Date dataReferencia) {
		
		ParametroSistema pathNFEImportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		
		pathNFEImportacao.getValor();
		
		//TODO: 
		//chamar rotina para ler arquivos
		//retornar dados pra pagina
	}
	
	
	@Post("/confirmar.json")
	public void confirmar() {

		//TODO: processar nota fiscal
		//notaFiscalService.processarRetornoNotaFiscal(chaveAcesso, retornoComunicacaoEletronica);
		
	}
	
}