package br.com.abril.nds.repository;

import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;


public interface CotaBaseRepository extends Repository<Cota, Long> {

	FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota);

}
