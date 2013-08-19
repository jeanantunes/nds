package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;

/**
 * DTO com as informações do fechamento diário
 * 
 * @author francisco.garcia
 *
 */
public class FechamentoDiarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataFechamento;
    
    private Sumarizacao sumarizacao;
    
    private List<ReparteFecharDiaDTO> reparte = new ArrayList<ReparteFecharDiaDTO>();
    
    private List<EncalheFecharDiaDTO> encalhe = new ArrayList<EncalheFecharDiaDTO>();
    
    private List<SuplementarFecharDiaDTO> suplementar = new ArrayList<SuplementarFecharDiaDTO>();
    
    private List<DiferencaDTO> faltasSobras = new ArrayList<DiferencaDTO>();

    private FechamentoDiarioDTO(Builder builder) {
        this.dataFechamento = builder.dataFechamento;
      
        this.sumarizacao = new Sumarizacao();
        this.sumarizacao.resumoReparte = builder.resumoReparte;
        this.sumarizacao.resumoEncalhe = builder.resumoEncalhe;
        this.sumarizacao.resumoSuplementar = builder.resumoSuplementar;
        this.sumarizacao.dividasReceber = builder.dividasReceber;
        this.sumarizacao.dividasVencer = builder.dividasVencer;
        this.sumarizacao.resumoCotas = builder.resumoCotas;
        this.sumarizacao.resumoEstoque = builder.resumoEstoque;
        this.sumarizacao.resumoConsignado = builder.resumoConsignado;
        
        this.reparte = builder.reparte;
        this.encalhe = builder.encalhe;
        this.suplementar = builder.suplementar;      
        this.faltasSobras = builder.faltasSobras;
    }

    /**
     * @return the dataFechamento
     */
    public Date getDataFechamento() {
        return dataFechamento;
    }
    
    /**
     * @return the sumarizacao
     */
    public Sumarizacao getSumarizacao() {
        return sumarizacao;
    }
    
    /**
     * @return the reparte
     */
    public List<ReparteFecharDiaDTO> getReparte() {
        return reparte;
    }

    /**
     * @return the encalhe
     */
    public List<EncalheFecharDiaDTO> getEncalhe() {
        return encalhe;
    }
    
    /**
     * @return the suplementar
     */
    public List<SuplementarFecharDiaDTO> getSuplementar() {
        return suplementar;
    }

    /**
     * @return the faltasSobras
     */
    public List<DiferencaDTO> getFaltasSobras() {
        return faltasSobras;
    }


    /**
     * Builder para o DTO de fechamento diário
     *
     */
    public static class Builder {
        
        private Date dataFechamento;
        
        private SumarizacaoReparteDTO resumoReparte;
        
        private ResumoEncalheFecharDiaDTO resumoEncalhe;
        
        private ResumoSuplementarFecharDiaDTO resumoSuplementar;

        private List<SumarizacaoDividasDTO> dividasReceber;
        
        private List<SumarizacaoDividasDTO> dividasVencer;
        
        private ResumoFechamentoDiarioCotasDTO resumoCotas;
        
        private ResumoEstoqueDTO resumoEstoque;
        
        private ResumoFechamentoDiarioConsignadoDTO resumoConsignado;
        
        private List<ReparteFecharDiaDTO> reparte = new ArrayList<ReparteFecharDiaDTO>();
        
        private List<EncalheFecharDiaDTO> encalhe = new ArrayList<EncalheFecharDiaDTO>();
        
        private List<SuplementarFecharDiaDTO> suplementar = new ArrayList<SuplementarFecharDiaDTO>();
        
        private List<DiferencaDTO> faltasSobras = new ArrayList<DiferencaDTO>();
        
        public Builder(Date dataFechamento) {
            this.dataFechamento = dataFechamento;
        }
        
        public Builder resumoReparte(SumarizacaoReparteDTO resumoReparte) {
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
        
        public Builder reparte(List<ReparteFecharDiaDTO> reparte) {
            this.reparte = reparte;
            return this;
        }
        
        public Builder encalhe(List<EncalheFecharDiaDTO> encalhe) {
            this.encalhe = encalhe;
            return this;
        }
        
        public Builder suplementar(List<SuplementarFecharDiaDTO> suplementar) {
            this.suplementar = suplementar;
            return this;
        }
        
        public Builder faltasSobras(List<DiferencaDTO> faltasSobras) {
            this.faltasSobras = faltasSobras;
            return this;
        }

        public FechamentoDiarioDTO build() {
            return new FechamentoDiarioDTO(this);
        }
        
    }
    
    /**
     * Sumarização do Fechamento diário
  
     * @author francisco.garcia
     *
     */
    public static class Sumarizacao {
        
        private SumarizacaoReparteDTO resumoReparte;
        
        private ResumoEncalheFecharDiaDTO resumoEncalhe;
        
        private ResumoSuplementarFecharDiaDTO resumoSuplementar;

        private List<SumarizacaoDividasDTO> dividasReceber;
        
        private List<SumarizacaoDividasDTO> dividasVencer;
        
        private ResumoFechamentoDiarioCotasDTO resumoCotas;
        
        private ResumoEstoqueDTO resumoEstoque;
        
        private ResumoFechamentoDiarioConsignadoDTO resumoConsignado;

        /**
         * @return the resumoReparte
         */
        public SumarizacaoReparteDTO getResumoReparte() {
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
        
    }
    
}
