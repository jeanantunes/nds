<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<link rel="stylesheet" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">
function popup() {
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:280,
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
	
	function popup_confirm() {
	
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:280,
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
	
	function popup_rejeitar() {
	
		$( "#dialog-rejeitar" ).dialog({
			resizable: false,
			height:'auto',
			width:280,
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
	function popup_dadosNotaFiscal(numeroNfe, dataEncalhe, chaveAcesso, serie, vlrNota, idNotaFiscalEntrada) {
		
		$('#numeroNotaFiscalPopUp').text(numeroNfe);
		$('#dataNotaFiscalPopUp').text(dataEncalhe);
		$('#chaveAcessoNotaFiscalPopUp').text(chaveAcesso);
		$('#serieNotaFiscalPopUp').text(serie);
		$('#valorNotaFiscalPopUp').text(vlrNota);
		
		$(".pesqProdutosNotaGrid").flexOptions({
			url: "<c:url value='/nfe/consultaNFEEncalheTratamento/pesquisarItensPorNota'/>",
			dataType : 'json',
			params: [{name:'filtro.codigoNota', value:idNotaFiscalEntrada}]
		});

		$(".pesqProdutosNotaGrid").flexReload();
	
		$( "#dialog-dadosNotaFiscal" ).dialog({
			resizable: false,
			height:'auto',
			width:860,
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
	function popup_confirmar() {
	
		$( "#dialog-confirmar-cancelamento" ).dialog({
			resizable: false,
			height:'auto',
			width:280,
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
	
	function popup_nfe(numeroCota, nome){
		
		if(numeroCota != '0'){
			$('#cotaCadastroNota').val(numeroCota);
			$('#nomeCotaCadastroNota').val(nome);
			$('#cotaCadastroNota').attr('disabled', 'disabled');
			$('#nomeCotaCadastroNota').attr('disabled', 'disabled');
			}
			
		$( "#dialog-nfe" ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					cadastrarNota();
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function cadastrarNota(){		

		$.postJSON(
				'<c:url value="/nfe/consultaNFEEncalheTratamento/cadastrarNota" />',
				[
					{ name: "nota.numero", value: $('#numeroNotaCadastroNota').val() },
					{ name: "nota.serie", value: $('#serieNotaCadastroNota').val() },
					{ name: "nota.chaveAcesso", value: $('#chaveAcessoCadastroNota').val() },
					{ name: "numeroCota", value: $('#cotaCadastroNota').val() },
				],
				function(result) {
					alert(result);					
				},
				null,
				true
			);
	
	}
	
	
	
	
	
  
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
		};	

	
	
	$(function() {
		$( "#data" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
	});
	function confirmar(){
		$(".dados").show();
	}
	function pesqEncalhe(){
		$(".dadosFiltro").show();		
		var status = $('#situacaoNfe').val();		
		if(status == 'RECEBIDA'){			
			pesquisarNotaRecebidas();		
		}else{			
			pesquisarNotasPendente();
		}
		
	}
	
	function pesquisarNotaRecebidas(){
		
		$(".notaRecebidaGrid").flexOptions({
			url: "<c:url value='/nfe/consultaNFEEncalheTratamento/pesquisarNotasRecebidas'/>",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota').val()},
						{name:'filtro.data', value:$('#data').val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe').val()}						
						]
		});

		$(".notaRecebidaGrid").flexReload();
	}
	
	function pesquisarNotasPendente(){
		
		$(".encalheNfeGrid").flexOptions({
			url: "<c:url value='/nfe/consultaNFEEncalheTratamento/pesquisarNotasPendentes'/>",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota').val()},
						{name:'filtro.data', value:$('#data').val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe').val()}						
						]
		});

		$(".encalheNfeGrid").flexReload();		
	}
	
	function mostrar_nfes(){
		$(".nfes").show();
		};

	function executarPreProcessamento(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return resultado;
			}
			var status = $('#situacaoNfe').val();					
			if(status == 'RECEBIDA'){
				$.each(resultado.rows, function(index, row) {
					
					var linkAviso = '<a href="javascript:;" style="cursor:pointer">' +
									   	 '<img title="Lançamentos da Edição" src="${pageContext.request.contextPath}/images/ico_alert.gif" hspace="5" border="0px" />' +
									   '</a>';
					
					row.cell.acao = linkAviso;
				});
				
			}else{
				
				$.each(resultado.rows, function(index, row) {					
					
					var linkLancamento = '<a href="javascript:;"  onclick="popup_nfe(\''+row.cell.numeroCota+'\',\''+row.cell.nome+'\');" style="cursor:pointer">' +
									   	 '<img title="Lançamentos da Edição" src="${pageContext.request.contextPath}/images/bt_lancamento.png" hspace="5" border="0px" />' +
									   '</a>';
				   var linkCadastro = '<a href="javascript:;" onclick="popup_dadosNotaFiscal('+row.cell.numeroNfe+','
						   																		+row.cell.dataEncalhe+','
						   																		+row.cell.chaveAcesso+','
						   																		+row.cell.serie+','
						   																		+row.cell.vlrNota+','
						   																		+row.cell.idNotaFiscalEntrada+');" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="${pageContext.request.contextPath}/images/bt_cadastros.png" hspace="5" border="0px" />' +
			                         '</a>';
                   var checkBox = '<input type="checkbox" id="checkNota" name="checkNota" />';
					
					row.cell.acao = linkLancamento + linkCadastro;
					row.cell.sel = checkBox;
				});
			
			}
			
			$(".grids").show();
			
			return resultado;
		}
	
	function pesquisarCota() {
 		
		numeroCota = $("#codigoCota").val();
 		
 		$.postJSON(
			'<c:url value="/nfe/consultaNFEEncalheTratamento/buscarCotaPorNumero" />',
			{ "numeroCota": numeroCota },
			function(result) {

				$("#nomeCota").html(result);
				
			},
			null,
			true
		);
 	}
</script>
<style type="text/css">
#dialog-nfe{display:none;}
  .dados, .dadosFiltro, .nfes{display:none;}
  #dialog-novo, #dialog-alterar, #dialog-excluir, #dialog-rejeitar, #dialog-confirm{display:none; font-size:12px;}
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialog-dadosNotaFiscal fieldset{width:810px!important;}
  </style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">
<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	<fieldset>
        <legend>Nota Fiscal</legend>
        <table width="670" border="0" cellspacing="1" cellpadding="1" style="color:#666;">
          <tr>
            <td width="133">Núm. Nota Fiscal:</td>
            <td width="307" id="numeroNotaFiscalPopUp"></td>
            <td width="106">Série:</td>
            <td width="111" id="serieNotaFiscalPopUp"></td>
          </tr>
          <tr>
            <td>Data:</td>
            <td id="dataNotaFiscalPopUp"></td>
            <td>Valor Total R$:</td>
            <td id="valorNotaFiscalPopUp"></td>
          </tr>
          <tr>
            <td>Chave de Acesso:</td>
            <td colspan="3" id="chaveAcessoNotaFiscalPopUp"></td>
          </tr>
        </table>
     </fieldset>
	<br clear="all" />
    <br />

	<fieldset>
        <legend>Produtos Nota Fiscal</legend>
        <table class="pesqProdutosNotaGrid"></table>
    


    <table width="800" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td width="352" align="right"><strong>Total:</strong>&nbsp;&nbsp;</td>
        <td width="83">09</td>
        <td width="82">06</td>
        <td width="182">&nbsp;</td>
        <td width="85">43,00</td>
      </tr>
    </table>
</fieldset>

</div>
<div id="dialog-nfe" title="NF-e">
	<fieldset style="width:310px!important;">
    <legend>Incluir NF-e</legend>
    <table width="280" border="0" cellspacing="1" cellpadding="0">
  <tr>
    <td width="84">Cota:</td>
    <td width="193"><input type="text" id="cotaCadastroNota" name="cotaCadastroNota" style="width:80px; float:left; margin-right:5px;"/>
      <span class="classPesquisar"><a href="javascript:;" onclick="pesqEncalhe();">&nbsp;</a></span></td>
  </tr>
  <tr>
    <td>Nome:</td>
    <td><input type="text" name="nomeCotaCadastroNota" id="nomeCotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>NF-e:</td>
    <td><input type="text" name="numeroNotaCadastroNota" id="numeroNotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>Série:</td>
    <td><input type="text" name="serieNotaCadastroNota" id="serieNotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>Chave-Acesso:</td>
    <td><input type="text" name="chaveAcessoCadastroNota" id="chaveAcessoCadastroNota" /></td>
  </tr>
</table>
    </fieldset>
 

</div>



<div id="dialog-confirmar-cancelamento" title="Cancelamento de NF-e" style="display:none;">
	<p>Confirma o cancelamento da NF-e?</p>
 

</div>

<div id="dialog-confirm" title="Aprovar Solicitação">
	<p>Você esta Aprovando uma Solicitação, tem certeza?</p>
 

</div>

<div id="dialog-rejeitar" title="Rejeitar Solicitação">
	<p>Tem certeza que deseja Rejeitar esta Solicitação?</p>
 

</div>



<div id="dialog-novo" title="Geração arquivos Nf-e">
     <p>Confirma a Geração arquivos Nf-e?</p>
</div>


<div class="corpo">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Arquivos Nf-e < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisa NF-e Encalhe para Tratamento</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="31">Cota:</td>
    <td width="120"><input type="text" id="codigoCota" name="codigoCota" style="width:80px; float:left; margin-right:5px;" onblur="pesquisarCota();"/></td>
    <td width="35">Nome:</td>    
    <td width="259"><span name="nomeCota" id="nomeCota"></span></td>
    <td width="35">Data:</td>
    <td width="105"><input name="data" type="text" id="data" style="width:80px;"/></td>
    <td width="42">Status:</td>
    <td width="173">    
		<select name="situacaoNfe" id="situacaoNfe" style="width:290px;" onchange="mostra_status(this.value);">
		    <option value=""  selected="selected"></option>
		    <c:forEach items="${comboStatusNota}" var="comboStatusNota">
		      		<option value="${comboStatusNota.key}">${comboStatusNota.value}</option>	
		    </c:forEach>
	    </select>
    </td><td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesqEncalhe();">Pesquisar</a></span></td></tr>
  </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend>NF-e Encalhe para Tratamento</legend>
        <div class="grids" style="display:none;">
		  
          
          <div id="notaRecebida" style="display:none;">
          	<table class="notaRecebidaGrid"></table>
          </div>
          
          <div id="pendenteRecEmissao" style="display:none;">
          	<table class="encalheNfeGrid"></table>
          </div>
          
          
          
			<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/nfe/consultaNFEEncalheTratamento/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/nfe/consultaNFEEncalheTratamento/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
			</span>
             
            <span class="bt_confirmar_novo" title="Confirmar Cancelamento"><a href="javascript:;" onclick="popup_nfe('0','0');">
            	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Registrar NF-e</a>
            </span>
            
            <span class="bt_confirmar_novo" title="Confirmar Cancelamento">
            	<a href="javascript:;" onclick="popup_confirmar();">
            	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Gerar</a>
            </span>
            
             
             <span class="bt_sellAll" style="float:right;"><label for="sel">Selecionar Todos</label><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left; margin-right:25px;"/></span>
             
		</div>
              
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

    </div>
</div> 
</form>
<script>
	$(".pesqProdutosNotaGrid").flexigrid({
		preProcess: executarPreProcessamento,
		dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dia',
				name : 'dia',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde. Info',
				name : 'qtdInformada',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde. Recebida',
				name : 'qtdRecebida',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desc R$',
				name : 'precoDescontoFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'totalDoItemFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			width : 810,
			height : 250
		});
	$(".notaRecebidaGrid").flexigrid({
		preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 280,
				sortable : true,
				align : 'left'
			}, {
				display : 'NF-e',
				name : 'numeroNfe',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Série',
				name : 'serie',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Chave Acesso',
				name : 'chaveAcesso',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'acao',
				width : 25,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	
	
	$(".encalheNfeGrid").flexigrid({
		preProcess: executarPreProcessamento,
		dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Encalhe',
				name : 'dataEncalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo de Nota',
				name : 'tipoNota',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor Nota R$',
				name : 'vlrNotaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Real R$',
				name : 'vlrRealFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Diferença',
				name : 'diferencaFormatado',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
</script>
</body>
</html>
