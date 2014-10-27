package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoDescontoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long descontoId;
	
	@Export(label="", exhibitionOrder = 1, alignment=Alignment.CENTER)
	private Long sequencial;
	
	@Export(label="Desconto %", exhibitionOrder = 2, alignment=Alignment.CENTER)
	private BigDecimal desconto;
	
	@Export(label="Fornecedor(es)", exhibitionOrder = 3, alignment=Alignment.LEFT)
	private String fornecedor;
	
	@Export(label="Data Alteração", exhibitionOrder = 4, alignment=Alignment.CENTER)
	private Date dataAlteracao;
	
	@Export(label="Usuário", exhibitionOrder = 5,alignment=Alignment.LEFT)
	private String usuario;
	
	private String descTipoDesconto;
	
	private Long idTipoDesconto;

	public Long getDescontoId() {
		return descontoId;
	}

	public void setDescontoId(Long descontoId) {
		this.descontoId = descontoId;
	}

	/**
	 * @return the sequencial
	 */
	public Long getSequencial() {
		return sequencial;
	}

	/**
	 * @param sequencial the sequencial to set
	 */
	public void setSequencial(Long sequencial) {
		this.sequencial = sequencial;
	}

	/**
	 * @return the idTipoDesconto
	 */
	public Long getIdTipoDesconto() {
		return idTipoDesconto;
	}

	/**
	 * @param idTipoDesconto the idTipoDesconto to set
	 */
	public void setIdTipoDesconto(Long idTipoDesconto) {
		this.idTipoDesconto = idTipoDesconto;
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
	 * @return the fornecedor
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the dataAlteracao
	 */
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	/**
	 * @param dataAlteracao the dataAlteracao to set
	 */
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the tipoDesconto
	 */
	public String getDescTipoDesconto() {
		return descTipoDesconto;
	}

	/**
	 * @param tipoDesconto the tipoDesconto to set
	 */
	public void setDescTipoDesconto(String descTipoDesconto) {
		this.descTipoDesconto = descTipoDesconto;
	}

	
}
