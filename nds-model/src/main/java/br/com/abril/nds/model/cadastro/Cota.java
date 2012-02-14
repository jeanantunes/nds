package br.com.abril.nds.model.cadastro;

import java.util.List;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public class Cota {

	private int cota;
	public Pessoa pessoa;
	public List<PDV> pdvs;
	public SituacaoCadastro situacaoCadastro;

	public Cota(){

	}

}