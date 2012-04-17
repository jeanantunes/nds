
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript"	src="${pageContext.request.contextPath}/scripts/jquery-dateFormat/jquery.dateFormat-1.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>

<title>NDS - Novo Distrib</title>


<script language="javascript" type="text/javascript">


$(function() {		
	$("#idNumCota").numeric();
	$("#idNomeCota").autocomplete({source: ""});
});

function cliquePesquisar() {
	
	var periodoDe = "B";//$('#idCota').attr('value');
	var periodoAte = "C";//$('#idNomeCota').attr('value');
	var numCota = 1;//$('#idBox').attr('value');
	var nomeCota = "E";//$('#idBox').attr('value');
	var status = "F";//$('#idBox').attr('value');
	var situacao = "G";//$('#idBox').attr('value');	
		
	$(".inadimplenciaGrid").flexOptions({			
		url : '<c:url value="/inadimplencia/pesquisar"/>',
		dataType : 'json',
		preProcess:processaRetornoPesquisa,
		params:[{name:'periodoDe',value:periodoDe},
		        {name:'periodoAte',value:periodoAte},
		        {name:'numCota',value:numCota},
		        {name:'nomeCota',value:nomeCota},
		        {name:'statusDivida',value:status},
		        {name:'situacao',value:situacao}]		
	});
	
	$(".inadimplenciaGrid").flexReload();
}

function processaRetornoPesquisa(result) {
	
	var grid = result[0];
	var mensagens = result[1];
	var status = result[2];
	
	if(mensagens!=null && mensagens.length!=0) {
		exibirMensagem(status,mensagens);
	}
	
	if(!grid.rows) {
		return grid;
	}
	
	$.each(grid.rows, function(index, row) {
		
		row.cell.detalhe = gerarBotaoDetalhes(row.cell.idCota);		
		
  	});
	
	return grid;
}

function gerarBotaoDetalhes(idCota) {
	return "<a href=\"javascript:;\" onclick=\"popup("+idCota+");\"><img src=\"${pageContext.request.contextPath}/images/ico_detalhes.png\" border=\"0\" hspace=\"5\" title=\"Detalhes\" /></a>";
	
}

