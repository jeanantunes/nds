package br.com.abril.nds.fixture;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.LeiautePicking;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Algoritmo;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaCobrancaBoleto;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.ParametroUsuarioBox;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosCotaNotaFiscalEletronica;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.NotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetencaoICMSTransporte;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.FormaDevolucao;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaBanco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCodigoDescricao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaConcentracaoCobranca;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoProduto;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFornecedor;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaOutros;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaFisica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaJuridica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaReferenciaCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaTelefone;
import br.com.abril.nds.util.DateUtil;


public class Fixture {
    
    private static final AtomicInteger ORDEM_ROTA = new AtomicInteger(1);
    
    private static final AtomicInteger ORDEM_ROTEIRO = new AtomicInteger(1);
	
	public static PessoaJuridica juridicaAbril() {
		return pessoaJuridica("Editora Abril", "00000000000200", "010000000000",
				"abril@mail.com", "99.999-1");
	}
	
	public static PessoaJuridica juridicaFC() {
		return pessoaJuridica("FC", "00000000000100", "000000000002",
				"fc@mail.com", "99.999-9");
	}
	
	public static CotaAusente cotaAusenteAtivo(){
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		Pessoa manoel = Fixture.pessoaFisica("123.456.789-00",
				"sys.discover@gmail.com", "Manoel da Silva");	
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		return cotaAusente(criarData(10, 03, 2012), true, cotaManoel);
		
	}

	
	public static ParametroUsuarioBox parametroUsuarioBox(
			Box box, 
			Usuario usuario, 
			boolean principal) {
	
		ParametroUsuarioBox parametroUsuarioBox = new ParametroUsuarioBox();
		
		parametroUsuarioBox.setBox(box);
		parametroUsuarioBox.setUsuario(usuario);
		parametroUsuarioBox.setPrincipal(principal);
		
		return parametroUsuarioBox;
	}
	
	
	public static PessoaJuridica juridicaDinap() {		
		return pessoaJuridica("Dinap", "11111111000100", "111111111112",
				"dinap@mail.com", "99.999-8");
	}
	
	public static PessoaJuridica juridicaAcme() {
		return pessoaJuridica("ACME", "11222333000100", "111222333444",
				"acme@mail.com", "99.999-7");
	}

