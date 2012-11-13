package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.repository.LocalidadeRepository;

@Repository
public class LocalidadeRepositoryImpl extends
AbstractRepositoryModel<Localidade, String> implements LocalidadeRepository {

	public LocalidadeRepositoryImpl() {
		super(Localidade.class);
	}

	
	public List<Localidade> obterListaLocalidadeCotas() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select distinct(localidade) ");
		
		hql.append(" from Localidade localidade, Cota cota 				");
		
		hql.append(" inner join cota.enderecos as enderecoCota			");
		
		hql.append(" inner join enderecoCota.endereco as endereco		");
		
		hql.append(" where enderecoCota.principal = :indPrincipal and 	");
		
		hql.append(" endereco.codigoCidadeIBGE = localidade.codigoMunicipioIBGE	");
				
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("indPrincipal", true);
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Localidade> pesquisarLocalidades(String nomeLocalidade) {
		
		Query query = 
				this.getSession().createQuery("select l from Localidade l where l.nome like :nome ");
		query.setParameter("nome", "%" + nomeLocalidade + "%");
		
		return query.list();
	}
}
