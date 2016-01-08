package br.com.abril.nds.model.cadastro.pdv;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.TipoRoteiro;

/**
 * Associação de Rota com PDV
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "ROTA_PDV")
@SequenceGenerator(name="ROTA_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class RotaPDV implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "ROTA_PDV_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ROTA_ID")
    private Rota rota;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "PDV_ID")
    private PDV pdv;
    
    @Column(name="ORDEM")
    private Integer ordem;
    
    public RotaPDV() {
    }
    
    public RotaPDV(Rota rota, PDV pdv, Integer ordem) {
        this.rota = rota;
        this.pdv = pdv;
        this.ordem = ordem;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the rota
     */
    public Rota getRota() {
        return rota;
    }

    /**
     * @param rota the rota to set
     */
    public void setRota(Rota rota) {
        this.rota = rota;
    }

    /**
     * @return the pdv
     */
    public PDV getPdv() {
        return pdv;
    }

    /**
     * @param pdv the pdv to set
     */
    public void setPdv(PDV pdv) {
        this.pdv = pdv;
    }

    /**
     * @return the ordem
     */
    public Integer getOrdem() {
        return ordem;
    }

    /**
     * @param ordem the ordem to set
     */
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    public boolean isTipoRoteiroEspecial(){
    	
    	if(getRota().getRoteiro()!= null){
    		
    		final TipoRoteiro tipoRoteiro = getRota().getRoteiro().getTipoRoteiro();
    		
    		return  (tipoRoteiro!= null && TipoRoteiro.ESPECIAL.equals(tipoRoteiro));
    	}
    	
    	return false;
    }

}
