package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoRepository extends Repository<Boleto,Long> {

	/**
	 * Obtém uma lista de Boletos pelo número da Cota.
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return {@link List<Boleto>}
	 */
	List<Boleto> obterBoletosPorCota(Integer numeroCota, Date vencimentoDe, Date vencimentoAte,StatusCobranca status);
	
}
