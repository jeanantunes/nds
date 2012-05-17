package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao.TipoOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
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
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;

public class DataLoader {

	private static PessoaJuridica juridicaAcme;
	private static PessoaJuridica juridicaDinap;
	private static PessoaJuridica juridicaFc;
	private static PessoaJuridica juridicaValida;
	private static PessoaFisica manoel;
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
	private static TipoMovimentoEstoque tipoMovimentoEnvioEncalhe;
	private static TipoMovimentoEstoque tipoMovimentoEstornoCotaAusente;
	private static TipoMovimentoEstoque tipoMovimentoSuplementarCotaAusente;

	private static TipoMovimentoEstoque tipoMovimentoReparteCotaAusente;
	private static TipoMovimentoEstoque tipoMovimentoRestautacaoReparteCotaAusente;

	private static TipoMovimentoEstoque tipoMovimentoVendaEncalhe;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompraEncalhe;

	private static TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro;

	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte;

	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe;

	private static MovimentoEstoqueCota movimentoEstoqueCota1;
	private static MovimentoEstoqueCota movimentoEstoqueCota2;
	private static MovimentoEstoqueCota movimentoEstoqueCota3;
	private static MovimentoEstoqueCota movimentoEstoqueCota4;
	private static MovimentoEstoqueCota movimentoEstoqueCota5;
	private static MovimentoEstoqueCota movimentoEstoqueCota6;
	private static MovimentoEstoqueCota movimentoEstoqueCota7;
	private static MovimentoEstoqueCota movimentoEstoqueCota8;
	private static MovimentoEstoqueCota movimentoEstoqueCota9;

	private static MovimentoFinanceiroCota movimentoFinanceiroCota1;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota2;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota3;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota4;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota5;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota6;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota7;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota8;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota9;

	private static Carteira carteiraRegistrada;
	private static Carteira carteiraSemRegistro;

	private static CFOP cfop5102;
	private static TipoNotaFiscal tipoNotaFiscalRecebimento;
	private static Usuario usuarioJoao;
	private static Fornecedor fornecedorAcme;
	private static Fornecedor fornecedorDinap;
	private static Fornecedor fornecedorFc;
	private static Distribuidor distribuidor;
	private static TipoProduto tipoProdutoRevista;
	private static TipoProduto tipoRefrigerante;

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

	private static Lancamento lancamentoVeja1;
	private static Lancamento lancamentoVeja2;
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

	private static NotaFiscalEntradaFornecedor notaFiscalFornecedor;
	private static ItemNotaFiscalEntrada itemNotaFiscalFornecedor;
	private static RecebimentoFisico recebimentoFisico;
	private static ItemRecebimentoFisico itemRecebimentoFisico;

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

	private static Cota cotaJose;
	private static Cota cotaManoel;
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

	private static EstudoCota estudoCotaSuper1Manoel;
	private static EstudoCota estudoCotaManoel;
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

	private static Box box300Reparte;
	private static Box box1;
	private static Box box2;

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
	
