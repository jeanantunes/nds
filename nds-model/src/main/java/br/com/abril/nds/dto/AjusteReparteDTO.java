package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAjusteReparte;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class AjusteReparteDTO implements Serializable {

	private static final long serialVersionUID = -4961603374837991607L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Nome do PDV", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nomePDV;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String statusCota;

	@Export(label = "Forma de Ajuste", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String formaAjusteAplicado;
	
	@Export(label = "Ajuste Aplicado", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private BigDecimal ajusteAplicado;

	@Export(label = "Data Inicio", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String dataInicio;
	
	@Export(label = "Data Fim", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String dataFim;
	
	@Export(label = "Motivo", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String motivoAjusteAplicado;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String nomeUsuario;
	
	@Export(label = "Data Alteração", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private String dataAlteracao;
	
	@Export(label = "Hora", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private String hora;
	
	private TipoSegmentoProduto tipoSegmento_Ajuste;
	private Usuario usuario;
	private Long idAjusteReparte;
	private Date dataInicioCadastro;
	private Date dataFimCadastro;
	private Date dataAlteracaoCadastro;
	private TipoAjusteReparte formaAjuste;
	private MotivoAlteracaoSituacao motivoAjuste; 
	private SituacaoCadastro status;
	private Long idSegmento;
	private PaginacaoVO paginacao;
	
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public Date getDataInicioCadastro() {
		return dataInicioCadastro;
	}
	public void setDataInicioCadastro(Date dataInicioCadastro) {
		this.dataInicioCadastro = dataInicioCadastro;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(Date date){
		this.hora = DateUtil.formatarHoraMinuto(date);
	}
	public Date getDataFimCadastro() {
		return dataFimCadastro;
	}
	public void setDataFimCadastro(Date dataFimCadastro) {
		this.dataFimCadastro = dataFimCadastro;
	}
	public Long getIdAjusteReparte() {
		return idAjusteReparte;
	}
	public void setIdAjusteReparte(Long idAjusteReparte) {
		this.idAjusteReparte = idAjusteReparte;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public TipoAjusteReparte getFormaAjuste() {
		return formaAjuste;
	}
	public void setFormaAjuste(TipoAjusteReparte formaAjuste) {
		this.formaAjuste = formaAjuste;
	}
	public BigDecimal getAjusteAplicado() {
		return ajusteAplicado;
	}
	public void setAjusteAplicado(BigDecimal ajusteAplicado) {
		this.ajusteAplicado = ajusteAplicado;
	}
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = DateUtil.formatarDataPTBR(dataInicio);
	}
	public String getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = DateUtil.formatarDataPTBR(dataFim);
	}
	public String getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = DateUtil.formatarDataPTBR(dataAlteracao);
		this.hora = DateUtil.formatarHoraMinuto(dataAlteracao);
	}
	public MotivoAlteracaoSituacao getMotivoAjuste() {
		return motivoAjuste;
	}
	public void setMotivoAjuste(MotivoAlteracaoSituacao motivoAjuste) {
		this.motivoAjuste = motivoAjuste;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getNomePDV() {
		return nomePDV;
	}
	public void setNomePDV(String nomePDV) {
		if(nomePDV == null){
			this.nomePDV = "";
		}else{
			this.nomePDV = nomePDV;			
		}
	}
	public SituacaoCadastro getStatus() {
		return status;
	}
	public void setStatus(SituacaoCadastro status) {
		this.status = status;
	}
	
	public String getFormaAjusteAplicado() {
		return formaAjusteAplicado;
	}
	
	public void setFormaAjusteAplicado(TipoAjusteReparte formaAjuste) {
		if(formaAjuste == null){
			this.formaAjusteAplicado = "";
		}else{
			this.formaAjusteAplicado = formaAjuste.toString();
		}
	}
	
	public String getMotivoAjusteAplicado() {
		return motivoAjusteAplicado;
	}
	
	public void setMotivoAjusteAplicado(MotivoAlteracaoSituacao motivoAjuste) {
		if(motivoAjuste == null){
			this.motivoAjusteAplicado = "";
		}else{
			this.motivoAjusteAplicado = motivoAjuste.toString();
		}
	}
	
	public String getStatusCota() {
		return statusCota;
	}
	
	public void setStatusCota(SituacaoCadastro status) {
		if(status == null){
			this.statusCota = "";
		}else{
			this.statusCota = status.toString();
		}
	}
	
	public Date getDataAlteracaoCadastro() {
		return dataAlteracaoCadastro;
	}
	
	public void setDataAlteracaoCadastro(Date dataAlteracaoCadastro) {
		this.dataAlteracaoCadastro = dataAlteracaoCadastro;
	}
	public TipoSegmentoProduto getTipoSegmento_Ajuste() {
		return tipoSegmento_Ajuste;
	}
	public void setTipoSegmento_Ajuste(TipoSegmentoProduto tipoSegmento_Ajuste) {
		
		if(tipoSegmento_Ajuste != null){
			this.tipoSegmento_Ajuste = tipoSegmento_Ajuste;
		}else{
			TipoSegmentoProduto segmento = new TipoSegmentoProduto();
			segmento.setId((long) 0);
			this.tipoSegmento_Ajuste = segmento;
		}
	}
	public Long getIdSegmento() {
		return idSegmento;
	}
	public void setIdSegmento(Long idSegmento) {
		this.idSegmento = idSegmento;
	}
	
}
