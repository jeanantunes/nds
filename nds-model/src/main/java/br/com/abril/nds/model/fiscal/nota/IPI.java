package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.xml.bind.annotation.XmlElement;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name="cst", column=@Column(name="CST_IPI", length = 2, nullable = true)),
    @AttributeOverride(name="valorBaseCalculo", column=@Column(name="VLR_BASE_CALC_IPI", precision=18, scale=4, nullable = true)),
    @AttributeOverride(name="aliquota", column=@Column(name="ALIQUOTA_IPI", precision=18, scale=4, nullable = true)),
    @AttributeOverride(name="valor", column=@Column(name="VLR_IPI", precision=18, scale=4, nullable = true))
})
public class IPI extends ImpostoProduto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6435493668780899303L;
	
	/**
	 * Construtor padrão.
	 */
	public IPI() {
		
	}
	
	
	/**
	 * clEnq
	 */
	@Column(name="CLASSE_ENQUADRAMENTO_IPI", length=5, nullable=true)
	@NFEExport(secao = TipoSecao.O, posicao = 0, tamanho = 5)
	private String classeEnquadramento;
	
	/**
	 * CNPJProd
	 */
	@Column(name="CNPJ_PRODUTOR_IPI", length=14, nullable=true)
	@NFEExport(secao = TipoSecao.O, posicao = 1, tamanho = 14)
	private String cnpjProdutor;
	
	/**
	 * cSelo
	 */
	@Column(name="CODIGO_SELO_IPI", length=60, nullable=true)
	@NFEExport(secao = TipoSecao.O, posicao = 2)
	@XmlElement(name="cSelo")
	private String codigoSelo;
	/**
	 * qSelo
	 */	
	@Column(name="QUANTIDADE_SELO_IPI", length=12, nullable=true)
	@NFEExport(secao = TipoSecao.O, posicao = 3)
	@XmlElement(name="qSelo")
	private Long quantidadeSelo;
	
	/**
	 * cEnq
	 */
	@Column(name="CODIGO_ENQUADRAMENTO_IPI", length=3, nullable=true)
	@NFEExport(secao = TipoSecao.O, posicao = 4, tamanho = 3)
	@XmlElement(name="cEnq")
	private String codigoEnquadramento;
	
	/**
	 * IPITrib
	 */
	
	@XmlElement(name="IPITrib")
	@Embedded
	private TribIPI IPITrib;
	
	/**
	 * qUnid
	 */
	@Column(name="QUANTIDADE_UNIDADES", scale=4, precision=16, nullable=true)
	@NFEWhen(condition = NFEConditions.IPI_TRIB_UNID, export = @NFEExport(secao = TipoSecao.O11, posicao = 1))
	@XmlElement(name="qUnid")
	private Double quantidadeUnidades;
	
	/**
	 * vUnid
	 */
	@Column(name="VALOR_UNIDADE_TRIBUTAVEL_IPI", scale=4, precision=15, nullable=true)
	@NFEWhen(condition = NFEConditions.IPI_TRIB_UNID, export = @NFEExport(secao = TipoSecao.O11, posicao = 0))
	@XmlElement(name="vUnid")
	private Double valorUnidade;

	/**
	 * @return the classeEnquadramento
	 */
	public String getClasseEnquadramento() {
		return classeEnquadramento;
	}

	/**
	 * @param classeEnquadramento the classeEnquadramento to set
	 */
	public void setClasseEnquadramento(String classeEnquadramento) {
		this.classeEnquadramento = classeEnquadramento;
	}

	/**
	 * @return the cnpjProdutor
	 */
	public String getCnpjProdutor() {
		return cnpjProdutor;
	}

	/**
	 * @param cnpjProdutor the cnpjProdutor to set
	 */
	public void setCnpjProdutor(String cnpjProdutor) {
		this.cnpjProdutor = cnpjProdutor;
	}

	/**
	 * @return the codigoSelo
	 */
	public String getCodigoSelo() {
		return codigoSelo;
	}

	/**
	 * @param codigoSelo the codigoSelo to set
	 */
	public void setCodigoSelo(String codigoSelo) {
		this.codigoSelo = codigoSelo;
	}

	/**
	 * @return the quantidadeSelo
	 */
	public Long getQuantidadeSelo() {
		return quantidadeSelo;
	}

	/**
	 * @param quantidadeSelo the quantidadeSelo to set
	 */
	public void setQuantidadeSelo(Long quantidadeSelo) {
		this.quantidadeSelo = quantidadeSelo;
	}

	/**
	 * @return the codigoEnquadramento
	 */
	public String getCodigoEnquadramento() {
		return codigoEnquadramento;
	}

	/**
	 * @param codigoEnquadramento the codigoEnquadramento to set
	 */
	public void setCodigoEnquadramento(String codigoEnquadramento) {
		this.codigoEnquadramento = codigoEnquadramento;
	}

	/**
	 * @return the quantidadeUnidades
	 */
	public Double getQuantidadeUnidades() {
		return quantidadeUnidades;
	}

	/**
	 * @param quantidadeUnidades the quantidadeUnidades to set
	 */
	public void setQuantidadeUnidades(Double quantidadeUnidades) {
		this.quantidadeUnidades = quantidadeUnidades;
	}

	/**
	 * @return the valorUnidade
	 */
	public Double getValorUnidade() {
		return valorUnidade;
	}

	/**
	 * @param valorUnidade the valorUnidade to set
	 */
	public void setValorUnidade(Double valorUnidade) {
		this.valorUnidade = valorUnidade;
	}

	public TribIPI getIPITrib() {
		return IPITrib;
	}

	public void setIPITrib(TribIPI iPITrib) {
		IPITrib = iPITrib;
	}
}
