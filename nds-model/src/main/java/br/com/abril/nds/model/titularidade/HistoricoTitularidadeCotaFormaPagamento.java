package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * Representa as formas de pagamento no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_FORMA_PAGAMENTO")
@SequenceGenerator(name = "HIST_TIT_COTA_FORMA_PAGAMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaFormaPagamento implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_FORMA_PAGAMENTO_SEQ")
    @Column(name = "ID")
    private Long id;
    
    /**
     * Fornecedores associados à forma de pagamento
     */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "HISTORICO_TITULARIDADE_COTA_FORMA_PAGAMENTO_FORNECEDOR", 
        joinColumns = {@JoinColumn(name = "HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID")}, 
        inverseJoinColumns = {@JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID")})
    private Collection<HistoricoTitularidadeCotaFornecedor> fornecedores;
    
    /**
     * Tipo de pagamento
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_COBRANCA", nullable = false)
    private TipoCobranca tipoCobranca;
    
    /**
     * Banco associado a forma de pagamento
     */
    @Embedded
    private HistoricoTitularidadeCotaBanco banco;
    
    /**
     * Concentrações de pagamento
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID")
    private HistoricoTitularidadeCotaConcentracaoCobranca concentracaoCobranca;

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
     * @return the fornecedores
     */
    public Collection<HistoricoTitularidadeCotaFornecedor> getFornecedores() {
        return fornecedores;
    }

    /**
     * @param fornecedores the fornecedores to set
     */
    public void setFornecedores(
            Collection<HistoricoTitularidadeCotaFornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

    /**
     * @return the tipoCobranca
     */
    public TipoCobranca getTipoCobranca() {
        return tipoCobranca;
    }

    /**
     * @param tipoCobranca the tipoCobranca to set
     */
    public void setTipoCobranca(TipoCobranca tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }

    /**
     * @return the banco
     */
    public HistoricoTitularidadeCotaBanco getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(HistoricoTitularidadeCotaBanco banco) {
        this.banco = banco;
    }

	/**
	 * @return the concentracaoCobranca
	 */
	public HistoricoTitularidadeCotaConcentracaoCobranca getConcentracaoCobranca() {
		return concentracaoCobranca;
	}

	/**
	 * @param concentracaoCobranca the concentracaoCobranca to set
	 */
	public void setConcentracaoCobranca(
			HistoricoTitularidadeCotaConcentracaoCobranca concentracaoCobranca) {
		this.concentracaoCobranca = concentracaoCobranca;
	}
}
