<div class="divConteudoEntregaBanca">

    <div class="divImpressaoTermoAdesao">
		<table width="415" border="0" cellspacing="1" cellpadding="1">
		  <tr>
		    <td width="130">Termo Adesão:</td>
		    
		    <td width="20">
		    	<input type="checkbox" id="${param.tela}utilizaTermoAdesao"
		    		   onclick="DISTRIB_COTA.mostrarEsconderDivUtilizaArquivo('divUtilizaTermoAdesao', 'divTermoAdesaoRecebido','utilizaTermoAdesao', 'termoAdesaoRecebido')" />
		    </td>
		    <td width="245" height="39">
		    	<div class="divUtilizaTermoAdesao" style="display:none">
		    		<span class="bt_imprimir">
		    			<a href="javascript:;" onclick="DISTRIB_COTA.downloadTermoAdesao();">Termo</a>
		    		</span>
	    		</div>
		    </td>
		  </tr>
	  	</table>
  	</div>
  	
  	<div class="divUtilizaTermoAdesao" style="display:none">
	  	<table width="415" border="0" cellspacing="1" cellpadding="1">
		  <tr>
		    <td width="130">Termo Adesão Recebido?</td>
		    <td width="265">
		    	<input type="checkbox" id="${param.tela}termoAdesaoRecebido"
		    		   onclick="DISTRIB_COTA.mostrarEsconderDivArquivoRecebido('divTermoAdesaoRecebido', 'termoAdesaoRecebido')" />
		    </td>
		  </tr>
		</table>
		
		<div class="divTermoAdesaoRecebido" style="display:none">
			<table width="415" border="0" cellspacing="1" cellpadding="1">
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
</div>