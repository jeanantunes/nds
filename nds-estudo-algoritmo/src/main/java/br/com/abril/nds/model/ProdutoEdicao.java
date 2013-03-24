package br.com.abril.nds.model;

import java.math.BigDecimal;

public class ProdutoEdicao extends ProdutoEdicaoBase {

    private static final long serialVersionUID = 733461221142668493L;

    private Long idCota;
    private BigDecimal reparte;
    private BigDecimal venda;
    private BigDecimal indiceCorrecao;
    private BigDecimal vendaCorrigida;

    public BigDecimal getReparte() {
	return reparte;
    }

    public void setReparte(BigDecimal reparte) {
	this.reparte = reparte;
    }

    public BigDecimal getVenda() {
	return venda;
    }

    public void setVenda(BigDecimal venda) {
	this.venda = venda;
    }

    public BigDecimal getIndiceCorrecao() {
	return indiceCorrecao;
    }

    public void setIndiceCorrecao(BigDecimal indiceCorrecao) {
	this.indiceCorrecao = indiceCorrecao;
    }

    public BigDecimal getVendaCorrigida() {
	return vendaCorrigida;
    }

    public void setVendaCorrigida(BigDecimal vendaCorrigida) {
	this.vendaCorrigida = vendaCorrigida;
    }

    public Long getIdCota() {
	return idCota;
    }

    public void setIdCota(Long idCota) {
	this.idCota = idCota;
    }

}
