package br.com.abril.nds.integracao.ems0116.processor;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0116.inbound.EMS0116Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0116MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message) {
		EMS0116Input input = (EMS0116Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		// Verifica cota
		sql.append("SELECT co  ");
		sql.append("FROM Cota co ");
		sql.append("JOIN FETCH co.pessoa ");
		sql.append("WHERE ");
		sql.append("     co.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("numeroCota", input.getCodCota());
		Cota cota = null;

		cota = (Cota) query.uniqueResult();
		if (null == cota) {
			// Não encontrou a Cota. Realizar Log
			// Passar para a próxima linha
			ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.HIERARQUIA,
					"Codigo Cota " + input.getCodCota() + " nao encontrado.");
			return;
		}

		// Obter PDV
		sql = new StringBuilder();
		PDV pdv = null;

		sql.append("SELECT p  ");
		sql.append("FROM PDV p ");
		sql.append("WHERE ");
		sql.append("     p.cota = :cota ");

		query = getSession().createQuery(sql.toString());
		query.setParameter("cota", cota);

		List<PDV> pdvs = (List<PDV>) query.list();

		if (pdvs.isEmpty()) {

			SegmentacaoPDV segmentacaoPDV = null;
			CaracteristicasPDV caracteristicasPDV = new CaracteristicasPDV();
			pdv = new PDV();

			pdv.setDentroOutroEstabelecimento(false);
			pdv.setNome(cota.getPessoa().getNome());
			pdv.setPontoReferencia(input.getPontoReferencia());
			pdv.setPorcentagemFaturamento(new BigDecimal(0));
			pdv.setPossuiSistemaIPV(false);
			pdv.setQtdeFuncionarios(0);
			pdv.setStatus(StatusPDV.ATIVO);
			pdv.setCota(cota);

			caracteristicasPDV.setPontoPrincipal(true);
			pdv.setCaracteristicas(caracteristicasPDV);

			// Obter Tipo PDV
			sql = new StringBuilder();
			sql.append("SELECT tp  ");
			sql.append("FROM TipoPontoPDV tp ");
			sql.append("WHERE ");
			sql.append("     tp.codigo = :codigo ");

			query = getSession().createQuery(sql.toString());
			query.setParameter("codigo", input.getTipoPontoVenda());

			List<TipoPontoPDV> tpdvs = (List<TipoPontoPDV>) query.list();

			if (tpdvs.isEmpty()) {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.SEM_DOMINIO,
						"Tipo Ponto PDV " + input.getTipoPontoVenda()
								+ " nao encontrado.");

			} else {

				segmentacaoPDV = new SegmentacaoPDV();
				segmentacaoPDV.setAreaInfluenciaPDV(null);
				segmentacaoPDV.setTipoCaracteristica(null);
				segmentacaoPDV.setTipoClusterPDV(null);
				segmentacaoPDV.setTipoPontoPDV(tpdvs.get(0));

				pdv.setSegmentacao(segmentacaoPDV);

			}

			getSession().persist(pdv);

			if (!input.getEndereco().isEmpty()
					&& !".".equals(input.getEndereco())) {

				Endereco endereco = new Endereco();
				endereco.setCodigoBairro(input.getCodBairro());
				endereco.setCep(input.getCep());
				endereco.setCidade(input.getNomeMunicipio());
				endereco.setLogradouro(input.getEndereco());
				endereco.setNumero(null);
				endereco.setUf(input.getSiglaUF());
				getSession().persist(endereco);

				EnderecoPDV enderecoPDV = new EnderecoPDV();
				enderecoPDV.setEndereco(endereco);
				enderecoPDV.setPdv(pdv);
				enderecoPDV.setPrincipal(true);
				enderecoPDV.setTipoEndereco(TipoEndereco.COMERCIAL);
				getSession().persist(enderecoPDV);
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

				TelefonePDV telefonePDV = new TelefonePDV();
				telefonePDV.setTelefone(telefone);
				telefonePDV.setPdv(pdv);
				telefonePDV.setPrincipal(true);
				telefonePDV.setTipoTelefone(TipoTelefone.COMERCIAL);
				getSession().persist(telefonePDV);
			} else {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"O arquivo nao contem dados de telefone para a cota "
								+ cota.getNumeroCota());
			}

		} else {

			Endereco endereco = null;
			EnderecoPDV enderecoPDV = null;
			Telefone telefone = null;
			TelefonePDV telefonePDV = null;

			for (PDV pdvs2 : pdvs) {

				if (pdvs2.getCota().equals(cota)) {

					pdv = pdvs2;
				}
			}

			pdv.setNome(cota.getPessoa().getNome());
			pdv.setPontoReferencia(input.getPontoReferencia());
			pdv.setCota(cota);

			// Obter Tipo PDV
			sql = new StringBuilder();
			sql.append("SELECT tp  ");
			sql.append("FROM TipoPontoPDV tp ");
			sql.append("WHERE ");
			sql.append("     tp.codigo = :codigo ");

			query = getSession().createQuery(sql.toString());
			query.setParameter("codigo", input.getTipoPontoVenda());

			List<TipoPontoPDV> tpdvs = (List<TipoPontoPDV>) query.list();

			if (tpdvs.isEmpty()) {

				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.SEM_DOMINIO,
						"Tipo Ponto PDV " + input.getTipoPontoVenda()
								+ " nao encontrado.");

			} else {

				for (TipoPontoPDV tpdvs2 : tpdvs) {

					if (tpdvs2.getCodigo().equals(input.getTipoPontoVenda())) {

						pdv.getSegmentacao().setTipoPontoPDV(tpdvs2);
					}
				}
			}

			ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da PDV " + pdv.getId());

			if (!input.getEndereco().isEmpty()
					&& !".".equals(input.getEndereco())) {

				// Verifica EnderecoCota
				sql = new StringBuilder();
				sql.append("SELECT ep  ");
				sql.append("FROM EnderecoPDV ep ");
				sql.append("JOIN FETCH ep.endereco ed  ");
				sql.append("WHERE ");
				sql.append("     ep.pdv = :pdv ");
				sql.append(" AND    ed.logradouro = :logradouro ");

				query = getSession().createQuery(sql.toString());
				query.setParameter("pdv", pdv);
				query.setParameter("logradouro", input.getEndereco());

				List<EnderecoPDV> enderecosPDV = (List<EnderecoPDV>) query
						.list();

				if (enderecosPDV.isEmpty()) {

					endereco = new Endereco();
					endereco.setCodigoBairro(input.getCodBairro());
					endereco.setCep(input.getCep());
					endereco.setCidade(input.getNomeMunicipio());
					endereco.setLogradouro(input.getEndereco());
					endereco.setNumero(null);
					endereco.setUf(input.getSiglaUF());
					getSession().persist(endereco);

					enderecoPDV = new EnderecoPDV();
					enderecoPDV.setEndereco(endereco);
					enderecoPDV.setPdv(pdv);
					enderecoPDV.setPrincipal(true);
					enderecoPDV.setTipoEndereco(TipoEndereco.COMERCIAL);
					getSession().persist(enderecoPDV);

				} else {

					for (EnderecoPDV enderecosPDV2 : enderecosPDV) {

						if (enderecosPDV2.getPdv().equals(pdv)) {

							enderecoPDV = enderecosPDV2;

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

								endereco = new Endereco();
								endereco.setCodigoBairro(input.getCodBairro());
								endereco.setCep(input.getCep());
								endereco.setCidade(input.getNomeMunicipio());
								endereco.setLogradouro(input.getEndereco());
								endereco.setNumero(null);
								endereco.setUf(input.getSiglaUF());
								getSession().persist(endereco);

							} else {

								for (Endereco enderecos2 : enderecos) {

									if (enderecos2.getLogradouro().equals(
											input.getEndereco())) {

										endereco = enderecos2;
									}
								}
							}
						}
					}

					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do  Endereco PDV "
									+ enderecoPDV.getId());

					enderecoPDV.setEndereco(endereco);
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
				sql.append("FROM TelefonePDV tc ");
				sql.append("JOIN FETCH tc.telefone t  ");
				sql.append("WHERE ");
				sql.append("     tc.pdv = :pdv ");
				sql.append(" AND    t.numero = :numeroTelefone ");

				query = getSession().createQuery(sql.toString());
				query.setParameter("pdv", pdv);
				query.setParameter("numeroTelefone", input.getTelefone());

				List<TelefonePDV> telefonesPDV = (List<TelefonePDV>) query
						.list();

				if (telefonesPDV.isEmpty()) {

					telefone = new Telefone();
					telefone.setDdd(input.getDdd());
					telefone.setNumero(input.getTelefone());
					getSession().persist(telefone);

					telefonePDV = new TelefonePDV();
					telefonePDV.setTelefone(telefone);
					telefonePDV.setPdv(pdvs.get(0));
					telefonePDV.setPrincipal(true);
					telefonePDV.setTipoTelefone(TipoTelefone.COMERCIAL);
					getSession().persist(telefonePDV);

				} else {

					for (TelefonePDV telefonesPDV2 : telefonesPDV) {

						if (telefonesPDV2.getPdv().equals(pdv)) {

							telefonePDV = telefonesPDV2;

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
							"Atualizacao do Telefone PDV "
									+ telefonePDV.getId());

					telefonePDV.setTelefone(telefone);
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
	
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void posProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
}