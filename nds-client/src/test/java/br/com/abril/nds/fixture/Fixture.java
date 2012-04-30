package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
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
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao.TipoOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
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
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
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
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;

public class Fixture {

	public static PessoaJuridica juridicaFC() {
		return pessoaJuridica("FC", "00.000.000/0001-00", "000.000.000.000",
				"fc@mail.com");
	}
	
	public static CotaAusente cotaAusenteAtivo(){
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		Pessoa manoel = Fixture.pessoaFisica("123.456.789-00",
				"sys.discover@gmail.com", "Manoel da Silva");	
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		return cotaAusente(criarData(10, 03, 2012), true, cotaManoel);
		
	}

	public static PessoaJuridica juridicaDinap() {		
		return pessoaJuridica("Dinap", "11.111.111/0001-00", "111.111.111.111",
				"dinap@mail.com");
	}
	
	public static PessoaJuridica juridicaAcme() {
		return pessoaJuridica("ACME", "11.222.333/0001-00", "111.222.333.444",
				"acme@mail.com");
	}

	public static Fornecedor fornecedorFC(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaFC(), SituacaoCadastro.ATIVO, true, tipoFornecedor);
	}

	public static Fornecedor fornecedorDinap(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaDinap(), SituacaoCadastro.ATIVO, true, tipoFornecedor);
	}
	
	public static Fornecedor fornecedorAcme(TipoFornecedor tipoFornecedor) {
		return fornecedor(juridicaAcme(), SituacaoCadastro.ATIVO, false, tipoFornecedor);
	}

	public static Produto produtoVeja(TipoProduto tipoProduto) {
		return produto("1", "Veja", "Veja", PeriodicidadeProduto.SEMANAL,
				tipoProduto);
	}

	public static Produto produtoQuatroRodas(TipoProduto tipoProduto) {
		return produto("2", "Quatro Rodas", "Quatro Rodas",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}

	public static Produto produtoInfoExame(TipoProduto tipoProduto) {
		return produto("3", "Info Exame", "Info Exame",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}

	public static Produto produtoCapricho(TipoProduto tipoProduto) {
		return produto("4", "Capricho", "Capricho",
				PeriodicidadeProduto.QUINZENAL, tipoProduto);
	}
	
	public static Produto produtoSuperInteressante(TipoProduto tipoProduto) {
		return produto("5",
				"Superinteressante", "Superinteressante",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoBoaForma(TipoProduto tipoProduto) {
		return produto("6", "Boa Forma", "Boa Forma",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoBravo(TipoProduto tipoProduto) {
		return produto("7", "Bravo", "Bravo",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoContigo(TipoProduto tipoProduto) {
		return produto("8", "Contigo", "Contigo",
				PeriodicidadeProduto.QUINZENAL, tipoProduto);
	}
	
	public static Produto produtoCaras(TipoProduto tipoProduto) {
		return produto("9", "Caras", "Caras",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoClaudia(TipoProduto tipoProduto) {
		return produto("10", "Claudia", "Claudia",
				PeriodicidadeProduto.SEMANAL, tipoProduto);
	}
	
	public static Produto produtoCasaClaudia(TipoProduto tipoProduto) {
		return produto("11", "Casa Claudia", "Casa Claudia",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoManequim(TipoProduto tipoProduto) {
		return produto("12", "Manequim", "Manequim",
				PeriodicidadeProduto.SEMANAL, tipoProduto);
	}
	
	public static Produto produtoNationalGeographic(TipoProduto tipoProduto) {
		return produto("13", "National Geographic", "National Geographic",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoPlacar(TipoProduto tipoProduto) {
		return produto("14", "Placar", "Placar",
				PeriodicidadeProduto.MENSAL, tipoProduto);
	}
	
	public static Produto produtoCromoReiLeao(TipoProduto tipoProduto) {
		return produto("15", "Cromo Rei Leao", "Cromo Rei Leao",
				PeriodicidadeProduto.SEMANAL, tipoProduto);
	}

	public static TipoProduto tipoRevista() {
		return tipoProduto("Revistas", GrupoProduto.REVISTA, "99000642");
	}
	
	public static TipoProduto tipoCromo() {
		return tipoProduto("Cromos", GrupoProduto.CROMO, "1230004560");
	}
	
	public static TipoFornecedor tipoFornecedorPublicacao() {
		return tipoFornecedor("Fornecedor Publicação", GrupoFornecedor.PUBLICACAO);
	}
	
	public static TipoFornecedor tipoFornecedorOutros() {
		return tipoFornecedor("Fornecedor Outros", GrupoFornecedor.OUTROS);
	}
	
	public static Box boxReparte300() {
		return criarBox("300", "Box 300", TipoBox.LANCAMENTO);
	}
	
	public static Banco hsbc(Carteira carteira) {
		return Fixture.banco(10L, true, carteira, "1010",
				  123456L, "1", "1", "Sem instruções", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	public static Editor editoraAbril() {
		return criarEditor("Editora Abril", 10L);
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

	public static PessoaJuridica pessoaJuridica(String razaoSocial,
			String cnpj, String ie, String email) {
		PessoaJuridica juridica = new PessoaJuridica();
		juridica.setRazaoSocial(razaoSocial);
		juridica.setNomeFantasia(razaoSocial);
		juridica.setCnpj(cnpj);
		juridica.setInscricaoEstadual(ie);
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
			TipoFornecedor tipo) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setJuridica(juridica);
		fornecedor.setSituacaoCadastro(situacaoCadastro);
		fornecedor.setPermiteBalanceamento(permiteBalanceamento);
		fornecedor.setTipoFornecedor(tipo);
		return fornecedor;
	}

	public static TipoProduto tipoProduto(String descricao, GrupoProduto grupo,
			String ncm) {
		TipoProduto tipoProduto = new TipoProduto();
		tipoProduto.setDescricao(descricao);
		tipoProduto.setGrupoProduto(grupo);
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
			PeriodicidadeProduto periodicidade, TipoProduto tipo) {
		Produto produto = new Produto();
		produto.setCodigo(codigo);
		produto.setDescricao(descricao);
		produto.setNome(nome);
		produto.setPeriodicidade(periodicidade);
		produto.setTipoProduto(tipo);
		return produto;
	}

	public static ProdutoEdicao produtoEdicao(Long numeroEdicao,
			int pacotePadrao, int peb, BigDecimal peso, BigDecimal precoCusto,
			BigDecimal precoVenda, Produto produto) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		produtoEdicao.setPacotePadrao(pacotePadrao);
		produtoEdicao.setPeb(peb);
		produtoEdicao.setPeso(peso);
		produtoEdicao.setPrecoCusto(precoCusto);
		produtoEdicao.setPrecoVenda(precoVenda);
		produtoEdicao.setProduto(produto);
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		return produtoEdicao;
	}

	public static Lancamento lancamento(TipoLancamento tipoLancamento,
			ProdutoEdicao produtoEdicao, Date dlp, Date drp, Date dataCriacao,
			Date dataStatus, BigDecimal reparte,
			StatusLancamento statusLancamento, ItemRecebimentoFisico recebimento) {
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
		
		if (recebimento != null) {
			lancamento.addRecebimento(recebimento);
		}
		return lancamento;
	}
	
	public static ChamadaEncalhe chamadaEncalhe(
			Date inicioRecolhimento, 
			Date finalRecolhimento, 
			ProdutoEdicao produtoEdicao, 
			TipoChamadaEncalhe tipoChamadaEncalhe) {
		
		ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setInicioRecolhimento(inicioRecolhimento);
		chamadaEncalhe.setFinalRecolhimento(finalRecolhimento);
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
			BigDecimal qtde) {
		
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
			Date dataStatus, BigDecimal reparte,
			StatusLancamento statusLancamento, ItemRecebimentoFisico recebimento,Expedicao expedicao) {
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
		if (recebimento != null) {
			lancamento.addRecebimento(recebimento);
		}
		return lancamento;
	}

	public static Distribuidor distribuidor(PessoaJuridica juridica,
			Date dataOperacao, PoliticaCobranca politicaCobranca) {
		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setDataOperacao(dataOperacao);
		distribuidor.setJuridica(juridica);
		distribuidor.setPoliticaCobranca(politicaCobranca);
		distribuidor.setCapacidadeDistribuicao(10000);
		distribuidor.setCapacidadeRecolhimento(10000L);
		return distribuidor;
	}

	public static DistribuicaoFornecedor distribuicaoFornecedor(
			Distribuidor distribuidor, Fornecedor fornecedor,
			DiaSemana diaSemana, OperacaoDistribuidor operacaoDistribuidor) {
		DistribuicaoFornecedor df = new DistribuicaoFornecedor();
		df.setDistribuidor(distribuidor);
		df.setFornecedor(fornecedor);
		df.setDiaSemana(diaSemana);
		df.setOperacaoDistribuidor(operacaoDistribuidor);
		distribuidor.getDiasDistribuicao().add(df);
		return df;
	}

	public static Cota cota(Integer numeroCota, Pessoa pessoa,
			SituacaoCadastro situacaoCadastro, Box box) {
		Cota cota = new Cota();
		cota.setNumeroCota(numeroCota);
		cota.setPessoa(pessoa);
		cota.setSituacaoCadastro(situacaoCadastro);
		cota.setBox(box);
		ContratoCota contratoCota = criarContratoCota(cota, true);
		cota.setContratoCota(contratoCota);
		cota.setInicioAtividade(new Date());
		return cota;
	}

	public static Estudo estudo(BigDecimal qtdReparte, Date data,
			ProdutoEdicao produtoEdicao) {

		Estudo estudo = new Estudo();

		estudo.setQtdeReparte(qtdReparte);

		estudo.setDataLancamento(data);

		estudo.setProdutoEdicao(produtoEdicao);

		return estudo;
	}

	public static EstudoCota estudoCota(BigDecimal qtdePrevista,
			BigDecimal qtdeEfetiva, Estudo estudo, Cota cota) {

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
		usuario.setSenha("ABC123");
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
	
	public static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito() {
		TipoMovimentoFinanceiro tipoMovimento = new TipoMovimentoFinanceiro();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("Débito");
		tipoMovimento.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
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
			Usuario usuario, NotaFiscalEntrada notaFiscal, Date dataLancamento, Date dataRecolhimento, TipoLancamento tipoLancamento, BigDecimal qtde) {
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

	public static ItemNotaFiscalSaida itemNotaFiscalSaida(
			ProdutoEdicao produtoEdicao,
			NotaFiscalSaida notaFiscal, 
			BigDecimal qtde) {
		
		ItemNotaFiscalSaida itemNotaFiscal = new ItemNotaFiscalSaida();
		
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQtde(qtde);
		itemNotaFiscal.setNotaFiscal(notaFiscal);
		
		return itemNotaFiscal;
		
	}
	
	public static TipoNotaFiscal tipoNotaFiscalRecebimento() {
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("RECEBIMENTO");
		tipoNotaFiscal.setGrupoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS);
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
		return tipoNotaFiscal;
	}
	
	public static NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor(CFOP cfop,
			PessoaJuridica emitente, Fornecedor fornecedor, TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, BigDecimal valorBruto, BigDecimal valorDesconto, BigDecimal valorLiquido) {
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = new NotaFiscalEntradaFornecedor();
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setEmitente(emitente);
		notaFiscalFornecedor.setNumero(2344242L);
		notaFiscalFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalFornecedor.setUsuario(usuario);
		notaFiscalFornecedor.setFornecedor(fornecedor);
		notaFiscalFornecedor.setValorBruto(valorBruto);
		notaFiscalFornecedor.setValorDesconto(valorDesconto);
		notaFiscalFornecedor.setValorLiquido(valorLiquido);

		return notaFiscalFornecedor;
	}
	
	public static NotaFiscalEntradaCota notaFiscalEntradaCotaNFE(
			CFOP cfop,
			PessoaJuridica emitente, 
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
			boolean indEmitida) {
		
		NotaFiscalEntradaCota notaFiscalEntradaCota = new NotaFiscalEntradaCota();
		
		notaFiscalEntradaCota.setCfop(cfop);
		notaFiscalEntradaCota.setChaveAcesso(chaveAcesso);
		notaFiscalEntradaCota.setDataEmissao(new Date());
		notaFiscalEntradaCota.setDataExpedicao(new Date());
		notaFiscalEntradaCota.setEmitente(emitente);
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
		
		return notaFiscalEntradaCota;
		
	}
	
	
	public static NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedorNFE(
			CFOP cfop,
			PessoaJuridica emitente, 
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
			boolean indEmitida) {
		
		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor = new NotaFiscalEntradaFornecedor();
		
		notaFiscalEntradaFornecedor.setCfop(cfop);
		notaFiscalEntradaFornecedor.setChaveAcesso(chaveAcesso);
		notaFiscalEntradaFornecedor.setDataEmissao(new Date());
		notaFiscalEntradaFornecedor.setDataExpedicao(new Date());
		notaFiscalEntradaFornecedor.setEmitente(emitente);
		notaFiscalEntradaFornecedor.setNumero(numero);
		notaFiscalEntradaFornecedor.setSerie(serie);
		notaFiscalEntradaFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalEntradaFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.PENDENTE);
		notaFiscalEntradaFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalEntradaFornecedor.setUsuario(usuario);
		notaFiscalEntradaFornecedor.setFornecedor(fornecedor);
		notaFiscalEntradaFornecedor.setValorBruto(valorBruto);
		notaFiscalEntradaFornecedor.setValorDesconto(valorDesconto);
		notaFiscalEntradaFornecedor.setValorLiquido(valorLiquido);
		notaFiscalEntradaFornecedor.setStatusEmissaoNfe(statusEmissaoNfe);
		notaFiscalEntradaFornecedor.setTipoEmissaoNfe(tipoEmissaoNfe);
		notaFiscalEntradaFornecedor.setEmitida(indEmitida);
		
		return notaFiscalEntradaFornecedor;
		
	}
	
	
	public static NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedorNFE(
			CFOP cfop,
			PessoaJuridica emitente, 
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
			boolean indEmitida) {
		
		NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedor = new NotaFiscalSaidaFornecedor();
		
		notaFiscalSaidaFornecedor.setCfop(cfop);
		notaFiscalSaidaFornecedor.setChaveAcesso(chaveAcesso);
		notaFiscalSaidaFornecedor.setDataEmissao(new Date());
		notaFiscalSaidaFornecedor.setDataExpedicao(new Date());
		notaFiscalSaidaFornecedor.setEmitente(emitente);
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
															  BigDecimal qtdeFisico) {
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(qtdeFisico);
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		return itemRecebimentoFisico;
	}
	
	public static EstoqueProduto estoqueProduto(ProdutoEdicao produtoEdicao, BigDecimal qtde) {
		EstoqueProduto estoqueProduto = new EstoqueProduto();
		estoqueProduto.setProdutoEdicao(produtoEdicao);
		estoqueProduto.setQtde(qtde);
		estoqueProduto.setQtdeSuplementar(qtde);
		return estoqueProduto;
	}
	
	public static EstoqueProdutoCota estoqueProdutoCota(ProdutoEdicao produtoEdicao, BigDecimal qtde,
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
													BigDecimal qtde, 
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
	
	public static ParametroSistema parametroSistema(TipoParametroSistema tipoParametroSistema, String valor){
		ParametroSistema parametroSistema = new ParametroSistema();
		parametroSistema.setTipoParametroSistema(tipoParametroSistema);
		parametroSistema.setValor(valor);
		
		return parametroSistema;
	}
	
	public static Diferenca diferenca(BigDecimal qtde,
									  Usuario usuarioResponsavel,
									  ProdutoEdicao produtoEdicao,
									  TipoDiferenca tipoDiferenca,
									  StatusConfirmacao statusConfirmacao,
									  ItemRecebimentoFisico itemRecebimentoFisico,
									  MovimentoEstoque movimentoEstoque,
									  Boolean automatica) {
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(qtde);
		diferenca.setResponsavel(usuarioResponsavel);
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setTipoDiferenca(tipoDiferenca);
		diferenca.setStatusConfirmacao(statusConfirmacao);
		diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
		diferenca.setMovimentoEstoque(movimentoEstoque);
		diferenca.setAutomatica(automatica);
		
		return diferenca;
	}
	
	public static EstoqueProdutoCota estoqueProdutoCota(
			ProdutoEdicao produtoEdicao, Cota cota, BigDecimal qtdeRecebida,
			BigDecimal qtdeDevolvida) {
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
			BigDecimal qtde, Cota cota, StatusAprovacao statusAprovacao, String motivo) {

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
			BigDecimal qtde, 
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
	
	public static RateioDiferenca rateioDiferenca(BigDecimal qtde, Cota cota, Diferenca diferenca, EstudoCota estudoCota){
		RateioDiferenca rateioDiferenca = new RateioDiferenca();
		rateioDiferenca.setCota(cota);
		rateioDiferenca.setDiferenca(diferenca);
		rateioDiferenca.setEstudoCota(estudoCota);
		rateioDiferenca.setQtde(qtde);
		
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
			Date dataStatus, BigDecimal reparte,
			StatusLancamento statusLancamento, List<ItemRecebimentoFisico> recebimentos) {
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
		
		for(ItemRecebimentoFisico x : recebimentos){
			lancamento.addRecebimento(x);
		}
		
		return lancamento;
	}
	
	public static Box criarBox(String codigo, String nome, TipoBox tipoBox) {
		Box box = new Box();
		box.setCodigo(codigo);
		box.setNome(nome);
		box.setTipoBox(tipoBox);
		return box;
	}
	
	public static Carteira carteira(int codigo, TipoRegistroCobranca tipoRegistroCobranca){
		Carteira carteira = new Carteira();
		carteira.setCodigo(codigo);
		carteira.setTipoRegistroCobranca(tipoRegistroCobranca);
		return carteira;
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
			boolean exigeDocSuspensao) {
		ContratoCota contratoCota = new ContratoCota();
		contratoCota.setCota(cota);
		contratoCota.setExigeDocumentacaoSuspencao(exigeDocSuspensao);
		return contratoCota;
	}
	
	public static PoliticaCobranca criarPoliticaCobranca(
			Distribuidor distribuidor, FormaCobranca formaCobranca, 
			boolean aceitaBaixaPagamentoMaior, boolean aceitaBaixaPagamentoMenor,
			boolean aceitaBaixaPagamentoVencido, int inadimplenciasSuspencao,
			String assuntoEmailCobranca, String mensagemEmailCobranca) {
		
		PoliticaCobranca politicaCobranca = new PoliticaCobranca();
		politicaCobranca.setAceitaBaixaPagamentoMaior(aceitaBaixaPagamentoMaior);
		politicaCobranca.setAceitaBaixaPagamentoMenor(aceitaBaixaPagamentoMenor);
		politicaCobranca.setAceitaBaixaPagamentoVencido(aceitaBaixaPagamentoVencido);
		politicaCobranca.setDistribuidor(distribuidor);
		politicaCobranca.setInadimplenciasSuspencao(inadimplenciasSuspencao);
		politicaCobranca.setFormaCobranca(formaCobranca);
		politicaCobranca.setAssuntoEmailCobranca(assuntoEmailCobranca);
		politicaCobranca.setMensagemEmailCobranca(mensagemEmailCobranca);
		return politicaCobranca;
	}
	
	public static Feriado feriado(Date data, String descricao) {
		
		Feriado feriado = new Feriado();
		
		feriado.setData(data);
		feriado.setDescricao(descricao);
		
		return feriado;
	}

	public static Endereco criarEndereco(TipoEndereco tipoEndereco, String cep,
										 String logradouro, int numero, 
										 String bairro, String cidade, String uf) {
		
		Endereco endereco = new Endereco();
		
		endereco.setBairro(bairro);
		endereco.setCep(cep);
		endereco.setCidade(cidade);
		endereco.setLogradouro(logradouro);
		endereco.setNumero(numero);
		endereco.setUf(uf);
		
		return endereco;
	}
	
	public static ParametroSistema[] criarParametrosEmail(){
		
		ParametroSistema[] parametrosEmail = new ParametroSistema[5];
		parametrosEmail[0] = Fixture.parametroSistema(TipoParametroSistema.EMAIL_HOST,"smtp.gmail.com");
		parametrosEmail[1] = Fixture.parametroSistema(TipoParametroSistema.EMAIL_PROTOCOLO,"smtps");
		parametrosEmail[2] = Fixture.parametroSistema(TipoParametroSistema.EMAIL_USUARIO, "sys.discover@gmail.com");
		parametrosEmail[3] = Fixture.parametroSistema(TipoParametroSistema.EMAIL_SENHA, "discover10");
		parametrosEmail[4] = Fixture.parametroSistema(TipoParametroSistema.EMAIL_PORTA, "465");
		
		return parametrosEmail;
		
	}
	
	public static Banco banco(Long agencia, boolean ativo, Carteira carteira, String codigoCedente, Long conta, String dvAgencia,
								 String dvConta, String instrucoes, Moeda moeda, String nome, String numeroBanco, BigDecimal juros, BigDecimal multa) {
		
		Banco banco = new Banco();
		
		banco.setAgencia(agencia);
		banco.setAtivo(ativo);
		banco.setCarteira(carteira);
		banco.setCodigoCedente(codigoCedente);
		banco.setConta(conta);
		banco.setDvAgencia(dvAgencia);
		banco.setDvConta(dvConta);
		banco.setInstrucoes(instrucoes);
		banco.setMoeda(moeda);
		banco.setNome(nome);
		banco.setNumeroBanco(numeroBanco);
		banco.setJuros(juros);
		banco.setMulta(multa);
		
		return banco;
	}
	
	public static ControleBaixaBancaria controleBaixaBancaria(Date data, StatusControle status, Usuario responsavel) {
	
		ControleBaixaBancaria controleBaixaBancaria = new ControleBaixaBancaria();
		
		controleBaixaBancaria.setData(data);
		controleBaixaBancaria.setStatus(status);
		controleBaixaBancaria.setResponsavel(responsavel);
		
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
			BigDecimal valorConsolidado) {
		ConsolidadoFinanceiroCota consolidado = new ConsolidadoFinanceiroCota();
		consolidado.setConsignado(valorConsolidado);
		consolidado.setMovimentos(movimentos);
		consolidado.setCota(cota);
		consolidado.setDataConsolidado(data);
		consolidado.setTotal(valorConsolidado);
		consolidado.setVendaEncalhe(valorConsolidado);
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
			BigDecimal valor) {
		Divida divida = new Divida();
		divida.setConsolidado(consolidado);
		divida.setCota(cota);
		divida.setData(data);
		divida.setResponsavel(usuario);
		divida.setStatus(status);
		divida.setValor(valor);
		return divida;
	}
	
	public static FormaCobranca formaCobrancaBoleto(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.BOLETO);
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}
	
	public static FormaCobranca formaCobrancaDinheiro(boolean recebeCobrancaEmail,
			BigDecimal valorMinimo, boolean vctoDiaUtil, Banco banco,
			BigDecimal taxaJurosMensal, BigDecimal taxaMulta, ParametroCobrancaCota parametroCobranca) {
		
		FormaCobranca formaBoleto = new FormaCobranca();
		formaBoleto.setAtiva(true);
		formaBoleto.setPrincipal(true);
		formaBoleto.setTipoCobranca(TipoCobranca.DINHEIRO);
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
		formaBoleto.setValorMinimoEmissao(valorMinimo);
		formaBoleto.setVencimentoDiaUtil(vctoDiaUtil);
		formaBoleto.setBanco(banco);
		formaBoleto.setTaxaJurosMensal(taxaJurosMensal);
		formaBoleto.setTaxaMulta(taxaMulta);
		formaBoleto.setRecebeCobrancaEmail(recebeCobrancaEmail);
		formaBoleto.setParametroCobrancaCota(parametroCobranca);
		
		return formaBoleto;
	}

	public static ParametroCobrancaCota parametroCobrancaCota(Set<FormaCobranca> formasCobranca,
							Integer numeroAcumuloDivida, BigDecimal valor, Cota cota,
							int fatorVencimento,boolean recebeCobrancaEmail, BigDecimal valorMininoCobranca) {
		
		ParametroCobrancaCota parametro = new ParametroCobrancaCota();
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		
		politicaSuspensao.setNumeroAcumuloDivida(numeroAcumuloDivida);
		politicaSuspensao.setValor(valor);
		
		parametro.setCota(cota);
		parametro.setFatorVencimento(fatorVencimento);
		parametro.setValorMininoCobranca(valorMininoCobranca);
		parametro.setPoliticaSuspensao(politicaSuspensao);
		parametro.setFormasCobrancaCota(formasCobranca);
		
		return parametro;
	}
	public static Rota rota(String codigoRota){
		Rota rota = new Rota();
		rota.setCodigoRota(codigoRota);
		return rota;
	}
	
	public static Roteiro roteiro(String descricaoRoteiro){
		Roteiro rota = new Roteiro();
		rota.setDescricaoRoteiro(descricaoRoteiro);
		return rota;
	}
	
	public static RotaRoteiroOperacao rotaRoteiroOperacao (Rota rota,Roteiro roteiro,Cota cota, TipoOperacao tipoOperacao){
		
		RotaRoteiroOperacao rotaRoteiroOperacao = new RotaRoteiroOperacao();
		rotaRoteiroOperacao.setRota(rota);
		rotaRoteiroOperacao.setRoteiro(roteiro);
		rotaRoteiroOperacao.setCota(cota);
		rotaRoteiroOperacao.setTipoOperacao(tipoOperacao);
		
		return rotaRoteiroOperacao;


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
	
	public static Editor criarEditor(String nome, Long codigo) {
		Editor editor = new Editor();
		editor.setNome(nome);
		editor.setCodigo(10L);
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
		pdv.setPrincipal(principal);
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
	
	public static EspecialidadePDV criarEspecialidadesPDV(Long codigo, String descricao){
		
		EspecialidadePDV especialidadePDV  = new EspecialidadePDV();
		especialidadePDV.setCodigo(codigo);
		especialidadePDV.setDescricao(descricao);
		
		return especialidadePDV;
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
	
	public static SegmentacaoPDV criarSegmentacaoPdv(AreaInfluenciaPDV areaInfluenciaPDV, ClusterPDV clusterPDV, 
													 TipoCaracteristicaSegmentacaoPDV tipoCaracteristica, 
													 TipoPontoPDV tipoPontoPDV, TipoClusterPDV tipoClusterPDV){
		
		SegmentacaoPDV segmentacaoPDV = new SegmentacaoPDV();
		segmentacaoPDV.setAreaInfluenciaPDV(areaInfluenciaPDV);
		segmentacaoPDV.setClusterPDV(clusterPDV);
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
	
	public static ClusterPDV criarClusterPDV(Long codigo, String descricao){
		
		ClusterPDV clusterPDV = new ClusterPDV();
		clusterPDV.setCodigo(codigo);
		clusterPDV.setDescricao(descricao);
		
		return clusterPDV;
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
}
