package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ValidacaoRecebimentoFisicoFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = -3535976906255346558L;
	
	@Export(label = "Nº Nota Fiscal" , alignment = Alignment.LEFT, exhibitionOrder = 1)
	private Long numeroNotaFiscal;
	
	@Export(label = "Inconsistência" , alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String inconsistencia  = "Gravado, não Confirmado";

	public Long getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public String getInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(String inconsistencia) {
		this.inconsistencia = inconsistencia;
	}
	
}
