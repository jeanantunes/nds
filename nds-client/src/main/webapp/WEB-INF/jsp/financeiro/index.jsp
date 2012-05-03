<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">


	//PRÉ CARREGAMENTO DA PAGINA
	function carregaFinanceiro(){
		var idCota = $("#_idCotaRef").val();
		obterParametroCobranca(idCota);
		mostrarGrid(idCota);
	}

	
	
	
	
	
	
	//GRID DE FORMAS DE COBRANÇA
    $(function() {	
		$(".boletosUnificadosGrid").flexigrid({
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel : [  {
				display : 'Fornecedores',
				name : 'fornecedor',
				width : 170,
				sortable : true,
				align : 'left'
			},{
				display : 'Concentração de Pagamento',
				name : 'concentracaoPagto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pagamento',
				name : 'tipoPagto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Detalhes - Tipo de Pagamento',
				name : 'detalhesTipoPagto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			width : 870,
			height : 150
		});
    });	
    
    function mostrarGrid(idCota) {
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosUnificadosGrid").flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/cadastro/financeiro/obterFormasCobranca' />",
			params: [
			         {name:'idCota', value:idCota},
			        ] ,
			        newp: 1
		});
		
		$(".grids").hide();
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosUnificadosGrid").flexReload();
		
		$(".grids").show();
	}	
	
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado;
		}	
		
		$.each(resultado.rows, function(index, row) {
			
			var linkEditar = '<a href="javascript:;" onclick="popup_editar_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" onclick="popup_excluir_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkEditar + linkExcluir;
		});
			
		$(".grids").show();
		
		return resultado;
	}
    
    
    
    
	
	
    //MODOS DE EXIBIÇÃO 
	function exibe_botao_contrato(contrato){
		if (contrato){
			$('#botaoContrato').show();
		}
		else{
			$('#botaoContrato').hide();
		}
	}

	function opcaoPagto(op){
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			$('#divComboBanco').show();
			$('#divRecebeEmail').show();
			$('#divDadosBancarios').hide();
	    }
		else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
			$('#divComboBanco').show();
			$('#divDadosBancarios').show();
			$('#divRecebeEmail').hide();
		}    
		else if (op=='DEPOSITO'){
			$('#divDadosBancarios').hide();
			$('#divRecebeEmail').hide();
			$('#divComboBanco').show();
		}    
		else{
			$('#divRecebeEmail').hide();
			$('#divComboBanco').hide();
			$('#divDadosBancarios').hide();
		}
		
	};
	
	function mostraSemanal(){
		$("#tipoFormaCobranca").val('SEMANAL');
		$( ".semanal" ).show();
		$( ".mensal" ).hide();
	};
		
	function mostraMensal(){
		$("#tipoFormaCobranca").val('MENSAL');
		$( ".semanal" ).hide();
		$( ".mensal" ).show();
	};
	
	function opcaoTipoFormaCobranca(op){
		if (op=='SEMANAL'){
			mostraSemanal();
			document.formularioFormaCobranca.mensal.checked = false;
			document.formularioFormaCobranca.semanal.checked = true;	
	    }
		else if (op=='MENSAL'){
			mostraMensal();
			document.formularioFormaCobranca.mensal.checked = true;
			document.formularioFormaCobranca.semanal.checked = false;
		}    
	};
	
	
	
	
	
	
	
	//PARAMETROS DE COBRANÇA
	function obterParametroCobranca(idCota){
		var data = [{name: 'idCota', value: idCota}];
		$.postJSON("<c:url value='/cadastro/financeiro/obterParametroCobranca' />",
				   data,
				   sucessCallbackParametroCobranca, 
				   null,
				   true);
	}

	function sucessCallbackParametroCobranca(resultado) {
		
		//hidden
		$("#_idParametroCobranca").val(resultado.idParametroCobranca);
		$("#_idCota").val(resultado.idCota);
		$("#_numCota").val(resultado.numCota);
	
		$("#fatorVencimento").val(resultado.fatorVencimento);
		
		$("#sugereSuspensao").val(resultado.sugereSuspensao);
		document.formFinanceiro.sugereSuspensao.checked = resultado.sugereSuspensao;
		
		$("#contrato").val(resultado.contrato);
		document.formFinanceiro.contrato.checked = resultado.contrato;
		exibe_botao_contrato(resultado.contrato);

		$("#valorMinimo").val(resultado.valorMinimo);
		$("#comissao").val(resultado.comissao);
		$("#qtdDividasAberto").val(resultado.qtdDividasAberto);
		$("#vrDividasAberto").val(resultado.vrDividasAberto);
		
	}

	function postarParametroCobranca() {
		
		//hidden
		var idParametroCobranca = $("#_idParametroCobranca").val();
		var idCota = $("#_idCota").val();
		var numCota = $("#_numCota").val();
		
		var fatorVencimento     = $("#fatorVencimento").val();
		
		$("#sugereSuspensao").val(0);
		if (document.formFinanceiro.sugereSuspensao.checked){
			$("#sugereSuspensao").val(1);
		}
		var sugereSuspensao = $("#sugereSuspensao").val();
		
		$("#contrato").val(0);
		if (document.formFinanceiro.contrato.checked){
			$("#contrato").val(1);
		}
		var contrato = $("#contrato").val();
	 
		var valorMinimo = $("#valorMinimo").val();
		var comissao = $("#comissao").val();
		var qtdDividasAberto = $("#qtdDividasAberto").val();
		var vrDividasAberto = $("#vrDividasAberto").val();
		
		$.postJSON("<c:url value='/cadastro/financeiro/postarParametroCobrancaSessao'/>",
				   "parametroCobranca.idParametroCobranca="+idParametroCobranca+ 
				   "&parametroCobranca.idCota="+idCota+ 
				   "&parametroCobranca.numCota="+numCota+    
				   "&parametroCobranca.fatorVencimento="+fatorVencimento+    
				   "&parametroCobranca.sugereSuspensao="+sugereSuspensao+    
				   "&parametroCobranca.contrato="+contrato+          
				   "&parametroCobranca.valorMinimo="+valorMinimo+        
				   "&parametroCobranca.comissao="+comissao+          
				   "&parametroCobranca.qtdDividasAberto="+qtdDividasAberto+   
				   "&parametroCobranca.vrDividasAberto="+vrDividasAberto,
				   null,
				   true);
	}

	
	
	
	
	
	
	//FORMAS DE COBRANÇA
	function obterFornecedoresUnificados(unificados) {
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			document.getElementById("fornecedor_"+$(this).val()).checked = false;
		});
		for(i=0;i<unificados.length;i++){
			document.getElementById("fornecedor_"+unificados[i]).checked = true;
		}
	}
	
	function obterFormaCobranca(idFormaCobranca){
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}];
		$.postJSON("<c:url value='/cadastro/financeiro/obterFormaCobranca' />",
				   data,
				   sucessCallbackFormaCobranca, 
				   null,
				   true);
	}

	function sucessCallbackFormaCobranca(resultado) {
		
		//hidden
		$("#_idFormaCobranca").val(resultado.idFormaCobranca);
		
		$("#tipoCobranca").val(resultado.tipoCobranca);
		$("#tipoFormaCobranca").val(resultado.tipoFormaCobranca);
		$("#banco").val(resultado.idBanco);
		$("#numBanco").val(resultado.numBanco);
		$("#nomeBanco").val(resultado.nomeBanco);
		$("#agencia").val(resultado.agencia);
		$("#agenciaDigito").val(resultado.agenciaDigito);
		$("#conta").val(resultado.conta);
	    $("#contaDigito").val(resultado.contaDigito);
	    $("#diaDoMes").val(resultado.diaDoMes);
		
		$("#recebeEmail").val(resultado.recebeEmail);
		document.formularioDadosBoleto.recebeEmail.checked = resultado.recebeEmail;

		$("#PS").val(resultado.segunda);
		document.formularioFormaCobranca.PS.checked = resultado.segunda;
		
		$("#PT").val(resultado.terca);
		document.formularioFormaCobranca.PT.checked = resultado.terca;
		
		$("#PQ").val(resultado.quarta);
		document.formularioFormaCobranca.PQ.checked = resultado.quarta;
		
		$("#PQu").val(resultado.quinta);
		document.formularioFormaCobranca.PQu.checked = resultado.quinta;
		
		$("#PSex").val(resultado.sexta);
		document.formularioFormaCobranca.PSex.checked = resultado.sexta;
		
		$("#PSab").val(resultado.sabado);
		document.formularioFormaCobranca.PSab.checked = resultado.sabado;
		
		$("#PDom").val(resultado.domingo);
		document.formularioFormaCobranca.PDom.checked = resultado.domingo;
		
		opcaoPagto(resultado.tipoCobranca);
		opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
		obterFornecedoresUnificados(resultado.fornecedoresId);
	}
	
	function obterFornecedoresMarcados() {
		var fornecedorMarcado = "";
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			fornecedorMarcado += 'listaIdsFornecedores=' + $(this).val() + '&';
		});
		return fornecedorMarcado;
	}
	
	function postarFormaCobranca(novo) {
		
		//hidden
		var idFormaCobranca = $("#_idFormaCobranca").val();
		var idCota = $("#_idCota").val();
		var idParametroCobranca = $("#_idParametroCobranca").val();
		
		var tipoCobranca        = $("#tipoCobranca").val();
		var tipoFormaCobranca   = $("#tipoFormaCobranca").val();
		var idBanco             = $("#banco").val();
		var numBanco            = $("#numBanco").val();
		var nomeBanco           = $("#nomeBanco").val();
		var agencia             = $("#agencia").val();
		var agenciaDigito       = $("#agenciaDigito").val();
		var conta               = $("#conta").val();
		var contaDigito         = $("#contaDigito").val();
		var diaDoMes            = $("#diaDoMes").val();

		$("#recebeEmail").val(0);
		if (document.formularioDadosBoleto.recebeEmail.checked){
			$("#recebeEmail").val(1);
		}
		var recebeEmail = $("#recebeEmail").val();
		
		$("#PS").val(0);
		if (document.formularioFormaCobranca.PS.checked){
			$("#PS").val(1);
		}
		var segunda = $("#PS").val();
		
		$("#PT").val(0);
		if (document.formularioFormaCobranca.PT.checked){
			$("#PT").val(1);
		}
		var terca = $("#PT").val();
		
		$("#PQ").val(0);
		if (document.formularioFormaCobranca.PQ.checked){
			$("#PQ").val(1);
		}
		var quarta = $("#PQ").val();
		
		$("#PQu").val(0);
		if (document.formularioFormaCobranca.PQu.checked){
			$("#PQu").val(1);
		}
		var quinta = $("#PQu").val();
		
		$("#PSex").val(0);
		if (document.formularioFormaCobranca.PSex.checked){
			$("#PSex").val(1);
		}
		var sexta  = $("#PSex").val();
		
		$("#PSab").val(0);
		if (document.formularioFormaCobranca.PSab.checked){
			$("#PSab").val(1);
		}
		var sabado = $("#PSab").val();
		
		$("#PDom").val(0);
		if (document.formularioFormaCobranca.PDom.checked){
			$("#PDom").val(1);
		}
		var domingo  = $("#PDom").val();
		 	
		if (novo) {
			$.postJSON("<c:url value='/cadastro/financeiro/postarFormaCobranca'/>",
					   "formaCobranca.idCota="+idCota+ 
					   "&formaCobranca.idParametroCobranca="+idParametroCobranca+ 
					   "&formaCobranca.tipoCobranca="+tipoCobranca+  
					   "&formaCobranca.idBanco="+idBanco+            
					   "&formaCobranca.recebeEmail="+recebeEmail+    
					   "&formaCobranca.numBanco="+numBanco+        
					   "&formaCobranca.nomeBanco="+nomeBanco+          
					   "&formaCobranca.agencia="+agencia+            
					   "&formaCobranca.agenciaDigito="+agenciaDigito+     
					   "&formaCobranca.conta="+conta+              
					   "&formaCobranca.contaDigito="+contaDigito+        
					   "&formaCobranca.domingo="+domingo+    
					   "&formaCobranca.segunda="+segunda+            
					   "&formaCobranca.terca="+terca+            
					   "&formaCobranca.quarta="+quarta+            
					   "&formaCobranca.quinta="+quinta+            
					   "&formaCobranca.sexta="+sexta+            
					   "&formaCobranca.sabado="+sabado+
					   "&formaCobranca.diaDoMes="+diaDoMes+
					   "&tipoFormaCobranca="+tipoFormaCobranca+
					   "&"+obterFornecedoresMarcados(),
					   null,
					   true);
		}
		else{
			$.postJSON("<c:url value='/cadastro/financeiro/postarFormaCobranca'/>",
					   "formaCobranca.idFormaCobranca="+idFormaCobranca+ 
					   "&formaCobranca.idCota="+idCota+ 
					   "&formaCobranca.idParametroCobranca="+idParametroCobranca+ 
					   "&formaCobranca.tipoCobranca="+tipoCobranca+  
					   "&formaCobranca.idBanco="+idBanco+            
					   "&formaCobranca.recebeEmail="+recebeEmail+    
					   "&formaCobranca.numBanco="+numBanco+        
					   "&formaCobranca.nomeBanco="+nomeBanco+          
					   "&formaCobranca.agencia="+agencia+            
					   "&formaCobranca.agenciaDigito="+agenciaDigito+     
					   "&formaCobranca.conta="+conta+              
					   "&formaCobranca.contaDigito="+contaDigito+        
					   "&formaCobranca.domingo="+domingo+    
					   "&formaCobranca.segunda="+segunda+            
					   "&formaCobranca.terca="+terca+            
					   "&formaCobranca.quarta="+quarta+            
					   "&formaCobranca.quinta="+quinta+            
					   "&formaCobranca.sexta="+sexta+            
					   "&formaCobranca.sabado="+sabado+
					   "&formaCobranca.diaDoMes="+diaDoMes+
					   "&tipoFormaCobranca="+tipoFormaCobranca+
					   "&"+obterFornecedoresMarcados(),
					   null,
					   true);
	    }
		mostrarGrid(idCota)
	}
	
	function excluirFormaCobranca(idFormaCobranca){
		var idCota = $("#_idCota").val();
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}];
		$.postJSON("<c:url value='/cadastro/financeiro/excluirFormaCobranca' />",
				   data,
				   mostrarGrid(idCota), 
				   null,
				   true);
	}
	
	

	
	
	
	
	//POPUPS
	function popup_nova_unificacao() {
		
		$( "#dialog-unificacao" ).dialog({
			resizable: false,
			height:800,
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					postarFormaCobranca(true);
					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
    };

    function popup_editar_unificacao(idFormaCobranca) {
    	
    	obterFormaCobranca(idFormaCobranca);
		
		$( "#dialog-unificacao" ).dialog({
			resizable: false,
			height:550,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					postarFormaCobranca(false);
					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
    };
    
    function popup_excluir_unificacao(idFormaCobranca) {
    	
		$( "#dialog-excluir-unificacao" ).dialog({
			resizable: false,
			height:170,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					excluirFormaCobranca(idFormaCobranca);
					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	
	
	
	
	
	//IMPRESSÃO DO CONTRATO
	function imprimeContrato(){
		var idCota = $("#_idCota").val();
	    document.location.assign("${pageContext.request.contextPath}/cadastro/financeiro/imprimeContrato?idCota="+idCota);
	}
	
	
	
	
	
	
	
	
</script>


<style>

#divRecebeEmail,
#divTrasnsferenciaBancaria,
#divDadosBancarios {display:none;}
#dialog-pesq-fornecedor{display:none;}
.forncedoresSel, .semanal, .mensal{display:none;}
#dialog-unificacao, #dialog-excluir-unificacao{display:none;}
#dialog-excluir-unificacao fieldset{width:430px!important;}
#dialog-unificacao fieldset {width:440px!important;}

</style>


</head>

   <!--PESSOA FISICA - FINANCEIRO -->
    
     
    
    <!--  <div id="tabpf-financeiro" > -->
       
    <input type="hidden" name="_idCota" id="_idCota"/>
    <input type="hidden" name="_numCota" id="_numCota"/>
    <input type="hidden" name="_idFormaCobranca" id="_idFormaCobranca"/>
    <input type="hidden" name="_idParametroCobranca"id="_idParametroCobranca"/>
    
    <input type="hidden" name="tipoFormaCobranca" id="tipoFormaCobranca"/>
   
    <form name="formFinanceiro" id="formFinanceiro">
	    <table width="671" border="0" cellspacing="2" cellpadding="2">
		      
		   <tr>
		   
		     <td width="212">Contrato:</td>
		     <td width="254">
		         <input id="contrato" name="contrato" type="checkbox" style="float:left;" onclick="exibe_botao_contrato(this.checked);" />
		         <span name="botaoContrato" id="botaoContrato" class="bt_imprimir">
		             <!-- BOTAO PARA IMPRESSÃO DE CONTRATO - FUNÇÃO PROVISÓRIA ADICIONADA PARA POSTAR DADOS NA SESSAO -->
		             <a href="javascript:;" onclick="imprimeContrato()">Contrato</a>
		         </span>
		     </td>
		      
		     <td width="168">Fator Vencimento em D+:</td>
		       
		     <td width="123">
			     <select id="fatorVencimento" name="fatorVencimento" size="1" multiple="multiple" style="width:50px; height:19px;" >
			       <option>1</option>
			       <option>2</option>
			       <option>3</option>
			       <option>4</option>
			       <option>5</option>
			       <option>6</option>
			       <option>7</option>
			       <option>8</option>
			       <option>9</option>
			       <option>10</option>
			       <option>11</option>
			       <option>12</option>
			       <option>13</option>
			       <option>14</option>
			       <option>15</option>
			       <option>16</option>
			       <option>17</option>
			       <option>18</option>
			       <option>19</option>
			       <option>20</option>
			     </select>
		     </td>
		     
		   </tr>
		   
		   <tr>
		     
		     <td>Valor Mínimo R$:</td>
		     <td>
		         <input name="valorMinimo" id="valorMinimo" type="text" style="width:60px;" />
		     </td>
		     
		     <td>Comissão %:</td>
		     <td>
		         <input name="comissao" id="comissao" type="text" style="width:60px;" />
		     </td>
		     
		   </tr>
		   
		   <tr>
		     <td>Sugere Suspensão:</td>
		     <td><input id="sugereSuspensao" name="sugereSuspensao" type="checkbox" value="" /></td>
		   </tr>

		   <tr>
		     <td height="23">Sugere Suspensão quando:</td>
		     <td>
			     <table width="100%" border="0" cellspacing="0" cellpadding="0">
			           
			        <tr>
			           <td width="31%">Qtde de dividas em aberto:</td>
			            
			           <td width="13%">
			               <input type="text" name="qtdDividasAberto" id="qtdDividasAberto" style="width:60px;" />
			           </td>
			            
			           <td width="8%">ou</td>
			           <td width="6%">R$: </td>
			            
			           <td width="42%">
			               <input type="text" name="vrDividasAberto" id="vrDividasAberto" style="width:60px;" />
			           </td>
			       </tr>
			       
		         </table>
		      </td>
		   </tr>

		</table>
	</form>  

	<strong>Formas de Pagamento</strong>
	  
	<table class="boletosUnificadosGrid"></table>
	  
	<br clear="all" />
	  
	<span class="bt_novos" title="Nova Unificação">
	    <a href="javascript:;" onclick="popup_nova_unificacao();">
	        <img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
	        Nova Forma de Pagamento
	    </a>
	</span>
			
			
    <br clear="all" />


    
    
    
    <div id="dialog-unificacao" title="Nova Unificação de Boletos">
		<fieldset>
			<legend>Unificar Boletos</legend>
			
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

			                    <c:forEach items="${fornecedores}" var="fornecedor">     
			                        <tr>
							            <td width="23">
			                    	        <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}" name="checkGroupFornecedores" type="checkbox"/>
			                    	    </td>
			                    	    <td width="138">
			                    	        <label for="fornecedor_${fornecedor.id}" >${fornecedor.juridica.razaoSocial}</label>
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
						             <td width="156"><input type="text" name="diaDoMes" id="diaDoMes" style="width:60px;"/></td>
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
		
					  
					 <tr>
				        <td valign="top"><strong>Tipo de Pagamento:</strong></td>
					    <td valign="top">&nbsp;</td>
					    <td valign="top">
		
					        <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="opcaoPagto(this.value);">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
		
				        </td>    
				     </tr>
	
				</table>
				
		    </form>
		    

	        <div id="divComboBanco" style="display:none;">   
	            
	            <form name="formularioDadosBanco" id="formularioDadosBanco">	
	            
	                <table width="417" border="0" cellpadding="2" cellspacing="2">
	                    <tr>
				      	  <td colspan="2"><b>Dados do Banco</b></td>
				       	</tr>
				       	  
				      	<tr>
				      	  <td width="57">Nome:</td>
				      	  <td width="346">
				    	    
				    	    <select name="banco" id="banco" style="width:150px;">
				    	        <option value=""></option>
	                            <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
					                <option value="${banco.key}">${banco.value}</option>
					            </c:forEach>
	                        </select>
				    	 
				      	</tr>
	                 </table>
	                 
		         </form>
	        </div>    
	        
        
        
	        <div id="divRecebeEmail" style="display:none;">   
	        
                <form name="formularioDadosBoleto" id="formularioDadosBoleto">
                
			  		<table width="417" border="0" cellpadding="2" cellspacing="2">
			  		
				      	<tr>
				      	  <td align="right">
				      	      <input type="checkbox" id="recebeEmail" name="recebeEmail" /></td>
				      	  <td>Receber por E-mail?</td>
				        </tr>
				        
			        </table>
			        
		        </form>
		        
	        </div>   
       
       
       
	        <div id="divDadosBancarios" style="display:none;">   
	        
				<form name="formularioDadosDeposito" id="formularioDadosDeposito">   
				           
		      	 	<table width="558" border="0" cellspacing="2" cellpadding="2">
						  
						  <tr>
						    <td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
						  </tr>
						  
						  <tr>
						    <td width="88">Num. Banco:</td>
						    <td width="120"><input type="text" id="numBanco" name="numBanco" style="width:60px;" /></td>
						    <td width="47">Nome:</td>
						    <td width="277"><input type="text" id="nomeBanco" name="nomeBanco" style="width:150px;" /></td>
						  </tr>
						  
						  <tr>
						    <td>Agência:</td>
						    <td><input type="text" id="agencia" name="agencia" style="width:60px;" />
						      -
						      <input type="text" id="agenciaDigito" name="agenciaDigito" style="width:30px;" /></td>
						    <td>Conta:</td>
						    <td>
						        <input type="text" id="conta" name="conta" style="width:60px;" />
						      -
						        <input type="text" id="contaDigito" name="contaDigito" style="width:30px;" /></td>
						  </tr>
						  
						  <tr>
						    <td>&nbsp;</td>
						    <td>&nbsp;</td>
						    <td>&nbsp;</td>
						    <td>&nbsp;</td>
						  </tr>
						  
					 </table>
					 
				 </form>
				 
	        </div>


			<br clear="all" />
			<span class="bt_add">
			    <a href="javascript:;" onclick="postarFormaCobranca();">
			        Incluir Novo
			    </a>
			</span>


	    </fieldset>
	</div>	
			
	<div id="dialog-excluir-unificacao" title="Unificação de Boletos">
	<fieldset>
		<legend>Exclusão de Unificação de Boletos</legend>
	    <p>Confirma a exclusão desta Unificação de Boleto</p>
	</fieldset>
	</div>
	
	
	<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
	<fieldset>
		<legend>Selecione um ou mais Fornecedores para unificação dos boletos</legend>
	    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
	      <option>Dinap</option>
	      <option>FC</option>
	      <option>Treelog</option>
	    </select>
	</fieldset>
	</div>
				    		    
    
    <!-- /PESSOA FISICA - FINANCEIRO -->  
    