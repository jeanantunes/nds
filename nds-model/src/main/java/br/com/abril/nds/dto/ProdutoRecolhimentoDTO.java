package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.util.Util;

/**
 * @author Discover Technology
 *
 */
public class ProdutoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4687524586257381194L;
	
	private Long idProdutoEdicao;
	
	private Long numeroEdicao;

	private BigDecimal precoVenda;
	
	private StatusLancamento statusLancamento;

	private boolean possuiBrinde;

	private Long peso;

	private BigDecimal desconto = BigDecimal.ZERO;

	private String codigoProduto;

	private String nomeProduto;
	
	private Long idLancamento;
	
	private Long idFornecedor;
	
	private String nomeFornecedor;

	private Long idEditor;

	private String nomeEditor;

	private TipoLancamentoParcial parcial;
	
	private Date dataLancamento;

	private Date dataRecolhimentoPrevista;
	
	private Date dataRecolhimentoDistribuidor;

	private BigDecimal expectativaEncalheSede;

	private BigDecimal expectativaEncalheAtendida;
	
	private BigDecimal expectativaEncalhe;

	private BigDecimal valorTotal;

	private Date novaData;
	
	private boolean produtoAgrupado;
	
	private List<Long> idsLancamentosAgrupados = new ArrayList<>();
	
	//TODO
	private Long peb;

	public Long getPeb() {

      return peb;
	}

	public void setPeb(Long peb) {
		this.peb = peb;
	}

	/**
	 * Construtor padrão.
	 */
	public ProdutoRecolhimentoDTO() {
		
	}
	
	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
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
	 * @return the precoVenda
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the statusLancamento
	 */
	public StatusLancamento getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param status do lançamento em formato de String para ser convertida
	 */
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = Util.getEnumByStringValue(StatusLancamento.values(), statusLancamento);
	}

	/**
	 * @return the possuiBrinde
	 */
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}

	/**
	 * @param possuiBrinde the possuiBrinde to set
	 */
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
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
		this.codigoProduto = codigoProduto;
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
		this.nomeProduto = nomeProduto;
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
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
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
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	/**
	 * @return the parcial
	 */
	public TipoLancamentoParcial getParcial() {
		return parcial;
	}

	/**
	 * @param parcial em formato de String para ser convertida.
	 */
	public void setParcial(String parcial) {
		
		this.parcial = Util.getEnumByStringValue(TipoLancamentoParcial.values(), parcial);
	}

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the expectativaEncalheSede
	 */
	public BigDecimal getExpectativaEncalheSede() {
		return expectativaEncalheSede;
	}

	/**
	 * @param expectativaEncalheSede the expectativaEncalheSede to set
	 */
	public void setExpectativaEncalheSede(BigDecimal expectativaEncalheSede) {
		this.expectativaEncalheSede = expectativaEncalheSede;
	}

	/**
	 * @return the expectativaEncalheAtendida
	 */
	public BigDecimal getExpectativaEncalheAtendida() {
		return expectativaEncalheAtendida;
	}

	/**
	 * @param expectativaEncalheAtendida the expectativaEncalheAtendida to set
	 */
	public void setExpectativaEncalheAtendida(BigDecimal expectativaEncalheAtendida) {
		this.expectativaEncalheAtendida = expectativaEncalheAtendida;
	}

	/**
	 * @return the expectativaEncalhe
	 */
	public BigDecimal getExpectativaEncalhe() {
		return expectativaEncalhe;
	}

	/**
	 * @param expectativaEncalhe the expectativaEncalhe to set
	 */
	public void setExpectativaEncalhe(BigDecimal expectativaEncalhe) {
		this.expectativaEncalhe = expectativaEncalhe;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the novaData
	 */
	public Date getNovaData() {
		return novaData;
	}

	/**
	 * @param novaData the novaData to set
	 */
	public void setNovaData(Date novaData) {
		this.novaData = novaData;
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
	 * @return the dataRecolhimentoDistribuidor
	 */
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	/**
	 * @param dataRecolhimentoDistribuidor the dataRecolhimentoDistribuidor to set
	 */
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}

	/**
	 * @return the balanceamentoSalvo
	 */
	public boolean isBalanceamentoSalvo() {
		//return StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.equals(statusLancamento);
		return StatusLancamento.BALANCEADO_RECOLHIMENTO.equals(statusLancamento);
	}
	
	public boolean isBalanceamentoCadeado() {
		return StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.equals(statusLancamento);
	}
	
	/**
	 * @return the produtoAgrupado
	 */
	public boolean isProdutoAgrupado() {
		return produtoAgrupado;
	}
	
	/**
	 * @param produtoAgrupado the produtoAgrupado to set
	 */
	public void setProdutoAgrupado(boolean produtoAgrupado) {
		this.produtoAgrupado = produtoAgrupado;
	}
	
	/**
	 * @return the idsLancamentosAgrupados
	 */
	public List<Long> getIdsLancamentosAgrupados() {
		return idsLancamentosAgrupados;
	}

	/**
	 * @param idsLancamentosAgrupados the idsLancamentosAgrupados to set
	 */
	public void setIdsLancamentosAgrupados(List<Long> idsLancamentosAgrupados) {
		this.idsLancamentosAgrupados = idsLancamentosAgrupados;
	}

	/**
	 * @return the balanceamentoConfirmado
	 */
	public boolean isBalanceamentoConfirmado() {
		return 
				StatusLancamento.BALANCEADO_RECOLHIMENTO.equals(statusLancamento) || 
				StatusLancamento.EM_RECOLHIMENTO.equals(statusLancamento) ||  
				StatusLancamento.RECOLHIDO.equals(statusLancamento);
	}
	
}
