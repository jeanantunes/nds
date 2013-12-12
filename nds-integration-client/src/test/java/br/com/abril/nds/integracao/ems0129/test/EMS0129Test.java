package br.com.abril.nds.integracao.ems0129.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.InterfacePickingDTO;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Header;
import br.com.abril.nds.integracao.ems0129.processor.EMS0129MessageProcessor;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0129Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0129Route route;
	
	@Autowired
	private EMS0129MessageProcessor messageProcessor;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

	@Override
	public void test() {
		
		Calendar calendar = new GregorianCalendar(2013, 9, 9);
		
		route.execute("TestUnitarioUser", calendar.getTime(), "6248116");

	}
	
	@Test
	public void testCreateHeaderModelo01() {
		List<EMS0129Picking1Header> headers = this.messageProcessor.criarHeaderModelo1(new GregorianCalendar(2013, 9, 9).getTime());
		
		Assert.assertNotNull(headers);
	}
	
	@Test
	public void testGetDetalhesPicking() {
		Date data = new GregorianCalendar(2013, 9, 9).getTime();
		List<EMS0129Picking1Header> headers = this.messageProcessor.criarHeaderModelo1(data);

		Assert.assertNotNull(headers);
		
		Assert.assertTrue(!headers.isEmpty());
		
		EMS0129Picking1Header header = headers.get(0);
		
		List<InterfacePickingDTO> detalhes = this.messageProcessor.getDetalhesPicking(header.getIdCota(), data);
	
		Assert.assertNotNull(detalhes);
	}
}
