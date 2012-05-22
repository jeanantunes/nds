package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;

public class RegistroCurvaABCEditorVO extends RegistroCurvaABC implements
		Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	@Export(label = "Número", exhibitionOrder = 1)
	private Integer codigoEditor;

	@Export(label = "Cota", exhibitionOrder = 2)
	private String nomeEditor;

	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 3)
	private Integer reparte;

	@Export(label = "Municipio", exhibitionOrder = 4)
	private String municipio;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private BigDecimal vendaExemplares;

	@Export(label = "Faturamento da Capa", exhibitionOrder = 6)
	private BigDecimal faturamentoCapa;

	public RegistroCurvaABCEditorVO() {
	}

	public RegistroCurvaABCEditorVO(Integer codigoEditor, String nomeEditor,
			Integer reparte, String municipio,
			BigDecimal vendaExemplares, BigDecimal faturamentoCapa) {
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		this.reparte = reparte;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
	}

	public Integer getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Integer codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public Integer getReparte() {
		return reparte;
	}

	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public BigDecimal getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigDecimal vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
	}

}
