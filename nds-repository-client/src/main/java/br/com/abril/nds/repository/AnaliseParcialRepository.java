package br.com.abril.nds.repository;

import br.com.abril.nds.dto.*;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;

import java.util.List;

public interface AnaliseParcialRepository {
	
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaClassificacaoCota(Long estudoId, Long numeroCota);
	void atualizaReparteCota(Long estudoId, Long numeroCota, Long reparteSubtraido);
	void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido);
	void liberar(Long id);
	AnaliseEstudoDetalhesDTO buscarDetalhesAnalise(Long produtoEdicao);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo);
	List<EdicoesProdutosDTO> buscaHistoricoDeVendaParaCota(Long numeroCota, List<Long> listProdutoEdicaoId);
	List<EdicoesProdutosDTO> getEdicoesBaseParciais(Long numeroCota, Long numeroEdicao, String codigoProduto, Long periodo);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId);
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);
    AnaliseEstudoDetalhesDTO historicoEdicaoBase(Long id, Integer numeroPeriodo);
    public List<EdicoesProdutosDTO> carregarPublicacaoDoEstudo(Long estudoId);
    AnaliseParcialDTO buscarReparteDoEstudo(Long estudoOrigem, Integer numeroCota);
    Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo);
}
