package br.com.abril.nds.repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;


public interface VisaoEstoqueRepository {

	VisaoEstoqueDTO obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro);
	
	VisaoEstoqueDTO obterVisaoEstoqueHistorico(FiltroConsultaVisaoEstoque filtro);
	
	VisaoEstoqueDetalheDTO obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro);

}
