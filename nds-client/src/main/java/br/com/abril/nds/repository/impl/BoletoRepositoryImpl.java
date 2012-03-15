package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoletoRepositoryImpl extends AbstractRepository<Boleto,Long> implements BoletoRepository {

	
	/**
	 * Construtor padrão
	 */
	public BoletoRepositoryImpl() {
		super(Boleto.class);
	}

	
	@Override
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count (b) from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		if (filtro.getStatusCobranca()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setParameter("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setParameter("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatusCobranca()!=null){	
		    query.setParameter("status", filtro.getStatusCobranca());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		
		if (filtro.getStatusCobranca()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case NOSSO_NUMERO:
					hql.append(" order by b.cota.numeroCota ");
					break;
				case DATA_EMISSAO:
					hql.append(" order by b.dataEmissao ");
					break;
				case DATA_VENCIMENTO:
					hql.append(" order by dataVencimento ");
					break;
				case DATA_PAGAMENTO:
					hql.append(" order by dataPagamento ");
					break;
				case ENCARGOS:
					hql.append(" order by encargos ");
					break;
				case VALOR:
					hql.append(" order by valor ");
					break;
				case TIPO_BAIXA:
					hql.append(" order by tipoBaixa ");
					break;
				case STATUS:
					hql.append(" order by status ");
					break;
				case ACAO:
					hql.append(" order by acao ");
					break;	
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setParameter("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setParameter("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatusCobranca()!=null){	
		    query.setParameter("status", filtro.getStatusCobranca());
		}

        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return query.list();
		
	}
	
	public Boleto obterPorNossoNumero(String nossoNumero) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumero", nossoNumero));
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}

	@Override
	public BoletoDTO obterDadosBoleto(String nossoNumero) {
        BoletoDTO boleto = new BoletoDTO();
		
        
        
        
        
        boleto.setCedenteNome("PROJETO JRimum");
		boleto.setCedenteDocumento("00.000.208/0001-00");

		boleto.setSacadoNome("PROJETO JRimum");
		boleto.setSacadoDocumento("00.000.208/0001-00");

        boleto.setEnderecoSacadoUf("RN");//!!
        boleto.setEnderecoSacadoLocalidade("Natal");
        boleto.setEnderecoSacadoCep("59064-120");
        boleto.setEnderecoSacadoBairro("Grande Centro");
        boleto.setEnderecoSacadoLogradouro("Rua poeta dos programas");
        boleto.setEnderecoSacadoNumero("1");
 
        boleto.setSacadorAvalistaNome("PROJETO JRimum");
		boleto.setSacadorAvalistaDocumento("00.000.208/0001-00");
		
		boleto.setEnderecoSacadorAvalistaUf("RN");//!!
        boleto.setEnderecoSacadorAvalistaLocalidade("Natal");
        boleto.setEnderecoSacadorAvalistaCep("59064-120");
        boleto.setEnderecoSacadorAvalistaBairro("Grande Centro");
        boleto.setEnderecoSacadorAvalistaLogradouro("Rua poeta dos programas");
        boleto.setEnderecoSacadorAvalistaNumero("1");

        boleto.setContaBanco("BANCO_BRADESCO");
        boleto.setContaNumero(123456);
        boleto.setContaCarteira(30);
        boleto.setContaAgencia(1234);
        
        boleto.setTituloNumeroDoDocumento("123456");
        boleto.setTituloNossoNumero("99345678912");
        boleto.setTituloDigitoDoNossoNumero("5");
        boleto.setTituloValor(new BigDecimal(0.23));
        boleto.setTituloDataDoDocumento(new Date());
        boleto.setTituloDataDoVencimento(new Date());
        boleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");//!!!
        boleto.setTituloAceite("A");//!!!
        boleto.setTituloDesconto(new BigDecimal(0.05));
        boleto.setTituloDeducao(BigDecimal.ZERO);
        boleto.setTituloMora(BigDecimal.ZERO);
        boleto.setTituloAcrecimo(BigDecimal.ZERO);
        boleto.setTituloValorCobrado(BigDecimal.ZERO);

        boleto.setBoletoLocalPagamento("Pagável preferencialmente na Rede X ou em " +
                        "qualquer Banco até o Vencimento.");
        boleto.setBoletoInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
                        "cobrado não é o esperado, aproveite o DESCONTÃO!");
        boleto.setBoletoInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
        boleto.setBoletoInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
        boleto.setBoletoInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
        boleto.setBoletoInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
        boleto.setBoletoInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
        boleto.setBoletoInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
        boleto.setBoletoInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
        boleto.setBoletoInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
        

        
        
        
        return boleto;
	}

}



