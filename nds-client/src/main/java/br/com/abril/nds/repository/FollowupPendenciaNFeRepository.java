package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface FollowupPendenciaNFeRepository extends Repository<Cota,Long> {
    List<ConsultaFollowupPendenciaNFeDTO> obterConsignadosParaChamadao(FiltroFollowupPendenciaNFeDTO filtro);
}
