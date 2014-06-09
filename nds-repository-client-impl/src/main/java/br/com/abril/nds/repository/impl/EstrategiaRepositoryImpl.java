package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;

@Repository
public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<Estrategia,Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
	super(Estrategia.class);
    }
  
    @Override
    public Estrategia buscarPorProdutoEdicao(ProdutoEdicao produtoEdicao) {
	Criteria criteria = getSession().createCriteria(Estrategia.class);
	criteria.add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
	return (Estrategia) criteria.uniqueResult();
    }
}
