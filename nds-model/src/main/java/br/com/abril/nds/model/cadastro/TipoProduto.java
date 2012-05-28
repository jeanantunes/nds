package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class TipoProduto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5644686996571892781L;
	
	@Id
	@GeneratedValue(generator = "TP_PRODUTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO", nullable = false, unique = true)
	private String descricao;
	
	@Column(name = "CODIGO", nullable = false, unique = true)
	private Long codigo;
	
	@Column(name = "CODIGO_NCM")
	private String codigoNCM;
	
	@Column(name = "CODIGO_NBM")
	private String codigoNBM;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_PRODUTO", nullable = false)
	private GrupoProduto grupoProduto;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="tipoProduto")
	private List<Produto> listaProdutos;
	
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
	
	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}
	
	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCodigoNCM() {
		return codigoNCM;
	}

	public void setCodigoNCM(String codigoNCM) {
		this.codigoNCM = codigoNCM;
	}

	public String getCodigoNBM() {
		return codigoNBM;
	}

	public void setCodigoNBM(String codigoNBM) {
		this.codigoNBM = codigoNBM;
	}

	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}
	
}