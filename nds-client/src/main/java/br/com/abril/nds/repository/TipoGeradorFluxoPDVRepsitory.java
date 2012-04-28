package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;

public interface TipoGeradorFluxoPDVRepsitory extends Repository<TipoGeradorFluxoPDV, Long> {
	
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long...codigos);
}
