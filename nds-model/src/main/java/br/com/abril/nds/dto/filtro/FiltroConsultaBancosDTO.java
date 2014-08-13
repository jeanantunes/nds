package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de boletos
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroConsultaBancosDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Nome")
	private String nome;
	
	@Export(label = "Banco")
	private String numero;
	
	@Export(label = "Cedente")
	private String cedente;
	
	@Export(label = "Status")
	private boolean ativo;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaBancos ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroConsultaBancosDTO(){

	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBancosDTO(String nome,
						           String numero,
						           String cedente,
						           boolean ativo){
		this.nome=nome;
		this.numero=numero;
		this.cedente=cedente;
		this.ativo=ativo;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBancosDTO(String nome,
						           String numero,
						           String cedente){
		this.nome=nome;
		this.numero=numero;
		this.cedente=cedente;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBancosDTO(String nome,
						           String numero){
		this.nome=nome;
		this.numero=numero;

	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBancosDTO(String nome){
		this.nome=nome;
	}
	
	
	
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaBancos{
		
		CODIGO_BANCO("codigo"),
		NUMERO_BANCO("banco"),
		NOME_BANCO("nome"),
		AGENCIA_BANCO("agencia"),
		CONTA_CORRENTE_BANCO("contaCorrente"),
		CEDENTE_BANCO("cedente"),
		APELIDO_BANCO("apelido"),
		CARTEIRA_BANCO("carteira"),
		ATIVO_BANCO("status");
		
		private String nomeColuna;
		
		private OrdenacaoColunaBancos(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCedente() {
		return cedente;
	}

	public void setCedente(String cedente) {
		this.cedente = cedente;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public OrdenacaoColunaBancos getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaBancos ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ativo ? 1231 : 1237);
		result = prime * result + ((cedente == null) ? 0 : cedente.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroConsultaBancosDTO other = (FiltroConsultaBancosDTO) obj;
		if (ativo != other.ativo)
			return false;
		if (cedente == null) {
			if (other.cedente != null)
				return false;
		} else if (!cedente.equals(other.cedente))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
	
}
