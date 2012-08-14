package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;

/**
 * Paramêtros para termo de adesão de entrega em bancas
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class ParametroEntregaBanca implements Serializable {

    private static final long serialVersionUID = 1L;
    
	/**
	 * Flag indicando se é utilizado termo de adesão para entrega em bancas
	 */
    @Column(name = "UTILIZA_ADESAO_ENTREGA_BANCA")
    private boolean utilizaTermoAdesao;
	
	/**
	 * Complemento do termo de adesão de entrega em bancas, caso seja utilizado
	 */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "COMPLEMENTO_ADESAO_ENTREGA_BANCA")
	private String complementoTermoAdesao;

    /**
     * @return the utilizaTermoAdesao
     */
    public boolean isUtilizaTermoAdesao() {
        return utilizaTermoAdesao;
    }

    /**
     * @param utilizaTermoAdesao the utilizaTermoAdesao to set
     */
    public void setUtilizaTermoAdesao(boolean utilizaTermoAdesao) {
        this.utilizaTermoAdesao = utilizaTermoAdesao;
    }

    /**
     * @return the complementoTermoAdesao
     */
    public String getComplementoTermoAdesao() {
        return complementoTermoAdesao;
    }

    /**
     * @param complementoTermoAdesao the complementoTermoAdesao to set
     */
    public void setComplementoTermoAdesao(String complementoTermoAdesao) {
        this.complementoTermoAdesao = complementoTermoAdesao;
    }

}
