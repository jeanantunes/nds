package br.com.abril.nds.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.CotaGarantiaService;

public class CotaGarantiaServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	private Cota cota;

	@Before
	public void setUp() throws Exception {
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("FC",
				"01.001.001/001-00", "000.000.000.00", "fc@mail.com", "9999-999");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39",
				"joao@gmail.com", "João da Silva");
		save(pessoaFisica);
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		cota = Fixture
				.cota(1, pessoaFisica, SituacaoCadastro.ATIVO, box1);
		cota.setSugereSuspensao(true);
		save(cota);
	}

	@Test
	public void testSalvaNotaPromissoria() {
		
		NotaPromissoria notaPromissoria = new NotaPromissoria();
		
		notaPromissoria.setValor(10000D);
		notaPromissoria.setVencimento(new Date());
		
		notaPromissoria.setValorExtenso("Mil Conto");
		
	
			cotaGarantiaService.salvaNotaPromissoria(notaPromissoria,cota.getId());
			
		CotaGarantiaDTO cotaGarantia = cotaGarantiaService.getByCota(cota.getId());
	
		assertNotNull(cotaGarantia);
	}
	
	@Test
	public void testSalvaChequeCalcao() {
		
		CotaGarantia cotaGarantia = null;
		
		Cheque cheque = new Cheque();
		
		cheque.setNumeroBanco("321");
		cheque.setNomeBanco("Banco Panamericano");
		cheque.setAgencia(312L);
		cheque.setDvAgencia("3");
		cheque.setConta(444444L);
		cheque.setDvConta("0");
		cheque.setValor(2500000D);
		cheque.setNumeroCheque("123456");
		cheque.setEmissao(new Date());
		cheque.setValidade(new Date());
		cheque.setCorrentista("Senor Abravanel");
		
		
			cotaGarantia = cotaGarantiaService.salvaChequeCaucao(cheque, cota.getId());
			
		
		
		assertNotNull(cotaGarantia);
	}
	
	@Ignore
	@Test
	public void testSalvaImovel() {
		
		CotaGarantiaImovel cotaGarantia = null;
		
		List<Imovel> listaImoveis = new ArrayList<Imovel>();
		
		for (int i = 0 ; i <= 5; i++) {
			
			Imovel imovel = new Imovel();
			
			imovel.setProprietario("Proprietario0"+i);
			imovel.setEndereco("Endereço0"+i);
			imovel.setNumeroRegistro(i+"001");
			imovel.setValor((i+1)*1000D);
			imovel.setObservacao("Obs: "+i+"000");
			
			listaImoveis.add(imovel);
		}
		
		cotaGarantia = cotaGarantiaService.salvaImovel(listaImoveis, cota.getId());
				
		assertNotNull(cotaGarantia);
		
		listaImoveis.remove(1);
		listaImoveis.remove(2);
		
		Imovel imovel = cotaGarantia.getImoveis().get(3);
		
		imovel.setProprietario("proprietario");
		
		cotaGarantiaService.salvaImovel(listaImoveis, cota.getId());
		
		cotaGarantia = null;

		cotaGarantia = (CotaGarantiaImovel) cotaGarantiaService.getByCota(cota.getId()).getCotaGarantia();
		
		int expectedSize = 4;
		
		Assert.assertEquals(expectedSize, cotaGarantia.getImoveis().size());
		
		Assert.assertEquals("proprietario", cotaGarantia.getImoveis().get(3).getProprietario());
		
	}
}
