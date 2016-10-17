<head>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaConsignadoCota.js"></script>
<script language="javascript" type="text/javascript">
	$(function() {
		consultaConsignadoCotaController.init();
	});
</script>
<style type="text/css">
	#detalhes input{float:left;}
	#detalhes label, #dialog-detalhes label{width:auto !important; line-height:30px ; margin-bottom:0px!important;}
	#dialog-detalhes fieldset{width:800px!important;}
</style>
</head>

<body>

	 <div id="dialog-detalhes" title="Detalhes" style="display:none;">
		<fieldset>
	    	<legend><span name="numeroNomeCotaPopUp" id="numeroNomeCotaPopUp"></span></legend>
	        
			<table class="consignadosCotaDetalhesGrid"></table>
		</fieldset>
	 </div>
	 
	 <div id="dialog-total-consignado-avista" title="Total consignado e a vista" style="display:none;">
		<fieldset>
	    	<legend>Total Consignado e a Vista</legend>
		</fieldset>
	 </div>

	 <div class="areaBts">
	 	<div class="area">
	 		<div class="pesqTodos" style="display:none;">
		 		<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
	 		</div>
	 		
	 		<div class="pesqCota" style="display:none;">
	 			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
	 		</div>
	 	</div>
	 </div>
	 
	 <div class="linha_separa_fields">&nbsp;</div>
	 
        <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	   	    <legend>Pesquisar Consignados Cota
	        </legend>
	        
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			  <tr>
			    <td width="34">Cota:</td>
			    <td width="98"><input type="text" name="codigoCota" id="codigoCota" style="width:60px; float:left; margin-right:5px;" onblur="consultaConsignadoCotaController.pesquisarCota();" />
			    	<input type="hidden" id="valorGrid" name="valorGrid" value="total" />
			    </td>
			    <td width="42">Nome:</td>    
			    <td width="220"><span name="nomeCota" id="nomeCota"></span></td>
			    <td width="77">Fornecedor:</td>
			    <td width="208">    	
			    	<select id="idFornecedor" name="idFornecedor" style="width:200px;" onchange="consultaConsignadoCotaController.detalharTodos(this.value);">
					    <option value="-1">Todos</option>
					    <c:forEach items="${listaFornecedores}" var="fornecedor">
					      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
					    </c:forEach>
					</select>
			    </td>
			    <td width="91">
				    <div id="detalhes" style="display:none;">
			        <table border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td width="15%"><input name="opcaoDetalhe" id="opcaoDetalhe" type="checkbox" /></td>
			            <td width="85%"><label>Detalhar</label></td>
			          </tr>
			        </table>
			      </div>
			    </td>  
			    <td width="139"><span class="bt_novos"><a href="javascript:;"  onclick="consultaConsignadoCotaController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
			  </tr>
	      </table>
      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids">
	      <fieldset class="fieldGrid">
	      	<div class="pesqCota" style="display:none;">
	       	  <legend><span name="numeroNomeCota" id="numeroNomeCota"></span></legend>
	        
	       	  <table class="consignadosCotaGrid"></table>
				
				<span name="totalGeralCota" id="totalGeralCota" ></span>        
	         </div>
	         
	         <div class="pesqTodos" style="display:none;">
	       	  <legend>Consignados</legend>
	        <div class="grids">
	       	  <table class="consignadosGrid"></table>
	         </div>
         </div>
          <br />
         
         
			<div class="tabelaGeralDetalhado" style="display:none;">
				<span name="totalGeralDetalhado" id="totalGeralDetalhado" ></span>
			</div>
			
			<div class="tabelaGeralPorFornecedor" style="display:none;">
				<span name="totalGeralPorFornecedor" id="totalGeralPorFornecedor" ></span>
			</div>
			
		  </fieldset>
     </div>
    
</body>
