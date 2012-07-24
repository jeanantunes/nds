package br.com.abril.nds.server.fixture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.server.model.Distribuidor;
import br.com.abril.nds.server.model.FormatoIndicador;
import br.com.abril.nds.server.model.GrupoIndicador;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.Status;
import br.com.abril.nds.server.model.StatusOperacao;
import br.com.abril.nds.server.model.TipoIndicador;

public class DataLoader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext-dataLoader.xml");
		SessionFactory sf = null;
		Session session = null;
		Transaction tx = null;
		
		try {
			sf = ctx.getBean(SessionFactory.class);
			session = sf.openSession();
			tx = session.beginTransaction();
			
			mock(session);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			tx.commit();
			
			if (session != null) {
				session.close();
			}
			if (sf != null) {
				sf.close();
			}
		}
	}

	private static void mock(Session session) {

		Distribuidor distribuidorServer = new Distribuidor();
		distribuidorServer.setIdDistribuidorInterface(1L);
		distribuidorServer.setNome("teste 0001");
		StatusOperacao statusOperacao = new StatusOperacao();
		statusOperacao.setStatus(Status.FECHAMENTO);
		distribuidorServer.setStatusOperacao(statusOperacao);
		
		session.save(statusOperacao);
		
		distribuidorServer.setUf("AP");

		List<Indicador> indicadores = new ArrayList<Indicador>();
		Indicador indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.DATA);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setValor(new SimpleDateFormat(FormatoIndicador.DATA.getFormato()).format(new Date()));
		indicador.setDistribuidor(distribuidorServer);
		indicadores.add(indicador);
		session.save(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("R$ 123.987");
		indicador.setDistribuidor(distribuidorServer);
		indicadores.add(indicador);
		session.save(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		distribuidorServer.setIndicadores(indicadores);

		session.save(distribuidorServer);

		// -------------------------------------//

		distribuidorServer = new Distribuidor();
		distribuidorServer.setIdDistribuidorInterface(2L);
		distribuidorServer.setNome("distrib 2");
		statusOperacao = new StatusOperacao();
		statusOperacao.setStatus(Status.OPERANDO);
		distribuidorServer.setStatusOperacao(statusOperacao);
		
		session.save(statusOperacao);
		
		distribuidorServer.setUf("SP");

		indicadores = new ArrayList<Indicador>();
		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setValor("abc");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setId(2L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		distribuidorServer.setIndicadores(indicadores);

		session.save(distribuidorServer);

		// -------------------------------------//

		distribuidorServer = new Distribuidor();
		distribuidorServer.setIdDistribuidorInterface(3L);
		distribuidorServer.setNome("distrib 33");
		statusOperacao = new StatusOperacao();
		statusOperacao.setStatus(Status.ENCERRADO);
		distribuidorServer.setStatusOperacao(statusOperacao);
		
		session.save(statusOperacao);
		
		distribuidorServer.setUf("SP");

		indicadores = new ArrayList<Indicador>();
		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setValor("abc");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setId(2L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		distribuidorServer.setIndicadores(indicadores);

		session.save(distribuidorServer);

		// -------------------------------------//

		distribuidorServer = new Distribuidor();
		distribuidorServer.setIdDistribuidorInterface(4L);
		distribuidorServer.setNome("la longe");
		statusOperacao = new StatusOperacao();
		statusOperacao.setStatus(Status.ENCERRADO);
		distribuidorServer.setStatusOperacao(statusOperacao);
		
		session.save(statusOperacao);
		
		distribuidorServer.setUf("SE");

		indicadores = new ArrayList<Indicador>();
		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setValor("abc");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setId(1L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		indicador = new Indicador();

		indicador.setData(new Date());
		indicador.setFormatoIndicador(FormatoIndicador.TEXTO);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setId(2L);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setValor("123");
		indicador.setDistribuidor(distribuidorServer);
		session.save(indicador);
		indicadores.add(indicador);

		distribuidorServer.setIndicadores(indicadores);

		session.save(distribuidorServer);
	}
}