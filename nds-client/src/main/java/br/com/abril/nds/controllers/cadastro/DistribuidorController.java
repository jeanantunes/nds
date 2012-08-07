package br.com.abril.nds.controllers.cadastro;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pelo Distribuidor.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/distribuidor")
public class DistribuidorController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Get
	@Path("/obterNumeroSemana")
	public void obterNumeroSemana(Date data) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Integer numeroSemana = 
			DateUtil.obterNumeroSemanaNoAno(data, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		result.use(Results.json()).from(numeroSemana).serialize();
	}
	
	@Get
	@Path("/obterDataDaSemana")
	public void obterDataDaSemanaNoAno(Integer numeroSemana) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Date data = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), null);
		
		String dataFormatada = "";
		
		if (data != null) {
			
			dataFormatada = DateUtil.formatarDataPTBR(data);
		}
		
		result.use(Results.json()).from(dataFormatada, "result").serialize();
	}
	
}
