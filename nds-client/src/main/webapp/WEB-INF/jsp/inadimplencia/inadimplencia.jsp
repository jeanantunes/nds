
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript"	src="${pageContext.request.contextPath}/scripts/jquery-dateFormat/jquery.dateFormat-1.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<title>NDS - Novo Distrib</title>


<script language="javascript" type="text/javascript">

var pesquisaCotaInadimplencia = new PesquisaCota();

$(function() {		
	$("#idNumCota").numeric();
	$("#idNomeCota").autocomplete({source: ""});
});

function cliquePesquisar() {
	
	var periodoDe = $('#idDataDe').attr('value');
	var periodoAte = $('#idDataAte').attr('value');
	var numCota = $('#idNumCota').attr('value');
	var nomeCota = $('#idNomeCota').attr('value');
	var statusCota = $('#idStatusCota').attr('value');
	
	var situacaoEmAberto = ( $('#idDividaEmAberto').attr('checked') == "checked" ) ;	
	var situacaoNegociada = ( $('#idDividaNegociada').attr('checked') == "checked" );
	var situacaoPaga = ( $('#idDividaPaga').attr('checked') == "checked" );
	
		
	$(".inadimplenciaGrid").flexOptions({			
		url : '<c:url value="/inadimplencia/pesquisar"/>',
		dataType : 'json',
		preProcess:processaRetornoPesquisa,
		
		params:[{name:'periodoDe',value : periodoDe},
		        {name:'periodoAte',value : periodoAte},
		        {name:'numCota',value : numCota},
		        {name:'nomeCota',value : nomeCota},
		        {name:'statusCota',value : statusCota},
		        {name:'situacaoEmAberto',value : situacaoEmAberto},
		        {name:'situacaoNegociada',value : situacaoNegociada},
		        {name:'situacaoPaga',value : situacaoPaga}]		
	});
	
	$(".inadimplenciaGrid").flexReload();
}


function selecionarTodos(elementoCheck) {
	
	var selects =  document.getElementsByName("checkgroup_menu_divida");
	
	$.each(selects, function(index, row) {
		row.checked=elementoCheck.checked;
	});
	
}
	

function processaRetornoPesquisa(result) {
	
	var grid = result[0];
	var mensagens = result[1];
	var status = result[2];
	var total = result[3];
	var qtde = result[4];
	
	if(mensagens!=null && mensagens.length!=0) {
		exibirMensagem(status,mensagens);
	}
	
	if(!grid.rows) {
		document.getElementById("idTotal").innerHTML  = "0,00";
		document.getElementById("idQtde").innerHTML  = 0;	
		return grid;
	}
	
	$.each(grid.rows, function(index, row) {
		
		row.cell.detalhe = gerarBotaoDetalhes(row.cell.idDivida,row.cell.nome);		
		
  	});
	
	document.getElementById("idQtde").innerHTML  = qtde;
	document.getElementById("idTotal").innerHTML  = total;	
	
	return grid;
}

function gerarBotaoDetalhes(idDivida, nome) {
	return "<a href=\"javascript:;\" onclick=\"getDetalhes("+idDivida+",'"+nome+"');\"><img src=\"${pageContext.request.contextPath}/images/ico_detalhes.png\" border=\"0\" hspace=\"5\" title=\"Detalhes\" /></a>";
	
}

var nomeCota;

function getDetalhes(idDivida, nome) {
	nomeCota = nome;
	$.postJSON("<c:url value='/inadimplencia/getDetalhesDivida'/>", 
			"idDivida="+idDivida+"&method='get'", 
			popupDetalhes);	
};


