package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResumoCotaBasesDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 7771337394074049477L;
	
	@Export(label = "Indíce", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String indiceAjuste;
	@Export(label = "Base 1", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String equivalente01 = "";
	@Export(label = "Base 2", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String equivalente02 = "";
	@Export(label = "Base 3", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String equivalente03 = "";
	
	public ResumoCotaBasesDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public String getIndiceAjuste() {
		return indiceAjuste;
	}
	public void setIndiceAjuste(String indiceAjuste) {
		this.indiceAjuste = indiceAjuste;
	}
	public String getEquivalente01() {
		return equivalente01;
	}
	public void setEquivalente01(String equivalente01) {
		this.equivalente01 = equivalente01;
	}
	public String getEquivalente02() {
		return equivalente02;
	}
	public void setEquivalente02(String equivalente02) {
		this.equivalente02 = equivalente02;
	}
	public String getEquivalente03() {
		return equivalente03;
	}
	public void setEquivalente03(String equivalente03) {
		this.equivalente03 = equivalente03;
	}
	
	
	
}
