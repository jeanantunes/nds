package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Segmentação de Produto
 * 
 * @author luiz.marcili
 *
 */
@Embeddable
public class SegmentacaoProduto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CLASSE_SOCIAL")
	private ClasseSocial classeSocial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO")
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FAIXA_ETARIA")
	private FaixaEtaria faixaEtaria;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMATO_PRODUTO")
	private FormatoProduto formatoProduto;
	
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TEMA_PRINCIPAL")
	private TemaProduto temaPrincipal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TEMA_SECUNDARIO")
	private TemaProduto temaSecundario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMA_FISICA")
	private FormaFisica formaFisica;

	/**
	 * @return the classeSocial
	 */
	public ClasseSocial getClasseSocial() {
		return classeSocial;
	}

	/**
	 * @param classeSocial the classeSocial to set
	 */
	public void setClasseSocial(ClasseSocial classeSocial) {
		this.classeSocial = classeSocial;
	}

	/**
	 * @return the sexo
	 */
	public Sexo getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the faixaEtaria
	 */
	public FaixaEtaria getFaixaEtaria() {
		return faixaEtaria;
	}

	/**
	 * @param faixaEtaria the faixaEtaria to set
	 */
	public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	/**
	 * @return the formatoProduto
	 */
	public FormatoProduto getFormatoProduto() {
		return formatoProduto;
	}

	/**
	 * @param formatoProduto the formatoProduto to set
	 */
	public void setFormatoProduto(FormatoProduto formatoProduto) {
		this.formatoProduto = formatoProduto;
	}


	/**
	 * @return the temaPrincipal
	 */
	public TemaProduto getTemaPrincipal() {
		return temaPrincipal;
	}

	/**
	 * @param temaPrincipal the temaPrincipal to set
	 */
	public void setTemaPrincipal(TemaProduto temaPrincipal) {
		this.temaPrincipal = temaPrincipal;
	}

	/**
	 * @return the temaSecundario
	 */
	public TemaProduto getTemaSecundario() {
		return temaSecundario;
	}

	/**
	 * @param temaSecundario the temaSecundario to set
	 */
	public void setTemaSecundario(TemaProduto temaSecundario) {
		this.temaSecundario = temaSecundario;
	}

	public FormaFisica getFormaFisica() {
		return formaFisica;
	}

	public void setFormaFisica(FormaFisica formaFisica) {
		this.formaFisica = formaFisica;
	}

 
}

