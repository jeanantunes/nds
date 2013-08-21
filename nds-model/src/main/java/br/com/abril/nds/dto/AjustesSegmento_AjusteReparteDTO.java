package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AjustesSegmento_AjusteReparteDTO implements Serializable {

	private static final long serialVersionUID = 3828583521517240573L;

	private BigDecimal ajusteAplicado;
	private TipoSegmentoProduto segmentoAplicado;

	public BigDecimal getAjusteAplicado() {
		return ajusteAplicado;
	}
	public void setAjusteAplicado(BigDecimal ajusteAplicado) {
		this.ajusteAplicado = ajusteAplicado;
	}
	public TipoSegmentoProduto getSegmentoAplicado() {
		return segmentoAplicado;
	}
	public void setSegmentoAplicado(TipoSegmentoProduto segmentoAplicado) {
		this.segmentoAplicado = segmentoAplicado;
	}
	
}
