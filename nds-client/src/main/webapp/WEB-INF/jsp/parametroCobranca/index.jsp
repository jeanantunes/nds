<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">

	$(function() {
		$(".parametrosGrid").flexigrid({
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Forma de Pagamento',
				name : 'forma',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'banco',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor Mínimo Emissão R$',
				name : 'vlr_minimo_emissao',
				width : 140,
				sortable : true,
				align : 'right'
			}, {
				display : 'Acumula Divida',
				name : 'acumula_divida',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobrança Unificada',
				name : 'cobranca_unificada',
				width : 100,
				sortable : true,
				align : 'center',
			}, {
				display : 'Forma Emissão',
				name : 'formaEmissao',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Envio por E-Mail',
				name : 'envio_email',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "Nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	});		
	
    function mostrarGridConsulta() {
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".parametrosGrid").flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/distribuidor/parametroCobranca/consultaParametrosCobranca' />",
			params: [
			         {name:'idBanco', value:$("#filtroBanco").val()},
			         {name:'tipoCobranca', value:$("#filtroTipoCobranca").val()}
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".parametrosGrid").flexReload();
		
		$(".grids").show();
	}	
	
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado;
		}	
		
		$.each(resultado.rows, function(index, row) {
			
			var linkEditar = '<a href="javascript:;" onclick="popup_alterar(' + row.cell.idParametro + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" onclick="popup_excluir(' + row.cell.idParametro + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkEditar + linkExcluir;
		});
			
		$(".grids").show();
		
		return resultado;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    function novoParametro() {
		
    	var tipoCobranca = $("#tipoCobranca").val();
		var banco = $("#banco").val();
		var valorMinimo = $("#valorMinimo").val();
		var taxaMulta = $("#taxaMulta").val();
		var valorMulta = $("#valorMulta").val();
		var taxaJuros = $("#taxaJuros").val();
		var instrucoes = $("#instrucoes").val();

		var acumulaDivida = $("#acumulaDivida").val();
		var vencimentoDiaUtil = $("#vencimentoDiaUtil").val();
		var unificada = $("#unificada").val();
		var envioEmail = $("#envioEmail").val();
		
		var formaEmissao = $("#formaEmissao").val();

		$.postJSON("<c:url value='/distribuidor/parametroCobranca/novoParametroCobranca'/>",
				   "tipoCobranca="+tipoCobranca+
				   "&banco="+ banco +
				   "&valorMinimo="+ valorMinimo+
				   "&taxaMulta="+ taxaMulta +
				   "&valorMulta="+ valorMulta+
				   "&taxaJuros="+ taxaJuros+
				   "&instrucoes="+ instrucoes+
				   "&acumulaDivida="+ acumulaDivida+
				   "&vencimentoDiaUtil="+ vencimentoDiaUtil+
				   "&unificada="+ unificada+
				   "&envioEmail="+ envioEmail+
				   "&formaEmissao="+ formaEmissao,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   mostrarGridConsulta();
	               },
				   null,
				   true);
	}
	
	function alteraParametro() {
		
		var idParametro = $("#idParametro").val();
    	var tipoCobranca = $("#tipoCobranca").val();
		var banco = $("#banco").val();
		var valorMinimo = $("#valorMinimo").val();
		var taxaMulta = $("#taxaMulta").val();
		var valorMulta = $("#valorMulta").val();
		var taxaJuros = $("#taxaJuros").val();
		var instrucoes = $("#instrucoes").val();

		var acumulaDivida = $("#acumulaDivida").val();
		var vencimentoDiaUtil = $("#vencimentoDiaUtil").val();
		var unificada = $("#unificada").val();
		var envioEmail = $("#envioEmail").val();
		
		var formaEmissao = $("#formaEmissao").val();

		$.postJSON("<c:url value='/distribuidor/parametroCobranca/alteraParametroCobranca'/>",
				   "idParametro="+idParametro+
				   "&tipoCobranca="+tipoCobranca+
				   "&banco="+ banco +
				   "&valorMinimo="+ valorMinimo+
				   "&taxaMulta="+ taxaMulta +
				   "&valorMulta="+ valorMulta+
				   "&taxaJuros="+ taxaJuros+
				   "&instrucoes="+ instrucoes+
				   "&acumulaDivida="+ acumulaDivida+
				   "&vencimentoDiaUtil="+ vencimentoDiaUtil+
				   "&unificada="+ unificada+
				   "&envioEmail="+ envioEmail+
				   "&formaEmissao="+ formaEmissao,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   mostrarGridConsulta();
	               },
				   null,
				   true);
	}
	
	function editarParametro(idParametro){
		var data = [{name: 'idParametro', value: idParametro}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/buscaParametroCobranca' />",
				   data,
				   sucessCallbackCadastroBanco, 
				   fecharDialogs);
	}
	
	function sucessCallbackCadastroBanco(resultado) {
		
		$("#tipoCobranca").val(resultado.);
		$("#banco").val(resultado.);
		$("#valorMinimo").val(resultado.);
		$("#taxaMulta").val(resultado.);
		$("#valorMulta").val(resultado.);
		$("#taxaJuros").val(resultado.);
		$("#instrucoes").val(resultado.);

		$("#acumulaDivida").val(resultado.);
		$("#vencimentoDiaUtil").val(resultado.);
		$("#unificada").val(resultado.);
		$("#envioEmail").val(resultado.);
		
		$("#formaEmissao").val(resultado.);
		
		
		popup_alterar();
	}
	
    function desativarParametro(idParametro) {
    	var data = [{name: 'idParametro', value: idParametro}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/desativaParametroCobranca'/>",
				   data,
				   function(result) {
			           mostrarGridConsulta();
			       },
				   null);
	}
	
    function limparTelaCadastroParametro() {
    	$("#tipoCobranca").val("");
		$("#banco").val("");
		$("#valorMinimo").val("");
		$("#taxaMulta").val("");
		$("#valorMulta").val("");
		$("#taxaJuros").val("");
		$("#instrucoes").val("");

		$("#acumulaDivida").val("");
		$("#vencimentoDiaUtil").val("");
		$("#unificada").val("");
		$("#envioEmail").val("");
		
		$("#formaEmissao").val("");
	}
	
	
	
	
	
	
	
	

    
    
	
	
	function popup() {
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:390,
			width:890,
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
	};
	
	
	
	function popup_alterar(idParametro) {
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:390,
			width:890,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					$( "#abaPdv" ).show( );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	
	
	function popup_excluir(idParametro) {
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
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

	function mostrarCobranca(){
		$(".linha_fornecedor").show();
	}
	
	</script>


	<style type="text/css">
	#dialog-excluir, #dialog-novo{display:none;}
	.linha_fornecedor{display:none;}
	</style>


</head>




<div id="dialog-excluir" title="Excluir Parâmetro de Cobrança">
	<p>Confirma a exclusão deste Parâmetro de Cobrança?</p>
</div>


<div id="dialog-novo" title="Incluir Forma de Recebimento">
	<table width="809" border="0" cellspacing="2" cellpadding="2">
	   
	   
	   <tr>
	     <td width="144">Forma de Pagamento:</td>
	     <td>
	     
		      <select name="tipoCobranca" id="tipoCobranca" style="width:150px;">
                   <c:forEach varStatus="counter" var="tipoCobranca" items="${listaTiposCobranca}">
			          <option value="${tipoCobranca.key}">${tipoCobranca.value}</option>
			       </c:forEach>
	          </select>  
	          
	     </td>
	     
	     <td width="204">Acumula Dívida:</td>
	     <td width="234">
		     <select name="acumulaDivida" id="acumulaDivida" style="width:80px;">
		        <option>Sim</option>
		        <option>Não</option>
		     </select>
	     </td>
	   </tr>
	   
	   
	   <tr>
		    <td width="144">Banco:</td>
		    <td>
		    
			    <select name="banco" id="banco" style="width:150px;">
                   <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
			          <option value="${banco.key}">${banco.value}</option>
			       </c:forEach>
	            </select>
	            
		    </td>
		    
		    <td>Vencimentos somente em dia útil:</td>
		    <td>
			    <select name="vencimentoDiaUtil" id="vencimentoDiaUtil" style="width:80px;">
			      <option>Sim</option>
			      <option>Não</option>
			    </select>
		    </td>
	   </tr>
	   
	   
	   <tr>
		    <td valign="top">Valor Mínimo Emissão:</td>
		    <td valign="top">
		        <input type="text" name="valorMinimo" id="valorMinimo" style="width:100px;" />
		    </td>
		    
		    <td>Cobrança Unificada:</td>
		    <td>
			    <select name="unificada" id="unificada" style="width:80px;">
			      <option>Sim</option>
			      <option>Não</option>
			    </select>
		        <br clear="all" />
		    </td>
	    </tr>
	    
	    
	    <tr>
		    <td>Multa  %:</td>
		    <td width="201"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td width="37%">
		              <input type="text" name="taxaMulta" id="taxaMulta" style="width:70px;" />
		          </td>
		          <td width="24%">&nbsp;ou  R$:</td>
		          <td width="39%">
		              <input type="text" name="valorMulta" id="valorMulta" style="width:70px;" />
		          </td>
		        </tr>
		    </table></td>
		    <td width="204">Envio por E-mail:</td>
		    <td colspan="2"><select name="envioEmail" id="envioEmail" style="width:80px;">
		      <option>Sim</option>
		      <option>Não</option>
		    </select></td>
	    </tr>
	  
	  
	    <tr>
		    <td>Juros%:</td>
		    <td><input type="text" name="taxaJuros" id="taxaJuros" style="width:100px;" /></td>
		    <td>Impressão:</td>
		    
		    <td>
		        
			    <select name="formaEmissao" id="formaEmissao" style="width:150px;">
                   <c:forEach varStatus="counter" var="formaEmissao" items="${listaFormasEmissao}">
			          <option value="${formaEmissao.key}">${formaEmissao.value}</option>
			       </c:forEach>
	            </select>
 
		    </td>
	    </tr>
	  
	  
	    <tr>
		    <td valign="top">Instruções:</td>
		    <td colspan="3">
		        <textarea name="instrucoes" rows="4" id="instrucoes" style="width:645px;"></textarea>
		    </td>
	    </tr>
	    
	</table>
</div>

<div class="container">	
<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
			<b>Parâmetro < evento > com < status >.</b></p>
</div>

   <fieldset class="classFieldset">
  	    <legend>Pesquisar Parâmetros de Cobrança</legend>
       <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
           <tr>
           
              <td width="113">Forma Pagamento:</td>
              <td colspan="3">
             
              <select name="filtroTipoCobranca" id="filtroTipoCobranca" style="width:150px;">
                   <c:forEach varStatus="counter" var="filtroTipoCobranca" items="${listaTiposCobranca}">
			          <option value="${filtroTipoCobranca.key}">${filtroTipoCobranca.value}</option>
			       </c:forEach>
	          </select>
	          
          </td>
          
          <td width="43">Banco:</td>
          <td width="450">
          
              <select name="filtroBanco" id="filtroBanco" style="width:150px;">
                   <c:forEach varStatus="counter" var="filtroBanco" items="${listaBancos}">
			          <option value="${filtroBanco.key}">${filtroBanco.value}</option>
			       </c:forEach>
	          </select>
	          
          </td>
          
          <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a></span></td>
        </tr>
    </table>
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>
   <fieldset class="classFieldset">
 
   	  <legend>Parâmetros de Cobranças Cadastrados</legend>
      <div class="grids" style="display:none;">
    	  <table class="parametrosGrid"></table>
      </div>

      <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   </fieldset>
  
   <div class="linha_separa_fields">&nbsp;</div>
</div>




