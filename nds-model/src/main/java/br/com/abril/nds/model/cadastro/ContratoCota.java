package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "COTA")
public class ContratoCota extends Contrato {
	
	@OneToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@Column(name = "EXIGE_DOC_SUSPENSAO", nullable = false)
	private boolean exigeDocumentacaoSuspencao;
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public boolean isExigeDocumentacaoSuspencao() {
		return exigeDocumentacaoSuspencao;
	}
	
	public void setExigeDocumentacaoSuspencao(boolean exigeDocumentacaoSuspencao) {
		this.exigeDocumentacaoSuspencao = exigeDocumentacaoSuspencao;
	}

}
