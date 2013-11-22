package br.com.abril.nds.model.integracao.icd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class IcdEstrategia implements Serializable {

    private static final long serialVersionUID = -3153202639762497831L;
	
	private String tipoDocumento;
    @Id
    private BigDecimal codigoEstrategia;
    private String codigoProduto;
    private BigDecimal numeroEdicao;
    private BigDecimal codigoDistribuidor;
    private String oportunidadeVenda;
    private BigDecimal reparteMinimo;
    private BigDecimal abrangenciaDistribuicao;
    private String cesta;
    @OneToMany(mappedBy = "estrategia", fetch = FetchType.LAZY)
    private List<IcdEdicaoBaseEstrategia> basesEstrategia;

    public IcdEstrategia() { }
    public IcdEstrategia(BigDecimal codigoEstrategia) {
    	this.codigoEstrategia = codigoEstrategia;
    }
    
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public BigDecimal getCodigoEstrategia() {
        return codigoEstrategia;
    }
    public void setCodigoEstrategia(BigDecimal codigoEstrategia) {
        this.codigoEstrategia = codigoEstrategia;
    }
    public String getCodigoProduto() {
        return codigoProduto;
    }
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    public BigDecimal getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(BigDecimal numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public BigDecimal getCodigoDistribuidor() {
        return codigoDistribuidor;
    }
    public void setCodigoDistribuidor(BigDecimal codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }
    public String getOportunidadeVenda() {
        return oportunidadeVenda;
    }
    public void setOportunidadeVenda(String oportunidadeVenda) {
        this.oportunidadeVenda = oportunidadeVenda;
    }
    public BigDecimal getReparteMinimo() {
        return reparteMinimo;
    }
    public void setReparteMinimo(BigDecimal reparteMinimo) {
        this.reparteMinimo = reparteMinimo;
    }
    public BigDecimal getAbrangenciaDistribuicao() {
        return abrangenciaDistribuicao;
    }
    public void setAbrangenciaDistribuicao(BigDecimal abrangenciaDistribuicao) {
        this.abrangenciaDistribuicao = abrangenciaDistribuicao;
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
