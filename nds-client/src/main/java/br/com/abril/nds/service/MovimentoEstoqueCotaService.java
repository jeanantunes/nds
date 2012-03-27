package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public interface MovimentoEstoqueCotaService {

	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
}
