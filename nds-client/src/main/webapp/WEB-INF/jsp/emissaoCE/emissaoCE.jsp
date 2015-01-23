<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/emissaoCE.js"></script>

<style type="text/css">
  .dados, .dadosFiltro{display:none;}

  #dialog-novo, #dialog-alterar, #dialog-excluir{display:none; font-size:12px;}
  .box_field{width:200px;}
  
  #dialog-pesq-fornecedor fieldset {width:450px!important;}
#dialog-pesq-fornecedor{display:none;}

.fornecedores ul{margin:0px; padding:0px;}
.fornecedores li{display:inline; margin:0px; padding:0px;}
  </style>
</head>

<body>

		<form id="idEmissaoCEfornecedor">
			<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
			<fieldset>
				<legend>Selecione um ou mais Fornecedores</legend>
			    <select id="selectFornecedores" name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
			      
				      <c:forEach items="${listaFornecedores}" var="fornecedor">
				      	<option value="${fornecedor.key}_${fornecedor.value}">${fornecedor.value}</option>
				      </c:forEach>
			    
			    </select>
			
			</fieldset>
			</div>
		</form>
		
		<form id="idDialogReemissaoBoletoAntecipado">
			<div id="dialog-reemissao-boleto-antecipado" title="Reemissão de Boletos em Branco">
			<fieldset>
				<legend>Já existem boletos emitidos no período informado. Deseja cancelar os boletos já emitidos?</legend>
			</fieldset>
			</div>
		</form>
		
		<div class="areaBts">
			<div class="area">
				
				<span class="bt_novos">
					<a a href="javascript:;" onclick="EmissaoCEController.imprimirCEPDF();" id="imprimirCEPDF" rel="tipsy" title="Imprimir CE">
					<img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0" /></a>
				</span>
				
				<span class="bt_arq">
					<!-- ARQUIVO EXCEL -->
					<a href="${pageContext.request.contextPath}/emissaoCE/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a>
				</span>
				
				<span class="bt_arq">
					<!-- IMPRESSAO BOLETOS EM BRANCO -->
					<a href="javascript:void(0)" id="imprimirBoletosEmBranco" rel="tipsy" title="Imprimir Boletos em Branco">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a>
				</span>
				
				<span class="bt_arq">
					<!-- ARQUIVO PDF -->
					<a href="${pageContext.request.contextPath}/emissaoCE/exportar?fileType=PDF" rel="tipsy" title="Imprimir Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a>
				</span>	
			</div>
		</div>
    	<div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend>Pesquisar CE´s</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td nowrap="nowrap">Dt. Recolhimento:</td>
    <td width="113">
    
<!-- Dt Recolhimento de -->    
<<<<<<< HEAD
<input id="emissaoCE-dataDe"  value="${data}" name="dtRecolhimentoDe" type="text" style="width:70px;"/></td>
=======
<input id="emissaoce-dataDe"  value="${data}" name="dtRecolhimentoDe" type="text" style="width:70px;"/></td>
>>>>>>> 0acf996c4827ea9abe79a83897063f5f0cb4b18e

    <td width="28" colspan="-1" align="center">Até</td>
    <td width="130">
    
<!-- Dt Recolhimento até -->
<<<<<<< HEAD
<input id="emissaoCE-dataAte" value="${data}" name="dtRecolhimentoAte" type="text" style="width:80px;"/></td>
=======
<input id="emissaoce-dataAte" value="${data}" name="dtRecolhimentoAte" type="text" style="width:80px;"/></td>
>>>>>>> 0acf996c4827ea9abe79a83897063f5f0cb4b18e

    <td>Intervalo Box:</td>
    <td width="91">

<!-- Box de -->     
<select id="emissaoCE-boxDe" name="jumpMenu" style="width:80px;" onchange="EmissaoCEController.carregarComboRoteiro(this.value)">
	<option selected="selected"> </option>	    
	<c:forEach items="${listaBoxes}" var="box">
		<option value="${box.key}" >${box.value}</option>
	</c:forEach>
</select>
	
	</td>
    <td width="22" align="center">Até</td>
    <td width="91">
    
<!-- Box até --> 	
<select id="emissaoCE-boxAte" name="jumpMenu2" style="width:80px;" onchange="EmissaoCEController.carregarComboRoteiro(this.value)">
  	<option selected="selected"> </option>
	<c:forEach items="${listaBoxes}" var="box">
		<option value="${box.key}">${box.value}</option>
	</c:forEach>
</select>
    
    </td>
    <td width="28">Cota:</td>
    <td width="68">

<!-- Cota De -->    
<input id="emissaoCE-cotaDe" type="text" style="width:60px;"/></td>

    <td width="30" align="center">Até</td>
    <td width="104">
    
<!-- Cota Até -->
<input id="emissaoCE-cotaAte" type="text" style="width:60px;"/></td>

  </tr>
  <tr>
    <td width="105">Roteiro:</td>
    <td>
    

<!-- Roteiro --> 	
<select id="emissaoCE-Roteiro"  style="width:100px;" onchange="EmissaoCEController.carregarComboRota(this.value)">
    <option selected="selected"> </option>
	<%-- <c:forEach items="${listaRoteiros}" var="roteiro">
		<option value="${roteiro.key}">${roteiro.value}</option>
	</c:forEach>  --%>    
</select>

	</td>

    <td>Rota:</td>
    <td>

<!-- Rota --> 	
<select id="emissaoCE-Rota" name="select" style="width:130px;">
    <option selected="selected"> </option>
    <%-- <c:forEach items="${listaRotas}" var="rota">
		<option value="${rota.key}">${rota.value}</option>
	</c:forEach> --%>
</select>
    
    </td>
    <td width="79" align="right">


<!-- Capa -->    
<input  id="capa" type="checkbox" name="checkbox" onclick="EmissaoCEController.ativarPersonalizada(this);"  /></td>


    <td> Capa </td>
    <td>
    
<!-- Personalizada -->

<input  id="personalizada" type="checkbox" name="checkbox2" class="imprimirPersonalizada personalizada" style="display:none" /></td>

    <td><span class="imprimirPersonalizada personalizada" style="display:none">Personalizada?</span></td>
    
    <td align="right">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Fornecedor:</td>
    <td colspan="2"><div style="float:left; line-height:35px; margin-right:5px;"><a href="javascript:;" onclick="EmissaoCEController.popup_pesq_fornecedor();">clique para selecionar</a></div></td>
    <td colspan="8">
    
<!-- Fornecedores Selecionados -->
<div id="fornecedoresSelecionados" class="fornecedores">
     		
</div>

	</td>
    <td><span class="bt_pesquisar">
    
    
<!-- Pesquisar -->    
<a href="javascript:;" onclick="EmissaoCEController.cliquePesquisar();"></a></span></td>


    </tr>
  </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="fieldGrid">
       	  <legend> Emissão CE</legend>
        <div class="grids" style="display:none;">
		  <table class="ceEmissaoGrid"></table>
		  		      
        </div>
		
      </fieldset>
      

</body>
