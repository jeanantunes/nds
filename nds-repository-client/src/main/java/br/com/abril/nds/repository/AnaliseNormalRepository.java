package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.AnaliseNormalDTO;
import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.model.cadastro.AnaliseNormalProdutoEdicaoDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;

public interface AnaliseNormalRepository {

	EstudoGerado buscarPorId(Long id);

	void atualizaReparte(Long estudoId, Long numeroCota, Long reparte);

	List<AnaliseNormalDTO> buscaAnaliseNormalPorEstudo(
			AnaliseNormalQueryDTO queryDTO);

	List<AnaliseNormalProdutoEdicaoDTO> buscaProdutoParaGrid(Long id);

	void liberarEstudo(Long id);

}
