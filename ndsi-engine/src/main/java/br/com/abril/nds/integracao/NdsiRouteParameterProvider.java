package br.com.abril.nds.integracao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.RouteParameterProvider;
import br.com.abril.nds.integracao.model.NdsiParametroSistema;


@Component
public class NdsiRouteParameterProvider implements RouteParameterProvider {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParameters() {
		Query query = entityManager.createNamedQuery("findAllNdsiParameters");
		
		query.setParameter("prefix", "NDSI_%");
		
		List<NdsiParametroSistema> result = query.getResultList();
		
		Map<String, Object> parameters = new HashMap<String, Object>(result.size());
		
		for (NdsiParametroSistema parametroSistema : result) {
			parameters.put(parametroSistema.getTipoParametroSistema(), parametroSistema.getValor());
		}
		
		return parameters;
	}
}