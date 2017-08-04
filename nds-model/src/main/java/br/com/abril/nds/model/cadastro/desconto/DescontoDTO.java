package br.com.abril.nds.model.cadastro.desconto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DescontoDTO implements Serializable {

	private static final long serialVersionUID = 5523508217786687630L;

	private Long id;
	
	private String tipoDesconto; 
	
	private Long idEspecifico;
	
	private Long cotaId; 
	
	private Long produtoEdicaoId;
	
	private Long produtoId;
	
	private Long fornecedorId;
	
	private Long editorId;
	
	private BigDecimal valor;
	
	private boolean predominante;
	
	private boolean proximoLancamento;
	
	private BigDecimal margemDistribuidor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCotaId() {
		return cotaId;
	}

	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}

	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}

	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	public Long getFornecedorId() {
		return fornecedorId;
	}

	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}

	public Long getEditorId() {
		return editorId;
	}

	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public boolean isPredominante() {
		return predominante;
	}

	public void setPredominante(boolean predominante) {
		this.predominante = predominante;
	}
	
	public boolean isProximoLancamento() {
		return proximoLancamento;
	}

	public void setProximoLancamento(boolean proximoLancamento) {
		this.proximoLancamento = proximoLancamento;
	}

	public String getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public Long getIdEspecifico() {
		return idEspecifico;
	}

	public void setIdEspecifico(Long idEspecifico) {
		this.idEspecifico = idEspecifico;
	}

	public BigDecimal getMargemDistribuidor() {
		return margemDistribuidor;
	}

	public void setMargemDistribuidor(BigDecimal margemDistribuidor) {
		this.margemDistribuidor = margemDistribuidor;
	}
}