	public static Fornecedor fornecedorFC(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaFC(), SituacaoCadastro.ATIVO, true, tipoFornecedor, 9999998);
	}

	public static Fornecedor fornecedorDinap(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaDinap(), SituacaoCadastro.ATIVO, true, tipoFornecedor, 9999999);
	}
	
	public static Fornecedor fornecedorAcme(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaAcme(), SituacaoCadastro.ATIVO, false, tipoFornecedor, 3);
	}

	public static Produto produtoVeja(TipoProduto tipoProduto) {
		return produto("1", "Veja", "Veja", PeriodicidadeProduto.SEMANAL,
				tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}

	public static Produto produtoQuatroRodas(TipoProduto tipoProduto) {
		return produto("2", "Quatro Rodas", "Quatro Rodas",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}

	public static Produto produtoInfoExame(TipoProduto tipoProduto) {
		return produto("3", "Info Exame", "Info Exame",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}

	public static Produto produtoCapricho(TipoProduto tipoProduto) {
		return produto("4", "Capricho", "Capricho",
				PeriodicidadeProduto.QUINZENAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoSuperInteressante(TipoProduto tipoProduto) {
		return produto("5",
				"Superinteressante", "Superinteressante",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoBoaForma(TipoProduto tipoProduto) {
		return produto("6", "Boa Forma", "Boa Forma",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoBravo(TipoProduto tipoProduto) {
		return produto("7", "Bravo", "Bravo",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoContigo(TipoProduto tipoProduto) {
		return produto("8", "Contigo", "Contigo",
				PeriodicidadeProduto.QUINZENAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoCaras(TipoProduto tipoProduto) {
		return produto("9", "Caras", "Caras",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoClaudia(TipoProduto tipoProduto) {
		return produto("10", "Claudia", "Claudia",
				PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoCasaClaudia(TipoProduto tipoProduto) {
		return produto("11", "Casa Claudia", "Casa Claudia",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoManequim(TipoProduto tipoProduto) {
		return produto("12", "Manequim", "Manequim",
				PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoNationalGeographic(TipoProduto tipoProduto) {
		return produto("13", "National Geographic", "National Geographic",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoPlacar(TipoProduto tipoProduto) {
		return produto("14", "Placar", "Placar",
				PeriodicidadeProduto.MENSAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static Produto produtoCromoReiLeao(TipoProduto tipoProduto) {
		return produto("15", "Cromo Rei Leao", "Cromo Rei Leao",
				PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10), TributacaoFiscal. TRIBUTADO);
	}
	
	public static TipoProduto tipoRevista(NCM ncm) {
		return tipoProduto("Revistas", GrupoProduto.REVISTA, ncm, "4902.90.00", 001L);
	}
	
	public static TipoProduto tipoCromo(NCM ncm) {
		return tipoProduto("Cromos", GrupoProduto.CROMO, ncm, "4908.90.00", 002L);
	}
	
	public static TipoFornecedor tipoFornecedorPublicacao() {
		return tipoFornecedor("Fornecedor Publicação", GrupoFornecedor.PUBLICACAO);
	}
	
	public static TipoFornecedor tipoFornecedorOutros() {
		return tipoFornecedor("Fornecedor Outros", GrupoFornecedor.OUTROS);
	}
	
	public static Box boxReparte300() {
		return criarBox(300, "Box 300", TipoBox.LANCAMENTO);
	}
	
	public static Banco hsbc() {
		return Fixture.banco(10L, true, 30, "1010",
				  123456L, "1", "1", "Sem instruções", "HSBC","BANCO HSBC S/A", "399", BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	public static Editor editoraAbril() {
		return criarEditor(10L, juridicaAbril(), true);
	}

	public static Date criarData(int dia, int mes, int ano) {
		Calendar data = criarCalendar(dia, mes, ano, 0, 0, 0);
		return data.getTime();
	}

	public static Date criarData(int dia, int mes, int ano, int hora, int minuto) {
		Calendar data = criarCalendar(dia, mes, ano, hora, minuto, 0);
		return data.getTime();
	}

	private static Calendar criarCalendar(int dia, int mes, int ano, int hora,
			int minuto, int segundo) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes);
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.HOUR_OF_DAY, hora);
		data.set(Calendar.MINUTE, minuto);
		data.set(Calendar.SECOND, segundo);
		data.clear(Calendar.MILLISECOND);
		return data;
	}

	public static PessoaFisica pessoaFisica(String cpf, String email,
			String nome) {
		PessoaFisica fisica = new PessoaFisica();
		fisica.setCpf(cpf);
		fisica.setNome(nome);
		fisica.setEmail(email);
		return fisica;
	}
	
    public static PessoaFisica pessoaFisica(String cpf, String email,
            String nome, String rg, String orgaoEmissor, String uf,
            Date dataNascimento, EstadoCivil estadoCivil) {
        PessoaFisica fisica = pessoaFisica(cpf, email, nome);
        fisica.setRg(rg);
        fisica.setOrgaoEmissor(orgaoEmissor);
        fisica.setUfOrgaoEmissor(uf);
        fisica.setDataNascimento(dataNascimento);
        fisica.setEstadoCivil(estadoCivil);
        return fisica;
    }

	public static PessoaJuridica pessoaJuridica(String razaoSocial,
			String cnpj, String ie, String email, String im) {
		PessoaJuridica juridica = new PessoaJuridica();
		juridica.setRazaoSocial(razaoSocial);
		juridica.setNomeFantasia(razaoSocial);
		juridica.setCnpj(cnpj);
		juridica.setInscricaoEstadual(ie);
		juridica.setInscricaoMunicipal(im);
		juridica.setEmail(email);
		return juridica;
	}
	
	public static CotaAusente cotaAusente(Date data,
			boolean ativo,
			Cota cota){
		CotaAusente cotaAusente = new CotaAusente();
		cotaAusente.setAtivo(ativo);
		cotaAusente.setCota(cota);
		cotaAusente.setData(data);
		return cotaAusente;
	}

	public static Fornecedor fornecedor(PessoaJuridica juridica,
			SituacaoCadastro situacaoCadastro, boolean permiteBalanceamento,
			TipoFornecedor tipo, Integer codigoInterface) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setJuridica(juridica);
		fornecedor.setSituacaoCadastro(situacaoCadastro);
		fornecedor.setPermiteBalanceamento(permiteBalanceamento);
		fornecedor.setTipoFornecedor(tipo);
		fornecedor.setInicioAtividade(new Date());
		fornecedor.setCodigoInterface(codigoInterface);
		fornecedor.setOrigem(Origem.MANUAL);
		fornecedor.setEmailNfe("teste@gmail.com");
		fornecedor.setMargemDistribuidor(BigDecimal.TEN);
		return fornecedor;
	}
	
	public static NCM ncm(Long codigo, String descricao, String unidadeMedida) {
		NCM ncm = new NCM();
		ncm.setCodigo(codigo);
		ncm.setDescricao(descricao);
		ncm.setUnidadeMedida(unidadeMedida);
		return ncm;
	}

	public static TipoProduto tipoProduto(String descricao, GrupoProduto grupo,
			NCM ncm, String codigoNBM, Long codigo) {
		TipoProduto tipoProduto = new TipoProduto();
		tipoProduto.setDescricao(descricao);
		tipoProduto.setGrupoProduto(grupo);
		tipoProduto.setCodigo(codigo);
		tipoProduto.setCodigoNBM(codigoNBM);
		tipoProduto.setNcm(ncm);
		return tipoProduto;
	}
	
	public static TipoFornecedor tipoFornecedor(String descricao, GrupoFornecedor grupo) {
		TipoFornecedor tipoFornecedor = new TipoFornecedor();
		tipoFornecedor.setDescricao(descricao);
		tipoFornecedor.setGrupoFornecedor(grupo);
		return tipoFornecedor;
	}

	public static Produto produto(String codigo, String descricao, String nome,
								  PeriodicidadeProduto periodicidade, TipoProduto tipo, 
								  int peb, int pacotePadrao, Long peso, TributacaoFiscal tributacaoFiscal) {
		
		Produto produto = new Produto();
		produto.setCodigo(codigo);
		produto.setNomeComercial(descricao);
		produto.setNome(nome);
		produto.setPeriodicidade(periodicidade);
		produto.setTipoProduto(tipo);
		produto.setPeb(peb);
		produto.setPacotePadrao(pacotePadrao);
		produto.setPeso(peso);
		produto.setTributacaoFiscal(tributacaoFiscal);
		produto.setOrigem(Origem.INTERFACE);
		return produto;
	}

	public static ProdutoEdicao produtoEdicao(String codigoProdutoEdicao, Long numeroEdicao, 
			int pacotePadrao, int peb, Long peso, BigDecimal precoCusto,
			BigDecimal precoVenda, String codigoDeBarras, Produto produto, 
			BigDecimal expectativaVenda, boolean parcial,String nomeComercial) {
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		produtoEdicao.setPacotePadrao(pacotePadrao);
		produtoEdicao.setPeb(peb);
		produtoEdicao.setPeso(peso);
		produtoEdicao.setPrecoCusto(precoCusto);
		produtoEdicao.setPrecoVenda(precoVenda);
		produtoEdicao.setProduto(produto);
		produtoEdicao.setCodigoDeBarras(codigoDeBarras);
		produtoEdicao.setExpectativaVenda(expectativaVenda);
		produtoEdicao.setParcial(parcial);
		produtoEdicao.setNomeComercial(nomeComercial);
		produtoEdicao.setOrigem(Origem.INTERFACE);
		return produtoEdicao;
	}
	
	public static ProdutoEdicao produtoEdicao(Long numeroEdicao, int pacotePadrao, 
			int peb, Long peso, BigDecimal precoCusto, BigDecimal precoVenda,
			String codigoDeBarras, Produto produto, BigDecimal expectativaVenda, 
			boolean parcial) {
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		produtoEdicao.setPacotePadrao(pacotePadrao);
		produtoEdicao.setPeb(peb);
		produtoEdicao.setPeso(peso);
		produtoEdicao.setPrecoCusto(precoCusto);
		produtoEdicao.setPrecoVenda(precoVenda);
		produtoEdicao.setProduto(produto);
		produtoEdicao.setCodigoDeBarras(codigoDeBarras);
		produtoEdicao.setExpectativaVenda(expectativaVenda);
		produtoEdicao.setParcial(parcial);
		
		produtoEdicao.setOrigem(br.com.abril.nds.model.Origem.INTERFACE);
		return produtoEdicao;
	}
	
	
	public static Lancamento lancamento(TipoLancamento tipoLancamento,
			ProdutoEdicao produtoEdicao, Date dlp, Date drp, Date dataCriacao,
			Date dataStatus, BigInteger reparte, StatusLancamento statusLancamento,
			ItemRecebimentoFisico recebimento, Integer sequenciaMatriz) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDataCriacao(dataCriacao);
		lancamento.setDataStatus(dataStatus);
		lancamento.setReparte(reparte);
		lancamento.setStatus(statusLancamento);
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dlp);
		lancamento.setDataLancamentoDistribuidor(dlp);
		lancamento.setDataRecolhimentoPrevista(drp);
		lancamento.setDataRecolhimentoDistribuidor(drp);
		lancamento.setSequenciaMatriz(sequenciaMatriz);
		
		if (recebimento != null) {
			lancamento.addRecebimento(recebimento);
		}
		
		
		return lancamento;
	}
	
	public static ChamadaEncalhe chamadaEncalhe(Date dataRecolhimento, 
												ProdutoEdicao produtoEdicao, 
												TipoChamadaEncalhe tipoChamadaEncalhe) {
											
		ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(tipoChamadaEncalhe);
		
		return chamadaEncalhe;
	}
	
	public static ConferenciaEncalheParcial conferenciaEncalheParcial(
			Usuario usuario,
			ProdutoEdicao produtoEdicao, 
			StatusAprovacao statusAprovacao,
			Date drd, 
			Date dataStatus, 
			BigInteger qtde) {
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataStatus);
		conferenciaEncalheParcial.setDataMovimento(drd);
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(qtde);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(statusAprovacao);
		
		return conferenciaEncalheParcial;
	}
	
	
	public static ControleConferenciaEncalhe controleConferenciaEncalhe (
			StatusOperacao statusOperacao,
			Date data) {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = new ControleConferenciaEncalhe();
		
		controleConferenciaEncalhe.setData(data);
		controleConferenciaEncalhe.setStatus(statusOperacao);
		
		return controleConferenciaEncalhe;
	}

	public static ControleConferenciaEncalheCota controleConferenciaEncalheCota (
			ControleConferenciaEncalhe controleConferenciaEncalhe,
			Cota cota,
			Date dataInicio,
			Date dataFim,
			Date dataOperacao,
			StatusOperacao statusOperacao,
			Usuario usuario,
			Box box) {
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = new ControleConferenciaEncalheCota();
		
		controleConferenciaEncalheCota.setControleConferenciaEncalhe(controleConferenciaEncalhe);
		controleConferenciaEncalheCota.setCota(cota);
		controleConferenciaEncalheCota.setDataInicio(dataInicio);
		controleConferenciaEncalheCota.setDataFim(dataFim);
		controleConferenciaEncalheCota.setDataOperacao(dataOperacao);
		controleConferenciaEncalheCota.setStatus(statusOperacao);
		controleConferenciaEncalheCota.setUsuario(usuario);
		controleConferenciaEncalheCota.setBox(box);
		
		return controleConferenciaEncalheCota;
	}
	
	public static ControleContagemDevolucao controleContagemDevolucao (
			StatusOperacao statusOperacao,
			Date data,
			ProdutoEdicao produtoEdicao) {
		
		ControleContagemDevolucao controleContagemDevolucao = new ControleContagemDevolucao();
		
		controleContagemDevolucao.setData(data);
		controleContagemDevolucao.setStatus(statusOperacao);
		controleContagemDevolucao.setProdutoEdicao(produtoEdicao);
		
		return controleContagemDevolucao;
	}
	
	public static Lancamento lancamentoExpedidos(TipoLancamento tipoLancamento,
			ProdutoEdicao produtoEdicao, Date dlp, Date drp, Date dataCriacao,
			Date dataStatus, BigInteger reparte, StatusLancamento statusLancamento,
			ItemRecebimentoFisico recebimento,Expedicao expedicao, Integer sequenciaMatriz) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDataCriacao(dataCriacao);
		lancamento.setDataStatus(dataStatus);
		lancamento.setReparte(reparte);
		lancamento.setStatus(statusLancamento);
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dlp);
		lancamento.setDataLancamentoDistribuidor(dlp);
		lancamento.setDataRecolhimentoPrevista(drp);
		lancamento.setDataRecolhimentoDistribuidor(drp);
		lancamento.setExpedicao(expedicao);
		lancamento.setExpedicao(expedicao);
		lancamento.setSequenciaMatriz(sequenciaMatriz);
		
		if (recebimento != null) {
			lancamento.addRecebimento(recebimento);
		}
		
		return lancamento;
	}

	public static Distribuidor distribuidor(Integer codigo, PessoaJuridica juridica,
											Date dataOperacao, Set<PoliticaCobranca> politicasCobranca) {
		
		Distribuidor distribuidor = new Distribuidor();
		
		distribuidor.setCodigo(codigo);
		distribuidor.setDataOperacao(dataOperacao);
		distribuidor.setJuridica(juridica);
		distribuidor.setPoliticasCobranca(politicasCobranca);
		distribuidor.setCapacidadeDistribuicao(new BigDecimal("3000"));
		distribuidor.setCapacidadeRecolhimento(new BigDecimal("1000"));
		distribuidor.setPreenchimentoAutomaticoPDV(true);
		distribuidor.setExecutaRecolhimentoParcial(true);
		distribuidor.setFatorRelancamentoParcial(7);
		distribuidor.setQuantidadeDiasSuspensaoCotas(7);
		distribuidor.setValorConsignadoSuspensaoCotas(BigDecimal.TEN);
		distribuidor.setLeiautePicking(LeiautePicking.DOIS);
		distribuidor.setRequerAutorizacaoEncalheSuperaReparte(false);
		distribuidor.setObrigacaoFiscal(ObrigacaoFiscal.COTA_TOTAL);
		distribuidor.setRegimeEspecial(true);
		distribuidor.setUtilizaProcuracaoEntregadores(true);
		distribuidor.setInformacoesComplementaresProcuracao("Info Complementares Procuração");
		distribuidor.setUtilizaGarantiaPdv(true);
		distribuidor.setParcelamentoDividas(false);
		distribuidor.setNegociacaoAteParcelas(Integer.valueOf(3));
		distribuidor.setUtilizaControleAprovacao(false);
		distribuidor.setPrazoFollowUp(Integer.valueOf(7));
		distribuidor.setPrazoAvisoPrevioValidadeGarantia(Integer.valueOf(7));
		distribuidor.setQtdDiasLimiteParaReprogLancamento(Integer.valueOf(7));
		
		ParametrosRecolhimentoDistribuidor prd = new ParametrosRecolhimentoDistribuidor();
		prd.setConferenciaCegaEncalhe(false);
		prd.setConferenciaCegaRecebimento(false);
		prd.setDiaRecolhimentoPrimeiro(false);
		prd.setDiaRecolhimentoSegundo(false);
		prd.setDiaRecolhimentoTerceiro(false);
		prd.setDiaRecolhimentoQuarto(false);
		prd.setDiaRecolhimentoQuinto(false);
		prd.setPermiteRecolherDiasPosteriores(false);
		distribuidor.setParametrosRecolhimentoDistribuidor(prd);
		
		ParametrosAprovacaoDistribuidor pad = new ParametrosAprovacaoDistribuidor();
		pad.setDebitoCredito(false);
		pad.setNegociacao(false);
		pad.setAjusteEstoque(false);
		pad.setPostergacaoCobranca(false);
		pad.setDevolucaoFornecedor(false);
		pad.setFaltasSobras(false);
		distribuidor.setParametrosAprovacaoDistribuidor(pad);
		
		distribuidor.setDescontoCotaNegociacao(BigDecimal.ZERO);
		
		return distribuidor;
	}

	public static DistribuicaoFornecedor distribuicaoFornecedor(
			Fornecedor fornecedor, DiaSemana diaSemana,
			OperacaoDistribuidor operacaoDistribuidor, Distribuidor distribuidor) {
		DistribuicaoFornecedor df = new DistribuicaoFornecedor();
		df.setFornecedor(fornecedor);
		df.setDistribuidor(distribuidor);
		df.setDiaSemana(diaSemana);
		df.setOperacaoDistribuidor(operacaoDistribuidor);
		
		return df;
	}
	
	public static DistribuicaoDistribuidor distribuicaoDistribuidor(
											Distribuidor distribuidor, DiaSemana diaSemana,
											OperacaoDistribuidor operacaoDistribuidor) {
		
		DistribuicaoDistribuidor distribuicaoDistribuidor = new DistribuicaoDistribuidor();
		
		distribuicaoDistribuidor.setDistribuidor(distribuidor);
		distribuicaoDistribuidor.setDiaSemana(diaSemana);
		distribuicaoDistribuidor.setOperacaoDistribuidor(operacaoDistribuidor);
		
		return distribuicaoDistribuidor;
	}

	public static Cota cota(Integer numeroCota, Pessoa pessoa,
			SituacaoCadastro situacaoCadastro, Box box) {
		Cota cota = new Cota();
		cota.setNumeroCota(numeroCota);
		cota.setPessoa(pessoa);
		cota.setSituacaoCadastro(situacaoCadastro);
		cota.setBox(box);
		cota.setInicioAtividade(new Date());
		cota.setClassificacaoEspectativaFaturamento(ClassificacaoEspectativaFaturamento.D);
		return cota;
	}

	public static Estudo estudo(BigInteger qtdReparte, Date data,
			ProdutoEdicao produtoEdicao) {

		Estudo estudo = new Estudo();

		estudo.setQtdeReparte(qtdReparte);

		estudo.setDataLancamento(data);

		estudo.setProdutoEdicao(produtoEdicao);
		
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		estudo.setDataCadastro(new Date());
		return estudo;
	}

	public static EstudoCota estudoCota(BigInteger qtdePrevista,
			BigInteger qtdeEfetiva, Estudo estudo, Cota cota) {

		EstudoCota estudoCota = new EstudoCota();

		estudoCota.setQtdePrevista(qtdePrevista);

		estudoCota.setQtdeEfetiva(qtdeEfetiva);

		estudoCota.setEstudo(estudo);

		estudoCota.setCota(cota);

		return estudoCota;
	}

	public static Usuario usuarioJoao() {
		Usuario usuario = new Usuario();
		usuario.setNome("João");
		usuario.setLogin("joao");
		usuario.setSenha("81dc9bdb52d04dc20036dbd8313ed055"); // senha: 1234
		usuario.setEmail("cabradapeste@bol.com");
		usuario.setContaAtiva(false);
		return usuario;
	}

	public static CFOP cfop5102() {
		CFOP cfop = new CFOP();
		cfop.setCodigo("5102");
		cfop.setDescricao("Venda de mercadoria adquirida ou recebida de terceiros");
		return cfop;
	}

	public static CFOP cfop1209() {
		CFOP cfop = new CFOP();
		cfop.setCodigo("1209");
		cfop.setDescricao("Devolução de mercadoria adquirida ou recebida de terceiros, remetida em transferência dentro do estado");
		return cfop;
	}

	public static CFOP cfop1210() {
		CFOP cfop = new CFOP();
		cfop.setCodigo("1210");
		cfop.setDescricao("Devolução de mercadoria adquirida ou recebida de terceiros, remetida em transferência fora do estado");
		return cfop;
	}

	
	public static TipoMovimentoEstoque tipoMovimentoFaltaEm() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(false);
		tipoMovimento.setDescricao("Falta EM");
		tipoMovimento.setIncideDivida(false);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.FALTA_EM);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoFaltaDe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(false);
		tipoMovimento.setDescricao("Falta DE");
		tipoMovimento.setIncideDivida(false);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.FALTA_DE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoSobraEm() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(false);
		tipoMovimento.setDescricao("Sobra EM");
		tipoMovimento.setIncideDivida(false);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_EM);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoSobraDe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(false);
		tipoMovimento.setDescricao("Sobra DE");
		tipoMovimento.setIncideDivida(false);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_DE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoRecebimentoFisico() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Recebimento de Mercadoria");
		tipoMovimento.setIncideDivida(false);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoRecebimentoReparte() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Recebimento Reparte");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		return tipoMovimento;
	}
		
	public static TipoMovimentoEstoque tipoMovimentoNivelamentoEntrada() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Nivelamento Entrada");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.NIVELAMENTO_ENTRADA);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoNivelamentoSaida() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Nivelamento Saida");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.NIVELAMENTO_SAIDA);
		return tipoMovimento;
	}
		
	public static TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Envio a Jornaleiro");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoRestauracaoReparteCotaAusente() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Restauracao Reparte cota ausente");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoReparteCotaAusente() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Reparte cota ausente");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Crédito");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.CREDITO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Envio Encalhe - Financeiro");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoEnvioEncalhe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Envio Encalhe - Estoque");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
	
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoRecebimentoEncalheJuramentado() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Recebimento Encalhe Juramentado");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO);
	
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Recebimento Encalhe");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
	
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoSuplementarEnvioEncalheAnteriroProgramacao() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Suplementar Envio de Encalhe Anterior Programacao");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
	
		return tipoMovimento;
	}

	
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompraEncalhe() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Compra do Encalhe");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.COMPRA_ENCALHE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoVendaEncalhe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Venda Encalhe");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE);
	
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoVendaEncalheSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Venda Encalhe Suplementar");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR);
	
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEncalheAntecipado() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Encalhe Antecipado");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
	
		return tipoMovimento;
	}	
	
	public static TipoMovimentoEstoque tipoMovimentoEstornoVendaEncalhe() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Venda Encalhe");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE);
	
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEstornoVendaEncalheSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Venda Encalhe Suplementar");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
	
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoCompraSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Compra Suplementar");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
	
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEstornoCompraSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Compra Suplementar");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);
	
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoSuplementarCotaAusente() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Suplementar Cota Ausente");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEstornoCotaAusente() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Cota Ausente");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEstornoCotaEstornoEnvioReparte() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Envio Reparte");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_ENVIO_REPARTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoEntradaSuplementarEnvioReparte() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada Suplementar de Envio Reparte Distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoCancelamentoNFEnvioConsignado() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Saida de mercadorias das NFs canceladas para o estoque da Cota");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_ENVIO_CONSIGNADO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaLancamento() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada por transferência de estoque do tipo Lançamento, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaLancamento() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Saída por transferência de estoque do tipo Lançamento, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada por transferência de estoque do tipo Suplementar, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaSuplementar() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Saída por transferência de estoque do tipo Suplementar, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaRecolhimento() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada por transferência de estoque do tipo Recolhimento, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaRecolhimento() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Saída por transferência de estoque do tipo Recolhimento, do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaProdutosDanificados() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada por transferência de estoque do tipo 'Produtos danificados', do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaProdutosDanificados() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Saída por transferência de estoque do tipo 'Produtos danificados', do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS);
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoCancelamentoNFDevolucaoConsignado() {
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Entrada de mercadorias das NFs canceladas para o estoque do distribuidor");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_CONSIGNADO);
		return tipoMovimento;
	}
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Débito");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoPostergado() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Postergado");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
		return tipoMovimento;
	}	
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoPendente() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Pendente");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
		return tipoMovimento;
	}	
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoNA() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("NA");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoFaturamento() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Débito (Faturamento)");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Recebimento Reparte");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		return tipoMovimento;
	}
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Juros");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.JUROS);
		return tipoMovimento;
	}

	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Multa");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.MULTA);
		return tipoMovimento;
	}
		
	public static ItemNotaFiscalEntrada itemNotaFiscal(ProdutoEdicao produtoEdicao,
			Usuario usuario, NotaFiscalEntrada notaFiscal, Date dataLancamento, Date dataRecolhimento, TipoLancamento tipoLancamento, BigInteger qtde) {
		ItemNotaFiscalEntrada itemNotaFiscal = new ItemNotaFiscalEntrada();
		itemNotaFiscal.setOrigem(Origem.MANUAL);
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQtde(qtde);
		itemNotaFiscal.setUsuario(usuario);
		itemNotaFiscal.setNotaFiscal(notaFiscal);
		itemNotaFiscal.setDataLancamento(dataLancamento);
		itemNotaFiscal.setDataRecolhimento(dataRecolhimento);
		itemNotaFiscal.setTipoLancamento(tipoLancamento);
		return itemNotaFiscal;
	}

	public static ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE(
			ProdutoEdicao produtoEdicao,
			Usuario usuario, 
			NotaFiscalEntrada notaFiscal, 
			Date dataLancamento, 
			Date dataRecolhimento, 
			TipoLancamento tipoLancamento, 
			BigInteger qtde,
			String 		NCMProduto,
			String 		CFOPProduto,
			Long 		unidadeProduto,
			String 		CSTProduto,
			String 		CSOSNProduto,
			BigDecimal 	baseCalculoProduto,
			BigDecimal 	aliquotaICMSProduto,
			BigDecimal 	valorICMSProduto,
			BigDecimal 	aliquotaIPIProduto,
			BigDecimal 	valorIPIProduto) {
		
		ItemNotaFiscalEntrada itemNotaFiscal = new ItemNotaFiscalEntrada();
		itemNotaFiscal.setOrigem(Origem.MANUAL);
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQtde(qtde);
		itemNotaFiscal.setUsuario(usuario);
		itemNotaFiscal.setNotaFiscal(notaFiscal);
		itemNotaFiscal.setDataLancamento(dataLancamento);
		itemNotaFiscal.setDataRecolhimento(dataRecolhimento);
		itemNotaFiscal.setTipoLancamento(tipoLancamento);
		
		itemNotaFiscal.setNCMProduto(NCMProduto);
		itemNotaFiscal.setCFOPProduto(CFOPProduto);
		itemNotaFiscal.setUnidadeProduto(unidadeProduto);
		itemNotaFiscal.setCSTProduto(CSTProduto);
		itemNotaFiscal.setCSOSNProduto(CSOSNProduto);
		itemNotaFiscal.setBaseCalculoProduto(baseCalculoProduto);
		itemNotaFiscal.setAliquotaICMSProduto(aliquotaICMSProduto);
		itemNotaFiscal.setValorICMSProduto(valorICMSProduto);
		itemNotaFiscal.setAliquotaIPIProduto(aliquotaIPIProduto);
		itemNotaFiscal.setValorIPIProduto(valorIPIProduto);
		
		return itemNotaFiscal;
	}

	

	
	public static ItemNotaFiscalSaida itemNotaFiscalSaida(
			ProdutoEdicao produtoEdicao,
			NotaFiscalSaida notaFiscal, 
			BigInteger qtde) {
		
		ItemNotaFiscalSaida itemNotaFiscal = new ItemNotaFiscalSaida();
		
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQtde(qtde);
		itemNotaFiscal.setNotaFiscal(notaFiscal);
		
		return itemNotaFiscal;
		
	}

	public static ItemNotaFiscalSaida itemNotaFiscalSaidaNFE(
			ProdutoEdicao produtoEdicao,
			NotaFiscalSaida notaFiscal, 
			BigInteger qtde,
			String		NCMProduto,
			String 		CFOPProduto,
			Long 		unidadeProduto,
			String 		CSTProduto,
			String 		CSOSNProduto,
			BigDecimal 	baseCalculoProduto,
			BigDecimal 	aliquotaICMSProduto,
			BigDecimal 	valorICMSProduto,
			BigDecimal 	aliquotaIPIProduto,
			BigDecimal 	valorIPIProduto) {
		
		ItemNotaFiscalSaida itemNotaFiscal = new ItemNotaFiscalSaida();
		
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQtde(qtde);
		itemNotaFiscal.setNotaFiscal(notaFiscal);
		itemNotaFiscal.setNCMProduto(NCMProduto);
		itemNotaFiscal.setCFOPProduto(CFOPProduto);
		itemNotaFiscal.setUnidadeProduto(unidadeProduto);
		itemNotaFiscal.setCSTProduto(CSTProduto);
		itemNotaFiscal.setCSOSNProduto(CSOSNProduto);
		itemNotaFiscal.setBaseCalculoProduto(baseCalculoProduto);
		itemNotaFiscal.setAliquotaICMSProduto(aliquotaICMSProduto);
		itemNotaFiscal.setValorICMSProduto(valorICMSProduto);
		itemNotaFiscal.setAliquotaIPIProduto(aliquotaIPIProduto);
		itemNotaFiscal.setValorIPIProduto(valorIPIProduto);
		
		return itemNotaFiscal;
		
	}

	
	public static TipoNotaFiscal tipoNotaFiscalRecebimento() {
		
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		
		tipoNotaFiscal.setDescricao("RECEBIMENTO");
		tipoNotaFiscal.setGrupoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS);
		tipoNotaFiscal.setNopDescricao("NF-e de Devolução de Remessa para Distruibuição");
		tipoNotaFiscal.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		tipoNotaFiscal.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		tipoNotaFiscal.setContribuinte(false);
		tipoNotaFiscal.setNopCodigo(0L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.ENTRADA);		
		tipoNotaFiscal.setTipoAtividade(TipoAtividade.MERCANTIL);
		tipoNotaFiscal.setSerieNotaFiscal(2);
		return tipoNotaFiscal;
	}

	public static ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscal(
			CFOP cfopDentroEstado, 
			CFOP cfopForaEstado, 
			GrupoNotaFiscal grupoNotaFiscal,
			String serieNF) {
		
		ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscal = new ParametroEmissaoNotaFiscal();
		
		parametroEmissaoNotaFiscal.setCfopDentroEstado(cfopDentroEstado);
		parametroEmissaoNotaFiscal.setCfopForaEstado(cfopForaEstado);
		parametroEmissaoNotaFiscal.setGrupoNotaFiscal(grupoNotaFiscal);
		parametroEmissaoNotaFiscal.setSerieNF(serieNF);
		
		return parametroEmissaoNotaFiscal;
		
	}

	
	
	public static ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal( Long proximoNumeroNF, String serieNF ) {
		
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = new ControleNumeracaoNotaFiscal();
		
		controleNumeracaoNotaFiscal.setProximoNumeroNF(proximoNumeroNF);
		controleNumeracaoNotaFiscal.setSerieNF(serieNF);
		
		return controleNumeracaoNotaFiscal;
		
	}

	public static TipoNotaFiscal tipoNotaFiscalDevolucao() {
		
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		
		tipoNotaFiscal.setDescricao("DEVOLUCAO");
		tipoNotaFiscal.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		tipoNotaFiscal.setNopDescricao("NF-e de Remessa em Consignação (NECE / DANFE)");
		tipoNotaFiscal.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		tipoNotaFiscal.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		tipoNotaFiscal.setContribuinte(true);
		tipoNotaFiscal.setNopCodigo(0L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.SAIDA);
		tipoNotaFiscal.setTipoAtividade(TipoAtividade.MERCANTIL);
		tipoNotaFiscal.setSerieNotaFiscal(3);
		tipoNotaFiscal.setProcesso(new HashSet<Processo>());
		return tipoNotaFiscal;
	}

	public static TipoNotaFiscal tipoNotaFiscalRecebimentoMercadoriasEncalhe() {
		
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		
		tipoNotaFiscal.setDescricao("RECEBIMENTO DE MERCADORIAS ENCALHE");
		tipoNotaFiscal.setGrupoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);
		tipoNotaFiscal.setNopDescricao("NF-e de Devolução de Remessa para Distruibuição");
		tipoNotaFiscal.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		tipoNotaFiscal.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		tipoNotaFiscal.setContribuinte(false);
		tipoNotaFiscal.setNopCodigo(0L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.ENTRADA);
		tipoNotaFiscal.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		tipoNotaFiscal.setSerieNotaFiscal(4);
		
		return tipoNotaFiscal;
	}
	
	public static NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor(CFOP cfop,
			Fornecedor fornecedor, TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, BigDecimal valorBruto, BigDecimal valorDesconto, BigDecimal valorLiquido) {
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = new NotaFiscalEntradaFornecedor();
		notaFiscalFornecedor.setFornecedor(fornecedor);
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setNumero(2344242L);
		notaFiscalFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalFornecedor.setUsuario(usuario);
		notaFiscalFornecedor.setValorBruto(valorBruto);
		notaFiscalFornecedor.setValorDesconto(valorDesconto);
		notaFiscalFornecedor.setValorLiquido(valorLiquido);

		return notaFiscalFornecedor;
	}
	
	public static NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor(Long numeroNF, String serie, String chaveAcesso, CFOP cfop,
			Fornecedor fornecedor, TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, BigDecimal valorBruto, BigDecimal valorDesconto, BigDecimal valorLiquido) {
		
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = notaFiscalEntradaFornecedor(cfop, fornecedor, tipoNotaFiscal, usuario, valorBruto, valorDesconto, valorLiquido);
		
		notaFiscalFornecedor.setNumero(numeroNF);
		notaFiscalFornecedor.setSerie(serie);
		notaFiscalFornecedor.setChaveAcesso(chaveAcesso);
		
		return notaFiscalFornecedor;
	}
	
	public static NotaFiscalEntradaCota notaFiscalEntradaCotaNFE(
			CFOP cfop,
			Long numero,
			String serie,
			String chaveAcesso,
			Cota cota,
			StatusEmissaoNfe statusEmissaoNfe,
			TipoEmissaoNfe tipoEmissaoNfe,
			TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, 
			BigDecimal valorBruto, 
			BigDecimal valorDesconto, 
			BigDecimal valorLiquido,
			boolean indEmitida,
			String naturezaOperacao,
			String formaPagamento,
			String horaSaida,
			String ambiente,
			String protocolo,
			String versao,
			String emissorInscricaoEstadualSubstituto,
			String emissorInscricaoMunicipal,
			BigDecimal valorBaseICMS,
			BigDecimal valorICMS,
			BigDecimal valorBaseICMSSubstituto,
			BigDecimal valorICMSSubstituto,
			BigDecimal valorProdutos,
			BigDecimal valorFrete,
			BigDecimal valorSeguro,
			BigDecimal valorOutro,
			BigDecimal valorIPI,
			BigDecimal valorNF,
			Integer frete,
			String transportadoraCNPJ,
			String transportadoraNome,
			String transportadoraInscricaoEstadual,
			String transportadoraEndereco,
			String transportadoraMunicipio,
			String transportadoraUF,
			String transportadoraQuantidade,
			String transportadoraEspecie,
			String transportadoraMarca,
			String transportadoraNumeracao,
			BigDecimal transportadoraPesoBruto,
			BigDecimal transportadoraPesoLiquido,
			String transportadoraANTT,
			String transportadoraPlacaVeiculo,
			String transportadoraPlacaVeiculoUF,
			BigDecimal ISSQNTotal,
			BigDecimal ISSQNBase,
			BigDecimal ISSQNValor,
			String informacoesComplementares,
			String numeroFatura,
			BigDecimal valorFatura ) {
		
		NotaFiscalEntradaCota notaFiscalEntradaCota = new NotaFiscalEntradaCota();
		
		notaFiscalEntradaCota.setCfop(cfop);
		notaFiscalEntradaCota.setChaveAcesso(chaveAcesso);
		notaFiscalEntradaCota.setDataEmissao(new Date());
		notaFiscalEntradaCota.setDataExpedicao(new Date());
		notaFiscalEntradaCota.setNumero(numero);
		notaFiscalEntradaCota.setSerie(serie);
		notaFiscalEntradaCota.setOrigem(Origem.INTERFACE);
		notaFiscalEntradaCota.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscalEntradaCota.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalEntradaCota.setUsuario(usuario);
		notaFiscalEntradaCota.setCota(cota);
		notaFiscalEntradaCota.setValorBruto(valorBruto);
		notaFiscalEntradaCota.setValorDesconto(valorDesconto);
		notaFiscalEntradaCota.setValorLiquido(valorLiquido);
		notaFiscalEntradaCota.setStatusEmissaoNfe(statusEmissaoNfe);
		notaFiscalEntradaCota.setTipoEmissaoNfe(tipoEmissaoNfe);
		notaFiscalEntradaCota.setEmitida(indEmitida);
		
		notaFiscalEntradaCota.setNaturezaOperacao(naturezaOperacao);
		notaFiscalEntradaCota.setFormaPagamento(formaPagamento);
		notaFiscalEntradaCota.setHoraSaida(horaSaida);
		notaFiscalEntradaCota.setAmbiente(ambiente);
		notaFiscalEntradaCota.setProtocolo(protocolo);
		notaFiscalEntradaCota.setVersao(versao);
		notaFiscalEntradaCota.setEmissorInscricaoEstadualSubstituto(emissorInscricaoEstadualSubstituto);
		notaFiscalEntradaCota.setEmissorInscricaoMunicipal(emissorInscricaoMunicipal);
		notaFiscalEntradaCota.setValorBaseICMS(valorBaseICMS);
		notaFiscalEntradaCota.setValorICMS(valorICMS);
		notaFiscalEntradaCota.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
		notaFiscalEntradaCota.setValorICMSSubstituto(valorICMSSubstituto);
		notaFiscalEntradaCota.setValorProdutos(valorProdutos);
		notaFiscalEntradaCota.setValorFrete(valorFrete);
		notaFiscalEntradaCota.setValorSeguro(valorSeguro);
		notaFiscalEntradaCota.setValorOutro(valorOutro);
		notaFiscalEntradaCota.setValorIPI(valorIPI);
		notaFiscalEntradaCota.setValorNF(valorNF);
		notaFiscalEntradaCota.setFrete(frete);
		notaFiscalEntradaCota.setTransportadoraCNPJ(transportadoraCNPJ);
		notaFiscalEntradaCota.setTransportadoraNome(transportadoraNome);
		notaFiscalEntradaCota.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
		notaFiscalEntradaCota.setTransportadoraEndereco(transportadoraEndereco);
		notaFiscalEntradaCota.setTransportadoraMunicipio(transportadoraMunicipio);
		notaFiscalEntradaCota.setTransportadoraUF(transportadoraUF);
		notaFiscalEntradaCota.setTransportadoraQuantidade(transportadoraQuantidade);
		notaFiscalEntradaCota.setTransportadoraEspecie(transportadoraEspecie);
		notaFiscalEntradaCota.setTransportadoraMarca(transportadoraMarca);
		notaFiscalEntradaCota.setTransportadoraNumeracao(transportadoraNumeracao);
		notaFiscalEntradaCota.setTransportadoraPesoBruto(transportadoraPesoBruto);
		notaFiscalEntradaCota.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
		notaFiscalEntradaCota.setTransportadoraANTT(transportadoraANTT);
		notaFiscalEntradaCota.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
		notaFiscalEntradaCota.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);
		notaFiscalEntradaCota.setISSQNTotal(ISSQNTotal);
		notaFiscalEntradaCota.setISSQNBase(ISSQNBase);
		notaFiscalEntradaCota.setISSQNValor(ISSQNValor);
		notaFiscalEntradaCota.setInformacoesComplementares(informacoesComplementares);
		notaFiscalEntradaCota.setNumeroFatura(numeroFatura);
		notaFiscalEntradaCota.setValorFatura(valorFatura);		

		
		return notaFiscalEntradaCota;
		
	}
	
	
	public static NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedorNFE(
			CFOP cfop,
			Long numero,
			String serie,
			String chaveAcesso,
			Fornecedor fornecedor,
			StatusEmissaoNfe statusEmissaoNfe,
			TipoEmissaoNfe tipoEmissaoNfe,
			TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, 
			BigDecimal valorBruto, 
			BigDecimal valorDesconto, 
			BigDecimal valorLiquido,
			boolean indEmitida,
			String naturezaOperacao,
			String formaPagamento,
			String horaSaida,
			String ambiente,
			String protocolo,
			String versao,
			String emissorInscricaoEstadualSubstituto,
			String emissorInscricaoMunicipal,
			BigDecimal valorBaseICMS,
			BigDecimal valorICMS,
			BigDecimal valorBaseICMSSubstituto,
			BigDecimal valorICMSSubstituto,
			BigDecimal valorProdutos,
			BigDecimal valorFrete,
			BigDecimal valorSeguro,
			BigDecimal valorOutro,
			BigDecimal valorIPI,
			BigDecimal valorNF,
			Integer frete,
			String transportadoraCNPJ,
			String transportadoraNome,
			String transportadoraInscricaoEstadual,
			String transportadoraEndereco,
			String transportadoraMunicipio,
			String transportadoraUF,
			String transportadoraQuantidade,
			String transportadoraEspecie,
			String transportadoraMarca,
			String transportadoraNumeracao,
			BigDecimal transportadoraPesoBruto,
			BigDecimal transportadoraPesoLiquido,
			String transportadoraANTT,
			String transportadoraPlacaVeiculo,
			String transportadoraPlacaVeiculoUF,
			BigDecimal ISSQNTotal,
			BigDecimal ISSQNBase,
			BigDecimal ISSQNValor,
			String informacoesComplementares,
			String numeroFatura,
			BigDecimal valorFatura ) {
		
		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor = new NotaFiscalEntradaFornecedor();
		
		notaFiscalEntradaFornecedor.setFornecedor(fornecedor);
		notaFiscalEntradaFornecedor.setCfop(cfop);
		notaFiscalEntradaFornecedor.setChaveAcesso(chaveAcesso);
		notaFiscalEntradaFornecedor.setDataEmissao(new Date());
		notaFiscalEntradaFornecedor.setDataExpedicao(new Date());
		notaFiscalEntradaFornecedor.setNumero(numero);
		notaFiscalEntradaFornecedor.setSerie(serie);
		notaFiscalEntradaFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalEntradaFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscalEntradaFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalEntradaFornecedor.setUsuario(usuario);
		notaFiscalEntradaFornecedor.setValorBruto(valorBruto);
		notaFiscalEntradaFornecedor.setValorDesconto(valorDesconto);
		notaFiscalEntradaFornecedor.setValorLiquido(valorLiquido);
		notaFiscalEntradaFornecedor.setStatusEmissaoNfe(statusEmissaoNfe);
		notaFiscalEntradaFornecedor.setTipoEmissaoNfe(tipoEmissaoNfe);
		notaFiscalEntradaFornecedor.setEmitida(indEmitida);
		
		notaFiscalEntradaFornecedor.setNaturezaOperacao(naturezaOperacao);
		notaFiscalEntradaFornecedor.setFormaPagamento(formaPagamento);
		notaFiscalEntradaFornecedor.setHoraSaida(horaSaida);
		notaFiscalEntradaFornecedor.setAmbiente(ambiente);
		notaFiscalEntradaFornecedor.setProtocolo(protocolo);
		notaFiscalEntradaFornecedor.setVersao(versao);
		notaFiscalEntradaFornecedor.setEmissorInscricaoEstadualSubstituto(emissorInscricaoEstadualSubstituto);
		notaFiscalEntradaFornecedor.setEmissorInscricaoMunicipal(emissorInscricaoMunicipal);
		notaFiscalEntradaFornecedor.setValorBaseICMS(valorBaseICMS);
		notaFiscalEntradaFornecedor.setValorICMS(valorICMS);
		notaFiscalEntradaFornecedor.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
		notaFiscalEntradaFornecedor.setValorICMSSubstituto(valorICMSSubstituto);
		notaFiscalEntradaFornecedor.setValorProdutos(valorProdutos);
		notaFiscalEntradaFornecedor.setValorFrete(valorFrete);
		notaFiscalEntradaFornecedor.setValorSeguro(valorSeguro);
		notaFiscalEntradaFornecedor.setValorOutro(valorOutro);
		notaFiscalEntradaFornecedor.setValorIPI(valorIPI);
		notaFiscalEntradaFornecedor.setValorNF(valorNF);
		notaFiscalEntradaFornecedor.setFrete(frete);
		notaFiscalEntradaFornecedor.setTransportadoraCNPJ(transportadoraCNPJ);
		notaFiscalEntradaFornecedor.setTransportadoraNome(transportadoraNome);
		notaFiscalEntradaFornecedor.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
		notaFiscalEntradaFornecedor.setTransportadoraEndereco(transportadoraEndereco);
		notaFiscalEntradaFornecedor.setTransportadoraMunicipio(transportadoraMunicipio);
		notaFiscalEntradaFornecedor.setTransportadoraUF(transportadoraUF);
		notaFiscalEntradaFornecedor.setTransportadoraQuantidade(transportadoraQuantidade);
		notaFiscalEntradaFornecedor.setTransportadoraEspecie(transportadoraEspecie);
		notaFiscalEntradaFornecedor.setTransportadoraMarca(transportadoraMarca);
		notaFiscalEntradaFornecedor.setTransportadoraNumeracao(transportadoraNumeracao);
		notaFiscalEntradaFornecedor.setTransportadoraPesoBruto(transportadoraPesoBruto);
		notaFiscalEntradaFornecedor.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
		notaFiscalEntradaFornecedor.setTransportadoraANTT(transportadoraANTT);
		notaFiscalEntradaFornecedor.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
		notaFiscalEntradaFornecedor.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);
		notaFiscalEntradaFornecedor.setISSQNTotal(ISSQNTotal);
		notaFiscalEntradaFornecedor.setISSQNBase(ISSQNBase);
		notaFiscalEntradaFornecedor.setISSQNValor(ISSQNValor);
		notaFiscalEntradaFornecedor.setInformacoesComplementares(informacoesComplementares);
		notaFiscalEntradaFornecedor.setNumeroFatura(numeroFatura);
		notaFiscalEntradaFornecedor.setValorFatura(valorFatura);		
		
		return notaFiscalEntradaFornecedor;
		
	}
	
	
	public static NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedorNFE(
			CFOP cfop,
			Long numero,
			String serie,
			String chaveAcesso,
			Fornecedor fornecedor,
			StatusEmissaoNfe statusEmissaoNfe,
			TipoEmissaoNfe tipoEmissaoNfe,
			TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, 
			BigDecimal valorBruto, 
			BigDecimal valorDesconto, 
			BigDecimal valorLiquido,
			boolean indEmitida,
			String naturezaOperacao,
			String formaPagamento,
			String horaSaida,
			String ambiente,
			String protocolo,
			String versao,
			String emissorInscricaoEstadualSubstituto,
			String emissorInscricaoMunicipal,
			BigDecimal valorBaseICMS,
			BigDecimal valorICMS,
			BigDecimal valorBaseICMSSubstituto,
			BigDecimal valorICMSSubstituto,
			BigDecimal valorProdutos,
			BigDecimal valorFrete,
			BigDecimal valorSeguro,
			BigDecimal valorOutro,
			BigDecimal valorIPI,
			BigDecimal valorNF,
			Integer frete,
			String transportadoraCNPJ,
			String transportadoraNome,
			String transportadoraInscricaoEstadual,
			String transportadoraEndereco,
			String transportadoraMunicipio,
			String transportadoraUF,
			String transportadoraQuantidade,
			String transportadoraEspecie,
			String transportadoraMarca,
			String transportadoraNumeracao,
			BigDecimal transportadoraPesoBruto,
			BigDecimal transportadoraPesoLiquido,
			String transportadoraANTT,
			String transportadoraPlacaVeiculo,
			String transportadoraPlacaVeiculoUF,
			BigDecimal ISSQNTotal,
			BigDecimal ISSQNBase,
			BigDecimal ISSQNValor,
			String informacoesComplementares,
			String numeroFatura,
			BigDecimal valorFatura ) {
		
		NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedor = new NotaFiscalSaidaFornecedor();
		
		notaFiscalSaidaFornecedor.setCfop(cfop);
		notaFiscalSaidaFornecedor.setChaveAcesso(chaveAcesso);
		notaFiscalSaidaFornecedor.setDataEmissao(new Date());
		notaFiscalSaidaFornecedor.setDataExpedicao(new Date());
		notaFiscalSaidaFornecedor.setNumero(numero);
		notaFiscalSaidaFornecedor.setSerie(serie);
		
		//notaFiscalSaidaFornecedor.setOrigem(Origem.INTERFACE);
		
		//notaFiscalSaidaFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		
		notaFiscalSaidaFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		
		//notaFiscalSaidaFornecedor.setUsuario(usuario);
		
		notaFiscalSaidaFornecedor.setFornecedor(fornecedor);
		notaFiscalSaidaFornecedor.setValorBruto(valorBruto);
		notaFiscalSaidaFornecedor.setValorDesconto(valorDesconto);
		notaFiscalSaidaFornecedor.setValorLiquido(valorLiquido);
		notaFiscalSaidaFornecedor.setStatusEmissaoNfe(statusEmissaoNfe);
		notaFiscalSaidaFornecedor.setTipoEmissaoNfe(tipoEmissaoNfe);
		notaFiscalSaidaFornecedor.setEmitida(indEmitida);
		

		notaFiscalSaidaFornecedor.setNaturezaOperacao(naturezaOperacao);
		notaFiscalSaidaFornecedor.setFormaPagamento(formaPagamento);
		notaFiscalSaidaFornecedor.setHoraSaida(horaSaida);
		notaFiscalSaidaFornecedor.setAmbiente(ambiente);
		notaFiscalSaidaFornecedor.setProtocolo(protocolo);
		notaFiscalSaidaFornecedor.setVersao(versao);
		notaFiscalSaidaFornecedor.setEmissorInscricaoEstadualSubstituto(emissorInscricaoEstadualSubstituto);
		notaFiscalSaidaFornecedor.setEmissorInscricaoMunicipal(emissorInscricaoMunicipal);
		notaFiscalSaidaFornecedor.setValorBaseICMS(valorBaseICMS);
		notaFiscalSaidaFornecedor.setValorICMS(valorICMS);
		notaFiscalSaidaFornecedor.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
		notaFiscalSaidaFornecedor.setValorICMSSubstituto(valorICMSSubstituto);
		notaFiscalSaidaFornecedor.setValorProdutos(valorProdutos);
		notaFiscalSaidaFornecedor.setValorFrete(valorFrete);
		notaFiscalSaidaFornecedor.setValorSeguro(valorSeguro);
		notaFiscalSaidaFornecedor.setValorOutro(valorOutro);
		notaFiscalSaidaFornecedor.setValorIPI(valorIPI);
		notaFiscalSaidaFornecedor.setValorNF(valorNF);
		notaFiscalSaidaFornecedor.setFrete(frete);
		notaFiscalSaidaFornecedor.setTransportadoraCNPJ(transportadoraCNPJ);
		notaFiscalSaidaFornecedor.setTransportadoraNome(transportadoraNome);
		notaFiscalSaidaFornecedor.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
		notaFiscalSaidaFornecedor.setTransportadoraEndereco(transportadoraEndereco);
		notaFiscalSaidaFornecedor.setTransportadoraMunicipio(transportadoraMunicipio);
		notaFiscalSaidaFornecedor.setTransportadoraUF(transportadoraUF);
		notaFiscalSaidaFornecedor.setTransportadoraQuantidade(transportadoraQuantidade);
		notaFiscalSaidaFornecedor.setTransportadoraEspecie(transportadoraEspecie);
		notaFiscalSaidaFornecedor.setTransportadoraMarca(transportadoraMarca);
		notaFiscalSaidaFornecedor.setTransportadoraNumeracao(transportadoraNumeracao);
		notaFiscalSaidaFornecedor.setTransportadoraPesoBruto(transportadoraPesoBruto);
		notaFiscalSaidaFornecedor.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
		notaFiscalSaidaFornecedor.setTransportadoraANTT(transportadoraANTT);
		notaFiscalSaidaFornecedor.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
		notaFiscalSaidaFornecedor.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);
		notaFiscalSaidaFornecedor.setISSQNTotal(ISSQNTotal);
		notaFiscalSaidaFornecedor.setISSQNBase(ISSQNBase);
		notaFiscalSaidaFornecedor.setISSQNValor(ISSQNValor);
		notaFiscalSaidaFornecedor.setInformacoesComplementares(informacoesComplementares);
		notaFiscalSaidaFornecedor.setNumeroFatura(numeroFatura);
		notaFiscalSaidaFornecedor.setValorFatura(valorFatura);		
		
		
		return notaFiscalSaidaFornecedor;
		
	}
	
	
	
	public static RecebimentoFisico recebimentoFisico(NotaFiscalEntradaFornecedor notaFiscalFornecedor, 
													  Usuario usuario,
													  Date dataRecebimento,
													  Date dataConfirmacao,
													  StatusConfirmacao statusConfirmacao) {
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		
		recebimentoFisico.setDataRecebimento(dataRecebimento);
		recebimentoFisico.setDataConfirmacao(dataConfirmacao);
		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);
		recebimentoFisico.setStatusConfirmacao(statusConfirmacao);
		recebimentoFisico.setRecebedor(usuario);
		
		return recebimentoFisico;
	}

	public static ItemRecebimentoFisico itemRecebimentoFisico(ItemNotaFiscalEntrada itemNotaFiscal, 
															  RecebimentoFisico recebimentoFisico,
															  BigInteger qtdeFisico) {
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(qtdeFisico);
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		return itemRecebimentoFisico;
	}
	
	public static EstoqueProduto estoqueProduto(ProdutoEdicao produtoEdicao, BigInteger qtde) {
		EstoqueProduto estoqueProduto = new EstoqueProduto();
		estoqueProduto.setProdutoEdicao(produtoEdicao);
		estoqueProduto.setQtde(qtde);
		estoqueProduto.setQtdeSuplementar(qtde.add(BigInteger.TEN));
		estoqueProduto.setQtdeDevolucaoEncalhe(qtde);
		estoqueProduto.setQtdeDevolucaoFornecedor(qtde.add(BigInteger.TEN));
		return estoqueProduto;
	}
	
	public static EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado(Date data, ProdutoEdicao produtoEdicao, Cota cota, BigInteger qtde) {
		EstoqueProdutoCotaJuramentado ep = new EstoqueProdutoCotaJuramentado();
		ep.setCota(cota);
		ep.setData(data);
		ep.setProdutoEdicao(produtoEdicao);
		ep.setQtde(qtde);
		return ep;
	}
	
	public static EstoqueProdutoCota estoqueProdutoCota(ProdutoEdicao produtoEdicao, BigInteger qtde,
			Cota cota, List<MovimentoEstoqueCota> movimentos) {
		EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
		estoqueProdutoCota.setCota(cota);
		estoqueProdutoCota.setMovimentos(movimentos);
		estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
		estoqueProdutoCota.setQtdeDevolvida(qtde);
		estoqueProdutoCota.setQtdeRecebida(qtde);
		estoqueProdutoCota.setVersao(2L);
		
		return estoqueProdutoCota;
	}
	
	public static MovimentoEstoque movimentoEstoque(ItemRecebimentoFisico itemRecebimentoFisico,
													ProdutoEdicao produtoEdicao, 
													TipoMovimentoEstoque tipoMovimento,
													Usuario usuario, 
													EstoqueProduto estoqueProduto,
													Date dataInclusao,
													BigInteger qtde, 
													StatusAprovacao status,
													String motivo) {

		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		
		movimentoEstoque.setData(dataInclusao);
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(qtde);
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		movimentoEstoque.setEstoqueProduto(estoqueProduto);
		movimentoEstoque.setStatus(status);
		movimentoEstoque.setMotivo(motivo);
		return movimentoEstoque;
	}
	
	public static ParametroSistema parametroSistema(TipoParametroSistema tipoParametroSistema, String valor) {
		ParametroSistema parametroSistema = new ParametroSistema();
		parametroSistema.setTipoParametroSistema(tipoParametroSistema);
		parametroSistema.setValor(valor);
		
		return parametroSistema;
	}
	
	public static ParametroSistema parametroSistema(Long id, TipoParametroSistema tipoParametroSistema, String valor) {
		ParametroSistema ps = parametroSistema(tipoParametroSistema, valor);
		ps.setId(id);
		
		return ps;
	}
	
	public static Diferenca diferenca(BigInteger qtde,
									  Usuario usuarioResponsavel,
									  ProdutoEdicao produtoEdicao,
									  TipoDiferenca tipoDiferenca,
									  StatusConfirmacao statusConfirmacao,
									  ItemRecebimentoFisico itemRecebimentoFisico,
									  Boolean automatica, TipoEstoque tipoEstoque, 
									  TipoDirecionamentoDiferenca tipoDirecionamento,
									  Date dataLancamentoDiferenca) {
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(qtde);
		diferenca.setResponsavel(usuarioResponsavel);
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setTipoDiferenca(tipoDiferenca);
		diferenca.setStatusConfirmacao(statusConfirmacao);
		diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
		diferenca.setAutomatica(automatica);
		diferenca.setTipoEstoque(tipoEstoque);
		diferenca.setTipoDirecionamento(tipoDirecionamento);
		diferenca.setDataMovimento(dataLancamentoDiferenca);
		
		return diferenca;
	}
	
	public static EstoqueProdutoCota estoqueProdutoCota(
			ProdutoEdicao produtoEdicao, Cota cota, BigInteger qtdeRecebida,
			BigInteger qtdeDevolvida) {
		EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
		estoqueProdutoCota.setCota(cota);
		estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
		estoqueProdutoCota.setQtdeRecebida(qtdeRecebida);
		estoqueProdutoCota.setQtdeDevolvida(qtdeDevolvida);
		return estoqueProdutoCota;
	}
	
	public static MovimentoEstoqueCota movimentoEstoqueCota(
			ProdutoEdicao produtoEdicao, TipoMovimentoEstoque tipoMovimento,
			Usuario usuario, EstoqueProdutoCota estoqueProdutoCota,
			BigInteger qtde, Cota cota, StatusAprovacao statusAprovacao, String motivo) {

		MovimentoEstoqueCota movimentoEstoque = new MovimentoEstoqueCota();
		movimentoEstoque.setData(new Date());
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(qtde);
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		movimentoEstoque.setEstoqueProdutoCota(estoqueProdutoCota);
		movimentoEstoque.setCota(cota);
		movimentoEstoque.setStatus(statusAprovacao);
		movimentoEstoque.setMotivo(motivo);
		return movimentoEstoque;
	}

	public static MovimentoEstoqueCota movimentoEstoqueCotaEnvioEncalhe(
			Date dataInclusao,
			ProdutoEdicao produtoEdicao, 	
			TipoMovimentoEstoque tipoMovimento,
			Usuario usuario, 
			EstoqueProdutoCota estoqueProdutoCota,
			BigInteger qtde, 
			Cota cota, 
			StatusAprovacao statusAprovacao, 
			String motivo) {

		MovimentoEstoqueCota movimentoEstoque = new MovimentoEstoqueCota();
		movimentoEstoque.setData(dataInclusao);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(qtde);
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		movimentoEstoque.setEstoqueProdutoCota(estoqueProdutoCota);
		movimentoEstoque.setCota(cota);
		movimentoEstoque.setStatus(statusAprovacao);
		movimentoEstoque.setMotivo(motivo);
		return movimentoEstoque;
	}
	
	public static ConferenciaEncalhe conferenciaEncalhe(
			MovimentoEstoqueCota movimentoEstoqueCota,
			ChamadaEncalheCota chamadaEncalheCota,
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			Date data, BigInteger qtdeInformada, BigInteger qtde,
			ProdutoEdicao produtoEdicao) {
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();
		
		conferenciaEncalhe.setMovimentoEstoqueCota(movimentoEstoqueCota);
		conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
		conferenciaEncalhe.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		conferenciaEncalhe.setData(data);
		conferenciaEncalhe.setQtdeInformada(qtdeInformada);
		conferenciaEncalhe.setQtde(qtde);
		conferenciaEncalhe.setProdutoEdicao(produtoEdicao);
		
		return conferenciaEncalhe;
		
	}
	
	public static ChamadaEncalheCota chamadaEncalheCota(
			ChamadaEncalhe chamadaEncalhe,
			boolean conferido,
			Cota cota,
			BigInteger qtdePrevista) {
		
		ChamadaEncalheCota chamadaEncalheCota = new ChamadaEncalheCota();
		
		chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		chamadaEncalheCota.setFechado(conferido);
		chamadaEncalheCota.setCota(cota);
		chamadaEncalheCota.setQtdePrevista(qtdePrevista);
		
		return chamadaEncalheCota;
		
	}
	
	public static RateioDiferenca rateioDiferenca(BigInteger qtde, Cota cota, Diferenca diferenca, EstudoCota estudoCota, Date dataNotaEnvio){
		RateioDiferenca rateioDiferenca = new RateioDiferenca();
		rateioDiferenca.setCota(cota);
		rateioDiferenca.setDiferenca(diferenca);
		rateioDiferenca.setEstudoCota(estudoCota);
		rateioDiferenca.setQtde(qtde);
		rateioDiferenca.setDataNotaEnvio(dataNotaEnvio);
		
		return rateioDiferenca;
	}

	public static Expedicao expedicao(Usuario responsavel,Date dataExpedicao){
		
		Expedicao expedicao = new Expedicao();
		expedicao.setResponsavel(responsavel);
		expedicao.setDataExpedicao(dataExpedicao);
		
		return expedicao;
	}
	
	public static Lancamento lancamentos(TipoLancamento tipoLancamento,
			ProdutoEdicao produtoEdicao, Date dlp, Date drp, Date dataCriacao,
			Date dataStatus, BigInteger reparte, StatusLancamento statusLancamento,
			List<ItemRecebimentoFisico> recebimentos, Integer sequenciaMatriz) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDataCriacao(dataCriacao);
		lancamento.setDataStatus(dataStatus);
		lancamento.setReparte(reparte);
		lancamento.setStatus(statusLancamento);
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dlp);
		lancamento.setDataLancamentoDistribuidor(dlp);
		lancamento.setDataRecolhimentoPrevista(drp);
		lancamento.setDataRecolhimentoDistribuidor(drp);
		lancamento.setSequenciaMatriz(sequenciaMatriz);
		
		for(ItemRecebimentoFisico x : recebimentos){
			lancamento.addRecebimento(x);
		}
		
		
		return lancamento;
	}
	
	public static Box criarBox(Integer codigo, String nome, TipoBox tipoBox) {
		Box box = new Box();
		box.setCodigo(codigo);
		box.setNome(nome);
		box.setTipoBox(tipoBox);
		return box;
	}
	

	public static Boleto boleto(String nossoNumero,
								String digitoNossoNumero,
								String nossoNumeroCompleto,
				                Date dataEmissao,
				                Date dataVencimento,
				                Date dataPagamento,
				                BigDecimal encargos,
				                BigDecimal valor,
				                String tipoBaixa,
				                String acao,
				                StatusCobranca status,
				                Cota cota,
				                Banco banco,
				                Divida divida,
				                Integer vias){
			
		Boleto boleto = new Boleto();
		boleto.setNossoNumero(nossoNumero);
		boleto.setDigitoNossoNumero(digitoNossoNumero);
		boleto.setNossoNumeroCompleto(nossoNumeroCompleto);
		boleto.setDataEmissao(dataEmissao);
		boleto.setDataVencimento(dataVencimento);
		boleto.setDataPagamento(dataPagamento);
		boleto.setEncargos(encargos);
		boleto.setValor(valor);
		boleto.setTipoBaixa(tipoBaixa);
		boleto.setStatusCobranca(status);
		boleto.setCota(cota);
		boleto.setBanco(banco);
		boleto.setDivida(divida);
		boleto.setVias(vias);
		
		return boleto;
	}
	
	public static ContratoCota criarContratoCota(Cota cota,
			boolean exigeDocSuspensao, Date dataInicio, Date dataTermino, Integer prazo, Integer aviso) {
		ContratoCota contratoCota = new ContratoCota();
		contratoCota.setCota(cota);
		contratoCota.setExigeDocumentacaoSuspencao(exigeDocSuspensao);
		contratoCota.setDataInicio(dataInicio);
		contratoCota.setDataTermino(dataTermino);
		contratoCota.setPrazo(prazo);
		contratoCota.setAvisoPrevioRescisao(aviso);
		return contratoCota;
	}

	public static PoliticaCobranca criarPoliticaCobranca(
			Distribuidor distribuidor, FormaCobranca formaCobranca, 
			boolean aceitaBaixaPagamentoMaior, boolean aceitaBaixaPagamentoMenor,
			boolean aceitaBaixaPagamentoVencido, int inadimplenciasSuspencao,
			String assuntoEmailCobranca, String mensagemEmailCobranca, 
			boolean principal,FormaEmissao formaEmissao) {
		
		PoliticaCobranca politicaCobranca = new PoliticaCobranca();
		politicaCobranca.setAceitaBaixaPagamentoMaior(aceitaBaixaPagamentoMaior);
		politicaCobranca.setAceitaBaixaPagamentoMenor(aceitaBaixaPagamentoMenor);
		politicaCobranca.setAceitaBaixaPagamentoVencido(aceitaBaixaPagamentoVencido);
		politicaCobranca.setDistribuidor(distribuidor);
		politicaCobranca.setInadimplenciasSuspencao(inadimplenciasSuspencao);
		politicaCobranca.setFormaCobranca(formaCobranca);
		politicaCobranca.setAssuntoEmailCobranca(assuntoEmailCobranca);
		politicaCobranca.setMensagemEmailCobranca(mensagemEmailCobranca);
		politicaCobranca.setPrincipal(principal);
		politicaCobranca.setFormaEmissao(formaEmissao);
		politicaCobranca.setAtivo(true);
		return politicaCobranca;
	}
	
	public static Feriado feriado(
			Date data, 
			TipoFeriado tipoFeriado,
			UnidadeFederacao unidadeFederacao,
			Localidade localidade, 
			String descricao,
			boolean indEfetuaCobranca,
			boolean indOpera,
			boolean indRepeteAnualmente) {
		
		Feriado feriado = new Feriado();
		
		feriado.setData(data);
		feriado.setDescricao(descricao);
		feriado.setTipoFeriado(tipoFeriado);
		feriado.setUnidadeFederacao(unidadeFederacao);
		feriado.setLocalidade(localidade);
		
		feriado.setIndEfetuaCobranca(indEfetuaCobranca);
		feriado.setIndOpera(indOpera);
		feriado.setIndRepeteAnualmente(indRepeteAnualmente);
		
		return feriado;
		
	}

	public static Endereco criarEndereco(TipoEndereco tipoEndereco, String cep,
										 String logradouro, String numero, 
										 String bairro, String cidade, String uf,Integer codigoCidadeIBGE) {
		
		Endereco endereco = new Endereco();
		
		endereco.setBairro(bairro);
		endereco.setCep(cep);
		endereco.setCidade(cidade);
		endereco.setLogradouro(logradouro);
		endereco.setNumero(numero);
		endereco.setUf(uf);
		endereco.setCodigoCidadeIBGE(codigoCidadeIBGE);
		
		return endereco;
	}
	
	public static ParametroSistema[] criarParametrosEmail() {
		
		ParametroSistema[] parametrosEmail = new ParametroSistema[5];
		parametrosEmail[0] = Fixture.parametroSistema(1L, TipoParametroSistema.EMAIL_HOST,"smtp.gmail.com");
		parametrosEmail[1] = Fixture.parametroSistema(2L, TipoParametroSistema.EMAIL_PROTOCOLO,"smtps");
		parametrosEmail[2] = Fixture.parametroSistema(3L, TipoParametroSistema.EMAIL_USUARIO, "sys.discover@gmail.com");
		parametrosEmail[3] = Fixture.parametroSistema(4L, TipoParametroSistema.EMAIL_SENHA, "discover10");
		parametrosEmail[4] = Fixture.parametroSistema(5L, TipoParametroSistema.EMAIL_PORTA, "465");
		
		return parametrosEmail;
	}
	
	public static Banco banco(Long agencia, boolean ativo, Integer carteira, String codigoCedente, Long conta, String dvAgencia,
								 String dvConta, String instrucoes, String apelido, String nome, String numeroBanco, BigDecimal juros, BigDecimal multa) {
		
		Banco banco = new Banco();
		
		banco.setAgencia(agencia);
		banco.setAtivo(ativo);
		banco.setCarteira(carteira);
		banco.setCodigoCedente(codigoCedente);
		banco.setConta(conta);
		banco.setDvAgencia(dvAgencia);
		banco.setDvConta(dvConta);
		banco.setInstrucoes(instrucoes);
		banco.setApelido(apelido);
		banco.setNome(nome);
		banco.setNumeroBanco(numeroBanco);
		banco.setJuros(juros);
		banco.setMulta(multa);
		
		return banco;
	}
	
	public static ControleBaixaBancaria controleBaixaBancaria(Date data, StatusControle status, Usuario responsavel, Banco banco) {
	
		ControleBaixaBancaria controleBaixaBancaria = new ControleBaixaBancaria();
		
		controleBaixaBancaria.setData(data);
		controleBaixaBancaria.setStatus(status);
		controleBaixaBancaria.setResponsavel(responsavel);
		controleBaixaBancaria.setBanco(banco);
		
		return controleBaixaBancaria;
	}
	
	public static MovimentoFinanceiroCota movimentoFinanceiroCota(Cota cota,
			TipoMovimentoFinanceiro tipoMovimento, Usuario usuario,
			BigDecimal valor, List<MovimentoEstoqueCota> lista,
			StatusAprovacao statusAprovacao, Date data, boolean lancamentoManual) {
		MovimentoFinanceiroCota mfc = new MovimentoFinanceiroCota();
		mfc.setAprovadoAutomaticamente(true);
		mfc.setCota(cota);
		mfc.setDataAprovacao(data);
		mfc.setData(data);
		mfc.setMovimentos(lista);
		mfc.setStatus(statusAprovacao);
		mfc.setTipoMovimento(tipoMovimento);
		mfc.setUsuario(usuario);
		mfc.setValor(valor);
		mfc.setLancamentoManual(lancamentoManual);
		mfc.setObservacao(cota==null?"":"Movimento Financeiro da cota "+cota.getNumeroCota()+".");
		return mfc;
	}



	public static HistoricoAcumuloDivida criarHistoricoAcumuloDivida(
			Divida divida, Date dataInclusao, Usuario usuario,
			StatusInadimplencia status) {
		HistoricoAcumuloDivida historicoInadimplencia = new HistoricoAcumuloDivida();
		historicoInadimplencia.setDivida(divida);
		historicoInadimplencia.setDataInclusao(dataInclusao);
		historicoInadimplencia.setResponsavel(usuario);
		historicoInadimplencia.setStatus(status);
		return historicoInadimplencia;
	}
	
	public static ConsolidadoFinanceiroCota consolidadoFinanceiroCota(
			List<MovimentoFinanceiroCota> movimentos, Cota cota, Date data,
			BigDecimal valorConsignado,BigDecimal valorVendaEncalhe,
			BigDecimal encalhe, BigDecimal debitoCredito, BigDecimal encargos, 
			BigDecimal valorPostergado, BigDecimal pendente) {
		
		ConsolidadoFinanceiroCota consolidado = new ConsolidadoFinanceiroCota();
		consolidado.setMovimentos(movimentos);
		consolidado.setCota(cota);
		consolidado.setDataConsolidado(data);
		
		consolidado.setConsignado(valorConsignado);
		consolidado.setVendaEncalhe(valorVendaEncalhe);
		consolidado.setEncalhe(encalhe);
		consolidado.setDebitoCredito(debitoCredito);
		consolidado.setEncargos(encargos);
		consolidado.setValorPostergado(valorPostergado);
		consolidado.setPendente(pendente);
		
		consolidado.setTotal(valorConsignado.subtract(encalhe).add(valorVendaEncalhe).add(debitoCredito).add(encargos).add(valorPostergado).add(pendente));
		
		consolidado.setVendaEncalhe(valorVendaEncalhe);
		
		return consolidado;
	}
	
	public static ConsolidadoFinanceiroCota consolidadoFinanceiroCotaEncalhe(
			List<MovimentoFinanceiroCota> movimentos, Cota cota, Date data,
			BigDecimal valorConsolidado, BigDecimal valorEncalhe) {
		
		ConsolidadoFinanceiroCota consolidado = new ConsolidadoFinanceiroCota();
		
		consolidado.setConsignado(valorConsolidado);
		consolidado.setEncalhe(valorEncalhe);
		consolidado.setMovimentos(movimentos);
		consolidado.setCota(cota);
		consolidado.setDataConsolidado(data);
		consolidado.setTotal(valorConsolidado);
		
		return consolidado;
	}
	
	public static Divida divida(ConsolidadoFinanceiroCota consolidado,
			Cota cota, Date data, Usuario usuario, StatusDivida status,
			BigDecimal valor, boolean acumulada) {
		Divida divida = new Divida();
		divida.setConsolidado(consolidado);
		divida.setCota(cota);
		divida.setData(data);
		divida.setResponsavel(usuario);
		divida.setStatus(status);
		divida.setValor(valor);
		divida.setAcumulada(acumulada);
		return divida;
	}
	
	public static FormaCobranca formaCobrancaBoleto(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.BOLETO);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		formaBoleto.setFormaCobrancaBoleto(FormaCobrancaBoleto.SEM_REGISTRO);
		
		
		return formaBoleto;
	}
	
	public static FormaCobranca formaCobrancaDinheiro(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.DINHEIRO);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}
	
	public static FormaCobranca formaCobrancaCheque(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.CHEQUE);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}
	
	public static FormaCobranca formaCobrancaDeposito(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.DEPOSITO);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}
	
	public static FormaCobranca formaCobrancaTransferencia(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.TRANSFERENCIA_BANCARIA);
		formaBoleto.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}
	
	public static ParametrosCotaNotaFiscalEletronica parametrosCotaNotaFiscalEletronica(boolean emiteNotaFiscalEletronica, String emailNotaFiscalEletronica) {
		
		ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronica = new ParametrosCotaNotaFiscalEletronica();
		
		parametroCotaNotaFiscalEletronica.setEmailNotaFiscalEletronica(emailNotaFiscalEletronica);
		parametroCotaNotaFiscalEletronica.setEmiteNotaFiscalEletronica(emiteNotaFiscalEletronica);
		
		return parametroCotaNotaFiscalEletronica;
	}

	public static ParametroCobrancaCota parametroCobrancaCota(Set<FormaCobranca> formasCobranca,
							Integer numeroAcumuloDivida, BigDecimal valor, Cota cota,
							int fatorVencimento,boolean recebeCobrancaEmail, BigDecimal valorMininoCobranca, TipoCota tipoCota) {
		
		ParametroCobrancaCota parametro = new ParametroCobrancaCota();
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		
		politicaSuspensao.setNumeroAcumuloDivida(numeroAcumuloDivida);
		politicaSuspensao.setValor(valor);
		
		parametro.setCota(cota);
		parametro.setFatorVencimento(fatorVencimento);
		parametro.setValorMininoCobranca(valorMininoCobranca);
		parametro.setPoliticaSuspensao(politicaSuspensao);
		parametro.setFormasCobrancaCota(formasCobranca);
		parametro.setTipoCota(tipoCota);
		
		return parametro;
	}
	
	public static Rota rota(String descricaoRota, Roteiro roteiro){
		Rota rota = new Rota();
		rota.setDescricaoRota(descricaoRota);
		rota.setOrdem(ORDEM_ROTA.getAndIncrement());
		rota.setRoteiro(roteiro);
		roteiro.addRota(rota);
		return rota;
	}
	
	public static Roteiro roteiro(String descricaoRoteiro){
		Roteiro rota = new Roteiro();
		rota.setDescricaoRoteiro(descricaoRoteiro);
		rota.setOrdem(0);
		rota.setTipoRoteiro(TipoRoteiro.ESPECIAL);
		return rota;
	}

	public static CobrancaDinheiro criarCobrancaDinheiro(String nossoNumero,
            Date dataEmissao,
            Date dataVencimento,
            Date dataPagamento,
            BigDecimal encargos,
            BigDecimal valor,
            String tipoBaixa,
            String acao,
            StatusCobranca status,
            Cota cota,
            Banco banco,
            Divida divida,
            Integer vias) {
		
		CobrancaDinheiro cobrencaDinheiro= new CobrancaDinheiro();
		cobrencaDinheiro.setNossoNumero(nossoNumero);
		cobrencaDinheiro.setDataEmissao(dataEmissao);
		cobrencaDinheiro.setDataVencimento(dataVencimento);
		cobrencaDinheiro.setDataPagamento(dataPagamento);
		cobrencaDinheiro.setEncargos(encargos);
		cobrencaDinheiro.setValor(valor);
		cobrencaDinheiro.setTipoBaixa(tipoBaixa);
		cobrencaDinheiro.setStatusCobranca(status);
		cobrencaDinheiro.setCota(cota);
		cobrencaDinheiro.setBanco(banco);
		cobrencaDinheiro.setDivida(divida);
		cobrencaDinheiro.setVias(vias);
		
		return cobrencaDinheiro;
	}
	
	public static CobrancaDeposito criarCobrancaDeposito(String nossoNumero,
            Date dataEmissao,
            Date dataVencimento,
            Date dataPagamento,
            BigDecimal encargos,
            BigDecimal valor,
            String tipoBaixa,
            String acao,
            StatusCobranca status,
            Cota cota,
            Banco banco,
            Divida divida,
            Integer vias) {
		
		CobrancaDeposito cobrencaDeposito= new CobrancaDeposito();
		cobrencaDeposito.setNossoNumero(nossoNumero);
		cobrencaDeposito.setDataEmissao(dataEmissao);
		cobrencaDeposito.setDataVencimento(dataVencimento);
		cobrencaDeposito.setDataPagamento(dataPagamento);
		cobrencaDeposito.setEncargos(encargos);
		cobrencaDeposito.setValor(valor);
		cobrencaDeposito.setTipoBaixa(tipoBaixa);
		cobrencaDeposito.setStatusCobranca(status);
		cobrencaDeposito.setCota(cota);
		cobrencaDeposito.setBanco(banco);
		cobrencaDeposito.setDivida(divida);
		cobrencaDeposito.setVias(vias);
		
		return cobrencaDeposito;
	}
	
	public static CobrancaCheque cobrancaCheque(String nossoNumero,
									            Date dataEmissao,
									            Date dataVencimento,
									            Date dataPagamento,
									            BigDecimal encargos,
									            BigDecimal valor,
									            String tipoBaixa,
									            String acao,
									            StatusCobranca status,
									            Cota cota,
									            Divida divida,
									            Integer vias){
		
		CobrancaCheque cobrancaCheque = new CobrancaCheque();
		cobrancaCheque.setNossoNumero(nossoNumero);
		cobrancaCheque.setDataEmissao(dataEmissao);
		cobrancaCheque.setDataVencimento(dataVencimento);
		cobrancaCheque.setDataPagamento(dataPagamento);
		cobrancaCheque.setEncargos(encargos);
		cobrancaCheque.setValor(valor);
		cobrancaCheque.setTipoBaixa(tipoBaixa);
		cobrancaCheque.setStatusCobranca(status);
		cobrancaCheque.setCota(cota);
		cobrancaCheque.setDivida(divida);
		cobrancaCheque.setVias(vias);
		
		
		return cobrancaCheque;
	}
	
	public static CobrancaDeposito cobrancaDeposito(String nossoNumero,
									            Date dataEmissao,
									            Date dataVencimento,
									            Date dataPagamento,
									            BigDecimal encargos,
									            BigDecimal valor,
									            String tipoBaixa,
									            String acao,
									            StatusCobranca status,
									            Cota cota,
									            Banco banco,
									            Divida divida,
									            Integer vias
									            ){

		CobrancaDeposito cobrancaDeposito = new CobrancaDeposito();
		cobrancaDeposito.setNossoNumero(nossoNumero);
		cobrancaDeposito.setDataEmissao(dataEmissao);
		cobrancaDeposito.setDataVencimento(dataVencimento);
		cobrancaDeposito.setDataPagamento(dataPagamento);
		cobrancaDeposito.setEncargos(encargos);
		cobrancaDeposito.setValor(valor);
		cobrancaDeposito.setTipoBaixa(tipoBaixa);
		cobrancaDeposito.setStatusCobranca(status);
		cobrancaDeposito.setCota(cota);
		cobrancaDeposito.setDivida(divida);
		cobrancaDeposito.setVias(vias);
		cobrancaDeposito.setBanco(banco);
		
		return cobrancaDeposito;
	}
	
	
	public static CobrancaDinheiro cobrancaDinheiro(String nossoNumero,
										            Date dataEmissao,
										            Date dataVencimento,
										            Date dataPagamento,
										            BigDecimal encargos,
										            BigDecimal valor,
										            String tipoBaixa,
										            String acao,
										            StatusCobranca status,
										            Cota cota,
										            Banco banco,
										            Divida divida,
										            Integer vias
										            ){
		
		CobrancaDinheiro cobrancaDinheiro = new CobrancaDinheiro();
		cobrancaDinheiro.setNossoNumero(nossoNumero);
		cobrancaDinheiro.setDataEmissao(dataEmissao);
		cobrancaDinheiro.setDataVencimento(dataVencimento);
		cobrancaDinheiro.setDataPagamento(dataPagamento);
		cobrancaDinheiro.setEncargos(encargos);
		cobrancaDinheiro.setValor(valor);
		cobrancaDinheiro.setTipoBaixa(tipoBaixa);
		cobrancaDinheiro.setStatusCobranca(status);
		cobrancaDinheiro.setCota(cota);
		cobrancaDinheiro.setDivida(divida);
		cobrancaDinheiro.setVias(vias);
		
		return cobrancaDinheiro;
	}
	
	public static CobrancaTransferenciaBancaria cobrancaTransferencaiBancaria(String nossoNumero,
										            Date dataEmissao,
										            Date dataVencimento,
										            Date dataPagamento,
										            BigDecimal encargos,
										            BigDecimal valor,
										            String tipoBaixa,
										            String acao,
										            StatusCobranca status,
										            Cota cota,
										            Banco banco,
										            Divida divida,
										            Integer vias
										            ){
	
		CobrancaTransferenciaBancaria cobrancaTransferenciaBancaria = new CobrancaTransferenciaBancaria();
		cobrancaTransferenciaBancaria.setNossoNumero(nossoNumero);
		cobrancaTransferenciaBancaria.setDataEmissao(dataEmissao);
		cobrancaTransferenciaBancaria.setDataVencimento(dataVencimento);
		cobrancaTransferenciaBancaria.setDataPagamento(dataPagamento);
		cobrancaTransferenciaBancaria.setEncargos(encargos);
		cobrancaTransferenciaBancaria.setValor(valor);
		cobrancaTransferenciaBancaria.setTipoBaixa(tipoBaixa);
		cobrancaTransferenciaBancaria.setStatusCobranca(status);
		cobrancaTransferenciaBancaria.setCota(cota);
		cobrancaTransferenciaBancaria.setDivida(divida);
		cobrancaTransferenciaBancaria.setVias(vias);
		
		return cobrancaTransferenciaBancaria;
	}
	
	public static EnderecoDistribuidor enderecoDistribuidor(Distribuidor distribuidor, Endereco endereco, boolean isPrincipal,TipoEndereco tipoEndereco){
		
		EnderecoDistribuidor enderecoDistribuidor = new EnderecoDistribuidor();
		enderecoDistribuidor.setEndereco(endereco);
		enderecoDistribuidor.setDistribuidor(distribuidor);
		enderecoDistribuidor.setPrincipal(isPrincipal);
		enderecoDistribuidor.setTipoEndereco(tipoEndereco);
		
		return enderecoDistribuidor;
	}
	
	
	public static EnderecoCota enderecoCota(Cota cota, Endereco endereco, boolean isPrincipal,TipoEndereco tipoEndereco){
		
		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setEndereco(endereco);
		enderecoCota.setCota(cota);
		enderecoCota.setPrincipal(isPrincipal);
		enderecoCota.setTipoEndereco(tipoEndereco);
		
		return enderecoCota;
	}
	
	public static TelefoneDistribuidor telefoneDistribuidor(Distribuidor distribuidor, boolean principal, Telefone telefone, TipoTelefone tipoTelefone){
		
		TelefoneDistribuidor telefoneDistribuidor = new TelefoneDistribuidor();
		telefoneDistribuidor.setDistribuidor(distribuidor);
		telefoneDistribuidor.setPrincipal(principal);
		telefoneDistribuidor.setTelefone(telefone);
		telefoneDistribuidor.setTipoTelefone(tipoTelefone);
		
		return telefoneDistribuidor;
		
	}
	
	public static Telefone telefone(String ddd, String numero, String ramal){
		Telefone telefone = new Telefone();
		telefone.setDdd(ddd);
		telefone.setNumero(numero);
		telefone.setRamal(ramal);
		return telefone;
	}
	
	public static Editor criarEditor(Long codigo, 
									 PessoaJuridica pessoaJuridica,
									 boolean ativo) {
		
		Editor editor = new Editor();
		
		editor.setCodigo(10L);
		editor.setPessoaJuridica(pessoaJuridica);
		editor.setAtivo(ativo);
		
		return editor;
	}	
	
	public static TelefoneEntregador telefoneEntregador(Entregador entregador, boolean principal, Telefone telefone, TipoTelefone tipoTelefone){
		
		TelefoneEntregador telefoneEntregador = new TelefoneEntregador();
		telefoneEntregador.setEntregador(entregador);
		telefoneEntregador.setPrincipal(principal);
		telefoneEntregador.setTelefone(telefone);
		telefoneEntregador.setTipoTelefone(tipoTelefone);
		
		return telefoneEntregador;
		
	}
	
	public static EnderecoEntregador enderecoEntregador(Entregador entregador, Endereco endereco, boolean isPrincipal,TipoEndereco tipoEndereco){
		
		EnderecoEntregador enderecoEntregador = new EnderecoEntregador();
		enderecoEntregador.setEndereco(endereco);
		enderecoEntregador.setEntregador(entregador);
		enderecoEntregador.setPrincipal(isPrincipal);
		enderecoEntregador.setTipoEndereco(tipoEndereco);
		
		return enderecoEntregador;
	}
	
	public static Entregador criarEntregador(
			Long codigo, boolean comissionado,
			Date inicioAtividade, BigDecimal percentualComissao, Pessoa pessoa, 
			boolean procuracao, ProcuracaoEntregador procuracaoEntregador) {
		
		Entregador entregador = new Entregador();

		entregador.setCodigo(codigo);
		entregador.setComissionado(comissionado);
		entregador.setInicioAtividade(inicioAtividade);
		entregador.setPercentualComissao(percentualComissao);
		entregador.setPessoa(pessoa);
		entregador.setProcuracao(procuracao);
		entregador.setProcuracaoEntregador(procuracaoEntregador);
		
		return entregador;
	}
	
	public static PDV criarPDV(String nome,BigDecimal porcentagemFaturamento,
							   TamanhoPDV tamanhoPDV,Cota cota , 
							   Boolean principal, StatusPDV status,
							   CaracteristicasPDV caracteristicas,
							   LicencaMunicipal licencaMunicipal, 
							   SegmentacaoPDV segmentacao){

		PDV pdv = new PDV();
		pdv.setNome(nome);
		pdv.setPorcentagemFaturamento(porcentagemFaturamento);
		pdv.setTamanhoPDV(tamanhoPDV);
		pdv.setCota(cota);
		pdv.setStatus(status);
		pdv.setCaracteristicas(caracteristicas);
		
		pdv.setLicencaMunicipal(licencaMunicipal);
		
		pdv.setSegmentacao(segmentacao);
		return pdv;
	}
	
	public static CaracteristicasPDV criarCaracteristicaPDV(boolean balcaoCentral, boolean pontoPrincipal, 
															boolean possuiComputador, boolean possuiLuminoso, 
															String textoLuminoso){
		
		CaracteristicasPDV caracteristicasPDV = new CaracteristicasPDV();
		caracteristicasPDV.setBalcaoCentral(balcaoCentral);
		caracteristicasPDV.setPontoPrincipal(pontoPrincipal);
		caracteristicasPDV.setPossuiComputador(possuiComputador);
		caracteristicasPDV.setPossuiLuminoso(possuiLuminoso);
		caracteristicasPDV.setTextoLuminoso(textoLuminoso);
		
		return caracteristicasPDV;
	}
	
	public static LicencaMunicipal criarLicencaMunicipal(String nomeLicenca, String numeroLicenca, 
														 TipoLicencaMunicipal tipoLicencaMunicipal){
		
		LicencaMunicipal licencaMunicipal = new LicencaMunicipal();
		licencaMunicipal.setNomeLicenca(nomeLicenca);
		licencaMunicipal.setNumeroLicenca(numeroLicenca);
		licencaMunicipal.setTipoLicencaMunicipal(tipoLicencaMunicipal);
		
		return licencaMunicipal;
	}
	
	public static MaterialPromocional criarMaterialPromocional(Long codigo, String descricao){
		
		MaterialPromocional materialPromocional = new MaterialPromocional();
		materialPromocional.setCodigo(codigo);
		materialPromocional.setDescricao(descricao);
		
		return materialPromocional;
	}
	
	public static SegmentacaoPDV criarSegmentacaoPdv(AreaInfluenciaPDV areaInfluenciaPDV,   
													 TipoCaracteristicaSegmentacaoPDV tipoCaracteristica, 
													 TipoPontoPDV tipoPontoPDV, TipoClusterPDV tipoClusterPDV){
		
		SegmentacaoPDV segmentacaoPDV = new SegmentacaoPDV();
		segmentacaoPDV.setAreaInfluenciaPDV(areaInfluenciaPDV);
		segmentacaoPDV.setTipoCaracteristica(tipoCaracteristica);
		segmentacaoPDV.setTipoPontoPDV(tipoPontoPDV);
		segmentacaoPDV.setTipoClusterPDV(tipoClusterPDV);
		
		return segmentacaoPDV;
		
	}
	
	public static AreaInfluenciaPDV criarAreaInfluenciaPDV(Long codigo, String descricao){
		
		AreaInfluenciaPDV areaInfluenciaPDV = new AreaInfluenciaPDV();
		areaInfluenciaPDV.setCodigo(codigo);
		areaInfluenciaPDV.setDescricao(descricao);
		
		return areaInfluenciaPDV;
	}
	
	public static TipoPontoPDV criarTipoPontoPDV (Long codigo, String descricao){
		
		TipoPontoPDV tipoPdv = new TipoPontoPDV();
		tipoPdv.setCodigo(codigo);
		tipoPdv.setDescricao(descricao);
		
		return tipoPdv;
	}
	
	public static TipoClusterPDV criarTipoClusterPDV(Long codigo, String descricao){
		
		TipoClusterPDV tipoClusterPDV = new TipoClusterPDV();
		tipoClusterPDV.setCodigo(codigo);
		tipoClusterPDV.setDescricao(descricao);
		
		return tipoClusterPDV;
	}
	
	public static EnderecoPDV criarEnderecoPDV(Endereco endereco, PDV pdv, Boolean principal, TipoEndereco tipoEndereco){
		
		EnderecoPDV enderecoPDV = new EnderecoPDV();
		enderecoPDV.setEndereco(endereco);
		enderecoPDV.setPdv(pdv);
		enderecoPDV.setPrincipal(principal);
		enderecoPDV.setTipoEndereco(tipoEndereco);
		
		return enderecoPDV;
	}
	
	public static TelefonePDV criarTelefonePDV(Telefone telefone, PDV pdv, Boolean principal, TipoTelefone tipoTelefone ){
		
		TelefonePDV telefonePDV = new TelefonePDV();
		telefonePDV.setPdv(pdv);
		telefonePDV.setPrincipal(principal);
		telefonePDV.setTelefone(telefone);
		telefonePDV.setTipoTelefone(tipoTelefone);
		
		return telefonePDV;
	}

	public static TipoLicencaMunicipal criarTipoLicencaMunicipal(Long codigo, String descricao) {
		
		TipoLicencaMunicipal tipoLicencaMunicipal = new TipoLicencaMunicipal();
		tipoLicencaMunicipal.setCodigo(codigo);
		tipoLicencaMunicipal.setDescricao(descricao);
		
		return tipoLicencaMunicipal;
	}
	
	public static PeriodoFuncionamentoPDV gerarPeriodoFuncionamentoPDV(Date horarioFim, Date horarioInicio, 
																	   PDV pdv, TipoPeriodoFuncionamentoPDV 
																	   tipoPeriodoFuncionamentoPDV){
		
		PeriodoFuncionamentoPDV periodoFuncionamentoPDV = new PeriodoFuncionamentoPDV();
		
		periodoFuncionamentoPDV.setHorarioFim(horarioFim);
		periodoFuncionamentoPDV.setHorarioInicio(horarioInicio);
		periodoFuncionamentoPDV.setPdv(pdv);
		periodoFuncionamentoPDV.setTipoPeriodoFuncionamentoPDV(tipoPeriodoFuncionamentoPDV);
		
		return periodoFuncionamentoPDV;
		
	}
	
	public static TipoGeradorFluxoPDV criarTipoGeradorFluxoPDV(Long codigo, String descricao){
		
		TipoGeradorFluxoPDV tipoGeradorFluxoPDV  = new TipoGeradorFluxoPDV();
		tipoGeradorFluxoPDV.setCodigo(codigo);
		tipoGeradorFluxoPDV.setDescricao(descricao);
		
		return tipoGeradorFluxoPDV;
	}
	
	public static TipoEstabelecimentoAssociacaoPDV criarTipoEstabelecimentoAssociacaoPDV(Long codigo, String descricao){
		
		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV  = new TipoEstabelecimentoAssociacaoPDV();
		tipoEstabelecimentoAssociacaoPDV.setCodigo(codigo);
		tipoEstabelecimentoAssociacaoPDV.setDescricao(descricao);
		
		return tipoEstabelecimentoAssociacaoPDV;
	}
	
	public static ParametroContratoCota criarParametroContratoCota(String complemento, String condicoes, Integer diasAviso, Integer duracaoContrato){
		ParametroContratoCota parametroContratoCota = new ParametroContratoCota();
		parametroContratoCota.setComplementoContrato(complemento);
		parametroContratoCota.setCondicoesContratacao(condicoes);
		parametroContratoCota.setDiasAvisoRescisao(diasAviso);
		parametroContratoCota.setDuracaoContratoCota(duracaoContrato);
		return parametroContratoCota;
	}

	public static TipoEntrega criarTipoEntrega(Long id, DescricaoTipoEntrega descricaoTipoEntrega, Periodicidade periodicidade) {
		
		TipoEntrega tipoEntrega = new TipoEntrega();
		
		tipoEntrega.setId(id);
		tipoEntrega.setDescricaoTipoEntrega(descricaoTipoEntrega);
		tipoEntrega.setPeriodicidade(periodicidade);
		
		return tipoEntrega;
	}

	public static ParametroDistribuicaoCota criarParametroDistribuidor(Integer qtdePDV,
			String assistenteComercial, DescricaoTipoEntrega descricaoTipoEntrega,
			String observacao,
			Boolean repartePorPontoVenda, Boolean solicitaNumAtras,
			Boolean recebeRecolheParciais, Boolean notaEnvioImpresso,
			Boolean notaEnvioEmail, Boolean chamadaEncalheImpresso,
			Boolean chamadaEncalheEmail, Boolean slipImpresso, Boolean slipEmail) {
		
		ParametroDistribuicaoCota parametroDistribuicaoCota = new ParametroDistribuicaoCota();
		
		parametroDistribuicaoCota.setQtdePDV(qtdePDV);
		parametroDistribuicaoCota.setAssistenteComercial(assistenteComercial);
		parametroDistribuicaoCota.setDescricaoTipoEntrega(descricaoTipoEntrega);
		parametroDistribuicaoCota.setObservacao(observacao);
		parametroDistribuicaoCota.setRepartePorPontoVenda(repartePorPontoVenda);
		parametroDistribuicaoCota.setSolicitaNumAtras(solicitaNumAtras);
		parametroDistribuicaoCota.setRecebeRecolheParciais(recebeRecolheParciais);
		parametroDistribuicaoCota.setNotaEnvioImpresso(notaEnvioImpresso);
		parametroDistribuicaoCota.setNotaEnvioEmail(notaEnvioEmail);
		parametroDistribuicaoCota.setChamadaEncalheImpresso(chamadaEncalheImpresso);
		parametroDistribuicaoCota.setChamadaEncalheEmail(chamadaEncalheEmail);
		parametroDistribuicaoCota.setSlipImpresso(slipImpresso);
		parametroDistribuicaoCota.setSlipEmail(slipEmail);
		parametroDistribuicaoCota.setBoletoSlipImpresso(slipEmail);
		
		return parametroDistribuicaoCota;
	}
	
	public static TipoGarantiaAceita criarTipoGarantiaAceita(Distribuidor distribuidor, TipoGarantia tipoGarantia){
		TipoGarantiaAceita tipoGarantiaAceita = new TipoGarantiaAceita();
		
		tipoGarantiaAceita.setDistribuidor(distribuidor);
		tipoGarantiaAceita.setTipoGarantia(tipoGarantia);
		tipoGarantiaAceita.setValor(12);
		
		return tipoGarantiaAceita;
	}
	
	public static LancamentoParcial criarLancamentoParcial(ProdutoEdicao produtoEdicao, 
														   Date lancamentoInicial, 
														   Date recolhimentoFinal,
														   StatusLancamentoParcial status) {
		
		LancamentoParcial lancamentoParcial = new LancamentoParcial();
		
		lancamentoParcial.setProdutoEdicao(produtoEdicao);
		lancamentoParcial.setLancamentoInicial(lancamentoInicial);
		lancamentoParcial.setRecolhimentoFinal(recolhimentoFinal);
		lancamentoParcial.setStatus(status);
		
		return lancamentoParcial;
	}
	
	public static PeriodoLancamentoParcial criarPeriodoLancamentoParcial(Lancamento lancamento, 
																		 LancamentoParcial lancamentoParcial,
																		 StatusLancamentoParcial status,
																		 TipoLancamentoParcial tipo) {
		
		PeriodoLancamentoParcial parcial = new PeriodoLancamentoParcial();
		
		parcial.setLancamento(lancamento);
		
		parcial.setLancamentoParcial(lancamentoParcial);
		
		parcial.setStatus(status);
		
		parcial.setTipo(tipo);
		
		return parcial;
	}
	
	public static Roteirizacao criarRoteirizacao(Box box ){
		
		Roteirizacao roteirizacao = new Roteirizacao();
		roteirizacao.setBox(box);

		return roteirizacao;
	}
	
	public static Roteiro criarRoteiro(String descricaoRoteiro, Roteirizacao roteirizacao, TipoRoteiro tipoRoteiro ){
		
		Roteiro roteiro = new Roteiro();
		roteiro.setDescricaoRoteiro(descricaoRoteiro);
		roteiro.setOrdem(ORDEM_ROTEIRO.getAndIncrement());
		roteiro.setTipoRoteiro(tipoRoteiro);
		roteiro.setRoteirizacao(roteirizacao);
		roteirizacao.addRoteiro(roteiro);
		
		return roteiro;
	}
	
	public static  PDV criarPDVPrincipal(String nome, Cota cota){
		PDV pdv = new PDV();
		pdv.setNome(nome);
		pdv.setCota(cota);
		pdv.setCaracteristicas(new CaracteristicasPDV());
		pdv.getCaracteristicas().setPontoPrincipal(true);
		return pdv;
	}

	public static Algoritmo criarAlgoritmo(String descricao) {
		Algoritmo algoritmo = new Algoritmo();
		
		algoritmo.setDescricao(descricao);
		
		return algoritmo;
	}
	
	public static InterfaceExecucao criarInterfaceExecucao(Long id, String nome) {
		
		InterfaceExecucao ie = new InterfaceExecucao();
		ie.setId(id);
		ie.setNome(nome);
		
		return ie;
	}
	
	public static EventoExecucao criarEventoExecucao(Long id, String nome, String descricao) {
		
		EventoExecucao ee = new EventoExecucao();
		ee.setId(id);
		ee.setNome(nome);
		ee.setDescricao(descricao);
		
		return ee;
	}
	
	public static UnidadeFederacao criarUnidadeFederacao(String sigla) {
		
		UnidadeFederacao uf = new UnidadeFederacao();
		
		uf.setSigla(sigla);
		
		return uf;
	}
	
	public static Localidade criarLocalidade(Long id, String nome, 
											 Long codigoMunicipioIBGE, 
											 UnidadeFederacao unidadeFederacao) {
		
		Localidade localidade = new Localidade();
		
		localidade.setId(id);
		localidade.setNome(nome);
		localidade.setUnidadeFederacao(unidadeFederacao);
		localidade.setCodigoMunicipioIBGE(codigoMunicipioIBGE);
		
		return localidade;
	}
	
	public static Bairro criarBairro(Long id, String nome, Localidade localidade) {
		
		Bairro bairro = new Bairro();
		
		bairro.setId(id);
		bairro.setNome(nome);
		bairro.setLocalidade(localidade);
		
		return bairro;
	}
	
	public static Logradouro criarLogradouro(Long id, String nome, 
											 String cep, Long chaveBairroInicial,
											 Localidade localidade, String tipoLogradouro) {
		
		Logradouro logradouro = new Logradouro();
		
		logradouro.setCep(cep);
		logradouro.setChaveBairroInicial(chaveBairroInicial);
		logradouro.setId(id);
		logradouro.setLocalidade(localidade);
		logradouro.setNome(nome);
		logradouro.setTipoLogradouro(tipoLogradouro);
		
		return logradouro;
	}
	
	public static FuroProduto furoProduto(Date data, Lancamento lancamento,
										  ProdutoEdicao produtoEdicao, Usuario usuario) {
		
		FuroProduto furoProduto = new FuroProduto();
		
		furoProduto.setData(data);
		furoProduto.setLancamento(lancamento);
		furoProduto.setProdutoEdicao(produtoEdicao);
		furoProduto.setUsuario(usuario);
		
		return furoProduto;
	}
	
	
	public static ValoresTotaisISSQN valoresTotaisISSQN(
			Long id,
			BigDecimal valorBaseCalculo,
			BigDecimal valorCOFINS,
			BigDecimal valorISS,
			BigDecimal valorPIS,
			BigDecimal valorServicos) {
		
		ValoresTotaisISSQN valoresTotaisISSQN = new ValoresTotaisISSQN();
		
		valoresTotaisISSQN.setId(id);
		valoresTotaisISSQN.setValorBaseCalculo(valorBaseCalculo);
		valoresTotaisISSQN.setValorCOFINS(valorCOFINS);
		valoresTotaisISSQN.setValorISS(valorISS);
		valoresTotaisISSQN.setValorPIS(valorPIS);
		valoresTotaisISSQN.setValorServicos(valorServicos);
		
		return valoresTotaisISSQN;
	}
	
	public static ProdutoServico produtoServico(
			Integer sequencia,
			Integer cfop,
			Long codigoBarras,
			String codigoProduto,
			String descricaoProduto,
			EncargoFinanceiro encargoFinanceiro,
			Long extipi,
			Long ncm,
			NotaFiscal notaFiscal,
			ProdutoEdicao produtoEdicao,
			BigInteger quantidade,
			String unidade,
			BigDecimal valorDesconto,
			BigDecimal valorFrete,
			BigDecimal valorOutros,
			BigDecimal valorSeguro,
			BigDecimal valorTotalBruto,
			BigDecimal valorUnitario) {
		
		ProdutoServicoPK produtoServicoPK = new ProdutoServicoPK();
		produtoServicoPK.setNotaFiscal(notaFiscal);
		produtoServicoPK.setSequencia(sequencia);

		ProdutoServico produtoServico = new ProdutoServico();
		
		produtoServico.setProdutoServicoPK(produtoServicoPK);
		produtoServico.setCfop(cfop);
		produtoServico.setCodigoBarras(codigoBarras);
		produtoServico.setCodigoProduto(codigoProduto);
		produtoServico.setDescricaoProduto(descricaoProduto);
		produtoServico.setEncargoFinanceiro(encargoFinanceiro);
		produtoServico.setExtipi(extipi);
		produtoServico.setNcm(ncm);
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);
		produtoServico.setUnidade(unidade);
		produtoServico.setValorDesconto(valorDesconto);
		produtoServico.setValorFrete(valorFrete);
		produtoServico.setValorOutros(valorOutros);
		produtoServico.setValorSeguro(valorSeguro);
		produtoServico.setValorTotalBruto(valorTotalBruto);
		produtoServico.setValorUnitario(valorUnitario);
		
		return produtoServico;
		
	}
	
	public static ValoresRetencoesTributos valoresRetencoesTributos(
			Long id,
			BigDecimal valorBaseCalculoIRRF,
			BigDecimal valorBaseCalculoPrevidencia,
			BigDecimal valorRetidoCOFINS,
			BigDecimal valorRetidoCSLL,
			BigDecimal valorRetidoIRRF,
			BigDecimal valorRetidoPIS,
			BigDecimal valorRetidoPrevidencia) {
		
		ValoresRetencoesTributos valoresRetencoesTributos = new ValoresRetencoesTributos();
		
		valoresRetencoesTributos.setId(id);
		valoresRetencoesTributos.setValorBaseCalculoIRRF(valorBaseCalculoIRRF);
		valoresRetencoesTributos.setValorBaseCalculoPrevidencia(valorBaseCalculoPrevidencia);
		valoresRetencoesTributos.setValorRetidoCOFINS(valorRetidoCOFINS);
		valoresRetencoesTributos.setValorRetidoCSLL(valorRetidoCSLL);
		valoresRetencoesTributos.setValorRetidoIRRF(valorRetidoIRRF);
		valoresRetencoesTributos.setValorRetidoPIS(valorRetidoPIS);
		valoresRetencoesTributos.setValorRetidoPrevidencia(valorRetidoPrevidencia);
		
		return valoresRetencoesTributos;
		
		
	}
	
	public static InformacaoValoresTotais informacaoValoresTotais(
			ValoresRetencoesTributos valoresRetencoesTributos,
			ValoresTotaisISSQN valoresTotaisISSQN,
			BigDecimal valorBaseCalculoICMS,
			BigDecimal valorBaseCalculoICMSST,
			BigDecimal valorCOFINS,
			BigDecimal valorDesconto,
			BigDecimal valorFrete,
			BigDecimal valorICMS,
			BigDecimal valorICMSST,
			BigDecimal valorIPI,
			BigDecimal valorNotaFiscal,
			BigDecimal valorOutro,
			BigDecimal valorPIS,
			BigDecimal valorProdutos,
			BigDecimal valorSeguro){
		
		InformacaoValoresTotais informacaoValoresTotais = new InformacaoValoresTotais();
		
		informacaoValoresTotais.setRetencoesTributos(valoresRetencoesTributos);
		informacaoValoresTotais.setTotaisISSQN(valoresTotaisISSQN);
		informacaoValoresTotais.setValorBaseCalculoICMS(valorBaseCalculoICMS);
		informacaoValoresTotais.setValorBaseCalculoICMSST(valorBaseCalculoICMSST);
		informacaoValoresTotais.setValorCOFINS(valorCOFINS);
		informacaoValoresTotais.setValorDesconto(valorDesconto);
		informacaoValoresTotais.setValorFrete(valorFrete);
		informacaoValoresTotais.setValorICMS(valorICMS);
		informacaoValoresTotais.setValorICMSST(valorICMSST);
		informacaoValoresTotais.setValorIPI(valorIPI);
		informacaoValoresTotais.setValorNotaFiscal(valorNotaFiscal);
		informacaoValoresTotais.setValorOutro(valorOutro);
		informacaoValoresTotais.setValorPIS(valorPIS);
		informacaoValoresTotais.setValorProdutos(valorProdutos);
		informacaoValoresTotais.setValorSeguro(valorSeguro);
		
		return informacaoValoresTotais;
		
		
	}
	

	public static InformacaoTransporte informacaoTransporte(
			String documento,
			Endereco endereco,
			String inscricaoEstadual,
			Integer modalidadeFrente,
			String municipio,
			String nome,
			RetencaoICMSTransporte retencaoICMS,
			String uf,
			Veiculo veiculo) {
		
		InformacaoTransporte informacaoTransporte = new InformacaoTransporte();
		
		informacaoTransporte.setDocumento(documento);
		informacaoTransporte.setInscricaoEstadual(inscricaoEstadual);
		informacaoTransporte.setModalidadeFrente(modalidadeFrente);
		informacaoTransporte.setMunicipio(municipio);
		informacaoTransporte.setNome(nome);
		informacaoTransporte.setRetencaoICMS(retencaoICMS);
		informacaoTransporte.setUf(uf);
		informacaoTransporte.setVeiculo(veiculo);
		informacaoTransporte.setEndereco(endereco);
		
		return informacaoTransporte;
		
	}
	
	public static RetornoComunicacaoEletronica retornoComunicacaoEletronica(
			Date dataRecebimento,
			String motivo,
			Long protocolo,
			Status status) {
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = 
				new RetornoComunicacaoEletronica();
		
		retornoComunicacaoEletronica.setDataRecebimento(dataRecebimento);
		retornoComunicacaoEletronica.setMotivo(motivo);
		retornoComunicacaoEletronica.setProtocolo(protocolo);
		retornoComunicacaoEletronica.setStatus(status);
		
		return retornoComunicacaoEletronica;
		
	}
	
	public static InformacaoEletronica informacaoEletronica(
			String chaveAcesso,
			RetornoComunicacaoEletronica retornoComunicacaoEletronica) {
		
		InformacaoEletronica informacaoEletronica = new InformacaoEletronica();
		
		informacaoEletronica.setChaveAcesso(chaveAcesso);
		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);
		
		return informacaoEletronica;
		
	}
	
	public static InformacaoAdicional informacaoAdicional(String informacoesComplementares) {
		
		InformacaoAdicional informacaoAdicional = new InformacaoAdicional();
		
		informacaoAdicional.setInformacoesComplementares(informacoesComplementares);
		
		return informacaoAdicional;
		
	}
	
	public static IdentificacaoEmitente identificacaoEmitente(
			String cnae,
			String documento,
			Endereco endereco,
			String inscricaoEstual,
			String inscricaoEstualSubstituto,
			String inscricaoMunicipal,
			String nome,
			String nomeFantasia,
			Pessoa pessoaEmitenteReferencia,
			RegimeTributario regimeTributario,
			Telefone telefone) {
		
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();
		
		identificacaoEmitente.setCnae(cnae);
		identificacaoEmitente.setDocumento(documento);
		identificacaoEmitente.setEndereco(endereco);
		identificacaoEmitente.setInscricaoEstual(inscricaoEstual);
		identificacaoEmitente.setInscricaoEstualSubstituto(inscricaoEstualSubstituto);
		identificacaoEmitente.setInscricaoMunicipal(inscricaoMunicipal);
		identificacaoEmitente.setNome(nome);
		identificacaoEmitente.setNomeFantasia(nomeFantasia);
		identificacaoEmitente.setPessoaEmitenteReferencia(pessoaEmitenteReferencia);
		identificacaoEmitente.setRegimeTributario(regimeTributario);
		identificacaoEmitente.setTelefone(telefone);
		
		return identificacaoEmitente;
		
	}
	
	
	public static IdentificacaoDestinatario identificacaoDestinatario(
			String documento,
			String email,
			Endereco endereco,
			String inscricaoEstadual,
			String inscricaoSuframa,
			String nome,
			String nomeFantasia,
			Pessoa pessoaDestinatarioReferencia,
			Telefone telefone) {
		
		IdentificacaoDestinatario identificacaoDestinatario = new IdentificacaoDestinatario();
		
		identificacaoDestinatario.setDocumento(documento);
		identificacaoDestinatario.setEmail(email);
		identificacaoDestinatario.setEndereco(endereco);
		identificacaoDestinatario.setInscricaoEstual(inscricaoEstadual);
		identificacaoDestinatario.setInscricaoSuframa(inscricaoSuframa);
		identificacaoDestinatario.setNome(nome);
		identificacaoDestinatario.setNomeFantasia(nomeFantasia);
		identificacaoDestinatario.setPessoaDestinatarioReferencia(pessoaDestinatarioReferencia);
		identificacaoDestinatario.setTelefone(telefone);
		
		return identificacaoDestinatario;
		
	}
	
	public static Identificacao identificacao(
			Date dataEmissao, 
			Date dataEntradaContigencia,
			Date dataSaidaEntrada,
			String descricaoNaturezaOperacao,
			Integer digitoVerificadorChaveAcesso,
			FormaPagamento formaPagamento,
			Date horaSaidaEntrada,
			String justificativaEntradaContigencia,
			List<NotaFiscalReferenciada> listReferenciadas,
			Long numeroDocumentoFiscal,
			Integer serie,
			TipoOperacao tipoOperacao) {
		
		Identificacao identificacao = new Identificacao();
		
		identificacao.setDataEmissao(dataEmissao);
		identificacao.setDataEntradaContigencia(dataEntradaContigencia);
		identificacao.setDataSaidaEntrada(dataSaidaEntrada);
		identificacao.setDescricaoNaturezaOperacao(descricaoNaturezaOperacao);
		identificacao.setFormaPagamento(formaPagamento);
		identificacao.setHoraSaidaEntrada(horaSaidaEntrada);
		identificacao.setJustificativaEntradaContigencia(justificativaEntradaContigencia);
		identificacao.setListReferenciadas(listReferenciadas);
		identificacao.setNumeroDocumentoFiscal(numeroDocumentoFiscal);
		identificacao.setSerie(serie);
		identificacao.setTipoOperacao(tipoOperacao);
		
		return identificacao;
		
	}
	
	public static DescontoDistribuidor descontoDistribuidor(BigDecimal desconto, Distribuidor distribuidor, Set<Fornecedor> fornecedores,Usuario usuario, Date dataAtualizacao ){
		
		DescontoDistribuidor descontoReturn = new DescontoDistribuidor();
		descontoReturn.setDesconto(desconto);
		descontoReturn.setDataAlteracao(dataAtualizacao);
		descontoReturn.setDistribuidor(distribuidor);
		descontoReturn.setFornecedores(fornecedores);
		descontoReturn.setUsuario(usuario);
		
		return descontoReturn;
	}
	
    public static DescontoCota descontoCota(BigDecimal desconto, Distribuidor distribuidor, Cota cota, Set<Fornecedor> fornecedores,Usuario usuario, Date dataAtualizacao ){
		
		DescontoCota descontoReturn = new DescontoCota();
		descontoReturn.setDesconto(desconto);
		descontoReturn.setDataAlteracao(dataAtualizacao);
		descontoReturn.setCota(cota);
		descontoReturn.setDistribuidor(distribuidor);
		descontoReturn.setFornecedores(fornecedores);
		descontoReturn.setUsuario(usuario);
		
		return descontoReturn;
	}

	public static GrupoCota criarGrupoCota(Long id, String nome, TipoGrupo tipoGrupo,
			Set<DiaSemana> diasRecolhimento,
			TipoCaracteristicaSegmentacaoPDV tipoCota,
			Set<Localidade> municipios, Set<Cota> cotas) {
		
		GrupoCota grupo = new GrupoCota();
		
		grupo.setCotas(cotas);
		grupo.setDiasRecolhimento(diasRecolhimento);
		grupo.setId(id);
		grupo.setMunicipios(municipios);
		grupo.setNome(nome);
		grupo.setTipoCota(tipoCota);
		grupo.setTipoGrupo(tipoGrupo);
		
		return grupo;
	}
	
	public static DescontoProdutoEdicao descontoProdutoEdicao(Cota cota, BigDecimal desconto, Fornecedor fornecedor, ProdutoEdicao produtoEdicao, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto){
		
		DescontoProdutoEdicao descontoP = new DescontoProdutoEdicao();
		descontoP.setCota(cota);
		descontoP.setDesconto(desconto);
		descontoP.setFornecedor(fornecedor);
		descontoP.setProdutoEdicao(produtoEdicao);
		descontoP.setTipoDesconto(tipoDesconto);
		
		return descontoP;
	}
	
    public static DescontoLogistica descontoLogistica(Date dataInicioVigencia, Float percentualDesconto, Float percentualPrestacaoServico, Integer tipoDesconto, String descricao){
		
    	DescontoLogistica descontoL = new DescontoLogistica();
		descontoL.setDataInicioVigencia(dataInicioVigencia);
		descontoL.setPercentualDesconto(percentualDesconto);
		descontoL.setPercentualPrestacaoServico(percentualPrestacaoServico);
		descontoL.setTipoDesconto(tipoDesconto);	
		descontoL.setDescricao(descricao);
		
		return descontoL;
	}
    
    public static DescontoLogistica descontoLogistica(Date dataInicioVigencia, Float percentualDesconto, Float percentualPrestacaoServico, Integer tipoDesconto, Set<Produto> produtos){
		
    	DescontoLogistica descontoL = new DescontoLogistica();
		descontoL.setDataInicioVigencia(dataInicioVigencia);
		descontoL.setPercentualDesconto(percentualDesconto);
		descontoL.setPercentualPrestacaoServico(percentualPrestacaoServico);
		descontoL.setTipoDesconto(tipoDesconto);
		descontoL.setProdutos(produtos);
		
		return descontoL;
	}
    
    public static HistoricoTitularidadeCota historicoTitularidade(Cota cota) {
        HistoricoTitularidadeCota historico = new HistoricoTitularidadeCota();
        historico.setCota(cota);
        historico.setDataInclusao(cota.getInicioAtividade());
        historico.setBox(cota.getBox().getNome());
        
        ParametrosCotaNotaFiscalEletronica paramNFE = cota.getParametrosCotaNotaFiscalEletronica();
        if(paramNFE != null) {
            historico.setEmiteNfe(paramNFE.getEmiteNotaFiscalEletronica());
            historico.setEmailNfe(paramNFE.getEmailNotaFiscalEletronica());
        }

        historico.setInicio(cota.getInicioAtividade());
        historico.setFim(DateUtil.adicionarDias(cota.getInicioAtividade(), 60));
        historico.setEmail("aristoteles@mail.com");
        historico.setNumeroCota(cota.getNumeroCota());
        historico.setClassificacaoExpectativaFaturamento(cota.getClassificacaoEspectativaFaturamento());
        historico.setSituacaoCadastro(cota.getSituacaoCadastro());
        
        historico.setInicioPeriodoCotaBase(cota.getInicioAtividade());
        historico.setFimPeriodoCotaBase(DateUtil.adicionarDias(cota.getInicioAtividade(), 60));
        historico.addCotaReferencia(new HistoricoTitularidadeCotaReferenciaCota(Integer.valueOf(123), BigDecimal.valueOf(33)));
        historico.addCotaReferencia(new HistoricoTitularidadeCotaReferenciaCota(Integer.valueOf(456), BigDecimal.valueOf(33)));
        historico.addCotaReferencia(new HistoricoTitularidadeCotaReferenciaCota(Integer.valueOf(789), BigDecimal.valueOf(33)));

        HistoricoTitularidadeCotaPessoaFisica htcpf = new HistoricoTitularidadeCotaPessoaFisica(
                    "Aristoteles da Silva", "862.243.913-51", "30.887.357-9",
                    "SSP", "SP", criarData(10, Calendar.APRIL, 1974), EstadoCivil.SOLTEIRO, Sexo.MASCULINO,
                    "Brasileira", "Mococa", "Ari");
        historico.setPessoaFisica(htcpf);
        
       
        
        HistoricoTitularidadeCotaEndereco endereco1 = new HistoricoTitularidadeCotaEndereco(
                10, "Centro", "13720-000", 150, "São José do Rio Pardo", null,
                "Rua", "Treze de Maio", "13", "SP", 15, TipoEndereco.COMERCIAL,
                true);
        historico.addEndereco(endereco1);
        
        HistoricoTitularidadeCotaEndereco endereco2 = new HistoricoTitularidadeCotaEndereco(
                10, "Centro", "13720-000", 150, "São José do Rio Pardo", null,
                "Rua", "Nove de Julho", "100", "SP", 15, TipoEndereco.RESIDENCIAL,
                false);
        historico.addEndereco(endereco2);
        
        HistoricoTitularidadeCotaTelefone telefone1 = new HistoricoTitularidadeCotaTelefone(
                "3681-6669-", null, "19", TipoTelefone.COMERCIAL, true);
        historico.addTelefone(telefone1);
        
        HistoricoTitularidadeCotaTelefone telefone2 = new HistoricoTitularidadeCotaTelefone(
                "9899-4321-", null, "19", TipoTelefone.CELULAR, false);
        historico.addTelefone(telefone2);
        
        HistoricoTitularidadeCotaFornecedor fornecedor1 = new HistoricoTitularidadeCotaFornecedor();
        fornecedor1.setPessoaJuridica(new HistoricoTitularidadeCotaPessoaJuridica("Acme Inc", "Acme", "64.138.131/0001-12", null, null));
        historico.addFornecedor(fornecedor1);
        
        HistoricoTitularidadeCotaFornecedor fornecedor2 = new HistoricoTitularidadeCotaFornecedor();
        fornecedor2.setPessoaJuridica(new HistoricoTitularidadeCotaPessoaJuridica("Massive Dynamic", "Massive Dynamic", "44.864.479/0001-80", null, null));
        historico.addFornecedor(fornecedor2);
        
        HistoricoTitularidadeCotaDescontoCota descontoCota1 = new HistoricoTitularidadeCotaDescontoCota(
                TipoDesconto.ESPECIFICO, "Acme", new Date(),
                BigDecimal.valueOf(10));
        HistoricoTitularidadeCotaDescontoCota descontoCota2 = new HistoricoTitularidadeCotaDescontoCota(
                TipoDesconto.ESPECIFICO, "Massive Dinamic", new Date(),
                BigDecimal.valueOf(5));
        historico.addDesconto(descontoCota1);
        historico.addDesconto(descontoCota2);
        
        HistoricoTitularidadeCotaDescontoProduto descontoProduto1 = new HistoricoTitularidadeCotaDescontoProduto(
                "123", "Quatro Rodas", Long.valueOf(45), new Date(),
                BigDecimal.valueOf(10));
        
        HistoricoTitularidadeCotaDescontoProduto descontoProduto2 = new HistoricoTitularidadeCotaDescontoProduto(
                "456", "Super Interessante", Long.valueOf(46), new Date(), BigDecimal.valueOf(5));
        historico.addDesconto(descontoProduto1);
        historico.addDesconto(descontoProduto2);
 
        HistoricoTitularidadeCotaPDV pdv = new HistoricoTitularidadeCotaPDV();
        pdv.setStatus(StatusPDV.ATIVO);
        pdv.setDataInclusao(cota.getInicioAtividade());
        pdv.setNome("Super PDV");
        pdv.setContato("Aristóteles Da Silva");
        pdv.setSite("www.superpdvdoaristoteles.com");
        pdv.setEmail("emaildosuperpdvdoaristotles@localhost");
        pdv.setPontoReferencia("Logo ali");
        pdv.setDentroOutroEstabelecimento(true);
        
        pdv.setTipoEstabelecimentoPDV(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(10), "Supermercado"));
        
        CaracteristicasPDV caracteristicas = new CaracteristicasPDV();
        caracteristicas.setPontoPrincipal(true);
        caracteristicas.setPossuiComputador(true);
        caracteristicas.setPossuiLuminoso(true);
        caracteristicas.setTextoLuminoso("Super PDV do Aristóteles");
        pdv.setCaracteristicas(caracteristicas);
        pdv.setTamanhoPDV(TamanhoPDV.G);
        pdv.setQtdeFuncionarios(2);
        pdv.setPossuiSistemaIPV(true);
        pdv.setPorcentagemFaturamento(BigDecimal.valueOf(100));
        
        HistoricoTitularidadeCotaCodigoDescricao tipoLicenca = new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(10), "Alvará");
        pdv.setTipoLicencaMunicipal(tipoLicenca);
        pdv.setNumeroLicencaMunicipal("100");
        pdv.setNomeLicencaMunicipal("Alvará de Funcionamento");
        
        HistoricoTitularidadeCotaFuncionamentoPDV periodo = new HistoricoTitularidadeCotaFuncionamentoPDV(
                TipoPeriodoFuncionamentoPDV.DIARIA, DateUtil.parseData("08:00",
                        DateUtil.PADRAO_HORA_MINUTO), DateUtil.parseData(
                        "17:00", DateUtil.PADRAO_HORA_MINUTO));
        pdv.addPeriodoFuncionamento(periodo);
        
        HistoricoTitularidadeCotaCodigoDescricao tipoPonto = new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(10), "Comercial");
        pdv.setTipoPonto(tipoPonto);
        
        HistoricoTitularidadeCotaEndereco enderecoPDV = new HistoricoTitularidadeCotaEndereco(
                10, "Centro", "13720-000", 99, "São José do Rio Pardo", null,
                "Rua", "Benjamin Constant", "50", "SP", 15, TipoEndereco.COMERCIAL,
                true);
        pdv.addEndereco(enderecoPDV);
        
        HistoricoTitularidadeCotaTelefone telefonePDV = new HistoricoTitularidadeCotaTelefone(
                "3681-1234", null, "19", TipoTelefone.COMERCIAL, true);
        pdv.addTelefone(telefonePDV);
        
        pdv.setAreaInfluencia(new HistoricoTitularidadeCotaCodigoDescricao(Long.valueOf(10), "Residencial"));
        pdv.setTipoCaracteristica(TipoCaracteristicaSegmentacaoPDV.CONVENCIONAL);
        
        pdv.setGeradorFluxoPrincipal(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(1), "Cursinho"));
        pdv.addGeradorFluxoSecundario(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(2), "Padarias")); 
        pdv.addGeradorFluxoSecundario(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(3), "Restaurantes")); 
        
        pdv.addMaterialPromocional(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(1), "Adesivo")); 
        
        pdv.addMaterialPromocional(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(2), "Bandeirola")); 
        
        pdv.addMaterialPromocional(new HistoricoTitularidadeCotaCodigoDescricao(
                Long.valueOf(3), "Poster")); 
        
        pdv.setExpositor(true);
        pdv.setTipoExpositor("Tipo Expositor");
        
        try {
            URL urlImagem = Thread.currentThread().getContextClassLoader().getResource("bancaJornal.jpg");
            File fileImagem = new File(urlImagem.toURI());
            byte[] imagem = FileUtils.readFileToByteArray(fileImagem);
            pdv.setImagem(imagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro definindo imagem histórico titularidade PDV", e);
        }
        
        historico.addPdv(pdv);
        
        HistoricoTitularidadeCotaFinanceiro financeiro = new HistoricoTitularidadeCotaFinanceiro();
        financeiro.setPossuiContrato(true);
        financeiro.setDataInicioContrato(cota.getInicioAtividade());
        financeiro.setDataTerminoContrato(DateUtil.adicionarDias(cota.getInicioAtividade(), 180));
        financeiro.setContratoRecebido(true);
        financeiro.setFatorVencimento(2);
        financeiro.setTipoCota(TipoCota.CONSIGNADO);
        financeiro.setValorMininoCobranca(BigDecimal.valueOf(500));
        PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
        politicaSuspensao.setValor(BigDecimal.valueOf(1000));
        politicaSuspensao.setNumeroAcumuloDivida(3);
        financeiro.setPoliticaSuspensao(politicaSuspensao);
        
        HistoricoTitularidadeCotaFormaPagamento formaPagto1 = new HistoricoTitularidadeCotaFormaPagamento();
        formaPagto1.setBanco(new HistoricoTitularidadeCotaBanco("349", "Banco Itau", Long.valueOf(809), null, Long.valueOf(123456), "7"));
        formaPagto1.setTipoCobranca(TipoCobranca.BOLETO);
        
        HistoricoTitularidadeCotaConcentracaoCobranca concentracao1 = new HistoricoTitularidadeCotaConcentracaoCobranca();
        concentracao1.setTipoFormaCobranca(TipoFormaCobranca.SEMANAL);
        concentracao1.addDiaSemana(DiaSemana.SEGUNDA_FEIRA);
        concentracao1.addDiaSemana(DiaSemana.QUARTA_FEIRA);
        concentracao1.addDiaSemana(DiaSemana.SEXTA_FEIRA);
        formaPagto1.setConcentracaoCobranca(concentracao1);
        
        
        HistoricoTitularidadeCotaFornecedor fornecedorPagto1 = new HistoricoTitularidadeCotaFornecedor();
        fornecedorPagto1.setPessoaJuridica(new HistoricoTitularidadeCotaPessoaJuridica("Acme Inc", "Acme", "64.138.131/0001-12", null, null));
        formaPagto1.addFornecedor(fornecedorPagto1);
        
        HistoricoTitularidadeCotaFornecedor fornecedorPagto2 = new HistoricoTitularidadeCotaFornecedor();
        fornecedorPagto2.setPessoaJuridica(new HistoricoTitularidadeCotaPessoaJuridica("Massive Dynamic", "Massive Dynamic", "44.864.479/0001-80", null, null));
        formaPagto1.addFornecedor(fornecedorPagto2);
        financeiro.addFormaPagamento(formaPagto1);
        
        HistoricoTitularidadeCotaFormaPagamento formaPagto2 = new HistoricoTitularidadeCotaFormaPagamento();
        formaPagto2.setTipoCobranca(TipoCobranca.DINHEIRO);
        
        HistoricoTitularidadeCotaConcentracaoCobranca concentracao2 = new HistoricoTitularidadeCotaConcentracaoCobranca();
        concentracao2.setTipoFormaCobranca(TipoFormaCobranca.QUINZENAL);
        concentracao2.addDiaMes(10);
        concentracao2.addDiaMes(20);
        formaPagto2.setConcentracaoCobranca(concentracao2);
        
        HistoricoTitularidadeCotaFornecedor fornecedorPagto3 = new HistoricoTitularidadeCotaFornecedor();
        fornecedorPagto3.setPessoaJuridica(new HistoricoTitularidadeCotaPessoaJuridica("XYZ Inc", "XYZ", "64.138.131/0001-12", null, null));
        formaPagto2.addFornecedor(fornecedorPagto3);
        
        financeiro.addFormaPagamento(formaPagto2);
        historico.setFinanceiro(financeiro);
        
        HistoricoTitularidadeCotaOutros outros1 = new HistoricoTitularidadeCotaOutros();
        outros1.setDescricao("Carro");
        outros1.setValor(BigDecimal.valueOf(5000));
        outros1.setValidade(DateUtil.adicionarDias(cota.getInicioAtividade(), 180));
        historico.addGarantia(outros1);
        
        HistoricoTitularidadeCotaOutros outros2 = new HistoricoTitularidadeCotaOutros();
        outros2.setDescricao("Notebook");
        outros2.setValor(BigDecimal.valueOf(1000));
        outros2.setValidade(DateUtil.adicionarDias(cota.getInicioAtividade(), 180));
        historico.addGarantia(outros2);
        
        HistoricoTitularidadeCotaDistribuicao distribuicao = new HistoricoTitularidadeCotaDistribuicao();
        distribuicao.setQtdePDV(1);
        distribuicao.setAssistenteComercial("John Doe");
        distribuicao.setGerenteComercial("Jane Doe");
        distribuicao.setObservacao("Observação");
        distribuicao.setEntregaReparteVenda(true);
        distribuicao.setSolicitaNumAtrasados(true);
        distribuicao.setRecebeRecolheParcias(true);
        distribuicao.setTipoEntrega(DescricaoTipoEntrega.ENTREGA_EM_BANCA);
        distribuicao.setPercentualFaturamentoEntrega(BigDecimal.valueOf(10));
        distribuicao.setInicioPeriodoCarencia(cota.getInicioAtividade());
        distribuicao.setFimPeriodoCarencia(DateUtil.adicionarDias(cota.getInicioAtividade(), 60));
        distribuicao.setSlipEmail(true);
        distribuicao.setSlipImpresso(true);
        distribuicao.setBoletoEmail(true);
        distribuicao.setBoletoImpresso(true);
        distribuicao.setBoletoSlipEmail(true);
        distribuicao.setBoletoSlipImpresso(true);
        distribuicao.setReciboEmail(true);
        distribuicao.setReciboImpresso(true);
        distribuicao.setNotaEnvioEmail(true);
        distribuicao.setNotaEnvioImpresso(true);
        distribuicao.setChamadaEncalheEmail(true);
        distribuicao.setChamadaEncalheImpresso(true);
        historico.setDistribuicao(distribuicao);
    
        cota.addTitularCota(historico);
        return historico;
    }
    
    public static RateioDiferenca criarRateioDiferenca(Cota cota, Date dataNotaEnvio, BigInteger qtde, 
    		EstudoCota estudoCota, Diferenca diferenca) {
    	
    	RateioDiferenca rateio = new RateioDiferenca();
    	
    	rateio.setCota(cota);
    	rateio.setDataNotaEnvio(dataNotaEnvio);
    	rateio.setQtde(qtde);
    	rateio.setEstudoCota(estudoCota);
    	rateio.setDiferenca(diferenca);
    	
    	return rateio;
    }

    
    public static BaixaAutomatica baixaAutomatica(Cobranca cobranca, Date dataBaixa,
    									   		  String nomeArquivo, String nossoNumero,
    									   		  Integer numeroRegistroArquivo, StatusBaixa status,
    									   		  BigDecimal valorPago, Banco banco) {
    	
    	BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
    	
    	baixaAutomatica.setCobranca(cobranca);
    	baixaAutomatica.setDataBaixa(dataBaixa);
    	baixaAutomatica.setNomeArquivo(nomeArquivo);
    	baixaAutomatica.setNossoNumero(nossoNumero);
    	baixaAutomatica.setNumeroRegistroArquivo(numeroRegistroArquivo);
    	baixaAutomatica.setStatus(status);
    	baixaAutomatica.setValorPago(valorPago);
    	baixaAutomatica.setBanco(banco);
    	
    	return baixaAutomatica;
    }
    
    
    public static BaixaManual baixaManual(Cobranca cobranca, Date dataBaixa, 
    									  StatusBaixa status, BigDecimal valorPago, 
    									  Banco banco, StatusAprovacao statusAprovacao) {

		BaixaManual baixaManual= new BaixaManual();
		
		baixaManual.setCobranca(cobranca);
		baixaManual.setDataBaixa(dataBaixa);
		baixaManual.setStatus(status);
		baixaManual.setValorPago(valorPago);
		baixaManual.setBanco(banco);
		baixaManual.setStatusAprovacao(statusAprovacao);
	
	return baixaManual;
	}



	public static TipoMovimentoEstoque tipoMovimentoCompraEncalhe() {
		
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Compra Encalhe");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_ENCALHE);
		
		return tipoMovimento;
	}

	public static TipoMovimentoEstoque tipoMovimentoEstornoCompraEncalhe() {
		
		TipoMovimentoEstoque tipoMovimento = new TipoMovimentoEstoque();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Estorno Compra Encalhe");
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE);
		
		return tipoMovimento;
	}

    public static ChamadaEncalheFornecedor newChamadaEncalheFornecedor(
            Long codigoDistribuidor, Fornecedor fornecedor, Long numero,
            Long controle, Integer tipo, Integer anoReferencia, Integer semana,
            Date dataLimite, Date dataEmissao, BigDecimal totalCreditoApurado,
            BigDecimal totalCreditoInformado, BigDecimal totalMargemApurado,
            BigDecimal totalMargemInformado, BigDecimal totalVendaApurado,
            BigDecimal totalVendaInformada, String status,
            String codigoPreenchimento, BigDecimal notaValoresDiversos) {
        
        ChamadaEncalheFornecedor ce = new ChamadaEncalheFornecedor();
        ce.setCodigoDistribuidor(codigoDistribuidor);
        ce.setFornecedor(fornecedor);
        ce.setNumeroChamadaEncalhe(numero);
        ce.setControle(controle);
        ce.setTipoChamadaEncalhe(tipo);
        ce.setAnoReferencia(anoReferencia);
        ce.setNumeroSemana(semana);
        ce.setDataLimiteRecebimento(dataLimite);
        ce.setCodigoPreenchimento(codigoPreenchimento);
        ce.setDataEmissao(dataEmissao);
        ce.setNotaValoresDiversos(notaValoresDiversos);
        ce.setStatus(status);
        ce.setTotalCreditoApurado(totalCreditoApurado);
        ce.setTotalCreditoInformado(totalCreditoInformado);
        ce.setTotalMargemApurado(totalMargemApurado);
        ce.setTotalMargemInformado(totalMargemInformado);
        ce.setTotalVendaApurada(totalVendaApurado);
        ce.setTotalVendaInformada(totalVendaInformada);

        return ce;
    }
	
    public static ItemChamadaEncalheFornecedor newItemChamadaEncalheFornecedor(
            ChamadaEncalheFornecedor chamada, ProdutoEdicao pe, Integer controle, Long numeroDocumento,
            Integer numeroItem, Long qtdeEnviada, BigDecimal precoUnitario,
            FormaDevolucao formaDevolucao,
            RegimeRecolhimento regimeRecolhimento,
            BigDecimal valorMargemApurado, 
            Long numeroNota,
            Long qtdeVendaInformada,
            BigDecimal valorVendaInformado, 
            BigDecimal valorVendaApurado, 
            Long qtdeDevolucaoInformada, Date dataRecolhimento, String status, String tipoProduto) {

        ItemChamadaEncalheFornecedor item = new ItemChamadaEncalheFornecedor();
        item.setChamadaEncalheFornecedor(chamada);
        chamada.getItens().add(item);
        item.setProdutoEdicao(pe);
        item.setControle(controle);
        item.setNumeroDocumento(numeroDocumento);
        item.setNumeroItem(numeroItem);
        item.setQtdeEnviada(qtdeEnviada);
        item.setPrecoUnitario(precoUnitario);
        item.setFormaDevolucao(formaDevolucao);
        item.setRegimeRecolhimento(regimeRecolhimento);
        item.setValorMargemApurado(valorMargemApurado);
        item.setNumeroNotaEnvio(numeroNota);
        item.setQtdeVendaInformada(qtdeVendaInformada);
        item.setValorVendaInformado(valorVendaInformado);
        item.setValorVendaApurado(valorVendaApurado);
        item.setQtdeDevolucaoInformada(qtdeDevolucaoInformada);
        item.setDataRecolhimento(dataRecolhimento);
        item.setStatus(status);
        item.setTipoProduto(tipoProduto);

        return item;
    }

}
