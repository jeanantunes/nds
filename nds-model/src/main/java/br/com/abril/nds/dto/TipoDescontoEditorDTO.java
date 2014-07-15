package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TipoDescontoEditorDTO implements Serializable {

	private static final long serialVersionUID = -792066273850207153L;

	private BigInteger editorId;
	
	private BigInteger codigoEditor;
	
	private String nomeEditor;
	
	private BigInteger descontoId;
	
	private BigDecimal desconto;
	
	private BigInteger qtdCotas;
	
	private Date dataAlteracao;
	
	private BigInteger usuarioId;
	
	private String nomeUsuario;
	
	private String descTipoDesconto;
	
	private BigInteger idTipoDesconto;
	
	public TipoDescontoEditorDTO() {}
	
    public TipoDescontoEditorDTO(BigDecimal desconto, Date dataAlteracao, String descTipoDesconto) {
        this.desconto = desconto;
        this.dataAlteracao = dataAlteracao;
        this.descTipoDesconto = descTipoDesconto;
    }

	public BigInteger getEditorId() {
		return editorId;
	}

	public void setEditorId(BigInteger editorId) {
		this.editorId = editorId;
	}

	public BigInteger getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(BigInteger codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public BigInteger getDescontoId() {
		return descontoId;
	}

	public void setDescontoId(BigInteger descontoId) {
		this.descontoId = descontoId;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigInteger getQtdCotas() {
		return qtdCotas;
	}

	public void setQtdCotas(BigInteger qtdCotas) {
		this.qtdCotas = qtdCotas;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public BigInteger getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(BigInteger usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getDescTipoDesconto() {
		return descTipoDesconto;
	}

	public void setDescTipoDesconto(String descTipoDesconto) {
		this.descTipoDesconto = descTipoDesconto;
	}

	public BigInteger getIdTipoDesconto() {
		return idTipoDesconto;
	}

	public void setIdTipoDesconto(BigInteger idTipoDesconto) {
		this.idTipoDesconto = idTipoDesconto;
	}
		
}