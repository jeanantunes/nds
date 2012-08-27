package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.abril.nds.dto.RegistroCurvaABCDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroCurvaABCEditorVO extends RegistroCurvaABCDTO implements
		Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	@Export(label = "Número", exhibitionOrder = 1)
	private Long codigoEditor;

	@Export(label = "Cota", exhibitionOrder = 2)
	private String nomeEditor;

	private BigInteger reparte;

	private BigInteger vendaExemplares;

	private BigDecimal porcentagemVendaExemplares;

	private BigDecimal faturamentoCapa;

	@Export(label = "Reparte", exhibitionOrder = 3)
	private String reparteFormatado;
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private String vendaExemplaresFormatado;
	
	@Export(label = "% Venda de Exemplares", exhibitionOrder = 5)
	private String porcentagemVendaExemplaresFormatado;
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 6)
	private String faturamentoCapaFormatado;
	
	private Date dataDe;
	
	private Date dataAte;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";  
	SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA);
	
	public RegistroCurvaABCEditorVO() {
	}

	public RegistroCurvaABCEditorVO(Long codigoEditor, String nomeEditor,
			BigInteger reparte, BigInteger vendaExemplares, BigDecimal faturamentoCapa) {
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		this.reparte = reparte;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamentoCapa;
		this.formatarCampos();
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
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
		this.porcentagemVendaExemplaresFormatado = CurrencyUtil.formatarValor(porcentagemVendaExemplares);
		this.porcentagemVendaExemplares = porcentagemVendaExemplares;
	}

	public Long getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	@Export(label = "Participação", exhibitionOrder = 7)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 8)
	public String getParticipacaoAcumuladaString() {
		return getParticipacaoAcumuladaFormatado();
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public String getDataDeString() {
		return sdf.format(dataDe);
	}

	public String getDataAteString() {
		return sdf.format(dataAte);
	}

	public String getReparteFormatado() {
		return reparteFormatado;
	}

	public void setReparteFormatado(String reparteFormatado) {
		this.reparteFormatado = reparteFormatado;
	}

	public String getVendaExemplaresFormatado() {
		return vendaExemplaresFormatado;
	}

	public void setVendaExemplaresFormatado(String vendaExemplaresFormatado) {
		this.vendaExemplaresFormatado = vendaExemplaresFormatado;
	}

	public String getPorcentagemVendaExemplaresFormatado() {
		return porcentagemVendaExemplaresFormatado;
	}

	public void setPorcentagemVendaExemplaresFormatado(
			String porcentagemVendaExemplaresFormatado) {
		this.porcentagemVendaExemplaresFormatado = porcentagemVendaExemplaresFormatado;
	}

	public String getFaturamentoCapaFormatado() {
		return faturamentoCapaFormatado;
	}

	public void setFaturamentoCapaFormatado(String faturamentoCapaFormatado) {
		this.faturamentoCapaFormatado = faturamentoCapaFormatado;
	}

	private void formatarCampos() {
		reparteFormatado = CurrencyUtil.formatarValorTruncado(reparte);
		vendaExemplaresFormatado = CurrencyUtil.formatarValorTruncado(vendaExemplares);
		porcentagemVendaExemplaresFormatado = CurrencyUtil.formatarValor(porcentagemVendaExemplares);
		faturamentoCapaFormatado = CurrencyUtil.formatarValor(faturamentoCapa);
	}

	
}
