package br.com.abril.nds.model.movimentacao;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "RATEIO_COTA_AUSENTE")
@SequenceGenerator(name="RATEIO_COTA_AUSENTE_SEQ", initialValue = 1, allocationSize = 1)
public class RateioCotaAusente {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "RATEIO_COTA_AUSENTE_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@ManyToOne
	@JoinColumn(name = "COTA_AUSENTE_ID")
	private CotaAusente cotaAusente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	public CotaAusente getCotaAusente() {
		return cotaAusente;
	}
	
	public void setCotaAusente(CotaAusente cotaAusente) {
		this.cotaAusente = cotaAusente;
	}

}
