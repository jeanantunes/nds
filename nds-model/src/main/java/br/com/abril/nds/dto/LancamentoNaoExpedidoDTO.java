package br.com.abril.nds.dto;

import java.io.Serializable;


public class LancamentoNaoExpedidoDTO implements Serializable {

	private static final long serialVersionUID = 6884763521167786938L;

	private Long idLancamento;
	
	//ItemRecebimentoFisico.recebimentoFisico.dataRecebimento;	
		//-ItemNotaFiscal.produtoEdicao = Lancamento.produtoEdicao
		//-ItemNotaFiscal.dataLancamento = Lancamento.dataLancamentoDistribuidor
	
	private String dataEntrada;
	//Lancamento.produtoEdicao.produto.id.
	private Long codigo;
	//Lancamento.produtoEdicao.produto.nome
	private String produto;
	//Lancamento.produtoEdicao.numeroEdicao
	private Long edicao;
	//Lancamento.produtoEdicao.produto.tipoProduto.descricao
	private String classificacao;
	//Lancamento.produtoEdicao.precoVenda
	private String preco;
	//Lancamento.produtoEdicao.pacotePadrao
	private Integer pctPadrao;
	//Lancamento.estudo.qtdeReparte
	private Integer reparte;
	//Lancamento.dataRecolhimentoPrevista
	private String  dataChamada;
	//Lancamento.produtoEdicao.produto.? List
	private String fornecedor;
	//Lancamento.estudo.id
	private Integer estudo;
	//campo de tela
	private Boolean selecionado; 

	
	
	
	public LancamentoNaoExpedidoDTO(Long idLancamento, String dataEntrada, Long codigo, String produto, Long edicao, String classificacao, String preco, Integer pctPadrao, Integer reparte, String dataChamada, String fornecedor, Integer estudo, Boolean selecionado) {
		super();
		this.idLancamento = idLancamento;
		this.dataEntrada = dataEntrada;
		this.codigo = codigo;
		this.produto = produto;
		this.edicao = edicao;
		this.classificacao = classificacao;
		this.preco = preco;
		this.pctPadrao = pctPadrao;
		this.reparte = reparte;
		this.dataChamada = dataChamada;
		this.fornecedor = fornecedor;
		this.estudo = estudo;
		this.selecionado = selecionado;
	}




	public Long getIdLancamento() {
		return idLancamento;
	}




	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}




	public String getDataEntrada() {
		return dataEntrada;
	}




	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}




	public Long getCodigo() {
		return codigo;
	}




	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}




	public String getProduto() {
		return produto;
	}




	public void setProduto(String produto) {
		this.produto = produto;
	}




	public Long getEdicao() {
		return edicao;
	}




	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}




	public String getClassificacao() {
		return classificacao;
	}




	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}




	public String getPreco() {
		return preco;
	}




	public void setPreco(String preco) {
		this.preco = preco;
	}




	public Integer getPctPadrao() {
		return pctPadrao;
	}




	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}




	public Integer getReparte() {
		return reparte;
	}




	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}




	public String getDataChamada() {
		return dataChamada;
	}




	public void setDataChamada(String dataChamada) {
		this.dataChamada = dataChamada;
	}




	public String getFornecedor() {
		return fornecedor;
	}




	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}




	public Integer getEstudo() {
		return estudo;
	}




	public void setEstudo(Integer estudo) {
		this.estudo = estudo;
	}




	public Boolean getSelecionado() {
		return selecionado;
	}




	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}




	public static enum SortColumn {
		
		DATA_ENTRADA("dataEntrada"),
		CODIGO_PRODUTO("codigo"),
		NOME_PRODUTO("produto"),
		EDICAO("edicao"),
		CLASSIFICACAO_PRODUTO("classificacao"),
		PRECO_PRODUTO("preco"),
		QTDE_PACOTE_PADRAO("pctPadrao"),
		QTDE_REPARTE("reparte"),
		DATA_CHAMADA("dataChamada"),
		FORNECEDOR("fornecedor"),
		ID_ESTUDO("estudo");
		
		private SortColumn(String property) {
			this.property = property;
		}

		private String property;

		public String getProperty() {
			return property;
		}

		public static SortColumn getByProperty(String property) {
			for (SortColumn column : values()) {
				if (column.getProperty().equals(property)) {
					return column;
				}
			}
			return null;
		}
	}	
	
}
