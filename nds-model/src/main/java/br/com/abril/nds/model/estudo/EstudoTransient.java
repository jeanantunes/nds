package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.seguranca.Usuario;

public class EstudoTransient extends EstudoGerado {

    private static final long serialVersionUID = 8058482081570920501L;

    private BigInteger reparteDistribuirInicial;
    private ProdutoEdicaoEstudo produtoEdicaoEstudo;
    private LinkedList<ProdutoEdicaoEstudo> edicoesBase;
    private LinkedList<CotaEstudo> cotas; //Cotas que receberam as edições base.
    private LinkedList<CotaEstudo> cotasExcluidas; //Cotas excluídas durante o estudo
    private LinkedList<CotaEstudo> cotasForaDaRegiao; //Cotas fora da regiao de abrangencia
    private LinkedList<CotaEstudo> cotasComReparteJaCalculado; //Cotas que já tiveram seu reparte calculado durante o processo
    private LinkedList<CotaEstudo> cotasSoComEdicaoAberta;
    private BigInteger reparteMinimo;
    private BigDecimal somatoriaVendaMedia;
    private BigDecimal somatoriaReparteEdicoesAbertas;
    private BigDecimal totalPDVs;
    private BigDecimal excedente;
    private BigDecimal excedenteDistribuir;
    private BigInteger reparteComplementar;
    private BigInteger reparteComplementarInicial;
    private String statusEstudo;
    private List<BonificacaoDTO> bonificacoes;
    private Usuario usuario;
    private boolean usarFixacao;

    private boolean complementarAutomatico;
    private boolean pracaVeraneio;
    private boolean geracaoAutomatica;
    private BigDecimal percentualMaximoFixacao;
    private BigInteger vendaMediaMais;

    private Map<String, PercentualExcedenteEstudo> percentualProporcaoExcedente;
    private BigDecimal percentualExcedente;
    
    private BigDecimal menorVenda;
    private BigInteger reservaAjuste;
    private BigInteger reservaAjusteInicial;
    private DistribuicaoVendaMediaDTO distribuicaoVendaMediaDTO;
    
    private Date dataLancamento;

    private Long usuarioId;

    private String tipoGeracao;
    
    private BigInteger reparteTotal;
    
    public EstudoTransient() {
	excedenteDistribuir = BigDecimal.ZERO;
	usarFixacao = true;
	complementarAutomatico = true; //Default conforme documentação.
	cotasExcluidas = new LinkedList<>();
	cotasForaDaRegiao = new LinkedList<>();
	cotasComReparteJaCalculado = new LinkedList<>();
	cotasSoComEdicaoAberta = new LinkedList<>();
	percentualExcedente = BigDecimal.ZERO;
    }

