package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroConsultaTransportadorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5613834193182583710L;

	private String razaoSocial;
	
	private String nomeFantasia;
	
	private String cnpj;
	
	private OrdenacaoColunaTransportador ordenacaoColunaTransportador;
	
	private PaginacaoVO paginacaoVO;
	
	public enum OrdenacaoColunaTransportador{
		
		CODIGO("id"),
		RAZAO_SOCIAL("razaoSocial"),
		CNPJ("cnpj"),
		RESPONSAVEL("responsavel"),
		TELEFONE("telefone"),
		EMAIL("email");
		
		private String nomeColuna;
		
		private OrdenacaoColunaTransportador(String nomeColuna){
			
			this.nomeColuna = nomeColuna;
		}
		
		public String toString(){
			return this.nomeColuna;
		}
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public OrdenacaoColunaTransportador getOrdenacaoColunaTransportador() {
		return ordenacaoColunaTransportador;
	}

	public void setOrdenacaoColunaTransportador(
			OrdenacaoColunaTransportador ordenacaoColunaTransportador) {
		this.ordenacaoColunaTransportador = ordenacaoColunaTransportador;
	}

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}