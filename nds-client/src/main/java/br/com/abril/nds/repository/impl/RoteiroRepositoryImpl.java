package br.com.abril.nds.repository.impl;

import java.util.List;

import org.exolab.castor.xml.validators.IdValidator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RoteiroRepositoryImpl extends AbstractRepository<Roteiro, Long>
		implements RoteiroRepository {

	public RoteiroRepositoryImpl() {
		super(Roteiro.class);
	}

	@SuppressWarnings("unchecked")
	public List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao){
		
		Criteria criteria =  getSession().createCriteria(Roteiro.class);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(sortname));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(sortname));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Roteiro> buscarRoteiroPorDescricao(String descricao,  MatchMode matchMode ) {
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.ilike("descricaoRoteiro", descricao ,matchMode));
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public void atualizaOrdenacao(Roteiro roteiro ){
		Criteria criteria =  getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.ge("ordem", roteiro.getOrdem()));
		criteria.add(Restrictions.ne("id", roteiro.getId()));
		criteria.addOrder(Order.asc("ordem"));
		List<Roteiro> roteiros = criteria.list();
		Integer ordem = roteiro.getOrdem();
		for(Roteiro entity : roteiros  ){
			ordem++;
			entity.setOrdem(ordem);
			merge(entity);
			
		}
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> buscarRoteiroDeBox(Long idBox) {
		
		Criteria criteria  = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.eq("box.id", idBox));
		criteria.addOrder(Order.asc("descricaoRoteiro"));
		return  criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	

	@Override
	public Integer buscarMaiorOrdemRoteiro(){
		Criteria criteria  = getSession().createCriteria(Roteiro.class);
		criteria.setProjection(Projections.max("ordem"));  
		return (Integer) criteria.uniqueResult();  
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> buscarRoteiroEspecial() {
		Criteria criteria  = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.eq("tipoRoteiro", TipoRoteiro.ESPECIAL));
		criteria.addOrder(Order.asc("descricaoRoteiro"));
		return  criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	
}