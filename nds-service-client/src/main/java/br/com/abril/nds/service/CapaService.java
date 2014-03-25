package br.com.abril.nds.service;

import java.io.InputStream;

import javax.persistence.NoResultException;

import org.lightcouch.Attachment;
import org.lightcouch.CouchDbException;
import org.lightcouch.NoDocumentException;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Interface de acesso ao repositorio de capas
 * 
 * @author Discover Technology
 * 
 */
public interface CapaService {

	/**
	 * Recupera a Capa da edição do produto.
	 * 
	 * @param idProdutoEdicao
	 *            Identificador do {@link ProdutoEdicao}
	 * 
	 * @return {@link InputStream} da capa.
	 * 
	 * @throws NoResultException
	 *             {@link ProdutoEdicao} não encontrado.
	 * @throws CouchDbException
	 *             Erro ao acessar o Repositorio de arquivos.
	  * @throws NoDocumentException Capa não encontrada no Repositorio de arquivos.             
	 */
	public InputStream getCapaInputStream(long idProdutoEdicao);

	/**
	 * Recupera a Capa da edição do produto.
	 * 
	 * @param codigoProduto
	 *            Codigo do produto Prodin
	 * @param numeroEdicao
	 *            Numero da Edição Prodin
	 * 
	 * @return {@link InputStream} da capa.
	 * 
	 * @throws CouchDbException
	 *             Erro ao acessar o Repositorio de arquivos.
	  * @throws NoDocumentException Capa não encontrada no Repositorio de arquivos.             
	 */
	public InputStream getCapaInputStream(String codigoProduto,
			long numeroEdicao);

	/**
	 * Salva a capa para a edição do produto.
	 * 
	 * @param codigoProduto
	 *            Codigo do produto Prodin
	 * @param numeroEdicao
	 *            Numero da Edição Prodin
	 * 
	 * @param attachment
	 * 
	 * @throws CouchDbException
	 *             Erro ao acessar o Repositorio de arquivos.
	  * @throws NoDocumentException Capa não encontrada no Repositorio de arquivos.          
	 */
	public void saveCapa(String codigoProduto, long numeroEdicao,
			Attachment attachment);



	/**
	 * Salva a capa para a edição do produto.
	 * 
	 * @param codigoProduto
	 *            Codigo do produto Prodin
	 * @param numeroEdicao
	 *            Numero da Edição Prodin
	 * @param contentType
	 *            ContentType da capa Ex:<code>"image/png"</code>
	 * @param inputStream
	 *            Stream da capa
	 * 
	 * @throws CouchDbException
	 *             Erro ao acessar o Repositorio de arquivos.
	  * @throws NoDocumentException Capa não encontrada no Repositorio de arquivos.             
	 */
	public void saveCapa(String codigoProduto, long numeroEdicao,
			String contentType, InputStream inputStream);

	/**
	 * Salva a capa para a edição do produto.
	 * 
	 * @param idProdutoEdicao
	 *            Identificador do {@link ProdutoEdicao}
	 * @param contentType
	 *            ContentType da capa Ex:<code>"image/png"</code>
	 * @param inputStream
	 *            Stream da capa
	 * 
	 * @throws NoResultException
	 *             {@link ProdutoEdicao} não encontrado.
	 * @throws CouchDbException
	 *             Erro ao acessar o Repositorio de arquivos.
	  * @throws NoDocumentException Capa não encontrada no Repositorio de arquivos.             
	 */
	public void saveCapa(long idProdutoEdicao, String contentType, InputStream inputStream);

	/**
	 * Excluir a capa associada a Edição.
	 * 
	 * @param idProdutoEdicao
	 */
	public void deleteCapa(long idProdutoEdicao);
	
}
	
