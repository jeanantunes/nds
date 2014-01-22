package br.com.abril.nds.controllers.devolucao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path(value="/devolucao/conferenciaEncalheContingencia")
public class ConferenciaEncalheContingenciaController extends BaseController {

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;

	
	@Autowired
	private Result result;
	
	@Path("/")
	public void index(){
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(dataOperacao));
		
		TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
	}
}