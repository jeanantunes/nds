package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import br.com.abril.nds.dto.CotaConsignadaCouchDTO;
import br.com.abril.nds.dto.CotaCouchDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.ExporteCouch;
import br.com.abril.nds.service.UsuarioService;

@Service
public class ExporteCouchImpl implements ExporteCouch {

	private static Logger LOGGER = LoggerFactory.getLogger(ExporteCouchImpl.class);

	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbClient couchDbClient;

	@Autowired
	private LogInterfaceExecucao logInterfaceExecucao;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private LogExecucaoRepository logExecucaoRepository;

	private static String LANCAMENTO_RECOLHIMENTO_COUCH = "db_carga_tpj_lancamentos_recolhimentos";
	private static String COTAS_CONSIGNADO_COUCH = "db_carga_tpj_cota_consignada";


	public void exportarLancamentoRecolhimento(List<CotaCouchDTO> listaReparte, String nomeEntidadeIntegrada) {
		LOGGER.info("iniciando o processo de exportação dos lançamentos e recolhimentos para o couch ");
		try {

			String data = listaReparte.get(0).getDataMovimento();
			this.couchDbClient = getCouchDBClient(LANCAMENTO_RECOLHIMENTO_COUCH);
			StringBuilder docName = new StringBuilder(nomeEntidadeIntegrada);
			docName.append("_")
					.append(data).append("_")
					.append(listaReparte.get(0).getCodigoDistribuidor()).append("_");

			for (CotaCouchDTO reparte : listaReparte) {
				String keyEntity = docName.toString() + reparte.getCodigoCota();
				try {
					JsonObject jsonDoc = couchDbClient.find(JsonObject.class,keyEntity);
					this.couchDbClient.remove(jsonDoc);
				} catch (NoDocumentException e) {

				}
				reparte.set_id(keyEntity);
				this.couchDbClient.save(reparte);
			}

			String mensagem = String.format("A exportação dos %ss foi efetuada com sucesso", nomeEntidadeIntegrada);
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.SUCESSO, "LCT",
					logExecucaoRepository.findByNome(nomeEntidadeIntegrada.substring(0, 7).toUpperCase()));
			LOGGER.info("finalizado o processo de exportação dos lançamentos e recolhimentos para o couch ");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			String mensagem = String.format("A exportação dos %ss não efetuada com sucesso, %s", nomeEntidadeIntegrada,
					e.getMessage());
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.FALHA, "LCT",
					logExecucaoRepository.findByNome(nomeEntidadeIntegrada.substring(0, 7).toUpperCase()));
		}finally{
            if (couchDbClient != null) {
                couchDbClient.shutdown();
            }
        }
	}

	private CouchDbClient getCouchDBClient(String dbBanco) {
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties().setDbName(dbBanco)
				.setCreateDbIfNotExist(true).setProtocol(couchDbProperties.getProtocol())
				.setHost(couchDbProperties.getHost()).setPort(couchDbProperties.getPort())
				.setUsername(couchDbProperties.getUsername()).setPassword(couchDbProperties.getPassword())
				.setConnectionTimeout(30000);

		return new CouchDbClient(properties);

	}

	@Override
	public void exportarCotaConsignada(CotaConsignadaCouchDTO cotaConsignada) {
		try {
			
			couchDbClient = getCouchDBClient(COTAS_CONSIGNADO_COUCH);
			String docName = "consignado_" + cotaConsignada.getCotaConsignadaDetalhes().get(0).getCodigoCota() + "_"
					+ cotaConsignada.getCotaConsignadaDetalhes().get(0).getCodigoDistribuidor();
			try {
				JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
				this.couchDbClient.remove(jsonDoc);
			} catch (NoDocumentException e) {

			}
			cotaConsignada.set_id(docName);
			this.couchDbClient.save(cotaConsignada);

			String mensagem = "A exportação das cotas consignadas foi realizada com sucesso";
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.SUCESSO, "CONSIGNADO",
					logExecucaoRepository.findByNome("CONSIGN"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			logInterfaceExecucao.salvar("A exportação das cotas consignadas não foi realizada com sucesso" + e.getMessage(), usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.FALHA, "CONSIGNADO",
					logExecucaoRepository.findByNome("CONSIGN"));
		}finally{

            if (couchDbClient != null) {
                couchDbClient.shutdown();
            }
        }

	}

}