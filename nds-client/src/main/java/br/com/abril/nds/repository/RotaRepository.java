package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Rota;

public interface RotaRepository extends Repository<Rota, Long> {

	List<Rota> buscarRotasRoteiro();

	List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao();
}