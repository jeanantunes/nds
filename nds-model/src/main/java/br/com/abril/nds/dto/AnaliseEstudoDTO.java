package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.vo.PaginacaoVO;

public class AnaliseEstudoDTO implements Serializable {

	private static final long serialVersionUID = 5998039236971366942L;

	private Estudo estudo;
	private Long numeroEstudo;
	private String codigoProduto;
	private String nomeProduto;
	private ProdutoEdicao prodEdicao;
	private Long numeroEdicaoProduto;
	private Integer codPeriodoProd;
	private String descicaoTpClassifProd;
	private boolean permiteParcial;
	
	private String statusEstudo = "";
	
	private StatusLancamento statusRecolhiOuExpedido;
	private Boolean statusLiberadoOuGerado;
	
	private TipoClassificacaoProduto tpClassifProduto;
	private PeriodicidadeProduto periodoProduto;
	
	private PaginacaoVO paginacao;
	
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	public Long getNumeroEstudo() {
		return numeroEstudo;
	}
	public void setNumeroEstudo(Long numeroEstudo) {
		this.numeroEstudo = numeroEstudo;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getNumeroEdicaoProduto() {
		return numeroEdicaoProduto;
	}
	public void setNumeroEdicaoProduto(Long numeroEdicaoProduto) {
		this.numeroEdicaoProduto = numeroEdicaoProduto;
	}
	public TipoClassificacaoProduto getTpClassifProduto() {
		return tpClassifProduto;
	}
	public void setTpClassifProduto(TipoClassificacaoProduto tpClassifProduto) {
		this.tpClassifProduto = tpClassifProduto;
	}
	public PeriodicidadeProduto getPeriodoProduto() {
		return periodoProduto;
	}
	public void setPeriodoProduto(PeriodicidadeProduto periodoProduto) {
		this.periodoProduto = periodoProduto;
	}
	public Integer getCodPeriodoProd() {
		return codPeriodoProd;
	}
	public void setCodPeriodoProd(Integer codPeriodoProd) {
		this.codPeriodoProd = codPeriodoProd;
	}
	public String getDescicaoTpClassifProd() {
		return descicaoTpClassifProd;
	}
	public void setDescicaoTpClassifProd(String descicaoTpClassifProd) {
		this.descicaoTpClassifProd = descicaoTpClassifProd;
	}
	public ProdutoEdicao getProdEdicao() {
		return prodEdicao;
	}
	public void setProdEdicao(ProdutoEdicao prodEdicao) {
		this.prodEdicao = prodEdicao;
	}
	public Estudo getEstudo() {
		return estudo;
	}
	public void setEstudo(Estudo estudo) {
		this.estudo = estudo;
	}
	public String getStatusEstudo() {
		return statusEstudo;
	}
	public void setStatusEstudo(String statusEstudo) {
		this.statusEstudo = statusEstudo;
	}
	public StatusLancamento getStatusRecolhiOuExpedido() {
		return statusRecolhiOuExpedido;
	}
	public void setStatusRecolhiOuExpedido(StatusLancamento statusRecolhiOuExpedido) {
		this.statusRecolhiOuExpedido = statusRecolhiOuExpedido;
	}
	public Boolean getStatusLiberadoOuGerado() {
		return statusLiberadoOuGerado;
	}
	public void setStatusLiberadoOuGerado(Boolean statusLiberadoOuGerado) {
		this.statusLiberadoOuGerado = statusLiberadoOuGerado;
	}
	public boolean isPermiteParcial() {
	    return permiteParcial;
	}
	public void setPermiteParcial(boolean permiteParcial) {
	    this.permiteParcial = permiteParcial;
	}
}
