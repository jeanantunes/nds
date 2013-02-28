package br.com.abril.nds.model.integracao.icd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LANCTO_EDICAO_PUBLICACAO")
@AttributeOverrides( {
    @AttributeOverride(name="ceItemPK.numeroChamadaEncalhe", column = @Column(name="NUM_CHAMADA_ENCALHE_CHEN") ),
    @AttributeOverride(name="ceItemPK.numeroItem", column = @Column(name="NUM_ITEM_ITCE"))
} )
public class IcdLancamentoEdicaoPublicacao {

	@Id
	@Column(name = "COD_LANCTO_EDICAO")
	private Long codigoLancamentoEdicao;
	
	@Column(name = "COD_PUBLICACAO_ADABAS")
	private String codigoPublicacao;
	
	@Column(name = "NUM_EDICAO")
	private Integer numeroEdicao;
	
	public Long getCodigoLancamentoEdicao() {
		return codigoLancamentoEdicao;
	}

	public void setCodigoLancamentoEdicao(Long codigoLancamentoEdicao) {
		this.codigoLancamentoEdicao = codigoLancamentoEdicao;
	}

	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	public Integer getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Integer numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
}
