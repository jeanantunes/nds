package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.ComponentesPDV;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class DistribuicaoVendaMediaDTO {
	
	private BigInteger reparteDistribuir;
	private BigInteger reparteMinimo;
	private boolean usarFixacao;
	private boolean usarMix;
	private Boolean distribuicaoPorMultiplo;
	private BigInteger multiplo;
	
	private BigInteger reparteTotal;
	
	private List<ProdutoEdicaoDTO> bases = new ArrayList<ProdutoEdicaoDTO>();
	private List<BonificacaoDTO> bonificacoes = new ArrayList<BonificacaoDTO>();

    @JsonRawValue
    private String bonificacoesVO;

	private Boolean todasAsCotas;
	private ComponentesPDV componente;
	private String elemento,elemento2,elemento3;
	private String abrangenciaCriterio;
	private Double abrangencia;
	private Date dataLancamento;
	
	private Long roteiroEntregaId;
	private Boolean complementarAutomatico;
	private boolean cotasAVista;
	private ComponentesPDV excecaoDeBancasComponente;
	private List<String> excecaoDeBancas = new ArrayList<>();
	
	public BigInteger getReparteDistribuir() {
		return reparteDistribuir;
	}
	public void setReparteDistribuir(BigInteger reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public boolean isUsarFixacao() {
		return usarFixacao;
	}
	public void setUsarFixacao(boolean usarFixacao) {
		this.usarFixacao = usarFixacao;
	}
	public Boolean isDistribuicaoPorMultiplo() {
		return distribuicaoPorMultiplo;
	}
	public void setDistribuicaoPorMultiplo(Boolean distribuicaoPorMultiplo) {
		this.distribuicaoPorMultiplo = distribuicaoPorMultiplo;
	}
	public List<ProdutoEdicaoDTO> getBases() {
		return bases;
	}
	public void setBases(List<ProdutoEdicaoDTO> bases) {
		this.bases = bases;
	}
	public List<BonificacaoDTO> getBonificacoes() {
		return bonificacoes;
	}
	public void setBonificacoes(List<BonificacaoDTO> bonificacoes) {
		this.bonificacoes = bonificacoes;
	}
	public Boolean getTodasAsCotas() {
		return todasAsCotas;
	}
	public void setTodasAsCotas(Boolean todasAsCotas) {
		this.todasAsCotas = todasAsCotas;
	}
	public ComponentesPDV getComponente() {
		return componente;
	}
	public void setComponente(ComponentesPDV componente) {
		this.componente = componente;
	}
	public String getElemento() {
		return elemento;
	}
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}
	public String getAbrangenciaCriterio() {
		return abrangenciaCriterio;
	}
	public void setAbrangenciaCriterio(String abrangenciaCriterio) {
		this.abrangenciaCriterio = abrangenciaCriterio;
	}
	public Double getAbrangencia() {
		return abrangencia;
	}
	public void setAbrangencia(Double abrangencia) {
		this.abrangencia = abrangencia;
	}
	public Long getRoteiroEntregaId() {
		return roteiroEntregaId;
	}
	public void setRoteiroEntregaId(Long roteiroEntregaId) {
		this.roteiroEntregaId = roteiroEntregaId;
	}
	public Boolean getComplementarAutomatico() {
		return complementarAutomatico;
	}
	public void setComplementarAutomatico(Boolean complementarAutomatico) {
		this.complementarAutomatico = complementarAutomatico;
	}
	public boolean isCotasAVista() {
		return cotasAVista;
	}
	public void setCotasAVista(boolean cotasAVista) {
		this.cotasAVista = cotasAVista;
	}
	public ComponentesPDV getExcecaoDeBancasComponente() {
		return excecaoDeBancasComponente;
	}
	public void setExcecaoDeBancasComponente(ComponentesPDV excecaoDeBancasComponente) {
		this.excecaoDeBancasComponente = excecaoDeBancasComponente;
	}

	public List<String> getExcecaoDeBancas() {
		return excecaoDeBancas;
	}
	
	public void setExcecaoDeBancas(List<String> excecaoDeBancas) {
		this.excecaoDeBancas = excecaoDeBancas;
	}

	public Date getDataLancamento() {
	    return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
	    this.dataLancamento = dataLancamento;
	}
	@Override
	public String toString() {
		return "DistribuicaoVendaMediaDTO [reparteDistribuir="
				+ reparteDistribuir + ", reparteMinimo=" + reparteMinimo
				+ ", usarFixacao=" + usarFixacao + ", distribuicaoPorMultiplo="
				+ distribuicaoPorMultiplo + ", bases=" + bases
				+ ", bonificacoes=" + bonificacoes + ", todasAsCotas="
				+ todasAsCotas + ", componente=" + componente + ", elemento="
				+ elemento + ", abrangenciaCriterio=" + abrangenciaCriterio
				+ ", abrangencia=" + abrangencia + ", roteiroEntregaId="
				+ roteiroEntregaId + ", complementarAutomatico="
				+ complementarAutomatico + ", cotasAVista=" + cotasAVista
				+ ", excecaoDeBancasComponente=" + excecaoDeBancasComponente
				+ ", excecaoDeBancasElemento=" + excecaoDeBancas + "]";
	}
	public BigInteger getMultiplo() {
		return multiplo;
	}
	public void setMultiplo(BigInteger multiplo) {
		this.multiplo = multiplo;
	}
	public String getElemento2() {
		return elemento2;
	}
	public void setElemento2(String elemento2) {
		this.elemento2 = elemento2;
	}
	public String getElemento3() {
		return elemento3;
	}
	public void setElemento3(String elemento3) {
		this.elemento3 = elemento3;
	}


    public String getBonificacoesVO() {
        return bonificacoesVO;
    }

    public void setBonificacoesVO(String bonificacoesVO) {
        this.bonificacoesVO = bonificacoesVO;
    }
	public BigInteger getReparteTotal() {
		return reparteTotal;
	}
	public void setReparteTotal(BigInteger reparteTotal) {
		this.reparteTotal = reparteTotal;
	}
	public boolean isUsarMix() {
		return usarMix;
	}
	public void setUsarMix(boolean usarMix) {
		this.usarMix = usarMix;
	}
}
