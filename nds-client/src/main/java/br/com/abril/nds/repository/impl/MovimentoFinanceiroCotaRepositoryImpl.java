package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;

@Repository
public class MovimentoFinanceiroCotaRepositoryImpl extends AbstractRepository<MovimentoFinanceiroCota, Long> 
												   implements MovimentoFinanceiroCotaRepository {

	public MovimentoFinanceiroCotaRepositoryImpl() {
		super(MovimentoFinanceiroCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(Long idCota){
		
		StringBuilder hql = new StringBuilder("from MovimentoFinanceiroCota mfc, Distribuidor d ");
		hql.append(" where mfc.data = d.dataOperacao ")
		   .append(" and mfc.status = :statusAprovado ");
		
		if (idCota != null){
			hql.append(" and mfc.cota = :idCota ");
		}
		
		hql.append(" order by mfc.cota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		if (idCota != null){
			query.setParameter("idCota", idCota);
		}
		
		return query.list();
	}

	/**
	 * @see br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository#obterMovimentosFinanceiroCota()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" from MovimentoFinanceiroCota movimentoFinanceiroCota ");

		String conditions = "";

		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {

			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.tipoMovimento.id = :idTipoMovimento ";
		}

		if (filtroDebitoCreditoDTO.getDataLancamentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataLancamentoFim() != null) {
			
			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.dataCriacao between :dataLancamentoInicio and :dataLancamentoFim ";
		}
		
		if (filtroDebitoCreditoDTO.getDataVencimentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataVencimentoFim() != null) {
			
			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.data between :dataVencimentoInicio and :dataVencimentoFim ";
		}

		if (filtroDebitoCreditoDTO.getNumeroCota() != null) {

			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.cota.numeroCota = :numeroCota ";
		}
		
		hql.append(conditions);

		Query query = getSession().createQuery(hql.toString());

		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {

			query.setParameter("idTipoMovimento", filtroDebitoCreditoDTO.getIdTipoMovimento());
		}

		if (filtroDebitoCreditoDTO.getDataLancamentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataLancamentoFim() != null) {

			query.setParameter("dataLancamentoInicio", filtroDebitoCreditoDTO.getDataLancamentoInicio());
			query.setParameter("dataLancamentoFim", filtroDebitoCreditoDTO.getDataLancamentoFim());
		}
		
		if (filtroDebitoCreditoDTO.getDataVencimentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataVencimentoFim() != null) {
			
			query.setParameter("dataVencimentoInicio", filtroDebitoCreditoDTO.getDataVencimentoInicio());
			query.setParameter("dataVencimentoFim", filtroDebitoCreditoDTO.getDataVencimentoFim());
		}

		if (filtroDebitoCreditoDTO.getNumeroCota() != null) {

			query.setParameter("numeroCota", filtroDebitoCreditoDTO.getNumeroCota());
		}

		return query.list();
	}
}