package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO_COTA")
@SequenceGenerator(name="ESTUDO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoCota implements Serializable {

    private static final long serialVersionUID = -2730755900853136814L;

    @Id
    @GeneratedValue(generator = "ESTUDO_COTA_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QTDE_PREVISTA")
    private BigInteger qtdePrevista;

    @Column(name = "QTDE_EFETIVA")
    private BigInteger qtdeEfetiva;

    @ManyToOne(optional = false)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "ESTUDO_ID")
    private Estudo estudo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "COTA_ID")
    private Cota cota;

    @Column(name = "REPARTE_MINIMO")
    private BigInteger reparteMinimo;

    @Column(name = "REPARTE")
    private BigInteger reparte;
    
    @Column(name="TIPO_ESTUDO",columnDefinition="VARCHAR(20) default 'NORMAL'")
	@Enumerated(EnumType.STRING)
	private TipoEstudoCota tipoEstudo;

    @OneToMany(mappedBy = "estudoCota", fetch = FetchType.LAZY)
    private Set<RateioDiferenca> rateiosDiferenca = new HashSet<RateioDiferenca>();

    @Cascade(value={CascadeType.DELETE})
    @OneToMany(mappedBy = "estudoCota", fetch = FetchType.LAZY)
    private List<MovimentoEstoqueCota> movimentosEstoqueCota; 

    @Cascade(value={CascadeType.DELETE})
    @OneToMany(mappedBy = "estudoCota", fetch = FetchType.LAZY)
    private List<ItemNotaEnvio> itemNotaEnvios;

    @Column(name = "CLASSIFICACAO")
    private String classificacao;
    
    @Column(name = "VENDA_MEDIA_NOMINAL")
    private BigDecimal vendaMediaNominal;
    
    @Column(name = "REPARTE_JURAMENTADO_A_FATURAR")
    private BigInteger reparteJuramentadoAFaturar;
    
    @Column(name = "QUANTIDADE_PDVS")
    private Integer quantidadePDVS;
    
    @Column(name = "REPARTE_MAXIMO")
    private BigInteger reparteMaximo;
    
    @Column(name = "VENDA_MEDIA_MAIS_N")
    private Integer vendaMediaMaisN;
    
    @Column(name = "INDICE_CORRECAO_TENDENCIA")
    private BigDecimal indiceCorrecaoTendencia;
    
    @Column(name = "INDICE_VENDA_CRESCENTE")
    private BigDecimal indiceVendaCrescente;
    
    @Column(name = "PERCENTUAL_ENCALHE_MAXIMO")
    private BigDecimal percentualEncalheMaximo;
    
    @Column(name = "MIX")
    private Integer mix;
    
    @Column(name = "VENDA_MEDIA")
    private BigDecimal vendaMedia;
    
    @Column(name = "COTA_NOVA")
    private Boolean cotaNova;

    public EstudoCota() {

    }
    
    public EstudoCota(EstudoCotaGerado estudoCotaGerado, Estudo estudo) {
    	
    	this.estudo = estudo;
    	this.classificacao = estudoCotaGerado.getClassificacao();
    	this.cota = estudoCotaGerado.getCota();
    	this.cotaNova = estudoCotaGerado.getCotaNova();
    	this.indiceCorrecaoTendencia = estudoCotaGerado.getIndiceCorrecaoTendencia();
    	this.indiceVendaCrescente = estudoCotaGerado.getIndiceVendaCrescente();
    	this.mix = estudoCotaGerado.getMix();
    	this.percentualEncalheMaximo = estudoCotaGerado.getPercentualEncalheMaximo();
    	this.qtdeEfetiva = estudoCotaGerado.getQtdeEfetiva();
    	this.qtdePrevista = estudoCotaGerado.getQtdePrevista();
    	this.quantidadePDVS = estudoCotaGerado.getQuantidadePDVS();
    	this.reparte = estudoCotaGerado.getReparte();
    	this.reparteJuramentadoAFaturar = estudoCotaGerado.getReparteJuramentadoAFaturar();
    	this.reparteMaximo = estudoCotaGerado.getReparteMaximo();
    	this.reparteMinimo = estudoCotaGerado.getReparteMinimo();
    	this.tipoEstudo = estudoCotaGerado.getTipoEstudo();
    	this.vendaMedia = estudoCotaGerado.getVendaMedia();
    	this.vendaMediaMaisN = estudoCotaGerado.getVendaMediaMaisN();
    	this.vendaMediaNominal = estudoCotaGerado.getVendaMediaNominal();
    }

    public EstudoCota(Long id) {
    	this.id=id;
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }

    public BigInteger getQtdePrevista() {
    	return qtdePrevista;
    }

    public void setQtdePrevista(BigInteger qtdePrevista) {
    	this.qtdePrevista = qtdePrevista;
    }

    public BigInteger getQtdeEfetiva() {
    	return qtdeEfetiva;
    }

    public void setQtdeEfetiva(BigInteger qtdeEfetiva) {
    	this.qtdeEfetiva = qtdeEfetiva;
    }

    public Estudo getEstudo() {
    	return estudo;
    }

    public void setEstudo(Estudo estudo) {
	this.estudo = estudo;
    }

    public Cota getCota() {
    	return cota;
    }

    public void setCota(Cota cota) {
    	this.cota = cota;
    }

    public Set<RateioDiferenca> getRateiosDiferenca() {
    	return rateiosDiferenca;
    }

    public void setRateiosDiferenca(Set<RateioDiferenca> rateiosDiferenca) {
    	this.rateiosDiferenca = rateiosDiferenca;
    }

    public List<MovimentoEstoqueCota> getMovimentosEstoqueCota() {
    	return movimentosEstoqueCota;
    }

    public void setMovimentosEstoqueCota(List<MovimentoEstoqueCota> movimentosEstoqueCota) {
    	this.movimentosEstoqueCota = movimentosEstoqueCota;
    }

    /**
     * @return the itemNotaEnvios
     */
    public List<ItemNotaEnvio> getItemNotaEnvios() {
    	return itemNotaEnvios;
    }

    /**
     * @param itemNotaEnvios the itemNotaEnvios to set
     */
    public void setItemNotaEnvios(List<ItemNotaEnvio> itemNotaEnvios) {
    	this.itemNotaEnvios = itemNotaEnvios;
    }

    public String getClassificacao() {
    	return classificacao;
    }

    public void setClassificacao(String classificacao) {
    	this.classificacao = classificacao;
    }

    public BigInteger getReparte() {
    	return reparte;
    }

    public void setReparte(BigInteger reparte) {
    	this.reparte = reparte;
    }

    public BigInteger getReparteMinimo() {
    	return reparteMinimo;
    }

    public void setReparteMinimo(BigInteger reparteMinimo) {
    	this.reparteMinimo = reparteMinimo;
    }

    public TipoEstudoCota getTipoEstudo() {
		return tipoEstudo;
	}

	public void setTipoEstudo(TipoEstudoCota tipoEstudo) {
		this.tipoEstudo = tipoEstudo;
	}

	public BigDecimal getVendaMediaNominal() {
		return vendaMediaNominal;
	}

	public void setVendaMediaNominal(BigDecimal vendaMediaNominal) {
		this.vendaMediaNominal = vendaMediaNominal;
	}

	public BigInteger getReparteJuramentadoAFaturar() {
		return reparteJuramentadoAFaturar;
	}

	public void setReparteJuramentadoAFaturar(BigInteger reparteJuramentadoAFaturar) {
		this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
	}

	public Integer getQuantidadePDVS() {
		return quantidadePDVS;
	}

	public void setQuantidadePDVS(Integer quantidadePDVS) {
		this.quantidadePDVS = quantidadePDVS;
	}

	public BigInteger getReparteMaximo() {
		return reparteMaximo;
	}

	public void setReparteMaximo(BigInteger reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}

	public Integer getVendaMediaMaisN() {
		return vendaMediaMaisN;
	}

	public void setVendaMediaMaisN(Integer vendaMediaMaisN) {
		this.vendaMediaMaisN = vendaMediaMaisN;
	}

	public BigDecimal getIndiceCorrecaoTendencia() {
		return indiceCorrecaoTendencia;
	}

	public void setIndiceCorrecaoTendencia(BigDecimal indiceCorrecaoTendencia) {
		this.indiceCorrecaoTendencia = indiceCorrecaoTendencia;
	}

	public BigDecimal getIndiceVendaCrescente() {
		return indiceVendaCrescente;
	}

	public void setIndiceVendaCrescente(BigDecimal indiceVendaCrescente) {
		this.indiceVendaCrescente = indiceVendaCrescente;
	}

	public BigDecimal getPercentualEncalheMaximo() {
		return percentualEncalheMaximo;
	}

	public void setPercentualEncalheMaximo(BigDecimal percentualEncalheMaximo) {
		this.percentualEncalheMaximo = percentualEncalheMaximo;
	}

	public Integer getMix() {
		return mix;
	}

	public void setMix(Integer mix) {
		this.mix = mix;
	}

	public BigDecimal getVendaMedia() {
		return vendaMedia;
	}

	public void setVendaMedia(BigDecimal vendaMedia) {
		this.vendaMedia = vendaMedia;
	}

	public Boolean getCotaNova() {
		return cotaNova;
	}

	public void setCotaNova(Boolean cotaNova) {
		this.cotaNova = cotaNova;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
 		result = prime * result + ((this.getEstudo() == null) ? 0 : this.getEstudo().hashCode());
 		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		EstudoCota other = (EstudoCota) obj;
 		if (this.getId() == null) {
 			if (other.getId() != null)
 				return false;
 		} else if (!this.getId().equals(other.getId()))
 			return false;
		if (this.getCota() == null) {
			if (other.getCota() != null)
				return false;
		} else if (!this.getCota().equals(other.getCota()))
			return false;
		if (this.getEstudo() == null) {
			if (other.getEstudo() != null)
				return false;
		} else if (!this.getEstudo().equals(other.getEstudo()))
			return false;
		
		return true;
	}
}