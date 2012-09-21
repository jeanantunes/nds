package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.AssociacaoEndereco;
import br.com.abril.nds.model.cadastro.AssociacaoTelefone;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
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
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoBoleto;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaChequeCaucao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFiador;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFornecedor;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaGarantia;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaImovel;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaNotaPromissoria;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaReferenciaCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaTelefone;
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
	private HistoricoTitularidadeService historicoTitularidadeService;
	
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

		HistoricoTitularidadeCota historicoCota = 
				this.historicoTitularidadeService.gerarHistoricoTitularidadeCota(this.cota);
		
		assertDadosCadastraisCota(this.cota, historicoCota);
		assertAssociacaoEnderecos(this.cota.getEnderecos(), historicoCota.getEnderecos());
		assertAssociacaoTelefones(this.cota.getTelefones(), historicoCota.getTelefones());
		assertPDV(this.cota.getPdvs(), (List<HistoricoTitularidadeCotaPDV>) historicoCota.getPdvs());
		assertGarantias(this.cota.getCotaGarantia(), historicoCota.getGarantias());
		assertFornecedores(this.cota.getFornecedores(), historicoCota.getFornecedores());
		assertParametroCobranca(this.cota.getParametroCobranca(), historicoCota.getFinanceiro());
		assertParametroDistribuicao(this.cota.getParametroDistribuicao(), historicoCota.getDistribuicao());
		assertSocios(this.cota.getSociosCota(), historicoCota.getSocios());
		
		testarGarantiasCota();
	}

	private void testarGarantiasCota() {

		for (TipoGarantia tipoGarantia : TipoGarantia.values()) {

			this.cota.setCotaGarantia(getCotaGarantia(tipoGarantia));
			
			if (this.cota.getCotaGarantia() == null) {
				
				return;
			}
	
			HistoricoTitularidadeCota historicoCota = 
					this.historicoTitularidadeService.gerarHistoricoTitularidadeCota(this.cota);
			
			assertGarantias(this.cota.getCotaGarantia(), historicoCota.getGarantias());
		}
	}
	
	private void assertDadosCadastraisCota(Cota cota, HistoricoTitularidadeCota historicoCota) {

		Assert.assertEquals(historicoCota.getInicio(), cota.getInicioAtividade());
		Assert.assertEquals(historicoCota.getEmail(), cota.getPessoa().getEmail());
//		Assert.assertEquals(historicoCota.getFim(), cota.get);
		Assert.assertEquals(historicoCota.getInicioPeriodoCotaBase(), cota.getBaseReferenciaCota().getInicioPeriodo());
		Assert.assertEquals(historicoCota.getFimPeriodoCotaBase(), cota.getBaseReferenciaCota().getFinalPeriodo());
		Assert.assertEquals(historicoCota.getNumeroCota(), cota.getNumeroCota());
		Assert.assertEquals(historicoCota.getSituacaoCadastro(), cota.getSituacaoCadastro());
		Assert.assertEquals(historicoCota.getClassificacaoExpectativaFaturamento(), cota.getClassificacaoEspectativaFaturamento());
		
		Collection<HistoricoTitularidadeCotaReferenciaCota> historicoReferencias = historicoCota.getReferencias();
		Set<ReferenciaCota> referenciasCota = cota.getBaseReferenciaCota().getReferenciasCota();
		
		boolean sameSizeList = historicoReferencias.size() == referenciasCota.size();
		
		Assert.assertTrue(sameSizeList);
		
		Iterator<ReferenciaCota> referenciasCotaIterator = referenciasCota.iterator();
		Iterator<HistoricoTitularidadeCotaReferenciaCota> historicoIterator = historicoReferencias.iterator();

		while (referenciasCotaIterator.hasNext() && historicoIterator.hasNext()) {

			ReferenciaCota referenciaCota = referenciasCotaIterator.next(); 
			HistoricoTitularidadeCotaReferenciaCota historicoReferencia = historicoIterator.next();

			Assert.assertEquals(historicoReferencia.getNumeroCota(), referenciaCota.getCota().getNumeroCota());
			Assert.assertEquals(historicoReferencia.getPercentual(), referenciaCota.getPercentual());
		}
	}

	@SuppressWarnings("unchecked")
	private void assertAssociacaoEnderecos(Set<? extends AssociacaoEndereco> enderecos, 
										   Collection<HistoricoTitularidadeCotaEndereco> historicoEnderecos) {

		Assert.assertNotNull(enderecos);
		Assert.assertNotNull(historicoEnderecos);

		boolean sameSizeList = enderecos.size() == historicoEnderecos.size();

		Assert.assertTrue(sameSizeList);
		
		Iterator<AssociacaoEndereco> enderecoIterator = (Iterator<AssociacaoEndereco>) enderecos.iterator();
		Iterator<HistoricoTitularidadeCotaEndereco> historicoIterator = historicoEnderecos.iterator();
		
		while (enderecoIterator.hasNext() && historicoIterator.hasNext()) {
			
			AssociacaoEndereco enderecoCota = enderecoIterator.next();
			Endereco endereco = enderecoCota.getEndereco();

			HistoricoTitularidadeCotaEndereco historicoEndereco = historicoIterator.next();
			
			Assert.assertNotNull(endereco);
			
			Assert.assertEquals(historicoEndereco.getTipoEndereco(), enderecoCota.getTipoEndereco());

			assertEndereco(endereco, historicoEndereco);
		}
	}

	private void assertEndereco(Endereco endereco, HistoricoTitularidadeCotaEndereco historicoEndereco) {
		
		Assert.assertEquals(historicoEndereco.getBairro(), endereco.getBairro());
		Assert.assertEquals(historicoEndereco.getCep(), endereco.getCep());
		Assert.assertEquals(historicoEndereco.getCidade(), endereco.getCidade());
		Assert.assertEquals(historicoEndereco.getComplemento(), endereco.getComplemento());
		Assert.assertEquals(historicoEndereco.getLogradouro(), endereco.getLogradouro());
		Assert.assertEquals(historicoEndereco.getNumero(), endereco.getNumero());
		Assert.assertEquals(historicoEndereco.getTipoLogradouro(), endereco.getTipoLogradouro());
		Assert.assertEquals(historicoEndereco.getUf(), endereco.getUf());
		Assert.assertEquals(historicoEndereco.getCodigoBairro(), endereco.getCodigoBairro());
		Assert.assertEquals(historicoEndereco.getCodigoCidadeIBGE(), endereco.getCodigoCidadeIBGE());
		Assert.assertEquals(historicoEndereco.getCodigoUf(), endereco.getCodigoUf());
	}

	@SuppressWarnings("unchecked")
	private void assertAssociacaoTelefones(Set<? extends AssociacaoTelefone> telefones, 
										   Collection<HistoricoTitularidadeCotaTelefone> historicoTelefones) {
		
		Assert.assertNotNull(telefones);
		Assert.assertNotNull(historicoTelefones);
		
		boolean sameSize = telefones.size() == historicoTelefones.size();
		
		Assert.assertTrue(sameSize);
		
		Iterator<AssociacaoTelefone> telefoneIterator = (Iterator<AssociacaoTelefone>) telefones.iterator();
		Iterator<HistoricoTitularidadeCotaTelefone> historicoIterator = historicoTelefones.iterator();
		
		while (telefoneIterator.hasNext() && historicoIterator.hasNext()) {

			AssociacaoTelefone telefoneCota = telefoneIterator.next();
			HistoricoTitularidadeCotaTelefone historicoTelefone = historicoIterator.next();
			
			Assert.assertEquals(telefoneCota.getTipoTelefone(), historicoTelefone.getTipoTelefone());
			
			assertTelefone(telefoneCota.getTelefone(), historicoTelefone);
		}
	}
	
	private void assertTelefone(Telefone telefone, HistoricoTitularidadeCotaTelefone historicoTelefone) {
		
		Assert.assertEquals(telefone.getNumero(), historicoTelefone.getNumero());
		Assert.assertEquals(telefone.getRamal(), historicoTelefone.getRamal());
	}
	
	private void assertPDV(List<PDV> pdvs, List<HistoricoTitularidadeCotaPDV> historicoPDVs) {

		Assert.assertNotNull(pdvs);
		Assert.assertNotNull(historicoPDVs);
		
		boolean sameSize = pdvs.size() == historicoPDVs.size();
		
		Assert.assertTrue(sameSize);

		for (int i = 0; i < pdvs.size(); i++) {

			PDV pdv = pdvs.get(i);

			HistoricoTitularidadeCotaPDV historicoPDV = historicoPDVs.get(i);

			Assert.assertEquals(pdv.getContato(), historicoPDV.getContato());
			Assert.assertEquals(pdv.getEmail(), historicoPDV.getEmail());
			Assert.assertEquals(pdv.getNome(), historicoPDV.getNome());
			Assert.assertEquals(pdv.getPontoReferencia(), historicoPDV.getPontoReferencia());
			Assert.assertEquals(pdv.getQtdeFuncionarios(), historicoPDV.getQtdeFuncionarios());
			Assert.assertEquals(pdv.getSite(), historicoPDV.getSite());
			Assert.assertEquals(pdv.getDataInclusao(), historicoPDV.getDataInclusao());
			Assert.assertEquals(pdv.getPorcentagemFaturamento(), historicoPDV.getPorcentagemFaturamento());
			Assert.assertEquals(pdv.getStatus(), historicoPDV.getStatus());
			Assert.assertEquals(pdv.getTamanhoPDV(), historicoPDV.getTamanhoPDV());
			Assert.assertEquals(pdv.getCaracteristicas().getTextoLuminoso(), historicoPDV.getCaracteristicas().getTextoLuminoso());
			Assert.assertEquals(pdv.getCaracteristicas().isBalcaoCentral(), historicoPDV.getCaracteristicas().isBalcaoCentral());
			Assert.assertEquals(pdv.getCaracteristicas().isPontoPrincipal(), historicoPDV.getCaracteristicas().isPontoPrincipal());
			Assert.assertEquals(pdv.getCaracteristicas().isPossuiCartaoCredito(), historicoPDV.getCaracteristicas().isPossuiCartaoCredito());
			Assert.assertEquals(pdv.getCaracteristicas().isPossuiComputador(), historicoPDV.getCaracteristicas().isPossuiComputador());
			Assert.assertEquals(pdv.getCaracteristicas().isPossuiLuminoso(), historicoPDV.getCaracteristicas().isPossuiLuminoso());
			
			assertAssociacaoEnderecos(pdv.getEnderecos(), historicoPDV.getEnderecos());
			assertAssociacaoTelefones(pdv.getTelefones(), historicoPDV.getTelefones());
		}
	}
	
	private void assertFornecedores(Set<Fornecedor> fornecedores, Collection<HistoricoTitularidadeCotaFornecedor> historicoFornecedores) {

		Assert.assertNotNull(fornecedores);
		Assert.assertNotNull(historicoFornecedores);
		
		boolean sameSize = fornecedores.size() == historicoFornecedores.size();

		Assert.assertTrue(sameSize);
		
		Iterator<Fornecedor> fornecedorIterator = fornecedores.iterator();
		Iterator<HistoricoTitularidadeCotaFornecedor> historicoIterator = historicoFornecedores.iterator();
		
		while (fornecedorIterator.hasNext() && historicoIterator.hasNext()) {

			Fornecedor fornecedor = fornecedorIterator.next();
			HistoricoTitularidadeCotaFornecedor historicoFornecedor = historicoIterator.next();

			Assert.assertEquals(fornecedor.getJuridica().getCnpj(), historicoFornecedor.getPessoaJuridica().getCnpj());
			Assert.assertEquals(fornecedor.getJuridica().getInscricaoEstadual(), historicoFornecedor.getPessoaJuridica().getInscricaoEstadual());
			Assert.assertEquals(fornecedor.getJuridica().getInscricaoMunicipal(), historicoFornecedor.getPessoaJuridica().getInscricaoMunicipal());
			Assert.assertEquals(fornecedor.getJuridica().getNomeFantasia(), historicoFornecedor.getPessoaJuridica().getNomeFantasia());
			Assert.assertEquals(fornecedor.getJuridica().getRazaoSocial(), historicoFornecedor.getPessoaJuridica().getRazaoSocial());
		}
	}
	
	private void assertParametroCobranca(ParametroCobrancaCota cobrancaCota, HistoricoTitularidadeCotaFinanceiro historicoFinanceiro) {
		
		Assert.assertEquals(cobrancaCota.getTipoCota(), historicoFinanceiro.getTipoCota());
		Assert.assertEquals(cobrancaCota.getFatorVencimento(), historicoFinanceiro.getFatorVencimento());
		Assert.assertEquals(cobrancaCota.getValorMininoCobranca(), historicoFinanceiro.getValorMininoCobranca());
		Assert.assertEquals(cobrancaCota.getPoliticaSuspensao().getValor(), historicoFinanceiro.getPoliticaSuspensao().getValor());
		Assert.assertEquals(cobrancaCota.getPoliticaSuspensao().getNumeroAcumuloDivida(), historicoFinanceiro.getPoliticaSuspensao().getNumeroAcumuloDivida());
		
		assertFormasPagamento(cobrancaCota.getFormasCobrancaCota(), historicoFinanceiro.getFormasPagamento());
	}
	
	private void assertFormasPagamento(Set<FormaCobranca> formasCobranca, Collection<HistoricoTitularidadeCotaFormaPagamento> historicoFormas) {

		Assert.assertNotNull(formasCobranca);
		Assert.assertNotNull(historicoFormas);
		
		boolean sameSize = formasCobranca.size() == historicoFormas.size();

		Assert.assertTrue(sameSize);
		
		Iterator<FormaCobranca> formasCobrancaIterator = formasCobranca.iterator();
		Iterator<HistoricoTitularidadeCotaFormaPagamento> historicoIterator = historicoFormas.iterator();
		
		while (formasCobrancaIterator.hasNext() && historicoIterator.hasNext()) {

			FormaCobranca cobranca = formasCobrancaIterator.next();
			HistoricoTitularidadeCotaFormaPagamento historicoCobranca = historicoIterator.next();
			
			Assert.assertEquals(historicoCobranca.getTipoCobranca(), cobranca.getTipoCobranca());
			
			Assert.assertEquals(historicoCobranca.getBanco().getDvAgencia(), cobranca.getBanco().getDvAgencia());
			Assert.assertEquals(historicoCobranca.getBanco().getDvConta(), cobranca.getBanco().getDvConta());
			Assert.assertEquals(historicoCobranca.getBanco().getNome(), cobranca.getBanco().getNome());
			Assert.assertEquals(historicoCobranca.getBanco().getNumeroBanco(), cobranca.getBanco().getNumeroBanco());
			Assert.assertEquals(historicoCobranca.getBanco().getAgencia(), cobranca.getBanco().getAgencia());
			Assert.assertEquals(historicoCobranca.getBanco().getConta(), cobranca.getBanco().getConta());

			assertFornecedores(cobranca.getFornecedores(), historicoCobranca.getFornecedores());
		}
	}
	
	private void assertParametroDistribuicao(ParametroDistribuicaoCota distribuicao, HistoricoTitularidadeCotaDistribuicao historicoDistribuicao) {
		
		Assert.assertEquals(historicoDistribuicao.getQtdePDV(), distribuicao.getQtdePDV());
		Assert.assertEquals(historicoDistribuicao.getObservacao(), distribuicao.getObservacao());
		Assert.assertEquals(historicoDistribuicao.getFimPeriodoCarencia(), distribuicao.getFimPeriodoCarencia());
		Assert.assertEquals(historicoDistribuicao.getAssistenteComercial(), distribuicao.getAssistenteComercial());
		Assert.assertEquals(historicoDistribuicao.getInicioPeriodoCarencia(), distribuicao.getInicioPeriodoCarencia());
		Assert.assertEquals(historicoDistribuicao.getPercentualFaturamentoEntrega(), distribuicao.getPercentualFaturamento());
		Assert.assertEquals(historicoDistribuicao.getTaxaFixaEntrega(), distribuicao.getTaxaFixa());
		Assert.assertEquals(historicoDistribuicao.getTipoEntrega(), distribuicao.getTipoEntrega().getDescricaoTipoEntrega());
	}
	
	private void assertSocios(Set<SocioCota> socios, Collection<HistoricoTitularidadeCotaSocio> historicoSocios) {

		Assert.assertNotNull(socios);
		Assert.assertNotNull(historicoSocios);
		
		boolean sameSize = socios.size() == historicoSocios.size();

		Assert.assertTrue(sameSize);
		
		Iterator<SocioCota> sociosIterator = socios.iterator();
		Iterator<HistoricoTitularidadeCotaSocio> historicoIterator = historicoSocios.iterator();
		
		while (sociosIterator.hasNext() && historicoIterator.hasNext()) {

			SocioCota socioCota = sociosIterator.next();
			HistoricoTitularidadeCotaSocio historicoSocio = historicoIterator.next();
			
			Assert.assertEquals(historicoSocio.getCargo(), socioCota.getCargo());
			Assert.assertEquals(historicoSocio.getNome(), socioCota.getNome());

			assertTelefone(socioCota.getTelefone(), historicoSocio.getTelefone());
			assertEndereco(socioCota.getEndereco(), historicoSocio.getEndereco());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void assertGarantias(CotaGarantia garantia, Collection<? extends HistoricoTitularidadeCotaGarantia> historicoGarantias) {
		
		Assert.assertNotNull(garantia);
		Assert.assertNotNull(historicoGarantias);
		
		if (garantia instanceof CotaGarantiaFiador) {
			
			assertFiador(((CotaGarantiaFiador) garantia).getFiador(), (Collection<HistoricoTitularidadeCotaFiador>) historicoGarantias);
		
		} else if (garantia instanceof CotaGarantiaChequeCaucao) {
			
			assertChequeCaucao(((CotaGarantiaChequeCaucao) garantia).getCheque(), (Collection<HistoricoTitularidadeCotaChequeCaucao>) historicoGarantias);
		
		} else if (garantia instanceof CotaGarantiaCaucaoLiquida) {

			assertCaucaoLiquida((CotaGarantiaCaucaoLiquida) garantia, 
				(Collection<HistoricoTitularidadeCotaCaucaoLiquida>) historicoGarantias
			);
		
		} else if (garantia instanceof CotaGarantiaImovel) {
			
			assertImovel(
				(List<Imovel>) ((CotaGarantiaImovel) garantia).getImoveis(), 
				(Collection<HistoricoTitularidadeCotaImovel>) historicoGarantias
			);
		
		} else if (garantia instanceof CotaGarantiaNotaPromissoria) {
			
			assertNotaPromissoria(
				((CotaGarantiaNotaPromissoria) garantia).getNotaPromissoria(),
				(Collection<HistoricoTitularidadeCotaNotaPromissoria>) historicoGarantias
			);
		}
	}
	
	private void assertFiador(Fiador fiador, Collection<HistoricoTitularidadeCotaFiador> historicoGarantias) {
		
		Iterator<HistoricoTitularidadeCotaFiador> historicoIterator = (Iterator<HistoricoTitularidadeCotaFiador>) historicoGarantias.iterator();
		
		while (historicoIterator.hasNext()) {

			HistoricoTitularidadeCotaFiador historicoGarantia = historicoIterator.next();
			
			Assert.assertEquals(historicoGarantia.getNome(), fiador.getPessoa().getNome());
			Assert.assertEquals(historicoGarantia.getCpfCnpj(), fiador.getPessoa().getDocumento());
			
			for (EnderecoFiador enderecoFiador : fiador.getEnderecoFiador()) {
				
				if (enderecoFiador.isPrincipal()) {

					assertEndereco(enderecoFiador.getEndereco(), historicoGarantia.getHistoricoTitularidadeCotaEndereco());
				}
			}

			for (TelefoneFiador telefoneFiador : fiador.getTelefonesFiador()) {
				
				if (telefoneFiador.isPrincipal()) {

					assertTelefone(telefoneFiador.getTelefone(), historicoGarantia.getHistoricoTitularidadeCotaTelefone());
				}
			}
		}
	}
	
	private void assertChequeCaucao(Cheque cheque, Collection<HistoricoTitularidadeCotaChequeCaucao> historicoGarantias) {
		
		Iterator<HistoricoTitularidadeCotaChequeCaucao> iterator = historicoGarantias.iterator();
		
		while (iterator.hasNext()) {
			
			HistoricoTitularidadeCotaChequeCaucao historicoCheque = iterator.next();

			Assert.assertEquals(historicoCheque.getCorrentista(), cheque.getCorrentista());
			Assert.assertEquals(historicoCheque.getDvAgencia(), cheque.getDvAgencia());
			Assert.assertEquals(historicoCheque.getDvConta(), cheque.getDvConta());
			Assert.assertEquals(historicoCheque.getNomeBanco(), cheque.getNomeBanco());
			Assert.assertEquals(historicoCheque.getNumeroBanco(), cheque.getNumeroBanco());
			Assert.assertEquals(historicoCheque.getNumeroCheque(), cheque.getNumeroCheque());
			Assert.assertEquals(historicoCheque.getAgencia(), cheque.getAgencia());
			Assert.assertEquals(historicoCheque.getConta(), cheque.getConta());
			Assert.assertEquals(historicoCheque.getEmissao(), cheque.getEmissao().getTime());
			Assert.assertEquals(historicoCheque.getImagem(), cheque.getChequeImage().getImagem());
			Assert.assertEquals(historicoCheque.getValidade(), cheque.getValidade().getTime());
			Assert.assertEquals(historicoCheque.getValor(), new BigDecimal(cheque.getValor()));
		}
	}
	
	private void assertCaucaoLiquida(CotaGarantiaCaucaoLiquida garantia, Collection<HistoricoTitularidadeCotaCaucaoLiquida> historicoGarantias) {
		Iterator<HistoricoTitularidadeCotaCaucaoLiquida> iteratorHistorico = historicoGarantias.iterator();
		HistoricoTitularidadeCotaCaucaoLiquida historicoCaucaoLiquida = iteratorHistorico.next();
		
		Assert.assertEquals(garantia.getTipoCobranca(),  historicoCaucaoLiquida.getPagamento().getTipoCobranca());

		PagamentoBoleto formaPagamento = (PagamentoBoleto) garantia.getFormaPagamento();
		Assert.assertEquals(formaPagamento.getValor(), historicoCaucaoLiquida.getValor());
        Assert.assertEquals(formaPagamento.getQuantidadeParcelas(), historicoCaucaoLiquida.getPagamento().getQtdeParcelasBoleto());
        Assert.assertEquals(formaPagamento.getValorParcela(), historicoCaucaoLiquida.getPagamento().getValorParcelasBoleto());
        FormaCobrancaCaucaoLiquida formaCobrancaCaucaoLiquida = formaPagamento.getFormaCobrancaCaucaoLiquida();
        Assert.assertEquals(formaCobrancaCaucaoLiquida.getTipoFormaCobranca(), historicoCaucaoLiquida.getPagamento().getPeriodicidadeBoleto());
        Assert.assertEquals(formaCobrancaCaucaoLiquida.getDiasDoMes(), historicoCaucaoLiquida.getPagamento().getDiasMesBoleto());
	}

	private void assertImovel(List<Imovel> imoveis, Collection<HistoricoTitularidadeCotaImovel> historicoGarantias) {
		
		Iterator<HistoricoTitularidadeCotaImovel> iteratorHistorico = historicoGarantias.iterator();
		Iterator<Imovel> iteratorImovel = imoveis.iterator();
		
		while (iteratorHistorico.hasNext() && iteratorImovel.hasNext()) {
			
			Imovel imovel = iteratorImovel.next();
			HistoricoTitularidadeCotaImovel historicoImovel = iteratorHistorico.next();
			
			Assert.assertEquals(historicoImovel.getEndereco(), imovel.getEndereco());
			Assert.assertEquals(historicoImovel.getNumeroRegistro(), imovel.getNumeroRegistro());
			Assert.assertEquals(historicoImovel.getObservacao(), imovel.getObservacao());
			Assert.assertEquals(historicoImovel.getProprietario(), imovel.getProprietario());
			Assert.assertEquals(historicoImovel.getValor(), new BigDecimal(imovel.getValor()));
		}
	}
	
	private void assertNotaPromissoria(NotaPromissoria notaPromissoria, Collection<HistoricoTitularidadeCotaNotaPromissoria> historicoGarantias) {
	
		Iterator<HistoricoTitularidadeCotaNotaPromissoria> iterator = historicoGarantias.iterator();
		
		while (iterator.hasNext()) {
			
			HistoricoTitularidadeCotaNotaPromissoria historicoNotaPromissoria = iterator.next();
			
			Assert.assertEquals(historicoNotaPromissoria.getValorExtenso(), notaPromissoria.getValorExtenso());
			Assert.assertEquals(historicoNotaPromissoria.getValor(), new BigDecimal(notaPromissoria.getValor()));
			Assert.assertEquals(historicoNotaPromissoria.getVencimento(), notaPromissoria.getVencimento().getTime());
		}
	}
	
	/*
	 * Cria dados referentes a aba "Dados Cadastrais" do cadastro de cota.
	 */
	private Cota getDadosCadastrais() {
		
		Pessoa pessoa = Fixture.juridicaAbril();

		pessoa = merge(pessoa);
		
		Box box = Fixture.boxReparte300();

		box = merge(box);
		
		Cota cota = Fixture.cota(123, pessoa, SituacaoCadastro.ATIVO, null);
		
		BaseReferenciaCota baseReferenciaCota = new BaseReferenciaCota();
		
		baseReferenciaCota.setInicioPeriodo(new Date());

		baseReferenciaCota.setFinalPeriodo(new Date());

		Set<ReferenciaCota> referenciasCota = new HashSet<ReferenciaCota>();
		
		ReferenciaCota referenciaCota = new ReferenciaCota();
		referenciaCota.setPercentual(new BigDecimal(300));
		referenciaCota.setCota(merge(
			Fixture.cota(555, pessoa, SituacaoCadastro.ATIVO, null)
		));
		
		referenciasCota.add(referenciaCota);

		baseReferenciaCota.setReferenciasCota(referenciasCota);
		
		cota.setBaseReferenciaCota(baseReferenciaCota);
		
		return cota;
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
	 * Cria dados referentes aos "Enderecos" do PDV da cota.
	 */
	private Set<EnderecoPDV> getEnderecosPDV() {

		Set<EnderecoPDV> enderecosPDV = new HashSet<EnderecoPDV>();

		EnderecoPDV enderecoPDV = new EnderecoPDV();
		enderecoPDV.setEndereco(getEndereco());
		enderecoPDV.setPrincipal(true);
		enderecoPDV.setTipoEndereco(TipoEndereco.COBRANCA);
		
		enderecosPDV.add(enderecoPDV);
		
		return enderecosPDV;
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
	 * Cria dados referentes a aba "Telefones" do cadastro de cota.
	 */
	private Set<TelefonePDV> getTelefonePDV() {
		
		Set<TelefonePDV> telefonesPDV = new HashSet<TelefonePDV>();
		
		TelefonePDV telefonePDV = new TelefonePDV();
		
		telefonePDV.setTelefone(getTelefone());
		telefonePDV.setTipoTelefone(TipoTelefone.COMERCIAL);
		telefonePDV.setPrincipal(true);
		
		telefonesPDV.add(telefonePDV);
		
		return telefonesPDV;
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
		
		pdv.setEnderecos(getEnderecosPDV());
		pdv.setTelefones(getTelefonePDV());
				
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
		
		tipoFornecedor = merge(tipoFornecedor);
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedor);

		fornecedorDinap.setJuridica(merge(fornecedorDinap.getJuridica()));
		
		fornecedorDinap = merge(fornecedorDinap);
		
		fornecedores.add(fornecedorDinap);
		
		return fornecedores;
	}

	//TODO: aba desconto
	private void gerarDescontosCota(Cota cota) {

		Usuario usuario = merge(Fixture.usuarioJoao());

		Distribuidor distribuidor = merge(Fixture.distribuidor(
			123, 
			(PessoaJuridica) cota.getPessoa(), 
			new Date(), 
			null
		));

		DescontoCota descontoCota = Fixture.descontoCota(
			new BigDecimal(20), 
			distribuidor, 
			cota, 
			cota.getFornecedores(), 
			usuario, 
			new Date()
		);
		
		save(descontoCota);

		Produto produto = Fixture.produtoBoaForma(
			merge(Fixture.tipoCromo(
				merge(Fixture.ncm(32L, "descricao", "unidadeMedida"))
			))
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

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		
		politicaSuspensao.setNumeroAcumuloDivida(234);
		politicaSuspensao.setValor(new BigDecimal(100));
		
		parametroCobrancaCota.setPoliticaSuspensao(politicaSuspensao);
		
		return parametroCobrancaCota;
	}

	/*
	 * Cria dados referentes a aba "Distribuição" do cadastro de cota.
	 */
	private ParametroDistribuicaoCota getDistribuicao() {
		
		TipoEntrega tipoEntrega = Fixture.criarTipoEntrega(1L, DescricaoTipoEntrega.ENTREGADOR, Periodicidade.MENSAL);
		
		tipoEntrega.setPercentualFaturamento(400f);
		tipoEntrega.setBaseCalculo(BaseCalculo.FATURAMENTO_BRUTO);
		tipoEntrega.setTaxaFixa(new BigDecimal(10));

		tipoEntrega = merge(tipoEntrega);

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
			false
		);

		distribuicao.setInicioPeriodoCarencia(new Date());
		distribuicao.setFimPeriodoCarencia(new Date());
		distribuicao.setPercentualFaturamento(new BigDecimal(40));
		distribuicao.setTaxaFixa(new BigDecimal(1034));

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

		sociosCota.add(socioCota);

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
	
	/*k
	 * Cria um telefone.
	 */
	private Telefone getTelefone() {
		
		return Fixture.telefone("19", "36560000", "0909");
	}
	
	/*
	 * Gera formas de cobrança para a cota.
	 */
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

		formaCobranca.setFornecedores(this.cota.getFornecedores());
		
		formasCobranca.add(formaCobranca);
		
		return formasCobranca;
	}
	
	/*
	 * Cria um banco.
	 */
	private Banco getBanco() {
		
		return Fixture.hsbc();
	}
	
	/*
	 * Cria garantia do tipo Fiador.
	 */
	private CotaGarantiaFiador getFiador() {
		
		Fiador fiador = new Fiador();
		fiador.setInicioAtividade(new Date());
		fiador.setPessoa(this.cota.getPessoa());
		
		List<EnderecoFiador> enderecos = new ArrayList<EnderecoFiador>(); 
		
		EnderecoFiador enderecoFiador = new EnderecoFiador();
		enderecoFiador.setEndereco(getEndereco());
		enderecoFiador.setPrincipal(false);
		enderecos.add(enderecoFiador);

		enderecoFiador = new EnderecoFiador();
		
		enderecoFiador.setEndereco(
			Fixture.criarEndereco(
				null,
				"99898", 
				"RUA ABC", 
				"54", 
				"Vila Carvalho", 
				"São Paulo", 
				"SP", 
				13
			)
		);
		
		enderecoFiador.setPrincipal(true);
		enderecos.add(enderecoFiador);

		fiador.setEnderecoFiador(enderecos);
		
		List<TelefoneFiador> telefones = new ArrayList<TelefoneFiador>();
		
		TelefoneFiador telefoneFiador = new TelefoneFiador();
		telefoneFiador.setPrincipal(false);
		telefoneFiador.setTelefone(getTelefone());
		telefones.add(telefoneFiador);
		
		telefoneFiador = new TelefoneFiador();
		telefoneFiador.setPrincipal(true);
		telefoneFiador.setTelefone(
			Fixture.telefone(
				"5050", 
				"00000", 
				"05"
			)
		);
		telefones.add(telefoneFiador);

		fiador.setTelefonesFiador(telefones);

		CotaGarantiaFiador garantiaFiador = new CotaGarantiaFiador();
		
		garantiaFiador.setData(Calendar.getInstance());
		garantiaFiador.setFiador(fiador);
		
		return garantiaFiador;
	}
	
	/*
	 * Cria garantia do tipo Imóvel
	 */
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
	
	/*
	 * Cria garantia do tipo Caução Líquida
	 */
	private CotaGarantiaCaucaoLiquida getCaucaoLiquida() {

	    CotaGarantiaCaucaoLiquida garantiaCaucaoLiquida = new CotaGarantiaCaucaoLiquida();
	    garantiaCaucaoLiquida.setTipoCobranca(TipoCobrancaCotaGarantia.BOLETO);
	    PagamentoBoleto pagamento = new PagamentoBoleto();
	    FormaCobrancaCaucaoLiquida cobranca = new FormaCobrancaCaucaoLiquida();
	    cobranca.setTipoFormaCobranca(TipoFormaCobranca.MENSAL);
	    cobranca.setDiasDoMes(Arrays.asList(15));
	    pagamento.setFormaCobrancaCaucaoLiquida(cobranca);
	    pagamento.setQuantidadeParcelas(5);
	    pagamento.setValor(BigDecimal.valueOf(400));
	    pagamento.setValorParcela(BigDecimal.valueOf(80));
	    garantiaCaucaoLiquida.setFormaPagamento(pagamento);
	    
		CaucaoLiquida caucaoLiquida = new CaucaoLiquida();
		caucaoLiquida.setAtualizacao(Calendar.getInstance());
		caucaoLiquida.setValor(415.0);
		List<CaucaoLiquida> caucoesLiquidas = new ArrayList<CaucaoLiquida>();
		caucoesLiquidas.add(caucaoLiquida);
		
		garantiaCaucaoLiquida.setData(Calendar.getInstance());
		garantiaCaucaoLiquida.setCaucaoLiquidas(caucoesLiquidas);

		return garantiaCaucaoLiquida;
	}
	
	/*
	 * Cria garantia do tipo Cheque Caução
	 */
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
	
	/*
	 * Cria garantia do tipo Nota Promissória
	 */
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
