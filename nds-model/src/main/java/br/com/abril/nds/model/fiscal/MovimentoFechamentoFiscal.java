package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL")
public class MovimentoFechamentoFiscal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "NOTA_FISCAL_EMITIDA")
	private boolean notaFiscalEmitida;
	
	@OneToMany
	private List<OrigemItemMovFechamentoFiscal> origemMovimentoFechamentoFiscal;
	
}