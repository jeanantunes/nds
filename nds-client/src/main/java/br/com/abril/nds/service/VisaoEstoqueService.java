package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;

public interface VisaoEstoqueService {
	
	List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro);
	

}
