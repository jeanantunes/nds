package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

public class TipoEntregaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;

	@Test
	public void testPesquisaTodosTiposEntrega() {
		
		List<TipoEntrega> listaTipoEntrega = 
			this.tipoEntregaRepository.pesquisarTiposEntrega(null, null, null, 
					"descricao", "asc", 1, 15);
		
		Assert.assertNotNull(listaTipoEntrega);
	}

	@Test
	public void testPesquisaTiposEntregaPorCodigo() {
		
		List<TipoEntrega> listaTipoEntrega = 
			this.tipoEntregaRepository.pesquisarTiposEntrega(1L, null, null, 
					"descricao", "asc", 1, 15);
		
		Assert.assertNotNull(listaTipoEntrega);
	}

	@Test
	public void testPesquisaTiposEntregaPorDescricao() {
		
		List<TipoEntrega> listaTipoEntrega = 
			this.tipoEntregaRepository.pesquisarTiposEntrega(null, "", null, 
					"descricao", "desc", 1, 15);
		
		Assert.assertNotNull(listaTipoEntrega);
	}

	@Test
	public void testPesquisaTiposEntregaPorPeriodicidade() {
		
		List<TipoEntrega> listaTipoEntrega = 
			this.tipoEntregaRepository.pesquisarTiposEntrega(null, null, "D", 
					"descricao", "desc", 1, 15);
		
		Assert.assertNotNull(listaTipoEntrega);
	}
	
}
