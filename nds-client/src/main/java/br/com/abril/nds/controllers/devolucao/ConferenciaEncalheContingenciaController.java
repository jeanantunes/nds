package br.com.abril.nds.controllers.devolucao;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.log.LogFuncional;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path(value="/devolucao/conferenciaEncalheContingencia")
public class ConferenciaEncalheContingenciaController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConferenciaEncalheContingenciaController.class);	
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private Result result;
	
	@Path("/")
	@LogFuncional(value="Conferência de Encalhe Contingência [Abertura da tela]")
	public void index(){
		LOGGER.warn("USUARIO ABRINDO TELA DE CONFERENCIA ENCALHE CONTINGENCIA usuario="+usuarioService.getUsuarioLogado().getLogin()+
				" sessionid="+session.getId());
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(dataOperacao));
		
		TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
	}
}