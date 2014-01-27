package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ESTRATEGIA")
@SequenceGenerator(name = "ESTRATEGIA_SEQ", initialValue = 1, allocationSize = 1)
public class Estrategia implements Serializable {

    private static final long serialVersionUID = -7650183772911488150L;

    @Id
    @GeneratedValue(generator = "ESTRATEGIA_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false)
    private ProdutoEdicao produtoEdicao;

    @Column(name = "REPARTE_MINIMO")
    private BigInteger reparteMinimo;

    @Column(name = "ABRANGENCIA")
    private BigDecimal abrangencia;

    @Column(name = "PERIODO")
    private Integer periodo;

    @Column(name = "OPORTUNIDADE_VENDA")
    private String oportunidadeVenda;

    @javax.persistence.Transient
    private String cesta;

    @OneToMany(mappedBy = "estrategia", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private List<EdicaoBaseEstrategia> basesEstrategia;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

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

    public List<EdicaoBaseEstrategia> getBasesEstrategia() {
	return basesEstrategia;
    }

    public void setBasesEstrategia(List<EdicaoBaseEstrategia> basesEstrategia) {
	this.basesEstrategia = basesEstrategia;
    }
}
