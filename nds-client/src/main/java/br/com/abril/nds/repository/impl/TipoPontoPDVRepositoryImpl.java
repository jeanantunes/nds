package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.TipoPontoPDVRepository;

@Repository
public class TipoPontoPDVRepositoryImpl extends AbstractRepositoryModel<TipoPontoPDV, Long> implements TipoPontoPDVRepository {
	
	public TipoPontoPDVRepositoryImpl() {
		super(TipoPontoPDV.class);
	}
	
	@Override
	public List<TipoPontoPDV> buscarTodosPdvPrincipal() {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select tipoPontoPDV from ")
			.append(" PDV pdv join pdv.segmentacao.tipoPontoPDV tipoPontoPDV ")
			.append(" where pdv.caracteristicas.pontoPrincipal = true ")
			.append(" group by tipoPontoPDV.codigo ")
			.append(" order by tipoPontoPDV.descricao ");
		
		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
	}
}
