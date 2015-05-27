package br.com.abril.nds.dto;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;


public class AnaliseNormalDTO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnaliseNormalDTO.class);
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private int cota;
	
	@Export(label = "Classificacao", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private ClassificacaoEspectativaFaturamento classificacao;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nome;
	
	@Export(label = "Quantidade PDVs", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private int npdv;
	
	@Export(label = "Reparte Sugerido", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private BigInteger reparteSugerido;
	
	@Export(label = "Legenda", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private TipoDistribuicaoCota leg;
	
	@Export(label = "Media Venda", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private double mediaVenda;
	
    @Export(label = "Último Reparte", alignment = Alignment.LEFT, exhibitionOrder = 8)
	private double ultimoReparte;
	
	@Export(label = "Reparte 6", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private Double reparte6;
	
	@Export(label = "Reparte 5", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private Double reparte5;
	
	@Export(label = "Reparte 4", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private Double reparte4;
	
	@Export(label = "Reparte 3", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private Double reparte3;
	
	@Export(label = "Reparte 2", alignment=Alignment.LEFT, exhibitionOrder = 17)
	private Double reparte2;
	
	@Export(label = "Reparte 1", alignment=Alignment.LEFT, exhibitionOrder = 19)
	private Double reparte1;
	
	
	@Export(label = "Venda 6", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private Double venda6;
	
	@Export(label = "Venda 5", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private Double venda5;
	
	@Export(label = "Venda 4", alignment=Alignment.LEFT, exhibitionOrder = 14)
	private Double venda4;
	
	@Export(label = "Venda 3", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private Double venda3;
	
	@Export(label = "Venda 2", alignment=Alignment.LEFT, exhibitionOrder = 18)
	private Double venda2;
	
	@Export(label = "Venda 1", alignment=Alignment.LEFT, exhibitionOrder = 20)
	private Double venda1;
	
	
	public void setCota(int cota) {
		this.cota = cota;
	}
	public void setClassificacao(ClassificacaoEspectativaFaturamento classificacao) {
		this.classificacao = classificacao;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setNpdv(int npdv) {
		this.npdv = npdv;
	}
	public void setReparteSugerido(BigInteger reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}
	public void setLeg(TipoDistribuicaoCota leg) {
		this.leg = leg;
	}
	public void setMediaVenda(double mediaVenda) {
		this.mediaVenda = mediaVenda;
	}
	public void setUltimoReparte(double ultimoReparte) {
		this.ultimoReparte = ultimoReparte;
	}
	
	public void setRepartes(Object[] tuple, int inicio) {
		double limit = Math.floor((tuple.length-inicio)/2.0);
		for (int i = 0; i < limit; i++) {
			setAtt("reparte", 6-i, toDouble(tuple[i*2+inicio]));
			setAtt("venda", 6-i, toDouble(tuple[i*2+inicio+1]));
		}
	}
	
	private void setAtt(String baseName, int number, Object value){
		try {
			Field field = this.getClass().getDeclaredField(baseName+number);
			field.setAccessible(true);
			field.set(this, value);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private Double getReparteOuVenda(String baseName, int number){
		try {
			Field field = this.getClass().getDeclaredField(baseName+number);
			field.setAccessible(true);
			return (Double) field.get(this);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	public void popularMedia() {
		int count = 0;
		double venda=0;
		for (int i = 1; i <= 6; i++) {
			Double v = this.getReparteOuVenda("venda", i);
			if(v!=null){
				venda+=v;
				count++;
			}
		}
		mediaVenda=venda/count;
	}
	
	
	private double toDouble(Object o) {
		return o==null?
				0:
				o instanceof Number?
						((Number)o).doubleValue():Double.parseDouble(o.toString());
	}
	public void setEdicoes(List<BigInteger> edicoes) {
		for (int i = 0; i < edicoes.size(); i++) {
			setAtt("edicao", 6-i, Long.valueOf(edicoes.get(i).longValue()));
		}
	}

}
