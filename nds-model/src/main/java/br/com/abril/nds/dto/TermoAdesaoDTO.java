package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;

public class TermoAdesaoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8960141758963663470L;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private String nomeDistribuidor;
	
	private BigDecimal valorDebito;
	
	private BigDecimal porcentagemDebito;
	
	private String logradouroEntrega;
	
	private String CEPEntrega;
	
	private String bairroEntrega;
	
	private String cidadeEntrega;
	
	private String referenciaEndereco;
	
	private Set<PeriodoFuncionamentoPDV> horariosFuncionamento;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getNomeDistribuidor() {
		return nomeDistribuidor;
	}

	public void setNomeDistribuidor(String nomeDistribuidor) {
		this.nomeDistribuidor = nomeDistribuidor;
	}

	public BigDecimal getValorDebito() {
		return valorDebito;
	}

	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

	public BigDecimal getPorcentagemDebito() {
		return porcentagemDebito;
	}

	public void setPorcentagemDebito(BigDecimal porcentagemDebito) {
		this.porcentagemDebito = porcentagemDebito;
	}

	public String getLogradouroEntrega() {
		return logradouroEntrega;
	}

	public void setLogradouroEntrega(String logradouroEntrega) {
		this.logradouroEntrega = logradouroEntrega;
	}

	public String getCEPEntrega() {
		return CEPEntrega;
	}

	public void setCEPEntrega(String cEPEntrega) {
		CEPEntrega = cEPEntrega;
	}

	public String getBairroEntrega() {
		return bairroEntrega;
	}

	public void setBairroEntrega(String bairroEntrega) {
		this.bairroEntrega = bairroEntrega;
	}

	public String getCidadeEntrega() {
		return cidadeEntrega;
	}

	public void setCidadeEntrega(String cidadeEntrega) {
		this.cidadeEntrega = cidadeEntrega;
	}

	public String getReferenciaEndereco() {
		return referenciaEndereco;
	}

	public void setReferenciaEndereco(String referenciaEndereco) {
		this.referenciaEndereco = referenciaEndereco;
	}

	public Set<PeriodoFuncionamentoPDV> getHorariosFuncionamento() {
		return horariosFuncionamento;
	}

	public void setHorariosFuncionamento(
			Set<PeriodoFuncionamentoPDV> horariosFuncionamento) {
		this.horariosFuncionamento = horariosFuncionamento;
	}
}
