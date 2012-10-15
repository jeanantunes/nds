package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;

public interface ChamadaEncalheRepository extends Repository<ChamadaEncalhe, Long> {
	
	ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
														 Date dataRecolhimento,
														 TipoChamadaEncalhe tipoChamadaEncalhe);
	
	List<ChamadaEncalhe> obterChamadasEncalhePor(Date dataOperacao, Long idCota);
		
	ChamadaEncalhe obterPorNumeroEdicaoEMaiorDataRecolhimento(ProdutoEdicao produtoEdicao,TipoChamadaEncalhe tipoChamadaEncalhe);
	
	List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro);

	List<CotaEmissaoDTO> obterDadosEmissaoImpressaoChamadasEncalhe(FiltroEmissaoCE filtro);

	List<ProdutoEmissaoDTO> obterProdutosEmissaoCE(FiltroEmissaoCE filtro, Long idCota);
	
	List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dataDe, Date dataAte);

	Date obterProximaDataEncalhe(Date base);

}
