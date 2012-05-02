package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TelefoneTransportadorRepositoty;

@Repository
public class TelefoneTransportadorRepositotyImpl extends
		AbstractRepository<Transportador, Long> implements
		TelefoneTransportadorRepositoty {

	public TelefoneTransportadorRepositotyImpl() {
		super(Transportador.class);
	}

	@Override
	public Telefone pesquisarTelefonePrincipalTransportador(Long idTransportador) {
		
		Criteria criteria = this.getSession().createCriteria(TelefoneTransportador.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));
		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);
		
		TelefoneTransportador telefoneTransportador = (TelefoneTransportador) criteria.uniqueResult();
		
		if (telefoneTransportador == null){
			
			this.getSession().createCriteria(TelefoneTransportador.class);
			criteria.add(Restrictions.eq("transportador.id", idTransportador));
			criteria.setMaxResults(1);
			
			telefoneTransportador = (TelefoneTransportador) criteria.uniqueResult();
		}
		
		return telefoneTransportador == null ? null : telefoneTransportador.getTelefone();
	}
}