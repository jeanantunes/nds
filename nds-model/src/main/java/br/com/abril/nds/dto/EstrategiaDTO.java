package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public class EstrategiaDTO {

    private ProdutoEdicao produtoEdicao;
    private BigInteger reparteMinimo;
    private BigDecimal abrangencia;
    private Integer periodo;
    private String oportunidadeVenda;
    private String cesta;
    private List<EdicaoBaseEstrategiaDTO> basesEstrategia;

    public ProdutoEdicao getProdutoEdicao() {
        return produtoEdicao;
    }
    public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
        this.produtoEdicao = produtoEdicao;
    }
    public BigInteger getReparteMinimo() {
        return reparteMinimo;
    }
    public void setReparteMinimo(BigInteger reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }
    public BigDecimal getAbrangencia() {
        return abrangencia;
    }
    public void setAbrangencia(BigDecimal abrangencia) {
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
    public String getCesta() {
        return cesta;
    }
    public void setCesta(String cesta) {
        this.cesta = cesta;
    }
    public List<EdicaoBaseEstrategiaDTO> getBasesEstrategia() {
        return basesEstrategia;
    }
    public void setBasesEstrategia(List<EdicaoBaseEstrategiaDTO> basesEstrategia) {
        this.basesEstrategia = basesEstrategia;
    }
}
