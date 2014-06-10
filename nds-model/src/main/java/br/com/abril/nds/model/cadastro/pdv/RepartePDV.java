package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;


@Entity
@Table(name = "REPARTE_PDV")
@SequenceGenerator(name="REPARTE_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class RepartePDV {

	@Id
	@GeneratedValue(generator = "REPARTE_PDV_SEQ")
	@Column(name = "ID",nullable=false)
	private Long id;
	
	@Column(name="REPARTE")
	private Integer reparte;
	
	@OneToOne
	@JoinColumn(name = "PDV_ID",nullable=false)
	private PDV pdv;
	
	@OneToOne
	@JoinColumn(name = "PRODUTO_ID",nullable=false)
	private Produto produto;
	
	@ManyToOne
    @JoinColumn(name="MIX_COTA_PRODUTO_ID")
    private MixCotaProduto mixCotaProduto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getReparte() {
		return reparte;
	}

	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public MixCotaProduto getMixCotaProduto() {
		return mixCotaProduto;
	}

	public void setMixCotaProduto(MixCotaProduto mixCotaProduto) {
		this.mixCotaProduto = mixCotaProduto;
	}
	
}
