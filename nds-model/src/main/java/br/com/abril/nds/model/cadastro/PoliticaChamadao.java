package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Política de Chamadão 
 * Parametrização do Distribuidor
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class PoliticaChamadao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "DIAS_SUSPENSO")
	private Integer diasSuspenso;
	
	@Column(name = "VALOR_CONSIGNADO")
	private BigDecimal valorConsignado;

    /**
     * @return the diasSuspenso
     */
    public Integer getDiasSuspenso() {
        return diasSuspenso;
    }

    /**
     * @param diasSuspenso the diasSuspenso to set
     */
    public void setDiasSuspenso(Integer diasSuspenso) {
        this.diasSuspenso = diasSuspenso;
    }

    /**
     * @return the valorConsignado
     */
    public BigDecimal getValorConsignado() {
        return valorConsignado;
    }

    /**
     * @param valorConsignado the valorConsignado to set
     */
    public void setValorConsignado(BigDecimal valorConsignado) {
        this.valorConsignado = valorConsignado;
    }

}
