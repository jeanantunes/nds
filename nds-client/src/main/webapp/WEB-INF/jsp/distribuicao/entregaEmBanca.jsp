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
	
	<table width="415" border="0" cellspacing="1" cellpadding="1">
	
	    <tr>
	
		    <td width="128">Percentual Faturamento:</td>
		    <td width="50">
		    	<input id="${param.tela}percentualFaturamentoEntregaBanca" name="percentualFaturamento"
		    		   type="text" style="width: 40px; text-align: right;" />
			</td>
			
			<td width="100">Base de Cálculo:</td>
			<td>
			
				<!-- Base de Cálculo -->
				<select id="${param.tela}baseCalculo" name="baseCalculo"  style="width:128px">
					<option selected="selected">...</option>
	
					<c:forEach items="${listaBaseCalculo}" var="item">
						<option value="${item.key}">${item.value}</option>
					</c:forEach>
				</select>
					     
			</td>
	
	    </tr>
	
	</table>    
	
	<table width="415" border="0" cellspacing="1" cellpadding="1">

	  <tr>
	    <td width="120">Taxa Fixa R$</td>
	    <td width="265">
	    	<input id="${param.tela}taxaFixaEntregaBanca" name="taxaFixa"
	    		   type="text" style="width: 40px; text-align: right;" />
	    </td>
	  </tr>
	  
	  <tr>
	    <td width="120">Período Carência:</td>
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