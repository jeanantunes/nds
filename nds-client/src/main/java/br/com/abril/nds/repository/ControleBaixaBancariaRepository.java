package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
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
	 * Obtém um controle de baixa bancária de acordo com os parâmetros informados.
	 * 
	 * @param data - data
	 * @param banco - banco
	 * 
	 * @return controle de baixa bancária
	 */
	ControleBaixaBancaria obterControleBaixaBancaria(Date data, Banco banco);
	
	/**
	 * Obtém uma lista de controle de baixa bancária de acordo com os parâmetros informados.
	 * 
	 * @param data - data
	 * @param status - status de controle da baixa bancária
	 * 
	 * @return lista de controle de baixa bancária
	 */
	List<ControleBaixaBancaria> obterListaControleBaixaBancaria(Date data,
																StatusControle status);
	
}
