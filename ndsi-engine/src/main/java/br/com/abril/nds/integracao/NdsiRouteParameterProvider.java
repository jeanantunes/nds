package br.com.abril.nds.integracao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.RouteParameterProvider;
import br.com.abril.nds.model.cadastro.ParametroSistema;


@Component
public class NdsiRouteParameterProvider implements RouteParameterProvider {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<String, Object> getParameters() {
		TypedQuery<ParametroSistema> query = entityManager.createQuery("SELECT p FROM ParametroSistema p", ParametroSistema.class);
		
//		query.setParameter("prefix", "NDSI_%");
		
		List<ParametroSistema> result = query.getResultList();
		
		Map<String, Object> parameters = new HashMap<String, Object>(result.size());
		
		for (ParametroSistema parametroSistema : result) {
			parameters.put(parametroSistema.getTipoParametroSistema().toString(), parametroSistema.getValor());
		}
		
		return parameters;
	}
}