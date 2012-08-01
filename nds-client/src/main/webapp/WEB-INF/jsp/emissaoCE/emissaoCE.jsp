<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/emissaoCE.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">
	
	var ECE = new EmissaoCE('${pageContext.request.contextPath}', 'ECE');
		
	
	function ativarPersonalizada(elemento) {
		if(elemento.checked==true)
			$('.personalizada').show();
		else
			$('.personalizada').hide();
	}
	
	function popup_pesq_fornecedor() {
		
		 	$("#selectFornecedores").val(ECE.fornecedoresSelecionados);
		
			$( "#dialog-pesq-fornecedor" ).dialog({
				resizable: false,
				height:300,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ECE.getFornecedoresSelecionados();
						ECE.gerarFornecedoresSelecionados();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	};
		
	function popup_alterar() {
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:210,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:230,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
  //callback function to bring a hidden box back
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
		};	

	function mostrar(){
		$(".grids").show();
	}	
	
	function confirmar(){
		$(".dados").show();
	}
	
	function pesqEncalhe(){
		$(".dadosFiltro").show();
	}
	
	function removeFornecedor(){
		$( ".forncedoresSel" ).fadeOut('fast');
		$( ".linhaForncedoresSel" ).hide();
	}	
	
</script>
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
<form action="" method="get" id="form1" name="form1">

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






<div id="dialog-excluir" title="Suspensão de Cotas">
	<p>Confirma a Suspensão destas Cotas?</p>
    
   	  <b>Motivo</b><br clear="all" />

        <textarea cols="" rows="4" style="width:340px;"></textarea>
    </fieldset>
</div>





<div id="dialog-novo" title="CE Antecipada">
	<p>Data Antecipada:      <input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;"/></p>
	<p>Confirma a gravação dessas informações? </p>     
</div>








<div class="corpo">
 
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>CE Antecipada < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend>Pesquisar CE´s</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td nowrap="nowrap">Dt. Recolhimento:</td>
    <td width="113">
    
<!-- Dt Recolhimento de -->    
<input id="dataDe"  value="${data}" name="dtRecolhimentoDe" type="text" style="width:80px;"/></td>

    <td width="28" colspan="-1" align="center">Até</td>
    <td width="130">
    
<!-- Dt Recolhimento até -->
<input id="dataAte" name="dtRecolhimentoAte" type="text" style="width:80px;"/></td>

    <td>Intervalo Box:</td>
    <td width="91">

<!-- Box de -->     
<select id="boxDe" name="jumpMenu" style="width:80px;">
	<option selected="selected"> </option>	    
	<c:forEach items="${listaBoxes}" var="box">
		<option value="${box.key}">${box.value}</option>
	</c:forEach>
</select>
	
	</td>
    <td width="22" align="center">Até</td>
    <td width="91">
    
<!-- Box até --> 	
<select id="boxAte" name="jumpMenu2" style="width:80px;">
  	<option selected="selected"> </option>
	<c:forEach items="${listaBoxes}" var="box">
		<option value="${box.key}">${box.value}</option>
	</c:forEach>
</select>
    
    </td>
    <td width="28">Cota:</td>
    <td width="68">

<!-- Cota De -->    
<input id="cotaDe" type="text" style="width:60px;"/></td>

    <td width="30" align="center">Até</td>
    <td width="104">
    
<!-- Cota Até -->
<input id="cotaAte" type="text" style="width:60px;"/></td>

  </tr>
  <tr>
    <td width="105">Roteiro:</td>
    <td>
    

<!-- Roteiro --> 	
<select id="roteiro"  style="width:100px;">
    <option selected="selected"> </option>
	<c:forEach items="${listaRoteiros}" var="roteiro">
		<option value="${roteiro.key}">${roteiro.value}</option>
	</c:forEach>     
</select>

	</td>

    <td>Rota:</td>
    <td>

<!-- Rota --> 	
<select id="rota" name="select" style="width:130px;">
    <option selected="selected"> </option>
    <c:forEach items="${listaRotas}" var="rota">
		<option value="${rota.key}">${rota.value}</option>
	</c:forEach>
</select>
    
    </td>
    <td width="79" align="right">


<!-- Capa -->    
<input  id="capa" type="checkbox" name="checkbox" onclick="ativarPersonalizada(this);"  /></td>


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
    <td colspan="2"><div style="float:left; line-height:35px; margin-right:5px;"><a href="javascript:;" onclick="popup_pesq_fornecedor();">clique para selecionar</a></div></td>
    <td colspan="8">
    
<!-- Fornecedores Selecionados -->
<div id="fornecedoresSelecionados" class="fornecedores">
     		
</div>

	</td>
    <td><span class="bt_pesquisar">
    
    
<!-- Pesquisar -->    
<a href="javascript:;" onclick="ECE.cliquePesquisar();">Pesquisar</a></span></td>


    </tr>
  </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> Emissão CE</legend>
        <div class="grids" style="display:none;">
		  <table class="ceEmissaoGrid"></table>
		  
		  
		      <span class="bt_novos" title="Gerar Arquivo">
	            
	<!-- ARQUIVO EXCEL -->
	<a href="${pageContext.request.contextPath}/emissaoCE/exportar?fileType=XLS">
			
			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
			</span>
			
			
			 <span class="bt_novos" title="Imprimir Arquivo">
			
	<!-- ARQUIVO PDF -->
	<a href="${pageContext.request.contextPath}/emissaoCE/exportar?fileType=PDF">
			
			
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
			</span>			

	<span class="bt_novos" title="Imprimir">

<!-- IMPRESSAO CE -->
<a href="${pageContext.request.contextPath}/emissaoCE/imprimirCE" target="blank">

	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir CE</a></span>
        </div>
		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
<script>

$(function(){
	$(".ceEmissaoGrid").flexigrid({
		colModel : [ {
			display : 'Cota',
			name : 'numCota',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 530,
			sortable : true,
			align : 'left'
		}, {
			display : 'Qtde. Exemplares',
			name : 'qtdeExemplares',
			width : 130,
			sortable : true,
			align : 'center'
		}, {
			display : 'Valor Total CE R$',
			name : 'vlrTotalCe',
			width : 130,
			sortable : true,
			align : 'right'
		}],
			width : 960,
			height : 180,
			sortname : "nomeCota",
			sortorder : "asc"
		});
	
		$(".grids").show();
		
		$("#dataDe").mask("99/99/9999");
		
		$( "#dataDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dataAte").mask("99/99/9999");
		
		$( "#dataAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});		
		
		$("#cotaDe").numeric();
		
		$("#cotaAte").numeric();

		
		
});
</script>
</form>

</body>