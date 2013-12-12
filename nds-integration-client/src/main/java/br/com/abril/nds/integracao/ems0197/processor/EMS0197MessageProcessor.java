package br.com.abril.nds.integracao.ems0197.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Detalhe;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0197MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final String REPARTE_FOLDER = "reparte";

	private static final String REPARTE_EXT = ".rep";

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private DescontoService descontoService; 	
	
	private Date dataLctoDistrib;

	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	
	/** Quantidade de arquivos processados. */
	private int quantidadeArquivosGerados = 0;
	
	@Override
	public void processMessage(Message message) {
		
		this.quantidadeArquivosGerados = 0;
		
		List<PDV> jornaleiros = obterPDVsPricipais();
		
		for (PDV jornaleiro : jornaleiros){

			try{
				
				String nomeArquivo = String.format("%1$04d%2$s", 
						jornaleiro.getCota().getNumeroCota(),
						sdf.format(dataLctoDistrib)); 												
				
				PrintWriter print = new PrintWriter(new FileWriter(
						message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO)
						+ File.separator + REPARTE_FOLDER +File.separator + nomeArquivo + REPARTE_EXT));
				
				EMS0197Header outHeader = createHeader(jornaleiro);
				
				print.println(fixedFormatManager.export(outHeader));
					
			
				for(MovimentoEstoqueCota movimentoEstoqueCota : jornaleiro.getCota().getMovimentoEstoqueCotas()){
					
					EMS0197Detalhe outDetalhe = createDetalhes(jornaleiro, movimentoEstoqueCota.getProdutoEdicao(), movimentoEstoqueCota.getQtde());
					
					print.println(fixedFormatManager.export(outDetalhe));
				}
					
				EMS0197Trailer outTrailer = createTrailer(jornaleiro);
				
				print.println(fixedFormatManager.export(outTrailer));
					
				print.flush();
				print.close();
						
				/*
				 * A quantidade de arquivos gerados é incrementado aqui pois 
				 * considera-se que o arquivo foi gerado corretamente.
				 */
				this.quantidadeArquivosGerados++;
			}
			catch(IOException e){
				
			}
		}
	}

	/**
	 * Cria o trailer do arquivo
	 * 
	 * @param jornaleiro
	 * @return
	 */
	private EMS0197Trailer createTrailer(PDV jornaleiro) {
		EMS0197Trailer outTrailer = new EMS0197Trailer();
		outTrailer.setNumeroCota(jornaleiro.getCota().getNumeroCota().toString());
		outTrailer.setQtdeRegTipo2(jornaleiro.getCota().getMovimentoEstoqueCotas().size());
		return outTrailer;
	}

	/**
	 * Cria os detalhes do arquivo
	 * 
	 * @param jornaleiro
	 * @param movimentoEstoqueCota
	 * @return
	 */
	private EMS0197Detalhe createDetalhes(PDV jornaleiro,
			ProdutoEdicao produtoEdicao, BigInteger qtd) {
		
		EMS0197Detalhe outDetalhe = new EMS0197Detalhe();
		
		outDetalhe.setCodigoCota(jornaleiro.getCota().getNumeroCota().toString());
		
		outDetalhe.setCodProduto(produtoEdicao.getProduto().getCodigo().toString());
		outDetalhe.setNumEdicao(produtoEdicao.getNumeroEdicao().toString());
		outDetalhe.setNomeProduto(produtoEdicao.getProduto().getNome());
		outDetalhe.setCodigoDeBarrasPE(produtoEdicao.getCodigoDeBarras());
		outDetalhe.setPrecoCustoPE(produtoEdicao.getPrecoCusto().toString());
		outDetalhe.setPrecoVendaPE(produtoEdicao.getPrecoVenda().toString());												
		
		outDetalhe.setDescontoPE(
				descontoService.obterValorDescontoPorCotaProdutoEdicao(null, jornaleiro.getCota(), produtoEdicao).toString());		
		
		outDetalhe.setQtdeMEC(qtd.toString());
		
		return outDetalhe;
	}

	/**
	 * Cria o cabeçalho para o arquivo
	 * 
	 * @param jornaleiro
	 * @return
	 */
	private EMS0197Header createHeader(PDV jornaleiro) {
		
		EMS0197Header outHeader = new EMS0197Header();
		outHeader.setCodigoCota(jornaleiro.getCota().getNumeroCota().toString());
		outHeader.setNomePDV(jornaleiro.getNome());
		outHeader.setDataLctoDistrib(sdf.format(dataLctoDistrib));
		
		return outHeader;
	}


	/**
	 * Obtém lista de PDVs principais
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PDV> obterPDVsPricipais() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT DISTINCT pdv FROM PDV pdv");
		sql.append(" JOIN FETCH pdv.cota cotaPDV ");
		sql.append(" JOIN FETCH cotaPDV.movimentoEstoqueCotas mec ");
		sql.append(" LEFT JOIN FETCH mec.produtoEdicao pe ");
		sql.append(" LEFT JOIN FETCH pe.produto pd ");
		sql.append(" LEFT JOIN pe.lancamentos lan ");
		sql.append(" LEFT JOIN mec.tipoMovimento tip ");
		sql.append(" WHERE pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" AND	lan.dataLancamentoDistribuidor = :dataInformada");
		sql.append(" AND	tip.grupoMovimentoEstoque='RECEBIMENTO_REPARTE'");
		sql.append(" ORDER BY cotaPDV.numeroCota");
		
		Query query = getSession().createQuery(sql.toString());
	    query.setParameter("dataInformada", this.dataLctoDistrib);

		return (List<PDV>) query.list();
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
				

	/**
	 * Retorna a quantidade de arquivos gerados apos o processamento.
	 * 
	 * @return
	 */
	public int getQuantidadeArquivosGerados() {
		return quantidadeArquivosGerados;
	}
	
}

