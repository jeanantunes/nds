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
   	
   	public ConsultaFollowupPendenciaNFeDTO() {}

	public ConsultaFollowupPendenciaNFeDTO( Integer numeroCota, String nomeJornaleiro,	
			StatusNotaFiscalEntrada tipoPendencia, String dataEntrada,
   	    BigDecimal valorDiferenca, String numeroTelefone ) {
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
		this.dataEntrada = DateUtil.formatarData(dataEntrada, Constantes.DATE_PATTERN_PT_BR) ;;
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
	
}
