package br.com.abril.nds.integracao.ems2021.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS2021MessageProcessor extends AbstractRepository implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMS2021MessageProcessor.class);

    //Autowired
	//private SessionFactory sessionFactoryIcd;
    
    /*
    private Session session = null;
	
	protected Session getSessionIcd() {
		
		if (session == null) {
			try {
				session = sessionFactoryIcd.getCurrentSession();
			} catch(Exception e) {
				LOGGER.error("Erro ao obter sessão do Hibernate.", e);
			}
			if(session == null) {
				session = sessionFactoryIcd.openSession();
			}
		}
		return session;
	}
	*/
    @Override
    public void preProcess(AtomicReference<Object> tempVar) {

    	/*
	List<Object> objs = new ArrayList<Object>();
	Object dummyObj = new Object();
	objs.add(dummyObj);
	tempVar.set(objs);
	*/
    }

    @Override
    public void processMessage(Message message) {

    /*
	CouchDbClient cdbc = null;
	List<IcdEstrategia> estrategias = obterEstrategias();
	IcdEstrategia estrategiaTemp = null;
	for (IcdEstrategia estrategia : estrategias) {
	    try {
		estrategia.setTipoDocumento("EMS2021");
		if ((estrategiaTemp != null) && (estrategia.getCodigoEstrategia().equals(estrategiaTemp.getCodigoEstrategia()))) {
			estrategia.setBasesEstrategia(estrategiaTemp.getBasesEstrategia());
		} else {
			estrategia.setBasesEstrategia(obterEdicaoBaseEstrategia(estrategia));
		}
		cdbc = this.getCouchDBClient(estrategia.getCodigoDistribuidor().toString());
		cdbc.save(estrategia);
	    } catch (Exception e) {
		LOGGER.error("Erro executando importação de Estratégias ICD.", e);
	    } finally {
		if (cdbc != null) {
		    cdbc.shutdown();
		}
	    }
	    estrategiaTemp = estrategia;
	}
	*/
    }

    /*
    private List<IcdEstrategia> obterEstrategias() {

	StringBuilder sql = new StringBuilder();
	sql.append("SELECT EMD.COD_ESTRATEGIA codigoEstrategia, ");
	sql.append("       TO_CHAR(LPAD(LEP.COD_PUBLICACAO_ADABAS, 8, '0')) AS codigoProduto, ");
	sql.append("       LEP.NUM_EDICAO numeroEdicao, ");
	sql.append("       P.COD_DISTRIBUIDOR codigoDistribuidor, ");
	sql.append("       EMD.TXT_OPORTUNIDADE_VENDA oportunidadeVenda, ");
	sql.append("       EMD.QTD_REPARTE_MINIMO reparteMinimo, ");
	sql.append("       EMD.PCT_ABRANGENCIA_DISTBCAO abrangenciaDistribuicao, ");
	sql.append("       null cesta, ");
	sql.append("       null tipoDocumento ");
	sql.append("  FROM ESTRATEGIA_MICRO_DISTBCAO EMD ");
	sql.append("  JOIN LANCTO_EDICAO_PUBLICACAO LEP ON EMD.COD_LANCTO_EDICAO = LEP.COD_LANCTO_EDICAO ");
	sql.append("  JOIN PUBLICACAO_DINAP PD ON PD.COD_PUBLICACAO = LEP.COD_PUBLICACAO ");
	sql.append("  JOIN ESTRATEGIA_LANCTO_PRACA ELP ON ELP.COD_ESTRATEGIA = EMD.COD_ESTRATEGIA ");
	sql.append("   AND ELP.COD_LANCTO_EDICAO = LEP.COD_LANCTO_EDICAO ");
	sql.append("  JOIN PRACA P ON P.COD_PRACA = ELP.COD_PRACA AND P.IND_PRACA_ATIVA = 'S' ");
	sql.append(" WHERE (EMD.DAT_ALT > (SYSDATE - 5) OR EMD.DAT_INC > (SYSDATE - 5)) ");

	Query query = getSessionIcd().createSQLQuery(sql.toString()).addEntity(IcdEstrategia.class);
	return query.list();
    }
    
    private List<IcdEdicaoBaseEstrategia> obterEdicaoBaseEstrategia(IcdEstrategia estrategia) {

	StringBuilder sql = new StringBuilder();
	sql.append("SELECT TO_CHAR(LPAD(LEP.COD_PUBLICACAO_ADABAS, 8, '0')) AS codigoProduto, ");
	sql.append("       LEP.NUM_EDICAO numeroEdicao, ");
	sql.append("       CBC.COD_ESTRATEGIA estrategia, ");
	sql.append("       null periodo, ");
	sql.append("       null peso ");
	sql.append("  FROM COMPOSICAO_BASE_CALCULO CBC ");
	sql.append("  JOIN LANCTO_EDICAO_PUBLICACAO LEP ON LEP.COD_LANCTO_EDICAO = CBC.COD_LANCTO_EDICAO ");
	sql.append(" WHERE CBC.COD_ESTRATEGIA = :COD_ESTRATEGIA ");

	Query query = getSessionIcd().createSQLQuery(sql.toString()).addEntity(IcdEdicaoBaseEstrategia.class);
	query.setBigDecimal("COD_ESTRATEGIA", estrategia.getCodigoEstrategia());
	return query.list();
    }

*/
    @Override
    public void posProcess(Object tempVar) {
    }
}
