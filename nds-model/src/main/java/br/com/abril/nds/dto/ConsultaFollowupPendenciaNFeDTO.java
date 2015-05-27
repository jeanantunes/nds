package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupPendenciaNFeDTO implements Serializable {

	private static final long serialVersionUID = 3464379590533509374L;
	
	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
    private Integer numeroCota;    
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
	@Export(label = "Tipo Pendencia", alignment=Alignment.RIGHT, exhibitionOrder = 3)
   	private StatusNotaFiscalEntrada tipoPendencia;   	
	
	@Export(label = "Dt. Entrada", alignment=Alignment.CENTER, exhibitionOrder = 4)
   	private String dataEntrada;   	

   	private BigDecimal valorDiferenca;   	

   	@Export(label = "Telefone", alignment=Alignment.RIGHT, exhibitionOrder = 6)
    private String numeroTelefone;
   	
   	private String valorDiferencaFormatado;
   	
   	private Long idNotaFiscalEntrada;
	
	private Long numeroNfe;
	
	private String serie;
	
	private String chaveAcesso;
	
	private Long idControleConferenciaEncalheCota;
   	
	private String dataEncalhe;
	
	private BigDecimal valorNota;
	
   	public ConsultaFollowupPendenciaNFeDTO() {}

	public ConsultaFollowupPendenciaNFeDTO(Integer numeroCota, String nomeJornaleiro, StatusNotaFiscalEntrada tipoPendencia, String dataEntrada, BigDecimal valorDiferenca, String numeroTelefone ) {
        this.numeroCota = numeroCota;	
        this.nomeJornaleiro = nomeJornaleiro;	
        this.tipoPendencia = tipoPendencia;	
        this.dataEntrada = dataEntrada;
        this.valorDiferenca = valorDiferenca;
        this.numeroTelefone = numeroTelefone;        
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public StatusNotaFiscalEntrada getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(StatusNotaFiscalEntrada tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
	}

	public String getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = DateUtil.formatarData(dataEntrada, Constantes.DATE_PATTERN_PT_BR);
	}

	public BigDecimal getValorDiferenca() {
		return valorDiferenca;
	}

	public void setValorDiferenca(BigDecimal valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
		if (valorDiferenca != null) {
			valorDiferencaFormatado = CurrencyUtil.formatarValor(valorDiferenca);
		}
	}
	
	@Export(label = "Valor Diferenca", alignment=Alignment.CENTER, exhibitionOrder = 5)
	public String getValorDiferencaFormatado() {
		return valorDiferencaFormatado;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public Long getIdNotaFiscalEntrada() {
		return idNotaFiscalEntrada;
	}

	public void setIdNotaFiscalEntrada(Long idNotaFiscalEntrada) {
		this.idNotaFiscalEntrada = idNotaFiscalEntrada;
	}

	public Long getNumeroNfe() {
		return numeroNfe;
	}

	public void setNumeroNfe(Long numeroNfe) {
		this.numeroNfe = numeroNfe;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public Long getIdControleConferenciaEncalheCota() {
		return idControleConferenciaEncalheCota;
	}

	public void setIdControleConferenciaEncalheCota(
			Long idControleConferenciaEncalheCota) {
		this.idControleConferenciaEncalheCota = idControleConferenciaEncalheCota;
	}
	
	public void setValorDiferencaFormatado(String valorDiferencaFormatado) {
		this.valorDiferencaFormatado = valorDiferencaFormatado;
	}
	
	public String getDataEncalhe() {
		return dataEncalhe;
	}

	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = DateUtil.formatarData(dataEncalhe, Constantes.DATE_PATTERN_PT_BR);
	}
	
	public BigDecimal getValorNota() {
		return valorNota;		
	}
	
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = CurrencyUtil.arredondarValorParaDuasCasas(valorNota);
	}
	
}