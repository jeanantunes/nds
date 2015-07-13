package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class MovimentoFinanceiroCotaDTO implements Serializable {

	private static final long serialVersionUID = 7301107686675113138L;

	private Long idMovimentoFinanceiroCota;
	
	private Cota cota; 
	
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
	
	private Usuario usuario;
	
	@Export(label="Valor", exhibitionOrder=6,columnType=ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal valor;
	
	private Date dataOperacao;
	
	private BaixaCobranca baixaCobranca;
	
	@Export(label="Data de vencimento", exhibitionOrder=2)
	private Date dataVencimento;

	private Date dataAprovacao;

	@Export(label="Data de Lançamento", exhibitionOrder=1)
	private Date dataCriacao;
	
	@Export(label="Observação", exhibitionOrder=7)
	private String observacao;
	
	private TipoEdicao tipoEdicao;
	
	private boolean aprovacaoAutomatica;
	
	private boolean lancamentoManual;
	
	private List<MovimentoEstoqueCota> movimentos;
	
	private Fornecedor fornecedor;
	
	private String motivo;
	
	private String status;
	
	private Long idUsuario;
	
	private Long idTipoMovimento;
	
	private Integer parcelas;
	
	private Integer prazo;
	
	private Long idBaixaCobranca;
	
	private Long idCota;
	
	private Date dataIntegracao;
	
	private String statusIntegracao;
	
	private Long idFornecedor;
	
	private Long usuarioAprovadorId;
	
	public MovimentoFinanceiroCotaDTO() {}
	
	/**
	 * @return the idMovimentoFinanceiroCota
	 */
	public Long getIdMovimentoFinanceiroCota() {
		return idMovimentoFinanceiroCota;
	}

	/**
	 * @param idMovimentoFinanceiroCota the idMovimentoFinanceiroCota to set
	 */
	public void setIdMovimentoFinanceiroCota(Long idMovimentoFinanceiroCota) {
		this.idMovimentoFinanceiroCota = idMovimentoFinanceiroCota;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the tipoMovimentoFinanceiro
	 */
	public TipoMovimentoFinanceiro getTipoMovimentoFinanceiro() {
		return tipoMovimentoFinanceiro;
	}

	/**
	 * @param tipoMovimentoFinanceiro the tipoMovimentoFinanceiro to set
	 */
	public void setTipoMovimentoFinanceiro(
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro) {
		this.tipoMovimentoFinanceiro = tipoMovimentoFinanceiro;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	/**
	 * @return the baixaCobranca
	 */
	public BaixaCobranca getBaixaCobranca() {
		return baixaCobranca;
	}

	/**
	 * @param baixaCobranca the baixaCobranca to set
	 */
	public void setBaixaCobranca(BaixaCobranca baixaCobranca) {
		this.baixaCobranca = baixaCobranca;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the dataAprovacao
	 */
	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	/**
	 * @param dataAprovacao the dataAprovacao to set
	 */
	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
	
	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the tipoEdicao
	 */
	public TipoEdicao getTipoEdicao() {
		return tipoEdicao;
	}

	/**
	 * @param tipoEdicao the tipoEdicao to set
	 */
	public void setTipoEdicao(TipoEdicao tipoEdicao) {
		this.tipoEdicao = tipoEdicao;
	}

	/**
	 * @return the isAprovacaoAutomatica
	 */
	public boolean isAprovacaoAutomatica() {
		return aprovacaoAutomatica;
	}

	/**
	 * @param isAprovacaoAutomatica the isAprovacaoAutomatica to set
	 */
	public void setAprovacaoAutomatica(boolean isAprovacaoAutomatica) {
		this.aprovacaoAutomatica = isAprovacaoAutomatica;
	}

	/**
	 * @return the isLancamentoManual
	 */
	public boolean isLancamentoManual() {
		return lancamentoManual;
	}

	/**
	 * @param isLancamentoManual the isLancamentoManual to set
	 */
	public void setLancamentoManual(boolean isLancamentoManual) {
		this.lancamentoManual = isLancamentoManual;
	}	
	
	@Export(label="Número da Cota", exhibitionOrder=3)
	public Integer getNumeroCota() {
		
		if (this.cota != null) {
			
			return cota.getNumeroCota();
		}
		
		return 0;
	}
	
	@Export(label="Nome da Cota", exhibitionOrder=4)
	public String getNomeCota() {

		if (this.cota != null) {

			if (this.cota.getPessoa() instanceof PessoaJuridica) {

				return ((PessoaJuridica) cota.getPessoa()).getRazaoSocial();

			} else {

				return ((PessoaFisica) cota.getPessoa()).getNome();			
			}
		}

		return "";
	}
	
	@Export(label="Tipo de Lancamento", exhibitionOrder=5 )
	public String getDescricaoTipoLancamento() {
		
		if (this.tipoMovimentoFinanceiro != null) {
			
			return tipoMovimentoFinanceiro.getDescricao();
		}

		return "";
	}

	/**
	 * Obtém movimentos
	 *
	 * @return List<MovimentoEstoqueCota>
	 */
	public List<MovimentoEstoqueCota> getMovimentos() {
		return movimentos;
	}

	/**
	 * Atribuí movimentos
	 * @param movimentos 
	 */
	public void setMovimentos(List<MovimentoEstoqueCota> movimentos) {
		this.movimentos = movimentos;
	}

	/**
	 * @return the fornecedor
	 */
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getMotivo() {
		return motivo;
	}

	public String getStatus() {
		return status;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public Long getIdTipoMovimento() {
		return idTipoMovimento;
	}

	public Integer getParcelas() {
		return parcelas;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public Long getIdBaixaCobranca() {
		return idBaixaCobranca;
	}

	public Long getIdCota() {
		return idCota;
	}

	public Date getDataIntegracao() {
		return dataIntegracao;
	}

	public String getStatusIntegracao() {
		return statusIntegracao;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setIdTipoMovimento(Long idTipoMovimento) {
		this.idTipoMovimento = idTipoMovimento;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public void setIdBaixaCobranca(Long idBaixaCobranca) {
		this.idBaixaCobranca = idBaixaCobranca;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public void setDataIntegracao(Date dataIntegracao) {
		this.dataIntegracao = dataIntegracao;
	}

	public void setStatusIntegracao(String statusIntegracao) {
		this.statusIntegracao = statusIntegracao;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Long getUsuarioAprovadorId() {
		return usuarioAprovadorId;
	}

	public void setUsuarioAprovadorId(Long usuarioAprovadorId) {
		this.usuarioAprovadorId = usuarioAprovadorId;
	}
	
	
	
}
