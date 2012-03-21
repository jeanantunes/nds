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
		
		
		
		
		/*
		DistribuidorService cedente = new DistribuidorServiceImpl();
		CotaService sacado = new CotaServiceImpl();
		
		Boleto boleto = this.obterPorNossoNumero(nossoNumero);
		Pessoa p = boleto.getCota().getPessoa();
		Endereco enderecoSacado = p.getEnderecos().get(1);
			
		
		
		String nomeSacado="";
		String documentoSacado="";
		if (pj != null){
			nomeSacado = pj.getRazaoSocial();
			documentoSacado = pj.getCnpj();
		}
		if (pf != null){
			nomeSacado = pf.getNome();
			documentoSacado = pf.getCpf();
		}
			
			
			
			
			
			
		
        BoletoDTO boletoDTO = new BoletoDTO();
		
        boletoDTO.setCedenteNome(cedente.obter().getJuridica().getRazaoSocial());         
		boletoDTO.setCedenteDocumento(cedente.obter().getJuridica().getCnpj());

		boletoDTO.setSacadoNome(nomeSacado);          
		boletoDTO.setSacadoDocumento(documentoSacado); 

        boletoDTO.setEnderecoSacadoUf(enderecoSacado.getUf());            
        boletoDTO.setEnderecoSacadoLocalidade(enderecoSacado.getCidade());     
        boletoDTO.setEnderecoSacadoCep(enderecoSacado.getCep());         
        boletoDTO.setEnderecoSacadoBairro(enderecoSacado.getBairro()); 
        boletoDTO.setEnderecoSacadoLogradouro(enderecoSacado.getLogradouro()); 
        boletoDTO.setEnderecoSacadoNumero(Integer.toString(enderecoSacado.getNumero())); 
                                                  
        String nomeBanco="";
        if (boleto.getBanco().getNumeroBanco()=="399"){
            nomeBanco="BANCO_HSBC";
        }
        
        boletoDTO.setContaBanco(nomeBanco);        
        boletoDTO.setContaNumero(boleto.getBanco().getConta().intValue());                
        
        
        boletoDTO.setContaCarteira(30);     //BANCO.CARTEIRA
        
        
        boletoDTO.setContaAgencia(boleto.getBanco().getAgencia().intValue());                   

        
        
        
        
        boletoDTO.setTituloNumeroDoDocumento("123456");   //???
        
        
        boletoDTO.setTituloNossoNumero(boleto.getNossoNumero());    
        
        
        boletoDTO.setTituloDigitoDoNossoNumero("4");      //PARAMETRO ???  
        
        
        boletoDTO.setTituloValor(boleto.getValor());   
        boletoDTO.setTituloDataDoDocumento(boleto.getDataEmissao());   
        boletoDTO.setTituloDataDoVencimento(boleto.getDataVencimento());  
        
        
        boletoDTO.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");//???
        
        boletoDTO.setTituloAceite("A");//???
        
        
        
        
        
        boletoDTO.setTituloDesconto(BigDecimal.ZERO);
        boletoDTO.setTituloDeducao(BigDecimal.ZERO);
        boletoDTO.setTituloMora(BigDecimal.ZERO);
        boletoDTO.setTituloAcrecimo(BigDecimal.ZERO);
        boletoDTO.setTituloValorCobrado(BigDecimal.ZERO);

        
        
        
        
        //PARAMETROS ?
        boletoDTO.setBoletoLocalPagamento("Pagável preferencialmente na Rede X ou em " +
                                       "qualquer Banco até o Vencimento.");
        boletoDTO.setBoletoInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
                                          "cobrado não é o esperado, aproveite o DESCONTÃO!");
        boletoDTO.setBoletoInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
        boletoDTO.setBoletoInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
        boletoDTO.setBoletoInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
        boletoDTO.setBoletoInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
        boletoDTO.setBoletoInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
        boletoDTO.setBoletoInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
        boletoDTO.setBoletoInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
        boletoDTO.setBoletoInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
        

        */
		
		
		
		
		
		
		
		    BoletoDTO boletoDTO = new BoletoDTO();
	        
	        
	        boletoDTO.setCedenteNome("PROJETO JRimum");
			boletoDTO.setCedenteDocumento("00.000.208/0001-00");

			boletoDTO.setSacadoNome("PROJETO JRimum");
			boletoDTO.setSacadoDocumento("00.000.208/0001-00");

	        boletoDTO.setEnderecoSacadoUf("RN");//!!
	        boletoDTO.setEnderecoSacadoLocalidade("Natal");
	        boletoDTO.setEnderecoSacadoCep("59064-120");
	        boletoDTO.setEnderecoSacadoBairro("Grande Centro");
	        boletoDTO.setEnderecoSacadoLogradouro("Rua poeta dos programas");
	        boletoDTO.setEnderecoSacadoNumero("1");
	        
	        
	        boletoDTO.setSacadorAvalistaNome("PROJETO JRimum");
			boletoDTO.setSacadorAvalistaDocumento("00.000.208/0001-00");
			
			boletoDTO.setEnderecoSacadorAvalistaUf("RN");//!!
	        boletoDTO.setEnderecoSacadorAvalistaLocalidade("Natal");
	        boletoDTO.setEnderecoSacadorAvalistaCep("59064-120");
	        boletoDTO.setEnderecoSacadorAvalistaBairro("Grande Centro");
	        boletoDTO.setEnderecoSacadorAvalistaLogradouro("Rua poeta dos programas");
	        boletoDTO.setEnderecoSacadorAvalistaNumero("1");
	        
         
	        boletoDTO.setContaBanco("BANCO_BRADESCO");
	        boletoDTO.setContaNumero(123456);
	        boletoDTO.setContaCarteira(30);
	        boletoDTO.setContaAgencia(1234);
	        
	        boletoDTO.setTituloNumeroDoDocumento("123456");
	        boletoDTO.setTituloNossoNumero("99345678912");
	        boletoDTO.setTituloDigitoDoNossoNumero("5");
	        boletoDTO.setTituloValor(new BigDecimal(0.23));
	        boletoDTO.setTituloDataDoDocumento(new Date());
	        boletoDTO.setTituloDataDoVencimento(new Date());
	        boletoDTO.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");//!!!
	        boletoDTO.setTituloAceite("A");//!!!
	        boletoDTO.setTituloDesconto(new BigDecimal(0.05));
	        boletoDTO.setTituloDeducao(BigDecimal.ZERO);
	        boletoDTO.setTituloMora(BigDecimal.ZERO);
	        boletoDTO.setTituloAcrecimo(BigDecimal.ZERO);
	        boletoDTO.setTituloValorCobrado(BigDecimal.ZERO);

	        boletoDTO.setBoletoLocalPagamento("Pagável preferencialmente na Rede X ou em " +
	                        "qualquer Banco até o Vencimento.");
	        boletoDTO.setBoletoInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
	                        "cobrado não é o esperado, aproveite o DESCONTÃO!");
	        boletoDTO.setBoletoInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
	        boletoDTO.setBoletoInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
	        boletoDTO.setBoletoInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
	        boletoDTO.setBoletoInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
	        boletoDTO.setBoletoInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
	        boletoDTO.setBoletoInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
	        boletoDTO.setBoletoInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
	        boletoDTO.setBoletoInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
		
		
		
		
		
        
        
        return boletoDTO;
	}

}