    public BigInteger getReparteDistribuirInicial() {
	return reparteDistribuirInicial;
    }
    public void setReparteDistribuirInicial(BigInteger reparteDistribuirInicial) {
	this.reparteDistribuirInicial = reparteDistribuirInicial;
    }
    public ProdutoEdicaoEstudo getProdutoEdicaoEstudo() {
	return produtoEdicaoEstudo;
    }
    public void setProdutoEdicaoEstudo(ProdutoEdicaoEstudo produtoEdicaoEstudo) {
	this.produtoEdicaoEstudo = produtoEdicaoEstudo;
    }
    public LinkedList<ProdutoEdicaoEstudo> getEdicoesBase() {
	return edicoesBase;
    }
    public void setEdicoesBase(LinkedList<ProdutoEdicaoEstudo> edicoesBase) {
	this.edicoesBase = edicoesBase;
    }
    public LinkedList<CotaEstudo> getCotas() {
	return cotas;
    }
    public void setCotas(LinkedList<CotaEstudo> cotas) {
	this.cotas = cotas;
    }
    public BigInteger getReparteMinimo() {
	return reparteMinimo;
    }
    public void setReparteMinimo(BigInteger reparteMinimo) {
	this.reparteMinimo = reparteMinimo;
    }
    public BigDecimal getSomatoriaVendaMedia() {
	return somatoriaVendaMedia;
    }
    public void setSomatoriaVendaMedia(BigDecimal somatoriaVendaMedia) {
	this.somatoriaVendaMedia = somatoriaVendaMedia;
    }
    public BigDecimal getSomatoriaReparteEdicoesAbertas() {
	return somatoriaReparteEdicoesAbertas;
    }
    public void setSomatoriaReparteEdicoesAbertas(BigDecimal somatoriaReparteEdicoesAbertas) {
	this.somatoriaReparteEdicoesAbertas = somatoriaReparteEdicoesAbertas;
    }
    public boolean isComplementarAutomatico() {
	return complementarAutomatico;
    }
    public void setComplementarAutomatico(boolean complementarAutomatico) {
	this.complementarAutomatico = complementarAutomatico;
    }
    public BigDecimal getTotalPDVs() {
	return totalPDVs;
    }
    public void setTotalPDVs(BigDecimal totalPDVs) {
	this.totalPDVs = totalPDVs;
    }
    public boolean isPracaVeraneio() {
	return pracaVeraneio;
    }
    public void setPracaVeraneio(boolean pracaVeraneio) {
	this.pracaVeraneio = pracaVeraneio;
    }
    public BigDecimal getExcedente() {
	return excedente;
    }
    public void setExcedente(BigDecimal excedente) {
	this.excedente = excedente;
    }
    public BigInteger getReparteComplementar() {
	return reparteComplementar;
    }
    public void setReparteComplementar(BigInteger reparteComplementar) {
	this.reparteComplementar = reparteComplementar;
    }
    public boolean isDistribuicaoPorMultiplos() {
	return getDistribuicaoPorMultiplos() == 1;
    }
    public boolean isGeracaoAutomatica() {
	return geracaoAutomatica;
    }
    public void setGeracaoAutomatica(boolean geracaoAutomatica) {
	this.geracaoAutomatica = geracaoAutomatica;
    }
    public BigInteger getVendaMediaMais() {
	return vendaMediaMais;
    }
    public void setVendaMediaMais(BigInteger vendaMediaMais) {
	this.vendaMediaMais = vendaMediaMais;
    }
    public BigDecimal getPercentualMaximoFixacao() {
	return percentualMaximoFixacao;
    }
    public void setPercentualMaximoFixacao(BigDecimal percentualMaximoFixacao) {
	this.percentualMaximoFixacao = percentualMaximoFixacao;
    }
    public List<BonificacaoDTO> getBonificacoes() {
	return bonificacoes;
    }
    public void setBonificacoes(List<BonificacaoDTO> bonificacoes) {
	this.bonificacoes = bonificacoes;
    }
    public String getStatusEstudo() {
	return statusEstudo;
    }
    public void setStatusEstudo(String statusEstudo) {
	this.statusEstudo = statusEstudo;
    }
    public Map<String, PercentualExcedenteEstudo> getPercentualProporcaoExcedente() {
	return percentualProporcaoExcedente;
    }
    public void setPercentualProporcaoExcedente(Map<String, PercentualExcedenteEstudo> percentualProporcaoExcedente) {
	this.percentualProporcaoExcedente = percentualProporcaoExcedente;
    }

    public boolean isUsarFixacao() {
	return usarFixacao;
    }

    public void setUsarFixacao(boolean usarFixacao) {
	this.usarFixacao = usarFixacao;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
	this.usuarioId = usuario.getId();
    }

    public LinkedList<CotaEstudo> getCotasExcluidas() {
	return cotasExcluidas;
    }

    public void setCotasExcluidas(LinkedList<CotaEstudo> cotasExcluidas) {
	this.cotasExcluidas = cotasExcluidas;
    }

    public BigInteger getReparteComplementarInicial() {
        return reparteComplementarInicial;
    }

    public void setReparteComplementarInicial(BigInteger reparteComplementarInicial) {
        this.reparteComplementarInicial = reparteComplementarInicial;
    }

    public BigDecimal getPercentualExcedente() {
        return percentualExcedente;
    }

