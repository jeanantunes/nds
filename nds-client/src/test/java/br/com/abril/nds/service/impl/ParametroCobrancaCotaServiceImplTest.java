package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ParametroCobrancaCotaService;


public class ParametroCobrancaCotaServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
	private  Long idCota;
	
	@Before
	public void setup() {
		
		
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteira);
		
		Banco banco = Fixture.hsbc(carteira); 
		save(banco);
		
		
		PessoaJuridica pj = Fixture.pessoaJuridica("Distrib", "01.001.001/001-00",
				"000.000.000.00", "distrib@mail.com", "99.999-9");
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE,null);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"","",true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, pj, new Date(), politicasCobranca);

		save(pj);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		Endereco enderecoDoDistruibuidor = Fixture.criarEndereco(
				TipoEndereco.COBRANCA, "13222-020", "Rua João de Souza", 51, "Centro", "São Paulo", "SP",1);
		
		EnderecoDistribuidor enderecoDistribuidor = Fixture.enderecoDistribuidor(distribuidor, enderecoDoDistruibuidor, true, TipoEndereco.COBRANCA);
		
		save(enderecoDoDistruibuidor,enderecoDistribuidor);
		
		PessoaFisica pessoaFisica = Fixture.pessoaFisica("123.456.789-00","sys.discover@gmail.com", "Cota da Silva");
		save(pessoaFisica);
		
		Box box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		Cota cota = Fixture.cota(1000, pessoaFisica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		Endereco enderecoDaCota = Fixture.criarEndereco(
				TipoEndereco.COBRANCA, "13222-020", "Rua Antonio Cristovan", 51, "Centro", "Mococa", "SP",1);
		
		
		EnderecoCota enderecoCota = Fixture.enderecoCota(cota, enderecoDaCota, true, TipoEndereco.COBRANCA);
		save(enderecoDaCota,enderecoCota);
		
		
		
		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(formasCobranca, 1, null, cota, 1, 
											  false, new BigDecimal(1000), null);
		save(parametroCobranca);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		formaBoleto.setPrincipal(true);
		save(formaBoleto);
		
		
		idCota = cota.getId();
		
	}
	
	@Test
	public void obterDadosParametroCobrancaPorCota(){
		ParametroCobrancaCotaDTO financeiroVO = this.financeiroService.obterDadosParametroCobrancaPorCota(idCota);
		Assert.assertTrue(financeiroVO!=null);
	}
	
	
	@Test
	public void obterFormasCobrancaPorCota(){
		List<FormaCobrancaDTO> formasCobranca = this.financeiroService.obterDadosFormasCobrancaPorCota(idCota);
		Assert.assertNotNull(formasCobranca);
		Assert.assertTrue((formasCobranca.size()>0));
	}
	
	
	@Test
	public void obtemContratoTransporte() {
		ContratoTransporteDTO contratoTransporteDTO =  financeiroService.obtemContratoTransporte(idCota);
		System.out.println(contratoTransporteDTO);
		Assert.assertNotNull(contratoTransporteDTO);
		Assert.assertNotNull(contratoTransporteDTO.getContratada());
		Assert.assertNotNull(contratoTransporteDTO.getContratada().getDescEndereco());
		Assert.assertNotNull(contratoTransporteDTO.getContratante());
		Assert.assertNotNull(contratoTransporteDTO.getContratante().getDescEndereco());
	}
	
	
}
