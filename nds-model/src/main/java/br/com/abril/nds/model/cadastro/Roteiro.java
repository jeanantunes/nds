package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "ROTEIRO")
@SequenceGenerator(name="ROTEIRO_SEQ", initialValue = 1, allocationSize = 1)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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
	@JoinColumn(name = "ROTEIRIZACAO_ID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Roteirizacao roteirizacao;
	
	@OneToMany(orphanRemoval = true)
	@JoinColumn( name="ROTEIRO_ID")
	@Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	@OrderBy("ordem ASC")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
	    if (rota.getOrdem() <= 0) {
            throw new IllegalArgumentException("Ordem [" + rota.getOrdem()  + "] para o Rota não é válida!");
        }
       	    
	    if (rotas == null) {
			rotas = new ArrayList<Rota>();
		}
		rota.setRoteiro(this);
		rotas.add(rota);
	}
	
   
    /**
     * Desassocia as rotas de acordo com os identificadores
     * recebidos 
     * @param idsRotas coleção de identificadores das rotas
     * para desassociação
     */
	public void desassociarRotas(Collection<Long> idsRotas) {
	    Iterator<Rota> iterator = rotas.iterator();
	    while(iterator.hasNext()) {
	        Rota rota = iterator.next();
	        if (idsRotas.contains(rota.getId())) {
	        	
	            iterator.remove();
	        }
	    }
    }
	
    /**
     * Recupera a rota pelo identificador
     * 
     * @param idRota
     *            identificador da rota para recuperação
     * @return Rota com o identificador ou null caso não exista rota com este
     *         identificador
     */
	public Rota getRota(Long idRota) {
	    for (Rota rota : rotas) {
	        if (rota.getId().equals(idRota)) {
	            return rota;
	        }
	    }
	    return null;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Id: ");
        builder.append(id).append(" - Ordem: ").append(ordem)
                .append(" - Descrição: ").append(descricaoRoteiro);
        return builder.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getRotas() == null) ? 0 : this.getRotas().hashCode());
		result = prime * result + ((this.getRoteirizacao() == null) ? 0 : this.getRoteirizacao().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Roteiro other = (Roteiro) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getRotas() == null) {
			if (other.getRotas() != null)
				return false;
		} else if (!this.getRotas().equals(other.getRotas()))
			return false;
		if (this.getRoteirizacao() == null) {
			if (other.getRoteirizacao() != null)
				return false;
		} else if (!this.getRoteirizacao().equals(other.getRoteirizacao()))
			return false;
		return true;
	}
}