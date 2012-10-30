package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.util.DateUtil;

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
	

	private Usuario usuario;

	private ProdutoEdicao veja1;
	private EstoqueProdutoCota estoqueProdutoCota;

	private TipoMovimentoEstoque tipoMovimetnoEstoque1;
	private TipoMovimentoEstoque tipoMovimetnoEstoque2;
	private TipoMovimentoEstoque tipoMovimetnoEstoque3;
	private TipoMovimentoEstoque tipoMovimetnoEstoque4;
	private TipoMovimentoEstoque tipoMovimetnoEstoque5;

	private MovimentoEstoqueCota movimentoEstoque1;
	private MovimentoEstoqueCota movimentoEstoque2;
	private MovimentoEstoqueCota movimentoEstoque3;
	private MovimentoEstoqueCota movimentoEstoque4;
	private MovimentoEstoqueCota movimentoEstoque5;
	private MovimentoEstoqueCota movimentoEstoque6;
	private MovimentoEstoqueCota movimentoEstoque7;
	
	private Date data =Fixture.criarData(01, 10, 2010);

	
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
	
	
	public void setupReparteEncalhe(){

	    Fornecedor fornecedor;
		TipoFornecedor tipoFornecedorPublicacao;
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedor = Fixture.fornecedor(this.ciclana, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao, 0);
		save(fornecedor);

		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoRevista);
		Produto veja = Fixture.produtoVeja(tipoRevista);
		save(veja);
		veja.addFornecedor(fornecedor);
		save(veja);
		
		veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100),
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", veja, null, false);

		save(veja1);

		usuario = Fixture.usuarioJoao();
		save(usuario);
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(veja1, this.cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
	
		
		tipoMovimetnoEstoque1 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque1.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		save(tipoMovimetnoEstoque1);
	
		tipoMovimetnoEstoque2 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque2.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE);
		save(tipoMovimetnoEstoque2);
		
		tipoMovimetnoEstoque3 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque3.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);
		save(tipoMovimetnoEstoque3);
		
		tipoMovimetnoEstoque4 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque4.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		save(tipoMovimetnoEstoque4);
		
		tipoMovimetnoEstoque5 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque5.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_ENCALHE);
		save(tipoMovimetnoEstoque5);
		

		movimentoEstoque1 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaAbril, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque1);
		
		movimentoEstoque2 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaAbril, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque2);
		
		movimentoEstoque3 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaAbril, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque3);
		
		movimentoEstoque4 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque2, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaAcme, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque4);
		
		movimentoEstoque5 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque3, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaAcme, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque5);
		
		movimentoEstoque6 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque4, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaDinap, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque6);
		
		movimentoEstoque7 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque5, usuario, estoqueProdutoCota, BigInteger.TEN, this.cotaFC, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque7);
	}

	
	private FiltroRelatorioGarantiasDTO getFiltroGarantias(){
		
		FiltroRelatorioGarantiasDTO filtro = new FiltroRelatorioGarantiasDTO();
		filtro.setDataBaseCalculo(data);
		filtro.setTipoGarantia(null);
		filtro.setStatusGarantiaEnum(TipoStatusGarantia.VENCIDA);
		
		return filtro;
	}
	
	@Test
	public void obterGarantiasCadastradas() {

		List<RelatorioGarantiasDTO> garantias = this.cotaGarantiaRepository.obterGarantiasCadastradas(this.getFiltroGarantias());
		
		Assert.assertNotNull(garantias);
		
		int expectedListSize = 6;
		int actualListSize = garantias.size();
		
		Assert.assertEquals(expectedListSize, actualListSize);
		
		for (RelatorioGarantiasDTO garantia : garantias) {
			
			switch (garantia.getTipoGarantia()) {
			
			case CAUCAO_LIQUIDA:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(200)));
				break;
			case CHEQUE_CAUCAO:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(25)));
				break;
			case FIADOR:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(200)));
				break;
			case IMOVEL:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(200)));
				break;
			case NOTA_PROMISSORIA:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(50)));
				break;
			case OUTROS:
				Assert.assertEquals(0, garantia.getVlrTotal().compareTo(new BigDecimal(200)));
				break;
			default:
				Assert.fail();
				break;
			}
		}
	}
	
	@Test
	public void obterCountGarantiasCadastradas() {

		Long count = this.cotaGarantiaRepository.obterCountGarantiasCadastradas(this.getFiltroGarantias());
		
		Assert.assertNotNull(count);
	}
	
	private void criarGarantiasCaucaoLiquida() {
		
		PagamentoCaucaoLiquida pagamentoCaucaoLiquida = new PagamentoCaucaoLiquida();
		pagamentoCaucaoLiquida.setValor(BigDecimal.TEN);

		save(pagamentoCaucaoLiquida);
		
		List<CaucaoLiquida> caucoes1 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance().getTime());
			caucaoLiquida.setValor(BigDecimal.TEN);
			
			save(caucaoLiquida);
			
			caucoes1.add(caucaoLiquida);
		}
		
		List<CaucaoLiquida> caucoes2 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance().getTime());
			caucaoLiquida.setValor(BigDecimal.TEN);
			
			save(caucaoLiquida);
			
			caucoes2.add(caucaoLiquida);
		}

		List<CaucaoLiquida> caucoes3 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(Calendar.getInstance().getTime());
			caucaoLiquida.setValor(BigDecimal.TEN);
			
			save(caucaoLiquida);
			
			caucoes3.add(caucaoLiquida);
		}

		List<CaucaoLiquida> caucoes4 = new ArrayList<CaucaoLiquida>();
		
		for (int i = 0; i < 10; i ++) {

			CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
			caucaoLiquida.setAtualizacao(DateUtil.adicionarDias(data,i));
			caucaoLiquida.setValor(BigDecimal.TEN);
			
			save(caucaoLiquida);
			
			caucoes4.add(caucaoLiquida);
		}
		
		CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida1 = new CotaGarantiaCaucaoLiquida();
		CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida2 = new CotaGarantiaCaucaoLiquida();
		
		garantiaCaucaoLiquida1.setCaucaoLiquidas(caucoes1);
		garantiaCaucaoLiquida1.setCota(cotaAbril);
		garantiaCaucaoLiquida1.setData(data);
		garantiaCaucaoLiquida1.setFormaPagamento(pagamentoCaucaoLiquida);
		garantiaCaucaoLiquida1.setTipoCobranca(TipoCobrancaCotaGarantia.BOLETO);
		
		garantiaCaucaoLiquida2.setCaucaoLiquidas(caucoes2);
		garantiaCaucaoLiquida2.setCota(cotaAcme);
		garantiaCaucaoLiquida2.setData(data);
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
		cheque1.setEmissao(data);
		cheque1.setNomeBanco("Banco do Brasil");
		cheque1.setNumeroBanco("555");
		cheque1.setNumeroCheque("888");
		cheque1.setValidade(data);
		cheque1.setValor(BigDecimal.TEN);
		
		Cheque cheque2 = new Cheque();
		cheque2.setAgencia(234L);
		cheque2.setConta(234L);
		cheque2.setCorrentista("Ciclano");
		cheque2.setDvAgencia("1");
		cheque2.setDvConta("1");
		cheque2.setEmissao(data);
		cheque2.setNomeBanco("Banco Itau");
		cheque2.setNumeroBanco("555");
		cheque2.setNumeroCheque("888");
		cheque2.setValidade(data);
		cheque2.setValor(new BigDecimal(15));

		CotaGarantiaChequeCaucao garantiaChequeCaucao1 = new CotaGarantiaChequeCaucao();
		CotaGarantiaChequeCaucao garantiaChequeCaucao2 = new CotaGarantiaChequeCaucao();

		garantiaChequeCaucao1.setData(data);
		garantiaChequeCaucao1.setCota(cotaDinap);
		garantiaChequeCaucao1.setCheque(cheque1);
		
		garantiaChequeCaucao2.setData(data);
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
			imovel.setValor(BigDecimal.TEN);
			
			imoveis1.add(imovel);
		}
		
		List<Imovel> imoveis2 = new ArrayList<Imovel>();
		
		for (int i = 0; i < 10; i++) {
			
			Imovel imovel = new Imovel();
			imovel.setEndereco("Endereco");
			imovel.setNumeroRegistro("123" + i);
			imovel.setObservacao("observação");
			imovel.setProprietario("proprietário");
			imovel.setValor(BigDecimal.TEN);
			
			imoveis2.add(imovel);
		}
		
		CotaGarantiaImovel garantiaImovel1 = new CotaGarantiaImovel();
		CotaGarantiaImovel garantiaImovel2 = new CotaGarantiaImovel();
		
		garantiaImovel1.setCota(cotaManoel);
		garantiaImovel1.setData(data);
		garantiaImovel1.setImoveis(imoveis1);
		
		garantiaImovel2.setCota(cotaMaria);
		garantiaImovel2.setData(data);
		garantiaImovel2.setImoveis(imoveis2);
		
		save(garantiaImovel1, garantiaImovel2);
	}
	
	private void criarGarantiasFiador() {
		
		Fiador fiador1 = new Fiador();
		Fiador fiador2 = new Fiador();
		
		fiador1.setInicioAtividade(data);
		fiador1.setPessoa(joao);
		
		fiador2.setInicioAtividade(data);
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
		garantiaFiador1.setData(data);
		garantiaFiador1.setFiador(fiador1);
		
		garantiaFiador2.setCota(cotaJose);
		garantiaFiador2.setData(data);
		garantiaFiador2.setFiador(fiador2);
		
		save(garantiaFiador1, garantiaFiador2);
	}
	
	private void criarGarantiasNotaPromissoria() {
		
		NotaPromissoria notaPromissoria1 = new NotaPromissoria();
		
		notaPromissoria1.setValor(new BigDecimal(20));
		notaPromissoria1.setValorExtenso("Vinte");
		notaPromissoria1.setVencimento(data);
		
		NotaPromissoria notaPromissoria2 = new NotaPromissoria();
		
		notaPromissoria2.setValor(new BigDecimal(30));
		notaPromissoria2.setValorExtenso("Trinta");
		notaPromissoria2.setVencimento(data);
		
		save(notaPromissoria1, notaPromissoria2);
		
		CotaGarantiaNotaPromissoria garantiaNotaPromissoria1 = new CotaGarantiaNotaPromissoria();
		CotaGarantiaNotaPromissoria garantiaNotaPromissoria2 = new CotaGarantiaNotaPromissoria();
		
		garantiaNotaPromissoria1.setCota(cotaSandra);
		garantiaNotaPromissoria1.setData(data);
		garantiaNotaPromissoria1.setNotaPromissoria(notaPromissoria1);
		
		garantiaNotaPromissoria2.setCota(cotaMadalena);
		garantiaNotaPromissoria2.setData(data);
		garantiaNotaPromissoria2.setNotaPromissoria(notaPromissoria2);
		
		save(garantiaNotaPromissoria1, garantiaNotaPromissoria2);
	}

	private void criarGarantiasOutros() {
	
		List<GarantiaCotaOutros> outros1 = new ArrayList<GarantiaCotaOutros>();
		
		for (int i = 0; i < 10; i++) {
			
			GarantiaCotaOutros outro = new GarantiaCotaOutros();
			outro.setDescricao("descrição");
			outro.setValidade(data);
			outro.setValor(BigDecimal.TEN);
			
			outros1.add(outro);
		}
		 
		List<GarantiaCotaOutros> outros2 = new ArrayList<GarantiaCotaOutros>();
		
		for (int i = 0; i < 10; i++) {
			
			GarantiaCotaOutros outro = new GarantiaCotaOutros();
			outro.setDescricao("descrição");
			outro.setValidade(data);
			outro.setValor(BigDecimal.TEN);
			
			outros2.add(outro);
		}
		
		CotaGarantiaOutros garantiaOutros1 = new CotaGarantiaOutros();
		CotaGarantiaOutros garantiaOutros2 = new CotaGarantiaOutros();
		
		garantiaOutros1.setCota(cotaFulana);
		garantiaOutros1.setData(data);
		garantiaOutros1.setOutros(outros1);
		
		garantiaOutros2.setCota(cotaCiclana);
		garantiaOutros2.setData(data);
		garantiaOutros2.setOutros(outros2);
		
		save(garantiaOutros1, garantiaOutros2);
	}
	
	
	@Test
	public void obterDetalheGarantiaCadastrada() {
		
		this.setupReparteEncalhe();
		
		FiltroRelatorioGarantiasDTO filtro = this.getFiltroGarantias();
		filtro.setTipoGarantiaEnum(TipoGarantia.CAUCAO_LIQUIDA);

		List<RelatorioDetalheGarantiaDTO> detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(10)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(10)));
		
		filtro.setTipoGarantiaEnum(TipoGarantia.CHEQUE_CAUCAO);

		detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(15)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(10)));
		
		filtro.setTipoGarantiaEnum(TipoGarantia.FIADOR);

		detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(10)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(10)));
        
		filtro.setTipoGarantiaEnum(TipoGarantia.NOTA_PROMISSORIA);

		detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(20)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(30)));
		
		filtro.setTipoGarantiaEnum(TipoGarantia.IMOVEL);

		detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(10)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(10)));
		
		filtro.setTipoGarantiaEnum(TipoGarantia.OUTROS);

        detalhesGgarantia = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro);
		Assert.assertNotNull(detalhesGgarantia);
		Assert.assertEquals(0, detalhesGgarantia.get(0).getVlrGarantia().compareTo(new BigDecimal(10)));
		Assert.assertEquals(0, detalhesGgarantia.get(1).getVlrGarantia().compareTo(new BigDecimal(10)));
	}
	
	@Test
	public void obterCountDetalheGarantiaCadastrada() {
		
		this.setupReparteEncalhe();

		FiltroRelatorioGarantiasDTO filtro = this.getFiltroGarantias();
		filtro.setTipoGarantiaEnum(TipoGarantia.CAUCAO_LIQUIDA);
		
		Integer count = this.cotaGarantiaRepository.obterCountDetalheGarantiaCadastrada(filtro).intValue();
		Assert.assertNotNull(count);
		Assert.assertEquals(2, count.intValue());
	}

}
