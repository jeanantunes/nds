package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface FollowupStatusCotaRepository extends Repository<Cota,Long> {
    List<ConsultaFollowupStatusCotaDTO> obterConsignadosParaChamadao(FiltroFollowupStatusCotaDTO filtro);
}
