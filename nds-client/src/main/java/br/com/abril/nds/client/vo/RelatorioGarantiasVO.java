package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RelatorioGarantiasVO implements Serializable{

	private static final long serialVersionUID = -8493929598560370649L;
	
	
	@Export(label="Tipo Garantia",alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String tipoGarantia;
	
	@Export(label="Qtde. Cotas",alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String qtdCotas;
	
	@Export(label="Valor Total R$",alignment=Alignment.RIGHT, exhibitionOrder = 3)
	private String vlrTotal;
	
	private String detalhe;
	
	private ItemDTO<TipoGarantia,String> tpGarantia = new ItemDTO<TipoGarantia,String>();
	
	public RelatorioGarantiasVO() {
		super();
		
	}
	
	public RelatorioGarantiasVO(RelatorioGarantiasDTO dto) {
		
		this.tipoGarantia = dto.getTipoGarantia();
		this.qtdCotas = dto.getQtdCotas().toString();
		this.vlrTotal = CurrencyUtil.formatarValor(dto.getVlrTotal());
		tpGarantia.setKey(dto.getTipoGarantiaEnum());
		this.tpGarantia.setKey(dto.getTipoGarantiaEnum());
		
	}
	
	
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public String getQtdCotas() {
		return qtdCotas;
	}
	public void setQtdCotas(String qtdCotas) {
		this.qtdCotas = qtdCotas;
	}
	public String getVlrTotal() {
		return vlrTotal;
	}
	public void setVlrTotal(String vlrTotal) {
		this.vlrTotal = vlrTotal;
	}
	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}

	public ItemDTO<TipoGarantia, String> getTpGarantia() {
		return tpGarantia;
	}

	public void setTpGarantia(ItemDTO<TipoGarantia, String> tpGarantia) {
		this.tpGarantia = tpGarantia;
	}


	
	

}
