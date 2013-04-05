package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EMS2021Input extends IntegracaoDocumentMaster<EMS2021InputItem> implements Serializable {

    private static final long serialVersionUID = -3176527752303474423L;
    private Long codigoProduto;
    private Integer numeroEdicao;
    private Double abrangencia;
    private Integer periodo;
    private Long reparteMinimo;
    private List<EMS2021InputItem> itens = new ArrayList<EMS2021InputItem>();

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(Integer numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    public Double getAbrangencia() {
        return abrangencia;
    }

    public void setAbrangencia(Double abrangencia) {
        this.abrangencia = abrangencia;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Long getReparteMinimo() {
        return reparteMinimo;
    }

    public void setReparteMinimo(Long reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }

    @Override
    public void addItem(IntegracaoDocumentDetail docD) {
	itens.add((EMS2021InputItem) docD);
    }

    @Override
    public List<EMS2021InputItem> getItems() {
	return itens;
    }

    @Override
    public boolean sameObject(IntegracaoDocumentMaster<?> docM) {
	return false;
    }
}
