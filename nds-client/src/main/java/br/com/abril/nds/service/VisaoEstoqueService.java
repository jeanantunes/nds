package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;

public interface VisaoEstoqueService {
	
	List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro);
	
	List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro);
	
	void transferirEstoque(FiltroConsultaVisaoEstoque filtro, Usuario usuario);
	
	void atualizarInventarioEstoque(Map<Long, BigInteger> mapaDiferencaProduto, Usuario usuario);
	
}
