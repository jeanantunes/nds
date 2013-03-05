package br.com.abril.nds.model;

import java.math.BigDecimal;

public class Segmento {

    private Long id;
    private TipoSegmentoProduto tipoSegmentoProduto;
    private Cota cota;
    private BigDecimal ajuste;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public TipoSegmentoProduto getTipoSegmentoProduto() {
	return tipoSegmentoProduto;
    }

    public void setTipoSegmentoProduto(TipoSegmentoProduto tipoSegmentoProduto) {
	this.tipoSegmentoProduto = tipoSegmentoProduto;
    }

    public Cota getCota() {
	return cota;
    }

    public void setCota(Cota cota) {
	this.cota = cota;
    }

    public BigDecimal getAjuste() {
	return ajuste;
    }

    public void setAjuste(BigDecimal ajuste) {
	this.ajuste = ajuste;
    }

}
