package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AcumuloDividasRepository;

/**
 * Repositório de dados referentes ao acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class AcumuloDividasRepositoryImpl extends AbstractRepositoryModel<AcumuloDivida, Long> implements AcumuloDividasRepository {

	public AcumuloDividasRepositoryImpl() {
		super(AcumuloDivida.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorMovimentoFinanceiroPendente(Long idMovimentoFinanceiro) {
		
		return (AcumuloDivida) getSession()
				.createSQLQuery(" SELECT * FROM acumulo_divida WHERE MOV_PENDENTE_ID = :idMovimentoFinanceiro ")
				.addEntity(AcumuloDivida.class)
				.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro)
				.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AcumuloDivida> obterAcumuloDividaPorDivida(Long idDivida) {
		
		return getSession()
				.createSQLQuery(" select ad.* " 
								+ " from acumulo_divida ad "
								+ " join movimento_financeiro_cota mfc on mfc.ID = ad.MOV_PENDENTE_ID "
								+ " join CONSOLIDADO_MVTO_FINANCEIRO_COTA cmfc on mfc.ID = cmfc.MVTO_FINANCEIRO_COTA_ID "
								+ " join consolidado_financeiro_cota cfc on cmfc.CONSOLIDADO_FINANCEIRO_ID = cfc.ID "
								+ " join divida_consolidado _d_cons on _d_cons.CONSOLIDADO_ID = cfc.ID "
								+ " join divida d on d.ID = _d_cons.DIVIDA_ID "
								+ " join cobranca c on c.DIVIDA_ID = d.ID "
								+ " where d.ID=:idDivida ")
		
				.addEntity(AcumuloDivida.class)
				.setParameter("idDivida", idDivida)
				.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroMaximoAcumuloCota(Long idCota, Long idDivida) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select max(ad.numero_acumulo) from ACUMULO_DIVIDA ad ");
		hql.append(" inner join divida d on d.DIVIDA_RAIZ_ID = ad.divida_id	");
		hql.append(" inner join cota c on ad.COTA_ID = c.id ");
		hql.append(" where 1=1 ");
		hql.append(" and c.id = :idCota ");
		hql.append(" and d.id = :idDivida ");
		hql.append(" and ad.STATUS <> :statusInadimplencia ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());

		query.setParameter("idCota", idCota);
		query.setParameter("idDivida", idDivida);
		query.setParameter("statusInadimplencia", StatusInadimplencia.QUITADA.name());
		
		Integer retorno = (Integer) query.uniqueResult();
		
		return BigInteger.valueOf(retorno == null ? 0 : retorno);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroDeAcumulosDivida(Long idConsolidadoFinanceiroCota) {
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" select ad.numeroAcumulo ");
		jpql.append(" from AcumuloDivida ad ");
		jpql.append(" join ad.movimentoFinanceiroPendente movimentoFinanceiroPendente ");
		jpql.append(" join movimentoFinanceiroPendente.consolidadoFinanceiroCota consolidadoFinanceiroCota ");		
		jpql.append(" where consolidadoFinanceiroCota.id = :idConsolidadoFinanceiroCota ");
		
		Query query = this.getSession().createQuery(jpql.toString());

		query.setParameter("idConsolidadoFinanceiroCota", idConsolidadoFinanceiroCota);
		
		query.setMaxResults(1);
		
		return (BigInteger) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public List<AcumuloDivida> buscarDividasPendentesIndimplencia(final Date dataMovimento,
            final Cobranca cobranca) {
        
        final String hql = "select a from AcumuloDivida a " +
                " join a.dividaAnterior d " +
                " join d.cobranca c " +
                " join c.cota cota " +
                " join d.consolidados con " +
                " join con.movimentos mov " +
                " where d.status = :pendenteIndimplencia " +
                " and d.id != :idDivida " +
                " and c.id != :idCobranca " +
                " and d.data < :dataMovimento " +
                " and cota.id = :idCota " +
                " and d.data > " +
                "     (select min(_d.data) " +
                "      from Divida _d " +
                "      join _d.cota _c  " +
                "      where _d.status = :statusAberto " +
                "      and _c.id = cota.id) " +
                " group by d.id ";
        
        final Query query = this.getSession().createQuery(hql);
        query.setParameter("pendenteIndimplencia", StatusDivida.PENDENTE_INADIMPLENCIA);
        query.setParameter("idDivida", cobranca.getDivida().getId());
        query.setParameter("idCobranca", cobranca.getId());
        query.setParameter("dataMovimento", dataMovimento);
        query.setParameter("idCota", cobranca.getCota().getId());
        query.setParameter("statusAberto", StatusDivida.EM_ABERTO);
        
        return query.list();
    }
}
