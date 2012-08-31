package br.com.abril.nds.integracao.ems0131.processor;

import java.io.FileWriter; 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0131.outbound.EMS0131Output;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0131MessageProcessor extends AbstractRepository implements MessageProcessor  {
	
	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c ");
		sql.append("FROM Cota c ");
		sql.append("JOIN FETCH c.pessoa p ");
		sql.append("JOIN FETCH c.enderecos e ");
		sql.append("JOIN FETCH e.endereco ender ");
		sql.append("JOIN FETCH c.telefones t ");
		sql.append("JOIN FETCH t.telefone tel ");
		
		
		Query query = getSession().createQuery(sql.toString());
		
		
		
		try {
			@SuppressWarnings("unchecked")
			List<Cota> cotas =  query.list();
			if (cotas.isEmpty()) {
				ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Nenhum resultado encontrado.");
				return;
			}

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/COTA.txt"));
						
			for(Cota cota : cotas){
				
				EMS0131Output output = new EMS0131Output();
				
				for(EnderecoCota enderecoCota : cota.getEnderecos()){
					for(TelefoneCota telefoneCota : cota.getTelefones()){
					
						output.setCodigoDaCota(cota.getNumeroCota());
						output.setNomeDoJornaleiro(cota.getPessoa().getNome());
						output.setQuantidadeDeCotas(cota.getParametroDistribuicao().getQtdePDV());
						output.setEndereco(enderecoCota.getEndereco().getLogradouro());
						output.setCodigoDoBairro(enderecoCota.getEndereco().getCodigoBairro());
						output.setMunicipio(enderecoCota.getEndereco().getCidade());
						output.setSiglaUf(enderecoCota.getEndereco().getUf());
						output.setCep(enderecoCota.getEndereco().getCep());
						output.setDdd(telefoneCota.getTelefone().getDdd());
						output.setTelefone(telefoneCota.getTelefone().getNumero());
						output.setSituacaoCota(cota.getSituacaoCadastro().toString());
						output.setCondPrazoPagamento("1");
						output.setCodigoDoBox(cota.getBox().getCodigo() + " - "+cota.getBox().getNome());
						output.setCodigoTipoBox(cota.getBox().getTipoBox().toString());
						output.setRepartePorPdv(cota.getParametroDistribuicao().getRepartePorPontoVenda());
						output.setCodigoDoCapataz(cota.getPessoa().getId());
						output.setCpfCnpj(cota.getPessoa().getDocumento());
						if(cota.getPessoa() instanceof PessoaFisica){
							output.setTipoDePessoa("F");
							output.setInscricaoEstadual("");
							output.setInscricaoMunicipal("");
						}else{
							output.setTipoDePessoa("J");
							output.setInscricaoEstadual("INSC ESTADUAL");
							output.setInscricaoMunicipal("INSC MUNICIPAL");
						}
						output.setNumeroDoLogradouro(enderecoCota.getEndereco().getNumero());
						output.setCodigoDaCidade(enderecoCota.getEndereco().getCodigoCidadeIBGE());
						print.println(fixedFormatManager.export(output));
						}						
					}	
				}
			
			print.flush();
			print.close();
			
		} catch (IOException e) {
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
		}
		
	
	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
