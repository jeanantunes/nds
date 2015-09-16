package br.com.abril.nds.util;

public class AnexoEmail {

	
	private String nome;
	private byte[] anexo;
	private TipoAnexo tipoAnexo;
	
	public enum TipoAnexo{
		PDF(".pdf"),
		XLS(".xls"),
		ENP(".ENP");
		
		private String tipo;
		
		private TipoAnexo(String tipo) {
			this.tipo = tipo;
		}
		
		public String getTipoAnexo(){
			return tipo;
		}
		
		@Override
		public String toString() {
			return this.tipo;
		}
	}
	
	public AnexoEmail() {}
	
	public AnexoEmail(String nome, byte[] anexo,TipoAnexo tipoAnexo) {
		this.nome = nome;
		this.anexo = anexo;
		this.tipoAnexo = tipoAnexo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome+tipoAnexo;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the anexo
	 */
	public byte[] getAnexo() {
		return anexo;
	}

	/**
	 * @param anexo the anexo to set
	 */
	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}

	/**
	 * @return the tipoAnexo
	 */
	public TipoAnexo getTipoAnexo() {
		return tipoAnexo;
	}

	/**
	 * @param tipoAnexo the tipoAnexo to set
	 */
	public void setTipoAnexo(TipoAnexo tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}
	
	
	
}
