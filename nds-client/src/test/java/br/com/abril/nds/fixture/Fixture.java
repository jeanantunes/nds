package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;

public class Fixture {

	public static PessoaJuridica juridicaFC() {
		return pessoaJuridica("FC", "00.000.000/0001-00", "000.000.000.000",
				"fc@mail.com");
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
		return criarBox("300", "Box 300", TipoBox.REPARTE);
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
			Date dataOperacao) {
		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setDataOperacao(dataOperacao);
		distribuidor.setJuridica(juridica);
		distribuidor.setPoliticaCobranca(criarPoliticaCobranca(distribuidor,
				TipoCobranca.BOLETO, new BigDecimal(200)));
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
		tipoMovimento.setDescricao("SObra EM");
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

	public static ItemNotaFiscal itemNotaFiscal(ProdutoEdicao produtoEdicao,
			Usuario usuario, NotaFiscal notaFiscal, Date dataLancamento, Date dataRecolhimento, TipoLancamento tipoLancamento, BigDecimal qtde) {
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
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

	public static TipoNotaFiscal tipoNotaFiscalRecebimento() {
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("RECEBIMENTO");
		tipoNotaFiscal.setTipoOperacao(br.com.abril.nds.model.fiscal.TipoOperacao.ENTRADA);
		return tipoNotaFiscal;
	}

	public static NotaFiscalFornecedor notaFiscalFornecedor(CFOP cfop,
			PessoaJuridica emitente, Fornecedor fornecedor, TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario, BigDecimal valorBruto, BigDecimal valorDesconto, BigDecimal valorLiquido) {
		NotaFiscalFornecedor notaFiscalFornecedor = new NotaFiscalFornecedor();
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setEmitente(emitente);
		notaFiscalFornecedor.setNumero("2344242");
		notaFiscalFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscal.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalFornecedor.setUsuario(usuario);
		notaFiscalFornecedor.setFornecedor(fornecedor);
		notaFiscalFornecedor.setValorBruto(valorBruto);
		notaFiscalFornecedor.setValorDesconto(valorDesconto);
		notaFiscalFornecedor.setValorLiquido(valorLiquido);

		return notaFiscalFornecedor;
	}

	public static RecebimentoFisico recebimentoFisico(NotaFiscalFornecedor notaFiscalFornecedor, 
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

	public static ItemRecebimentoFisico itemRecebimentoFisico(ItemNotaFiscal itemNotaFiscal, 
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
		return estoqueProduto;
	}

	public static MovimentoEstoque movimentoEstoque(
			ItemRecebimentoFisico itemRecebimentoFisico,
			ProdutoEdicao produtoEdicao, TipoMovimentoEstoque tipoMovimento,
			Usuario usuario, EstoqueProduto estoqueProduto,
			StatusAprovacao statusAprovacao, String motivo) {

		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(new BigDecimal(1.0));
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		if (tipoMovimento.getOperacaoEstoque() == OperacaoEstoque.ENTRADA) {
			estoqueProduto.setQtde(estoqueProduto.getQtde().add(movimentoEstoque.getQtde()));
		} else {
			estoqueProduto.setQtde(estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde()));
		}
		estoqueProduto.getMovimentos().add(movimentoEstoque);
		movimentoEstoque.setEstoqueProduto(estoqueProduto);
		movimentoEstoque.setStatus(statusAprovacao);
		movimentoEstoque.setMotivo(motivo);
		return movimentoEstoque;
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
		
		movimentoEstoque.setDataInclusao(dataInclusao);
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
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(qtde);
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		if (tipoMovimento.getOperacaoEstoque() == OperacaoEstoque.ENTRADA) {
			estoqueProdutoCota.setQtdeRecebida(estoqueProdutoCota
					.getQtdeRecebida().add(movimentoEstoque.getQtde()));
		} else {
			estoqueProdutoCota.setQtdeDevolvida(estoqueProdutoCota
					.getQtdeDevolvida().subtract(movimentoEstoque.getQtde()));
		}
		estoqueProdutoCota.getMovimentos().add(movimentoEstoque);
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

	//FINANCEIRO - HENRIQUE
	public static Boleto boleto(String nossoNumero,
				                Date dataEmissao,
				                Date dataVencimento,
				                Date dataPagamento,
				                String encargos,
				                Double valor,
				                String tipoBaixa,
				                String acao,
				                StatusCobranca status,
				                Cota cota){
			
		Boleto boleto = new Boleto();
		boleto.setNossoNumero(nossoNumero);
		boleto.setDataEmissao(dataEmissao);
		boleto.setDataVencimento(dataVencimento);
		boleto.setDataPagamento(dataPagamento);
		boleto.setEncargos(encargos);
		boleto.setValor(valor);
		boleto.setTipoBaixa(tipoBaixa);
		boleto.setAcao(acao);
		boleto.setStatusCobranca(status);
		boleto.setCota(cota);
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
			Distribuidor distribuidor, TipoCobranca tipo, BigDecimal valorMinimo) {
		PoliticaCobranca politicaCobranca = new PoliticaCobranca();
		politicaCobranca.setTipoCobranca(tipo);
		politicaCobranca.setValorMinino(valorMinimo);
		politicaCobranca.setDistribuidor(distribuidor);
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
}
