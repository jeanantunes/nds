package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface FollowupCadastroParcialRepository extends Repository<ProdutoEdicao,Long> {
    List<ConsultaFollowupCadastroParcialDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroParcialDTO filtro);
}
