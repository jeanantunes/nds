package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoEntrega;

public interface TipoEntregaRepository extends Repository<TipoEntrega, Long> {
	
	List<TipoEntrega> pesquisarTiposEntrega(Long codigo, String descricao, String periodicidade,
			String sortname, String sortorder, int page, int rp);
	
	Integer pesquisarQuantidadeTiposEntrega(Long codigo, String descricao, String periodicidade);
}
