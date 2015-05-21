package br.com.abril.nds.integracao.ems3100.processor;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.ExtratificacaoItem;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Component
public class EMS3100MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS3100MessageProcessor.class);
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	private String diretorio;
	
	private String pastaInterna;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);
		
		tempVar.set(objs);
		
	}

	@Override
	public void processMessage(Message message) {
				
		this.diretorio = "/opt/interface_server/prodin/";//parametroSistemaRepository.getParametro("INBOUND_DIR");
		// this.diretorio = "/opt/interface_server/prodin/";
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, null);
		
		CouchDbClient couchDbExtratificador = this.getCouchDBClient("db_extratificador", false);
		for(String distribuidor : distribuidores) {
		
			if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {

				CouchDbClient couchDbClient = this.getCouchDBClient(StringUtils.leftPad(distribuidor, 8, "0"), true);

				View view = couchDbClient.view("importacao/porTipoDocumento");
								
				view.startKey(new Object[] {InterfaceEnum.EMS3100.name(), null});
				view.endKey(InterfaceEnum.EMS3100.name(), "");
				
				view.includeDocs(true);

				try { 
					ViewResult<Object[], Void, ?> result = view.queryView(Object[].class, Void.class, ExtratificacaoItem.class);
					for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {						
						
						ExtratificacaoItem doc = (ExtratificacaoItem) row.getDoc();
						ExtratificacaoItem docServer = new ExtratificacaoItem(); 
						BeanUtils.copyProperties(doc, docServer, "_rev", "_id");
						
						couchDbExtratificador.save(docServer);
						couchDbClient.remove(doc);
					}
					
                } catch (NoDocumentException e) {
                    LOGGER.error(e.getMessage(), e);
				} catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw e;
				}
				
			}
		}
	}
	
	@Override
	public void posProcess(Object tempVar) {
	}
		
	/**
	 * Recupera distribuidores a serem processados.
	 */
	private List<String> getDistribuidores(String diretorio, Long codigoDistribuidor) {
		
		List<String> distribuidores = new ArrayList<String>();
		
		if (codigoDistribuidor == null) {
			
			FilenameFilter numericFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					 return name.matches("\\d+");  
				}
			};
			
			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list( numericFilter )));
			
		} else {
			
			distribuidores.add(codigoDistribuidor.toString());
		}
		
		return distribuidores;
	}
	
}