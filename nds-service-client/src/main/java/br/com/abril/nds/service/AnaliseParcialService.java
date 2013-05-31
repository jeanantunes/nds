package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.*;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface AnaliseParcialService {

	EstudoCota buscarPorId(Long id);
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaReparte(Long estudoId, Long numeroCota, Long reparte);
	void liberar(Long id);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId);
	List<AnaliseEstudoDetalhesDTO> buscarDetalhesAnalise(ProdutoEdicao produtoEdicao);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota);
	
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);

    BigDecimal calcularPercentualAbrangencia(Long estudoId);

    void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap);

    CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto);

    List<AnaliseEstudoDetalhesDTO> historicoEdicoesBase(Long[] idsProdutoEdicao);
}
