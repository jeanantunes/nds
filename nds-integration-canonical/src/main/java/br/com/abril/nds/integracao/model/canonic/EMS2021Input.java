package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EMS2021Input extends IntegracaoDocumentMaster<EMS2021InputItem> implements Serializable {

    private static final long serialVersionUID = -3176527752303474423L;
    private String codigoDistribuidor;
    private String codigoProduto;
    private Long numeroEdicao;
    private Double abrangencia;
    private Long reparteMinimo;
    private Integer periodo;
    private String oportunidadeVenda;
    private String cesta;
    private List<EMS2021InputItem> itens = new ArrayList<EMS2021InputItem>();

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

    public String getOportunidadeVenda() {
        return oportunidadeVenda;
    }

    public void setOportunidadeVenda(String oportunidadeVenda) {
        this.oportunidadeVenda = oportunidadeVenda;
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

    public String getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public void setCodigoDistribuidor(String codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    public String getCesta() {
        return cesta;
    }

    public void setCesta(String cesta) {
        this.cesta = cesta;
    }

    public Long getReparteMinimo() {
        return reparteMinimo;
    }

    public void setReparteMinimo(Long reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }
}
