package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupDistribuicaoDTO;

public interface FollowupDistribuicaoRepository extends Repository<ConsultaFollowupDistribuicaoDTO,Long> {
    List<ConsultaFollowupDistribuicaoDTO> obterCotas (ConsultaFollowupDistribuicaoDTO dto);
}
