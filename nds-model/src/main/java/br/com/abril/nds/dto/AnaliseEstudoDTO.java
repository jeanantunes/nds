package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class AnaliseEstudoDTO implements Serializable {

	private static final long serialVersionUID = 5998039236971366942L;

	private EstudoGerado estudo;
	private Long numeroEstudo;
	private String codigoProduto;
	private String nomeProduto;
	private ProdutoEdicao prodEdicao;
	private Long numeroEdicaoProduto;
	private String descicaoTpClassifProd;
	private boolean permiteParcial;
	
	private Integer codPeriodoProd;
	private String descricaoStatus;
	
	private boolean statusGeradoOuLiberado;
	
	private StatusLancamento statusEstudo;
	
	private TipoClassificacaoProduto tpClassifProduto;
	private PeriodicidadeProduto periodoProduto;
	
	private String dataLancamento;
	
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
		this.codPeriodoProd = periodoProduto.getOrdem();
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
	public EstudoGerado getEstudo() {
		return estudo;
	}
	public void setEstudo(EstudoGerado estudo) {
		this.estudo = estudo;
	}
	public boolean isPermiteParcial() {
	    return permiteParcial;
	}
	public void setPermiteParcial(boolean permiteParcial) {
	    this.permiteParcial = permiteParcial;
	}
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarDataPTBR(dataLancamento);
	}
	public String getDescricaoStatus() {
		return descricaoStatus;
	}
	public void setDescricaoStatus(String descricaoStatus) {
		this.descricaoStatus = descricaoStatus;
	}
	public StatusLancamento getStatusEstudo() {
		return statusEstudo;
	}
	public void setStatusEstudo(StatusLancamento statusEstudo) {
		this.statusEstudo = statusEstudo;
		this.descricaoStatus = statusEstudo.getDescricao();
	}
	public boolean isStatusGeradoOuLiberado() {
		return statusGeradoOuLiberado;
	}
	public void setStatusGeradoOuLiberado(boolean statusGeradoOuLiberado) {
		this.statusGeradoOuLiberado = statusGeradoOuLiberado;
		
		if(statusGeradoOuLiberado){
			this.descricaoStatus = "Liberado";
		}else{
			this.descricaoStatus = "Gerado";
		}
	}
	
}
