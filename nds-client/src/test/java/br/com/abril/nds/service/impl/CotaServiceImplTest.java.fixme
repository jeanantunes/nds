package br.com.abril.nds.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.CotaService;

public class CotaServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaService cotaService;
	
	@Before
	public void setup() {

		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH_TESTE", "01.001.001/001-00", "000.000.000.00", "lh@mail.com","100");
		save(pessoaJuridica);
		
		PessoaJuridica pessoaJuridica2 = Fixture.pessoaJuridica("LH_TESTE2", "01.001.001/001-10", "000.000.000.00", "lh2@mail.com","200");
		save(pessoaJuridica2);
		
		Box box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("TIPO_FORNECEDOR", GrupoFornecedor.PUBLICACAO);
		
		Fornecedor fornecedor1 = Fixture.fornecedor(pessoaJuridica, SituacaoCadastro.ATIVO, true, tipoFornecedor, null);
		Fornecedor fornecedor2 = Fixture.fornecedor(pessoaJuridica2, SituacaoCadastro.ATIVO, true, tipoFornecedor, null);
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor1);
		fornecedores.add(fornecedor2);
		
	}
	
	
	@Ignore
	@Test
	public void testeObterFornecedoresCota(){ 
		List<Fornecedor> fornecedores = this.cotaService.obterFornecedoresCota(1l);
		Assert.assertTrue(fornecedores!=null);	
	}
	
}
