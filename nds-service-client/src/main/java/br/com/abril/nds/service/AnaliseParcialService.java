package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface AnaliseParcialService {

	EstudoCota buscarPorId(Long id);
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaReparte(Long estudoId, Long numeroCota, Long reparte);
	void liberar(Long id);
	List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId);
	List<AnaliseEstudoDetalhesDTO> buscarDetalhesAnalise(ProdutoEdicao produtoEdicao);
	List<PdvDTO> carregarDetalhesCota(Long numeroCota);
	
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);
}
