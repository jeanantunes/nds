package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;

public interface VisaoEstoqueService {
	
	List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro);
	
	List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro);
	
	void transferirEstoque(FiltroConsultaVisaoEstoque filtro, Usuario usuario);
	
	void atualizarInventarioEstoque(List<VisaoEstoqueTransferenciaDTO> invetarioAtualizar, TipoEstoque tipoEstoque, Usuario usuario);
	
}
