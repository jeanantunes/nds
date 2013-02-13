package br.com.abril.nds.integracao.ems0131.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.ems0131.outbound.EMS0131Output;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0131MessageProcessor extends AbstractRepository implements MessageProcessor  {
	
	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c ");
		sql.append("FROM Cota c ");
		sql.append("JOIN FETCH c.pessoa p ");
		sql.append("LEFT JOIN c.enderecos e with e.principal = true ");
		sql.append("LEFT JOIN e.endereco ender ");
		sql.append("LEFT JOIN c.telefones t with t.principal = true ");
		sql.append("LEFT JOIN t.telefone tel ");
		
		
		Query query = getSession().createQuery(sql.toString());
		
		
		
		try {
			@SuppressWarnings("unchecked")
			List<Cota> cotas =  query.list();
			if (cotas.isEmpty()) {
				ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Nenhum resultado encontrado.");
				return;
			}

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/COTA.NEP"));
						
			for(Cota cota : cotas){
				
				EMS0131Output output = new EMS0131Output();
				
				output.setCodigoDaCota(cota.getNumeroCota());
				output.setNomeDoJornaleiro(cota.getPessoa().getNome());
				output.setQuantidadeDeCotas(cota.getParametroDistribuicao().getQtdePDV());
				if (!cota.getEnderecos().isEmpty()) {
					EnderecoCota enderecoCota = cota.getEnderecos().iterator().next();
					output.setEndereco(enderecoCota.getEndereco().getLogradouro());
					output.setMunicipio(enderecoCota.getEndereco().getCidade());
					output.setSiglaUf(enderecoCota.getEndereco().getUf());
					output.setCep(enderecoCota.getEndereco().getCep());
					output.setNumeroDoLogradouro(enderecoCota.getEndereco().getNumero());
					output.setCodigoDaCidade(enderecoCota.getEndereco().getCodigoCidadeIBGE());
				}
				if (!cota.getTelefones().isEmpty()) {
					TelefoneCota telefoneCota = cota.getTelefones().iterator().next();
					output.setDdd(telefoneCota.getTelefone().getDdd());
					output.setTelefone(telefoneCota.getTelefone().getNumero());
				}				
				output.setSituacaoCota(cota.getSituacaoCadastro().toString());
				output.setCondPrazoPagamento("1");
				if (null != cota.getBox()) {
					output.setCodigoDoBox(cota.getBox().getCodigo() + " - "+cota.getBox().getNome());
					output.setCodigoTipoBox(cota.getBox().getTipoBox().toString());
				}
				output.setRepartePorPdv(((null != cota.getParametroDistribuicao().getRepartePorPontoVenda()) ? cota.getParametroDistribuicao().getRepartePorPontoVenda() : false));
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
				print.println(fixedFormatManager.export(output));
			}						
			
			print.flush();
			print.close();
			
		} catch (IOException e) {
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
		}
		
	
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
