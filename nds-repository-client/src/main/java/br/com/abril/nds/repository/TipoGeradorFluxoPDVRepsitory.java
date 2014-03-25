package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;

public interface TipoGeradorFluxoPDVRepsitory extends Repository<TipoGeradorFluxoPDV, Long> {
	
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long...codigos);
	
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(Long... codigos );

	public abstract List<TipoGeradorFluxoPDV> obterTodosTiposGeradorFluxo();
	
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoOrdenado();

}
