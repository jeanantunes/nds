package br.com.abril.nds.service.impl;

import org.springframework.stereotype.Service;

import br.com.abril.nds.client.endereco.vo.BairroVO;
import br.com.abril.nds.client.endereco.vo.EnderecoVO;
import br.com.abril.nds.client.endereco.vo.LocalidadeVO;
import br.com.abril.nds.client.endereco.vo.LogradouroVO;
import br.com.abril.nds.client.endereco.vo.PaisVO;
import br.com.abril.nds.client.endereco.vo.TipoLogradouroVO;
import br.com.abril.nds.client.endereco.vo.UnidadeFederacaoVO;
import br.com.abril.nds.service.ConsultaBaseEnderecoService;

/**
 * Serviço de consulta a base de endereços
 *  
 *  //TODO: Implementação dummy para testes para tela de 
 *  cadastro de endereços
 *
 */
@Service
public class ConsultaBaseEnderecoServiceImpl implements ConsultaBaseEnderecoService {

	private static final PaisVO BRASIL = new PaisVO("BR", "Brasil");
    private static final UnidadeFederacaoVO UF_SP = new UnidadeFederacaoVO("SP", "São Paulo");
	
	@Override
	public EnderecoVO buscarPorCep(String cep) {
		if ("13720000".equals(cep)) {
			return montarEnderecoSJRioPardo();
		} else if ("13730110".equals(cep)) {
			return montarEnderecoAntonioCristovamMococa();
		}
		return null;
	}
	
	private EnderecoVO montarEnderecoSJRioPardo() {
		EnderecoVO endereco = new EnderecoVO();
		LocalidadeVO localidade = new LocalidadeVO("São José do Rio Pardo",
				"S.J.Rio Pardo", "13720000");
		endereco.setLocalidade(localidade);
		endereco.setPais(BRASIL);
		endereco.setUnidadeFederecao(UF_SP);
		return endereco;
	}
	
	private EnderecoVO montarEnderecoAntonioCristovamMococa() {
		EnderecoVO endereco = new EnderecoVO();
		LocalidadeVO localidade = new LocalidadeVO("Mococa", "Mococa", null);
		endereco.setLocalidade(localidade);
		endereco.setTipoLogradouro(new TipoLogradouroVO("Rua", "R."));
		endereco.setLogradouro(new LogradouroVO("Antonio Cristovam",
				"Ant.Cristovam", "13730110"));
		endereco.setBairro(new BairroVO("Centro", "Centro"));
		endereco.setPais(BRASIL);
		endereco.setUnidadeFederecao(UF_SP);
		return endereco;
	}

}
