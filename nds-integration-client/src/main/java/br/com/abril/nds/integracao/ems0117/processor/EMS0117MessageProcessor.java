package br.com.abril.nds.integracao.ems0117.processor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0117.inbound.EMS0117Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.TipoEdicao;
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
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0117MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

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
		Query query = null;
		
		Pessoa pessoa = null;
		Cota cota = null;

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

			incluirCota(message, input, pessoa);

		} else {

			atualizarCota(message, input, pessoa, cota, cotas);
		}
	}

	@SuppressWarnings("unchecked")
	private void atualizarCota(Message message, EMS0117Input input,
			Pessoa pessoa, Cota cota, List<Cota> cotas) {
		
		StringBuilder sql;
		Query query;
		Telefone telefone = null;
		TelefoneCota telefoneCota = null;

		for (Cota cota2 : cotas) {

			if (cota2.getNumeroCota().equals(input.getCodCota())) {

				cota = cota2;
			}
		}

		ndsiLoggerFactory.getLogger().logInfo(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"Atualização da Cota " + cota.getNumeroCota());

		cota.setNumeroCota(input.getCodCota());
		cota.setPossuiContrato(false);
		
		/*
		 * Conforme conversado com a Kaina, não serão mais atualizados os Status desta interface.
		 * 
		 * setSituacaoCadastro(input, cota);
		 * 
		*/
		
		/* NAO SERA ALTERADO O TIPO DE COTA NESSA INTERFACE
		if (input.getCondPrazoPagamento().equals("S")) {				
			cota.setTipoCota(TipoCota.CONSIGNADO);
		} else {
			cota.setTipoCota(TipoCota.A_VISTA);
		}
		*/

		cota.setSugereSuspensao(true);			
		cota.setPessoa(pessoa);

		if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())) {
		
			if (cota.getEnderecos().isEmpty()) {

				persistirEnderecoCotaSemEndereco(input, cota);

			} else {

				atualizarEnderecoExistenteCota(input, cota, message);

			}
			
		} else {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"O Arquivo não Contém dados de Endereço para a Cota "
							+ cota.getNumeroCota());
		}

		if (!input.getTelefone().isEmpty()) {

			// Verifica TelefoneCota
			sql = new StringBuilder();
			sql.append("SELECT tc  ");
			sql.append("FROM TelefoneCota tc ");
			sql.append("JOIN FETCH tc.telefone t  ");
			sql.append("WHERE tc.cota = :numeroCota ");
			sql.append("AND t.numero = :numeroTelefone ");
			query = getSession().createQuery(sql.toString());
			query.setParameter("numeroCota", cota);
			query.setParameter("numeroTelefone", input.getTelefone());

			List<TelefoneCota> telefonesCota = (List<TelefoneCota>) query.list();

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
						sql.append("WHERE tel.numero = :numero ");
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
						"Atualização do Telefone Cota "
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
					"O Arquivo não Contém dados de Telefone para a Cota "
							+ cota.getNumeroCota());
		}
	}

	private void incluirCota(Message message, EMS0117Input input, Pessoa pessoa) {
		Cota cota;
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		cota = new Cota();
		
		cota.setInicioAtividade(dataOperacao);
		cota.setNumeroCota(input.getCodCota());
		cota.setPossuiContrato(false);
					
		setSituacaoCadastro(input, cota);
		
		if (input.getCondPrazoPagamento().equals("S")) {				
			cota.setTipoCota(TipoCota.CONSIGNADO);
		} else {
			cota.setTipoCota(TipoCota.A_VISTA);
		}

		cota.setSugereSuspensao(true);
		//cota.setBox(box);
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
		
		Usuario usuarioResponsavel = this.usuarioRepository.buscarPorId(1L);
		historicoSituacaoCota.setResponsavel(usuarioResponsavel);
		
		getSession().persist(historicoSituacaoCota);
		
		
		// ParametroCobrancaCota - Realizado em conjunto com Cesar Pop Punk
		ParametroCobrancaCota parametroCobrancaCota = new ParametroCobrancaCota();
		cota.setParametroCobranca(parametroCobrancaCota);
		
		getSession().merge(cota);
		getSession().persist(parametroCobrancaCota);
		
		if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())) {
			
			Endereco endereco = null;

			endereco = new Endereco();
			endereco.setCep(input.getCep());
			endereco.setCidade(input.getMunicipio());
			endereco.setLogradouro(input.getEndereco());
			endereco.setUf(input.getSiglaUF());
			endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
			Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
			
			if (null != endTmp) {
				endereco.setBairro(endTmp.getBairro());
				endereco.setTipoLogradouro(endTmp.getTipoLogradouro());
			}

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
					"O Arquivo não Contém dados de Endereço para a Cota "
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
					"O Arquivo nao Contém dados de Telefone para a Cota "
							+ cota.getNumeroCota());
		}
	}

	
	@SuppressWarnings("unchecked")
	private void atualizarEnderecoExistenteCota(EMS0117Input input, Cota cota, Message message) {
		
		StringBuilder sql;
		
		Query query;
		
		// Verifica EnderecoCota
		sql = new StringBuilder();
		sql.append("SELECT ec ");
		sql.append("FROM EnderecoCota ec ");
		sql.append("JOIN FETCH ec.endereco e ");
		sql.append("WHERE ec.cota = :numeroCota ");

		query = getSession().createQuery(sql.toString());
		
		query.setParameter("numeroCota", cota);
		
		List<Endereco> enderecos = (List<Endereco>) query.list();

		if (enderecos.isEmpty()) {

			persistirEnderecoCotaSemEndereco(input, cota);
			
		} else {
			
			long idEndPrincipal = 0;
			
			String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim()).toUpperCase();
			boolean persistiuEndereco = false;
			
			for (EnderecoCota ec : cota.getEnderecos()) {
				
				if(ec.getEndereco() != null && ec.getEndereco().getLogradouro() == null || 
						(ec.getEndereco().getLogradouro() != null && "".equalsIgnoreCase(ec.getEndereco().getLogradouro().trim()))) {
					
					getSession().delete(ec);
					getSession().flush();
					getSession().clear();
					
					continue;
				}
				
				boolean objEndecoNDS_MDC_igual = compararObjEnderecoMdcNds(ec.getEndereco(), input);
				
				//Verifica alteracoes no mesmo endereco da cota
				if(ec.getEndereco() != null && ec.getEndereco().getLogradouro() != null && ec.getEndereco().getLogradouro().equals(logradouro)) {
					
					if(!objEndecoNDS_MDC_igual){
						
						idEndPrincipal = ec.getId();
						
						ec.setPrincipal(true);
						ec.getEndereco().setCep(input.getCep());
						ec.getEndereco().setCidade(input.getMunicipio() != null ? input.getMunicipio().toUpperCase() : input.getMunicipio());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
						ec.getEndereco().setLogradouro(logradouro != null ? logradouro.toUpperCase() : logradouro);
						ec.getEndereco().setUf(input.getSiglaUF());
						ec.getEndereco().setCodigoCidadeIBGE(input.getCodCidadeIbge());
						Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
						
						if (null != endTmp) {
							ec.getEndereco().setBairro(endTmp.getBairro() != null ? endTmp.getBairro().toUpperCase() : endTmp.getBairro());
							ec.getEndereco().setTipoLogradouro(endTmp.getTipoLogradouro() != null ? endTmp.getTipoLogradouro().toUpperCase() : endTmp.getTipoLogradouro());
						}
						
						try {
							ec.getEndereco().setNumero(Integer.valueOf(input.getNumLogradouro()).toString());
						} catch (Exception e) {
							ec.getEndereco().setNumero(input.getNumLogradouro());
						}
						
						getSession().merge(ec);
						
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.RELACIONAMENTO,
								"Endereço atualizado para Cota " + cota.getNumeroCota() + " / Logradouro: "+ logradouro +", "+ input.getNumLogradouro());
						
						persistiuEndereco = true;
					}
					
					continue;
				}else {
					
					//Compara se é principal e se o endereco do NDS é igual ao do MDC
					if(ec.isPrincipal() && !objEndecoNDS_MDC_igual){
						
						// Há um novo endereço principal, deletar e persistir o novo do MDC
						getSession().delete(ec);
						getSession().flush();
						getSession().clear();
						
						persistirEnderecoCotaSemEndereco(input, cota);
						
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.RELACIONAMENTO,
								"Endereço atualizado para Cota " + cota.getNumeroCota() + " / Logradouro: "+ logradouro +", "+ input.getNumLogradouro());
						
						persistiuEndereco = true;
						
						continue;
					}
				}
				
			}
			
			if(!persistiuEndereco){
				
				enderecos = (List<Endereco>) query.list();
				if (enderecos.isEmpty()) {
					persistirEnderecoCotaSemEndereco(input, cota);
				}
			}
			
			// Ajusta os enderecos para existir apenas 1 como principal
			for (EnderecoCota ec : cota.getEnderecos()) {
				
				if(idEndPrincipal > 0 && ec.isPrincipal() && ec.getId() != idEndPrincipal) {
					ec.setPrincipal(false);
				}
								
			}
			
		}
		
	}

	private EnderecoCota persistirEnderecoCotaSemEndereco(EMS0117Input input, Cota cota) {
		
		Endereco endereco;
		EnderecoCota enderecoCota;
		String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim());
		
		endereco = new Endereco();
		endereco.setCep(input.getCep());
		endereco.setCidade(input.getMunicipio() != null ? input.getMunicipio().toUpperCase() : input.getMunicipio());
		endereco.setLogradouro(logradouro != null ? logradouro.toUpperCase() : logradouro);
		endereco.setUf(input.getSiglaUF());
		endereco.setCodigoCidadeIBGE(input.getCodCidadeIbge());
		Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
		
		if (null != endTmp) {
			endereco.setBairro(endTmp.getBairro() != null ? endTmp.getBairro().toUpperCase() : endTmp.getBairro());
			endereco.setTipoLogradouro(endTmp.getTipoLogradouro() != null ? endTmp.getTipoLogradouro().toUpperCase() : endTmp.getTipoLogradouro());
		} else {
			endereco.setTipoLogradouro(getTipoLogradouro(input.getEndereco()));
		}

		try {
			endereco.setNumero(Integer.toString(Integer.parseInt(input.getNumLogradouro())));
		} catch (Exception e) {
			endereco.setNumero(input.getNumLogradouro());
		}
		
		getSession().persist(endereco);

		enderecoCota = new EnderecoCota();
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COMERCIAL);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setCota(cota);
		
		getSession().persist(enderecoCota);
		
		return enderecoCota;
	}

	private void setSituacaoCadastro(EMS0117Input input, Cota cota) {
		if ("1".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.ATIVO);

		} else if ("2".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);

		} else if ("3".equals(input.getSituacaoCota()) || "5".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);

		} else if ("4".equals(input.getSituacaoCota())) {
			cota.setSituacaoCadastro(SituacaoCadastro.INATIVO);
		}
	}
	
	private String getLogradouroSemTipo(String logradouro) {
		
		String rua = "RUA";
		if (logradouro.startsWith(rua))
			return logradouro.substring(rua.length()).trim();

		String avenida = "AV. ";
		if (logradouro.startsWith(avenida))
			return logradouro.substring(avenida.length()).trim();
		
		String estrada = "ES. ";
		if (logradouro.startsWith(estrada))
			return logradouro.substring(estrada.length()).trim();
		
		String alameda = "AL. ";
		if (logradouro.startsWith(alameda))
			return logradouro.substring(alameda.length()).trim();
		
		String praca = "PR. ";
		if (logradouro.startsWith(praca))
			return logradouro.substring(praca.length()).trim();
		
		String rodovia = "RO. ";
		if (logradouro.startsWith(rodovia))
			return logradouro.substring(rodovia.length()).trim();
		
		String lagoa = "LA. ";
		if (logradouro.startsWith(lagoa))
			return logradouro.substring(lagoa.length()).trim();
		
		String jardins = "JA. ";
		if (logradouro.startsWith(jardins))
			return logradouro.substring(jardins.length()).trim();
		return logradouro;
	}
	
	private String getTipoLogradouro(String logradouro) {
		
		String rua = "RUA";
		if (logradouro.startsWith(rua))
			return logradouro.substring(0, rua.length()).trim();

		String avenida = "AV. ";
		if (logradouro.startsWith(avenida))
			return logradouro.substring(0, avenida.length()).trim();
		
		String estrada = "ES. ";
		if (logradouro.startsWith(estrada))
			return logradouro.substring(0, estrada.length()).trim();
		
		String alameda = "AL. ";
		if (logradouro.startsWith(alameda))
			return logradouro.substring(0, alameda.length()).trim();
		
		String praca = "PR. ";
		if (logradouro.startsWith(praca))
			return logradouro.substring(0, praca.length()).trim();
		
		String rodovia = "RO. ";
		if (logradouro.startsWith(rodovia))
			return logradouro.substring(0, rodovia.length()).trim();
		
		String lagoa = "LA. ";
		if (logradouro.startsWith(lagoa))
			return logradouro.substring(0, lagoa.length()).trim();
		
		String jardins = "JA. ";
		if (logradouro.startsWith(jardins))
			return logradouro.substring(0, jardins.length()).trim();
		return logradouro;
	}

