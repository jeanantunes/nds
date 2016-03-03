package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.StatusProcessoEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.SemaforoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/devolucao/semaforo")
@Rules(Permissao.ROLE_RECOLHIMENTO_SEMAFORO)
public class SemaforoController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private SemaforoService semaforoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Path("/")
	public void index() {
				
	}
	
	@Post
	@Path("/statusProcessosEncalhe")
	public void obterStatusProcessosEncalhe() {
		
		List<Semaforo> semaforosNaDataDeOperacao = 
			this.semaforoService.obterStatusProcessosEncalhe(
				this.distribuidorService.obterDataOperacaoDistribuidor());
		
		
		Long total = 
				this.semaforoService.obterTotalStatusProcessosEncalhe(
						this.distribuidorService.obterDataOperacaoDistribuidor());
			
		List<StatusProcessoEncalheVO> statusProcessosEncalhe = new ArrayList<>();
		
		if (semaforosNaDataDeOperacao != null) {
			
			for (Semaforo semaforo : semaforosNaDataDeOperacao) {
			
				statusProcessosEncalhe.add(new StatusProcessoEncalheVO(semaforo));
			}
		}
		
		
		this.result.use(FlexiGridJson.class).from(
			statusProcessosEncalhe).total(total.intValue()).page(1).serialize();
	}
	
}
