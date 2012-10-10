package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Algoritmo;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
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
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
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
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
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
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
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
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
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
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.EntityUtil;
import br.com.abril.nds.util.Util;

public class DataLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

	private static final String PARAM_SKIP_DATA = "skipData";
	private static final String PARAM_CLEAN_DATA = "cleanData";
	
	private static PessoaJuridica juridicaTreelog;
	private static PessoaJuridica juridicaAcme;
	private static PessoaJuridica juridicaDinap;
	private static PessoaJuridica juridicaFc;
	private static PessoaJuridica juridicaValida;
	private static PessoaFisica manoel;
	private static PessoaFisica manoelCunha;
	private static PessoaFisica jose;
	private static PessoaFisica maria;

	private static PessoaFisica guilherme;
	private static PessoaFisica murilo;
	private static PessoaFisica mariana;
	private static PessoaFisica orlando;
	private static PessoaFisica joao;
	private static PessoaFisica luis;

	private static TipoMovimentoEstoque tipoMovimentoFaltaEm;
	private static TipoMovimentoEstoque tipoMovimentoFaltaDe;
	private static TipoMovimentoEstoque tipoMovimentoSobraEm;
	private static TipoMovimentoEstoque tipoMovimentoSobraDe;
	private static TipoMovimentoEstoque tipoMovimentoRecFisico;
	private static TipoMovimentoEstoque tipoMovimentoRecReparte;
	private static TipoMovimentoEstoque tipoMovimentoNivelamentoEntrada;
	private static TipoMovimentoEstoque tipoMovimentoNivelamentoSaida;
	
	private static TipoMovimentoEstoque tipoMovimentoEnvioEncalhe;

	private static TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe;
	private static TipoMovimentoEstoque tipoMovimentoRecebimentoEncalheJuramentado;
	private static TipoMovimentoEstoque tipoMovimentoSuplementarEnvioEncalheAnteriroProgramacao;
	private static TipoMovimentoEstoque tipoMovimentoEstoqueCompraSuplementar;
	private static TipoMovimentoEstoque tipoMovimentoEstoqueEstornoCompraSuplementar;

	private static TipoMovimentoEstoque tipoMovimentoEstornoCotaAusente;
	private static TipoMovimentoEstoque tipoMovimentoSuplementarCotaAusente;

	private static TipoMovimentoEstoque tipoMovimentoEstornoCotaEnvioReparte;
	private static TipoMovimentoEstoque tipoMovimentoEntradaSuplementarEnvioReparte;
	
	private static TipoMovimentoEstoque tipoMovimentoReparteCotaAusente;
	private static TipoMovimentoEstoque tipoMovimentoRestautacaoReparteCotaAusente;

	private static TipoMovimentoEstoque tipoMovimentoVendaEncalhe;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompraEncalhe;
	private static TipoMovimentoEstoque tipoMovimentoEstornoVendaEncalhe;
	private static TipoMovimentoEstoque tipoMovimentoVendaEncalheSuplementar;
	private static TipoMovimentoEstoque tipoMovimentoEncalheAntecipado;
	private static TipoMovimentoEstoque tipoMovimentoEstornoVendaEncalheSuplementar;

	private static TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro;

	private static TipoMovimentoEstoque tipoMovimentoCancelamentoNFDevolucaoConsignado;
	private static TipoMovimentoEstoque tipoMovimentoCancelamentoNFEnvioConsignado;
	
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoNA;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebitoFaturamento;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte;

	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe;

	private static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaLancamento;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaLancamento;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaSuplementar;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaSuplementar;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaRecolhimento;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaRecolhimento;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaEntradaProdutosDanificados;
	private static TipoMovimentoEstoque tipoMovimentoTransferenciaSaidaProdutosDanificados;
	
	private static MovimentoEstoqueCota movimentoEstoqueCota1;
	private static MovimentoEstoqueCota movimentoEstoqueCota2;
	private static MovimentoEstoqueCota movimentoEstoqueCota3;
	private static MovimentoEstoqueCota movimentoEstoqueCota4;
	private static MovimentoEstoqueCota movimentoEstoqueCota5;
	private static MovimentoEstoqueCota movimentoEstoqueCota6;
	private static MovimentoEstoqueCota movimentoEstoqueCota7;
	private static MovimentoEstoqueCota movimentoEstoqueCota8;
	private static MovimentoEstoqueCota movimentoEstoqueCota9;
	private static MovimentoEstoqueCota movimentoEstoqueCota10;
	private static MovimentoEstoqueCota movimentoEstoqueCota11;
	private static MovimentoEstoqueCota movimentoEstoqueCota12;
	private static MovimentoEstoqueCota movimentoEstoqueCota13;
	private static MovimentoEstoqueCota movimentoEstoqueCota14;
	private static MovimentoEstoqueCota movimentoEstoqueCota15;
	private static MovimentoEstoqueCota movimentoEstoqueCota16;
	private static MovimentoEstoqueCota movimentoEstoqueCota17;
	private static MovimentoEstoqueCota movimentoEstoqueCota18;
	private static MovimentoEstoqueCota movimentoEstoqueCota19;
	private static MovimentoEstoqueCota movimentoEstoqueCota20;
	private static MovimentoEstoqueCota movimentoEstoqueCota21;
	private static MovimentoEstoqueCota movimentoEstoqueCota22;
	private static MovimentoEstoqueCota movimentoEstoqueCota23;
	private static MovimentoEstoqueCota movimentoEstoqueCota24;
	private static MovimentoEstoqueCota movimentoEstoqueCota25;
	private static MovimentoEstoqueCota movimentoEstoqueCota26;
	private static MovimentoEstoqueCota movimentoEstoqueCota27;
	private static MovimentoEstoqueCota movimentoEstoqueCota28;
	private static MovimentoEstoqueCota movimentoEstoqueCota29;
	private static MovimentoEstoqueCota movimentoEstoqueCota30;


	private static MovimentoFinanceiroCota movimentoFinanceiroCota1;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota2;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota3;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota4;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota5;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota6;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota7;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota8;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota9;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota10;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota11;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota12;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota13;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota14;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota15;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota16;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota17;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota18;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota19;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota20;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota21;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota22;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota23;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota24;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota25;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota26;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota27;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota28;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota29;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota30;

	private static CFOP cfop5102;
	private static TipoNotaFiscal tipoNotaFiscalRecebimento;
	private static TipoNotaFiscal tipoNotaFiscalDevolucao;
	private static Usuario usuarioJoao;
	private static Fornecedor fornecedorAcme;
	private static Fornecedor fornecedorDinap;
	private static Fornecedor fornecedorFc;
	private static Distribuidor distribuidor;

	private static NCM ncmCartaz;	
	private static NCM ncmCd;	
	private static NCM ncmRevista;
	private static NCM ncmLivro;
	private static NCM ncmFasciculo;
	private static NCM ncmLivroilustrado;
	private static NCM ncmFigurinha;
	private static NCM ncmBebidas;

	
	private static TipoProduto tipoProdutoRefrigerante;
	private static TipoProduto tipoProdutoRevista;
	private static TipoProduto tipoProdutoFasciculo;
	private static TipoProduto tipoProdutoLivro;
	private static TipoProduto tipoProdutoCromo;
	private static TipoProduto tipoProdutoCard;
	private static TipoProduto tipoProdutoAlbun;
	private static TipoProduto tipoProdutoGuia;
	private static TipoProduto tipoProdutoQuadrinho;
	private static TipoProduto tipoProdutoAtividade;
	private static TipoProduto tipoProdutoPassatempo;
	private static TipoProduto tipoProdutoVideo;
	private static TipoProduto tipoProdutoCdrom;
	private static TipoProduto tipoProdutoPoster;
	private static TipoProduto tipoProdutoJornal;
	private static TipoProduto tipoProdutoTabloide;
	private static TipoProduto tipoProdutoOutro;
	private static TipoProduto tipoProdutoCapaDura;
	private static TipoProduto tipoProdutoRevistaDigital;
	private static TipoProduto tipoProdutoDvd;
	private static TipoProduto tipoProdutoLivroIlustrado;


	private static Produto produtoVeja;
	private static Produto produtoSuper;
	private static Produto produtoCapricho;
	private static Produto produtoInfoExame;
	private static Produto produtoQuatroRodas;
	private static Produto produtoBoaForma;
	private static Produto produtoBravo;
	private static Produto produtoCaras;
	private static Produto produtoCasaClaudia;
	private static Produto produtoClaudia;
	private static Produto produtoContigo;
	private static Produto produtoManequim;
	private static Produto produtoNatGeo;
	private static Produto produtoPlacar;
	private static Produto cocaCola;

	private static ProdutoEdicao produtoEdicaoVeja1;
	private static ProdutoEdicao produtoEdicaoVeja2;
	private static ProdutoEdicao produtoEdicaoVeja3;
	private static ProdutoEdicao produtoEdicaoVeja4;
	private static ProdutoEdicao produtoEdicaoSuper1;
	private static ProdutoEdicao produtoEdicaoCapricho1;
	private static ProdutoEdicao produtoEdicaoInfoExame1;
	private static ProdutoEdicao produtoEdicaoQuatroRodas1;
	private static ProdutoEdicao produtoEdicaoBoaForma1;
	private static ProdutoEdicao produtoEdicaoBravo1;
	private static ProdutoEdicao produtoEdicaoCaras1;
	private static ProdutoEdicao produtoEdicaoCasaClaudia1;
	private static ProdutoEdicao produtoEdicaoClaudia1;
	private static ProdutoEdicao produtoEdicaoContigo1;
	private static ProdutoEdicao produtoEdicaoManequim1;
	private static ProdutoEdicao produtoEdicaoNatGeo1;
	private static ProdutoEdicao produtoEdicaoPlacar1;
	private static ProdutoEdicao cocaColaLight;
	private static ProdutoEdicao produtoEdicaoVeja1EncalheAnt;
	private static ProdutoEdicao produtoEdicaoVeja2EncalheAnt;
	private static ProdutoEdicao produtoEdicaoSuper1EncalheAnt;
	private static ProdutoEdicao produtoEdicaoSuper2EncalheAnt;
	private static ProdutoEdicao produtoVeja10;
	private static ProdutoEdicao produtoSuperInteressante10;
	private static ProdutoEdicao produtoCaras10;
	private static ProdutoEdicao produtoPlacar10;

	private static Lancamento lancamentoVeja1;
	private static Lancamento lancamentoVeja2;
	private static Lancamento lancamentoCanceladoVeja5;
	private static Lancamento lancamentoSuper1;
	private static Lancamento lancamentoCapricho1;
	private static Lancamento lancamentoCocaCola;

	private static Lancamento lancamentoInfoExame1;
	private static Lancamento lancamentoQuatroRodas1;
	private static Lancamento lancamentoBoaForma1;
	private static Lancamento lancamentoBravo1;
	private static Lancamento lancamentoCaras1;
	private static Lancamento lancamentoCasaClaudia1;
	private static Lancamento lancamentoClaudia1;
	private static Lancamento lancamentoContigo1;
	private static Lancamento lancamentoManequim1;
	private static Lancamento lancamentoNatGeo1;
	private static Lancamento lancamentoPlacar1;

	private static Lancamento lancamentoVeja1EcncalheAnt;
	private static Lancamento lancamentoVeja2EcncalheAnt;
	private static Lancamento lancamentoSuper1EcncalheAnt;
	private static Lancamento lancamentoSuper2EcncalheAnt;

	private static Lancamento lancamentoVeja10ComEstudo;
	private static Lancamento lancamentoSuperInteressante10ComEstudo;
	private static Lancamento lancamentoCaras10ComEstudo;
	private static Lancamento lancamentoPlacar10SemEstudo;

	private static ChamadaEncalhe chamadaEncalheVeja1;
	private static ControleConferenciaEncalhe controleConferenciaEncalheVeja1;
	private static ControleConferenciaEncalheCota controleConferenciaEncalheCotaVeja1;
	private static ChamadaEncalheCota chamadaEncalheCotaVeja1CotaJose;
	
	private static NotaFiscalEntradaFornecedor notaFiscalFornecedor;
	private static ItemNotaFiscalEntrada itemNotaFiscalFornecedor;
	private static RecebimentoFisico recebimentoFisico;
	private static ItemRecebimentoFisico itemRecebimentoFisico;

	private static NotaFiscalEntradaFornecedor notaFiscal10;

	private static EstoqueProduto estoqueProdutoVeja1;
	private static EstoqueProduto estoqueProdutoVeja2;
	private static EstoqueProduto estoqueProdutoVeja3;
	private static EstoqueProduto estoqueProdutoVeja4;
	private static EstoqueProduto estoqueProdutoSuper1;
	private static EstoqueProduto estoqueProdutoCapricho1;
	private static EstoqueProduto estoqueProdutoInfoExame1;
	private static EstoqueProduto estoqueProdutoVeja1EncalheAnt;
	private static EstoqueProduto estoqueProdutoVeja2EncalheAnt;
	private static EstoqueProduto estoqueProdutoSuper1EncalheAnt;
	private static EstoqueProduto estoqueProdutoSuper2EncalheAnt;
	
	private static EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentadoVeja;
	private static EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentadoCapricho;
	private static EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentadoInfoExame;

	private static TipoFornecedor tipoFornecedorPublicacao;
	private static TipoFornecedor tipoFornecedorOutros;

	private static ItemNotaFiscalEntrada itemNotaFiscalCoca;
	private static ItemRecebimentoFisico itemCocaRecebimentoFisico;

	private static ItemNotaFiscalEntrada itemNotaInfoExame1;
	private static ItemRecebimentoFisico itemInfoExame1;

	private static ItemNotaFiscalEntrada itemNotaQuatroRodas1;
	private static ItemRecebimentoFisico itemQuatroRodas1;

	private static ItemNotaFiscalEntrada itemNotaBoaForma1;
	private static ItemRecebimentoFisico itemBoaForma1;

	private static ItemNotaFiscalEntrada itemNotaBravo1;
	private static ItemRecebimentoFisico itemBravo1;

	private static ItemNotaFiscalEntrada itemNotaCaras1;
	private static ItemRecebimentoFisico itemCaras1;

	private static ItemNotaFiscalEntrada itemNotaCasaClaudia1;
	private static ItemRecebimentoFisico itemCasaClaudia1;

	private static ItemNotaFiscalEntrada itemNotaClaudia1;
	private static ItemRecebimentoFisico itemClaudia1;

	private static ItemNotaFiscalEntrada itemNotaContigo1;
	private static ItemRecebimentoFisico itemContigo1;

	private static ItemNotaFiscalEntrada itemNotaManequim1;
	private static ItemRecebimentoFisico itemManequim1;

	private static ItemNotaFiscalEntrada itemNotaNatGeo1;
	private static ItemRecebimentoFisico itemNatGeo1;

	private static ItemNotaFiscalEntrada itemNotaPlacar1;
	private static ItemRecebimentoFisico itemPlacar1;

	private static RecebimentoFisico recebimentoFisico10;

	private static ItemNotaFiscalEntrada itemNotaVeja10;
	private static ItemRecebimentoFisico itemVeja10;

	private static ItemNotaFiscalEntrada itemNotaSuperInteressante10;
	private static ItemRecebimentoFisico itemSuperInteressante10;

	private static ItemNotaFiscalEntrada itemNotaCaras10;
	private static ItemRecebimentoFisico itemCaras10;

	private static ItemNotaFiscalEntrada itemNotaPlacar10;
	private static ItemRecebimentoFisico itemPlacar10;


	private static Cota cotaJose;
	private static Cota cotaManoel;
	private static Cota cotaManoelCunha;
	private static Cota cotaMaria;
	private static Cota cotaLuis;
	private static Cota cotaJoao;
	private static Cota cotaGuilherme;
	private static Cota cotaMurilo;
	private static Cota cotaMariana;
	private static Cota cotaOrlando;

	private static ParametroCobrancaCota parametroCobrancaGuilherme;
	private static ParametroCobrancaCota parametroCobrancaMurilo;
	private static ParametroCobrancaCota parametroCobrancaMariana;
	private static ParametroCobrancaCota parametroCobrancaOralando;
	private static ParametroCobrancaCota parametroCobrancaManoel;

	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaJose;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaManoel;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaMaria;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaLuis;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaJoao;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaGuilherme;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaMurilo;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaMariana;
	private static ParametrosCotaNotaFiscalEletronica parametroCotaNotaFiscalEletronicaOrlando;
	
	private static Estudo estudoVeja1;
	private static Estudo estudoVeja1Atual;
	private static Estudo estudoVeja2;
	private static Estudo estudoSuper1;
	private static Estudo estudoCapricho1;
	private static Estudo estudoVeja1EncalheAnt;
	private static Estudo estudoVeja2EncalheAnt;


	private static Estudo estudoInfoExame1;
	private static Estudo estudoQuatroRodas1;
	private static Estudo estudoBoaForma1;
	private static Estudo estudoBravo1;
	private static Estudo estudoCaras1;
	private static Estudo estudoCasaClaudia1;
	private static Estudo estudoClaudia1;
	private static Estudo estudoContigo1;
	private static Estudo estudoManequim1;
	private static Estudo estudoNatGeo1;
	private static Estudo estudoPlacar1;

	private static Estudo estudoVeja10;
	private static Estudo estudoSuperInteressante10;
	private static Estudo estudoCaras10;
	private static Estudo estudoPlacar10;

	private static EstudoCota estudoCotaSuper1Manoel;
	private static EstudoCota estudoCotaManoel;
	private static EstudoCota estudoCotaManoelVejaAtual;
	private static EstudoCota estudoCotaManoelVeja2;
	private static EstudoCota estudoCotaVeja2Joao;
	private static EstudoCota estudoCotaCaprichoZe;
	private static EstudoCota estudoCotaVeja1Joao;
	private static EstudoCota estudoCotaVeja1JoaoEncaljeAnt;
	private static EstudoCota estudoCotaVeja1ManoelEncaljeAnt;
	private static EstudoCota estudoCotaVeja1MariaEncaljeAnt;
	private static EstudoCota estudoCotaVeja2JoaoEncaljeAnt;
	private static EstudoCota estudoCotaVeja2ManoelEncaljeAnt;
	private static EstudoCota estudoCotaVeja2MariaEncaljeAnt;
	private static EstudoCota estudoCotaSuper1JoaoEncaljeAnt;
	private static EstudoCota estudoCotaSuper1ManoelEncaljeAnt;
	private static EstudoCota estudoCotaSuper1MariaEncaljeAnt;
	private static EstudoCota estudoCotaSuper2JoaoEncaljeAnt;
	private static EstudoCota estudoCotaSuper2ManoelEncaljeAnt;
	private static EstudoCota estudoCotaSuper2MariaEncaljeAnt;
	private static EstudoCota estudoJoseInfoExame1;
	private static EstudoCota estudoJoseQuatroRodas1;
	private static EstudoCota estudoJoseBoaForma1;
	private static EstudoCota estudoJoseBravo1;
	private static EstudoCota estudoJoseCaras1;
	private static EstudoCota estudoJoseCasaClaudia1;
	private static EstudoCota estudoJoseClaudia1;
	private static EstudoCota estudoJoseContigo1;
	private static EstudoCota estudoJoseManequim1;
	private static EstudoCota estudoJoseNatGeo1;
	private static EstudoCota estudoJoseEdicaoPlacar1;

	private static EstudoCota estudoCotaVeja10Guilherme;
	private static EstudoCota estudoCotaSuperInteressante10Murilo;
	private static EstudoCota estudoCotaCaras10Mariana;
	private static EstudoCota estudoCotaPlacarOrlando;

	private static Box box300Reparte;
	private static Box box1;
	private static Box box2;
	private static Box boxPostoAvancado;

	private static Banco bancoHSBC;
	private static Banco bancoITAU;
	private static Banco bancoDOBRASIL;
	private static Banco bancoBRADESCO;

	private static Divida divida1;
	private static Divida divida2;
	private static Divida divida3;
	private static Divida divida4;
	private static Divida divida5;
	private static Divida divida6;
	private static Divida divida7;
	private static Divida divida8;
	private static Divida divida9;
	private static Divida divida10;
	private static Divida divida11;
	private static Divida divida12;
	private static Divida divida13;
	private static Divida divida14;
	private static Divida divida15;
	private static Divida divida16;
	private static Divida divida17;


	private static Divida dividaGuilherme1;
	private static Divida dividaGuilherme2;
	private static Divida dividaGuilherme3;
	private static Divida dividaMurilo1;
	private static Divida dividaMurilo2;
	private static Divida dividaMurilo3;
	private static Divida dividaMariana1;
	private static Divida dividaMariana2;
	private static Divida dividaOrlando;

	private static Divida dividaAcumuladaGuilherme1;
	private static Divida dividaAcumuladaGuilherme2;
	private static Divida dividaAcumuladaMurilo1;
	private static Divida dividaAcumuladaMariana1;

	private static HistoricoAcumuloDivida acumDividaGuilherme1;
	private static HistoricoAcumuloDivida acumDividaGuilherme2;
	private static HistoricoAcumuloDivida acumDividaMurilo1;
	private static HistoricoAcumuloDivida acumDividaMariana1;

	private static EstoqueProdutoCota estoqueProdutoCotaVeja1;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja2;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja3;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja4;
	private static EstoqueProdutoCota estoqueProdutoCotaSuper1;
	private static EstoqueProdutoCota estoqueProdutoCotaCapricho1;
	private static EstoqueProdutoCota estoqueProdutoCotaQuatroRodas1;
	private static EstoqueProdutoCota estoqueProdutoCotaInfoExame1;
	private static EstoqueProdutoCota estoqueProdutoCotaJoseVeja1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaMariaVeja1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaJoseVeja2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaMariaVeja2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaJoseSuper1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelSuper1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaMariaSuper1EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaJoseSuper2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelSuper2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaMariaSuper2EncalheAnt;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaVeja1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaVeja2;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaVeja3;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaVeja4;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaSuper1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaCapricho1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCunhaQuatroRodas1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja2;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja3;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelVeja4;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelCapricho1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelQuatroRodas1;
	private static EstoqueProdutoCota estoqueProdutoCotaManoelSuper1;


	private static CobrancaDinheiro cobrancaGuilherme1;
	private static CobrancaDinheiro cobrancaGuilherme2;
	private static CobrancaDinheiro cobrancaGuilherme3;
	private static Boleto cobrancaMurilo1;
	private static Boleto cobrancaMurilo2;
	private static Boleto cobrancaMurilo3;
	private static Boleto cobrancaMariana1;
	private static Boleto cobrancaMariana2;
	private static CobrancaDeposito cobrancaOrlando;

	private static CobrancaDinheiro cobrancaAcumuloGuilherme1;
	private static CobrancaDinheiro cobrancaAcumuloGuilherme2;
	private static Boleto cobrancaAcumuloMurilo1;
	private static Boleto cobrancaAcumuloMariana1;


	private static ControleBaixaBancaria baixaBancaria;


	private static Boleto boleto1;
	private static Boleto boleto2;
	private static Boleto boleto3;
	private static Boleto boleto4;
	private static Boleto boleto5;
	private static Boleto boleto6;
	private static Boleto boleto7;
	private static Boleto boleto8;
	private static Boleto boleto9;


	private static FormaCobranca formaBoleto;
	private static FormaCobranca formaDinheiro;
	private static FormaCobranca formaCheque;
	private static FormaCobranca formaDeposito;
	private static FormaCobranca formaTransferenciBancaria;

	private static Editor editoraAbril;
	private static Estudo estudoSuper1EncalheAnt;
	private static Estudo estudoSuper2EncalheAnt;
	private static Cota cotaAcme;
	private static Cota cotaJoana;

	private static TipoEntrega tipoCotaRetira;
	private static TipoEntrega tipoEntregaEmBanca;
	private static TipoEntrega tipoEntregador;
	private static PDV pdvOrlando;
	private static PDV pdvMariana;
	private static PDV pdvMurilo;
	private static PDV pdvGuilherme;
	private static PDV pdvJoao;
	private static PDV pdvLuis;
	private static PDV pdvMaria;
	private static PDV pdvManoelCunha;
	private static PDV pdvManoel;
	private static PDV pdvJose;

	private static CFOP cfop1209;
	private static CFOP cfop1210;

	private static ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscalEntradaDevolucaoEncalhe;
	private static ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscalDevolucaoMercadoria;

	private static CFOP cfop6115;
	private static CFOP cfop5115;
	private static CFOP cfop2949;
	private static CFOP cfop1949;
	private static CFOP cfop6949;
	private static CFOP cfop5949;
	private static CFOP cfop6114;
	private static CFOP cfop5114;
	private static CFOP cfop2919;
	private static CFOP cfop1919;
	private static CFOP cfop2918;
	private static CFOP cfop1918;
	private static CFOP cfop6917;
	private static CFOP cfop5917;

	private static ControleNumeracaoSlip controleNumeracaoSlipConferenciaEncalhe;
	private static ControleNumeracaoSlip controleNumeracaoSlipVendaEncalhe;

	private static ProdutoEdicao javaMagazineEdicao101;
	private static ProdutoEdicao javaMagazineEdicao102;
	private static ProdutoEdicao mundoJavaEdicao101;
	private static ProdutoEdicao mundoJavaEdicao102;
	private static ProdutoEdicao sqlMagazineEdicao101;
	private static ProdutoEdicao sqlMagazineEdicao102;
	private static ProdutoEdicao galileuEdicao101;
	private static ProdutoEdicao galileuEdicao102;
	private static ProdutoEdicao duasRodasEdicao101;
	private static ProdutoEdicao duasRodasEdicao102;
	private static ProdutoEdicao guitarPlayerEdicao101;
	private static ProdutoEdicao guitarPlayerEdicao102;
	private static ProdutoEdicao roadieCrewEdicao101;
	private static ProdutoEdicao roadieCrewEdicao102;
	private static ProdutoEdicao rockBrigadeEdicao101;
	private static ProdutoEdicao rockBrigadeEdicao102;
	private static ProdutoEdicao valhallaEdicao101;
	private static ProdutoEdicao valhallaEdicao102;
	private static ProdutoEdicao rollingStoneEdicao101;
	private static ProdutoEdicao rollingStoneEdicao102;
	private static ProdutoEdicao bonsFluidosEdicao101;
	private static ProdutoEdicao bonsFluidosEdicao102;
	private static ProdutoEdicao bravoEdicao101;
	private static ProdutoEdicao bravoEdicao102;
	private static ProdutoEdicao casaClaudiaEdicao101;
	private static ProdutoEdicao casaClaudiaEdicao102;
	private static ProdutoEdicao jequitiEdicao101;
	private static ProdutoEdicao jequitiEdicao102;
	private static ProdutoEdicao mundoEstranhoEdicao101;
	private static ProdutoEdicao mundoEstranhoEdicao102;
	private static ProdutoEdicao novaEscolaEdicao101;
	private static ProdutoEdicao novaEscolaEdicao102;
	private static ProdutoEdicao minhaCasaEdicao101;
	private static ProdutoEdicao minhaCasaEdicao102;
	private static ProdutoEdicao recreioEdicao101;
	private static ProdutoEdicao recreioEdicao102;
	private static ProdutoEdicao womenHealthEdicao101;
	private static ProdutoEdicao womenHealthEdicao102;
	private static ProdutoEdicao viagemTurismoEdicao101;
	private static ProdutoEdicao viagemTurismoEdicao102;
	private static ProdutoEdicao vipEdicao101;
	private static ProdutoEdicao vipEdicao102;
	private static ProdutoEdicao gestaoEscolarEdicao101;
	private static ProdutoEdicao gestaoEscolarEdicao102;
	private static ProdutoEdicao lolaEdicao101;
	private static ProdutoEdicao lolaEdicao102;
	private static ProdutoEdicao heavyMetalEdicao101;
	private static ProdutoEdicao heavyMetalEdicao102;
	private static ProdutoEdicao metalUndergroundEdicao101;
	private static ProdutoEdicao metalUndergroundEdicao102;

	private static InterfaceExecucao interfaceEMS0106;
	private static InterfaceExecucao interfaceEMS0107;
	private static InterfaceExecucao interfaceEMS0108;
	private static InterfaceExecucao interfaceEMS0109;
	private static InterfaceExecucao interfaceEMS0110;
	private static InterfaceExecucao interfaceEMS0111;
	private static InterfaceExecucao interfaceEMS0112;
	private static InterfaceExecucao interfaceEMS0113;
	private static InterfaceExecucao interfaceEMS0114;
	private static InterfaceExecucao interfaceEMS0116;
	private static InterfaceExecucao interfaceEMS0117;
	private static InterfaceExecucao interfaceEMS0118;
	private static InterfaceExecucao interfaceEMS0119;
	private static InterfaceExecucao interfaceEMS0120;
	private static InterfaceExecucao interfaceEMS0121;
	private static InterfaceExecucao interfaceEMS0122;
	private static InterfaceExecucao interfaceEMS0123;
	private static InterfaceExecucao interfaceEMS0124;
	private static InterfaceExecucao interfaceEMS0125;
	private static InterfaceExecucao interfaceEMS0126;
	private static InterfaceExecucao interfaceEMS0129;
	private static InterfaceExecucao interfaceEMS0130;
	private static InterfaceExecucao interfaceEMS0131;
	private static InterfaceExecucao interfaceEMS0132;
	private static InterfaceExecucao interfaceEMS0133;
	private static InterfaceExecucao interfaceEMS0135;
	private static InterfaceExecucao interfaceEMS0185;
	private static InterfaceExecucao interfaceEMS0197;
	private static InterfaceExecucao interfaceEMS0198;

	private static EventoExecucao eventoErroInfraestrutura;
	private static EventoExecucao eventoSemDominio;
	private static EventoExecucao eventoHierarquiaCorrompida;
	private static EventoExecucao eventoRelacionamentoNaoEncontrado;
	private static EventoExecucao eventoGeracaoArquivo;
	private static EventoExecucao eventoInformacaoDadoAlterado;
	private static EventoExecucao eventoRegistroExistente;
	
	private static Endereco enderecoMococa1;
	private static Endereco enderecoMococa2;
	private static Endereco enderecoLuisMococa3;
	private static Endereco enderecoRioPardo1;
	private static Endereco enderecoRioPardo2;
	private static Endereco enderecoRioPardo3;

	private static TipoPontoPDV tipoPontoPDVBanca;
	private static TipoPontoPDV tipoPontoPDVRevistaria;
	private static TipoPontoPDV tipoPontoPDVLivraria;
	private static TipoPontoPDV tipoPontoPDVEtc;
	
	private static Lancamento lancamentoCanceladoSuper2;
	private static Lancamento lancamentoCanceladoCapricho2;
	private static ProdutoEdicao produtoEdicaoVeja5;
	private static ProdutoEdicao produtoEdicaoSuper2;
	private static ProdutoEdicao produtoEdicaoCapricho2;

	private static Object tipoMovimentoCompraEncalhe;

	private static Object tipoMovimentoEstornoCompraEncalhe;

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext-dataLoader.xml");
		SessionFactory sf = null;
		Session session = null;
		Transaction tx = null;
		boolean commit = false;

		LOG.info("==== INICIANDO EXECUÇÃO DATALOADER ====");
		List<String> parans =  Arrays.asList(args);
		if(!parans.contains(PARAM_SKIP_DATA)){
			try {
			    sf = ctx.getBean(SessionFactory.class);
				session = sf.openSession();
				tx = session.beginTransaction();				
				
				if (parans.contains(PARAM_CLEAN_DATA)) {
					carregarDadosClean(session);
				} else {
					carregarDados(session);
				}

				commit = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (commit) {
					tx.commit();
				} else {
					tx.rollback();
				}
				if (session != null) {
					session.close();
				}
				if (sf != null) {
					sf.close();
				}
			}
		}
		LOG.info("==== EXECUÇÃO DATALOADER CONCLUÍDA ====");
	}

	private static void gerarCotasAusentes(Session session) {
		CotaAusente cotaAusente1 = Fixture.cotaAusente(new Date(), true, cotaGuilherme);
		CotaAusente cotaAusente2 = Fixture.cotaAusente(new Date(), true, cotaMurilo);
		CotaAusente cotaAusente3 = Fixture.cotaAusente(new Date(), true, cotaMariana);
		CotaAusente cotaAusente4 = Fixture.cotaAusente(Fixture.criarData(1, 1, 2001), true, cotaOrlando);

		save(session, cotaAusente1, cotaAusente2, cotaAusente3, cotaAusente4);
	}

	private static void carregarDados(Session session) {
		carregarDadosClean(session);

		criarPessoas(session);
		criarFornecedores(session);

		
		//criarBanco(session);
		criarUsuarios(session);
		
		criarDiasDistribuicaoFornecedores(session);
		criarDiasDistribuicaoDistribuidor(session);
		criarCotas(session);
		criarPDVsCota(session);
		criarDistribuicaoCota(session);
		criarEditores(session);		
		criarProdutos(session);
		criarProdutosEdicao(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarEstoquesProdutosCotaJuramentados(session);
		criarEstoqueProdutoCota(session);
		criarMovimentosEstoque(session);
		criarLancamentos(session);
		criarLancamentosCancelados(session);
		criarEstudos(session);
		criarEstudosCota(session);
		
		massaConferenciaParaMovimentoEstoqueCota(session);
		
		criarMovimentosEstoqueCota(session);
		criarEndereco(session);
		criarEnderecoPDV(session);
		criarEnderecoCotaPF(session);
		criarTelefoneCotaPF(session);
		criarMovimentosFinanceiroCota(session);
		criarDivida(session);
		criarControleNumeracaoSlip(session);
		criarCobrancas(session);

		criarBoletos(session);
		criarCobrancaCheque(session);
		criarCobrancaDepositoBancaria(session);
		criarCobrancaDinheiro(session);
		criarCobrancaTranferenciaBancaria(session);
		criarNotasFiscaisEntradaFornecedor(session);
		criarRotaRoteiroCota(session);
		criarControleBaixaBancaria(session);
		criarParametrosCobrancaCota(session);
		gerarCotasAusentes(session);
		gerarHistoricosAculoDivida(session);

		//massaDadosContaCorrenteTipoMovimento(session);

		//criarDadosContaCorrenteConsigando(session);

		criarMassaNotaFiscalEntradaFornecedorParaRecebimentoFisico(session);

		gerarCargaDiferencaEstoque(
			session, 2, produtoEdicaoVeja1, tipoMovimentoFaltaEm,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM, TipoDirecionamentoDiferenca.COTA);

		gerarCargaDiferencaEstoque(
			session, 2, produtoEdicaoVeja2, tipoMovimentoFaltaDe,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE,TipoDirecionamentoDiferenca.ESTOQUE);

		gerarCargaDiferencaEstoque(
			session, 2, produtoEdicaoVeja3, tipoMovimentoSobraDe,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE,TipoDirecionamentoDiferenca.ESTOQUE);

		gerarCargaDiferencaEstoque(
			session, 2, produtoEdicaoVeja4, tipoMovimentoSobraEm,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM,TipoDirecionamentoDiferenca.ESTOQUE);

		gerarCargaHistoricoSituacaoCota(session, 100);

		gerarCargaDadosConsultaEncalhe(session);

		gerarCargaDadosNotasFiscaisNFE(session);

		gerarEntregadores(session);

		gerarAreaInfluenciaPDV(session);

		gerarTipoGeradorFluxoPDV(session);

		gerarMaterialPromocionalPDV(session);

		gerarTipoEstabelecimentoAssociacaoPDV(session);

		gerarTipoLicencaMunicipalPDV(session);

		gerarTipoEntrega(session);

		gerarParciais(session);

		criarDadosBalanceamentos(session);

		criarDadosBalanceamentoLancamento(session);

		criarDadosBalanceamentoRecolhimento(session);

		gerarCargaDadosConferenciaEncalhe(session);

		gerarTiposNotas(session);

		gerarLogExecucaoInterfaces(session);

		gerarLogradouros(session);

		//criarNovaNotaFiscal(session);
		
		gerarDescontoDistribuidorParaFornecedor(session);
		
		criarDescontoProduto(session);
		
		criarDescontoProdutoEdicao(session);

		gerarDescontoCota(session);
		
		gerarGrupos(session);
		
		criarDescontoLogistica(session);
	}

	
	private static void gerarGrupos(Session session) {
		
		Set<DiaSemana> diasGrupo1 = EnumSet.of(DiaSemana.DOMINGO,DiaSemana.QUARTA_FEIRA);
		
		GrupoCota grupo1 = Fixture.criarGrupoCota(
				null,
				"Grupo 1",
				TipoGrupo.TIPO_COTA,
				diasGrupo1,
				TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO,
				null,
				null);
		save(session, grupo1);
		
		Set<DiaSemana> diasGrupo2 = EnumSet.of(DiaSemana.SABADO,DiaSemana.SEXTA_FEIRA);
		
		GrupoCota grupo2 = Fixture.criarGrupoCota(
				null,
				"Grupo 2",
				TipoGrupo.MUNICIPIO,
				diasGrupo2,
				null,
				null,
				null);
		save(session, grupo2);
		
	}

	private static void gerarDescontoDistribuidorParaFornecedor(Session session) {
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedorAcme);
		fornecedores.add(fornecedorDinap);
		DescontoDistribuidor desconto1 = Fixture.descontoDistribuidor(new BigDecimal(2), distribuidor, fornecedores , usuarioJoao, new Date());
		
		Set<Fornecedor> fornecedores1 = new HashSet<Fornecedor>();
		fornecedores1.add(fornecedorAcme);
		fornecedores1.add(fornecedorDinap);
		DescontoDistribuidor desconto2 = Fixture.descontoDistribuidor(new BigDecimal(3), distribuidor, fornecedores1 , usuarioJoao, DateUtil.adicionarDias(new Date(), -2));
		
		Set<Fornecedor> fornecedores2 = new HashSet<Fornecedor>();
		fornecedores2.add(fornecedorAcme);
		DescontoDistribuidor desconto3 = Fixture.descontoDistribuidor(new BigDecimal(5), distribuidor, fornecedores2 , usuarioJoao,DateUtil.adicionarDias(new Date(), -3));
		
		Set<Fornecedor> fornecedores3 = new HashSet<Fornecedor>();
		fornecedores3.add(fornecedorDinap);
		DescontoDistribuidor desconto4 = Fixture.descontoDistribuidor(new BigDecimal(6), distribuidor, fornecedores3 , usuarioJoao,DateUtil.adicionarDias(new Date(), -4));
		
		Set<Fornecedor> fornecedores4 = new HashSet<Fornecedor>();
		fornecedores4.add(fornecedorAcme);
		fornecedores4.add(fornecedorDinap);
		DescontoDistribuidor desconto5 = Fixture.descontoDistribuidor(new BigDecimal(7), distribuidor, fornecedores4, usuarioJoao,DateUtil.adicionarDias(new Date(), -5));
		
		save(session,desconto1,desconto2,desconto3,desconto4,desconto5);
	}
	
	/*
	 * Carrega descontos especificos da cota
	 */
	public static void gerarDescontoCota(Session session){
		
		Usuario usuario = Fixture.usuarioJoao();
		save(session,usuario);
		
		Set<Fornecedor> fornecedores1 = new HashSet<Fornecedor>();
		fornecedores1.add(fornecedorAcme);
		DescontoCota desconto1 = Fixture.descontoCota(new BigDecimal(2), distribuidor, cotaManoel, fornecedores1, usuario, DateUtil.adicionarDias(new Date(), 0));
		
		Set<Fornecedor> fornecedores2 = new HashSet<Fornecedor>();
		fornecedores2.add(fornecedorAcme);
		fornecedores2.add(fornecedorDinap);
		DescontoCota desconto2 = Fixture.descontoCota(new BigDecimal(3), distribuidor, cotaManoel, fornecedores2, usuario,DateUtil.adicionarDias(new Date(), -1));
		
		Set<Fornecedor> fornecedores3 = new HashSet<Fornecedor>();
		fornecedores3.add(fornecedorAcme);
		fornecedores3.add(fornecedorDinap);
		fornecedores3.add(fornecedorFc);
		DescontoCota desconto3 = Fixture.descontoCota(new BigDecimal(4), distribuidor, cotaManoelCunha, fornecedores3, usuario,DateUtil.adicionarDias(new Date(), -2));
		
		save(session,desconto1,desconto2,desconto3);
	}

	/*
	 * Carga Inicial do sistema Zerado
	 * */
	
	private static void carregarDadosClean(Session session) {
		
		gerarCfops(session);			
		
		criarParametrosSistema(session);
		criarInterfaceExecucao(session);
		criarEventoExecucao(session);
		criarAlgoritmos(session);
				
		criarPessoasClean(session);
		criarTiposFornecedores(session);
		criarFornecedoresClean(session);
		
		tabelaNCM(session);
		criarTiposProduto(session);
		
		criarBanco(session);		
		
		//Remover Depois
		criarBox(session);criarDistribuidor
		(session);
		
		criarEnderecoDistribuidor(session);
		criarTelefoneDistribuidor(session);
		
		criarCFOP(session);		
		criarParametroEmissaoNotaFiscal(session);
		criarTiposNotaFiscal(session);
		
		criarFeriado(session);		
		
		criarUsuarioAdministrador(session); 
		
		criarTiposMovimento(session);
		gerarTiposPontoPDV(session);
		
	}

	private static void criarUsuarioAdministrador(Session session) {

		GrupoPermissao grupoAdmin = new GrupoPermissao();
		grupoAdmin.setNome("ADMIN");
		grupoAdmin.setPermissoes( new HashSet<Permissao>(Arrays.asList(Permissao.values())) );
		
		session.save(grupoAdmin);

		Usuario admin = new Usuario();
		admin.setLogin("admin");
		admin.setSenha("81dc9bdb52d04dc20036dbd8313ed055"); // Senha: 1234
		admin.setNome("Administrador");
		admin.setContaAtiva(true);
		admin.setEmail("adminteste@abril.com.br");
		
		Set<GrupoPermissao> gruposPermissoes = new HashSet<GrupoPermissao>();
		gruposPermissoes.add(grupoAdmin);
		
		admin.setGruposPermissoes(gruposPermissoes);
		
		session.save(admin);

	}
	
	private static void criarControleNumeracaoSlip(Session session) {

		controleNumeracaoSlipConferenciaEncalhe = new ControleNumeracaoSlip();
		controleNumeracaoSlipConferenciaEncalhe.setTipoSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE);
		controleNumeracaoSlipConferenciaEncalhe.setProximoNumeroSlip(1L);
		session.save(controleNumeracaoSlipConferenciaEncalhe);

		controleNumeracaoSlipVendaEncalhe = new ControleNumeracaoSlip();
		controleNumeracaoSlipVendaEncalhe.setProximoNumeroSlip(1L);
		controleNumeracaoSlipVendaEncalhe.setTipoSlip(TipoSlip.SLIP_VENDA_ENCALHE);
		session.save(controleNumeracaoSlipVendaEncalhe);

	}


	private static void criarAlgoritmos(Session session) {

		Algoritmo algoritmoSP = Fixture.criarAlgoritmo("Rota SP");
		Algoritmo algoritmoRJ = Fixture.criarAlgoritmo("Rota RJ");

		save(session, algoritmoRJ, algoritmoSP);
	}
	

	private static void gerarCfops(Session session) {

		cfop5917 = new CFOP();
		cfop5917.setCodigo("5917");
		cfop5917.setDescricao("Outra saída de mercadoria ou prestação de serviço não especificado");
		save(session, cfop5917);

		cfop6917 = new CFOP();
		cfop6917.setCodigo("6917");
		cfop6917.setDescricao("Remessa de mercadoria em consignação mercantil ou industrial");
		save(session, cfop6917);

		cfop1918 = new CFOP();
		cfop1918.setCodigo("1918");
		cfop1918.setDescricao("Devolução de mercadoria recebida em consignação mercantil ou industrial");
		save(session, cfop1918);

		cfop2918 = new CFOP();
		cfop2918.setCodigo("2918");
		cfop2918.setDescricao("Devolução de mercadoria remetida em consignação mercantil ou industrial");
		save(session, cfop2918);

		cfop1919 = new CFOP();
		cfop1919.setCodigo("1919");
		cfop1919.setDescricao("Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignação mercantil ou industrial");
		save(session, cfop1919);

		cfop2919 = new CFOP();
		cfop2919.setCodigo("2919");
		cfop2919.setDescricao("Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignação mercantil ou industrial");
		save(session, cfop2919);

		cfop5114 = new CFOP();
		cfop5114.setCodigo("5114");
		cfop5114.setDescricao("Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação mercantil");
		save(session, cfop5114);

		cfop6114 = new CFOP();
		cfop6114.setCodigo("6114");
		cfop6114.setDescricao("Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação mercantil");
		save(session, cfop6114);

		cfop5949 = new CFOP();
		cfop5949.setCodigo("5949");
		cfop5949.setDescricao("Outra saída de mercadoria ou prestação de serviço não especificado");
		save(session, cfop5949);

		cfop6949 = new CFOP();
		cfop6949.setCodigo("6949");
		cfop6949.setDescricao("Outra saída de mercadoria ou prestação de serviço não especificado");
		save(session, cfop6949);

		cfop1949 = new CFOP();
		cfop1949.setCodigo("1949");
		cfop1949.setDescricao("Outra entrada de mercadoria ou prestação de serviço não especificada");
		save(session, cfop1949);

		cfop2949 = new CFOP();
		cfop2949.setCodigo("2949");
		cfop2949.setDescricao("Outra entrada de mercadoria ou prestação de serviço não especificado");
		save(session, cfop2949);

		cfop5115 = new CFOP();
		cfop5115.setCodigo("5115");
		cfop5115.setDescricao("Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignação mercantil");
		save(session, cfop5115);

		cfop6115 = new CFOP();
		cfop6115.setCodigo("6115");
		cfop6115.setDescricao("Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignação mercantil");
		save(session, cfop6115);

	}

	private static void gerarLogExecucaoInterfaces(Session session) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			LogExecucao logExecucao1 = new LogExecucao();
			logExecucao1.setDataInicio(sdf.parse("12/01/2012"));
			logExecucao1.setDataFim(sdf.parse("12/01/2012"));
			logExecucao1.setInterfaceExecucao(interfaceEMS0106);
			logExecucao1.setNomeLoginUsuario("Junior");
			logExecucao1.setStatus(StatusExecucaoEnum.SUCESSO);

			LogExecucao logExecucao2 = new LogExecucao();
			logExecucao2.setDataInicio(sdf.parse("20/06/2012"));
			logExecucao2.setInterfaceExecucao(interfaceEMS0106);
			logExecucao2.setNomeLoginUsuario("Carla");
			logExecucao2.setStatus(StatusExecucaoEnum.SUCESSO);

			LogExecucao logExecucao3 = new LogExecucao();
			logExecucao3.setDataInicio(sdf.parse("19/06/2012"));
			logExecucao3.setInterfaceExecucao(interfaceEMS0109);
			logExecucao3.setNomeLoginUsuario("Junior");
			logExecucao3.setStatus(StatusExecucaoEnum.SUCESSO);
			session.save(logExecucao3);

			LogExecucao logExecucao4 = new LogExecucao();
			logExecucao4.setDataInicio(sdf.parse("20/06/2012"));
			logExecucao4.setInterfaceExecucao(interfaceEMS0109);
			logExecucao4.setNomeLoginUsuario("Junior");
			logExecucao4.setStatus(StatusExecucaoEnum.FALHA);

			LogExecucaoMensagem logExecucaoMensagem1 = new LogExecucaoMensagem();
			logExecucaoMensagem1.setEventoExecucao(eventoRelacionamentoNaoEncontrado);
			logExecucaoMensagem1.setMensagem("Erro teste sobre relacionamento.");
			logExecucaoMensagem1.setMensagemInfo("ACAO=Insert;LOCAL=No Lançamento;DETALHE=Cota Endereço");
			logExecucaoMensagem1.setNomeArquivo("arquivoGenerico.gen");
			logExecucaoMensagem1.setNumeroLinha(3);
			logExecucaoMensagem1.setLogExecucao(logExecucao2);


			LogExecucaoMensagem logExecucaoMensagem2 = new LogExecucaoMensagem();
			logExecucaoMensagem2.setEventoExecucao(eventoErroInfraestrutura);
			logExecucaoMensagem2.setMensagem("Erro! Teste sobre infraestrutura.");
			logExecucaoMensagem2.setMensagemInfo("ACAO=Insert;LOCAL=No Lançamento;DETALHE=Cota Endereço");
			logExecucaoMensagem2.setNomeArquivo("arquivoGenerico2.gen");
			logExecucaoMensagem2.setNumeroLinha(5);
			logExecucaoMensagem2.setLogExecucao(logExecucao2);

			List<LogExecucaoMensagem> listLogExecucaoMensagem = new ArrayList<LogExecucaoMensagem>();
			listLogExecucaoMensagem.add(logExecucaoMensagem1);
			listLogExecucaoMensagem.add(logExecucaoMensagem2);

			logExecucao2.setListLogExecucaoMensagem(listLogExecucaoMensagem);

			session.save(logExecucao2);

			session.save(logExecucaoMensagem1);
			session.save(logExecucaoMensagem2);

			//=====

			LogExecucaoMensagem logExecucaoMensagem3 = new LogExecucaoMensagem();
			logExecucaoMensagem3.setEventoExecucao(eventoErroInfraestrutura);
			logExecucaoMensagem3.setMensagem("Erro! 111 Teste sobre infraestrutura.");
			logExecucaoMensagem3.setMensagemInfo("ACAO=Insert;LOCAL=No Lançamento;DETALHE=Cota Endereço");
			logExecucaoMensagem3.setNomeArquivo("arquivoGenerico1.aaa");
			logExecucaoMensagem3.setNumeroLinha(7);
			logExecucaoMensagem3.setLogExecucao(logExecucao4);

			LogExecucaoMensagem logExecucaoMensagem4 = new LogExecucaoMensagem();
			logExecucaoMensagem4.setEventoExecucao(eventoErroInfraestrutura);
			logExecucaoMensagem4.setMensagem("Erro! 222 Teste sobre infraestrutura.");
			logExecucaoMensagem4.setMensagemInfo("ACAO=Insert;LOCAL=No Lançamento;DETALHE=Cota Endereço");
			logExecucaoMensagem4.setNomeArquivo("arquivoGenerico2.bbb");
			logExecucaoMensagem4.setNumeroLinha(9);
			logExecucaoMensagem4.setLogExecucao(logExecucao4);

			LogExecucaoMensagem logExecucaoMensagem5 = new LogExecucaoMensagem();
			logExecucaoMensagem5.setEventoExecucao(eventoHierarquiaCorrompida);
			logExecucaoMensagem5.setMensagem("Erro! 333 Teste sobre infraestrutura.");
			logExecucaoMensagem5.setMensagemInfo("ACAO=Insert;LOCAL=No Lançamento;DETALHE=Cota Endereço");
			logExecucaoMensagem5.setNomeArquivo("arquivoGenerico2.ccc");
			logExecucaoMensagem5.setNumeroLinha(13);
			logExecucaoMensagem5.setLogExecucao(logExecucao4);

			List<LogExecucaoMensagem> listLogExecucaoMensagem2 = new ArrayList<LogExecucaoMensagem>();
			listLogExecucaoMensagem2.add(logExecucaoMensagem3);
			listLogExecucaoMensagem2.add(logExecucaoMensagem4);
			listLogExecucaoMensagem2.add(logExecucaoMensagem5);

			logExecucao4.setListLogExecucaoMensagem(listLogExecucaoMensagem2);

			session.save(logExecucao4);

			session.save(logExecucaoMensagem3);
			session.save(logExecucaoMensagem4);
			session.save(logExecucaoMensagem5);

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		//LogExecucao
		//LogMensagemExecucao

	}

	private static void gerarTiposNotas(Session session) {

		// Tipo de Nota - Mercantil - Cota Contribuinte
		TipoNotaFiscal nfeRemessaConsignacaoContribuinte = new TipoNotaFiscal();
		nfeRemessaConsignacaoContribuinte.setCfopEstado(cfop5917);
		nfeRemessaConsignacaoContribuinte.setCfopOutrosEstados(cfop6917);
		nfeRemessaConsignacaoContribuinte.setNopDescricao("NF-e de Remessa em Consignação (NECE / DANFE)");
		nfeRemessaConsignacaoContribuinte.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeRemessaConsignacaoContribuinte.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeRemessaConsignacaoContribuinte.setContribuinte(true);
		nfeRemessaConsignacaoContribuinte.setDescricao("NF-e de Remessa em Consignação (NECE / DANFE) - Cota Contribuinte");
		nfeRemessaConsignacaoContribuinte.setNopCodigo(0L);
		nfeRemessaConsignacaoContribuinte.setTipoOperacao(TipoOperacao.SAIDA);
		nfeRemessaConsignacaoContribuinte.setGrupoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO);
		nfeRemessaConsignacaoContribuinte.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeRemessaConsignacaoContribuinte.setSerieNotaFiscal(1);
		nfeRemessaConsignacaoContribuinte.setProcesso(new HashSet<Processo>());
		nfeRemessaConsignacaoContribuinte.getProcesso().add(Processo.GERACAO_NF_E);
		nfeRemessaConsignacaoContribuinte.getProcesso().add(Processo.LANCAMENTO_FALTA_SOBRA);
		nfeRemessaConsignacaoContribuinte.getProcesso().add(Processo.VENDA_SUPLEMENTAR);
		save(session,nfeRemessaConsignacaoContribuinte);

		TipoNotaFiscal nfeEntradaDevolucaoRemessaConsignacaoContribuinte = new TipoNotaFiscal();
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setCfopEstado(cfop1918);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setCfopOutrosEstados(cfop2918);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setNopDescricao("NF-e de Remessa em Devolução de Remessa em Consignação");
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setContribuinte(true);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setDescricao("NF-e de Remessa em Devolução de Remessa em Consignação - Cota Contribuinte");
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setNopCodigo(0L);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setTipoOperacao(TipoOperacao.ENTRADA);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setGrupoNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setSerieNotaFiscal(2);
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.setProcesso(new HashSet<Processo>());
		nfeEntradaDevolucaoRemessaConsignacaoContribuinte.getProcesso().add(Processo.FECHAMENTO_ENCALHE);
		save(session, nfeEntradaDevolucaoRemessaConsignacaoContribuinte);

		TipoNotaFiscal nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte = new TipoNotaFiscal();
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setCfopEstado(cfop1919);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setCfopOutrosEstados(cfop2919);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setNopDescricao("NF-e de Devolução Simbólica de Mercadorias Vendidas");
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setContribuinte(true);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setDescricao("NF-e de Devolução Simbólica de Mercadorias Vendidas - Cota Contribuinte");
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setNopCodigo(0L);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setTipoOperacao(TipoOperacao.ENTRADA);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setGrupoNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_SIMBOLICA);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setSerieNotaFiscal(3);
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.setProcesso(new HashSet<Processo>());
		nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte.getProcesso().add(Processo.GERACAO_NF_E);
		save(session, nfeDevolucaoSimbolicaMercadoriasVendidasContribuinte);

		TipoNotaFiscal nfeVendasContribuinte = new TipoNotaFiscal();
		nfeVendasContribuinte.setCfopEstado(cfop5114);
		nfeVendasContribuinte.setCfopOutrosEstados(cfop6114);
		nfeVendasContribuinte.setNopDescricao("NF-e Venda");
		nfeVendasContribuinte.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeVendasContribuinte.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeVendasContribuinte.setContribuinte(true);
		nfeVendasContribuinte.setDescricao("NF-e Venda - Cota Contribuinte");
		nfeVendasContribuinte.setNopCodigo(0L);
		nfeVendasContribuinte.setTipoOperacao(TipoOperacao.SAIDA);
		nfeVendasContribuinte.setGrupoNotaFiscal(GrupoNotaFiscal.NF_VENDA);
		nfeVendasContribuinte.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeVendasContribuinte.setSerieNotaFiscal(4);
		nfeVendasContribuinte.setProcesso(new HashSet<Processo>());
		nfeVendasContribuinte.getProcesso().add(Processo.GERACAO_NF_E);
		save(session, nfeVendasContribuinte);

		// Tipo de Nota - Mercantil - Cota não Contribuinte (cota que não emite NF-e)
		TipoNotaFiscal nfeRemessaConsignacao = new TipoNotaFiscal();
		nfeRemessaConsignacao.setCfopEstado(cfop5917);
		nfeRemessaConsignacao.setCfopOutrosEstados(cfop6917);
		nfeRemessaConsignacao.setNopDescricao("NF-e de Remessa em Consignação (NECE / DANFE)");
		nfeRemessaConsignacao.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeRemessaConsignacao.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeRemessaConsignacao.setContribuinte(false);
		nfeRemessaConsignacao.setDescricao("NF-e de Remessa em Consignação (NECE / DANFE) - Cota NÃO Contribuinte");
		nfeRemessaConsignacao.setNopCodigo(0L);
		nfeRemessaConsignacao.setTipoOperacao(TipoOperacao.SAIDA);
		nfeRemessaConsignacao.setGrupoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO);
		nfeRemessaConsignacao.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeRemessaConsignacao.setSerieNotaFiscal(5);
		nfeRemessaConsignacao.setProcesso(new HashSet<Processo>());
		nfeRemessaConsignacao.getProcesso().add(Processo.GERACAO_NF_E);
		nfeRemessaConsignacao.getProcesso().add(Processo.LANCAMENTO_FALTA_SOBRA);
		nfeRemessaConsignacao.getProcesso().add(Processo.VENDA_SUPLEMENTAR);
		save(session,nfeRemessaConsignacao);

		TipoNotaFiscal nfeEntradaDevolucaoRemessaConsignacao = new TipoNotaFiscal();
		nfeEntradaDevolucaoRemessaConsignacao.setCfopEstado(cfop1918);
		nfeEntradaDevolucaoRemessaConsignacao.setCfopOutrosEstados(cfop2918);
		nfeEntradaDevolucaoRemessaConsignacao.setNopDescricao("NF-e de Remessa em Devolução de Remessa em Consignação");
		nfeEntradaDevolucaoRemessaConsignacao.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeEntradaDevolucaoRemessaConsignacao.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeEntradaDevolucaoRemessaConsignacao.setContribuinte(false);
		nfeEntradaDevolucaoRemessaConsignacao.setDescricao("NF-e de Remessa em Devolução de Remessa em Consignação - Cota NÃO Contribuinte");
		nfeEntradaDevolucaoRemessaConsignacao.setNopCodigo(0L);
		nfeEntradaDevolucaoRemessaConsignacao.setTipoOperacao(TipoOperacao.ENTRADA);
		nfeEntradaDevolucaoRemessaConsignacao.setGrupoNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO);
		nfeEntradaDevolucaoRemessaConsignacao.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeEntradaDevolucaoRemessaConsignacao.setSerieNotaFiscal(6);
		nfeEntradaDevolucaoRemessaConsignacao.setProcesso(new HashSet<Processo>());
		nfeEntradaDevolucaoRemessaConsignacao.getProcesso().add(Processo.FECHAMENTO_ENCALHE);
		save(session, nfeEntradaDevolucaoRemessaConsignacao);

		TipoNotaFiscal nfeDevolucaoSimbolicaMercadoriasVendidas = new TipoNotaFiscal();
		nfeDevolucaoSimbolicaMercadoriasVendidas.setCfopEstado(cfop1919);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setCfopOutrosEstados(cfop2919);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setNopDescricao("NF-e de Devolução Simbólica de Mercadorias Vendidas");
		nfeDevolucaoSimbolicaMercadoriasVendidas.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setContribuinte(false);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setDescricao("NF-e de Devolução Simbólica de Mercadorias Vendidas - Cota NÃO Contribuinte");
		nfeDevolucaoSimbolicaMercadoriasVendidas.setNopCodigo(0L);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setTipoOperacao(TipoOperacao.ENTRADA);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setGrupoNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_SIMBOLICA);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setSerieNotaFiscal(7);
		nfeDevolucaoSimbolicaMercadoriasVendidas.setProcesso(new HashSet<Processo>());
		nfeDevolucaoSimbolicaMercadoriasVendidas.getProcesso().add(Processo.GERACAO_NF_E);
		save(session, nfeDevolucaoSimbolicaMercadoriasVendidas);

		TipoNotaFiscal nfeVendas = new TipoNotaFiscal();
		nfeVendas.setCfopEstado(cfop5114);
		nfeVendas.setCfopOutrosEstados(cfop6114);
		nfeVendas.setNopDescricao("NF-e Venda");
		nfeVendas.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeVendas.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeVendas.setContribuinte(false);
		nfeVendas.setDescricao("NF-e Venda - Cota NÃO Contribuinte");
		nfeVendas.setNopCodigo(0L);
		nfeVendas.setTipoOperacao(TipoOperacao.SAIDA);
		nfeVendas.setGrupoNotaFiscal(GrupoNotaFiscal.NF_VENDA);
		nfeVendas.setTipoAtividade(TipoAtividade.MERCANTIL);
		nfeVendas.setSerieNotaFiscal(8);
		nfeVendas.setProcesso(new HashSet<Processo>());
		nfeVendas.getProcesso().add(Processo.GERACAO_NF_E);
		save(session, nfeVendas);

		// Tipo de Nota - Prestador de Serviço - Cota Contribuinte
		TipoNotaFiscal nfeRemessaDistribuicao1 = new TipoNotaFiscal();
		nfeRemessaDistribuicao1.setCfopEstado(cfop5949);
		nfeRemessaDistribuicao1.setCfopOutrosEstados(cfop6949);
		nfeRemessaDistribuicao1.setNopDescricao("NF-e de Remessa para Distribuição (NECA / DANFE)");
		nfeRemessaDistribuicao1.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeRemessaDistribuicao1.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeRemessaDistribuicao1.setContribuinte(true);
		nfeRemessaDistribuicao1.setDescricao("NF-e de Remessa para Distribuição (NECA / DANFE) - Cota Contribuinte");
		nfeRemessaDistribuicao1.setNopCodigo(0L);
		nfeRemessaDistribuicao1.setTipoOperacao(TipoOperacao.SAIDA);
		nfeRemessaDistribuicao1.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeRemessaDistribuicao1.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeRemessaDistribuicao1.setSerieNotaFiscal(9);
		nfeRemessaDistribuicao1.setProcesso(new HashSet<Processo>());
		nfeRemessaDistribuicao1.getProcesso().add(Processo.GERACAO_NF_E);
		nfeRemessaDistribuicao1.getProcesso().add(Processo.VENDA_SUPLEMENTAR);
		save(session, nfeRemessaDistribuicao1);

		TipoNotaFiscal nfeDevolucaoRemessaDistribuicao1 = new TipoNotaFiscal();
		nfeDevolucaoRemessaDistribuicao1.setCfopEstado(cfop5949);
		nfeDevolucaoRemessaDistribuicao1.setCfopOutrosEstados(cfop6949);
		nfeDevolucaoRemessaDistribuicao1.setNopDescricao("NF-e de Devolução de Remessa para Distribuição");
		nfeDevolucaoRemessaDistribuicao1.setEmitente(TipoUsuarioNotaFiscal.COTA);
		nfeDevolucaoRemessaDistribuicao1.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoRemessaDistribuicao1.setContribuinte(true);
		nfeDevolucaoRemessaDistribuicao1.setDescricao("NF-e de Devolução de Remessa para Distribuição - Cota Contribuinte");
		nfeDevolucaoRemessaDistribuicao1.setNopCodigo(0L);
		nfeDevolucaoRemessaDistribuicao1.setTipoOperacao(TipoOperacao.SAIDA);
		nfeDevolucaoRemessaDistribuicao1.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeDevolucaoRemessaDistribuicao1.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeDevolucaoRemessaDistribuicao1.setSerieNotaFiscal(10);
		save(session, nfeDevolucaoRemessaDistribuicao1);

		TipoNotaFiscal nfeVenda1 = new TipoNotaFiscal();
		nfeVenda1.setCfopEstado(cfop5115);
		nfeVenda1.setCfopOutrosEstados(cfop6115);
		nfeVenda1.setNopDescricao("NF-e Venda");
		nfeVenda1.setEmitente(TipoUsuarioNotaFiscal.TREELOG);
		nfeVenda1.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeVenda1.setContribuinte(true);
		nfeVenda1.setDescricao("NF-e Venda - Cota Contribuinte");
		nfeVenda1.setNopCodigo(0L);
		nfeVenda1.setTipoOperacao(TipoOperacao.SAIDA);
		nfeVenda1.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeVenda1.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeVenda1.setSerieNotaFiscal(11);
		save(session, nfeVenda1);

		// Tipo de Nota - Prestarore de Serviço - Cota NÃO Contribuinte
		TipoNotaFiscal nfeRemessaDistribuicao2 = new TipoNotaFiscal();
		nfeRemessaDistribuicao2.setCfopEstado(cfop5949);
		nfeRemessaDistribuicao2.setCfopOutrosEstados(cfop6949);
		nfeRemessaDistribuicao2.setNopDescricao("NF-e de Remessa para Distribuição (NECA / DANFE)");
		nfeRemessaDistribuicao2.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeRemessaDistribuicao2.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeRemessaDistribuicao2.setContribuinte(false);
		nfeRemessaDistribuicao2.setDescricao("NF-e de Remessa para Distribuição (NECA / DANFE) - Cota NÃO Contribuinte");
		nfeRemessaDistribuicao2.setNopCodigo(0L);
		nfeRemessaDistribuicao2.setTipoOperacao(TipoOperacao.SAIDA);
		nfeRemessaDistribuicao2.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeRemessaDistribuicao2.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeRemessaDistribuicao2.setSerieNotaFiscal(12);
		nfeRemessaDistribuicao2.setProcesso(new HashSet<Processo>());
		nfeRemessaDistribuicao2.getProcesso().add(Processo.GERACAO_NF_E);
		nfeRemessaDistribuicao2.getProcesso().add(Processo.VENDA_SUPLEMENTAR);
		save(session, nfeRemessaDistribuicao2);

		TipoNotaFiscal nfeDevolucaoRemessaDistribuicao2 = new TipoNotaFiscal();
		nfeDevolucaoRemessaDistribuicao2.setCfopEstado(cfop1949);
		nfeDevolucaoRemessaDistribuicao2.setCfopOutrosEstados(cfop2949);
		nfeDevolucaoRemessaDistribuicao2.setNopDescricao("NF-e de Devolução de Remessa para Distruibuição");
		nfeDevolucaoRemessaDistribuicao2.setEmitente(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoRemessaDistribuicao2.setDestinatario(TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		nfeDevolucaoRemessaDistribuicao2.setContribuinte(false);
		nfeDevolucaoRemessaDistribuicao2.setDescricao("NF-e de Devolução de Remessa para Distruibuição - Cota NÃO Contribuinte");
		nfeDevolucaoRemessaDistribuicao2.setNopCodigo(0L);
		nfeDevolucaoRemessaDistribuicao2.setTipoOperacao(TipoOperacao.ENTRADA);
		nfeDevolucaoRemessaDistribuicao2.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeDevolucaoRemessaDistribuicao2.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeDevolucaoRemessaDistribuicao2.setSerieNotaFiscal(13);
		nfeDevolucaoRemessaDistribuicao2.setProcesso(new HashSet<Processo>());
		nfeDevolucaoRemessaDistribuicao2.getProcesso().add(Processo.FECHAMENTO_ENCALHE);
		save(session, nfeDevolucaoRemessaDistribuicao2);

		TipoNotaFiscal nfeVenda2 = new TipoNotaFiscal();
		nfeVenda2.setCfopEstado(cfop5115);
		nfeVenda2.setCfopOutrosEstados(cfop6115);
		nfeVenda2.setNopDescricao("NF-e Venda");
		nfeVenda2.setEmitente(TipoUsuarioNotaFiscal.TREELOG);
		nfeVenda2.setDestinatario(TipoUsuarioNotaFiscal.COTA);
		nfeVenda2.setContribuinte(false);
		nfeVenda2.setDescricao("NF-e Venda - Cota NÃO Contribuinte");
		nfeVenda2.setNopCodigo(0L);
		nfeVenda2.setTipoOperacao(TipoOperacao.SAIDA);
		nfeVenda2.setGrupoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		nfeVenda2.setTipoAtividade(TipoAtividade.PRESTADOR_SERVICO);
		nfeVenda2.setSerieNotaFiscal(14);
		save(session, nfeVenda2);
	}

	private static void gerarParciais(Session session) {

		Produto guiaQuatroRodas = Fixture.produto("3111", "Guia Quatro Rodas", "Guia Quatro Rodas", PeriodicidadeProduto.ANUAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		guiaQuatroRodas.addFornecedor(fornecedorDinap);

		Produto cromoBrasileirao = Fixture.produto("3333", "Cromo Brasileirão", "Cromo Brasileirão", PeriodicidadeProduto.ANUAL, tipoProdutoCromo, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		cromoBrasileirao.addFornecedor(fornecedorFc);

		Produto guiaViagem = Fixture.produto("3113", "Guia Viagem", "Guia Viagem", PeriodicidadeProduto.ANUAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		guiaViagem.addFornecedor(fornecedorDinap);

		save(session,guiaQuatroRodas,cromoBrasileirao,guiaViagem);

		ProdutoEdicao guiaQuatroRodasEd1 = Fixture.produtoEdicao("COD_EE", 1L, 5, 30,
				new Long(50), new BigDecimal(100), new BigDecimal(100), "5546", 0L, guiaQuatroRodas, null, false, "Guia Quatro Rodas");
		guiaQuatroRodasEd1.setParcial(true);

		ProdutoEdicao cromoBrasileiraoEd1 = Fixture.produtoEdicao("COD_FF", 1L, 5, 30,
				new Long(50), new BigDecimal(100), new BigDecimal(100), "3333", 0L, cromoBrasileirao, null, false,"Cromo Brasileirão");
		cromoBrasileiraoEd1.setParcial(true);
		cromoBrasileiraoEd1.setOrigemInterface(false);
		
		ProdutoEdicao guiaViagemEd1 = Fixture.produtoEdicao("COD_GG", 1L, 5, 30,
				new Long(50), new BigDecimal(100), new BigDecimal(100), "2231", 0L, guiaViagem, null, false,"Guia Viagem");
		guiaViagemEd1.setParcial(true);

		save(session,guiaQuatroRodasEd1,cromoBrasileiraoEd1,guiaViagemEd1);


		LancamentoParcial lancamentoParcial1 = Fixture.criarLancamentoParcial(
				guiaQuatroRodasEd1, Fixture.criarData(1, 1, 2009), Fixture.criarData(1, 1, 2010), StatusLancamentoParcial.RECOLHIDO);

		LancamentoParcial lancamentoParcial2 = Fixture.criarLancamentoParcial(
				cromoBrasileiraoEd1, Fixture.criarData(1, 2, 2011), Fixture.criarData(1, 2, 2012), StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcial3 = Fixture.criarLancamentoParcial(
				guiaViagemEd1, Fixture.criarData(1, 3, 2011), Fixture.criarData(1, 3, 2012), StatusLancamentoParcial.PROJETADO);


		save(session, lancamentoParcial1,lancamentoParcial2,lancamentoParcial3);

		Lancamento lancamentoPeriodo = Fixture.lancamento(TipoLancamento.PARCIAL, cromoBrasileiraoEd1,
				Fixture.criarData(1, 2, 2011),
				Fixture.criarData(1, 3, 2011),
				new Date(),
				new Date(),
				BigInteger.valueOf(50),
				StatusLancamento.RECOLHIDO, null, 1);
		save(session,lancamentoPeriodo);
		
		Lancamento lancamentoPeriodo2 = Fixture.lancamento(TipoLancamento.PARCIAL, cromoBrasileiraoEd1,
				Fixture.criarData(5, 3, 2011),
				Fixture.criarData(5, 4, 2011),
				new Date(),
				new Date(),
				BigInteger.valueOf(80),
				StatusLancamento.PLANEJADO, null, 1);
		save(session,lancamentoPeriodo2);
		
		Lancamento lancamentoPeriodoSuplementar = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, cromoBrasileiraoEd1,
				Fixture.criarData(2, 2, 2011),
				Fixture.criarData(1, 3, 2011),
				new Date(),
				new Date(),
				BigInteger.valueOf(15),
				StatusLancamento.RECOLHIDO, null, 1);
		save(session,lancamentoPeriodoSuplementar);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(200), Fixture.criarData(1, 2, 2011), cromoBrasileiraoEd1);
		save(session,estudo);


		EstoqueProdutoCota estoque = Fixture.estoqueProdutoCota(cromoBrasileiraoEd1, BigInteger.valueOf(50), cotaGuilherme, null);
		save(session,estoque);
		
		MovimentoEstoqueCota movimento = Fixture.movimentoEstoqueCota(cromoBrasileiraoEd1, tipoMovimentoEnvioEncalhe,
				usuarioJoao, estoque, BigInteger.valueOf(10), cotaGuilherme, StatusAprovacao.APROVADO, "motivo");
		
		MovimentoEstoqueCota movimentoReparte = Fixture.movimentoEstoqueCota(cromoBrasileiraoEd1, tipoMovimentoRecReparte,
				usuarioJoao, estoque, BigInteger.valueOf(25), cotaGuilherme, StatusAprovacao.APROVADO, "motivo");
		movimentoReparte.setLancamento(lancamentoPeriodo);
		save(session,movimentoReparte);
		
		MovimentoEstoqueCota movimentoReparteSuplementar = Fixture.movimentoEstoqueCota(cromoBrasileiraoEd1, tipoMovimentoRecReparte,
				usuarioJoao, estoque, BigInteger.valueOf(15), cotaGuilherme, StatusAprovacao.APROVADO, "motivo");
		movimentoReparteSuplementar.setLancamento(lancamentoPeriodoSuplementar);
		save(session,movimentoReparteSuplementar);
		
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(Fixture.criarData(1, 3, 2011), cromoBrasileiraoEd1,TipoChamadaEncalhe.ANTECIPADA);

		chamadaEncalhe.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalhe.getLancamentos().add(lancamentoPeriodo);
		
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(chamadaEncalhe, true, cotaGuilherme, BigInteger.valueOf(50));

		ControleConferenciaEncalhe controle = Fixture.controleConferenciaEncalhe(StatusOperacao.CONCLUIDO, new Date());

		ControleConferenciaEncalheCota controleCota = Fixture.controleConferenciaEncalheCota(controle, cotaGuilherme,
				new Date(), new Date(), new Date(), StatusOperacao.CONCLUIDO, usuarioJoao, box1);

		ConferenciaEncalhe conferencia = Fixture.conferenciaEncalhe(
				movimento, chamadaEncalheCota,
				controleCota,
				new Date(),
				BigInteger.valueOf(50),
				BigInteger.valueOf(50),
				cromoBrasileiraoEd1);

		save(session,movimento,chamadaEncalhe,chamadaEncalheCota,controle,controleCota,conferencia);
		
		save(session, lancamentoPeriodo, lancamentoPeriodo2);
		
		PeriodoLancamentoParcial periodo = Fixture.criarPeriodoLancamentoParcial(lancamentoPeriodo, lancamentoParcial2,
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);
		save(session, periodo);

		PeriodoLancamentoParcial periodo2 = Fixture.criarPeriodoLancamentoParcial(lancamentoPeriodo2, lancamentoParcial2,
				StatusLancamentoParcial.RECOLHIDO, TipoLancamentoParcial.PARCIAL);
		save(session, periodo2);

	}

	private static void gerarTipoEntrega(Session session) {

		tipoCotaRetira = Fixture.criarTipoEntrega(1L,DescricaoTipoEntrega.COTA_RETIRA, Periodicidade.DIARIO);
		tipoEntregaEmBanca = Fixture.criarTipoEntrega(1L,DescricaoTipoEntrega.ENTREGA_EM_BANCA, Periodicidade.DIARIO);
		tipoEntregador = Fixture.criarTipoEntrega(1L,DescricaoTipoEntrega.ENTREGADOR, Periodicidade.DIARIO);

		save(session,tipoCotaRetira,tipoEntregaEmBanca,tipoEntregador);
	}

	private static void gerarTipoLicencaMunicipalPDV(Session session) {

		TipoLicencaMunicipal tipoLicencaMunicipal = Fixture.criarTipoLicencaMunicipal(10L, "Licença 1");
		TipoLicencaMunicipal tipoLicencaMunicipal1 = Fixture.criarTipoLicencaMunicipal(11L, "Licença 2");
		TipoLicencaMunicipal tipoLicencaMunicipal2 = Fixture.criarTipoLicencaMunicipal(12L, "Licença 3");
		TipoLicencaMunicipal tipoLicencaMunicipal3 = Fixture.criarTipoLicencaMunicipal(13L, "Licença 4");

		save(session,tipoLicencaMunicipal,tipoLicencaMunicipal1,tipoLicencaMunicipal2,tipoLicencaMunicipal3);
	}

	private static void gerarTipoEstabelecimentoAssociacaoPDV(Session session) {

		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV = Fixture.criarTipoEstabelecimentoAssociacaoPDV(10L, "Galeria");
		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV1 = Fixture.criarTipoEstabelecimentoAssociacaoPDV(11L, "Hiper / Supermercado");
		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV2 = Fixture.criarTipoEstabelecimentoAssociacaoPDV(12L, "Shopping");
		TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV3 = Fixture.criarTipoEstabelecimentoAssociacaoPDV(13L, "Posto Serviço");

		save(session,tipoEstabelecimentoAssociacaoPDV,tipoEstabelecimentoAssociacaoPDV1,tipoEstabelecimentoAssociacaoPDV2,tipoEstabelecimentoAssociacaoPDV3);
	}

	private static void gerarMaterialPromocionalPDV(Session session) {

		MaterialPromocional materialPromocional = Fixture.criarMaterialPromocional(1L, "Adesivo");
		MaterialPromocional materialPromocional1 = Fixture.criarMaterialPromocional(2L, "Brindes");
		MaterialPromocional materialPromocional2 = Fixture.criarMaterialPromocional(3L, "Poster");
		MaterialPromocional materialPromocional3 = Fixture.criarMaterialPromocional(4L, "Bandeirola");
		MaterialPromocional materialPromocional4 = Fixture.criarMaterialPromocional(5L, "Sapataria");
		MaterialPromocional materialPromocional5 = Fixture.criarMaterialPromocional(6L, "Cartaz Grande");

		save(session,materialPromocional,materialPromocional1,materialPromocional2,materialPromocional3,materialPromocional4,materialPromocional5);
	}

	private static void gerarTipoGeradorFluxoPDV(Session session) {

		TipoGeradorFluxoPDV tipoGeradorFluxoPDV = Fixture.criarTipoGeradorFluxoPDV(1L, "Cursinho");
		TipoGeradorFluxoPDV tipoGeradorFluxoPDV1 = Fixture.criarTipoGeradorFluxoPDV(2L, "Superior");
		TipoGeradorFluxoPDV tipoGeradorFluxoPDV2 = Fixture.criarTipoGeradorFluxoPDV(3L, "Padarias");
		TipoGeradorFluxoPDV tipoGeradorFluxoPDV3 = Fixture.criarTipoGeradorFluxoPDV(4L, "Restaurantes");
		TipoGeradorFluxoPDV tipoGeradorFluxoPDV4 = Fixture.criarTipoGeradorFluxoPDV(5L, "Pre-Escola");

		save(session,tipoGeradorFluxoPDV,tipoGeradorFluxoPDV1,tipoGeradorFluxoPDV2,tipoGeradorFluxoPDV3,tipoGeradorFluxoPDV4);
	}

	private static void gerarAreaInfluenciaPDV(Session session) {

		AreaInfluenciaPDV areaInfluenciaPDV = Fixture.criarAreaInfluenciaPDV(1L, "Residencial");
		AreaInfluenciaPDV areaInfluenciaPDV1 = Fixture.criarAreaInfluenciaPDV(2L, "Residencial XX");
		AreaInfluenciaPDV areaInfluenciaPDV2 = Fixture.criarAreaInfluenciaPDV(3L, "Estradas");

		save(session,areaInfluenciaPDV,areaInfluenciaPDV1,areaInfluenciaPDV2);
	}


	private static void gerarTiposPontoPDV(Session session) {

		 tipoPontoPDVBanca  = Fixture.criarTipoPontoPDV(1L, "Banca");
		 tipoPontoPDVRevistaria  = Fixture.criarTipoPontoPDV(2L, "Revistaria");
		 tipoPontoPDVLivraria  = Fixture.criarTipoPontoPDV(3L, "Livraria");
		 tipoPontoPDVEtc = Fixture.criarTipoPontoPDV(4L, "Outros");
		 
		save(session, tipoPontoPDVBanca, tipoPontoPDVRevistaria, tipoPontoPDVLivraria, tipoPontoPDVEtc);
	}

	private static void gerarHistoricosAculoDivida(Session session) {

		acumDividaGuilherme1 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaGuilherme1,
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.ATIVA);

		acumDividaGuilherme2 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaGuilherme2,
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.QUITADA);

		acumDividaMariana1 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaMariana1,
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.QUITADA);

		acumDividaMurilo1 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaMurilo1,
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.QUITADA);

		save(session, acumDividaGuilherme1,acumDividaGuilherme2,acumDividaMariana1,acumDividaMurilo1);
	}

	private static void tabelaNCM(Session session){
		
		
		ncmRevista = Fixture.ncm(49029000l,"REVISTAS","KG");
		ncmFasciculo = Fixture.ncm(49019100l,"FASCICULO","KG");
		ncmLivro = Fixture.ncm(49019000l, "LIVROS","KG");
		ncmFigurinha = Fixture.ncm(48205000l,"CROMO","KG");		
		ncmLivroilustrado = Fixture.ncm(49030000l,"LIVRO ILUSTRADO","KG");		
		ncmCd = Fixture.ncm(85243100l,"CD","KG");		
		ncmCartaz = Fixture.ncm(49111090l,"CARTAZ","KG");						
		ncmBebidas = Fixture.ncm(22029000l,"OUTRAS BEBIDAS","L");
		
		save(session, ncmRevista, ncmFasciculo, ncmLivro, ncmFigurinha, ncmLivroilustrado, ncmCd, ncmCartaz, ncmBebidas);
	}
	
	private static void criarDadosContaCorrenteConsigando(Session session){

		Date dataAtual = new Date();
		MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = new ArrayList<MovimentoEstoqueCota>();

		//Editor abril = Fixture.editoraAbril();
		//save(session, abril);

		//PessoaFisica manoel = Fixture.pessoaFisica("122.456.789-22",
			//	"manoel@mail.com", "Manoel da Silva");
		//save(session, manoel);

		//Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		//save(session,box1);


		TipoMovimentoEstoque tipoMovimentoEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(session,tipoMovimentoEncalhe);

		TipoMovimentoEstoque tipoMovimentoVenda = Fixture.tipoMovimentoVendaEncalhe();
		save(session,tipoMovimentoVenda);

		TipoMovimentoEstoque tipoMovimentoConsignado = Fixture.tipoMovimentoEnvioJornaleiro();
		save(session,tipoMovimentoConsignado);

		Usuario usuario = Fixture.usuarioJoao();
		save(session,usuario);

		TipoProduto tipoProduto = Fixture.tipoProduto("Revista C.C.Consignado", GrupoProduto.REVISTA, ncmRevista, "473794321", 003L);
		save(session,tipoProduto);

		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedor);

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedorPublicacao);

		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop5102, fornecedorFc, tipoNotaFiscalRecebimento, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
		save(session,notaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(session,recebimentoFisico);

		ItemNotaFiscalEntrada itemNotaFiscal=
				Fixture.itemNotaFiscal(
						produtoEdicaoBravo1,
						usuario,
						notaFiscal,
						new Date(),
						new Date(),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(12));
		save(session,itemNotaFiscal);

		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(12));
		save(session,itemRecebimentoFisico);

		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicaoBravo1, dataAtual, dataAtual, dataAtual, dataAtual, BigInteger.valueOf(30), StatusLancamento.ESTUDO_FECHADO, itemRecebimentoFisico, 1);
		save(session,lancamento);

		Estudo estudo = Fixture.estudo(BigInteger.TEN, dataAtual, produtoEdicaoBravo1);
		save(session,estudo);

		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(20), BigInteger.valueOf(10), estudo, cotaManoel);
		save(session,estudoCota);

		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(session,expedicao);

		EstoqueProdutoCota  estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicaoBravo1,BigInteger.valueOf(30), cotaManoel, listaMovimentoEstoqueCota);
		save(session,estoqueProdutoCota);

		TipoMovimentoEstoque tipoMovimentoEstoque = Fixture.tipoMovimentoSobraDe();
		save(session,tipoMovimentoEstoque);

		MovimentoEstoqueCota movimentoEstoqueCota = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoEstoque, usuario, estoqueProdutoCota, BigInteger.valueOf(23), cotaManoel, StatusAprovacao.APROVADO, "MOTIVO A");
		movimentoEstoqueCota.setEstudoCota(estudoCota);
		save(session,movimentoEstoqueCota);

		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicaoBravo1, BigInteger.valueOf(45));
		save(session,estoqueProduto);

		MovimentoEstoque movimentoEstoque = Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoBravo1, tipoMovimentoEstoque, usuario, estoqueProduto, dataAtual, BigInteger.valueOf(12), StatusAprovacao.APROVADO , "MOTIVO B");
		save(session,movimentoEstoque);

		Diferenca diferenca = Fixture.diferenca(BigInteger.valueOf(32), usuario, produtoEdicaoBravo1, TipoDiferenca.FALTA_DE, StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, true, TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.ESTOQUE, new Date());
		LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
		
		lancamentoDiferenca.setMovimentoEstoque(movimentoEstoque);

		diferenca.setLancamentoDiferenca(lancamentoDiferenca);
		save(session,lancamentoDiferenca,diferenca);

		RateioDiferenca rateioDiferenca = Fixture.rateioDiferenca(BigInteger.TEN , cotaManoel, diferenca, estudoCota, new Date());

		save(session,rateioDiferenca);

		movimento = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoConsignado, usuario, estoqueProdutoCota, BigInteger.valueOf(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		movimento.setEstudoCota(estudoCota);


		estoqueProdutoCota.getMovimentos().add(movimento);
		save(session,movimento);
		MovimentoEstoqueCota  movimentoEnvioJornaleiro = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoConsignado, usuario, estoqueProdutoCota, BigInteger.valueOf(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		movimentoEnvioJornaleiro.setEstudoCota(estudoCota);
		save(session,movimentoEnvioJornaleiro);

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		save(session,tipoMovimentoFinanceiro);

		listaMovimentoEstoqueCota.add(movimentoEstoqueCota);

		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario,
				new BigDecimal(230), listaMovimentoEstoqueCota,StatusAprovacao.APROVADO, dataAtual, true);
		save(session,movimentoFinanceiroCota);


		TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompra = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		save(session,tipoMovimentoFinanceiroCompra);

		List<MovimentoEstoqueCota>  listMovimentoEstoqueCotaVenda= new ArrayList<MovimentoEstoqueCota>();
		listMovimentoEstoqueCotaVenda.add(movimentoEnvioJornaleiro);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
 		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);

		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));
		save(session,consolidadoFinanceiroCota);


	}

	private static void massaDadosContaCorrenteTipoMovimento(Session session){

		Date dataAtual = new Date();
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = new ArrayList<MovimentoEstoqueCota>();

		PessoaFisica manoel = Fixture.pessoaFisica("12345678900",
				"manoel@mail.com", "Manoel da Silva");
				save(session, manoel);

		save(session, box1);

		Usuario usuario = Fixture.usuarioJoao();
		save(session, usuario);

		TipoProduto tipoProduto = Fixture.tipoProduto("Revista C.C.Movimento", GrupoProduto.REVISTA, ncmRevista, "513543", 004L);
		save(session, tipoProduto);

		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("Tipo A",GrupoFornecedor.PUBLICACAO);
		save(session, tipoFornecedor);


		produtoBravo.addFornecedor(fornecedorAcme);
		save(session, produtoBravo);

		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao("COD_HH", 234L,12 , 1, new Long(9), new BigDecimal(8),
				new BigDecimal(10), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoBravo, null, false, "Produto Bravo");
		save(session, produtoEdicao);

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session, tipoFornecedorPublicacao);

		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop5102, fornecedorAcme, tipoNotaFiscalRecebimento, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
		save(session, notaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(session, recebimentoFisico);

		ItemNotaFiscalEntrada itemNotaFiscal=
				Fixture.itemNotaFiscal(
						produtoEdicao,
						usuario,
						notaFiscal,
						new Date(),
						new Date(),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(12));
		save(session, itemNotaFiscal);

		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(12));
		save(session, itemRecebimentoFisico);

		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataAtual, dataAtual, dataAtual, dataAtual, BigInteger.valueOf(30), StatusLancamento.ESTUDO_FECHADO, itemRecebimentoFisico, 1);
		save(session, lancamento);

		Estudo estudo = Fixture.estudo(BigInteger.TEN, dataAtual, produtoEdicao);
		save(session, estudo);

		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(30), BigInteger.valueOf(30), estudo, cotaManoel);
		save(session, estudoCota);

		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(session, expedicao);

		EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
		estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao,BigInteger.valueOf(30), cotaManoel, listaMovimentoEstoqueCota);
		save(session, estoqueProdutoCota);




		massaDadosContaCorrenteMovimento(session, tipoMovimentoVendaEncalhe,
				tipoMovimentoFinanceiroCompraEncalhe, dataAtual, listaMovimentoEstoqueCota,
				usuario, produtoEdicao, estoqueProdutoCota);

    }

	private static void massaDadosContaCorrenteMovimento(Session session,
														 TipoMovimentoEstoque tipoMovimento,
														 TipoMovimentoFinanceiro tipoMovimentoFinanceiro, Date dataAtual,
														 List<MovimentoEstoqueCota> listaMovimentoEstoqueCota,
														 Usuario usuario, ProdutoEdicao produtoEdicao,
														 EstoqueProdutoCota estoqueProdutoCota) {

		MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, BigInteger.valueOf(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimento);

		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario,
				new BigDecimal(230), listaMovimentoEstoqueCota, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCota);


		MovimentoEstoqueCota movimentoVendaEncalhe  = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, BigInteger.valueOf(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimentoVendaEncalhe);

		List<MovimentoEstoqueCota> listMovimentoEstoqueCotas = new ArrayList<MovimentoEstoqueCota>();

		listMovimentoEstoqueCotas.add(movimentoVendaEncalhe);
		MovimentoFinanceiroCota movimentoFinanceiroCompraEncalhe= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario,
				new BigDecimal(230), listMovimentoEstoqueCotas, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCompraEncalhe);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCompraEncalhe);

		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));
		save(session, consolidadoFinanceiroCota);
	}

	private static void massaConferenciaParaMovimentoEstoqueCota(Session session) {
		
		chamadaEncalheVeja1 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoVeja1,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		chamadaEncalheVeja1.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalheVeja1.getLancamentos().add(lancamentoVeja1);
		
		save(session, chamadaEncalheVeja1);
		
		save(session, lancamentoVeja1);
				
		controleConferenciaEncalheVeja1 =
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(session, controleConferenciaEncalheVeja1);
		
		controleConferenciaEncalheCotaVeja1 = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalheVeja1,
				cotaJose,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				StatusOperacao.CONCLUIDO,
				usuarioJoao,
				box1);

		save(session, controleConferenciaEncalheCotaVeja1);
		
		chamadaEncalheCotaVeja1CotaJose = Fixture.chamadaEncalheCota(
				chamadaEncalheVeja1,
				false,
				cotaJose,
				BigInteger.TEN);
		save(session, chamadaEncalheCotaVeja1CotaJose);				
		
	}
	

	private static void criarControleBaixaBancaria(Session session) {
		baixaBancaria = Fixture.controleBaixaBancaria(new Date(), StatusControle.CONCLUIDO_SUCESSO, usuarioJoao, bancoHSBC);
		save(session, baixaBancaria);

	}

	private static void criarEstoqueProdutoCota(Session session) {

		estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaGuilherme, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaVeja1);

		estoqueProdutoCotaVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaGuilherme, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaVeja2);

		estoqueProdutoCotaVeja3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja3, cotaGuilherme, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaVeja3);

		estoqueProdutoCotaVeja4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja4, cotaGuilherme, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaVeja4);

		estoqueProdutoCotaSuper1 = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1, cotaMurilo, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaSuper1);

		estoqueProdutoCotaCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaMariana, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaCapricho1);

		estoqueProdutoCotaQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaOrlando, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaQuatroRodas1);

		estoqueProdutoCotaInfoExame1 = Fixture.estoqueProdutoCota(
				produtoEdicaoInfoExame1, cotaOrlando, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaInfoExame1);

		estoqueProdutoCotaJoseVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaJose, BigInteger.valueOf(20), BigInteger.ONE);
		save(session, estoqueProdutoCotaJoseVeja1EncalheAnt);

		estoqueProdutoCotaManoelVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaManoel, BigInteger.TEN, BigInteger.ONE);
		save(session, estoqueProdutoCotaManoelVeja1EncalheAnt);

		estoqueProdutoCotaMariaVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaMaria, BigInteger.valueOf(15), BigInteger.ONE);
		save(session, estoqueProdutoCotaMariaVeja1EncalheAnt);

		estoqueProdutoCotaJoseVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaJose,  BigInteger.valueOf(36), BigInteger.ONE);
		save(session, estoqueProdutoCotaJoseVeja2EncalheAnt);

		estoqueProdutoCotaManoelVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaManoel, BigInteger.valueOf(50), BigInteger.ONE);
		save(session, estoqueProdutoCotaManoelVeja2EncalheAnt);

		estoqueProdutoCotaMariaVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaMaria, BigInteger.valueOf(7), BigInteger.ONE);
		save(session, estoqueProdutoCotaMariaVeja2EncalheAnt);

		estoqueProdutoCotaJoseSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaJose, BigInteger.valueOf(20), BigInteger.ONE);
		save(session, estoqueProdutoCotaJoseSuper1EncalheAnt);

		estoqueProdutoCotaManoelSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaManoel, BigInteger.TEN, BigInteger.ONE);
		save(session, estoqueProdutoCotaManoelSuper1EncalheAnt);

		estoqueProdutoCotaMariaSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaMaria, BigInteger.valueOf(15), BigInteger.ONE);
		save(session, estoqueProdutoCotaMariaSuper1EncalheAnt);

		estoqueProdutoCotaJoseSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaJose,  BigInteger.valueOf(36), BigInteger.ONE);
		save(session, estoqueProdutoCotaJoseSuper2EncalheAnt);

		estoqueProdutoCotaManoelSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaManoel, BigInteger.valueOf(50), BigInteger.ONE);
		save(session, estoqueProdutoCotaManoelSuper2EncalheAnt);

		estoqueProdutoCotaMariaSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaMaria, BigInteger.valueOf(7), BigInteger.ONE);
		save(session, estoqueProdutoCotaMariaSuper2EncalheAnt);

		estoqueProdutoCotaManoelCunhaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaVeja1);

		estoqueProdutoCotaManoelCunhaVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaVeja2);

		estoqueProdutoCotaManoelCunhaVeja3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja3, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaVeja3);

		estoqueProdutoCotaManoelCunhaVeja4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja4, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaVeja4);

		estoqueProdutoCotaManoelCunhaSuper1 = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaSuper1);

		estoqueProdutoCotaManoelCunhaCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaCapricho1);

		estoqueProdutoCotaManoelCunhaQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaManoelCunha, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCunhaQuatroRodas1);


		estoqueProdutoCotaManoelVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelVeja1);

		estoqueProdutoCotaManoelVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelVeja2);

		estoqueProdutoCotaManoelVeja3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja3, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelVeja3);

		estoqueProdutoCotaManoelVeja4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja4, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelVeja4);

		estoqueProdutoCotaManoelCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelCapricho1);

		estoqueProdutoCotaManoelQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelQuatroRodas1);

		estoqueProdutoCotaManoelSuper1 = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1, cotaManoel, BigInteger.valueOf(100), BigInteger.TEN);
		save(session, estoqueProdutoCotaManoelSuper1);


	}

	private static void criarCobrancas(Session session) {

		cobrancaGuilherme1 = Fixture.criarCobrancaDinheiro("1234567890123",
                new Date(),  Fixture.criarData(1, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme1,1);

		cobrancaGuilherme2 = Fixture.criarCobrancaDinheiro("1234567890124",
                new Date(),  Fixture.criarData(2, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme2,1);

		cobrancaGuilherme3 = Fixture.criarCobrancaDinheiro("1234567890125",
                new Date(),  Fixture.criarData(3, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme3,1);

		cobrancaMurilo1 = Fixture.boleto("1234567890126", "41234", "1234567890126123",
                new Date(),  Fixture.criarData(4, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo1,1);

		cobrancaMurilo2 = Fixture.boleto("1234567890127", "4123", "1234567890127123",
                new Date(),  Fixture.criarData(5, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo2,1);

		cobrancaMurilo3 = Fixture.boleto("1234567890128", "4123", "1234567890128123",
                new Date(),  Fixture.criarData(6, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo3,1);

		cobrancaMariana1 = Fixture.boleto("1234567890129", "4123", "1234567890129123",
                new Date(),  Fixture.criarData(7, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMariana, bancoHSBC, dividaMariana1,1);

		cobrancaMariana2 = Fixture.boleto("1234567890120", "4123", "1234567890120123",
                new Date(),  Fixture.criarData(8, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMariana, bancoHSBC, dividaMariana2,1);

		cobrancaOrlando = Fixture.criarCobrancaDeposito("1234567890130",
                new Date(),  Fixture.criarData(9, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaOrlando, bancoHSBC, dividaOrlando,1);

		cobrancaAcumuloGuilherme1= Fixture.criarCobrancaDinheiro("3234567890123",
				new Date(),Fixture.criarData(1, 1, 2010),  null,
                BigDecimal.ZERO, new BigDecimal(210),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaAcumuladaGuilherme1,1);

		cobrancaAcumuloGuilherme2 = Fixture.criarCobrancaDinheiro("3234567890124",
				new Date(),Fixture.criarData(1, 1, 2010),  Fixture.criarData(2, 2, 2010),
                BigDecimal.ZERO, new BigDecimal(210),
				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
				cotaGuilherme, bancoHSBC, dividaAcumuladaGuilherme2,1);

		cobrancaAcumuloMurilo1 = Fixture.boleto("3234567890126", "323", "3234567890126123",
				new Date(),Fixture.criarData(1, 1, 2010),  Fixture.criarData(4, 2, 2010),
                 BigDecimal.ZERO, new BigDecimal(210),
				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
				cotaMurilo, bancoHSBC, dividaAcumuladaMurilo1,1);

		cobrancaAcumuloMariana1 = Fixture.boleto("3234567890129", "323", "3234567890129123",
				new Date(),Fixture.criarData(5, 1, 2010),  Fixture.criarData(7, 2, 2010),
                 BigDecimal.ZERO, new BigDecimal(210),
				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
				cotaMariana, bancoHSBC, dividaAcumuladaMariana1,1);

		save(session, cobrancaGuilherme1, cobrancaGuilherme2, cobrancaGuilherme3,
				cobrancaMurilo1, cobrancaMurilo2, cobrancaMurilo3,
				cobrancaMariana1, cobrancaMariana2,
				cobrancaOrlando,
				cobrancaAcumuloGuilherme1,cobrancaAcumuloGuilherme2,cobrancaAcumuloMariana1,cobrancaAcumuloMurilo1);

	}


	private static void criarParametrosCobrancaCota(Session session) {
		
		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);
		FormaCobranca formaBoleto2 =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(30), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);

		FormaCobranca formaDeposito =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);
		FormaCobranca formaDinheiro =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);

		save(session,formaBoleto,formaDeposito,formaDinheiro);

        Set<FormaCobranca> formasCobranca;

		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDinheiro);
		parametroCobrancaGuilherme = Fixture.parametroCobrancaCota(formasCobranca,
				1, null, cotaGuilherme, 1,
				false, new BigDecimal(1000), TipoCota.CONSIGNADO);
		save(session, parametroCobrancaGuilherme);
		formaDinheiro.setParametroCobrancaCota(parametroCobrancaGuilherme);
		formaDinheiro.setPrincipal(true);
		save(session,formaDinheiro);


		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		parametroCobrancaMurilo = Fixture.parametroCobrancaCota(formasCobranca,
				null, new BigDecimal(100), cotaMurilo, 1,
				false, new BigDecimal(1000), TipoCota.CONSIGNADO);
		save(session, parametroCobrancaMurilo);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaMurilo);
		formaBoleto.setPrincipal(true);
		save(session,formaBoleto);


		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto2);
		parametroCobrancaMariana = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaMariana, 1,
				false, new BigDecimal(1000), TipoCota.CONSIGNADO);
		save(session, parametroCobrancaMariana);
		formaBoleto2.setParametroCobrancaCota(parametroCobrancaMariana);
		formaBoleto2.setPrincipal(true);
		save(session,formaBoleto2);


		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDeposito);
		parametroCobrancaOralando = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaOrlando, 1,
				false, new BigDecimal(1000), null);
		save(session, parametroCobrancaOralando);
		formaDeposito.setParametroCobrancaCota(parametroCobrancaOralando);
		formaDeposito.setPrincipal(true);
		save(session,formaDeposito);
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDeposito);
		
		formaDeposito.setParametroCobrancaCota(parametroCobrancaManoel);
		formaDeposito.setPrincipal(true);
		save(session,formaDeposito);

	}


	private static void criarPDVsCota(Session session){

		save(session, tipoPontoPDVRevistaria, tipoPontoPDVBanca);
		
		SegmentacaoPDV segmentacaoPDV = Fixture.criarSegmentacaoPdv(null, TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, tipoPontoPDVRevistaria, null);
		
		SegmentacaoPDV segmentacaoPDV2 = Fixture.criarSegmentacaoPdv(null, TipoCaracteristicaSegmentacaoPDV.CONVENCIONAL, tipoPontoPDVBanca, null);
		
		pdvJose = Fixture.criarPDVPrincipal("PDV JOSE", cotaJose);
		pdvJose.setSegmentacao(segmentacaoPDV);
		session.save(pdvJose);

		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		pdvManoel.setSegmentacao(segmentacaoPDV2);
		session.save(pdvManoel);

		pdvManoelCunha = Fixture.criarPDVPrincipal("PDV CUNHA", cotaManoelCunha);
		pdvManoelCunha.setSegmentacao(segmentacaoPDV);
		session.save(pdvManoelCunha);

		pdvMaria = Fixture.criarPDVPrincipal("PDV MARIA", cotaMaria);
		pdvMaria.setSegmentacao(segmentacaoPDV);
		session.save(pdvMaria);

		pdvLuis = Fixture.criarPDVPrincipal("PDV LUIS", cotaLuis);
		pdvLuis.setSegmentacao(segmentacaoPDV2);
		session.save(pdvLuis);

		pdvJoao = Fixture.criarPDVPrincipal("PDV JOAO", cotaJoao);
		pdvJoao.setSegmentacao(segmentacaoPDV);
		session.save(pdvJoao);

		pdvGuilherme = Fixture.criarPDVPrincipal("PDV Guilherme", cotaGuilherme);
		pdvGuilherme.setSegmentacao(segmentacaoPDV);
		session.save(pdvGuilherme);

		pdvMurilo = Fixture.criarPDVPrincipal("PDV MURILO", cotaMurilo);
		pdvMurilo.setSegmentacao(segmentacaoPDV);
		session.save(pdvMurilo);

		pdvMariana = Fixture.criarPDVPrincipal("PDV MARINA", cotaMariana);
		pdvMariana.setSegmentacao(segmentacaoPDV);
		session.save(pdvMariana);

		pdvOrlando = Fixture.criarPDVPrincipal("PDV ORLANDO", cotaOrlando);
		pdvOrlando.setSegmentacao(segmentacaoPDV);
		session.save(pdvOrlando);

	}

	private static void criarRotaRoteiroCota(Session session) {

		Box boxA = Fixture.criarBox(1000, "BX-A", TipoBox.LANCAMENTO);
		session.save(boxA);
		
		Box boxB = Fixture.criarBox(2000, "BX-B", TipoBox.LANCAMENTO);
		session.save(boxB);
		
		Box boxC = Fixture.criarBox(3000, "BX-C", TipoBox.LANCAMENTO);
	    session.save(boxC);
		
		
		Roteirizacao roteirizacao1 = Fixture.criarRoteirizacao(boxA);
		session.save(roteirizacao1);
	
		Roteirizacao roteirizacao2 = Fixture.criarRoteirizacao(boxB);
		session.save(roteirizacao2);
		
		Roteirizacao roteirizacao3 = Fixture.criarRoteirizacao(boxC);
		session.save(roteirizacao3);;

		
		Roteiro roteiroPinheiros = Fixture.criarRoteiro("Pinheiros",roteirizacao1,TipoRoteiro.NORMAL);
		session.save(roteiroPinheiros);
		
		Roteiro roteiroCentro = Fixture.criarRoteiro("Centro", roteirizacao1, TipoRoteiro.NORMAL);
	    session.save(roteiroCentro);
	    
	    Roteiro roteiroBairro = Fixture.criarRoteiro("Bairro", roteirizacao1, TipoRoteiro.NORMAL);
        session.save(roteiroBairro);

		Roteiro roteiroInterlagos = Fixture.criarRoteiro("Interlagos",roteirizacao2,TipoRoteiro.NORMAL);
		session.save(roteiroInterlagos);
		
		Roteiro roteiroTCD = Fixture.criarRoteiro("TCD",roteirizacao3,TipoRoteiro.NORMAL);
		session.save(roteiroTCD);

		
		SegmentacaoPDV segmentacaoPDV = Fixture.criarSegmentacaoPdv(null, TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, tipoPontoPDVRevistaria, null);
		SegmentacaoPDV segmentacaoPDV2 = Fixture.criarSegmentacaoPdv(null, TipoCaracteristicaSegmentacaoPDV.CONVENCIONAL, tipoPontoPDVBanca, null);
		
		PDV pdvcotaJose2 = Fixture.criarPDVPrincipal("PDV cotaJose 2", cotaJose);
		pdvcotaJose2.setSegmentacao(segmentacaoPDV);
		session.save(pdvcotaJose2);

		PDV pdvcotaManoel2 = Fixture.criarPDVPrincipal("PDV cotaManoel 2", cotaManoel);
		pdvcotaManoel2.setSegmentacao(segmentacaoPDV2);
		session.save(pdvcotaManoel2);
		
		PDV pdvJoao = Fixture.criarPDVPrincipal("PDV João", cotaJoao);
		pdvJoao.setSegmentacao(segmentacaoPDV2);
        session.save(pdvJoao);
        
        PDV pdvJoana = Fixture.criarPDVPrincipal("PDV Joana", cotaJoana);
        pdvJoana.setSegmentacao(segmentacaoPDV2);
        session.save(pdvJoana);
        
        PDV pdvOrlando = Fixture.criarPDVPrincipal("PDV Orlando", cotaOrlando);
        pdvOrlando.setSegmentacao(segmentacaoPDV2);
        session.save(pdvOrlando);
		
		Rota rota1 = Fixture.rota("Rota 001",roteiroPinheiros);
		rota1.addPDV(pdvcotaJose2, 1);
		rota1.addPDV(pdvcotaManoel2, 2);
		session.save(rota1);
		
		Rota rota2 = Fixture.rota("Rota 002",roteiroInterlagos);
	    rota2.addPDV(pdvcotaJose2, 1);
	    rota2.addPDV(pdvcotaManoel2, 2);
		session.save(rota2);
		
		Rota rota10 = Fixture.rota("Rota 010",roteiroTCD);
	    rota10.addPDV(pdvcotaJose2, 1);
	    rota10.addPDV(pdvcotaManoel2, 2);
		session.save(rota10);
 
		
		Rota rotaRoteiroCentro1 = Fixture.rota("Rota 999", roteiroCentro);
		rotaRoteiroCentro1.addPDV(pdvJoao, 1);
		session.save(rotaRoteiroCentro1);
		
		Rota rotaRoteiroCentro2 = Fixture.rota("Rota 990", roteiroCentro);
        rotaRoteiroCentro2.addPDV(pdvMariana, 1);
        session.save(rotaRoteiroCentro2);
        
        Rota rotaRoteiroBairro = Fixture.rota("Rota 998", roteiroBairro);
        rotaRoteiroBairro.addPDV(pdvJoana, 1);
        rotaRoteiroBairro.addPDV(pdvOrlando, 2);
        session.save(rotaRoteiroBairro);
	}




    private static BigDecimal getTotalEncalhe(
            List<MovimentoFinanceiroCota> movimentosF) {
        Double totalD = 0d;
        List<MovimentoEstoqueCota> movimentosE = new ArrayList<MovimentoEstoqueCota>();
        for (MovimentoFinanceiroCota movF : movimentosF) {
            if (((TipoMovimentoFinanceiro) movF.getTipoMovimento())
                    .getGrupoMovimentoFinaceiro() == GrupoMovimentoFinaceiro.ENVIO_ENCALHE) {
                movimentosE = movF.getMovimentos();
                for (MovimentoEstoqueCota movE : movimentosE) {
                    if (((TipoMovimentoEstoque) movE.getTipoMovimento())
                            .getGrupoMovimentoEstoque() == GrupoMovimentoEstoque.ENVIO_ENCALHE) {
                        Double totalItem = (movE.getQtde().doubleValue())
                                * movE.getEstoqueProdutoCota()
                                        .getProdutoEdicao().getPrecoVenda()
                                        .doubleValue()
                                - Util.nvl(movE.getEstoqueProdutoCota()
                                        .getProdutoEdicao().getProduto()
                                        .getDesconto(), BigDecimal.ZERO).doubleValue();
                        totalD += totalItem;
                    }
                }
            }
        }
        return (new BigDecimal(totalD));
    }

    private static BigDecimal getTotalConsignado(
            List<MovimentoFinanceiroCota> movimentosF) {
        Double totalD = 0d;
        List<MovimentoEstoqueCota> movimentosE = new ArrayList<MovimentoEstoqueCota>();
        for (MovimentoFinanceiroCota movF : movimentosF) {
            if (((TipoMovimentoFinanceiro) movF.getTipoMovimento())
                    .getGrupoMovimentoFinaceiro() == GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE) {
                movimentosE = movF.getMovimentos();
                for (MovimentoEstoqueCota movE : movimentosE) {
                    if (((TipoMovimentoEstoque) movE.getTipoMovimento())
                            .getGrupoMovimentoEstoque() == GrupoMovimentoEstoque.ENVIO_JORNALEIRO) {
                        Double totalItem = (movE.getQtde().doubleValue())
                                * (movE.getEstoqueProdutoCota()
                                        .getProdutoEdicao().getPrecoVenda()
                                        .doubleValue() - Util.nvl(movE
                                        .getEstoqueProdutoCota()
                                        .getProdutoEdicao().getProduto()
                                        .getDesconto(), BigDecimal.ZERO).doubleValue());
                        totalD += totalItem;
                    }
                }
            }
        }
        return (new BigDecimal(totalD));
    }

    private static BigDecimal getTotalVendaEncalhe(
            List<MovimentoFinanceiroCota> movimentosF) {
        Double totalD = 0d;
        List<MovimentoEstoqueCota> movimentosE = new ArrayList<MovimentoEstoqueCota>();
        for (MovimentoFinanceiroCota movF : movimentosF) {
            if (((TipoMovimentoFinanceiro) movF.getTipoMovimento())
                    .getGrupoMovimentoFinaceiro() == GrupoMovimentoFinaceiro.COMPRA_ENCALHE) {
                movimentosE = movF.getMovimentos();
                for (MovimentoEstoqueCota movE : movimentosE) {
                    if (((TipoMovimentoEstoque) movE.getTipoMovimento())
                            .getGrupoMovimentoEstoque() == GrupoMovimentoEstoque.VENDA_ENCALHE) {
                        Double totalItem = (movE.getQtde().doubleValue())
                                * (movE.getEstoqueProdutoCota()
                                        .getProdutoEdicao().getPrecoVenda()
                                        .doubleValue() - Util.nvl(movE
                                        .getEstoqueProdutoCota()
                                        .getProdutoEdicao().getProduto()
                                        .getDesconto(), BigDecimal.ZERO).doubleValue());
                        totalD += totalItem;
                    }
                }
            }
        }
        return (new BigDecimal(totalD));
    }




	private static void criarDivida(Session session) {

		List<MovimentoFinanceiroCota> movimentosF = new ArrayList<MovimentoFinanceiroCota>();

		movimentosF = Arrays.asList(movimentoFinanceiroCota1,movimentoFinanceiroCota13,movimentoFinanceiroCota16,movimentoFinanceiroCota15,movimentoFinanceiroCota22,
				                    movimentoFinanceiroCota28,movimentoFinanceiroCota29,movimentoFinanceiroCota30,
				                    movimentoFinanceiroCota21,movimentoFinanceiroCota17,movimentoFinanceiroCota18,movimentoFinanceiroCota19,
				                    movimentoFinanceiroCota26,movimentoFinanceiroCota27);

		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(1, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));


		movimentosF = Arrays.asList(movimentoFinanceiroCota2, movimentoFinanceiroCota14, movimentoFinanceiroCota20,movimentoFinanceiroCota23,
				                    movimentoFinanceiroCota24, movimentoFinanceiroCota25);

		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(2, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota3);

		ConsolidadoFinanceiroCota consolidado3 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(3, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota4);

		ConsolidadoFinanceiroCota consolidado4 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(4, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota5);

		ConsolidadoFinanceiroCota consolidado5 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(5, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota6);

		ConsolidadoFinanceiroCota consolidado6 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(6, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota7);

		ConsolidadoFinanceiroCota consolidado7 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(7, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota8);

		ConsolidadoFinanceiroCota consolidado8 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(8, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		movimentosF = Arrays.asList(movimentoFinanceiroCota9);

		ConsolidadoFinanceiroCota consolidado9 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoel,
						Fixture.criarData(9, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));


		movimentosF = Arrays.asList(movimentoFinanceiroCota10);

		ConsolidadoFinanceiroCota consolidado15 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoelCunha,
						Fixture.criarData(10, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));


		movimentosF = Arrays.asList(movimentoFinanceiroCota11);

		ConsolidadoFinanceiroCota consolidado16 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoelCunha,
						Fixture.criarData(11, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));


		movimentosF = Arrays.asList(movimentoFinanceiroCota12);

		ConsolidadoFinanceiroCota consolidado17 = Fixture
				.consolidadoFinanceiroCota(
						movimentosF, cotaManoelCunha,
						Fixture.criarData(12, 6, 2012), getTotalConsignado(movimentosF), getTotalVendaEncalhe(movimentosF), getTotalEncalhe(movimentosF), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));




		ConsolidadoFinanceiroCota consolidado10 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaMaria,
						Fixture.criarData(13, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidado11 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaLuis,
						Fixture.criarData(14, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidado12 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaJoao,
						Fixture.criarData(15, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidado13 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaJose,
						Fixture.criarData(16, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidado14 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaJose,
						Fixture.criarData(17, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidadoGuilherme1 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(1, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(2, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoGuilherme3 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(3, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoMurilo1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(4, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoMurilo2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(5, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoMurilo3 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(6, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoMariana1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(7, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoMariana2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(8, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoOrlando1 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(9, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoOrlando2 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(10, 1, 2010), new BigDecimal(200), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));


		ConsolidadoFinanceiroCota consolidadoAcumuloGuilherme1 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(1, 2, 2010), new BigDecimal(210), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoAcumuloGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(2, 2, 2010), new BigDecimal(210), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoAcumuloMurilo1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(4, 2, 2010), new BigDecimal(210), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		ConsolidadoFinanceiroCota consolidadoAcumuloMariana1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(7, 2, 2010), new BigDecimal(210), new BigDecimal(230), new BigDecimal(180), new BigDecimal(180), new BigDecimal(180), new BigDecimal(45), new BigDecimal(25));

		save(session,consolidado1, consolidado2, consolidado3,
					 consolidado4, consolidado5, consolidado6,
					 consolidado7, consolidado8, consolidado9,
					 consolidado10, consolidado11,consolidado12,
					 consolidado13,consolidado14,consolidado15,consolidado16,consolidado17,
					 consolidadoGuilherme1, consolidadoGuilherme2, consolidadoGuilherme3,
					 consolidadoMurilo1, consolidadoMurilo2, consolidadoMurilo3,
					 consolidadoMariana1, consolidadoMariana2,
					 consolidadoOrlando1, consolidadoOrlando2,
					 consolidadoAcumuloGuilherme1,consolidadoAcumuloGuilherme2,consolidadoAcumuloMariana1,consolidadoAcumuloMurilo1);



		divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida2 = Fixture.divida(consolidado2, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida3 = Fixture.divida(consolidado3, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida4 = Fixture.divida(consolidado4, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida5 = Fixture.divida(consolidado5, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida6 = Fixture.divida(consolidado6, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida7 = Fixture.divida(consolidado7, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida8 = Fixture.divida(consolidado8, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida9 = Fixture.divida(consolidado9, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);


		divida10 = Fixture.divida(consolidado10, cotaMaria, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida11 = Fixture.divida(consolidado11, cotaLuis, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida12 = Fixture.divida(consolidado12, cotaJoao, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida13 = Fixture.divida(consolidado13, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida14 = Fixture.divida(consolidado14, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);


		divida15 = Fixture.divida(consolidado15, cotaManoelCunha, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida16 = Fixture.divida(consolidado16, cotaManoelCunha, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		divida17 = Fixture.divida(consolidado17, cotaManoelCunha, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);


		dividaGuilherme1 = Fixture.divida(consolidadoGuilherme1, cotaGuilherme, Fixture.criarData(1, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaGuilherme2 = Fixture.divida(consolidadoGuilherme2, cotaGuilherme, Fixture.criarData(2, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaGuilherme3 = Fixture.divida(consolidadoGuilherme3, cotaGuilherme, Fixture.criarData(3, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaMurilo1 = Fixture.divida(consolidadoMurilo1, cotaMurilo, Fixture.criarData(4, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaMurilo2 = Fixture.divida(consolidadoMurilo2, cotaMurilo, Fixture.criarData(5, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaMurilo3 = Fixture.divida(consolidadoMurilo3, cotaMurilo, Fixture.criarData(6, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaMariana1 = Fixture.divida(consolidadoMariana1, cotaMariana, Fixture.criarData(7, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaMariana2 = Fixture.divida(consolidadoMariana2, cotaMariana, Fixture.criarData(8, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);

		dividaOrlando = Fixture.divida(consolidadoOrlando1, cotaOrlando, Fixture.criarData(9, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);


		dividaAcumuladaGuilherme1 = Fixture.divida(consolidadoAcumuloGuilherme1, cotaGuilherme, Fixture.criarData(1, 2, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(210),true);

		dividaAcumuladaGuilherme2 = Fixture.divida(consolidadoAcumuloGuilherme2, cotaGuilherme, Fixture.criarData(2, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210),true);

		dividaAcumuladaMurilo1 = Fixture.divida(consolidadoAcumuloMurilo1, cotaMurilo, Fixture.criarData(4, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210),true);

		dividaAcumuladaMariana1 = Fixture.divida(consolidadoAcumuloMariana1, cotaMariana, Fixture.criarData(7, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210),true);


		save(session, divida1, divida2, divida3, divida4, divida5, divida6,
				      divida7, divida8, divida9, divida10, divida11,
				      divida12,divida13,divida14,divida15,divida16,divida17,
				      dividaGuilherme1, dividaGuilherme2, dividaGuilherme3,
				      dividaMurilo1, dividaMurilo2, dividaMurilo3,
				      dividaMariana1, dividaMariana2,
				      dividaOrlando,
				      dividaAcumuladaGuilherme1, dividaAcumuladaGuilherme2,dividaAcumuladaMariana1,dividaAcumuladaMurilo1);

		dividaAcumuladaGuilherme1.setDividaRaiz(dividaGuilherme1);
		dividaAcumuladaGuilherme2.setDividaRaiz(dividaGuilherme2);
		dividaAcumuladaMariana1.setDividaRaiz(dividaMariana1);
		dividaAcumuladaMurilo1.setDividaRaiz(dividaMurilo1);

		save(session, dividaAcumuladaGuilherme1,dividaAcumuladaGuilherme2,dividaAcumuladaMariana1,dividaAcumuladaMurilo1);
	}

	private static void criarTiposFornecedores(Session session) {
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session, tipoFornecedorPublicacao);
		tipoFornecedorOutros = Fixture.tipoFornecedorOutros();
		save(session, tipoFornecedorOutros);
	}

	private static void criarMovimentosEstoqueCota(Session session) {

		movimentoEstoqueCota1 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);


		movimentoEstoqueCota3 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);


		movimentoEstoqueCota4 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota5 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota6 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota7 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota8 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota9 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota10 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaManoelCunhaVeja1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota11 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaManoelCunhaVeja1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota12 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaManoelCunhaVeja1,
				BigInteger.TEN, cotaManoelCunha, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota333 = Fixture.movimentoEstoqueCota(produtoEdicaoInfoExame1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaInfoExame1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		//MOVIMENTOS TIPO ENCALHE
		movimentoEstoqueCota13 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota14 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja2,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota15 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja3,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota16 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja4,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota17 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelCapricho1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota18 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCotaManoelQuatroRodas1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);


		//MOVIMENTOS TIPO CONSIGNADO
		movimentoEstoqueCota19 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaManoelSuper1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota19.setEstudoCota(estudoCotaManoel);

		movimentoEstoqueCota20 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoCotaManoelQuatroRodas1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota20.setEstudoCota(estudoCotaManoel);

		movimentoEstoqueCota21 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoCotaManoelCapricho1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota21.setEstudoCota(estudoCotaManoel);

		movimentoEstoqueCota22 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoCotaManoelVeja4,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota22.setEstudoCota(estudoCotaManoel);

		movimentoEstoqueCota23 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoCotaManoelVeja3,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota23.setEstudoCota(estudoCotaManoel);

		movimentoEstoqueCota24 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoEnvioJornaleiro, usuarioJoao, estoqueProdutoCotaManoelVeja2,
				BigInteger.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota24.setEstudoCota(estudoCotaManoel);



		//MOVIMENTOS VENDA_ENCALHE
		movimentoEstoqueCota25 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		movimentoEstoqueCota26 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja3,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		movimentoEstoqueCota27 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja4,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		movimentoEstoqueCota28 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelVeja2,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		movimentoEstoqueCota29 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelQuatroRodas1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		movimentoEstoqueCota30 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoVendaEncalhe, usuarioJoao, estoqueProdutoCotaManoelSuper1,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, null);

		try {
			detach(session, estudoCotaManoel);
			
			EstudoCota estudoCotaManoel1 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel2 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel3 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel4 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel5 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel6 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel7 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel8 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel9 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel10 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel11 = EntityUtil.clonarSemID(estudoCotaManoel);

			EstudoCota estudoCotaManoel19 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel20 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel21 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel22 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel23 = EntityUtil.clonarSemID(estudoCotaManoel);
			EstudoCota estudoCotaManoel24 = EntityUtil.clonarSemID(estudoCotaManoel);

			estudoCotaManoel1.setRateiosDiferenca(null);
			estudoCotaManoel2.setRateiosDiferenca(null);
			estudoCotaManoel3.setRateiosDiferenca(null);
			estudoCotaManoel4.setRateiosDiferenca(null);
			estudoCotaManoel5.setRateiosDiferenca(null);
			estudoCotaManoel6.setRateiosDiferenca(null);
			estudoCotaManoel7.setRateiosDiferenca(null);
			estudoCotaManoel8.setRateiosDiferenca(null);
			estudoCotaManoel9.setRateiosDiferenca(null);
			estudoCotaManoel10.setRateiosDiferenca(null);
			estudoCotaManoel11.setRateiosDiferenca(null);

			estudoCotaManoel19.setRateiosDiferenca(null);
			estudoCotaManoel20.setRateiosDiferenca(null);
			estudoCotaManoel21.setRateiosDiferenca(null);
			estudoCotaManoel22.setRateiosDiferenca(null);
			estudoCotaManoel23.setRateiosDiferenca(null);
			estudoCotaManoel24.setRateiosDiferenca(null);

			movimentoEstoqueCota1.setEstudoCota(estudoCotaManoel1);
			movimentoEstoqueCota2.setEstudoCota(estudoCotaManoel2);
			movimentoEstoqueCota3.setEstudoCota(estudoCotaManoel3);
			movimentoEstoqueCota4.setEstudoCota(estudoCotaManoel4);
			movimentoEstoqueCota5.setEstudoCota(estudoCotaManoel5);
			movimentoEstoqueCota6.setEstudoCota(estudoCotaManoel6);
			movimentoEstoqueCota7.setEstudoCota(estudoCotaManoel7);
			movimentoEstoqueCota8.setEstudoCota(estudoCotaManoel8);
			movimentoEstoqueCota9.setEstudoCota(estudoCotaManoel9);
			movimentoEstoqueCota10.setEstudoCota(estudoCotaManoel10);
			movimentoEstoqueCota11.setEstudoCota(estudoCotaManoel11);

			movimentoEstoqueCota19.setEstudoCota(estudoCotaManoel19);
			movimentoEstoqueCota20.setEstudoCota(estudoCotaManoel20);
			movimentoEstoqueCota21.setEstudoCota(estudoCotaManoel21);
			movimentoEstoqueCota22.setEstudoCota(estudoCotaManoel22);
			movimentoEstoqueCota23.setEstudoCota(estudoCotaManoel23);
			movimentoEstoqueCota24.setEstudoCota(estudoCotaManoel24);
			
			save(session, estudoCotaManoel1, estudoCotaManoel2,
					estudoCotaManoel3, estudoCotaManoel4, estudoCotaManoel5,
					estudoCotaManoel6, estudoCotaManoel7, estudoCotaManoel8,
					estudoCotaManoel9, estudoCotaManoel10, estudoCotaManoel11, 
					estudoCotaManoel19, estudoCotaManoel20, estudoCotaManoel21,
					estudoCotaManoel22, estudoCotaManoel23, estudoCotaManoel24);

		} catch(Exception exception) {
			exception.printStackTrace();
		}
		movimentoEstoqueCota333.setEstudoCota(estudoCotaManoel);

		MovimentoEstoqueCota movimentoEstoqueCota31 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaGuilherme, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota32 = Fixture.movimentoEstoqueCota(produtoEdicaoInfoExame1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaInfoExame1,
				BigInteger.valueOf(55), cotaGuilherme, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota33 = Fixture.movimentoEstoqueCota(produtoEdicaoSuper1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaSuper1,
				BigInteger.TEN, cotaMurilo, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota34 = Fixture.movimentoEstoqueCota(produtoEdicaoCapricho1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaCapricho1,
				BigInteger.TEN, cotaMariana, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota35 = Fixture.movimentoEstoqueCota(produtoEdicaoQuatroRodas1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaQuatroRodas1,
				BigInteger.TEN, cotaOrlando, StatusAprovacao.PENDENTE, null);

		//MOVIMENTOS GERAÇÃO NFE
		MovimentoEstoqueCota movimentoEstoqueCota36 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe, 
				usuarioJoao, estoqueProdutoCotaVeja1, BigInteger.TEN, cotaJose, StatusAprovacao.APROVADO, null);
		
		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(
				movimentoEstoqueCota36, chamadaEncalheCotaVeja1CotaJose, controleConferenciaEncalheCotaVeja1,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(8),BigInteger.valueOf(8), produtoEdicaoVeja1);
		
		MovimentoEstoqueCota movimentoEstoqueCota37 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1, tipoMovimentoRecReparte, 
				usuarioJoao, estoqueProdutoCotaVeja1, BigInteger.TEN, cotaJose, StatusAprovacao.APROVADO, null);
		movimentoEstoqueCota37.setLancamento(lancamentoVeja1);
		
		MovimentoEstoqueCota movimentoEstoqueCota38 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1, tipoMovimentoRestautacaoReparteCotaAusente, 
				usuarioJoao, estoqueProdutoCotaVeja1, BigInteger.TEN, cotaJose, StatusAprovacao.APROVADO, null);
		movimentoEstoqueCota38.setLancamento(lancamentoVeja1);
		
		
		save(session, movimentoEstoqueCota1, movimentoEstoqueCota2, movimentoEstoqueCota3,
			 movimentoEstoqueCota4, movimentoEstoqueCota5, movimentoEstoqueCota6,
			 movimentoEstoqueCota7, movimentoEstoqueCota8, movimentoEstoqueCota9,
			 movimentoEstoqueCota10, movimentoEstoqueCota11, movimentoEstoqueCota12,
			 movimentoEstoqueCota13, movimentoEstoqueCota14, movimentoEstoqueCota15,
			 movimentoEstoqueCota16, movimentoEstoqueCota17, movimentoEstoqueCota18,
			 movimentoEstoqueCota19, movimentoEstoqueCota20, movimentoEstoqueCota21,
			 movimentoEstoqueCota22, movimentoEstoqueCota23, movimentoEstoqueCota24,
			 movimentoEstoqueCota25, movimentoEstoqueCota26, movimentoEstoqueCota27,
			 movimentoEstoqueCota28, movimentoEstoqueCota29, movimentoEstoqueCota30,
			 movimentoEstoqueCota31, movimentoEstoqueCota32,movimentoEstoqueCota33,
			 movimentoEstoqueCota34,movimentoEstoqueCota35, movimentoEstoqueCota36,
			 movimentoEstoqueCota37, movimentoEstoqueCota38, conferenciaEncalhe);
	}

	private static void criarMovimentosEstoqueCotaConferenciaEncalhe(Session session) {

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(session, estoqueProdutoCota);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe
				(DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

	}

	private static void criarParametrosSistema(Session session) {

		// Os dados do email são utilizados em outro trecho:
		merge(session, Fixture.parametroSistema(1L, TipoParametroSistema.EMAIL_HOST, "smtp.gmail.com"));
		merge(session, Fixture.parametroSistema(2L, TipoParametroSistema.EMAIL_PROTOCOLO, "smtps"));
		merge(session, Fixture.parametroSistema(3L, TipoParametroSistema.EMAIL_USUARIO, "sys.discover@gmail.com"));
		merge(session, Fixture.parametroSistema(4L, TipoParametroSistema.EMAIL_SENHA, "discover10"));
		merge(session, Fixture.parametroSistema(5L, TipoParametroSistema.EMAIL_PORTA, "465"));
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA,
				"C:\\Servidores\\apache-tomcat-7.0.25\\webapps\\nds-client\\distribuicao\\")); // windows;
//				"???"));					// linux;
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_IMAGENS_CAPA,
//				"C:\\apache-tomcat-7.0.25\\webapps\\nds-client\\capas\\"));	// windows;
				"/opt/tomcat/webapps/nds-client/capas/"));					// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_IMAGENS_PDV,
//				"\\images\\pdv\\"));	// windows;
				"/images/pdv/"));		// linux;
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_TERMO_ADESAO,
				"\\termo_adesao\\"));	// windows;
//				"/termo_adesao/"));		// linux;

		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_PROCURACAO,
				"\\procuracao\\"));	// windows;
//				"/procuracao/"));		// linux;
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_GERACAO_ARQUIVO_COBRANCA,
				"C:\\nds-client\\arquivo_cobranca\\")); // windows;
//				"???"));					// linux;
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_DE, "7"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM, "7"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM, "7"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE, "7"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO,
				"C:\\notas\\"));			// windows;
//				"/opt/interface/notas/"));	// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO,
				"C:\\notas\\"));			// windows;
//				"/opt/interface/notas/"));	// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_IMPORTACAO_CONTRATO,
				"C:\\contratos\\"));			// windows;
//				"/opt/interface/contratos/"));	// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO, 
//				"C:\\interface_mdc\\"));		// windows;
				"/opt/interface_mdc/"));		// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO,
//				"C:\\interface_mdc\\"));		// windows;
				"/opt/interface_mdc/"));	// linux;
		save(session, Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP,
//				"C:\\interface_mdc\\"));		// windows;
				"/opt/interface_mdc/"));		// linux;
		
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0106_IN_FILEMASK, "(?i:DEAPR19.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0107_IN_FILEMASK, "(?i:DEAJO19.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0107_OUT_FILEMASK, "DEAPR19.NEW"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0108_IN_FILEMASK, "(?i:MATRIZ.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0110_IN_FILEMASK, "([0-9]{8}).prd"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0112_IN_FILEMASK, "(?i:[0-9]{8}.edi)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0114_IN_FILEMASK, "([0-9]{8}).rec"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0116_IN_FILEMASK, "(?i:BANCA.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0117_IN_FILEMASK, "COTA.NEW"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0118_IN_FILEMASK, "(?i:PRECO.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0119_IN_FILEMASK, "(?i:PRODUTO.NEW)"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.NDSI_EMS0129_OUT_FILEMASK, "PICKING1.NEW"));
		
		save(session, Fixture.parametroSistema(TipoParametroSistema.OUTBOUND_FOLDER, "/opt/interface/"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.CODIGO_DISTRIBUIDOR_DINAP, "6248116"));
		save(session, Fixture.parametroSistema(TipoParametroSistema.ID_PJ_IMPORTACAO_NRE, "1"));
	}

	private static void criarMovimentosEstoque(Session session) {
		MovimentoEstoque movimentoRecFisicoVeja1 =
			Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
				 StatusAprovacao.APROVADO, "Aprovado");

		session.save(movimentoRecFisicoVeja1);

		MovimentoEstoque movimentoEstoque1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque1);

		MovimentoEstoque movimentoEstoque2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque2);

		MovimentoEstoque movimentoEstoque3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(2),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque3);

		MovimentoEstoque movimentoEstoque4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(2),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoque4);

		MovimentoEstoque movimentoEstoque5 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), BigInteger.valueOf(3),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque5);

		MovimentoEstoque movimentoEstoqueDiferenca6 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), BigInteger.valueOf(3),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoqueDiferenca6);

		MovimentoEstoque movimentoEstoque7 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), BigInteger.valueOf(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque7);

		MovimentoEstoque movimentoEstoque8 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), BigInteger.valueOf(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque8);

		MovimentoEstoque movimentoRecFisicoVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja2);


		MovimentoEstoque movimentoRecFisicoVeja3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja3, DateUtil.subtrairDias(new Date(), 6), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja3);

		MovimentoEstoque movimentoEnvioJornaleiroVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioJornaleiroVeja1);

		MovimentoEstoque movimentoEnvioJornaleiroSuper1 =
			Fixture.movimentoEstoque(null, produtoEdicaoSuper1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoSuper1, new Date(), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioJornaleiroSuper1);

		MovimentoEstoque movimentoEnvioEncalheVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja1);

		MovimentoEstoque movimentoEnvioEncalheVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja2);

		MovimentoEstoque movimentoEnvioEncalheVeja4 =
				Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoEnvioEncalhe, usuarioJoao,
						 				 estoqueProdutoVeja4, new Date(), BigInteger.valueOf(1),
						 				 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEnvioEncalheVeja4);
	}

	private static void criarEstoquesProdutos(Session session) {
		estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigInteger.TEN);

		estoqueProdutoVeja2 = Fixture.estoqueProduto(produtoEdicaoVeja2, BigInteger.TEN);

		estoqueProdutoVeja3 = Fixture.estoqueProduto(produtoEdicaoVeja3, BigInteger.TEN);

		estoqueProdutoVeja4 = Fixture.estoqueProduto(produtoEdicaoVeja4, BigInteger.TEN);

		estoqueProdutoSuper1 = Fixture.estoqueProduto(produtoEdicaoSuper1, BigInteger.TEN);

		estoqueProdutoCapricho1 = Fixture.estoqueProduto(produtoEdicaoCapricho1, BigInteger.TEN);

		estoqueProdutoInfoExame1 = Fixture.estoqueProduto(produtoEdicaoInfoExame1, BigInteger.TEN);

		estoqueProdutoVeja1EncalheAnt = Fixture.estoqueProduto(produtoEdicaoVeja1EncalheAnt, BigInteger.TEN);

		estoqueProdutoVeja2EncalheAnt = Fixture.estoqueProduto(produtoEdicaoVeja2EncalheAnt, BigInteger.TEN);

		estoqueProdutoSuper1EncalheAnt = Fixture.estoqueProduto(produtoEdicaoSuper1EncalheAnt, BigInteger.TEN);

		estoqueProdutoSuper2EncalheAnt = Fixture.estoqueProduto(produtoEdicaoSuper2EncalheAnt, BigInteger.TEN);

		save(session, estoqueProdutoVeja1, estoqueProdutoVeja2, estoqueProdutoVeja3,
			 estoqueProdutoVeja4, estoqueProdutoSuper1, estoqueProdutoCapricho1,
			 estoqueProdutoInfoExame1,estoqueProdutoVeja1EncalheAnt,estoqueProdutoVeja2EncalheAnt,
			 estoqueProdutoSuper1EncalheAnt,estoqueProdutoSuper2EncalheAnt);
	}
	
	private static void criarEstoquesProdutosCotaJuramentados(Session session) {
		estoqueProdutoCotaJuramentadoVeja = Fixture.estoqueProdutoCotaJuramentado(new Date(), produtoEdicaoVeja1, cotaMaria, BigInteger.TEN);
		
		estoqueProdutoCotaJuramentadoCapricho = Fixture.estoqueProdutoCotaJuramentado(new Date(), produtoEdicaoCapricho1, cotaMaria, BigInteger.TEN);
		
		estoqueProdutoCotaJuramentadoInfoExame = Fixture.estoqueProdutoCotaJuramentado(new Date(), produtoEdicaoInfoExame1, cotaJoao, BigInteger.TEN);
		
		save(session, estoqueProdutoCotaJuramentadoVeja, estoqueProdutoCotaJuramentadoCapricho, estoqueProdutoCotaJuramentadoInfoExame);
	}

	private static void criarRecebimentosFisicos(Session session) {
		recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);

		recebimentoFisico10 = Fixture.recebimentoFisico(
				notaFiscal10, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			session.save(recebimentoFisico);

		itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigInteger.TEN);
		session.save(itemRecebimentoFisico);

		itemCocaRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalCoca, recebimentoFisico, BigInteger.valueOf(50));
		session.save(itemCocaRecebimentoFisico);


		itemInfoExame1 = Fixture.itemRecebimentoFisico(itemNotaInfoExame1, recebimentoFisico, BigInteger.TEN);
		session.save(itemInfoExame1);

		itemQuatroRodas1 = Fixture.itemRecebimentoFisico(itemNotaQuatroRodas1, recebimentoFisico, BigInteger.TEN);
		session.save(itemQuatroRodas1);

		itemBoaForma1 = Fixture.itemRecebimentoFisico(itemNotaBoaForma1, recebimentoFisico, BigInteger.TEN);
		session.save(itemBoaForma1);

		itemBravo1 = Fixture.itemRecebimentoFisico(itemNotaBravo1, recebimentoFisico, BigInteger.TEN);
		session.save(itemBravo1);

		itemCaras1 = Fixture.itemRecebimentoFisico(itemNotaCaras1, recebimentoFisico, BigInteger.TEN);
		session.save(itemCaras1);

		itemCasaClaudia1 = Fixture.itemRecebimentoFisico(itemNotaCasaClaudia1, recebimentoFisico, BigInteger.TEN);
		session.save(itemCasaClaudia1);

		itemClaudia1 = Fixture.itemRecebimentoFisico(itemNotaClaudia1, recebimentoFisico, BigInteger.TEN);
		session.save(itemClaudia1);

		itemContigo1 = Fixture.itemRecebimentoFisico(itemNotaContigo1, recebimentoFisico, BigInteger.TEN);
		session.save(itemContigo1);

		itemManequim1 = Fixture.itemRecebimentoFisico(itemNotaManequim1, recebimentoFisico, BigInteger.TEN);
		session.save(itemManequim1);

		itemNatGeo1 = Fixture.itemRecebimentoFisico(itemNotaNatGeo1, recebimentoFisico, BigInteger.TEN);
		session.save(itemNatGeo1);

		itemPlacar1 = Fixture.itemRecebimentoFisico(itemNotaPlacar1, recebimentoFisico, BigInteger.TEN);
		session.save(itemPlacar1);


		itemVeja10 = Fixture.itemRecebimentoFisico(itemNotaVeja10, recebimentoFisico, BigInteger.valueOf(120));
		session.save(itemVeja10);

		itemSuperInteressante10 = Fixture.itemRecebimentoFisico(itemNotaSuperInteressante10, recebimentoFisico, BigInteger.valueOf(130));
		session.save(itemSuperInteressante10);

		itemCaras10 = Fixture.itemRecebimentoFisico(itemNotaCaras10, recebimentoFisico, BigInteger.valueOf(140));
		session.save(itemCaras10);

		itemPlacar10 = Fixture.itemRecebimentoFisico(itemNotaPlacar10, recebimentoFisico, BigInteger.valueOf(150));
		session.save(itemPlacar10);

	}

	private static void criarNotasFiscais(Session session) {

		notaFiscal10 = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(50), new BigDecimal(10), BigDecimal.TEN);
		session.save(notaFiscal10);

		notaFiscalFornecedor = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		session.save(notaFiscalFornecedor);

		itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaFiscalFornecedor);

		itemNotaFiscalCoca= Fixture.itemNotaFiscal(cocaColaLight,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaFiscalCoca);


		itemNotaInfoExame1 = Fixture.itemNotaFiscal(produtoEdicaoInfoExame1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaInfoExame1);

		itemNotaQuatroRodas1 = Fixture.itemNotaFiscal(produtoEdicaoQuatroRodas1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaQuatroRodas1);

		itemNotaBoaForma1 = Fixture.itemNotaFiscal(produtoEdicaoBoaForma1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaBoaForma1);

		itemNotaBravo1 = Fixture.itemNotaFiscal(produtoEdicaoBravo1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaBravo1);

		itemNotaCaras1 = Fixture.itemNotaFiscal(produtoEdicaoCaras1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaCaras1);

		itemNotaCasaClaudia1 = Fixture.itemNotaFiscal(produtoEdicaoCasaClaudia1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaCasaClaudia1);

		itemNotaClaudia1 = Fixture.itemNotaFiscal(produtoEdicaoClaudia1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaClaudia1);

		itemNotaContigo1 = Fixture.itemNotaFiscal(produtoEdicaoContigo1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaContigo1);

		itemNotaManequim1 = Fixture.itemNotaFiscal(produtoEdicaoManequim1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaManequim1);

		itemNotaNatGeo1 = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaNatGeo1);

		itemNotaPlacar1 = Fixture.itemNotaFiscal(produtoEdicaoPlacar1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		session.save(itemNotaPlacar1);



		itemNotaVeja10 = Fixture.itemNotaFiscal(produtoVeja10,
				usuarioJoao, notaFiscal10, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.valueOf(120));
		session.save(itemNotaVeja10);

		itemNotaSuperInteressante10 = Fixture.itemNotaFiscal(produtoSuperInteressante10,
				usuarioJoao, notaFiscal10, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.valueOf(130));
		session.save(itemNotaSuperInteressante10);

		itemNotaCaras10 = Fixture.itemNotaFiscal(produtoVeja10,
				usuarioJoao, notaFiscal10, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.valueOf(140));
		session.save(itemNotaCaras10);

		itemNotaPlacar10 = Fixture.itemNotaFiscal(produtoPlacar10,
				usuarioJoao, notaFiscal10, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.valueOf(150));
		session.save(itemNotaPlacar10);

	}

	private static void criarEstudos(Session session) {

		estudoVeja1 = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		session.save(estudoVeja1);

		estudoVeja2 = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja2.getDataLancamentoDistribuidor(), produtoEdicaoVeja2);
		session.save(estudoVeja2);

		estudoSuper1 = Fixture.estudo(
			BigInteger.TEN, lancamentoSuper1.getDataLancamentoDistribuidor(), produtoEdicaoSuper1);

		session.save(estudoSuper1);

		estudoCapricho1 = Fixture
				.estudo(BigInteger.TEN, lancamentoCapricho1.getDataLancamentoDistribuidor(), produtoEdicaoCapricho1);
		session.save(estudoCapricho1);

		estudoVeja1Atual = Fixture
				.estudo(BigInteger.TEN, new Date(), produtoEdicaoVeja1);
		session.save(estudoVeja1Atual);

		estudoVeja1EncalheAnt = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja1EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoVeja1EncalheAnt);
		session.save(estudoVeja1EncalheAnt);

		estudoVeja2EncalheAnt = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja2EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoVeja2EncalheAnt);
		session.save(estudoVeja2EncalheAnt);

		estudoSuper1EncalheAnt = Fixture
				.estudo(BigInteger.TEN, lancamentoSuper1EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoSuper1EncalheAnt);
		session.save(estudoSuper1EncalheAnt);

		estudoSuper2EncalheAnt = Fixture
				.estudo(BigInteger.TEN, lancamentoSuper2EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoSuper2EncalheAnt);
		session.save(estudoSuper2EncalheAnt);

		estudoInfoExame1 = Fixture
				.estudo(BigInteger.TEN, lancamentoInfoExame1.getDataLancamentoDistribuidor(), produtoEdicaoInfoExame1);
		session.save(estudoInfoExame1);

		estudoQuatroRodas1 = Fixture
				.estudo(BigInteger.TEN, lancamentoQuatroRodas1.getDataLancamentoDistribuidor(), produtoEdicaoQuatroRodas1);
		session.save(estudoQuatroRodas1);

		estudoBoaForma1 = Fixture
				.estudo(BigInteger.TEN, lancamentoBoaForma1.getDataLancamentoDistribuidor(), produtoEdicaoBoaForma1);
		session.save(estudoBoaForma1);

		estudoBravo1 = Fixture
				.estudo(BigInteger.TEN, lancamentoBravo1.getDataLancamentoDistribuidor(), produtoEdicaoBravo1);
		session.save(estudoBravo1);

		estudoCaras1 = Fixture
				.estudo(BigInteger.TEN, lancamentoCaras1.getDataLancamentoDistribuidor(), produtoEdicaoCaras1);
		session.save(estudoCaras1);

		estudoCasaClaudia1 = Fixture
				.estudo(BigInteger.TEN, lancamentoCasaClaudia1.getDataLancamentoDistribuidor(), produtoEdicaoCasaClaudia1);
		session.save(estudoCasaClaudia1);

		estudoClaudia1 = Fixture
				.estudo(BigInteger.TEN, lancamentoClaudia1.getDataLancamentoDistribuidor(), produtoEdicaoClaudia1);
		session.save(estudoClaudia1);

		estudoContigo1 = Fixture
				.estudo(BigInteger.TEN, lancamentoContigo1.getDataLancamentoDistribuidor(), produtoEdicaoContigo1);
		session.save(estudoContigo1);

		estudoManequim1 = Fixture
				.estudo(BigInteger.TEN, lancamentoManequim1.getDataLancamentoDistribuidor(), produtoEdicaoManequim1);
		session.save(estudoManequim1);

		estudoNatGeo1 = Fixture
				.estudo(BigInteger.TEN, lancamentoNatGeo1.getDataLancamentoDistribuidor(), produtoEdicaoNatGeo1);
		session.save(estudoNatGeo1);

		estudoPlacar1 = Fixture
				.estudo(BigInteger.TEN, lancamentoPlacar1.getDataLancamentoDistribuidor(), produtoEdicaoPlacar1);
		session.save(estudoPlacar1);

		estudoVeja10 = Fixture
				.estudo(BigInteger.valueOf(120), lancamentoVeja10ComEstudo.getDataLancamentoDistribuidor(), produtoVeja10);
		session.save(estudoVeja10);

		estudoSuperInteressante10= Fixture
				.estudo(BigInteger.valueOf(130), lancamentoSuperInteressante10ComEstudo.getDataLancamentoDistribuidor(), produtoSuperInteressante10);
		session.save(estudoSuperInteressante10);

		estudoCaras10 = Fixture
				.estudo(BigInteger.valueOf(140), lancamentoCaras10ComEstudo.getDataLancamentoDistribuidor(), produtoCaras10);
		session.save(estudoCaras10);

	}

    private static void criarEstudosCota(Session session) {

		estudoCotaVeja1Joao = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja1, cotaJose);
		save(session,estudoCotaVeja1Joao);

		estudoCotaVeja2Joao = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja2, cotaJose);
		save(session,estudoCotaVeja2Joao);

		estudoCotaCaprichoZe = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoCapricho1, cotaMaria);
		save(session,estudoCotaCaprichoZe);

		estudoCotaSuper1Manoel = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper1, cotaManoel);
		save(session,estudoCotaSuper1Manoel);

		estudoCotaManoel = Fixture.estudoCota(BigInteger.TEN, BigInteger.TEN, estudoVeja1, cotaManoel);
		save(session,estudoCotaManoel);

		estudoCotaManoelVejaAtual = Fixture.estudoCota(BigInteger.TEN, BigInteger.TEN, estudoVeja1Atual, cotaManoel);
		save(session,estudoCotaManoelVejaAtual);

		estudoCotaManoelVeja2 = Fixture.estudoCota(BigInteger.TEN, BigInteger.TEN, estudoVeja2, cotaManoel);
		save(session,estudoCotaManoelVeja2);

		estudoCotaVeja1JoaoEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja1EncalheAnt, cotaJose);
		save(session,estudoCotaVeja1JoaoEncaljeAnt);

		estudoCotaVeja1ManoelEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja1EncalheAnt, cotaManoel);
		save(session,estudoCotaVeja1ManoelEncaljeAnt);

		estudoCotaVeja1MariaEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja1EncalheAnt, cotaMaria);
		save(session,estudoCotaVeja1MariaEncaljeAnt);

		estudoCotaVeja2JoaoEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja2EncalheAnt, cotaJose);
		save(session,estudoCotaVeja2JoaoEncaljeAnt);

		estudoCotaVeja2ManoelEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja2EncalheAnt, cotaManoel);
		save(session,estudoCotaVeja2ManoelEncaljeAnt);

		estudoCotaVeja2MariaEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja2EncalheAnt, cotaMaria);
		save(session,estudoCotaVeja2MariaEncaljeAnt);

		estudoCotaSuper1JoaoEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper1EncalheAnt, cotaJose);
		save(session,estudoCotaSuper1JoaoEncaljeAnt);

		estudoCotaSuper1ManoelEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper1EncalheAnt, cotaManoel);
		save(session,estudoCotaSuper1ManoelEncaljeAnt);

		estudoCotaSuper1MariaEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper1EncalheAnt, cotaMaria);
		save(session,estudoCotaSuper1MariaEncaljeAnt);

		estudoCotaSuper2JoaoEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper2EncalheAnt, cotaJose);
		save(session,estudoCotaVeja2JoaoEncaljeAnt);

		estudoCotaSuper2ManoelEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper2EncalheAnt, cotaManoel);
		save(session,estudoCotaSuper2ManoelEncaljeAnt);

		estudoCotaSuper2MariaEncaljeAnt = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuper2EncalheAnt, cotaMaria);
		save(session,estudoCotaSuper2MariaEncaljeAnt);


		estudoJoseInfoExame1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoInfoExame1, cotaJose);
		save(session,estudoJoseInfoExame1);

		estudoJoseQuatroRodas1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoQuatroRodas1, cotaJose);
		save(session,estudoJoseQuatroRodas1);

		estudoJoseBoaForma1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoBoaForma1, cotaJose);
		save(session,estudoJoseBoaForma1);

		estudoJoseBravo1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoBravo1, cotaJose);
		save(session,estudoJoseBravo1);

		estudoJoseCaras1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoCaras1, cotaJose);
		save(session,estudoJoseCaras1);

		estudoJoseCasaClaudia1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoCasaClaudia1, cotaJose);
		save(session,estudoJoseCasaClaudia1);

		estudoJoseClaudia1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoClaudia1, cotaJose);
		save(session,estudoJoseClaudia1);

		estudoJoseContigo1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoContigo1, cotaJose);
		save(session,estudoJoseContigo1);

		estudoJoseManequim1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoManequim1, cotaJose);
		save(session,estudoJoseManequim1);

		estudoJoseNatGeo1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoNatGeo1, cotaJose);
		save(session,estudoJoseNatGeo1);

		estudoJoseEdicaoPlacar1 = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoPlacar1, cotaJose);
		save(session,estudoJoseEdicaoPlacar1);


		estudoCotaVeja10Guilherme = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja10, cotaGuilherme);
		save(session,estudoCotaVeja10Guilherme);

		estudoCotaSuperInteressante10Murilo = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoSuperInteressante10, cotaMurilo);
		save(session,estudoCotaSuperInteressante10Murilo);

		estudoCotaCaras10Mariana = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoCaras10, cotaMariana);
		save(session,estudoCotaCaras10Mariana);

	}

	private static void criarLancamentosExpedidos(Session session){

		Expedicao expedicao = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		session.save(expedicao);

		lancamentoVeja1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigInteger.TEN,  StatusLancamento.EXPEDIDO,
						itemRecebimentoFisico,expedicao, 1);
		
		session.save(lancamentoVeja1);

		lancamentoVeja2 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.EXPEDIDO,

						itemRecebimentoFisico,expedicao, 1);
		session.save(lancamentoVeja2);

		lancamentoSuper1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(100), StatusLancamento.EXPEDIDO,
								null,expedicao, 1);
		session.save(lancamentoSuper1);

		lancamentoCapricho1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(1000), StatusLancamento.EXPEDIDO,
								null,expedicao, 1);
		session.save(lancamentoCapricho1);


	}

	private static void criarLancamentosCancelados(Session session) {
		
		lancamentoCanceladoVeja5 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja5,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja5.getPeb()), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.CANCELADO,

						null, 2);
		session.save(lancamentoCanceladoVeja5);
		
		lancamentoCanceladoSuper2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper2,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper2.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(100), StatusLancamento.CANCELADO,
								null, 3);
		session.save(lancamentoCanceladoSuper2);

		
		lancamentoCanceladoCapricho2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho2,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho2.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(1000), StatusLancamento.CANCELADO,
								null, 1);
		session.save(lancamentoCanceladoCapricho2);
	}
	
	private static void criarLancamentos(Session session) {


		lancamentoVeja1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigInteger.TEN,  StatusLancamento.ESTUDO_FECHADO,
						itemRecebimentoFisico, 1);
		session.save(lancamentoVeja1);

		lancamentoVeja2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.BALANCEADO,

						null, 2);
		session.save(lancamentoVeja2);
		
		lancamentoSuper1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(100), StatusLancamento.CONFIRMADO,
								null, 3);
		session.save(lancamentoSuper1);
		
		lancamentoCapricho1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(1000), StatusLancamento.CANCELADO,
								null, 1);
		session.save(lancamentoCapricho1);

		lancamentoCocaCola = Fixture.lancamento(TipoLancamento.LANCAMENTO,cocaColaLight ,
				new Date(), new Date(), new Date(), new Date(), BigInteger.valueOf(100), StatusLancamento.CONFIRMADO, itemCocaRecebimentoFisico, 1);
		save(session, lancamentoCocaCola);


		lancamentoVeja1EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1EncalheAnt,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),+5), new Date(),
						new Date(), BigInteger.TEN,  StatusLancamento.EXPEDIDO,
						null, 4);
		session.save(lancamentoVeja1EcncalheAnt);

		lancamentoVeja2EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2EncalheAnt,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),+5), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.EXPEDIDO,

						null, 5);
		session.save(lancamentoVeja2EcncalheAnt);


		lancamentoInfoExame1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoInfoExame1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(500), StatusLancamento.ESTUDO_FECHADO,
								itemInfoExame1, 1);
		session.save(lancamentoInfoExame1);

		lancamentoQuatroRodas1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoQuatroRodas1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoQuatroRodas1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(1500), StatusLancamento.ESTUDO_FECHADO,
								itemQuatroRodas1, 1);
		session.save(lancamentoQuatroRodas1);

		lancamentoBoaForma1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBoaForma1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBoaForma1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(190), StatusLancamento.ESTUDO_FECHADO,
								itemBoaForma1, 1);
		session.save(lancamentoBoaForma1);

		lancamentoBravo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBravo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBravo1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(250), StatusLancamento.ESTUDO_FECHADO,
								itemBravo1, 1);
		session.save(lancamentoBravo1);

		lancamentoCaras1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCaras1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(250), StatusLancamento.ESTUDO_FECHADO,
								itemCaras1, 1);
		session.save(lancamentoCaras1);



		lancamentoCasaClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCasaClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCasaClaudia1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(350), StatusLancamento.ESTUDO_FECHADO,
								itemCasaClaudia1, 1);
		session.save(lancamentoCasaClaudia1);

		lancamentoClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoClaudia1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(400), StatusLancamento.ESTUDO_FECHADO,
								itemClaudia1, 1);
		session.save(lancamentoClaudia1);


		lancamentoContigo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoContigo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoContigo1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(185), StatusLancamento.ESTUDO_FECHADO,
								itemContigo1, 1);
		session.save(lancamentoContigo1);

		lancamentoManequim1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoManequim1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoManequim1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(225), StatusLancamento.ESTUDO_FECHADO,
								itemManequim1, 1);
		session.save(lancamentoManequim1);

		lancamentoNatGeo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoNatGeo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoNatGeo1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(75), StatusLancamento.ESTUDO_FECHADO,
								itemNatGeo1, 1);
		session.save(lancamentoNatGeo1);

		lancamentoPlacar1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoPlacar1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoPlacar1.getPeb()), new Date(),
								new Date(), BigInteger.valueOf(195), StatusLancamento.ESTUDO_FECHADO,
								itemPlacar1, 1);
		session.save(lancamentoPlacar1);

		lancamentoSuper1EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1EncalheAnt,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),+5), new Date(),
						new Date(), BigInteger.TEN,  StatusLancamento.EXPEDIDO,
						null, 1);
		session.save(lancamentoSuper1EcncalheAnt);

		lancamentoSuper2EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper2EncalheAnt,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),+5), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.EXPEDIDO,

						null, 1);
		session.save(lancamentoSuper2EcncalheAnt);

		lancamentoVeja10ComEstudo = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoVeja10,
						DateUtil.adicionarDias(new Date(), 2),
						DateUtil.adicionarDias(new Date(), 30), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.ESTUDO_FECHADO,

						itemVeja10, 1);
		session.save(lancamentoVeja10ComEstudo);

		lancamentoSuperInteressante10ComEstudo = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoSuperInteressante10,
						DateUtil.adicionarDias(new Date(), 2),
						DateUtil.adicionarDias(new Date(), 20), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.ESTUDO_FECHADO,

						itemSuperInteressante10, 2);
		session.save(lancamentoSuperInteressante10ComEstudo);

		lancamentoCaras10ComEstudo = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoCaras10,
						DateUtil.adicionarDias(new Date(), 2),
						DateUtil.adicionarDias(new Date(), 20), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.ESTUDO_FECHADO,

						itemCaras10, 3);
		session.save(lancamentoCaras10ComEstudo);

		lancamentoPlacar10SemEstudo = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoPlacar10,
						DateUtil.adicionarDias(new Date(), 2),
						DateUtil.adicionarDias(new Date(), 30), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.CONFIRMADO,

						itemPlacar10, 4);
		session.save(lancamentoPlacar10SemEstudo);
	}

	private static void criarProdutosEdicao(Session session) {
		produtoEdicaoVeja1 = Fixture.produtoEdicao("COD_1", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"111", 2L, produtoVeja, null, false, "Veja 1");
		session.save(produtoEdicaoVeja1);

		produtoEdicaoVeja2 = Fixture.produtoEdicao("COD_2", 2L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"112", 3L, produtoVeja, null, false, "Veja 2");
		session.save(produtoEdicaoVeja2);

		produtoEdicaoVeja3 = Fixture.produtoEdicao("COD_3", 3L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"113", 4L, produtoVeja, null, false, "Veja 3");
		session.save(produtoEdicaoVeja3);

		produtoEdicaoVeja4 = Fixture.produtoEdicao("COD_4", 4L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"114", 5L, produtoVeja, null, false, "Veja 4");
		session.save(produtoEdicaoVeja4);

		produtoEdicaoVeja5 = Fixture.produtoEdicao("COD_C5", 54321L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"114", 6L, produtoVeja, null, false, "Veja 5");
		session.save(produtoEdicaoVeja5);
		
		produtoEdicaoSuper1 = Fixture.produtoEdicao("COD_5", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"115", 6L, produtoSuper, null, false, "Super Int. 1");
		session.save(produtoEdicaoSuper1);
		
		produtoEdicaoSuper2 = Fixture.produtoEdicao("COD_C6", 24321L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"115", 6L, produtoSuper, null, false, "Super Int. 2");
		session.save(produtoEdicaoSuper2);

		produtoEdicaoCapricho1 = Fixture.produtoEdicao("COD_6", 1L, 9, 14,
				new Long(150), new BigDecimal(9), new BigDecimal(13.5),
				"116", 7L, produtoCapricho, null, false, "Capricho 1");
		session.save(produtoEdicaoCapricho1);
		
		produtoEdicaoCapricho2 = Fixture.produtoEdicao("COD_C7", 25432L, 9, 14,
				new Long(150), new BigDecimal(9), new BigDecimal(13.5),
				"116", 7L, produtoCapricho, null, false, "Capricho 2");
		session.save(produtoEdicaoCapricho2);

		produtoEdicaoInfoExame1 = Fixture.produtoEdicao("COD_7", 1L, 12, 30,
				new Long(250), new BigDecimal(11), new BigDecimal(14.5),
				"117", 8L, produtoInfoExame, null, false, "Info Exame 1");
		session.save(produtoEdicaoInfoExame1);

		produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao("COD_8", 1L, 7, 30,
				new Long(300), new BigDecimal(12.5), new BigDecimal(16.5),
				"118", 9L, produtoQuatroRodas, null, false, "Quatro Rodas 1");
		session.save(produtoEdicaoQuatroRodas1);

		produtoEdicaoBoaForma1 = Fixture.produtoEdicao("COD_9", 1L, 10, 30,
				new Long(100), new BigDecimal(12), new BigDecimal(15),
				"119", 10L, produtoBoaForma, null, false, "Boa Forma 1");
		session.save(produtoEdicaoBoaForma1);

		produtoEdicaoBravo1 = Fixture.produtoEdicao("COD_10", 1L, 10, 30,
				new Long(120), new BigDecimal(17), new BigDecimal(20),
				"119", 11L, produtoBravo, null, false, "Bravo 1");
		session.save(produtoEdicaoBravo1);

		produtoEdicaoCaras1 = Fixture.produtoEdicao("COD_11", 1L, 15, 30,
				new Long(200), new BigDecimal(20), new BigDecimal(25),
				"120", 12L, produtoCaras, null, false, "Caras 1");
		session.save(produtoEdicaoCaras1);

		produtoEdicaoCasaClaudia1 = Fixture.produtoEdicao("COD_12", 1L, 10, 30,
				new Long(200), new BigDecimal(20), new BigDecimal(25),
				"121", 13L, produtoCasaClaudia, null, false, "Casa Claudia 1");
		session.save(produtoEdicaoCasaClaudia1);

		produtoEdicaoClaudia1 = Fixture.produtoEdicao("COD_13", 1L, 10, 30,
				new Long(100), new BigDecimal(10), new BigDecimal(11),
				"122", 14L, produtoClaudia, null, false,"Claudia 1");
		session.save(produtoEdicaoClaudia1);

		produtoEdicaoContigo1 = Fixture.produtoEdicao("COD_14", 1L, 10, 30,
				new Long(100), new BigDecimal(12), new BigDecimal(15),
				"123", 15L, produtoContigo, null, false,"Contigo 1");
		session.save(produtoEdicaoContigo1);

		produtoEdicaoManequim1 = Fixture.produtoEdicao("COD_15", 1L, 10, 30,
				new Long(100), new BigDecimal(15), new BigDecimal(20),
				"124", 16L, produtoManequim, null, false, "Manequim 1");
		session.save(produtoEdicaoManequim1);

		produtoEdicaoNatGeo1 = Fixture.produtoEdicao("COD_16", 1L, 10, 30,
				new Long(100), new BigDecimal(20), new BigDecimal(23),
				"125", 17L, produtoNatGeo, null, false, "Nat Geo 1");
		session.save(produtoEdicaoNatGeo1);

		produtoEdicaoPlacar1 = Fixture.produtoEdicao("COD_17", 1L, 10, 30,
				new Long(200), new BigDecimal(9), new BigDecimal(12),
				"126", 18L, produtoPlacar, null, false, "Placar 1");
		session.save(produtoEdicaoPlacar1);

		cocaColaLight = Fixture.produtoEdicao("COD_18", 1L, 10, 30,
				new Long(200), new BigDecimal(9), new BigDecimal(12),
				"127", 19L, cocaCola, null, false, "Coca Cola Light");
		session.save(cocaColaLight);

		produtoEdicaoVeja1EncalheAnt = Fixture.produtoEdicao("COD_19", 5L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"128", 20L, produtoVeja, null, false, "Veja Encalhe 1");
		session.save(produtoEdicaoVeja1EncalheAnt);

		produtoEdicaoVeja2EncalheAnt = Fixture.produtoEdicao("COD_20", 6L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"129", 21L, produtoVeja, null, false, "Veja Encalhe 2");
		session.save(produtoEdicaoVeja2EncalheAnt);

		produtoEdicaoSuper1EncalheAnt = Fixture.produtoEdicao("COD_21", 2L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"130", 22L, produtoSuper, null, false, "Super Int. Encalhe Ant");
		session.save(produtoEdicaoSuper1EncalheAnt);

		produtoEdicaoSuper2EncalheAnt = Fixture.produtoEdicao("COD_22", 3L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"131", 23L, produtoSuper, null, false,"Super Int. Encalhe 2");
		session.save(produtoEdicaoSuper2EncalheAnt);

		produtoVeja10 = Fixture.produtoEdicao("COD_100", 10L, 50, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"132", 23L, produtoVeja, null, false, "Veja 10");
		session.save(produtoVeja10);

		produtoSuperInteressante10 = Fixture.produtoEdicao("COD_101", 10L, 50, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(25),
				"133", 23L, produtoSuper, null, false, "Super Int. 10");
		session.save(produtoSuperInteressante10);

		produtoCaras10 = Fixture.produtoEdicao("COD_102", 10L, 30, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(18),
				"134", 23L, produtoCaras, null, false, "Caras 10");
		session.save(produtoCaras10);

		produtoPlacar10 = Fixture.produtoEdicao("COD_103", 10L, 15, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(12),
				"135", 23L, produtoPlacar, null, false, "Placar 10");
		session.save(produtoPlacar10);

	}

	private static void criarProdutos(Session session) {
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(editoraAbril);
		produtoVeja.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoVeja);

		produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
		produtoSuper.addFornecedor(fornecedorDinap);
		produtoSuper.setEditor(editoraAbril);
		produtoSuper.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoSuper);

		produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		produtoCapricho.setEditor(editoraAbril);
		produtoCapricho.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoCapricho);

		produtoInfoExame = Fixture.produtoInfoExame(tipoProdutoRevista);
		produtoInfoExame.addFornecedor(fornecedorDinap);
		produtoInfoExame.setEditor(editoraAbril);
		produtoInfoExame.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoInfoExame);

		produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
		produtoQuatroRodas.addFornecedor(fornecedorDinap);
		produtoQuatroRodas.setEditor(editoraAbril);
		produtoQuatroRodas.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoQuatroRodas);

		produtoBoaForma = Fixture.produtoBoaForma(tipoProdutoRevista);
		produtoBoaForma.addFornecedor(fornecedorDinap);
		produtoBoaForma.setEditor(editoraAbril);
		produtoBoaForma.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoBoaForma);

		produtoBravo = Fixture.produtoBravo(tipoProdutoRevista);
		produtoBravo.addFornecedor(fornecedorDinap);
		produtoBravo.setEditor(editoraAbril);
		produtoBravo.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoBravo);

		produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		produtoCaras.addFornecedor(fornecedorDinap);
		produtoCaras.setEditor(editoraAbril);
		produtoCaras.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoCaras);

		produtoCasaClaudia = Fixture.produtoCasaClaudia(tipoProdutoRevista);
		produtoCasaClaudia.addFornecedor(fornecedorDinap);
		produtoCasaClaudia.setEditor(editoraAbril);
		produtoCasaClaudia.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoCasaClaudia);

		produtoClaudia = Fixture.produtoClaudia(tipoProdutoRevista);
		produtoClaudia.addFornecedor(fornecedorDinap);
		produtoClaudia.setEditor(editoraAbril);
		produtoClaudia.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoClaudia);

		produtoContigo = Fixture.produtoContigo(tipoProdutoRevista);
		produtoContigo.addFornecedor(fornecedorDinap);
		produtoContigo.setEditor(editoraAbril);
		produtoContigo.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoContigo);

		produtoManequim = Fixture.produtoManequim(tipoProdutoRevista);
		produtoManequim.addFornecedor(fornecedorDinap);
		produtoManequim.setEditor(editoraAbril);
		produtoManequim.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoManequim);

		produtoNatGeo = Fixture.produtoNationalGeographic(tipoProdutoRevista);
		produtoNatGeo.addFornecedor(fornecedorDinap);
		produtoNatGeo.setEditor(editoraAbril);
		produtoNatGeo.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoNatGeo);

		produtoPlacar = Fixture.produtoPlacar(tipoProdutoRevista);
		produtoPlacar.addFornecedor(fornecedorFc);
		produtoPlacar.setEditor(editoraAbril);
		produtoPlacar.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		session.save(produtoPlacar);

		cocaCola = Fixture.produto("564", "Coca-Cola", "Coca-Cola", PeriodicidadeProduto.MENSAL, tipoProdutoRefrigerante, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		cocaCola.addFornecedor(fornecedorAcme);
		cocaCola.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		save(session, cocaCola);


	}

	private static void criarEditores(Session session) {
		editoraAbril = Fixture.editoraAbril();
		save(session, editoraAbril);
	}

	private static void criarTiposProduto(Session session) {
		
		tipoProdutoRefrigerante = Fixture.tipoProduto("Refrigerante", GrupoProduto.OUTROS, ncmBebidas, "", Long.valueOf(40));
		tipoProdutoRevista = Fixture.tipoProduto("Revistas", GrupoProduto.REVISTA, ncmRevista, "4902.90.00", 001L);
		tipoProdutoFasciculo= Fixture.tipoProduto("Fasciculos",GrupoProduto.COLECIONAVEL, ncmFasciculo, "", 002L);
		tipoProdutoLivro= Fixture.tipoProduto("Livro",GrupoProduto.LIVRO, ncmLivro, "", 003L);
		tipoProdutoCromo= Fixture.tipoProduto("Cromos", GrupoProduto.CROMO, ncmFigurinha, "4908.90.00", 004L);		
		tipoProdutoCard= Fixture.tipoProduto("Cards/Similares",GrupoProduto.CROMO, ncmFigurinha, "", 005L);		
		tipoProdutoAlbun= Fixture.tipoProduto("Album",GrupoProduto.ALBUM, ncmLivroilustrado, "", 006L);		
		tipoProdutoGuia= Fixture.tipoProduto("Guia",GrupoProduto.GUIA, ncmLivro, "", 007L);		
		tipoProdutoQuadrinho= Fixture.tipoProduto("Quadrinhos",GrupoProduto.REVISTA, ncmRevista, "", Long.valueOf(8));		
		tipoProdutoAtividade= Fixture.tipoProduto("Atividades",GrupoProduto.REVISTA, ncmRevista, "", Long.valueOf(9));		
		tipoProdutoPassatempo= Fixture.tipoProduto("Passa Tempo",GrupoProduto.REVISTA, ncmRevista, "", Long.valueOf(10));		
		tipoProdutoVideo= Fixture.tipoProduto("Videos",GrupoProduto.OUTROS, ncmCd, "", Long.valueOf(11));		
		tipoProdutoCdrom= Fixture.tipoProduto("CD-ROM",GrupoProduto.OUTROS, ncmCd, "", Long.valueOf(12));		
		tipoProdutoPoster= Fixture.tipoProduto("Poster",GrupoProduto.OUTROS, ncmCartaz, "", Long.valueOf(13));		
		tipoProdutoJornal= Fixture.tipoProduto("Jornal",GrupoProduto.JORNAL, ncmRevista, "", Long.valueOf(14));		
		tipoProdutoTabloide= Fixture.tipoProduto("Tabloide",GrupoProduto.JORNAL, ncmRevista, "", Long.valueOf(15));		
		tipoProdutoOutro= Fixture.tipoProduto("Outros",GrupoProduto.OUTROS, ncmRevista, "", Long.valueOf(16));		
		tipoProdutoCapaDura= Fixture.tipoProduto("Capa Dura",GrupoProduto.OUTROS, ncmLivro, "", Long.valueOf(18));		
		tipoProdutoRevistaDigital= Fixture.tipoProduto("Revista Digital",GrupoProduto.REVISTA, ncmRevista, "", Long.valueOf(19));		
		tipoProdutoDvd= Fixture.tipoProduto("DVD",GrupoProduto.OUTROS, ncmCd, "", Long.valueOf(24));
		tipoProdutoLivroIlustrado= Fixture.tipoProduto("Livro Ilustrado",GrupoProduto.ALBUM, ncmLivroilustrado, "", Long.valueOf(36));
		tipoProdutoRefrigerante = Fixture.tipoProduto("Refrigerante", GrupoProduto.OUTROS, ncmBebidas, "", Long.valueOf(40));
		
		save(session, tipoProdutoRevista, tipoProdutoFasciculo, tipoProdutoLivro, tipoProdutoCromo, tipoProdutoCard, tipoProdutoAlbun, 
				tipoProdutoGuia, tipoProdutoQuadrinho, tipoProdutoAtividade, tipoProdutoPassatempo, tipoProdutoVideo, tipoProdutoCdrom,
				tipoProdutoPoster, tipoProdutoJornal, tipoProdutoTabloide, tipoProdutoOutro, tipoProdutoCapaDura, tipoProdutoRevistaDigital, 
				tipoProdutoDvd, tipoProdutoLivroIlustrado, tipoProdutoRefrigerante);
		
	}

	private static void criarDistribuidor(Session session) {

		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9");
		save(session, juridicaDistrib);

		//FORMAS DE COBRANÇA DA COTA

		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE, null);

		formaCheque = Fixture.formaCobrancaCheque(true, new BigDecimal(200), true, bancoHSBC,
				  								  BigDecimal.ONE, BigDecimal.ONE, null);

		formaDeposito = Fixture.formaCobrancaDeposito(true, new BigDecimal(200), true, bancoHSBC,
				  								  BigDecimal.ONE, BigDecimal.ONE, null);

		formaDinheiro = Fixture.formaCobrancaDinheiro(true, new BigDecimal(200), true, bancoHSBC,
				  								  BigDecimal.ONE, BigDecimal.ONE, null);

		formaTransferenciBancaria = Fixture.formaCobrancaTransferencia(true, new BigDecimal(200), true, bancoHSBC,
				  								  BigDecimal.ONE, BigDecimal.ONE, null);


		save(session, formaBoleto,formaCheque,formaDeposito,formaDinheiro,formaTransferenciBancaria);


		//FORMAS DE COBRANCA DO DISTRIBUIDOR
		FormaCobranca formaBoletoDistribuidor = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
				  															BigDecimal.ONE, BigDecimal.ONE, null);

		FormaCobranca formaChequeDistribuidor = Fixture.formaCobrancaCheque(true, new BigDecimal(200), true, bancoITAU,
						  													BigDecimal.ONE, BigDecimal.ONE, null);

		FormaCobranca formaDepositoDistribuidor = Fixture.formaCobrancaDeposito(true, new BigDecimal(200), true, bancoBRADESCO,
						  													BigDecimal.ONE, BigDecimal.ONE, null);

		FormaCobranca formaDinheiroDistribuidor = Fixture.formaCobrancaDinheiro(true, new BigDecimal(200), true, bancoDOBRASIL,
						  													BigDecimal.ONE, BigDecimal.ONE, null);

		FormaCobranca formaTransferenciBancariaDistribuidor = Fixture.formaCobrancaTransferencia(true, new BigDecimal(200), true, bancoHSBC,
						  													BigDecimal.ONE, BigDecimal.ONE, null);


		save(session, formaBoletoDistribuidor,formaChequeDistribuidor,formaDepositoDistribuidor,formaDinheiroDistribuidor,formaTransferenciBancariaDistribuidor);



		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(distribuidor, formaBoletoDistribuidor, true, true, true, 1,"Assunto","Mansagem",true,FormaEmissao.NAO_IMPRIME);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca2 =
				Fixture.criarPoliticaCobranca(null, formaBoletoDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.INDIVIDUAL_AGREGADA);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca3 =
				Fixture.criarPoliticaCobranca(null, formaDinheiroDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.NAO_IMPRIME);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca4 =
				Fixture.criarPoliticaCobranca(null, formaTransferenciBancariaDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.NAO_IMPRIME);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca5 =
				Fixture.criarPoliticaCobranca(null, formaDepositoDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.INDIVIDUAL_BOX);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca6 =
				Fixture.criarPoliticaCobranca(null, formaBoletoDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.INDIVIDUAL_AGREGADA);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca7 =
				Fixture.criarPoliticaCobranca(null, formaDepositoDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.NAO_IMPRIME);
			save(session, politicaCobranca);

		PoliticaCobranca politicaCobranca8 =
				Fixture.criarPoliticaCobranca(null, formaChequeDistribuidor, true, true, true, 1,"Assunto","Mansagem",false,FormaEmissao.NAO_IMPRIME);
			save(session, politicaCobranca);

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));


		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		politicasCobranca.add(politicaCobranca2);
		politicasCobranca.add(politicaCobranca3);
		politicasCobranca.add(politicaCobranca4);
		politicasCobranca.add(politicaCobranca5);
		politicasCobranca.add(politicaCobranca6);
		politicasCobranca.add(politicaCobranca7);
		politicasCobranca.add(politicaCobranca8);


		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);

		distribuidor.setPoliticaSuspensao(politicaSuspensao);

		ParametroContratoCota parametroContrato = Fixture.criarParametroContratoCota("<font color=\"blue\"><b>CONSIDERANDO QUE:</b></font><br>"+
																					 "<br>"+"<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"+
																					 "<br>"+"<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"+
																					 "<br>"+"<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"+
																					 "<br>"+"RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"+
																					 "<br><br>"+"<font color=\"blue\"><b>1.	OBJETO DO CONTRATO</b><br></font>"+
																					 "<br>"+"<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante."
																					 , "neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.", 30, 30);
		save(session, parametroContrato);


		ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor = new ParametrosAprovacaoDistribuidor();

		parametrosAprovacaoDistribuidor.setAjusteEstoque(true);
		parametrosAprovacaoDistribuidor.setDebitoCredito(true);
		parametrosAprovacaoDistribuidor.setDevolucaoFornecedor(true);
		parametrosAprovacaoDistribuidor.setFaltasSobras(true);
		parametrosAprovacaoDistribuidor.setNegociacao(true);
		parametrosAprovacaoDistribuidor.setPostergacaoCobranca(true);

		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();

		distribuidor.setParametroContratoCota(parametroContrato);

		distribuidor.setFatorRelancamentoParcial(5);
		distribuidor.setInformacoesComplementaresProcuracao("informacoesComplementaresProcuracao");
		distribuidor.setNegociacaoAteParcelas(10);
		distribuidor.setParametrosAprovacaoDistribuidor(parametrosAprovacaoDistribuidor);
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		distribuidor.setPrazoAvisoPrevioValidadeGarantia(40);
		distribuidor.setPrazoFollowUp(50);
		distribuidor.setValorConsignadoSuspensaoCotas(new BigDecimal(9999999));
		distribuidor.setQtdDiasLimiteParaReprogLancamento(2);
		distribuidor.setCodigoDistribuidorDinap("6248116");

		save(session, distribuidor);

		politicaCobranca.setDistribuidor(distribuidor);
		politicaCobranca2.setDistribuidor(distribuidor);
		politicaCobranca3.setDistribuidor(distribuidor);
		politicaCobranca4.setDistribuidor(distribuidor);
		politicaCobranca5.setDistribuidor(distribuidor);
		politicaCobranca6.setDistribuidor(distribuidor);
		politicaCobranca7.setDistribuidor(distribuidor);
		politicaCobranca8.setDistribuidor(distribuidor);
		save(session, politicaCobranca, politicaCobranca2, politicaCobranca3, politicaCobranca4, politicaCobranca5, politicaCobranca6, politicaCobranca7, politicaCobranca8);

		
		for(TipoGarantia tipo:TipoGarantia.values()){
			save(session,Fixture.criarTipoGarantiaAceita(distribuidor, tipo));
		}
	}

	private static void criarEnderecoDistribuidor(Session session){

		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COBRANCA, "13222-020", "Rua João de Souza", "51", "Centro", "São Paulo", "SP",3543402);

		EnderecoDistribuidor enderecoDistribuidor = Fixture.enderecoDistribuidor(distribuidor, endereco, true, TipoEndereco.COBRANCA);

		save(session,endereco,enderecoDistribuidor);
	}

	private static void criarTelefoneDistribuidor(Session session){

		Telefone telefone = Fixture.telefone("019", "259633", "012");

		TelefoneDistribuidor teDistribuidor = Fixture.telefoneDistribuidor(distribuidor, true, telefone, TipoTelefone.COMERCIAL);

		save(session, telefone,teDistribuidor);
	}

	private static void criarCotas(Session session) {


		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		cotaManoel.setInicioAtividade(DateUtil.adicionarDias(cotaManoel.getInicioAtividade(), -360));
		
		parametroCotaNotaFiscalEletronicaManoel = Fixture.parametrosCotaNotaFiscalEletronica(false, "manoel@email.com");
		cotaManoel.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaManoel);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedorAcme);
		fornecedores.add(fornecedorDinap);
		fornecedores.add(fornecedorFc);
		cotaManoel.setFornecedores(fornecedores);

		save(session, cotaManoel);
		ContratoCota contrato = Fixture.criarContratoCota(cotaManoel,true,DateUtil.parseData("01/01/2012", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato);


		cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		parametroCotaNotaFiscalEletronicaJose = Fixture.parametrosCotaNotaFiscalEletronica(false, "jose@email.com");
		cotaJose.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaJose);
		save(session, cotaJose);
		ContratoCota contrato2 = Fixture.criarContratoCota(cotaJose,true,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato2);


		cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO,box2);
		parametroCotaNotaFiscalEletronicaMaria = Fixture.parametrosCotaNotaFiscalEletronica(true, "maria@email.com");
		cotaMaria.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaMaria);
		save(session, cotaMaria);
		ContratoCota contrato3 = Fixture.criarContratoCota(cotaMaria,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato3);


		cotaGuilherme = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO,box2);
		parametroCotaNotaFiscalEletronicaGuilherme = Fixture.parametrosCotaNotaFiscalEletronica(true, "guilherme@email.com");
		cotaGuilherme.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaGuilherme);
		cotaGuilherme.setSugereSuspensao(true);
		save(session, cotaGuilherme);
		ContratoCota contrato4 = Fixture.criarContratoCota(cotaGuilherme,true,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato4);


		cotaMurilo= Fixture.cota(22345, murilo, SituacaoCadastro.ATIVO,box2);
		parametroCotaNotaFiscalEletronicaMurilo = Fixture.parametrosCotaNotaFiscalEletronica(true, "murilo@email.com");
		cotaMurilo.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaMurilo);
		cotaMurilo.setSugereSuspensao(true);
		save(session, cotaMurilo);
		ContratoCota contrato5 = Fixture.criarContratoCota(cotaMurilo,true,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato5);


		cotaMariana = Fixture.cota(32345, mariana, SituacaoCadastro.ATIVO,box1);
		parametroCotaNotaFiscalEletronicaMariana = Fixture.parametrosCotaNotaFiscalEletronica(true, "mariana@email.com");
		cotaMariana.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaMariana);
		cotaMariana.setSugereSuspensao(true);
		save(session, cotaMariana);
		ContratoCota contrato6 = Fixture.criarContratoCota(cotaMariana,true,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato6);


		cotaOrlando = Fixture.cota(4444, orlando, SituacaoCadastro.INATIVO,box1);
		parametroCotaNotaFiscalEletronicaOrlando = Fixture.parametrosCotaNotaFiscalEletronica(true, "orlando@email.com");
		cotaOrlando.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaOrlando);
		cotaOrlando.setSugereSuspensao(false);
		save(session, cotaOrlando);
		ContratoCota contrato7 = Fixture.criarContratoCota(cotaOrlando,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato7);


		cotaJoao = Fixture.cota(9999, joao, SituacaoCadastro.ATIVO,box2);
		parametroCotaNotaFiscalEletronicaJoao = Fixture.parametrosCotaNotaFiscalEletronica(true, "joao@email.com");
		cotaJoao.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaJoao);
		save(session, cotaJoao);
		ContratoCota contrato8 = Fixture.criarContratoCota(cotaJoao,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato8);


		cotaLuis = Fixture.cota(888, luis, SituacaoCadastro.ATIVO,box2);
		parametroCotaNotaFiscalEletronicaLuis = Fixture.parametrosCotaNotaFiscalEletronica(true, "luis@email.com");
		cotaLuis.setParametrosCotaNotaFiscalEletronica(parametroCotaNotaFiscalEletronicaLuis);
		save(session, cotaLuis);
		ContratoCota contrato9 = Fixture.criarContratoCota(cotaLuis,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato9);


		cotaAcme = Fixture.cota(100000, juridicaAcme, SituacaoCadastro.ATIVO, box1);
		save(session, cotaAcme);
		ContratoCota contrato10 = Fixture.criarContratoCota(cotaAcme,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato10);

		cotaJoana = Fixture.cota(567, juridicaAcme, SituacaoCadastro.ATIVO, boxPostoAvancado);
		save(session, cotaJoana);
		ContratoCota contrato11 = Fixture.criarContratoCota(cotaJoana,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato11);


		cotaManoelCunha = Fixture.cota(1232, manoelCunha, SituacaoCadastro.ATIVO,box1);

		fornecedores.add(fornecedorAcme);
		fornecedores.add(fornecedorDinap);
		fornecedores.add(fornecedorFc);
		cotaManoelCunha.setFornecedores(fornecedores);

		save(session, cotaManoelCunha);
		ContratoCota contrato12 = Fixture.criarContratoCota(cotaManoelCunha,true,DateUtil.parseData("01/01/2012", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato12);



		Set<FormaCobranca> formasCobranca;


		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		
		parametroCobrancaManoel = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaManoel, 1,
				false, new BigDecimal(1000), TipoCota.CONSIGNADO);
		save(session, parametroCobrancaManoel);
		
		formaBoleto.setParametroCobrancaCota(parametroCobrancaManoel);
		formaBoleto.setPrincipal(true);
		save(session, formaBoleto);



		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaCheque);
		ParametroCobrancaCota parametroCobrancaConta1 =
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaJose, 1,
											  true, BigDecimal.TEN, null);
		save(session, parametroCobrancaConta1);
		formaCheque.setParametroCobrancaCota(parametroCobrancaConta1);
		formaCheque.setPrincipal(true);
		save(session, formaCheque);



		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDeposito);
		ParametroCobrancaCota parametroCobrancaConta2 =
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaMaria, 1,
											  true, BigDecimal.TEN, null);
		save(session, parametroCobrancaConta2);
		formaDeposito.setParametroCobrancaCota(parametroCobrancaConta2);
		formaDeposito.setPrincipal(true);
		save(session, formaDeposito);



		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDinheiro);
		ParametroCobrancaCota parametroCobrancaConta3 =
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaJoao, 1,
											  false, BigDecimal.TEN, null);
		save(session, parametroCobrancaConta3);
		formaDinheiro.setParametroCobrancaCota(parametroCobrancaConta3);
		formaDinheiro.setPrincipal(true);
		save(session, formaDinheiro);



		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaTransferenciBancaria);
		ParametroCobrancaCota parametroCobrancaConta4 =
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaLuis, 1,
											  true, BigDecimal.TEN, null);
		save(session, parametroCobrancaConta4);
		formaTransferenciBancaria.setParametroCobrancaCota(parametroCobrancaConta4);
		formaTransferenciBancaria.setPrincipal(true);
		save(session, formaTransferenciBancaria);
		
		Fixture.historicoTitularidade(cotaManoel);
	}

	private static void criarDistribuicaoCota(Session session) {

		ParametroDistribuicaoCota parametroGuilherme = 	Fixture.criarParametroDistribuidor(
				100, "Joao da Silva", DescricaoTipoEntrega.ENTREGADOR, "Muito importante isso aeh!",
				true, true, true, true, true, true, true, true, true);

		ParametroDistribuicaoCota parametroJoao = 	Fixture.criarParametroDistribuidor(
				120, "Maria da Silva", DescricaoTipoEntrega.ENTREGA_EM_BANCA, "Muito importante isso aeh também!",
				false, false, false, false, false, false, false, false, false);

		cotaJoao.setParametroDistribuicao(parametroJoao);
		cotaGuilherme.setParametroDistribuicao(parametroGuilherme);

		save(session, cotaJoao, cotaGuilherme);
	}

	private static void criarFornecedoresClean(Session session) {

		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorDinap.setCodigoInterface(9999999);
		fornecedorDinap.setResponsavel("Maria");
		fornecedorDinap.setOrigem(Origem.MANUAL);
		fornecedorDinap.setEmailNfe("maria@email.com");
		fornecedorFc = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorFc.setCodigoInterface(9999998);
		fornecedorFc.setResponsavel("Sebastião");
		fornecedorFc.setEmailNfe("sebastiao@email.com");
		fornecedorFc.setOrigem(Origem.MANUAL);
		fornecedorFc.setEmailNfe("acme@acme.com");

		save(session, fornecedorDinap, fornecedorFc);

		Endereco enderecoPrincipal = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-500", "Rua Marechal", "50", "Centro", "Mococa", "SP",3530508);

		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.RESIDENCIAL, "92130-330", "Avenida Brasil", "50", "Centro", "Mococa", "SP",3530508);


		EnderecoFornecedor enderecoFornecedorDinap = new EnderecoFornecedor();
		enderecoFornecedorDinap.setFornecedor(fornecedorDinap);
		enderecoFornecedorDinap.setPrincipal(true);
		enderecoFornecedorDinap.setEndereco(enderecoPrincipal);
		enderecoFornecedorDinap.setTipoEndereco(TipoEndereco.COMERCIAL);

		EnderecoFornecedor enderecoFornecedorFc = new EnderecoFornecedor();
		enderecoFornecedorFc.setFornecedor(fornecedorFc);
		enderecoFornecedorFc.setPrincipal(true);
		enderecoFornecedorFc.setEndereco(enderecoPrincipal);
		enderecoFornecedorFc.setTipoEndereco(TipoEndereco.COMERCIAL);

		save(session, enderecoPrincipal, endereco, enderecoFornecedorDinap, enderecoFornecedorFc);
		
		
		Telefone telefonePrincipalDinap = Fixture.telefone("11", "18250104", null);
		TelefoneFornecedor telefoneFornecedorDinap = new TelefoneFornecedor();
		telefoneFornecedorDinap.setPrincipal(true);
		telefoneFornecedorDinap.setFornecedor(fornecedorDinap);
		telefoneFornecedorDinap.setTelefone(telefonePrincipalDinap);
		telefoneFornecedorDinap.setTipoTelefone(TipoTelefone.CELULAR);

		Telefone telefonePrincipalFc = Fixture.telefone("19", "9871234", null);
		TelefoneFornecedor telefoneFornecedorFc = new TelefoneFornecedor();
		telefoneFornecedorFc.setPrincipal(true);
		telefoneFornecedorFc.setFornecedor(fornecedorFc);
		telefoneFornecedorFc.setTelefone(telefonePrincipalFc);
		telefoneFornecedorFc.setTipoTelefone(TipoTelefone.CELULAR);

		save(session, telefonePrincipalDinap, telefonePrincipalFc,
					  telefoneFornecedorDinap, telefoneFornecedorFc);

	}
	
	private static void criarFornecedores(Session session) {

		fornecedorAcme = Fixture.fornecedorAcme(tipoFornecedorOutros);
		fornecedorAcme.setCodigoInterface(123);
		fornecedorAcme.setResponsavel("João");
		fornecedorAcme.setOrigem(Origem.INTERFACE);
		fornecedorAcme.setEmailNfe("joao@email.com");
		

		save(session, fornecedorAcme);

		Endereco enderecoPrincipal = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-500", "Rua Marechal", "50", "Centro", "Mococa", "SP",3530508);

		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.RESIDENCIAL, "92130-330", "Avenida Brasil", "50", "Centro", "Mococa", "SP",3530508);

		EnderecoFornecedor enderecoFornecedorAcme = new EnderecoFornecedor();
		enderecoFornecedorAcme.setFornecedor(fornecedorAcme);
		enderecoFornecedorAcme.setPrincipal(true);
		enderecoFornecedorAcme.setEndereco(enderecoPrincipal);
		enderecoFornecedorAcme.setTipoEndereco(TipoEndereco.COMERCIAL);


		save(session, enderecoPrincipal, endereco, enderecoFornecedorAcme);

		enderecoFornecedorAcme = new EnderecoFornecedor();
		enderecoFornecedorAcme.setFornecedor(fornecedorAcme);
		enderecoFornecedorAcme.setPrincipal(false);
		enderecoFornecedorAcme.setEndereco(endereco);
		enderecoFornecedorAcme.setTipoEndereco(TipoEndereco.RESIDENCIAL);


		save(session, enderecoPrincipal, endereco, enderecoFornecedorAcme);

		Telefone telefonePrincipalAcme = Fixture.telefone("19", "3216549", null);
		TelefoneFornecedor telefoneFornecedorAcme = new TelefoneFornecedor();
		telefoneFornecedorAcme.setPrincipal(true);
		telefoneFornecedorAcme.setFornecedor(fornecedorAcme);
		telefoneFornecedorAcme.setTelefone(telefonePrincipalAcme);
		telefoneFornecedorAcme.setTipoTelefone(TipoTelefone.CELULAR);


		save(session, telefonePrincipalAcme, telefoneFornecedorAcme);

		telefonePrincipalAcme = Fixture.telefone("19", "75110240", null);
		telefoneFornecedorAcme = new TelefoneFornecedor();
		telefoneFornecedorAcme.setPrincipal(false);
		telefoneFornecedorAcme.setFornecedor(fornecedorAcme);
		telefoneFornecedorAcme.setTelefone(telefonePrincipalAcme);
		telefoneFornecedorAcme.setTipoTelefone(TipoTelefone.RESIDENCIAL);


		save(session, telefonePrincipalAcme, telefoneFornecedorAcme);

		Fornecedor fornecedor = Fixture.fornecedor(juridicaValida, SituacaoCadastro.ATIVO, false, tipoFornecedorOutros,123456);
		fornecedor.setEmailNfe("email@email.com");
		save(session, fornecedor);
	}

	private static void criarUsuarios(Session session) {
		usuarioJoao = Fixture.usuarioJoao();
		session.save(usuarioJoao);
	}

	private static void criarTiposNotaFiscal(Session session) {
		tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();
		tipoNotaFiscalDevolucao = Fixture.tipoNotaFiscalDevolucao();

		save(session, tipoNotaFiscalRecebimento, tipoNotaFiscalDevolucao);


		session.save(tipoNotaFiscalRecebimento);
	}

	private static void criarCFOP(Session session) {

		cfop1209 = Fixture.cfop1209();
		session.save(cfop1209);

		cfop1210 = Fixture.cfop1210();
		session.save(cfop1210);

		cfop5102 = Fixture.cfop5102();
		session.save(cfop5102);
	}



	private static void criarParametroEmissaoNotaFiscal(Session session) {

		parametroEmissaoNotaFiscalEntradaDevolucaoEncalhe =
				Fixture.parametroEmissaoNotaFiscal(
						cfop1209,
						cfop1210,
						GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE, "0002");

		session.save(parametroEmissaoNotaFiscalEntradaDevolucaoEncalhe);


		parametroEmissaoNotaFiscalDevolucaoMercadoria =
				Fixture.parametroEmissaoNotaFiscal(
						cfop1209,
						cfop1210,
						GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR,
						"0001");

		session.save(parametroEmissaoNotaFiscalDevolucaoMercadoria);


	}

	private static void criarTiposMovimento(Session session) {
		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		tipoMovimentoSobraEm = Fixture.tipoMovimentoSobraEm();
		tipoMovimentoSobraDe = Fixture.tipoMovimentoSobraDe();
		tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();

		tipoMovimentoNivelamentoEntrada = Fixture.tipoMovimentoNivelamentoEntrada();
		tipoMovimentoNivelamentoSaida = Fixture.tipoMovimentoNivelamentoSaida();
		
		tipoMovimentoCompraEncalhe = Fixture.tipoMovimentoCompraEncalhe();
		tipoMovimentoEstornoCompraEncalhe = Fixture.tipoMovimentoEstornoCompraEncalhe();

		tipoMovimentoVendaEncalhe = Fixture.tipoMovimentoVendaEncalhe();
		tipoMovimentoEstornoVendaEncalhe = Fixture.tipoMovimentoEstornoVendaEncalhe();

		tipoMovimentoVendaEncalheSuplementar = Fixture.tipoMovimentoVendaEncalheSuplementar();
		tipoMovimentoEncalheAntecipado = Fixture.tipoMovimentoEncalheAntecipado();
		tipoMovimentoEstornoVendaEncalheSuplementar = Fixture.tipoMovimentoEstornoVendaEncalheSuplementar();
		tipoMovimentoEstoqueCompraSuplementar = Fixture.tipoMovimentoCompraSuplementar();
		tipoMovimentoEstoqueEstornoCompraSuplementar = Fixture.tipoMovimentoEstornoCompraSuplementar();

		tipoMovimentoFinanceiroCompraEncalhe = Fixture.tipoMovimentoFinanceiroCompraEncalhe();

		save(session, tipoMovimentoVendaEncalhe,tipoMovimentoFinanceiroCompraEncalhe,tipoMovimentoEstornoVendaEncalhe,tipoMovimentoVendaEncalheSuplementar,
					  tipoMovimentoEstornoVendaEncalheSuplementar,tipoMovimentoEstoqueCompraSuplementar,tipoMovimentoEstoqueEstornoCompraSuplementar, 
					  tipoMovimentoEncalheAntecipado,tipoMovimentoCompraEncalhe,tipoMovimentoEstornoCompraEncalhe);


		tipoMovimentoSuplementarCotaAusente = Fixture.tipoMovimentoSuplementarCotaAusente();
		
		tipoMovimentoEstornoCotaEnvioReparte = Fixture.tipoMovimentoEstornoCotaEstornoEnvioReparte();
		tipoMovimentoEntradaSuplementarEnvioReparte = Fixture.tipoMovimentoEntradaSuplementarEnvioReparte();

		tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();

		tipoMovimentoRecebimentoEncalhe 						= Fixture.tipoMovimentoRecebimentoEncalhe();
		tipoMovimentoRecebimentoEncalheJuramentado 				= Fixture.tipoMovimentoRecebimentoEncalheJuramentado();
		tipoMovimentoSuplementarEnvioEncalheAnteriroProgramacao = Fixture.tipoMovimentoSuplementarEnvioEncalheAnteriroProgramacao();


		tipoMovimentoEstornoCotaAusente = Fixture.tipoMovimentoEstornoCotaAusente();

		tipoMovimentoReparteCotaAusente = Fixture.tipoMovimentoReparteCotaAusente();

		tipoMovimentoRestautacaoReparteCotaAusente = Fixture.tipoMovimentoRestauracaoReparteCotaAusente();

		save(session,tipoMovimentoReparteCotaAusente,tipoMovimentoRestautacaoReparteCotaAusente);

		tipoMovimentoFinanceiroCredito = Fixture.tipoMovimentoFinanceiroCredito();
		tipoMovimentoFinanceiroDebito = Fixture.tipoMovimentoFinanceiroDebito();
		tipoMovimentoFinanceiroDebitoNA = Fixture.tipoMovimentoFinanceiroDebitoNA();
		tipoMovimentoFinanceiroDebitoFaturamento = Fixture.tipoMovimentoFinanceiroDebitoFaturamento();
		tipoMovimentoFinanceiroRecebimentoReparte = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		tipoMovimentoFinanceiroJuros = Fixture.tipoMovimentoFinanceiroJuros();
		tipoMovimentoFinanceiroMulta = Fixture.tipoMovimentoFinanceiroMulta();
		tipoMovimentoFinanceiroEnvioEncalhe = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();

		tipoMovimentoEnvioJornaleiro = Fixture.tipoMovimentoEnvioJornaleiro();

		save(session, tipoMovimentoEnvioJornaleiro,
				tipoMovimentoEstornoCotaAusente);

		tipoMovimentoFinanceiroCredito.setAprovacaoAutomatica(false);
		tipoMovimentoFinanceiroDebito.setAprovacaoAutomatica(false);
		tipoMovimentoFinanceiroDebitoNA.setAprovacaoAutomatica(false);

		tipoMovimentoCancelamentoNFDevolucaoConsignado = Fixture.tipoMovimentoCancelamentoNFDevolucaoConsignado();
		tipoMovimentoCancelamentoNFEnvioConsignado = Fixture.tipoMovimentoCancelamentoNFEnvioConsignado();
		
		tipoMovimentoTransferenciaEntradaLancamento = Fixture.tipoMovimentoTransferenciaEntradaLancamento();
		tipoMovimentoTransferenciaSaidaLancamento = Fixture.tipoMovimentoTransferenciaSaidaLancamento();
		tipoMovimentoTransferenciaEntradaSuplementar = Fixture.tipoMovimentoTransferenciaEntradaSuplementar();
		tipoMovimentoTransferenciaSaidaSuplementar = Fixture.tipoMovimentoTransferenciaSaidaSuplementar();
		tipoMovimentoTransferenciaEntradaRecolhimento = Fixture.tipoMovimentoTransferenciaEntradaRecolhimento();
		tipoMovimentoTransferenciaSaidaRecolhimento = Fixture.tipoMovimentoTransferenciaSaidaRecolhimento();
		tipoMovimentoTransferenciaEntradaProdutosDanificados = Fixture.tipoMovimentoTransferenciaEntradaProdutosDanificados();
		tipoMovimentoTransferenciaSaidaProdutosDanificados = Fixture.tipoMovimentoTransferenciaSaidaProdutosDanificados();
		
		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe, tipoMovimentoSuplementarCotaAusente,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte,
				tipoMovimentoFinanceiroCredito, tipoMovimentoFinanceiroDebito, tipoMovimentoFinanceiroDebitoNA,
				tipoMovimentoFinanceiroDebitoFaturamento,tipoMovimentoEnvioEncalhe, tipoMovimentoFinanceiroRecebimentoReparte,
				tipoMovimentoFinanceiroJuros, tipoMovimentoFinanceiroMulta,
				tipoMovimentoFinanceiroEnvioEncalhe, tipoMovimentoSuplementarCotaAusente,
				tipoMovimentoRecebimentoEncalhe, tipoMovimentoRecebimentoEncalheJuramentado,
				tipoMovimentoSuplementarEnvioEncalheAnteriroProgramacao, 
				tipoMovimentoCancelamentoNFDevolucaoConsignado, tipoMovimentoCancelamentoNFEnvioConsignado, 
				tipoMovimentoTransferenciaEntradaLancamento, tipoMovimentoTransferenciaSaidaLancamento, 
				tipoMovimentoTransferenciaEntradaSuplementar, tipoMovimentoTransferenciaSaidaSuplementar,
				tipoMovimentoTransferenciaEntradaRecolhimento, tipoMovimentoTransferenciaSaidaRecolhimento,
				tipoMovimentoTransferenciaEntradaProdutosDanificados, tipoMovimentoTransferenciaSaidaProdutosDanificados);

	}

	private static void criarDiasDistribuicaoFornecedores(Session session) {
		DistribuicaoFornecedor dinapSegunda = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		DistribuicaoFornecedor dinapQuarta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		DistribuicaoFornecedor dinapQuinta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUINTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		DistribuicaoFornecedor dinapSexta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		save(session, dinapSegunda, dinapQuarta, dinapQuinta, dinapSexta);

		DistribuicaoFornecedor fcSegunda = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		DistribuicaoFornecedor fcSexta = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		save(session, fcSegunda, fcSexta);

		DistribuicaoFornecedor dinapSextaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);

		DistribuicaoFornecedor fcSextaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);
		
		DistribuicaoFornecedor dinapQuartaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);

		DistribuicaoFornecedor fcQuartaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);

		save(session, dinapSextaRecolhimento, fcSextaRecolhimento,
				  	  dinapQuartaRecolhimento, fcQuartaRecolhimento);
	}

	private static void criarDiasDistribuicaoDistribuidor(Session session) {

		DistribuicaoDistribuidor recolhimentoDistribuidorTerca =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.TERCA_FEIRA,
											 OperacaoDistribuidor.RECOLHIMENTO);

		DistribuicaoDistribuidor recolhimentoDistribuidorQuinta =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.QUINTA_FEIRA,
											 OperacaoDistribuidor.RECOLHIMENTO);

		DistribuicaoDistribuidor distribuicaoDistribuidorTerca =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.TERCA_FEIRA,
											 OperacaoDistribuidor.DISTRIBUICAO);

		DistribuicaoDistribuidor distribuicaoDistribuidorSegunda =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.SEGUNDA_FEIRA,
											 OperacaoDistribuidor.DISTRIBUICAO);

		DistribuicaoDistribuidor distribuicaoDistribuidorQuinta =
				Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.QUINTA_FEIRA,
												 OperacaoDistribuidor.DISTRIBUICAO);

		DistribuicaoDistribuidor distribuicaoDistribuidorSexta =
				Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.SEXTA_FEIRA,
												 OperacaoDistribuidor.DISTRIBUICAO);

		save(session, recolhimentoDistribuidorTerca, recolhimentoDistribuidorQuinta,
					  distribuicaoDistribuidorTerca, distribuicaoDistribuidorSegunda,
					  distribuicaoDistribuidorQuinta, distribuicaoDistribuidorSexta);
	}

	private static void gerarCargaDiferencaEstoque(Session session,
												   int quantidadeRegistros,
												   ProdutoEdicao produtoEdicao,
												   TipoMovimentoEstoque tipoMovimento,
												   Usuario usuario,
												   EstoqueProduto estoqueProduto,
												   TipoDiferenca tipoDiferenca,
												   TipoDirecionamentoDiferenca tipoDirecionamento) {

		for (int i = 1; i <= quantidadeRegistros; i++) {

			MovimentoEstoque movimentoEstoqueDiferenca =
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), BigInteger.valueOf(i), StatusAprovacao.APROVADO, null);

			session.save(movimentoEstoqueDiferenca);

			Diferenca diferenca =
				Fixture.diferenca(
					BigInteger.valueOf(i), usuario, produtoEdicao, tipoDiferenca,
						StatusConfirmacao.PENDENTE, null,true, TipoEstoque.LANCAMENTO,tipoDirecionamento, new Date());
			
			save(session, diferenca);
			
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(tipoDirecionamento)) {
				RateioDiferenca rateio = Fixture.criarRateioDiferenca(cotaManoel, new Date(), BigInteger.valueOf(i), estudoCotaManoel, diferenca);
				session.save(rateio);
			}
		}

		for (int i = 1; i <= quantidadeRegistros; i++) {

			MovimentoEstoque movimentoEstoqueDiferenca =
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), BigInteger.valueOf(i), StatusAprovacao.PENDENTE, "Motivo");

			session.save(movimentoEstoqueDiferenca);

			Diferenca diferenca =
				Fixture.diferenca(
					BigInteger.valueOf(i), usuario, produtoEdicao, tipoDiferenca,
						StatusConfirmacao.CONFIRMADO, null, true, TipoEstoque.LANCAMENTO,tipoDirecionamento,new Date());

			session.save(diferenca);
			
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(tipoDirecionamento)) {
				RateioDiferenca rateio = Fixture.criarRateioDiferenca(cotaManoel, new Date(), BigInteger.valueOf(i), estudoCotaManoel, diferenca);
				session.save(rateio);
			}
			
			LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
			lancamentoDiferenca.setStatus(StatusAprovacao.APROVADO);
			lancamentoDiferenca.setMovimentoEstoque(movimentoEstoqueDiferenca);

			diferenca.setLancamentoDiferenca(lancamentoDiferenca);
			
			session.save(lancamentoDiferenca);
		}
	}


	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
			session.flush();
		}
	}

	private static void detach(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.evict(entidade);
		}
	}

	@SuppressWarnings("unchecked")
	protected static <T extends Object> T merge(Session session, T entidade) {

		entidade = (T) session.merge(entidade);

		session.flush();
		session.clear();

		return entidade;
	}

	/**
	 * Gera massa de dados para o teste de Resumo de Expedicao agrupadas por produto
	 * @param session
	 */
	private static void carregarDadosParaResumoExpedicao(Session session){

		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevista);
		session.save(tipoRevista);

		CFOP cfop = Fixture.cfop5102();
		session.save(cfop);

		Usuario usuario = Fixture.usuarioJoao();
		session.save(usuario);

		TipoMovimentoEstoque tipoMovimentoSobraDe  = Fixture.tipoMovimentoSobraDe();
		session.save(tipoMovimentoSobraDe);

		TipoMovimentoEstoque tipoMovimentoFaltDe  = Fixture.tipoMovimentoFaltaDe();
		session.save(tipoMovimentoFaltDe);

		TipoMovimentoEstoque tipoMovimentoFaltEM  = Fixture.tipoMovimentoFaltaEm();
		session.save(tipoMovimentoFaltEM);

		int indDiferenca = 10;

		for(Integer i=0;i<10; i++) {

			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"00.000.000/0001-00", "000000000003", "acme@mail.com", "99.999-9");
			session.save(juridica);

			TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
			session.save(tipoFornecedorPublicacao);

			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao, null);
			session.save(fornecedor);

			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
			produto.addFornecedor(fornecedor);
			session.save(produto);

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao("COD_II", i.longValue(), 50, 40,
					new Long(3000), new BigDecimal(20), new BigDecimal(10), "ZZZ", 24L, produto, null, false, "");
			session.save(produtoEdicao);



			List<ItemRecebimentoFisico> listaRecebimentos = new ArrayList<ItemRecebimentoFisico>() ;

			EstoqueProduto estoque  =  Fixture.estoqueProduto(produtoEdicao, BigInteger.ZERO);
			session.save(estoque);

			for(int x= 1; x< 3 ;x++){

				NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
						.notaFiscalEntradaFornecedor(cfop, fornecedor, tipoNotaFiscalDevolucao,
								usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
				session.save(notaFiscalFornecedor);

				ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
						produtoEdicao, usuario, notaFiscalFornecedor,
						Fixture.criarData(23, Calendar.FEBRUARY, 2012), new Date(),TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(i));
				session.save(itemNotaFiscal);


				RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
					notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
				session.save(recebimentoFisico);

				ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
						itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(i+x));
				session.save(itemFisico);


				MovimentoEstoque movimentoEstoque  =
					Fixture.movimentoEstoque(itemFisico, produtoEdicao, tipoMovimentoFaltDe, usuario,
						estoque, new Date(), BigInteger.valueOf(1),
						StatusAprovacao.APROVADO, "Aprovado");

				session.save(movimentoEstoque);

				if(indDiferenca > 5){


					Diferenca diferenca = Fixture.diferenca(BigInteger.valueOf(10), 
							usuario, produtoEdicao, TipoDiferenca.SOBRA_DE, 
							StatusConfirmacao.CONFIRMADO, itemFisico, true, 
							TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.ESTOQUE,new Date());
					
					LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
					
					lancamentoDiferenca.setMovimentoEstoque(movimentoEstoque);

					diferenca.setLancamentoDiferenca(lancamentoDiferenca);
					save(session, lancamentoDiferenca,diferenca);

					itemFisico.setDiferenca(diferenca);
					session.update(itemFisico);

					indDiferenca --;
				}


				listaRecebimentos.add(itemFisico);
			}

			Expedicao expedicao = Fixture.expedicao(usuario,Fixture.criarData(1, 3, 2010));
			session.save(expedicao);

			Lancamento lancamento = Fixture.lancamentos(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					BigInteger.valueOf(100),
					StatusLancamento.EXPEDIDO,
					listaRecebimentos, 1);
			lancamento.setReparte(BigInteger.valueOf(10));
			lancamento.setExpedicao(expedicao);
			session.save(lancamento);

			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(BigInteger.valueOf(i));
			estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
			estudo.setDataCadastro(new Date());
			session.save(estudo);
		}
	}



	/**
	 * Gera massa de dados para teste de consulta de lan�amentos agrupadas por Box
	 * @param session
	 */
	public static void carregarDadosParaResumoExpedicaoBox(Session session){

		criarDistribuidor(session);
		criarParametrosSistema(session);
		criarPessoas(session);
		criarUsuarios(session);
		criarTiposFornecedores(session);
		criarFornecedores(session);
		criarDiasDistribuicaoFornecedores(session);
		criarBox(session);
		criarCotas(session);
		criarTiposProduto(session);
//		criarProdutos(session);
//		criarProdutosEdicao(session);
		criarCFOP(session);
		criarParametroEmissaoNotaFiscal(session);
		criarTiposMovimento(session);
		criarTiposNotaFiscal(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarMovimentosEstoque(session);
		criarLancamentosExpedidos(session);
		criarEstudos(session);
		
		massaConferenciaParaMovimentoEstoqueCota(session);
		
		criarMovimentosEstoqueCota(session);
		criarEstudosCota(session);

		MovimentoEstoque movimentoEstoqueDiferenca =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca);

		MovimentoEstoque movimentoEstoqueDiferenca2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(2),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca2);

		MovimentoEstoque movimentoEstoqueDiferenca3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(3),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca3);

		MovimentoEstoque movimentoEstoqueDiferenca4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(4),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca4);


		Diferenca diferenca =
			Fixture.diferenca(BigInteger.valueOf(1), usuarioJoao, produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
							  StatusConfirmacao.CONFIRMADO, null, true, TipoEstoque.LANCAMENTO, TipoDirecionamentoDiferenca.COTA, new Date());
		
		LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
		
		lancamentoDiferenca.setMovimentoEstoque(movimentoEstoqueDiferenca);

		diferenca.setLancamentoDiferenca(lancamentoDiferenca);
		save(session,lancamentoDiferenca, diferenca);

		Diferenca diferenca2 =
			Fixture.diferenca(BigInteger.valueOf(2), usuarioJoao, produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
							  StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, true,TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.ESTOQUE, new Date());
		
		LancamentoDiferenca lancamentoDiferenca2 = new LancamentoDiferenca();
		
		lancamentoDiferenca2.setMovimentoEstoque(movimentoEstoqueDiferenca2);

		diferenca.setLancamentoDiferenca(lancamentoDiferenca);
		save(session,lancamentoDiferenca2, diferenca2);

		Diferenca diferenca3 =
			Fixture.diferenca(BigInteger.valueOf(3), usuarioJoao, produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
							  StatusConfirmacao.CONFIRMADO, null, true, TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.COTA, new Date());
		
		LancamentoDiferenca lancamentoDiferenca3 = new LancamentoDiferenca();
		
		lancamentoDiferenca3.setMovimentoEstoque(movimentoEstoqueDiferenca3);

		diferenca.setLancamentoDiferenca(lancamentoDiferenca);
		save(session,lancamentoDiferenca3, diferenca3);

		Diferenca diferenca4 =
			Fixture.diferenca(BigInteger.valueOf(4), usuarioJoao, produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
					          StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico,true, TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.ESTOQUE, new Date());
		
		LancamentoDiferenca lancamentoDiferenca4 = new LancamentoDiferenca();
		
		lancamentoDiferenca4.setMovimentoEstoque(movimentoEstoqueDiferenca4);

		diferenca.setLancamentoDiferenca(lancamentoDiferenca);
		
		save(session,lancamentoDiferenca4, diferenca4);

		// Fim dos inserts na tabela DIFERENCA

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja1, tipoMovimentoFaltaEm,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM,TipoDirecionamentoDiferenca.COTA);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE,TipoDirecionamentoDiferenca.ESTOQUE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja3, tipoMovimentoSobraDe,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE,TipoDirecionamentoDiferenca.ESTOQUE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja4, tipoMovimentoSobraEm,
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM,TipoDirecionamentoDiferenca.ESTOQUE);

		RateioDiferenca rateioDiferencaCotaManoel = Fixture.rateioDiferenca(BigInteger.valueOf(10), cotaManoel, diferenca3, estudoCotaSuper1Manoel, new Date());
		session.save(rateioDiferencaCotaManoel);

		RateioDiferenca rateioDiferencaJose = Fixture.rateioDiferenca(BigInteger.valueOf(10), cotaJose, diferenca, estudoCotaVeja2Joao, new Date());
		session.save(rateioDiferencaJose);

	}


	private static void criarBox(Session session){

		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		session.save(box1);

		box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		session.save(box2);

		boxPostoAvancado = Fixture.criarBox(3, "BX-AVANCADO", TipoBox.POSTO_AVANCADO);
		session.save(boxPostoAvancado);

		box300Reparte = Fixture.boxReparte300();
		session.save(box300Reparte);



		for (int i = 4; i < 100; i++) {
			session.save(Fixture.criarBox(i, "BX-"+i, TipoBox.LANCAMENTO));
		}
	}


	private static void criarPessoasClean(Session session) {
		juridicaTreelog = Fixture.pessoaJuridica("Treelog",
				"61.438.248/0001-23", "000000000000", "sys.discover@gmail.com", "99.999-9");
		juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11111111000111", "111111111111", "sys.discover@gmail.com", "99.999-9");
		juridicaFc = Fixture.pessoaJuridica("FC",
				"28.322.873/0001-30", "222222222222", "sys.discover@gmail.com", "99.999-9");
		
		save(session, juridicaTreelog, juridicaDinap, juridicaFc);
		
	}
	
	private static void criarPessoas(Session session){
				 
		juridicaValida = Fixture.pessoaJuridica("Juridica Valida",
				"93081738000101", "333333333333", "sys.discover@gmail.com", "99.999-9");
		juridicaAcme = Fixture.pessoaJuridica("Acme",
				"10000000000100", "000000000004", "sys.discover@gmail.com", "99.999-9");

		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva", "12654879-9", "SSP", "SP", DateUtil.parseDataPTBR("13/09/1979"), EstadoCivil.SOLTEIRO);

		manoelCunha = Fixture.pessoaFisica("45300458970",
				"sys.discover@gmail.com", "Manoel da Cunha");

		jose = Fixture.pessoaFisica("12345678901",
				"sys.discover@gmail.com", "Jose da Silva");

		maria = Fixture.pessoaFisica("12345678902",
				"sys.discover@gmail.com", "Maria da Silva");

		guilherme = Fixture.pessoaFisica("99933355511", "sys.discover@gmail.com", "Guilherme de Morais Leandro");

		murilo = Fixture.pessoaFisica("99933355522", "sys.discover@gmail.com", "Murilo");

		mariana = Fixture.pessoaFisica("99933355533", "sys.discover@gmail.com", "Mariana");

		orlando = Fixture.pessoaFisica("99933355544", "sys.discover@gmail.com", "Orlando");

		luis  = Fixture.pessoaFisica("12345678903",
				"sys.discover@gmail.com", "Luis Silva");

	    joao = Fixture.pessoaFisica("12345678904",
				"sys.discover@gmail.com", "João da Silva");

	    PessoaFisica joana  = Fixture.pessoaFisica("12345678905", "sys.discover@gmail.com", "Joana");

		save(session, juridicaAcme, juridicaValida,manoel,manoelCunha,jose,maria,
				guilherme,murilo,mariana,orlando,luis,joao, joana);

	}



	//FINANCEIRO - CONSULTA BOLETOS
	private static void criarBoletos(Session session) {

		//Boletos já pagos
		Boleto boleto1 = Fixture.boleto("1309309032012", "440", "1309309032012440",
				                        new Date(),  new Date(),
				                        new Date(), BigDecimal.ZERO, new BigDecimal(100),
										"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
										cotaManoel, bancoHSBC, divida1,0);

		Boleto boleto2 = Fixture.boleto("1309709032012", "747", "1309709032012747",
				                        new Date(), new Date(),
				                        new Date(),  BigDecimal.ZERO, new BigDecimal(200),
		                				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
		                				cotaManoel, bancoHSBC, divida2,0);


		//Boletos vencidos
		Boleto boleto3 = Fixture.boleto("1310209032012", "740", "1310209032012740",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"),
										null,  BigDecimal.ZERO, new BigDecimal(300),
										"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
										cotaManoel, bancoHSBC, divida3,0);

		Boleto boleto4 = Fixture.boleto("1310609032012", "041", "1310609032012041",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"),
		                				null, BigDecimal.ZERO, new BigDecimal(400),
		                				"TIPO_BAIXA", "ACAO",  StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida4,0);

		Boleto boleto5 = Fixture.boleto("1310809032012", "641", "1310809032012641",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"),
		                				null, BigDecimal.ZERO, new BigDecimal(500), "TIPO_BAIXA",
		                				"ACAO", StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida5,0);


		//Boletos não vencidos
		Boleto boleto6 = Fixture.boleto("1311009032012", "840", "1311009032012840",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
		                				null, BigDecimal.ZERO, new BigDecimal(2258.62),
		                				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida6,0);

		Boleto boleto7 = Fixture.boleto("1311109032012", "642", "1311109032012642",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
                						null, BigDecimal.ZERO, new BigDecimal(700),
                						"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
                						cotaManoel, bancoHSBC, divida7,0);

		Boleto boleto8 = Fixture.boleto("1312309032012", "043", "1312309032012043",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
                						null, BigDecimal.ZERO, new BigDecimal(800),
                						"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
                						cotaManoel, bancoHSBC, divida8,0);







		Boleto boleto9 = Fixture.boleto("1311009032025", "840", "1311009032012825",
						                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
										null, BigDecimal.ZERO, new BigDecimal(2258.62),
										"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
										cotaManoelCunha, bancoHSBC, divida15,0);

		Boleto boleto10 = Fixture.boleto("1311109032026", "642", "1311109032012626",
						                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
										null, BigDecimal.ZERO, new BigDecimal(700),
										"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
										cotaManoelCunha, bancoHSBC, divida16,0);

		Boleto boleto11 = Fixture.boleto("1312309032027", "043", "1312309032012027",
						                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
										null, BigDecimal.ZERO, new BigDecimal(800),
										"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
										cotaManoelCunha, bancoHSBC, divida17,0);






		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6,
				      boleto7, boleto8,boleto9,boleto10,boleto11);

	}


	private static void criarCobrancaCheque(Session session){

		CobrancaCheque cobranca = Fixture.cobrancaCheque("1234567890118",
                DateUtil.parseDataPTBR("11/03/2012"), DateUtil.parseDataPTBR("15/03/2012"),
                null,BigDecimal.ZERO,new BigDecimal(900),
                "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
                cotaJose,divida14,0);

		CobrancaCheque cobranca1 = Fixture.cobrancaCheque("1012345678812",
                new Date(),DateUtil.adicionarDias(new Date(), 30) ,
	             null,BigDecimal.ZERO,new BigDecimal(1000),
	             "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
	             cotaJose,divida13,0);

		save(session, cobranca,cobranca1);
	}

	private static void criarCobrancaDinheiro(Session session){


		CobrancaDinheiro cobranca = Fixture.cobrancaDinheiro("1313209032012",
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
				 null, BigDecimal.ZERO, new BigDecimal(1100),
				 "TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				 cotaLuis, bancoHSBC, divida11,0);

		save(session, cobranca);
	}

	private static void criarCobrancaTranferenciaBancaria(Session session){


		CobrancaTransferenciaBancaria cobranca1 = Fixture.cobrancaTransferencaiBancaria("1234567821123",
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("15/03/2012"),
                null,BigDecimal.ZERO,new BigDecimal(1200),
                "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
                cotaJoao,bancoITAU,divida12,0);

		save(session, cobranca1);
	}

	private static void criarCobrancaDepositoBancaria(Session session){

		CobrancaDeposito cobranca = Fixture.cobrancaDeposito("1312409032012",
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
				null, BigDecimal.ZERO, new BigDecimal(1300),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMaria, bancoHSBC, divida9,0);

		CobrancaDeposito cobranca1 = Fixture.cobrancaDeposito("1312509032012",
                 DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
				 null, BigDecimal.ZERO, new BigDecimal(1400),
				 "TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				 cotaMaria, bancoHSBC, divida10,0);

		save(session, cobranca,cobranca1);
	}

	private static void criarFeriado(Session session) {

//Fixos
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("01/01/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Confraternização Universal",
				false,
				false,
				true));

		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("21/04/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Tiradentes",
				false,
				false,
				true));		
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("01/05/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Dia Mundial do Trabalho",
				false,
				false,
				true));	
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("07/09/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Independência do Brasil",
				false,
				false,
				true));		
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("12/10/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Nossa Senhora Aparecida",
				false,
				false,
				true));				
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("02/11/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Finados",
				false,
				false,
				true));				

		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("15/11/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Proclamação da República",
				false,
				false,
				true));	
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("25/12/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Natal",
				false,
				false,
				true));	
		
//Moveis		
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("06/04/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Paixão de Cristo",
				false,
				false,
				false));
		
		save(session, Fixture.feriado(
				DateUtil.parseDataPTBR("07/06/2012"), 
				TipoFeriado.FEDERAL, 
				null, 
				null, 
				"Corpus Christi",
				false,
				false,
				false));
		
	}
	
	private static void criarEnderecoPDV(Session session){
		
		EnderecoPDV enderecoPDV = new EnderecoPDV();
		enderecoPDV.setEndereco(enderecoMococa1);
		enderecoPDV.setPrincipal(true);
		enderecoPDV.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV.setPdv(pdvManoel);
		
		EnderecoPDV enderecoPDV1 = new EnderecoPDV();
		enderecoPDV1.setEndereco(enderecoMococa2);
		enderecoPDV1.setPrincipal(true);
		enderecoPDV1.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV1.setPdv(pdvMaria);
		
		EnderecoPDV enderecoPDV2 = new EnderecoPDV();
		enderecoPDV2.setEndereco(enderecoLuisMococa3);
		enderecoPDV2.setPrincipal(true);
		enderecoPDV2.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV2.setPdv(pdvLuis);
		
		EnderecoPDV enderecoPDV3 = new EnderecoPDV();
		enderecoPDV3.setEndereco(enderecoRioPardo1);
		enderecoPDV3.setPrincipal(true);
		enderecoPDV3.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV3.setPdv(pdvJoao);
		
		EnderecoPDV enderecoPDV4 = new EnderecoPDV();
		enderecoPDV4.setEndereco(enderecoRioPardo2);
		enderecoPDV4.setPrincipal(true);
		enderecoPDV4.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV4.setPdv(pdvJose);
		
		EnderecoPDV enderecoPDV5 = new EnderecoPDV();
		enderecoPDV5.setEndereco(enderecoRioPardo3);
		enderecoPDV5.setPrincipal(true);
		enderecoPDV5.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoPDV5.setPdv(pdvGuilherme);
		
		save(session, enderecoPDV,enderecoPDV1,enderecoPDV2,enderecoPDV3,enderecoPDV4,enderecoPDV5);
		
	}
	
	private static void criarEndereco(Session session){
		
		enderecoMococa1 = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", "50", "Centro", "Mococa", "SP",3530508);
		
		enderecoMococa2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", "51", "Vila Carvalho", "Mococa", "SP",3530508);
		
		enderecoLuisMococa3 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X Alvorada", "52", "Vila Carvalho", "Mococa", "SP",3530508);
		
		enderecoRioPardo1 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13720-000", "Rua X Da silva", "50", "Vila Carvalho", "São Jose do Rio Pardo", "SP",3549706);
		
		enderecoRioPardo2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13720-000", "Rua Jose", "52", "Vila Jose", "São Jose do Rio Pardo", "SP",3549706);
		
		enderecoRioPardo3 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13720-000", "Rua Jose da Silva", "51", "Vila Jose", "São Jose do Rio Pardo", "SP",3549706);
		
		save(session, enderecoMococa1,enderecoMococa2,enderecoLuisMococa3,enderecoRioPardo1,enderecoRioPardo2,enderecoRioPardo3);

	}

	private static void criarEnderecoCotaPF(Session session) {
		
		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cotaManoel);
		enderecoCota.setEndereco(enderecoMococa1);
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);

		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cotaMaria);
		enderecoCota2.setEndereco(enderecoMococa2);
		enderecoCota2.setPrincipal(true);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);

		EnderecoCota enderecoCotaLuis = new EnderecoCota();
		enderecoCotaLuis.setCota(cotaLuis);
		enderecoCotaLuis.setEndereco(enderecoLuisMococa3);
		enderecoCotaLuis.setPrincipal(false);
		enderecoCotaLuis.setTipoEndereco(TipoEndereco.COBRANCA);

		EnderecoCota enderecoCotaJoao = new EnderecoCota();
		enderecoCotaJoao.setCota(cotaJoao);
		enderecoCotaJoao.setEndereco(enderecoRioPardo1);
		enderecoCotaJoao.setPrincipal(false);
		enderecoCotaJoao.setTipoEndereco(TipoEndereco.COBRANCA);
		
		EnderecoCota enderecoCotaJose = new EnderecoCota();
		enderecoCotaJose.setCota(cotaJose);
		enderecoCotaJose.setEndereco(enderecoRioPardo2);
		enderecoCotaJose.setPrincipal(true);
		enderecoCotaJose.setTipoEndereco(TipoEndereco.COBRANCA);
				
		EnderecoCota enderecoCotaGuilherme = new EnderecoCota();
		enderecoCotaGuilherme.setCota(cotaGuilherme);
		enderecoCotaGuilherme.setEndereco(enderecoRioPardo3);
		enderecoCotaGuilherme.setPrincipal(true);
		enderecoCotaGuilherme.setTipoEndereco(TipoEndereco.COBRANCA);
		
		save(session, enderecoCota,enderecoCota2, enderecoCotaLuis,enderecoCotaJoao,enderecoCotaJose, enderecoCotaGuilherme);
	}

	private static void criarTelefoneCotaPF(Session session) {
		
		Telefone telefoneJose = Fixture.telefone("019", "99999999", "12");
		
		TelefoneCota telefoneCotaJose = new TelefoneCota();
		telefoneCotaJose.setCota(cotaJose);
		telefoneCotaJose.setTelefone(telefoneJose);
		telefoneCotaJose.setPrincipal(true);
		telefoneCotaJose.setTipoTelefone(TipoTelefone.COMERCIAL);
		
		save(session, telefoneJose, telefoneCotaJose);
		
	}
	
	private static void dadosExpedicao(Session session) {

		Box box300Reparte = Fixture.boxReparte300();
		save(session,box300Reparte);


		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevista);
		save(session,tipoRevista);

		CFOP cfop = Fixture.cfop5102();
		save(session,cfop);

		Usuario usuario = Fixture.usuarioJoao();
		save(session,usuario);

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedorPublicacao);

		for(Integer i=1000;i<1050; i++) {

			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"30.000.000/0001-00", "000000000005", "acme@mail.com", "99.999-9");
			save(session,juridica);

			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao, null);
			save(session,fornecedor);

			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
			produto.addFornecedor(fornecedor);
			save(session,produto);

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao("COD_JJ", i.longValue(), 50, 40,
					new Long(3000), new BigDecimal(20), new BigDecimal(10), "ZZ2", 25L, produto, null, false);
			save(session,produtoEdicao);

			NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop, fornecedor, tipoNotaFiscalRecebimento,
							usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
			save(session,notaFiscalFornecedor);

			ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
					produtoEdicao, usuario, notaFiscalFornecedor,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					DateUtil.adicionarDias(Fixture.criarData(23, Calendar.FEBRUARY, 2012), 7),
					TipoLancamento.LANCAMENTO,
					BigInteger.valueOf(i));
			save(session,itemNotaFiscal);

			RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			save(session,recebimentoFisico);


			ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
					itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(i));
			save(session,itemFisico);

			Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					BigInteger.valueOf(100),
					StatusLancamento.CONFIRMADO,
					itemFisico, 1);
			lancamento.setReparte(BigInteger.valueOf(10));
			save(session,lancamento);

			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(BigInteger.valueOf(10));
			estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
			estudo.setDataCadastro(new Date());
			save(session,estudo);

			Pessoa pessoa = Fixture.pessoaJuridica("razaoS"+i, "CNPK" + i, "ie"+i, "email"+i,"99.999-9");
			Cota cota = Fixture.cota(i, pessoa, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(3), BigInteger.valueOf(3),
					estudo, cota);
			save(session,pessoa,cota,estudoCota);

			Pessoa pessoa2 = Fixture.pessoaJuridica("razaoS2"+i, "CNPK" + i, "ie"+i, "email"+i, "99.999-9");
			Cota cota2 = Fixture.cota(i, pessoa2, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota2 = Fixture.estudoCota(BigInteger.valueOf(7), BigInteger.valueOf(7),
					estudo, cota2);
			save(session, pessoa2,cota2,estudoCota2);


			TipoMovimento tipoMovimento = Fixture.tipoMovimentoRecebimentoReparte();

			TipoMovimento tipoMovimento2 = Fixture.tipoMovimentoEnvioJornaleiro();
			save(session,tipoMovimento,tipoMovimento2);
		}
	}

	private static void criarBanco(Session session) {

		bancoHSBC = Fixture.banco(454L, true, 1, "1010",
							  164L, "1", "6", "Instrucoes HSBC.", "HSBC","BANCO HSBC S/A", "399", BigDecimal.ONE, BigDecimal.ZERO);

		bancoITAU = Fixture.banco(10L, true, 1, "1010",
				  12345L, "1", "1", "Instrucoes ITAU.", "ITAU", "BANCO ITAU S/A", "184", BigDecimal.TEN, BigDecimal.ONE);

		bancoDOBRASIL = Fixture.banco(10L, true, 1, "1010",
				  123456L, "1", "1", "Instrucoes DOBRASIL.", "BB", "BANCO DO BRASIL", "001", BigDecimal.ZERO, BigDecimal.ONE);

		bancoBRADESCO = Fixture.banco(10L, true, 1, "1010",
				  123456L, "1", "1", "Instrucoes BRADESCO.", "BRADESCO", "BANCO BRADESCO S/A", "065", BigDecimal.ZERO, BigDecimal.TEN);

		save(session, bancoHSBC,bancoITAU,bancoDOBRASIL,bancoBRADESCO);
	}

	private static void carregarDadosInadimplencia(Session session) {
		carregarDados(session);

		boleto1.setDataVencimento( Fixture.criarData(10, 3, 2012));
		boleto2.setDataVencimento( Fixture.criarData(11, 2, 2012));
		boleto3.setDataVencimento( Fixture.criarData(12, 3, 2012));
		boleto4.setDataVencimento( Fixture.criarData(13, 3, 2012));
		boleto5.setDataVencimento( Fixture.criarData(14, 2, 2012));
		boleto6.setDataVencimento( Fixture.criarData(15, 3, 2012));
		boleto7.setDataVencimento( Fixture.criarData(16, 3, 2012));
		boleto8.setDataVencimento( Fixture.criarData(8, 1, 2012));
		boleto8.setCota(cotaMaria);
		boleto8.setStatusCobranca(StatusCobranca.NAO_PAGO);
		boleto9.setDataVencimento( Fixture.criarData(18, 3, 2012));

		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6, boleto7, boleto8, boleto9);

		HistoricoAcumuloDivida histInadimplencia1 =
				Fixture.criarHistoricoAcumuloDivida( boleto1.getDivida(), Fixture.criarData(10, 3, 2012),
						usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia1);
	    HistoricoAcumuloDivida histInadimplencia2 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto2.getDivida(), Fixture.criarData(10, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia2);
	    HistoricoAcumuloDivida histInadimplencia3 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto3.getDivida(), Fixture.criarData(11, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia3);
	    HistoricoAcumuloDivida histInadimplencia4 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto4.getDivida(), Fixture.criarData(12, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia4);
	    HistoricoAcumuloDivida histInadimplencia5 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto5.getDivida(), Fixture.criarData(13, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia5);
	    HistoricoAcumuloDivida histInadimplencia6 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto6.getDivida(), Fixture.criarData(14, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia6);
	    HistoricoAcumuloDivida histInadimplencia7 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto7.getDivida(), Fixture.criarData(15, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia7);
	    HistoricoAcumuloDivida histInadimplencia8 =
	    		Fixture.criarHistoricoAcumuloDivida( boleto8.getDivida(), Fixture.criarData(16, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia8);
	    HistoricoAcumuloDivida histInadimplencia =
	    		Fixture.criarHistoricoAcumuloDivida( boleto9.getDivida(), Fixture.criarData(17, 3, 2012),
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia);

	    EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaJose, BigInteger.valueOf(10), BigInteger.ZERO);
		save(session, estoqueProdutoCota);

		EstoqueProdutoCota estoqueProdutoCota2 = Fixture.estoqueProdutoCota(
				produtoEdicaoBravo1, cotaJose, BigInteger.valueOf(11), BigInteger.ZERO);
		save(session, estoqueProdutoCota2);

		EstoqueProdutoCota estoqueProdutoCota3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(session, estoqueProdutoCota3);

		EstoqueProdutoCota estoqueProdutoCota4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaMaria, BigInteger.valueOf(186), BigInteger.ZERO);
		save(session, estoqueProdutoCota4);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(100), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

		MovimentoEstoqueCota mec2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(28), cotaMaria, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec2);

	}


	private static void gerarCargaDadosNotasFiscaisNFE(Session session) {

		save(session, box1);

		Cota cotaJohnyConsultaEncalhe = null;

		PessoaFisica johnyDasNotas = Fixture.pessoaFisica(
				"65485291673",
				"johny@discover.com.br", "Johny da Silva");

		save(session, johnyDasNotas);

		cotaJohnyConsultaEncalhe = Fixture.cota(2700, johnyDasNotas, SituacaoCadastro.ATIVO, box1);

		save(session, cotaJohnyConsultaEncalhe);

		ProdutoEdicao produtoEdicao91 = null;
		ProdutoEdicao produtoEdicao92 = null;
		ProdutoEdicao produtoEdicao93 = null;

		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;

		Produto produto91 = Fixture.produto("00091", "Produto 91", "Produto 91", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produto92 = Fixture.produto("00092", "Produto 92", "Produto 92", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produto93 = Fixture.produto("00093", "Produto 93", "Produto 93", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);

		produto91.addFornecedor(fornecedorDinap);
		produto92.addFornecedor(fornecedorDinap);
		produto93.addFornecedor(fornecedorDinap);

		save(session, produto91, produto92, produto93);

		produtoEdicao91 = Fixture.produtoEdicao("COD_KK", 91L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(15), "ZZ3", 26L, produto91, null, false);


		produtoEdicao92 = Fixture.produtoEdicao("COD_LL", 92L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(18), "ZZ4", 27L, produto92, null, false);

		produtoEdicao93 = Fixture.produtoEdicao("COD_MM", 93L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(90), "ZZ5", 28L, produto93, null, false);


		save(session, produtoEdicao91, produtoEdicao92, produtoEdicao93);

		String 		NCMProduto               = "3424";
		String 		CFOPProduto              = "24234";
		Long 		unidadeProduto           = 0L;
		String 		CSTProduto               = "2344";
		String 		CSOSNProduto             = "1233";
		BigDecimal 	baseCalculoProduto       = BigDecimal.ZERO;
		BigDecimal 	aliquotaICMSProduto      = BigDecimal.ZERO;
		BigDecimal 	valorICMSProduto         = BigDecimal.ZERO;
		BigDecimal 	aliquotaIPIProduto       = BigDecimal.ZERO;
		BigDecimal 	valorIPIProduto          = BigDecimal.ZERO;

		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedorNFE = null;

		int contador = 0;

		contador = 0;

		String naturezaOperacao = "00001";
		String formaPagamento = "00002";
		String horaSaida = "12:00";
		String ambiente = "000005";
		String protocolo = "321321";
		String versao = "0000000885";
		String emissorInscricaoEstadualSubstituto = "111.565.365.25";
		String emissorInscricaoMunicipal = "854.632.002.54";
		BigDecimal valorBaseICMS = BigDecimal.TEN;
		BigDecimal valorICMS = BigDecimal.TEN;
		BigDecimal valorBaseICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorProdutos = BigDecimal.TEN;
		BigDecimal valorFrete = BigDecimal.TEN;
		BigDecimal valorSeguro = BigDecimal.TEN;
		BigDecimal valorOutro = BigDecimal.TEN;
		BigDecimal valorIPI = BigDecimal.TEN;
		BigDecimal valorNF = BigDecimal.TEN;
		Integer frete = 0;
		String transportadoraCNPJ = "123.235.284.663-52";
		String transportadoraNome = "TRANS. ABRIL LTDA";
		String transportadoraInscricaoEstadual = "465.456.878.34-785";
		String transportadoraEndereco = "Rua Laranjeiras";
		String transportadoraMunicipio = "Cachoeira do Itapemirim";
		String transportadoraUF = "SP";
		String transportadoraQuantidade = "100";
		String transportadoraEspecie = "0544400";
		String transportadoraMarca = "NOVA TRANSP.";
		String transportadoraNumeracao = "1111111111222";
		BigDecimal transportadoraPesoBruto = BigDecimal.TEN;
		BigDecimal transportadoraPesoLiquido = BigDecimal.TEN;
		String transportadoraANTT = "3454-4454545-345345-54";
		String transportadoraPlacaVeiculo = "BYS9012";
		String transportadoraPlacaVeiculoUF = "SP";
		BigDecimal ISSQNTotal = BigDecimal.TEN;
		BigDecimal ISSQNBase = BigDecimal.TEN;
		BigDecimal ISSQNValor = BigDecimal.TEN;
		String informacoesComplementares = "NENHUMA INFO A COMPLEMENTAR";
		String numeroFatura = "222222233354";
		BigDecimal valorFatura = BigDecimal.TEN;

		while(contador++<10) {

			notaFiscalEntradaFornecedorNFE = Fixture.notaFiscalEntradaFornecedorNFE(
					cfop5102,
					11011110L+contador,
					"11111000"+contador,
					"11101011101"+contador,
					fornecedorDinap,
					StatusEmissaoNfe.NFE_AUTORIZADA,
					TipoEmissaoNfe.CONTINGENCIA_DPEC,
					tipoNotaFiscalRecebimento,
					usuarioJoao,
					BigDecimal.TEN,
					BigDecimal.ZERO,
					BigDecimal.TEN,
					true,
					naturezaOperacao,
					formaPagamento,
					horaSaida,
					ambiente,
					protocolo,
					versao,
					emissorInscricaoEstadualSubstituto,
					emissorInscricaoMunicipal,
					valorBaseICMS,
					valorICMS,
					valorBaseICMSSubstituto,
					valorICMSSubstituto,
					valorProdutos,
					valorFrete,
					valorSeguro,
					valorOutro,
					valorIPI,
					valorNF,
					frete,
					transportadoraCNPJ,
					transportadoraNome,
					transportadoraInscricaoEstadual,
					transportadoraEndereco,
					transportadoraMunicipio,
					transportadoraUF,
					transportadoraQuantidade,
					transportadoraEspecie,
					transportadoraMarca,
					transportadoraNumeracao,
					transportadoraPesoBruto,
					transportadoraPesoLiquido,
					transportadoraANTT,
					transportadoraPlacaVeiculo,
					transportadoraPlacaVeiculoUF,
					ISSQNTotal,
					ISSQNBase,
					ISSQNValor,
					informacoesComplementares,
					numeroFatura,
					valorFatura);

			notaFiscalEntradaFornecedorNFE.setMovimentoIntegracao("Movimento teste para nota fiscal entrada.");

			save(session, notaFiscalEntradaFornecedorNFE);


			ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE =

					Fixture.itemNotaFiscalEntradaNFE(
							produtoEdicao91,
							usuarioJoao,
							notaFiscalEntradaFornecedorNFE,
							Fixture.criarData(22, Calendar.FEBRUARY,2012),
							Fixture.criarData(22, Calendar.FEBRUARY,2012),
							TipoLancamento.LANCAMENTO,
							BigInteger.valueOf(50),
							NCMProduto,
							CFOPProduto,
							unidadeProduto,
							CSTProduto,
							CSOSNProduto,
							baseCalculoProduto,
							aliquotaICMSProduto,
							valorICMSProduto,
							aliquotaIPIProduto,
							valorIPIProduto);

			save(session, itemNotaFiscalEntradaNFE);

		}


		///// ENTRADA COTA

		NotaFiscalEntradaCota notaFiscalEntradaCotaNFE = null;

		contador = 0;

		while(contador++<10) {

			notaFiscalEntradaCotaNFE =
					Fixture.notaFiscalEntradaCotaNFE(
							cfop5102,
							222220000202L+contador,
							"220202022220"+contador,
							"2000022"+contador,
							cotaJohnyConsultaEncalhe,
							StatusEmissaoNfe.NFE_AUTORIZADA,
							TipoEmissaoNfe.CONTINGENCIA_DPEC,
							tipoNotaFiscalRecebimento,
							usuarioJoao,
							BigDecimal.TEN,
							BigDecimal.ZERO,
							BigDecimal.TEN,
							true,
							naturezaOperacao,
							formaPagamento,
							horaSaida,
							ambiente,
							protocolo,
							versao,
							emissorInscricaoEstadualSubstituto,
							emissorInscricaoMunicipal,
							valorBaseICMS,
							valorICMS,
							valorBaseICMSSubstituto,
							valorICMSSubstituto,
							valorProdutos,
							valorFrete,
							valorSeguro,
							valorOutro,
							valorIPI,
							valorNF,
							frete,
							transportadoraCNPJ,
							transportadoraNome,
							transportadoraInscricaoEstadual,
							transportadoraEndereco,
							transportadoraMunicipio,
							transportadoraUF,
							transportadoraQuantidade,
							transportadoraEspecie,
							transportadoraMarca,
							transportadoraNumeracao,
							transportadoraPesoBruto,
							transportadoraPesoLiquido,
							transportadoraANTT,
							transportadoraPlacaVeiculo,
							transportadoraPlacaVeiculoUF,
							ISSQNTotal,
							ISSQNBase,
							ISSQNValor,
							informacoesComplementares,
							numeroFatura,
							valorFatura);


			save(session, notaFiscalEntradaCotaNFE);

			ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE_2 = Fixture.itemNotaFiscalEntradaNFE(
					produtoEdicao91,
					usuarioJoao,
					notaFiscalEntradaCotaNFE,
					Fixture.criarData(22, Calendar.FEBRUARY,2012),
					Fixture.criarData(22, Calendar.FEBRUARY,2012),
					TipoLancamento.LANCAMENTO,
					BigInteger.valueOf(50),
					NCMProduto,
					CFOPProduto,
					unidadeProduto,
					CSTProduto,
					CSOSNProduto,
					baseCalculoProduto,
					aliquotaICMSProduto,
					valorICMSProduto,
					aliquotaIPIProduto,
					valorIPIProduto);

			save(session, itemNotaFiscalEntradaNFE_2);
		}


		///// SAIDA FORNECEDOR

		NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedorNFE = null;

		contador = 0;

		while(contador++<10) {

			notaFiscalSaidaFornecedorNFE = Fixture.notaFiscalSaidaFornecedorNFE(
					cfop5102,
					33300003003L+contador,
					"30300333330"+contador,
					"0003303"+contador,
					fornecedorDinap,
					StatusEmissaoNfe.NFE_AUTORIZADA,
					TipoEmissaoNfe.CONTINGENCIA_DPEC,
					tipoNotaFiscalRecebimento,
					usuarioJoao,
					BigDecimal.TEN,
					BigDecimal.ZERO,
					BigDecimal.TEN,
					true,
					naturezaOperacao,
					formaPagamento,
					horaSaida,
					ambiente,
					protocolo,
					versao,
					emissorInscricaoEstadualSubstituto,
					emissorInscricaoMunicipal,
					valorBaseICMS,
					valorICMS,
					valorBaseICMSSubstituto,
					valorICMSSubstituto,
					valorProdutos,
					valorFrete,
					valorSeguro,
					valorOutro,
					valorIPI,
					valorNF,
					frete,
					transportadoraCNPJ,
					transportadoraNome,
					transportadoraInscricaoEstadual,
					transportadoraEndereco,
					transportadoraMunicipio,
					transportadoraUF,
					transportadoraQuantidade,
					transportadoraEspecie,
					transportadoraMarca,
					transportadoraNumeracao,
					transportadoraPesoBruto,
					transportadoraPesoLiquido,
					transportadoraANTT,
					transportadoraPlacaVeiculo,
					transportadoraPlacaVeiculoUF,
					ISSQNTotal,
					ISSQNBase,
					ISSQNValor,
					informacoesComplementares,
					numeroFatura,
					valorFatura);

			save(session, notaFiscalSaidaFornecedorNFE);

			ItemNotaFiscalSaida itemNotaFiscalSaida =
					Fixture.itemNotaFiscalSaidaNFE(
					produtoEdicao91,
					notaFiscalSaidaFornecedorNFE,
					BigInteger.TEN,
					NCMProduto,
					CFOPProduto,
					unidadeProduto,
					CSTProduto,
					CSOSNProduto,
					baseCalculoProduto,
					aliquotaICMSProduto,
					valorICMSProduto,
					aliquotaIPIProduto,
					valorIPIProduto);

			save(session, itemNotaFiscalSaida);

		}

	}

	private static void gerarCargaDadosConferenciaEncalhe(Session session) {

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimentoMercadoriasEncalhe();
		save(session, tipoNotaFiscal);

		Box boxRecolhimento = Fixture.criarBox(601, "Box Encalhe", TipoBox.ENCALHE);
		save(session, boxRecolhimento);

		ParametroUsuarioBox parametroUsuarioBox = Fixture.parametroUsuarioBox(boxRecolhimento, usuarioJoao, true);
		save(session, parametroUsuarioBox);

		boxRecolhimento = Fixture.criarBox(602, "Box Encalhe_2", TipoBox.ENCALHE);
		save(session, boxRecolhimento);

		boxRecolhimento = Fixture.criarBox(603, "Box Encalhe_3", TipoBox.ENCALHE);
		save(session, boxRecolhimento);

		boxRecolhimento = Fixture.criarBox(604, "Box Encalhe_4", TipoBox.ENCALHE);
		save(session, boxRecolhimento);

		/**
		 * COTA
		 */
		PessoaJuridica valdomiroDevolvedorEncalhe = Fixture.pessoaJuridica(
				"Vardomiro Devolve Jornal S.A.",
				"12345632167",
				"5675123156583",
				"vardomiro@devolve.com",
				"345345345345");

		save(session, valdomiroDevolvedorEncalhe);

		Cota cotaConferenciaEncalhe = Fixture.cota(5637, valdomiroDevolvedorEncalhe, SituacaoCadastro.ATIVO, box1);

		save(session, cotaConferenciaEncalhe);

		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);

		FormaCobranca formaDeposito =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);
		FormaCobranca formaDinheiro =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);


		save(session, formaBoleto, formaDeposito, formaDinheiro);

		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();

		formasCobranca.add(formaBoleto);
		formasCobranca.add(formaDeposito);
		formasCobranca.add(formaDinheiro);

		ParametroCobrancaCota parametroCobrancaCotaConfEncalhe = Fixture.parametroCobrancaCota(
				formasCobranca,
				1, null, cotaConferenciaEncalhe, 1,
				false, new BigDecimal(1000), null);

		save(session, parametroCobrancaCotaConfEncalhe);

		formaBoleto.setParametroCobrancaCota(parametroCobrancaCotaConfEncalhe);
		formaBoleto.setPrincipal(true);

		save(session, formaBoleto);

		String codigoProduto = "8611";
		String nomeProduto = "Produto 8611***";
		String descProduto = "Produto 8611";
		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;
		int produtoPeb = 1;
		int produtoPacotePadrao = 10;
		Long produtoPeso = new Long(10000);

		String codigoProdutoEdicao = "8611";
		Long numeroEdicao = 8611L;
		int pacotePadrao = 10;
		int peb = 1;
		Long peso = new Long(10);

		BigDecimal precoCusto = new BigDecimal(16);
		BigDecimal precoVenda = new BigDecimal(16);
		String codigoDeBarras = "BYX8611";

		BigDecimal expectativaVenda = BigDecimal.TEN;

		boolean parcial = false;

		Long numeroNF = 8611L;
		String serie = "8611";
		String chaveAcesso = "8611";

		BigDecimal valorBruto = new BigDecimal(17);
		BigDecimal valorDesconto = BigDecimal.ONE;
		BigDecimal valorLiquido = new BigDecimal(16);

		Date dataLancamento = Fixture.criarData(1, Calendar.JUNE, 2012);
		Date dataRecolhimento = DateUtil.adicionarDias(new Date(), 10);

		TipoLancamento tipoLancamento = TipoLancamento.LANCAMENTO;

		BigInteger qtdeItemNota = BigInteger.valueOf(80);

		Date dataRecebimentoFisico = Fixture.criarData(1, Calendar.JUNE, 2012);
		BigInteger qtdeItemRecebimentoFisico  = BigInteger.valueOf(80);

		BigInteger qtdeReparteLancamento = BigInteger.valueOf(80);
		Integer codigoSM = 8611;

		BigInteger qtdeReparteEstudo = BigInteger.valueOf(80);
		Date dataEstudo = Fixture.criarData(1, Calendar.JUNE, 2012);

		BigInteger estoqueProdCotaQtdeRecebida = BigInteger.valueOf(80);
		BigInteger estoqueProdCotaQtdeDevolvida = BigInteger.ZERO;

		Date dataRecolhimentoChamadaEncalhe = DateUtil.adicionarDias(new Date(), 10);

		TipoChamadaEncalhe tipoChamadaEncalhe = TipoChamadaEncalhe.CHAMADAO;

		boolean indChamadaEncalheCotaConferida = false;

		BigInteger qtdePrevistaChamadaEncCota = BigInteger.valueOf(80);

		int contador = 0;

		while(contador++ < 30) {

			codigoProduto 		= "8611";
			nomeProduto 		= "Produto 8611";
			codigoProdutoEdicao = "8611";
			numeroEdicao 		= 8611L;
			codigoSM 			= 8611;
			codigoDeBarras 		= "BYX8611";

			nomeProduto 		= nomeProduto 			+ contador;
			codigoProduto 		= codigoProduto 		+ contador;
			numeroEdicao 		= numeroEdicao 			+ contador;
			codigoProdutoEdicao = codigoProdutoEdicao 	+ contador;
			codigoSM 			= codigoSM 				+ contador;
			codigoDeBarras 		= codigoDeBarras 		+ contador;

			geraMassaParaConferenciaEncalheCota(
					session, cotaConferenciaEncalhe, codigoProduto,
					nomeProduto, descProduto, periodicidade,
					produtoPeb, produtoPacotePadrao, produtoPeso,
					codigoProdutoEdicao, numeroEdicao, pacotePadrao,
					peb, peso, precoCusto,
					precoVenda, codigoDeBarras, expectativaVenda,
					parcial, numeroNF, serie,
					chaveAcesso, valorBruto, valorDesconto,
					valorLiquido, dataLancamento, dataRecolhimento,
					tipoLancamento, qtdeItemNota, dataRecebimentoFisico,
					qtdeItemRecebimentoFisico, qtdeReparteLancamento, codigoSM,
					qtdeReparteEstudo, dataEstudo, estoqueProdCotaQtdeRecebida,
					estoqueProdCotaQtdeDevolvida, dataRecolhimentoChamadaEncalhe, tipoChamadaEncalhe,
					indChamadaEncalheCotaConferida, qtdePrevistaChamadaEncCota);

		}


	}

	private static void geraMassaParaConferenciaEncalheCota(

			Session session,

			Cota cotaConferenciaEncalhe,
			String codigoProduto,
			String nomeProduto,
			String descProduto,
			PeriodicidadeProduto periodicidade,
			int produtoPeb,
			int produtoPacotePadrao,
			Long produtoPeso,

			String codigoProdutoEdicao,
			Long numeroEdicao,
			int pacotePadrao,
			int peb,
			Long peso,
			BigDecimal precoCusto,
			BigDecimal precoVenda,
			String codigoDeBarras,
			BigDecimal expectativaVenda,
			boolean parcial,

			Long numeroNF,
			String serie,
			String chaveAcesso,

			BigDecimal valorBruto,
			BigDecimal valorDesconto,
			BigDecimal valorLiquido,

			Date dataLancamento,
			Date dataRecolhimento,
			TipoLancamento tipoLancamento,
			BigInteger qtdeItemNota,
			Date dataRecebimentoFisico,
			BigInteger qtdeItemRecebimentoFisico,

			BigInteger qtdeReparteLancamento,
			Integer codigoSM,

			BigInteger qtdeReparteEstudo,
			Date dataEstudo,

			BigInteger estoqueProdCotaQtdeRecebida,
			BigInteger estoqueProdCotaQtdeDevolvida,

			Date dataRecolhimentoChamadaEncalhe,
			TipoChamadaEncalhe tipoChamadaEncalhe,
			boolean indChamadaEncalheCotaConferida,
			BigInteger qtdePrevistaChamadaEncCota


			) {

		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE = null;

		/**
		 * PRODUTO EDICAO
		 */

		Lancamento lancamentoRevistaCE = null;

		ProdutoEdicao produtoEdicaoCE = null;

		Produto produtoCE = Fixture.produto(codigoProduto, descProduto, nomeProduto, periodicidade, tipoProdutoRevista, produtoPeb, produtoPacotePadrao, produtoPeso, TributacaoFiscal. TRIBUTADO);

		produtoCE.addFornecedor(fornecedorDinap);

		save(session, produtoCE);

		produtoEdicaoCE = Fixture.produtoEdicao(codigoProdutoEdicao, numeroEdicao, pacotePadrao, peb,
				peso, precoCusto, precoVenda, codigoDeBarras, null, produtoCE, expectativaVenda, parcial, descProduto + numeroEdicao);
		save(session, produtoEdicaoCE);

		/**
		 * RECEBIMENTO FISICO
		 */
		NotaFiscalEntradaFornecedor notaFiscalProdutoCE =
				Fixture.notaFiscalEntradaFornecedor(
						numeroNF,
						serie,
						chaveAcesso,
						cfop5102,
						fornecedorFc,
						tipoNotaFiscalRecebimento,
						usuarioJoao,
						valorBruto,
						valorDesconto,
						valorLiquido);

		save(session, notaFiscalProdutoCE);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE =
				Fixture.itemNotaFiscal(
						produtoEdicaoCE,
						usuarioJoao,
						notaFiscalProdutoCE,
						dataLancamento,
						dataRecolhimento,
						tipoLancamento,
						qtdeItemNota);

		save(session, itemNotaFiscalProdutoCE);

		RecebimentoFisico recebimentoFisicoProdutoCE =
				Fixture.recebimentoFisico(
						notaFiscalProdutoCE,
						usuarioJoao,
						dataRecebimentoFisico,
						dataRecebimentoFisico,
						StatusConfirmacao.CONFIRMADO);

		save(session, recebimentoFisicoProdutoCE);

		itemRecebimentoFisicoProdutoCE =
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE,
						recebimentoFisicoProdutoCE,
						qtdeItemRecebimentoFisico);

		save(session, itemRecebimentoFisicoProdutoCE);

		/**
		 * LANCAMENTOS
		 */

		lancamentoRevistaCE = Fixture.lancamento(
				tipoLancamento,
				produtoEdicaoCE,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				qtdeReparteLancamento,
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisicoProdutoCE,
				codigoSM);

		Estudo estudo = Fixture.estudo(
				qtdeReparteEstudo,
				dataEstudo,
				produtoEdicaoCE);

		save(session, lancamentoRevistaCE, estudo);

		/**
		 * ESTOQUE DA COTA
		 */
		EstoqueProdutoCota estoqueProdutoCotaConferenciaEncalhe =
				Fixture.estoqueProdutoCota(
						produtoEdicaoCE,
						cotaConferenciaEncalhe,
						estoqueProdCotaQtdeRecebida,
						estoqueProdCotaQtdeDevolvida);
		save(session, estoqueProdutoCotaConferenciaEncalhe);

		/**
		 * CHAMADA ENCALHE
		 */
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				dataRecolhimentoChamadaEncalhe,
				produtoEdicaoCE,
				tipoChamadaEncalhe);

		save(session, chamadaEncalhe);


		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe,
				indChamadaEncalheCotaConferida,
				cotaConferenciaEncalhe,
				qtdePrevistaChamadaEncCota);
		save(session, chamadaEncalheCota);

	}


	private static void gerarCargaDadosConsultaEncalhe(Session session) {

		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE = null;
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE_2 = null;
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE_3 = null;

		Date dataRecebimento = null;

		Lancamento lancamentoRevistaCE = null;
		Lancamento lancamentoRevistaCE_2 = null;
		Lancamento lancamentoRevistaCE_3 = null;

		Cota cotaJohnyConsultaEncalhe = null;

		ProdutoEdicao produtoEdicaoCE = null;
		ProdutoEdicao produtoEdicaoCE_2 = null;
		ProdutoEdicao produtoEdicaoCE_3 = null;

		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;

		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);

		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);

		save(session, produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao("COD_NN", 84L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(15), "EZ7", 29L, produtoCE, null, false, "Produto CE 1");


		produtoEdicaoCE_2 = Fixture.produtoEdicao("COD_OO", 85L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(18), "EZ8", 30L, produtoCE_2, null, false, "Produto CE 2");


		produtoEdicaoCE_3 = Fixture.produtoEdicao("COD_PP", 86L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(90), "EZ8", 31L, produtoCE_3, null, false, "Produto CE 3");

		save(session, produtoEdicaoCE, produtoEdicaoCE_2, produtoEdicaoCE_3);

		NotaFiscalEntradaFornecedor notaFiscalProdutoCE =
				Fixture.notaFiscalEntradaFornecedor(
						cfop5102,
						fornecedorFc,
						tipoNotaFiscalRecebimento,
						usuarioJoao,
						BigDecimal.TEN,
						BigDecimal.ZERO,
						BigDecimal.TEN);

		save(session, notaFiscalProdutoCE);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE =
				Fixture.itemNotaFiscal(
						produtoEdicaoCE,
						usuarioJoao,
						notaFiscalProdutoCE,
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));

		save(session, itemNotaFiscalProdutoCE);


		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_2 =
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_2,
						usuarioJoao,
						notaFiscalProdutoCE,
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));

		save(session, itemNotaFiscalProdutoCE_2);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_3 =
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_3,
						usuarioJoao,
						notaFiscalProdutoCE,
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));

		save(session, itemNotaFiscalProdutoCE_3);


		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);

		RecebimentoFisico recebimentoFisicoProdutoCE =
				Fixture.recebimentoFisico(
						notaFiscalProdutoCE,
						usuarioJoao,
						dataRecebimento,
						dataRecebimento,
						StatusConfirmacao.CONFIRMADO);

		save(session, recebimentoFisicoProdutoCE);

		itemRecebimentoFisicoProdutoCE =
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE,
						recebimentoFisicoProdutoCE,
						BigInteger.valueOf(50));

		save(session, itemRecebimentoFisicoProdutoCE);

		itemRecebimentoFisicoProdutoCE_2 =
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_2,
						recebimentoFisicoProdutoCE,
						BigInteger.valueOf(50));

		save(session, itemRecebimentoFisicoProdutoCE_2);

		itemRecebimentoFisicoProdutoCE_3 =
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_3,
						recebimentoFisicoProdutoCE,
						BigInteger.valueOf(50));

		save(session, itemRecebimentoFisicoProdutoCE_3);


		lancamentoRevistaCE = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				produtoEdicaoCE,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisicoProdutoCE, 1);

		lancamentoRevistaCE.getRecebimentos().add(itemRecebimentoFisicoProdutoCE);


		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE);

		save(session, lancamentoRevistaCE, estudo);


		lancamentoRevistaCE_2 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				produtoEdicaoCE_2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisicoProdutoCE_2, 1);

		lancamentoRevistaCE_2.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_2);


		Estudo estudo_2 = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE_2);

		save(session, lancamentoRevistaCE_2, estudo_2);


		lancamentoRevistaCE_3 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				produtoEdicaoCE_3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisicoProdutoCE_3, 1);

		lancamentoRevistaCE_3.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_3);


		Estudo estudo_3 = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE_3);

		save(session, lancamentoRevistaCE_3, estudo_3);


		PessoaFisica johnyCE = Fixture.pessoaFisica(
				"35285547400",
				"johny@discover.com.br", "Johny da Silva");
		save(session, johnyCE);

		cotaJohnyConsultaEncalhe = Fixture.cota(2593, johnyCE, SituacaoCadastro.ATIVO, box1);
		save(session, cotaJohnyConsultaEncalhe);


		EstoqueProdutoCota estoqueProdutoCotaJohny =
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE, cotaJohnyConsultaEncalhe, BigInteger.TEN, BigInteger.ZERO);
		save(session, estoqueProdutoCotaJohny);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe);


		EstoqueProdutoCota estoqueProdutoCotaJohny_2 =
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_2, cotaJohnyConsultaEncalhe, BigInteger.TEN, BigInteger.ZERO);
		save(session, estoqueProdutoCotaJohny_2);

		ChamadaEncalhe chamadaEncalhe_2 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE_2,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe_2);

		EstoqueProdutoCota estoqueProdutoCotaJohny_3 =
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_3, cotaJohnyConsultaEncalhe, BigInteger.TEN, BigInteger.ZERO);
		save(session, estoqueProdutoCotaJohny_3);

		ChamadaEncalhe chamadaEncalhe_3 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE_3,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe_3);


		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = Fixture.controleNumeracaoNotaFiscal(1L, "0001");
		save(session, controleNumeracaoNotaFiscal);

		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe,
				false,
				cotaJohnyConsultaEncalhe,
				BigInteger.TEN);
		save(session, chamadaEncalheCota);

		ChamadaEncalheCota chamadaEncalheCota_2 = Fixture.chamadaEncalheCota(
				chamadaEncalhe_2,
				false,
				cotaJohnyConsultaEncalhe,
				BigInteger.TEN);
		save(session, chamadaEncalheCota_2);

		ChamadaEncalheCota chamadaEncalheCota_3 = Fixture.chamadaEncalheCota(
				chamadaEncalhe_3,
				false,
				cotaJohnyConsultaEncalhe,
				BigInteger.TEN);
		save(session, chamadaEncalheCota_3);


		/**
		 * CONTROLE CONFERENCIA ENCALHE
		 */
		ControleConferenciaEncalhe controleConferenciaEncalhe =
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(session, controleConferenciaEncalhe);


		/**
		 * CONTROLE CONFERENCIA ENCALHE COTA
		 */
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalhe,
				cotaJohnyConsultaEncalhe,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				StatusOperacao.CONCLUIDO,
				usuarioJoao,
				box1);

		save(session, controleConferenciaEncalheCota);


		/**
		 * MOVIMENTOS DE ENVIO ENCALHE ABAIXO
		 */
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny,
				BigInteger.valueOf(8), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(8),BigInteger.valueOf(8), produtoEdicaoCE);
		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(2, Calendar.MARCH, 2012),
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny,
				BigInteger.valueOf(8), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota,
				controleConferenciaEncalheCota,
				Fixture.criarData(2, Calendar.MARCH, 2012),
				BigInteger.valueOf(8),
				BigInteger.valueOf(8), produtoEdicaoCE);

		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(3, Calendar.MARCH, 2012),
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny,
				BigInteger.valueOf(8), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec,
				chamadaEncalheCota,
				controleConferenciaEncalheCota,
				Fixture.criarData(3, Calendar.MARCH, 2012),
				BigInteger.valueOf(8),
				BigInteger.valueOf(8),
				produtoEdicaoCE);

		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_2,
				BigInteger.valueOf(34), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota_2,
				controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				BigInteger.valueOf(34),
				BigInteger.valueOf(34),
				produtoEdicaoCE_2);

		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(3, Calendar.MARCH, 2012),
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_2,
				BigInteger.valueOf(45), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota_2,
				controleConferenciaEncalheCota,
				Fixture.criarData(3, Calendar.MARCH, 2012),
				BigInteger.valueOf(45),
				BigInteger.valueOf(45),
				produtoEdicaoCE_2);

		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(4, Calendar.MARCH, 2012),
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_2,
				BigInteger.valueOf(65), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota_2,
				controleConferenciaEncalheCota,
				Fixture.criarData(4, Calendar.MARCH, 2012),
				BigInteger.valueOf(65),
				BigInteger.valueOf(65),
				produtoEdicaoCE_2);

		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(31), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota_3,
				controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				BigInteger.valueOf(31),BigInteger.valueOf(31),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(3, Calendar.MARCH, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(41), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota_3,
				controleConferenciaEncalheCota,
				Fixture.criarData(3, Calendar.MARCH, 2012),
				BigInteger.valueOf(41),
				BigInteger.valueOf(41),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(5, Calendar.MARCH, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(85), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota_3,
				controleConferenciaEncalheCota,
				Fixture.criarData(5, Calendar.MARCH, 2012),
				BigInteger.valueOf(85),
				BigInteger.valueOf(85),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(6, Calendar.MARCH, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(85), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota_3, controleConferenciaEncalheCota,
				Fixture.criarData(6, Calendar.MARCH, 2012),
				BigInteger.valueOf(85),
				BigInteger.valueOf(85),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(7, Calendar.MARCH, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(85), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec,
				chamadaEncalheCota_3,
				controleConferenciaEncalheCota,
				Fixture.criarData(7, Calendar.MARCH, 2012),
				BigInteger.valueOf(85),
				BigInteger.valueOf(85),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(7, Calendar.MARCH, 2012),
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe,
				usuarioJoao,
				estoqueProdutoCotaJohny_3,
				BigInteger.valueOf(85), cotaJohnyConsultaEncalhe,
				StatusAprovacao.APROVADO,
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota_3,
				controleConferenciaEncalheCota,
				Fixture.criarData(7, Calendar.MARCH, 2012),
				BigInteger.valueOf(85),
				BigInteger.valueOf(85),
				produtoEdicaoCE_3);

		save(session, conferenciaEncalhe);

	}

    private static BigDecimal obterValorMovimentosEstoque(
            List<MovimentoEstoqueCota> movimentosE) {
        Double total = 0d;
        for (MovimentoEstoqueCota movE : movimentosE) {
            total += (movE.getQtde().doubleValue() * (movE
                    .getEstoqueProdutoCota().getProdutoEdicao().getPrecoVenda()
                    .doubleValue() - Util.nvl(movE.getEstoqueProdutoCota()
                    .getProdutoEdicao().getProduto().getDesconto(), BigDecimal.ZERO)
                    .doubleValue()));
        }
        return new BigDecimal(total);
    }

	private static void criarMovimentosFinanceiroCota(Session session) {

		movimentoFinanceiroCota1 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota1)), Arrays.asList(movimentoEstoqueCota1),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota2 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota2)), Arrays.asList(movimentoEstoqueCota2),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota3 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota3)), Arrays.asList(movimentoEstoqueCota3),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota4 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota4)), Arrays.asList(movimentoEstoqueCota4),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota5 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota5)), Arrays.asList(movimentoEstoqueCota5),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota6 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota6)), Arrays.asList(movimentoEstoqueCota6),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota7 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota7)), Arrays.asList(movimentoEstoqueCota7),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota8 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota8)), Arrays.asList(movimentoEstoqueCota8),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota9 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota9)), Arrays.asList(movimentoEstoqueCota9),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota10 = Fixture.movimentoFinanceiroCota(
				cotaManoelCunha, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota10)), Arrays.asList(movimentoEstoqueCota10),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota11 = Fixture.movimentoFinanceiroCota(
				cotaManoelCunha, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota11)), Arrays.asList(movimentoEstoqueCota11),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota12 = Fixture.movimentoFinanceiroCota(
				cotaManoelCunha, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota12)), Arrays.asList(movimentoEstoqueCota12),
				StatusAprovacao.APROVADO, new Date(), true);




		//MOVIMENTOS TIPO ENCALHE
		movimentoFinanceiroCota13 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota13)), Arrays.asList(movimentoEstoqueCota13),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota14 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota14)), Arrays.asList(movimentoEstoqueCota14),
			StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota15 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota15)), Arrays.asList(movimentoEstoqueCota15),
			    StatusAprovacao.APROVADO, new Date(), true);


		movimentoFinanceiroCota16 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota16)), Arrays.asList(movimentoEstoqueCota16),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota17 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota17)), Arrays.asList(movimentoEstoqueCota17),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota18 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota18)), Arrays.asList(movimentoEstoqueCota18),
				StatusAprovacao.APROVADO, new Date(), true);



		//MOVIMENTOS TIPO CONSIGNADO
		movimentoFinanceiroCota19 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota19)), Arrays.asList(movimentoEstoqueCota19),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota20 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota20)), Arrays.asList(movimentoEstoqueCota20),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota21 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota21)), Arrays.asList(movimentoEstoqueCota21),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota22 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota22)), Arrays.asList(movimentoEstoqueCota22),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota23 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota23)), Arrays.asList(movimentoEstoqueCota23),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota24 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota24)), Arrays.asList(movimentoEstoqueCota24),
				StatusAprovacao.APROVADO, new Date(), true);



		//MOVIMENTOS TIPO VENDA_ENCALHE
		movimentoFinanceiroCota25 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota25)), Arrays.asList(movimentoEstoqueCota25),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota26 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota26)), Arrays.asList(movimentoEstoqueCota26),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota27 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota27)), Arrays.asList(movimentoEstoqueCota27),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota28 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota28)), Arrays.asList(movimentoEstoqueCota28),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota29 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota29)), Arrays.asList(movimentoEstoqueCota29),
				StatusAprovacao.APROVADO, new Date(), true);

		movimentoFinanceiroCota30 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuarioJoao,
				obterValorMovimentosEstoque(Arrays.asList(movimentoEstoqueCota30)), Arrays.asList(movimentoEstoqueCota30),
				StatusAprovacao.APROVADO, new Date(), true);



		save(session, movimentoFinanceiroCota1, movimentoFinanceiroCota2,
					  movimentoFinanceiroCota3, movimentoFinanceiroCota4,
					  movimentoFinanceiroCota5, movimentoFinanceiroCota6,
					  movimentoFinanceiroCota7, movimentoFinanceiroCota8,
					  movimentoFinanceiroCota9,  movimentoFinanceiroCota10,
					  movimentoFinanceiroCota11, movimentoFinanceiroCota12,
					  movimentoFinanceiroCota13, movimentoFinanceiroCota14,
					  movimentoFinanceiroCota15,  movimentoFinanceiroCota16,
					  movimentoFinanceiroCota17, movimentoFinanceiroCota18,
					  movimentoFinanceiroCota19, movimentoFinanceiroCota20,
					  movimentoFinanceiroCota21,  movimentoFinanceiroCota22,
					  movimentoFinanceiroCota23, movimentoFinanceiroCota24,
					  movimentoFinanceiroCota25, movimentoFinanceiroCota26,
					  movimentoFinanceiroCota27,  movimentoFinanceiroCota28,
					  movimentoFinanceiroCota29, movimentoFinanceiroCota30);




		MovimentoFinanceiroCota movimentoFinanceiroCota31 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 10), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota32 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 20), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota33 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 30), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota34 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 60), true);



		MovimentoFinanceiroCota movimentoFinanceiroDebito1 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					DateUtil.adicionarDias(new Date(), 50), true);

		MovimentoFinanceiroCota movimentoFinanceiroDebito2 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroCredito1 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 40), true);

		MovimentoFinanceiroCota movimentoFinanceiroCredito2 =
			Fixture.movimentoFinanceiroCota(
				cotaJoao, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroJuros1 =
			Fixture.movimentoFinanceiroCota(
				cotaJose, tipoMovimentoFinanceiroJuros, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroJuros2 =
			Fixture.movimentoFinanceiroCota(
				cotaMaria, tipoMovimentoFinanceiroJuros, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroMulta1 =
			Fixture.movimentoFinanceiroCota(
				cotaJose, tipoMovimentoFinanceiroMulta, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroMulta2 =
			Fixture.movimentoFinanceiroCota(
				cotaMaria, tipoMovimentoFinanceiroMulta, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroEnvioEncalhe1 =
				Fixture.movimentoFinanceiroCota(
					cotaJose, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroEnvioEncalhe2 =
				Fixture.movimentoFinanceiroCota(
					cotaMaria, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		save(session, movimentoFinanceiroCota31, movimentoFinanceiroCota32,
				  	  movimentoFinanceiroCota33, movimentoFinanceiroCota34,
				  	  movimentoFinanceiroDebito1, movimentoFinanceiroDebito2,
				  	  movimentoFinanceiroCredito1, movimentoFinanceiroCredito2,
				  	  movimentoFinanceiroJuros1, movimentoFinanceiroJuros2,
				  	  movimentoFinanceiroMulta1, movimentoFinanceiroMulta2,
				  	  movimentoFinanceiroEnvioEncalhe1, movimentoFinanceiroEnvioEncalhe2);



		movimentoFinanceiroCota31 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
					new BigDecimal(225), null, StatusAprovacao.PENDENTE,
					DateUtil.adicionarDias(new Date(), 10), true);

		movimentoFinanceiroCota31.setDataCriacao(DateUtil.adicionarDias(new Date(), -2));

		movimentoFinanceiroCota32 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
					new BigDecimal(225), null, StatusAprovacao.APROVADO,
					DateUtil.adicionarDias(new Date(), 20), true);

		movimentoFinanceiroCota32.setDataCriacao(DateUtil.adicionarDias(new Date(), -6));

		movimentoFinanceiroCota33 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
					new BigDecimal(225), null, StatusAprovacao.PENDENTE,
					DateUtil.adicionarDias(new Date(), 30), true);

		movimentoFinanceiroCota33.setDataCriacao(DateUtil.adicionarDias(new Date(), -6));

		movimentoFinanceiroCota34 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.APROVADO,
					DateUtil.adicionarDias(new Date(), 60), true);

		movimentoFinanceiroCota34.setDataCriacao(DateUtil.adicionarDias(new Date(), -2));



		movimentoFinanceiroDebito1 =
					Fixture.movimentoFinanceiroCota(
						cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
						new BigDecimal(650), null, StatusAprovacao.PENDENTE,
						DateUtil.adicionarDias(new Date(), 50), true);

		movimentoFinanceiroDebito1.setDataCriacao(DateUtil.adicionarDias(new Date(), -12));

		movimentoFinanceiroDebito2 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroDebito2.setDataCriacao(DateUtil.adicionarDias(new Date(), -8));

		movimentoFinanceiroCredito1 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.APROVADO,
					DateUtil.adicionarDias(new Date(), 40), true);

		movimentoFinanceiroCredito1.setDataCriacao(DateUtil.adicionarDias(new Date(), -2));

		movimentoFinanceiroCredito2 =
				Fixture.movimentoFinanceiroCota(
					cotaJoao, tipoMovimentoFinanceiroCredito, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroCredito2.setDataCriacao(DateUtil.adicionarDias(new Date(), -2));

		movimentoFinanceiroJuros1 =
				Fixture.movimentoFinanceiroCota(
					cotaJose, tipoMovimentoFinanceiroJuros, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroJuros1.setDataCriacao(DateUtil.adicionarDias(new Date(), -2));

		movimentoFinanceiroJuros2 =
				Fixture.movimentoFinanceiroCota(
					cotaMaria, tipoMovimentoFinanceiroJuros, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroJuros2.setDataCriacao(DateUtil.adicionarDias(new Date(), -6));

		movimentoFinanceiroMulta1 =
				Fixture.movimentoFinanceiroCota(
					cotaJose, tipoMovimentoFinanceiroMulta, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroMulta1.setDataCriacao(DateUtil.adicionarDias(new Date(), -6));

		movimentoFinanceiroMulta2 =
				Fixture.movimentoFinanceiroCota(
					cotaMaria, tipoMovimentoFinanceiroMulta, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);

		movimentoFinanceiroMulta2.setDataCriacao(DateUtil.adicionarDias(new Date(), -6));

		movimentoFinanceiroEnvioEncalhe1 =
					Fixture.movimentoFinanceiroCota(
						cotaJose, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
						new BigDecimal(650), null, StatusAprovacao.PENDENTE,
						new Date(), true);

		movimentoFinanceiroEnvioEncalhe1.setDataCriacao(DateUtil.adicionarDias(new Date(), -8));

		movimentoFinanceiroEnvioEncalhe2 =
					Fixture.movimentoFinanceiroCota(
						cotaMaria, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
						new BigDecimal(650), null, StatusAprovacao.PENDENTE,
						new Date(), true);

		movimentoFinanceiroEnvioEncalhe2.setDataCriacao(DateUtil.adicionarDias(new Date(), -8));

		save(session, movimentoFinanceiroCota31, movimentoFinanceiroCota32,
				  	  movimentoFinanceiroCota33, movimentoFinanceiroCota34,
				  	  movimentoFinanceiroDebito1, movimentoFinanceiroDebito2,
				  	  movimentoFinanceiroCredito1, movimentoFinanceiroCredito2,
				  	  movimentoFinanceiroJuros1, movimentoFinanceiroJuros2,
				  	  movimentoFinanceiroMulta1, movimentoFinanceiroMulta2,
				  	  movimentoFinanceiroEnvioEncalhe1, movimentoFinanceiroEnvioEncalhe2);
	}

	private static void criarMassaNotaFiscalEntradaFornecedorParaRecebimentoFisico(Session session) {

		NotaFiscalEntradaFornecedor nfEntradaFornec = null;

		Long 		unidadeProduto           = 0L;

		Long numero = null;
		String serie = null;
		String chaveAcesso = null;

		numero 		= 1000000L;
		serie 		= "8585";
		chaveAcesso = "939490";

		nfEntradaFornec = Fixture.notaFiscalEntradaFornecedor(
				numero,
				serie,
				chaveAcesso,
				cfop5102,
				fornecedorDinap,
				tipoNotaFiscalRecebimento,
				usuarioJoao,
				new BigDecimal(2500),
				new BigDecimal(500),
				new BigDecimal(2000));

		nfEntradaFornec.setOrigem(Origem.MANUAL);
		session.save(nfEntradaFornec);

		numero 		= 1000001L;
		serie 		= "8585";
		chaveAcesso = "939491";

		nfEntradaFornec = Fixture.notaFiscalEntradaFornecedor(
				numero,
				serie,
				chaveAcesso,
				cfop5102,
				fornecedorDinap,
				tipoNotaFiscalRecebimento,
				usuarioJoao,
				new BigDecimal(3500),
				new BigDecimal(400),
				new BigDecimal(3100));

		nfEntradaFornec.setOrigem(Origem.INTERFACE);
		session.save(nfEntradaFornec);


		ItemNotaFiscalEntrada itemNFEntrada = Fixture.itemNotaFiscalEntradaNFE(
				produtoEdicaoCaras1,
				usuarioJoao,
				nfEntradaFornec,
				Fixture.criarData(05, Calendar.MAY, 2012),
				Fixture.criarData(20, Calendar.MAY, 2012),
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50),
				"",
				"",
				unidadeProduto,
				"",
				"",
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO);

		session.save(itemNFEntrada);

		numero 		= 1000002L;
		serie 		= "8585";
		chaveAcesso = "939492";

		nfEntradaFornec = Fixture.notaFiscalEntradaFornecedor(
				numero,
				serie,
				chaveAcesso,
				cfop5102,
				fornecedorDinap,
				tipoNotaFiscalRecebimento,
				usuarioJoao,
				new BigDecimal(1500),
				new BigDecimal(500),
				new BigDecimal(1000));

		nfEntradaFornec.setOrigem(Origem.INTERFACE);
		session.save(nfEntradaFornec);

		itemNFEntrada = Fixture.itemNotaFiscalEntradaNFE(
				produtoEdicaoCaras1,
				usuarioJoao,
				nfEntradaFornec,
				Fixture.criarData(05, Calendar.MAY, 2012),
				Fixture.criarData(20, Calendar.MAY, 2012),
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50),
				"",
				"",
				unidadeProduto,
				"",
				"",
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				BigDecimal.ZERO);
		
		itemNFEntrada.setOrigem(Origem.INTERFACE);
		
		session.save(itemNFEntrada);

	}

	private static void criarNotasFiscaisEntradaFornecedor(Session session) {

		for (int i = 0; i < 50; i++) {

			Long numero = 0L;
			String serie = "";
			String chaveAcesso = "";

			Calendar calendar = Calendar.getInstance();

			TipoNotaFiscal tipoNotaFiscal = i % 2 == 0 ? tipoNotaFiscalRecebimento : tipoNotaFiscalDevolucao;

			notaFiscalFornecedor = Fixture.notaFiscalEntradaFornecedor(
					cfop5102,
					fornecedorDinap,
					tipoNotaFiscal,
					usuarioJoao,
					new BigDecimal(15),
					new BigDecimal(5),
					BigDecimal.TEN);

			calendar.add(Calendar.DATE, i * 3);
			notaFiscalFornecedor.setDataEmissao(calendar.getTime());
			session.save(notaFiscalFornecedor);

		}
	}

	private static void gerarCargaHistoricoSituacaoCota(Session session, int quantidade) {

		Date dataAtual = new Date();

		for (int i = 1; i <= quantidade; i++) {

			HistoricoSituacaoCota historicoSituacaoCota = new HistoricoSituacaoCota();

			historicoSituacaoCota.setCota(cotaManoel);
			historicoSituacaoCota.setDataEdicao(new Date());
			historicoSituacaoCota.setDescricao("Descrição " + i);
			historicoSituacaoCota.setMotivo(MotivoAlteracaoSituacao.INADIMPLENCIA);
			historicoSituacaoCota.setNovaSituacao(SituacaoCadastro.ATIVO);
			historicoSituacaoCota.setSituacaoAnterior(SituacaoCadastro.SUSPENSO);
			historicoSituacaoCota.setTipoEdicao(TipoEdicao.ALTERACAO);
			historicoSituacaoCota.setDataInicioValidade(DateUtil.adicionarDias(dataAtual, i));
			historicoSituacaoCota.setDataFimValidade(DateUtil.adicionarDias(dataAtual, i));
			historicoSituacaoCota.setResponsavel(usuarioJoao);

			session.save(historicoSituacaoCota);
		}
	}

	private static void gerarEntregadores(Session session) {

		Entregador entregador = Fixture.criarEntregador(
				234L, true, new Date(),
				BigDecimal.TEN, juridicaAcme, false, null);

		save(session, juridicaAcme, entregador);

		entregador = Fixture.criarEntregador(
				123L, false, new Date(),
				null, juridicaFc, false, null);
		save(session, juridicaFc, entregador);

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "13131313", "Rua Marechal deodoro", "50", "Centro", "Mococa", "SP",3530508);

		EnderecoEntregador enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.COMERCIAL);

		Telefone telefone = Fixture.telefone("19", "36560000", null);

		TelefoneEntregador telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.COMERCIAL);

		save(session, endereco, enderecoEntregador, telefone, telefoneEntregador);

		jose.setApelido("Zezinho");

		entregador = Fixture.criarEntregador(
				345L, false, new Date(),
				null, jose, false, null);
		save(session, jose, entregador);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Avenida Brasil", "10", "Centro", "Ribeirão Preto", "SP",3543402);

		enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.COBRANCA);

		telefone = Fixture.telefone("19", "36112887", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.CELULAR);

		save(session, endereco, enderecoEntregador, telefone, telefoneEntregador);

		maria.setApelido("Mariazinha");

		save(session, maria);

		entregador = Fixture.criarEntregador(
				456L, false, new Date(),
				null, maria, false, null);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Itaquera", "10", "Centro", "São Paulo", "SP",3550308);

		enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.RESIDENCIAL);

		telefone = Fixture.telefone("11", "31053333", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.CELULAR);

		save(session, entregador, endereco, enderecoEntregador, telefone, telefoneEntregador);
	}

	private static void criarDadosBalanceamentos(Session session) {

		//EDITORES
		Editor globo = Fixture.criarEditor(680L, juridicaDinap, true);

		Editor europa = Fixture.criarEditor( 681L, juridicaAcme, true);

		Editor jazz = Fixture.criarEditor(682L, juridicaFc, true);

		//TipoProduto tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(session, globo, europa, jazz);

		//PRODUTOS
		Produto javaMagazine = Fixture.produto("541", "Java Magazine", "Java Magazine", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		javaMagazine.setEditor(europa);
		javaMagazine.addFornecedor(fornecedorDinap);
		save(session, javaMagazine);

		Produto mundoJava = Fixture.produto("542", "Mundo Java", "Mundo Java", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		mundoJava.setEditor(globo);
		mundoJava.addFornecedor(fornecedorDinap);
		save(session, mundoJava);

		Produto sqlMagazine = Fixture.produto("543", "SQL Magazine", "SQL Magazine", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		sqlMagazine.setEditor(europa);
		sqlMagazine.addFornecedor(fornecedorDinap);
		save(session, sqlMagazine);

		Produto galileu = Fixture.produto("544", "Galileu", "Galileu", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		galileu.setEditor(globo);
		galileu.addFornecedor(fornecedorDinap);
		save(session, galileu);

		Produto duasRodas = Fixture.produto("545", "Duas Rodas", "Duas Rodas", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		duasRodas.setEditor(globo);
		duasRodas.addFornecedor(fornecedorFc);
		save(session, duasRodas);

		Produto guitarPlayer = Fixture.produto("546", "Guitar Player", "Guitar Player", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		guitarPlayer.setEditor(jazz);
		guitarPlayer.addFornecedor(fornecedorDinap);
		save(session, guitarPlayer);

		Produto roadieCrew = Fixture.produto("547", "Roadie Crew", "Roadie Crew", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		roadieCrew.setEditor(editoraAbril);
		roadieCrew.addFornecedor(fornecedorFc);
		save(session, roadieCrew);

		Produto rockBrigade = Fixture.produto("548", "Rock Brigade", "Rock Brigade", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		rockBrigade.setEditor(editoraAbril);
		rockBrigade.addFornecedor(fornecedorFc);
		save(session, rockBrigade);

		Produto valhalla = Fixture.produto("549", "Valhalla", "Valhalla", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		valhalla.setEditor(editoraAbril);
		valhalla.addFornecedor(fornecedorFc);
		save(session, valhalla);

		Produto rollingStone = Fixture.produto("550", "Rolling Stone", "Rolling Stone", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		rollingStone.setEditor(editoraAbril);
		rollingStone.addFornecedor(fornecedorFc);
		save(session, rollingStone);

		Produto bonsFluidos = Fixture.produto("551", "Bons Fluídos", "Bons Fluídos", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		bonsFluidos.setEditor(europa);
		bonsFluidos.addFornecedor(fornecedorDinap);
		save(session, bonsFluidos);

		Produto bravo = Fixture.produto("552", "Revista Bravo", "Revista Bravo", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		bravo.setEditor(globo);
		bravo.addFornecedor(fornecedorDinap);
		save(session, bravo);

		Produto casaClaudia = Fixture.produto("553", "Casa Claudia", "Revista Casa Claudia", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		casaClaudia.setEditor(europa);
		casaClaudia.addFornecedor(fornecedorDinap);
		save(session, casaClaudia);

		Produto jequiti = Fixture.produto("554", "Jequiti", "Jequiti", PeriodicidadeProduto.SEMANAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		jequiti.setEditor(globo);
		jequiti.addFornecedor(fornecedorDinap);
		save(session, jequiti);

		Produto mundoEstranho = Fixture.produto("555", "Mundo Estranho", "Revista Mundo Estranho", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		mundoEstranho.setEditor(globo);
		mundoEstranho.addFornecedor(fornecedorFc);
		save(session, mundoEstranho);

		Produto novaEscola = Fixture.produto("556", "Nova Escola", "Revista Nova Escola", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		novaEscola.setEditor(jazz);
		novaEscola.addFornecedor(fornecedorDinap);
		save(session, novaEscola);

		Produto minhaCasa = Fixture.produto("557", "Minha Casa", "Revista Minha Casa", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		minhaCasa.setEditor(editoraAbril);
		minhaCasa.addFornecedor(fornecedorFc);
		save(session, minhaCasa);

		Produto recreio = Fixture.produto("558", "Recreio", "Revista Recreio", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		recreio.setEditor(editoraAbril);
		recreio.addFornecedor(fornecedorFc);
		save(session, recreio);

		Produto womenHealth = Fixture.produto("559", "Women's Health", "Revista Women's Health", PeriodicidadeProduto.MENSAL, tipoProdutoCromo, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		womenHealth.setEditor(editoraAbril);
		womenHealth.addFornecedor(fornecedorFc);
		save(session, womenHealth);

		Produto viagemTurismo = Fixture.produto("560", "Viagem e Turismo", "Revista Viagem e Turismo", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		viagemTurismo.setEditor(editoraAbril);
		viagemTurismo.addFornecedor(fornecedorFc);
		save(session, viagemTurismo);

		Produto vip = Fixture.produto("561", "VIP", "Revista VIP", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		vip.setEditor(jazz);
		vip.addFornecedor(fornecedorDinap);
		save(session, vip);

		Produto gestaoEscolar = Fixture.produto("562", "Gestão Escolar", "Gestão Escolar", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		gestaoEscolar.setEditor(editoraAbril);
		gestaoEscolar.addFornecedor(fornecedorFc);
		save(session, gestaoEscolar);

		Produto lola = Fixture.produto("563", "Lola", "Lola", PeriodicidadeProduto.MENSAL, tipoProdutoCromo, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		lola.setEditor(editoraAbril);
		lola.addFornecedor(fornecedorFc);
		save(session, lola);

		Produto heavyMetal = Fixture.produto("539", "Heavy Metal", "Heavy Metal", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		heavyMetal.setEditor(editoraAbril);
		heavyMetal.addFornecedor(fornecedorFc);
		save(session, heavyMetal);

		Produto metalUnderground = Fixture.produto("540", "Metal Underground", "Metal Underground", PeriodicidadeProduto.MENSAL, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		metalUnderground.setEditor(editoraAbril);
		metalUnderground.addFornecedor(fornecedorFc);
		save(session, metalUnderground);


		//PRODUTOS EDIÇÃO
		javaMagazineEdicao101 =
				Fixture.produtoEdicao("COD_001", 101L, 10, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(20),
									  "001", 1L, javaMagazine, BigDecimal.TEN, true, "javaMagazineEdicao101");

		javaMagazineEdicao102 =
				Fixture.produtoEdicao("COD_002", 102L, 13, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(17),
									  "002", 2L, javaMagazine, BigDecimal.TEN, true);

		mundoJavaEdicao101 =
				Fixture.produtoEdicao("COD_003", 101L, 5, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(10.5),
									  "003", 3L, mundoJava, BigDecimal.TEN, false);

		mundoJavaEdicao102 =
				Fixture.produtoEdicao("COD_004", 102L, 15, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(2.9),
									  "004", 4L, mundoJava, BigDecimal.TEN, false);

		sqlMagazineEdicao101 =
				Fixture.produtoEdicao("COD_005", 101L, 10, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(23),
									  "005", 5L, sqlMagazine, BigDecimal.TEN, false);

		sqlMagazineEdicao102 =
				Fixture.produtoEdicao("COD_006", 102L, 8, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(10),
									  "006", 6L, sqlMagazine, BigDecimal.TEN, false);

		galileuEdicao101 =
				Fixture.produtoEdicao("COD_007", 101L, 3, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(12),
									  "007", 7L, galileu, BigDecimal.TEN, true);

		galileuEdicao102 =
				Fixture.produtoEdicao("COD_008", 102L, 9, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(32.1),
									  "008", 8L, galileu, BigDecimal.TEN, true);

		duasRodasEdicao101 =
				Fixture.produtoEdicao("COD_009", 101L, 8, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(11),
									  "009", 9L, duasRodas, BigDecimal.TEN, false);

		duasRodasEdicao102 =
				Fixture.produtoEdicao("COD_010", 102L, 6, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "010", 10L, duasRodas, BigDecimal.TEN, false);

		guitarPlayerEdicao101 =
				Fixture.produtoEdicao("COD_011", 101L, 2, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "011", 11L, guitarPlayer, BigDecimal.TEN, false);

		guitarPlayerEdicao102 =
				Fixture.produtoEdicao("COD_012", 102L, 6, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(15),
									  "012", 12L, guitarPlayer, BigDecimal.TEN, true);

		roadieCrewEdicao101 =
				Fixture.produtoEdicao("COD_013", 101L, 3, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(20),
									  "013", 13L, roadieCrew, BigDecimal.TEN, true);

		roadieCrewEdicao102 =
				Fixture.produtoEdicao("COD_014", 102L, 9, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(28),
									  "014", 14L, roadieCrew, BigDecimal.TEN, false);

		rockBrigadeEdicao101 =
				Fixture.produtoEdicao("COD_015", 101L, 5, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(19),
									  "015", 15L, rockBrigade, BigDecimal.TEN, false);

		rockBrigadeEdicao102 =
				Fixture.produtoEdicao("COD_016", 102L, 15, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(21.9),
									  "016", 16L, rockBrigade, BigDecimal.TEN, true);

		valhallaEdicao101 =
				Fixture.produtoEdicao("COD_017", 101L, 10, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(29.9),
									  "017", 17L, valhalla, BigDecimal.TEN, true);

		valhallaEdicao102 =
				Fixture.produtoEdicao("COD_018", 102L, 7, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(19.9),
									  "018", 18L, valhalla, BigDecimal.TEN, true);

		rollingStoneEdicao101 =
				Fixture.produtoEdicao("COD_019", 101L, 12, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(14),
									  "019", 19L, rollingStone, BigDecimal.TEN, false);

		rollingStoneEdicao102 =
				Fixture.produtoEdicao("COD_020", 102L, 11, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(13.5),
									  "020", 20L, rollingStone, BigDecimal.TEN, false);

		bonsFluidosEdicao101 =
				Fixture.produtoEdicao("COD_021", 101L, 8, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(10),
									  "021", 1L, bonsFluidos, BigDecimal.TEN, false);

		bonsFluidosEdicao102 =
				Fixture.produtoEdicao("COD_022", 102L, 7, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(20),
									  "022", 2L, bonsFluidos, BigDecimal.TEN, false);

		bravoEdicao101 =
				Fixture.produtoEdicao("COD_023", 101L, 6, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(20),
									  "023", 3L, bravo, BigDecimal.TEN, false);

		bravoEdicao102 =
				Fixture.produtoEdicao("COD_024", 102L, 7, 14,
						new Long(100), BigDecimal.TEN, new BigDecimal(20),
									  "024", 4L, bravo, BigDecimal.TEN, false);

		casaClaudiaEdicao101 =
				Fixture.produtoEdicao("COD_025", 101L, 4, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(15),
									  "025", 5L, casaClaudia, BigDecimal.TEN, false);

		casaClaudiaEdicao102 =
				Fixture.produtoEdicao("COD_026", 102L, 10, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(18.9),
									  "026", 6L, casaClaudia, BigDecimal.TEN, false);

		jequitiEdicao101 =
				Fixture.produtoEdicao("COD_027", 101L, 11, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(19.9),
									  "027", 7L, jequiti, BigDecimal.TEN, false);

		jequitiEdicao102 =
				Fixture.produtoEdicao("COD_028", 102L, 2, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(12.5),
									  "028", 8L, jequiti, BigDecimal.TEN, false);

		mundoEstranhoEdicao101 =
				Fixture.produtoEdicao("COD_029", 101L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(17.5),
									  "029", 9L, mundoEstranho, BigDecimal.TEN, false);

		mundoEstranhoEdicao102 =
				Fixture.produtoEdicao("COD_030", 102L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(13),
									  "030", 10L, mundoEstranho, BigDecimal.TEN, false);

		novaEscolaEdicao101 =
				Fixture.produtoEdicao("COD_031", 101L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "031", 11L, novaEscola, BigDecimal.TEN, false);

		novaEscolaEdicao102 =
				Fixture.produtoEdicao("COD_032", 102L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(30),
									  "032", 12L, novaEscola, BigDecimal.TEN, false);

		minhaCasaEdicao101 =
				Fixture.produtoEdicao("COD_033", 101L, 5, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(31.5),
									  "033", 13L, minhaCasa, BigDecimal.TEN, false);

		minhaCasaEdicao102 =
				Fixture.produtoEdicao("COD_034", 102L, 15, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "034", 14L, minhaCasa, BigDecimal.TEN, false);

		recreioEdicao101 =
				Fixture.produtoEdicao("COD_035", 101L, 10, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(18.9),
									  "035", 15L, recreio, BigDecimal.TEN, false);

		recreioEdicao102 =
				Fixture.produtoEdicao("COD_036", 102L, 10, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(22),
									  "036", 16L, recreio, BigDecimal.TEN, false);

		womenHealthEdicao101 =
				Fixture.produtoEdicao("COD_037", 101L, 7, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(29),
									  "037", 17L, womenHealth, BigDecimal.TEN, false);

		womenHealthEdicao102 =
				Fixture.produtoEdicao("COD_038", 102L, 9, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(10.5),
									  "038", 18L, womenHealth, BigDecimal.TEN, false);

		viagemTurismoEdicao101 =
				Fixture.produtoEdicao("COD_039", 101L, 7, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(20.05),
									  "039", 19L, viagemTurismo, BigDecimal.TEN, false);

		viagemTurismoEdicao102 =
				Fixture.produtoEdicao("COD_040", 102L, 6, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(14.5),
									  "040", 20L, viagemTurismo, BigDecimal.TEN, false);


		vipEdicao101 =
				Fixture.produtoEdicao("COD_041", 101L, 8, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(12),
									  "041", 11L, vip, BigDecimal.TEN, false);

		vipEdicao102 =
				Fixture.produtoEdicao("COD_042", 102L, 15, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(17),
									  "042", 12L, vip, BigDecimal.TEN, false);

		gestaoEscolarEdicao101 =
				Fixture.produtoEdicao("COD_043", 101L, 14, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(9.9),
									  "043", 13L, gestaoEscolar, BigDecimal.TEN, false);

		gestaoEscolarEdicao102 =
				Fixture.produtoEdicao("COD_044", 102L, 4, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(9.9),
									  "044", 14L, gestaoEscolar, BigDecimal.TEN, true);

		lolaEdicao101 =
				Fixture.produtoEdicao("COD_045", 101L, 5, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(8.5),
									  "045", 15L, lola, BigDecimal.TEN, false);

		lolaEdicao102 =
				Fixture.produtoEdicao("COD_046", 102L, 9, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(6.9),
									  "046", 16L, lola, BigDecimal.TEN, false);

		heavyMetalEdicao101 =
				Fixture.produtoEdicao("COD_047", 101L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(50),
									  "047", 17L, heavyMetal, BigDecimal.TEN, false);

		heavyMetalEdicao102 =
				Fixture.produtoEdicao("COD_048", 102L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(50),
									  "048", 18L, heavyMetal, BigDecimal.TEN, false);

		metalUndergroundEdicao101 =
				Fixture.produtoEdicao("COD_049", 101L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "049", 19L, metalUnderground, BigDecimal.TEN, false);

		metalUndergroundEdicao102 =
				Fixture.produtoEdicao("COD_050", 102L, 1, 14,
						new Long(200), BigDecimal.TEN, new BigDecimal(16),
									  "050", 20L, metalUnderground, BigDecimal.TEN, false);

		save(session, javaMagazineEdicao101, javaMagazineEdicao102, mundoJavaEdicao101, mundoJavaEdicao102, sqlMagazineEdicao101, sqlMagazineEdicao102,
					  galileuEdicao101, galileuEdicao102, duasRodasEdicao101, duasRodasEdicao102, guitarPlayerEdicao101, guitarPlayerEdicao102,
					  roadieCrewEdicao101, roadieCrewEdicao102, rockBrigadeEdicao101, rockBrigadeEdicao102, valhallaEdicao101, valhallaEdicao102,
					  rollingStoneEdicao101, rollingStoneEdicao102, bonsFluidosEdicao101, bonsFluidosEdicao102, bravoEdicao101, bravoEdicao102,
					  casaClaudiaEdicao101, casaClaudiaEdicao102, jequitiEdicao101, jequitiEdicao102, mundoEstranhoEdicao101, mundoEstranhoEdicao102,
					  novaEscolaEdicao101, novaEscolaEdicao102, minhaCasaEdicao101, minhaCasaEdicao102, recreioEdicao101, recreioEdicao102,
					  womenHealthEdicao101, womenHealthEdicao102, viagemTurismoEdicao101, viagemTurismoEdicao102, vipEdicao101, vipEdicao102,
					  gestaoEscolarEdicao101, gestaoEscolarEdicao102, lolaEdicao101, lolaEdicao102, heavyMetalEdicao101, heavyMetalEdicao102,
					  metalUndergroundEdicao101, metalUndergroundEdicao102);
	}

	private static void criarDadosBalanceamentoLancamento(Session session) {

		Date dataAtual = new Date();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(
				dataAtual, distribuidor.getInicioSemana().getCodigoDiaSemana());

		Date dataLancamento =
			DateUtil.obterDataDaSemanaNoAno(
				numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), dataAtual);

		Date dataRecolhimento = DateUtil.adicionarDias(dataLancamento, 15);

		NotaFiscalEntradaFornecedor notaFiscal =
			Fixture.notaFiscalEntradaFornecedor(cfop5102, fornecedorFc,
												tipoNotaFiscalRecebimento, usuarioJoao, BigDecimal.TEN,
												BigDecimal.ZERO, BigDecimal.TEN);

		save(session, notaFiscal);

		ItemNotaFiscalEntrada itemNotaFiscalJavaMagazine101 =
			Fixture.itemNotaFiscal(javaMagazineEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalMundoJavaEdicao101 =
			Fixture.itemNotaFiscal(mundoJavaEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalSqlMagazineEdicao101 =
			Fixture.itemNotaFiscal(sqlMagazineEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalGalileuEdicao101 =
			Fixture.itemNotaFiscal(galileuEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalDuasRodasEdicao101 =
			Fixture.itemNotaFiscal(duasRodasEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalBonsFluidosEdicao101 =
			Fixture.itemNotaFiscal(bonsFluidosEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalBravoEdicao101 =
			Fixture.itemNotaFiscal(bravoEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalCasaClaudiaEdicao101 =
			Fixture.itemNotaFiscal(casaClaudiaEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalJequitiEdicao101 =
			Fixture.itemNotaFiscal(jequitiEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalMundoEstranhoEdicao101 =
			Fixture.itemNotaFiscal(mundoEstranhoEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalBonsFluidosEdicao102 =
			Fixture.itemNotaFiscal(bonsFluidosEdicao102, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalBravoEdicao102 =
			Fixture.itemNotaFiscal(bravoEdicao102, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalCasaClaudiaEdicao102 =
			Fixture.itemNotaFiscal(casaClaudiaEdicao102, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalJequitiEdicao102 =
			Fixture.itemNotaFiscal(jequitiEdicao102, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		ItemNotaFiscalEntrada itemNotaFiscalMundoEstranhoEdicao102 =
			Fixture.itemNotaFiscal(mundoEstranhoEdicao102, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));
		
		ItemNotaFiscalEntrada itemNotaFiscalJavaMagazine101Suplementar =
			Fixture.itemNotaFiscal(javaMagazineEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));
		
		ItemNotaFiscalEntrada itemNotaFiscalJavaMagazine101Suplementar2 =
			Fixture.itemNotaFiscal(javaMagazineEdicao101, usuarioJoao, notaFiscal,
								   dataLancamento, dataRecolhimento, TipoLancamento.LANCAMENTO,
								   BigInteger.valueOf(100));

		save(session, itemNotaFiscalJavaMagazine101, itemNotaFiscalMundoJavaEdicao101,
					  itemNotaFiscalSqlMagazineEdicao101, itemNotaFiscalGalileuEdicao101,
					  itemNotaFiscalDuasRodasEdicao101, itemNotaFiscalBonsFluidosEdicao101,
					  itemNotaFiscalBravoEdicao101, itemNotaFiscalCasaClaudiaEdicao101,
					  itemNotaFiscalJequitiEdicao101, itemNotaFiscalMundoEstranhoEdicao101,
					  itemNotaFiscalBonsFluidosEdicao102, itemNotaFiscalBravoEdicao102,
					  itemNotaFiscalCasaClaudiaEdicao102, itemNotaFiscalJequitiEdicao102,
					  itemNotaFiscalMundoEstranhoEdicao102, itemNotaFiscalJavaMagazine101Suplementar,
					  itemNotaFiscalJavaMagazine101Suplementar2);

		RecebimentoFisico recebimentoFisico =
			Fixture.recebimentoFisico(notaFiscal, usuarioJoao, new Date(),
									  new Date(), StatusConfirmacao.CONFIRMADO);

		save(session, recebimentoFisico);

		ItemRecebimentoFisico itemRecebimentoFisicoJavaMagazineEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalJavaMagazine101, recebimentoFisico,
										  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoMundoJavaEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalMundoJavaEdicao101, recebimentoFisico,
										  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoSqlMagazineEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalSqlMagazineEdicao101, recebimentoFisico,
										  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoGalileuEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalGalileuEdicao101, recebimentoFisico,
										  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoDuasRodasEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalDuasRodasEdicao101, recebimentoFisico,
										  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoBonsFluidosEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalBonsFluidosEdicao101, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoBravoEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalBravoEdicao101, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoCasaClaudiaEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalCasaClaudiaEdicao101, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoJequitiEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalJequitiEdicao101, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoMundoEstranhoEdicao101 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalMundoEstranhoEdicao101, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoBonsFluidosEdicao102 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalBonsFluidosEdicao102, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoBravoEdicao102 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalBravoEdicao102, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoCasaClaudiaEdicao102 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalCasaClaudiaEdicao102, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoJequitiEdicao102 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalJequitiEdicao102, recebimentoFisico,
											  BigInteger.valueOf(100));

		ItemRecebimentoFisico itemRecebimentoFisicoMundoEstranhoEdicao102 =
			Fixture.itemRecebimentoFisico(itemNotaFiscalMundoEstranhoEdicao102, recebimentoFisico,
											  BigInteger.valueOf(100));
		
		ItemRecebimentoFisico itemRecebimentoFisicoJavaMagazineEdicao101Suplementar =
				Fixture.itemRecebimentoFisico(itemNotaFiscalJavaMagazine101Suplementar, recebimentoFisico,
											  BigInteger.valueOf(100));
		
		ItemRecebimentoFisico itemRecebimentoFisicoJavaMagazineEdicao101Suplementar2 =
				Fixture.itemRecebimentoFisico(itemNotaFiscalJavaMagazine101Suplementar2, recebimentoFisico,
											  BigInteger.valueOf(100));

		save(session, itemRecebimentoFisicoJavaMagazineEdicao101, itemRecebimentoFisicoMundoJavaEdicao101,
					  itemRecebimentoFisicoSqlMagazineEdicao101, itemRecebimentoFisicoGalileuEdicao101,
					  itemRecebimentoFisicoDuasRodasEdicao101, itemRecebimentoFisicoBonsFluidosEdicao101,
					  itemRecebimentoFisicoBravoEdicao101, itemRecebimentoFisicoCasaClaudiaEdicao101,
					  itemRecebimentoFisicoJequitiEdicao101, itemRecebimentoFisicoMundoEstranhoEdicao101,
					  itemRecebimentoFisicoBonsFluidosEdicao102, itemRecebimentoFisicoBravoEdicao102,
					  itemRecebimentoFisicoCasaClaudiaEdicao102, itemRecebimentoFisicoJequitiEdicao102,
					  itemRecebimentoFisicoMundoEstranhoEdicao102, itemRecebimentoFisicoJavaMagazineEdicao101Suplementar,
					  itemRecebimentoFisicoJavaMagazineEdicao101Suplementar2);

		//LANCAMENTOS
		Lancamento lancamentoJavaMagazineEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, javaMagazineEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoJavaMagazineEdicao101, 1);

		Lancamento lancamentoMundoJavaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoJavaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoMundoJavaEdicao101, 1);

		Lancamento lancamentoSqlMagazineEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, sqlMagazineEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoSqlMagazineEdicao101, 1);

		Lancamento lancamentoGalileuEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, galileuEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoGalileuEdicao101, 1);

		Lancamento lancamentoDuasRodasEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, duasRodasEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoDuasRodasEdicao101, 1);

		Lancamento lancamentoGuitarPlayerEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, guitarPlayerEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.FURO, null, 1);

		Lancamento lancamentoRoadieCrewEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, roadieCrewEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRockBrigadeEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rockBrigadeEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoValhallaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, valhallaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRollingStoneEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rollingStoneEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);

		Lancamento lancamentoBonsFluidosEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bonsFluidosEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBonsFluidosEdicao101, 1);

		Lancamento lancamentoBravoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bravoEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBravoEdicao101, 1);

		Lancamento lancamentoCasaClaudiaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, casaClaudiaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoCasaClaudiaEdicao101, 1);

		Lancamento lancamentoJequitiEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, jequitiEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoJequitiEdicao101, 1);

		Lancamento lancamentoMundoEstranhoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoEstranhoEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoMundoEstranhoEdicao101, 1);

		Lancamento lancamentoNovaEscolaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, novaEscolaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoMinhaCasaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, minhaCasaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRecreioEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, recreioEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoWomenHealthEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, womenHealthEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoViagemTurismoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, viagemTurismoEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);

		Lancamento lancamentoVipEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, vipEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoGestaoEscolarEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, gestaoEscolarEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.FURO, null, 1);

		Lancamento lancamentoLolaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, lolaEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoHeavyMetalEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, heavyMetalEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.FURO, null, 1);

		Lancamento lancamentoMetalUndergroundEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, metalUndergroundEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoJavaMagazineEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, javaMagazineEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoMundoJavaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoJavaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoSqlMagazineEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, sqlMagazineEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoGalileuEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, galileuEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoDuasRodasEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, duasRodasEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoJavaMagazineEdicao101Suplementar = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, javaMagazineEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoJavaMagazineEdicao101Suplementar, 1);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);

		Lancamento lancamentoGuitarPlayerEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, guitarPlayerEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRoadieCrewEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, roadieCrewEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRockBrigadeEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rockBrigadeEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoValhallaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, valhallaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRollingStoneEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rollingStoneEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoBonsFluidosEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bonsFluidosEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBonsFluidosEdicao102, 1);

		Lancamento lancamentoBravoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bravoEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBravoEdicao102, 1);

		Lancamento lancamentoCasaClaudiaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, casaClaudiaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoCasaClaudiaEdicao102, 1);

		Lancamento lancamentoJequitiEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, jequitiEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoJequitiEdicao102, 1);

		Lancamento lancamentoMundoEstranhoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoEstranhoEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoMundoEstranhoEdicao102, 1);

		Lancamento lancamentoJavaMagazineEdicao101Suplementar2 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, javaMagazineEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoJavaMagazineEdicao101Suplementar2, 1);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);

		Lancamento lancamentoNovaEscolaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, novaEscolaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoMinhaCasaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, minhaCasaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRecreioEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, recreioEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoWomenHealthEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, womenHealthEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoViagemTurismoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, viagemTurismoEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoVipEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, vipEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoGestaoEscolarEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, gestaoEscolarEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoLolaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, lolaEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoHeavyMetalEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, heavyMetalEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoMetalUndergroundEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, metalUndergroundEdicao102,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoJavaMagazineEdicao101Suplementar3 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, javaMagazineEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		Lancamento lancamentoRoadieCrewEdicao101Suplementar = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, roadieCrewEdicao101,
				dataLancamento,
				dataRecolhimento,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		save(session, lancamentoJavaMagazineEdicao101, lancamentoMundoJavaEdicao101, lancamentoSqlMagazineEdicao101, lancamentoGalileuEdicao101,
					  lancamentoDuasRodasEdicao101, lancamentoGuitarPlayerEdicao101, lancamentoRoadieCrewEdicao101, lancamentoRockBrigadeEdicao101,
					  lancamentoValhallaEdicao101, lancamentoRollingStoneEdicao101, lancamentoBonsFluidosEdicao101, lancamentoBravoEdicao101,
					  lancamentoCasaClaudiaEdicao101, lancamentoJequitiEdicao101, lancamentoMundoEstranhoEdicao101, lancamentoNovaEscolaEdicao101,
					  lancamentoMinhaCasaEdicao101, lancamentoRecreioEdicao101, lancamentoWomenHealthEdicao101, lancamentoViagemTurismoEdicao101,
					  lancamentoVipEdicao101, lancamentoGestaoEscolarEdicao101, lancamentoLolaEdicao101, lancamentoHeavyMetalEdicao101,
					  lancamentoMetalUndergroundEdicao101, lancamentoJavaMagazineEdicao102, lancamentoMundoJavaEdicao102, lancamentoSqlMagazineEdicao102,
					  lancamentoGalileuEdicao102, lancamentoDuasRodasEdicao102, lancamentoGuitarPlayerEdicao102, lancamentoRoadieCrewEdicao102,
					  lancamentoRockBrigadeEdicao102, lancamentoValhallaEdicao102, lancamentoRollingStoneEdicao102, lancamentoBonsFluidosEdicao102,
					  lancamentoBravoEdicao102, lancamentoCasaClaudiaEdicao102, lancamentoJequitiEdicao102, lancamentoMundoEstranhoEdicao102,
					  lancamentoNovaEscolaEdicao102, lancamentoMinhaCasaEdicao102, lancamentoRecreioEdicao102, lancamentoWomenHealthEdicao102,
					  lancamentoViagemTurismoEdicao102, lancamentoVipEdicao102, lancamentoGestaoEscolarEdicao102, lancamentoLolaEdicao102,
					  lancamentoHeavyMetalEdicao102, lancamentoMetalUndergroundEdicao102, lancamentoJavaMagazineEdicao101Suplementar,
					  lancamentoJavaMagazineEdicao101Suplementar2, lancamentoJavaMagazineEdicao101Suplementar3, lancamentoRoadieCrewEdicao101Suplementar);
		
		LancamentoParcial lancamentoParcialGalileuEdicao102 =
			Fixture.criarLancamentoParcial(galileuEdicao102,
					lancamentoGalileuEdicao102.getDataLancamentoPrevista(),
					DateUtil.adicionarDias(lancamentoGalileuEdicao102.getDataRecolhimentoPrevista(), 10),
				    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialGuitarPlayerEdicao102 =
			Fixture.criarLancamentoParcial(guitarPlayerEdicao102,
					lancamentoGuitarPlayerEdicao102.getDataLancamentoPrevista(),
					DateUtil.adicionarDias(lancamentoGuitarPlayerEdicao102.getDataRecolhimentoPrevista(), 10),
				    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialRoadieCrewEdicao101 =
			Fixture.criarLancamentoParcial(roadieCrewEdicao101,
					lancamentoRoadieCrewEdicao101.getDataLancamentoPrevista(),
					DateUtil.adicionarDias(lancamentoRoadieCrewEdicao101.getDataRecolhimentoPrevista(), 10),
				    StatusLancamentoParcial.PROJETADO);

		save(session, lancamentoParcialGalileuEdicao102, lancamentoParcialGuitarPlayerEdicao102,
					  lancamentoParcialRoadieCrewEdicao101);

		PeriodoLancamentoParcial periodoLancamentoParcialGalileuEdicao102 =
			Fixture.criarPeriodoLancamentoParcial(
					lancamentoGalileuEdicao102,
					lancamentoParcialGalileuEdicao102,
					StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialGuitarPlayerEdicao102 =
			Fixture.criarPeriodoLancamentoParcial(
					lancamentoGuitarPlayerEdicao102,
					lancamentoParcialGuitarPlayerEdicao102,
					StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);

		PeriodoLancamentoParcial periodoLancamentoParcialRoadieCrewEdicao101 =
			Fixture.criarPeriodoLancamentoParcial(
					lancamentoRoadieCrewEdicao101,
					lancamentoParcialRoadieCrewEdicao101,
					StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		save(session, periodoLancamentoParcialGalileuEdicao102,
					  periodoLancamentoParcialGuitarPlayerEdicao102,
					  periodoLancamentoParcialRoadieCrewEdicao101);

		//ESTUDOS
		Estudo estudoJavaMagazineEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoJavaMagazineEdicao101.getDataLancamentoDistribuidor(), javaMagazineEdicao101);

		Estudo estudoMundoJavaEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoMundoJavaEdicao101.getDataLancamentoDistribuidor(), mundoJavaEdicao101);

		Estudo estudoSqlMagazineEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoSqlMagazineEdicao101.getDataLancamentoDistribuidor(), sqlMagazineEdicao101);

		Estudo estudoGalileuEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoGalileuEdicao101.getDataLancamentoDistribuidor(), galileuEdicao101);

		Estudo estudoDuasRodasEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoDuasRodasEdicao101.getDataLancamentoDistribuidor(), duasRodasEdicao101);

		Estudo estudoBonsFluidosEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoBonsFluidosEdicao101.getDataLancamentoDistribuidor(), bonsFluidosEdicao101);

		Estudo estudoBravoEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoBravoEdicao101.getDataLancamentoDistribuidor(), bravoEdicao101);

		Estudo estudoCasaClaudiaEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoCasaClaudiaEdicao101.getDataLancamentoDistribuidor(), casaClaudiaEdicao101);

		Estudo estudoJequitiEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoJequitiEdicao101.getDataLancamentoDistribuidor(), jequitiEdicao101);

		Estudo estudoMundoEstranhoEdicao101 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoMundoEstranhoEdicao101.getDataLancamentoDistribuidor(), mundoEstranhoEdicao101);

		Estudo estudoBonsFluidosEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoBonsFluidosEdicao102.getDataLancamentoDistribuidor(), bonsFluidosEdicao102);

		Estudo estudoBravoEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoBravoEdicao102.getDataLancamentoDistribuidor(), bravoEdicao102);

		Estudo estudoCasaClaudiaEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoCasaClaudiaEdicao102.getDataLancamentoDistribuidor(), casaClaudiaEdicao102);

		Estudo estudoJequitiEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoJequitiEdicao102.getDataLancamentoDistribuidor(), jequitiEdicao102);

		Estudo estudoMundoEstranhoEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoMundoEstranhoEdicao102.getDataLancamentoDistribuidor(), mundoEstranhoEdicao102);

		Estudo estudoVipEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoVipEdicao102.getDataLancamentoDistribuidor(), vipEdicao102);

		Estudo estudoGestaoEscolarEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoGestaoEscolarEdicao102.getDataLancamentoDistribuidor(), gestaoEscolarEdicao102);

		Estudo estudoLolaEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoLolaEdicao102.getDataLancamentoDistribuidor(), lolaEdicao102);

		Estudo estudoHeavyMetalEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoHeavyMetalEdicao102.getDataLancamentoDistribuidor(), heavyMetalEdicao102);

		Estudo estudoMetalUndergroundEdicao102 =
			Fixture.estudo(BigInteger.valueOf(180),
					lancamentoMetalUndergroundEdicao102.getDataLancamentoDistribuidor(), metalUndergroundEdicao102);
		
		Estudo estudoJavaMagazineEdicao101Suplementar =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJavaMagazineEdicao101Suplementar.getDataLancamentoDistribuidor(), javaMagazineEdicao101);
		
		Estudo estudoJavaMagazineEdicao101Suplementar2 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJavaMagazineEdicao101Suplementar2.getDataLancamentoDistribuidor(), javaMagazineEdicao101);

		save(session, estudoJavaMagazineEdicao101, estudoMundoJavaEdicao101,
					  estudoSqlMagazineEdicao101, estudoGalileuEdicao101,
					  estudoDuasRodasEdicao101, estudoBonsFluidosEdicao101,
					  estudoBravoEdicao101, estudoCasaClaudiaEdicao101,
					  estudoJequitiEdicao101, estudoMundoEstranhoEdicao101,
					  estudoBonsFluidosEdicao102, estudoBravoEdicao102,
					  estudoCasaClaudiaEdicao102, estudoJequitiEdicao102,
					  estudoMundoEstranhoEdicao102, estudoVipEdicao102,
					  estudoGestaoEscolarEdicao102, estudoLolaEdicao102,
					  estudoHeavyMetalEdicao102, estudoMetalUndergroundEdicao102,
					  estudoJavaMagazineEdicao101Suplementar, estudoJavaMagazineEdicao101Suplementar2);

		//ESTUDOS COTA
		EstudoCota estudoCotaAcmeJavaMagazineEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMundoJavaEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeSqlMagazineEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeGalileuEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeDuasRodasEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeBonsFluidosEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeBravoEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeCasaClaudiaEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeJequitiEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMundoEstranhoEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeBonsFluidosEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeBravoEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeCasaClaudiaEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeJequitiEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMundoEstranhoEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeVipEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeGestaoEscolarEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeLolaEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeHeavyMetalEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMetalUndergroundEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeJavaMagazineEdicao101Suplementar =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101Suplementar, cotaAcme);
		
		EstudoCota estudoCotaAcmeJavaMagazineEdicao101Suplementar2 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101Suplementar2, cotaAcme);
		
		EstudoCota estudoCotaManoelJavaMagazineEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelMundoJavaEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelSqlMagazineEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelGalileuEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelDuasRodasEdicao101 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelBonsFluidosEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelBravoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelCasaClaudiaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelJequitiEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelMundoEstranhoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelBonsFluidosEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelBravoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelCasaClaudiaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelJequitiEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMundoEstranhoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelVipEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelGestaoEscolarEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelLolaEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelHeavyMetalEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMetalUndergroundEdicao102 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelJavaMagazineEdicao101Suplementar =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101Suplementar, cotaManoel);
		
		EstudoCota estudoCotaManoelJavaMagazineEdicao101Suplementar2 =
			Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101Suplementar2, cotaManoel);
		
		save(session, estudoCotaAcmeJavaMagazineEdicao101, estudoCotaAcmeMundoJavaEdicao101,
					  estudoCotaAcmeSqlMagazineEdicao101, estudoCotaAcmeGalileuEdicao101,
					  estudoCotaAcmeDuasRodasEdicao101, estudoCotaAcmeBonsFluidosEdicao101,
					  estudoCotaAcmeBravoEdicao101, estudoCotaAcmeCasaClaudiaEdicao101,
					  estudoCotaAcmeJequitiEdicao101, estudoCotaAcmeMundoEstranhoEdicao101,
					  estudoCotaAcmeBonsFluidosEdicao102, estudoCotaAcmeBravoEdicao102,
					  estudoCotaAcmeCasaClaudiaEdicao102, estudoCotaAcmeJequitiEdicao102,
					  estudoCotaAcmeMundoEstranhoEdicao102, estudoCotaAcmeVipEdicao102,
					  estudoCotaAcmeGestaoEscolarEdicao102, estudoCotaAcmeLolaEdicao102,
					  estudoCotaAcmeHeavyMetalEdicao102, estudoCotaAcmeMetalUndergroundEdicao102,
					  estudoCotaAcmeJavaMagazineEdicao101Suplementar, estudoCotaAcmeJavaMagazineEdicao101Suplementar2,
					  estudoCotaManoelJavaMagazineEdicao101, estudoCotaManoelMundoJavaEdicao101,
					  estudoCotaManoelSqlMagazineEdicao101, estudoCotaManoelGalileuEdicao101,
					  estudoCotaManoelDuasRodasEdicao101, estudoCotaManoelBonsFluidosEdicao101,
					  estudoCotaManoelBravoEdicao101, estudoCotaManoelCasaClaudiaEdicao101,
					  estudoCotaManoelJequitiEdicao101, estudoCotaManoelMundoEstranhoEdicao101,
					  estudoCotaManoelBonsFluidosEdicao102, estudoCotaManoelBravoEdicao102,
					  estudoCotaManoelCasaClaudiaEdicao102, estudoCotaManoelJequitiEdicao102,
					  estudoCotaManoelMundoEstranhoEdicao102, estudoCotaManoelVipEdicao102,
					  estudoCotaManoelGestaoEscolarEdicao102, estudoCotaManoelLolaEdicao102,
					  estudoCotaManoelHeavyMetalEdicao102, estudoCotaManoelMetalUndergroundEdicao102,
					  estudoCotaManoelJavaMagazineEdicao101Suplementar, estudoCotaManoelJavaMagazineEdicao101Suplementar2);

		FuroProduto furoProdutoHeavyMetalEdicao101 =
			Fixture.furoProduto(new Date(), lancamentoHeavyMetalEdicao101,
								heavyMetalEdicao101, usuarioJoao);

		FuroProduto furoProdutoGestaoEscolarEdicao101 =
			Fixture.furoProduto(new Date(), lancamentoGestaoEscolarEdicao101,
								gestaoEscolarEdicao101, usuarioJoao);

		FuroProduto furoProdutoGuitarPlayerEdicao101 =
			Fixture.furoProduto(new Date(), lancamentoGuitarPlayerEdicao101,
								guitarPlayerEdicao101, usuarioJoao);

		save(session, furoProdutoHeavyMetalEdicao101, furoProdutoGestaoEscolarEdicao101,
					  furoProdutoGuitarPlayerEdicao101);
		
	}

	private static void criarDadosBalanceamentoRecolhimento(Session session) {

		Date dataAtual = new Date();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(
				dataAtual, distribuidor.getInicioSemana().getCodigoDiaSemana());

		Date dataInicioSemanaAtual =
			DateUtil.obterDataDaSemanaNoAno(
				numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), dataAtual);

		Date dataRecolhimentoProximaSemana = DateUtil.adicionarDias(dataInicioSemanaAtual, 7);

		Date dataLancamento = DateUtil.subtrairDias(dataInicioSemanaAtual, 7);

		//LANCAMENTOS
		Lancamento lancamentoJavaMagazineEdicao101 = Fixture.lancamento(
				TipoLancamento.PARCIAL, javaMagazineEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMundoJavaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoJavaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoSqlMagazineEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, sqlMagazineEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoGalileuEdicao101 = Fixture.lancamento(
				TipoLancamento.PARCIAL, galileuEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoDuasRodasEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, duasRodasEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoGuitarPlayerEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, guitarPlayerEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRoadieCrewEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, roadieCrewEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRockBrigadeEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rockBrigadeEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoValhallaEdicao101 = Fixture.lancamento(
				TipoLancamento.PARCIAL, valhallaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRollingStoneEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rollingStoneEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		dataRecolhimentoProximaSemana = DateUtil.adicionarDias(dataRecolhimentoProximaSemana, 1);

		Lancamento lancamentoBonsFluidosEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bonsFluidosEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoBravoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bravoEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoCasaClaudiaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, casaClaudiaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoJequitiEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, jequitiEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMundoEstranhoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoEstranhoEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoNovaEscolaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, novaEscolaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMinhaCasaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, minhaCasaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRecreioEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, recreioEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoWomenHealthEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, womenHealthEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoViagemTurismoEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, viagemTurismoEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		dataRecolhimentoProximaSemana = DateUtil.adicionarDias(dataRecolhimentoProximaSemana, 1);

		Lancamento lancamentoVipEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, vipEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoGestaoEscolarEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, gestaoEscolarEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoLolaEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, lolaEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoHeavyMetalEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, heavyMetalEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMetalUndergroundEdicao101 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, metalUndergroundEdicao101,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoJavaMagazineEdicao102 = Fixture.lancamento(
				TipoLancamento.PARCIAL, javaMagazineEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMundoJavaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoJavaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoSqlMagazineEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, sqlMagazineEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoGalileuEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, galileuEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoDuasRodasEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, duasRodasEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		dataRecolhimentoProximaSemana = DateUtil.adicionarDias(dataRecolhimentoProximaSemana, 1);

		Lancamento lancamentoGuitarPlayerEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, guitarPlayerEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRoadieCrewEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, roadieCrewEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRockBrigadeEdicao102 = Fixture.lancamento(
				TipoLancamento.PARCIAL, rockBrigadeEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoValhallaEdicao102 = Fixture.lancamento(
				TipoLancamento.PARCIAL, valhallaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRollingStoneEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, rollingStoneEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoBonsFluidosEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bonsFluidosEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoBravoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, bravoEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoCasaClaudiaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, casaClaudiaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoJequitiEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, jequitiEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMundoEstranhoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, mundoEstranhoEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		dataRecolhimentoProximaSemana = DateUtil.adicionarDias(dataRecolhimentoProximaSemana, 1);

		Lancamento lancamentoNovaEscolaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, novaEscolaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMinhaCasaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, minhaCasaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoRecreioEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, recreioEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoWomenHealthEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, womenHealthEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoViagemTurismoEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, viagemTurismoEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoVipEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, vipEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoGestaoEscolarEdicao102 = Fixture.lancamento(
				TipoLancamento.PARCIAL, gestaoEscolarEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoLolaEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, lolaEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoHeavyMetalEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, heavyMetalEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		Lancamento lancamentoMetalUndergroundEdicao102 = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, metalUndergroundEdicao102,
				dataLancamento,
				dataRecolhimentoProximaSemana,
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, null, 1);

		save(session, lancamentoJavaMagazineEdicao101, lancamentoMundoJavaEdicao101, lancamentoSqlMagazineEdicao101, lancamentoGalileuEdicao101,
					  lancamentoDuasRodasEdicao101, lancamentoGuitarPlayerEdicao101, lancamentoRoadieCrewEdicao101, lancamentoRockBrigadeEdicao101,
					  lancamentoValhallaEdicao101, lancamentoRollingStoneEdicao101, lancamentoBonsFluidosEdicao101, lancamentoBravoEdicao101,
					  lancamentoCasaClaudiaEdicao101, lancamentoJequitiEdicao101, lancamentoMundoEstranhoEdicao101, lancamentoNovaEscolaEdicao101,
					  lancamentoMinhaCasaEdicao101, lancamentoRecreioEdicao101, lancamentoWomenHealthEdicao101, lancamentoViagemTurismoEdicao101,
					  lancamentoVipEdicao101, lancamentoGestaoEscolarEdicao101, lancamentoLolaEdicao101, lancamentoHeavyMetalEdicao101,
					  lancamentoMetalUndergroundEdicao101, lancamentoJavaMagazineEdicao102, lancamentoMundoJavaEdicao102, lancamentoSqlMagazineEdicao102,
					  lancamentoGalileuEdicao102, lancamentoDuasRodasEdicao102, lancamentoGuitarPlayerEdicao102, lancamentoRoadieCrewEdicao102,
					  lancamentoRockBrigadeEdicao102, lancamentoValhallaEdicao102, lancamentoRollingStoneEdicao102, lancamentoBonsFluidosEdicao102,
					  lancamentoBravoEdicao102, lancamentoCasaClaudiaEdicao102, lancamentoJequitiEdicao102, lancamentoMundoEstranhoEdicao102,
					  lancamentoNovaEscolaEdicao102, lancamentoMinhaCasaEdicao102, lancamentoRecreioEdicao102, lancamentoWomenHealthEdicao102,
					  lancamentoViagemTurismoEdicao102, lancamentoVipEdicao102, lancamentoGestaoEscolarEdicao102, lancamentoLolaEdicao102,
					  lancamentoHeavyMetalEdicao102, lancamentoMetalUndergroundEdicao102);

		//ESTOQUE PRODUTO COTA
		EstoqueProdutoCota estoqueProdutoCotaAcmeJavaMagazineEdicao101 =
				Fixture.estoqueProdutoCota(javaMagazineEdicao101, cotaAcme, BigInteger.valueOf(110), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelJavaMagazineEdicao101 =
				Fixture.estoqueProdutoCota(javaMagazineEdicao101, cotaManoel, BigInteger.valueOf(110), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeJavaMagazineEdicao102 =
				Fixture.estoqueProdutoCota(javaMagazineEdicao102, cotaAcme, BigInteger.valueOf(210), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelJavaMagazineEdicao102 =
				Fixture.estoqueProdutoCota(javaMagazineEdicao102, cotaManoel, BigInteger.valueOf(190), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMundoJavaEdicao101 =
				Fixture.estoqueProdutoCota(mundoJavaEdicao101, cotaAcme, BigInteger.valueOf(11), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMundoJavaEdicao101 =
				Fixture.estoqueProdutoCota(mundoJavaEdicao101, cotaManoel, BigInteger.valueOf(710), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMundoJavaEdicao102 =
				Fixture.estoqueProdutoCota(mundoJavaEdicao102, cotaAcme, BigInteger.valueOf(540), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMundoJavaEdicao102 =
				Fixture.estoqueProdutoCota(mundoJavaEdicao102, cotaManoel, BigInteger.valueOf(345), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeSqlMagazineEdicao101 =
				Fixture.estoqueProdutoCota(sqlMagazineEdicao101, cotaAcme, BigInteger.valueOf(810), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelSqlMagazineEdicao101 =
				Fixture.estoqueProdutoCota(sqlMagazineEdicao101, cotaManoel, BigInteger.valueOf(90), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeSqlMagazineEdicao102 =
				Fixture.estoqueProdutoCota(sqlMagazineEdicao102, cotaAcme, BigInteger.valueOf(84), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelSqlMagazineEdicao102 =
				Fixture.estoqueProdutoCota(sqlMagazineEdicao102, cotaManoel, BigInteger.valueOf(97), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGalileuEdicao101 =
				Fixture.estoqueProdutoCota(galileuEdicao101, cotaAcme, BigInteger.valueOf(2054), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGalileuEdicao101 =
				Fixture.estoqueProdutoCota(galileuEdicao101, cotaManoel, BigInteger.valueOf(215), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGalileuEdicao102 =
				Fixture.estoqueProdutoCota(galileuEdicao102, cotaAcme, BigInteger.valueOf(1100), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGalileuEdicao102 =
				Fixture.estoqueProdutoCota(galileuEdicao102, cotaManoel, BigInteger.valueOf(811), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeDuasRodasEdicao101 =
				Fixture.estoqueProdutoCota(duasRodasEdicao101, cotaAcme, BigInteger.valueOf(618), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelDuasRodasEdicao101 =
				Fixture.estoqueProdutoCota(duasRodasEdicao101, cotaManoel, BigInteger.valueOf(310), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeDuasRodasEdicao102 =
				Fixture.estoqueProdutoCota(duasRodasEdicao102, cotaAcme, BigInteger.valueOf(975), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelDuasRodasEdicao102 =
				Fixture.estoqueProdutoCota(duasRodasEdicao102, cotaManoel, BigInteger.valueOf(320), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGuitarPlayerEdicao101 =
				Fixture.estoqueProdutoCota(guitarPlayerEdicao101, cotaAcme, BigInteger.valueOf(610), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGuitarPlayerEdicao101 =
				Fixture.estoqueProdutoCota(guitarPlayerEdicao101, cotaManoel, BigInteger.valueOf(781), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGuitarPlayerEdicao102 =
				Fixture.estoqueProdutoCota(guitarPlayerEdicao102, cotaAcme, BigInteger.valueOf(110), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGuitarPlayerEdicao102 =
				Fixture.estoqueProdutoCota(guitarPlayerEdicao102, cotaManoel, BigInteger.valueOf(110), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRoadieCrewEdicao101 =
				Fixture.estoqueProdutoCota(roadieCrewEdicao101, cotaAcme, BigInteger.valueOf(6452), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRoadieCrewEdicao101 =
				Fixture.estoqueProdutoCota(roadieCrewEdicao101, cotaManoel, BigInteger.valueOf(1234), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRoadieCrewEdicao102 =
				Fixture.estoqueProdutoCota(roadieCrewEdicao102, cotaAcme, BigInteger.valueOf(975), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRoadieCrewEdicao102 =
				Fixture.estoqueProdutoCota(roadieCrewEdicao102, cotaManoel, BigInteger.valueOf(20000), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRockBrigadeEdicao101 =
				Fixture.estoqueProdutoCota(rockBrigadeEdicao101, cotaAcme, BigInteger.valueOf(116), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRockBrigadeEdicao101 =
				Fixture.estoqueProdutoCota(rockBrigadeEdicao101, cotaManoel, BigInteger.valueOf(117), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRockBrigadeEdicao102 =
				Fixture.estoqueProdutoCota(rockBrigadeEdicao102, cotaAcme, BigInteger.valueOf(775), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRockBrigadeEdicao102 =
				Fixture.estoqueProdutoCota(rockBrigadeEdicao102, cotaManoel, BigInteger.valueOf(150), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeValhallaEdicao101 =
				Fixture.estoqueProdutoCota(valhallaEdicao101, cotaAcme, BigInteger.valueOf(982), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelValhallaEdicao101 =
				Fixture.estoqueProdutoCota(valhallaEdicao101, cotaManoel, BigInteger.valueOf(1010), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeValhallaEdicao102 =
				Fixture.estoqueProdutoCota(valhallaEdicao102, cotaAcme, BigInteger.valueOf(315), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelValhallaEdicao102 =
				Fixture.estoqueProdutoCota(valhallaEdicao102, cotaManoel, BigInteger.valueOf(450), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRollingStoneEdicao101 =
				Fixture.estoqueProdutoCota(rollingStoneEdicao101, cotaAcme, BigInteger.valueOf(504), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRollingStoneEdicao101 =
				Fixture.estoqueProdutoCota(rollingStoneEdicao101, cotaManoel, BigInteger.valueOf(548), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRollingStoneEdicao102 =
				Fixture.estoqueProdutoCota(rollingStoneEdicao102, cotaAcme, BigInteger.valueOf(178), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRollingStoneEdicao102 =
				Fixture.estoqueProdutoCota(rollingStoneEdicao102, cotaManoel, BigInteger.valueOf(495), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeBonsFluidosEdicao101 =
				Fixture.estoqueProdutoCota(bonsFluidosEdicao101, cotaAcme, BigInteger.valueOf(654), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelBonsFluidosEdicao101 =
				Fixture.estoqueProdutoCota(bonsFluidosEdicao101, cotaManoel, BigInteger.valueOf(156), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeBonsFluidosEdicao102 =
				Fixture.estoqueProdutoCota(bonsFluidosEdicao102, cotaAcme, BigInteger.valueOf(50000), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelBonsFluidosEdicao102 =
				Fixture.estoqueProdutoCota(bonsFluidosEdicao102, cotaManoel, BigInteger.valueOf(70000), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeBravoEdicao101 =
				Fixture.estoqueProdutoCota(bravoEdicao101, cotaAcme, BigInteger.valueOf(168), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelBravoEdicao101 =
				Fixture.estoqueProdutoCota(bravoEdicao101, cotaManoel, BigInteger.valueOf(157), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeBravoEdicao102 =
				Fixture.estoqueProdutoCota(bravoEdicao102, cotaAcme, BigInteger.valueOf(6458), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelBravoEdicao102 =
				Fixture.estoqueProdutoCota(bravoEdicao102, cotaManoel, BigInteger.valueOf(1085), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeCasaClaudiaEdicao101 =
				Fixture.estoqueProdutoCota(casaClaudiaEdicao101, cotaAcme, BigInteger.valueOf(2103), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelCasaClaudiaEdicao101 =
				Fixture.estoqueProdutoCota(casaClaudiaEdicao101, cotaManoel, BigInteger.valueOf(8075), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeCasaClaudiaEdicao102 =
				Fixture.estoqueProdutoCota(casaClaudiaEdicao102, cotaAcme, BigInteger.valueOf(665), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelCasaClaudiaEdicao102 =
				Fixture.estoqueProdutoCota(casaClaudiaEdicao102, cotaManoel, BigInteger.valueOf(120), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeJequitiEdicao101 =
				Fixture.estoqueProdutoCota(jequitiEdicao101, cotaAcme, BigInteger.valueOf(705), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelJequitiEdicao101 =
				Fixture.estoqueProdutoCota(jequitiEdicao101, cotaManoel, BigInteger.valueOf(804), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeJequitiEdicao102 =
				Fixture.estoqueProdutoCota(jequitiEdicao102, cotaAcme, BigInteger.valueOf(401), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelJequitiEdicao102 =
				Fixture.estoqueProdutoCota(jequitiEdicao102, cotaManoel, BigInteger.valueOf(305), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMundoEstranhoEdicao101 =
				Fixture.estoqueProdutoCota(mundoEstranhoEdicao101, cotaAcme, BigInteger.valueOf(111), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMundoEstranhoEdicao101 =
				Fixture.estoqueProdutoCota(mundoEstranhoEdicao101, cotaManoel, BigInteger.valueOf(11), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMundoEstranhoEdicao102 =
				Fixture.estoqueProdutoCota(mundoEstranhoEdicao102, cotaAcme, BigInteger.valueOf(110), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMundoEstranhoEdicao102 =
				Fixture.estoqueProdutoCota(mundoEstranhoEdicao102, cotaManoel, BigInteger.valueOf(1166), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeNovaEscolaEdicao101 =
				Fixture.estoqueProdutoCota(novaEscolaEdicao101, cotaAcme, BigInteger.valueOf(1082), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelNovaEscolaEdicao101 =
				Fixture.estoqueProdutoCota(novaEscolaEdicao101, cotaManoel, BigInteger.valueOf(1010), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeNovaEscolaEdicao102 =
				Fixture.estoqueProdutoCota(novaEscolaEdicao102, cotaAcme, BigInteger.valueOf(11002), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelNovaEscolaEdicao102 =
				Fixture.estoqueProdutoCota(novaEscolaEdicao102, cotaManoel, BigInteger.valueOf(11048), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMinhaCasaEdicao101 =
				Fixture.estoqueProdutoCota(minhaCasaEdicao101, cotaAcme, BigInteger.valueOf(610), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMinhaCasaEdicao101 =
				Fixture.estoqueProdutoCota(minhaCasaEdicao101, cotaManoel, BigInteger.valueOf(1700), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMinhaCasaEdicao102 =
				Fixture.estoqueProdutoCota(minhaCasaEdicao102, cotaAcme, BigInteger.valueOf(2210), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMinhaCasaEdicao102 =
				Fixture.estoqueProdutoCota(minhaCasaEdicao102, cotaManoel, BigInteger.valueOf(3165), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRecreioEdicao101 =
				Fixture.estoqueProdutoCota(recreioEdicao101, cotaAcme, BigInteger.valueOf(640), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRecreioEdicao101 =
				Fixture.estoqueProdutoCota(recreioEdicao101, cotaManoel, BigInteger.valueOf(758), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeRecreioEdicao102 =
				Fixture.estoqueProdutoCota(recreioEdicao102, cotaAcme, BigInteger.valueOf(316), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelRecreioEdicao102 =
				Fixture.estoqueProdutoCota(recreioEdicao102, cotaManoel, BigInteger.valueOf(450), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeWomenHealthEdicao101 =
				Fixture.estoqueProdutoCota(womenHealthEdicao101, cotaAcme, BigInteger.valueOf(665), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelWomenHealthEdicao101 =
				Fixture.estoqueProdutoCota(womenHealthEdicao101, cotaManoel, BigInteger.valueOf(11585), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeWomenHealthEdicao102 =
				Fixture.estoqueProdutoCota(womenHealthEdicao102, cotaAcme, BigInteger.valueOf(447), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelWomenHealthEdicao102 =
				Fixture.estoqueProdutoCota(womenHealthEdicao102, cotaManoel, BigInteger.valueOf(777), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeViagemTurismoEdicao101 =
				Fixture.estoqueProdutoCota(viagemTurismoEdicao101, cotaAcme, BigInteger.valueOf(6065), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelViagemTurismoEdicao101 =
				Fixture.estoqueProdutoCota(viagemTurismoEdicao101, cotaManoel, BigInteger.valueOf(66666666), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeViagemTurismoEdicao102 =
				Fixture.estoqueProdutoCota(viagemTurismoEdicao102, cotaAcme, BigInteger.valueOf(8742), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelViagemTurismoEdicao102 =
				Fixture.estoqueProdutoCota(viagemTurismoEdicao102, cotaManoel, BigInteger.valueOf(7450), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeVipEdicao101 =
				Fixture.estoqueProdutoCota(vipEdicao101, cotaAcme, BigInteger.valueOf(85200), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelVipEdicao101 =
				Fixture.estoqueProdutoCota(vipEdicao101, cotaManoel, BigInteger.valueOf(4888), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeVipEdicao102 =
				Fixture.estoqueProdutoCota(vipEdicao102, cotaAcme, BigInteger.valueOf(6165), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelVipEdicao102 =
				Fixture.estoqueProdutoCota(vipEdicao102, cotaManoel, BigInteger.valueOf(120), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGestaoEscolarEdicao101 =
				Fixture.estoqueProdutoCota(gestaoEscolarEdicao101, cotaAcme, BigInteger.valueOf(710), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGestaoEscolarEdicao101 =
				Fixture.estoqueProdutoCota(gestaoEscolarEdicao101, cotaManoel, BigInteger.valueOf(140), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeGestaoEscolarEdicao102 =
				Fixture.estoqueProdutoCota(gestaoEscolarEdicao102, cotaAcme, BigInteger.valueOf(5410), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelGestaoEscolarEdicao102 =
				Fixture.estoqueProdutoCota(gestaoEscolarEdicao102, cotaManoel, BigInteger.valueOf(110), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeLolaEdicao101 =
				Fixture.estoqueProdutoCota(lolaEdicao101, cotaAcme, BigInteger.valueOf(902), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelLolaEdicao101 =
				Fixture.estoqueProdutoCota(lolaEdicao101, cotaManoel, BigInteger.valueOf(781), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeLolaEdicao102 =
				Fixture.estoqueProdutoCota(lolaEdicao102, cotaAcme, BigInteger.valueOf(620), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelLolaEdicao102 =
				Fixture.estoqueProdutoCota(lolaEdicao102, cotaManoel, BigInteger.valueOf(1208), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeHeavyMetalEdicao101 =
				Fixture.estoqueProdutoCota(heavyMetalEdicao101, cotaAcme, BigInteger.valueOf(420), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelHeavyMetalEdicao101 =
				Fixture.estoqueProdutoCota(heavyMetalEdicao101, cotaJoana, BigInteger.valueOf(487), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeHeavyMetalEdicao102 =
				Fixture.estoqueProdutoCota(heavyMetalEdicao102, cotaAcme, BigInteger.valueOf(133), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelHeavyMetalEdicao102 =
				Fixture.estoqueProdutoCota(heavyMetalEdicao102, cotaManoel, BigInteger.valueOf(321), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMetalUndergroundEdicao101 =
				Fixture.estoqueProdutoCota(metalUndergroundEdicao101, cotaAcme, BigInteger.valueOf(952), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMetalUndergroundEdicao101 =
				Fixture.estoqueProdutoCota(metalUndergroundEdicao101, cotaManoel, BigInteger.valueOf(888), BigInteger.ZERO);

		EstoqueProdutoCota estoqueProdutoCotaAcmeMetalUndergroundEdicao102 =
				Fixture.estoqueProdutoCota(metalUndergroundEdicao102, cotaJoana, BigInteger.valueOf(999), BigInteger.ZERO);
		EstoqueProdutoCota estoqueProdutoCotaManoelMetalUndergroundEdicao102 =
				Fixture.estoqueProdutoCota(metalUndergroundEdicao102, cotaManoel, BigInteger.valueOf(8720), BigInteger.ZERO);

		save(session, estoqueProdutoCotaAcmeJavaMagazineEdicao101, estoqueProdutoCotaManoelJavaMagazineEdicao101,
					  estoqueProdutoCotaAcmeJavaMagazineEdicao102, estoqueProdutoCotaManoelJavaMagazineEdicao102,
					  estoqueProdutoCotaAcmeMundoJavaEdicao101, estoqueProdutoCotaManoelMundoJavaEdicao101,
					  estoqueProdutoCotaAcmeMundoJavaEdicao102, estoqueProdutoCotaManoelMundoJavaEdicao102,
					  estoqueProdutoCotaAcmeSqlMagazineEdicao101, estoqueProdutoCotaManoelSqlMagazineEdicao101,
					  estoqueProdutoCotaAcmeSqlMagazineEdicao102, estoqueProdutoCotaManoelSqlMagazineEdicao102,
					  estoqueProdutoCotaAcmeGalileuEdicao101, estoqueProdutoCotaManoelGalileuEdicao101,
					  estoqueProdutoCotaAcmeGalileuEdicao102, estoqueProdutoCotaManoelGalileuEdicao102,
					  estoqueProdutoCotaAcmeDuasRodasEdicao101, estoqueProdutoCotaManoelDuasRodasEdicao101,
					  estoqueProdutoCotaAcmeDuasRodasEdicao102, estoqueProdutoCotaManoelDuasRodasEdicao102,
					  estoqueProdutoCotaAcmeGuitarPlayerEdicao101, estoqueProdutoCotaManoelGuitarPlayerEdicao101,
					  estoqueProdutoCotaAcmeGuitarPlayerEdicao102, estoqueProdutoCotaManoelGuitarPlayerEdicao102,
					  estoqueProdutoCotaAcmeRoadieCrewEdicao101, estoqueProdutoCotaManoelRoadieCrewEdicao101,
					  estoqueProdutoCotaAcmeRoadieCrewEdicao102, estoqueProdutoCotaManoelRoadieCrewEdicao102,
					  estoqueProdutoCotaAcmeRockBrigadeEdicao101, estoqueProdutoCotaManoelRockBrigadeEdicao101,
					  estoqueProdutoCotaAcmeRockBrigadeEdicao102, estoqueProdutoCotaManoelRockBrigadeEdicao102,
					  estoqueProdutoCotaAcmeValhallaEdicao101, estoqueProdutoCotaManoelValhallaEdicao101,
					  estoqueProdutoCotaAcmeValhallaEdicao102, estoqueProdutoCotaManoelValhallaEdicao102,
					  estoqueProdutoCotaAcmeRollingStoneEdicao101, estoqueProdutoCotaManoelRollingStoneEdicao101,
					  estoqueProdutoCotaAcmeRollingStoneEdicao102, estoqueProdutoCotaManoelRollingStoneEdicao102,
					  estoqueProdutoCotaAcmeBonsFluidosEdicao101, estoqueProdutoCotaManoelBonsFluidosEdicao101,
					  estoqueProdutoCotaAcmeBonsFluidosEdicao102, estoqueProdutoCotaManoelBonsFluidosEdicao102,
					  estoqueProdutoCotaAcmeBravoEdicao101, estoqueProdutoCotaManoelBravoEdicao101,
					  estoqueProdutoCotaAcmeBravoEdicao102, estoqueProdutoCotaManoelBravoEdicao102,
					  estoqueProdutoCotaAcmeCasaClaudiaEdicao101, estoqueProdutoCotaManoelCasaClaudiaEdicao101,
					  estoqueProdutoCotaAcmeCasaClaudiaEdicao102, estoqueProdutoCotaManoelCasaClaudiaEdicao102,
					  estoqueProdutoCotaAcmeJequitiEdicao101, estoqueProdutoCotaManoelJequitiEdicao101,
					  estoqueProdutoCotaAcmeJequitiEdicao102, estoqueProdutoCotaManoelJequitiEdicao102,
					  estoqueProdutoCotaAcmeMundoEstranhoEdicao101, estoqueProdutoCotaManoelMundoEstranhoEdicao101,
					  estoqueProdutoCotaAcmeMundoEstranhoEdicao102, estoqueProdutoCotaManoelMundoEstranhoEdicao102,
					  estoqueProdutoCotaAcmeNovaEscolaEdicao101, estoqueProdutoCotaManoelNovaEscolaEdicao101,
					  estoqueProdutoCotaAcmeNovaEscolaEdicao102, estoqueProdutoCotaManoelNovaEscolaEdicao102,
					  estoqueProdutoCotaAcmeMinhaCasaEdicao101, estoqueProdutoCotaManoelMinhaCasaEdicao101,
					  estoqueProdutoCotaAcmeMinhaCasaEdicao102, estoqueProdutoCotaManoelMinhaCasaEdicao102,
					  estoqueProdutoCotaAcmeRecreioEdicao101, estoqueProdutoCotaManoelRecreioEdicao101,
					  estoqueProdutoCotaAcmeRecreioEdicao102, estoqueProdutoCotaManoelRecreioEdicao102,
					  estoqueProdutoCotaAcmeWomenHealthEdicao101, estoqueProdutoCotaManoelWomenHealthEdicao101,
					  estoqueProdutoCotaAcmeWomenHealthEdicao102, estoqueProdutoCotaManoelWomenHealthEdicao102,
					  estoqueProdutoCotaAcmeViagemTurismoEdicao101, estoqueProdutoCotaManoelViagemTurismoEdicao101,
					  estoqueProdutoCotaAcmeViagemTurismoEdicao102, estoqueProdutoCotaManoelViagemTurismoEdicao102,
					  estoqueProdutoCotaAcmeVipEdicao101, estoqueProdutoCotaManoelVipEdicao101,
					  estoqueProdutoCotaAcmeVipEdicao102, estoqueProdutoCotaManoelVipEdicao102,
					  estoqueProdutoCotaAcmeGestaoEscolarEdicao101, estoqueProdutoCotaManoelGestaoEscolarEdicao101,
					  estoqueProdutoCotaAcmeGestaoEscolarEdicao102, estoqueProdutoCotaManoelGestaoEscolarEdicao102,
					  estoqueProdutoCotaAcmeLolaEdicao101, estoqueProdutoCotaManoelLolaEdicao101,
					  estoqueProdutoCotaAcmeLolaEdicao102, estoqueProdutoCotaManoelLolaEdicao102,
					  estoqueProdutoCotaAcmeHeavyMetalEdicao101, estoqueProdutoCotaManoelHeavyMetalEdicao101,
					  estoqueProdutoCotaAcmeHeavyMetalEdicao102, estoqueProdutoCotaManoelHeavyMetalEdicao102,
					  estoqueProdutoCotaAcmeMetalUndergroundEdicao101, estoqueProdutoCotaManoelMetalUndergroundEdicao101,
					  estoqueProdutoCotaAcmeMetalUndergroundEdicao102, estoqueProdutoCotaManoelMetalUndergroundEdicao102);

		//LANCAMENTO PARCIAL
		LancamentoParcial lancamentoParcialJavaMagazineEdicao101 =
				Fixture.criarLancamentoParcial(javaMagazineEdicao101,
						lancamentoJavaMagazineEdicao101.getDataLancamentoPrevista(),
					    DateUtil.adicionarDias(lancamentoJavaMagazineEdicao101.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialJavaMagazineEdicao102 =
				Fixture.criarLancamentoParcial(javaMagazineEdicao102,
						lancamentoJavaMagazineEdicao102.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoJavaMagazineEdicao102.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialGalileuEdicao101 =
				Fixture.criarLancamentoParcial(galileuEdicao101,
						lancamentoGalileuEdicao101.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoGalileuEdicao101.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialRockBrigadeEdicao102 =
				Fixture.criarLancamentoParcial(rockBrigadeEdicao102,
						lancamentoRockBrigadeEdicao102.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoRockBrigadeEdicao102.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialValhallaEdicao101 =
				Fixture.criarLancamentoParcial(valhallaEdicao101,
						lancamentoValhallaEdicao101.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoValhallaEdicao101.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialValhallaEdicao102 =
				Fixture.criarLancamentoParcial(valhallaEdicao102,
						lancamentoValhallaEdicao102.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoValhallaEdicao102.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		LancamentoParcial lancamentoParcialGestaoEscolarEdicao102 =
				Fixture.criarLancamentoParcial(gestaoEscolarEdicao102,
						lancamentoGestaoEscolarEdicao102.getDataLancamentoPrevista(),
						DateUtil.adicionarDias(lancamentoGestaoEscolarEdicao102.getDataRecolhimentoPrevista(), 10),
					    StatusLancamentoParcial.PROJETADO);

		save(session, lancamentoParcialJavaMagazineEdicao101, lancamentoParcialJavaMagazineEdicao102,
				  lancamentoParcialGalileuEdicao101, lancamentoParcialRockBrigadeEdicao102,
				  lancamentoParcialValhallaEdicao101, lancamentoParcialValhallaEdicao102,
				  lancamentoParcialGestaoEscolarEdicao102);

		//PERIODO LANCAMENTO PARCIAL
		PeriodoLancamentoParcial periodoLancamentoParcialJavaMagazineEdicao101 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoJavaMagazineEdicao101,
						lancamentoParcialJavaMagazineEdicao101,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialGalileuEdicao101 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoGalileuEdicao101,
						lancamentoParcialGalileuEdicao101,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);

		PeriodoLancamentoParcial periodoLancamentoParcialValhallaEdicao101 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoValhallaEdicao101,
						lancamentoParcialValhallaEdicao101,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialJavaMagazineEdicao102 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoJavaMagazineEdicao102,
						lancamentoParcialJavaMagazineEdicao102,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialRockBrigadeEdicao102 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoRockBrigadeEdicao102,
						lancamentoParcialRockBrigadeEdicao102,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialValhallaEdicao102 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoValhallaEdicao102,
						lancamentoParcialValhallaEdicao102,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial periodoLancamentoParcialGestaoEscolarEdicao102 =
				Fixture.criarPeriodoLancamentoParcial(
						lancamentoGestaoEscolarEdicao102,
						lancamentoParcialGestaoEscolarEdicao102,
						StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.PARCIAL);

		save(session, periodoLancamentoParcialJavaMagazineEdicao101, periodoLancamentoParcialGalileuEdicao101,
					  periodoLancamentoParcialValhallaEdicao101, periodoLancamentoParcialJavaMagazineEdicao102,
					  periodoLancamentoParcialRockBrigadeEdicao102, periodoLancamentoParcialValhallaEdicao102,
					  periodoLancamentoParcialGestaoEscolarEdicao102);

		//ESTUDOS
		Estudo estudoJavaMagazineEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJavaMagazineEdicao101.getDataLancamentoDistribuidor(), javaMagazineEdicao101);

		Estudo estudoMundoJavaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMundoJavaEdicao101.getDataLancamentoDistribuidor(), mundoJavaEdicao101);

		Estudo estudoSqlMagazineEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoSqlMagazineEdicao101.getDataLancamentoDistribuidor(), sqlMagazineEdicao101);

		Estudo estudoGalileuEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGalileuEdicao101.getDataLancamentoDistribuidor(), galileuEdicao101);

		Estudo estudoDuasRodasEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoDuasRodasEdicao101.getDataLancamentoDistribuidor(), duasRodasEdicao101);

		Estudo estudoGuitarPlayerEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGuitarPlayerEdicao101.getDataLancamentoDistribuidor(), guitarPlayerEdicao101);

		Estudo estudoRoadieCrewEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRoadieCrewEdicao101.getDataLancamentoDistribuidor(), roadieCrewEdicao101);

		Estudo estudoRockBrigadeEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRockBrigadeEdicao101.getDataLancamentoDistribuidor(), rockBrigadeEdicao101);

		Estudo estudoValhallaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoValhallaEdicao101.getDataLancamentoDistribuidor(), valhallaEdicao101);

		Estudo estudoRollingStoneEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRollingStoneEdicao101.getDataLancamentoDistribuidor(), rollingStoneEdicao101);

		Estudo estudoBonsFluidosEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoBonsFluidosEdicao101.getDataLancamentoDistribuidor(), bonsFluidosEdicao101);

		Estudo estudoBravoEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoBravoEdicao101.getDataLancamentoDistribuidor(), bravoEdicao101);

		Estudo estudoCasaClaudiaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoCasaClaudiaEdicao101.getDataLancamentoDistribuidor(), casaClaudiaEdicao101);

		Estudo estudoJequitiEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJequitiEdicao101.getDataLancamentoDistribuidor(), jequitiEdicao101);

		Estudo estudoMundoEstranhoEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMundoEstranhoEdicao101.getDataLancamentoDistribuidor(), mundoEstranhoEdicao101);

		Estudo estudoNovaEscolaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoNovaEscolaEdicao101.getDataLancamentoDistribuidor(), novaEscolaEdicao101);

		Estudo estudoMinhaCasaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMinhaCasaEdicao101.getDataLancamentoDistribuidor(), minhaCasaEdicao101);

		Estudo estudoRecreioEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRecreioEdicao101.getDataLancamentoDistribuidor(), recreioEdicao101);

		Estudo estudoWomenHealthEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoWomenHealthEdicao101.getDataLancamentoDistribuidor(), womenHealthEdicao101);

		Estudo estudoViagemTurismoEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoViagemTurismoEdicao101.getDataLancamentoDistribuidor(), viagemTurismoEdicao101);

		Estudo estudoVipEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoVipEdicao101.getDataLancamentoDistribuidor(), vipEdicao101);

		Estudo estudoGestaoEscolarEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGestaoEscolarEdicao101.getDataLancamentoDistribuidor(), gestaoEscolarEdicao101);

		Estudo estudoLolaEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoLolaEdicao101.getDataLancamentoDistribuidor(), lolaEdicao101);

		Estudo estudoHeavyMetalEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoHeavyMetalEdicao101.getDataLancamentoDistribuidor(), heavyMetalEdicao101);

		Estudo estudoMetalUndergroundEdicao101 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMetalUndergroundEdicao101.getDataLancamentoDistribuidor(), metalUndergroundEdicao101);

		Estudo estudoJavaMagazineEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJavaMagazineEdicao102.getDataLancamentoDistribuidor(), javaMagazineEdicao102);

		Estudo estudoMundoJavaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMundoJavaEdicao102.getDataLancamentoDistribuidor(), mundoJavaEdicao102);

		Estudo estudoSqlMagazineEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoSqlMagazineEdicao102.getDataLancamentoDistribuidor(), sqlMagazineEdicao102);

		Estudo estudoGalileuEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGalileuEdicao102.getDataLancamentoDistribuidor(), galileuEdicao102);

		Estudo estudoDuasRodasEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoDuasRodasEdicao102.getDataLancamentoDistribuidor(), duasRodasEdicao102);

		Estudo estudoGuitarPlayerEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGuitarPlayerEdicao102.getDataLancamentoDistribuidor(), guitarPlayerEdicao102);

		Estudo estudoRoadieCrewEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRoadieCrewEdicao102.getDataLancamentoDistribuidor(), roadieCrewEdicao102);

		Estudo estudoRockBrigadeEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRockBrigadeEdicao102.getDataLancamentoDistribuidor(), rockBrigadeEdicao102);

		Estudo estudoValhallaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoValhallaEdicao102.getDataLancamentoDistribuidor(), valhallaEdicao102);

		Estudo estudoRollingStoneEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRollingStoneEdicao102.getDataLancamentoDistribuidor(), rollingStoneEdicao102);

		Estudo estudoBonsFluidosEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoBonsFluidosEdicao102.getDataLancamentoDistribuidor(), bonsFluidosEdicao102);

		Estudo estudoBravoEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoBravoEdicao102.getDataLancamentoDistribuidor(), bravoEdicao102);

		Estudo estudoCasaClaudiaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoCasaClaudiaEdicao102.getDataLancamentoDistribuidor(), casaClaudiaEdicao102);

		Estudo estudoJequitiEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoJequitiEdicao102.getDataLancamentoDistribuidor(), jequitiEdicao102);

		Estudo estudoMundoEstranhoEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMundoEstranhoEdicao102.getDataLancamentoDistribuidor(), mundoEstranhoEdicao102);

		Estudo estudoNovaEscolaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoNovaEscolaEdicao102.getDataLancamentoDistribuidor(), novaEscolaEdicao102);

		Estudo estudoMinhaCasaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMinhaCasaEdicao102.getDataLancamentoDistribuidor(), minhaCasaEdicao102);

		Estudo estudoRecreioEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoRecreioEdicao102.getDataLancamentoDistribuidor(), recreioEdicao102);

		Estudo estudoWomenHealthEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoWomenHealthEdicao102.getDataLancamentoDistribuidor(), womenHealthEdicao102);

		Estudo estudoViagemTurismoEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoViagemTurismoEdicao102.getDataLancamentoDistribuidor(), viagemTurismoEdicao102);

		Estudo estudoVipEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoVipEdicao102.getDataLancamentoDistribuidor(), vipEdicao102);

		Estudo estudoGestaoEscolarEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoGestaoEscolarEdicao102.getDataLancamentoDistribuidor(), gestaoEscolarEdicao102);

		Estudo estudoLolaEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoLolaEdicao102.getDataLancamentoDistribuidor(), lolaEdicao102);

		Estudo estudoHeavyMetalEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoHeavyMetalEdicao102.getDataLancamentoDistribuidor(), heavyMetalEdicao102);

		Estudo estudoMetalUndergroundEdicao102 =
				Fixture.estudo(BigInteger.valueOf(180),
						lancamentoMetalUndergroundEdicao102.getDataLancamentoDistribuidor(), metalUndergroundEdicao102);

		save(session, estudoJavaMagazineEdicao101, estudoMundoJavaEdicao101,
					  estudoSqlMagazineEdicao101, estudoGalileuEdicao101,
					  estudoDuasRodasEdicao101, estudoGuitarPlayerEdicao101,
					  estudoRoadieCrewEdicao101, estudoRockBrigadeEdicao101,
					  estudoValhallaEdicao101, estudoRollingStoneEdicao101,
					  estudoBonsFluidosEdicao101, estudoBravoEdicao101,
					  estudoCasaClaudiaEdicao101, estudoJequitiEdicao101,
					  estudoMundoEstranhoEdicao101, estudoNovaEscolaEdicao101,
					  estudoMinhaCasaEdicao101, estudoRecreioEdicao101,
					  estudoWomenHealthEdicao101, estudoViagemTurismoEdicao101,
					  estudoVipEdicao101, estudoGestaoEscolarEdicao101,
					  estudoLolaEdicao101, estudoHeavyMetalEdicao101,
					  estudoMetalUndergroundEdicao101, estudoJavaMagazineEdicao102,
					  estudoMundoJavaEdicao102, estudoSqlMagazineEdicao102,
					  estudoGalileuEdicao102, estudoDuasRodasEdicao102,
					  estudoGuitarPlayerEdicao102, estudoRoadieCrewEdicao102,
					  estudoRockBrigadeEdicao102, estudoValhallaEdicao102,
					  estudoRollingStoneEdicao102, estudoBonsFluidosEdicao102,
					  estudoBravoEdicao102, estudoCasaClaudiaEdicao102,
					  estudoJequitiEdicao102, estudoMundoEstranhoEdicao102,
					  estudoNovaEscolaEdicao102, estudoMinhaCasaEdicao102,
					  estudoRecreioEdicao102, estudoWomenHealthEdicao102,
					  estudoViagemTurismoEdicao102, estudoVipEdicao102,
					  estudoGestaoEscolarEdicao102, estudoLolaEdicao102,
					  estudoHeavyMetalEdicao102, estudoMetalUndergroundEdicao102);

		//ESTUDOS COTA
		EstudoCota estudoCotaAcmeJavaMagazineEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMundoJavaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeSqlMagazineEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeGalileuEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeDuasRodasEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeGuitarPlayerEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGuitarPlayerEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeRoadieCrewEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRoadieCrewEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeRockBrigadeEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRockBrigadeEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeValhallaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoValhallaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeRollingStoneEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRollingStoneEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeBonsFluidosEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeBravoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeCasaClaudiaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeJequitiEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMundoEstranhoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeNovaEscolaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoNovaEscolaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMinhaCasaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMinhaCasaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeRecreioEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRecreioEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeWomenHealthEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoWomenHealthEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeViagemTurismoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoViagemTurismoEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeVipEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeGestaoEscolarEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeLolaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeHeavyMetalEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeMetalUndergroundEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao101, cotaAcme);

		EstudoCota estudoCotaAcmeJavaMagazineEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMundoJavaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeSqlMagazineEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeGalileuEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeDuasRodasEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeGuitarPlayerEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGuitarPlayerEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeRoadieCrewEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRoadieCrewEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeRockBrigadeEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRockBrigadeEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeValhallaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoValhallaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeRollingStoneEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRollingStoneEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeBonsFluidosEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeBravoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeCasaClaudiaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeJequitiEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMundoEstranhoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeNovaEscolaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoNovaEscolaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMinhaCasaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMinhaCasaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeRecreioEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRecreioEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeWomenHealthEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoWomenHealthEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeViagemTurismoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoViagemTurismoEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeVipEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeGestaoEscolarEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeLolaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeHeavyMetalEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao102, cotaAcme);

		EstudoCota estudoCotaAcmeMetalUndergroundEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao102, cotaAcme);

		EstudoCota estudoCotaManoelJavaMagazineEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelMundoJavaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelSqlMagazineEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelGalileuEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelDuasRodasEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelGuitarPlayerEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGuitarPlayerEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelRoadieCrewEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRoadieCrewEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelRockBrigadeEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRockBrigadeEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelValhallaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoValhallaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelRollingStoneEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRollingStoneEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelBonsFluidosEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelBravoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelCasaClaudiaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelJequitiEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelMundoEstranhoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelNovaEscolaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoNovaEscolaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelMinhaCasaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMinhaCasaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelRecreioEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRecreioEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelWomenHealthEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoWomenHealthEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelViagemTurismoEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoViagemTurismoEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelVipEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelGestaoEscolarEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelLolaEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelHeavyMetalEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao101, cotaJoana);

		EstudoCota estudoCotaManoelMetalUndergroundEdicao101 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao101, cotaManoel);

		EstudoCota estudoCotaManoelJavaMagazineEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJavaMagazineEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMundoJavaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoJavaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelSqlMagazineEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoSqlMagazineEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelGalileuEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGalileuEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelDuasRodasEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoDuasRodasEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelGuitarPlayerEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGuitarPlayerEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelRoadieCrewEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRoadieCrewEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelRockBrigadeEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRockBrigadeEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelValhallaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoValhallaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelRollingStoneEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRollingStoneEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelBonsFluidosEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBonsFluidosEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelBravoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoBravoEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelCasaClaudiaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCasaClaudiaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelJequitiEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoJequitiEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMundoEstranhoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMundoEstranhoEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelNovaEscolaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoNovaEscolaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMinhaCasaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMinhaCasaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelRecreioEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoRecreioEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelWomenHealthEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoWomenHealthEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelViagemTurismoEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoViagemTurismoEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelVipEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVipEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelGestaoEscolarEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoGestaoEscolarEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelLolaEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoLolaEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelHeavyMetalEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoHeavyMetalEdicao102, cotaManoel);

		EstudoCota estudoCotaManoelMetalUndergroundEdicao102 =
				Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoMetalUndergroundEdicao102, cotaJoana);

		save(session, estudoCotaAcmeJavaMagazineEdicao101, estudoCotaAcmeMundoJavaEdicao101,
					  estudoCotaAcmeSqlMagazineEdicao101, estudoCotaAcmeGalileuEdicao101,
					  estudoCotaAcmeDuasRodasEdicao101, estudoCotaAcmeGuitarPlayerEdicao101,
					  estudoCotaAcmeRoadieCrewEdicao101, estudoCotaAcmeRockBrigadeEdicao101,
					  estudoCotaAcmeValhallaEdicao101, estudoCotaAcmeRollingStoneEdicao101,
					  estudoCotaAcmeBonsFluidosEdicao101, estudoCotaAcmeBravoEdicao101,
					  estudoCotaAcmeCasaClaudiaEdicao101,  estudoCotaAcmeJequitiEdicao101,
					  estudoCotaAcmeMundoEstranhoEdicao101, estudoCotaAcmeNovaEscolaEdicao101,
					  estudoCotaAcmeMinhaCasaEdicao101, estudoCotaAcmeRecreioEdicao101,
					  estudoCotaAcmeWomenHealthEdicao101, estudoCotaAcmeViagemTurismoEdicao101,
					  estudoCotaAcmeVipEdicao101, estudoCotaAcmeGestaoEscolarEdicao101,
					  estudoCotaAcmeLolaEdicao101, estudoCotaAcmeHeavyMetalEdicao101,
					  estudoCotaAcmeMetalUndergroundEdicao101, estudoCotaAcmeJavaMagazineEdicao102,
					  estudoCotaAcmeMundoJavaEdicao102, estudoCotaAcmeSqlMagazineEdicao102,
					  estudoCotaAcmeGalileuEdicao102, estudoCotaAcmeDuasRodasEdicao102,
					  estudoCotaAcmeGuitarPlayerEdicao102, estudoCotaAcmeRoadieCrewEdicao102,
					  estudoCotaAcmeRockBrigadeEdicao102, estudoCotaAcmeValhallaEdicao102,
					  estudoCotaAcmeRollingStoneEdicao102, estudoCotaAcmeBonsFluidosEdicao102,
					  estudoCotaAcmeBravoEdicao102, estudoCotaAcmeCasaClaudiaEdicao102,
					  estudoCotaAcmeJequitiEdicao102, estudoCotaAcmeMundoEstranhoEdicao102,
					  estudoCotaAcmeNovaEscolaEdicao102, estudoCotaAcmeMinhaCasaEdicao102,
					  estudoCotaAcmeRecreioEdicao102, estudoCotaAcmeWomenHealthEdicao102,
					  estudoCotaAcmeViagemTurismoEdicao102, estudoCotaAcmeVipEdicao102,
					  estudoCotaAcmeGestaoEscolarEdicao102, estudoCotaAcmeLolaEdicao102,
					  estudoCotaAcmeHeavyMetalEdicao102, estudoCotaAcmeMetalUndergroundEdicao102,
					  estudoCotaManoelJavaMagazineEdicao101, estudoCotaManoelMundoJavaEdicao101,
					  estudoCotaManoelSqlMagazineEdicao101, estudoCotaManoelGalileuEdicao101,
					  estudoCotaManoelDuasRodasEdicao101, estudoCotaManoelGuitarPlayerEdicao101,
					  estudoCotaManoelRoadieCrewEdicao101, estudoCotaManoelRockBrigadeEdicao101,
					  estudoCotaManoelValhallaEdicao101, estudoCotaManoelRollingStoneEdicao101,
					  estudoCotaManoelBonsFluidosEdicao101, estudoCotaManoelBravoEdicao101,
					  estudoCotaManoelCasaClaudiaEdicao101, estudoCotaManoelJequitiEdicao101,
					  estudoCotaManoelMundoEstranhoEdicao101, estudoCotaManoelNovaEscolaEdicao101,
					  estudoCotaManoelMinhaCasaEdicao101, estudoCotaManoelRecreioEdicao101,
					  estudoCotaManoelWomenHealthEdicao101, estudoCotaManoelViagemTurismoEdicao101,
					  estudoCotaManoelVipEdicao101, estudoCotaManoelGestaoEscolarEdicao101,
					  estudoCotaManoelLolaEdicao101, estudoCotaManoelHeavyMetalEdicao101,
					  estudoCotaManoelMetalUndergroundEdicao101, estudoCotaManoelJavaMagazineEdicao102,
					  estudoCotaManoelMundoJavaEdicao102, estudoCotaManoelSqlMagazineEdicao102,
					  estudoCotaManoelGalileuEdicao102, estudoCotaManoelDuasRodasEdicao102,
					  estudoCotaManoelGuitarPlayerEdicao102, estudoCotaManoelRoadieCrewEdicao102,
					  estudoCotaManoelRockBrigadeEdicao102, estudoCotaManoelValhallaEdicao102,
					  estudoCotaManoelRollingStoneEdicao102, estudoCotaManoelBonsFluidosEdicao102,
					  estudoCotaManoelBravoEdicao102, estudoCotaManoelCasaClaudiaEdicao102,
					  estudoCotaManoelJequitiEdicao102, estudoCotaManoelMundoEstranhoEdicao102,
					  estudoCotaManoelNovaEscolaEdicao102, estudoCotaManoelMinhaCasaEdicao102,
					  estudoCotaManoelRecreioEdicao102, estudoCotaManoelWomenHealthEdicao102,
					  estudoCotaManoelViagemTurismoEdicao102, estudoCotaManoelVipEdicao102,
					  estudoCotaManoelGestaoEscolarEdicao102, estudoCotaManoelLolaEdicao102,
					  estudoCotaManoelHeavyMetalEdicao102, estudoCotaManoelMetalUndergroundEdicao102);

		ChamadaEncalhe chamadaEncalheMundoJava101 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
								   mundoJavaEdicao101,
								   TipoChamadaEncalhe.CHAMADAO);

		ChamadaEncalhe chamadaEncalheGuitarPlayer101 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
								   guitarPlayerEdicao101,
								   TipoChamadaEncalhe.ANTECIPADA);
		chamadaEncalheGuitarPlayer101.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalheGuitarPlayer101.getLancamentos().add(lancamentoGuitarPlayerEdicao101);

		ChamadaEncalhe chamadaEncalheJequiti101 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
								   jequitiEdicao101,
								   TipoChamadaEncalhe.CHAMADAO);

		ChamadaEncalhe chamadaEncalheJavaMagazine102 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
								   javaMagazineEdicao102,
								   TipoChamadaEncalhe.ANTECIPADA);
		chamadaEncalheJavaMagazine102.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalheJavaMagazine102.getLancamentos().add(lancamentoJavaMagazineEdicao102);

		ChamadaEncalhe chamadaEncalheBravo102 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
					 			   bravoEdicao102,
								   TipoChamadaEncalhe.CHAMADAO);
		chamadaEncalheBravo102.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalheBravo102.getLancamentos().add(lancamentoBravoEdicao102);
		
		ChamadaEncalhe chamadaEncalheWomenHealth102 =
			Fixture.chamadaEncalhe(dataRecolhimentoProximaSemana,
								   womenHealthEdicao102,
								   TipoChamadaEncalhe.ANTECIPADA);
		chamadaEncalheWomenHealth102.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalheWomenHealth102.getLancamentos().add(lancamentoWomenHealthEdicao102);

		save(session, chamadaEncalheMundoJava101, chamadaEncalheGuitarPlayer101,
					  chamadaEncalheJequiti101, chamadaEncalheJavaMagazine102,
					  chamadaEncalheBravo102, chamadaEncalheWomenHealth102);

		ChamadaEncalheCota chamadaEncalheCotaAcmeMundoJava101 =
			Fixture.chamadaEncalheCota(chamadaEncalheMundoJava101, false, cotaAcme, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaManoelGuitarPlayer101 =
			Fixture.chamadaEncalheCota(chamadaEncalheGuitarPlayer101, false, cotaManoel, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaAcmeJequiti101 =
			Fixture.chamadaEncalheCota(chamadaEncalheJequiti101, false, cotaAcme, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaManoelJavaMagazine102 =
			Fixture.chamadaEncalheCota(chamadaEncalheJavaMagazine102, false, cotaManoel, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaManoelBravo102 =
			Fixture.chamadaEncalheCota(chamadaEncalheBravo102, false, cotaManoel, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaAcmeBravo102 =
				Fixture.chamadaEncalheCota(chamadaEncalheBravo102, false, cotaAcme, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaManoelWomenHealth102 =
			Fixture.chamadaEncalheCota(chamadaEncalheWomenHealth102, false, cotaManoel, BigInteger.TEN);

		ChamadaEncalheCota chamadaEncalheCotaAcmeWomenHealth102 =
			Fixture.chamadaEncalheCota(chamadaEncalheWomenHealth102, false, cotaAcme, BigInteger.TEN);

		save(session, chamadaEncalheCotaAcmeMundoJava101, chamadaEncalheCotaManoelGuitarPlayer101,
					  chamadaEncalheCotaAcmeJequiti101, chamadaEncalheCotaManoelJavaMagazine102,
					  chamadaEncalheCotaManoelBravo102, chamadaEncalheCotaAcmeBravo102,
					  chamadaEncalheCotaManoelWomenHealth102, chamadaEncalheCotaAcmeWomenHealth102);
		
		save(session, lancamentoGuitarPlayerEdicao101, lancamentoBravoEdicao102, lancamentoWomenHealthEdicao102);
	}


	private static void criarInterfaceExecucao(Session session) {

		interfaceEMS0106 = Fixture.criarInterfaceExecucao(106L, "EMS0106");
		interfaceEMS0107 = Fixture.criarInterfaceExecucao(107L, "EMS0107");
		interfaceEMS0108 = Fixture.criarInterfaceExecucao(108L, "EMS0108");
		interfaceEMS0109 = Fixture.criarInterfaceExecucao(109L, "EMS0109");
		interfaceEMS0110 = Fixture.criarInterfaceExecucao(110L, "EMS0110");
		interfaceEMS0111 = Fixture.criarInterfaceExecucao(111L, "EMS0111");
		interfaceEMS0112 = Fixture.criarInterfaceExecucao(112L, "EMS0112");
		interfaceEMS0113 = Fixture.criarInterfaceExecucao(113L, "EMS0113");
		interfaceEMS0114 = Fixture.criarInterfaceExecucao(114L, "EMS0114");
		interfaceEMS0116 = Fixture.criarInterfaceExecucao(116L, "EMS0116");
		interfaceEMS0117 = Fixture.criarInterfaceExecucao(117L, "EMS0117");
		interfaceEMS0118 = Fixture.criarInterfaceExecucao(118L, "EMS0118");
		interfaceEMS0119 = Fixture.criarInterfaceExecucao(119L, "EMS0119");
		interfaceEMS0120 = Fixture.criarInterfaceExecucao(120L, "EMS0120");
		interfaceEMS0121 = Fixture.criarInterfaceExecucao(121L, "EMS0121");
		interfaceEMS0122 = Fixture.criarInterfaceExecucao(122L, "EMS0122");
		interfaceEMS0123 = Fixture.criarInterfaceExecucao(123L, "EMS0123");
		interfaceEMS0124 = Fixture.criarInterfaceExecucao(124L, "EMS0124");
		interfaceEMS0125 = Fixture.criarInterfaceExecucao(125L, "EMS0125");
		interfaceEMS0126 = Fixture.criarInterfaceExecucao(126L, "EMS0126");
		interfaceEMS0129 = Fixture.criarInterfaceExecucao(129L, "EMS0129");
		interfaceEMS0130 = Fixture.criarInterfaceExecucao(130L, "EMS0130");
		interfaceEMS0131 = Fixture.criarInterfaceExecucao(131L, "EMS0131");
		interfaceEMS0132 = Fixture.criarInterfaceExecucao(132L, "EMS0132");
		interfaceEMS0133 = Fixture.criarInterfaceExecucao(133L, "EMS0133");
		interfaceEMS0135 = Fixture.criarInterfaceExecucao(135L, "EMS0135");
		interfaceEMS0185 = Fixture.criarInterfaceExecucao(185L, "EMS0185");
		interfaceEMS0197 = Fixture.criarInterfaceExecucao(197L, "EMS0197");
		interfaceEMS0198 = Fixture.criarInterfaceExecucao(198L, "EMS0198");


		
		save(session, Fixture.criarInterfaceExecucao(106L, "EMS0106"));
		save(session, Fixture.criarInterfaceExecucao(107L, "EMS0107"));
		save(session, Fixture.criarInterfaceExecucao(108L, "EMS0108"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0109.getCodigoInterface(), "EMS0109"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0110.getCodigoInterface(), "EMS0110"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0111.getCodigoInterface(), "EMS0111"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0112.getCodigoInterface(), "EMS0112"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0113.getCodigoInterface(), "EMS0113"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0114.getCodigoInterface(), "EMS0114"));
		save(session, Fixture.criarInterfaceExecucao(116L, "EMS0116"));
		save(session, Fixture.criarInterfaceExecucao(117L, "EMS0117"));
		save(session, Fixture.criarInterfaceExecucao(118L, "EMS0118"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0119.getCodigoInterface(), "EMS0119"));
		save(session, Fixture.criarInterfaceExecucao(120L, "EMS0120"));
		save(session, Fixture.criarInterfaceExecucao(121L, "EMS0121"));
		save(session, Fixture.criarInterfaceExecucao(122L, "EMS0122"));
		save(session, Fixture.criarInterfaceExecucao(123L, "EMS0123"));
		save(session, Fixture.criarInterfaceExecucao(124L, "EMS0124"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0125.getCodigoInterface(), "EMS0125"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0126.getCodigoInterface(), "EMS0126"));
		save(session, Fixture.criarInterfaceExecucao(129L, "EMS0129"));
		save(session, Fixture.criarInterfaceExecucao(130L, "EMS0130"));
		save(session, Fixture.criarInterfaceExecucao(131L, "EMS0131"));
		save(session, Fixture.criarInterfaceExecucao(132L, "EMS0132"));
		save(session, Fixture.criarInterfaceExecucao(133L, "EMS0133"));
//		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0134.getCodigoInterface(), "EMS0134"));
		save(session, Fixture.criarInterfaceExecucao(InterfaceEnum.EMS0185.getCodigoInterface(), "EMS0185"));
		save(session, Fixture.criarInterfaceExecucao(197L, "EMS0197"));
		save(session, Fixture.criarInterfaceExecucao(198L, "EMS0198"));
		save(session, Fixture.criarInterfaceExecucao(135L, "EMS0135"));
	}

	private static void criarEventoExecucao(Session session) {

		eventoErroInfraestrutura 		  	  = Fixture.criarEventoExecucao(EventoExecucaoEnum.ERRO_INFRA.getCodigo(), "Erro Infra", "Erro de infraestrutura");
		eventoSemDominio 				  	  = Fixture.criarEventoExecucao(EventoExecucaoEnum.SEM_DOMINIO.getCodigo(), "Dominio", "Sem Domínio");
		eventoHierarquiaCorrompida 		  	  = Fixture.criarEventoExecucao(EventoExecucaoEnum.HIERARQUIA.getCodigo(), "Hierarquia", "Hierarquia Corrompida");
		eventoRelacionamentoNaoEncontrado  	  = Fixture.criarEventoExecucao(EventoExecucaoEnum.RELACIONAMENTO.getCodigo(), "Relacionamento", "Relacionamento Não Encontrado");
		eventoGeracaoArquivo				  = Fixture.criarEventoExecucao(EventoExecucaoEnum.GERACAO_DE_ARQUIVO.getCodigo(), "Arquivo", "Geração de Arquivo");
		eventoInformacaoDadoAlterado		  = Fixture.criarEventoExecucao(EventoExecucaoEnum.INF_DADO_ALTERADO.getCodigo(), "Alteração Dado", "Informação de dado Alterado");
		eventoRegistroExistente 			  = Fixture.criarEventoExecucao(EventoExecucaoEnum.REGISTRO_JA_EXISTENTE.getCodigo(), "Registro já existente", "Registro já existente");

		save(session, eventoErroInfraestrutura);
		save(session, eventoSemDominio);
		save(session, eventoHierarquiaCorrompida);
		save(session, eventoRelacionamentoNaoEncontrado);
		save(session, eventoGeracaoArquivo);
		save(session, eventoInformacaoDadoAlterado);
		save(session, eventoRegistroExistente);

	}

	private static void gerarLogradouros(Session session) {

		UnidadeFederacao saoPaulo = Fixture.criarUnidadeFederacao("SP");
		UnidadeFederacao minasGerais = Fixture.criarUnidadeFederacao("MG");
		save(session, saoPaulo, minasGerais);

		Localidade casaBranca = Fixture.criarLocalidade(14L, "Casa Branca", 555L, saoPaulo);
		Localidade arceburgo = Fixture.criarLocalidade(15L, "Arceburgo", 556L, minasGerais);
		Localidade ribeirao = Fixture.criarLocalidade(16L, "Ribeirão Preto", 231L, saoPaulo);
		Localidade saoJose = Fixture.criarLocalidade(17L, "São Jose do Rio Pardo", 486L, saoPaulo);
		Localidade saoPauloLoc = Fixture.criarLocalidade(18L, "São Paulo", 465L, saoPaulo);
		
		save(session, casaBranca, arceburgo,ribeirao,saoJose,saoPauloLoc);

		Bairro vilaCarvalho = Fixture.criarBairro(1L, "Vila Carvalho", casaBranca);
		Bairro centro = Fixture.criarBairro(2L, "Centro", arceburgo);
		save(session, vilaCarvalho, centro);

		Logradouro joseCristovam = Fixture.criarLogradouro(1L, "Capitão José Cristovam de Lima", "13735430", 1L, casaBranca, "Rua");
		Logradouro avenidaBrasil = Fixture.criarLogradouro(2L, "Brasil", "37820000", 2L, arceburgo, "Avenida");
		save(session, joseCristovam, avenidaBrasil);
	}


	private static void criarNovaNotaFiscal(Session session) {
		
		Date dataEmissao = Fixture.criarData(01, Calendar.JANUARY, 2012); 
		Date dataEntradaContigencia = Fixture.criarData(01, Calendar.JANUARY, 2012); 
		Date dataSaidaEntrada = Fixture.criarData(01, Calendar.JANUARY, 2012); 
		String descricaoNaturezaOperacao = "Natureza";
		Integer digitoVerificadorChaveAcesso = 1;
		FormaPagamento formaPagamento = FormaPagamento.A_VISTA;
		Date horaSaidaEntrada = new Date();
		String justificativaEntradaContigencia = "Justificativa";
		List<NotaFiscalReferenciada> listReferenciadas = null;
		Long numeroDocumentoFiscal = 1234L;
		Integer serie = 123;
		TipoOperacao tipoOperacao = TipoOperacao.ENTRADA;
		
		Identificacao identificacao = Fixture.identificacao(
				dataEmissao, 
				dataEntradaContigencia, 
				dataSaidaEntrada, 
				descricaoNaturezaOperacao, 
				digitoVerificadorChaveAcesso, 
				formaPagamento, 
				horaSaidaEntrada, 
				justificativaEntradaContigencia, 
				listReferenciadas, 
				numeroDocumentoFiscal, 
				serie, 
				tipoOperacao);
		
		String documento 	= "";
		String email 		= "";
		
		Endereco enderecoDestinatario 	= 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852123", "Rua das paineiras", "4585", "Jrd Limeira", "Pedra de Guaratiba", "RJ",3543402);
		
		session.save(enderecoDestinatario);
		
		String inscricaoEstadual 	= "";
		String inscricaoSuframa 	= "";
		String nome 		= "";
		String nomeFantasia = "";
		Pessoa pessoaDestinatarioReferencia = null;
		Telefone telefone = null;
		
		IdentificacaoDestinatario identificacaoDestinatario = 
				Fixture.identificacaoDestinatario(
						documento, 
						email, 
						enderecoDestinatario, 
						inscricaoEstadual, 
						inscricaoSuframa, 
						nome, 
						nomeFantasia, 
						pessoaDestinatarioReferencia, 
						telefone);
		
		String cnae = "";
		String documentoEmitente = "";
		
		Endereco enderecoEmitente= 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852345", "Rua Laranjeiras", "4585", "Jrd Brasil", "Santana do Livramento", "RJ",6);
		
		session.save(enderecoEmitente);
		
		
		String inscricaoEstualEmitente = "";
		String inscricaoEstualSubstituto = "";
		String inscricaoMunicipalEmitente = "";
		String nomEmitente = "";
		String nomeFantasiaEmitente = "";
		Pessoa pessoaEmitenteReferencia = null;
		RegimeTributario regimeTributario = null;
		Telefone telefoneEmitente = null;
		
		
		
		IdentificacaoEmitente identificacaoEmitente = 
				Fixture.identificacaoEmitente(
						cnae, 
						documentoEmitente, 
						enderecoEmitente, 
						inscricaoEstualEmitente, 
						inscricaoEstualSubstituto, 
						inscricaoMunicipalEmitente, 
						nomEmitente, 
						nomeFantasiaEmitente, 
						pessoaEmitenteReferencia, 
						regimeTributario, 
						telefoneEmitente);
		
		String informacoesComplementares = "";
		
		InformacaoAdicional informacaoAdicional = Fixture.informacaoAdicional(informacoesComplementares);
		
		String chaveAcesso = "523524352354";
		
		Date dataRecebimento 	= Fixture.criarData(01, Calendar.JANUARY, 2012); 
		String motivo 			= "";
		Long protocolo 			= 32165487L;
		Status status			= Status.AUTORIZADO;
		
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = 
				Fixture.retornoComunicacaoEletronica(dataRecebimento, motivo, protocolo, status);
		
		InformacaoEletronica informacaoEletronica = Fixture.informacaoEletronica(
				chaveAcesso, 
				retornoComunicacaoEletronica);
		
		String documentoTranposrte = "564645664";
	
		
		Endereco enderecoTransporte = 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852345", "Rua Maracuja", "4585", "Jrd Brasil", "Piuí", "MG",3543402);
		
		session.save(enderecoTransporte);
		
		Integer modalidadeFrente = 1;
		String municipio = "";
		String nomeFantasiaTransporte = "";
		RetencaoICMSTransporte retencaoICMS = null;
		String ufTransporte = "";
		Veiculo veiculo = null;
		
		
		InformacaoTransporte informacaoTransporte = 
				Fixture.informacaoTransporte(
						documentoTranposrte, 
						enderecoTransporte, 
						inscricaoEstadual, 
						modalidadeFrente, 
						municipio, 
						nomeFantasiaTransporte, 
						retencaoICMS, 
						ufTransporte, 
						veiculo);

		ValoresRetencoesTributos valoresRetencoesTributos = 
				Fixture.valoresRetencoesTributos(
						1L,
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO);
		
		//save(valoresRetencoesTributos);
		
		BigDecimal valorBaseCalculoICMS = BigDecimal.ZERO;
		BigDecimal valorBaseCalculoICMSST = BigDecimal.ZERO;
		BigDecimal valorCOFINS = BigDecimal.ZERO;
		BigDecimal valorDesconto = BigDecimal.ZERO;
		BigDecimal valorFrete = BigDecimal.ZERO;
		BigDecimal valorICMS = BigDecimal.ZERO;
		BigDecimal valorICMSST = BigDecimal.ZERO;
		BigDecimal valorIPI = BigDecimal.ZERO;
		BigDecimal valorNotaFiscal = BigDecimal.ZERO;
		BigDecimal valorOutro = BigDecimal.ZERO;
		BigDecimal valorPIS = BigDecimal.ZERO;
		BigDecimal valorProdutos = BigDecimal.ZERO;
		BigDecimal valorSeguro = BigDecimal.ZERO;
		BigDecimal valorBaseCalculo = BigDecimal.ZERO;
		BigDecimal valorISS = BigDecimal.ZERO;
		BigDecimal valorServicos = BigDecimal.ZERO;
		
		ValoresTotaisISSQN valoresTotaisISSQN = null;
		
		InformacaoValoresTotais informacaoValoresTotais = 
				Fixture.informacaoValoresTotais(
						valoresRetencoesTributos, 
						valoresTotaisISSQN, 
						valorBaseCalculoICMS, 
						valorBaseCalculoICMSST, 
						valorCOFINS, 
						valorDesconto, 
						valorFrete, 
						valorICMS, 
						valorICMSST, 
						valorIPI, 
						valorNotaFiscal, 
						valorOutro, 
						valorPIS, 
						valorProdutos, 
						valorSeguro);
		
		StatusProcessamentoInterno statusProcessamentoInterno = StatusProcessamentoInterno.ENVIADA;
		
		NotaFiscal notaFiscal = new NotaFiscal();
		
		notaFiscal.setIdentificacao(identificacao);
		notaFiscal.setIdentificacaoDestinatario(identificacaoDestinatario);
		notaFiscal.setIdentificacaoEmitente(identificacaoEmitente);
		notaFiscal.setInformacaoAdicional(informacaoAdicional);
		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setInformacaoTransporte(informacaoTransporte);
		notaFiscal.setInformacaoValoresTotais(informacaoValoresTotais);
		notaFiscal.setStatusProcessamentoInterno(statusProcessamentoInterno);
		
		session.save(notaFiscal);
		
		criarProdutosServicos(session, notaFiscal);
		
	}
	
	
	private static void criarProdutosServicos(Session session, NotaFiscal notaFiscal) {
		
		Integer cfop = 1;
		Long codigoBarras = 1L;
		String descricaoProduto = "";
		EncargoFinanceiro encargoFinanceiro = null;
		Long extipi = 1L;
		Long ncm = 1L;
		BigInteger quantidade = BigInteger.ZERO;
		String unidade = "";
		BigDecimal valorDesconto 	= BigDecimal.ZERO;
		BigDecimal valorFrete 		= BigDecimal.ZERO;
		BigDecimal valorOutros 		= BigDecimal.ZERO;
		BigDecimal valorSeguro 		= BigDecimal.ZERO;
		BigDecimal valorTotalBruto 	= BigDecimal.ZERO;
		BigDecimal valorUnitario 	= BigDecimal.ZERO;
	
		int contador = 950;
		
		while(contador++<960) {
			
			String codigoProduto = ""+contador;
			String nomeProduto = "produto_"+contador;
			String descProduto = "";
			PeriodicidadeProduto periodicidade = PeriodicidadeProduto.ANUAL;
			int produtoPeb = 1;
			int produtoPacotePadrao = 1;
			Long produtoPeso = new Long(10000);

			String codigoProdutoEdicao = contador+"";
			Long numeroEdicao = new Long(contador);
			int pacotePadrao = 1;
			int peb = 1;
			Long peso = new Long(0);
			BigDecimal precoCusto = BigDecimal.ZERO;
			BigDecimal precoVenda = BigDecimal.ZERO;
			String codigoDeBarras = contador+"";
			BigDecimal expectativaVenda = BigDecimal.ZERO;
			boolean parcial = false;
			
			ProdutoEdicao produtoEdicaoCE = null;
			Produto produtoCE = Fixture.produto(codigoProduto, descProduto, nomeProduto, periodicidade, tipoProdutoCromo, produtoPeb, produtoPacotePadrao, produtoPeso, TributacaoFiscal. TRIBUTADO);
			produtoCE.addFornecedor(fornecedorDinap);
			session.save(produtoCE);
			produtoEdicaoCE = Fixture.produtoEdicao(codigoProdutoEdicao, numeroEdicao, pacotePadrao, peb,
					peso, precoCusto, precoVenda, codigoDeBarras, null, produtoCE, expectativaVenda, parcial,descProduto);
			session.save(produtoEdicaoCE);
			

			
			codigoProduto = String.valueOf(contador);
			
			ProdutoServico produtoServico = Fixture.produtoServico(
					contador,
					cfop, 
					codigoBarras, 
					codigoProduto, 
					descricaoProduto, 
					encargoFinanceiro, 
					extipi, 
					ncm, 
					notaFiscal, 
					produtoEdicaoCE, 
					quantidade, 
					unidade, 
					valorDesconto, 
					valorFrete, 
					valorOutros, 
					valorSeguro, 
					valorTotalBruto, 
					valorUnitario);
			
			session.save(produtoServico);
			
		}
		
		
	}
	
	private static void criarDescontoProduto(Session session) {
		
		Set<Cota> cotas = new LinkedHashSet<Cota>();
		
		cotas.add(cotaAcme);
		cotas.add(cotaGuilherme);
		cotas.add(cotaJoana);
		
		DescontoProduto descontoProdutoVeja = new DescontoProduto();
		descontoProdutoVeja.setDataAlteracao(new Date());
		descontoProdutoVeja.setCotas(cotas);
		descontoProdutoVeja.setDesconto(new BigDecimal(50));
		descontoProdutoVeja.setDistribuidor(distribuidor);
		descontoProdutoVeja.setProdutoEdicao(produtoEdicaoVeja1);
		descontoProdutoVeja.setUsuario(usuarioJoao);
		
		cotas = new LinkedHashSet<Cota>();
		
		cotas.add(cotaJoao);
		cotas.add(cotaLuis);
		
		DescontoProduto descontoProdutoQuatroRodas = new DescontoProduto();
		descontoProdutoQuatroRodas.setDataAlteracao(DateUtil.adicionarDias(new Date(), -2));
		descontoProdutoQuatroRodas.setCotas(cotas);
		descontoProdutoQuatroRodas.setDesconto(new BigDecimal(44.30));
		descontoProdutoQuatroRodas.setDistribuidor(distribuidor);
		descontoProdutoQuatroRodas.setProdutoEdicao(produtoEdicaoQuatroRodas1);
		descontoProdutoQuatroRodas.setUsuario(usuarioJoao);
		
		cotas = new LinkedHashSet<Cota>();
		
		cotas.add(cotaManoel);
		cotas.add(cotaManoelCunha);
		
		DescontoProduto descontoProdutoInfoExame = new DescontoProduto();
		descontoProdutoInfoExame.setDataAlteracao(DateUtil.adicionarDias(new Date(), -3));
		descontoProdutoInfoExame.setCotas(cotas);
		descontoProdutoInfoExame.setDesconto(new BigDecimal(4.50));
		descontoProdutoInfoExame.setDistribuidor(distribuidor);
		descontoProdutoInfoExame.setProdutoEdicao(produtoEdicaoInfoExame1);
		descontoProdutoInfoExame.setUsuario(usuarioJoao);
		
		cotas = new LinkedHashSet<Cota>();
		
		cotas.add(cotaAcme);
		cotas.add(cotaOrlando);
		cotas.add(cotaMariana);
		
		DescontoProduto descontoProdutoCapricho = new DescontoProduto();
		descontoProdutoCapricho.setDataAlteracao(DateUtil.adicionarDias(new Date(), -4));
		descontoProdutoCapricho.setCotas(cotas);
		descontoProdutoCapricho.setDesconto(new BigDecimal(5.11));
		descontoProdutoCapricho.setDistribuidor(distribuidor);
		descontoProdutoCapricho.setProdutoEdicao(produtoEdicaoCapricho1);
		descontoProdutoCapricho.setUsuario(usuarioJoao);
		
		cotas = new LinkedHashSet<Cota>();
		
		cotas.add(cotaMurilo);
		cotas.add(cotaJoao);
		cotas.add(cotaLuis);
		
		DescontoProduto descontoProdutoSuperInteressante = new DescontoProduto();
		descontoProdutoSuperInteressante.setDataAlteracao(DateUtil.adicionarDias(new Date(), -6));
		descontoProdutoSuperInteressante.setCotas(cotas);
		descontoProdutoSuperInteressante.setDesconto(new BigDecimal(13.22));
		descontoProdutoSuperInteressante.setDistribuidor(distribuidor);
		descontoProdutoSuperInteressante.setProdutoEdicao(produtoEdicaoSuper1);
		descontoProdutoSuperInteressante.setUsuario(usuarioJoao);
		
		save(
			session, descontoProdutoVeja, descontoProdutoQuatroRodas, 
			descontoProdutoInfoExame, descontoProdutoCapricho, descontoProdutoSuperInteressante
		);
	}
	
	private static void criarDescontoProdutoEdicao(Session session) {

		DescontoProdutoEdicao descontoProdutoEdicao = 
				Fixture.descontoProdutoEdicao(cotaManoel, new BigDecimal("10"), fornecedorDinap, produtoEdicaoVeja1, TipoDesconto.ESPECIFICO);

		save(session, descontoProdutoEdicao);
		
		descontoProdutoEdicao = 
				Fixture.descontoProdutoEdicao(cotaJose, new BigDecimal("10"), fornecedorDinap, produtoEdicaoVeja1, TipoDesconto.ESPECIFICO);

		save(session, descontoProdutoEdicao);
		
		descontoProdutoEdicao = 
				Fixture.descontoProdutoEdicao(cotaMaria, new BigDecimal("10"), fornecedorDinap, produtoEdicaoVeja1, TipoDesconto.ESPECIFICO);

		save(session, descontoProdutoEdicao);
		
		descontoProdutoEdicao = 
				Fixture.descontoProdutoEdicao(cotaGuilherme, new BigDecimal("10"), fornecedorDinap, produtoEdicaoVeja1, TipoDesconto.ESPECIFICO);

		save(session, descontoProdutoEdicao);
	}
	
	private static void criarDescontoLogistica(Session session){
		
        Set<Produto> produtos = new LinkedHashSet<Produto>();
		
        produtos.add(produtoVeja);
		
		DescontoLogistica dl = Fixture.descontoLogistica(new Date(), 25f, 25f, new Integer(1),produtos);
	    save(session,dl);
	}
}
