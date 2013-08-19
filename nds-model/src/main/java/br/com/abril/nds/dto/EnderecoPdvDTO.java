package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.AssociacaoEndereco;

public class EnderecoPdvDTO implements Serializable {
	
	private List<AssociacaoEndereco> assosiacaoEndereco;

	/**
	 * @return the assosiacaoEndereco
	 */
	public List<AssociacaoEndereco> getAssosiacaoEndereco() {
		return assosiacaoEndereco;
	}

	/**
	 * @param assosiacaoEndereco the assosiacaoEndereco to set
	 */
	public void setAssosiacaoEndereco(List<AssociacaoEndereco> assosiacaoEndereco) {
		this.assosiacaoEndereco = assosiacaoEndereco;
	}
	
	
}
