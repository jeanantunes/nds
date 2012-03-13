package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;

public interface MovimentoEstoqueRepository extends Repository<MovimentoEstoque, Long> {

	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(String codigoProduto, Long numeroEdicao, StatusAprovacao statusAprovacao);
	
}
