
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaCotaAusente = new PesquisaCota();

var numCotaAusente;

$(function() {
	$("#idNovaCota").numeric();
	$("#idNomeNovaCota").autocomplete({source: ""});
	
	$("#idCota").numeric();
	$("#idNomeCota").autocomplete({source: ""});
});

var mov;
var indiceAtual;

function cliquePesquisar() {
	
	var dataAusencia = $('#idData').attr('value');
	var numcota = $('#idCota').attr('value');
	var nomeCota = $('#idNomeCota').attr('value');
	var box = $('#idBox').attr('value');
		
	$(".ausentesGrid").flexOptions({			
		url : '<c:url value="/cotaAusente/pesquisarCotasAusentes"/>',
		dataType : 'json',
		preProcess:processaRetornoPesquisa,
		params:[{name:'dataAusencia',value:dataAusencia},
		        {name:'numCota',value:numcota},
		        {name:'nomeCota',value:nomeCota},
		        {name:'box',value:box}]		
	});
	
	$(".ausentesGrid").flexReload();
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
		
		row.cell.acao = gerarBotaoExcluir(row.cell.idCotaAusente);		
		
  	});
	
	return grid;
}

function gerarBotaoExcluir(idCotaAusente) {
	
	if(idCotaAusente) {
		return "<a href=\"javascript:;\" onclick=\"popup_excluir("+idCotaAusente+");\"> "+
		 "<img src=\"${pageContext.request.contextPath}/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" /></a>";
	} else {
		return  "<img style=\"opacity: 0.5\" src=\"${pageContext.request.contextPath}/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" />";
	}
}

function popupNovaCotaAusente() {
	
	$( "#dialog-novo" ).dialog({
		resizable: false,
		height:'auto',
		width:540,
		modal: true,
		buttons: {
			"Confirmar": function() {
				
				var numCota = $("#idNovaCota").attr("value");
				var nomeCota = $("#idNomeNovaCota").attr("value");
				
				if($.trim(numCota) == "" || $.trim(nomeCota) == "") {
					exibirMensagemDialog("WARNING",["O campo \"Cota\" &eacute obrigat&oacuterio."]);	
					return;
				}
				
				popupConfirmaAusenciaCota(numCota);
				
				$("#idNovaCota").attr("value","");
				$("#idNomeNovaCota").attr("value",""); 
				$( this ).dialog( "close" );
				
			},
			"Cancelar": function() {
				
				$("#idNovaCota").attr("value","");
				$("#idNomeNovaCota").attr("value","");
				
				$( this ).dialog( "close" );
			}
		}
	});
}


function popupConfirmaAusenciaCota(numcota) {
	
	numCotaAusente = numcota;
	
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Sim": function() {
					
					$.postJSON("<c:url value='/cotaAusente/enviarParaSuplementar'/>", 
							"numCota="+numcota, 
							retornoEnvioSuplementar);
				
					$( "#dialog-confirm" ).dialog("close");
					
				},
				"Não": function() {
					
					$.postJSON("<c:url value='/cotaAusente/carregarDadosRateio'/>", 
							"numCota="+numcota, 
							popupRateio);
					
					$( this ).dialog( "close" );
				}
			}
		});
}

function retornoEnvioSuplementar(result) {
	
	var mensagens = result[0];
	var status = result[1];
	
	exibirMensagem(status, mensagens);
	
	$(".ausentesGrid").flexReload();
}

function retornoRateio(result) {
	
	var mensagens = result[0];
	var status = result[1];
	
	if(status == "SUCCESS") {
	
		exibirMensagem(status, mensagens);
		
		$(".ausentesGrid").flexReload();
		
		$( "#dialog-confirm" ).dialog("close");
	} else {
		exibirMensagemDialog(status, mensagens);
	}
}

function gerarMovimentos(movimentos) {
	
	var tabMovimentos = $("#idMovimentos");	
	var cabecalho = $("#idCabecalhoMovimentos");
		
	tabMovimentos.clear();
	
	tabMovimentos.append(cabecalho);
	
	$.each(movimentos, function(index, movimento) {
		var novaLinha = $(document.createElement("TR"));
		
		var codigo = document.createElement("TD");
		var produto = document.createElement("TD");
		var edicao = document.createElement("TD");
		var reparte = document.createElement("TD");
		var botao = document.createElement("TD");
				
		codigo.innerHTML = movimento.codigoProd;
		produto.innerHTML = movimento.nomeProd;
		edicao.innerHTML = movimento.edicaoProd;
		reparte.innerHTML = movimento.qtdeReparte;
		botao.innerHTML = "<a onclick=\"gerarGridRateios("+index+");\" href=\"javascript:;\"><img src=\"${pageContext.request.contextPath}/images/ico_negociar.png\" border=\"0\" /></a>";
		
		novaLinha.append(codigo);
		novaLinha.append(produto);
		novaLinha.append(edicao);
		novaLinha.append(reparte);	
		novaLinha.append(botao);
		
		if(index%2 == 0) {
			novaLinha.attr("style", "background:#F8F8F8;");
		}
		
		tabMovimentos.append(novaLinha);
	});
}

