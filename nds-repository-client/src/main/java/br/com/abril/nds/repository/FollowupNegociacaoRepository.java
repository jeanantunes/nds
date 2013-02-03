package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface FollowupNegociacaoRepository extends Repository<Cota,Long> {
    List<ConsultaFollowupNegociacaoDTO> obterConsignadosParaChamadao(FiltroFollowupNegociacaoDTO filtro);
}
