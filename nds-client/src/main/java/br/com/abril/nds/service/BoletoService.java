package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;

//import br.com.abril.nds.model.cadastro.Boleto;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoService {
    
	List<Boleto> obterBoletosPorCota(Integer numeroCota, Date vencimentoDe, Date vencimentoAte, StatusCobranca status);
	
}
