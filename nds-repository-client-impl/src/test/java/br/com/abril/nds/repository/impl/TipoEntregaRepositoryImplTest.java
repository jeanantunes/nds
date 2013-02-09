package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

public class TipoEntregaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
	
	@SuppressWarnings("unused")
	@Test
	public void buscarPorDescricaoTipoEntrega(){
		TipoEntrega tipoEntrega = tipoEntregaRepository.buscarPorDescricaoTipoEntrega(DescricaoTipoEntrega.COTA_RETIRA);
	}

}
