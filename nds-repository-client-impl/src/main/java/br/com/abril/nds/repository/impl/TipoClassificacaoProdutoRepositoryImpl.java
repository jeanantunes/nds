package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;

@Repository
public class TipoClassificacaoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoClassificacaoProduto, Long> implements TipoClassificacaoProdutoRepository {

	public TipoClassificacaoProdutoRepositoryImpl() {
		super(TipoClassificacaoProduto.class);
	}
	
	
    @SuppressWarnings("unchecked")
    @Override
	public List<TipoClassificacaoProduto> obterTodos() {

        return getSession().createCriteria(TipoClassificacaoProduto.class).addOrder(Order.asc("id")).list();

    }
    @Override
    public TipoClassificacaoProduto obterPorClassificacao(String classificacao) {
        
        classificacao = classificacao.toUpperCase();
        
        Object obj = getSession().createCriteria(TipoClassificacaoProduto.class)
                .add(Restrictions.eq("descricao", classificacao))
                .setMaxResults(1)
                .uniqueResult();
        
         return obj == null ? null : (TipoClassificacaoProduto) obj;
    }

}
