package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;

/**
 * DTO que representa os dados referentes ao balanceamento do recolhimento. 
 * 
 * @author Discover Technology
 *
 */
public class RecolhimentoDTO implements Serializable {

	private static final long serialVersionUID = -2168766293591158494L;

	private Long sequencia;
	
	private ProdutoEdicao produtoEdicao;

	private Long idFornecedor;
	
	private String nomeFornecedor;

	private Long idEditor;

	private String nomeEditor;

	private TipoLancamentoParcial parcial;
	
	private boolean possuiChamada;

	private Date dataLancamento;

	private Date dataRecolhimento;

	private BigDecimal sede;

	private BigDecimal atendida;

	private BigDecimal qtdeExemplares;

	private BigDecimal valorTotal;

	private Date novaData;

	/**
	 * Construtor padrão.
	 */
	public RecolhimentoDTO() {
		
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
	 * @return the sede
	 */
	public BigDecimal getSede() {
		return sede;
	}

	/**
	 * @param sede the sede to set
	 */
	public void setSede(BigDecimal sede) {
		this.sede = sede;
	}

	/**
	 * @return the atendida
	 */
	public BigDecimal getAtendida() {
		return atendida;
	}

	/**
	 * @param atendida the atendida to set
	 */
	public void setAtendida(BigDecimal atendida) {
		this.atendida = atendida;
	}

	/**
	 * @return the qtdeExemplares
	 */
	public BigDecimal getQtdeExemplares() {
		return qtdeExemplares;
	}

	/**
	 * @param qtdeExemplares the qtdeExemplares to set
	 */
	public void setQtdeExemplares(BigDecimal qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
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
	
}
