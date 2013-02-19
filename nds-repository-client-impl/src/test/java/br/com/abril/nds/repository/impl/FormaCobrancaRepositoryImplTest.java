package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.util.DateUtil;

public class FormaCobrancaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private FormaCobrancaRepositoryImpl formaCobrancaRepositoryImpl;
	
	private Distribuidor distribuidor;
	
	private Pessoa pessoaPf;
	
	private Pessoa pessoaPj;
	
	private Fornecedor fornecedor1;
	
	private Fornecedor fornecedor2;	
	
	private Fornecedor fornecedor3;	
	
	private Box box;
	
	private Cota cota;
	
	private Banco banco;
	
	private ParametroCobrancaCota parametroCobrancaCota;
	
	private PoliticaCobranca politicaCobranca1;
	
	private PoliticaCobranca politicaCobranca2;
	
	private PoliticaCobranca politicaCobranca3;
	
	private PoliticaCobranca politicaCobranca4;
	
	private Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
	
	private FormaCobranca formaCobranca1;
	
	private FormaCobranca formaCobranca2;
	
	private FormaCobranca formaCobranca3;
	
	private FormaCobranca formaCobranca4;
	
	private Set<FormaCobranca> formasCobranca;
	
	private Set<ConcentracaoCobrancaCota> concentracoesCobranca;

	private Set<Fornecedor> fornecedores;
	
	private Set<Fornecedor> fornecedores2;
	
	private Date data;
	
	private BigDecimal valor;
	
	
	public void carregarFormasCobrancaCota(){
		
		
        Integer diaSemana =  DateUtil.obterDiaDaSemana(new Date());
		
		Integer diaMes = DateUtil.obterDiaDoMes(new Date());
		
		
		pessoaPf = Fixture.pessoaFisica("123.456.789-00", "manoel@mail.com", "Manoel da Silva");
		save(pessoaPf);
		
		pessoaPj = Fixture.juridicaFC();
		save(pessoaPj);
		
		box = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box);
		
		cota = Fixture.cota(123, pessoaPf, SituacaoCadastro.ATIVO, box);
		cota.setId(null);
		save(cota);

		
        concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		
		ConcentracaoCobrancaCota concentracao1 = new ConcentracaoCobrancaCota();
		concentracao1.setId(null);
		concentracao1.setDiaSemana(DiaSemana.getByCodigoDiaSemana(diaSemana));
		save(concentracao1);
		
		ConcentracaoCobrancaCota concentracao2 = new ConcentracaoCobrancaCota();
		concentracao2.setId(null);
		concentracao2.setDiaSemana(DiaSemana.QUARTA_FEIRA);
		save(concentracao2);
		
		ConcentracaoCobrancaCota concentracao3 = new ConcentracaoCobrancaCota();
		concentracao3.setId(null);
		concentracao3.setDiaSemana(DiaSemana.SABADO);
		save(concentracao3);
		
		concentracoesCobranca.add(concentracao1);
		concentracoesCobranca.add(concentracao2);
		concentracoesCobranca.add(concentracao3);
		
		
		parametroCobrancaCota = Fixture.parametroCobrancaCota(formasCobranca, 3, BigDecimal.TEN, cota, 1, true, new BigDecimal(10), TipoCota.CONSIGNADO);
		parametroCobrancaCota.setId(null);
		save(parametroCobrancaCota);
		
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("Tipo Publicação", GrupoFornecedor.PUBLICACAO);
        
		
		fornecedor1 = Fixture.fornecedorAcme(tipoFornecedor);
        fornecedor1.setId(null);
		save(fornecedor1);
        
		fornecedor2 = Fixture.fornecedorDinap(tipoFornecedor);
		fornecedor2.setId(null);
		save(fornecedor2);
		
		fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor1);
		fornecedores.add(fornecedor2);

		
		banco = Fixture.banco(123l, true, 1, "123", 2l, "1", "1", "instrucoes", "apelido", "nome", "0809", BigDecimal.ONE, BigDecimal.TEN);
		banco.setId(null);
		save(banco);
		
		
		formaCobranca1 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, parametroCobrancaCota);
		formaCobranca1.setId(null);
		formaCobranca1.setConcentracaoCobrancaCota(concentracoesCobranca);
		formaCobranca1.setPrincipal(true);
		formaCobranca1.setAtiva(true);
		formaCobranca1.setFornecedores(fornecedores);
		formaCobranca1.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaCobranca1.setTipoCobranca(TipoCobranca.BOLETO);
		
		save(formaCobranca1);
		
		concentracao1.setFormaCobranca(formaCobranca1);
		save(concentracao1);
		
		concentracao2.setFormaCobranca(formaCobranca1);
		save(concentracao2);
		
		concentracao3.setFormaCobranca(formaCobranca1);
		save(concentracao3);
		
		
		concentracao1.setFormaCobranca(formaCobranca1);
		save(concentracao1);
		concentracao2.setFormaCobranca(formaCobranca1);
		save(concentracao2);
		concentracao3.setFormaCobranca(formaCobranca1);
		save(concentracao3);
		
		
		formaCobranca2 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, parametroCobrancaCota);
		formaCobranca2.setId(null);
		formaCobranca2.setDiasDoMes(Arrays.asList(new Integer(10), diaMes));
		formaCobranca2.setPrincipal(false);
		formaCobranca2.setAtiva(true);
		formaCobranca2.setTipoFormaCobranca(TipoFormaCobranca.QUINZENAL);
		formaCobranca2.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca2);
		
		formaCobranca3 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, parametroCobrancaCota);
		formaCobranca3.setId(null);
		formaCobranca3.setPrincipal(false);
		formaCobranca3.setAtiva(true);
		formaCobranca3.setTipoFormaCobranca(TipoFormaCobranca.DIARIA);
		formaCobranca3.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca3);
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaCobranca1);
		formasCobranca.add(formaCobranca2);
		formasCobranca.add(formaCobranca3);
		
		
		parametroCobrancaCota.setFormasCobrancaCota(formasCobranca);
		save(parametroCobrancaCota);
		
		
		cota.setParametroCobranca(parametroCobrancaCota);
		save(cota);
		
		
		formaCobranca1.setParametroCobrancaCota(parametroCobrancaCota);
		save(formaCobranca1);
		
		formaCobranca2.setParametroCobrancaCota(parametroCobrancaCota);
		save(formaCobranca2);
		
		formaCobranca3.setParametroCobrancaCota(parametroCobrancaCota);
		save(formaCobranca3);
	}
	
	public void carregarFormasCobrancaDistribuidor(){
		
		pessoaPf = Fixture.pessoaFisica("123.456.789-00", "manoel@mail.com", "Manoel da Silva");
		save(pessoaPf);
		
		pessoaPj = Fixture.juridicaFC();
		save(pessoaPj);

		
        concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		
		ConcentracaoCobrancaCota concentracao1 = new ConcentracaoCobrancaCota();
		concentracao1.setId(null);
		concentracao1.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
		save(concentracao1);
		
		ConcentracaoCobrancaCota concentracao2 = new ConcentracaoCobrancaCota();
		concentracao2.setId(null);
		concentracao2.setDiaSemana(DiaSemana.QUARTA_FEIRA);
		save(concentracao2);
		
		ConcentracaoCobrancaCota concentracao3 = new ConcentracaoCobrancaCota();
		concentracao3.setId(null);
		concentracao3.setDiaSemana(DiaSemana.SABADO);
		save(concentracao3);
		
		concentracoesCobranca.add(concentracao1);
		concentracoesCobranca.add(concentracao2);
		concentracoesCobranca.add(concentracao3);
		
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("Tipo Publicação", GrupoFornecedor.PUBLICACAO);
        
		
		fornecedor1 = Fixture.fornecedorAcme(tipoFornecedor);
        fornecedor1.setId(null);
		save(fornecedor1);
		
		fornecedor2 = Fixture.fornecedorDinap(tipoFornecedor);
		fornecedor2.setId(null);
		save(fornecedor2);
		
		fornecedor3 = Fixture.fornecedor((PessoaJuridica)pessoaPj, SituacaoCadastro.ATIVO, true, tipoFornecedor, 1);
		fornecedor3.setId(null);
		save(fornecedor3);
		
		fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor1);
		
		fornecedores2 = new HashSet<Fornecedor>();
        fornecedores2.add(fornecedor3);
        
		
		banco = Fixture.banco(123l, true, 1, "123", 2l, "1", "1", "instrucoes", "apelido", "nome", "0809", BigDecimal.ONE, BigDecimal.TEN);
		banco.setId(null);
		save(banco);
		
		
		formaCobranca1 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, null);
		formaCobranca1.setId(null);
		formaCobranca1.setConcentracaoCobrancaCota(concentracoesCobranca);
		formaCobranca1.setPrincipal(false);
		formaCobranca1.setAtiva(true);
		formaCobranca1.setFornecedores(fornecedores);
		formaCobranca1.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaCobranca1.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca1);
		
		concentracao1.setFormaCobranca(formaCobranca1);
		save(concentracao1);
		
		concentracao2.setFormaCobranca(formaCobranca1);
		save(concentracao2);
		
		concentracao3.setFormaCobranca(formaCobranca1);
		save(concentracao3);
		
		concentracao1.setFormaCobranca(formaCobranca1);
		save(concentracao1);
		concentracao2.setFormaCobranca(formaCobranca1);
		save(concentracao2);
		concentracao3.setFormaCobranca(formaCobranca1);
		save(concentracao3);
		
		
		formaCobranca2 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, null);
		formaCobranca2.setId(null);
		formaCobranca2.setDiasDoMes(Arrays.asList(new Integer(16),new Integer(19)));
		formaCobranca2.setPrincipal(false);
		formaCobranca2.setAtiva(true);
		formaCobranca2.setTipoFormaCobranca(TipoFormaCobranca.QUINZENAL);
		formaCobranca2.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca2);
		
		
		formaCobranca3 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, null);
		formaCobranca3.setId(null);
		formaCobranca3.setDiasDoMes(Arrays.asList(new Integer(10)));
		formaCobranca3.setPrincipal(true);
		formaCobranca3.setAtiva(true);
		formaCobranca3.setTipoFormaCobranca(TipoFormaCobranca.MENSAL);
		formaCobranca3.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca3);
		
		
		formaCobranca4 = Fixture.formaCobrancaBoleto(true, BigDecimal.TEN, true, banco, BigDecimal.ONE, BigDecimal.TEN, null);
		formaCobranca4.setId(null);
		formaCobranca4.setPrincipal(false);
		formaCobranca4.setAtiva(true);
		formaCobranca4.setFornecedores(fornecedores2);
		formaCobranca4.setTipoFormaCobranca(TipoFormaCobranca.DIARIA);
		formaCobranca4.setTipoCobranca(TipoCobranca.BOLETO);
		save(formaCobranca4);
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaCobranca1);
		formasCobranca.add(formaCobranca2);
		formasCobranca.add(formaCobranca3);
		formasCobranca.add(formaCobranca4);
		
		
        distribuidor = Fixture.distribuidor(123, (PessoaJuridica) pessoaPj, new Date(), politicasCobranca);
        save(distribuidor);
		
		politicaCobranca1 = Fixture.criarPoliticaCobranca(distribuidor, formaCobranca1, false, FormaEmissao.NAO_IMPRIME);
        save(politicaCobranca1);
		
		politicaCobranca2 = Fixture.criarPoliticaCobranca(distribuidor, formaCobranca2, false, FormaEmissao.NAO_IMPRIME);
		save(politicaCobranca2);
		
		politicaCobranca3 = Fixture.criarPoliticaCobranca(distribuidor, formaCobranca3, true, FormaEmissao.NAO_IMPRIME);
		save(politicaCobranca3);
		
		politicaCobranca4 = Fixture.criarPoliticaCobranca(distribuidor, formaCobranca4, false, FormaEmissao.NAO_IMPRIME);
		save(politicaCobranca4);
		
		
		formaCobranca1.setPoliticaCobranca(politicaCobranca1);
		save(formaCobranca1);
		
		formaCobranca2.setPoliticaCobranca(politicaCobranca2);
		save(formaCobranca2);
		
		formaCobranca3.setPoliticaCobranca(politicaCobranca3);
		save(formaCobranca3);
		
		formaCobranca3.setPoliticaCobranca(politicaCobranca4);
		save(formaCobranca4);
	}
	
	@Test
	public void testarObterPorTipoEBanco() {
		
		this.carregarFormasCobrancaCota();
		
		FormaCobranca formaCobranca;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterPorTipoEBanco(TipoCobranca.BOLETO, banco);
		
		Assert.assertNotNull(formaCobranca);
		
	}
	
	@Test
	public void testarObterBancosPorTipoDeCobranca() {
		
		this.carregarFormasCobrancaCota();
		
		List<Banco> listaBanco;
		
		listaBanco = formaCobrancaRepositoryImpl.obterBancosPorTipoDeCobranca(TipoCobranca.BOLETO);
		
		Assert.assertNotNull(listaBanco);
	}
	
	@Test
	public void testarObterFormaCobrancaPrincipalCota() {
		
		this.carregarFormasCobrancaCota();
		
		FormaCobranca formaCobranca;
		
		Long idCota = cota.getId();
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(idCota);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(formaCobranca1.getId(), formaCobranca.getId());
	}
	
	@Test
	public void testarObterFormaCobrancaPrincipalDistribuidor() {
		
		this.carregarFormasCobrancaDistribuidor();
		
		FormaCobranca formaCobranca;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca();
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(formaCobranca3.getId(), formaCobranca.getId());
	}
	
	@Test
	public void testarObterFormaCobrancaCota() {
		
		this.carregarFormasCobrancaCota();
		
		List<Long> fornecedoresId = Arrays.asList(fornecedor1.getId(),fornecedor2.getId());
		
		data = new Date();
		
		valor = new BigDecimal(150);
		
		Integer diaSemana =  DateUtil.obterDiaDaSemana(data);
		
		Integer diaMes = DateUtil.obterDiaDoMes(data); 	
		
		
		FormaCobranca formaCobranca;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(cota.getNumeroCota(), fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		
		diaSemana = DiaSemana.SABADO.getCodigoDiaSemana();
		
		diaMes = 10;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(cota.getNumeroCota(), fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
	}
	
	@Test
	public void testarObterFormaCobrancaDistribuidor() {
		
		this.carregarFormasCobrancaDistribuidor();
		
	
		Date data = new Date();
		
		BigDecimal valor = new BigDecimal(150);
		
		Integer diaSemana =  DateUtil.obterDiaDaSemana(data);
		
		Integer diaMes = DateUtil.obterDiaDoMes(data); 		
		
		FormaCobranca formaCobranca;
		
		List<Long> fornecedoresId;
		
		
        fornecedoresId = Arrays.asList(fornecedor1.getId());
		
		diaSemana = DiaSemana.SEGUNDA_FEIRA.getCodigoDiaSemana();
		
		diaMes = null;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(TipoFormaCobranca.SEMANAL, formaCobranca.getTipoFormaCobranca());
		
		
		fornecedoresId = Arrays.asList(fornecedor3.getId());
		
		diaSemana = null;
		
		diaMes = null;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(TipoFormaCobranca.DIARIA, formaCobranca.getTipoFormaCobranca());
		
		Assert.assertEquals(formaCobranca4.getId(), formaCobranca.getId());
		
		
		fornecedoresId = null;
		
		diaSemana = null;
		
		diaMes = 10;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(TipoFormaCobranca.MENSAL, formaCobranca.getTipoFormaCobranca());
		
		Assert.assertEquals(formaCobranca3.getId(), formaCobranca.getId());
		
		
        fornecedoresId = null;
		
		diaSemana = null;
		
		diaMes = 16;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(TipoFormaCobranca.QUINZENAL, formaCobranca.getTipoFormaCobranca());
		
		Assert.assertEquals(formaCobranca2.getId(), formaCobranca.getId());
		
		
		fornecedoresId = null;
		
		diaSemana = DiaSemana.SABADO.getCodigoDiaSemana();
		
		diaMes = null;
		
		formaCobranca = formaCobrancaRepositoryImpl.obterFormaCobranca(fornecedoresId, diaMes, diaSemana, valor);
		
		Assert.assertNotNull(formaCobranca);
		
		Assert.assertEquals(TipoFormaCobranca.SEMANAL, formaCobranca.getTipoFormaCobranca());
		
		Assert.assertEquals(formaCobranca1.getId(), formaCobranca.getId());
	}
	
	@Test
	public void testarObterFormasCobrancaCota() {
		
		this.carregarFormasCobrancaCota();
		
		List<FormaCobranca> listaFormaCobranca;
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterFormasCobrancaCota(cota);
		
		Assert.assertNotNull(listaFormaCobranca);
		
	}
	
	@Test
	public void testarObterQuantidadeFormasCobrancaCota() {
		
		this.carregarFormasCobrancaCota();
		
		int quantidadeFormas;
		
		quantidadeFormas = formaCobrancaRepositoryImpl.obterQuantidadeFormasCobrancaCota(cota);
		
		Assert.assertNotNull(quantidadeFormas);
		
	}
	
	@Test
	public void testarDesativarFormaCobranca() {
		
		this.carregarFormasCobrancaCota();
		
		long idFormaCobranca = formaCobranca1.getId();
		
		formaCobrancaRepositoryImpl.desativarFormaCobranca(idFormaCobranca);
	}
	
	@Test
	public void testarObterPorCotaETipoCobranca() {
		
		this.carregarFormasCobrancaCota();
		
		List<FormaCobranca> listaFormaCobranca;
		
		Long idCota = cota.getId();
		Long idFormaCobranca = formaCobranca1.getId();
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterPorCotaETipoCobranca(idCota, TipoCobranca.BOLETO, idFormaCobranca);
		
		Assert.assertNotNull(listaFormaCobranca);
			
	}
	
	@Test
	public void testarObterPorDistribuidorETipoCobranca() {
		
		this.carregarFormasCobrancaDistribuidor();
		
		List<FormaCobranca> listaFormaCobranca;
		
		Long idDistribuidor = distribuidor.getId();
		Long idFormaCobranca = formaCobranca1.getId();
		
		listaFormaCobranca = formaCobrancaRepositoryImpl.obterPorDistribuidorETipoCobranca(idDistribuidor, TipoCobranca.BOLETO, idFormaCobranca);
		
		Assert.assertNotNull(listaFormaCobranca);
		
	}

}
