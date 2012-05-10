package br.com.abril.nds.integracao.ems0116.processor;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0116.inbound.EMS0116Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;


@Component
public class EMS0116MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
		
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message){
		EMS0116Input input = (EMS0116Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		// Verifica cota
		sql.append("SELECT co  ");
		sql.append("FROM Cota co ");
		sql.append("JOIN FETCH co.pessoa ");
		sql.append("WHERE ");
		sql.append("     co.numeroCota = :numeroCota ");
		Query query = entityManager.createQuery(sql.toString());
		query = entityManager.createQuery(sql.toString());
		query.setParameter("numeroCota", input.getCodCota());
		Cota cota = null;
		
		try {
			cota = (Cota) query.getSingleResult();
		} catch(NoResultException e) {
			// FIXME Não encontrou a Box. Realizar Log
			// Passar para a próxima linha
			ndsiLoggerFactory.getLogger().logError(message, "Codigo Cota " +  input.getCodCota() + " nao encontrado.");
			e.printStackTrace();
			return;
		}	
		
		try {
			
			Telefone telefone = new Telefone();
			Endereco endereco = new Endereco();
			
			if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())){
				
				//Definir Endereco
				sql = new StringBuilder();
				sql.append("SELECT e  ");
				sql.append("FROM Endereco e ");
				sql.append("WHERE ");
				sql.append("     e.cep = :cep ");
				query = entityManager.createQuery(sql.toString());
				query.setParameter("cep", input.getCep());	
				
				List<Endereco> enderecos = (List<Endereco>) query.getResultList();
					
					if (enderecos.isEmpty()) {
												
						endereco.setCodigoBairro(input.getCodBairro());
						endereco.setCep(input.getCep());
						endereco.setCidade(input.getNomeMunicipio());
						endereco.setLogradouro(input.getEndereco());
						endereco.setUf(input.getSiglaUF());	
						entityManager.persist(endereco);
						
					} else {
						
						enderecos.get(0).setCodigoBairro(input.getCodBairro());
						enderecos.get(0).setCep(input.getCep());
						enderecos.get(0).setCidade(input.getNomeMunicipio());
						enderecos.get(0).setLogradouro(input.getEndereco());
						enderecos.get(0).setUf(input.getSiglaUF());	
						endereco = enderecos.get(0);
					}
			}
			
			if (!input.getTelefone().isEmpty()){
			
				//Definir Telefone
				sql = new StringBuilder();
				sql.append("SELECT tel ");
				sql.append("FROM Telefone tel ");
				sql.append("WHERE ");
				sql.append("     tel.numero = :numero ");
				query = entityManager.createQuery(sql.toString());
				query.setParameter("numero", input.getTelefone());	
				
				List<Telefone> telefones = (List<Telefone>) query.getResultList();
				
					if (telefones.isEmpty()) {
												
						telefone.setDdd(input.getDdd());
						telefone.setNumero(input.getTelefone());
						entityManager.persist(telefone);
						
					} else {
								
						telefones.get(0).setDdd(input.getDdd());
						telefones.get(0).setNumero(input.getTelefone());
						telefone = telefones.get(0);
					}
			}
			
			//Obter PDV
			sql = new StringBuilder();
			sql.append("SELECT p  ");
			sql.append("FROM PDV p ");
			sql.append("WHERE ");
			sql.append("     p.cota = :cota ");
			query = entityManager.createQuery(sql.toString());
			query.setParameter("cota", cota);	
			
			List<PDV> pdvs = (List<PDV>) query.getResultList();
			
			if (pdvs.isEmpty()){
								
				String nomeEleito = cota.getPessoa().getNome();
		
				PDV pdv = new PDV();
				pdv.setDentroOutroEstabelecimento(false);
				pdv.setNome(nomeEleito);
				pdv.setPontoReferencia(input.getPontoReferencia());
				pdv.setPorcentagemFaturamento(new BigDecimal(0));
				pdv.setPossuiSistemaIPV(false);

				pdv.setQtdeFuncionarios(0);
				pdv.setStatus(StatusPDV.ATIVO);
				pdv.setCota(cota);
			
				entityManager.persist(pdv);
				
			}
			
		} catch (NoResultException e) {
			// Logar erro e passar para a próxima linha
			ndsiLoggerFactory.getLogger().logError(message, "Erro na execucao da interface de insercao de BANCA - EMS0116");
			e.printStackTrace();
		}
	}
}