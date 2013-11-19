package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;

public interface FechamentoDiarioResumoEstoqueRepository extends Repository<FechamentoDiarioResumoEstoque, Long> {
	
	FechamentoDiarioResumoEstoque obterResumoEstoque(Date dataFechamento, TipoEstoque tipoEstoque);
}
