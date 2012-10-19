package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroContasAPagarDTO implements Serializable {

	private static final long serialVersionUID = 5604665210912108694L;
	
	private boolean primeiraCarga; /** define se a flexigrid esta sendo carregada pela primeira vez */
	
	private Date dataDe;
	private Date dataAte;
	private Integer ce;
	private String produto;
	private Long edicao;
	private	List <Long> produtoEdicaoIDs;
	private List<Long> idsFornecedores;
	private PaginacaoVO paginacaoVO;
	private Date dataDetalhe;
	
	
	public boolean isPrimeiraCarga() {
		return primeiraCarga;
	}
	public void setPrimeiraCarga(boolean primeiraCarga) {
		this.primeiraCarga = primeiraCarga;
	}
	public Date getDataDe() {
		return dataDe;
	}
	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}
	public Date getDataAte() {
		return dataAte;
	}
	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}
	public Integer getCe() {
		return ce;
	}
	public void setCe(Integer ce) {
		this.ce = ce;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public List<Long> getProdutoEdicaoIDs() {
		return produtoEdicaoIDs;
	}
	public void setProdutoEdicaoIDs(List<Long> produtoEdicaoIDs) {
		this.produtoEdicaoIDs = produtoEdicaoIDs;
	}
	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}
	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
	public Date getDataDetalhe() {
		return dataDetalhe;
	}
	public void setDataDetalhe(Date dataDetalhe) {
		this.dataDetalhe = dataDetalhe;
	}
}