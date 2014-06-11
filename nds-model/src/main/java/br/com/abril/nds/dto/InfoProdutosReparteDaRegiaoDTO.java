package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.dto.filtro.FiltroDTO;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: Reparte da região.
 * 
 */

public class InfoProdutosReparteDaRegiaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 3767423621264413776L;
	
	private BigInteger reparteDistribuido;
	private BigInteger repartePromocional;

	
	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}
	public void setReparteDistribuido(BigInteger reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}
	public BigInteger getRepartePromocional() {
		return repartePromocional;
	}
	public void setRepartePromocional(BigInteger repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
	}
