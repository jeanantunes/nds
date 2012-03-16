package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Ignore
public class BoletoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	private static final Integer NUMERO_COTA = 1000;
	private static final Date    DT_VENCTO_DE = new Date();
	private static final Date    DT_VENCTO_ATE = new Date();
	private static final String  SORT_ORDER = "asc";
	private static final Integer PAGINA = 1;
	private static final Integer QTD_PAGINA = 15;

	
	//TAREFAS ANTES DA EXECUCAO DO METODO A SER TESTADO
	@Before
	public void setup() {
		
		//CRIA UM OBJETO PESSOA NA SESSAO PARA TESTES
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com");
		save(pessoaJuridica);
		
		//CRIA UM OBJETO BOX NA SESSAO PARA TESTES
		Box box = Fixture.criarBox("300", "Box 300", TipoBox.REPARTE);
		save(box);
		
		//CRIA UM OBJETO COTA NA SESSAO PARA TESTES
		Cota cota = Fixture.cota(NUMERO_COTA, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		
		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010",
				  			  		123456L, "1", "1", "Instruções.", Moeda.REAL, "HSBC", "399");
		save(bancoHSBC);
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

	    Boleto boleto = Fixture.boleto("5", 
                					   new Date(), 
                					   new Date(), 
                					   new Date(), 
                					   "0", 
                					   BigDecimal.TEN, 
                					   "1", 
                					   "1",
                					   StatusCobranca.PAGO,
                					   cota,
                					   bancoHSBC);
		save(boleto);		
	}
	
	
	//TESTE DE EXECUCAO DO METODO A SER TESTADO
	@Test
	public void obterBoletosPorNumeroDaCota() {
		
		FiltroConsultaBoletosCotaDTO filtro = new FiltroConsultaBoletosCotaDTO(NUMERO_COTA,
                															   DT_VENCTO_DE,
                															   DT_VENCTO_ATE,
                															   StatusCobranca.PAGO);
		PaginacaoVO paginacao = new PaginacaoVO(PAGINA,QTD_PAGINA,SORT_ORDER);
		filtro.setPaginacao(paginacao);
		
        List<Boleto> boletos = this.boletoRepository.obterBoletosPorCota(filtro);

        //VERIFICA SE A LISTA DE BOLETOS E NULA
        Assert.assertNotNull(boletos);
        
        //VERIFICA SE A LISTA DE BOLETOS CONTEM RESULTADOS
        Assert.assertTrue(boletos.size() > 0);
        
        //VERIFICA SE FILTRO TROUXE VALORES CORRETOS
		for (int i=0; i<boletos.size();i++){
		    Assert.assertEquals(NUMERO_COTA, boletos.get(i).getCota().getNumeroCota());
		}
	}
	
	@Test
	public void obterPorNossoNumero() {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero("5");
		
		Assert.assertNotNull(boleto);	
	}

}