    public void setPercentualExcedente(BigDecimal percentualExcedente) {
        this.percentualExcedente = percentualExcedente;
    }

    public BigDecimal getMenorVenda() {
        return menorVenda;
    }

    public void setMenorVenda(BigDecimal menorVenda) {
        this.menorVenda = menorVenda;
    }

    public BigInteger getReservaAjuste() {
        return reservaAjuste;
    }

    public void setReservaAjuste(BigInteger reservaAjuste) {
        this.reservaAjuste = reservaAjuste;
    }

    public BigInteger getReservaAjusteInicial() {
        return reservaAjusteInicial;
    }

    public void setReservaAjusteInicial(BigInteger reservaAjusteInicial) {
        this.reservaAjusteInicial = reservaAjusteInicial;
    }

    public DistribuicaoVendaMediaDTO getDistribuicaoVendaMediaDTO() {
        return distribuicaoVendaMediaDTO;
    }

    public void setDistribuicaoVendaMediaDTO(DistribuicaoVendaMediaDTO distribuicaoVendaMediaDTO) {
        this.distribuicaoVendaMediaDTO = distribuicaoVendaMediaDTO;
    }

    public BigDecimal getExcedenteDistribuir() {
        return excedenteDistribuir;
    }

    public void setExcedenteDistribuir(BigDecimal excedenteDistribuir) {
        this.excedenteDistribuir = excedenteDistribuir;
    }

    public LinkedList<CotaEstudo> getCotasForaDaRegiao() {
        return cotasForaDaRegiao;
    }

    public void setCotasForaDaRegiao(LinkedList<CotaEstudo> cotasForaDaRegiao) {
        this.cotasForaDaRegiao = cotasForaDaRegiao;
    }
    
    public LinkedList<CotaEstudo> getCotasComReparteJaCalculado() {
		return cotasComReparteJaCalculado;
	}

	public void setCotasComReparteJaCalculado(LinkedList<CotaEstudo> cotasComReparteJaCalculado) {
		this.cotasComReparteJaCalculado = cotasComReparteJaCalculado;
	}

	public LinkedList<CotaEstudo> getCotasSoComEdicaoAberta() {
        return cotasSoComEdicaoAberta;
    }

    public void setCotasSoComEdicaoAberta(LinkedList<CotaEstudo> cotasSoComEdicaoAberta) {
        this.cotasSoComEdicaoAberta = cotasSoComEdicaoAberta;
    }
    
    public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipoGeracao() {
        return tipoGeracao;
    }

    public void setTipoGeracao(String tipoGeracao) {
        this.tipoGeracao = tipoGeracao;
    }

    public BigInteger getReparteTotal() {
    	return reparteTotal;
    }
    
    public void setReparteTotal(BigInteger reparteTotal) {
    	this.reparteTotal = reparteTotal;
    }

    @Override
    public String toString() {
	 String string =  "EstudoTransient [reparteDistribuirInicial="
		+ reparteDistribuirInicial + ", produtoEdicaoEstudo="
		+ produtoEdicaoEstudo + ", edicoesBase=" + edicoesBase
		+ ", reparteMinimo=" + reparteMinimo + ", somatoriaVendaMedia="
		+ somatoriaVendaMedia + ", somatoriaReparteEdicoesAbertas="
		+ somatoriaReparteEdicoesAbertas + ", totalPDVs=" + totalPDVs
		+ ", excedente=" + excedente + ", reparteComplementar=" + reparteComplementar
		+ ", statusEstudo=" + statusEstudo + ", bonificacoes="
		+ bonificacoes + ", complementarAutomatico="
		+ complementarAutomatico + ", pracaVeraneio=" + pracaVeraneio
		+ ", geracaoAutomatica=" + geracaoAutomatica
		+ ", percentualMaximoFixacao=" + percentualMaximoFixacao
		+ ", vendaMediaMais=" + vendaMediaMais;
	 
		if (percentualProporcaoExcedente != null) {
			
			string += ", percentualExcedente=" + percentualProporcaoExcedente.values();
		}
		
		string += "]";
	 
	 return string;
    }

    
    

}
