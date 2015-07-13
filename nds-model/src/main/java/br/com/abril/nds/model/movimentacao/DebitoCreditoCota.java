package br.com.abril.nds.model.movimentacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

//DTO transformado em entidade para melhorar desempenho na impressão do slip + boleto em massa
//foi evitado qualquer tipo de relacionamento que poderia comprometer o desempenho da funcionalidade
//evite usar essa entidade para outras consultas

@Exportable
@Entity
@Table(name = "DEBITO_CREDITO_COTA")
@SequenceGenerator(name="DEBITO_CREDITO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class DebitoCreditoCota implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="ID")
    @GeneratedValue(generator = "DEBITO_CREDITO_COTA_SEQ")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="SLIP_ID")
    private Slip slip;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TIPO_LANCAMENTO")
    private OperacaoFinaceira tipoLancamento;
    
    @Column(name="VALOR", precision=18, scale=4)
    @Export(label = "Valor R$", exhibitionOrder = 2, columnType = ColumnType.MOEDA)
    private BigDecimal valor;
    
    @Temporal(TemporalType.DATE)
    @Column(name="DATA_LANCAMENTO")
    @Export(label = "Data", exhibitionOrder = 0)
    private Date dataLancamento;
    
    @Temporal(TemporalType.DATE)
    @Column(name="TIPO_VENCIMENTO")
    private Date dataVencimento;
    
    @Column(name="OBSERVACOES")
    @Export(label = "Observação", exhibitionOrder = 3)
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TIPO_MOVIMENTO")
    @Export(label = "Tipo Movimento", exhibitionOrder = 1)
    private GrupoMovimentoFinaceiro tipoMovimento;
    
    @Column(name="TIPO_DESCRICAO")
    private String descricao;
    
    @Column(name="COMPOSICAO_COBRANCA")
    private boolean composicaoCobranca;
    
    public DebitoCreditoCota(){}
    
    public DebitoCreditoCota(final BigDecimal valor, final String observacoes, final OperacaoFinaceira tipoLancamento, 
            final GrupoMovimentoFinaceiro tipoMovimento, final Date dataLancamento, final Date dataVencimento){
        
        this.setValor(valor);
        this.setObservacoes((observacoes == null ? tipoMovimento.getDescricao() : observacoes) + 
                " (" + DateUtil.formatarDataPTBR(dataVencimento) + ")");
        this.setTipoLancamento(tipoLancamento);
        this.setTipoMovimento(tipoMovimento);
        this.setDataLancamento(dataLancamento);
    }
    
    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public OperacaoFinaceira getTipoLancamento() {
        return tipoLancamento;
    }
    
    public String getTipoLancamentoDescricao() {
        return (tipoLancamento!=null)? tipoLancamento.getDescricao():null;
    }

    //usado em query, populado pelo hibernate
    public void setTipoLancamento(OperacaoFinaceira tipoLancamento) {
        
        this.tipoLancamento = tipoLancamento;
    }

    //usado em query, populado pelo hibernate
    public void setTipoLancamentoDescricao(String tipoLancamentoDescricao) {
        this.tipoLancamento = OperacaoFinaceira.valueOf(tipoLancamentoDescricao);
    }

    
    
    //usado em métodos que usam esse DTO
    public void setTipoLancamentoEnum(OperacaoFinaceira operacaoFinaceira) {
        
        this.tipoLancamento = operacaoFinaceira;
    }

    /**
     * @return the observacoes
     */
    public String getObservacoes() {
        return observacoes;
    }

    /**
     * @param observacoes the observacoes to set
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public GrupoMovimentoFinaceiro getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(GrupoMovimentoFinaceiro tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Slip getSlip() {
        return slip;
    }

    
    public void setSlip(Slip slip) {
        this.slip = slip;
    }

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public boolean isComposicaoCobranca() {
        return composicaoCobranca;
    }

    
    public void setComposicaoCobranca(boolean composicaoCobranca) {
        this.composicaoCobranca = composicaoCobranca;
    }
}
