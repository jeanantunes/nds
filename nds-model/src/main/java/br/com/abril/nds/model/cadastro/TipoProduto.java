package br.com.abril.nds.model.cadastro;

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
@Table(name = "TIPO_PRODUTO")
@SequenceGenerator(name="TP_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class TipoProduto {

	@Id
	@GeneratedValue(generator = "TP_PRODUTO_SEQ")
	private Long id;
	private String descricao;
	private String ncm;
	@Enumerated(EnumType.STRING)
	private GrupoProduto grupoProduto;
	
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
	
	public String getNcm() {
		return ncm;
	}
	
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}
	
	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}
	
	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

}