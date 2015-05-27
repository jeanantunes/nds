package br.com.abril.nds.client.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.ValeDesconto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ValeDescontoVO {

	private Long id;
	
	private Long idProduto;
	
	@Export(label="Código", widthPercent=15)
	private String codigo;
	
	@Export(label="Nome")
	private String nome;
	
	@Export(label="Edição", widthPercent=10)
	private Long numeroEdicao;
	
	@Export(label="Situação", widthPercent=15)
	private StatusLancamento situacao;
	
	private Long idFornecedor;
	
	private Long idEditor;
	
	private Long idLancamento;
	
	private BigDecimal valor;
	
	private String codigoBarras;
	
	private boolean vincularRecolhimento;
	
	private Date dataRecolhimentoPrevista;
	
	private Date dataRecolhimentoReal;
	
	private BigInteger semana;
	
	private String historico;
	
	private List<PublicacaoCuponada> publicacoesCuponadas = new ArrayList<PublicacaoCuponada>();
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the idProduto
	 */
	public Long getIdProduto() {
		return idProduto;
	}

	/**
	 * @param idProduto the idProduto to set
	 */
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the situacao
	 */
	public StatusLancamento getSituacao() {
		return situacao;
	}

	/**
	 * @param situacao the situacao to set
	 */
	public void setSituacao(StatusLancamento situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the idEditor
	 */
	public Long getIdEditor() {
		return idEditor;
	}

	/**
	 * @param idEditor the idEditor to set
	 */
	public void setIdEditor(Long idEditor) {
		this.idEditor = idEditor;
	}

	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}

	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	/**
	 * @return the vincularRecolhimento
	 */
	public boolean isVincularRecolhimento() {
		return vincularRecolhimento;
	}

	/**
	 * @param vincularRecolhimento the vincularRecolhimento to set
	 */
	public void setVincularRecolhimento(boolean vincularRecolhimento) {
		this.vincularRecolhimento = vincularRecolhimento;
	}

	/**
	 * @return the dataRecolhimentoPrevista
	 */
	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	/**
	 * @param dataRecolhimentoPrevista the dataRecolhimentoPrevista to set
	 */
	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}

	/**
	 * @return the dataRecolhimentoReal
	 */
	public Date getDataRecolhimentoReal() {
		return dataRecolhimentoReal;
	}

	/**
	 * @param dataRecolhimentoReal the dataRecolhimentoReal to set
	 */
	public void setDataRecolhimentoReal(Date dataRecolhimentoReal) {
		this.dataRecolhimentoReal = dataRecolhimentoReal;
	}

	/**
	 * @return the semana
	 */
	public BigInteger getSemana() {
		return semana;
	}

	/**
	 * @param semana the semana to set
	 */
	public void setSemana(BigInteger semana) {
		this.semana = semana;
	}

	/**
	 * @return the historico
	 */
	public String getHistorico() {
		return historico;
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(String historico) {
		this.historico = historico;
	}

	/**
	 * @return the publicacoesCuponadas
	 */
	public List<PublicacaoCuponada> getPublicacoesCuponadas() {
		return publicacoesCuponadas;
	}

	/**
	 * @param publicacoesCuponadas the publicacoesCuponadas to set
	 */
	public void setPublicacoesCuponadas(List<PublicacaoCuponada> publicacoesCuponadas) {
		this.publicacoesCuponadas = publicacoesCuponadas;
	}

	public static class PublicacaoCuponada {
		
		private Long id;
		
		private String codigo;
		
		private String nome;
		
		private Long numeroEdicao;
		
		private Date dataRecolhimento;

		private StatusLancamento situacao;

		public PublicacaoCuponada() { }
		
		public PublicacaoCuponada(Long id, String codigo, String nome, Long numeroEdicao, Date dataRecolhimento, StatusLancamento situacao) {
			this.id=id;
			this.codigo=codigo;
			this.nome=nome;
			this.numeroEdicao=numeroEdicao;
			this.dataRecolhimento=dataRecolhimento;
			this.situacao=situacao;
		}
		
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the codigo
		 */
		public String getCodigo() {
			return codigo;
		}

		/**
		 * @param codigo the codigo to set
		 */
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		/**
		 * @return the nome
		 */
		public String getNome() {
			return nome;
		}

		/**
		 * @param nome the nome to set
		 */
		public void setNome(String nome) {
			this.nome = nome;
		}

		/**
		 * @return the numeroEdicao
		 */
		public Long getNumeroEdicao() {
			return numeroEdicao;
		}

		/**
		 * @param numeroEdicao the numeroEdicao to set
		 */
		public void setNumeroEdicao(Long numeroEdicao) {
			this.numeroEdicao = numeroEdicao;
		}

		/**
		 * @return the dataRecolhimento
		 */
		public Date getDataRecolhimento() {
			return dataRecolhimento;
		}

		/**
		 * @param dataRecolhimento the dataRecolhimento to set
		 */
		public void setDataRecolhimento(Date dataRecolhimento) {
			this.dataRecolhimento = dataRecolhimento;
		}

		/**
		 * @return the situacao
		 */
		public String getSituacao() {
			return situacao.getDescricao();
		}

		/**
		 * @param situacao the situacao to set
		 */
		public void setSituacao(StatusLancamento situacao) {
			this.situacao = situacao;
		}
	}
	
	public ValeDesconto toValeDesconto() {
		
		Editor editor = new Editor();
		editor.setId(this.idEditor);

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(this.idFornecedor);
		
		HashSet<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		fornecedores.add(fornecedor);
		
		Produto produto = new Produto();
		
		produto.setId(this.idProduto);
		produto.setCodigo(this.codigo);
		produto.setNome(this.nome);
		produto.setEditor(editor);
		produto.setFornecedores(fornecedores);
		produto.setPeso(0L);
		produto.setOrigem(Origem.MANUAL);
		produto.setPeriodicidade(PeriodicidadeProduto.INDEFINIDO);
		
		TipoProduto tipoProduto = new TipoProduto();
		tipoProduto.setId(23L);
		tipoProduto.setGrupoProduto(GrupoProduto.VALE_DESCONTO);
		
		produto.setTipoProduto(tipoProduto);
		
		ValeDesconto valeDesconto = new ValeDesconto();
		
		valeDesconto.setId(this.id);
		valeDesconto.setProduto(produto);
		valeDesconto.setNumeroEdicao(this.numeroEdicao);
		valeDesconto.setPrecoVenda(this.valor);
		valeDesconto.setCodigoDeBarras(this.codigoBarras);
		valeDesconto.setPeso(0L);
		valeDesconto.setHistorico(this.historico);
		valeDesconto.setVincularRecolhimentoProdutosCuponados(this.vincularRecolhimento);		
		valeDesconto.setOrigem(Origem.MANUAL);
		
		Lancamento lancamento = new Lancamento();
		
		lancamento.setId(this.idLancamento);
		lancamento.setStatus(this.situacao == null ? StatusLancamento.PLANEJADO : this.situacao);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataLancamentoPrevista(this.dataRecolhimentoPrevista == null ? new Date() : this.dataRecolhimentoPrevista);
		lancamento.setDataLancamentoDistribuidor(this.dataRecolhimentoReal == null ? new Date() : this.dataRecolhimentoReal);
		lancamento.setDataRecolhimentoPrevista(this.dataRecolhimentoPrevista == null ? new Date() : this.dataRecolhimentoPrevista);
		lancamento.setDataRecolhimentoDistribuidor(this.dataRecolhimentoReal == null ? new Date() : this.dataRecolhimentoReal);
//		lancamento.setTipoLancamento(TipoLancamento.NORMAL);
		lancamento.setDataStatus(new Date());
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setNumeroLancamento(1);

		Set<Lancamento> lancamentos = new HashSet<Lancamento>();
		lancamentos.add(lancamento);

		valeDesconto.setLancamentos(lancamentos);

		Set<ProdutoEdicao> produtosAplicacao = new HashSet<ProdutoEdicao>();
		
		for (PublicacaoCuponada publicacaoCuponada : this.publicacoesCuponadas) {
			
			ProdutoEdicao produtoEdicao = new ProdutoEdicao();
			
			produtoEdicao.setId(publicacaoCuponada.getId());
			
			produtosAplicacao.add(produtoEdicao);
		}
		
		valeDesconto.setProdutosAplicacao(produtosAplicacao);
		
		return valeDesconto;
	}
}
