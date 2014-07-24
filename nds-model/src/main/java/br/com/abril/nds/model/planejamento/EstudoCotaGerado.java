package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO_COTA_GERADO")
@SequenceGenerator(name="ESTUDO_COTA_GERADO_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoCotaGerado implements Serializable {

	private static final long serialVersionUID = -2730755900853136814L;

	@Id
	@GeneratedValue(generator = "ESTUDO_COTA_GERADO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "QTDE_PREVISTA")
	private BigInteger qtdePrevista;

	@Column(name = "QTDE_EFETIVA")
	private BigInteger qtdeEfetiva;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTUDO_ID")
	private EstudoGerado estudo;

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

	@Column(name = "REPARTE_INICIAL")
	private BigInteger reparteInicial;

	public EstudoCotaGerado() {

	}

	public EstudoCotaGerado(Long id) {
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

	public EstudoGerado getEstudo() {
		return estudo;
	}

	public void setEstudo(EstudoGerado estudo) {
		this.estudo = estudo;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
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

	public BigInteger getReparteInicial() {
		return reparteInicial;
	}

	public void setReparteInicial(BigInteger reparteInicial) {
		this.reparteInicial = reparteInicial;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		EstudoCotaGerado other = (EstudoCotaGerado) obj;

		if(this.getId()==null && other.id==null)
			return false;

		if (this.getId() !=null && !this.getId().equals(other.id))
			return false;
		return true;
	}
}