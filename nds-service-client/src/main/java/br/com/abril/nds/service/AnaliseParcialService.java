package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface AnaliseParcialService {

	EstudoCota buscarPorId(Long id);
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaClassificacaoCota(Long estudoId, Long numeroCota);
	void atualizaReparte(Long estudoId, Long numeroCota, Long reparte);
	void liberar(Long id);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo);
	
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);

    BigDecimal calcularPercentualAbrangencia(Long estudoId);

    void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap, String legenda, boolean manterFixa);

    CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto);

    List<AnaliseEstudoDetalhesDTO> historicoEdicoesBase(List<AnaliseEstudoDetalhesDTO> idsProdutoEdicao);

    Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo);
}
