package br.com.abril.nds.model.integracao.icd;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class IcdEstrategia implements Serializable {

    private static final long serialVersionUID = -3153202639762497831L;

	private String tipoDocumento;

    private Long codigoEstrategia;
    private Long codigoDistribuidor;
    private Integer codigoPraca;
    private String codigoPublicacao;
    private Long numeroEdicao;
    private Integer periodo;
    private BigInteger codigoLancamentoEdicao;
    private BigInteger abrangenciaDistribuicao;
    private String oportunidadeVenda;
    private BigInteger reparteMinimo;
    private Date dataCriacao;
    private List<IcdEdicaoBaseEstrategia> basesEstrategia;

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getCodigoEstrategia() {
        return codigoEstrategia;
    }

    public void setCodigoEstrategia(Long codigoEstrategia) {
        this.codigoEstrategia = codigoEstrategia;
    }

    public Long getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public void setCodigoDistribuidor(Long codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    public Integer getCodigoPraca() {
        return codigoPraca;
    }

    public void setCodigoPraca(Integer codigoPraca) {
        this.codigoPraca = codigoPraca;
    }

    public String getCodigoPublicacao() {
        return codigoPublicacao;
    }

    public void setCodigoPublicacao(String codigoPublicacao) {
        this.codigoPublicacao = codigoPublicacao;
    }

    public Long getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(Long numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public BigInteger getCodigoLancamentoEdicao() {
        return codigoLancamentoEdicao;
    }

    public void setCodigoLancamentoEdicao(BigInteger codigoLancamentoEdicao) {
        this.codigoLancamentoEdicao = codigoLancamentoEdicao;
    }

    public BigInteger getAbrangenciaDistribuicao() {
        return abrangenciaDistribuicao;
    }

    public void setAbrangenciaDistribuicao(BigInteger abrangenciaDistribuicao) {
        this.abrangenciaDistribuicao = abrangenciaDistribuicao;
    }

    public String getOportunidadeVenda() {
        return oportunidadeVenda;
    }

    public void setOportunidadeVenda(String oportunidadeVenda) {
        this.oportunidadeVenda = oportunidadeVenda;
    }

    public BigInteger getReparteMinimo() {
        return reparteMinimo;
    }

    public void setReparteMinimo(BigInteger reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }

    public List<IcdEdicaoBaseEstrategia> getBasesEstrategia() {
        return basesEstrategia;
    }

    public void setBasesEstrategia(List<IcdEdicaoBaseEstrategia> basesEstrategia) {
        this.basesEstrategia = basesEstrategia;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    @Override
    public String toString() {
        return "IcdEstrategia{" +
                "tipoDocumento='" + tipoDocumento + '\'' +
                ", codigoEstrategia=" + codigoEstrategia +
                ", codigoDistribuidor=" + codigoDistribuidor +
                ", codigoPraca=" + codigoPraca +
                ", codigoPublicacao='" + codigoPublicacao + '\'' +
                ", numeroEdicao=" + numeroEdicao +
                ", periodo=" + periodo +
                ", codigoLancamentoEdicao=" + codigoLancamentoEdicao +
                ", abrangenciaDistribuicao=" + abrangenciaDistribuicao +
                ", oportunidadeVenda='" + oportunidadeVenda + '\'' +
                ", reparteMinimo=" + reparteMinimo +
                ", basesEstrategia=" + basesEstrategia +
                '}';
    }
}
