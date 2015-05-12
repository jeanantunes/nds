package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;

public interface FechamentoCEIntegracaoRepository {
	
	List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);

	FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	BigInteger countItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
	
	FechamentoCEIntegracaoConsolidadoDTO obterConsolidadoCEIntegracao(Long idChamadaEncalheForncecdor);
	
	List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracaoComDiferenca(FiltroFechamentoCEIntegracaoDTO filtro) ;

	BigInteger countItensFechamentoSemCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro);
}