function gerarGridRateios(indice) {
	
	indiceAtual = indice;
	
	$("#idFieldRateios").attr("style","");

	document.getElementById("idLegendRateios").innerHTML= "Redistribuição - "+mov[indice].nomeProd;
	
	var tabRateios = $("#idRateios");	
	var cabecalho = $("#idCabecalhoRateios");
		
	tabRateios.clear();
	tabRateios.append(cabecalho);
	
	var proxIndice;
	
	if(mov[indice].rateios) {
	
		proxIndice = mov[indice].rateios.length;
		
		$.each(mov[indice].rateios, function(index, rateio) {
			
			gerarLinhaNova(index,rateio.numCota,rateio.nomeCota,rateio.qtde);
		});
		
	}  else {
		mov[indice]["rateios"] = new Array();
		proxIndice = 0;
	}	
	
	gerarLinhaNova(proxIndice,"","","");
	
	var qtdeRateios = mov[indiceAtual].rateios.length;
	document.getElementById('idNum'+ qtdeRateios).focus();
}
	
function gerarNovoRateio(indiceLinhaAlterada) {
	
	var numCota = $("#idNum" + indiceLinhaAlterada).attr("value");
	var nomeCota = $("#idNom" + indiceLinhaAlterada).attr("value");
	var qtde = $("#idQtde" + indiceLinhaAlterada).attr("value");
	
		
	var totalRateado = 0 * 1;
	$.each(mov[indiceAtual].rateios, function(index, rateio) {		
		totalRateado = totalRateado*1 + rateio.qtde*1;
	});
	
	var soma = totalRateado*1 + qtde*1; 
	
	if( soma > mov[indiceAtual].qtdeReparte) {
		exibirMensagemDialog("WARNING",["N&atildeo h&aacute reparte suficiente."]);	
		
		alterarEvento(
				"idQtde"+indiceLinhaAlterada,
				'idQtde'+ indiceLinhaAlterada, 
				"onblur");
		
		$("#idQtde" + indiceLinhaAlterada).attr("value","");
		return;
	}
	
	
	var qtdeRateios = mov[indiceAtual].rateios.length;
	
	if( indiceLinhaAlterada == (qtdeRateios) ) {
		
		mov[indiceAtual].rateios.push({"numCota":numCota, "nomeCota":nomeCota, "qtde":qtde});
				
		gerarLinhaNova( (qtdeRateios + 1) ,"","","");
				
		alterarEvento(
				"idQtde"+indiceLinhaAlterada,
				'idNum'+ (qtdeRateios +1), 
				"onblur");
		
	} else {
		
		mov[indiceAtual].rateios[indiceLinhaAlterada] = {"numCota":numCota, "nomeCota":nomeCota, "qtde":qtde};
		
		alterarEvento(
				"idQtde"+indiceLinhaAlterada,
				'idNum'+ qtdeRateios, 
				"onblur");
		
	}
		
	if($.trim(numCota) == "" || $.trim(nomeCota) == "" || $.trim(qtde) == "" || $.trim(qtde) == "0") {
		
		mov[indiceAtual].rateios.splice(indiceLinhaAlterada,indiceLinhaAlterada + 1);
		
		gerarGridRateios(indiceAtual);
		return;
	}
}

function alterarEvento(idFocoAtual, idNovoFoco, evento) {
	
	var elemAtual = document.getElementById(idFocoAtual);	
	var elemNovoFoco = document.getElementById(idNovoFoco);	
	
	var valorEvento = elemAtual.getAttribute(evento);
	
	elemAtual.setAttribute(evento,null);
		
	elemNovoFoco.focus();
	
	elemAtual.setAttribute(evento,valorEvento);	
}
	
