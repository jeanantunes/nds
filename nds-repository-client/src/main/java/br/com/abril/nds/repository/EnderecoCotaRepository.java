package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.vo.EnderecoVO;

public interface EnderecoCotaRepository extends Repository<EnderecoCota, Long> {

	void removerEnderecosCota(Long idCota, List<Endereco> listaEndereco);
	
	/**
	 * Recupera o endereço principal da cota
	 * @param idCota
	 * @return <code>null</code> caço não encontre resgistros;
	 */
	public abstract EnderecoCota getPrincipal(Long idCota);

	EnderecoCota obterEnderecoPorTipoEndereco(Long idCota,
			TipoEndereco tipoEndereco);

	Long obterQtdEnderecoAssociadoCota(Long idCota);
	
	List<ItemDTO<String, String>> buscarMunicipio();

	List<Endereco> enderecosIncompletos();
}