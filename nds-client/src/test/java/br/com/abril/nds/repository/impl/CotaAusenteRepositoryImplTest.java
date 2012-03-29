package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;

public class CotaAusenteRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaAusenteRepositoryImpl cotaAusenteRepository;
	
	private Cota cotaManoel;
	
	private PessoaFisica manoel;
	
	private Box box1;
	
	
	@Before
	public void setup() {
		
		manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
	
	}
	
	@Test
	public void buscarCotaAusente(){
		
				
		CotaAusenteDTO cotaAusenteDTO = new CotaAusenteDTO();
				
		List<CotaAusenteDTO> listaCotaAusenteDTO =cotaAusenteRepository.obterCotasAusentes(new Date(), cotaManoel.getId(), cotaAusenteDTO);
		
		Assert.assertTrue(listaCotaAusenteDTO.size() == 1);
		
	}

}
