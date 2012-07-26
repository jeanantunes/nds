package br.com.abril.nds.client.vo;

/**
 * @author InfoA2
 */
public class DetalheProcessamentoVO {

	private String tipoErro;
	
	private String mensagem;
	
	private String numeroLinha;

	public String getTipoErro() {
		return tipoErro;
	}

	public void setTipoErro(String tipoErro) {
		this.tipoErro = tipoErro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getNumeroLinha() {
		return numeroLinha;
	}

	public void setNumeroLinha(String numeroLinha) {
		this.numeroLinha = numeroLinha;
	}
	
}
