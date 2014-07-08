<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/relatorioGarantias.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script> 
	
	
	<style type="text/css">
		#dialog-detalhe-garantia fieldset{width:800px!important;}
	</style>

</head>

<body>

	<div class="areaBts">
		<div class="area">
			<div id="relatorioGarantias-fileExport" style="display: none; float:right;">
				<span class="bt_arq" >
					<a id="relatorioGarantias-btnImprimirXLS" href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarTodasGarantias?fileType=XLS" title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
		
        		<span id="relatorioGarantias-btnImprimir" class="bt_arq" >
        			<a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarTodasGarantias?fileType=PDF" title="Imprimir Relatório de Garantias" >
        				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir Relatório de Garantias" hspace="5" border="0" />
        			</a>
        		</span>
        	</div>
        </div>
    </div>
    <div class="linha_separa_fields">&nbsp;</div>
<!-- FILTRO DA BUSCA-->
<!-- <div class="container"> -->

<form id="relatorioGarantiasForm">
     <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Garantias</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="98">Tipo de Garantia:</td>
	                <td width="203">
	                	<select name="filtro.tipoGarantia" id="selectTipoGarantia" onchange="relatorioGarantiasController.hideGrids()" style="width:180px;">
		                	<option value="">Todas</option>
		                	<c:forEach varStatus="counter" var="tipoGarantia"
	                                        items="${listaTiposGarantia}">
	                                        <option value="${tipoGarantia.key}">${tipoGarantia.value}</option>
	                        </c:forEach>
	                	</select>
	                </td>
	                <td width="39">Status:</td>
	                <td width="478">
	                	<select name="filtro.statusGarantia" id="selectStatusGarantia" style="width:180px;">
			                
			                <option value="">Todos</option>
		                	<c:forEach varStatus="counter" var="tipoStatusGarantia"
	                                        items="${listaTiposStatusGarantia}">
	                                        <option value="${tipoStatusGarantia.key}">${tipoStatusGarantia.value}</option>
	                        </c:forEach>
	              		</select>
	              </td>
	              <td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioGarantiasController.pesquisar()">Pesquisar</a></span></td>
	            </tr>
	        </table>
      </fieldset>
</form>     
 
      <input type="hidden" id=valorTotalGarantiaslHidden />
 
      <div class="linha_separa_fields">&nbsp;</div>
      
      <!-- RESULTADO DA BUSCA-->
      <fieldset class="classFieldset">
       	  <legend>Garantias Cadastradas</legend>
        	<div class="grids">
            	<div id="todasGarantias" style="display:none;">
           			<table class="relatorioTodasGarantiasGrid"></table>	
          		</div>
          	</div>
          	
          	<div class="grids">
            	<div id="garantiasEspecificas" style="display:none;">
            		<table class="relatorioGarantiaGrid"></table>
           			<table width="950" border="0" cellspacing="1" cellpadding="1">
						<tr>
		                	<td width="277">
					            <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=XLS">Arquivo</a></span>
								<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=PDF">Imprimir</a></span>
	          				</td>
	          			</tr>
	          		</table>
           		</div>
         	</div>
   
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
<!-- </div> -->

<!--Detalhe Garantia-->

<div id="dialog-detalhe-garantia" title="Detalhes da Garantia" style="display:none;">
	<fieldset>
    	<legend id="detalheGarantias-legenda"></legend>
        <table class="garantiaDetalheGrid"></table>
        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=XLS">Arquivo</a></span>
		<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/relatorioGarantias/exportPesquisarGarantia?fileType=PDF">Imprimir</a></span>
    
    <div style="float:right; margin-top:10px; margin-right:270px;">
        <table>
            <tr>
	        <td><strong>Total R$:&nbsp;</strong></td>
	        <td id="totalGarantia" ></td>
	        </tr>
        </table>
    </div>
    
    </fieldset>
</div>



<script type="text/javascript">
		$(function(){
			relatorioGarantiasController.init();
		});
</script>

</body>