package br.com.abril.nds.client.vo;

public class EstudoComplementarVO {


	int  reparteCota;
    int  reparteDistribuicao;
    int  reparteLancamento;
    int  reparteSobra;
    long codigoEstudo;
    int  tipoSelecao;
    long idEstudoComplementar;
    long idLancamento;
    
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
	public int getTipoSelecao() {
		return tipoSelecao;
	}
	public void setTipoSelecao(int tipoSelecao) {
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

}
