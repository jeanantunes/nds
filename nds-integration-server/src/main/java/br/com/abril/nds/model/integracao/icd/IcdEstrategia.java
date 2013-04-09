package br.com.abril.nds.model.integracao.icd;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class IcdEstrategia {

    private String tipoDocumento;
    @Id
    private Long codigoEstrategia;
    private String codigoProduto;
    private Integer numeroEdicao;
    private Integer codigoDistribuidor;
    private String oportunidadeVenda;
    private Long reparteMinimo;
    private Integer abrangenciaDistribuicao;
    private Integer peso;
    private String cesta;
    @OneToMany(mappedBy = "estrategia")
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
    public String getCodigoProduto() {
        return codigoProduto;
    }
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    public Integer getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(Integer numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public Integer getCodigoDistribuidor() {
        return codigoDistribuidor;
    }
    public void setCodigoDistribuidor(Integer codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }
    public String getOportunidadeVenda() {
        return oportunidadeVenda;
    }
    public void setOportunidadeVenda(String oportunidadeVenda) {
        this.oportunidadeVenda = oportunidadeVenda;
    }
    public Long getReparteMinimo() {
        return reparteMinimo;
    }
    public void setReparteMinimo(Long reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }
    public Integer getAbrangenciaDistribuicao() {
        return abrangenciaDistribuicao;
    }
    public void setAbrangenciaDistribuicao(Integer abrangenciaDistribuicao) {
        this.abrangenciaDistribuicao = abrangenciaDistribuicao;
    }
    public Integer getPeso() {
        return peso;
    }
    public void setPeso(Integer peso) {
        this.peso = peso;
    }
    public String getCesta() {
        return cesta;
    }
    public void setCesta(String cesta) {
        this.cesta = cesta;
    }
    public List<IcdEdicaoBaseEstrategia> getBasesEstrategia() {
        return basesEstrategia;
    }
    public void setBasesEstrategia(List<IcdEdicaoBaseEstrategia> basesEstrategia) {
        this.basesEstrategia = basesEstrategia;
    }
}
