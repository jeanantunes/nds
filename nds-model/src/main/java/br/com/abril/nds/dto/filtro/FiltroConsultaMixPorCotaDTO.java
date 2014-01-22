package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@SuppressWarnings("serial")
@Exportable
public class FiltroConsultaMixPorCotaDTO extends FiltroDTO implements Serializable{
 
	private Long id;
	private Integer cota;
	private String nomeCota;
	//pesquisa repartes pdv
	private Long produtoId;
	private Long cotaId;
	
	public Integer getCota() {
		return cota;
	}
	public void setCota(Integer cota) {
		this.cota = cota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}
	public Long getCotaId() {
		return cotaId;
	}
	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}
}
