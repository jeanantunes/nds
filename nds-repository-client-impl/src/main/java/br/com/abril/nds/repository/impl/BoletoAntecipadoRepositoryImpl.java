package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BoletoAntecipadoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.BoletoAntecipado}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoletoAntecipadoRepositoryImpl extends AbstractRepositoryModel<BoletoAntecipado,Long> implements BoletoAntecipadoRepository {

	/**
	 * Construtor padrão
	 */
	public BoletoAntecipadoRepositoryImpl() {
		
		super(BoletoAntecipado.class);
	}

	/**
	 * Obtem BoletoAntecipado por ChamadaEncalheCota
	 * @param idCE
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	@Override
	public BoletoAntecipado obterBoletoAntecipadoPorCECota(Long idCE, List<StatusDivida> listaStatus) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" Select ba from BoletoAntecipado ba ");
		
		hql.append(" where ba.chamadaEncalheCota.id = :idCE ");
		
		hql.append(" and ba.status in (:listaStatusDivida) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idCE", idCE);
		
		query.setParameterList("listaStatusDivida", listaStatus);
		
		return (BoletoAntecipado) query.uniqueResult();
	}

	/**
	 * Obtem BoletoAntecipado por ChamadaEncalheCota e Periodo de Recolhimento selecionado na Emissao
	 * @param idCE
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	@Override
	public BoletoAntecipado obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(Long idCE, 
																	           Date dataRecolhimentoCEDe, 
																	           Date dataRecolhimentoCEAte, 
																	           List<StatusDivida> listaStatus) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" Select ba from BoletoAntecipado ba ");
		
		hql.append(" where ba.chamadaEncalheCota.id = :idCE ");
		
		hql.append(" and ba.emissaoBoletoAntecipado.dataRecolhimentoCEDe = :dataRecolhimentoCEDe ");
		
		hql.append(" and ba.emissaoBoletoAntecipado.dataRecolhimentoCEAte = :dataRecolhimentoCEAte ");
		
		hql.append(" and ba.status in (:listaStatusDivida) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idCE", idCE);
		
        query.setParameter("dataRecolhimentoCEDe", dataRecolhimentoCEDe);
		
		query.setParameter("dataRecolhimentoCEAte", dataRecolhimentoCEAte);
		
		query.setParameterList("listaStatusDivida", listaStatus);
		
		return (BoletoAntecipado) query.uniqueResult();
	}
	
	/**
	 * Obtem BoletoAntecipado por Nosso Numero
	 * @param nossoNumero
	 * @return BoletoAntecipado
	 */
	@Override
	public BoletoAntecipado obterBoletoAntecipadoPorNossoNumero(String nossoNumero) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" Select ba from BoletoAntecipado ba ");
		
		hql.append(" where ba.nossoNumero = :nossoNumero ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("nossoNumero", nossoNumero);
		
		return (BoletoAntecipado) query.uniqueResult();
	}
	
	/**
	 * Obtem BoletoAntecipado por Data de Recolhimento e Cota
	 * @param idCota
	 * @param dataRecolhimento
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	@Override
	public BoletoAntecipado obterBoletoAntecipadoPorDataRecolhimentoECota(Long idCota, Date dataRecolhimento, List<StatusDivida> listaStatus) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" Select ba from BoletoAntecipado ba ");
		
		hql.append(" where ba.emissaoBoletoAntecipado.dataRecolhimentoCEDe <= :dataRecolhimento ");
		
		hql.append(" and ba.emissaoBoletoAntecipado.dataRecolhimentoCEAte >= :dataRecolhimento ");
		
		hql.append(" and ba.chamadaEncalheCota.cota.id = :idCota ");
		
		hql.append(" and ba.status in (:listaStatusDivida) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		query.setParameter("idCota", idCota);
		
		query.setParameterList("listaStatusDivida", listaStatus);
		
		return (BoletoAntecipado) query.uniqueResult();
	}
	
	/**
	 * Obtem Boletos Antecipados por Periodo Recolhimento CE e cota
	 * @param numeroCota
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @return List<BoletoAntecipado>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BoletoAntecipado> obterBoletosAntecipadosPorPeriodoRecolhimentoECota(Integer numeroCota, 
			                                                                         Date dataRecolhimentoCEDe, 
			                                                                         Date dataRecolhimentoCEAte) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" Select ba from BoletoAntecipado ba ");
		
		hql.append(" where ");
		
		hql.append(" ( ");
		
		hql.append("     ( ba.emissaoBoletoAntecipado.dataRecolhimentoCEDe between :dataRecolhimentoCEDe and :dataRecolhimentoCEAte ) ");
		
		hql.append("     OR ");
		
		hql.append("     ( ba.emissaoBoletoAntecipado.dataRecolhimentoCEAte between :dataRecolhimentoCEDe and :dataRecolhimentoCEAte ) ");
		
		hql.append(" ) ");
		
		hql.append(" and ba.chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoCEDe", dataRecolhimentoCEDe);
		
		query.setParameter("dataRecolhimentoCEAte", dataRecolhimentoCEAte);
		
		query.setParameter("numeroCota", numeroCota);
		
		return (List<BoletoAntecipado>) query.list();
	}
}
