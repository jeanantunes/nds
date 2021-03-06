package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.cadastro.TipoTelefone;

/**
 * Entidade para os telefones utilizados no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaTelefone implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Número do telefone
     */
    @Column(name = "TELEFONE_NUMERO")
    private String numero;
    
    /**
     * Ramal do telefone
     */
    @Column(name = "TELEFONE_RAMAL")
    private String ramal;
    
    /**
     * DDD do telefone
     */
    @Column(name = "DDD")
    private String ddd;
    
    /**
     * Tipo do telefone
     */
    @Column(name = "TELEFONE_TIPO_TELEFONE")
    @Enumerated(EnumType.STRING)
    private TipoTelefone tipoTelefone;
    
    /**
     * Flag indicando se este é o telefone principal
     */
    @Column(name = "TELEFONE_PRINCIPAL")
    private boolean principal;
    
    public HistoricoTitularidadeCotaTelefone() {
    }

    public HistoricoTitularidadeCotaTelefone(String numero, String ramal,
            String ddd, TipoTelefone tipoTelefone, boolean principal) {
        this.numero = numero;
        this.ramal = ramal;
        this.ddd = ddd;
        this.tipoTelefone = tipoTelefone;
        this.principal = principal;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the ramal
     */
    public String getRamal() {
        return ramal;
    }

    /**
     * @param ramal the ramal to set
     */
    public void setRamal(String ramal) {
        this.ramal = ramal;
    }
    
    /**
     * @return the ddd
     */
    public String getDdd() {
        return ddd;
    }

    /**
     * @param ddd the ddd to set
     */
    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    /**
     * @return the tipoTelefone
     */
    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    /**
     * @param tipoTelefone the tipoTelefone to set
     */
    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    /**
     * @return the principal
     */
    public boolean isPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

}
