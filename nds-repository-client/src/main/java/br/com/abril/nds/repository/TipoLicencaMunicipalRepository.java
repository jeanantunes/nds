package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;

public interface TipoLicencaMunicipalRepository extends Repository<TipoLicencaMunicipal, Long> {
	
	TipoLicencaMunicipal obterTipoLicencaMunicipal(Long codigo);
}
