package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoEndereco;

public class CotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaRepositoryImpl cotaRepository;
	
	private static final Integer NUMERO_COTA = 1;
	
	@Before
	public void setup() {
		
		PessoaJuridica pessoaJuridica = 
			Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com");

		save(pessoaJuridica);
		
		Box box = Fixture.boxReparte300();
		save(box);
		
		Cota cota = Fixture.cota(NUMERO_COTA, pessoaJuridica, SituacaoCadastro.ATIVO, box);
		
		save(cota);
		
		criarEnderecoCota(cota);
	}
	
	@Test
	public void obterPorNumeroCota() {
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);
		
		Assert.assertNotNull(cota);
		
		Assert.assertEquals(NUMERO_COTA, cota.getNumeroCota());
	}
	
	@Test
	public void obterEnderecosPorIdCotaSucesso() {

		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);
	
		Assert.assertNotNull(cota);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.cotaRepository.obterEnderecosPorIdCota(cota.getId());

		Assert.assertNotNull(listaEnderecoAssociacao);
		
		int expectedListSize = 2;

		int actualListSize = listaEnderecoAssociacao.size();

		Assert.assertEquals(expectedListSize, actualListSize);
	}
	
	private void criarEnderecoCota(Cota cota) {
		
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", 50, "Centro", "Mococa", "SP");

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cota);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(false);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cota);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(true);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);
		
		save(endereco, enderecoCota, endereco2, enderecoCota2);
	}

}
