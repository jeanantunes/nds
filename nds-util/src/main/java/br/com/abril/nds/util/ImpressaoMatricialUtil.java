package br.com.abril.nds.util;


import java.io.IOException;

import javax.print.PrintException;
import javax.print.PrintService;



public class ImpressaoMatricialUtil {


    private StringBuffer saida;

	public ImpressaoMatricialUtil(StringBuffer saida) {
		this.saida = saida;
	}

	
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		new ImpressaoMatricialUtil(sb).adicionarCompleteTraco("12", "500,66");
		System.out.println(sb);
		System.out.println(sb.length());
	}
	
	public void adicionar(String texto) {
		this.saida.append(texto);
	}
	
	public String preencher(String texto, String caracterPreenchimento, int qtd){
		String result = texto;
		for(int i=0; i < qtd; i++){
			result+=caracterPreenchimento;
		}
		return result;
	}
	
	public void adicionarCompleteEspaco(String textoColuna, String valor) {
		adicionar(textoColuna, valor, ImpressaoConstantes.LARGURA_FOLHA_MATRICIAL, ImpressaoConstantes.ESPACO);
	}
	
	public void adicionarCompleteTraco(String textoColuna, String valor) {
		adicionar(textoColuna, valor, ImpressaoConstantes.LARGURA_FOLHA_MATRICIAL, ImpressaoConstantes.TRACO);
	}
	
	public void adicionar(String textoColuna, String valor, int tamanhoColuna, String caracterPreenchimento) {
		if(textoColuna != null){

			if((textoColuna.length() + valor.length()) > tamanhoColuna){//Maior que o tamanho total
				adicionar(textoColuna, tamanhoColuna - valor.length());//trunca conforme o tamanho
			}else{
				String preencher = preencher(textoColuna, caracterPreenchimento, tamanhoColuna-(textoColuna.length()+valor.length()));
				adicionar(preencher);
			}
			adicionar(valor);
		}
	}
	
	public void adicionar(String texto, int tamanhoColuna) {
		if(texto != null){
			if(texto.length() > tamanhoColuna){
				texto = texto.substring(0, tamanhoColuna);
			}
		}
		tamanhoColuna = tamanhoColuna - texto.length();
		this.saida.append(texto).append(getEspaco(tamanhoColuna));
	}

	
	public void darEspaco(int qtde) {
		this.saida.append(getEspaco(qtde));
	}

	public String getEspaco(int qtde) {
		String result = "";
		for (int x2 = 1; x2 <= qtde; x2++) {
			result += " ";
		}

		return result;
	}

    //negrito
	public void adicionarNegrito(String texto) {
    
		adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.E + texto + ImpressaoConstantes.ESC + ImpressaoConstantes.F);

	}

    public void inic(){
        //reset default settings
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.AT);
    }

    //define tabela de caracteres
    
    public void setCharacterSet(String charset) {
        //assign character table
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.PARENTHESIS_LEFT + ImpressaoConstantes.t + ImpressaoConstantes.ARGUMENT_3 + ImpressaoConstantes.ARGUMENT_0 + ImpressaoConstantes.ARGUMENT_1 + charset + ImpressaoConstantes.ARGUMENT_0);
        //adicionar("\u001B" + "(t" + (char)3 + (char)0 + (char)0 + (char)25 + (char)0);
        
        
        //select character table
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.t + ImpressaoConstantes.ARGUMENT_1);
        //adicionar("\u001B" + "t" + (char)0);
        //Character.toString( ESC);
        
    }
	
    public void quebrarLinha(int qtde) {
		for (int x1 = 1; x1 <= qtde; x1++) {
			quebrarLinha();
		}
	}
    
    public void quebrarLinhaEscape(int qtde) {
		for (int x1 = 1; x1 <= qtde; x1++) {
			quebrarLinhaEscape();
		}
	}
	
    //pula linha
    public void quebrarLinha() {
          
        adicionar(ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);//according to epson esc/p ref. manual always send carriage return before line feed
    }
    
    
    //pula linha
    public void quebrarLinhaEscape() {
          
        adicionar(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE);
    }
    

    /**
     * Ejeta folha
     */
    public void ejectFolha() {
        //post: ejects single sheet
        adicionar(ImpressaoConstantes.CR + ImpressaoConstantes.FF );
        
    }
    public void comecoPag(){
        adicionar(ImpressaoConstantes.CR);
    }


    /**
     * pre: centimeters >= 0p
     * post: advances horizontal print position approx. centimeters
     * @param centimeters
     */
    public void avancaHorizontal(float cem) {
        //pre: centimeters >= 0
        //post: advances horizontal print position approx. centimeters
        float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        int units_low = (int) (inches * 120) % 256;
        int units_high = (int) (inches * 120) / 256;

        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.BACKSLASH + (char) units_low + (char) units_high);

    }

    /**
     * pre: centimenters >= 0 (cm)
     * post: sets absolute horizontal print position to x centimeters from left margin
     * @param centimeters
     */
    public void avancaAbsHorizontal(float cem) {

        float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        int units_low = (int) (inches * 60) % 256;
        int units_high = (int) (inches * 60) / 256;

        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.$ + (char) units_low + (char) units_high);

    }

    public void avancaVertical(float cem) {
        /*
        float inches = cem*256 / CM_PER_INCH;
        adicionar(ESC + J + (char) inches);
         * */
        //pre: centimeters >= 0 (cm)
        //post: advances vertical print position approx. y centimeters (not precise due to truncation)
        float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        int units = (int) (inches * 216);

        while (units > 0) {
            char n;
            if (units > ImpressaoConstantes.MAX_UNITS)
                n = (char) ImpressaoConstantes.MAX_UNITS; //want to move more than range of parameter allows (0 - 255) so move max amount
            else
                n = (char) units; //want to move a distance which fits in range of parameter (0 - 255)

           adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.J + (char) n);

           units -= ImpressaoConstantes.MAX_UNITS;
        }

    }

    /**
     * //pre: tabs >= 0
     * //post: performs horizontal tabs tabs number of times
     * @param tabs
     */
    public void horizontalTab(int tabs) {
        //pre: tabs >= 0
        //post: performs horizontal tabs tabs number of times
        for (int i = 0; i < tabs; i++)
            adicionar(ImpressaoConstantes.TAB+"");
    }

    /**
     * pre: columnsLeft > 0 && <= 255, columnsRight > 0 && <= 255
     * post: sets left margin to columnsLeft columns and right margin to columnsRight columns
     * @param columnsLeft
     * @param columnsRight
     */
    public void setMargem(int colunaEsq, int colunaDir) {
        //esquerda
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.l + (char) colunaEsq);

        //direita
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.Q + (char) colunaDir);
    }


    /**
     * This command sets the page length in 1-inch increments only.
     * Set the page length before paper is loaded or when the print position is at the top-ofform
     * position. Otherwise, the current print position becomes the top-of-form position.
     * Setting the page length cancels the top and bottom-margin settings.
     * @param tam
     */
    public void setTamPag(float tam){
        float inches = tam / ImpressaoConstantes.CM_PER_INCH;
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.C + ImpressaoConstantes.NULL+ (char) inches);
    }
    /**
     * ESC k 0 -Roman
     * ESC k 1 -Sans serif
     * @param fonte
     */
    public void setFonte(int fonte){
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.k + (char) fonte);
    }

    public void setCondensado(){
         adicionar(ImpressaoConstantes.SI);
    }

    public void setRascunho() { //set draft quality printing
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.x +(char) 48);
    }

    public void setLQ() { //set letter quality printing
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.x +(char) 49);
    }
    public void set10CPI() { //10 characters per inch (condensed available)
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.P);
    }

    public void imprimirImpressoraPadrao(String saida) throws PrintException, IOException {
    	
    	saida = replaceComandosImpressao(saida);
		
		new ImpressoraUtil().imprimir(saida.getBytes(), ImpressoraUtil.getImpressoraLocalConfiguradaPadrao());
    }


    public void imprimir() throws PrintException, IOException {
		
        if (this.saida == null) {
			return;
		}
		
		imprimirImpressoraPadrao(this.saida.toString());
	}

    public void imprimir(PrintService impressora) throws PrintException, IOException {
		
        if (this.saida == null) {
            return;
		}
		
		new ImpressoraUtil().imprimir(replaceComandosImpressao(saida.toString()).getBytes(), impressora);
	}
    
    private String replaceComandosImpressao(String saida) {
		saida = saida.replaceAll(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE, ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);
    	System.out.println("#########>>>>>>>>>>>>>>>");
		System.out.println(saida);
		System.out.println("<<<<<<<<<<<<<<<<###############");
		return saida;
	}
}
