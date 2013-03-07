package br.com.abril.nds.controllers.cadastro;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pelo Distribuidor.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/distribuidor")
public class DistribuidorController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService; 
	
	@Get
	@Path("/obterNumeroSemana")
	public void obterNumeroSemana(Date data) {
		
		DiaSemana diaSemana = this.distribuidorService.inicioSemana();
		
		if (diaSemana == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes: início semana");
		}
		
		Integer numeroSemana = 
			DateUtil.obterNumeroSemanaNoAno(data, diaSemana.getCodigoDiaSemana());
		
		result.use(Results.json()).from(numeroSemana).serialize();
	}
	
	@Get
	@Path("/obterDataDaSemana")
	public void obterDataDaSemanaNoAno(Integer numeroSemana) {
		
		DiaSemana diaSemana = this.distribuidorService.inicioSemana();
		
		if (diaSemana == null){
			
			throw new RuntimeException("Dados do distribuidor inexistentes: inicio semana");
		}
		
		Date data = 
			DateUtil.obterDataDaSemanaNoAno(numeroSemana, diaSemana.getCodigoDiaSemana(), null);
		
		String dataFormatada = "";
		
		if (data != null) {
			
			dataFormatada = DateUtil.formatarDataPTBR(data);
		}
		
		result.use(Results.json()).from(dataFormatada, "result").serialize();
	}
	
	@Get
	public Download logo(){		
		try {
			
			InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
			
			if(inputStream == null){
			  
				return new InputStreamDownload(new ByteArrayInputStream(new byte[0]), null, null);
			}
			
			return new InputStreamDownload(inputStream, null,null);
			
		} catch (Exception e) {			
			
			return new InputStreamDownload(new ByteArrayInputStream(new byte[0]), null, null);
		}
	}	
	
}
