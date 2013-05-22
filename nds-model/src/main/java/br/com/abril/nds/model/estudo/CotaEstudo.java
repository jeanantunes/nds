package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaEstudo extends Cota {

    private static final long serialVersionUID = 5051141966750537344L;

    private Long idEstudo;
    private List<ProdutoEdicaoEstudo> edicoesRecebidas;
    private ClassificacaoCota classificacao;
    private BigInteger reparteCalculado;
    private BigInteger reparteJuramentadoAFaturar;
    private BigInteger reparteMinimo;
    private BigInteger intervaloMinimo;
    private BigInteger intervaloMaximo;
    private BigInteger reparteFixado;
    private BigDecimal vendaMedia;
    private BigDecimal vendaMediaCorrigida;
    private BigDecimal vendaMediaNominal;
    private BigInteger vendaEdicaoMaisRecenteFechada;
    private boolean cotaSoRecebeuEdicaoAberta;
    private BigDecimal somaReparteEdicoesAbertas;
    private BigDecimal indiceCorrecaoTendencia;
    private BigDecimal quantidadePDVs;
    private boolean recebeReparteComplementar;
    private BigDecimal ajusteReparte;
    private BigInteger vendaMediaMaisN;
    private BigDecimal percentualEncalheMaximo;
    private boolean mix;
    private BigDecimal indiceAjusteCota;
    private BigDecimal indiceVendaCrescente;
    private BigDecimal indiceTratamentoRegional;
    private BigDecimal indiceAjusteEquivalente;
    private List<Integer> regioes;
    private boolean nova;
    private boolean recebeuUltimaEdicaoAberta;
    private BigInteger qtdeRanking;

    public CotaEstudo() {
	nova = false;
	mix = false;
	vendaMedia = BigDecimal.ZERO;
	indiceTratamentoRegional = BigDecimal.ONE;
	reparteMinimo = BigInteger.ZERO;
	intervaloMinimo = BigInteger.ZERO;
	reparteCalculado = BigInteger.ZERO;
	indiceAjusteCota = BigDecimal.ONE;
	indiceVendaCrescente = BigDecimal.ONE;
	indiceTratamentoRegional = BigDecimal.ONE;
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
    public void setReparteCalculado(BigInteger reparteCalculado, EstudoTransient estudo) {
	boolean minimoMaximo = false;
	if (intervaloMaximo != null && reparteCalculado.compareTo(intervaloMaximo) > 0) {
	    reparteCalculado = intervaloMaximo;
	    minimoMaximo = true;
	} else if (reparteCalculado.compareTo(intervaloMinimo) < 0) {
	    reparteCalculado = intervaloMinimo;
	    minimoMaximo = true;
	}
	if (minimoMaximo) {
	    if (mix) {
		classificacao = ClassificacaoCota.CotaMix;
	    } else {
		classificacao = ClassificacaoCota.MaximoMinimo;
	    }
	}
	BigInteger variacao = reparteCalculado.subtract(this.reparteCalculado);
	estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(variacao));
	this.reparteCalculado = reparteCalculado;
    }
    public BigInteger getReparteJuramentadoAFaturar() {
	return reparteJuramentadoAFaturar;
    }
    public void setReparteJuramentadoAFaturar(BigInteger reparteJuramentadoAFaturar) {
	this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
    }
    public BigInteger getReparteMinimo() {
	return reparteMinimo;
    }
    public void setReparteMinimo(BigInteger reparteMinimo) {
	this.reparteMinimo = reparteMinimo;
    }
    public BigInteger getIntervaloMinimo() {
        return intervaloMinimo;
    }

    public void setIntervaloMinimo(BigInteger intervaloMinimo) {
        this.intervaloMinimo = intervaloMinimo;
    }

    public BigInteger getIntervaloMaximo() {
        return intervaloMaximo;
    }

    public void setIntervaloMaximo(BigInteger intervaloMaximo) {
        this.intervaloMaximo = intervaloMaximo;
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
    public BigInteger getVendaEdicaoMaisRecenteFechada() {
	return vendaEdicaoMaisRecenteFechada;
    }
    public void setVendaEdicaoMaisRecenteFechada(BigInteger vendaEdicaoMaisRecenteFechada) {
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
    public BigDecimal getIndiceAjusteEquivalente() {
	return indiceAjusteEquivalente;
    }
    public void setIndiceAjusteEquivalente(BigDecimal indiceAjusteEquivalente) {
	this.indiceAjusteEquivalente = indiceAjusteEquivalente;
    }

    public BigInteger getReparteFixado() {
	return reparteFixado;
    }

    public void setReparteFixado(BigInteger reparteFixado) {
	this.reparteFixado = reparteFixado;
    }

    public List<Integer> getRegioes() {
	return regioes;
    }

    public void setRegioes(List<Integer> regioes) {
	this.regioes = regioes;
    }

    public boolean isNova() {
	return nova;
    }

    public void setNova(boolean nova) {
	this.nova = nova;
    }

    public boolean isRecebeuUltimaEdicaoAberta() {
        return recebeuUltimaEdicaoAberta;
    }

    public void setRecebeuUltimaEdicaoAberta(boolean recebeuUltimaEdicaoAberta) {
        this.recebeuUltimaEdicaoAberta = recebeuUltimaEdicaoAberta;
    }

    public BigInteger getQtdeRanking() {
        return qtdeRanking;
    }

    public void setQtdeRanking(BigInteger qtdeRanking) {
        this.qtdeRanking = qtdeRanking;
    }

    public BigDecimal getVendaMediaCorrigida() {
        return vendaMediaCorrigida;
    }

    public void setVendaMediaCorrigida(BigDecimal vendaMediaCorrigida) {
        this.vendaMediaCorrigida = vendaMediaCorrigida;
    }

    @Override
    public String toString() {
	return "" + getNumeroCota() + "";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((regioes == null) ? 0 : regioes.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CotaEstudo other = (CotaEstudo) obj;
	if (regioes == null) {
	    if (other.regioes != null) {
		return false;
	    }
	} else if (!regioes.equals(other.regioes)) {
	    return false;
	}
	if (getId() == null) {
	    if (other.getId() != null) {
		return false;
	    }
	} else if (!getId().equals(other.getId())) {
	    return false;
	}
	return true;
    }
}
