package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.repository.RotaRepository;

@Repository
public class RotaRepositoryImpl extends AbstractRepository<Rota, Long>
		implements RotaRepository {

	public RotaRepositoryImpl() {
		super(Rota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> buscarRotasRoteiro() {
		
		StringBuilder hql = new StringBuilder("select r ");
		hql.append(" from Rota r ")
		   .append(" where r.roteiro.id is not null ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(){
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RotaRoteiroDTO.class.getCanonicalName())
		   .append("(r.id, r.descricaoRota, r.roteiro.descricaoRoteiro, a.id)")
		   .append(" from AssociacaoVeiculoMotoristaRota a right join a.rota r ")
		   .append(" where r.roteiro.id is not null ")
		   .append(" and (a.rota.id = r.id or a.rota.id is null)");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
}