package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DISTRIBUIDOR_NOTA_FISCAL_TIPO_EMISSAO")
public class NotaFiscalTipoEmissao {

	public enum NotaFiscalTipoEmissaoEnum {
		DESOBRIGA_EMISSAO,
		CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS,
		CONSOLIDA_EMISSAO_POR_DESTINATARIO
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TIPO_EMISSAO_ENUM")
	private NotaFiscalTipoEmissaoEnum tipoEmissao;

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

	public NotaFiscalTipoEmissaoEnum getTipoEmissao() {
		return tipoEmissao;
	}

	public void setTipoEmissao(NotaFiscalTipoEmissaoEnum tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}
	
}