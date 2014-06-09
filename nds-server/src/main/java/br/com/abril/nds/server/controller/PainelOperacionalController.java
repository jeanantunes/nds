package br.com.abril.nds.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.service.PainelOperacionalService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/painelOperacional")
public class PainelOperacionalController {

	@Autowired
	private PainelOperacionalService painelOperacionalService;
	
	private Result result;
	
	public PainelOperacionalController(Result result){
		
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		
		this.result.include("dataHora", new SimpleDateFormat("dd/MM/yyyy - hh:mm").format(new Date()));
		
		List<OperacaoDistribuidor> distribuidores = this.painelOperacionalService.buscarIndicadoresPorDistribuidor();
		
		this.obterEstadosPresentes(distribuidores);
		
		this.result.include("distribuidores", distribuidores);
	}
	
	private void obterEstadosPresentes(List<OperacaoDistribuidor> distribuidores){
		
		Map<String, Object> estados = new HashMap<String, Object>();
		
		if (distribuidores != null){
			
			for (OperacaoDistribuidor distribuidor : distribuidores){
				
				estados.put(distribuidor.getUf(), distribuidor.getUf());
			}
		}
		
		this.result.include("estados", estados);
	}
	
}