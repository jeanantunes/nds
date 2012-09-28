package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.abril.nds.model.cadastro.pdv.PDV;

@Entity
@Table(name = "ROTA")
@SequenceGenerator(name="ROTA_SEQ", initialValue = 1, allocationSize = 1)
public class Rota implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7234522705455824338L;

	@Id
	@GeneratedValue(generator = "ROTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO_ROTA")
	private String codigoRota;
	
	@Column(name = "DESCRICAO_ROTA")
	private String descricaoRota;
	
	@ManyToOne
	@JoinColumn(name = "ROTEIRO_ID", nullable = false )
	private Roteiro roteiro;

	
	@ManyToMany
	@JoinTable(name = "PDV_ROTA", joinColumns = {@JoinColumn(name = "ROTA_ID")},
	inverseJoinColumns = {@JoinColumn(name = "PDV_ID")})
	@NotFound(action=NotFoundAction.IGNORE)
	private List<PDV> pdvs =  new ArrayList<PDV>();
	
	@Column(name="ORDEM", nullable = false)
	private Integer ordem;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoRota() {
		return codigoRota;
	}

	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public Roteiro getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(Roteiro roteiro) {
		this.roteiro = roteiro;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public List<PDV> getPdvs() {
		return pdvs;
	}

	public void setPdvs(List<PDV> pdvs) {
		this.pdvs = pdvs;
	}
	
	/**
	 * Adiciona um novo PDV à Rota
	 * @param pdv: PDV para inclusão
	 */
	public void addPdv(PDV pdv){
		if (pdvs == null){
			pdvs = new ArrayList<PDV>();
		}
		pdvs.add(pdv);
	}
	
	/**
	 * Adiciona novos PDV's à Rota
	 * @param listaPdv: List<PDV> para inclusão
	 */
	public void addAllPdv(List<PDV> listaPdv){
		if (pdvs == null){
			pdvs = new ArrayList<PDV>();
		}
		pdvs.addAll(listaPdv);
	}
}