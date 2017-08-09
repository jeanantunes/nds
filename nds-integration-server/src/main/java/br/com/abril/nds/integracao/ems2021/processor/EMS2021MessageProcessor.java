package br.com.abril.nds.integracao.ems2021.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.model.InterfaceEnum;
import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.icd.IcdEdicaoBaseEstrategia;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;
import org.hibernate.Session;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS2021MessageProcessor extends AbstractRepository implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMS2021MessageProcessor.class);

    @Autowired
    private IcdObjectService icdObjectService;

    private Session session = null;

    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        LOGGER.info("EMS2021 - preProcess " + tempVar);
        List<Object> objs = new ArrayList<Object>();
        Object dummyObj = new Object();
        objs.add(dummyObj);
        tempVar.set(objs);
    }

    @Override
    public void processMessage(Message message) {

        message.getHeader().get(MessageHeaderProperties.URI.getValue());
        message.getHeader().get(MessageHeaderProperties.USER_NAME.getValue());
        Long codigoDistribuidor = (Long) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue());

        // TODO - Adequar o fluxo para caso seja necessario executar a carga para todos os distribuidores

        LOGGER.info("# Iniciando carga das estratégias para distribuidor {} . ", codigoDistribuidor);
        CouchDbClient couchDbClient = null;
        try {
            couchDbClient = this.getCouchDBClient(String.valueOf(codigoDistribuidor), true);
            List<EMS2021Input> estrategias = icdObjectService.obterEstrategias(codigoDistribuidor);

            for (EMS2021Input estrategia : estrategias) {

                estrategia.setTipoDocumento(InterfaceEnum.EMS2021.name());
                List<EMS2021InputItem> edicoesBase  = icdObjectService.obterEdicaoBaseEstrategia(estrategia.getCodigoPraca(), estrategia.getCodigoLancamentoEdicao());
                estrategia.setItens(edicoesBase);

                couchDbClient.save(estrategia);
                LOGGER.info(":: Estrategia do produto {}  edição {} salva! - Total de {}  edições base", estrategia.getCodigoProduto(), estrategia.getNumeroEdicao(), edicoesBase.size());

            }

        } catch (Exception e) {
            LOGGER.error("Erro executando importação de Estratégias ICD.", e);
        } finally {
            if (couchDbClient != null) {
                couchDbClient.shutdown();
            }
        }
    }

    @Override
    public void posProcess(Object tempVar) {
        LOGGER.info("EMS2021 - posProcess " + tempVar);
    }
}
