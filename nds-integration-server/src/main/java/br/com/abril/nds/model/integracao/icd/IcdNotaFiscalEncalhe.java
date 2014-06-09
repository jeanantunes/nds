package br.com.abril.nds.model.integracao.icd;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.abril.nds.model.integracao.icd.pks.CEPK;

@Entity
@Table(name = "NOTA_FISCAL_ENCALHE")
public class IcdNotaFiscalEncalhe {
	
	@EmbeddedId
	private CEPK cePK;
	
	@Transient
	private String tipoDocumento;
	
	@OneToMany(mappedBy="id")
	List<IcdNotaFiscalEncalheItem> notaFiscalEncalheItens;

	/**
	 * Getters e Setters 
	 */
	public CEPK getCePK() {
		return cePK;
	}

	public void setCePK(CEPK cePK) {
		this.cePK = cePK;
	}
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<IcdNotaFiscalEncalheItem> getNotaFiscalEncalheItens() {
		return notaFiscalEncalheItens;
	}

	public void setNotaFiscalEncalheItens(List<IcdNotaFiscalEncalheItem> notaFiscalEncalheItens) {
		this.notaFiscalEncalheItens = notaFiscalEncalheItens;
	}
	
}
