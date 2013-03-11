package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dao.RankingSegmentoDAO;
import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link AjusteFinalReparte} Próximo Processo:
 * {@link GravarReparteFinalCota}
 * </p>
 */
public class ReparteComplementarPorCota extends ProcessoAbstrato {

	private List<Ordenador> ordenadorList = new ArrayList<Ordenador>();
	private RankingSegmentoDAO rankingSegmentoDAO; 
	private List<Long> cotasIdList;
    public ReparteComplementarPorCota(Estudo estudo) {
	super(estudo);

	this.rankingSegmentoDAO = new RankingSegmentoDAO();
	cotasIdList = rankingSegmentoDAO.getCotasOrdenadasMaiorMenor(getEstudo().getCotas(),getEstudo().getProduto());
	
//	Prioridade de recebimento de reparte:
	
	/*
	 * A: As que nao receberam as edicoes-base, porem receberam a edicao aberta, caso exista, 
	 * da maior para menor no ranking de segmento da publicação(cotas SH);
	 */
	
	ordenadorList.add(new Ordenador("A"){
		
		@Override
		void filtrar(List<Cota> cotaListRecebeComplementar) {

			List<Cota> cList = new ArrayList<Cota>();
			for (Cota cota : cotaListRecebeComplementar) {
				if(cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)
						&& cota.isCotaSoRecebeuEdicaoAberta()){

					/*
					 * Se idProduto das recebidas é igual ao idProduto das edicoes-base
					 * então recebeu edicaoAberta
					 */
					for(ProdutoEdicaoBase pe:cota.getEdicoesRecebidas()){
						for(ProdutoEdicaoBase edBase:getEstudo().getEdicoesBase()){
							if(pe.getIdProduto().equals(edBase.getIdProduto())){
								cList.add(cota);
							}
						}
					}
					
				}
			}
			
			realizarReparteComplementar(cList);
			
		}
	});
	
	/*
	 * B: As que n�o receberam as edi��es-base, da maior para a menor no ranking de segmento da publica��o (cotas SH);
	 */
	ordenadorList.add(new Ordenador("B"){
		@Override
		void filtrar(List<Cota> cotaListRecebeComplementar) {
			
			List<Cota> cList = new ArrayList<Cota>();
			for (Cota cota : cotaListRecebeComplementar) {
				if(cota.getClassificacao().equals(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga)
						&& cota.isCotaSoRecebeuEdicaoAberta()){
					
					for(ProdutoEdicaoBase pe:cota.getEdicoesRecebidas()){
						for(ProdutoEdicaoBase edBase:getEstudo().getEdicoesBase()){
							if(pe.getIdProduto().equals(edBase.getIdProduto())){
								continue;
							}
						}
					}
					
					cList.add(cota);
				}
			}

			realizarReparteComplementar(cList);
		}
	});
	
