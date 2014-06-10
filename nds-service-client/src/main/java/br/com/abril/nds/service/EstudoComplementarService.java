package br.com.abril.nds.service;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.dto.EstudoComplementarDTO;

public interface EstudoComplementarService {
	
	public EstudoComplementarDTO obterEstudoComplementarPorIdEstudoBase(long idEstudoBase);
	
	public Long gerarEstudoComplementar(EstudoComplementarVO parametros);
}
