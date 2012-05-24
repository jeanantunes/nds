package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoEntrega;

public interface TipoEntregaService {

	List<TipoEntrega> obterTodos();

	List<TipoEntrega> pesquisarTiposEntrega(Long codigo, String descricao, String periodicidade,
			String sortname, String sortorder, int page, int rp);

	Integer pesquisarQuantidadeTiposEntrega(Long codigo, String descricao, String periodicidade);
	
	void removerTipoEntrega(Long id);
	
	void salvarTipoEntrega(Long id, String descricao, BigDecimal taxaFixa, Integer percentualFaturamento,
			String baseCalculo, String periodicidadeCadastro, Integer diaSemana, Integer diaMes);
	
}
