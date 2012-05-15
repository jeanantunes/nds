package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;

public interface DistribuidorService {
	
	Distribuidor obter();

	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

}
