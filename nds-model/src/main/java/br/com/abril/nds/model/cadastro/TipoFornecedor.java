package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_FORNECEDOR")
@SequenceGenerator(name="TP_FORNECEDOR_SEQ", initialValue = 1, allocationSize = 1)
public class TipoFornecedor {

	@Id
	@GeneratedValue(generator = "TP_FORNECEDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_FORNECEDOR", nullable = false)
	private GrupoFornecedor grupoFornecedor;

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
	
	public GrupoFornecedor getGrupoFornecedor() {
		return grupoFornecedor;
	}
	
	public void setGrupoFornecedor(GrupoFornecedor grupoFornecedor) {
		this.grupoFornecedor = grupoFornecedor;
	}

}