package br.com.abril.nds.integracao.ems0117.processor;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0117.inbound.EMS0117Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;


@Component
public class EMS0117MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
		
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message) {
		EMS0117Input input = (EMS0117Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		// Obter Box
		sql.append("SELECT b  ");
		sql.append("FROM Box b ");
		sql.append("WHERE ");
		sql.append("     b.codigo = :codigo ");
		Query query = entityManager.createQuery(sql.toString());
		query = entityManager.createQuery(sql.toString());
		query.setParameter("codigo", input.getCodBox().toString());		
		Box box = null;
		
		try {
			box = (Box) query.getSingleResult();
		} catch(NoResultException e) {
			// FIXME Não encontrou a Box. Realizar Log
			// Passar para a próxima linha
			ndsiLoggerFactory.getLogger().logError(message, "Codigo BOX " +  input.getCodBox() + " nao encontrado para a Cota " + input.getCodCota());
			e.printStackTrace();
			return;
		}
		
		// Obter Pessoa
		sql = new StringBuilder();
	
		if ("F".equals(input.getTipoPessoa())){
			sql.append("SELECT pe ");
			sql.append("FROM PessoaFisica pe ");
			sql.append("WHERE ");
			sql.append("	pe.cpf = :cpf ");
		} else if ("J".equals(input.getTipoPessoa())){
			sql.append("SELECT pe ");
			sql.append("FROM PessoaJuridica pe ");
			sql.append("WHERE ");
			sql.append("	pe.cnpj = :cnpj ");
		}
		
		query = entityManager.createQuery(sql.toString());

		if ("F".equals(input.getTipoPessoa())){
			query.setParameter("cpf", input.getCpfCNPJ());
		} else if ("J".equals(input.getTipoPessoa())){
			query.setParameter("cnpj", input.getCpfCNPJ());
		}
		
		try {
			
			Pessoa pessoa = null;
			Telefone telefone = new Telefone();
			Endereco endereco = new Endereco();
					
				//Definir Pessoa
				if ("F".equals(input.getTipoPessoa())){
					
					List<PessoaFisica> pessoas = (List<PessoaFisica>) query.getResultList();
					
						if (pessoas.isEmpty()) {
							
							PessoaFisica pessoaFis = new PessoaFisica();
							
							pessoaFis.setNome(input.getNomeJornaleiro());
							pessoaFis.setCpf(input.getCpfCNPJ());
							entityManager.persist(pessoaFis);
							
							pessoa = pessoaFis;
							
						} else {
							
							pessoas.get(0).setCpf(input.getCpfCNPJ());
							pessoas.get(0).setNome(input.getNomeJornaleiro());
							
							pessoa = pessoas.get(0);
						}
	
				} else if ("J".equals(input.getTipoPessoa())){
					
					List<PessoaJuridica> pessoas = (List<PessoaJuridica>) query.getResultList();
					
						if (pessoas.isEmpty()) {
							
							PessoaJuridica pessoaJur = new PessoaJuridica();
							
							pessoaJur.setRazaoSocial(input.getNomeJornaleiro());
							pessoaJur.setCnpj(input.getCpfCNPJ());
							pessoaJur.setInscricaoEstadual(input.getInscrEstadual());
							pessoaJur.setInscricaoMunicipal(input.getInscrMunicipal());
							entityManager.persist(pessoaJur);
							
							pessoa = pessoaJur;
							
						} else {
							
							pessoas.get(0).setRazaoSocial(input.getNomeJornaleiro());
							pessoas.get(0).setCnpj(input.getCpfCNPJ());
							pessoas.get(0).setInscricaoEstadual(input.getInscrEstadual());
							pessoas.get(0).setInscricaoMunicipal(input.getInscrMunicipal());
							
							pessoa = pessoas.get(0);
						}					
				}
			
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
						endereco.setCidade(input.getMunicipio());
						endereco.setLogradouro(input.getEndereco());
						endereco.setNumero(input.getNumLogradouro());
						endereco.setUf(input.getSiglaUF());	
						endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
						entityManager.persist(endereco);
						
					} else {
						
						enderecos.get(0).setCodigoBairro(input.getCodBairro());
						enderecos.get(0).setCep(input.getCep());
						enderecos.get(0).setCidade(input.getMunicipio());
						enderecos.get(0).setLogradouro(input.getEndereco());
						enderecos.get(0).setNumero(input.getNumLogradouro());
						enderecos.get(0).setUf(input.getSiglaUF());	
						enderecos.get(0).setCodigoCidadeIBGE(input.getCodCidadeIbge());
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
			
			// Verifica cota
			sql = new StringBuilder();
			sql.append("SELECT co  ");
			sql.append("FROM Cota co ");
			sql.append("WHERE ");
			sql.append("     co.numeroCota = :numeroCota ");
			
			query = entityManager.createQuery(sql.toString());
			query.setParameter("numeroCota", input.getCodCota());
			
			List<Cota> cotas = (List<Cota>) query.getResultList();

				if (cotas.isEmpty()) {
					
					Cota cota = new Cota();
									
					cota.setInicioAtividade(new Date());
					cota.setNumeroCota(input.getCodCota());
					cota.setPossuiContrato(false);
					
						if ("1".equals(input.getSituacaoCota())){
							cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
							
						} else if ("2".equals(input.getSituacaoCota())){
							cota.setSituacaoCadastro(SituacaoCadastro.ATIVO);
							
						} else if ("3".equals(input.getSituacaoCota())){
							cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
							
						} else if ("4".equals(input.getSituacaoCota())){
							cota.setSituacaoCadastro(SituacaoCadastro.INATIVO);
						}
					
					cota.setSugereSuspensao(true);
					cota.setBox(box);
					cota.setPessoa(pessoa);
					cota.setQtdePDV(input.getQtdeCotas());
					entityManager.persist(cota);
						
						if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())){
							
							EnderecoCota enderecoCota = new EnderecoCota();
							enderecoCota.setPrincipal(true);
							enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
							enderecoCota.setEndereco(endereco);
							enderecoCota.setCota(cota);
							entityManager.persist(enderecoCota);
						}
						
						if (!input.getTelefone().isEmpty()){
							
							TelefoneCota telefoneCota = new TelefoneCota();
							telefoneCota.setPrincipal(true);
							telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
							telefoneCota.setTelefone(telefone);
							telefoneCota.setCota(cota);
							entityManager.persist(telefoneCota);
						}		
						
				} else {
					
					cotas.get(0).setInicioAtividade(new Date());
					cotas.get(0).setNumeroCota(input.getCodCota());
					cotas.get(0).setPossuiContrato(false);
					
						if ("1".equals(input.getSituacaoCota())){
							cotas.get(0).setSituacaoCadastro(SituacaoCadastro.PENDENTE);
							
						} else if ("2".equals(input.getSituacaoCota())){
							cotas.get(0).setSituacaoCadastro(SituacaoCadastro.ATIVO);
							
						} else if ("3".equals(input.getSituacaoCota())){
							cotas.get(0).setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
							
						} else if ("4".equals(input.getSituacaoCota())){
							cotas.get(0).setSituacaoCadastro(SituacaoCadastro.INATIVO);
						}
					
					cotas.get(0).setSugereSuspensao(true);
					cotas.get(0).setBox(box);
					cotas.get(0).setPessoa(pessoa);
					cotas.get(0).setQtdePDV(input.getQtdeCotas());
					
					if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())){
						
						// Verifica EnderecoCota
						sql = new StringBuilder();
						sql.append("SELECT ec  ");
						sql.append("FROM EnderecoCota ec ");
						sql.append("WHERE ");
						sql.append("     ec.cota = :numeroCota ");
						
						query = entityManager.createQuery(sql.toString());
						query.setParameter("numeroCota", cotas.get(0));
						
						List<EnderecoCota> enderecosCota = (List<EnderecoCota>) query.getResultList();
						
							if (enderecosCota.isEmpty()) {
								
								EnderecoCota enderecoCota = new EnderecoCota();
								
								enderecoCota.setPrincipal(true);
								enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
								enderecoCota.setEndereco(endereco);
								enderecoCota.setCota(cotas.get(0));
								entityManager.persist(enderecoCota);
								
							} else {
													
								enderecosCota.get(0).setPrincipal(true);
								enderecosCota.get(0).setTipoEndereco(TipoEndereco.COMERCIAL);
								enderecosCota.get(0).setEndereco(endereco);
								enderecosCota.get(0).setCota(cotas.get(0));
							}
					}
					
						
					if (!input.getTelefone().isEmpty()){
						
						// Verifica TelefoneCota
						sql = new StringBuilder();
						sql.append("SELECT tc  ");
						sql.append("FROM TelefoneCota tc ");
						sql.append("WHERE ");
						sql.append("     tc.cota = :numeroCota ");
						
						query = entityManager.createQuery(sql.toString());
						query.setParameter("numeroCota", cotas.get(0));
						
						List<TelefoneCota> telefonesCota = (List<TelefoneCota>) query.getResultList();
						
							if (telefonesCota.isEmpty()) {
								
								TelefoneCota telefoneCota = new TelefoneCota();
								
								telefoneCota.setPrincipal(true);
								telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
								telefoneCota.setTelefone(telefone);
								telefoneCota.setCota(cotas.get(0));
								entityManager.persist(telefoneCota);
								
							} else {
								
								telefonesCota.get(0).setPrincipal(true);
								telefonesCota.get(0).setTipoTelefone(TipoTelefone.COMERCIAL);
								telefonesCota.get(0).setTelefone(telefone);
								telefonesCota.get(0).setCota(cotas.get(0));
							}
					}				
				}				
		
		} catch (NoResultException e) {
			// Logar erro e passar para a próxima linha
			ndsiLoggerFactory.getLogger().logError(message, "Erro na execucao da interface de insercao de COTA - EMS0117");
			e.printStackTrace();
		}
	}
}