package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Cota;
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
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class Fixture {

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
			int periodicidade, TipoProduto tipo) {
		Produto produto = new Produto();
		produto.setCodigo(codigo);
		produto.setDescricao(descricao);
		produto.setNome(nome);
		produto.setPeriodicidade(PeriodicidadeProduto.SEMANAL);
		produto.setTipoProduto(tipo);
		return produto;
	}

	public static ProdutoEdicao produtoEdicao(Long numeroEdicao, int pacotePadrao, int peb,
			BigDecimal peso, BigDecimal precoCusto, BigDecimal precoVenda,
			Produto produto) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		produtoEdicao.setPacotePadrao(pacotePadrao);
		produtoEdicao.setPeb(peb);
		produtoEdicao.setPeso(peso);
		produtoEdicao.setPrecoCusto(precoCusto);
		produtoEdicao.setPrecoVenda(precoVenda);
		produtoEdicao.setProduto(produto);
		return produtoEdicao;
	}
	
	public static Lancamento lancamento(TipoLancamento tipoLancamento, ProdutoEdicao produtoEdicao, Date dlp, Date drp) {
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dlp);
		lancamento.setDataLancamentoDistribuidor(dlp);
		lancamento.setDataRecolhimentoPrevista(drp);
		lancamento.setDataRecolhimentoDistribuidor(drp);
		return lancamento;
	}
	

	public static Distribuidor distribuidor(PessoaJuridica juridica, Date dataOperacao) {
		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setDataOperacao(dataOperacao);
		distribuidor.setJuridica(juridica);
		return distribuidor;
	}
	
	public static DistribuicaoFornecedor distribuicaoFornecedor(
			Distribuidor distribuidor, Fornecedor fornecedor, DiaSemana diaSemana) {
		DistribuicaoFornecedor df = new DistribuicaoFornecedor();
		df.setDistribuidor(distribuidor);
		df.setFornecedor(fornecedor);
		df.setDiaSemana(diaSemana);
		distribuidor.getDiasDistribuicao().add(df);
		return df;
	}
	
	public static Cota cota(Integer numeroCota, Pessoa pessoa, SituacaoCadastro situacaoCadastro) {
		
		Cota cota = new Cota();
		
		cota.setNumeroCota(numeroCota);
		
		cota.setPessoa(pessoa);
		
		cota.setSituacaoCadastro(situacaoCadastro);
		
		return cota;
	}
	
	public static Estudo estudo(Double qtdReparte, Date data, Lancamento lancamento, ProdutoEdicao produtoEdicao) {
		
		Estudo estudo = new Estudo();
		
		estudo.setQtdeReparte(qtdReparte);
		
		estudo.setData(data);
		
		estudo.setLancamento(lancamento);
		
		estudo.setProdutoEdicao(produtoEdicao);
		
		return estudo;
	}

}
