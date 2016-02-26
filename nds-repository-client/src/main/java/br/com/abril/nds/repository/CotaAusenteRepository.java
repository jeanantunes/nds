package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.OutraMovimentacaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.util.Intervalo;

public interface CotaAusenteRepository extends Repository<CotaAusente,Long> {
	
	 List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtro);
	 
	 Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro);
	 
	 CotaAusente obterCotaAusentePor(Long idCota, Date data);
	 
	 List<ProdutoEdicaoSuplementarDTO> obterDadosExclusaoCotaAusente(Long idCotaAusente);
	 
	 List<Long> obterIdsCotasAusentesNoPeriodo(Intervalo<Date> periodo);
	 
	 BigDecimal obterSaldoDeEntradaDoConsignadoDasCotasAusenteNoDistribuidor(final Date dataMovimentacao);
	 
	 BigDecimal obterSaldoDeSaidaDoConsignadoDasCotasAusenteNoDistribuidor(final Date dataMovimentacao);
	 
	 List<OutraMovimentacaoDTO> obterOutraMovimentacaoCotaAusente(final Date dataMovimentacao);
}