function gerarLinhaNova(indice,num, nome, qtd) {
		
	var tabRateios = $("#idRateios");	
	
	var novaLinha = $(document.createElement("TR"));
	
	var numCota = $(document.createElement("TD"));
	var nomeCota = $(document.createElement("TD"));
	var qtde = $(document.createElement("TD"));
			
	numCota.append(getInput(
			num,
			"idNum"+indice ,
			"60px",
			null,
			null,
			"pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNum"+indice+"', '#idNom"+indice+"',true)"));
	
	nomepesquisaCotaCotaAusente.append(getInput(
			nome,
			"idNom"+indice ,
			"180px",
			null,
			"pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNum"+indice+"', '#idNom"+indice+"',true)", 
			null,
			"pesquisaCotaCotaAusente.autoCompletarPorNome('#idNom"+indice+"')"));
	
	qtde.append(getInput(
			qtd,
			"idQtde"+indice ,
			"60px",
			"center",
			"gerarNovoRateio("+indice+");"));
	
	novaLinha.append(numCota);
	novaLinha.append(nomeCota);
	novaLinha.append(qtde);
	novaLinha.append("<td><input type=\"hidden\" value=\""+indice+"\"></td>");
	
	tabRateios.append(novaLinha);
	
	$("#idNum"+indice).numeric();
	$("#idQtde"+indice).numeric();
	$("#idNom"+indice).autocomplete({source: ""});
}

function getInput(value,id, width,textAlign,onblur,onchange,onkeyup) {
	
	var input = document.createElement("INPUT");
	input.type="text";
	input.name=id;
	input.id=id;
	input.value=value;
	input.style.setProperty("width",width);
	
	if(textAlign) {
		input.style.setProperty("text-align",textAlign);
	}
	
	if(onblur) {
		input.setAttribute("onblur",onblur);
	}
	
	if(onchange) {
		input.setAttribute("onchange",onchange);
	}
	
	if(onkeyup) {
		input.setAttribute("onkeyup",onkeyup);
	}
		
	return input;
}
	
function popupRateio(movimentos) {
	
	mov = movimentos;
		
	gerarMovimentos(movimentos);
	
	if(movimentos[0])
		gerarGridRateios(0);
	
	$( "#dialog-suplementar" ).dialog({
		resizable: false,
		height:450,
		width:800,
		modal: true,
		buttons: {
			"Suplementar": function() {
				
				$.postJSON("<c:url value='/cotaAusente/enviarParaSuplementar'/>", 
						"numCota=" + numCotaAusente, 
						retornoEnvioSuplementar);
				
				$( this ).dialog( "close" );
				
			},
			"Redistribuir": function() {
				
				var parametros = getParametrosFromMovimentos();
				
				if(!parametros) {
					return;
				}
				
				$.postJSON("<c:url value='/cotaAusente/realizarRateio'/>", 
						parametros,
						retornoRateio);
				
				$( this ).dialog( "close" );
			}
		}
	});
}

function getParametrosFromMovimentos() {
	
	var parametros = [];
	
	$.each(mov, function(index, movimento) {
		
		parametros.push({name:'movimentos['+ index +'].idCota', value: movimento.idCota});
		parametros.push({name:'movimentos['+ index +'].idProdEd', value: movimento.idProdEd});
		parametros.push({name:'movimentos['+ index +'].codigoProd', value: movimento.codigoProd});
		parametros.push({name:'movimentos['+ index +'].edicaoProd', value: movimento.edicaoProd});
		parametros.push({name:'movimentos['+ index +'].nomeProd', value: movimento.nomeProd});
		parametros.push({name:'movimentos['+ index +'].qtdeReparte', value: movimento.qtdeReparte});
		
		if(movimento.rateios) {
						
			$.each(movimento.rateios, function(indexR, rateio) {
				parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].numCota', value: rateio.numCota});
				parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].nomeCota', value: rateio.nomeCota});
				parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].qtde', value: rateio.qtde});			
			});		
		}
  	});
	
	parametros.push({name:'numCota', value: numCotaAusente});
	
	return parametros;
}
		
function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:220,
			width:540,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					
					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
}	

function retornoExlusaoCotaAusente(result) {
	
	var mensagens = result[0];
	var status = result[1];
	
	exibirMensagem(status, mensagens);
	
	$(".ausentesGrid").flexReload();
}
	
function popup_excluir(idCotaAusente) {
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					$.postJSON("<c:url value='/cotaAusente/cancelarCotaAusente'/>", 
							"idCotaAusente="+idCotaAusente, 
							retornoExlusaoCotaAusente);
					
					$( this ).dialog( "close" );
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
}

	

