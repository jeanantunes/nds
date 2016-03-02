package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BoletoCotaDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.vo.PaginacaoVO;


/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class BoletoRepositoryImpl extends AbstractRepositoryModel<Boleto,Long> implements BoletoRepository {

	
	    /**
     * Construtor padrão
     */
	public BoletoRepositoryImpl() {
		super(Boleto.class);
	}

	    /**
     * Método responsável por obter a quantidade de boletos
     * 
     * @param filtro
     * @return quantidade: quantidade de boletos
     */
	@Override
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(b) from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		if (filtro.getStatus()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setDate("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setDate("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatus()!=null){	
		    query.setParameter("status", filtro.getStatus());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	private StringBuilder getSQLBoletosCota(FiltroConsultaBoletosCotaDTO filtro){
	
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");	
		
		sql.append(" COB.NOSSO_NUMERO as nossoNumero, ");	
		
		sql.append(" COB.DT_EMISSAO as dataEmissao, ");	
		
		sql.append(" COB.DT_VENCIMENTO as dataVencimento, ");	
		
		sql.append(" COB.DT_PAGAMENTO as dataPagamento, ");	
		
		sql.append(" (B.VALOR_MULTA+B.VALOR_JUROS) as encargos, ");
		
		// sql.append(" COB.ENCARGOS as encargos, ");	
		
		sql.append(" ROUND(COB.VALOR,2) as valor, ");	
		
		sql.append(" COB.TIPO_BAIXA as tipoBaixa, ");	
		
		sql.append(" COB.STATUS_COBRANCA as statusCobranca, ");	
		
		sql.append(" D.STATUS as statusDivida, ");	
		
		sql.append(" 'false' as boletoAntecipado, ");	
		
		sql.append(" 'true' as recebeCobrancaEmail ");	
		
		sql.append(" FROM COBRANCA COB ");	
		
		sql.append(" INNER JOIN COTA C ON C.ID = COB.COTA_ID ");
		
		sql.append(" LEFT JOIN BAIXA_COBRANCA B ON B.COBRANCA_ID = COB.ID ");
		
		sql.append(" INNER JOIN DIVIDA D ON D.ID = COB.DIVIDA_ID ");
		
		sql.append(" WHERE COB.TIPO_COBRANCA = :tipoCobrancaBoleto ");		
		
		sql.append(" AND C.NUMERO_COTA = :ncota ");

		if (filtro.getDataVencimentoDe()!=null){
		
			sql.append(" AND COB.DT_VENCIMENTO >= :vctode ");
		}
		
		if (filtro.getDataVencimentoAte()!=null){
		    
			sql.append(" AND COB.DT_VENCIMENTO <= :vctoate ");
		}
		
		if (filtro.getStatus()!=null){	  
			
			sql.append(" AND COB.STATUS_COBRANCA = :status");
		}
		
		return sql;
	}
	
	private StringBuilder getSQLBoletosAntecipadosCota(FiltroConsultaBoletosCotaDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
        sql.append(" SELECT ");	
		
		sql.append(" BA.NOSSO_NUMERO as nossoNumero, ");	
		
		sql.append(" BA.DATA as dataEmissao, ");	
		
		sql.append(" BA.DATA_VENCIMENTO as dataVencimento, ");	
		
		sql.append(" BA.DATA_PAGAMENTO as dataPagamento, ");	

		sql.append(" COALESCE(BA.VALOR_JUROS,0) + COALESCE(BA.VALOR_MULTA,0) as encargos, ");	
		
		sql.append(" ROUND(BA.VALOR,2) as valor, ");	
		
		sql.append(" BA.TIPO_BAIXA as tipoBaixa, ");	
		
		sql.append(" null as statusCobranca, ");	
		
		sql.append(" BA.STATUS as statusDivida, ");	
		
		sql.append(" 'true' as boletoAntecipado, ");	
		
		sql.append(" 'true' as recebeCobrancaEmail ");	
		
		sql.append(" FROM BOLETO_ANTECIPADO BA ");	
		
		sql.append(" INNER JOIN CHAMADA_ENCALHE_COTA AS CE ON (CE.ID = BA.CHAMADA_ENCALHE_COTA_ID) ");
		
		sql.append(" INNER JOIN COTA C ON C.ID = CE.COTA_ID ");
		
		sql.append(" WHERE C.NUMERO_COTA = :ncota ");
		
		sql.append(" AND BA.BOLETO_ANTECIPADO_ID IS NULL ");

		if (filtro.getDataVencimentoDe()!=null){
		
			sql.append(" AND BA.DATA_VENCIMENTO >= :vctode ");
		}
		
		if (filtro.getDataVencimentoAte()!=null){
		    
			sql.append(" AND BA.DATA_VENCIMENTO <= :vctoate ");
		}
		
		if (filtro.getStatus()!=null){	  
			
			sql.append(" AND BA.STATUS = :statusBa ");
		}
		
		return sql;
	}
	
	private StringBuilder getOrdenacaoConsultaBoletos(FiltroConsultaBoletosCotaDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
        if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case NOSSO_NUMERO:
					
					sql.append(" ORDER BY nossoNumero ");
					
					break;
				case DATA_EMISSAO:
					
					sql.append(" ORDER BY dataEmissao ");
					
					break;
				case DATA_VENCIMENTO:

					sql.append(" ORDER BY dataVencimento ");
					
					break;
				case DATA_PAGAMENTO:
					
					sql.append(" ORDER BY dataPagamento ");
					
					break;
				case ENCARGOS:
					
					sql.append(" ORDER BY encargos ");
					
					break;
				case VALOR:
					
					sql.append(" ORDER BY valor ");
					
					break;
				case TIPO_BAIXA:
					
					sql.append(" ORDER BY tipoBaixa ");
					
					break;
				case STATUS_COBRANCA:
					
					sql.append(" ORDER BY statusCobranca ");
					
					break;
				default:
					
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				sql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}

		return sql;
	}
	
	    /**
     * Método responsável por obter uma lista de boletos
     * 
     * @param filtro
     * @return query.list(): lista de boletos
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<BoletoCotaDTO> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
        sql.append(this.getSQLBoletosCota(filtro));
        
        sql.append(" UNION ALL ");
        
        sql.append(this.getSQLBoletosAntecipadosCota(filtro));

		sql.append(this.getOrdenacaoConsultaBoletos(filtro));
		
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		query.addScalar("nossoNumero", StandardBasicTypes.STRING);
		query.addScalar("dataEmissao", StandardBasicTypes.DATE);
		query.addScalar("dataVencimento", StandardBasicTypes.DATE);
		query.addScalar("dataPagamento", StandardBasicTypes.DATE);
		query.addScalar("encargos", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipoBaixa");
		query.addScalar("statusCobranca");
		query.addScalar("statusDivida");
		query.addScalar("boletoAntecipado", StandardBasicTypes.BOOLEAN);
		query.addScalar("recebeCobrancaEmail", StandardBasicTypes.BOOLEAN);
		
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
			
		    query.setDate("vctode", filtro.getDataVencimentoDe());
		}
		
		if (filtro.getDataVencimentoAte()!=null){
			
		    query.setDate("vctoate", filtro.getDataVencimentoAte());
		}
		
		if (filtro.getStatus()!=null){	
			
		    query.setParameter("status", filtro.getStatus().name());
		    
            query.setParameter("statusBa",
                    (filtro.getStatus().equals(StatusCobranca.PAGO) ? StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO
                            : StatusDivida.QUITADA).name());
		}

		query.setParameter("tipoCobrancaBoleto", TipoCobranca.BOLETO.name());
		
        if (filtro.getPaginacao() != null) {
        	
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
        
        query.setResultTransformer(Transformers.aliasToBean(BoletoCotaDTO.class));

		return query.list();
	}
	
	    /**
     * Método responsável por obter Boleto individual
     * 
     * @param nossoNumero
     * @param dividaAcumulada
     */
	@Override
	public Boleto obterPorNossoNumero(String nossoNumero, Boolean dividaAcumulada, boolean apenasBoletoPagavel) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.or(Restrictions.eq("nossoNumero", nossoNumero), Restrictions.eq("nossoNumeroCompleto", nossoNumero)));
		
		if (apenasBoletoPagavel) {
			
			criteria.createAlias("divida", "divida");

			if(dividaAcumulada != null) {
				
				criteria.add(Restrictions.eq("divida.acumulada", dividaAcumulada));
			}
		}
		
		if (apenasBoletoPagavel){
		    
		    criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		    criteria.add(Restrictions.isNull("dataPagamento"));
		    criteria.add(Restrictions.in("divida.status", 
		            Arrays.asList(
		                    StatusDivida.EM_ABERTO, 
		                    StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, 
		                    StatusDivida.PENDENTE_INADIMPLENCIA)));
		}
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public List<Boleto> obterPorNossoNumero(Collection<String> nossoNumero) {
	    
	    StringBuilder hql = new StringBuilder("select b from Boleto b ");
	    hql.append(" JOIN b.cota cota ")
           .append(" left JOIN cota.box box ")
           .append(" left JOIN cota.pdvs pdv ")
           .append(" left JOIN cota.pessoa pessoa ")
           .append(" left JOIN cota.parametroCobranca parametroCobranca ")
           .append(" left JOIN pdv.rotas rotaPdv  ")
           .append(" left JOIN rotaPdv.rota rota  ")
           .append(" left JOIN rota.roteiro roteiro ")
           .append(" where b.nossoNumero in (:nossoNumero) ")
           .append(" and roteiro.tipoRoteiro != :tipoRoteiroEspecial ")
           .append(" ORDER BY box.codigo, roteiro.ordem, rota.ordem, rotaPdv.ordem ");
	    
	    final Query query = this.getSession().createQuery(hql.toString());
        
	    query.setParameterList("nossoNumero", nossoNumero);
	    
	    query.setParameter("tipoRoteiroEspecial", TipoRoteiro.ESPECIAL);
	    
        return query.list();
    }
	
	@Override
	public Boleto obterPorNossoNumeroCompleto(String nossoNumeroCompleto, Boolean dividaAcumulada) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumeroCompleto", nossoNumeroCompleto));
		
		if (dividaAcumulada != null) {
			
			criteria.createAlias("divida", "divida");
			
			criteria.add(Restrictions.eq("divida.acumulada", dividaAcumulada));
		}
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosPrevistos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca.banco.conta) as quantidadePrevisao ");
		hql.append(this.obterFromWhereBoletosPrevistos());

		Query query = this.obterQueryBoletosPrevistos(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosLidos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeLidos ");
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeBaixados ");
		hql.append(this.obterFromWhereBoletosBaixados());
		
		Query query = this.obterQueryBoletosBaixados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosRejeitados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeRejeitados ");
		hql.append(this.obterFromWhereConsultaBoletosRejeitados());
		
		Query query = this.obterQueryBoletosRejeitados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixadosComDivergencia(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeBaixadosComDivergencia ");
		hql.append(this.obterFromWhereConsultaBoletosBaixadosComDivergencia());

		Query query = this.obterQueryBoletosBaixadosComDivergencia(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosInadimplentes(Date dataVencimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadeInadimplentes ");
		hql.append(this.obterFromWhereBoletosInadimplentes());
		
		Query query = this.obterQueryBoletosInadimplentes(hql.toString(), dataVencimento);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(baixaAutomatica.valorPago) as valorTotalBancario ");
		hql.append(this.obterFromWhereConsultaTotalBancario());
		
		Query query = this.obterQureryTotalBancario(data, hql);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Long obterQuantidadeTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select banco as valorTotalBancario ");
		hql.append(this.obterFromWhereConsultaTotalBancario());
		hql.append(" group by banco, baixaAutomatica.dataPagamento ");
		
		Query query = this.obterQureryTotalBancario(data, hql);
		
		
		List<Banco> lista = (List<Banco>) query.list();
		
		return Long.valueOf(lista.size());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select cobranca.cota.numeroCota as numeroCota, ")
  		   .append(" 		(case when (cobranca.cota.pessoa.nome is not null)")
  		   .append("			then cobranca.cota.pessoa.nome")
  		   .append("			else cobranca.cota.pessoa.razaoSocial end) as nomeCota, ")
  		   .append(" 		cobranca.banco.apelido as nomeBanco, ")
		   .append(" 		concat(cobranca.banco.conta, case when cobranca.banco.dvConta is not null then concat('-', cobranca.banco.dvConta) else '' end) as numeroConta, ")
  		   .append(" 		cobranca.nossoNumeroCompleto as nossoNumero, ")
		   .append(" 		cobranca.valor as valorBoleto, ")
		   .append(" 		cobranca.dataVencimento as dataVencimento, ")
		   .append(" 		cobranca.dataEmissao as dataEmissao ")
		   .append(this.obterFromWhereBoletosPrevistos());

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosPrevistos(hql.toString(), filtro.getData());

		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("  cobranca.cota.numeroCota as numeroCota, ");
		hql.append(" 		(case when (cobranca.cota.pessoa.nome is not null)");
		hql.append("			then cobranca.cota.pessoa.nome");
		hql.append("			else cobranca.cota.pessoa.razaoSocial end) as nomeCota, ");
		hql.append("  banco.apelido as nomeBanco, ");
		hql.append("  concat(banco.conta, case when banco.dvConta is not null then concat('-', banco.dvConta) else '' end) as numeroConta, ");
		hql.append("  cobranca.nossoNumero as nossoNumero, ");
		hql.append("  baixaAutomatica.valorPago as valorBoleto, ");
		hql.append("  cobranca.dataVencimento as dataVencimento, ");
		hql.append("  cobranca.dataEmissao as dataEmissao ");
		hql.append(this.obterFromWhereBoletosBaixados());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}
		
		Query query = this.obterQueryBoletosBaixados(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select baixaAutomatica.status as motivoRejeitado, ")
		   .append(" 		baixaAutomatica.banco.apelido as nomeBanco, ")
		   .append(" 		concat(baixaAutomatica.banco.conta, case when baixaAutomatica.banco.dvConta is not null then concat('-',baixaAutomatica.banco.dvConta) else '' end) as numeroConta, ")
		   .append(" 		baixaAutomatica.valorPago as valorBoleto ")
		   .append(this.obterFromWhereConsultaBoletosRejeitados());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosRejeitados(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select baixaAutomatica.status as motivoDivergencia, ")
		   .append(" 		baixaAutomatica.banco.apelido as nomeBanco, ")
		   .append(" 		concat(baixaAutomatica.banco.conta, case when baixaAutomatica.banco.dvConta is not null then concat('-',baixaAutomatica.banco.dvConta) else '' end) as numeroConta, ")
		   .append(" 		cobranca.valor as valorBoleto, ")
		   .append(" 		baixaAutomatica.valorPago as valorPago, ")
		   .append(" 		cobranca.valor - baixaAutomatica.valorPago as valorDiferenca, ")
		   .append("  		cobranca.dataVencimento as dataVencimento, ")
		   .append("  		cobranca.dataEmissao as dataEmissao ")
		   .append(this.obterFromWhereConsultaBoletosBaixadosComDivergencia());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosBaixadosComDivergencia(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("  cobranca.cota.numeroCota as numeroCota, ");
		hql.append(" 		(case when (cobranca.cota.pessoa.nome is not null)");
		hql.append("			then cobranca.cota.pessoa.nome");
		hql.append("			else cobranca.cota.pessoa.razaoSocial end) as nomeCota, ");
		hql.append("  cobranca.banco.apelido as nomeBanco, ");
		hql.append("  concat(cobranca.banco.conta, case when cobranca.banco.dvConta is not null then concat('-', cobranca.banco.dvConta) else '' end) as numeroConta, ");
		hql.append("  cobranca.nossoNumeroCompleto as nossoNumero, ");
		hql.append("  cobranca.valor as valorBoleto, ");
		hql.append("  cobranca.dataVencimento as dataVencimento, ");
		hql.append("  cobranca.dataEmissao as dataEmissao ");
		hql.append(this.obterFromWhereBoletosInadimplentes());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosInadimplentes(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select banco.apelido as nomeBanco, ")
		   .append(" 		concat(banco.conta, case when banco.dvConta is not null then concat('-', banco.dvConta) else '' end) as numeroConta, ")
  		   .append(" 		sum(baixaAutomatica.valorPago) as valorPago, ")
  		   .append(" 		baixaAutomatica.dataPagamento as dataVencimento ")
  		   .append(this.obterFromWhereConsultaTotalBancario())
  		   .append(" group by banco, baixaAutomatica.dataPagamento ");

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {
			
			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}
		
		Query query = this.obterQureryTotalBancario(filtro.getData(), hql);
		
		this.paginarConsultasBaixaBoleto(query, filtro);

		return query.list();
	}
	
	private String obterFromWhereBoletosPrevistos() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Cobranca cobranca ");
		hql.append(" where cobranca.dataVencimento = :data ");
		hql.append(" and cobranca.tipoCobranca in (:tipoCobranca) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereBoletosBaixados() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaCobranca baixaAutomatica ");
		hql.append(" join baixaAutomatica.cobranca cobranca ");
		hql.append(" join cobranca.banco banco ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and baixaAutomatica.status in (:statusBaixa) ");
		hql.append(" and cobranca.tipoCobranca in (:tipoCobranca) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereConsultaBoletosRejeitados() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" left join baixaAutomatica.cobranca cobranca ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and baixaAutomatica.status in (:statusBaixa) ");
		hql.append(" and ( cobranca.tipoCobranca is null or cobranca.tipoCobranca in (:tipoCobranca) ) ");
		
		return hql.toString();
	}

	private String obterFromWhereConsultaBoletosBaixadosComDivergencia() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" left join baixaAutomatica.cobranca cobranca ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and baixaAutomatica.status in (:statusBaixa) ");
		hql.append(" and cobranca.tipoCobranca in (:tipoCobranca) ");
		
		return hql.toString();
	}

	
	private String obterFromWhereBoletosInadimplentes() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Cobranca cobranca ");
		hql.append(" where cobranca.dataVencimento = :data ");
		hql.append(" and cobranca.statusCobranca =:statusCobranca");
		hql.append(" and cobranca.tipoCobranca in (:tipoCobranca) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereConsultaTotalBancario() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" join baixaAutomatica.banco banco ");
		hql.append(" left join baixaAutomatica.cobranca cobranca ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and (cobranca.tipoCobranca is null or cobranca.tipoCobranca in (:tipoCobranca)) ");
		
		return hql.toString();
	}
	
	private Query obterQueryBoletosPrevistos(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));
		
		return query;
	}
	
	private Query obterQueryBoletosBaixados(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO);
		
		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));
		
		return query;
	}
	
	private Query obterQueryBoletosRejeitados(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA);
		listaParametros.add(StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO);
		
		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));
		

		return query;
	}
	
	private Query obterQueryBoletosBaixadosComDivergencia(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());

		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_VALOR);

		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));

		return query;
	}
	
	private Query obterQueryBoletosInadimplentes(String hql, Date data) {
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));
		
		return query;
	}
	
	private Query obterQureryTotalBancario(Date data, StringBuilder hql) {
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameterList("tipoCobranca", Arrays.asList(TipoCobranca.BOLETO,TipoCobranca.BOLETO_EM_BRANCO));
		
		return query;
	}
	
	private String obterOrdenacaoConsultaBaixaBoletos(OrdenacaoColunaDetalheBaixaBoleto ordenacao, String orderSort) {
		
		StringBuilder orderBy = new StringBuilder();
		
		orderBy.append(" order by ");
		orderBy.append(ordenacao.toString());
		orderBy.append(" ");
		orderBy.append(orderSort == null ? "asc" : orderSort);

		return orderBy.toString();
	}
	
	private void paginarConsultasBaixaBoleto(Query query, FiltroDetalheBaixaBoletoDTO filtro) {
		
		if (filtro.getPaginacao() != null) {

			PaginacaoVO paginacao = filtro.getPaginacao();

			if (paginacao.getPosicaoInicial() != null) {
			
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (paginacao.getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(DetalheBaixaBoletoDTO.class));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosGeradosNaDataOperacaoDistribuidor(Date dataOperacao) {
		
		Criteria criteria = this.getSession().createCriteria(Boleto.class);
		criteria.add(Restrictions.eq("dataEmissao", dataOperacao));
		
		ProjectionList projectionList = Projections.projectionList();
	
		projectionList.add(Projections.property("banco").as("banco"));
	    projectionList.add(Projections.property("nossoNumeroCompleto").as("nossoNumeroCompleto"));
	    projectionList.add(Projections.property("dataVencimento").as("dataVencimento"));
	    projectionList.add(Projections.property("valor").as("valor"));
	    projectionList.add(Projections.property("dataEmissao").as("dataEmissao"));
	    
	    criteria.setProjection(projectionList);
	    
	    criteria.setResultTransformer(new AliasToBeanResultTransformer(Boleto.class));
	    
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterBoletosNaoPagos(Date data){
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select cobranca ");
		hql.append(" from Cobranca cobranca ");
		hql.append(" join cobranca.divida divida ");
		hql.append(" where cobranca.dataVencimento < :data ");
		hql.append(" and cobranca.statusCobranca =:statusCobranca ");
		hql.append(" and divida.status not in(:statusPendente) ");
		hql.append(" and cobranca.dataPagamento is null ");
		hql.append(" and cobranca.oriundaNegociacaoAvulsa = :oriundaNegociacaoAvulsa ");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		query.setParameter("oriundaNegociacaoAvulsa", false);
		query.setParameterList("statusPendente", Arrays.asList(StatusDivida.PENDENTE_INADIMPLENCIA, StatusDivida.POSTERGADA));
		return query.list();
		
	}

	public Long verificaEnvioDeEmail(String nossoNumero) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(forma_cobranca.recebeCobrancaEmail) " +
		
				   "from Cobranca as cobranca join " +
				   
				   "cobranca.cota as cota join " +
				   
				   "cota.parametroCobranca as parametro_cobranca_cota join " +
				   
				   "parametro_cobranca_cota.formasCobrancaCota as forma_cobranca " +
				   
				   "where " + 
				   
				   "forma_cobranca.recebeCobrancaEmail = true " +
				   
				   "and " +
				   
				   "forma_cobranca.ativa= true " +
				   
				   "and " +
				   
				   "cobranca.nossoNumero = :nossoNumero");
						
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("nossoNumero", nossoNumero);
	    
		return  (Long) query.uniqueResult();

	}
}
