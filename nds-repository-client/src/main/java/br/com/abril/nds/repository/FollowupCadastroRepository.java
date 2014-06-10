package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface FollowupCadastroRepository extends Repository<Cota,Long> {
    List<ConsultaFollowupCadastroDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroDTO filtro);
}
