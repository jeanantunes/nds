package br.com.abril.nds.integracao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.RouteParameterProvider;
import br.com.abril.nds.model.cadastro.ParametroSistema;


@Component
public class NdsiRouteParameterProvider implements RouteParameterProvider {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParameters() {
		Query query = entityManager.createNativeQuery("select * from parametro_sistema where TIPO_PARAMETRO_SISTEMA like :prefix");
		
		query.setParameter("prefix", "NDSI_%");
		
		List<Object[]> result = query.getResultList();
		
		Map<String, Object> parameters = new HashMap<String, Object>(result.size());
		
		for (Object[] values : result) {
			parameters.put(values[1].toString(), values[2]);
		}
		
		return parameters;
	}
}