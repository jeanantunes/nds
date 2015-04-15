package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@DiscriminatorValue(value = "COTA")
public class ContratoCota extends Contrato implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2620583362349546771L;
	@JsonBackReference
	@OneToOne(optional = false)
	
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "EXIGE_DOC_SUSPENSAO", nullable = false)
	private boolean exigeDocumentacaoSuspencao;
	
	@Column(name="RECEBIDO", nullable = true)
	private Boolean recebido = false;
	
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

	public boolean isRecebido() {
		return recebido;
	}

	public void setRecebido(boolean recebido) {
		this.recebido = recebido;
	}
	
	
}