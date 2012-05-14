package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

public class PoliticaCobrancaServiceImplTest  extends AbstractRepositoryImplTest {
	
	@Autowired
	private PoliticaCobrancaServiceImpl politicaCobrancaServiceImpl;
	
	private Distribuidor distribuidor;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		Carteira carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteiraSemRegistro);
		
		Banco bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
			  							123456L, "1", "1", "Instruções.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com", "99.999-9");
		save(pessoaJuridica);
		
		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
			  BigDecimal.ONE, BigDecimal.ONE,null);
		formaBoleto.setTipoCobranca(TipoCobranca.BOLETO);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mansagem",true,FormaEmissao.INDIVIDUAL_BOX);
		save(politicaCobranca);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		distribuidor = Fixture.distribuidor(1, pessoaJuridica, new Date(), politicasCobranca);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
	}	
			
	@Test
	public void obterDadosPoliticasCobranca() {
	    
		FiltroParametrosCobrancaDTO filtro = new FiltroParametrosCobrancaDTO();
		filtro.setIdBanco(10l);
		filtro.setTipoCobranca(TipoCobranca.BOLETO);
		filtro.setOrdenacaoColuna(null);
		filtro.setPaginacao(null);
		List<ParametroCobrancaVO> politicas = this.politicaCobrancaServiceImpl.obterDadosPoliticasCobranca(filtro);
		Assert.assertTrue(politicas.size()>0);
     
	}
	
	
}
