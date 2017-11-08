package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EventoExecucaoRepository;
import br.com.abril.nds.repository.LogExecucaoMensagemRepository;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class LogInterfaceExecucao {

	private static final String EVENTO_INTERFACE_MICRO_DISTRIBUICAO = "Arquivo";

	@Autowired
	private LogExecucaoRepository logExecucaoRepository;
	
	@Autowired
	private LogExecucaoMensagemRepository logExecucaoMensagemRepository;

	@Autowired
	private EventoExecucaoRepository eventoExecucaoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void salvar(List<String> mensagens, Usuario usuario, Date dataInicio, StatusExecucaoEnum status,
			String nomeArquivo, InterfaceExecucao interfaceExecucao) {
		
		LogExecucao logExecucao = this.inserirLogExecucao(usuario, interfaceExecucao, dataInicio, status);

		EventoExecucao eventoExecucao = eventoExecucaoRepository.findByNome(EVENTO_INTERFACE_MICRO_DISTRIBUICAO);
		
		List<LogExecucaoMensagem> logsMsg = new ArrayList<>();
		for (String msg : mensagens) {
			LogExecucaoMensagem logMsg = this.gerarLogExecucaoMensagem(nomeArquivo, msg, logExecucao, eventoExecucao);
			logsMsg.add(logMsg);
			
		}
		logExecucaoMensagemRepository.inserir(logsMsg);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void salvar(String mensagem, Usuario usuario, Date dataInicio, StatusExecucaoEnum status,
			String nomeArquivo, InterfaceExecucao interfaceExecucao) {
		
		LogExecucao logExecucao = this.inserirLogExecucao(usuario, interfaceExecucao, dataInicio, status);

		EventoExecucao eventoExecucao = eventoExecucaoRepository.findByNome(EVENTO_INTERFACE_MICRO_DISTRIBUICAO);
		
		LogExecucaoMensagem logMensagem = this.gerarLogExecucaoMensagem(nomeArquivo, mensagem, logExecucao, eventoExecucao);
		logExecucaoMensagemRepository.inserir(logMensagem);
	}

	private LogExecucao inserirLogExecucao(Usuario usuario,
			InterfaceExecucao interfaceExecucao, Date dataInicio,
			StatusExecucaoEnum status) {
		
		LogExecucao logExecucao = new LogExecucao();
		logExecucao.setStatus				(status);
		logExecucao.setDataInicio			(dataInicio);
		logExecucao.setDataFim				(new Date());
		logExecucao.setInterfaceExecucao	(interfaceExecucao);
		logExecucao.setNomeLoginUsuario		(usuario.getNome());
		logExecucao.setCodigoDistribuidor(distribuidorRepository.obter().getCodigoDistribuidorDinap());
		logExecucaoRepository.inserir(logExecucao);
		return logExecucao;
	}

	private LogExecucaoMensagem gerarLogExecucaoMensagem(String nomeArquivo, String mensagem, LogExecucao logExecucao, EventoExecucao eventoExecucao) {
		
		LogExecucaoMensagem logExecucaoMensagem = new LogExecucaoMensagem();
		logExecucaoMensagem.setLogExecucao		(logExecucao);
		logExecucaoMensagem.setEventoExecucao	(eventoExecucao);
		logExecucaoMensagem.setNomeArquivo		(nomeArquivo);
		logExecucaoMensagem.setMensagem			(mensagem);
//		logExecucaoMensagem.setMensagemInfo		(erroMsg); // Setar mensagem de erro de sistema. Sugestacao - Exception.getMessage()
		return logExecucaoMensagem;
	}


	
}