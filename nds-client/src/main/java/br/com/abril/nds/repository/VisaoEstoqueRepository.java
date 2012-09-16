package br.com.abril.nds.repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;


public interface VisaoEstoqueRepository {

	VisaoEstoqueDTO obterLancamento(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterLancamentoJuramentado(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterSuplementar(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterRecolhimento(FiltroConsultaVisaoEstoque filtro);
	VisaoEstoqueDTO obterProdutosDanificados(FiltroConsultaVisaoEstoque filtro);
}
