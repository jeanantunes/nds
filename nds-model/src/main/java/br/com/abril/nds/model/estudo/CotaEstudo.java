package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import br.com.abril.nds.model.cadastro.Cota;

@Entity
public class CotaEstudo extends Cota {

	private static final long serialVersionUID = 5051141966750537344L;

	@Transient private Long idEstudo;
	@Transient private List<ProdutoEdicaoEstudo> edicoesRecebidas;
	@Transient private ClassificacaoCota classificacao;
	@Transient private BigInteger reparteCalculado;
	@Transient private BigInteger reparteJuramentadoAFaturar;
	@Transient private BigDecimal reparteMinimo;
	@Transient private BigDecimal reparteMaximo;
	@Transient private BigDecimal vendaMedia;
	@Transient private BigDecimal vendaMediaNominal;
	@Transient private BigDecimal vendaEdicaoMaisRecenteFechada;
	@Transient private boolean cotaSoRecebeuEdicaoAberta;
	@Transient private BigDecimal somaReparteEdicoesAbertas;
	@Transient private BigDecimal indiceCorrecaoTendencia;
	@Transient private BigDecimal quantidadePDVs;
	@Transient private boolean recebeReparteComplementar;
	@Transient private BigDecimal ajusteReparte;
	@Transient private BigInteger vendaMediaMaisN;
	@Transient private BigDecimal percentualEncalheMaximo;
	@Transient private boolean mix;
	@Transient private BigDecimal indiceAjusteCota;
	@Transient private BigDecimal indiceVendaCrescente;
	@Transient private BigDecimal indiceTratamentoRegional;
	@Transient private List<CotaEstudo> equivalente;
	@Transient private BigDecimal indiceAjusteEquivalente;
	
	public CotaEstudo() {
		reparteCalculado = BigInteger.ZERO;
		classificacao = ClassificacaoCota.SemClassificacao;
	}
	
	public Long getIdEstudo() {
		return idEstudo;
	}
	public void setIdEstudo(Long idEstudo) {
		this.idEstudo = idEstudo;
	}
	public List<ProdutoEdicaoEstudo> getEdicoesRecebidas() {
		return edicoesRecebidas;
	}
	public void setEdicoesRecebidas(List<ProdutoEdicaoEstudo> edicoesRecebidas) {
		this.edicoesRecebidas = edicoesRecebidas;
	}
	public ClassificacaoCota getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(ClassificacaoCota classificacao) {
		this.classificacao = classificacao;
	}
	public BigInteger getReparteCalculado() {
		return reparteCalculado;
	}
	public void setReparteCalculado(BigInteger reparteCalculado) {
		this.reparteCalculado = reparteCalculado;
	}
	public BigInteger getReparteJuramentadoAFaturar() {
		return reparteJuramentadoAFaturar;
	}
	public void setReparteJuramentadoAFaturar(BigInteger reparteJuramentadoAFaturar) {
		this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
	}
	public BigDecimal getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigDecimal reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public BigDecimal getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(BigDecimal reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}
	public BigDecimal getVendaMedia() {
		return vendaMedia;
	}
	public void setVendaMedia(BigDecimal vendaMedia) {
		this.vendaMedia = vendaMedia;
	}
	public BigDecimal getVendaMediaNominal() {
		return vendaMediaNominal;
	}
	public void setVendaMediaNominal(BigDecimal vendaMediaNominal) {
		this.vendaMediaNominal = vendaMediaNominal;
	}
	public BigDecimal getVendaEdicaoMaisRecenteFechada() {
		return vendaEdicaoMaisRecenteFechada;
	}
	public void setVendaEdicaoMaisRecenteFechada(
			BigDecimal vendaEdicaoMaisRecenteFechada) {
		this.vendaEdicaoMaisRecenteFechada = vendaEdicaoMaisRecenteFechada;
	}
	public boolean isCotaSoRecebeuEdicaoAberta() {
		return cotaSoRecebeuEdicaoAberta;
	}
	public void setCotaSoRecebeuEdicaoAberta(boolean cotaSoRecebeuEdicaoAberta) {
		this.cotaSoRecebeuEdicaoAberta = cotaSoRecebeuEdicaoAberta;
	}
	public BigDecimal getSomaReparteEdicoesAbertas() {
		return somaReparteEdicoesAbertas;
	}
	public void setSomaReparteEdicoesAbertas(BigDecimal somaReparteEdicoesAbertas) {
		this.somaReparteEdicoesAbertas = somaReparteEdicoesAbertas;
	}
	public BigDecimal getIndiceCorrecaoTendencia() {
		return indiceCorrecaoTendencia;
	}
	public void setIndiceCorrecaoTendencia(BigDecimal indiceCorrecaoTendencia) {
		this.indiceCorrecaoTendencia = indiceCorrecaoTendencia;
	}
	public BigDecimal getQuantidadePDVs() {
		return quantidadePDVs;
	}
	public void setQuantidadePDVs(BigDecimal quantidadePDVs) {
		this.quantidadePDVs = quantidadePDVs;
	}
	public boolean isRecebeReparteComplementar() {
		return recebeReparteComplementar;
	}
	public void setRecebeReparteComplementar(boolean recebeReparteComplementar) {
		this.recebeReparteComplementar = recebeReparteComplementar;
	}
	public BigDecimal getAjusteReparte() {
		return ajusteReparte;
	}
	public void setAjusteReparte(BigDecimal ajusteReparte) {
		this.ajusteReparte = ajusteReparte;
	}
	public BigInteger getVendaMediaMaisN() {
		return vendaMediaMaisN;
	}
	public void setVendaMediaMaisN(BigInteger vendaMediaMaisN) {
		this.vendaMediaMaisN = vendaMediaMaisN;
	}
	public BigDecimal getPercentualEncalheMaximo() {
		return percentualEncalheMaximo;
	}
	public void setPercentualEncalheMaximo(BigDecimal percentualEncalheMaximo) {
		this.percentualEncalheMaximo = percentualEncalheMaximo;
	}
	public boolean isMix() {
		return mix;
	}
	public void setMix(boolean mix) {
		this.mix = mix;
	}
	public BigDecimal getIndiceAjusteCota() {
		return indiceAjusteCota;
	}
	public void setIndiceAjusteCota(BigDecimal indiceAjusteCota) {
		this.indiceAjusteCota = indiceAjusteCota;
	}
	public BigDecimal getIndiceVendaCrescente() {
		return indiceVendaCrescente;
	}
	public void setIndiceVendaCrescente(BigDecimal indiceVendaCrescente) {
		this.indiceVendaCrescente = indiceVendaCrescente;
	}
	public BigDecimal getIndiceTratamentoRegional() {
		return indiceTratamentoRegional;
	}
	public void setIndiceTratamentoRegional(BigDecimal indiceTratamentoRegional) {
		this.indiceTratamentoRegional = indiceTratamentoRegional;
	}
	public List<CotaEstudo> getEquivalente() {
		return equivalente;
	}
	public void setEquivalente(List<CotaEstudo> equivalente) {
		this.equivalente = equivalente;
	}
	public BigDecimal getIndiceAjusteEquivalente() {
		return indiceAjusteEquivalente;
	}
	public void setIndiceAjusteEquivalente(BigDecimal indiceAjusteEquivalente) {
		this.indiceAjusteEquivalente = indiceAjusteEquivalente;
	}

	public boolean isNova() {
		// TODO Auto-generated method stub
		return false;
	}

}
