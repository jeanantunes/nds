package br.com.abril.nds.repository.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.vo.EnderecoVO;

public class AbstractRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	@Autowired
	public EnderecoService enderecoService;

	public AbstractRepository() {
		
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @return client
	 */
	protected CouchDbClient getCouchDBClient(String codDistribuidor) {
		
		return new CouchDbClient(
				"db_" + StringUtils.leftPad(codDistribuidor, 8, "0"),
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}

	protected Endereco getEnderecoSaneado(String cep) {
		EnderecoVO endSaneado = enderecoService.obterEnderecoPorCep(cep);
		Endereco endereco = null;
		if (null != endSaneado) {
			endereco = new Endereco();
			endereco.setTipoLogradouro(endSaneado.getTipoLogradouro());
			endereco.setLogradouro(endSaneado.getLogradouro());
			endereco.setCidade(endSaneado.getLocalidade());
			endereco.setUf(endSaneado.getUf());
			endereco.setCep(endSaneado.getCep());
			endereco.setBairro(endSaneado.getBairro());				
		}
		return endereco;
	}
}