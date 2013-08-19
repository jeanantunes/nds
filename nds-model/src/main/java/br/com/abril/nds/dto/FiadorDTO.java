package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO com as informações de Fiador
 * @author francisco.garcia
 *
 */
public class FiadorDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String nome;
    
    private String documento;
    
    private EnderecoDTO enderecoPrincipal;
    
    private TelefoneDTO telefonePrincipal;
    
    private List<GarantiaDTO> garantias;
    
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
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }


    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the enderecoPrincipal
     */
    public EnderecoDTO getEnderecoPrincipal() {
        return enderecoPrincipal;
    }

    /**
     * @param enderecoPrincipal the enderecoPrincipal to set
     */
    public void setEnderecoPrincipal(EnderecoDTO enderecoPrincipal) {
        this.enderecoPrincipal = enderecoPrincipal;
    }

    /**
     * @return the telefonePrincipal
     */
    public TelefoneDTO getTelefonePrincipal() {
        return telefonePrincipal;
    }

    /**
     * @param telefonePrincipal the telefonePrincipal to set
     */
    public void setTelefonePrincipal(TelefoneDTO telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    /**
     * @return the garantias
     */
    public List<GarantiaDTO> getGarantias() {
        return garantias;
    }


    /**
     * @param garantias the garantias to set
     */
    public void setGarantias(List<GarantiaDTO> garantias) {
        this.garantias = garantias;
    }
    
    /**
     * Adiciona uma garantia ao fiador
     * 
     * @param descricao
     *            descrição da garantia
     * @param valor
     *            valor da garantia
     */
    public void addGarantia(String descricao, BigDecimal valor) {
        if (this.garantias == null) {
            this.garantias = new ArrayList<FiadorDTO.GarantiaDTO>();
        }
        this.garantias.add(new GarantiaDTO(valor, descricao));
    }
  

    public static class GarantiaDTO {
        
        public GarantiaDTO(BigDecimal valor, String descricao) {
            this.valor = valor;
            this.descricao = descricao;
        }

        private BigDecimal valor;
        
        private String descricao;

        /**
         * @return the valor
         */
        public BigDecimal getValor() {
            return valor;
        }

        /**
         * @param valor the valor to set
         */
        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }

        /**
         * @return the descricao
         */
        public String getDescricao() {
            return descricao;
        }

        /**
         * @param descricao the descricao to set
         */
        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }
        
    }

}
