package br.com.abril.nds.integracao.ems0130.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0130.outbound.EMS0130Output;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.impl.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component

public class EMS0130MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	public EMS0130MessageProcessor() {

	}
	
	@Override
	
	public void processMessage(Message message) {
		
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT pdv ");
		sql.append("FROM PDV pdv ");
		sql.append("JOIN FETCH pdv.telefones t ");
		sql.append("JOIN FETCH t.telefone tel ");
		sql.append("JOIN FETCH pdv.enderecos e ");
		sql.append("JOIN FETCH e.endereco ender ");
		sql.append("JOIN FETCH pdv.cota c ");
		sql.append("JOIN FETCH pdv.segmentacao.tipoPontoPDV tpdv ");
		sql.append("WHERE c.situacaoCadastro = :ativo ");
		sql.append("AND t.principal = true ");
		sql.append("AND e.principal = true ");
	
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("ativo", SituacaoCadastro.ATIVO);
		
		@SuppressWarnings("unchecked")
		List<PDV> pdvs = (List<PDV>) query.list();
		
		
		try {
			
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/BANCA.txt"));	
			for (PDV pdv : pdvs){
				 
			
				EMS0130Output output = new EMS0130Output();			
				
				if(!pdv.getEnderecos().isEmpty()){
					Endereco endereco = pdv.getEnderecos().iterator().next().getEndereco();
					output.setEndereco(endereco.getLogradouro());
					output.setCodigoBairro(endereco.getCodigoBairro());
					output.setNomeMunicipio(endereco.getCidade());
					output.setSiglaUf(endereco.getUf());
					output.setCep(endereco.getCep());				
				}
				
				if(!pdv.getTelefones().isEmpty()){
					Telefone tel = pdv.getTelefones().iterator().next().getTelefone();
					output.setDdd(tel.getDdd());
					output.setTelefone(tel.getNumero());			
				}
				
				output.setCodigoDaCota(pdv.getCota().getNumeroCota());
				output.setPontoDeReferencia(pdv.getPontoReferencia());
				output.setTipoPontoVenda(pdv.getSegmentacao().getTipoPontoPDV().getCodigo());
			
				print.println(fixedFormatManager.export(output));
				
			
			}
			
			print.flush();
			print.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
		
	}

	

	

}
