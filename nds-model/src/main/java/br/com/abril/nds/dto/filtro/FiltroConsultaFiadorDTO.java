package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroConsultaFiadorDTO implements Serializable {

	private static final long serialVersionUID = 2330695774689445990L;
	
	private String nome;
	
	private String cpfCnpj;
	
	private OrdenacaoColunaFiador ordenacaoColunaFiador;
	
	private PaginacaoVO paginacaoVO;
	
	public enum OrdenacaoColunaFiador {
		CODIGO("codigo"),
		NOME("nome"),
		CPF_CNPJ("cpfCnpj"),
		RG_INSCRICAO("rgInscricao"),
		EMAIL("email"),
		TELEFONE("telefone");
		
		private String nomeColuna;
		
		private OrdenacaoColunaFiador(String nomeColuna){
			this.nomeColuna = nomeColuna;
		}
		
		public String toString(){
			return this.nomeColuna;
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public OrdenacaoColunaFiador getOrdenacaoColunaFiador() {
		return ordenacaoColunaFiador;
	}

	public void setOrdenacaoColunaFiador(OrdenacaoColunaFiador ordenacaoColunaFiador) {
		this.ordenacaoColunaFiador = ordenacaoColunaFiador;
	}

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
}