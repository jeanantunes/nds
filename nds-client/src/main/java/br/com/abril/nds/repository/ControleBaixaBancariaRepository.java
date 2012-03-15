package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.ControleBaixaBancaria}  
 * 
 * @author Discover Technology
 *
 */
public interface ControleBaixaBancariaRepository extends Repository<ControleBaixaBancaria,Long> {

	/**
	 * Obtém um controle de baixa bancária de acordo com o data.
	 * 
	 * @param data - data
	 * 
	 * @return controle de baixa bancária
	 */
	ControleBaixaBancaria obterPorData(Date data);
	
}
