package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;

@Repository
public class RelatorioTiposProdutosRepositoryImpl extends AbstractRepository implements RelatorioTiposProdutosRepository {

	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {

		return null;
	}
}
