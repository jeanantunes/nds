package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity
@Table(name = "ROTEIRIZACAO")
@SequenceGenerator(name="ROTEIRIZACAO_SEQ", initialValue = 1, allocationSize = 1)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Roteirizacao implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8484659529985789201L;

	@Id
	@GeneratedValue(generator = "ROTEIRIZACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "BOX_ID", unique = true)
	private Box box;
	
	@OneToMany(orphanRemoval = true)
	@JoinColumn( name="ROTEIRIZACAO_ID")
	@Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	@OrderBy("ordem ASC")
	private List<Roteiro> roteiros = new ArrayList<Roteiro>();
	
	/**
	 * Construtor padrão
	 */
	public Roteirizacao() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the box
	 */
	public Box getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(Box box) {
		this.box = box;
	}

	/**
	 * @return the roteiros
	 */
	public List<Roteiro> getRoteiros() {
		return roteiros;
	}

	/**
	 * @param roteiros the roteiros to set
	 */
	public void setRoteiros(List<Roteiro> roteiros) {
		this.roteiros = roteiros;
	}

	/**
	 * Adiciona um novo roteiro à roteirização
	 * @param roteiro: Roteiro para inclusão
	 */
	public void addRoteiro(Roteiro roteiro) {
	    if (roteiro.getOrdem() <= 0) {
            throw new IllegalArgumentException("Ordem [" + roteiro.getOrdem()  + "] para o Roteiro não é válida!");
        }
       
	    if (roteiros == null) {
			roteiros = new ArrayList<Roteiro>();
		}
		roteiro.setRoteirizacao(this);
		roteiros.add(roteiro);
	}
	    
    /**
     * Desassocia os roteiros da roteirização de acordo com os identificadores
     * recebidos
     * 
     * @param idsRoteiros
     *            identificadores dos roteiros para desassociação
     */
	public void desassociarRoteiros(Collection<Long> idsRoteiros) {
        Iterator<Roteiro> iterator = roteiros.iterator();
        while(iterator.hasNext()) {
            Roteiro roteiro = iterator.next();
            if (idsRoteiros.contains(roteiro.getId())) {
                iterator.remove();
            }
        }
    }

    /**
     * Recupera o roteiro pelo identificador
     * 
     * @param id
     *            identificador do roteiro
     * @return Roteiro com o identificador recebido ou null caso não exista
     *         roteiro com o identificador
     */
	public Roteiro getRoteiro(Long id) {
        for (Roteiro roteiro : roteiros) {
            if (roteiro.getId().equals(id)) {
                return roteiro;
            }
        }
        return null;
    }
}
