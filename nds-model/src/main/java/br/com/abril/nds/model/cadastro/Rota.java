package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;

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
	@JoinColumn(name = "ROTEIRO_ID")
	private Roteiro roteiro;
	
	@OneToMany(mappedBy = "rota", orphanRemoval = true)
	@Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private List<RotaPDV> rotaPDVs =  new ArrayList<RotaPDV>();
	
	@Column(name="ORDEM", nullable = false)
	private Integer ordem;
	
    public Rota() {
    }
	
	public Rota(String descricaoRota, Integer ordem) {
        this.descricaoRota = descricaoRota;
        this.ordem = ordem;
    }

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

    /**
     * @return the rotaPDVs
     */
    public List<RotaPDV> getRotaPDVs() {
        return rotaPDVs;
    }

    /**
     * @param rotaPDVs the rotaPDVs to set
     */
    public void setRotaPDVs(List<RotaPDV> rotaPDVs) {
        this.rotaPDVs = rotaPDVs;
    }
    
    /**
     * Adiciona um PDV à Rota
     * @param pdv pdv para inclusão
     * @param ordem ordem do PDV na Rota
     * @return {@link RotaPDV} que representa a associação
     */
    public RotaPDV addPDV(PDV pdv, Integer ordem) {
        if (rotaPDVs == null) {
            rotaPDVs = new ArrayList<RotaPDV>();
        }
        RotaPDV rotaPDV = new RotaPDV(this, pdv, ordem);
        rotaPDVs.add(rotaPDV);
        return rotaPDV;
    }
	
}