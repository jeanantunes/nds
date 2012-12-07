package br.com.abril.nds.integracao.ems0117.processor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0117.inbound.EMS0117Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0117MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	private static final String INDICE_PESSOA_JURIDICA = "J";
	private static final String INDICE_PESSOA_FISICA = "F";

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message) {
		EMS0117Input input = (EMS0117Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		// Obter Box
		sql.append("FROM Box b ");
		sql.append("WHERE ");
		sql.append("     b.codigo = :codigo ");
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigo", Integer.valueOf( input.getCodBox().toString() ));
		Box box = (Box) query.uniqueResult();
		if (null == box) {
			// Não encontrou a Box. Realizar Log
			// Passar para a próxima linha
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					"Codigo BOX " + input.getCodBox().toString()
							+ " nao encontrado para a Cota "
							+ input.getCodCota().toString());
			return;
		}

		Pessoa pessoa = null;
		Cota cota = null;
		sql = new StringBuilder();

		// Obter Pessoa
		if (INDICE_PESSOA_FISICA.equals(input.getTipoPessoa())) {

			sql.append("SELECT pe ");
			sql.append("FROM PessoaFisica pe ");
			sql.append("WHERE ");
			sql.append("	pe.cpf = :cpf ");

		} else if (INDICE_PESSOA_JURIDICA.equals(input.getTipoPessoa())) {

			sql.append("SELECT pe ");
			sql.append("FROM PessoaJuridica pe ");
			sql.append("WHERE ");
			sql.append("	pe.cnpj = :cnpj ");
		}

		query = getSession().createQuery(sql.toString());

		if (INDICE_PESSOA_FISICA.equals(input.getTipoPessoa())) {

			query.setParameter("cpf", input.getCpf());

		} else if (INDICE_PESSOA_JURIDICA.equals(input.getTipoPessoa())) {

			query.setParameter("cnpj", input.getCnpj());
		}

		// Definir Pessoa
		if (INDICE_PESSOA_FISICA.equals(input.getTipoPessoa())) {

			List<PessoaFisica> pessoas = (List<PessoaFisica>) query.list();
			PessoaFisica pessoaFis = null;

			if (pessoas.isEmpty()) {

				pessoaFis = new PessoaFisica();
				pessoaFis.setNome(input.getNomeJornaleiro());
				pessoaFis.setCpf(input.getCpf());
				getSession().persist(pessoaFis);

				pessoa = pessoaFis;

			} else {
//Não Precisa iterar, isso eh uma busca por uma unique 
				for (PessoaFisica pessoaFis2 : pessoas) {

					if (pessoaFis2.getCpf().equals(input.getCpf())) {

						pessoaFis = pessoaFis2;
					}
				}

				pessoaFis.setCpf(input.getCpf());
				pessoaFis.setNome(input.getNomeJornaleiro());
				pessoa = pessoaFis;
			}

		} else if (INDICE_PESSOA_JURIDICA.equals(input.getTipoPessoa())) {

			List<PessoaJuridica> pessoas = (List<PessoaJuridica>) query.list();
			PessoaJuridica pessoaJur = null;

			if (pessoas.isEmpty()) {

				pessoaJur = new PessoaJuridica();
				pessoaJur.setRazaoSocial(input.getNomeJornaleiro());
				pessoaJur.setCnpj(input.getCnpj());
				pessoaJur.setInscricaoEstadual(input.getInscrEstadual());
				pessoaJur.setInscricaoMunicipal(input.getInscrMunicipal());
				getSession().persist(pessoaJur);

				pessoa = pessoaJur;

			} else {

				for (PessoaJuridica pessoaJur2 : pessoas) {

					if (pessoaJur2.getCnpj().equals(input.getCnpj())) {

						pessoaJur = pessoaJur2;
					}
				}

				pessoaJur.setRazaoSocial(input.getNomeJornaleiro());
				pessoaJur.setCnpj(input.getCnpj());
				pessoaJur.setInscricaoEstadual(input.getInscrEstadual());
				pessoaJur.setInscricaoMunicipal(input.getInscrMunicipal());
				pessoa = pessoaJur;

			}
		}

		// Verifica cota
		sql = new StringBuilder();
		sql.append("SELECT co  ");
		sql.append("FROM Cota co ");
		sql.append("WHERE ");
		sql.append("     co.numeroCota = :numeroCota ");

		query = getSession().createQuery(sql.toString());
		query.setParameter("numeroCota", input.getCodCota());

		List<Cota> cotas = (List<Cota>) query.list();

		if (cotas.isEmpty()) {

			cota = new Cota();

			cota.setInicioAtividade(new Date());
			cota.setNumeroCota(input.getCodCota());
			cota.setPossuiContrato(false);
						
			setSituacaoCadastro(input, cota);

			cota.setSugereSuspensao(true);
			cota.setBox(box);
			cota.setPessoa(pessoa);
			getSession().persist(cota);

			// HistoricoSituacaoCota - Realizado em conjunto com Cesar Pop Punk
			HistoricoSituacaoCota historicoSituacaoCota = new HistoricoSituacaoCota();
			historicoSituacaoCota.setCota(cota);
			historicoSituacaoCota.setSituacaoAnterior(null);
			historicoSituacaoCota.setNovaSituacao(cota.getSituacaoCadastro());
			historicoSituacaoCota.setMotivo(MotivoAlteracaoSituacao.OUTROS);
			historicoSituacaoCota.setDataInicioValidade(new Date());
			historicoSituacaoCota.setDataFimValidade(null);
			historicoSituacaoCota.setDescricao("INTERFACE");
			historicoSituacaoCota.setDataEdicao(new Date());
			historicoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
			
			Usuario usuarioResponsavel = new Usuario();
			usuarioResponsavel.setId(2L);
			historicoSituacaoCota.setResponsavel(usuarioResponsavel);
			
			getSession().persist(historicoSituacaoCota);
			
			// ParametroCobrancaCota - Realizado em conjunto com Cesar Pop Punk
			ParametroCobrancaCota parametroCobrancaCota = new ParametroCobrancaCota();
			parametroCobrancaCota.setCota(cota);
			if (input.getCondPrazoPagamento().equals("S")) {
				parametroCobrancaCota.setTipoCota(TipoCota.CONSIGNADO);
			} else {
				parametroCobrancaCota.setTipoCota(TipoCota.A_VISTA);
			}
			getSession().persist(parametroCobrancaCota);
			
			if (!input.getEndereco().isEmpty()
					&& !".".equals(input.getEndereco())) {
				Endereco endereco = null;
//				Endereco endereco = getEnderecoSaneado(input.getCep());
//				if (null == endereco ) {
					endereco = new Endereco();
					endereco.setCep(input.getCep());
					endereco.setCidade(input.getMunicipio());
					endereco.setLogradouro(input.getEndereco());
					endereco.setUf(input.getSiglaUF());
					endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
					Endereco endTmp = getEnderecoSaneado(input.getCep());
					if (null != endTmp) {
						endereco.setBairro(endTmp.getBairro());
						endereco.setTipoLogradouro(endTmp.getTipoLogradouro());
					}
//				}
				endereco.setNumero(input.getNumLogradouro());
				getSession().persist(endereco);

				EnderecoCota enderecoCota = new EnderecoCota();
				enderecoCota.setPrincipal(true);
				enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
				enderecoCota.setEndereco(endereco);
				enderecoCota.setCota(cota);
				getSession().persist(enderecoCota);

			} else {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"O arquivo nao contem dados de endereco para a cota "
								+ cota.getNumeroCota());
			}

			if (!input.getTelefone().isEmpty()) {

				Telefone telefone = new Telefone();
				telefone.setDdd(input.getDdd());
				telefone.setNumero(input.getTelefone());
				getSession().persist(telefone);

				TelefoneCota telefoneCota = new TelefoneCota();
				telefoneCota.setPrincipal(true);
				telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
				telefoneCota.setTelefone(telefone);
				telefoneCota.setCota(cota);
				getSession().persist(telefoneCota);

			} else {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"O arquivo nao contem dados de telefone para a cota "
								+ cota.getNumeroCota());
			}

		} else {

			Endereco endereco = null;
			EnderecoCota enderecoCota = null;
			Telefone telefone = null;
			TelefoneCota telefoneCota = null;

			for (Cota cota2 : cotas) {

				if (cota2.getNumeroCota().equals(input.getCodCota())) {

					cota = cota2;
				}
			}

			ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Cota " + cota.getNumeroCota());

			cota.setInicioAtividade(new Date());
			cota.setNumeroCota(input.getCodCota());
			cota.setPossuiContrato(false);
			
			setSituacaoCadastro(input, cota);

			cota.setSugereSuspensao(true);
			cota.setBox(box);
			cota.setPessoa(pessoa);

			if (!input.getEndereco().isEmpty()
					&& !".".equals(input.getEndereco())) {
			
				if (cota.getEnderecos().isEmpty()) {

//					endereco = getEnderecoSaneado(input.getCep());
//					if (null == endereco ) {
						endereco = new Endereco();
						endereco.setCep(input.getCep());
						endereco.setCidade(input.getMunicipio());
						endereco.setLogradouro(input.getEndereco());
						endereco.setUf(input.getSiglaUF());
						endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
						Endereco endTmp = getEnderecoSaneado(input.getCep());
						if (null != endTmp) {
							endereco.setBairro(endTmp.getBairro());
							endereco.setTipoLogradouro(endTmp.getTipoLogradouro());
						}						
//					}
					endereco.setNumero(input.getNumLogradouro());
					getSession().persist(endereco);

					enderecoCota = new EnderecoCota();
					enderecoCota.setPrincipal(true);
					enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
					enderecoCota.setEndereco(endereco);
					enderecoCota.setCota(cota);
					getSession().persist(enderecoCota);
					

				} else {

					for (EnderecoCota enderecoCota2 : cota.getEnderecos()) {

						if (enderecoCota2.getCota().equals(cota)) {

							enderecoCota = enderecoCota2;

							// Definir Endereco
							sql = new StringBuilder();
							sql.append("SELECT e  ");
							sql.append("FROM Endereco e ");
							sql.append("WHERE ");
							sql.append("     e.logradouro = :logradouro ");
							query = getSession().createQuery(sql.toString());
							query.setParameter("logradouro",
									input.getEndereco());

							List<Endereco> enderecos = (List<Endereco>) query
									.list();

							if (enderecos.isEmpty()) {

//								endereco = getEnderecoSaneado(input.getCep());
//								if (null == endereco ) {
									endereco = new Endereco();
									endereco.setCep(input.getCep());
									endereco.setCidade(input.getMunicipio());
									endereco.setLogradouro(input.getEndereco());
									endereco.setUf(input.getSiglaUF());
									endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
									Endereco endTmp = getEnderecoSaneado(input.getCep());
									if (null != endTmp) {
										endereco.setBairro(endTmp.getBairro());
										endereco.setTipoLogradouro(endTmp.getTipoLogradouro());
									}									
//								}
								endereco.setNumero(input.getNumLogradouro());

								getSession().persist(endereco);

							} else {

								for (Endereco endereco2 : enderecos) {

									if (endereco2.getLogradouro().equals(
											input.getEndereco())) {

										endereco = endereco2;
									}
								}
							}
						}
					}

					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do  Endereco Cota "
									+ enderecoCota.getId());

					enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
					enderecoCota.setEndereco(endereco);
					enderecoCota.setCota(cota);

				}
			} else {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"O arquivo nao contem dados de endereco para a cota "
								+ cota.getNumeroCota());
			}

			if (!input.getTelefone().isEmpty()) {

				// Verifica TelefoneCota
				sql = new StringBuilder();
				sql.append("SELECT tc  ");
				sql.append("FROM TelefoneCota tc ");
				sql.append("JOIN FETCH tc.telefone t  ");
				sql.append("WHERE ");
				sql.append("     tc.cota = :numeroCota ");
				sql.append(" AND    t.numero = :numeroTelefone ");
				query = getSession().createQuery(sql.toString());
				query.setParameter("numeroCota", cota);
				query.setParameter("numeroTelefone", input.getTelefone());

				List<TelefoneCota> telefonesCota = (List<TelefoneCota>) query
						.list();

				if (telefonesCota.isEmpty()) {

					telefone = new Telefone();
					telefone.setDdd(input.getDdd());
					telefone.setNumero(input.getTelefone());
					getSession().persist(telefone);
					
					telefoneCota = new TelefoneCota();
					
					telefoneCota.setPrincipal(true);
					telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
					telefoneCota.setTelefone(telefone);
					telefoneCota.setCota(cota);
					getSession().persist(telefoneCota);


				} else {

					for (TelefoneCota telefoneCota2 : telefonesCota) {

						if (telefoneCota2.getCota().equals(cota)) {

							telefoneCota = telefoneCota2;

							// Definir Telefone
							sql = new StringBuilder();
							sql.append("SELECT tel ");
							sql.append("FROM Telefone tel ");
							sql.append("WHERE ");
							sql.append("     tel.numero = :numero ");
							query = getSession().createQuery(sql.toString());
							query.setParameter("numero", input.getTelefone());

							List<Telefone> telefones = (List<Telefone>) query
									.list();

							if (telefones.isEmpty()) {

								telefone = new Telefone();
								telefone.setDdd(input.getDdd());
								telefone.setNumero(input.getTelefone());
								getSession().persist(telefone);

							} else {

								for (Telefone telefones2 : telefones) {

									if (telefones2.getNumero().equals(
											input.getTelefone())) {

										telefone = telefones2;
									}
								}
							}
						}
					}

					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Telefone Cota "
									+ telefoneCota.getId());


					// Alteração para falso no principal, todos os dados de importacao se tornavam telefones principais Eduardo "PunkRock" Castro
					telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
					telefoneCota.setTelefone(telefone);
					telefoneCota.setCota(cota);

				}
			} else {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"O arquivo nao contem dados de telefone para a cota "
								+ cota.getNumeroCota());
			}
		}
	}

	private void setSituacaoCadastro(EMS0117Input input, Cota cota) {
		if ("1".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.ATIVO);

		} else if ("2".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);

		} else if ("3".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);

		} else if ("4".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.INATIVO);
		}
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}