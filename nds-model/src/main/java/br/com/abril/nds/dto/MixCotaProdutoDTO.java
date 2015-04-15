package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class MixCotaProdutoDTO implements Serializable{
	
	private Long id;
	private Long cotaId;
	//Preenchimento Grid Reparte
	private String nomeProduto;
	private String codigoProduto;
	private String classificacaoProduto;
	private String nomeCota;
	private String numeroCota;
	//	//Preenchimento Grid Reparte
	private Long produtoId;
	private Long usuarioId;
	private Date dataHora;
	private Long vendaMedia;
	private Long reparteMedio;
	private Long ultimoReparte;
	private Long reparteMinimo;
	private Long reparteMaximo;
	private String codigoICD;
	
	private boolean itemValido; 
	
	List<RepartePDVDTO> repartesPDV;
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
	public Long getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}
	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public Long getVendaMedia() {
		return vendaMedia;
	}
	public void setVendaMedia(Long vendaMedia) {
		this.vendaMedia = vendaMedia;
	}
	public Long getReparteMedio() {
		return reparteMedio;
	}
	public void setReparteMedio(Long reparteMedio) {
		this.reparteMedio = reparteMedio;
	}
	public Long getUltimoReparte() {
		return ultimoReparte;
	}
	public void setUltimoReparte(Long ultimoReparte) {
		this.ultimoReparte = ultimoReparte;
	}
	public Long getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(Long reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public Long getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(Long reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}
	public List<RepartePDVDTO> getRepartesPDV() {
		return repartesPDV;
	}
	public void setRepartesPDV(List<RepartePDVDTO> repartesPDV) {
		this.repartesPDV = repartesPDV;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}
	public boolean isItemValido() {
		return itemValido;
	}
	public void setItemValido(boolean itemValido) {
		this.itemValido = itemValido;
	}
	public String getCodigoICD() {
		return codigoICD;
	}
	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}

	
}

