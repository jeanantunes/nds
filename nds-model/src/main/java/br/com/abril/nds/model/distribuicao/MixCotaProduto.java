package br.com.abril.nds.model.distribuicao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "MIX_COTA_PRODUTO")
@SequenceGenerator(name="MIX_COTA_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class MixCotaProduto {

	@Id
	@GeneratedValue(generator = "MIX_COTA_PRODUTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cota;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;
	
	@Column(name = "DATAHORA")
	private Date dataHora;
	
	@Column(name = "VENDA_MED")
	private Long vendaMedia;
	
	@Column(name = "REPARTE_MED")
	private Long reparteMedio;
	
	@Column(name = "ULTIMO_REPARTE")
	private Long ultimoReparte;
	
	@Column(name = "REPARTE_MIN")
	private Long reparteMinimo;
	
	@Column(name = "REPARTE_MAX")
	private Long reparteMaximo;
	
	@Column(name = "CODIGO_ICD")
	private String codigoICD;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "TIPO_CLASSIFICACAO_PRODUTO_ID")
	private TipoClassificacaoProduto tipoClassificacaoProduto;
	
	@Cascade({CascadeType.ALL})
	@OneToMany(mappedBy="mixCotaProduto")
	List<RepartePDV> repartesPDV;
	
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Long getVendaMedia() {
		return vendaMedia;
	}

	public void setVendaMedia(Long vendaMedia) {
		this.vendaMedia = vendaMedia;
	}

	public Long getReparteMedio() {
		return reparteMedio;
	}

	public void setReparteMedio(Long reparteMedio) {
		this.reparteMedio = reparteMedio;
	}

	public Long getUltimoReparte() {
		return ultimoReparte;
	}

	public void setUltimoReparte(Long ultimoReparte) {
		this.ultimoReparte = ultimoReparte;
	}

	public Long getReparteMinimo() {
		return reparteMinimo;
	}

	public void setReparteMinimo(Long reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}

	public Long getReparteMaximo() {
		return reparteMaximo;
	}

	public void setReparteMaximo(Long reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}

	public List<RepartePDV> getRepartesPDV() {
		return repartesPDV;
	}

	public void setRepartesPDV(List<RepartePDV> repartesPDV) {
		this.repartesPDV = repartesPDV;
	}

	public String getCodigoICD() {
		return codigoICD;
	}

	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}

	public TipoClassificacaoProduto getTipoClassificacaoProduto() {
		return tipoClassificacaoProduto;
	}

	public void setTipoClassificacaoProduto(
			TipoClassificacaoProduto tipoClassificacaoProduto) {
		this.tipoClassificacaoProduto = tipoClassificacaoProduto;
	}
	
}
