package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Moeda;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}
 * 
 * @author Discover Technology
 */
public interface BancoService {

	List<Banco> obterBancos(FiltroConsultaBancosDTO filtro);

	long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro);
	
	Banco obterbancoPorNumero(String numero);
	
	Banco obterbancoPorNome(String nome);
	
	Banco obterBancoPorId(long idBanco);

	BancoVO obterDadosBanco(long idBanco);
	
	void incluirBanco(Banco banco);
	
	void alterarBanco(Banco banco);
	
	boolean verificarPendencias(long idBanco);
	
	void dasativarBanco(long idBanco);
	
	List<ItemDTO<Integer, String>> getComboCarteiras();
	
	List<ItemDTO<Moeda, String>> getComboMoedas();
	
	Carteira obterCarteiraPorCodigo(Integer codigoCarteira);
}
