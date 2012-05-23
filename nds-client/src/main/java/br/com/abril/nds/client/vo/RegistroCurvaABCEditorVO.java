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
	private Long codigoEditor;

	@Export(label = "Cota", exhibitionOrder = 2)
	private String nomeEditor;

	@Export(label = "Reparte", exhibitionOrder = 3)
	private BigDecimal reparte;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private BigDecimal vendaExemplares;

	@Export(label = "% Venda de Exemplares", exhibitionOrder = 5)
	private BigDecimal porcentagemVendaExemplares;

	@Export(label = "Faturamento da Capa", exhibitionOrder = 6)
	private BigDecimal faturamentoCapa;

	public RegistroCurvaABCEditorVO() {
	}

	public RegistroCurvaABCEditorVO(Long codigoEditor, String nomeEditor,
			BigDecimal reparte, BigDecimal vendaExemplares, BigDecimal faturamentoCapa) {
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		this.reparte = reparte;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
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

	public BigDecimal getPorcentagemVendaExemplares() {
		return porcentagemVendaExemplares;
	}

	public void setPorcentagemVendaExemplares(BigDecimal porcentagemVendaExemplares) {
		this.porcentagemVendaExemplares = porcentagemVendaExemplares;
	}

	public Long getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}
	
}
