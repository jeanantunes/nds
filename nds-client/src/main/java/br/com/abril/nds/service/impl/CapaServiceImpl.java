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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.Capa;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CapaService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @see CapaService
 * @author Diego Fernandes
 * 
 */
@Service
public class CapaServiceImpl implements CapaService {	
	private static final String DEFAULT_EXTENSION = ".jpg";

	private static final String DB_NAME  =  "db_integracao";

	@Autowired
	private CouchDbProperties couchDbProperties;


	private CouchDbClient couchDbClient;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@PostConstruct
	public void initCouchDbClient() {
		this.couchDbClient = new CouchDbClient(DB_NAME,true, couchDbProperties.getProtocol(), couchDbProperties.getHost(), couchDbProperties.getPort(), couchDbProperties.getUsername(), couchDbProperties.getPassword());
	}

	@Override
	@Transactional(readOnly=true)
	public boolean hasCapa(long idProdutoEdicao) {
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		return hasCapa(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao());
	}
	
	

	@Override
	@Transactional(readOnly=true)
	public Attachment getCapa(long idProdutoEdicao) {
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		return getCapa(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao());
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
	public void saveCapa(long idProdutoEdicao, Attachment capa) {
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		saveCapa(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao(), capa);

	}

	@Override
	@Transactional(readOnly=true)
	public void saveCapa(long idProdutoEdicao, String contentType, InputStream inputStream) {
		ProdutoEdicao produtoEdicao = getProdutoEdicao(idProdutoEdicao);
		saveCapa(produtoEdicao.getProduto().getCodigo(),
				produtoEdicao.getNumeroEdicao(), contentType, inputStream);

	}

	@Override
	public boolean hasCapa(String codigoProduto, long numeroEdicao) {		
		return couchDbClient.contains(toId(codigoProduto, numeroEdicao));
	}

	

	@Override
	public Attachment getCapa(String codigoProduto, long numeroEdicao) {
		String id = toId(codigoProduto, numeroEdicao);
		Capa capa = couchDbClient.find(Capa.class,id);
		
		if(!capa.getAttachments().containsKey(id + DEFAULT_EXTENSION)){
			throw new NoDocumentException("Capa: "+ id +  " - imagem inexistente.");
		}
		return capa.getAttachments().get(id + DEFAULT_EXTENSION);
	}

	@Override
	public InputStream getCapaInputStream(String codigoProduto,
			long numeroEdicao) {
		String id = toId(codigoProduto, numeroEdicao);
		return couchDbClient.find(id + "/" + id+ DEFAULT_EXTENSION);
	}

	@Override
	public void saveCapa(String codigoProduto, long numeroEdicao,
			Attachment attachment) {
		String id = toId(codigoProduto, numeroEdicao);
		
		Capa capa = new Capa();
		
		capa.setId(id);
		capa.getAttachments().put(id + DEFAULT_EXTENSION, attachment);
		couchDbClient.save(capa);

	}

	@Override
	public void saveCapa(String codigoProduto, long numeroEdicao,
			String contentType, InputStream inputStream) {		
		String id = toId(codigoProduto, numeroEdicao);
		
		couchDbClient.saveAttachment(inputStream, id + DEFAULT_EXTENSION, contentType, toId(codigoProduto, numeroEdicao),null);
	}
	
	
	/**
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @return
	 */
	private String toId(String codigoProduto, long numeroEdicao) {		
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
		String id = toId(pe.getProduto().getCodigo(), pe.getNumeroEdicao());
		
		// Obter o atributo 'rev':
		JsonObject json = couchDbClient.find(JsonObject.class, id);	
		this.couchDbClient.remove(json);
	}

}
