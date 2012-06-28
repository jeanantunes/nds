package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupPendenciaNFeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3464379590533509374L;
	
	public ConsultaFollowupPendenciaNFeDTO() {}

	public ConsultaFollowupPendenciaNFeDTO( Long numeroCota, String nomeJornaleiro,	
   	    BigDecimal tipoPendencia, Date dataEntrada,
   	    BigDecimal valorDiferenca, String numeroTelefone ) {		
        this.numeroCota = numeroCota;	
        this.nomeJornaleiro = nomeJornaleiro;	
        this.tipoPendencia = tipoPendencia;	
        this.dataEntrada = dataEntrada;
        this.valorDiferenca = valorDiferenca;
        this.numeroTelefone = numeroTelefone;        
	}
   	
	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
    private Long numeroCota;    
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
	@Export(label = "Tipo Pendencia", alignment=Alignment.RIGHT, exhibitionOrder = 3)
   	private BigDecimal tipoPendencia;   	
	
	@Export(label = "Dt. Entrada", alignment=Alignment.CENTER, exhibitionOrder = 4)
   	private Date dataEntrada;   	

	@Export(label = "Valor Diferenca", alignment=Alignment.CENTER, exhibitionOrder = 5)
   	private BigDecimal valorDiferenca;   	

   	@Export(label = "Telefone", alignment=Alignment.RIGHT, exhibitionOrder = 6)
    private String numeroTelefone;

	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public BigDecimal getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(BigDecimal tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public BigDecimal getValorDiferenca() {
		return valorDiferenca;
	}

	public void setValorDiferenca(BigDecimal valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}
	
}
