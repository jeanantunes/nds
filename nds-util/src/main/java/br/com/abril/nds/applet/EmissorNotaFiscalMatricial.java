package br.com.abril.nds.applet;


public class EmissorNotaFiscalMatricial {


	protected StringBuffer destino;

	public EmissorNotaFiscalMatricial(StringBuffer destino) {

		this.destino = destino;
	}

	public void adicionar(String texto) {
		this.destino.append(texto);
	}

	public void darEspaco(int qtde) {
		this.destino.append(getEspaco(qtde));
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
    
		adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.E + texto + ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.F);

	}

    public void inic(){
        //reset default settings
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.AT);
    }

    //define tabela de caracteres
    
    public void setCharacterSet(String charset) {
        //assign character table
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.PARENTHESIS_LEFT + ConstantesAppletImpressao.t + ConstantesAppletImpressao.ARGUMENT_3 + ConstantesAppletImpressao.ARGUMENT_0 + ConstantesAppletImpressao.ARGUMENT_1 + charset + ConstantesAppletImpressao.ARGUMENT_0);
        //adicionar("\u001B" + "(t" + (char)3 + (char)0 + (char)0 + (char)25 + (char)0);
        
        
        //select character table
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.t + ConstantesAppletImpressao.ARGUMENT_1);
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
          
        adicionar(ConstantesAppletImpressao.CR + ConstantesAppletImpressao.LINE_FEED);//according to epson esc/p ref. manual always send carriage return before line feed
    }
    
    
    //pula linha
    public void quebrarLinhaEscape() {
          
        adicionar(ConstantesAppletImpressao.CARACTER_INDENT_LINEFEED_SCAPE);
    }
    

    /**
     * Ejeta folha
     */
    public void ejectFolha() {
        //post: ejects single sheet
        adicionar(ConstantesAppletImpressao.CR + ConstantesAppletImpressao.FF );
        
    }
    public void comecoPag(){
        adicionar(ConstantesAppletImpressao.CR);
    }


    /**
     * pre: centimeters >= 0p
     * post: advances horizontal print position approx. centimeters
     * @param centimeters
     */
    public void avancaHorizontal(float cem) {
        //pre: centimeters >= 0
        //post: advances horizontal print position approx. centimeters
        float inches = cem / ConstantesAppletImpressao.CM_PER_INCH;
        int units_low = (int) (inches * 120) % 256;
        int units_high = (int) (inches * 120) / 256;

        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.BACKSLASH + (char) units_low + (char) units_high);

    }

    /**
     * pre: centimenters >= 0 (cm)
     * post: sets absolute horizontal print position to x centimeters from left margin
     * @param centimeters
     */
    public void avancaAbsHorizontal(float cem) {

        float inches = cem / ConstantesAppletImpressao.CM_PER_INCH;
        int units_low = (int) (inches * 60) % 256;
        int units_high = (int) (inches * 60) / 256;

        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.$ + (char) units_low + (char) units_high);

    }

    public void avancaVertical(float cem) {
        /*
        float inches = cem*256 / CM_PER_INCH;
        adicionar(ESC + J + (char) inches);
         * */
        //pre: centimeters >= 0 (cm)
        //post: advances vertical print position approx. y centimeters (not precise due to truncation)
        float inches = cem / ConstantesAppletImpressao.CM_PER_INCH;
        int units = (int) (inches * 216);

        while (units > 0) {
            char n;
            if (units > ConstantesAppletImpressao.MAX_UNITS)
                n = (char) ConstantesAppletImpressao.MAX_UNITS; //want to move more than range of parameter allows (0 - 255) so move max amount
            else
                n = (char) units; //want to move a distance which fits in range of parameter (0 - 255)

           adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.J + (char) n);

           units -= ConstantesAppletImpressao.MAX_UNITS;
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
            adicionar(ConstantesAppletImpressao.TAB+"");
    }

    /**
     * pre: columnsLeft > 0 && <= 255, columnsRight > 0 && <= 255
     * post: sets left margin to columnsLeft columns and right margin to columnsRight columns
     * @param columnsLeft
     * @param columnsRight
     */
    public void setMargem(int colunaEsq, int colunaDir) {
        //esquerda
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.l + (char) colunaEsq);

        //direita
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.Q + (char) colunaDir);
    }


    /**
     * This command sets the page length in 1-inch increments only.
     * Set the page length before paper is loaded or when the print position is at the top-ofform
     * position. Otherwise, the current print position becomes the top-of-form position.
     * Setting the page length cancels the top and bottom-margin settings.
     * @param tam
     */
    public void setTamPag(float tam){
        float inches = tam / ConstantesAppletImpressao.CM_PER_INCH;
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.C + ConstantesAppletImpressao.NULL+ (char) inches);
    }
    /**
     * ESC k 0 -Roman
     * ESC k 1 -Sans serif
     * @param fonte
     */
    public void setFonte(int fonte){
        adicionar(ConstantesAppletImpressao.ESC + ConstantesAppletImpressao.k + (char) fonte);
    }

    public void setCondensado(){
         adicionar(ConstantesAppletImpressao.SI);
    }

    public void setRascunho() { //set draft quality printing
        adicionar(ConstantesAppletImpressao.ESC+ ConstantesAppletImpressao.x +(char) 48);
    }

    public void setLQ() { //set letter quality printing
        adicionar(ConstantesAppletImpressao.ESC+ ConstantesAppletImpressao.x +(char) 49);
    }
    public void set10CPI() { //10 characters per inch (condensed available)
        adicionar(ConstantesAppletImpressao.ESC+ ConstantesAppletImpressao.P);
    }


}
