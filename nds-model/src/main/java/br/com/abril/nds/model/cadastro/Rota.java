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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;

@Entity
@Table(name = "ROTA")
@SequenceGenerator(name = "ROTA_SEQ", initialValue = 1, allocationSize = 1)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Rota implements Serializable {

	/**
     * Serial Version UID
     */
	private static final long serialVersionUID = -6297324866478453809L;
	
	
    @Id
    @GeneratedValue(generator = "ROTA_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "DESCRICAO_ROTA")
    private String descricaoRota;

    @ManyToOne
    @JoinColumn(name = "ROTEIRO_ID")
    private Roteiro roteiro;
    
    @OneToOne(mappedBy = "rota")
    private Entregador entregador;

    @OneToMany(mappedBy = "rota", orphanRemoval = true)
    @Cascade(value = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.SAVE_UPDATE, CascadeType.DELETE })
    @OrderBy("ordem ASC")
    private List<RotaPDV> rotaPDVs = new ArrayList<RotaPDV>();

    @Column(name = "ORDEM", nullable = false)
    private Integer ordem;

    @OneToMany(mappedBy = "rota")
	private List<AssociacaoVeiculoMotoristaRota> associacoesVeiculoMotoristaRota;
    
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
     * @param rotaPDVs
     *            the rotaPDVs to set
     */
    public void setRotaPDVs(List<RotaPDV> rotaPDVs) {
        this.rotaPDVs = rotaPDVs;
    }

    /**
     * Adiciona um PDV à Rota
     * 
     * @param pdv
     *            pdv para inclusão
     * @param ordem
     *            ordem do PDV na Rota
     * @param box 
     * 			  box da roteirização
     * 
     * @return {@link RotaPDV} que representa a associação
     */
    public RotaPDV addPDV(PDV pdv, Integer ordem, Box box) {
        if (ordem <= 0) {
            throw new IllegalArgumentException("Ordem [" + ordem
                    + "] para o PDV não é válida!");
        }

        if (rotaPDVs == null) {
            rotaPDVs = new ArrayList<RotaPDV>();
        }
        RotaPDV rotaPDV = new RotaPDV(this, pdv, ordem);
        rotaPDVs.add(rotaPDV);
        
        if (pdv.getCaracteristicas().isPontoPrincipal() && box != null) {
        	
        	pdv.getCota().setBox(box);
        }
        
        return rotaPDV;
    }

    /**
     * Altera a ordem da Associação RotaPDV
     * 
     * @param idPdv identificador do PDV para alteração
     * @param ordem nova ordem do PDV
     * @throws IllegalArgumentException caso a ordem não for válida
     */
    public void alterarOrdemPdv(Long idPdv, Integer ordem) {
        
    	if (ordem <= 0) {
        
    		throw new IllegalArgumentException("Ordem [" + ordem + "] para o PDV não é válida!");
        
    	} else {
            
        	RotaPDV rotaPdvAlteracao = getRotaPDVPorPDV(idPdv);
                    
        	rotaPdvAlteracao.setOrdem(ordem);
        }
    }

    /**
     * @return the entregador
     */
    public Entregador getEntregador() {
        return entregador;
    }

    /**
     * @param entregador
     *            the entregador to set
     */
    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    /**
     * Desassocia os PDVs da Rota de acordo com os identificadores de PDV
     * recebidos
     * 
     * @param pdvsExclusao
     *            coleção de identificadores de PDV para exclusão
     */
    public void desassociarPDVs(Collection<Long> pdvsExclusao) {
        Iterator<RotaPDV> iterator = rotaPDVs.iterator();
        RotaPDV rotaPDVRemover = null;
        List<RotaPDV> listaRotaPDVRemover = new ArrayList<RotaPDV>();
        
        while (iterator.hasNext()) {
            RotaPDV rotaPDV = iterator.next();
            if (pdvsExclusao.contains(rotaPDV.getPdv().getId())) {
                
            	rotaPDVRemover = rotaPDV;
            	PDV pdv = rotaPDV.getPdv();
            	
            	final boolean tipoDeRoteiroNaoEspecial = !rotaPDV.isTipoRoteiroEspecial();
            	
            	if (pdv.getCaracteristicas().isPontoPrincipal() && tipoDeRoteiroNaoEspecial) {
            	
            		pdv.getCota().setBox(null);
            	}
            	listaRotaPDVRemover.add(rotaPDVRemover);
            	//iterator.remove();
            }
        }
        for(RotaPDV rotaPDVRemoverAux:listaRotaPDVRemover){
         rotaPDVs.remove(rotaPDVRemoverAux);
        }
    }

    /**
     * Recupera a associação de RotaPDV pelo identificador do PDV
     * 
     * @param idPDV
     *            identificador do PDV para recuperação da associação
     * @return PDV com o identificador ou null caso o PDV não esteja associado à
     *         Rota
     */
    public RotaPDV getRotaPDVPorPDV(Long idPDV) {
        for (RotaPDV rotaPdv : rotaPDVs) {
            PDV pdv = rotaPdv.getPdv();
            if (pdv.getId().equals(idPDV)) {
                return rotaPdv;
            }
        }
        return null;
    }

    /**
     * Recupera a associação de RotaPDV pela ordem
     * 
     * @param ordem
     *            ordem para recuperação da ordem
     * @return {@link RotaPDV} que possui a ordem recebida como parâmetro ou
     *         null caso não possua RotaPDV com a ordem
     */
    public RotaPDV getRotaPDVByOrdem(Integer ordem) {
        for (RotaPDV rotaPdv : rotaPDVs) {
            if (rotaPdv.getOrdem().equals(ordem)) {
                return rotaPdv;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Id: ");
        builder.append(id).append(" - Ordem: ").append(ordem)
                .append(" - Descrição: ").append(descricaoRota);
        return builder.toString();
    }

	public List<AssociacaoVeiculoMotoristaRota> getAssociacoesVeiculoMotoristaRota() {
		return associacoesVeiculoMotoristaRota;
	}

	public void setAssociacoesVeiculoMotoristaRota(
			List<AssociacaoVeiculoMotoristaRota> associacoesVeiculoMotoristaRota) {
		this.associacoesVeiculoMotoristaRota = associacoesVeiculoMotoristaRota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getRotaPDVs() == null) ? 0 : this.getRotaPDVs().hashCode());
		result = prime * result + ((this.getRoteiro() == null) ? 0 : this.getRoteiro().hashCode());
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
		Rota other = (Rota) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getRotaPDVs() == null) {
			if (other.getRotaPDVs() != null)
				return false;
		} else if (!this.getRotaPDVs().equals(other.getRotaPDVs()))
			return false;
		if (this.getRoteiro() == null) {
			if (other.getRoteiro() != null)
				return false;
		} else if (!this.getRoteiro().equals(other.getRoteiro()))
			return false;
		return true;
	}
}