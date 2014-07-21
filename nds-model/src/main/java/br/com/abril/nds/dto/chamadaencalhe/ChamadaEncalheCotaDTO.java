package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;


/**
 * DTO com as informações de Chamada de Encalhe da Cota
 * 
 */
public class ChamadaEncalheCotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private Long numeroChamadaEncalhe;
    
    private Long idCota;
    
    private Integer numeroCota;
    
    private boolean exigeNFE;
    
    private boolean contribuinteICMS;
    
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

	public Long getNumeroChamadaEncalhe() {
		return numeroChamadaEncalhe;
	}

	public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
		this.numeroChamadaEncalhe = numeroChamadaEncalhe;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public boolean isExigeNFE() {
		return exigeNFE;
	}

	public void setExigeNFE(boolean exigeNFE) {
		this.exigeNFE = exigeNFE;
	}

	public boolean isContribuinteICMS() {
		return contribuinteICMS;
	}

	public void setContribuinteICMS(boolean contribuinteICMS) {
		this.contribuinteICMS = contribuinteICMS;
	}

}