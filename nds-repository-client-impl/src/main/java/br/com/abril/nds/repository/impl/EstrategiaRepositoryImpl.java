package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.apache.commons.lang.StringUtils.leftPad;

@Repository
public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<Estrategia, Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
        super(Estrategia.class);
    }

    @Override
    public Estrategia buscarPorProdutoEdicaoId(ProdutoEdicao produtoEdicao) {
        Criteria criteria = getSession().createCriteria(Estrategia.class);
        criteria.add(Restrictions.eq("produtoEdicao.id", produtoEdicao.getId()));
        return (Estrategia) criteria.uniqueResult();
    }

    @Override
    public Estrategia buscarPorCodigoProdutoNumeroEdicao(final String codigoProduto, final Long numeroEdicao) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select new Estrategia(es.id, pe, es.reparteMinimo, es.abrangencia, es.periodo, es.oportunidadeVenda) ");
        hql.append(" from Estrategia AS es ");
        //hql.append(" left join fetch es.basesEstrategia ");
        hql.append(" join es.produtoEdicao AS pe ");
        hql.append(" join pe.produto ");
        hql.append(" where pe.produto.codigo = :codigoProduto ");
        hql.append(" and   pe.numeroEdicao   = :numeroEdicao ");

        final Query query = super.getSession().createQuery(hql.toString());

        query.setParameter("codigoProduto",  leftPad(codigoProduto, 8, "0"));
        query.setParameter("numeroEdicao", numeroEdicao);
        query.setMaxResults(1);

        return (Estrategia) query.uniqueResult();
    }

    @Override
    public EdicaoBaseEstrategia buscarBasePorCodigoProdutoNumEdicao(final Long estrategiaID, final String codigoProduto, final Long numeroEdicao) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select new EdicaoBaseEstrategia(ede.id, pe, ede.peso, ede.periodoEdicao) ");
        hql.append(" from EdicaoBaseEstrategia AS ede ");
        hql.append(" join ede.produtoEdicao AS pe ");
        hql.append(" join pe.produto pr ");
        hql.append(" where ede.estrategia.id = :estrategiaID ");
        hql.append(" and   pr.codigo = :codigoProduto ");
        hql.append(" and   pe.numeroEdicao = :numeroEdicao ");

        final Query query = super.getSession().createQuery(hql.toString());

        query.setParameter("estrategiaID", estrategiaID);
        query.setParameter("codigoProduto", leftPad(codigoProduto, 8, "0"));
        query.setParameter("numeroEdicao", numeroEdicao);
        query.setMaxResults(1);

        return (EdicaoBaseEstrategia) query.uniqueResult();
    }
}