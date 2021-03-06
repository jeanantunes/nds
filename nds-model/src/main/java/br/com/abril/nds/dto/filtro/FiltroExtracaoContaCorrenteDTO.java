package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import com.itextpdf.text.Document;

import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroExtracaoContaCorrenteDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -380146702694592516L;
	
	@Export(label="FECHAMENTO ENCALHE", alignment = Alignment.CENTER, xlsAutoSize = true, fontSize = 16f, alignWithHeader = "EDIÇÃO")
	private String tituloRelatorio;
	
	private Date dataDe;
	private Date dataAte;
	private String semana;
	private Intervalo<Date> periodoRecolhimento;
	
	private boolean exportarPDF;
	
	private boolean buscarCotasPostergadas;
	
	private boolean saveDocument;
	
	private Document document;
	
	public String getTituloRelatorio() {
		return tituloRelatorio;
	}
	public void setTituloRelatorio(String tituloRelatorio) {
		this.tituloRelatorio = tituloRelatorio;
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
	public boolean isBuscarCotasPostergadas() {
		return buscarCotasPostergadas;
	}
	public void setBuscarCotasPostergadas(boolean buscarCotasPostergadas) {
		this.buscarCotasPostergadas = buscarCotasPostergadas;
	}
	public String getSemana() {
		return semana;
	}
	public void setSemana(String semana) {
		this.semana = semana;
	}
	public Intervalo<Date> getPeriodoRecolhimento() {
		return periodoRecolhimento;
	}
	public void setPeriodoRecolhimento(Intervalo<Date> periodoRecolhimento) {
		this.periodoRecolhimento = periodoRecolhimento;
	}
	public boolean isExportarPDF() {
		return exportarPDF;
	}
	public void setExportarPDF(boolean exportarPDF) {
		this.exportarPDF = exportarPDF;
	}
	public boolean isSaveDocument() {
		return saveDocument;
	}
	public void setSaveDocument(boolean saveDocument) {
		this.saveDocument = saveDocument;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	
}
