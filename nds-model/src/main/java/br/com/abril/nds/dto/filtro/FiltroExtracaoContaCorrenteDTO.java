package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroExtracaoContaCorrenteDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -380146702694592516L;
	
	@Export(label="FECHAMENTO ENCALHE", alignment = Alignment.CENTER, xlsAutoSize = true, widthPercent = 10f, fontSize = 12f, alignWithHeader = "EDIÇÃO")
	private String tituloRelatorio;
	
	private Date dataDe;
	private Date dataAte;
	
	private boolean buscarCotasPostergadas;
	
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
	
}
