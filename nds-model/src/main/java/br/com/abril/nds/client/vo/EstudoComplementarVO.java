package br.com.abril.nds.client.vo;

import java.util.Date;

public class EstudoComplementarVO {

    int  reparteCota;
    int  reparteDistribuicao;
    int  reparteLancamento;
    int  reparteSobra;
    long codigoEstudo;
    String tipoSelecao;
    long idEstudoComplementar;
    long idLancamento;
    long idProdutoEdicao;
    boolean multiplo;
    Long idCopia;
    
    private Date dataDeLancamento;
    
    private String dataLancamento;
    
	public int getReparteCota() {
		return reparteCota;
	}
	public void setReparteCota(int reparteCota) {
		this.reparteCota = reparteCota;
	}
	public int getReparteDistribuicao() {
		return reparteDistribuicao;
	}
	public void setReparteDistribuicao(int reparteDistribuicao) {
		this.reparteDistribuicao = reparteDistribuicao;
	}
	public int getReparteLancamento() {
		return reparteLancamento;
	}
	public void setReparteLancamento(int reparteLancamento) {
		this.reparteLancamento = reparteLancamento;
	}
	public long getCodigoEstudo() {
		return codigoEstudo;
	}
	public void setCodigoEstudo(long codigoEstudo) {
		this.codigoEstudo = codigoEstudo;
	}
	public int getReparteSobra() {
		return reparteSobra;
	}
	public void setReparteSobra(int reparteSobra) {
		this.reparteSobra = reparteSobra;
	}
	public String getTipoSelecao() {
		return tipoSelecao;
	}
	public void setTipoSelecao(String tipoSelecao) {
		this.tipoSelecao = tipoSelecao;
	}
	public long getIdEstudoComplementar() {
		return idEstudoComplementar;
	}
	public void setIdEstudoComplementar(long idEstudoComplementar) {
		this.idEstudoComplementar = idEstudoComplementar;
	}
	public long getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(long idLancamento) {
		this.idLancamento = idLancamento;
	}
	public long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	public boolean isMultiplo() {
		return multiplo;
	}
	public void setMultiplo(boolean multiplo) {
		this.multiplo = multiplo;
	}
	public Long getIdCopia() {
		return idCopia;
	}
	public void setIdCopia(Long idCopia) {
		this.idCopia = idCopia;
	}
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public Date getDataDeLancamento() {
		return dataDeLancamento;
	}
	public void setDataDeLancamento(Date dataDeLancamento) {
		this.dataDeLancamento = dataDeLancamento;
	}
	
}
