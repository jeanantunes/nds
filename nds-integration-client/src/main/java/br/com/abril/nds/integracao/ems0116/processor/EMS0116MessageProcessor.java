package br.com.abril.nds.integracao.ems0116.processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0116.inbound.EMS0116Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
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
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.EnderecoRepository;

@Component
public class EMS0116MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0116Input input = (EMS0116Input) message.getBody();

		Cota cota = obterCota(input);
		
		if (null == cota) {
			// Não encontrou a Cota. Realizar Log
			// Passar para a próxima linha
			ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.HIERARQUIA,
					"Cota " + input.getCodCota() + " não encontrado.");
			return;
		}
	
		List<PDV> pdvs = obterPDVsCota(cota);

		if (pdvs.isEmpty()) {

			processarNovoPDV(message, input, cota);

		} else {

			processarPDV(message, input, cota);
		}
	}

	/*
	 * Processa os dados de importação de um PDV
	 */
	private void processarPDV(Message message, EMS0116Input input, Cota cota) {
		
		PDV pdvCandidatoAlteracao  = obterPdvCorrenteImportacao(input, cota);
		
		// comentado por cesar pop punk em 26/03/2013 pois quem "manda" no cadastro é o novo distrib e não mais o mdc.
		if(pdvCandidatoAlteracao == null){
			this.processarNovoPDV(message, input, cota);
			return;
		}
		
		pdvCandidatoAlteracao.setNome(cota.getPessoa().getNome());
		pdvCandidatoAlteracao.setPontoReferencia(input.getPontoReferencia());
		pdvCandidatoAlteracao.setCota(cota);

		processarTipoPDV(message, input, pdvCandidatoAlteracao);

		processarEnderecoPDV(message, input, cota, pdvCandidatoAlteracao);

		processarTelefonePDV(message, input, cota, pdvCandidatoAlteracao);
		
		getSession().update(pdvCandidatoAlteracao);
	}

	
	/*
	 * Processa os dados de telefone de um determinado PDV
	 */
	private void processarTelefonePDV(Message message, EMS0116Input input,Cota cota, PDV pdv) {
		
		if (!input.getTelefone().isEmpty()) {

			List<TelefonePDV> telefonesPDV = obterTelefonePDV(input, pdv);

			if (telefonesPDV.isEmpty()) {

				incluirNovoTelefonePDV(input,pdv);

			} else {

				alterarTelefonePDV(message, input, pdv,telefonesPDV);
			}
		} else {

			ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"O arquivo não contém dados de Telefone para a Cota "
							+ cota.getNumeroCota());
		}
	}
	
	/*
	 * Altera os dados do telefone de um determinado PDV
	 */
	private void alterarTelefonePDV(Message message, EMS0116Input input,PDV pdv,List<TelefonePDV> telefonesPDV) {
		
		TelefonePDV telefonePDV = null;
		
		for (TelefonePDV item : telefonesPDV) {

			if(item.getTelefone().getNumero().equals(input.getTelefone())){
				telefonePDV = item;
				break;
			}
		}
		
		if(telefonePDV == null){
			telefonePDV = incluirNovoTelefonePDV(input, pdv);
		}
		else{
			
			telefonePDV.getTelefone().setDdd(input.getDdd());
			getSession().merge(telefonePDV);
		}
		
		ndsiLoggerFactory.getLogger().logInfo(
				message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"Alteração do Telefone PDV "
						+ telefonePDV.getId());
			
	}
	
	/*
	 * Inclui um novo telefone a um PDV
	 */
	private TelefonePDV incluirNovoTelefonePDV(EMS0116Input input, PDV pdv) {
		
		Telefone telefone = new Telefone();
		telefone.setDdd(input.getDdd());
		telefone.setNumero(input.getTelefone());
		
		getSession().persist(telefone);

		TelefonePDV telefonePDV = new TelefonePDV();
		telefonePDV.setTelefone(telefone);
		telefonePDV.setPdv(pdv);
		telefonePDV.setPrincipal(!isTelefonePrincipal(pdv.getTelefones()));
		telefonePDV.setTipoTelefone(TipoTelefone.COMERCIAL);
		
		return (TelefonePDV) getSession().merge(telefonePDV);
	}
	
	/*
	 * Verifica se existe telefone principal associado ao PDV
	 */
	private boolean isTelefonePrincipal(Set<TelefonePDV> telefones){
		
		if(telefones == null){
			return false;
		}
		
		for(TelefonePDV item : telefones){
			if(item.isPrincipal()){
				return item.isPrincipal();
			}
		}
		return false;
	}
	
	/*
	 * Retorna os telefones de um PDV
	 */
	@SuppressWarnings("unchecked")
	private List<TelefonePDV> obterTelefonePDV(EMS0116Input input, PDV pdv) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tc  ");
		sql.append("FROM TelefonePDV tc ");
		sql.append("JOIN FETCH tc.telefone t  ");
		sql.append("WHERE ");
		sql.append("     tc.pdv = :pdv ");
		sql.append(" AND    t.numero = :numeroTelefone ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("pdv", pdv);
		query.setParameter("numeroTelefone", input.getTelefone());
		
		return query.list();
	}
	
	/*
	 * Processa os dados referente aos endereços de um PDV
	 */
	private void processarEnderecoPDV(Message message, EMS0116Input input,Cota cota, PDV pdv) {
		
		if (!input.getEndereco().isEmpty() && !".".equals(input.getEndereco())) {

			List<EnderecoPDV> enderecosPDV = obterEnderecoPDVPorLogradouro(input, pdv);

			if (enderecosPDV.isEmpty()) {
				
				removerPrincipais(input, pdv);
				
				incluirNovoEnderecoPDV(input, pdv);

			} else {

				alterarEnderecoPDV(message, input, pdv,enderecosPDV);
			}

		} else {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"O arquivo não contém dados de endereço para a Cota "
							+ cota.getNumeroCota());
		}
	}
	
	/*
	 * Altera os dados do endereço relacionado ao PDV
	 */
	private void alterarEnderecoPDV(Message message, EMS0116Input input, PDV pdv, List<EnderecoPDV> enderecosPDV) {
		
		EnderecoPDV enderecoPDV = null;

		String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim());
		String numero = input.getEndereco().split(",")[1].trim();
		
		for (EnderecoPDV item : enderecosPDV) {

			if(item.getEndereco().getLogradouro().equalsIgnoreCase(logradouro)
					&& item.getEndereco().getNumero().equals(numero)) {
				
				enderecoPDV = item;
				
				break;
				
			}
		}
		
		if(enderecoPDV == null) {
			
			enderecoPDV = incluirNovoEnderecoPDV(input, pdv);
			
		} else {
			
			Endereco endereco = enderecoPDV.getEndereco();
			
			endereco.setCep(input.getCep());
			endereco.setCidade((input.getNomeMunicipio() != null ? input.getNomeMunicipio().toUpperCase() : input.getNomeMunicipio()));
			endereco.setLogradouro((logradouro != null ? logradouro.toUpperCase() : logradouro));
			try {
				Long l = Long.parseLong(numero);
				endereco.setNumero(l.toString());
			} catch (Exception e) {
				
			}
			
			endereco.setUf(input.getSiglaUF());
			
			Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
			
			if (null != endTmp) {
				endereco.setBairro((endTmp.getBairro() != null ? endTmp.getBairro().toUpperCase() : endTmp.getBairro()));
				endereco.setTipoLogradouro((endTmp.getTipoLogradouro() != null ? endTmp.getTipoLogradouro().toUpperCase() : endTmp.getTipoLogradouro()));
			}
			
			if(!isEnderecoPrincipal(pdv.getEnderecos())) {
				enderecoPDV.setPrincipal(true);
			} else {
				if(!pdv.getEnderecos().contains(enderecoPDV)) {
					enderecoPDV.setPrincipal(false);
				}
			}
			
			getSession().merge(enderecoPDV);
			
			getSession().merge(endereco);
		}
		
		ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Endereco PDV "
									+ enderecoPDV.getId());
			
	}
	
	public void removerPrincipais(EMS0116Input input, PDV pdv) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE EnderecoPDV SET principal = false ");
		sql.append("WHERE ");
		sql.append(" pdv = :pdv ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("pdv", pdv);
		
		query.executeUpdate();
		
	}
	
	/*
	 * Inclui um novo Endereço para o PDV
	 */
	private EnderecoPDV incluirNovoEnderecoPDV(EMS0116Input input, PDV pdv) {
		
		String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim());
		String numero = input.getEndereco().split(",")[1].trim();
		
		Endereco endereco = new Endereco();
		endereco.setCep(input.getCep());
		endereco.setCidade((input.getNomeMunicipio() != null ? input.getNomeMunicipio().toUpperCase() : input.getNomeMunicipio()));
		endereco.setLogradouro((logradouro != null ? logradouro.toUpperCase() : logradouro));
		endereco.setNumero(numero);
		endereco.setUf(input.getSiglaUF());
		Endereco endTmp = enderecoRepository.getEnderecoSaneado(input.getCep());
		
		if (null != endTmp) {
			endereco.setBairro((endTmp.getBairro() != null ? endTmp.getBairro().toUpperCase() : endTmp.getBairro()));
			endereco.setTipoLogradouro((endTmp.getTipoLogradouro() != null ? endTmp.getTipoLogradouro().toUpperCase() : endTmp.getTipoLogradouro()));
		}

		getSession().persist(endereco);

		EnderecoPDV enderecoPDV = new EnderecoPDV();
		enderecoPDV.setEndereco(endereco);
		enderecoPDV.setPdv(pdv);
		//enderecoPDV.setPrincipal(!isEnderecoPrincipal(pdv.getEnderecos()));
		enderecoPDV.setPrincipal(true);
		enderecoPDV.setTipoEndereco(TipoEndereco.COMERCIAL);
		
		return (EnderecoPDV) getSession().merge(enderecoPDV);
	}
	
	/*
	 * Verifica se existe Endereço principal associada a um PDV
	 */
	private boolean isEnderecoPrincipal(Set<EnderecoPDV> enderecos){
		
		if(enderecos == null || enderecos.isEmpty()){
			return false;
		}
		
		for(EnderecoPDV item : enderecos){
			
			if(item.isPrincipal()) {
				return item.isPrincipal();
			}
		}
		
		return false;
	}
	
	/*
	 * Processa os dados do tipo de ponto PDV
	 */
	private void processarTipoPDV(Message message, EMS0116Input input, PDV pdv) {
		
		List<TipoPontoPDV> tpdvs = obterTipoPontoPDV(input);

		if (tpdvs.isEmpty()) {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Ponto PDV " + input.getTipoPontoVenda()
							+ " não encontrado.");

		} else {

			for (TipoPontoPDV tpdvs2 : tpdvs) {

				if (tpdvs2.getCodigo().equals(input.getTipoPontoVenda())) {
					if (null != pdv.getSegmentacao()) {
						pdv.getSegmentacao().setTipoPontoPDV(tpdvs2);
						break;
					}
				}
			}
		}
		
		ndsiLoggerFactory.getLogger().logInfo(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"Alteração da PDV " + pdv.getId());
	}
	
	/*
	 * Processa os dados de um novo PDV
	 */
	private void processarNovoPDV(Message message, EMS0116Input input, Cota cota) {
		
		CaracteristicasPDV caracteristicasPDV = new CaracteristicasPDV(); 
		
		caracteristicasPDV.setPontoPrincipal(!isPdvPrincipal(cota.getPdvs()));
		
		PDV pdv = new PDV();

		pdv.setDentroOutroEstabelecimento(false);
		pdv.setNome(cota.getPessoa().getNome());
		pdv.setPontoReferencia(input.getPontoReferencia());
		pdv.setPorcentagemFaturamento(new BigDecimal(0));
		pdv.setPossuiSistemaIPV(false);
		pdv.setQtdeFuncionarios(0);
		pdv.setStatus(StatusPDV.ATIVO);
		pdv.setCota(cota);
		pdv.setCaracteristicas(caracteristicasPDV);

		List<TipoPontoPDV> tpdvs = obterTipoPontoPDV(input);

		if (tpdvs.isEmpty()) {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Ponto PDV " + input.getTipoPontoVenda()
							+ " não encontrado.");

		} else {

			SegmentacaoPDV segmentacaoPDV = new SegmentacaoPDV();
			segmentacaoPDV.setAreaInfluenciaPDV(null);
			segmentacaoPDV.setTipoCaracteristica(null);
			segmentacaoPDV.setTipoClusterPDV(null);
			segmentacaoPDV.setTipoPontoPDV(tpdvs.get(0));

			pdv.setSegmentacao(segmentacaoPDV);

		}

		getSession().persist(pdv);

		if (!input.getEndereco().isEmpty()
				&& !".".equals(input.getEndereco())) {
			
			incluirNovoEnderecoPDV(input, pdv);
			
		} else {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"O arquivo nao contem dados de endereco para o pdv "
							+ pdv.getId());
		}

		if (!input.getTelefone().isEmpty()) {

			incluirNovoTelefonePDV(input, pdv);
			
		} else {

			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"O arquivo nao contem dados de telefone para o pdv "
							+ pdv.getId());
		}
	}
	
	/*
	 * Retorna o tipo de ponto do PDV
	 */
	@SuppressWarnings("unchecked")
	private List<TipoPontoPDV> obterTipoPontoPDV(EMS0116Input input) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tp  ");
		sql.append("FROM TipoPontoPDV tp ");
		sql.append("WHERE ");
		sql.append("     tp.codigo = :codigo ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigo", input.getTipoPontoVenda());

		return query.list();
	}
	
	/*
	 * Verifica se existe PDV com ponto principal
	 */
	private boolean isPdvPrincipal(List<PDV> pdvs){
		
		if(pdvs == null || pdvs.isEmpty()){
			return false;
		}
		
		for(PDV item : pdvs){
			
			if(item.getCaracteristicas()!= null 
					&& item.getCaracteristicas().isPontoPrincipal()){
				
				return item.getCaracteristicas().isPontoPrincipal();
			}
		}
		
		return false;
	}
	
	/*
	 * Retorna o PDV referente aos dados de importação
	 */
	private PDV obterPdvCorrenteImportacao(EMS0116Input input,Cota cota) {
		
		String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim());
		String numero = input.getEndereco().split(",")[1].trim();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT p  ");
		sql.append(" FROM PDV p join p.enderecos enderecoPDV join enderecoPDV.endereco endereco ");
		sql.append(" WHERE ");
		sql.append(" p.cota = :cota ");
		sql.append(" and endereco.logradouro = :logradouro");
		sql.append(" and endereco.numero = :numero");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("cota", cota);
		query.setParameter("logradouro", logradouro);
		query.setParameter("numero", numero);
		query.setMaxResults(1);
		
		return (PDV) query.uniqueResult();
		
	}
	
	/*
	 * Retorna os PDVs associados a Cota informada no arquivo de importação
	 */
	@SuppressWarnings("unchecked")
	private List<PDV> obterPDVsCota(Cota cota) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT p  ");
		sql.append("FROM PDV p ");
		sql.append("WHERE ");
		sql.append("     p.cota = :cota ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("cota", cota);
		
		return query.list();
	}
	
	/*
	 * Retorna Cota informada no arquivo de importação
	 */
	private Cota obterCota(EMS0116Input input) {
		
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT co  ");
		sql.append("FROM Cota co ");
		sql.append("JOIN FETCH co.pessoa ");
		sql.append("WHERE ");
		sql.append("     co.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("numeroCota", input.getCodCota());
		Cota cota = null;

		cota = (Cota) query.uniqueResult();
		
		return cota;
	}
	
	/*
	 * Retorna os endereços de um determinado PDV
	 */
	@SuppressWarnings("unchecked")
	private List<EnderecoPDV> obterEnderecoPDVPorLogradouro(EMS0116Input input, PDV pdv) {
		
		String logradouro = getLogradouroSemTipo(input.getEndereco().split(",")[0].trim());
		String numero = input.getEndereco().split(",")[1].trim();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ep  ");
		sql.append("FROM EnderecoPDV ep ");
		sql.append("JOIN FETCH ep.endereco ed  ");
		sql.append("WHERE ");
		sql.append("     ep.pdv = :pdv ");
		sql.append(" AND    ed.logradouro = :logradouro ");
		sql.append(" AND    ed.numero = :numero ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("pdv", pdv);
		query.setParameter("logradouro", logradouro);
		query.setParameter("numero", numero);
		
		return query.list();
	}

	private String getLogradouroSemTipo(String logradouro) {
		
		String rua = "RUA";
		if (logradouro.startsWith("RUA"))
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
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}