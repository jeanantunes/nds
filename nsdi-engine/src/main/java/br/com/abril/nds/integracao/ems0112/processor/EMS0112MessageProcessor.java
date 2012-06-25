package br.com.abril.nds.integracao.ems0112.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0112Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEditor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEditor;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

@Component
public class EMS0112MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void processMessage(Message message) {

		EMS0112Input input = (EMS0112Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM Editor e ");
		sql.append("LEFT JOIN FETCH e.enderecos ender ");
		sql.append("LEFT JOIN FETCH e.telefones tel ");
		sql.append("JOIN FETCH e.pessoaJuridica p ");
		sql.append("WHERE e.codigo = :codigoEditor ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("codigoEditor", input.getCodigoEditor());
		
		
		
		try {
			
			
			Editor editor = (Editor) query.getSingleResult();
			
			if(distribuidorService.isDistribuidor(input.getCodigoDistribuidor()) &&
					input.getTipoOperacao().equals("A")){
				
				if(!input.getNomeEditor().equals(editor.getNome())){
					editor.setNome(input.getNomeEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Nome do Editor para: "+editor.getNome());
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
		} catch (NoResultException e) {
			//CASO: NAO EXISTE EDITOR CADASTRADO
			
			//INSERE
			
			//PESSOA
			PessoaJuridica pessoa = new PessoaJuridica();
			pessoa.setCnpj(input.getCnpj());
			pessoa.setInscricaoEstadual(input.getInscricaoEstadual());
			pessoa.setInscricaoMunicipal(input.getInscricaoMunicipal());		
			entityManager.persist(pessoa);
			
			//EDITOR
			Editor ed = new Editor();
			ed.setCodigo(input.getCodigoEditor());
			ed.setNome(input.getNomeEditor());
			ed.setAtivo(input.getStatus());
			ed.setNomeContato(input.getNomeContato());
			ed.setPessoaJuridica(pessoa);
			entityManager.persist(ed);
			
			
			//ENDERECO EDITOR [COMERCIAL]
			Endereco endComercial = new Endereco();
			endComercial.setTipoLogradouro(input.getTipoLogradouroEditor());
			endComercial.setLogradouro(input.getLogradouroEditor());
			endComercial.setNumero(input.getNumeroEditor());
			endComercial.setComplemento(input.getComplementoEditor());
			endComercial.setCidade(input.getCidadeEditor());
			endComercial.setUf(input.getUfEditor());
			endComercial.setCep(input.getCepEditor());
			endComercial.setCodigoBairro(input.getBairroEditor());
			entityManager.persist(endComercial);
			
			EnderecoEditor enderecoEditorCom = new EnderecoEditor();
			enderecoEditorCom.setTipoEndereco(TipoEndereco.COMERCIAL);
			enderecoEditorCom.setEditor(ed);
			enderecoEditorCom.setEndereco(endComercial);
			entityManager.persist(enderecoEditorCom);			
			
			
			//ENDERECO EDITOR [ENTREGA]
			Endereco endEntrega = new Endereco();
			endEntrega.setTipoLogradouro(input.getTipoLogradouroEntrega());
			endEntrega.setLogradouro(input.getLogradouroEntrega());
			endEntrega.setNumero(input.getNumeroEntrega());
			endEntrega.setComplemento(input.getComplementoEntrega());
			endEntrega.setCidade(input.getCidadeEntrega());
			endEntrega.setUf(input.getUfEntrega());
			endEntrega.setCep(input.getCepEntrega());
			endEntrega.setCodigoBairro(input.getBairroEntrega());
			entityManager.persist(endEntrega);
			
			EnderecoEditor enderecoEditorEnt = new EnderecoEditor();		
			enderecoEditorEnt.setTipoEndereco(TipoEndereco.LOCAL_ENTREGA);
			enderecoEditorEnt.setEditor(ed);
			enderecoEditorEnt.setEndereco(endEntrega);
			entityManager.persist(enderecoEditorEnt);
			
			//TELEFONE [CONTATO]
			Telefone telContato = new Telefone();
			telContato.setDdd(input.getDddContato());
			telContato.setNumero(input.getTelefoneContato());
			telContato.setPessoa(ed.getPessoaJuridica()); 
			entityManager.persist(telContato);
			
			TelefoneEditor telefoneContato = new TelefoneEditor();
			telefoneContato.setTelefone(telContato);
			telefoneContato.setEditor(ed);
			telefoneContato.setTipoTelefone(TipoTelefone.CONTATO);
			entityManager.persist(telefoneContato);
			
			//TELEFONE [FAX]
			Telefone telFax = new Telefone();
			telFax.setDdd(input.getDddFax());
			telFax.setNumero(input.getTelefoneFax());
			telFax.setPessoa(ed.getPessoaJuridica());
			entityManager.persist(telFax);
			
			TelefoneEditor telefoneFax = new TelefoneEditor();
			telefoneFax.setTelefone(telFax);
			telefoneFax.setEditor(ed);
			telefoneFax.setTipoTelefone(TipoTelefone.FAX);
			entityManager.persist(telefoneFax);
			
			//TELEFONE [PRINCIPAL]
			Telefone telPrincipal = new Telefone();
			telPrincipal.setDdd(input.getDddEditor());
			telPrincipal.setNumero(input.getTelefoneEditor());
			telPrincipal.setPessoa(ed.getPessoaJuridica());
			entityManager.persist(telPrincipal);

			TelefoneEditor telefonePrincipal = new TelefoneEditor();
			telefonePrincipal.setTelefone(telPrincipal);
			telefonePrincipal.setEditor(ed);
			telefonePrincipal.setTipoTelefone(TipoTelefone.COMERCIAL);
			telefonePrincipal.setPrincipal(true);
			entityManager.persist(telefonePrincipal);
			
			
		}
		
	}
	
	private void atualizaEndereco(EMS0112Input input, Message message, Editor editor, TipoEndereco tipo){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM EnderecoEditor e ");
		sql.append("WHERE e.editor.codigo = :codigoEditor ");
		sql.append("AND e.tipoEndereco = :tipo ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("codigoEditor", editor.getCodigo());
		query.setParameter("tipo", tipo);
		
		try {
			
			EnderecoEditor ed = (EnderecoEditor) query.getSingleResult();
			
			if(ed.getTipoEndereco() == TipoEndereco.COMERCIAL){
				
				if(!input.getTipoLogradouroEditor().equals(ed.getTipoEndereco().toString())){
					ed.getEndereco().setTipoLogradouro(input.getTipoLogradouroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Tipo Endereco para: "+ed.getTipoEndereco().toString());
				}
				
				if(!input.getLogradouroEditor().equals(ed.getEndereco().getLogradouro())){
					ed.getEndereco().setLogradouro(input.getLogradouroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Logradouro para: "+ed.getEndereco().getLogradouro());
				}
				
				if(!input.getNumeroEditor().equals(ed.getEndereco().getNumero())){
					ed.getEndereco().setNumero(input.getNumeroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Numero Endereco para: "+ed.getEndereco().getNumero());
				}
				
				if(!input.getComplementoEditor().equals(ed.getEndereco().getComplemento())){
					ed.getEndereco().setComplemento(input.getComplementoEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Complemento para: "+ed.getEndereco().getComplemento());
				}
				
				if(!input.getCidadeEditor().equals(ed.getEndereco().getCidade())){
					ed.getEndereco().setCidade(input.getCidadeEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Cidade para: "+ed.getEndereco().getCidade());
				}
				
				if(!input.getUfEditor().equals(ed.getEndereco().getUf())){
					ed.getEndereco().setUf(input.getUfEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Sigla UF para: "+ed.getEndereco().getUf());
				}
				
				if(!input.getCepEditor().equals(ed.getEndereco().getCep())){
					ed.getEndereco().setCep(input.getCepEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do CEP para: "+ed.getEndereco().getCep());
				}
				
				if(!input.getBairroEditor().equals(ed.getEndereco().getCodigoBairro())){
					ed.getEndereco().setCodigoBairro(input.getBairroEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Codigo Bairro para: "+ed.getEndereco().getCodigoBairro());
				}
				
				
			}else if(ed.getTipoEndereco() == TipoEndereco.LOCAL_ENTREGA){
				
				if(!input.getTipoLogradouroEntrega().equals(ed.getTipoEndereco().toString())){
					ed.getEndereco().setTipoLogradouro(input.getTipoLogradouroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Tipo Endereco para: "+ed.getTipoEndereco().toString());
				}
				
				if(!input.getLogradouroEntrega().equals(ed.getEndereco().getLogradouro())){
					ed.getEndereco().setLogradouro(input.getLogradouroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Logradouro para: "+ed.getEndereco().getLogradouro());
				}
				
				if(!input.getNumeroEntrega().equals(ed.getEndereco().getNumero())){
					ed.getEndereco().setNumero(input.getNumeroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Numero Endereco para: "+ed.getEndereco().getNumero());
				}
				
				if(!input.getComplementoEntrega().equals(ed.getEndereco().getComplemento())){
					ed.getEndereco().setComplemento(input.getComplementoEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Complemento para: "+ed.getEndereco().getComplemento());
				}
				
				if(!input.getCidadeEntrega().equals(ed.getEndereco().getCidade())){
					ed.getEndereco().setCidade(input.getCidadeEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Cidade para: "+ed.getEndereco().getCidade());
				}
				
				if(!input.getUfEntrega().equals(ed.getEndereco().getUf())){
					ed.getEndereco().setUf(input.getUfEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da Sigla UF para: "+ed.getEndereco().getUf());
				}
				
				if(!input.getCepEntrega().equals(ed.getEndereco().getCep())){
					ed.getEndereco().setCep(input.getCepEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do CEP para: "+ed.getEndereco().getCep());
				}
				
				if(!input.getBairroEntrega().equals(ed.getEndereco().getCodigoBairro())){
					ed.getEndereco().setCodigoBairro(input.getBairroEntrega());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Codigo Bairro para: "+ed.getEndereco().getCodigoBairro());
				}
			}
		
			
		} catch (NoResultException e) {
			//CASO: EXISTE EDITOR POREM NAO EXISTE ENDERECO CADASTRADO
			
			// INSERE 
			
			if(tipo == TipoEndereco.COMERCIAL){
				//ENDERECO EDITOR [COMERCIAL]
				Endereco endComercial = new Endereco();
				endComercial.setTipoLogradouro(input.getTipoLogradouroEditor());
				endComercial.setLogradouro(input.getLogradouroEditor());
				endComercial.setNumero(input.getNumeroEditor());
				endComercial.setComplemento(input.getComplementoEditor());
				endComercial.setCidade(input.getCidadeEditor());
				endComercial.setUf(input.getUfEditor());
				endComercial.setCep(input.getCepEditor());
				endComercial.setCodigoBairro(input.getBairroEditor());
				entityManager.persist(endComercial);
				
				EnderecoEditor enderecoEditorCom = new EnderecoEditor();
				enderecoEditorCom.setTipoEndereco(TipoEndereco.COMERCIAL);
				enderecoEditorCom.setEditor(editor);
				enderecoEditorCom.setEndereco(endComercial);
				entityManager.persist(enderecoEditorCom);
				
			
			}else if(tipo == TipoEndereco.LOCAL_ENTREGA){
				//ENDERECO EDITOR [ENTREGA]
				Endereco endEntrega = new Endereco();
				endEntrega.setTipoLogradouro(input.getTipoLogradouroEntrega());
				endEntrega.setLogradouro(input.getLogradouroEntrega());
				endEntrega.setNumero(input.getNumeroEntrega());
				endEntrega.setComplemento(input.getComplementoEntrega());
				endEntrega.setCidade(input.getCidadeEntrega());
				endEntrega.setUf(input.getUfEntrega());
				endEntrega.setCep(input.getCepEntrega());
				endEntrega.setCodigoBairro(input.getBairroEntrega());
				entityManager.persist(endEntrega);
				
				EnderecoEditor enderecoEditorEnt = new EnderecoEditor();		
				enderecoEditorEnt.setTipoEndereco(TipoEndereco.LOCAL_ENTREGA);
				enderecoEditorEnt.setEditor(editor);
				enderecoEditorEnt.setEndereco(endEntrega);
				entityManager.persist(enderecoEditorEnt);
				
			}
			

		}
	}
		
	private void atualizaTelefone (EMS0112Input input, Message message, Editor editor, TipoTelefone tipo){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM TelefoneEditor e ");
		sql.append("WHERE e.editor.codigo = :codigoEditor ");
		sql.append("AND e.tipoTelefone= :tipo ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("codigoEditor", editor.getCodigo());
		query.setParameter("tipo", tipo);
		
		try {
			
			TelefoneEditor telefone = (TelefoneEditor) query.getSingleResult();
			
			if(telefone.getTipoTelefone() == TipoTelefone.FAX){
				
				if(!input.getDddFax().equals(telefone.getTelefone().getDdd())){
					telefone.getTelefone().setDdd(input.getDddFax());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do DDD (Fax) para: "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneFax().equals(telefone.getTelefone().getNumero())){
					telefone.getTelefone().setNumero(input.getTelefoneFax());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Numero (Fax) para: "+telefone.getTelefone().getNumero());
				}
				
			}else if(telefone.isPrincipal() == true){
				
				if(!input.getDddEditor().equals(telefone.getTelefone().getDdd())){
					telefone.getTelefone().setDdd(input.getDddEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do DDD Principal para: "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneEditor().equals(telefone.getTelefone().getNumero())){
					telefone.getTelefone().setNumero(input.getTelefoneEditor());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Numero Principal para: "+telefone.getTelefone().getNumero());
				}
				
			}else if(telefone.getTipoTelefone() == TipoTelefone.CONTATO){
				
				if(!input.getDddContato().equals(telefone.getTelefone().getDdd())){
					telefone.getTelefone().setDdd(input.getDddContato());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do DDD Principal para: "+telefone.getTelefone().getDdd());
				}
				
				if(!input.getTelefoneContato().equals(telefone.getTelefone().getNumero())){
					telefone.getTelefone().setNumero(input.getTelefoneContato());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Numero Principal para: "+telefone.getTelefone().getNumero());
				}
			}
					
		} catch (NoResultException e) {
			//CASO: EXISTE EDITOR POREM NAO EXISTE TELEFONE CADASTRADO
			
			// INSERE 
			
			if(tipo == TipoTelefone.CONTATO){
				//TELEFONE [CONTATO]
				Telefone telContato = new Telefone();
				telContato.setDdd(input.getDddContato());
				telContato.setNumero(input.getTelefoneContato());
				telContato.setPessoa(editor.getPessoaJuridica()); 
				entityManager.persist(telContato);
				
				TelefoneEditor telefoneContato = new TelefoneEditor();
				telefoneContato.setTelefone(telContato);
				telefoneContato.setEditor(editor);
				telefoneContato.setTipoTelefone(TipoTelefone.CONTATO);
				entityManager.persist(telefoneContato);
				
				
			}else if(tipo == TipoTelefone.FAX){
				//TELEFONE [FAX]
				Telefone telFax = new Telefone();
				telFax.setDdd(input.getDddFax());
				telFax.setNumero(input.getTelefoneFax());
				telFax.setPessoa(editor.getPessoaJuridica());
				entityManager.persist(telFax);
				
				TelefoneEditor telefoneFax = new TelefoneEditor();
				telefoneFax.setTelefone(telFax);
				telefoneFax.setEditor(editor);
				telefoneFax.setTipoTelefone(TipoTelefone.FAX);
				entityManager.persist(telefoneFax);
				
			}else if(tipo == TipoTelefone.COMERCIAL){	
				//TELEFONE [PRINCIPAL]
				Telefone telPrincipal = new Telefone();
				telPrincipal.setDdd(input.getDddEditor());
				telPrincipal.setNumero(input.getTelefoneEditor());
				telPrincipal.setPessoa(editor.getPessoaJuridica());
				entityManager.persist(telPrincipal);
	
				TelefoneEditor telefonePrincipal = new TelefoneEditor();
				telefonePrincipal.setTelefone(telPrincipal);
				telefonePrincipal.setEditor(editor);
				telefonePrincipal.setTipoTelefone(TipoTelefone.COMERCIAL);
				telefonePrincipal.setPrincipal(true);
				entityManager.persist(telefonePrincipal);
				
			}
			
		}
		
	}
		
		
		

}
