package br.com.abril.nds.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.service.EstudoComplementarService;

import static org.mockito.Mockito.mock;


public class EstudoComplementarTest {

	@Autowired
	EstudoComplementarService estudoComplementar;
	
	@Test
	public void pesquisaTest(){
		 EstudoCotaDTO estudoBase= mock( EstudoCotaDTO.class);
		 estudoBase.setId(81828L);
		 
		 EstudoComplementarServiceImpl  eCompl = mock(EstudoComplementarServiceImpl.class);
		 EstudoComplementarDTO ec = eCompl.obterEstudoComplementarPorIdEstudoBase(estudoBase.getId());

		
		 
		 
	}
	
	@Test
	public void gerarEstudo(){
		
		EstudoComplementarVO parametros= mock( EstudoComplementarVO.class );
		parametros.setCodigoEstudo(0);
		parametros.setReparteCota(0);
		parametros.setReparteLancamento(0);
		parametros.setReparteSobra(0);
		parametros.setTipoSelecao(1);
		
		EstudoComplementarServiceImpl  eCompl = mock(EstudoComplementarServiceImpl.class);
		eCompl.gerarEstudoComplementar(parametros);
	}
}
