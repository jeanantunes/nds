package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.financeiro.BoletoEmail;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.BoletoEmail}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoEmailRepository extends Repository<BoletoEmail,Long>{

	/**
	 * Obtem controle de envio de boletos por email por cobranca
	 * @param cobrancaId
	 * @return BoletoEmail
	 */
	BoletoEmail obterBoletoEmailPorCobranca(Long cobrancaId);

    List<BoletoEmail> buscarTodosOrdenados();
}