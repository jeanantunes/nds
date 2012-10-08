<form id="formDialogPrevisao">
	<div id="dialog-previsao" title="Previsão" style="display: none;">
		<fieldset style="width:750px!important;">
	    	
	    	<legend>Previsão</legend>
	    	
	    	<table class="previsaoGrid"></table>
	        
	        <div id="botoesExportacao">
		        <span class="bt_novos" title="Gerar Arquivo">
		        	<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=PREVISTOS">
		        		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        		Arquivo
		        	</a>
		        </span>
		        <span class="bt_novos" title="Imprimir">
		        	<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=PREVISTOS">
		        		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
		        		Imprimir
		        	</a>
		        </span>
	        </div>
	    </fieldset>
	</div>
</form>

<form id="formDialogBoletosBaixados">
	<div id="dialog-boletos-baixados" title="Boletos Baixados" style="display: none;">
		<fieldset style="width:750px!important;">
		
    		<legend>Boletos Baixados</legend>
    		
    		<table class="boletoBaixadoGrid"></table>
    		
    		<div id="botoesExportacao">
	       		<span class="bt_novos" title="Gerar Arquivo">
	       			<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=BAIXADOS">
	       				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	       				Arquivo
	       			</a>
	       		</span>
	       		<span class="bt_novos" title="Imprimir">
	       			<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=BAIXADOS">
	       				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
	       				Imprimir
	       			</a>
	       		</span>
       		</div>
    	</fieldset>
	</div>
</form>

<form id="formDialogBaixadosRejeitados">
	<div id="dialog-baixados-rejeitados" title="Boletos Rejeitados" style="display: none;">
		<fieldset style="width:750px!important;">
		
    		<legend>Boletos Rejeitados</legend>
    		
    		<table class="boletoRejeitadoGrid"></table>
    		
    		<div id="botoesExportacao">
	       		<span class="bt_novos" title="Gerar Arquivo">
	       			<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=REJEITADOS">
	       				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	       				Arquivo
	       			</a>
	       		</span>
	        	<span class="bt_novos" title="Imprimir">
	        		<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=REJEITADOS">
	        			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
	        			Imprimir
	        		</a>
	        	</span>
        	</div>
   	 	</fieldset>
	</div>
</form>

<form id="formDialogBaixadosDivergentes">
	<div id="dialog-baixados-divergentes" title="Boletos Baixados Divergentes" style="display: none;">
		<fieldset style="width:750px!important;">
		
    		<legend>Boletos com Diverg&ecirc;ncia</legend>
    		
    		<table class="boletoDivergenciaGrid"></table>
    		
    		<div id="botoesExportacao">
	        	<span class="bt_novos" title="Gerar Arquivo">
	        		<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=DIVERGENTES">
	        			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	        			Arquivo
	        		</a>
	        	</span>
	        	<span class="bt_novos" title="Imprimir">
	        		<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=DIVERGENTES">
	        			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
	        			Imprimir
	        		</a>
	        	</span>
        	</div>
    	</fieldset>
	</div>
</form>	

<form id="formDialogInadimplentes">
	<div id="dialog-inadimplentes" title="Inadimplentes" style="display: none;">
		<fieldset style="width:750px!important;">
		
	    	<legend>Inadimplentes</legend>
	    	
	    	<table class="inadimplentesGrid"></table>
	    	
	    	<div id="botoesExportacao">
		        <span class="bt_novos" title="Gerar Arquivo">
		        	<a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=INADIMPLENTES">
		        		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        		Arquivo
		        	</a>
		        </span>
		        <span class="bt_novos" title="Imprimir">
			        <a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=INADIMPLENTES">
				        <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
				        Imprimir
			        </a>
		        </span>
	        </div>
    	</fieldset>
	</div>
</form>

<form id="formDialogTotalBancario">	
	<div id="dialog-total" title="Total Banc&aacute;rio" style="display: none;">
		<fieldset style="width:550px!important;">
	    	
	    	<table class="totalGrid"></table>
	    	
	    	<div id="botoesExportacao">
		        <span class="bt_novos" title="Gerar Arquivo">
			        <a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=XLS&tipoBaixaBoleto=TOTAL_BANCARIO">
				        <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				        Arquivo
			        </a>
		        </span>
		        <span class="bt_novos" title="Imprimir">
			        <a href="${pageContext.request.contextPath}/financeiro/exportarResumoBaixaAutomatica?fileType=PDF&tipoBaixaBoleto=TOTAL_BANCARIO">
				        <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
				        Imprimir
			        </a>
		        </span>
       		</div>
   		</fieldset>
	</div>
</form>