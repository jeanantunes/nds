package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;

public interface EstudoComplementarRepository {
     
    public List<EstudoCotaGerado> selecionarBancas(EstudoComplementarVO estudoComplementarVO);

    List<EstudoCotaGerado> getCotasOrdenadas(EstudoComplementarVO estudoComplementarVO);
}
