package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.GarantiaCadastradaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;
import br.com.abril.nds.repository.CotaGarantiaRepository;

public class CotaGarantiaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;
	
	private Cota cotaAbril;
	private Cota cotaManoel;
	private Cota cotaDinap;
	private Cota cotaFC;
	private Cota cotaAcme;
	private Cota cotaMaria;
	private Cota cotaJoao;
	private Cota cotaJose;
	private Cota cotaSandra;
	private Cota cotaMadalena;
	private Cota cotaFulana;
	private Cota cotaCiclana;
	
	
	private Box box300;
	
	private PessoaJuridica juridicaAbril;
	private PessoaJuridica juridicaAcme;
	private PessoaJuridica juridicaDinap;
	private PessoaJuridica juridicaFC;
	private PessoaJuridica fulana;
	private PessoaJuridica ciclana;
	private PessoaFisica manoel;
	private PessoaFisica maria;
	private PessoaFisica joao;
	private PessoaFisica jose;
	private PessoaFisica sandra;
	private PessoaFisica madalena;
	
	@Before
	public void setup() {

		box300 = Fixture.boxReparte300();

		juridicaFC = Fixture.juridicaFC();
		juridicaAcme = Fixture.juridicaAcme();
		juridicaDinap = Fixture.juridicaDinap();
		juridicaAbril = Fixture.juridicaAbril();
		fulana = Fixture.pessoaJuridica("fulana", "33333333", "32154", "aaaa", "55012");
		ciclana = Fixture.pessoaJuridica("fulana", "111111", "331225", "bbbb", "80155");

		manoel = Fixture.pessoaFisica("333333", "manoel", "Manoel");
		maria = Fixture.pessoaFisica("111111", "maria", "Maria");
		joao = Fixture.pessoaFisica("22222", "joao", "João");
		jose = Fixture.pessoaFisica("000000", "jose", "José");
		sandra = Fixture.pessoaFisica("4444444", "jose", "Sandra");
		madalena = Fixture.pessoaFisica("5555555", "jose", "Madalena");

		cotaFC = Fixture.cota(456, juridicaFC, SituacaoCadastro.ATIVO, box300);
		cotaAcme = Fixture.cota(234, juridicaAcme, SituacaoCadastro.ATIVO, box300);
		cotaAbril = Fixture.cota(123, juridicaAbril, SituacaoCadastro.ATIVO, box300);
		cotaDinap = Fixture.cota(345, juridicaDinap, SituacaoCadastro.ATIVO, box300);
		cotaManoel = Fixture.cota(567, manoel, SituacaoCadastro.ATIVO, box300);
		cotaMaria = Fixture.cota(678, maria, SituacaoCadastro.ATIVO, box300);
		cotaJoao = Fixture.cota(789, joao, SituacaoCadastro.ATIVO, box300);
		cotaJose = Fixture.cota(890, jose, SituacaoCadastro.ATIVO, box300);
		cotaSandra = Fixture.cota(111, sandra, SituacaoCadastro.ATIVO, box300);
		cotaMadalena = Fixture.cota(222, madalena, SituacaoCadastro.ATIVO, box300);
		cotaFulana = Fixture.cota(333, fulana, SituacaoCadastro.ATIVO, box300);
		cotaCiclana = Fixture.cota(444, ciclana, SituacaoCadastro.ATIVO, box300);

		save(
			box300, juridicaFC, juridicaAcme, juridicaDinap, juridicaAbril, 
			manoel, maria, joao, jose, fulana, ciclana, sandra, madalena,
			cotaSandra, cotaMadalena, cotaMaria, cotaJoao, cotaJose, cotaFulana, cotaCiclana,
			cotaFC, cotaAcme, cotaAbril, cotaDinap, cotaManoel
		);

		this.criarGarantiasCaucaoLiquida();
		
		this.criarGarantiasChequeCaucao();
		
		this.criarGarantiasImovel();
		
		this.criarGarantiasFiador();
		
		this.criarGarantiasNotaPromissoria();
		
		this.criarGarantiasOutros();
	}
	
	@Test
	public void obterGarantiasCadastradas() {

		List<GarantiaCadastradaDTO> garantias = this.cotaGarantiaRepository.obterGarantiasCadastradas();
		
		Assert.assertNotNull(garantias);
		
		int expectedListSize = 6;
		int actualListSize = garantias.size();
		
		Assert.assertEquals(expectedListSize, actualListSize);
		
		for (GarantiaCadastradaDTO garantia : garantias) {
			
			switch (garantia.getTipoGarantia()) {
			
			case CAUCAO_LIQUIDA:
				Assert.assertEquals(new BigDecimal(200), garantia.getValorTotal());
				break;
			case CHEQUE_CAUCAO:
				Assert.assertEquals(new BigDecimal(25), garantia.getValorTotal());
				break;
			case FIADOR:
				Assert.assertEquals(new BigDecimal(200), garantia.getValorTotal());
				break;
			case IMOVEL:
				Assert.assertEquals(new BigDecimal(200), garantia.getValorTotal());
				break;
			case NOTA_PROMISSORIA:
				Assert.assertEquals(new BigDecimal(50), garantia.getValorTotal());
				break;
			case OUTROS:
				Assert.assertEquals(new BigDecimal(200), garantia.getValorTotal());
				break;
			default:
				Assert.fail();
				break;
			}
		}
	}
	
	private void criarGarantiasCaucaoLiquida() {
		
		PagamentoCaucaoLiquida pagamentoCaucaoLiquida = new PagamentoCaucaoLiquida();
		pagamentoCaucaoLiquida.setValor(BigDecimal.TEN);

		save(pagamentoCaucaoLiquida);
		
		Double valorCaucaoLiquida = 10D;
		
		List<CaucaoLiquida> caucoes1 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			caucaoLiquida.setValor(valorCaucaoLiquida);
			
			save(caucaoLiquida);
			
			caucoes1.add(caucaoLiquida);
		}
		
		List<CaucaoLiquida> caucoes2 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			caucaoLiquida.setValor(valorCaucaoLiquida);
			
			save(caucaoLiquida);
			
			caucoes2.add(caucaoLiquida);
		}

		List<CaucaoLiquida> caucoes3 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			caucaoLiquida.setValor(valorCaucaoLiquida);
			
			save(caucaoLiquida);
			
			caucoes3.add(caucaoLiquida);
		}

		List<CaucaoLiquida> caucoes4 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			caucaoLiquida.setValor(valorCaucaoLiquida);
			
			save(caucaoLiquida);
			
			caucoes4.add(caucaoLiquida);
		}
		
		CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida1 = new CotaGarantiaCaucaoLiquida();
		CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida2 = new CotaGarantiaCaucaoLiquida();
		
		garantiaCaucaoLiquida1.setCaucaoLiquidas(caucoes1);
		garantiaCaucaoLiquida1.setCota(cotaAbril);
		garantiaCaucaoLiquida1.setData(new Date());
		garantiaCaucaoLiquida1.setFormaPagamento(pagamentoCaucaoLiquida);
		garantiaCaucaoLiquida1.setTipoCobranca(TipoCobrancaCotaGarantia.BOLETO);
		
		garantiaCaucaoLiquida2.setCaucaoLiquidas(caucoes2);
		garantiaCaucaoLiquida2.setCota(cotaAcme);
		garantiaCaucaoLiquida2.setData(new Date());
		garantiaCaucaoLiquida2.setFormaPagamento(pagamentoCaucaoLiquida);
		garantiaCaucaoLiquida2.setTipoCobranca(TipoCobrancaCotaGarantia.BOLETO);
		
		save(garantiaCaucaoLiquida1, garantiaCaucaoLiquida2);
	}
	
	private void criarGarantiasChequeCaucao() {
		
		Cheque cheque1 = new Cheque();
		cheque1.setAgencia(123L);
		cheque1.setConta(123L);
		cheque1.setCorrentista("Fulano");
		cheque1.setDvAgencia("1");
		cheque1.setDvConta("1");
		cheque1.setEmissao(new Date());
		cheque1.setNomeBanco("Banco do Brasil");
		cheque1.setNumeroBanco("555");
		cheque1.setNumeroCheque("888");
		cheque1.setValidade(new Date());
		cheque1.setValor(10D);
		
		Cheque cheque2 = new Cheque();
		cheque2.setAgencia(234L);
		cheque2.setConta(234L);
		cheque2.setCorrentista("Ciclano");
		cheque2.setDvAgencia("1");
		cheque2.setDvConta("1");
		cheque2.setEmissao(new Date());
		cheque2.setNomeBanco("Banco Itau");
		cheque2.setNumeroBanco("555");
		cheque2.setNumeroCheque("888");
		cheque2.setValidade(new Date());
		cheque2.setValor(15D);

		CotaGarantiaChequeCaucao garantiaChequeCaucao1 = new CotaGarantiaChequeCaucao();
		CotaGarantiaChequeCaucao garantiaChequeCaucao2 = new CotaGarantiaChequeCaucao();

		garantiaChequeCaucao1.setData(new Date());
		garantiaChequeCaucao1.setCota(cotaDinap);
		garantiaChequeCaucao1.setCheque(cheque1);
		
		garantiaChequeCaucao2.setData(new Date());
		garantiaChequeCaucao2.setCota(cotaFC);
		garantiaChequeCaucao2.setCheque(cheque2);
		
		save(garantiaChequeCaucao1, garantiaChequeCaucao2);
	}
	
	private void criarGarantiasImovel() {
		
		List<Imovel> imoveis1 = new ArrayList<Imovel>();
		
		for (int i = 0; i < 10; i++) {
			
			Imovel imovel = new Imovel();
			imovel.setEndereco("Endereco");
			imovel.setNumeroRegistro("123" + i);
			imovel.setObservacao("observação");
			imovel.setProprietario("proprietário");
			imovel.setValor(10D);
			
			imoveis1.add(imovel);
		}
		
		List<Imovel> imoveis2 = new ArrayList<Imovel>();
		
		for (int i = 0; i < 10; i++) {
			
			Imovel imovel = new Imovel();
			imovel.setEndereco("Endereco");
			imovel.setNumeroRegistro("123" + i);
			imovel.setObservacao("observação");
			imovel.setProprietario("proprietário");
			imovel.setValor(10D);
			
			imoveis2.add(imovel);
		}
		
		CotaGarantiaImovel garantiaImovel1 = new CotaGarantiaImovel();
		CotaGarantiaImovel garantiaImovel2 = new CotaGarantiaImovel();
		
		garantiaImovel1.setCota(cotaManoel);
		garantiaImovel1.setData(new Date());
		garantiaImovel1.setImoveis(imoveis1);
		
		garantiaImovel2.setCota(cotaMaria);
		garantiaImovel2.setData(new Date());
		garantiaImovel2.setImoveis(imoveis2);
		
		save(garantiaImovel1, garantiaImovel2);
	}
	
	private void criarGarantiasFiador() {
		
		Fiador fiador1 = new Fiador();
		Fiador fiador2 = new Fiador();
		
		fiador1.setInicioAtividade(new Date());
		fiador1.setPessoa(joao);
		
		fiador2.setInicioAtividade(new Date());
		fiador2.setPessoa(manoel);
		
		save(fiador1, fiador2);
		
		List<Garantia> garantias1 = new ArrayList<Garantia>();
		
		for (int i = 0; i < 10; i++) {
			
			Garantia garantia = new Garantia();
			garantia.setDescricao("garantia " + i);
			garantia.setFiador(fiador1);
			garantia.setValor(BigDecimal.TEN);
			
			save(garantia);
			
			garantias1.add(garantia);
		}

		List<Garantia> garantias2 = new ArrayList<Garantia>();
		
		for (int i = 0; i < 10; i++) {
			
			Garantia garantia = new Garantia();
			garantia.setDescricao("garantia " + i);
			garantia.setFiador(fiador2);
			garantia.setValor(BigDecimal.TEN);
			
			save(garantia);
			
			garantias2.add(garantia);
		}

		CotaGarantiaFiador garantiaFiador1 = new CotaGarantiaFiador();
		CotaGarantiaFiador garantiaFiador2 = new CotaGarantiaFiador();
		
		garantiaFiador1.setCota(cotaJoao);
		garantiaFiador1.setData(new Date());
		garantiaFiador1.setFiador(fiador1);
		
		garantiaFiador2.setCota(cotaJose);
		garantiaFiador2.setData(new Date());
		garantiaFiador2.setFiador(fiador2);
		
		save(garantiaFiador1, garantiaFiador2);
	}
	
	private void criarGarantiasNotaPromissoria() {
		
		NotaPromissoria notaPromissoria1 = new NotaPromissoria();
		
		notaPromissoria1.setValor(20D);
		notaPromissoria1.setValorExtenso("Vinte");
		notaPromissoria1.setVencimento(new Date());
		
		NotaPromissoria notaPromissoria2 = new NotaPromissoria();
		
		notaPromissoria2.setValor(30D);
		notaPromissoria2.setValorExtenso("Trinta");
		notaPromissoria2.setVencimento(new Date());
		
		save(notaPromissoria1, notaPromissoria2);
		
		CotaGarantiaNotaPromissoria garantiaNotaPromissoria1 = new CotaGarantiaNotaPromissoria();
		CotaGarantiaNotaPromissoria garantiaNotaPromissoria2 = new CotaGarantiaNotaPromissoria();
		
		garantiaNotaPromissoria1.setCota(cotaSandra);
		garantiaNotaPromissoria1.setData(new Date());
		garantiaNotaPromissoria1.setNotaPromissoria(notaPromissoria1);
		
		garantiaNotaPromissoria2.setCota(cotaMadalena);
		garantiaNotaPromissoria2.setData(new Date());
		garantiaNotaPromissoria2.setNotaPromissoria(notaPromissoria2);
		
		save(garantiaNotaPromissoria1, garantiaNotaPromissoria2);
	}

	private void criarGarantiasOutros() {
	
		List<GarantiaCotaOutros> outros1 = new ArrayList<GarantiaCotaOutros>();
		
		for (int i = 0; i < 10; i++) {
			
			GarantiaCotaOutros outro = new GarantiaCotaOutros();
			outro.setDescricao("descrição");
			outro.setValidade(new Date());
			outro.setValor(BigDecimal.TEN);
			
			outros1.add(outro);
		}
		 
		List<GarantiaCotaOutros> outros2 = new ArrayList<GarantiaCotaOutros>();
		
		for (int i = 0; i < 10; i++) {
			
			GarantiaCotaOutros outro = new GarantiaCotaOutros();
			outro.setDescricao("descrição");
			outro.setValidade(new Date());
			outro.setValor(BigDecimal.TEN);
			
			outros2.add(outro);
		}
		
		CotaGarantiaOutros garantiaOutros1 = new CotaGarantiaOutros();
		CotaGarantiaOutros garantiaOutros2 = new CotaGarantiaOutros();
		
		garantiaOutros1.setCota(cotaFulana);
		garantiaOutros1.setData(new Date());
		garantiaOutros1.setOutros(outros1);
		
		garantiaOutros2.setCota(cotaCiclana);
		garantiaOutros2.setData(new Date());
		garantiaOutros2.setOutros(outros2);
		
		save(garantiaOutros1, garantiaOutros2);
	}
}
