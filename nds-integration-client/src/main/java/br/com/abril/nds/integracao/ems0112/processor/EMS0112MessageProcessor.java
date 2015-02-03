package br.com.abril.nds.integracao.ems0112.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0112Input;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEditor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEditor;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component

public class EMS0112MessageProcessor extends AbstractRepository implements MessageProcessor  {


    private static final Logger LOGGER = LoggerFactory.getLogger(EMS0112MessageProcessor.class);

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		EMS0112Input input = (EMS0112Input) message.getBody();
		try{
			
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT e ");
			sql.append(" FROM Editor e ");
			sql.append(" LEFT JOIN FETCH e.enderecos ender ");
			sql.append(" LEFT JOIN FETCH e.telefones tel ");
			sql.append(" JOIN FETCH e.pessoaJuridica p ");
			sql.append(" WHERE e.codigo = :codigoEditor AND e.ativo = true ");
			
			Query query = getSession().createQuery(sql.toString());
			query.setParameter("codigoEditor", input.getCodigoEditor());

			Object object = query.uniqueResult();
			
			if (null != object) {
				Editor editor = (Editor) object;
				if(distribuidorService.isDistribuidor(input.getCodigoDistribuidor()) &&
						input.getTipoOperacao().equals("A")) {
					
					if(!input.getNomeEditor().equals(editor.getPessoaJuridica().getNome())){
						editor.getPessoaJuridica().setRazaoSocial(input.getNomeEditor());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Nome do Editor para: "+editor.getPessoaJuridica().getNome());
					}
					
					if(!input.getInscricaoMunicipal().equals(editor.getPessoaJuridica().getInscricaoMunicipal())){
						editor.getPessoaJuridica().setInscricaoMunicipal(input.getInscricaoMunicipal());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Inscricao Municipal para: "+editor.getPessoaJuridica().getInscricaoMunicipal());
					}
					
					
					if(!input.getNomeContato().equals(editor.getNomeContato())){
						editor.setNomeContato(input.getNomeContato());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Nome Contato para: "+editor.getNomeContato());
					}
					
					if(input.getStatus() != editor.isAtivo()){
						editor.setAtivo(input.getStatus());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Status para: "+editor.isAtivo());
					}
					
					if(!input.getCnpj().equals(editor.getPessoaJuridica().getCnpj())){
						editor.getPessoaJuridica().setCnpj(input.getCnpj());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do CNPJ para: "+editor.getPessoaJuridica().getCnpj());
					}
					
					if(!input.getInscricaoEstadual().equals(editor.getPessoaJuridica().getInscricaoEstadual())){
						editor.getPessoaJuridica().setInscricaoEstadual(input.getInscricaoEstadual());
						ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Inscricao Estadual para: "+editor.getPessoaJuridica().getInscricaoEstadual());
					}
					
					atualizaEndereco(input, message, editor, TipoEndereco.COMERCIAL);
					
					atualizaEndereco(input, message, editor, TipoEndereco.LOCAL_ENTREGA);
	
					
					atualizaTelefone(input, message, editor, TipoTelefone.COMERCIAL);
					
					atualizaTelefone(input, message, editor, TipoTelefone.FAX);
					
					atualizaTelefone(input, message, editor, TipoTelefone.CONTATO);			
					
				}
			} else {
				//CASO: NAO EXISTE EDITOR CADASTRADO		
				// VERIFICA A EXISTENCIA DA PESSOA
				
				StringBuilder hql = new StringBuilder();
				hql.append("SELECT j ");
				hql.append("FROM PessoaJuridica j ");
				hql.append("WHERE j.cnpj = :cnpj ");
				
				Query pjQuery = getSession().createQuery(hql.toString());
				
				pjQuery.setParameter("cnpj", input.getCnpj());
						
				PessoaJuridica pessoa = (PessoaJuridica) pjQuery.uniqueResult();
				
				if (null == pessoa) {			
					//INSERE
							
					//PESSOA
					pessoa = new PessoaJuridica();				
					pessoa.setCnpj(input.getCnpj());
					pessoa.setInscricaoEstadual(input.getInscricaoEstadual());
					pessoa.setInscricaoMunicipal(input.getInscricaoMunicipal());
					pessoa.setRazaoSocial(input.getNomeEditor());
					getSession().persist(pessoa);
					
					ndsiLoggerFactory.getLogger().logInfo(
							message
							, EventoExecucaoEnum.INF_DADO_ALTERADO
							, "Inserido Editor/Pessoa: "+ input.getNomeEditor());
					
				} else {
					
					pessoa.setInscricaoEstadual(input.getInscricaoEstadual());
					pessoa.setInscricaoMunicipal(input.getInscricaoMunicipal());		
					pessoa.setRazaoSocial(input.getNomeEditor());
					getSession().update(pessoa);	
					
					ndsiLoggerFactory.getLogger().logInfo(
							message
							, EventoExecucaoEnum.INF_DADO_ALTERADO
							, "Atualizado Editor/Pessoa: "+ input.getNomeEditor());
					
				}
				
				//EDITOR
				Editor ed = new Editor();
				ed.setCodigo(input.getCodigoEditor());
				ed.setAtivo(input.getStatus());
				ed.setNomeContato(input.getNomeContato());
				ed.setPessoaJuridica(pessoa);
				getSession().persist(ed);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
						, "Atualizado Editor/Pessoa: "+ input.getNomeEditor());
				
				//ENDERECO EDITOR [COMERCIAL]
				Endereco endComercial = new Endereco();
				endComercial.setTipoLogradouro(input.getTipoLogradouroEditor());
				endComercial.setLogradouro(input.getLogradouroEditor());
				endComercial.setCidade(input.getCidadeEditor());
				endComercial.setUf(input.getUfEditor());
				endComercial.setCep(input.getCepEditor());
				endComercial.setBairro(input.getBairroEditor());
				
				endComercial.setNumero(input.getNumeroEditor());
				endComercial.setComplemento(input.getComplementoEditor());			
				getSession().persist(endComercial);
				
				EnderecoEditor enderecoEditorCom = new EnderecoEditor();
				enderecoEditorCom.setTipoEndereco(TipoEndereco.COMERCIAL);
				enderecoEditorCom.setEditor(ed);
				enderecoEditorCom.setEndereco(endComercial);
				getSession().persist(enderecoEditorCom);
				
				
				//ENDERECO EDITOR [ENTREGA]
				Endereco endEntrega = new Endereco();				
				endEntrega.setTipoLogradouro(input.getTipoLogradouroEntrega());
				endEntrega.setLogradouro(input.getLogradouroEntrega());
				endEntrega.setCidade(input.getCidadeEntrega());
				endEntrega.setUf(input.getUfEntrega());
				endEntrega.setCep(input.getCepEntrega());
				endEntrega.setBairro(input.getBairroEntrega());
	
				endEntrega.setNumero(input.getNumeroEntrega());
				endEntrega.setComplemento(input.getComplementoEntrega());
				getSession().persist(endEntrega);
				
				EnderecoEditor enderecoEditorEnt = new EnderecoEditor();		
				enderecoEditorEnt.setTipoEndereco(TipoEndereco.LOCAL_ENTREGA);
				enderecoEditorEnt.setEditor(ed);
				enderecoEditorEnt.setEndereco(endEntrega);
				getSession().persist(enderecoEditorEnt);
				
				//TELEFONE [CONTATO]
				Telefone telContato = new Telefone();
				telContato.setDdd(input.getDddContato());
				telContato.setNumero(input.getTelefoneContato());
				telContato.setPessoa(ed.getPessoaJuridica()); 
				getSession().persist(telContato);
				
				TelefoneEditor telefoneContato = new TelefoneEditor();
				telefoneContato.setTelefone(telContato);
				telefoneContato.setEditor(ed);
				telefoneContato.setTipoTelefone(TipoTelefone.CONTATO);
				getSession().persist(telefoneContato);
				
				//TELEFONE [FAX]
				Telefone telFax = new Telefone();
				telFax.setDdd(input.getDddFax());
				telFax.setNumero(input.getTelefoneFax());
				telFax.setPessoa(ed.getPessoaJuridica());
				getSession().persist(telFax);
				
				TelefoneEditor telefoneFax = new TelefoneEditor();
				telefoneFax.setTelefone(telFax);
				telefoneFax.setEditor(ed);
				telefoneFax.setTipoTelefone(TipoTelefone.FAX);
				getSession().persist(telefoneFax);
				
				//TELEFONE [PRINCIPAL]
				Telefone telPrincipal = new Telefone();
				telPrincipal.setDdd(input.getDddEditor());
				telPrincipal.setNumero(input.getTelefoneEditor());
				telPrincipal.setPessoa(ed.getPessoaJuridica());
				getSession().persist(telPrincipal);
	
				TelefoneEditor telefonePrincipal = new TelefoneEditor();
				telefonePrincipal.setTelefone(telPrincipal);
				telefonePrincipal.setEditor(ed);
				telefonePrincipal.setTipoTelefone(TipoTelefone.COMERCIAL);
				telefonePrincipal.setPrincipal(true);
				getSession().persist(telefonePrincipal);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
,
                        "Inseridos dados de Endereço e Telefone do Editor: " + input.getNomeEditor());
				
				
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.HIERARQUIA,
			String.format( "Ocorreu um erro inesperado para numeroEditor: "+ input.getNumeroEditor()+" codigoEditor: "+ input.getCodigoEditor()
 + " no Interface 112, descrição do erro: "
                            + e.getMessage() + ". Erro não para o processamento."));
		}
	}

	private void atualizaEndereco(EMS0112Input input, Message message, Editor editor, TipoEndereco tipo){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM EnderecoEditor e ");
		sql.append("WHERE e.editor.codigo = :codigoEditor ");
		sql.append("AND e.tipoEndereco = :tipo ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoEditor", editor.getCodigo());
		query.setParameter("tipo", tipo);
				
			
		EnderecoEditor ed = (EnderecoEditor) query.uniqueResult();
		if (null != ed) {
			
			if(ed.getTipoEndereco() == TipoEndereco.COMERCIAL){
				
				if(!input.getNumeroEditor().equals(ed.getEndereco().getNumero())){
					ed.getEndereco().setNumero(input.getNumeroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Número Endereço para "+ed.getEndereco().getNumero());
				}
				
				if(!input.getComplementoEditor().equals(ed.getEndereco().getComplemento())){
					ed.getEndereco().setComplemento(input.getComplementoEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Complemento para "+ed.getEndereco().getComplemento());
				}
				
				if(!input.getTipoLogradouroEditor().equals(ed.getTipoEndereco().toString())){
					ed.getEndereco().setTipoLogradouro(input.getTipoLogradouroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Tipo Endereço para "+ed.getTipoEndereco().toString());
				}

				if(!input.getLogradouroEditor().equals(ed.getEndereco().getLogradouro())){
					ed.getEndereco().setLogradouro(input.getLogradouroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Logradouro para "+ed.getEndereco().getLogradouro());
				}

				if(!input.getCidadeEditor().equals(ed.getEndereco().getCidade())){
					ed.getEndereco().setCidade(input.getCidadeEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração da Cidade para "+ed.getEndereco().getCidade());
				}

				if(!input.getUfEditor().equals(ed.getEndereco().getUf())){
					ed.getEndereco().setUf(input.getUfEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração da Sigla UF para "+ed.getEndereco().getUf());
				}

				if(!input.getCepEditor().equals(ed.getEndereco().getCep())){
					ed.getEndereco().setCep(input.getCepEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do CEP para "+ed.getEndereco().getCep());
				}

				if(!input.getBairroEditor().equals(ed.getEndereco().getBairro())){
					ed.getEndereco().setBairro(input.getBairroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Bairro para "+ed.getEndereco().getBairro());
				}						
				
			} else if(ed.getTipoEndereco() == TipoEndereco.LOCAL_ENTREGA) {
				
				if(!input.getNumeroEntrega().equals(ed.getEndereco().getNumero())){
					ed.getEndereco().setNumero(input.getNumeroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Número Endereço para "+ed.getEndereco().getNumero());
				}
				
				if(!input.getComplementoEntrega().equals(ed.getEndereco().getComplemento())){
					ed.getEndereco().setComplemento(input.getComplementoEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Complemento para "+ed.getEndereco().getComplemento());
				}
									
				if(!input.getTipoLogradouroEntrega().equals(ed.getTipoEndereco().toString())){
					ed.getEndereco().setTipoLogradouro(input.getTipoLogradouroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Tipo Endereço para "+ed.getTipoEndereco().toString());
				}
				
				if(!input.getLogradouroEntrega().equals(ed.getEndereco().getLogradouro())){
					ed.getEndereco().setLogradouro(input.getLogradouroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Logradouro para "+ed.getEndereco().getLogradouro());
				}
				
				if(!input.getCidadeEntrega().equals(ed.getEndereco().getCidade())){
					ed.getEndereco().setCidade(input.getCidadeEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração da Cidade para "+ed.getEndereco().getCidade());
				}
				
				if(!input.getUfEntrega().equals(ed.getEndereco().getUf())){
					ed.getEndereco().setUf(input.getUfEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração da Sigla UF para "+ed.getEndereco().getUf());
				}
				
				if(!input.getCepEntrega().equals(ed.getEndereco().getCep())){
					ed.getEndereco().setCep(input.getCepEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do CEP para "+ed.getEndereco().getCep());
				}
				
				if(!input.getBairroEntrega().equals(ed.getEndereco().getBairro())){
					ed.getEndereco().setBairro(input.getBairroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Código Bairro para "+ed.getEndereco().getBairro());
				}
			
			}
		
			
		} else {
			//CASO: EXISTE EDITOR POREM NAO EXISTE ENDERECO CADASTRADO
			
			// INSERE 
			
			if(tipo == TipoEndereco.COMERCIAL){
				//ENDERECO EDITOR [COMERCIAL]
				
				Endereco endComercial = new Endereco();
				endComercial.setTipoLogradouro(input.getTipoLogradouroEditor());
				endComercial.setLogradouro(input.getLogradouroEditor());
				endComercial.setCidade(input.getCidadeEditor());
				endComercial.setUf(input.getUfEditor());
				endComercial.setCep(input.getCepEditor());
				endComercial.setBairro(input.getBairroEditor());

				endComercial.setNumero(input.getNumeroEditor());
				endComercial.setComplemento(input.getComplementoEditor());
				
				getSession().persist(endComercial);
				
				EnderecoEditor enderecoEditorCom = new EnderecoEditor();
				enderecoEditorCom.setTipoEndereco(TipoEndereco.COMERCIAL);
				enderecoEditorCom.setEditor(editor);
				enderecoEditorCom.setEndereco(endComercial);
				getSession().persist(enderecoEditorCom);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
,
                        "Inseridos dados de Endereço do Editor: " + input.getNomeEditor());
			
			} else if(tipo == TipoEndereco.LOCAL_ENTREGA) {
				//ENDERECO EDITOR [ENTREGA]

				Endereco endEntrega = new Endereco();
				endEntrega.setTipoLogradouro(input.getTipoLogradouroEntrega());
				endEntrega.setLogradouro(input.getLogradouroEntrega());
				endEntrega.setCidade(input.getCidadeEntrega());
				endEntrega.setUf(input.getUfEntrega());
				endEntrega.setCep(input.getCepEntrega());
				endEntrega.setBairro(input.getBairroEntrega());

				endEntrega.setNumero(input.getNumeroEntrega());
				endEntrega.setComplemento(input.getComplementoEntrega());

				getSession().persist(endEntrega);
				
				EnderecoEditor enderecoEditorEnt = new EnderecoEditor();		
				enderecoEditorEnt.setTipoEndereco(TipoEndereco.LOCAL_ENTREGA);
				enderecoEditorEnt.setEditor(editor);
				enderecoEditorEnt.setEndereco(endEntrega);
				getSession().persist(enderecoEditorEnt);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
,
                        "Inseridos dados de Endereço do Editor: " + input.getNomeEditor());
				
			}
			

		}
	}
		
	
	private void atualizaTelefone (EMS0112Input input, Message message, Editor editor, TipoTelefone tipo){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM TelefoneEditor e ");
		sql.append("WHERE e.editor.codigo = :codigoEditor ");
		sql.append("AND e.tipoTelefone= :tipo ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoEditor", editor.getCodigo());
		query.setParameter("tipo", tipo);
			
		TelefoneEditor telefone = (TelefoneEditor) query.uniqueResult();
		
		if (null != telefone) {
			if(telefone.getTipoTelefone() == TipoTelefone.FAX){
				
				if(!input.getDddFax().equals(telefone.getTelefone().getDdd())) {
					telefone.getTelefone().setDdd(input.getDddFax());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do DDD (Fax) para "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneFax().equals(telefone.getTelefone().getNumero())) {
					telefone.getTelefone().setNumero(input.getTelefoneFax());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Número (Fax) para "+telefone.getTelefone().getNumero());
				}
				
			} else if(telefone.isPrincipal() == true) {
				
				if(!input.getDddEditor().equals(telefone.getTelefone().getDdd())) {
					telefone.getTelefone().setDdd(input.getDddEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do DDD Principal para "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneEditor().equals(telefone.getTelefone().getNumero())) {
					telefone.getTelefone().setNumero(input.getTelefoneEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Número Principal para "+telefone.getTelefone().getNumero());
				}
				
			} else if(telefone.getTipoTelefone() == TipoTelefone.CONTATO) {
				
				if(!input.getDddContato().equals(telefone.getTelefone().getDdd())) {
					telefone.getTelefone().setDdd(input.getDddContato());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do DDD Principal para "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneContato().equals(telefone.getTelefone().getNumero())) {
					telefone.getTelefone().setNumero(input.getTelefoneContato());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Alteração do Número Principal para "+telefone.getTelefone().getNumero());
				}
			}
					
		} else {
			//CASO: EXISTE EDITOR POREM NAO EXISTE TELEFONE CADASTRADO
			
			// INSERE 
			
			if(tipo == TipoTelefone.CONTATO) {
				//TELEFONE [CONTATO]
				Telefone telContato = new Telefone();
				telContato.setDdd(input.getDddContato());
				telContato.setNumero(input.getTelefoneContato());
				telContato.setPessoa(editor.getPessoaJuridica()); 
				getSession().persist(telContato);
				
				TelefoneEditor telefoneContato = new TelefoneEditor();
				telefoneContato.setTelefone(telContato);
				telefoneContato.setEditor(editor);
				telefoneContato.setTipoTelefone(TipoTelefone.CONTATO);
				getSession().persist(telefoneContato);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
						, "Inseridos dados de Telefone do Editor: "+ input.getNomeEditor());
				
			} else if(tipo == TipoTelefone.FAX) {
				//TELEFONE [FAX]
				Telefone telFax = new Telefone();
				telFax.setDdd(input.getDddFax());
				telFax.setNumero(input.getTelefoneFax());
				telFax.setPessoa(editor.getPessoaJuridica());
				getSession().persist(telFax);
				
				TelefoneEditor telefoneFax = new TelefoneEditor();
				telefoneFax.setTelefone(telFax);
				telefoneFax.setEditor(editor);
				telefoneFax.setTipoTelefone(TipoTelefone.FAX);
				getSession().persist(telefoneFax);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
						, "Inseridos dados de Telefone do Editor "+ input.getNomeEditor());
				
			} else if(tipo == TipoTelefone.COMERCIAL) {	
				//TELEFONE [PRINCIPAL]
				Telefone telPrincipal = new Telefone();
				telPrincipal.setDdd(input.getDddEditor());
				telPrincipal.setNumero(input.getTelefoneEditor());
				telPrincipal.setPessoa(editor.getPessoaJuridica());
				getSession().persist(telPrincipal);
	
				TelefoneEditor telefonePrincipal = new TelefoneEditor();
				telefonePrincipal.setTelefone(telPrincipal);
				telefonePrincipal.setEditor(editor);
				telefonePrincipal.setTipoTelefone(TipoTelefone.COMERCIAL);
				telefonePrincipal.setPrincipal(true);
				getSession().persist(telefonePrincipal);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
						, "Inseridos dados de Telefone do Editor "+ input.getNomeEditor());
				
			}
			
		}
		
	}
		
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}