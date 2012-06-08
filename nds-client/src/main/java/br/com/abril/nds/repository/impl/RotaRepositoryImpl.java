package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RotaRepositoryImpl extends AbstractRepository<Rota, Long>
		implements RotaRepository {

	public RotaRepositoryImpl() {
		super(Rota.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(String sortname, String sortorder){
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RotaRoteiroDTO.class.getCanonicalName())
		   .append("(r.id, r.descricaoRota, r.roteiro.descricaoRoteiro, a.id)")
		   .append(" from AssociacaoVeiculoMotoristaRota a right join a.rota r ")
		   .append(" where r.roteiro.id is not null ")
		   .append(" and (a.rota.id = r.id or a.rota.id is null) ");
		
		if ("descricaoRota".equals(sortname)){
			
			hql.append(" order by r.descricaoRota ").append("asc".equals(sortorder) ? "asc" : "desc" );
		} else {
			
			hql.append(" order by r.roteiro.descricaoRoteiro ").append("asc".equals(sortorder) ? "asc" : "desc" );
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Rota> buscarRota(String sortname, Ordenacao ordenacao){
		
		Criteria criteria =  getSession().createCriteria(Rota.class);
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(sortname));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(sortname));
		}
		return criteria.list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Rota> buscarRotaPorRoteiro(Long roteiroId, String sortname, Ordenacao ordenacao ){
		
		Criteria criteria =  getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(sortname));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(sortname));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public void atualizaOrdenacao(Rota rota ){
		Criteria criteria =  getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro", rota.getRoteiro()));
		criteria.add(Restrictions.ge("ordem", rota.getOrdem()));
		criteria.add(Restrictions.ne("id", rota.getId()));
		criteria.addOrder(Order.asc("ordem"));
		List<Rota> rotas = criteria.list();
		Integer ordem = rota.getOrdem();
		for(Rota entity : rotas  ){
			ordem++;
			entity.setOrdem(ordem);
			merge(entity);

		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome , MatchMode matchMode) {
		Criteria criteria = getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		criteria.add(Restrictions.ilike("descricaoRota", rotaNome ,matchMode));
		return criteria.list();
	}

	@Override
	public List<Roteirizacao> buscarRoterizacaoPorRotaRoteiro(Long rotaId,
			Long roteiroId) {
		List<Roteirizacao> roteirizacao =  new ArrayList<Roteirizacao>();
		Criteria criteria = getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		criteria.add(Restrictions.eq("id", rotaId));
		Rota rota = (Rota)criteria.uniqueResult();
		if ( rota != null ){
			roteirizacao = rota.getRoteirizacao();
		}
		
		return roteirizacao;

			
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> buscarRotaDeBox(Long idBox) {
		
		String hql  = "select rota from Rota rota join rota.roteiro roteiro join roteiro.box box where box.id=:idBox group by rota ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("idBox", idBox);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> buscarRotaDeRoteiro(String descRoteiro) {
		
		String hql = " select rota from Rota rota where rota.roteiro.descricaoRoteiro=:descRoteiro ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("descRoteiro", descRoteiro);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Rota> obterRotasPorCota(Integer numeroCota){
		
		Criteria criteria =  getSession().createCriteria(Rota.class, "rota");
		criteria.createAlias("rota.roteirizacao","roteirizacao");
		criteria.createAlias("roteirizacao.pdv","pdv");
		criteria.createAlias("pdv.cota","cota");
		
		criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
		criteria.addOrder(Order.asc("rota.descricaoRota"));
		
		return criteria.list();
	}	
}