package br.com.abril.nds.service.impl;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoBoleto;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDepositoTransferencia;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDescontoCota;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDinheiro;
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
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		cota = Fixture
				.cota(1, pessoaFisica, SituacaoCadastro.ATIVO, box1);
		cota.setSugereSuspensao(true);
		save(cota);
	}

	@Test
	public void testSalvaNotaPromissoria()throws Exception {
		
		NotaPromissoria notaPromissoria = new NotaPromissoria();
		
		notaPromissoria.setValor(10000D);
		notaPromissoria.setVencimento(Calendar.getInstance());
		
		notaPromissoria.setValorExtenso("Mil Conto");
		
	
		cotaGarantiaService.salvaNotaPromissoria(notaPromissoria,cota.getId());
		
		
		assertNotNull(cotaGarantiaService.getByCota(cota.getId()));
	}
	
	@Test
	public void testSalvaChequeCalcao() throws Exception {
		
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
		cheque.setEmissao(Calendar.getInstance());
		cheque.setValidade(Calendar.getInstance());
		cheque.setCorrentista("Senor Abravanel");
		
		cotaGarantia = cotaGarantiaService.salvaChequeCaucao(cheque, cota.getId());
		
		assertNotNull(cotaGarantia);
	}
	
	@Test
	public void testSalvaImovel()throws Exception {
		
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
						
		cotaGarantia = cotaGarantiaService.salvaImovel(listaImoveis, cota.getId());
			
		int expectedSize = 4;
		
		Assert.assertEquals(expectedSize, cotaGarantia.getImoveis().size());					
	}


	@Test
	public void testSalvaCaucaoLiquida() throws Exception {
	
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = new CotaGarantiaCaucaoLiquida();
		
		salvarCaucaoLiquidaPagamentoBoleto(cotaGarantiaCaucaoLiquida);
		
		salvarCaucaoLiquidaPagamentoDescontoCota(cotaGarantiaCaucaoLiquida);
		
		salvarCaucaoLiquidaPagamentoDinheiro(cotaGarantiaCaucaoLiquida);
		
		salvarCaucaoLiquidaPagamentoTransferencia(cotaGarantiaCaucaoLiquida);
	}

	private ContaBancariaDeposito getCotaCaucaoLiquida() {
		
		ContaBancariaDeposito contaDepositoCaucaoLiquida = new ContaBancariaDeposito();
		contaDepositoCaucaoLiquida.setNomeBanco("Nome Banco");
		contaDepositoCaucaoLiquida.setNomeCorrentista("Correntista");
		contaDepositoCaucaoLiquida.setAgencia(123l);
		contaDepositoCaucaoLiquida.setNumeroBanco("12");
		contaDepositoCaucaoLiquida.setConta(12345898l);
		return contaDepositoCaucaoLiquida;
	}

	private List<CaucaoLiquida> getListaCaucaoLiquida() {
		List<CaucaoLiquida> listaCaucaoLiquida = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0 ; i <= 5; i++) {
			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			
			caucaoLiquida.setValor(5000D);
			caucaoLiquida.setIndiceReajuste(10D);
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			
			listaCaucaoLiquida.add(caucaoLiquida);
		}
		return listaCaucaoLiquida;
	}
	
	private void salvarCaucaoLiquidaPagamentoDinheiro(CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida) throws ValidacaoException, InstantiationException, IllegalAccessException{
		
		PagamentoDinheiro pagamentoDinheiro = new PagamentoDinheiro();
		pagamentoDinheiro.setValor(BigDecimal.TEN);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamentoDinheiro);
		
		cotaGarantiaCaucaoLiquida = this.cotaGarantiaService.salvarCaucaoLiquida(getListaCaucaoLiquida(), cota.getId(),pagamentoDinheiro,getCotaCaucaoLiquida());
		
		assertNotNull(cotaGarantiaCaucaoLiquida);
		
		Assert.assertTrue( cotaGarantiaCaucaoLiquida.getFormaPagamento() instanceof  PagamentoDinheiro  );
	}
	
	private void salvarCaucaoLiquidaPagamentoBoleto(CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida) throws ValidacaoException, InstantiationException, IllegalAccessException{
		
		PagamentoBoleto pagamentoBoleto = new PagamentoBoleto();
		pagamentoBoleto.setValor(BigDecimal.TEN);
		pagamentoBoleto.setQuantidadeParcelas(10);
		pagamentoBoleto.setValorParcela(BigDecimal.ONE);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamentoBoleto);
	
		cotaGarantiaCaucaoLiquida = this.cotaGarantiaService.salvarCaucaoLiquida(getListaCaucaoLiquida(), cota.getId(),pagamentoBoleto,getCotaCaucaoLiquida());
		
		assertNotNull(cotaGarantiaCaucaoLiquida);
		
		Assert.assertTrue( cotaGarantiaCaucaoLiquida.getFormaPagamento() instanceof  PagamentoBoleto  );
	}
	
	private void salvarCaucaoLiquidaPagamentoTransferencia(CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida) throws ValidacaoException, InstantiationException, IllegalAccessException{
		
		PagamentoDepositoTransferencia pagamentoDepositoTransferencia = new PagamentoDepositoTransferencia();
		pagamentoDepositoTransferencia.setValor(BigDecimal.TEN);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamentoDepositoTransferencia);
		
		cotaGarantiaCaucaoLiquida = this.cotaGarantiaService.salvarCaucaoLiquida(getListaCaucaoLiquida(), cota.getId(),pagamentoDepositoTransferencia,getCotaCaucaoLiquida());
		
		assertNotNull(cotaGarantiaCaucaoLiquida);
		
		Assert.assertTrue( cotaGarantiaCaucaoLiquida.getFormaPagamento() instanceof  PagamentoDepositoTransferencia  );
	}
	
	private void salvarCaucaoLiquidaPagamentoDescontoCota(CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida) throws ValidacaoException, InstantiationException, IllegalAccessException{
		
		PagamentoDescontoCota pagamentoDescontoCota = new PagamentoDescontoCota();
		pagamentoDescontoCota.setDescontoAtual(BigDecimal.ONE);
		pagamentoDescontoCota.setDescontoCota(BigDecimal.TEN);
		pagamentoDescontoCota.setValor(BigDecimal.TEN);
		pagamentoDescontoCota.setValorUtilizado(BigDecimal.TEN);
		
		cotaGarantiaCaucaoLiquida = this.cotaGarantiaService.salvarCaucaoLiquida(getListaCaucaoLiquida(), cota.getId(),pagamentoDescontoCota,getCotaCaucaoLiquida());
		
		assertNotNull(cotaGarantiaCaucaoLiquida);
		
		Assert.assertTrue( cotaGarantiaCaucaoLiquida.getFormaPagamento() instanceof  PagamentoDescontoCota  );
	}
}
