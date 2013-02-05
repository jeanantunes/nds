package br.com.abril.nds.integracao.ems0108.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0108.route.EMS0108Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0108Test extends RouteTestTemplate {
	@Autowired
	private EMS0108Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		// List<NdsiParametroSistema> list = entityManager.createQuery("select o from NdsiParametroSistema o where o.id = ?").setParameter(1, 1L).getResultList();
		// System.out.println(list);
		return rota;
	}

}
