package br.com.abril.nds.integracao.ems2021.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.log.NdsServerLoggerFactory;
import br.com.abril.nds.integracao.model.InterfaceEnum;
import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.icd.IcdEdicaoBaseEstrategia;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;
import br.com.abril.nds.repository.ParametroSistemaRepository;
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

    @Autowired
    private NdsServerLoggerFactory ndsServerLoggerFactory;

    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;

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

        CouchDbClient couchDbClient = null;
        try {

            Long p_codigoDistribuidor = (Long) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue());

            String diretorio    = parametroSistemaRepository.getParametro("INBOUND_DIR");
            String pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");

            List<String> distribuidores = super.getDistribuidores(diretorio, p_codigoDistribuidor);

            for (String distribuidor : distribuidores) {

                if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {

                    LOGGER.info("# Iniciando carga das estratégias para o distribuidor {                //if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {\n} . ", distribuidor);

                    couchDbClient = super.getCouchDBClient(distribuidor, true);
                    List<EMS2021Input> estrategias = icdObjectService.obterEstrategias(Long.valueOf(distribuidor));

                    for (EMS2021Input estrategia : estrategias) {

                        estrategia.setTipoDocumento(InterfaceEnum.EMS2021.name());
                        List<EMS2021InputItem> edicoesBase = icdObjectService.obterEdicaoBaseEstrategia(estrategia.getCodigoPraca(), estrategia.getCodigoLancamentoEdicao());
                        estrategia.setItens(edicoesBase);

                        couchDbClient.save(estrategia);
                        LOGGER.info(":: Estrategia do produto {} edição {} salva! - Total de {} edições base.", estrategia.getCodigoProduto(), estrategia.getNumeroEdicao(), edicoesBase.size());

                    }

                }
            }


        } catch (Exception e) {
            LOGGER.error("Erro executando carga de Estratégias ICD.", e);
            ndsServerLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());

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