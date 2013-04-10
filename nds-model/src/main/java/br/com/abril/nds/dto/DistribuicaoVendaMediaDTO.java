package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.util.ComponentesPDV;

public class DistribuicaoVendaMediaDTO {
	
	private BigInteger reparteDistribuir;
	private BigInteger reparteMinimo;
	private Boolean usarFixacao;
	private Boolean distribuicaoPorMultiplo;
	private BigInteger multiplo;
	
	private List<ProdutoEdicaoDTO> bases = new ArrayList<ProdutoEdicaoDTO>();
	private List<BonificacaoDTO> bonificacoes = new ArrayList<BonificacaoDTO>();
	
	private Boolean todasAsCotas;
	private ComponentesPDV componente;
	private String elemento;
	private String abrangenciaCriterio;
	private Double abrangencia;
	
	private Long roteiroEntregaId;
	private Boolean complementarAutomatico;
	private Boolean cotasAVista;
	private ComponentesPDV excecaoDeBancasComponente;
	private String excecaoDeBancasElemento;
	
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
	public Boolean getUsarFixacao() {
		return usarFixacao;
	}
	public void setUsarFixacao(Boolean usarFixacao) {
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
	public Boolean getCotasAVista() {
		return cotasAVista;
	}
	public void setCotasAVista(Boolean cotasAVista) {
		this.cotasAVista = cotasAVista;
	}
	public ComponentesPDV getExcecaoDeBancasComponente() {
		return excecaoDeBancasComponente;
	}
	public void setExcecaoDeBancasComponente(ComponentesPDV excecaoDeBancasComponente) {
		this.excecaoDeBancasComponente = excecaoDeBancasComponente;
	}
	public String getExcecaoDeBancasElemento() {
		return excecaoDeBancasElemento;
	}
	public void setExcecaoDeBancasElemento(String excecaoDeBancasElemento) {
		this.excecaoDeBancasElemento = excecaoDeBancasElemento;
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
				+ ", excecaoDeBancasElemento=" + excecaoDeBancasElemento + "]";
	}
	public BigInteger getMultiplo() {
		return multiplo;
	}
	public void setMultiplo(BigInteger multiplo) {
		this.multiplo = multiplo;
	}
	
}
