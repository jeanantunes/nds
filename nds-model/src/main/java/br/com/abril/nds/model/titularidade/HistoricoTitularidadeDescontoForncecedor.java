package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Entidade que representa o desconto do fornecedor
 * no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("FORNECEDOR")
public class HistoricoTitularidadeDescontoForncecedor extends HistoricoTitularidadeDescontoCota {
    
    @Column(name = "NOME_FORNECEDOR")
    private String fornecedor;

    /**
     * @return the fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

}
