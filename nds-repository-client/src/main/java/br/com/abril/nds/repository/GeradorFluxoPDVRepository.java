package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;


public interface GeradorFluxoPDVRepository extends Repository<GeradorFluxoPDV,Long> {
	
	GeradorFluxoPDV obterGeradorFluxoPDV(Long idPDV);
	
	void removerGeradorFluxoPDV(Long idPDV);
}
