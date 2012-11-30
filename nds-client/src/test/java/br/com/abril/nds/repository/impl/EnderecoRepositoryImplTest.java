package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.EnderecoRepository;

public class EnderecoRepositoryImplTest extends AbstractRepositoryImplTest {
	

	private static final Integer NUMERO_COTA = 1;

	@Autowired
	EnderecoRepository enderecoRepository;
	
	private Cota cota;
	private Usuario usuario;
	private PessoaJuridica pessoaJuridica;
	private Editor abril;
	
	@Before
	public void setup() {
		abril = Fixture.editoraAbril();
		save(abril);
		
		pessoaJuridica = 
			Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com", "99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39", "joao@gmail.com", "João da Silva");
		save(pessoaFisica);
		
		Box box = Fixture.boxReparte300();
		save(box);
		
		cota = Fixture.cota(NUMERO_COTA, pessoaFisica, SituacaoCadastro.ATIVO, box);
		cota.setSugereSuspensao(true);
		save(cota);
		
		criarEnderecoCota(cota);
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
		

		UnidadeFederacao uf = Fixture.criarUnidadeFederacao("SP");
		save(uf);
		
		Localidade localidade = Fixture.criarLocalidade(1L, "Mococa", 1L, uf);
		save(localidade);

	}
	
	private void criarEnderecoCota(Cota cota) {
		
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", "50", "Centro", "Mococa", "SP", 1);

		endereco.setCodigoCidadeIBGE(1);
		
		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cota);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(false);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		
		
		
		
		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", "50", "Vila Carvalho", "Mococa", "SP", 1);

		endereco2.setCodigoCidadeIBGE(1);
		
		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cota);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(true);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);
		
		save(endereco, enderecoCota, endereco2, enderecoCota2);
	}
	
	@Test
	public void teste() {
		
		List<String> listaLocalidade = enderecoRepository.obterListaLocalidadeCotas();
		
		Assert.assertNotNull(listaLocalidade);
				
	}
	@Test
	public void testarPesquisarBairros() {

		List<String> bairros;

		bairros = enderecoRepository.pesquisarBairros("teste");

		Assert.assertNotNull(bairros);

	}
	
	@Test
	public void removerEnderecos(){
		List<Long> idsEndereco = new ArrayList<Long>();
		idsEndereco.add(1L);
		
		enderecoRepository.removerEnderecos(idsEndereco);
	}
	
	@Test
	public void buscarEnderecosPessoaIdPessoa(){
		Long idPessoa = 1L;
		
		List<Endereco> endereco = enderecoRepository.buscarEnderecosPessoa(idPessoa, null);
		
		Assert.assertNotNull(endereco);
	}
	
	@Test
	public void buscarEnderecosPessoaIdsIgnorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		List<Endereco> endereco = enderecoRepository.buscarEnderecosPessoa(null, idsIgnorar);
		
		Assert.assertNotNull(endereco);
	}
	
	@Test
	public void obterMunicipiosCotas(){
				
		List<String> string = enderecoRepository.obterMunicipiosCotas();
		
		Assert.assertNotNull(string);
	}
	
	
	@Test
	public void obterLocalidadesPorUFNomeNome(){
		String nome = "";
				
		List<String> localidade = enderecoRepository.obterLocalidadesPorUFNome(nome, null);
		
		Assert.assertNotNull(localidade);
	}
	
	@Test
	public void obterLocalidadesPorUFNomeSiglaUF(){
		String siglaUF = "SP";
				
		List<String> localidade = enderecoRepository.obterLocalidadesPorUFNome(null, siglaUF);
		
		Assert.assertNotNull(localidade);
	}
	
	@Test
	public void obterBairrosPorCodigoIBGENomeNome(){
		String nome = "bairroTeste";
				
		List<String> bairro = enderecoRepository.obterBairrosPorCodigoIBGENome(nome, null);
		
		Assert.assertNotNull(bairro);
	}
	
	@Test
	public void obterBairrosPorCodigoIBGENomeCodigoIBGE(){
		Long codigoIBGE = 1234567L ;
				
		List<String> bairro = enderecoRepository.obterBairrosPorCodigoIBGENome(null, codigoIBGE);
		
		Assert.assertNotNull(bairro);
	}
	
	@Test
	public void obterLogradourosPorCodigoBairroNomeCodBairro(){
		Long codBairro = 1L ;
				
		List<String> logradouro = 
				enderecoRepository.obterLogradourosPorCodigoBairroNome(codBairro, null);
		
		Assert.assertNotNull(logradouro);
	}
	
	@Test
	public void obterLogradourosPorCodigoBairroNomeNomeLogradouro(){
		String nomeLogradouro = "logradouroTeste";
				
		List<String> logradouro = 
				enderecoRepository.obterLogradourosPorCodigoBairroNome(null, nomeLogradouro);
		
		Assert.assertNotNull(logradouro);
	}

}
