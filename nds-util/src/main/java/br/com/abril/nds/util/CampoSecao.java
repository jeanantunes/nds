package br.com.abril.nds.util;

/**
 * Classe que representa o campo da sessão de exportação do arquivo NFe.
 * 
 * @author Discover Technology
 *
 */
public class CampoSecao {
	
	private TipoSecao sessao;
	
	private Integer tamanho;
	
	private Integer posicao;
	
	private Object valor;
	
	private String mascara;

	public CampoSecao() {
		
	}
	
	public CampoSecao(TipoSecao sessao, Integer tamanho, Integer posicao,
			Object valor, String mascara) {
		
		this.sessao = sessao;
		this.tamanho = tamanho;
		this.posicao = posicao;
		this.valor = valor;
		this.mascara = mascara;
	}

	public CampoSecao(TipoSecao sessao, Integer posicao, Object valor) {
		
		this.sessao = sessao;
		this.posicao = posicao;
		this.valor = valor;
	}
	
	/**
	 * @return the sessao
	 */
	public TipoSecao getSessao() {
		return sessao;
	}

	/**
	 * @param sessao the sessao to set
	 */
	public void setSessao(TipoSecao sessao) {
		this.sessao = sessao;
	}

	/**
	 * @return the tamanho
	 */
	public Integer getTamanho() {
		return tamanho;
	}

	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	/**
	 * @return the posicao
	 */
	public Integer getPosicao() {
		return posicao;
	}

	/**
	 * @param posicao the posicao to set
	 */
	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	/**
	 * @return the valor
	 */
	public Object getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Object valor) {
		this.valor = valor;
	}

	/**
	 * @return the mascara
	 */
	public String getMascara() {
		return mascara;
	}

	/**
	 * @param mascara the mascara to set
	 */
	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
	
}
