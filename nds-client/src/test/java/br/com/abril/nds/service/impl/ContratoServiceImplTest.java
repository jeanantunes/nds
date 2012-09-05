package br.com.abril.nds.service.impl;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ContratoService;

public class ContratoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ContratoService contratoService;
	
	private ContratoCota contrato;
	
	@Test
	public void salvarContrato() {
		
		PessoaJuridica pessoaJuridica = 
				Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com", "99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39", "joao@gmail.com", "João da Silva");
		save(pessoaFisica);
			
		Box box = Fixture.boxReparte300();
		save(box);
			
		Cota cota = Fixture.cota(123, pessoaFisica, SituacaoCadastro.ATIVO, box);
		cota.setSugereSuspensao(true);
		save(cota);
		
		this.contrato = Fixture.criarContratoCota(cota, false, new Date(), new Date(), 12, 12);
		this.contrato.setRecebido(true);
		
		this.contratoService.salvarContrato(contrato);
	}
}
