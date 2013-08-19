package br.com.abril.nds.integracao.ems0128.processor;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.DetalheFaltaSobra;
import br.com.abril.nds.model.integracao.icd.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.SolicitacaoFaltaSobra;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Component
public class EMS0128MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0128MessageProcessor.class);
	
	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private IcdObjectService icdObjectService;
	
	private String diretorio;
	
	private String pastaInterna;

	protected Session getSessionIcd() {
		
		Session session = null;
		try {
			session = sessionFactoryIcd.getCurrentSession();
		} catch(Exception e) {
			LOGGER.error("Erro ao obter sessão do Hibernate.", e);
		}
		
		if(session == null) {
			session = sessionFactoryIcd.openSession();
		}
		
		return session;
		
	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);
		
		tempVar.set(objs);
		
	}

	@Override
	public void processMessage(Message message) {
		
		this.diretorio = parametroSistemaRepository.getParametro("INBOUND_DIR");
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, null);
		
		for(String distribuidor : distribuidores) {
		
			if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {

				CouchDbClient couchDbClient = this.getCouchDBClient("db_" + StringUtils.leftPad(distribuidor, 8, "0"));
										
				View view = couchDbClient.view("importacao/porTipoDocumento");
								
				view.key("EMS0128");
				view.includeDocs(true);
				try {
					ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, EMS0128Input.class);
					for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {						
						
						EMS0128Input doc = (EMS0128Input) row.getDoc();
						
						if (doc.getSituacaoSolicitacao().equals("SOLICITADO")) {
							icdObjectService.insereSolicitacao(doc);
							doc.setSituacaoSolicitacao("AGUARDANDO_GFS");
						} else if (
								doc.getSituacaoSolicitacao().equals("AGUARDANDO_GFS") 
								|| doc.getSituacaoSolicitacao().equals("EM PROCESSAMENTO")) {
							
							SolicitacaoFaltaSobra solicitacao = icdObjectService.recuperaSolicitacao(Long.valueOf(distribuidor), doc);
							
							doc.setSituacaoSolicitacao(solicitacao.getCodigoSituacao());
							
							List<DetalheFaltaSobra> listaDetalhes = solicitacao.getItens();
							
							for (DetalheFaltaSobra item : listaDetalhes)
							{
								for ( EMS0128InputItem eitem : doc.getItems()) {
									if (item.getDfsPK().getNumeroSequencia().equals(eitem.getNumSequenciaDetalhe())) {
										eitem.setSituacaoAcerto(item.getCodigoAcerto());
										eitem.setNumeroDocumentoAcerto(item.getNumeroDocumentoAcerto());
										eitem.setDataEmicaoDocumentoAcerto(item.getDataEmissaoDocumentoAcerto());
										
										MotivoSituacaoFaltaSobra motivo = icdObjectService.recuperaMotivoPorDetalhe(item.getDfsPK());
										
										if (null!=motivo) {
											eitem.setDescricaoMotivo(motivo.getDescricaoMotivo());
											eitem.setCodigoOrigemMotivo(motivo.getCodigoMotivo());
										}
									}
								}
							}							
						}
						couchDbClient.update(doc);
						
					}
					
				} catch (NoDocumentException ex ) {
						
				}			
			}
			
		}
		
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
	/* =============================================
	 
	============================================= */
		
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