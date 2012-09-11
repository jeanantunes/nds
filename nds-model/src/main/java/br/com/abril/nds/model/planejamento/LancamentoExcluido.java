package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LANCAMENTO_EXCLUIDO")
@SequenceGenerator(name = "LANCAMENTO_EXCLUIDO_SEQ", initialValue = 1, allocationSize = 1)
public class LancamentoExcluido implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5606394144169829387L;

	@Id
	@GeneratedValue(generator = "LANCAMENTO_EXCLUIDO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO_PRODUTO")
	private String codigoProduto;
	
	@Column(name = "NOME_PRODUTO")
	private String nomeProduto;
	
	@Column(name = "NUMERO_EDICAO")
	private Long numeroEdicao;
	
	@Column(name = "REPARTE")
	private BigInteger reparte;
	
	@Column(name = "DATA_LANCAMENTO")
	private Date dataLancamento;
	
	public LancamentoExcluido(String codigoProduto, String nomeProduto, Long numeroEdicao, 
			BigInteger reparte, Date dataLancamento){
		
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.reparte = reparte;
		this.dataLancamento = dataLancamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	} 
}