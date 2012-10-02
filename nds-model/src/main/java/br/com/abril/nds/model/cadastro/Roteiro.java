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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
	
	@ManyToOne
	@JoinColumn(name = "ROTEIRIZACAO_ID", nullable = false )
	private Roteirizacao roteirizacao;
	
	@OneToMany
	@JoinColumn( name="ROTEIRO_ID")
	@Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	private List<Rota> rotas = new ArrayList<Rota>();
	
	@Column(name="ORDEM", nullable = false)
	private Integer ordem;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ROTEIRO", nullable = false)
	private TipoRoteiro tipoRoteiro;

	public Roteiro() {
    }
	
    public Roteiro(String descricaoRoteiro, Integer ordem, TipoRoteiro tipoRoteiro) {
        this.descricaoRoteiro = descricaoRoteiro;
        this.ordem = ordem;
        this.tipoRoteiro = tipoRoteiro;
    }

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
	
	public Roteirizacao getRoteirizacao() {
		return roteirizacao;
	}

	public void setRoteirizacao(Roteirizacao roteirizacao) {
		this.roteirizacao = roteirizacao;
	}

	public List<Rota> getRotas() {
		return rotas;
	}

	public void setRotas(List<Rota> rotas) {
		this.rotas = rotas;
	}

	public TipoRoteiro getTipoRoteiro() {
		return tipoRoteiro;
	}

	
	public void setTipoRoteiro(TipoRoteiro tipoRoteiro) {
		this.tipoRoteiro = tipoRoteiro;
	}
	
	/**
	 * Adiciona uma nova rota ao Roteiro
	 * @param rota: Rota para inclusão
	 */
	public void addRota(Rota rota) {
		if (rotas == null) {
			rotas = new ArrayList<Rota>();
		}
		rota.setRoteiro(this);
		rotas.add(rota);
	}
	
	/**
	 * Adiciona novas Rotas ao Roteiro
	 * @param listaRota: List<Rota> para inclusão
	 */
	public void addAllRota(List<Rota> listaRota){
		if (rotas == null){
			rotas = new ArrayList<Rota>();
		}
		rotas.addAll(listaRota);
	}
}