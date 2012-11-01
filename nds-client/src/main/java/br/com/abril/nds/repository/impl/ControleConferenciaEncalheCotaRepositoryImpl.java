package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;

@Repository
public class ControleConferenciaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ControleConferenciaEncalheCota, Long> implements ControleConferenciaEncalheCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public ControleConferenciaEncalheCotaRepositoryImpl() {
		super(ControleConferenciaEncalheCota.class);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository#obterControleConferenciaEncalheCota(java.lang.Integer, java.util.Date)
	 */
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(Integer numeroCota, Date dataOperacao) {
			
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select controleConferenciaEncalheCota  ");
		
		hql.append(" from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" controleConferenciaEncalheCota.cota.numeroCota = :numeroCota and ");
		
		hql.append(" controleConferenciaEncalheCota.dataOperacao = :dataOperacao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);

		query.setParameter("dataOperacao", dataOperacao);
		
		return (ControleConferenciaEncalheCota) query.uniqueResult();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ControleConferenciaEncalheCota> obterControleConferenciaEncalheCotaPorFiltro(FiltroConsultaEncalheDTO filtro) {
				
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" distinct conferencia.controleConferenciaEncalheCota");
		
		hql.append(" from ConferenciaEncalhe conferencia	");
		
		hql.append(" join conferencia.movimentoEstoqueCota movimentoEstoqueCota ");
		hql.append(" join movimentoEstoqueCota.produtoEdicao.produto.fornecedores fornecedor ");
		hql.append(" join conferencia.movimentoEstoqueCota.cota cota ");

		hql.append(" where	");
		
		hql.append(" movimentoEstoqueCota.data between :dataRecolhimentoInicial and :dataRecolhimentoFinal ");		

		if (filtro.getIdCota() != null) {
			hql.append(" and cota.id = :idCota ");		
		}
		
		if (filtro.getIdFornecedor() != null) {
			hql.append(" and fornecedor.id = :idFornecedor ");		
		}
		
		Query query =  this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		query.setParameter("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		
		if (filtro.getIdCota() != null) {
			query.setParameter("idCota", filtro.getIdCota());
		}

		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		return query.list();
	}
	
	
}
