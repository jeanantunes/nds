package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;

/**
 * DTO com as informações do fechamento diário
 * 
 * @author francisco.garcia
 *
 */
public class FechamentoDiarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataFechamento;
    
    private ResumoReparteFecharDiaDTO resumoReparte;
    
    private ResumoEncalheFecharDiaDTO resumoEncalhe;
    
    private ResumoSuplementarFecharDiaDTO resumoSuplementar;

    private List<SumarizacaoDividasDTO> dividasReceber;
    
    private List<SumarizacaoDividasDTO> dividasVencer;
    
    private ResumoFechamentoDiarioCotasDTO resumoCotas;
    
    private ResumoEstoqueDTO resumoEstoque;
    
    private ResumoFechamentoDiarioConsignadoDTO resumoConsignado;

    private FechamentoDiarioDTO(Builder builder) {
        this.dataFechamento = builder.dataFechamento;
        this.resumoReparte = builder.resumoReparte;
        this.resumoEncalhe = builder.resumoEncalhe;
        this.resumoSuplementar = builder.resumoSuplementar;
        this.dividasReceber = builder.dividasReceber;
        this.dividasVencer = builder.dividasVencer;
        this.resumoCotas = builder.resumoCotas;
        this.resumoEstoque = builder.resumoEstoque;
        this.resumoConsignado = builder.resumoConsignado;
    }

    /**
     * @return the dataFechamento
     */
    public Date getDataFechamento() {
        return dataFechamento;
    }

    /**
     * @return the resumoReparte
     */
    public ResumoReparteFecharDiaDTO getResumoReparte() {
        return resumoReparte;
    }

    /**
     * @return the resumoEncalhe
     */
    public ResumoEncalheFecharDiaDTO getResumoEncalhe() {
        return resumoEncalhe;
    }

    /**
     * @return the resumoSuplementar
     */
    public ResumoSuplementarFecharDiaDTO getResumoSuplementar() {
        return resumoSuplementar;
    }

    /**
     * @return the dividasReceber
     */
    public List<SumarizacaoDividasDTO> getDividasReceber() {
        return dividasReceber;
    }

    /**
     * @return the dividasVencer
     */
    public List<SumarizacaoDividasDTO> getDividasVencer() {
        return dividasVencer;
    }

    /**
     * @return the resumoCotas
     */
    public ResumoFechamentoDiarioCotasDTO getResumoCotas() {
        return resumoCotas;
    }

    /**
     * @return the resumoEstoque
     */
    public ResumoEstoqueDTO getResumoEstoque() {
        return resumoEstoque;
    }
    
    /**
     * @return the resumoConsignado
     */
    public ResumoFechamentoDiarioConsignadoDTO getResumoConsignado() {
        return resumoConsignado;
    }

    /**
     * Builder para o DTO de fechamento diário
     *
     */
    public static class Builder {
        
        private Date dataFechamento;
        
        private ResumoReparteFecharDiaDTO resumoReparte;
        
        private ResumoEncalheFecharDiaDTO resumoEncalhe;
        
        private ResumoSuplementarFecharDiaDTO resumoSuplementar;

        private List<SumarizacaoDividasDTO> dividasReceber;
        
        private List<SumarizacaoDividasDTO> dividasVencer;
        
        private ResumoFechamentoDiarioCotasDTO resumoCotas;
        
        private ResumoEstoqueDTO resumoEstoque;
        
        private ResumoFechamentoDiarioConsignadoDTO resumoConsignado;
        
        public Builder(Date dataFechamento) {
            this.dataFechamento = dataFechamento;
        }
        
        public Builder resumoReparte(ResumoReparteFecharDiaDTO resumoReparte) {
            this.resumoReparte = resumoReparte;
            return this;
        }
        
        public Builder resumoEncalhe(ResumoEncalheFecharDiaDTO resumoEncalhe) {
            this.resumoEncalhe = resumoEncalhe;
            return this;
        }
        
        public Builder resumoSuplementar(ResumoSuplementarFecharDiaDTO resumoSuplementar) {
            this.resumoSuplementar = resumoSuplementar;
            return this;
        }
        
        public Builder dividasReceber(List<SumarizacaoDividasDTO> dividasReceber) {
            this.dividasReceber = dividasReceber;
            return this;
        }
        
        public Builder dividasVencer(List<SumarizacaoDividasDTO> dividasVencer) {
            this.dividasVencer = dividasVencer;
            return this;
        }
        
        public Builder resumoCotas(ResumoFechamentoDiarioCotasDTO resumoCotas) {
            this.resumoCotas = resumoCotas;
            return this;
        }
        
        public Builder resumoEstoque(ResumoEstoqueDTO resumoEstoque) {
            this.resumoEstoque = resumoEstoque;
            return this;
        }
        
        public Builder resumoConsignado(ResumoFechamentoDiarioConsignadoDTO resumoConsignado) {
            this.resumoConsignado = resumoConsignado;
            return this;
        }

        public FechamentoDiarioDTO build() {
            return new FechamentoDiarioDTO(this);
        }
        
    }
    
}
