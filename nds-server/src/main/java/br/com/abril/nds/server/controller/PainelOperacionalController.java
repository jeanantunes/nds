package br.com.abril.nds.server.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.server.model.DistribuidorServer;
import br.com.abril.nds.server.model.FormatoIndicador;
import br.com.abril.nds.server.model.GrupoIndicador;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.Status;
import br.com.abril.nds.server.model.StatusOperacao;
import br.com.abril.nds.server.model.TipoIndicador;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/painelOperacional")
public class PainelOperacionalController {

	private Result result;
	
	public PainelOperacionalController(Result result){
		
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		
		this.result.include("dataHora", new SimpleDateFormat("dd/MM/yyyy - hh:mm").format(new Date()));
		
		this.result.include("distribuidores", this.mock());
	}
	
	@Post
	public void atualizarPainel(){
		
	}
	
	private List<DistribuidorServer> mock(){
		
		List<DistribuidorServer> distribuidores = new ArrayList<DistribuidorServer>();
		
		DistribuidorServer distribuidorServer = new DistribuidorServer();
		distribuidorServer.setIdDistribuidorInterface(1L);
		distribuidorServer.setNome("teste 0001");
		StatusOperacao statusOperacao = new StatusOperacao();
		statusOperacao.setStatus(Status.FECHAMENTO);
		distribuidorServer.setStatusOperacao(statusOperacao);
		Endereco endereco = new Endereco();
		endereco.setUf("SP");
		distribuidorServer.setEndereco(endereco);
		
		List<Indicador> indicadores = new ArrayList<Indicador>();
		Indicador indicador = new Indicador();
		
		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setValor("abc");
		
		indicadores.add(indicador);
		
		indicador = new Indicador();
		
		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		
		indicadores.add(indicador);
		
		indicador = new Indicador();
		
		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		
		indicadores.add(indicador);
		
		distribuidorServer.setIndicadores(indicadores);
		
		distribuidores.add(distribuidorServer);
		
		return distribuidores;
	}
}