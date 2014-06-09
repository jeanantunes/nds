package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoRomaneioVO implements Serializable {

	private static final long serialVersionUID = -3608080303127493137L;
	

	@Export(label = "Total Cotas", alignment = Alignment.CENTER)
	private Integer totalCotas;


	public Integer getTotalCotas() {
		return totalCotas;
	}


	public void setTotalCotas(Integer totalCotas) {
		this.totalCotas = totalCotas;
	}

}
