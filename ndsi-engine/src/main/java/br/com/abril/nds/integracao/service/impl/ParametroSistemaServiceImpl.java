package br.com.abril.nds.integracao.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Service
public class ParametroSistemaServiceImpl implements ParametroSistemaService {

	private static final String ATTACHMENT_LOGOTIPO = "imagem_logotipo";
	private static final String DB_NAME = "db_parametro_sistema";
	
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbClient couchDbClient;	
	
	@PostConstruct
	public void initCouchDbClient() {
		this.couchDbClient = new CouchDbClient(DB_NAME, true,
				couchDbProperties.getProtocol(), 
				couchDbProperties.getHost(),
				couchDbProperties.getPort(), 
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword());
	}
	
	
	@Transactional
	public ParametroSistema buscarParametroPorTipoParametro(TipoParametroSistema tipoParametroSistema) {
		return parametroSistemaRepository.buscarParametroPorTipoParametro(tipoParametroSistema);
	}

	@Override
	@Transactional
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = parametroSistemaRepository.buscarParametroSistemaGeral();
		
		ParametroSistemaGeralDTO dto = new ParametroSistemaGeralDTO();
		dto.setParametrosSistema(lst);
		
		return dto;
	}
	
	@Override
	public InputStream getLogotipoDistribuidor() {
		InputStream inputStream;
		try {
			
			//TODO alterar o modo de obter o LOGOTIPO_DISTRIBUIDOR, não é mais dominio do Parametro do Sistema
			inputStream = couchDbClient.find(
					TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name()
					+ "/" + ATTACHMENT_LOGOTIPO);
		} catch (NoDocumentException e) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		return inputStream;
	}
	
	@Override
	@Transactional
	public void salvar(ParametroSistemaGeralDTO dto, InputStream imgLogotipo, String imgContentType) {
		
		List<ParametroSistema> lst = dto.getParametrosSistema();
		parametroSistemaRepository.salvar(lst);
	}
	
	@Override
	@Transactional
	public void salvar(Collection<ParametroSistema> parametrosSistema) {
		parametroSistemaRepository.salvar(parametrosSistema);
	}
	
}
