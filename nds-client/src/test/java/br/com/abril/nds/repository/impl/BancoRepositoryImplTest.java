package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class BancoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	private static final String NOME_BANCO1 = "HSBC";
	private static final String NUMERO_BANCO1 = "399";
	private static final String CEDENTE_BANCO1 = "1010";
    private static final boolean ATIVO_BANCO1 = true;
    
    private static final String NOME_BANCO2 = "BANCO_DO_BRASIL";
	private static final String NUMERO_BANCO2 = "001";
	private static final String CEDENTE_BANCO2 = "1011";
    private static final boolean ATIVO_BANCO2 = false;
    
    private static final String  SORT_ORDER = "asc";
	private static final Integer PAGINA = 1;
	private static final Integer QTD_PAGINA = 15;


    //TAREFAS ANTES DA EXECUCAO DO METODO A SER TESTADO
  	@Before
  	public void setup() {
  		
  	    //CRIA UM OBJETO CARTEIRA NA SESSAO PARA TESTES
  		Carteira carteiraRegistrada = Fixture.carteira(30, TipoRegistroCobranca.REGISTRADA);
  	    
  		//CRIA UM OBJETO CARTEIRA NA SESSAO PARA TESTES
		Carteira carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteiraRegistrada,carteiraSemRegistro);
  		
  		//CRIA UM OBJETO BANCO NA SESSAO PARA TESTES
  		Banco bancoHSBC= Fixture.banco(100L, 
				  				       true, 
				  				       30, 
				  				       "1010",
								       123456L, 
								       "1", 
								       "1", 
								       "Sem instruções",
								       "HSBC", 
								       "BANCO HSBC", 
								       "399", 
								       BigDecimal.ZERO, 
								       BigDecimal.ZERO);
        
  	    //CRIA UM OBJETO BANCO NA SESSAO PARA TESTES
  		Banco bancoBB= Fixture.banco(101L, 
  				                     false, 
				  				     30, 
				  				     "1011",
								     123456L, 
								     "2", 
								     "2", 
								     "Sem instruções",
								     "BB",
								     "BANCO_DO_BRASIL", 
								     "001", 
								     BigDecimal.TEN, 
								     BigDecimal.TEN);
  		
  		save(bancoHSBC,bancoBB);
  		
  		
  		//CRIA UM OBJETO FORMA DE COBRANCA BOLETO NA SESSAO PARA TESTES
  		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
  		save(parametroCobranca);
  		
		FormaCobranca formaCobranca = Fixture.formaCobrancaBoleto(false,
						  			  BigDecimal.ZERO, false, bancoBB,
						  			  BigDecimal.TEN, BigDecimal.ZERO, parametroCobranca);
  		save(formaCobranca);
  		
  	}
  	
  	@Test
  	public void obterBancos() {
  		
  		List<Banco> bancos;
  		FiltroConsultaBancosDTO filtro;
  		PaginacaoVO paginacao;
  		
  		
  		//TESTE BANCO HSBC
  		filtro = new FiltroConsultaBancosDTO(NOME_BANCO1, NUMERO_BANCO1, CEDENTE_BANCO1, ATIVO_BANCO1);
  		paginacao = new PaginacaoVO(PAGINA,QTD_PAGINA,SORT_ORDER);
  		filtro.setPaginacao(paginacao);
  		
        bancos = this.bancoRepository.obterBancos(filtro);

        //VERIFICA SE A LISTA DE BOLETOS E NULA
        Assert.assertNotNull(bancos);
          
        //VERIFICA SE A LISTA DE BOLETOS CONTEM RESULTADOS
        Assert.assertTrue(bancos.size() > 0);
          
        //VERIFICA SE FILTRO TROUXE VALORES CORRETOS
  		for (int i=0; i<bancos.size();i++){
  		    Assert.assertEquals(NUMERO_BANCO1, bancos.get(i).getNumeroBanco());
  		    Assert.assertEquals(NOME_BANCO1, bancos.get(i).getNome());
  		    Assert.assertEquals(CEDENTE_BANCO1, bancos.get(i).getCodigoCedente());
  		    Assert.assertEquals(ATIVO_BANCO1, bancos.get(i).isAtivo());
  		}
  		
  		
  	    //TESTE BANCO DO BRASIL
  		filtro = new FiltroConsultaBancosDTO(NOME_BANCO2, NUMERO_BANCO2, CEDENTE_BANCO2, ATIVO_BANCO2);
  		paginacao = new PaginacaoVO(PAGINA,QTD_PAGINA,SORT_ORDER);
  		filtro.setPaginacao(paginacao);
  		
        bancos = this.bancoRepository.obterBancos(filtro);

        //VERIFICA SE A LISTA DE BOLETOS E NULA
        Assert.assertNotNull(bancos);
          
        //VERIFICA SE A LISTA DE BOLETOS CONTEM RESULTADOS
        Assert.assertTrue(bancos.size() > 0);
          
        //VERIFICA SE FILTRO TROUXE VALORES CORRETOS
  		for (int i=0; i<bancos.size();i++){
  		    Assert.assertEquals(NUMERO_BANCO2, bancos.get(i).getNumeroBanco());
  		    Assert.assertEquals(NOME_BANCO2, bancos.get(i).getNome());
		    Assert.assertEquals(CEDENTE_BANCO2, bancos.get(i).getCodigoCedente());
		    Assert.assertEquals(ATIVO_BANCO2, bancos.get(i).isAtivo());
  		}
  	}
    
	@Test
  	public void obterCarteiraPorCodigo() {
		Carteira carteira = this.bancoRepository.obterCarteiraPorCodigo(30);
		Assert.assertTrue(carteira!=null);
		Assert.assertEquals(TipoRegistroCobranca.REGISTRADA, carteira.getTipoRegistroCobranca());
		
		carteira = this.bancoRepository.obterCarteiraPorCodigo(1);
		Assert.assertNotNull(carteira);
		Assert.assertEquals(TipoRegistroCobranca.SEM_REGISTRO, carteira.getTipoRegistroCobranca());
	}


	@Test
  	public void getComboBancosTipoCobranca(){
		List<Banco> bancos = this.formaCobrancaRepository.obterBancosPorTipoDeCobranca(TipoCobranca.BOLETO);
		Assert.assertTrue(bancos!=null);
	}
	
}
