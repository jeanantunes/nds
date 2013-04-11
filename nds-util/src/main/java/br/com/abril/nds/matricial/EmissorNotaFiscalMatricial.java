package br.com.abril.nds.matricial;


import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;


public class EmissorNotaFiscalMatricial {


	private StringBuffer saida = new StringBuffer();

	public EmissorNotaFiscalMatricial(StringBuffer saida) {
		this.saida = saida;
	}

	
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		new EmissorNotaFiscalMatricial(sb).adicionarCompleteTraco("12", "500,66");
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
		adicionar(textoColuna, valor, ConstantesImpressao.LARGURA_FOLHA_MATRICIAL, ConstantesImpressao.ESPACO);
	}
	
	public void adicionarCompleteTraco(String textoColuna, String valor) {
		adicionar(textoColuna, valor, ConstantesImpressao.LARGURA_FOLHA_MATRICIAL, ConstantesImpressao.TRACO);
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
    
		adicionar(ConstantesImpressao.ESC + ConstantesImpressao.E + texto + ConstantesImpressao.ESC + ConstantesImpressao.F);

	}

    public void inic(){
        //reset default settings
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.AT);
    }

    //define tabela de caracteres
    
    public void setCharacterSet(String charset) {
        //assign character table
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.PARENTHESIS_LEFT + ConstantesImpressao.t + ConstantesImpressao.ARGUMENT_3 + ConstantesImpressao.ARGUMENT_0 + ConstantesImpressao.ARGUMENT_1 + charset + ConstantesImpressao.ARGUMENT_0);
        //adicionar("\u001B" + "(t" + (char)3 + (char)0 + (char)0 + (char)25 + (char)0);
        
        
        //select character table
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.t + ConstantesImpressao.ARGUMENT_1);
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
          
        adicionar(ConstantesImpressao.CR + ConstantesImpressao.LINE_FEED);//according to epson esc/p ref. manual always send carriage return before line feed
    }
    
    
    //pula linha
    public void quebrarLinhaEscape() {
          
        adicionar(ConstantesImpressao.CARACTER_INDENT_LINEFEED_SCAPE);
    }
    

    /**
     * Ejeta folha
     */
    public void ejectFolha() {
        //post: ejects single sheet
        adicionar(ConstantesImpressao.CR + ConstantesImpressao.FF );
        
    }
    public void comecoPag(){
        adicionar(ConstantesImpressao.CR);
    }


    /**
     * pre: centimeters >= 0p
     * post: advances horizontal print position approx. centimeters
     * @param centimeters
     */
    public void avancaHorizontal(float cem) {
        //pre: centimeters >= 0
        //post: advances horizontal print position approx. centimeters
        float inches = cem / ConstantesImpressao.CM_PER_INCH;
        int units_low = (int) (inches * 120) % 256;
        int units_high = (int) (inches * 120) / 256;

        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.BACKSLASH + (char) units_low + (char) units_high);

    }

    /**
     * pre: centimenters >= 0 (cm)
     * post: sets absolute horizontal print position to x centimeters from left margin
     * @param centimeters
     */
    public void avancaAbsHorizontal(float cem) {

        float inches = cem / ConstantesImpressao.CM_PER_INCH;
        int units_low = (int) (inches * 60) % 256;
        int units_high = (int) (inches * 60) / 256;

        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.$ + (char) units_low + (char) units_high);

    }

    public void avancaVertical(float cem) {
        /*
        float inches = cem*256 / CM_PER_INCH;
        adicionar(ESC + J + (char) inches);
         * */
        //pre: centimeters >= 0 (cm)
        //post: advances vertical print position approx. y centimeters (not precise due to truncation)
        float inches = cem / ConstantesImpressao.CM_PER_INCH;
        int units = (int) (inches * 216);

        while (units > 0) {
            char n;
            if (units > ConstantesImpressao.MAX_UNITS)
                n = (char) ConstantesImpressao.MAX_UNITS; //want to move more than range of parameter allows (0 - 255) so move max amount
            else
                n = (char) units; //want to move a distance which fits in range of parameter (0 - 255)

           adicionar(ConstantesImpressao.ESC + ConstantesImpressao.J + (char) n);

           units -= ConstantesImpressao.MAX_UNITS;
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
            adicionar(ConstantesImpressao.TAB+"");
    }

    /**
     * pre: columnsLeft > 0 && <= 255, columnsRight > 0 && <= 255
     * post: sets left margin to columnsLeft columns and right margin to columnsRight columns
     * @param columnsLeft
     * @param columnsRight
     */
    public void setMargem(int colunaEsq, int colunaDir) {
        //esquerda
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.l + (char) colunaEsq);

        //direita
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.Q + (char) colunaDir);
    }


    /**
     * This command sets the page length in 1-inch increments only.
     * Set the page length before paper is loaded or when the print position is at the top-ofform
     * position. Otherwise, the current print position becomes the top-of-form position.
     * Setting the page length cancels the top and bottom-margin settings.
     * @param tam
     */
    public void setTamPag(float tam){
        float inches = tam / ConstantesImpressao.CM_PER_INCH;
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.C + ConstantesImpressao.NULL+ (char) inches);
    }
    /**
     * ESC k 0 -Roman
     * ESC k 1 -Sans serif
     * @param fonte
     */
    public void setFonte(int fonte){
        adicionar(ConstantesImpressao.ESC + ConstantesImpressao.k + (char) fonte);
    }

    public void setCondensado(){
         adicionar(ConstantesImpressao.SI);
    }

    public void setRascunho() { //set draft quality printing
        adicionar(ConstantesImpressao.ESC+ ConstantesImpressao.x +(char) 48);
    }

    public void setLQ() { //set letter quality printing
        adicionar(ConstantesImpressao.ESC+ ConstantesImpressao.x +(char) 49);
    }
    public void set10CPI() { //10 characters per inch (condensed available)
        adicionar(ConstantesImpressao.ESC+ ConstantesImpressao.P);
    }

    public void imprimir(String saida) throws PrintException, IOException {
    	
    	saida = saida.replaceAll(ConstantesImpressao.CARACTER_INDENT_LINEFEED_SCAPE, ConstantesImpressao.CR + ConstantesImpressao.LINE_FEED);
    	System.out.println("#########>>>>>>>>>>>>>>>");
		System.out.println(saida);
		System.out.println("<<<<<<<<<<<<<<<<###############");
		
		imprimir(saida.getBytes(), PrinterJob.getPrinterJob().getPrintService());
    }
    
    public void imprimir(byte[] saida, PrintService impressora) throws PrintException, IOException {
    	
		InputStream ps = null;
		ps = new ByteArrayInputStream(saida);
		
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		DocPrintJob job = null;
		
		job = impressora.createPrintJob();
		
		MatricialThreadUtil pjDone = new MatricialThreadUtil(job);
		Doc doc = new SimpleDoc(ps, flavor, null);
		
		job.print(doc, null);
		
		// AGUARDA A CONCLUSAO DO TRABALHO
		pjDone.waitForDone();
		
		ps.close();
    }
    
    public void imprimir() throws PrintException, IOException {
		
		if(this.saida == null || "".equals(this.saida)){
			this.saida.append("parameter NULL");
			return;
		}
		
		imprimir(this.saida.toString());
	}

}