$(function() {
		$( "#idData" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#idData" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#idData" ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idData").mask("99/99/9999");
		
});	

$(function() {
		$( "#tabs-pop" ).tabs();
});

</script>
<script>
function abre_linha_1(){
	$( '.linha_1' ).show();
	textfield5.focus();
}
function abre_linha_2(){
	$( '.linha_2' ).show();
	textfield8.focus();
}
function abre_linha_3(){
	$( '.linha_3' ).show();
	textfield11.focus();
}
function abre_linha_4(){
	$( '.linha_4' ).show();
	textfield11.focus();
}
function abre_linha_4(){
	$( '.linha_5' ).show();
	textfield17.focus();
}
function abre_linha_5(){
	$( '.linha_6' ).show();
	textfield20.focus();
}
function abre_linha_21(){
	$( '.linha_21' ).show();
	textfield24.focus();
}
function abre_linha_22(){
	$( '.linha_22' ).show();
	textfield27.focus();
}

function abre_linha_31(){
	$( '.linha_31' ).show();
	textfield34.focus();
}
function abre_linha_32(){
	$( '.linha_32' ).show();
	textfield37.focus();
}


function mostra_grid(){
	$( '#grid_1' ).show();
}


</script>
<style>
.linha_1, .linha_2, .linha_3, .linha_4, .linha_5, .linha_6, .linha_21, .linha_22, .linha_31, .linha_32 {display:none;}
#dialog-suplementar fieldset{width:350px!important;}
#dialog-suplementar .linha_separa_fields{width:350px!important;}

</style>
</head>

<body>


<div id="dialog-confirm" title="Suplementar">
	<p>Confirma Suplementar?</p>
</div>

<div id="dialog-excluir" title="Cota Ausente">
	<p>Confirma a exclusão desse Cota Ausente?</p>
</div>



<div id="dialog-novo" title="Incluir Cota Ausente"> 

	<jsp:include page="../messagesDialog.jsp" />

    <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Cota:</td>
             
              <td width="446" colspan="3">
 <!-- NOVA COTA - NUM -->     
<input id="idNovaCota" name="idNovaCota" type="text" style="width:80px; float:left; margin-right:5px;" 
	onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNovaCota', '#idNomeNovaCota',true);" />
	
<!-- PESQUISAR NOVA COTA -->           
	<label style="margin-left:10px;">
           			Nome:
           		
           		</label>
           		
 <!-- NOVA COTA - NOME -->
<input id="idNomeNovaCota" name="idNomeNovaCota" type="text" class="nome_jornaleiro" style="width:280px;" 
	onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeNovaCota');" 
		 	   onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNovaCota', '#idNomeNovaCota',true);" />
		 	   
       			</td>
            
            </tr>
          </table>
    </div>
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Ausente < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Cotas Ausentes</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="35">Data:</td>
              <td colspan="3">
<!--DATA-->
<input id="idData" value="${data}" type="text" name="datepickerDe" style="width:80px;" /></td>
              
                <td width="38">Cota:</td>
                <td width="123">
<!-- COTA -->                
<input id="idCota" name="idCota" type="text" style="width:80px; float:left; margin-right:5px;" 
	onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idCota', '#idNomeCota');"/>
	
<!-- PESQUISAR NOME COTA -->
<td width="40">Nome:</td>
                <td width="296">
<!-- NOME -->            
<input id="idNomeCota" name="idNomeCota" type="text" class="nome_jornaleiro" style="width:280px;" 
	onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeCota');" 
		 	   onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idCota', '#idNomeCota');"
	/>
				</td>
                <td width="27">Box:</td>
                <td width="111">
<!-- BOX -->
<input id="idBox" type="text" name="textfield" id="textfield" style="width:80px;"/></td>
              <td width="114"><span class="bt_pesquisar">
<!-- PESQUISAR -->
<a href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Cotas Ausentes Cadastradas</legend>
        <div class="grids" style="display:none;">
       	  <table class="ausentesGrid"></table>
          <br />
          <span class="bt_novos" title="Gerar Arquivo">
<!-- ARQUIVO -->
<a href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=XLS">
	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	Arquivo
</a></span>

	<span class="bt_novos" title="Imprimir">
<!-- IMPRIMIR -->	
<a href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=PDF">
	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
	Imprimir
</a></span>

        </div>
        <span class="bt_novos" title="Novo">
<!-- NOVO -->
<a href="javascript:;" onclick="popupNovaCotaAusente();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

<script type="text/javascript">
	
	$("#idCota").mask("?99999999999999999999", {placeholder:""});
	
	$(function() {	
		
		$(".ausentesGrid").flexigrid($.extend({},{
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 480,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor NE R$',
				name : 'valorNe',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		})); 	
		
		$(".grids").show();		
	});
	

</script>

<jsp:include page="modalRateio.jsp"/>
</body>
