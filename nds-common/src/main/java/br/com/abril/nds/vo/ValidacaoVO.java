package br.com.abril.nds.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;

/**
 * Value Object que representa as validações do sistema. 
 * 
 * @author Discover Technology
 *
 */
public class ValidacaoVO {

	private TipoMensagem tipoMensagem;
	
	private List<String> listaMensagens;
	
	private List<? extends Serializable> dados;
	
	private boolean tratarValidacao;

	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem
	 * @param mensagem
	 */
	public ValidacaoVO(TipoMensagem tipoMensagem, String mensagem) {
		
		this.tipoMensagem = tipoMensagem;
		
		this.listaMensagens = new ArrayList<String>();
		
		this.listaMensagens.add(mensagem);
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem
	 * @param mensagem
	 */
	public ValidacaoVO(TipoMensagem tipoMensagem, String mensagem, boolean tratarValidacao) {
		
		this.tipoMensagem = tipoMensagem;
		
		this.listaMensagens = new ArrayList<String>();
		
		this.listaMensagens.add(mensagem);
		
		this.tratarValidacao = tratarValidacao;
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem
	 * @param listaMensagens
	 */
	public ValidacaoVO(TipoMensagem tipoMensagem, List<String> listaMensagens) {
		
		this.tipoMensagem = tipoMensagem;
		
		this.listaMensagens = listaMensagens;
	}
	
	/**
	 * Construtor padrão.
	 */
	public ValidacaoVO() { }


	/**
	 * @return the tipoMensagem
	 */
	public TipoMensagem getTipoMensagem() {
		return tipoMensagem;
	}

	/**
	 * @param tipoMensagem the tipoMensagem to set
	 */
	public void setTipoMensagem(TipoMensagem tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	/**
	 * @return the listaMensagens
	 */
	public List<String> getListaMensagens() {
		return listaMensagens;
	}

	/**
	 * @param listaMensagens the listaMensagens to set
	 */
	public void setListaMensagens(List<String> listaMensagens) {
		this.listaMensagens = listaMensagens;
	}

	/**
	 * @return the dados
	 */
	public List<? extends Serializable> getDados() {
		return dados;
	}

	/**
	 * @param dados the dados to set
	 */
	public void setDados(List<? extends Serializable> dados) {
		this.dados = dados;
	}

	public boolean isTratarValidacao() {
		return tratarValidacao;
	}

	public void setTratarValidacao(boolean tratarValidacao) {
		this.tratarValidacao = tratarValidacao;
	}
	
	public void addMensagem(String mensagem) {
		
		if (this.listaMensagens == null) {
			this.listaMensagens = new ArrayList<String>();
		}
		
		this.listaMensagens.add(mensagem);
	}	
}
