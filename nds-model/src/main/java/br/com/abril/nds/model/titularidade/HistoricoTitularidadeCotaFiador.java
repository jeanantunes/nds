package br.com.abril.nds.model.titularidade;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import br.com.abril.nds.model.cadastro.TipoGarantia;

/**
 * Representa a garantia do tipo "FIADOR" no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("FIADOR")
public class HistoricoTitularidadeCotaFiador extends HistoricoTitularidadeCotaGarantia{
    
    private static final long serialVersionUID = 1L;
    
    public HistoricoTitularidadeCotaFiador() {
        this.tipoGarantia = TipoGarantia.FIADOR;
    }

    /**
     * Nome do fiador
     */
    @Column(name = "FIADOR_NOME")
    private String nome;
    
    /**
     * CPF/CNPJ do fiador
     */
    @Column(name = "FIADOR_CPF_CNPJ")
    private String cpfCnpj;
    
    /**
     * Garantias do fiador
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_FIADOR_GARANTIA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FIADOR_ID")})
    private Collection<HistoricoTitularidadeCotaFiadorGarantia> garantias;

    /**
     * Endereço do fiador
     */
    @Embedded
    private HistoricoTitularidadeCotaEndereco historicoTitularidadeCotaEndereco;
    
    /**
     * Telefone do fiador
     */
    @Embedded
    private HistoricoTitularidadeCotaTelefone historicoTitularidadeCotaTelefone;

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * @return the garantias
     */
    public Collection<HistoricoTitularidadeCotaFiadorGarantia> getGarantias() {
        return garantias;
    }

    /**
     * @param garantias the garantias to set
     */
    public void setGarantias(Collection<HistoricoTitularidadeCotaFiadorGarantia> garantias) {
        this.garantias = garantias;
    }

	/**
	 * @return the historicoTitularidadeCotaEndereco
	 */
	public HistoricoTitularidadeCotaEndereco getHistoricoTitularidadeCotaEndereco() {
		return historicoTitularidadeCotaEndereco;
	}

	/**
	 * @param historicoTitularidadeCotaEndereco the historicoTitularidadeCotaEndereco to set
	 */
	public void setHistoricoTitularidadeCotaEndereco(
			HistoricoTitularidadeCotaEndereco historicoTitularidadeCotaEndereco) {
		this.historicoTitularidadeCotaEndereco = historicoTitularidadeCotaEndereco;
	}

	/**
	 * @return the historicoTitularidadeCotaTelefone
	 */
	public HistoricoTitularidadeCotaTelefone getHistoricoTitularidadeCotaTelefone() {
		return historicoTitularidadeCotaTelefone;
	}

	/**
	 * @param historicoTitularidadeCotaTelefone the historicoTitularidadeCotaTelefone to set
	 */
	public void setHistoricoTitularidadeCotaTelefone(
			HistoricoTitularidadeCotaTelefone historicoTitularidadeCotaTelefone) {
		this.historicoTitularidadeCotaTelefone = historicoTitularidadeCotaTelefone;
	}

    /**
     * Adiciona uma nova garantia para o fiador
     * 
     * @param garantia
     *            garantia para inclusão
     */
	public void addGarantia(HistoricoTitularidadeCotaFiadorGarantia garantia) {
        if (this.garantias == null) {
            garantias = new ArrayList<HistoricoTitularidadeCotaFiadorGarantia>();
        }
        garantias.add(garantia);
    }
}
