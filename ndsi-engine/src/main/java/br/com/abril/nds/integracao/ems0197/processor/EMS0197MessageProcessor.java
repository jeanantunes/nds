package br.com.abril.nds.integracao.ems0197.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Detalhe;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Trailer;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.impl.AbstractRepository;
import br.com.abril.nds.service.DescontoService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0197MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private DescontoService descontoService; 	
	
	private Date dataLctoDistrib;

	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	
	@Override
	public void processMessage(Message message) {
		
		
		// OBTER LISTA DE JORNALEIROS PARA DEFINIR QTDE DE ARQUIVOS
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT DISTINCT pdv FROM PDV pdv");
		sql.append(" JOIN FETCH pdv.cota cotaPDV ");
		sql.append(" LEFT JOIN FETCH cotaPDV.movimentoEstoqueCotas mec ");
		sql.append(" LEFT JOIN FETCH mec.produtoEdicao pe ");
		sql.append(" LEFT JOIN FETCH pe.produto pd ");
		sql.append(" LEFT JOIN pe.lancamentos lan ");
		sql.append(" LEFT JOIN mec.tipoMovimento tip ");
		sql.append(" WHERE pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" AND	lan.dataLancamentoDistribuidor = :dataInformada");
		sql.append(" AND	tip.grupoMovimentoEstoque='ENVIO_JORNALEIRO'");
		sql.append(" ORDER BY cotaPDV.numeroCota");
		

		Query query = getSession().createQuery(sql.toString());
	    query.setParameter("dataInformada", this.dataLctoDistrib);

		try {
			@SuppressWarnings("unchecked")
			List<PDV> jornaleiros = query.list();


		// PARA CADA JORNALEIRO VERIFICAR OS REGISTROS COM OS DADOS PERTINENTES A ELE	
			for (PDV jornaleiro : jornaleiros){
				
				System.out.println("break");
				try{
					
					//CRIA O NOME DO ARQUIVO COM O NUMERO DA COTA + DATA INFORMADA
					String nomeArquivo = String.format("%1$04d%2$s", jornaleiro.getCota().getNumeroCota(), sdf.format(dataLctoDistrib)); 														
					
					PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/"+nomeArquivo+".rep"));	
					
					//REGISTRO TIPO 1
					EMS0197Header outHeader = new EMS0197Header();
					
					outHeader.setCodigoCota(jornaleiro.getCota().getNumeroCota().toString());
					outHeader.setNomePDV(jornaleiro.getNome());
					outHeader.setDataLctoDistrib(sdf.format(dataLctoDistrib));//data recebida pela interface
					
					print.println(fixedFormatManager.export(outHeader));
					
					for(MovimentoEstoqueCota pe : jornaleiro.getCota().getMovimentoEstoqueCotas()){
						//REGISTRO TIPO 2
						EMS0197Detalhe outDetalhe = new EMS0197Detalhe();
						
						outDetalhe.setCodigoCota(jornaleiro.getCota().getNumeroCota().toString());
						outDetalhe.setCodProduto(pe.getProdutoEdicao().getProduto().getCodigo().toString());
						outDetalhe.setNumEdicao(pe.getProdutoEdicao().getNumeroEdicao().toString());
						outDetalhe.setNomeProduto(pe.getProdutoEdicao().getProduto().getNome());
						outDetalhe.setCodigoDeBarrasPE(pe.getProdutoEdicao().getCodigoDeBarras());
						outDetalhe.setPrecoCustoPE(pe.getProdutoEdicao().getPrecoCusto().toString());
						outDetalhe.setPrecoVendaPE(pe.getProdutoEdicao().getPrecoVenda().toString());												
						outDetalhe.setDescontoPE(descontoService.obterDescontoPorCotaProdutoEdicao(jornaleiro.getCota(), pe.getProdutoEdicao()).toString());						
						outDetalhe.setQtdeMEC(pe.getQtde().toString());
					
						print.println(fixedFormatManager.export(outDetalhe));
						
					}
					//REGISTRO TIPO 3
					EMS0197Trailer outTrailer = new EMS0197Trailer();
					
					outTrailer.setNumeroCota(jornaleiro.getCota().getNumeroCota().toString());
					outTrailer.setQtdeRegTipo2(jornaleiro.getCota().getMovimentoEstoqueCotas().size());
					
					print.println(fixedFormatManager.export(outTrailer));
					
					print.flush();
					print.close();
					
					}
					catch(IOException e){
				
					}
				}
			}	
			catch(NoResultException e){}	
	
			}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
		
	}

	public void setDataLctoDistrib(Date dataLctoDistrib) {
		this.dataLctoDistrib = dataLctoDistrib;		
	}
				
}

