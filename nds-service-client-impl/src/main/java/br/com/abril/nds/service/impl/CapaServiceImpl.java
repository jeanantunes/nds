/**
 * 
 */
package br.com.abril.nds.service.impl;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.Attachment;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.Capa;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.util.export.FileExporter.FileType;

import com.google.gson.JsonObject;

/**
 * @see CapaService
 * @author Diego Fernandes
 * 
 */
@Service
public class CapaServiceImpl implements CapaService {	
	private static final String DB_NAME  =  "capas";

	private static final Logger LOGGER = LoggerFactory.getLogger(CapaService.class);
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbClient couchDbClient;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@PostConstruct
	public void initCouchDbClient() {
		//this.couchDbClient = new CouchDbClient(DB_NAME,true, couchDbProperties.getProtocol(), couchDbProperties.getHost(), couchDbProperties.getPort(), couchDbProperties.getUsername(), couchDbProperties.getPassword());
		LOGGER.error("INICIANDO COUCH PARA ACESSAR CAPA COM TIMEOUT DE 30000 ms");
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties()
			.setDbName(DB_NAME)
			.setCreateDbIfNotExist(true)
			.setProtocol(couchDbProperties.getProtocol())
			.setHost(couchDbProperties.getHost())
			.setPort(couchDbProperties.getPort())
			.setUsername(couchDbProperties.getUsername())
			.setPassword(couchDbProperties.getPassword())
			.setMaxConnections(30000)
			.setConnectionTimeout(30000); // timeout de 30 segundos
	
		this.couchDbClient = new CouchDbClient(properties);

	}
	
	

	@Override
	@Transactional(readOnly=true)
	public InputStream getCapaInputStream(long idProdutoEdicao) {
		
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		
		return getCapaInputStream(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao());
	}


	@Override
	@Transactional(readOnly=true)
	public void saveCapa(long idProdutoEdicao, String contentType, InputStream inputStream) {
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		
		try{
			
			String docName = getDocName(produtoEdicao.getProduto().getCodigo(), produtoEdicao.getNumeroEdicao());
			
			JsonObject json = couchDbClient.find(JsonObject.class, docName);	
		   
			this.couchDbClient.remove(json);
		}
		catch (NoDocumentException e){
			
		}
		
		saveCapa(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao(), contentType, inputStream);

	}

	@Override
	@Transactional
	public InputStream getCapaInputStream(String codigoProduto,long numeroEdicao) {
		String docName = getDocName(codigoProduto, numeroEdicao);
		String fileName = getCapaFileName(docName);
		return couchDbClient.find(docName+"/"+fileName);
	}
	
	private String getCapaFileName(String docName) {
		Capa capa = findCapa(docName);
		return getCapaFileName(capa);
	}
	
	private String getCapaFileName(Capa capa) {
		return capa.getAttachments().keySet().iterator().next();
	}
	
	private Capa findCapa(String docName) {
		Capa capa = couchDbClient.find(Capa.class, docName);
		
		if(capa.getAttachments() == null || capa.getAttachments().isEmpty()){
			throw new NoDocumentException("Capa: "+ docName +  " - imagem inexistente.");
		}
		return capa;
	}
	
	@Override
	@Transactional
	public void saveCapa(String codigoProduto, long numeroEdicao, Attachment attachment) {
		String docName = getDocName(codigoProduto, numeroEdicao);
		
		Capa capa = new Capa();
		
		capa.setId(docName);
		capa.getAttachments().put(docName + FileType.JPG.getExtension(), attachment);
		couchDbClient.save(capa);

	}

	@Override
	@Transactional
	public void saveCapa(String codigoProduto, long numeroEdicao, String contentType, InputStream inputStream) {		
		String docName = getDocName(codigoProduto, numeroEdicao);
		
		couchDbClient.saveAttachment(inputStream, docName + FileType.JPG.getExtension(), contentType, getDocName(codigoProduto, numeroEdicao),null);
	}
	
	
	/**
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @return
	 */
	private String getDocName(String codigoProduto, long numeroEdicao) {		
		return StringUtils.leftPad(codigoProduto, 8,'0') +  StringUtils.leftPad(""+numeroEdicao, 4,'0') ;
	}

	/**
	 * @param idProdutoEdicao
	 * @return
	 * @throws NoResultException
	 */
	private ProdutoEdicao getProdutoEdicao(long idProdutoEdicao)
			throws NoResultException {
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new NoResultException("Produto Edicao: " + idProdutoEdicao
					+ " não encontrado.");
		}
		return produtoEdicao;
	}
	
	@Override
	@Transactional
	public void deleteCapa(long idProdutoEdicao) {
		
		ProdutoEdicao pe = getProdutoEdicao(idProdutoEdicao);
		String docName = getDocName(pe.getProduto().getCodigo(), pe.getNumeroEdicao());
		
		try{
			
		    JsonObject json = couchDbClient.find(JsonObject.class, docName);	
		    this.couchDbClient.remove(json);
		}
		catch (NoDocumentException e){
			
			throw new NoDocumentException("Capa ainda não definida.");
		}
	}

}
