<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaBoletos.js"></script>

<script type="text/javascript">

	var pesquisaCotaConsultaBoletos = new PesquisaCota();

	$(function(){
		consultaBoletosController.init();
	});

	
</script>

</head>

<body>

   
    <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Boletos por Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
            
              <td width="29">Cota:</td>
              <td width="105">
              	<input name="numCota" 
              		   id="numCota" 
              		   type="text"
              		   maxlength="11"
              		   style="width:80px; 
              		   float:left; margin-right:5px;"
              		   onchange="pesquisaCotaConsultaBoletos.pesquisarPorNumeroCota('#numCota', '#descricaoCota');" />
			  </td>
				
			  <td>
			      <input name="descricaoCota" 
			      		 id="descricaoCota" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:130px;"
			      		 onkeyup="pesquisaCotaConsultaBoletos.autoCompletarPorNome('#descricaoCota');" 
			      		 onblur="pesquisaCotaConsultaBoletos.pesquisarPorNomeCota('#numCota', '#descricaoCota');" />
			  </td>
              
              <td width="124">Data de Vencimento:</td>
              <td width="114"><input name="dataDe" id="dataDe" type="text" style="width:80px; float:left; margin-right:5px;" /></td>
              <td width="25">Até:</td>
              <td width="110"><input name="dataAte" id="dataAte" type="text" style="width:80px; float:left; margin-right:5px;" /></td>
              
              <td width="40">Status:</td>
              <td width="98">
                 <select name="status" id="status" style="width:100px;">
                    <c:forEach varStatus="counter" var="status" items="${listaStatusCombo}">
				       <option value="${status.key}">${status.value}</option>
				    </c:forEach>
                 </select>
              </td>
              
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="consultaBoletosController.mostrarGridConsulta();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
	       <fieldset class="classFieldset">
	       
	       	  <legend>Boletos Cadastrados</legend>

		       	<table class="boletosCotaGrid"></table>
		        
				<span class="bt_novos" title="Gerar Arquivo">
					<a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=XLS">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
				</span>
				
				<span class="bt_novos" title="Imprimir">
					<a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=PDF">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir
					</a>
				</span>
	      </fieldset>
	    </div>  
	  <div class="linha_separa_fields">&nbsp;</div>

  </div>

</body>