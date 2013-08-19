package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;

public interface FollowupChamadaoRepository extends Repository<ConsultaFollowupChamadaoDTO,Long> {
    List<ConsultaFollowupChamadaoDTO> obterConsignadosParaChamadao(FiltroFollowupChamadaoDTO filtro);
}