	private static TipoEntrega tipoCotaRetira;
	private static TipoEntrega tipoEntregaEmBanca;
	private static TipoEntrega tipoEntregador;
	

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext-dataLoader.xml");
		SessionFactory sf = null;
		Session session = null;
		Transaction tx = null;
		boolean commit = false;
		try {
			sf = ctx.getBean(SessionFactory.class);
			session = sf.openSession();
			tx = session.beginTransaction();			
			carregarDados(session);
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

	private static void gerarCotasAusentes(Session session) {
		CotaAusente cotaAusente1 = Fixture.cotaAusente(new Date(), true, cotaGuilherme);
		CotaAusente cotaAusente2 = Fixture.cotaAusente(new Date(), true, cotaMurilo);
		CotaAusente cotaAusente3 = Fixture.cotaAusente(new Date(), true, cotaMariana);
		CotaAusente cotaAusente4 = Fixture.cotaAusente(Fixture.criarData(1, 1, 2001), true, cotaOrlando);

		save(session, cotaAusente1, cotaAusente2, cotaAusente3, cotaAusente4);
	}

	private static void carregarDados(Session session) {
		criarCarteira(session);
		criarBanco(session);
		criarPessoas(session);
		criarDistribuidor(session);
		criarEnderecoDistribuidor(session);
		criarTelefoneDistribuidor(session);
		criarParametrosSistema(session);
		criarUsuarios(session);
		criarTiposFornecedores(session);
		criarBox(session);
		criarFornecedores(session);
		criarDiasDistribuicaoFornecedores(session);
		criarDiasDistribuicaoDistribuidor(session);
		criarCotas(session);
		criarDistribuicaoCota(session);
		criarEditores(session);
		criarTiposProduto(session);
		criarProdutos(session);
		criarProdutosEdicao(session);
		criarCFOP(session);
		criarTiposMovimento(session);

		criarTiposNotaFiscal(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarEstoqueProdutoCota(session);
		criarMovimentosEstoque(session);
		criarLancamentos(session);
		criarEstudos(session);
		criarEstudosCota(session);
		criarMovimentosEstoqueCota(session);
		criarFeriado(session);		
		criarEnderecoCotaPF(session);
		criarParametroEmail(session);
		criarDivida(session);

		criarCobrancas(session);

		criarBoletos(session);
		criarCobrancaCheque(session);
		criarCobrancaDepositoBancaria(session);
		criarCobrancaDinheiro(session);
		criarCobrancaTranferenciaBancaria(session);
		criarMovimentosFinanceiroCota(session);
		criarNotasFiscaisEntradaFornecedor(session);
		criarRotaRoteiroCota(session);		
		criarControleBaixaBancaria(session);
		criarParametrosCobrancaCota(session);
		criarNotasFiscaisEntradaFornecedor(session);
		gerarCotasAusentes(session);
		gerarHistoricosAculoDivida(session);

		massaDadosContaCorrenteTipoMovimento(session);

		criarDadosContaCorrenteConsigando(session);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja1, tipoMovimentoFaltaEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja3, tipoMovimentoSobraDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja4, tipoMovimentoSobraEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM);

		gerarCargaHistoricoSituacaoCota(session, 100);

		gerarCargaDadosConsultaEncalhe(session);

		gerarCargaDadosNotasFiscaisNFE(session);

		gerarEntregadores(session);
		
		gerarTiposPontoPDV(session);
			
		gerarAreaInfluenciaPDV(session);
		
		gerarClusterPDV(session);
		
		gerarEspecialidadePDV(session);
		
		gerarTipoGeradorFluxoPDV(session);
		
		gerarMaterialPromocionalPDV(session);
		
		gerarTipoEstabelecimentoAssociacaoPDV(session);
		
		gerarTipoLicencaMunicipalPDV(session);
		
		gerarTipoEntrega(session);
		
		gerarLancamentos(session);
		
		gerarDescontos(session);
		
	}

	private static void gerarDescontos(Session session){
		
		TipoDesconto tipoDesconto = Fixture.criarTipoDesconto("001", "Normal", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("002", "Produtos Tributados", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("003", "Video Print de 1/1/96 A", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("004", "Cromos - Normal Exc Ju", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("005", "Importadas - Eletrolibe", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("006", "Promoções", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("007", "Especial Globo", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("008", "Magazine Fome Zero", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("009", "Impratadas Mag", new BigDecimal(10));
		save(session, tipoDesconto);
		tipoDesconto = Fixture.criarTipoDesconto("010", "Importadas MagExpress", new BigDecimal(10));
		save(session, tipoDesconto);
	}
	
	private static void gerarLancamentos(Session session) {
		
		LancamentoParcial lancamentoParcial1 = Fixture.criarLancamentoParcial(
				produtoEdicaoBoaForma1, Fixture.criarData(1, 1, 2009), Fixture.criarData(1, 1, 2010), StatusLancamentoParcial.RECOLHIDO);
		
		LancamentoParcial lancamentoParcial2 = Fixture.criarLancamentoParcial(
				produtoEdicaoBravo1, Fixture.criarData(1, 2, 2011), Fixture.criarData(1, 2, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial3 = Fixture.criarLancamentoParcial(
				produtoEdicaoCaras1, Fixture.criarData(1, 3, 2011), Fixture.criarData(1, 3, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial4 = Fixture.criarLancamentoParcial(
				produtoEdicaoClaudia1, Fixture.criarData(1, 4, 2011), Fixture.criarData(1, 4, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial5 = Fixture.criarLancamentoParcial(
				produtoEdicaoContigo1, Fixture.criarData(1, 5, 2011), Fixture.criarData(1, 5, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial6 = Fixture.criarLancamentoParcial(
				produtoEdicaoManequim1, Fixture.criarData(1, 6, 2011), Fixture.criarData(1, 6, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial7 = Fixture.criarLancamentoParcial(
				produtoEdicaoNatGeo1, Fixture.criarData(1, 7, 2011), Fixture.criarData(1, 7, 2012), StatusLancamentoParcial.PROJETADO);
		
		LancamentoParcial lancamentoParcial8 = Fixture.criarLancamentoParcial(
				produtoEdicaoVeja1, Fixture.criarData(1, 8, 2011), Fixture.criarData(1, 8, 2012), StatusLancamentoParcial.PROJETADO);
		
		save(session, lancamentoParcial1,lancamentoParcial2,lancamentoParcial3,
				lancamentoParcial4,lancamentoParcial5,lancamentoParcial6,lancamentoParcial7,
				lancamentoParcial8);
	}

	private static void gerarTipoEntrega(Session session) {
		
		tipoCotaRetira = Fixture.criarTipoEntrega(1L,"Cota Retira");
		tipoEntregaEmBanca = Fixture.criarTipoEntrega(1L,"Entrega em Banca");
		tipoEntregador = Fixture.criarTipoEntrega(1L,"Entregador");
		
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

	private static void gerarEspecialidadePDV(Session session) {
		
		EspecialidadePDV especialidadePDV = Fixture.criarEspecialidadesPDV(1L, "Moda");
		EspecialidadePDV especialidadePDV1 = Fixture.criarEspecialidadesPDV(2L, "Decoração");
		EspecialidadePDV especialidadePDV2 = Fixture.criarEspecialidadesPDV(3L, "Informatica");
		EspecialidadePDV especialidadePDV3 = Fixture.criarEspecialidadesPDV(4L, "Sexo");
		EspecialidadePDV especialidadePDV4 = Fixture.criarEspecialidadesPDV(5L, "Quadrinhos");
		EspecialidadePDV especialidadePDV5 = Fixture.criarEspecialidadesPDV(6L, "Moda");
		
		save(session,especialidadePDV,especialidadePDV1,especialidadePDV2,especialidadePDV3,especialidadePDV4,especialidadePDV5);
	}

	private static void gerarClusterPDV(Session session) {
		
		ClusterPDV clusterPDV = Fixture.criarClusterPDV(1L, "Basico");
		ClusterPDV clusterPDV1 = Fixture.criarClusterPDV(1L, "Monaco");
		ClusterPDV clusterPDV2 = Fixture.criarClusterPDV(1L, "Exportador");
		
		save(session,clusterPDV,clusterPDV1,clusterPDV2);
	}

	private static void gerarAreaInfluenciaPDV(Session session) {
		
		AreaInfluenciaPDV areaInfluenciaPDV = Fixture.criarAreaInfluenciaPDV(1L, "Residencial");
		AreaInfluenciaPDV areaInfluenciaPDV1 = Fixture.criarAreaInfluenciaPDV(2L, "Residencial XX");
		AreaInfluenciaPDV areaInfluenciaPDV2 = Fixture.criarAreaInfluenciaPDV(3L, "Estradas");
		
		save(session,areaInfluenciaPDV,areaInfluenciaPDV1,areaInfluenciaPDV2);
	}

	
	private static void gerarTiposPontoPDV(Session session) {

		TipoPontoPDV tipoPontoPDV  = Fixture.criarTipoPontoPDV(1L, "Residencial");
		TipoPontoPDV tipoPontoPDV2  = Fixture.criarTipoPontoPDV(2L, "Comercial");
		
		save(session,tipoPontoPDV,tipoPontoPDV2);
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

		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(session,tipoProduto);

		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedor);

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedorPublicacao);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(session,tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop5102, juridicaFc, fornecedorFc, tipoNotaFiscal, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
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
						new BigDecimal(12));
		save(session,itemNotaFiscal);

		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal(12));
		save(session,itemRecebimentoFisico);

		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicaoBravo1, dataAtual, dataAtual, dataAtual, dataAtual, new BigDecimal(30), StatusLancamento.CONFIRMADO, itemRecebimentoFisico, 1);
		save(session,lancamento);	

		Estudo estudo = Fixture.estudo(BigDecimal.TEN, dataAtual, produtoEdicaoBravo1);
		save(session,estudo);

		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(20), new BigDecimal(10), estudo, cotaManoel);
		save(session,estudoCota);

		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(session,expedicao);

		EstoqueProdutoCota  estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicaoBravo1,new BigDecimal(30), cotaManoel, listaMovimentoEstoqueCota);		
		save(session,estoqueProdutoCota);

		TipoMovimentoEstoque tipoMovimentoEstoque = Fixture.tipoMovimentoSobraDe();
		save(session,tipoMovimentoEstoque);

		MovimentoEstoqueCota movimentoEstoqueCota = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoEstoque, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "MOTIVO A");
		movimentoEstoqueCota.setEstudoCota(estudoCota);
		save(session,movimentoEstoqueCota);

		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicaoBravo1, new BigDecimal(45)); 
		save(session,estoqueProduto);

		MovimentoEstoque movimentoEstoque = Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoBravo1, tipoMovimentoEstoque, usuario, estoqueProduto, dataAtual, new BigDecimal(12), StatusAprovacao.APROVADO , "MOTIVO B");
		save(session,movimentoEstoque);

		Diferenca diferenca = Fixture.diferenca(new BigDecimal(32), usuario, produtoEdicaoBravo1, TipoDiferenca.FALTA_DE, StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoque, true);
		save(session,diferenca);

		RateioDiferenca rateioDiferenca = Fixture.rateioDiferenca(BigDecimal.TEN , cotaManoel, diferenca, estudoCota);
		save(session,rateioDiferenca);

		movimento = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoConsignado, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		movimento.setEstudoCota(estudoCota);


		estoqueProdutoCota.getMovimentos().add(movimento);
		save(session,movimento);
		MovimentoEstoqueCota  movimentoEnvioJornaleiro = Fixture.movimentoEstoqueCota(produtoEdicaoBravo1, tipoMovimentoConsignado, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
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

		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230));
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

	TipoProduto tipoProduto = Fixture.tipoRevista();
	save(session, tipoProduto);

	TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("Tipo A",GrupoFornecedor.PUBLICACAO);
	save(session, tipoFornecedor);


	produtoBravo.addFornecedor(fornecedorAcme);
	save(session, produtoBravo);

	ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(234L,12 , 1, new BigDecimal(9), new BigDecimal(8), 
			new BigDecimal(10), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoBravo);		
	save(session, produtoEdicao);

	TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
	save(session, tipoFornecedorPublicacao);

	TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
	save(session, tipoNotaFiscal);

	NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop5102, juridicaDinap, fornecedorAcme, tipoNotaFiscal, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
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
					new BigDecimal(12));
	save(session, itemNotaFiscal);

	ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal(12));
	save(session, itemRecebimentoFisico);

	Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataAtual, dataAtual, dataAtual, dataAtual, new BigDecimal(30), StatusLancamento.CONFIRMADO, itemRecebimentoFisico, 1);
	save(session, lancamento);	

	Estudo estudo = Fixture.estudo(BigDecimal.TEN, dataAtual, produtoEdicao);
	save(session, estudo);

	EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(30), new BigDecimal(30), estudo, cotaManoel);
	save(session, estudoCota);

	Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
	save(session, expedicao);

	EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
	estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao,new BigDecimal(30), cotaManoel, listaMovimentoEstoqueCota);
	save(session, estoqueProdutoCota);




	massaDadosContaCorrenteMovimento(session, tipoMovimentoVendaEncalhe,
			tipoMovimentoFinanceiroCompraEncalhe, dataAtual, listaMovimentoEstoqueCota,
			usuario, produtoEdicao, estoqueProdutoCota);


	massaDadosContaCorrenteMovimento(session, tipoMovimentoEnvioEncalhe,
			tipoMovimentoFinanceiroEnvioEncalhe, dataAtual, listaMovimentoEstoqueCota,
			usuario, produtoEdicao, estoqueProdutoCota);

}

	private static void massaDadosContaCorrenteMovimento(Session session,
			TipoMovimentoEstoque tipoMovimento,
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro, Date dataAtual,
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota,
			Usuario usuario, ProdutoEdicao produtoEdicao,
			EstoqueProdutoCota estoqueProdutoCota) {
		MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimento);

		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario, 
				new BigDecimal(230), listaMovimentoEstoqueCota, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCota);


		MovimentoEstoqueCota movimentoVendaEncalhe  = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimentoVendaEncalhe);

		List<MovimentoEstoqueCota> listMovimentoEstoqueCotas = new ArrayList<MovimentoEstoqueCota>();

		listMovimentoEstoqueCotas.add(movimentoVendaEncalhe);
		MovimentoFinanceiroCota movimentoFinanceiroCompraEncalhe= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario, 
				new BigDecimal(230), listMovimentoEstoqueCotas, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCompraEncalhe);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCompraEncalhe);

		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230));
		save(session, consolidadoFinanceiroCota);
	}






	private static void criarControleBaixaBancaria(Session session) {
		baixaBancaria = Fixture.controleBaixaBancaria(new Date(), StatusControle.CONCLUIDO_SUCESSO, usuarioJoao);
		save(session, baixaBancaria);

	}

	private static void criarEstoqueProdutoCota(Session session) {

		estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja1);

		estoqueProdutoCotaVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja2);

		estoqueProdutoCotaVeja3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja3, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja3);

		estoqueProdutoCotaVeja4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja4, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja4);

		estoqueProdutoCotaSuper1 = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1, cotaMurilo, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaSuper1);

		estoqueProdutoCotaCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaMariana, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaCapricho1);

		estoqueProdutoCotaQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaOrlando, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaQuatroRodas1);

		estoqueProdutoCotaInfoExame1 = Fixture.estoqueProdutoCota(
				produtoEdicaoInfoExame1, cotaOrlando, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaInfoExame1);

		estoqueProdutoCotaJoseVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaJose, new BigDecimal(20), BigDecimal.ONE);
		save(session, estoqueProdutoCotaJoseVeja1EncalheAnt);

		estoqueProdutoCotaManoelVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaManoel, BigDecimal.TEN, BigDecimal.ONE);
		save(session, estoqueProdutoCotaManoelVeja1EncalheAnt);

		estoqueProdutoCotaMariaVeja1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1EncalheAnt, cotaMaria, new BigDecimal(15), BigDecimal.ONE);
		save(session, estoqueProdutoCotaMariaVeja1EncalheAnt);

		estoqueProdutoCotaJoseVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaJose,  new BigDecimal(36), BigDecimal.ONE);
		save(session, estoqueProdutoCotaJoseVeja2EncalheAnt);

		estoqueProdutoCotaManoelVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaManoel, new BigDecimal(50), BigDecimal.ONE);
		save(session, estoqueProdutoCotaManoelVeja2EncalheAnt);

		estoqueProdutoCotaMariaVeja2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2EncalheAnt, cotaMaria, new BigDecimal(7), BigDecimal.ONE);
		save(session, estoqueProdutoCotaMariaVeja2EncalheAnt);

		estoqueProdutoCotaJoseSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaJose, new BigDecimal(20), BigDecimal.ONE);
		save(session, estoqueProdutoCotaJoseSuper1EncalheAnt);

		estoqueProdutoCotaManoelSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaManoel, BigDecimal.TEN, BigDecimal.ONE);
		save(session, estoqueProdutoCotaManoelSuper1EncalheAnt);

		estoqueProdutoCotaMariaSuper1EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1EncalheAnt, cotaMaria, new BigDecimal(15), BigDecimal.ONE);
		save(session, estoqueProdutoCotaMariaSuper1EncalheAnt);

		estoqueProdutoCotaJoseSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaJose,  new BigDecimal(36), BigDecimal.ONE);
		save(session, estoqueProdutoCotaJoseSuper2EncalheAnt);

		estoqueProdutoCotaManoelSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaManoel, new BigDecimal(50), BigDecimal.ONE);
		save(session, estoqueProdutoCotaManoelSuper2EncalheAnt);

		estoqueProdutoCotaMariaSuper2EncalheAnt = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper2EncalheAnt, cotaMaria, new BigDecimal(7), BigDecimal.ONE);
		save(session, estoqueProdutoCotaMariaSuper2EncalheAnt);

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
				false, new BigDecimal(1000));
		save(session, parametroCobrancaGuilherme);
		formaDinheiro.setParametroCobrancaCota(parametroCobrancaGuilherme);
		formaDinheiro.setPrincipal(true);
		save(session,formaDinheiro);
		

		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		parametroCobrancaMurilo = Fixture.parametroCobrancaCota(formasCobranca,
				null, new BigDecimal(100), cotaMurilo, 1,
				false, new BigDecimal(1000));
		save(session, parametroCobrancaMurilo);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaMurilo);
		formaBoleto.setPrincipal(true);
		save(session,formaBoleto);
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto2);
		parametroCobrancaMariana = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaMariana, 1,
				false, new BigDecimal(1000));
		save(session, parametroCobrancaMariana);
		formaBoleto2.setParametroCobrancaCota(parametroCobrancaMariana);
		formaBoleto2.setPrincipal(true);
		save(session,formaBoleto2);
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDeposito);
		parametroCobrancaOralando = Fixture.parametroCobrancaCota(formasCobranca,
				null, null, cotaOrlando, 1,
				false, new BigDecimal(1000));
		save(session, parametroCobrancaOralando);
		formaDeposito.setParametroCobrancaCota(parametroCobrancaOralando);
		formaDeposito.setPrincipal(true);
		save(session,formaDeposito);
		
	}
	
	private static void criarRotaRoteiroCota(Session session) {

		Rota rota = Fixture.rota("005", "Rota 005");
		session.save(rota);

		Roteiro roteiro = Fixture.roteiro("Pinheiros");
		session.save(roteiro);

		RotaRoteiroOperacao rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaManoel, TipoOperacao.IMPRESSAO_DIVIDA);
		session.save(rotaRoteiroOperacao);

		rota = Fixture.rota("004", "Rota 004");
		session.save(rota);

		roteiro = Fixture.roteiro("Interlagos");
		session.save(roteiro);

		rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaJose, TipoOperacao.IMPRESSAO_DIVIDA);
		session.save(rotaRoteiroOperacao);
		
		rota = Fixture.rota("007", "Rota 007");
		session.save(rota);

		roteiro = Fixture.roteiro("Mococa");
		session.save(roteiro);

		rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaAcme, TipoOperacao.IMPRESSAO_DIVIDA);
		session.save(rotaRoteiroOperacao);
	}

	private static void criarDivida(Session session) {

		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota1), cotaManoel,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota2), cotaJose,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado3 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota3), cotaMaria,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado4 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota4), cotaManoel,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado5 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota5), cotaJose,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado6 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota6), cotaMaria,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado7 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaManoel,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado8 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaJose,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado9 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaMaria,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado10 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaManoel,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado11 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaLuis,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado12 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota7), cotaJoao,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado13 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota8), cotaJose,
						new Date(), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidado14 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota9), cotaJose,
						new Date(), new BigDecimal(200));


		ConsolidadoFinanceiroCota consolidadoGuilherme1 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(1, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(2, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoGuilherme3 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(3, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoMurilo1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(4, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoMurilo2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(5, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoMurilo3 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(6, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoMariana1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(7, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoMariana2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(8, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoOrlando1 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(9, 1, 2010), new BigDecimal(200));

		ConsolidadoFinanceiroCota consolidadoOrlando2 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(10, 1, 2010), new BigDecimal(200));


		ConsolidadoFinanceiroCota consolidadoAcumuloGuilherme1 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(1, 2, 2010), new BigDecimal(210));

		ConsolidadoFinanceiroCota consolidadoAcumuloGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(2, 2, 2010), new BigDecimal(210));

		ConsolidadoFinanceiroCota consolidadoAcumuloMurilo1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(4, 2, 2010), new BigDecimal(210));

		ConsolidadoFinanceiroCota consolidadoAcumuloMariana1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(7, 2, 2010), new BigDecimal(210));

		save(session, consolidado1, consolidado2, consolidado3,
					  consolidado4, consolidado5, consolidado6,
					  consolidado7, consolidado8, consolidado9,
					  consolidado10, consolidado11,consolidado12,
					  consolidado13,consolidado14,
					  consolidadoGuilherme1, consolidadoGuilherme2, consolidadoGuilherme3,
					  consolidadoMurilo1, consolidadoMurilo2, consolidadoMurilo3,
					  consolidadoMariana1, consolidadoMariana2,
					  consolidadoOrlando1, consolidadoOrlando2,
					  consolidadoAcumuloGuilherme1,consolidadoAcumuloGuilherme2,consolidadoAcumuloMariana1,consolidadoAcumuloMurilo1);



		divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida2 = Fixture.divida(consolidado2, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida3 = Fixture.divida(consolidado3, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida4 = Fixture.divida(consolidado4, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida5 = Fixture.divida(consolidado5, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida6 = Fixture.divida(consolidado6, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida7 = Fixture.divida(consolidado7, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida8 = Fixture.divida(consolidado8, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida9 = Fixture.divida(consolidado9, cotaMaria, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida10 = Fixture.divida(consolidado10, cotaMaria, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida11 = Fixture.divida(consolidado11, cotaLuis, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida12 = Fixture.divida(consolidado12, cotaJoao, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida13 = Fixture.divida(consolidado13, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		divida14 = Fixture.divida(consolidado14, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));


		dividaGuilherme1 = Fixture.divida(consolidadoGuilherme1, cotaGuilherme, Fixture.criarData(1, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaGuilherme2 = Fixture.divida(consolidadoGuilherme2, cotaGuilherme, Fixture.criarData(2, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaGuilherme3 = Fixture.divida(consolidadoGuilherme3, cotaGuilherme, Fixture.criarData(3, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaMurilo1 = Fixture.divida(consolidadoMurilo1, cotaMurilo, Fixture.criarData(4, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaMurilo2 = Fixture.divida(consolidadoMurilo2, cotaMurilo, Fixture.criarData(5, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaMurilo3 = Fixture.divida(consolidadoMurilo3, cotaMurilo, Fixture.criarData(6, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaMariana1 = Fixture.divida(consolidadoMariana1, cotaMariana, Fixture.criarData(7, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaMariana2 = Fixture.divida(consolidadoMariana2, cotaMariana, Fixture.criarData(8, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		dividaOrlando = Fixture.divida(consolidadoOrlando1, cotaOrlando, Fixture.criarData(9, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));



		dividaAcumuladaGuilherme1 = Fixture.divida(consolidadoAcumuloGuilherme1, cotaGuilherme, Fixture.criarData(1, 2, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(210));

		dividaAcumuladaGuilherme2 = Fixture.divida(consolidadoAcumuloGuilherme2, cotaGuilherme, Fixture.criarData(2, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210));

		dividaAcumuladaMurilo1 = Fixture.divida(consolidadoAcumuloMurilo1, cotaMurilo, Fixture.criarData(4, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210));

		dividaAcumuladaMariana1 = Fixture.divida(consolidadoAcumuloMariana1, cotaMariana, Fixture.criarData(7, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210));

		save(session, divida1, divida2, divida3, divida4, divida5, divida6,
				      divida7, divida8, divida9, divida10, divida11,
				      divida12,divida13,divida14,
				      dividaGuilherme1, dividaGuilherme2, dividaGuilherme3,
				      dividaMurilo1, dividaMurilo2, dividaMurilo3,
				      dividaMariana1, dividaMariana2,
				      dividaOrlando,
				      dividaAcumuladaGuilherme1, dividaAcumuladaGuilherme2,dividaAcumuladaMariana1,dividaAcumuladaMurilo1);

		dividaAcumuladaGuilherme1.getAcumulado().add(dividaGuilherme1);
		dividaAcumuladaGuilherme2.getAcumulado().add(dividaGuilherme2);
		dividaAcumuladaMariana1.getAcumulado().add(dividaMariana1);
		dividaAcumuladaMurilo1.getAcumulado().add(dividaMurilo1);

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
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota3 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota4 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota5 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota6 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota7 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota8 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		movimentoEstoqueCota9 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota10 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaGuilherme, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota11 = Fixture.movimentoEstoqueCota(produtoEdicaoInfoExame1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaInfoExame1,
				new BigDecimal(55), cotaGuilherme, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota12 = Fixture.movimentoEstoqueCota(produtoEdicaoSuper1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaSuper1,
				BigDecimal.TEN, cotaMurilo, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota13 = Fixture.movimentoEstoqueCota(produtoEdicaoCapricho1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaCapricho1,
				BigDecimal.TEN, cotaMariana, StatusAprovacao.PENDENTE, null);

		MovimentoEstoqueCota movimentoEstoqueCota14 = Fixture.movimentoEstoqueCota(produtoEdicaoQuatroRodas1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaQuatroRodas1,
				BigDecimal.TEN, cotaOrlando, StatusAprovacao.PENDENTE, null);

		save(session, movimentoEstoqueCota1, movimentoEstoqueCota2, movimentoEstoqueCota3,
			 movimentoEstoqueCota4, movimentoEstoqueCota5, movimentoEstoqueCota6,
			 movimentoEstoqueCota7, movimentoEstoqueCota8, movimentoEstoqueCota9,
			 movimentoEstoqueCota10, movimentoEstoqueCota11, movimentoEstoqueCota12,
			 movimentoEstoqueCota13, movimentoEstoqueCota14);
	}

	private static void criarMovimentosEstoqueCotaConferenciaEncalhe(Session session) {

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe
				(DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()), 
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()), 
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

	}

	private static void criarParametrosSistema(Session session) {

		ParametroSistema parametroSistema = 
			Fixture.parametroSistema(
				TipoParametroSistema.PATH_IMAGENS_CAPA, "C:\\apache-tomcat-7.0.25\\webapps\\nds-client\\capas\\");

		session.save(parametroSistema);
		
		ParametroSistema parametroPathImagemPDV = 
				Fixture.parametroSistema(
					TipoParametroSistema.PATH_IMAGENS_PDV, "\\images\\pdv\\");
		session.save(parametroPathImagemPDV);
				
		parametroSistema = 
			Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_DE, "7");

		session.save(parametroSistema);

		parametroSistema = 
			Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM, "7");

		session.save(parametroSistema);

		parametroSistema = 
			Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM, "7");

		session.save(parametroSistema);

		parametroSistema = 
			Fixture.parametroSistema(TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE, "7");

		session.save(parametroSistema);
	}

	private static void criarMovimentosEstoque(Session session) {
		MovimentoEstoque movimentoRecFisicoVeja1 = 
			Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
				 StatusAprovacao.APROVADO, "Aprovado");

		session.save(movimentoRecFisicoVeja1);

		MovimentoEstoque movimentoEstoque1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque1);

		MovimentoEstoque movimentoEstoque2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque2);

		MovimentoEstoque movimentoEstoque3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), new BigDecimal(2),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque3);

		MovimentoEstoque movimentoEstoque4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), new BigDecimal(2),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoque4);

		MovimentoEstoque movimentoEstoque5 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), new BigDecimal(3),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque5);

		MovimentoEstoque movimentoEstoqueDiferenca6 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), new BigDecimal(3),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoqueDiferenca6);

		MovimentoEstoque movimentoEstoque7 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), new BigDecimal(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque7);

		MovimentoEstoque movimentoEstoque8 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), new BigDecimal(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque8);

		MovimentoEstoque movimentoRecFisicoVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja2);

		MovimentoEstoque movimentoRecFisicoVeja3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja3, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja3);

		MovimentoEstoque movimentoEnvioJornaleiroVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);	
		session.save(movimentoEnvioJornaleiroVeja1);

		MovimentoEstoque movimentoEnvioJornaleiroSuper1 =
			Fixture.movimentoEstoque(null, produtoEdicaoSuper1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoSuper1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioJornaleiroSuper1);

		MovimentoEstoque movimentoEnvioEncalheVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja1);

		MovimentoEstoque movimentoEnvioEncalheVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja2);
	}

	private static void criarEstoquesProdutos(Session session) {
		estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigDecimal.TEN);

		estoqueProdutoVeja2 = Fixture.estoqueProduto(produtoEdicaoVeja2, BigDecimal.TEN);

		estoqueProdutoVeja3 = Fixture.estoqueProduto(produtoEdicaoVeja3, BigDecimal.TEN);

		estoqueProdutoVeja4 = Fixture.estoqueProduto(produtoEdicaoVeja4, BigDecimal.TEN);

		estoqueProdutoSuper1 = Fixture.estoqueProduto(produtoEdicaoSuper1, BigDecimal.TEN);

		estoqueProdutoCapricho1 = Fixture.estoqueProduto(produtoEdicaoCapricho1, BigDecimal.TEN);

		estoqueProdutoInfoExame1 = Fixture.estoqueProduto(produtoEdicaoInfoExame1, BigDecimal.TEN);

		estoqueProdutoVeja1EncalheAnt = Fixture.estoqueProduto(produtoEdicaoVeja1EncalheAnt, BigDecimal.TEN);

		estoqueProdutoVeja2EncalheAnt = Fixture.estoqueProduto(produtoEdicaoVeja2EncalheAnt, BigDecimal.TEN);

		estoqueProdutoSuper1EncalheAnt = Fixture.estoqueProduto(produtoEdicaoSuper1EncalheAnt, BigDecimal.TEN);

		estoqueProdutoSuper2EncalheAnt = Fixture.estoqueProduto(produtoEdicaoSuper2EncalheAnt, BigDecimal.TEN);

		save(session, estoqueProdutoVeja1, estoqueProdutoVeja2, estoqueProdutoVeja3,
			 estoqueProdutoVeja4, estoqueProdutoSuper1, estoqueProdutoCapricho1,
			 estoqueProdutoInfoExame1,estoqueProdutoVeja1EncalheAnt,estoqueProdutoVeja2EncalheAnt,
			 estoqueProdutoSuper1EncalheAnt,estoqueProdutoSuper2EncalheAnt);
	}

	private static void criarRecebimentosFisicos(Session session) {
		recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);

		itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigDecimal.TEN);
		session.save(itemRecebimentoFisico);

		itemCocaRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalCoca, recebimentoFisico, new BigDecimal(50));
		session.save(itemCocaRecebimentoFisico);


		itemInfoExame1 = Fixture.itemRecebimentoFisico(itemNotaInfoExame1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemInfoExame1);

		itemQuatroRodas1 = Fixture.itemRecebimentoFisico(itemNotaQuatroRodas1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemQuatroRodas1);

		itemBoaForma1 = Fixture.itemRecebimentoFisico(itemNotaBoaForma1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemBoaForma1);

		itemBravo1 = Fixture.itemRecebimentoFisico(itemNotaBravo1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemBravo1);

		itemCaras1 = Fixture.itemRecebimentoFisico(itemNotaCaras1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemCaras1);

		itemCasaClaudia1 = Fixture.itemRecebimentoFisico(itemNotaCasaClaudia1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemCasaClaudia1);

		itemClaudia1 = Fixture.itemRecebimentoFisico(itemNotaClaudia1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemClaudia1);

		itemContigo1 = Fixture.itemRecebimentoFisico(itemNotaContigo1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemContigo1);

		itemManequim1 = Fixture.itemRecebimentoFisico(itemNotaManequim1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemManequim1);

		itemNatGeo1 = Fixture.itemRecebimentoFisico(itemNotaNatGeo1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemNatGeo1);

		itemPlacar1 = Fixture.itemRecebimentoFisico(itemNotaPlacar1, recebimentoFisico, BigDecimal.TEN);
		session.save(itemPlacar1);
	}

	private static void criarNotasFiscais(Session session) {
		notaFiscalFornecedor = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		session.save(notaFiscalFornecedor);

		itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaFiscalFornecedor);

		itemNotaFiscalCoca= Fixture.itemNotaFiscal(cocaColaLight,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaFiscalCoca);


		itemNotaInfoExame1 = Fixture.itemNotaFiscal(produtoEdicaoInfoExame1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaInfoExame1);

		itemNotaQuatroRodas1 = Fixture.itemNotaFiscal(produtoEdicaoQuatroRodas1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaQuatroRodas1);

		itemNotaBoaForma1 = Fixture.itemNotaFiscal(produtoEdicaoBoaForma1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaBoaForma1);

		itemNotaBravo1 = Fixture.itemNotaFiscal(produtoEdicaoBravo1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaBravo1);

		itemNotaCaras1 = Fixture.itemNotaFiscal(produtoEdicaoCaras1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaCaras1);

		itemNotaCasaClaudia1 = Fixture.itemNotaFiscal(produtoEdicaoCasaClaudia1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaCasaClaudia1);

		itemNotaClaudia1 = Fixture.itemNotaFiscal(produtoEdicaoClaudia1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaClaudia1);

		itemNotaContigo1 = Fixture.itemNotaFiscal(produtoEdicaoContigo1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaContigo1);

		itemNotaManequim1 = Fixture.itemNotaFiscal(produtoEdicaoManequim1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaManequim1);

		itemNotaNatGeo1 = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaNatGeo1);

		itemNotaPlacar1 = Fixture.itemNotaFiscal(produtoEdicaoPlacar1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaPlacar1); 

	}

	private static void criarEstudos(Session session) {

		estudoVeja1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		session.save(estudoVeja1);

		estudoVeja2 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja2.getDataLancamentoDistribuidor(), produtoEdicaoVeja2);
		session.save(estudoVeja2);

		estudoSuper1 = Fixture.estudo(
			BigDecimal.TEN, lancamentoSuper1.getDataLancamentoDistribuidor(), produtoEdicaoSuper1);

		session.save(estudoSuper1);

		estudoCapricho1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoCapricho1.getDataLancamentoDistribuidor(), produtoEdicaoCapricho1);
		session.save(estudoCapricho1);

		estudoVeja1Atual = Fixture
				.estudo(BigDecimal.TEN, new Date(), produtoEdicaoVeja1);
		session.save(estudoVeja1Atual);

		estudoVeja1EncalheAnt = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja1EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoVeja1EncalheAnt);
		session.save(estudoVeja1EncalheAnt);

		estudoVeja2EncalheAnt = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja2EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoVeja2EncalheAnt);
		session.save(estudoVeja2EncalheAnt);

		estudoSuper1EncalheAnt = Fixture
				.estudo(BigDecimal.TEN, lancamentoSuper1EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoSuper1EncalheAnt);
		session.save(estudoSuper1EncalheAnt);

		estudoSuper2EncalheAnt = Fixture
				.estudo(BigDecimal.TEN, lancamentoSuper2EcncalheAnt.getDataLancamentoDistribuidor(), produtoEdicaoSuper2EncalheAnt);
		session.save(estudoSuper2EncalheAnt);

		estudoInfoExame1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoInfoExame1.getDataLancamentoDistribuidor(), produtoEdicaoInfoExame1);
		session.save(estudoInfoExame1);

		estudoQuatroRodas1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoQuatroRodas1.getDataLancamentoDistribuidor(), produtoEdicaoQuatroRodas1);
		session.save(estudoQuatroRodas1);

		estudoBoaForma1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoBoaForma1.getDataLancamentoDistribuidor(), produtoEdicaoBoaForma1);
		session.save(estudoBoaForma1);

		estudoBravo1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoBravo1.getDataLancamentoDistribuidor(), produtoEdicaoBravo1);
		session.save(estudoBravo1);

		estudoCaras1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoCaras1.getDataLancamentoDistribuidor(), produtoEdicaoCaras1);
		session.save(estudoCaras1);

		estudoCasaClaudia1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoCasaClaudia1.getDataLancamentoDistribuidor(), produtoEdicaoCasaClaudia1);
		session.save(estudoCasaClaudia1);

		estudoClaudia1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoClaudia1.getDataLancamentoDistribuidor(), produtoEdicaoClaudia1);
		session.save(estudoClaudia1);

		estudoContigo1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoContigo1.getDataLancamentoDistribuidor(), produtoEdicaoContigo1);
		session.save(estudoContigo1);

		estudoManequim1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoManequim1.getDataLancamentoDistribuidor(), produtoEdicaoManequim1);
		session.save(estudoManequim1);

		estudoNatGeo1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoNatGeo1.getDataLancamentoDistribuidor(), produtoEdicaoNatGeo1);
		session.save(estudoNatGeo1);

		estudoPlacar1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoPlacar1.getDataLancamentoDistribuidor(), produtoEdicaoPlacar1);
		session.save(estudoPlacar1);

	}

    private static void criarEstudosCota(Session session) {

		estudoCotaVeja1Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1, cotaJose);
		save(session,estudoCotaVeja1Joao);

		estudoCotaVeja2Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2, cotaJose);
		save(session,estudoCotaVeja2Joao);

		estudoCotaCaprichoZe = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoCapricho1, cotaMaria);
		save(session,estudoCotaCaprichoZe);

		estudoCotaSuper1Manoel = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1, cotaManoel);
		save(session,estudoCotaSuper1Manoel);

		estudoCotaManoel = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.TEN, estudoVeja1Atual, cotaManoel);
		save(session,estudoCotaManoel);

		estudoCotaVeja1JoaoEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1EncalheAnt, cotaJose);
		save(session,estudoCotaVeja1JoaoEncaljeAnt);

		estudoCotaVeja1ManoelEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1EncalheAnt, cotaManoel);
		save(session,estudoCotaVeja1ManoelEncaljeAnt);

		estudoCotaVeja1MariaEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1EncalheAnt, cotaMaria);
		save(session,estudoCotaVeja1MariaEncaljeAnt);

		estudoCotaVeja2JoaoEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2EncalheAnt, cotaJose);
		save(session,estudoCotaVeja2JoaoEncaljeAnt);

		estudoCotaVeja2ManoelEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2EncalheAnt, cotaManoel);
		save(session,estudoCotaVeja2ManoelEncaljeAnt);

		estudoCotaVeja2MariaEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2EncalheAnt, cotaMaria);
		save(session,estudoCotaVeja2MariaEncaljeAnt);

		estudoCotaSuper1JoaoEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1EncalheAnt, cotaJose);
		save(session,estudoCotaSuper1JoaoEncaljeAnt);

		estudoCotaSuper1ManoelEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1EncalheAnt, cotaManoel);
		save(session,estudoCotaSuper1ManoelEncaljeAnt);

		estudoCotaSuper1MariaEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1EncalheAnt, cotaMaria);
		save(session,estudoCotaSuper1MariaEncaljeAnt);

		estudoCotaSuper2JoaoEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper2EncalheAnt, cotaJose);
		save(session,estudoCotaVeja2JoaoEncaljeAnt);

		estudoCotaSuper2ManoelEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper2EncalheAnt, cotaManoel);
		save(session,estudoCotaSuper2ManoelEncaljeAnt);

		estudoCotaSuper2MariaEncaljeAnt = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper2EncalheAnt, cotaMaria);
		save(session,estudoCotaSuper2MariaEncaljeAnt);


		estudoJoseInfoExame1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoInfoExame1, cotaJose);
		save(session,estudoJoseInfoExame1);

		estudoJoseQuatroRodas1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoQuatroRodas1, cotaJose);
		save(session,estudoJoseQuatroRodas1);

		estudoJoseBoaForma1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoBoaForma1, cotaJose);
		save(session,estudoJoseBoaForma1);

		estudoJoseBravo1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoBravo1, cotaJose);
		save(session,estudoJoseBravo1);

		estudoJoseCaras1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoCaras1, cotaJose);
		save(session,estudoJoseCaras1);

		estudoJoseCasaClaudia1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoCasaClaudia1, cotaJose);
		save(session,estudoJoseCasaClaudia1);

		estudoJoseClaudia1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoClaudia1, cotaJose);
		save(session,estudoJoseClaudia1);

		estudoJoseContigo1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoContigo1, cotaJose);
		save(session,estudoJoseContigo1);

		estudoJoseManequim1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoManequim1, cotaJose);
		save(session,estudoJoseManequim1);

		estudoJoseNatGeo1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoNatGeo1, cotaJose);
		save(session,estudoJoseNatGeo1);

		estudoJoseEdicaoPlacar1 = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoPlacar1, cotaJose);
		save(session,estudoJoseEdicaoPlacar1);

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
						new Date(), BigDecimal.TEN,  StatusLancamento.EXPEDIDO,
						itemRecebimentoFisico,expedicao, 1);
		session.save(lancamentoVeja1);

		lancamentoVeja2 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,

						null,expedicao, 1);
		session.save(lancamentoVeja2);

		lancamentoSuper1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.EXPEDIDO,
								null,expedicao, 1);
		session.save(lancamentoSuper1);

		lancamentoCapricho1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.EXPEDIDO,
								null,expedicao, 1);
		session.save(lancamentoCapricho1);			


	}

	private static void criarLancamentos(Session session) {


		lancamentoVeja1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigDecimal.TEN,  StatusLancamento.ESTUDO_FECHADO,
						itemRecebimentoFisico, 1);
		session.save(lancamentoVeja1);

		lancamentoVeja2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.BALANCEADO,

						null, 1);
		session.save(lancamentoVeja2);

		lancamentoSuper1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.CONFIRMADO,
								null, 1);
		session.save(lancamentoSuper1);

		lancamentoCapricho1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.CONFIRMADO,
								null, 1);
		session.save(lancamentoCapricho1);


		lancamentoCocaCola = Fixture.lancamento(TipoLancamento.LANCAMENTO,cocaColaLight , 
				new Date(), new Date(), new Date(), new Date(), new BigDecimal(100), StatusLancamento.CONFIRMADO, itemCocaRecebimentoFisico, 1);
		save(session, lancamentoCocaCola);


		lancamentoVeja1EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1EncalheAnt,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),+5), new Date(),
						new Date(), BigDecimal.TEN,  StatusLancamento.EXPEDIDO,
						null, 1);
		session.save(lancamentoVeja1EcncalheAnt);

		lancamentoVeja2EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2EncalheAnt,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),+5), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,

						null, 1);
		session.save(lancamentoVeja2EcncalheAnt);


		lancamentoInfoExame1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoInfoExame1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), new BigDecimal(500), StatusLancamento.CONFIRMADO,
								itemInfoExame1, 1);
		session.save(lancamentoInfoExame1);

		lancamentoQuatroRodas1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoQuatroRodas1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoQuatroRodas1.getPeb()), new Date(),
								new Date(), new BigDecimal(1500), StatusLancamento.CONFIRMADO,
								itemQuatroRodas1, 1);
		session.save(lancamentoQuatroRodas1);

		lancamentoBoaForma1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBoaForma1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBoaForma1.getPeb()), new Date(),
								new Date(), new BigDecimal(190), StatusLancamento.CONFIRMADO,
								itemBoaForma1, 1);
		session.save(lancamentoBoaForma1);

		lancamentoBravo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBravo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBravo1.getPeb()), new Date(),
								new Date(), new BigDecimal(250), StatusLancamento.CONFIRMADO,
								itemBravo1, 1);
		session.save(lancamentoBravo1);

		lancamentoCaras1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCaras1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), new BigDecimal(250), StatusLancamento.CONFIRMADO,
								itemCaras1, 1);
		session.save(lancamentoCaras1);



		lancamentoCasaClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCasaClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCasaClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(350), StatusLancamento.CONFIRMADO,
								itemCasaClaudia1, 1);
		session.save(lancamentoCasaClaudia1);

		lancamentoClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(400), StatusLancamento.CONFIRMADO,
								itemClaudia1, 1);
		session.save(lancamentoClaudia1);


		lancamentoContigo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoContigo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoContigo1.getPeb()), new Date(),
								new Date(), new BigDecimal(185), StatusLancamento.CONFIRMADO,
								itemContigo1, 1);
		session.save(lancamentoContigo1);

		lancamentoManequim1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoManequim1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoManequim1.getPeb()), new Date(),
								new Date(), new BigDecimal(225), StatusLancamento.CONFIRMADO,
								itemManequim1, 1);
		session.save(lancamentoManequim1);

		lancamentoNatGeo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoNatGeo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoNatGeo1.getPeb()), new Date(),
								new Date(), new BigDecimal(75), StatusLancamento.CONFIRMADO,
								itemNatGeo1, 1);
		session.save(lancamentoNatGeo1);

		lancamentoPlacar1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoPlacar1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoPlacar1.getPeb()), new Date(),
								new Date(), new BigDecimal(195), StatusLancamento.CONFIRMADO,
								itemPlacar1, 1);
		session.save(lancamentoPlacar1);

		lancamentoSuper1EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1EncalheAnt,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),+5), new Date(),
						new Date(), BigDecimal.TEN,  StatusLancamento.EXPEDIDO,
						null, 1);
		session.save(lancamentoSuper1EcncalheAnt);

		lancamentoSuper2EcncalheAnt = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper2EncalheAnt,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),+5), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,

						null, 1);
		session.save(lancamentoSuper2EcncalheAnt);

	}

	private static void criarProdutosEdicao(Session session) {
		produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"ABC", 2L, produtoVeja);
		session.save(produtoEdicaoVeja1);

		produtoEdicaoVeja2 = Fixture.produtoEdicao(2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"DEF", 3L, produtoVeja);
		session.save(produtoEdicaoVeja2);

		produtoEdicaoVeja3 = Fixture.produtoEdicao(3L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"GHI", 4L, produtoVeja);
		session.save(produtoEdicaoVeja3);

		produtoEdicaoVeja4 = Fixture.produtoEdicao(4L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"JKL", 5L, produtoVeja);
		session.save(produtoEdicaoVeja4);

		produtoEdicaoSuper1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"MNO", 6L, produtoSuper);
		session.save(produtoEdicaoSuper1);

		produtoEdicaoCapricho1 = Fixture.produtoEdicao(1L, 9, 14,
				new BigDecimal(0.15), new BigDecimal(9), new BigDecimal(13.5),
				"PQR", 7L, produtoCapricho);
		session.save(produtoEdicaoCapricho1);

		produtoEdicaoInfoExame1 = Fixture.produtoEdicao(1L, 12, 30,
				new BigDecimal(0.25), new BigDecimal(11), new BigDecimal(14.5),
				"STU", 8L, produtoInfoExame);
		session.save(produtoEdicaoInfoExame1);

		produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao(1L, 7, 30,
				new BigDecimal(0.30), new BigDecimal(12.5), new BigDecimal(16.5),
				"VWX", 9L, produtoQuatroRodas);
		session.save(produtoEdicaoQuatroRodas1);

		produtoEdicaoBoaForma1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(12), new BigDecimal(15),
				"YZ1", 10L, produtoBoaForma);
		session.save(produtoEdicaoBoaForma1);

		produtoEdicaoBravo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.12), new BigDecimal(17), new BigDecimal(20),
				"YZ2", 11L, produtoBravo);
		session.save(produtoEdicaoBravo1);

		produtoEdicaoCaras1 = Fixture.produtoEdicao(1L, 15, 30,
				new BigDecimal(0.20), new BigDecimal(20), new BigDecimal(25),
				"YZ3", 12L, produtoCaras);
		session.save(produtoEdicaoCaras1);

		produtoEdicaoCasaClaudia1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(20), new BigDecimal(25),
				"YZ4", 13L, produtoCasaClaudia);
		session.save(produtoEdicaoCasaClaudia1);

		produtoEdicaoClaudia1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(10), new BigDecimal(11),
				"YZ5", 14L, produtoClaudia);
		session.save(produtoEdicaoClaudia1);

		produtoEdicaoContigo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(12), new BigDecimal(15),
				"YZ6", 15L, produtoContigo);
		session.save(produtoEdicaoContigo1);

		produtoEdicaoManequim1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(15), new BigDecimal(20),
				"YZ7", 16L, produtoManequim);
		session.save(produtoEdicaoManequim1);

		produtoEdicaoNatGeo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.15), new BigDecimal(20), new BigDecimal(23),
				"YZ8", 17L, produtoNatGeo);
		session.save(produtoEdicaoNatGeo1);

		produtoEdicaoPlacar1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(9), new BigDecimal(12),
				"YZ9", 18L, produtoPlacar);
		session.save(produtoEdicaoPlacar1);

		cocaColaLight = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(9), new BigDecimal(12),
				"WZ1", 19L, cocaCola);
		session.save(cocaColaLight);

		produtoEdicaoVeja1EncalheAnt = Fixture.produtoEdicao(5L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"WZ2", 20L, produtoVeja);
		session.save(produtoEdicaoVeja1EncalheAnt);

		produtoEdicaoVeja2EncalheAnt = Fixture.produtoEdicao(6L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"WZ3", 21L, produtoVeja);
		session.save(produtoEdicaoVeja2EncalheAnt);

		produtoEdicaoSuper1EncalheAnt = Fixture.produtoEdicao(2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"WZ4", 22L, produtoSuper);
		session.save(produtoEdicaoSuper1EncalheAnt);

		produtoEdicaoSuper2EncalheAnt = Fixture.produtoEdicao(3L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				"WZ5", 23L, produtoSuper);
		session.save(produtoEdicaoSuper2EncalheAnt);

	}

	private static void criarProdutos(Session session) {
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(editoraAbril);
		session.save(produtoVeja);

		produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
		produtoSuper.addFornecedor(fornecedorDinap);
		produtoSuper.setEditor(editoraAbril);
		session.save(produtoSuper);

		produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		produtoCapricho.setEditor(editoraAbril);
		session.save(produtoCapricho);

		produtoInfoExame = Fixture.produtoInfoExame(tipoProdutoRevista);
		produtoInfoExame.addFornecedor(fornecedorDinap);
		produtoInfoExame.setEditor(editoraAbril);
		session.save(produtoInfoExame);

		produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
		produtoQuatroRodas.addFornecedor(fornecedorDinap);
		produtoQuatroRodas.setEditor(editoraAbril);
		session.save(produtoQuatroRodas);

		produtoBoaForma = Fixture.produtoBoaForma(tipoProdutoRevista);
		produtoBoaForma.addFornecedor(fornecedorDinap);
		produtoBoaForma.setEditor(editoraAbril);
		session.save(produtoBoaForma);

		produtoBravo = Fixture.produtoBravo(tipoProdutoRevista);
		produtoBravo.addFornecedor(fornecedorDinap);
		produtoBravo.setEditor(editoraAbril);
		session.save(produtoBravo);

		produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		produtoCaras.addFornecedor(fornecedorDinap);
		produtoCaras.setEditor(editoraAbril);
		session.save(produtoCaras);

		produtoCasaClaudia = Fixture.produtoCasaClaudia(tipoProdutoRevista);
		produtoCasaClaudia.addFornecedor(fornecedorDinap);
		produtoCasaClaudia.setEditor(editoraAbril);
		session.save(produtoCasaClaudia);

		produtoClaudia = Fixture.produtoClaudia(tipoProdutoRevista);
		produtoClaudia.addFornecedor(fornecedorDinap);
		produtoClaudia.setEditor(editoraAbril);
		session.save(produtoClaudia);

		produtoContigo = Fixture.produtoContigo(tipoProdutoRevista);
		produtoContigo.addFornecedor(fornecedorDinap);
		produtoContigo.setEditor(editoraAbril);
		session.save(produtoContigo);

		produtoManequim = Fixture.produtoManequim(tipoProdutoRevista);
		produtoManequim.addFornecedor(fornecedorDinap);
		produtoManequim.setEditor(editoraAbril);
		session.save(produtoManequim);

		produtoNatGeo = Fixture.produtoNationalGeographic(tipoProdutoRevista);
		produtoNatGeo.addFornecedor(fornecedorDinap);
		produtoNatGeo.setEditor(editoraAbril);
		session.save(produtoNatGeo);

		produtoPlacar = Fixture.produtoPlacar(tipoProdutoRevista);
		produtoPlacar.addFornecedor(fornecedorDinap);
		produtoPlacar.setEditor(editoraAbril);
		session.save(produtoPlacar);

		cocaCola = Fixture.produto("564", "Coca-Cola", "Coca-Cola", PeriodicidadeProduto.MENSAL, tipoRefrigerante);
		cocaCola.addFornecedor(fornecedorAcme);
		save(session, cocaCola);
	}

	private static void criarEditores(Session session) {
		editoraAbril = Fixture.editoraAbril();
		save(session, editoraAbril);
	}

	private static void criarTiposProduto(Session session) {
		tipoProdutoRevista = Fixture.tipoRevista();
		session.save(tipoProdutoRevista);

		tipoRefrigerante = Fixture.tipoProduto("Refrigerante",GrupoProduto.OUTROS, "5644566");
		session.save(tipoRefrigerante);
	}

	private static void criarDistribuidor(Session session) {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333.333.333.333", "distrib_acme@mail.com", "99.999-9");
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

		ParametroContratoCota parametroContrato = Fixture.criarParametroContratoCota("<font color='blue'><b>CONSIDERANDO QUE:</b></font><br>"+
																					 "<br>"+"<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"+
																					 "<br>"+"<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"+
																					 "<br>"+"<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"+
																					 "<br>"+"RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"+
																					 "<br><br>"+"<font color='blue'><b>1.	OBJETO DO CONTRATO</b><br></font>"+
																					 "<br>"+"<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante."
																					 , "neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.", 30, 30);
		save(session, parametroContrato);
		
		distribuidor.setParametroContratoCota(parametroContrato);

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
				TipoEndereco.COBRANCA, "13222-020", "Rua João de Souza", 51, "Centro", "São Paulo", "SP");

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
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedorAcme);
		fornecedores.add(fornecedorDinap);
		fornecedores.add(fornecedorFc);
		cotaManoel.setFornecedores(fornecedores);
		
		save(session, cotaManoel);
		ContratoCota contrato = Fixture.criarContratoCota(cotaManoel,true,DateUtil.parseData("01/01/2012", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato);
		

		cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		save(session, cotaJose);
		ContratoCota contrato2 = Fixture.criarContratoCota(cotaJose,true,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato2);

		
		cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO,box2);
		save(session, cotaMaria);
		ContratoCota contrato3 = Fixture.criarContratoCota(cotaMaria,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato3);
		
	
		cotaGuilherme = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO,box2);
		cotaGuilherme.setSugereSuspensao(true);
		save(session, cotaGuilherme);
		ContratoCota contrato4 = Fixture.criarContratoCota(cotaGuilherme,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato4);
		
		
		cotaMurilo= Fixture.cota(22345, murilo, SituacaoCadastro.ATIVO,box2);
		cotaMurilo.setSugereSuspensao(true);
		save(session, cotaMurilo);
		ContratoCota contrato5 = Fixture.criarContratoCota(cotaMurilo,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato5);
		
		
		cotaMariana = Fixture.cota(32345, mariana, SituacaoCadastro.ATIVO,box1);
		cotaMariana.setSugereSuspensao(true);
		save(session, cotaMariana);
		ContratoCota contrato6 = Fixture.criarContratoCota(cotaMariana,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato6);
		
		
		cotaOrlando = Fixture.cota(4444, orlando, SituacaoCadastro.INATIVO,box1);
		cotaOrlando.setSugereSuspensao(false);
		save(session, cotaOrlando);
		ContratoCota contrato7 = Fixture.criarContratoCota(cotaOrlando,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato7);
		
		
		cotaJoao = Fixture.cota(9999, joao, SituacaoCadastro.ATIVO,box2);
		save(session, cotaJoao);
		ContratoCota contrato8 = Fixture.criarContratoCota(cotaJoao,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato8);
		
		
		cotaLuis = Fixture.cota(888, luis, SituacaoCadastro.ATIVO,box2);
		save(session, cotaLuis);
		ContratoCota contrato9 = Fixture.criarContratoCota(cotaLuis,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato9);

		
		cotaAcme = Fixture.cota(100000, juridicaAcme, SituacaoCadastro.ATIVO, box1);
		save(session, cotaAcme);
		ContratoCota contrato10 = Fixture.criarContratoCota(cotaAcme,false,DateUtil.parseData("01/01/2011", "dd/mm/yyyy"), DateUtil.parseData("01/01/2013", "dd/mm/yyyy"), 12, 30);
		save(session, contrato10);
		
		
		Set<FormaCobranca> formasCobranca;
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobrancaConta = 
			Fixture.parametroCobrancaCota(formasCobranca, 1, BigDecimal.TEN, cotaManoel, 1, 
					                      true, BigDecimal.TEN);
		save(session, parametroCobrancaConta);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaConta);
		formaBoleto.setPrincipal(true);
		save(session, formaBoleto);
		
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaCheque);
		ParametroCobrancaCota parametroCobrancaConta1 = 
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaJose, 1, 
											  true, BigDecimal.TEN);
		save(session, parametroCobrancaConta1);
		formaCheque.setParametroCobrancaCota(parametroCobrancaConta);
		formaCheque.setPrincipal(true);
		save(session, formaCheque);
		
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDeposito);
		ParametroCobrancaCota parametroCobrancaConta2 = 
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaMaria, 1, 
											  true, BigDecimal.TEN);
		save(session, parametroCobrancaConta2);
		formaDeposito.setParametroCobrancaCota(parametroCobrancaConta);
		formaDeposito.setPrincipal(true);
		save(session, formaDeposito);
		
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaDinheiro);
		ParametroCobrancaCota parametroCobrancaConta3 = 
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaJoao, 1, 
											  false, BigDecimal.TEN);
		save(session, parametroCobrancaConta3);
		formaDinheiro.setParametroCobrancaCota(parametroCobrancaConta);
		formaDinheiro.setPrincipal(true);
		save(session, formaDinheiro);
		
		
		
		formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaTransferenciBancaria);
		ParametroCobrancaCota parametroCobrancaConta4 = 
				Fixture.parametroCobrancaCota(formasCobranca, 2, BigDecimal.TEN, cotaLuis, 1, 
											  true, BigDecimal.TEN);
		save(session, parametroCobrancaConta4);
		formaTransferenciBancaria.setParametroCobrancaCota(parametroCobrancaConta);
		formaTransferenciBancaria.setPrincipal(true);
		save(session, formaTransferenciBancaria);
		

	}

	private static void criarDistribuicaoCota(Session session) {
		
		ParametroDistribuicaoCota parametroGuilherme = 	Fixture.criarParametroDistribuidor(
				100, "Joao da Silva", tipoEntregador, "Muito importante isso aeh!", 
				true, true, true, true, true, true, true, true, true, true);
		
		ParametroDistribuicaoCota parametroJoao = 	Fixture.criarParametroDistribuidor(
				120, "Maria da Silva", tipoEntregaEmBanca, "Muito importante isso aeh também!", 
				false, false, false, false, false, false, false, false, false, false);
				
		cotaJoao.setParametroDistribuicao(parametroJoao);
		cotaGuilherme.setParametroDistribuicao(parametroGuilherme);
		
		save(session, cotaJoao, cotaGuilherme);
	}
	
	private static void criarFornecedores(Session session) {
		fornecedorAcme = Fixture.fornecedorAcme(tipoFornecedorOutros);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorFc = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		save(session, fornecedorAcme, fornecedorDinap, fornecedorFc);
	}
	
	private static void criarUsuarios(Session session) {
		usuarioJoao = Fixture.usuarioJoao();
		session.save(usuarioJoao);
	}

	private static void criarTiposNotaFiscal(Session session) {
		tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();
		session.save(tipoNotaFiscalRecebimento);
	}

	private static void criarCFOP(Session session) {
		cfop5102 = Fixture.cfop5102();
		session.save(cfop5102);
	}

	private static void criarTiposMovimento(Session session) {
		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		tipoMovimentoSobraEm = Fixture.tipoMovimentoSobraEm();
		tipoMovimentoSobraDe = Fixture.tipoMovimentoSobraDe();
		tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();


		tipoMovimentoVendaEncalhe = Fixture.tipoMovimentoVendaEncalhe();

		tipoMovimentoFinanceiroCompraEncalhe = Fixture.tipoMovimentoFinanceiroCompraEncalhe();

		save(session, tipoMovimentoVendaEncalhe,tipoMovimentoFinanceiroCompraEncalhe);


		tipoMovimentoSuplementarCotaAusente = Fixture.tipoMovimentoSuplementarCotaAusente();

		tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();

		tipoMovimentoEstornoCotaAusente = Fixture.tipoMovimentoEstornoCotaAusente();

		tipoMovimentoReparteCotaAusente = Fixture.tipoMovimentoReparteCotaAusente();
		
		tipoMovimentoRestautacaoReparteCotaAusente = Fixture.tipoMovimentoRestauracaoReparteCotaAusente();

		save(session,tipoMovimentoReparteCotaAusente,tipoMovimentoRestautacaoReparteCotaAusente);
		
		tipoMovimentoFinanceiroCredito = Fixture.tipoMovimentoFinanceiroCredito();
		tipoMovimentoFinanceiroDebito = Fixture.tipoMovimentoFinanceiroDebito();
		tipoMovimentoFinanceiroRecebimentoReparte = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		tipoMovimentoFinanceiroJuros = Fixture.tipoMovimentoFinanceiroJuros();
		tipoMovimentoFinanceiroMulta = Fixture.tipoMovimentoFinanceiroMulta();
		tipoMovimentoFinanceiroEnvioEncalhe = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();

		tipoMovimentoEnvioJornaleiro = Fixture.tipoMovimentoEnvioJornaleiro();

		save(session, tipoMovimentoEnvioJornaleiro, 
				tipoMovimentoEstornoCotaAusente);

		tipoMovimentoFinanceiroCredito.setAprovacaoAutomatica(false);
		tipoMovimentoFinanceiroDebito.setAprovacaoAutomatica(false);

		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe, tipoMovimentoSuplementarCotaAusente,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte,
				tipoMovimentoFinanceiroCredito, tipoMovimentoFinanceiroDebito,
				tipoMovimentoEnvioEncalhe, tipoMovimentoFinanceiroRecebimentoReparte,
				tipoMovimentoFinanceiroJuros, tipoMovimentoFinanceiroMulta,
				tipoMovimentoFinanceiroEnvioEncalhe, tipoMovimentoSuplementarCotaAusente);

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

		DistribuicaoFornecedor dinapQuartaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);

		DistribuicaoFornecedor fcQuartaRecolhimento = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO, distribuidor);

		save(session, dinapQuartaRecolhimento, fcQuartaRecolhimento);
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
		
		DistribuicaoDistribuidor distribuicaoDistribuidorQuinta =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.QUINTA_FEIRA,
											 OperacaoDistribuidor.DISTRIBUICAO);
		
		save(session, recolhimentoDistribuidorTerca, recolhimentoDistribuidorQuinta,
					  distribuicaoDistribuidorTerca, distribuicaoDistribuidorQuinta);
	}
	
	private static void gerarCargaDiferencaEstoque(Session session,
												   int quantidadeRegistros,
												   ProdutoEdicao produtoEdicao, 
												   TipoMovimentoEstoque tipoMovimento, 
												   Usuario usuario,
												   EstoqueProduto estoqueProduto,
												   TipoDiferenca tipoDiferenca) {

		for (int i = 1; i <= quantidadeRegistros; i++) {

			MovimentoEstoque movimentoEstoqueDiferenca = 
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.APROVADO, null);

			session.save(movimentoEstoqueDiferenca);

			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.PENDENTE, null, movimentoEstoqueDiferenca, true);

			session.save(diferenca);
		}

		for (int i = 1; i <= quantidadeRegistros; i++) {

			MovimentoEstoque movimentoEstoqueDiferenca = 
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.PENDENTE, null);

			session.save(movimentoEstoqueDiferenca);

			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);

			session.save(diferenca);
		}
	}


	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
			session.flush();
		}
	}


	/**
	 * Gera massa de dados para o teste de Resumo de Expedicao agrupadas por produto
	 * @param session
	 */
	private static void carregarDadosParaResumoExpedicao(Session session){

		TipoProduto tipoRevista = Fixture.tipoRevista();
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
					"00.000.000/0001-00", "000.000.000.000", "acme@mail.com", "99.999-9");
			session.save(juridica);

			TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
			session.save(tipoFornecedorPublicacao);

			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao);
			session.save(fornecedor);

			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista);
			produto.addFornecedor(fornecedor);
			session.save(produto); 

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), "ZZZ", 24L, produto);	
			session.save(produtoEdicao);


			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			session.save(tipoNotaFiscal);


			List<ItemRecebimentoFisico> listaRecebimentos = new ArrayList<ItemRecebimentoFisico>() ;

			EstoqueProduto estoque  =  Fixture.estoqueProduto(produtoEdicao, BigDecimal.ZERO);
			session.save(estoque);

			for(int x= 1; x< 3 ;x++){

				NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
						.notaFiscalEntradaFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
								usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
				session.save(notaFiscalFornecedor);

				ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
						produtoEdicao, usuario, notaFiscalFornecedor, 
						Fixture.criarData(23, Calendar.FEBRUARY, 2012), new Date(),TipoLancamento.LANCAMENTO,
						new BigDecimal(i));					
				session.save(itemNotaFiscal);


				RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
					notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
				session.save(recebimentoFisico);

				ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
						itemNotaFiscal, recebimentoFisico, new BigDecimal(i+x));
				session.save(itemFisico);


				MovimentoEstoque movimentoEstoque  = 
					Fixture.movimentoEstoque(itemFisico, produtoEdicao, tipoMovimentoFaltDe, usuario,
						estoque, new Date(), new BigDecimal(1),
						StatusAprovacao.APROVADO, "Aprovado");

				session.save(movimentoEstoque);

				if(indDiferenca > 5){


					Diferenca diferenca = Fixture.diferenca(new BigDecimal(10), usuario, produtoEdicao, TipoDiferenca.SOBRA_DE, StatusConfirmacao.CONFIRMADO, itemFisico, movimentoEstoque, true);
					session.save(diferenca);

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
					new BigDecimal(100), 
					StatusLancamento.EXPEDIDO, 
					listaRecebimentos, 1);
			lancamento.setReparte(new BigDecimal(10));
			lancamento.setExpedicao(expedicao);
			session.save(lancamento);

			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(new BigDecimal(i));
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
		criarProdutos(session);
		criarProdutosEdicao(session);
		criarCFOP(session);
		criarTiposMovimento(session);
		criarTiposNotaFiscal(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarMovimentosEstoque(session);
		criarLancamentosExpedidos(session);
		criarEstudos(session);
		criarMovimentosEstoqueCota(session);
		criarEstudosCota(session);

		MovimentoEstoque movimentoEstoqueDiferenca =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca);

		MovimentoEstoque movimentoEstoqueDiferenca2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(2),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca2);

		MovimentoEstoque movimentoEstoqueDiferenca3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(3),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca3);

		MovimentoEstoque movimentoEstoqueDiferenca4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(4),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca4);


		Diferenca diferenca =
			Fixture.diferenca(new BigDecimal(1), usuarioJoao, produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
		session.save(diferenca);

		Diferenca diferenca2 =
			Fixture.diferenca(new BigDecimal(2), usuarioJoao, produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
							  StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca2, true);
		session.save(diferenca2);

		Diferenca diferenca3 =
			Fixture.diferenca(new BigDecimal(3), usuarioJoao, produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca3, true);
		session.save(diferenca3);

		Diferenca diferenca4 =
			Fixture.diferenca(new BigDecimal(4), usuarioJoao, produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
					          StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca4, true);
		session.save(diferenca4);

		// Fim dos inserts na tabela DIFERENCA

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja1, tipoMovimentoFaltaEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja3, tipoMovimentoSobraDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE);

		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja4, tipoMovimentoSobraEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM);

		RateioDiferenca rateioDiferencaCotaManoel = Fixture.rateioDiferenca(new BigDecimal(10), cotaManoel, diferenca3, estudoCotaSuper1Manoel);
		session.save(rateioDiferencaCotaManoel);

		RateioDiferenca rateioDiferencaJose = Fixture.rateioDiferenca(new BigDecimal(10), cotaJose, diferenca,estudoCotaVeja2Joao);
		session.save(rateioDiferencaJose);

	}


	private static void criarCarteira(Session session){
		carteiraRegistrada = Fixture.carteira(30, TipoRegistroCobranca.REGISTRADA);

		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);

		save(session,carteiraRegistrada,carteiraSemRegistro);
	}


	private static void criarBox(Session session){

		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		session.save(box1);

		box2 = Fixture.criarBox("Box-2", "BX-002", TipoBox.LANCAMENTO);
		session.save(box2);

		box300Reparte = Fixture.boxReparte300();
		session.save(box300Reparte);
		
		
		
		for (int i = 3; i < 100; i++) {
			session.save(Fixture.criarBox("Box-"+i, "BX-"+i, TipoBox.LANCAMENTO));
		}
	}


	private static void criarPessoas(Session session){
		juridicaAcme = Fixture.pessoaJuridica("Acme",
				"10000000000100", "000.000.000.000", "sys.discover@gmail.com", "99.999-9");
		juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11111111000111", "111.111.111.111", "sys.discover@gmail.com", "99.999-9");
		juridicaFc = Fixture.pessoaJuridica("FC",
				"22222222000122", "222.222.222.222", "sys.discover@gmail.com", "99.999-9");
		juridicaValida = Fixture.pessoaJuridica("Juridica Valida",
				"93081738000101", "333.333.333.333", "sys.discover@gmail.com", "99.999-9");

		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva");

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

		save(session, juridicaAcme, juridicaDinap, juridicaFc, juridicaValida,manoel,jose,maria,
				guilherme,murilo,mariana,orlando,luis,joao);

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

		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6, 
				      boleto7, boleto8);	

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
		Feriado feriadoIndependencia =

				Fixture.feriado(DateUtil.parseDataPTBR("07/09/2012"), "Independência do Brasil");
		save(session, feriadoIndependencia);

		Feriado feriadoProclamacao =
				Fixture.feriado(DateUtil.parseDataPTBR("15/11/2012"), "Proclamação da República");

		save(session, feriadoProclamacao);
	}

	private static void criarEnderecoCotaPF(Session session) {
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", 50, "Centro", "Mococa", "SP");

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cotaManoel);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);

		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cotaMaria);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(false);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);

		Endereco enderecoLuis = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");


		EnderecoCota enderecoCotaLuis = new EnderecoCota();
		enderecoCotaLuis.setCota(cotaLuis);
		enderecoCotaLuis.setEndereco(enderecoLuis);
		enderecoCotaLuis.setPrincipal(false);
		enderecoCotaLuis.setTipoEndereco(TipoEndereco.COBRANCA);

		Endereco enderecoJoao = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");


		EnderecoCota enderecoCotaJoao = new EnderecoCota();
		enderecoCotaJoao.setCota(cotaJoao);
		enderecoCotaJoao.setEndereco(enderecoJoao);
		enderecoCotaJoao.setPrincipal(false);
		enderecoCotaJoao.setTipoEndereco(TipoEndereco.COBRANCA);

		save(session, endereco, enderecoCota, endereco2,enderecoCota2,enderecoLuis,enderecoCotaLuis,enderecoJoao,enderecoCotaJoao);
	}

	private static void dadosExpedicao(Session session) {

		Box box300Reparte = Fixture.boxReparte300();
		save(session,box300Reparte);


		TipoProduto tipoRevista = Fixture.tipoRevista();
		save(session,tipoRevista);

		CFOP cfop = Fixture.cfop5102();
		save(session,cfop);

		Usuario usuario = Fixture.usuarioJoao();
		save(session,usuario);

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedorPublicacao);

		for(Integer i=1000;i<1050; i++) {

			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"30.000.000/0001-00", "000.000.000.000", "acme@mail.com", "99.999-9");
			save(session,juridica);

			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao);
			save(session,fornecedor);

			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista);
			produto.addFornecedor(fornecedor);
			save(session,produto); 

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), "ZZ2", 25L, produto);	
			save(session,produtoEdicao);


			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			save(session,tipoNotaFiscal);

			NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
							usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
			save(session,notaFiscalFornecedor);

			ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
					produtoEdicao, usuario, notaFiscalFornecedor, 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					DateUtil.adicionarDias(Fixture.criarData(23, Calendar.FEBRUARY, 2012), 7),
					TipoLancamento.LANCAMENTO,
					new BigDecimal(i));					
			save(session,itemNotaFiscal);

			RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			save(session,recebimentoFisico);


			ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
					itemNotaFiscal, recebimentoFisico, new BigDecimal(i));
			save(session,itemFisico);

			Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					new BigDecimal(100), 
					StatusLancamento.CONFIRMADO, 
					itemFisico, 1);
			lancamento.setReparte(new BigDecimal(10));
			save(session,lancamento);

			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(new BigDecimal(10));
			save(session,estudo);

			Pessoa pessoa = Fixture.pessoaJuridica("razaoS"+i, "CNPK" + i, "ie"+i, "email"+i,"99.999-9");
			Cota cota = Fixture.cota(i, pessoa, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(3), new BigDecimal(3), 
					estudo, cota);
			save(session,pessoa,cota,estudoCota);		

			Pessoa pessoa2 = Fixture.pessoaJuridica("razaoS2"+i, "CNPK" + i, "ie"+i, "email"+i, "99.999-9");
			Cota cota2 = Fixture.cota(i, pessoa2, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota2 = Fixture.estudoCota(new BigDecimal(7), new BigDecimal(7), 
					estudo, cota2);
			save(session, pessoa2,cota2,estudoCota2);		


			TipoMovimento tipoMovimento = Fixture.tipoMovimentoRecebimentoReparte();	

			TipoMovimento tipoMovimento2 = Fixture.tipoMovimentoEnvioJornaleiro();
			save(session,tipoMovimento,tipoMovimento2);
		}
	}

	private static void criarParametroEmail(Session session){
		save(session, Fixture.criarParametrosEmail());
	}

	private static void criarBanco(Session session) {

		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
							  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);

		bancoITAU = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  12345L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_ITAU", "184", BigDecimal.ZERO, BigDecimal.ZERO);

		bancoDOBRASIL = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_DO_BRASIL", "001", BigDecimal.ZERO, BigDecimal.ZERO);

		bancoBRADESCO = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_BRADESCO", "065", BigDecimal.ZERO, BigDecimal.ZERO);

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
				produtoEdicaoVeja1, cotaJose, new BigDecimal(10), BigDecimal.ZERO);
		save(session, estoqueProdutoCota);

		EstoqueProdutoCota estoqueProdutoCota2 = Fixture.estoqueProdutoCota(
				produtoEdicaoBravo1, cotaJose, new BigDecimal(10.77), BigDecimal.ZERO);
		save(session, estoqueProdutoCota2);

		EstoqueProdutoCota estoqueProdutoCota3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota3);

		EstoqueProdutoCota estoqueProdutoCota4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaMaria, new BigDecimal(186), BigDecimal.ZERO);
		save(session, estoqueProdutoCota4);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);

		MovimentoEstoqueCota mec2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(28), cotaMaria, StatusAprovacao.APROVADO, "Aprovado");
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

		Produto produto91 = Fixture.produto("00091", "Produto 91", "Produto 91", periodicidade, tipoProdutoRevista);
		Produto produto92 = Fixture.produto("00092", "Produto 92", "Produto 92", periodicidade, tipoProdutoRevista);
		Produto produto93 = Fixture.produto("00093", "Produto 93", "Produto 93", periodicidade, tipoProdutoRevista);

		produto91.addFornecedor(fornecedorDinap);
		produto92.addFornecedor(fornecedorDinap);
		produto93.addFornecedor(fornecedorDinap);

		save(session, produto91, produto92, produto93);

		produtoEdicao91 = Fixture.produtoEdicao(91L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ZZ3", 26L, produto91);

		produtoEdicao91.setDesconto(BigDecimal.ZERO);


		produtoEdicao92 = Fixture.produtoEdicao(92L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), "ZZ4", 27L, produto92);
		produtoEdicao92.setDesconto(BigDecimal.ONE);


		produtoEdicao93 = Fixture.produtoEdicao(93L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), "ZZ5", 28L, produto93);
		produtoEdicao93.setDesconto(BigDecimal.ONE);


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
					fornecedorDinap.getJuridica(),
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
							new BigDecimal(50), 
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
							fornecedorDinap.getJuridica(),
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
					new BigDecimal(50), 
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
					fornecedorDinap.getJuridica(),
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
					BigDecimal.TEN,
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

		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista);

		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);

		save(session, produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao(84L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "EZ7", 29L, produtoCE);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);


		produtoEdicaoCE_2 = Fixture.produtoEdicao(85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), "EZ8", 30L, produtoCE_2);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);


		produtoEdicaoCE_3 = Fixture.produtoEdicao(86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), "EZ8", 31L, produtoCE_3);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);


		save(session, produtoEdicaoCE, produtoEdicaoCE_2, produtoEdicaoCE_3);

		NotaFiscalEntradaFornecedor notaFiscalProdutoCE = 
				Fixture.notaFiscalEntradaFornecedor(
						cfop5102, 
						fornecedorFc.getJuridica(), 
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
						new BigDecimal(50));

		save(session, itemNotaFiscalProdutoCE);


		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_2 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_2, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));

		save(session, itemNotaFiscalProdutoCE_2);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_3 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_3, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));

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
						new BigDecimal(50));

		save(session, itemRecebimentoFisicoProdutoCE);

		itemRecebimentoFisicoProdutoCE_2 = 
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_2, 
						recebimentoFisicoProdutoCE, 
						new BigDecimal(50));

		save(session, itemRecebimentoFisicoProdutoCE_2);

		itemRecebimentoFisicoProdutoCE_3 = 
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_3, 
						recebimentoFisicoProdutoCE, 
						new BigDecimal(50));

		save(session, itemRecebimentoFisicoProdutoCE_3);


		lancamentoRevistaCE = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE, 1);

		lancamentoRevistaCE.getRecebimentos().add(itemRecebimentoFisicoProdutoCE);


		Estudo estudo = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE);

		save(session, lancamentoRevistaCE, estudo);


		lancamentoRevistaCE_2 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE_2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE_2, 1);

		lancamentoRevistaCE_2.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_2);


		Estudo estudo_2 = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE_2);

		save(session, lancamentoRevistaCE_2, estudo_2);


		lancamentoRevistaCE_3 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE_3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE_3, 1);

		lancamentoRevistaCE_3.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_3);


		Estudo estudo_3 = Fixture.estudo(new BigDecimal(100),
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
				produtoEdicaoCE, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe);


		EstoqueProdutoCota estoqueProdutoCotaJohny_2 = 
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_2, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny_2);

		ChamadaEncalhe chamadaEncalhe_2 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE_2, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe_2);

		EstoqueProdutoCota estoqueProdutoCotaJohny_3 = 
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_3, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny_3);

		ChamadaEncalhe chamadaEncalhe_3 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE_3, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(session, chamadaEncalhe_3);


		CFOP cfop1209 = Fixture.cfop1209();
		save(session, cfop1209);

		CFOP cfop1210 = Fixture.cfop1210();
		save(session, cfop1210);

		ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscal = 
				Fixture.parametroEmissaoNotaFiscal(
						cfop1209, 
						cfop1210, 
						GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR, 
						"0001");
		save(session,parametroEmissaoNotaFiscal);

		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = Fixture.controleNumeracaoNotaFiscal(1L, "0001");
		save(session, controleNumeracaoNotaFiscal);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalDevolucao();
		save(session, tipoNotaFiscal);

		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, 
				false, 
				cotaJohnyConsultaEncalhe, 
				BigDecimal.TEN);
		save(session, chamadaEncalheCota);
		
		ChamadaEncalheCota chamadaEncalheCota_2 = Fixture.chamadaEncalheCota(
				chamadaEncalhe_2, 
				false, 
				cotaJohnyConsultaEncalhe, 
				BigDecimal.TEN);
		save(session, chamadaEncalheCota_2);
		
		ChamadaEncalheCota chamadaEncalheCota_3 = Fixture.chamadaEncalheCota(
				chamadaEncalhe_3, 
				false, 
				cotaJohnyConsultaEncalhe, 
				BigDecimal.TEN);
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
				StatusOperacao.CONCLUIDO);
		
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
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(
				lancamentoRevistaCE, mec, chamadaEncalheCota, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(2, Calendar.MARCH, 2012), 
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny,
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE, mec, chamadaEncalheCota, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny,
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE, mec, chamadaEncalheCota, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(34), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_2, mec, chamadaEncalheCota_2, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(45), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);
		
		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_2, mec, chamadaEncalheCota_2, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(4, Calendar.MARCH, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(65), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_2, mec, chamadaEncalheCota_2, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(31), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(41), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(5, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(6, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(7, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);

		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(7, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");

		save(session, mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(lancamentoRevistaCE_3, mec, chamadaEncalheCota_3, controleConferenciaEncalheCota);
		save(session, conferenciaEncalhe);
		
	}


	private static void criarMovimentosFinanceiroCota(Session session) {

		movimentoFinanceiroCota1 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota1),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota2 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota2),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota3 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota3),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota4 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota4),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota5 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota5),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota6 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota6),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota7 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota7),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota8 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota8),
			StatusAprovacao.PENDENTE, new Date(), true);

		movimentoFinanceiroCota9 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota9),
			StatusAprovacao.PENDENTE, new Date(), true);

		save(session, movimentoFinanceiroCota1, movimentoFinanceiroCota2,
					  movimentoFinanceiroCota3, movimentoFinanceiroCota4,
					  movimentoFinanceiroCota5, movimentoFinanceiroCota6,
					  movimentoFinanceiroCota7, movimentoFinanceiroCota8,
					  movimentoFinanceiroCota9);

		MovimentoFinanceiroCota movimentoFinanceiroCota10 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 10), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota11 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 20), true);		

		MovimentoFinanceiroCota movimentoFinanceiroCota12 = 
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 30), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota13 =
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

		save(session, movimentoFinanceiroCota10, movimentoFinanceiroCota11,
				  	  movimentoFinanceiroCota12, movimentoFinanceiroCota13,
				  	  movimentoFinanceiroDebito1, movimentoFinanceiroDebito2,
				  	  movimentoFinanceiroCredito1, movimentoFinanceiroCredito2,
				  	  movimentoFinanceiroJuros1, movimentoFinanceiroJuros2,
				  	  movimentoFinanceiroMulta1, movimentoFinanceiroMulta2,
				  	  movimentoFinanceiroEnvioEncalhe1, movimentoFinanceiroEnvioEncalhe2);
	}

	private static void criarNotasFiscaisEntradaFornecedor(Session session) {

		for (int i = 0; i < 50; i++) {

			Calendar calendar = Calendar.getInstance();

			notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
							usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);

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

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "13131313", "Rua Marechal deodoro", 50, "Centro", "Mococa", "SP");

		EnderecoEntregador enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.COMERCIAL);

		Telefone telefone = Fixture.telefone("19", "36560000", null);

		TelefoneEntregador telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.COMERCIAL);

		save(session, endereco, enderecoEntregador, telefone, telefoneEntregador);

		jose.setApelido("Zezinho");

		entregador = Fixture.criarEntregador(
				345L, false, new Date(), 
				null, jose, false, null);
		save(session, jose, entregador);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Avenida Brasil", 10, "Centro", "Ribeirão Preto", "SP");

		enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.COBRANCA);

		telefone = Fixture.telefone("19", "36112887", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.CELULAR);

		save(session, endereco, enderecoEntregador, telefone, telefoneEntregador);

		maria.setApelido("Mariazinha");

		save(session, maria);

		entregador = Fixture.criarEntregador(
				456L, false, new Date(), 
				null, maria, false, null);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Itaquera", 10, "Centro", "São Paulo", "SP");

		enderecoEntregador = Fixture.enderecoEntregador(entregador, endereco, true, TipoEndereco.RESIDENCIAL);

		telefone = Fixture.telefone("11", "31053333", null);

		telefoneEntregador = Fixture.telefoneEntregador(entregador, true, telefone, TipoTelefone.CELULAR);

		save(session, entregador, endereco, enderecoEntregador, telefone, telefoneEntregador);
	}
}
