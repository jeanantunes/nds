package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;

@Repository
public class HistoricoMovimentoFinanceiroCotaRepositoryImpl 
										extends AbstractRepositoryModel<HistoricoMovimentoFinanceiroCota, Long> 
										implements HistoricoMovimentoFinanceiroCotaRepository {
	
	public HistoricoMovimentoFinanceiroCotaRepositoryImpl() {
		super(HistoricoMovimentoFinanceiroCota.class);
	}
	
	@Override
	public void removeByIdConsolidadoAndGrupos(Long idConsolidado, List<String> grupoMovimentoFinaceiros){
		
	  	final StringBuilder sql =  new StringBuilder();
    	sql.append("DELETE histo FROM HISTORICO_MOVTO_FINANCEIRO_COTA AS histo  ");
    	sql.append("join MOVIMENTO_FINANCEIRO_COTA movi on ");
    	sql.append("movi.id = histo.MOVTO_FINANCEIRO_COTA_ID ");
    	sql.append("join TIPO_MOVIMENTO tipo on ");
    	sql.append("movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' ");
    	sql.append("join CONSOLIDADO_MVTO_FINANCEIRO_COTA con on ");
    	sql.append("con.MVTO_FINANCEIRO_COTA_ID = movi.id ");

    	sql.append("where con.CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");
    	sql.append("and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros)");
    	
    	 this.getSession().createSQLQuery(sql.toString() )
	        .setParameter("idConsolidado", idConsolidado)
	        .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
	        .executeUpdate();
		
	}
}
