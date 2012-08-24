package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

/**
 * Representa a concentração de cobrança no histórico de titularidade da cota
 * 
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_CONCENTRACAO_COBRANCA")
@SequenceGenerator(name = "HIST_TIT_COTA_CONCENTRACAO_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaConcentracaoCobranca implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_CONCENTRACAO_COBRANCA_SEQ")
    @Column(name = "ID")
    private Long id;
    
    /**
     * Tipo de periodiciodade da cobrança
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_FORMA_COBRANCA", nullable = false)
    private TipoFormaCobranca tipoFormaCobranca;
    
    /**
     * Dias da semana para cobrança
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_CONCENTRACAO_COBRANCA_DIA_SEMANA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID")})
    @Column(name = "DIA_SEMANA")
    private Collection<DiaSemana> diasSemana;
    
    /**
     * Dias do mês para cobrança
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_CONCENTRACAO_COBRANCA_DIA_MES", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID")})
    @Column(name = "DIA_MES")
    private Collection<Integer> diasMes;

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
     * @return the tipoFormaCobranca
     */
    public TipoFormaCobranca getTipoFormaCobranca() {
        return tipoFormaCobranca;
    }

    /**
     * @param tipoFormaCobranca the tipoFormaCobranca to set
     */
    public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
        this.tipoFormaCobranca = tipoFormaCobranca;
    }

    /**
     * @return the diasSemana
     */
    public Collection<DiaSemana> getDiasSemana() {
        return diasSemana;
    }

    /**
     * @param diasSemana the diasSemana to set
     */
    public void setDiasSemana(Collection<DiaSemana> diasSemana) {
        this.diasSemana = diasSemana;
    }

    /**
     * @return the diasMes
     */
    public Collection<Integer> getDiasMes() {
        return diasMes;
    }

    /**
     * @param diasMes the diasMes to set
     */
    public void setDiasMes(Collection<Integer> diasMes) {
        this.diasMes = diasMes;
    }

}
