package br.com.abril.nds.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.AnaliseParcialExportXLSDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.helper.LancamentoHelper;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;

public interface AnaliseParcialRepository {
	
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaClassificacaoCota(Long estudoId, Integer numeroCota, String classificacaoCota);
	void atualizaReparteCota(Long estudoId, Integer numeroCota, Long reparteSubtraido);
	void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido);
	void liberar(Long id);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo);
	List<EdicoesProdutosDTO> buscaHistoricoDeVendaParaCota(Long numeroCota, List<Long> listProdutoEdicaoId);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudoParcial(Long estudoId, Integer numeroPeriodoBase, boolean parcialComRedistribuicao);
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);
    public List<EdicoesProdutosDTO> carregarPublicacaoDoEstudo(Long estudoId);
    AnaliseParcialDTO buscarReparteDoEstudo(Long estudoOrigem, Integer numeroCota);
    Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo);
    void atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal);
    boolean verificarRedistribuicaoNoPeriodoParcial(final Long estudoId,final Integer numeroPeriodoBase);
    
    DetalhesEdicoesBasesAnaliseEstudoDTO buscarReparteVendaTotalPorEdicao(String codigoProduto, Long edicao, Long idTipoClassificacao, Integer numPeriodoParcial);
	List<DataLancamentoPeriodoEdicoesBasesDTO> obterDataDeLacmtoPeriodoParcial(Long idEstudo, Long idProdutoEdicao);
	public abstract Integer qtdBasesNoEstudo(Long idEstudo, boolean isBaseParcial);
	public abstract List<EdicoesProdutosDTO> carregarPeriodosAnterioresParcial(Long estudoId, Boolean isTrazerEdicoesAbertas);
	Map<Integer, List<EdicoesProdutosDTO>> buscaHistoricoDeVendaTodasCotas(List<Long> listCotaId, List<Long> listProdutoEdicaoId);
	Map<Integer, AnaliseParcialExportXLSDTO> buscarDadosPdvParaXLS(AnaliseParcialQueryDTO queryDTO);
	List<Long> obterCotasDentroDoPercentualReparteFiltro(List<Long> listCotaId, List<Long> listProdutoEdicaoId, AnaliseParcialQueryDTO queryDTO);
    
}
