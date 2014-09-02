package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public class ProdutoEdicaoEstudo extends ProdutoEdicao {

	private static final long serialVersionUID = -8906834711485549978L;

    private Long idEstudo;
    private boolean lancamentoParcial;
    private boolean edicaoAberta;
    private BigDecimal indicePeso;
    private Long idLancamento;
    private Date dataLancamento;
    private boolean colecao; // Atributo que define se o Produto é um fascículo/coleção
    private Integer periodo;

    private Long idCota;
    private BigDecimal reparte;
    private BigDecimal venda;
    private BigDecimal indiceCorrecao;
    private BigDecimal vendaCorrigida;
    private BigDecimal divisaoVendaCrescente;
    
    private boolean parcialConsolidada;
    
    public ProdutoEdicaoEstudo() {
    	indicePeso = BigDecimal.ONE;
    }

    public ProdutoEdicaoEstudo(String codigoProduto) {
    	this.setProduto(new Produto());
    	this.getProduto().setCodigo(codigoProduto);
    	indicePeso = BigDecimal.ONE;
    }
    
	public Long getIdEstudo() {
		return idEstudo;
	}
	public void setIdEstudo(Long idEstudo) {
		this.idEstudo = idEstudo;
	}
	public boolean isLancamentoParcial() {
		return lancamentoParcial;
	}
	public void setLancamentoParcial(boolean lancamentoParcial) {
		this.lancamentoParcial = lancamentoParcial;
	}
	public boolean isEdicaoAberta() {
		return edicaoAberta;
	}
	public void setEdicaoAberta(boolean edicaoAberta) {
		this.edicaoAberta = edicaoAberta;
	}
	public BigDecimal getIndicePeso() {
		return indicePeso;
	}
	public void setIndicePeso(BigDecimal indicePeso) {
		this.indicePeso = indicePeso;
	}
	public Long getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public boolean isColecao() {
		return colecao;
	}
	public void setColecao(boolean colecao) {
		this.colecao = colecao;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
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

	@Override
	public String toString() {
		return "" + numeroEdicao + "";
	}

	public BigDecimal getDivisaoVendaCrescente() {
	    return divisaoVendaCrescente;
	}

	public void setDivisaoVendaCrescente(BigDecimal divisaoVendaCrescente) {
	    this.divisaoVendaCrescente = divisaoVendaCrescente;
	}

	public boolean isParcialConsolidada() {
		return parcialConsolidada;
	}

	public void setParcialConsolidada(boolean parcialConsolidada) {
		this.parcialConsolidada = parcialConsolidada;
	}
	
}
