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
	
	
	
	
	
	
	
	
	
	
	
	//MODOS DE EXIBIÇÃO
	function opcaoPagto(op){
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			/*
			$('#divComboBanco').show();
			$('#divRecebeEmail').show();
			$('#divDadosBancarios').hide();
			*/
	    }
		else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
			/*
			$('#divComboBanco').show();
			$('#divDadosBancarios').show();
			$('#divRecebeEmail').hide();
			*/
		}    
		else if (op=='DEPOSITO'){
			/*
			$('#divDadosBancarios').hide();
			$('#divRecebeEmail').hide();
			$('#divComboBanco').show();
			*/
		}    
		else{
			/*
			$('#divRecebeEmail').hide();
			$('#divComboBanco').hide();
			$('#divDadosBancarios').hide();
			*/
		}
		
	};
	
	function mostraSemanal(){
		$("#tipoFormaCobranca").val('SEMANAL');
		document.formularioFormaCobranca.mensal.checked = false;
		$( ".semanal" ).show();
		$( ".mensal" ).hide();
	};
		
	function mostraMensal(){
		$("#tipoFormaCobranca").val('MENSAL');
		document.formularioFormaCobranca.semanal.checked = false;
		$( ".semanal" ).hide();
		$( ".mensal" ).show();
	};
	
	function opcaoTipoFormaCobranca(op){
		if (op=='SEMANAL'){
			document.formularioFormaCobranca.semanal.checked = true;
			document.formularioFormaCobranca.mensal.checked = false;
			mostraSemanal();
	    }
		else if (op=='MENSAL'){
			document.formularioFormaCobranca.semanal.checked = false;
			document.formularioFormaCobranca.mensal.checked = true;
			mostraMensal();
		}    
	};
	
	
	
	
	
	
	
	
	
	
	
	//OBTEM FORNECEDORES MARCADOS PELO USUARIO PARA UNIFICÁ-LOS
	function obterFornecedoresMarcados() {
		var fornecedorMarcado = "";
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			fornecedorMarcado += 'listaIdsFornecedores=' + $(this).val() + '&';
		});
		return fornecedorMarcado;
	}
	
	//OBTEM FORNECEDORES UNIFICADOS
	function obterFornecedoresUnificados(unificados) {
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			document.getElementById("fornecedor_"+$(this).val()).checked = false;
		});
		for(i=0;i<unificados.length;i++){
			document.getElementById("fornecedor_"+unificados[i]).checked = true;
		}
	}
	
	
	

	
	
	

	
	
	//INCLUSÃO DE NOVO PARAMETRO
    function novoParametro() {
    	
    	$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			document.getElementById("fornecedor_"+$(this).val()).checked = false;
		});
    	
    	$( ".semanal" ).hide();
		$( ".mensal" ).hide();
		document.formularioFormaCobranca.mensal.checked = false;
		document.formularioFormaCobranca.semanal.checked = false;
		
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
		
		$("#principal").val(0);
    	if (document.formularioParametro.principal.checked){
    		$("#principal").val(1);
		}
		var principal      = $("#principal").val();

		$.postJSON("<c:url value='/distribuidor/parametroCobranca/novoParametroCobranca'/>",
				   "parametros.tipoCobranca="+tipoCobranca+
				   "&parametros.idBanco="+ banco +
				   "&parametros.valorMinimo="+ valorMinimo+
				   "&parametros.taxaMulta="+ taxaMulta +
				   "&parametros.valorMulta="+ valorMulta+
				   "&parametros.taxaJuros="+ taxaJuros+
				   "&parametros.instrucoes="+ instrucoes+
				   "&parametros.acumulaDivida="+ acumulaDivida+
				   "&parametros.vencimentoDiaUtil="+ vencimentoDiaUtil+
				   "&parametros.unificada="+ unificada+
				   "&parametros.envioEmail="+ envioEmail+
				   "&parametros.formaEmissao="+ formaEmissao+
				   "&parametros.principal="+ principal,
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
	
	
	
	
	
	
	
	
	//ALTERAÇÃO DE UM PARÂMETRO
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
		
		$("#principal").val(0);
    	if (document.formularioNovoParametro.principal.checked){
    		$("#principal").val(1);
		}
		var principal      = $("#principal").val();

		$.postJSON("<c:url value='/distribuidor/parametroCobranca/alteraParametroCobranca'/>",
				   "parametros.idParametro="+idParametro+
				   "&parametros.tipoCobranca="+tipoCobranca+
				   "&parametros.idBanco="+ banco +
				   "&parametros.valorMinimo="+ valorMinimo+
				   "&parametros.taxaMulta="+ taxaMulta +
				   "&parametros.valorMulta="+ valorMulta+
				   "&parametros.taxaJuros="+ taxaJuros+
				   "&parametros.instrucoes="+ instrucoes+
				   "&parametros.acumulaDivida="+ acumulaDivida+
				   "&parametros.vencimentoDiaUtil="+ vencimentoDiaUtil+
				   "&parametros.unificada="+ unificada+
				   "&parametros.envioEmail="+ envioEmail+
				   "&parametros.formaEmissao="+ formaEmissao+
				   "&parametros.principal="+ principal,
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
	
	
	
	
	
	
	
	
	//OBTEM UM PARÂMETRO PARA ALTERAÇÃO
	function obterParametro(idParametro){
		var data = [{name: 'idParametro', value: idParametro}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/buscaParametroCobranca' />",
				   data,
				   sucessCallbackCadastroBanco, 
				   fecharDialogs);
	}
	
	function sucessCallbackCadastroBanco(resultado) {
		
		$("#tipoCobranca").val(resultado.tipoCobranca);
		$("#banco").val(resultado.idBanco);
		$("#valorMinimo").val(resultado.valorMinimo);
		$("#taxaMulta").val(resultado.taxaMulta);
		$("#valorMulta").val(resultado.valorMulta);
		$("#taxaJuros").val(resultado.taxaJuros);
		$("#instrucoes").val(resultado.instrucoes);

		$("#acumulaDivida").val(resultado.acumulaDivida);
		$("#vencimentoDiaUtil").val(resultado.vencimentoDiaUtil);
		$("#unificada").val(resultado.unificada);
		$("#envioEmail").val(resultado.envioEmail);
		
		$("#formaEmissao").val(resultado.formaEmissao);
		
		$("#principal").val(resultado.principal);
		document.formularioParametro.principal.checked = resultado.principal;
		
		opcaoPagto(resultado.tipoCobranca);
		opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
		obterFornecedoresUnificados(resultado.fornecedoresId);
		
		popup_alterar();
	}
	
	
	
	
	
	
	
	
	
	//EXCLUI (DESATIVA) UM PARÂMETRO
    function desativarParametro(idParametro) {
    	var data = [{name: 'idParametro', value: idParametro}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/desativaParametroCobranca'/>",
				   data,
				   function(result) {
			           mostrarGridConsulta();
			       },
				   null);
	}
	
	
	
	
	
	
	
	
	//LIMPA TODOS OS DADOS PARA INCLUSÃO DE NOVO REGISTRO
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
		
		novoParametro();
		
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:720,
			width:890,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
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
			height:720,
			width:890,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
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
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	
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

    <form id="formularioParametro" name="formularioParametro">

		<table width="809" border="0" cellspacing="2" cellpadding="2">
		   
		   <tr>
		       <td width="200">Principal</td>
			   <td width="200">
			       <input name="principal" type="checkbox"
			       id="principal" style="float: left;" value="" checked="checked" />
			   </td>
		   </tr>
		   
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
			        <option value="1">Sim</option>
			        <option value="0">Não</option>
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
				      <option value="1">Sim</option>
				      <option value="0">Não</option>
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
				      <option value="1">Sim</option>
				      <option value="0">Não</option>
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
			      <option value="1">Sim</option>
			      <option value="0">Não</option>
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
		
    </form>		
    
    
    
    <legend>Unificar Cobranças</legend>
    
	<form name="formularioFormaCobranca" id="formularioFormaCobranca">		
                         
	    <table width="434" height="25" border="0" cellpadding="1" cellspacing="1">
		    
		     <tr class="header_table">
		         <td align="left">Fornecedores</td>
		         <td align="left">&nbsp;</td>
		         <td align="left">Concentração de Pagamentos</td>
		     </tr>
		     
	         <tr>
	             <td width="170" align="left" valign="top" style="border:1px solid #ccc;">

	                 <table width="168" border="0" cellspacing="1" cellpadding="1">

	                      <c:forEach varStatus="counter" var="fornecedores" items="${listaFornecedores}">
	                          <tr> 
	                              <td width='23'>
							          <input id= "fornecedor_${filtroTipoCobranca.key}" value="${fornecedores.key}" name="checkGroupFornecedores" type='checkbox' />
							      </td> 
							      <td width='138'>	
									  <label for="fornecedor_${filtroTipoCobranca.key}" >${fornecedores.value}</label>
								  </td>
						      </tr>
					      </c:forEach>
					      
		             </table>
		             
	                 <p><br clear="all" />
		                 <br clear="all" />
		                 <br clear="all" />
		                 <br clear="all" />
	                 </p>
	                 
                 </td>
                 
			     <td width="21" align="left" valign="top">&nbsp;</td>
			     <td width="233" align="left" valign="top"style="border:1px solid #ccc;">

			         <table width="100%" border="0" cellspacing="1" cellpadding="1">
				         <tr>
				             <td width="20"><input type="radio" name="mensal" id="mensal" value="radio" onclick="mostraMensal();" /></td>
				             <td width="173">Mensal</td>
				             <td width="20"><input type="radio" name="semanal" id="semanal" value="radio" onclick="mostraSemanal();" /></td>
				             <td width="173">Semanal</td>
				         </tr>
				     </table>
				    
				     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="mensal">
				         <tr>
				             <td width="68">Todo dia:</td>
				             <td width="156"><input maxlength="2" type="text" name="diaDoMes" id="diaDoMes" style="width:60px;"/></td>
				         </tr>
				     </table>
			     
		        
                     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="semanal">
					        
			             <tr>
			                 <td>
			                     <input type="checkbox" name="PS" id="PS" />
			                 </td>    
			                 <td>
			                     <label for="PS">Segunda-feira</label>
			                 </td>
			             </tr>
					            
					     <tr>
			                 <td>           
					             <input type="checkbox" name="PT" id="PT" />
					         </td>    
			                 <td>    
					             <label for="PT">Terça-feira</label>
					         </td>
			             </tr>
			             
			             <tr>
			                 <td>            
					             <input type="checkbox" name="PQ" id="PQ" />
					         </td>    
			                 <td>      
					             <label for="PQ">Quarta-feira</label>
					         </td>
			              </tr>    
					                          
					      <tr>
			                 <td>          
					             <input type="checkbox" name="PQu" id="PQu" />
					          </td>    
			                  <td>  
					             <label for="PQu">Quinta-feira</label>
					          </td>
			              </tr>
					                  
					      <tr>
			                 <td>          
					             <input type="checkbox" name="PSex" id="PSex" />
					         </td>    
			                 <td>      
					             <label for="PSex">Sexta-feira</label>
					         </td>
			              </tr>    
					               
					      <tr>
			                 <td>    
					             <input type="checkbox" name="PSab" id="PSab" />
					             </td>    
			                 <td>  
					             <label for="PSab">Sábado</label>
					         </td>
			              </tr>
					                   
					      <tr>
			                  <td>
					             <input type="checkbox" name="PDom" id="PDom" />
					             </td>    
			                 <td>  
					             <label for="PDom">Domingo</label>
					         </td>
			              </tr>
					
					 </table>
					 
				 	
					 
			     </td>
  
             </tr>  

			 <tr>
			    <td valign="top">&nbsp;</td>
			    <td valign="top">&nbsp;</td>
			    <td valign="top">&nbsp;</td>
			 </tr>

		</table>
		
    </form>
    
    
    
    
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

      <span class="bt_novo" title="Novo"><a href="javascript:;" onclick="popup();">Novo</a></span>
   </fieldset>
  
   <div class="linha_separa_fields">&nbsp;</div>
</div>