	/*
	 * C: As que receberam 1 ediçãoo das ediçõees-base, da maior para a menor no ranking de segmento da publicação (cotas VZ);
	 */
	ordenadorList.add(new Ordenador("C"){
		@Override
		void filtrar(List<Cota> cotaListRecebeComplementar) {
			List<Cota> cList = new ArrayList<Cota>();
			for (Cota cota : cotaListRecebeComplementar) {
				if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
						&& getQtdeEdicoesBaseRecebida(cota)==1){
					cList.add(cota);
				}
			}
			
			realizarReparteComplementar(cList);
		}
	});
	
	/*
	 * D: As que receberam 2 edi��es das edi��es-base, da maior para a menor no ranking de segmento da publica��o (cotas VZ);
	 */
	ordenadorList.add(new Ordenador("D"){
		@Override
		void filtrar(List<Cota> cotaListRecebeComplementar) {
			List<Cota> cList = new ArrayList<Cota>();
			for (Cota cota : cotaListRecebeComplementar) {
				if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
						&& getQtdeEdicoesBaseRecebida(cota)==2){
					cList.add(cota);
				}
			}
			
			realizarReparteComplementar(cList);
		}
	});
	
	/*
	 * E: As que receberam 3 ou mais edi��es das edi��es-base, da maior para a menor no ranking de segmento da publica��o (cotas VZ).
	 */
	ordenadorList.add(new Ordenador("E"){
		@Override
		void filtrar(List<Cota> cotaListRecebeComplementar) {
			List<Cota> cList = new ArrayList<Cota>();
			for (Cota cota : cotaListRecebeComplementar) {
				if(cota.getClassificacao().equals(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga)
						&& getQtdeEdicoesBaseRecebida(cota)>=3){
					cList.add(cota);
				}
			}

			realizarReparteComplementar(cList);
		}
	});
	
    }

    @Override
    protected void executarProcesso() {
    	
//    	1)	Listar todas as cotas ativas que n�o entraram no Estudo Normal, considerando-se as exclus�es por CLASSIFICA��O, SEGMENTO e MIX; 
//    	2)	Excluir Cotas que n�o recebem Complementar ( marcado no Cadastro de Cotas )
    	List<Cota> cotaListRecebeComplementar = new ArrayList<Cota>();
    	List<Cota> cotaListOrdenada = new ArrayList<Cota>();
    	
    	
		for(Cota cota:getEstudo().getCotas()){
			if( cota.isRecebeReparteComplementar()==false && (
					!cota.getClassificacao().equals(ClassificacaoCota.BancaSemClassificacaoDaPublicacao)
					&& !cota.getClassificacao().equals(ClassificacaoCota.BancaQueRecebemDeterminadoSegmento)
					&& !cota.getClassificacao().equals(ClassificacaoCota.CotaMix))){
				cotaListRecebeComplementar.add(cota);
			}
		}
   	
    	
//    	3)	Orden�-las na seguinte prioridade de recebimento de reparte:
		loop:while(getEstudo().getReparteComplementar().compareTo(BigDecimal.ZERO)==1){
			for(Ordenador ordenador:this.ordenadorList){
				ordenador.filtrar(cotaListRecebeComplementar);
				if(getEstudo().getReparteComplementar().compareTo(BigDecimal.ZERO)<=0){
					break loop;
				}
			}
		}
    	
    	/*
    	 * 4)	As bancas receberao a quantidade de reparte por banca definido no estudo (default = 2 exemplares)
    	 *  ou 1 pacote-padr�o se a distribuicao for por multiplos 
    	 * ate acabar o reparte complementar, sempre considerando-se a prioriza��o acima. 
    	 * 
    	 * Caso haja saldo a distribuir e todas as bancas selecionadas j� receberam, 
    	 * enviar 1 exemplar ou 1 pacote-padr�o se a distribui��o for por m�ltiplos para as bancas do estudo normal, 
    	 * da maior para a menor at� finalizar o estoque. 
    	 * N�o incluir bancas marcadas com `FX` `MX` e `MM` nessa redistribui��o;
    	 */
    	
    		
    		while(getEstudo().getReparteDistribuir().compareTo(BigDecimal.ZERO) == 1){
    			for(Cota c:getEstudo().getCotas()){
    				if(!c.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    						&& !c.getClassificacao().equals(ClassificacaoCota.CotaMix)
    						&& !c.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)){
    					
    					//TODO: FAZER REDISTRIBUICAO
//    			5)	Marcar cotas com 'CP'    					
    					c.setClassificacao(ClassificacaoCota.BancaEstudoComplementar);
    					c.setReparteCalculado(c.getReparteCalculado().add(BigDecimal.ONE));
    					getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(BigDecimal.ONE));
    				}
    			}
    		}
    		
    }
    
    public int getQtdeEdicoesBaseRecebida(Cota cota){
    	
    	List<ProdutoEdicao> edicoesRecebidas = cota.getEdicoesRecebidas();
    	List<ProdutoEdicaoBase> edicoesBase = getEstudo().getEdicoesBase();
    	
    	int qtdeEdicoesBaseRecebidas=0;
    	
    	for (ProdutoEdicaoBase produtoEdicaoBase : edicoesBase) {
			for (ProdutoEdicaoBase edRec : edicoesRecebidas) {
				if(edRec.getId().equals(produtoEdicaoBase.getId())){
					qtdeEdicoesBaseRecebidas++;
				}
			}
		}
    	return qtdeEdicoesBaseRecebidas;
    }
   
    
    
    
    private void realizarReparteComplementar(List<Cota> cList) {
		/*
		 4)	As bancas receberão 
		 	a quantidade de reparte por banca definido no estudo (default = 2 exemplares) 
		 	ou 1 pacote-padrão se a distribuição for por múltiplos até acabar o reparte complementar, 
		 	sempre considerando-se a priorização acima.
		 */
    	for(Long id:cotasIdList){
			for (Cota c : cList) {
				if(c.getId().equals(id)){
					if(getEstudo().isDistribuicaoPorMultiplos()){
						c.setReparteCalculado(c.getReparteCalculado().add(getEstudo().getPacotePadrao()));
					}else{
						
					}
					getEstudo().setReparteComplementar(getEstudo().getReparteComplementar().subtract(BigDecimal.ONE));

					if(getEstudo().getReparteComplementar().compareTo(BigDecimal.ZERO)<=0){
						return;
					}
				}
			}
		}
	}
   
//FIXME talvez usar o Guava do google para ordenar? 
   private abstract class Ordenador{
	   private String name;
	   public Ordenador(String name){
		   this.name=name;
	   }
    	abstract void filtrar(List<Cota> cotaListRecebeComplementar) ;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    	
    }
    
}
