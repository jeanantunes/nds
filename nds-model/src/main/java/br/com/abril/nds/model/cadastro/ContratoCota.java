package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "COTA")
public class ContratoCota extends Contrato implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2620583362349546771L;
	
	@Column(name = "EXIGE_DOC_SUSPENSAO", nullable = false)
	private boolean exigeDocumentacaoSuspencao;
	
	@Column(name="RECEBIDO", nullable = true)
	private Boolean recebido = false;
	
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
