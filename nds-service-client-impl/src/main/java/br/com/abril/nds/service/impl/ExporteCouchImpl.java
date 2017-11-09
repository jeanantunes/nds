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
	private static Logger LOGGER = LoggerFactory.getLogger(ExporteCouchImpl.class);

	public void exportarLancamentoRecolhimento(List<CotaCouchDTO> listaReparte, String nomeEntidadeIntegrada) {
		try {
			String data = listaReparte.get(0).getDataMovimento();
			this.couchDbClient = getCouchDBClient(LANCAMENTO_RECOLHIMENTO_COUCH);
			String docName = nomeEntidadeIntegrada + "_" + data;
			try {
				JsonObject jsonDoc = couchDbClient.find(JsonObject.class,docName + "_" + listaReparte.get(0).getCodigoCota());
				this.couchDbClient.remove(jsonDoc);
			} catch (NoDocumentException e) {

			}
			for (CotaCouchDTO reparte : listaReparte) {
				reparte.set_id(docName + "_" + reparte.getCodigoCota());
				this.couchDbClient.save(reparte);
			}

			if (couchDbClient != null) {
				couchDbClient.shutdown();
			}
			String mensagem = String.format("A exportação dos %ss foi efetuada com sucesso", nomeEntidadeIntegrada);
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.SUCESSO, "LCT",
					logExecucaoRepository.findByNome(nomeEntidadeIntegrada.substring(0, 7).toUpperCase()));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			String mensagem = String.format("A exportação dos %ss não efetuada com sucesso, %s", nomeEntidadeIntegrada,
					e.getMessage());
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.FALHA, "LCT",
					logExecucaoRepository.findByNome(nomeEntidadeIntegrada.substring(0, 7).toUpperCase()));
		}
	}

	private CouchDbClient getCouchDBClient(String dbBanco) {
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties().setDbName(dbBanco)
				.setCreateDbIfNotExist(true).setProtocol(couchDbProperties.getProtocol())
				.setHost(couchDbProperties.getHost()).setPort(couchDbProperties.getPort())
				.setUsername(couchDbProperties.getUsername()).setPassword(couchDbProperties.getPassword())
				.setMaxConnections(100).setConnectionTimeout(500);

		return new CouchDbClient(properties);

	}

	@Override
	public void exportarCotaConsignada(CotaConsignadaCouchDTO cotaConsignada) {
		try {
			
			couchDbClient = getCouchDBClient(COTAS_CONSIGNADO_COUCH);
			String docName = "consignado_" + cotaConsignada.getCotaConsignadaDetalhes().get(0).getNumeroCota() + "_"
					+ cotaConsignada.getCotaConsignadaDetalhes().get(0).getDistribuidor();
			try {
				JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
				this.couchDbClient.remove(jsonDoc);
			} catch (NoDocumentException e) {

			}
			cotaConsignada.set_id(docName);
			this.couchDbClient.save(cotaConsignada);

			if (couchDbClient != null) {
				couchDbClient.shutdown();
			}
			String mensagem = "A exportação das cotas consignadas foi realizada com sucesso";
			logInterfaceExecucao.salvar(mensagem, usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.SUCESSO, "CONSIGNADO",
					logExecucaoRepository.findByNome("CONSIGN"));
		} catch (Exception e) {
			logInterfaceExecucao.salvar("A exportação das cotas consignadas não foi realizada com sucesso" + e.getMessage(), usuarioService.getUsuarioLogado(), new Date(),
					StatusExecucaoEnum.FALHA, null,
					logExecucaoRepository.findByNome("CONSIGN"));
		}

	}

}
