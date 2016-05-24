package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaEstudo extends Cota {

    private static final long serialVersionUID = 5051141966750537344L;

    private Long idEstudo;
    private List<ProdutoEdicaoEstudo> edicoesRecebidas;
    private ClassificacaoCota classificacao;
    private BigInteger reparteCalculado;
    private BigInteger reparteJuramentadoAFaturar;
    private BigInteger reparteMinimo;
    private BigInteger reparteMinimoFinal;
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
    private boolean recebeParcial;
    private boolean excecaoParcial;
    private BigDecimal ajusteReparte;
    private BigInteger vendaMediaMaisN;
    private BigDecimal percentualEncalheMaximo;
    private boolean mix;
    private BigDecimal indiceAjusteCota;
    private BigDecimal indiceVendaCrescente;
    private BigDecimal indiceTratamentoRegional;
    private BigDecimal indiceAjusteEquivalente;
    private BigDecimal menorVenda;
    private BigDecimal pesoMenorVenda;
    private boolean nova;
    private boolean recebeuUltimaEdicaoAberta;
    private BigInteger qtdeRankingSegmento;
    private BigDecimal qtdeRankingFaturamento;
    // informacoes referentes à selecao de componentes
    private Set<Integer> tiposPontoPdv;
    private Set<Integer> tiposGeradorFluxo;
    private Set<String> bairros;
    private Set<Integer> regioes;
    private Set<String> tiposCota; // consignado ou a vista
    private Set<Integer> areasInfluenciaPdv;
    private Set<String> estados;
    // informacoes para a copia proporcional de estudos
    private boolean cotaNaoRecebeSegmento;
    private boolean cotaExcecaoSegmento;
    private boolean cotaNaoRecebeClassificacao;
    private String tipoDistribuicao;
    private String status;
    private boolean isRecebeFornecedor;

    public CotaEstudo() {
	nova = false;
	mix = false;
	vendaMedia = BigDecimal.ZERO;
	indiceTratamentoRegional = BigDecimal.ONE;
	reparteMinimo = BigInteger.ZERO;
	reparteMinimoFinal = BigInteger.ZERO;
	intervaloMinimo = BigInteger.ZERO;
	qtdeRankingFaturamento = BigDecimal.ZERO;
	qtdeRankingSegmento = BigInteger.ZERO;
	reparteCalculado = BigInteger.ZERO;
	indiceAjusteCota = BigDecimal.ONE;
	indiceVendaCrescente = BigDecimal.ONE;
	indiceTratamentoRegional = BigDecimal.ONE;
	classificacao = ClassificacaoCota.SemClassificacao;

	tiposPontoPdv = new HashSet<>();
	tiposGeradorFluxo = new HashSet<>();
	bairros = new HashSet<>();
	regioes = new HashSet<>();
	tiposCota = new HashSet<>();
	areasInfluenciaPdv = new HashSet<>();
	estados = new HashSet<>();
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

    public BigInteger getQtdeRankingSegmento() {
	return qtdeRankingSegmento;
    }

    public void setQtdeRankingSegmento(BigInteger qtdeRankingSegmento) {
	this.qtdeRankingSegmento = qtdeRankingSegmento;
    }

    public BigDecimal getQtdeRankingFaturamento() {
        return qtdeRankingFaturamento;
    }

    public void setQtdeRankingFaturamento(BigDecimal qtdeRankingFaturamento) {
        this.qtdeRankingFaturamento = qtdeRankingFaturamento;
    }

    public BigDecimal getVendaMediaCorrigida() {
	return vendaMediaCorrigida;
    }

    public void setVendaMediaCorrigida(BigDecimal vendaMediaCorrigida) {
	this.vendaMediaCorrigida = vendaMediaCorrigida;
    }

    public Set<Integer> getTiposPontoPdv() {
        return tiposPontoPdv;
    }

    public void setTiposPontoPdv(Set<Integer> tiposPontoPdv) {
        this.tiposPontoPdv = tiposPontoPdv;
    }

    public Set<Integer> getTiposGeradorFluxo() {
        return tiposGeradorFluxo;
    }

    public void setTiposGeradorFluxo(Set<Integer> tiposGeradorFluxo) {
        this.tiposGeradorFluxo = tiposGeradorFluxo;
    }

    public Set<String> getBairros() {
        return bairros;
    }

    public void setBairros(Set<String> bairros) {
        this.bairros = bairros;
    }

    public Set<Integer> getRegioes() {
        return regioes;
    }

    public void setRegioes(Set<Integer> regioes) {
        this.regioes = regioes;
    }

    public Set<String> getTiposCota() {
        return tiposCota;
    }

    public void setTiposCota(Set<String> tiposCota) {
        this.tiposCota = tiposCota;
    }

    public Set<Integer> getAreasInfluenciaPdv() {
        return areasInfluenciaPdv;
    }

    public void setAreasInfluenciaPdv(Set<Integer> areasInfluenciaPdv) {
        this.areasInfluenciaPdv = areasInfluenciaPdv;
    }

    public Set<String> getEstados() {
        return estados;
    }

    public void setEstados(Set<String> estados) {
        this.estados = estados;
    }

    @Override
    public String toString() {
	return "" + getNumeroCota() + "";
    }

    public BigDecimal getMenorVenda() {
        return menorVenda;
    }

    public void setMenorVenda(BigDecimal menorVenda) {
        this.menorVenda = menorVenda;
    }

    public BigDecimal getPesoMenorVenda() {
        return pesoMenorVenda;
    }

    public void setPesoMenorVenda(BigDecimal pesoMenorVenda) {
        this.pesoMenorVenda = pesoMenorVenda;
    }

    public BigInteger getReparteMinimoFinal() {
        return reparteMinimoFinal;
    }

    public void setReparteMinimoFinal(BigInteger reparteMinimoFinal) {
	if (reparteMinimoFinal != null && reparteMinimoFinal.compareTo(this.reparteMinimoFinal) >= 0) {
	    this.reparteMinimoFinal = reparteMinimoFinal;
	}
    }

    public boolean isCotaNaoRecebeSegmento() {
        return cotaNaoRecebeSegmento;
    }

    public void setCotaNaoRecebeSegmento(boolean cotaNaoRecebeSegmento) {
        this.cotaNaoRecebeSegmento = cotaNaoRecebeSegmento;
    }

    public boolean isCotaExcecaoSegmento() {
        return cotaExcecaoSegmento;
    }

    public void setCotaExcecaoSegmento(boolean cotaExcecaoSegmento) {
        this.cotaExcecaoSegmento = cotaExcecaoSegmento;
    }

    public boolean isCotaNaoRecebeClassificacao() {
        return cotaNaoRecebeClassificacao;
    }

    public void setCotaNaoRecebeClassificacao(boolean cotaNaoRecebeClassificacao) {
        this.cotaNaoRecebeClassificacao = cotaNaoRecebeClassificacao;
    }

    public String getTipoDistribuicao() {
        return tipoDistribuicao;
    }

    public void setTipoDistribuicao(String tipoDistribuicao) {
        this.tipoDistribuicao = tipoDistribuicao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRecebeParcial() {
        return recebeParcial;
    }

    public void setRecebeParcial(boolean recebeParcial) {
        this.recebeParcial = recebeParcial;
    }

    public boolean isExcecaoParcial() {
        return excecaoParcial;
    }

    public void setExcecaoParcial(boolean excecaoParcial) {
        this.excecaoParcial = excecaoParcial;
    }

    public boolean isRecebeFornecedor() {
		return isRecebeFornecedor;
	}

	public void setRecebeFornecedor(boolean isRecebeFornecedor) {
		this.isRecebeFornecedor = isRecebeFornecedor;
	}

	@Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
	if (getId() == null) {
	    if (other.getId() != null)
		return false;
	} else if (!getId().equals(other.getId()))
	    return false;
	return true;
    }
}
