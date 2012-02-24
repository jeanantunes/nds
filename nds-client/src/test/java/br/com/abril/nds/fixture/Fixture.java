package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
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

	public static Fornecedor fornecedorFC() {
		return fornecedor(juridicaFC(), SituacaoCadastro.ATIVO, true);
	}

	public static Fornecedor fornecedorDinap() {
		return fornecedor(juridicaDinap(), SituacaoCadastro.ATIVO, true);
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

	public static TipoProduto tipoRevista() {
		return tipoProduto("Revistas", GrupoProduto.REVISTA, "99000642");
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
			SituacaoCadastro situacaoCadastro, boolean permiteBalanceamento) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setJuridica(juridica);
		fornecedor.setSituacaoCadastro(situacaoCadastro);
		fornecedor.setPermiteBalanceamento(permiteBalanceamento);
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
			ProdutoEdicao produtoEdicao, Date dlp, Date drp) {
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dlp);
		lancamento.setDataLancamentoDistribuidor(dlp);
		lancamento.setDataRecolhimentoPrevista(drp);
		lancamento.setDataRecolhimentoDistribuidor(drp);
		return lancamento;
	}

	public static Distribuidor distribuidor(PessoaJuridica juridica,
			Date dataOperacao) {
		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setDataOperacao(dataOperacao);
		distribuidor.setJuridica(juridica);
		return distribuidor;
	}

	public static DistribuicaoFornecedor distribuicaoFornecedor(
			Distribuidor distribuidor, Fornecedor fornecedor,
			DiaSemana diaSemana) {
		DistribuicaoFornecedor df = new DistribuicaoFornecedor();
		df.setDistribuidor(distribuidor);
		df.setFornecedor(fornecedor);
		df.setDiaSemana(diaSemana);
		distribuidor.getDiasDistribuicao().add(df);
		return df;
	}

	public static Cota cota(Integer numeroCota, Pessoa pessoa,
			SituacaoCadastro situacaoCadastro) {

		Cota cota = new Cota();

		cota.setNumeroCota(numeroCota);

		cota.setPessoa(pessoa);

		cota.setSituacaoCadastro(situacaoCadastro);

		return cota;
	}

	public static Estudo estudo(Double qtdReparte, Date data,
			Lancamento lancamento, ProdutoEdicao produtoEdicao) {

		Estudo estudo = new Estudo();

		estudo.setQtdeReparte(qtdReparte);

		estudo.setData(data);

		estudo.setLancamento(lancamento);

		estudo.setProdutoEdicao(produtoEdicao);

		return estudo;
	}

	public static EstudoCota estudoCota(Double qtdePrevista,
			Double qtdeEfetiva, Estudo estudo, Cota cota) {

		EstudoCota estudoCota = new EstudoCota();

		estudoCota.setQtdePrevista(qtdePrevista);

		estudoCota.setQtdeEfetiva(qtdeEfetiva);

		estudoCota.setEstudo(estudo);

		estudoCota.setCota(cota);

		return estudoCota;
	}

	public static Usuario usuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("usuario");
		usuario.setSenha("123");

		return usuario;

	}

	public static CFOP cfop() {
		CFOP cfop = new CFOP();
		cfop.setCodigo("010101");
		cfop.setDescricao("default");
		cfop.setId(1L);
		return cfop;
	}

	public static TipoMovimento tipoMovimento() {
		TipoMovimento tipoMovimento = new TipoMovimento();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("ssddgggg");
		tipoMovimento.setId(1L);
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setTipoMovimentoEstoque(TipoMovimentoEstoque.FALTA_EM);
		return tipoMovimento;
	}

	public static ItemNotaFiscal itemNotaFiscal(ProdutoEdicao produtoEdicao,
			Usuario usuario) {
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
		itemNotaFiscal.setId(1L);
		itemNotaFiscal.setOrigem(Origem.MANUAL);
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQuantidade(new BigDecimal(1.0));
		itemNotaFiscal.setUsuario(usuario);
		return itemNotaFiscal;
	}

	public static TipoNotaFiscal tipoNotaFiscal() {
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("TIPO NOTA");
		tipoNotaFiscal.setId(1L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.ENTRADA);
		return tipoNotaFiscal;
	}

	public static NotaFiscalFornecedor notaFiscalFornecedor(CFOP cfop,
			PessoaJuridica juridicaAcme, TipoNotaFiscal tipoNotaFiscal,
			Usuario usuario) {
		NotaFiscalFornecedor notaFiscalFornecedor = new NotaFiscalFornecedor();
		notaFiscalFornecedor.setId(1L);
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setJuridica(juridicaAcme);
		notaFiscalFornecedor.setNumero("2344242");
		notaFiscalFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscal.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalFornecedor.setUsuario(usuario);
		return notaFiscalFornecedor;
	}

	public static RecebimentoFisico recebimentoFisico(
			NotaFiscalFornecedor notaFiscalFornecedor, Usuario usuario) {
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		recebimentoFisico.setData(new Date());
		recebimentoFisico.setDataConfirmacao(new Date());
		recebimentoFisico.setId(1L);
		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		recebimentoFisico.setUsuario(usuario);
		return recebimentoFisico;
	}

	public static ItemRecebimentoFisico itemRecebimentoFisico(
			ItemNotaFiscal itemNotaFiscal, RecebimentoFisico recebimentoFisico) {

		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setId(1L);
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(new BigDecimal(1.0));
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		return itemRecebimentoFisico;
	}

	public static MovimentoEstoque movimentoEstoque(
			ItemRecebimentoFisico itemRecebimentoFisico,
			ProdutoEdicao produtoEdicao, TipoMovimento tipoMovimento,
			Usuario usuario) {

		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		movimentoEstoque.setId(1L);
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setDiferenca(null);
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(new BigDecimal(1.0));
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		return movimentoEstoque;
	}

}
