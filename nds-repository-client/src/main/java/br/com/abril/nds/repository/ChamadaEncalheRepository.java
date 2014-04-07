package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.CotaProdutoEmissaoCEDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

public interface ChamadaEncalheRepository extends Repository<ChamadaEncalhe, Long> {
	
	ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
														 Date dataRecolhimento,
														 TipoChamadaEncalhe tipoChamadaEncalhe);
	
	public List<Long> obterIdsProdutoEdicaoNaMatrizRecolhimento(
			Date dataEncalhe, 
			List<Long> idsProdutoEdicao);
	
	List<ChamadaEncalhe> obterChamadasEncalhePor(Date dataOperacao, Long idCota);
		
	ChamadaEncalhe obterPorNumeroEdicaoEMaiorDataRecolhimento(ProdutoEdicao produtoEdicao,TipoChamadaEncalhe tipoChamadaEncalhe);
	
	List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro);

	List<CotaEmissaoDTO> obterDadosEmissaoImpressaoChamadasEncalhe(FiltroEmissaoCE filtro);

	List<ProdutoEmissaoDTO> obterProdutosEmissaoCE(
			FiltroEmissaoCE filtro, 
			Long idCota, 
			List<Date> datasControleFechamentoEncalhe, 
			List<Date> datasControleConferenciaEncalheCotaFinalizada);
	
	List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dataDe, Date dataAte);

	Date obterProximaDataEncalhe(Date base);

	List<BandeirasDTO> obterBandeirasNoIntervalo(
			Intervalo<Date> intervaloRecolhimento, TipoChamadaEncalhe tipoChamadaEncalhe, Long fornecedor, PaginacaoVO paginacaoVO);

	List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(
			Intervalo<Date> intervalo, Long fornecedor);

	Long countObterBandeirasNoIntervalo(Intervalo<Date> intervalo);

    Long countObterBandeirasNoIntervalo(Intervalo<Date> intervalo, TipoChamadaEncalhe tipoChamadaEncalhe, Long fornecedor);
	
	List<ChamadaEncalhe> obterChamadasEncalhe(ProdutoEdicao produtoEdicao, TipoChamadaEncalhe tipoChamadaEncalhe, Date dataRecolhimento);

	Integer obterMaiorSequenciaPorDia(Date dataRecolhimento);

	Set<Lancamento> obterLancamentos(Long idChamadaEncalhe);

	List<CotaProdutoEmissaoCEDTO> obterDecomposicaoReparteSuplementarRedistribuicao(FiltroEmissaoCE filtro);

    public abstract Date obterMaxDataRecolhimento(final TipoChamadaEncalhe tipoChamadaEncalhe);
	
}
