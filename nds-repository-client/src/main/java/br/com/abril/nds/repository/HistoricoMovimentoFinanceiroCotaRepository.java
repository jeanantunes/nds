package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;

public interface HistoricoMovimentoFinanceiroCotaRepository extends Repository<HistoricoMovimentoFinanceiroCota, Long> {

	public abstract void removeByIdConsolidadoAndGrupos(Long idConsolidado,
			List<String> grupoMovimentoFinaceiros);

    public abstract void removeByCotaAndDataOpAndGrupos(Long idCota, Date dataOperacao,
            List<String> grupoMovimentoFinaceiros);
	
}
