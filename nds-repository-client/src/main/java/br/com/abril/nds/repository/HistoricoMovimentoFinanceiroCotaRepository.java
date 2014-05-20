package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;

public interface HistoricoMovimentoFinanceiroCotaRepository extends Repository<HistoricoMovimentoFinanceiroCota, Long> {

	public abstract void removeByIdConsolidadoAndGrupos(Long idConsolidado,
			List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceiros);
	
}
