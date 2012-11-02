package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.CobrancaControleConferenciaEncalheCota;

public interface CobrancaControleConferenciaEncalheCotaRepository  extends Repository<CobrancaControleConferenciaEncalheCota, Long> {

	public List<CobrancaControleConferenciaEncalheCota> obterCobrancaControleConferenciaEncalheCota(Long idControleConferenciaEncalheCota);
	
}