private boolean compararObjEnderecoMdcNds(Endereco endereco, EMS0117Input input){
		
		if(endereco == null && input == null ){
			return true;
		}
		
		if(endereco == null ^ input == null ){
			return false;
		}
		
		if (input.getCep() == null) {
			if (endereco.getCep() != null)
				return false;
		} else if (!input.getCep().equals(endereco.getCep()))
			return false;
		if (input.getMunicipio() == null) {
			if (endereco.getCidade() != null)
				return false;
		} else if (!input.getMunicipio().equals(endereco.getCidade()))
			return false;
		if (input.getCodCidadeIbge() == null) {
			if (endereco.getCodigoCidadeIBGE() != null)
				return false;
		} else if (!input.getCodCidadeIbge().equals(endereco.getCodigoCidadeIBGE()))
			return false;
		if (input.getEndereco() == null) {
			if (endereco.getLogradouro() != null)
				return false;
		} else if (!getLogradouroSemTipo(input.getEndereco().split(",")[0].trim()).toUpperCase().equalsIgnoreCase(endereco.getLogradouro()))
			return false;
		
		String numeroMDC = "";
		try {
			numeroMDC = Integer.valueOf(input.getNumLogradouro()).toString();
		} catch (Exception e) {
			//Numero com carcater não numérico
			numeroMDC = input.getNumLogradouro();
		}
		
		if (numeroMDC == null) {
			if (endereco.getNumero() != null)
				return false;
		} else if (!numeroMDC.equals(endereco.getNumero()))
			return false;
		
		Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
		
		if (null != endTmp) {
			
			String tipoEndereco = endTmp.getTipoLogradouro() != null ? endTmp.getTipoLogradouro().toUpperCase() : endTmp.getTipoLogradouro();
			
			if (tipoEndereco == null) {
				if (endereco.getTipoLogradouro() != null)
					return false;
			} else if (!tipoEndereco.equals(endereco.getTipoLogradouro()))
				return false;

			
			String bairro = endTmp.getBairro() != null ? endTmp.getBairro().toUpperCase() : endTmp.getBairro();

			if (bairro == null) {
				if (endereco.getBairro() != null)
					return false;
			} else if (!bairro.equals(endereco.getBairro()))
				return false;
			
		}
		
		if (input.getSiglaUF() == null) {
			if (endereco.getUf() != null)
				return false;
		} else if (!input.getSiglaUF().equals(endereco.getUf()))
			return false;
		
		return true;
	}
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}