package br.com.abril.nds.client.vo;


/**
 * Value Object para os produtos da matriz de recolhimento, formatados para exibir na tela.
 * 
 * @author Discover Technology
 */
public class ProdutoRecolhimentoFormatadoVO {

	private Long idFornecedor;
	
	private String statusLancamento;
	
	private String idLancamento;
	
	private String idProdutoEdicao;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private String numeroEdicao;
	
	private String precoVenda;
	
	private String precoDesconto;
	
	private String nomeFornecedor;
	
	private String nomeEditor;
	
	private String parcial;
	
	private String brinde;
	
	private String dataLancamento;
	
	private String dataRecolhimento;
	
	private String encalheSede;
	
	private String encalheAtendida;
	
	private String encalheAlternativo;
	
	private String encalhe;
	
	private String valorTotal;
	
	private String novaData;
	
	private boolean bloqueioAlteracaoBalanceamento;
	
	private boolean aceiteDataNova;
	
	private Long peb;
	
	private String replicar = "";
	
	public Long getPeb() {
		return peb;
	}

	public void setPeb(Long peb) {
		this.peb = peb;
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
	 * @return the idLancamento
	 */
	public String getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(String idLancamento) {
		this.idLancamento = (idLancamento != null) ? idLancamento : "";
	}

	/**
	 * @return the statusLancamento
	 */
	public String getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = (statusLancamento != null) ? statusLancamento : "";
	}
	
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = (codigoProduto != null) ? codigoProduto : "";
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = (nomeProduto != null) ? nomeProduto : "";
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = (numeroEdicao != null) ? numeroEdicao : "";
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = (precoVenda != null) ? precoVenda : "";
	}

	/**
	 * @return the precoDesconto
	 */
	public String getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = (precoDesconto != null) ? precoDesconto : "";
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = (nomeFornecedor != null) ? nomeFornecedor : "";
	}

	/**
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = (nomeEditor != null) ? nomeEditor : "";
	}

	/**
	 * @return the parcial
	 */
	public String getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(String parcial) {
		this.parcial = (parcial != null) ? parcial : "";
	}

	/**
	 * @return the brinde
	 */
	public String getBrinde() {
		return brinde;
	}

	/**
	 * @param brinde the brinde to set
	 */
	public void setBrinde(String brinde) {
		this.brinde = (brinde != null) ? brinde : "";
	}

	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = (dataLancamento != null) ? dataLancamento : "";
	}

	/**
	 * @return the dataRecolhimento
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = (dataRecolhimento != null) ? dataRecolhimento : "";
	}

	/**
	 * @return the encalheSede
	 */
	public String getEncalheSede() {
		return encalheSede;
	}

	/**
	 * @param encalheSede the encalheSede to set
	 */
	public void setEncalheSede(String encalheSede) {
		this.encalheSede = (encalheSede != null) ? encalheSede : "";
	}

	/**
	 * @return the encalheAtendida
	 */
	public String getEncalheAtendida() {
		return encalheAtendida;
	}

	/**
	 * @param encalheAtendida the encalheAtendida to set
	 */
	public void setEncalheAtendida(String encalheAtendida) {
		this.encalheAtendida = (encalheAtendida != null) ? encalheAtendida : "";
	}

	/**
	 * @return the encalheAlternativo
	 */
	public String getEncalheAlternativo() {
		return encalheAlternativo;
	}

	/**
	 * @param encalheAlternativo the encalheAlternativo to set
	 */
	public void setEncalheAlternativo(String encalheAlternativo) {
		this.encalheAlternativo = (encalheAlternativo != null ) ? encalheAlternativo : "";
	}

	/**
	 * @return the encalhe
	 */
	public String getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(String encalhe) {
		this.encalhe = (encalhe != null) ? encalhe : "";
	}

	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = (valorTotal != null) ? valorTotal : "";
	}

	/**
	 * @return the novaData
	 */
	public String getNovaData() {
		return novaData;
	}

	/**
	 * @param novaData the novaData to set
	 */
	public void setNovaData(String novaData) {
		this.novaData = (novaData != null) ? novaData : "";
	}
	
	/**
	 * @return the bloqueioAlteracaoBalanceamento
	 */
	public boolean isBloqueioAlteracaoBalanceamento() {
		return bloqueioAlteracaoBalanceamento;
	}

	/**
	 * @param bloqueioAlteracaoBalanceamento the bloqueioAlteracaoBalanceamento to set
	 */
	public void setBloqueioAlteracaoBalanceamento(
			boolean bloqueioAlteracaoBalanceamento) {
		this.bloqueioAlteracaoBalanceamento = bloqueioAlteracaoBalanceamento;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public String getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(String idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	
	
	/**
	 * @return the aceiteDataNova
	 */
	
	public boolean isAceiteDataNova() {
		return aceiteDataNova;
	}

	/**
	 * @param aceiteDataNova the aceiteDataNova to set
	 */
	
	public void setAceiteDataNova(boolean aceiteDataNova) {
		this.aceiteDataNova = aceiteDataNova;
	}

	public String getReplicar() {
		return replicar;
	}

	public void setReplicar(String replicar) {
		this.replicar = replicar;
	}
	
}
