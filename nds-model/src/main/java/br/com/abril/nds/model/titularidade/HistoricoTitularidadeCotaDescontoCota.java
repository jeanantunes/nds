package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

/**
 * Entidade que representa o desconto do fornecedor
 * no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("COTA")
public class HistoricoTitularidadeCotaDescontoCota extends HistoricoTitularidadeCotaDesconto {
    
    private static final long serialVersionUID = 1L;


    /**
     * Tipo do desconto
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DESCONTO")
    protected TipoDesconto tipoDesconto;

    /**
     * Nome do fornecedor
     */
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

	/**
	 * @return the tipoDesconto
	 */
	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}

	/**
	 * @param tipoDesconto the tipoDesconto to set
	 */
	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
}
