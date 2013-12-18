package br.com.abril.nds.integracao.ems0197.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DetalhesPickingDTO;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.ems0197.processor.EMS0197MessageProcessor;
import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0197Test extends RouteTestTemplate {
	@Autowired
	private EMS0197Route ems0197Route;
	
	@Autowired
	private EMS0197MessageProcessor messageProcessor;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0197Route;
	}
	
	
	@Override
	public void test() {
		Calendar data = Calendar.getInstance();
		data.set(2012, Calendar.MAY, 18);
		ems0197Route.execute("Jones", data.getTime(), "6248116");
	}

	@Test
	public void createHeaderTest() {
		List<EMS0197Header> headers = this.messageProcessor.criarHeader(new GregorianCalendar(2013, 9, 10).getTime());
		Assert.assertNotNull(headers);
	}
	
	@Test
	public void createDetalhesTest() {
		Date data =  new GregorianCalendar(2013, 9, 10).getTime();
		
		List<EMS0197Header> headers = this.messageProcessor.criarHeader(new GregorianCalendar(2013, 9, 10).getTime());
		
		Assert.assertNotNull(headers);
		Assert.assertTrue(!headers.isEmpty());
		
		EMS0197Header header = headers.get(0);
		
		List<DetalhesPickingDTO> detalhes = this.messageProcessor.getDetalhesPicking(header.getIdCota(), data);
		
		Assert.assertNotNull(detalhes);
		
	}
}
