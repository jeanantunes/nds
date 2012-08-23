package br.com.abril.nds.model.titularidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe Embeddable com informações de código e descrição, 
 * facilitando o mapeamento de entidades que possuem informações
 * populadas com informações oriundas de sistemas legados
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class HistoricoTitularidadeCodigoDescricao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Identificação de informação legada
     */
    @Column(name = "CODIGO", nullable = false)
    private Long codigo;
    
    /**
     * Descrição da informação
     */
    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
