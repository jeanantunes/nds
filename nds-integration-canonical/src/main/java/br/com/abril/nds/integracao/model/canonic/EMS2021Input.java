package br.com.abril.nds.integracao.model.canonic;

import com.ancientprogramming.fixedformat4j.annotation.Record;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Record
public class EMS2021Input extends IntegracaoDocumentMaster<EMS2021InputItem> implements Serializable {

    private static final long serialVersionUID = -3176527752303474423L;
    private Long codigoDistribuidor;
    private Integer codigoPraca;
    private String codigoProduto;
    private Long numeroEdicao;
    private Integer periodo;
    private BigInteger codigoLancamentoEdicao;
    private Long abrangenciaDistribuicao;
    private String oportunidadeVenda;
    private BigInteger reparteMinimo;

    private String cesta;
    private List<EMS2021InputItem> itens = new ArrayList<EMS2021InputItem>();

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

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
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

    public Long getAbrangenciaDistribuicao() {
        return abrangenciaDistribuicao;
    }

    public void setAbrangenciaDistribuicao(Long abrangenciaDistribuicao) {
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

    public String getCesta() {
        return cesta;
    }

    public void setCesta(String cesta) {
        this.cesta = cesta;
    }

    @Override
    public void addItem(IntegracaoDocumentDetail docD) {
        itens.add((EMS2021InputItem) docD);
    }

    @Override
    public boolean sameObject(IntegracaoDocumentMaster<?> docM) {
        return false;
    }

    @Override
    public List<EMS2021InputItem> getItens() {
        return itens;
    }

    public void setItens(List<EMS2021InputItem> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "EMS2021Input{" +
                "codigoDistribuidor=" + codigoDistribuidor +
                ", codigoPraca=" + codigoPraca +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", numeroEdicao=" + numeroEdicao +
                ", periodo=" + periodo +
                ", codigoLancamentoEdicao=" + codigoLancamentoEdicao +
                ", abrangenciaDistribuicao=" + abrangenciaDistribuicao +
                ", oportunidadeVenda='" + oportunidadeVenda + '\'' +
                ", reparteMinimo=" + reparteMinimo +
                ", cesta='" + cesta + '\'' +
                ", itens=" + itens +
                '}';
    }}
