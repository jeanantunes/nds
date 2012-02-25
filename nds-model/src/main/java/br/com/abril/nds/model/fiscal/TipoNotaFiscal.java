package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "TIPO_NOTA_FISCAL")
@SequenceGenerator(name="TP_NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class TipoNotaFiscal {

	@Id
	@GeneratedValue(generator = "TP_NOTA_FISCAL_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}
	
	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

}