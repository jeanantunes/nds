package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;

public class ProdutoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4687524586257381194L;
	
	private Long idLancamento;
	
	private Long sequencia;
	
	private ProdutoEdicao produtoEdicao;

	private Long idFornecedor;
	
	private String nomeFornecedor;

	private Long idEditor;

	private String nomeEditor;

	private TipoLancamentoParcial parcial;
	
	private boolean possuiChamada;

	private Date dataLancamento;

	private Date dataRecolhimentoPrevista;
	
	private Date dataRecolhimentoDistribuidor;

	private BigDecimal expectativaEncalheSede;

	private BigDecimal expectativaEncalheAtendida;

	private BigDecimal expectativaEncalhe;

	private BigDecimal valorTotal;

	private Date novaData;

	/**
	 * Construtor padrão.
	 */
	public ProdutoRecolhimentoDTO() {
		
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
	 * @return the sequencia
	 */
	public Long getSequencia() {
		return sequencia;
	}

	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}

	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
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
	 * @param parcial the parcial to set
	 */
	public void setParcial(TipoLancamentoParcial parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the possuiChamada
	 */
	public boolean isPossuiChamada() {
		return possuiChamada;
	}

	/**
	 * @param possuiChamada the possuiChamada to set
	 */
	public void setPossuiChamada(boolean possuiChamada) {
		this.possuiChamada = possuiChamada;
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

}
