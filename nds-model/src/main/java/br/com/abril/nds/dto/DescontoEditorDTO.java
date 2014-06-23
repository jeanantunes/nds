package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.seguranca.Usuario;

public class DescontoEditorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3130564160911530014L;

	private Long codigoEditor;
	
	private String nomeEditor;
	
	private Boolean hasCotaEspecifica;
	
	private Boolean isTodasCotas;
	
	private BigDecimal valorDesconto;
	
	private List<Integer> cotas;
	
	private Usuario usuario;

	public Long getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public Boolean getHasCotaEspecifica() {
		return hasCotaEspecifica;
	}

	public void setHasCotaEspecifica(Boolean hasCotaEspecifica) {
		this.hasCotaEspecifica = hasCotaEspecifica;
	}

	public Boolean getIsTodasCotas() {
		return isTodasCotas;
	}

	public void setIsTodasCotas(Boolean isTodasCotas) {
		this.isTodasCotas = isTodasCotas;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public List<Integer> getCotas() {
		return cotas;
	}

	public void setCotas(List<Integer> cotas) {
		this.cotas = cotas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}