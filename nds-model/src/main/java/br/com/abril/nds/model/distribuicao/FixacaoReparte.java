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
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "FIXACAO_REPARTE")
@SequenceGenerator(name="FIXREP_SEQ", initialValue = 1, allocationSize = 1)
public class FixacaoReparte {
	
	@Id
	@GeneratedValue(generator = "FIXREP_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cotaFixada;

    @ManyToOne
    @JoinColumn(name = "ID_CLASSIFICACAO_EDICAO", nullable = true)
    private TipoClassificacaoProduto classificacaoProdutoEdicao;
	
	@Column(name = "CODIGO_ICD")
	private String codigoICD;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Cascade({CascadeType.REMOVE,CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@OneToMany(mappedBy="fixacaoReparte")
	List<FixacaoRepartePdv> repartesPDV;
	
	@Column(name="QTDE_EDICOES")
	private Integer qtdeEdicoes;
	
	@Column(name="QTDE_EXEMPLARES")
	private Integer qtdeExemplares;
	
	@Column(name="ED_INICIAL")
	private Integer edicaoInicial;
	
	@Column(name="ED_FINAL")
	private Integer edicaoFinal;
	
	@Column(name="ED_ATENDIDAS")
	private Integer edicoesAtendidas;
	
	@Column(name="DATA_HORA")
	private Date dataHora;
	
	@Column(name="MANTER_FIXA")
	private Boolean manterFixa = false;
	
	@Column(name="DATA_FIXA_CADASTRO_FIXACAO")
	private Date dataFixa;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCotaFixada() {
		return cotaFixada;
	}

	public void setCotaFixada(Cota cotaFixada) {
		this.cotaFixada = cotaFixada;
	}

	public String getCodigoICD() {
		return codigoICD;
	}

	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}


	public Integer getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(Integer qtdeEdicoes) {
		this.qtdeEdicoes = qtdeEdicoes;
	}

	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}

	public void setQtdeExemplares(Integer qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getEdicaoInicial() {
		return edicaoInicial;
	}

	public void setEdicaoInicial(Integer edicaoInicial) {
		if(edicaoInicial==null){
			this.edicaoInicial = 0;
		}else{
			this.edicaoInicial = edicaoInicial;	
		}
		
	}

	public Integer getEdicaoFinal() {
		return edicaoFinal;
	}

	public void setEdicaoFinal(Integer edicaoFinal) {

		if(edicaoFinal==null){
			this.edicaoFinal=0;
		}else{
			this.edicaoFinal = edicaoFinal;	
		}

	}

	public Integer getEdicoesAtendidas() {
		return edicoesAtendidas;
	}

	public void setEdicoesAtendidas(Integer edicoesAtendidas) {
		if(edicoesAtendidas==null){
			this.edicoesAtendidas=0;
		}else{
			this.edicoesAtendidas = edicoesAtendidas;	
		}
		
	}

	public List<FixacaoRepartePdv> getRepartesPDV() {
		return repartesPDV;
	}

	public void setRepartesPDV(List<FixacaoRepartePdv> repartesPDV) {
		this.repartesPDV = repartesPDV;
	}

	public Boolean isManterFixa() {
		return manterFixa;
	}

	public void setManterFixa(Boolean manterFixa) {
		this.manterFixa = manterFixa;
	}

    public TipoClassificacaoProduto getClassificacaoProdutoEdicao() {
        return classificacaoProdutoEdicao;
    }

    public void setClassificacaoProdutoEdicao(TipoClassificacaoProduto classificacaoProdutoEdicao) {
        this.classificacaoProdutoEdicao = classificacaoProdutoEdicao;
    }

	public Date getDataFixa() {
		return dataFixa;
	}

	public void setDataFixa(Date dataFixa) {
		this.dataFixa = dataFixa;
	}
    
}