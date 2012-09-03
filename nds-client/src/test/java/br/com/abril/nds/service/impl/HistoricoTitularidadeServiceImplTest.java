package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.HistoricoTitularidadeService;

/**
 * Testa as funcionalidades da geração de histórico de titularidade da cota.
 * 
 * @author Discover Technology
 *
 */
public class HistoricoTitularidadeServiceImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private HistoricoTitularidadeService historicoTitularidadeServiceImpl;
	
	private Cota cota;
	
	/**
	 * Inicializa os dados necessários ao teste.
	 */
	@Before
	public void setup() {
	
		this.cota = getDadosCadastrais();
		
		this.cota.setEnderecos(getEnderecosCota());
		
		this.cota.setTelefones(getTelefoneCota());
		
		this.cota.setPdvs(getPDVs());
		
		this.cota.setCotaGarantia(getCotaGarantia(TipoGarantia.FIADOR));
		
		this.cota.setFornecedores(getFornecedores());
		
		this.cota.setParametroCobranca(getFinanceiro());
		
		this.cota.setParametroDistribuicao(getDistribuicao());

		this.cota.setSociosCota(getSociosCota());

		save(this.cota);

		gerarDescontosCota(this.cota);
	}
	
	/**
	 * Realiza o teste de geração de histórico.
	 */
	@Test
	public void gerarHistoricoTitularidadeCotaTeste() {
		
		Assert.assertTrue(true);
	}
	
	/*
	 * Cria dados referentes a aba "Dados Cadastrais" do cadastro de cota.
	 */
	private Cota getDadosCadastrais() {
		
		Pessoa pessoa = Fixture.juridicaAbril();
		
		Box box = Fixture.boxReparte300();
		
		return Fixture.cota(123, pessoa, SituacaoCadastro.ATIVO, box);
	}

	/*
	 * Cria dados referentes a aba "Enderecos" do cadastro de cota.
	 */
	private Set<EnderecoCota> getEnderecosCota() {

		Set<EnderecoCota> enderecosCota = new HashSet<EnderecoCota>();

		EnderecoCota enderecoCota = Fixture.enderecoCota(
			this.cota, 
			getEndereco(), 
			true, 
			TipoEndereco.COMERCIAL
		);
		
		enderecosCota.add(enderecoCota);
		
		return enderecosCota;
	}
	
	/*
	 * Cria dados referentes a aba "Telefones" do cadastro de cota.
	 */
	private Set<TelefoneCota> getTelefoneCota() {
		
		Set<TelefoneCota> telefonesCota = new HashSet<TelefoneCota>();
		
		TelefoneCota telefoneCota = new TelefoneCota();
		
		telefoneCota.setCota(this.cota);
		telefoneCota.setTelefone(getTelefone());
		telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
		telefoneCota.setPrincipal(true);
		
		telefonesCota.add(telefoneCota);
		
		return telefonesCota;
	}
	
	/*
	 * Cria dados referentes a aba "PDV" do cadastro de cota.
	 */
	private List<PDV> getPDVs() {

		List<PDV> listaPdvs = new ArrayList<PDV>(); 

		CaracteristicasPDV caracteristicas = Fixture.criarCaracteristicaPDV(
			true, true, true, true, "P-D-V"
		);

		LicencaMunicipal licencaMunicipal = Fixture.criarLicencaMunicipal(
			"nome", 
			"1350", 
			Fixture.criarTipoLicencaMunicipal(1L, "tipo licença")
		);

		SegmentacaoPDV segmentacao = Fixture.criarSegmentacaoPdv(
			Fixture.criarAreaInfluenciaPDV(1L, "area influencia"), 
			TipoCaracteristicaSegmentacaoPDV.CONVENCIONAL, 
			Fixture.criarTipoPontoPDV(1L, "tipo ponto"), 
			Fixture.criarTipoClusterPDV(1L, "tipo cluster")
		);

		PDV pdv = Fixture.criarPDV(
			"pdv 1", 
			new BigDecimal(10), 
			TamanhoPDV.M, this.cota, 
			true, 
			StatusPDV.ATIVO, 
			caracteristicas, 
			licencaMunicipal, 
			segmentacao
		);
				
		listaPdvs.add(pdv);

		return listaPdvs;
	}
	
	/*
	 * Cria dados referentes a aba "Garantia" do cadastro de cota.
	 */
	private CotaGarantia getCotaGarantia(TipoGarantia tipoGarantia) {

		switch(tipoGarantia) {
		
		case FIADOR:

			return getFiador();

		case IMOVEL:

			return getImovel();
			
		case CAUCAO_LIQUIDA:
			
			return getCaucaoLiquida();
			
		case CHEQUE_CAUCAO:
			
			return getChequeCaucao();
			
		case NOTA_PROMISSORIA:
			
			return getNotaPromissoria();
			
		default:

			return null;
		}
	}

	/*
	 * Cria dados referentes a aba "Fornecedores" do cadastro de cota.
	 */
	private Set<Fornecedor> getFornecedores() {
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedor);
		
		fornecedores.add(fornecedorDinap);
		
		Fornecedor fornecedorFC = Fixture.fornecedorFC(tipoFornecedor);
		
		fornecedores.add(fornecedorFC);
		
		return fornecedores;
	}

	//TODO: aba desconto
	private void gerarDescontosCota(Cota cota) {

		Usuario usuario = Fixture.usuarioJoao();

		Distribuidor distribuidor = Fixture.distribuidor(
			123, 
			Fixture.juridicaAbril(), 
			new Date(), 
			null
		);

		DescontoCota descontoCota = Fixture.descontoCota(
			new BigDecimal(20), 
			distribuidor, 
			cota, 
			cota.getFornecedores(), 
			usuario, 
			new Date()
		);
		
		save(usuario, distribuidor, descontoCota);

		Produto produto = Fixture.produtoBoaForma(
			Fixture.tipoCromo(
				Fixture.ncm(32L, "descricao", "unidadeMedida")
			)
		);

		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(
			"214", 
			1333L, 
			31, 
			32, 
			20L, 
			new BigDecimal(20), 
			new BigDecimal(20), 
			"6873510", 
			65465L, 
			produto, 
			new BigDecimal(64), 
			true
		);

		Set<Cota> cotas = new HashSet<Cota>();
		cotas.add(cota);

		DescontoProduto descontoProduto = new DescontoProduto();
		descontoProduto.setCotas(cotas);
		descontoProduto.setDataAlteracao(new Date());
		descontoProduto.setDesconto(new BigDecimal(10));
		descontoProduto.setDistribuidor(distribuidor);
		descontoProduto.setProdutoEdicao(produtoEdicao);
		descontoProduto.setUsuario(usuario);

		save(produto, produtoEdicao, descontoProduto);
	}
	
	/*
	 * Cria dados referentes a aba "Financeiro" do cadastro de cota.
	 */
	private ParametroCobrancaCota getFinanceiro() {

		ParametroCobrancaCota parametroCobrancaCota = Fixture.parametroCobrancaCota(
			getFormasCobranca(), 
			250, 
			new BigDecimal(500), 
			this.cota, 
			20, 
			true, 
			new BigDecimal(150), 
			TipoCota.CONSIGNADO
		);

		return parametroCobrancaCota;
	}

	/*
	 * Cria dados referentes a aba "Distribuição" do cadastro de cota.
	 */
	private ParametroDistribuicaoCota getDistribuicao() {
		
		TipoEntrega tipoEntrega = Fixture.criarTipoEntrega(1L, "descricao", Periodicidade.MENSAL);
		
		ParametroDistribuicaoCota distribuicao = Fixture.criarParametroDistribuidor(
			13, 
			"Assistente", 
			tipoEntrega, 
			"observacao", 
			true, 
			true, 
			true, 
			true, 
			false, 
			false, 
			true, 
			false, 
			false, 
			true
		);
		
		return distribuicao;
	}
	
	/*
	 * Cria dados referentes a aba "Sócios" do cadastro de cota.
	 */
	private Set<SocioCota> getSociosCota() {
		
		Set<SocioCota> sociosCota = new HashSet<SocioCota>();
		
		SocioCota socioCota = new SocioCota();
		
		socioCota.setCargo("Vendedor");
		socioCota.setCota(this.cota);
		socioCota.setEndereco(getEndereco());
		socioCota.setNome("Tanaka");
		socioCota.setPrincipal(true);
		socioCota.setTelefone(getTelefone());
		return sociosCota;
	}
	
	/*
	 * Cria um endereço.
	 */
	private Endereco getEndereco() {

		return Fixture.criarEndereco(
			null,
			"13730000", 
			"Avenida XYZ", 
			"44", 
			"Centro", 
			"Mococa", 
			"SP", 
			32
		);
	}
	
	/*
	 * Cria um telefone.
	 */
	private Telefone getTelefone() {
		
		return Fixture.telefone("19", "36560000", "0909");
	}
	
	private Set<FormaCobranca> getFormasCobranca() {
		
		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		
		FormaCobranca formaCobranca = Fixture.formaCobrancaBoleto(
			true, 
			new BigDecimal(20), 
			false, 
			getBanco(), 
			new BigDecimal(20), 
			new BigDecimal(20), 
			null
		); 

		formasCobranca.add(formaCobranca);
		
		return formasCobranca;
	}
	
	private Banco getBanco() {
		
		return Fixture.hsbc();
	}
	
	private CotaGarantiaFiador getFiador() {
		
		Fiador fiador = new Fiador();
		fiador.setInicioAtividade(new Date());
		fiador.setPessoa(this.cota.getPessoa());

		CotaGarantiaFiador garantiaFiador = new CotaGarantiaFiador();
		
		garantiaFiador.setData(Calendar.getInstance());
		garantiaFiador.setFiador(fiador);
		
		return garantiaFiador;
	}
	
	private CotaGarantiaImovel getImovel() {
		
		Imovel imovel = new Imovel();
		imovel.setEndereco("rua x");
		imovel.setNumeroRegistro("123");
		imovel.setObservacao("observacao");
		imovel.setProprietario("proprietario");
		imovel.setValor(150000.0);

		List<Imovel> imoveis = new ArrayList<Imovel>();
		imoveis.add(imovel);

		CotaGarantiaImovel cotaGarantiaImovel = new CotaGarantiaImovel();
		cotaGarantiaImovel.setData(Calendar.getInstance());
		cotaGarantiaImovel.setImoveis(imoveis);
		
		return cotaGarantiaImovel;
	}
	
	private CotaGarantiaCaucaoLiquida getCaucaoLiquida() {
		
		CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
		caucaoLiquida.setAtualizacao(Calendar.getInstance());
		caucaoLiquida.setIndiceReajuste(30.0);
		caucaoLiquida.setValor(400.0);
		
		List<CaucaoLiquida> caucoesLiquidas = new ArrayList<CaucaoLiquida>();
		caucoesLiquidas.add(caucaoLiquida);
		
		CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida = new CotaGarantiaCaucaoLiquida();
		garantiaCaucaoLiquida.setData(Calendar.getInstance());
		garantiaCaucaoLiquida.setCaucaoLiquidas(caucoesLiquidas);

		return garantiaCaucaoLiquida;
	}
	
	private CotaGarantiaChequeCaucao getChequeCaucao() {

		Cheque cheque = new Cheque();
		cheque.setAgencia(13L);
		cheque.setChequeImage(new ChequeImage());
		cheque.setConta(14L);
		cheque.setCorrentista("correntista");
		cheque.setDvAgencia("dvAgencia");
		cheque.setDvConta("dvConta");
		cheque.setEmissao(Calendar.getInstance());
		cheque.setNomeBanco("nomeBanco");
		cheque.setNumeroBanco("numeroBanco");
		cheque.setNumeroCheque("numeroCheque");
		cheque.setValidade(Calendar.getInstance());
		cheque.setValor(150.0);

		CotaGarantiaChequeCaucao garantiaChequeCaucao = new CotaGarantiaChequeCaucao();
		garantiaChequeCaucao.setData(Calendar.getInstance());
		garantiaChequeCaucao.setCheque(cheque);
		
		return garantiaChequeCaucao;
	}
	
	private CotaGarantiaNotaPromissoria getNotaPromissoria() {
		
		NotaPromissoria notaPromissoria = new NotaPromissoria();
		notaPromissoria.setValor(1000.0);
		notaPromissoria.setValorExtenso("Mil");
		notaPromissoria.setVencimento(Calendar.getInstance());
		
		CotaGarantiaNotaPromissoria garantiaNotaPromissoria = new CotaGarantiaNotaPromissoria();
		garantiaNotaPromissoria.setData(Calendar.getInstance());
		garantiaNotaPromissoria.setNotaPromissoria(notaPromissoria);

		return garantiaNotaPromissoria;
	}
}
