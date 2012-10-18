package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;

public interface ContasAPagarRepository {
	
	List<Date> buscarDatasLancamentoContasAPagar(FiltroContasAPagarDTO filtro);
	
	List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro,
			Date dataMovimento);

	BigDecimal pesquisaPorDistribuidorValorPorGrupoMovimento(
			Date dataMovimento, List<GrupoMovimentoEstoque> movimentosSuplementar);

	BigDecimal pesquisaPorDistribuidorFaltasSobras(Date dataMovimento, TipoDiferenca tipoDiferenca);

	BigDecimal pesquisaPorDistribuidorPerdasGanhos(Date dataMovimento,
			StatusAprovacao status);
}