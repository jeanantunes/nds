package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorRepository;

@Repository
public class DistribuidorRepositoryImpl extends
		AbstractRepositoryModel<Distribuidor, Long> implements
		DistribuidorRepository {

	public DistribuidorRepositoryImpl() {
		super(Distribuidor.class);
	}

	@Override
	public Distribuidor obter() {
		String hql = "from Distribuidor";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Distribuidor> distribuidores = query.list();
		return distribuidores.isEmpty() ? null : distribuidores.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores,
															OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder hql =
			new StringBuilder("from DistribuicaoFornecedor where fornecedor.id in (:idsFornecedores) ");

		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsFornecedores", idsForncedores);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterEnderecoPrincipal()
	 */
	@Override
	public EnderecoDistribuidor obterEnderecoPrincipal(){
		Criteria criteria=  getSession().createCriteria(EnderecoDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );
		criteria.setMaxResults(1);

		return (EnderecoDistribuidor) criteria.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterTelefonePrincipal()
	 */
	@Override
	public TelefoneDistribuidor obterTelefonePrincipal(){
		Criteria criteria=  getSession().createCriteria(TelefoneDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );
		criteria.setMaxResults(1);

		return (TelefoneDistribuidor) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {
		Criteria criteria =  getSession().createCriteria(TipoGarantiaAceita.class);
		criteria.setProjection(Projections.property("tipoGarantia"));
		return criteria.list();
	}

	@Override
	public List<String> obterNomeCNPJDistribuidor() {
		
		Query query = 
			this.getSession().createQuery(
				"select d.juridica.razaoSocial, d.juridica.cnpj from Distribuidor d");
		
		List<String> retorno = new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		List<Object[]> listaResultados = query.list();
		
		for (Object[] dados : listaResultados){
			
			retorno.add((String)dados[0]);
			retorno.add((String)dados[1]);
		}
		
		return retorno;
	}

	@Override
	public String obterInformacoesComplementaresProcuracao() {
		
		return (String) 
				this.getSession().
				createQuery(
						"select d.informacoesComplementaresProcuracao from Distribuidor d").uniqueResult();
	}

	@Override
	public String obterRazaoSocialDistribuidor() {
		
		return (String)
				this.getSession().
				createQuery("select d.juridica.razaoSocial from Distribuidor d").uniqueResult();
	}
	
	@Override
	public String obterInformacoesComplementaresTermoAdesao() {
		
		String resultado = (String) 
				this.getSession().
				createQuery(
						"select d.parametroEntregaBanca.complementoTermoAdesao from Distribuidor d").uniqueResult();

		
		return resultado; 	
	}

	@Override
	public DiaSemana buscarInicioSemana() {
		
		return (DiaSemana) 
				this.getSession().createQuery("select inicioSemana from Distribuidor").uniqueResult();
	}

	@Override
	public Date obterDataOperacaoDistribuidor() {
		
		return (Date) this.getSession().createQuery("select dataOperacao from Distribuidor").uniqueResult();
	}
	
	@Override
	public BigDecimal obterDescontoCotaNegociacao(){
		
		return (BigDecimal) 
				this.getSession().createQuery(
						"select descontoCotaNegociacao from Distribuidor").uniqueResult();
	}
	
	@Override
	public boolean utilizaGarantiaPdv(){
		
		Boolean utilizaGarantiaPdv = 
				(Boolean) this.getSession().createQuery(
						"select utilizaGarantiaPdv from Distribuidor").uniqueResult();
		
		return utilizaGarantiaPdv == null ? false : utilizaGarantiaPdv;
	}
	
	@Override
	public boolean aceitaJuramentado(){
		
		Boolean aceitaJuramentado =
				(Boolean) this.getSession().createQuery(
						"select aceitaJuramentado from Distribuidor").uniqueResult();
		
		return aceitaJuramentado == null ? false : aceitaJuramentado;
	}
	
	@Override
	public int qtdDiasEncalheAtrasadoAceitavel(){
		
		Integer qtd = (Integer)
				this.getSession().createQuery(
						"select qtdDiasEncalheAtrasadoAceitavel from Distribuidor").uniqueResult();
		
		return qtd == null ? 0 : qtd;
	}

	@Override
	public Integer obterNumeroDiasNovaCobranca() {
		
		return (Integer)
				this.getSession().createQuery("select numeroDiasNovaCobranca from Distribuidor").uniqueResult();
	}

	@Override
	public boolean utilizaControleAprovacao() {
		
		Boolean controle = 
				(Boolean) this.getSession().createQuery(
						"select utilizaControleAprovacao from Distribuidor").uniqueResult();
		
		return controle == null ? false : controle;
	}

	@Override
	public Boolean utilizaTermoAdesao() {
		
		Boolean utilizaTermoAdesao = 
				(Boolean) this.getSession().createQuery(
						"select d.parametroEntregaBanca.utilizaTermoAdesao from Distribuidor d").uniqueResult();
		
		return utilizaTermoAdesao == null ? false : utilizaTermoAdesao;
	}

	@Override
	public Boolean utilizaProcuracaoEntregadores() {
		
		Boolean utilizaProcuracaoEntregadores = 
				(Boolean) this.getSession().createQuery(
						"select utilizaProcuracaoEntregadores from Distribuidor").uniqueResult();
		
		return utilizaProcuracaoEntregadores == null ? false : utilizaProcuracaoEntregadores;
	}

	@Override
	public Boolean utilizaSugestaoIncrementoCodigo() {
		
		Boolean utilizaSugestaoIncrementoCodigo = 
				(Boolean) this.getSession().createQuery(
						"select utilizaSugestaoIncrementoCodigo from Distribuidor").uniqueResult();
		
		return utilizaSugestaoIncrementoCodigo == null ? false : utilizaSugestaoIncrementoCodigo;
	}

	@Override
	public String getEmail() {
		
		return (String) this.getSession().createQuery(
						"select d.juridica.email from Distribuidor d").uniqueResult();
	}

	@Override
	public ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor() {
		
		return (ParametrosRecolhimentoDistribuidor)
				this.getSession().createQuery(
						"select parametrosRecolhimentoDistribuidor from Distribuidor").uniqueResult();
	}

	@Override
	public TipoImpressaoCE tipoImpressaoCE() {
		
		return (TipoImpressaoCE)
				this.getSession().createQuery(
						"select tipoImpressaoCE from Distribuidor").uniqueResult();
	}

	@Override
	public Integer qntDiasVencinemtoVendaEncalhe() {
		
		return (Integer)
				this.getSession().createQuery(
						"select qntDiasVencinemtoVendaEncalhe from Distribuidor").uniqueResult();
	}

	@Override
	public Boolean aceitaBaixaPagamentoVencido() {
		
		Boolean aceitaBaixaPagamentoVencido = 
				(Boolean) this.getSession().createQuery(
						"select aceitaBaixaPagamentoVencido from Distribuidor").uniqueResult();
		
		return aceitaBaixaPagamentoVencido == null ? false : aceitaBaixaPagamentoVencido;
	}

	@Override
	public Boolean aceitaBaixaPagamentoMaior() {
		
		Boolean aceitaBaixaPagamentoMaior = 
				(Boolean) this.getSession().createQuery(
						"select aceitaBaixaPagamentoMaior from Distribuidor").uniqueResult();
		
		return aceitaBaixaPagamentoMaior == null ? false : aceitaBaixaPagamentoMaior;
	}

	@Override
	public Boolean aceitaBaixaPagamentoMenor() {

		Boolean aceitaBaixaPagamentoMenor = 
				(Boolean) this.getSession().createQuery(
						"select aceitaBaixaPagamentoMenor from Distribuidor").uniqueResult();
		
		return aceitaBaixaPagamentoMenor == null ? false : aceitaBaixaPagamentoMenor;
	}

	@Override
	public Integer negociacaoAteParcelas() {
		
		return (Integer)
				this.getSession().createQuery(
						"select negociacaoAteParcelas from Distribuidor").uniqueResult();
	}

	@Override
	public Integer qtdDiasLimiteParaReprogLancamento() {
		
		return (Integer)
				this.getSession().createQuery(
						"select qtdDiasLimiteParaReprogLancamento from Distribuidor").uniqueResult();
	}

	@Override
	public ObrigacaoFiscal obrigacaoFiscal() {
		
		return (ObrigacaoFiscal)
				this.getSession().createQuery(
						"select obrigacaoFiscal from Distribuidor").uniqueResult();
	}

	@Override
	public TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE() {
		
		return (TipoImpressaoNENECADANFE)
				this.getSession().createQuery(
						"select tipoImpressaoNENECADANFE from Distribuidor").uniqueResult();
	}

	@Override
	public BigInteger capacidadeRecolhimento() {
		
		return (BigInteger)
				this.getSession().createQuery(
						"select capacidadeRecolhimento from Distribuidor").uniqueResult();
	}

	@Override
	public String cidadeDistribuidor() {
		
		return (String) this.getSession().createQuery(
				"select d.enderecoDistribuidor.endereco.cidade from Distribuidor d").uniqueResult();
	}

	@Override
	public String codigoDistribuidorDinap() {
		
		return (String)
				this.getSession().
				createQuery("select codigoDistribuidorDinap from Distribuidor").uniqueResult();
	}

	@Override
	public String codigoDistribuidorFC() {
		
		return (String)
				this.getSession().
				createQuery("select codigoDistribuidorFC from Distribuidor").uniqueResult();
	}

	@Override
	public Integer diasNegociacao() {
		
		return (Integer)
				this.getSession().
				createQuery("select d.parametroCobrancaDistribuidor.diasNegociacao from Distribuidor d").uniqueResult();
	}

	@Override
	public TipoContabilizacaoCE tipoContabilizacaoCE() {
		
		return (TipoContabilizacaoCE)
				this.getSession().
				createQuery("select tipoContabilizacaoCE from Distribuidor").uniqueResult();
	}

	@Override
	public Boolean preenchimentoAutomaticoPDV() {
		
		Boolean preenchimentoAutomaticoPDV = 
				(Boolean) this.getSession().createQuery(
						"select preenchimentoAutomaticoPDV from Distribuidor").uniqueResult();
		
		return preenchimentoAutomaticoPDV == null ? false : preenchimentoAutomaticoPDV;
	}

	@Override
	public Long qntDiasReutilizacaoCodigoCota() {
		
		Long qntDiasReutilizacaoCodigoCota = 
				(Long) this.getSession().createQuery(
						"select qntDiasReutilizacaoCodigoCota from Distribuidor").uniqueResult();
		
		return qntDiasReutilizacaoCodigoCota == null ? 0L : qntDiasReutilizacaoCodigoCota;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<PoliticaCobranca> politicasCobranca() {
		
		return new HashSet<PoliticaCobranca>(
				this.getSession().
				createQuery("select politicasCobranca from Distribuidor").list());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<PoliticaCobranca> politicasCobrancaAtivas() {
		
		return new HashSet<PoliticaCobranca>(
				this.getSession().
				createQuery("select p from PoliticaCobranca p JOIN p.formaCobranca f where p.ativo = true").list());
	}

	@Override
	public String assuntoEmailCobranca() {
		
		return (String)
				this.getSession().
				createQuery("select assuntoEmailCobranca from Distribuidor").uniqueResult();
	}

	@Override
	public String mensagemEmailCobranca() {
		
		return (String)
				this.getSession().
				createQuery("select mensagemEmailCobranca from Distribuidor").uniqueResult();
	}

	@Override
	public Boolean regimeEspecial() {
		
		Boolean regimeEspecial = 
				(Boolean) this.getSession().createQuery(
						"select regimeEspecial from Distribuidor").uniqueResult();
		
		return regimeEspecial == null ? false : regimeEspecial;
	}

	@Override
	public TipoAtividade tipoAtividade() {
		
		return (TipoAtividade)
				this.getSession().
				createQuery("select tipoAtividade from Distribuidor").uniqueResult();
	}

	@Override
	public Integer fatorRelancamentoParcial() {
		
		Integer fatorRelancamentoParcial = 
				(Integer) this.getSession().createQuery(
						"select fatorRelancamentoParcial from Distribuidor").uniqueResult();
		
		return fatorRelancamentoParcial == null ? 0 : fatorRelancamentoParcial;
	}

	@Override
	public Long obterId() {
		
		return (Long)
				this.getSession().
				createQuery("select id from Distribuidor").uniqueResult();
	}
	
	@Override
	public String cnpj(){
		
		return (String)
				this.getSession().
				createQuery("select d.juridica.cnpj from Distribuidor d").uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParametrosDistribuidorEmissaoDocumento> parametrosDistribuidorEmissaoDocumentos(){
		
		return (List<ParametrosDistribuidorEmissaoDocumento>)
				this.getSession().createQuery("select parametrosDistribuidorEmissaoDocumentos from Distribuidor").list();
	}
	
	@Override
	public Integer codigo(){
		
		return (Integer)
				this.getSession().createQuery("select codigo from Distribuidor").uniqueResult();
	}

	@Override
	public BigInteger capacidadeDistribuicao() {
		
		return (BigInteger)
				this.getSession().createQuery("select capacidadeDistribuicao from Distribuidor").uniqueResult();
	}
	
	@Override
	public PessoaJuridica juridica(){
		
		return (PessoaJuridica)
				this.getSession().createQuery("select juridica from Distribuidor").uniqueResult();
	}

	@Override
	public ParametroContratoCota parametroContratoCota() {
		
		return (ParametroContratoCota)
				this.getSession().createQuery("select parametroContratoCota from Distribuidor").uniqueResult();
	}
	
	@Override
	public ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor(){
		
		return (ParametrosAprovacaoDistribuidor)
				this.getSession().createQuery("select parametrosAprovacaoDistribuidor from Distribuidor").uniqueResult();
	}
}