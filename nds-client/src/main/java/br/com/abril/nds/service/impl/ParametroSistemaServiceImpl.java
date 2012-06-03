package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.ParametroSistemaService;

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

	/**
	 * Busca os parâmetros gerais do sistema.<br>
	 * Atualmente são considerados como parãmetros gerais do sistema:
	 * <ul>
	 * <li>Logo;</li>
	 * <li>CNPJ;</li>
	 * <li>Razão Social;</li>
	 * <li>E-mail;</li>
	 * <li>UF;</li>
	 * <li>Código Distribuidor Dinap;</li>
	 * <li>Código Distribuidor FC;</li>
	 * <li>Login;</li>
	 * <li>Senha;</li>
	 * <li>Versão Sistema;</li>
	 * <li>Inteface CE Exportação;</li>
	 * <li>Inteface PRODIN Importação;</li>
	 * <li>Inteface PRODIN Exportação;</li>
	 * <li>Inteface MDC Importação;</li>
	 * <li>Inteface MDC Exportação;</li>
	 * <li>Inteface Bancas Exportação;</li>
	 * <li>Inteface GFS Importação;</li>
	 * <li>Inteface GFS Exportação;</li>
	 * <li>Inteface NF-e Importação;</li>
	 * <li>Inteface NF-e Exportação;</li>
	 * <li>NF-e em DPEC;</li>
	 * <li>Data de Operação Corrente;</li>
	 * </ul>
	 * 
	 * @return Lista dos parâmetros do sistema que são considerados gerais. 
	 */
	@Transactional
	public ParametroSistemaGeralDTO buscarParametroSistemaGeral() {
		
		List<ParametroSistema> lst = parametroSistemaRepository.buscarParametroSistemaGeral();
		
		ParametroSistemaGeralDTO dto = new ParametroSistemaGeralDTO();
		dto.setParametrosSistema(lst);
		
		return dto;
	}
	
	/**
	 * Retorna o logotipo do distribuidor, caso exista.
	 * 
	 * @return
	 */
	public InputStream getLogotipoDistribuidor() {
		InputStream inputStream;
		try {
			inputStream = couchDbClient.find(
					TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name()
					+ "/" + ATTACHMENT_LOGOTIPO);
		} catch (NoDocumentException e) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		return inputStream;
	}
	
	/**
	 * Salva os Parâmetros do Sistema.
	 *  
	 * @param dto
	 * @param imgLogotipo
	 * @param imgContentType
	 */
	@Transactional
	public void salvar(ParametroSistemaGeralDTO dto, InputStream imgLogotipo, String imgContentType) {
		
		// Salvar logotipo:
		if (imgLogotipo != null) {
			couchDbClient.saveAttachment(imgLogotipo, ATTACHMENT_LOGOTIPO, imgContentType, TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name(), null);
		}
		
		// Salvar dto:
		List<ParametroSistema> lst = dto.getParametrosSistema();
		parametroSistemaRepository.salvar(lst);
	}
}
