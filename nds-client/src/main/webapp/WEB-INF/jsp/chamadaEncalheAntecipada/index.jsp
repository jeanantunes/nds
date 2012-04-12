<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
			
	<script language="javascript" type="text/javascript">
		
	$(function() {
		
		$("#dataAntecipacao").datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]').mask("99/99/9999");
		
		$("#edicao").numeric();
		
		$("#produto").autocomplete({source: ""});
		
	});
	
	var EncalheAntecipado = {
		
		params:function(){
			
			var formData = [ 
				             {name:"codigoProduto",value:$("#codigoProduto").val()},
				             {name:"numeroEdicao",value:$("#edicao").val()},
				             {name:"box",value:$("#box").val()},
				             {name:"fornecedor",value:$("#fornecedor").val()},
				            ];
			return formData;
		},
		
		getCodigoProdutoPesquisa: function (){
			
			return  "codigoProduto=" + $("#codigoProduto").val();
		},
			
		pesquisar: function(){
			
		},
		
		gravar: function (){
			
		},
		
		checkAll:function(input,checkgroup){
			checkAll(input,checkgroup);
		},
		
		exibirDialogData:function(){
			
			$("#dialog-novo" ).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						EncalheAntecipado.gravar();
						$("#dataAntecipacao").val("");
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
						$("#dataAntecipacao").val("");
					}
				}
			});
		},
		
		atualizarTotal:function(){
			
		},
		
		pesquisarProdutosSuccessCallBack:function() {
			
			EncalheAntecipado.pesquisarBox(EncalheAntecipado.getCodigoProdutoPesquisa());
			EncalheAntecipado.pesquisarFornecedor(EncalheAntecipado.getCodigoProdutoPesquisa());
			
			$("#dataProgramada").val("");
		},
		
		pesquisarProdutosErrorCallBack: function() {
			
			EncalheAntecipado.pesquisarBox(EncalheAntecipado.getCodigoProdutoPesquisa());
			EncalheAntecipado.pesquisarFornecedor(EncalheAntecipado.getCodigoProdutoPesquisa());
			
			$("#dataProgramada").val("");
		},
		
		montarComboFornecedores:function(result) {
			var comboFornecedores =  montarComboBox(result, true);
			
			$("#fornecedor").html(comboFornecedores);
		},
		
		montarComboBoxs:function(result) {
			var comboBoxes = "<option selected='selected'  value=''></option>";  
				
			comboBoxes = comboBoxes + montarComboBox(result, true);
			
			$("#box").html(comboBoxes);
		}, 
		
		pesquisarFornecedor:function(data){
		
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor' />",
					   data, EncalheAntecipado.montarComboFornecedores);
			
		},
		
		pesquisarBox:function(data){
			
			$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarBox' />",
					   data, EncalheAntecipado.montarComboBoxs);
		},
		
		validarEdicaoSuccessCallBack : function(){
			
			 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
             			 {name:"numeroEdicao",value:$("#edicao").val()},
						];
			
			 $.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada' />",
					   data, function(result){
				 $("#dataProgramada").val(result);
			 });
		},
		
		validarEdicaoErrorCallBack: function(){
			 $("#dataProgramada").val("");
		}
		
	};
	
	</script>
</head>

<body>
	
	<div id="dialog-novo" title="CE Antecipada">
		<p>Data Antecipada:<input name="dataAntecipacao" type="text" id="dataAntecipacao" style="width:80px;"/></p>
		<p>Confirma a gravação dessas informações? </p>      
	</div>
	
	<fieldset class="classFieldset">
   	   
   	   <legend> CE Produto</legend>
   	   
   	   <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		    <td width="54">Código:</td>
		    
		    <td colspan="3">
		    	
		    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
								   									   EncalheAntecipado.pesquisarProdutosErrorCallBack);" />
		    	
		    </td>
		    
		    <td width="76">Produto:</td>
		    <td width="258">
		    	<input type="text" name="produto" id="produto" style="width: 200px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
					    	   EncalheAntecipado.pesquisarProdutosSuccessCallBack,
					    	   EncalheAntecipado.pesquisarProdutosErrorCallBack);"/>
		    </td>
		    
		    <td width="45">Edição:</td>
		    <td width="100">
		    	
		    	<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
						   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
						   										EncalheAntecipado.validarEdicaoSuccessCallBack,
					    	   									EncalheAntecipado.validarEdicaoErrorCallBack);"/>
		    	
		    </td>
		    <td width="110">Data Programada:</td>
		    <td width="115">
		    	<input name="dataProgramada" type="text" id="dataProgramada" style="width:95px; text-align:center;" disabled="disabled" />
		    </td>
		  </tr>
		  <tr>
		    <td>Box:</td>
		    <td colspan="3">
			    <select name="box" id="box" style="width:100px;">
			      <option selected="selected"></option>
			      <option selected="selected">Todos</option>
			      <c:forEach var="box" items="${listaBoxes}">
							<option value="${box.key}">${box.value}</option>
				  </c:forEach>
			    </select>
			</td>
		    <td>Fornecedor:</td>
		    <td>
		    	<select name="fornecedor" id="fornecedor" style="width:220px;">
		      		<option selected="selected">Todos</option>
		      		<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
					</c:forEach>
		    	</select>
		    </td>
		    <td>&nbsp;</td>
		    <td colspan="2">&nbsp;</td>
		    <td>
		    	<span class="bt_pesquisar">
		    		<a href="javascript:;" onclick="mostrar();">Pesquisar</a>
		    	</span>
		    </td>
		  </tr>
        </table>

	 </fieldset>
     
     <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> CE Produto</legend>
          <div class="grids" style="display:none;">
			  
			  <table class="ceAntecipadaGrid"></table>
	          
	          <table width="950" border="0" cellspacing="1" cellpadding="1">
				  
			  		<tr>
					   	<td width="448" valign="top">
						    <span class="bt_confirmar_novo" title="Gravar">
						    	<a onclick="EncalheAntecipado.exibirDialogData();" href="javascript:;">
						    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Gravar
						    	</a>
						    </span>
						
						    <span class="bt_novos" title="Gerar Arquivo">
						    	<a href="javascript:;">
						    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
						    	</a>
						    </span>
						
							<span class="bt_novos" title="Imprimir">
								<a href="javascript:;">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
								</a>
							</span>
					    </td>
					      
					    <td width="452"> 
						     <div class="box_resumo" style="width: 300px;">
					          <table width="200" border="0" cellspacing="2" cellpadding="2">
					            <tr>
					              	<td width="8"> <strong>Totais</strong></td>
					              	<td width="178">
					                  <table width="150" border="0" cellspacing="1" cellpadding="1">
					                    <tr>
					                      <td width="73" height="23"><strong>Cotas:</strong></td>
					                      <td width="70">
					                      	<input type="text" id="idTotalCotas" style="width:60px; text-align:center;"  disabled="disabled"/>
					                      </td>
					                      <td width="70"><strong>Exemplares:</strong></td>
					                      <td width="70">
					                      	<input type="text" id="idTotalExemplares" style="width:60px; text-align:center;"  disabled="disabled"/>
					                      </td>
					                    </tr>
					                    </table>
					                </td>
					              </tr>
					            </table>
				           </div>
		          		</td>
		          		
		      			<td width="177" valign="top">
			        		<span class="bt_sellAll">
			        			<label for="sel">Selecionar Todos</label>
			        			<input type="checkbox" name="Todos" id="sel" onclick="EncalheAntecipado.checkAll();" style="float:left;"/>
			        		</span>
		    		     </td>
	      			</tr>
	 		</table>

		</div>
		
      </fieldset>
</body>
