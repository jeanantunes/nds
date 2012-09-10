<div class="divConteudoEntregaBanca">
	<table width="399" border="0" cellspacing="1" cellpadding="1">
	  <tr>
	    <td width="130">Termo Adesão:</td>
	    
	    <td width="20">
	    	<input type="checkbox" id="${param.tela}utilizaTermoAdesao"
	    		   onclick="DISTRIB_COTA.mostrarEsconderDivUtilizaArquivo('divUtilizaTermoAdesao', 'divTermoAdesaoRecebido',
											   							  'utilizaTermoAdesao', 'termoAdesaoRecebido')" />
	    </td>
	    <td width="245" height="39">
	    	<div class="divUtilizaTermoAdesao">
	    		<span class="bt_imprimir" style="display:block;">
	    			<a href="javascript:;" target="_blank"
	    			   onclick="DISTRIB_COTA.downloadTermoAdesao();">Termo</a>
	    		</span>
    		</div>
	    </td>
	  </tr>
  	</table>
  	<div class="divUtilizaTermoAdesao">
	  	<table width="399" border="0" cellspacing="1" cellpadding="1">
		  <tr>
		    <td width="130">Termo Adesão Recebido?</td>
		    <td width="265">
		    	<input type="checkbox" id="${param.tela}termoAdesaoRecebido"
		    		   onclick="DISTRIB_COTA.mostrarEsconderDivArquivoRecebido('divTermoAdesaoRecebido', 'termoAdesaoRecebido')" />
		    </td>
		  </tr>
		</table>
		
		<div class="divTermoAdesaoRecebido">
			<table width="399" border="0" cellspacing="1" cellpadding="1">
			  <tr>
			    <td width="130">Arquivo:</td>
			    <td width="265">
			    
					<form action="<c:url value='/cadastro/cota/uploadTermoAdesao' />" id="formUploadTermoAdesao"
						  method="post" enctype="multipart/form-data" >		
									
						<input type="hidden" name="formUploadAjax" value="true" />
						<input type="hidden" name="numCotaUpload" />
						
						<div id="uploadTermo">
							<input name="uploadedFileTermo" type="file" id="uploadedFileTermo"
								   size="30" onchange="DISTRIB_COTA.submitForm('formUploadTermoAdesao')" />
						</div>
					</form>
			    
			    </td>
			  </tr>
			  <tr>
			    <td width="130">&nbsp;</td>
			    <td width="265">
			    	<span id="nomeArquivoTermoAdesao"></span>
			    </td>
			  </tr>
			</table>
		</div>
	</div>
	<table width="399" border="0" cellspacing="1" cellpadding="1">
	  
	  <tr>
	    <td width="130">Percentual Faturamento:</td>
	    <td width="265">
	    	<input id="${param.tela}percentualFaturamentoEntregaBanca" name="percentualFaturamento"
	    		   type="text" style="width: 70px; text-align: right;" />
		</td>
	  </tr>
	  <tr>
	    <td width="130">Taxa Fixa R$</td>
	    <td width="265">
	    	<input id="${param.tela}taxaFixaEntregaBanca" name="taxaFixa"
	    		   type="text" style="width: 70px; text-align: right;" />
	    </td>
	  </tr>
	  <tr>
	    <td width="130">Período Carência:</td>
	    <td width="265">
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td width="27%">
		        	<input id="${param.tela}inicioPeriodoCarenciaEntregaBanca"
						   name="inicioPeriodoCarencia" type="text" style="width: 70px" />		        	
		        </td>
		        <td width="6%">Até</td>
		        <td width="34%">
		        	<input id="${param.tela}fimPeriodoCarenciaEntregaBanca"
						   name="fimPeriodoCarencia" type="text" style="width: 70px" />
		        </td>
		      </tr>
	    	</table>
	    </td>
	  </tr>
	</table>
</div>