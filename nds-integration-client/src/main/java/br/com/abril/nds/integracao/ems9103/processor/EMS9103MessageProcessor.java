package br.com.abril.nds.integracao.ems9103.processor;

import br.com.abril.nds.dto.LancamentoCapaCouchDTO;
import br.com.abril.nds.dto.LancamentoCapaDetalheCouchDTO;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.ExporteCouch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class EMS9103MessageProcessor implements MessageProcessor {

    @Autowired
    private DistribuidorRepository distribuidorRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private ExporteCouch exporteCouch;

    @Override
    public void preProcess(AtomicReference<Object> tempVar) {

    }

    @Override
    public void processMessage(Message message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String dateInString = "8-ago-2016";

        Distribuidor distribuidor=distribuidorRepository.obter();
        try {
            LancamentoCapaCouchDTO lancamentoCapaCouchDTO = new LancamentoCapaCouchDTO();
            lancamentoCapaCouchDTO.setLancamentoCapaDetalheCouchDTO(lancamentoRepository.getLancamentoCapaCouch(formatter.parse(dateInString), distribuidor.getCodigoDistribuidorDinap()));
            exporteCouch.exportarLancamentoCapa(lancamentoCapaCouchDTO);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void posProcess(Object tempVar) {

    }


}
