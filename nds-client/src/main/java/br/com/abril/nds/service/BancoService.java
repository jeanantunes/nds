package br.com.abril.nds.service;
import java.util.List;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}
 * 
 * @author Discover Technology
 */
public interface BancoService {

	List<Banco> obterBancos(FiltroConsultaBancosDTO filtro);

	long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro);
	
}