function popupDetalhes(result) {
	
		gerarTabelaDetalhes(result, nomeCota);
	
		$( "#dialog-detalhes" ).dialog({
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
	
function gerarTabelaDetalhes(dividas, nome) {
	var div = document.getElementById("dialog-detalhes");
	
	div.innerHTML="";
	
	var fieldset  = document.createElement("FIELDSET");
	
	fieldset.style.cssText = "width:330px;" + fieldset.style.cssText;
	
	div.appendChild(fieldset);
	
	var legend = document.createElement("LEGEND");
	legend.innerHTML = "Cota: ".bold() + " - " + nome;
	
	fieldset.appendChild(legend);
	
	var table = document.createElement("TABLE");
	table.id = "tabelaDetalhesId";
	table.width = "330";
	table.border = "0";
	table.cellspacing = "1";
	table.cellpadding = "1";
	
	fieldset.appendChild(table);
	
	var tbody = document.createElement("TBODY");
	
	table.appendChild(tbody);
	
 	var cabecalho = document.createElement("TR");
 	cabecalho.className="header_table";
 	
 	var tdDia = document.createElement("TD");
 	tdDia.width="136";
 	tdDia.align="left";
 	tdDia.innerHTML="Dia Vencimento".bold();
 	cabecalho.appendChild(tdDia);
 	
 	var tdValor = document.createElement("TD");
 	tdValor.width="157";
 	tdValor.align="right";
 	tdValor.innerHTML="Valor R$".bold();		 	
 	cabecalho.appendChild(tdValor);
 	
 	tbody.appendChild(cabecalho);
	
	 $(dividas).each(function (index, divida) {
		 
		 var linha = document.createElement("TR");
		 
		 var lin = (index%2==0) ? 1:2;
		 
		 linha.className="class_linha_" + lin ;
 	 
	 	var cel = document.createElement("TD");
	 	cel.align="left";
	 	text = document.createTextNode(divida.vencimento);
	 	cel.appendChild(text);			 	
	 	linha.appendChild(cel);
	 	
	 	var cel2 = document.createElement("TD");
	 	cel2.align="right";
	 	text2 = document.createTextNode(divida.valor);
	 	cel2.appendChild(text2);			 	
	 	linha.appendChild(cel2);
	 	
	 	tbody.appendChild(linha);
		 
		
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
		
		$( "#idDataDe" ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataDe").mask("99/99/9999");
		
		$( "#idDataAte" ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataAte").mask("99/99/9999");
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

<div id="dialog-detalhes" title="Detalhe da Divida">     
    
  </div>


<div id="dialog-novo" title="Detalhe da Divida">
     
    <fieldset style="width:330px;">
  <legend>Cota: 9999 - Jos� da Silva Pereira</legend>
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
				<b>Suspens�o < evento > com < status >.</b></p>
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
	onchange="pesquisaCotaInadimplencia.pesquisarPorNumeroCota('#idNumCota', '#idNomeCota');"/></td>
              
              <td width="45">Nome:</td>
              <td width="193">

<!-- NOME COTA -->
<input type="text" name="idNomeCota" id="idNomeCota" style="width:180px;" 
	onkeyup="pesquisaCotaInadimplencia.autoCompletarPorNome('#idNomeCota');" /></td>
            
              <td width="48">Status:</td>
              <td width="170">
              
 
 <!-- COMBO STATUS -->             
 <select name="select" id="idStatusCota" style="width:90px;">
  <option value="none">Selecione...</option>
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
<input type="checkbox" id="idSelecaoTodos" name="Todos2" onclick="selecionarTodos(this);" style="float:left;"/>
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
                <td width="23%"><span class="bt_novos" title="Gerar Arquivo">
                
<!-- EXCEL -->
<a href="${pageContext.request.contextPath}/inadimplencia/exportar?fileType=XLS">

				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

				<span class="bt_novos" title="Imprimir">

<!-- PDF -->
<a href="${pageContext.request.contextPath}/inadimplencia/exportar?fileType=PDF">

				
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>

				</span></td>
                <td width="8%"><strong>Qtde Cotas:</strong></td>
                <td width="5%">
                	<div id="idQtde">0</div>  
                </td>
                <td width="2%">&nbsp;</td>
                <td width="10%"><strong>Divida Total R$:</strong></td>
                <td width="9%">
                	<div id="idTotal">0,00</div>  
                </td>
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
				align : 'center',
			}],
			sortname : "nome",
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