function popup(idCota) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			}
		});
}
	
	
$(function() {
		$( "#idDataDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#idDataAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
$(document).ready(function(){	
	$("#selDivida").click(function() {
		$(".menu_dividas").show().fadeIn("fast");
	});

	$(".menu_dividas").mouseleave(function() {
		$(".menu_dividas").hide();
	});
	
});
</script>

</head>

<body>


<form action="" method="get" id="form1" name="form1">




<div id="dialog-novo" title="Detalhe da Divida">
     
    <fieldset style="width:330px;">
  <legend>Cota: 9999 - José da Silva Pereira</legend>
   	<table width="330" border="0" cellspacing="1" cellpadding="1">
      <tr class="header_table">
        <td width="166" align="left"><strong>Dia Vencimento</strong></td>
        <td width="157" align="right"><strong>Valor R$</strong></td>
      </tr>
      <tr class="class_linha_1">
        <td align="left">99/99/9999</td>
        <td align="right">120,00</td>
      </tr>
      <tr class="class_linha_2">
        <td align="left">99/99/9999</td>
        <td align="right">125,00</td>
      </tr>
      <tr class="class_linha_1">
        <td align="left">99/99/9999</td>
        <td align="right">87,00</td>
      </tr>
    </table>
</fieldset>
</div>

    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Suspensão < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Hist&oacutericos de Inadimpl&ecircncias</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="72">Per&Iacuteodo de:</td>
              <td width="108" >

<!-- DATA DE -->
<input type="text" name="datepickerDe" id="idDataDe" style="width:80px;" />
            
              </td>
              <td width="28" >At&eacute:
              </td>
              <td width="106" >

<!-- DATA ATE --> 
<input type="text" name="datepickerAte" id="idDataAte" style="width:80px;" />
              
              </td>
              <td width="36">Cota:</td>
              <td width="93">

<!-- NUM COTA -->
<input type="text" name="idNumCota" id="idNumCota" style="width:80px;" 
	onchange="cota.pesquisarPorNumeroCota('#idNumCota', '#idNomeCota');"/></td>
              
              <td width="45">Nome:</td>
              <td width="193">

<!-- NOME COTA -->
<input type="text" name="idNomeCota" id="idNomeCota" style="width:180px;" 
	onkeyup="cota.autoCompletarPorNome('#idNomeCota');" 
		 	   onblur="cota.pesquisarPorNomeCota('#idNumCota', '#idNomeCota');"/></td>
            
              <td width="48">Status:</td>
              <td width="170">
              
 
 <!-- COMBO STATUS -->             
 <select name="select" id="idStatusCota" style="width:90px;">
  <option>Selecione...</option>
  <c:forEach items="${itensStatus}" var="status" varStatus="index">
  	 <option value="${status.key}">${status.value}</option>
  </c:forEach>
       
 </select>
 			</td>
            </tr>
            <tr>
              <td colspan="2">
<!-- SITUACAO -->              
<a href="javascript:;" id="selDivida">Situa&ccedil&atildeo da D&iacutevida:</a>
              
              <div class="menu_dividas" style="display:none;border: 1px solid #CCC;position: absolute;background: white;z-index: 10;">

<!-- SITUACAO DIVIDA  TODAS -->                

			<table>
				<tr>
					
                
				
				<td>
					<span class="bt_sellAll">
<input type="checkbox" id="idTodas" name="Todos2" onclick="checkAll_dividas();" style="float:left;"/>
					</span>
				</td>
				<td>
					<span class="bt_sellAll">
<label for="selDivida">Selecionar Todas</label>
					</span>
				</td>
								
				</tr>
				<tr>
				
<!-- SITUACAO DIVIDA - ABERTAS -->
				<td>
<input id="idDividaEmAberto" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
                </td>
                <td>
                <label for="emaberto">Em Aberto</label>
                </td>
                
                </tr>
                <tr>
                
                <td>
<!-- SITUACAO DIVIDA - NEGOCIADA -->
<input id="idDividaNegociada" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
				</td>
				<td>
                <label for="negociada">Negociada</label>
                </td>
                
                </tr>
                <tr>
                
                <td>
<!-- SITUACAO DIVIDA - PAGA -->
<input id="idDividaPaga" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
				</td>
				<td>
                <label for="paga">Paga</label>
                </td>
                
				</tr>
			</table>
                
           </div>
           
           
           </td>
           
           
              <td colspan="2" >
                
              </td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td><span class="bt_pesquisar"><a href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> Cotas Inadimplentes</legend>
        <div class="grids" style="display:none;">
			<table class="inadimplenciaGrid"></table>
          	
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23%"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span></td>
                <td width="8%"><strong>Qtde Cotas:</strong></td>
                <td width="5%">05</td>
                <td width="2%">&nbsp;</td>
                <td width="10%"><strong>Divida Total R$:</strong></td>
                <td width="9%">9.999.999,99</td>
                <td width="43%">&nbsp;</td>
              </tr>
            </table>

            
            
            
            
           
        
</div>
		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
<script>
	
$("#idNumCota").mask("?99999999999999999999", {placeholder:""});

$(function() {	
	
	$(".inadimplenciaGrid").flexigrid($.extend({},{
		colModel : [ {
				display : 'Cota',
				name : 'numCota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado at&eacute Data',
				name : 'consignado',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dataVencimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Pagamento',
				name : 'dataPagamento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Situa&ccedil&atildeo da D&iacutevida',
				name : 'situacao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Divida Acumulada',
				name : 'dividaAcumulada',
				width : 85,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dias em Atraso',
				name : 'diasAtraso',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Detalhes',
				name : 'detalhe',
				width : 40,
				sortable : true,
				align : 'center',
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
})); 	

$(".grids").show();	
});
</script>
</form>

</body>