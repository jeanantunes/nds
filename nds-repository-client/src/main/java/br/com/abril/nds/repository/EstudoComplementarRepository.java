package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface EstudoComplementarRepository {
     public List<EstudoCota> selecionarBancas(EstudoComplementarVO estudoComplementarVO);
     public long gerarNumeroEstudoComplementar();
}
