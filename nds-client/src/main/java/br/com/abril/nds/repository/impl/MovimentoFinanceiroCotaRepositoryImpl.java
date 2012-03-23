package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;

@Repository
public class MovimentoFinanceiroCotaRepositoryImpl extends AbstractRepository<MovimentoFinanceiroCota, Long> 
												   implements MovimentoFinanceiroCotaRepository {

	public MovimentoFinanceiroCotaRepositoryImpl() {
		super(MovimentoFinanceiroCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(){
		
		StringBuilder hql = new StringBuilder("from MovimentoFinanceiroCota mfc, Distribuidor d ");
		hql.append(" where mfc.dataInclusao = d.dataOperacao ")
		   .append(" order by mfc.cota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}

	@Override
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}