package br.com.abril.nds.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.server.model.Distribuidor;
import br.com.abril.nds.server.service.PainelOperacionalService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
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
		
		List<Distribuidor> distribuidores = this.painelOperacionalService.buscarIndicadoresPorDistribuidor();
		
		this.obterEstadosPresentes(distribuidores);
		
		this.result.include("distribuidores", distribuidores);
		
		//this.result.include("distribuidores", mock());
	}
	
	@Post
	public void atualizarPainel(){
		
	}
	
//	public List<Distribuidor> mock(){
//		
//		List<Distribuidor> lista = new ArrayList<Distribuidor>();
//		
//		Distribuidor distribuidorServer = new Distribuidor();
//		distribuidorServer.setIdDistribuidorInterface(1L);
//		distribuidorServer.setNome("teste 0001");
//		StatusOperacao statusOperacao = new StatusOperacao();
//		statusOperacao.setStatus(Status.FECHAMENTO);
//		distribuidorServer.setStatusOperacao(statusOperacao);
//		
//		
//		
//		distribuidorServer.setUf("AP");
//
//		List<Indicador> indicadores = new ArrayList<Indicador>();
//		Indicador indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
//		indicador.setValor("abc");
//		indicador.setDistribuidor(distribuidorServer);
//		indicadores.add(indicador);
//		
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		indicadores.add(indicador);
//		
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		distribuidorServer.setIndicadores(indicadores);
//
//		lista.add(distribuidorServer);
//
//		// -------------------------------------//
//
//		distribuidorServer = new Distribuidor();
//		distribuidorServer.setIdDistribuidorInterface(2L);
//		distribuidorServer.setNome("distrib 2");
//		statusOperacao = new StatusOperacao();
//		statusOperacao.setStatus(Status.OPERANDO);
//		distribuidorServer.setStatusOperacao(statusOperacao);
//		
//		
//		
//		distribuidorServer.setUf("SP");
//
//		indicadores = new ArrayList<Indicador>();
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
//		indicador.setValor("abc");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
//		indicador.setId(2L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		distribuidorServer.setIndicadores(indicadores);
//
//		lista.add(distribuidorServer);
//
//		// -------------------------------------//
//
//		distribuidorServer = new Distribuidor();
//		distribuidorServer.setIdDistribuidorInterface(3L);
//		distribuidorServer.setNome("distrib 33");
//		statusOperacao = new StatusOperacao();
//		statusOperacao.setStatus(Status.ENCERRADO);
//		distribuidorServer.setStatusOperacao(statusOperacao);
//		
//		
//		
//		distribuidorServer.setUf("SP");
//
//		indicadores = new ArrayList<Indicador>();
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
//		indicador.setValor("abc");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
//		indicador.setId(2L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		distribuidorServer.setIndicadores(indicadores);
//
//		lista.add(distribuidorServer);
//
//		// -------------------------------------//
//
//		distribuidorServer = new Distribuidor();
//		distribuidorServer.setIdDistribuidorInterface(4L);
//		distribuidorServer.setNome("la longe");
//		statusOperacao = new StatusOperacao();
//		statusOperacao.setStatus(Status.ENCERRADO);
//		distribuidorServer.setStatusOperacao(statusOperacao);
//		
//		
//		
//		distribuidorServer.setUf("SE");
//
//		indicadores = new ArrayList<Indicador>();
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
//		indicador.setValor("abc");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
//		indicador.setId(1L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		indicador = new Indicador();
//
//		indicador.setData(new Date());
//		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
//		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
//		indicador.setId(2L);
//		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
//		indicador.setValor("123");
//		indicador.setDistribuidor(distribuidorServer);
//		
//		indicadores.add(indicador);
//
//		distribuidorServer.setIndicadores(indicadores);
//		this.obterEstadosPresentes(lista);
//		return lista;
//	}
	
	private void obterEstadosPresentes(List<Distribuidor> distribuidores){
		
		Map<String, Object> estados = new HashMap<String, Object>();
		
		if (distribuidores != null){
			
			for (Distribuidor distribuidor : distribuidores){
				
				estados.put(distribuidor.getUf(), distribuidor.getUf());
			}
		}
		
		this.result.include("estados", estados);
	}
}