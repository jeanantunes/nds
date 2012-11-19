package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;

public class GrupoPermissaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private GrupoPermissaoRepositoryImpl grupoPermissaoRepositoryImpl;
	
	@Test
	 public void testarBuscaFiltrada() {
		
		List<ResultadoGrupoVO> listaBuscaFiltrada;
		
		FiltroConsultaGrupoDTO filtro = new FiltroConsultaGrupoDTO();
		
		listaBuscaFiltrada = grupoPermissaoRepositoryImpl.buscaFiltrada(filtro);
		
		Assert.assertNotNull(listaBuscaFiltrada);
				
	}
	
//	getNome
	@Test
	 public void testarBuscaFiltradaGetNome() {
		
		List<ResultadoGrupoVO> listaBuscaFiltrada;
		
		FiltroConsultaGrupoDTO filtro = new FiltroConsultaGrupoDTO();
		filtro.setNome("testeNome");
		
		listaBuscaFiltrada = grupoPermissaoRepositoryImpl.buscaFiltrada(filtro);
		
		Assert.assertNotNull(listaBuscaFiltrada);
				
	}

}
