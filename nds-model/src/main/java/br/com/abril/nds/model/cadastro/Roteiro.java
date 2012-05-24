package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROTEIRO")
@SequenceGenerator(name="ROTEIRO_SEQ", initialValue = 1, allocationSize = 1)
public class Roteiro implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8766648817754986408L;

	@Id
	@GeneratedValue(generator = "ROTEIRO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="DESCRICAO_ROTEIRO")
	private String descricaoRoteiro;
	
	@OneToMany
	@JoinColumn( name="ROTEIRO_ID")
	private List<Rota> rotas = new ArrayList<Rota>();
	
	@ManyToOne
	@JoinColumn(name = "BOX_ID", nullable = true)
	private Box box;
	
	@Column(name="ORDEM", nullable = false)
	private Integer ordem;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ROTEIRO", nullable = false)
	private TipoRoteiro tipoRoteiro;
	

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	public List<Rota> getRotas() {
		return rotas;
	}

	public void setRotas(List<Rota> rotas) {
		this.rotas = rotas;
	}
	
	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public TipoRoteiro getTipoRoteiro() {
		return tipoRoteiro;
	}

	public void setTipoRoteiro(TipoRoteiro tipoRoteiro) {
		this.tipoRoteiro = tipoRoteiro;
	}

}