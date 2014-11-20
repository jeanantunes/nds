package br.com.abril.nds.integracao.ems0198.outbound;

public class EMS198HeaderDTO {
	
	private String cotaId;
	private String numCota;
	private String dataRecolhimento;
	private String codDistribuidor;
	
	public String getCotaId() {
		return cotaId;
	}
	public void setCotaId(String cotaId) {
		this.cotaId = cotaId;
	}
	public String getNumCota() {
		return numCota;
	}
	public void setNumCota(String numCota) {
		this.numCota = numCota;
	}
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	public String getCodDistribuidor() {
		return codDistribuidor;
	}
	public void setCodDistribuidor(String codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
}
