package br.com.abril.nds.model.titularidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;

/**
 * Representa a o período de funcionamento do pdv, utilizado para armazenamento
 * das informações do PDV no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeFuncionamentoPDV {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "FUNCIONAMENTO_PDV")
    private TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV;
    
    @Temporal(TemporalType.TIME)
    @Column(name = "HORARIO_INICIO")
    private Date horarioInicio;
    
    @Temporal(TemporalType.TIME)
    @Column(name = "HORARIO_FIM")
    private Date horarioFim;

    /**
     * @return the tipoPeriodoFuncionamentoPDV
     */
    public TipoPeriodoFuncionamentoPDV getTipoPeriodoFuncionamentoPDV() {
        return tipoPeriodoFuncionamentoPDV;
    }

    /**
     * @param tipoPeriodoFuncionamentoPDV the tipoPeriodoFuncionamentoPDV to set
     */
    public void setTipoPeriodoFuncionamentoPDV(
            TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV) {
        this.tipoPeriodoFuncionamentoPDV = tipoPeriodoFuncionamentoPDV;
    }

    /**
     * @return the horarioInicio
     */
    public Date getHorarioInicio() {
        return horarioInicio;
    }

    /**
     * @param horarioInicio the horarioInicio to set
     */
    public void setHorarioInicio(Date horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    /**
     * @return the horarioFim
     */
    public Date getHorarioFim() {
        return horarioFim;
    }

    /**
     * @param horarioFim the horarioFim to set
     */
    public void setHorarioFim(Date horarioFim) {
        this.horarioFim = horarioFim;
    }

}
