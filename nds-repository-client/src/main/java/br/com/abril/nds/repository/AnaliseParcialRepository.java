package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;

public interface AnaliseParcialRepository {
	
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaClassificacaoCota(Long estudoId, Long numeroCota, String classificacaoCota);
	void atualizaReparteCota(Long estudoId, Long numeroCota, Long reparteSubtraido);
	void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido);
	void liberar(Long id);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo);
	List<EdicoesProdutosDTO> buscaHistoricoDeVendaParaCota(Long numeroCota, List<Long> listProdutoEdicaoId);
	List<EdicoesProdutosDTO> getEdicoesBaseParciais(Long numeroCota, Long numeroEdicao, String codigoProduto, Long periodo);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId, Date date);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudoParcial(Long estudoId, Integer numeroPeriodoBase);
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);
    public List<EdicoesProdutosDTO> carregarPublicacaoDoEstudo(Long estudoId);
    AnaliseParcialDTO buscarReparteDoEstudo(Long estudoOrigem, Integer numeroCota);
    Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo);
    void atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal);
}
