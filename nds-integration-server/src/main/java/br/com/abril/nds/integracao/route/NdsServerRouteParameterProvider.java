package br.com.abril.nds.integracao.route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.AbstractRepository;


@Component
public class NdsServerRouteParameterProvider extends AbstractRepository implements RouteParameterProvider {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> getParameters() {
		Query query = getSession().createSQLQuery("select * from PARAMETRO_SISTEMA");
		
		List<Object[]> result = query.list();
		
		Map<String, Object> parameters = new HashMap<String, Object>(result.size());
		
		for (Object[] values : result) {
			parameters.put(values[1].toString(), values[2]);
		}
		
		return parameters;
	}
}