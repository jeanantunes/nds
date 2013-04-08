package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;

@Repository
public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<Estrategia,Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
	super(Estrategia.class);
    }
    
    @Override
    public Estrategia buscarPorProdutoEdicao(ProdutoEdicao produtoEdicao){
//    	Criteria criteria = getSession().createCriteria(Estrategia.class);
//    	criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao.getId()));
    	String hql = "select e from Estrategia e where e.produtoEdicao = " + produtoEdicao.getId();
    	Query query = getSession().createQuery(hql);
//    	query.setParameter("produtoEdicao", produtoEdicao.getId());
//    		List<Estrategia> result = criteria.list();
		List<Estrategia> result = query.list();
		for(Estrategia e : result){
			return e;
		}
		return null;
    	
    }
    
}
