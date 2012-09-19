<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/chamadao.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script>

	var pesquisaCotaChamadao = new PesquisaCota();

	$(function(){
		chamadaoController.init();
	});
		
</script>

</head>

<body>
<input type="hidden" value="${numeroCotaFollowUp}" id="numeroCotaFollowUp" name="numeroCotaFollowUp">
<input type="hidden" value="${dataCotaFollowUp}" id="dataCotaFollowUp" name="dataCotaFollowUp">
	<form id="form-confirm">
	<div id="dialog-confirm" title="Chamadão">
		
		<jsp:include page="../messagesDialog.jsp" />
		
		<br />
		<strong>Confirma a Programação do Chamadão?</strong>
		<br />
	</div>
	</form>
	
	<fieldset class="classFieldset">
   	    <legend>Pesquisar</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  			<tr>
			    <td width="73">Cota:</td>
			    <td width="175">
			    	<input type="text" name="numeroCota" id="numeroCota" style="width:70px; float:left;
						   margin-right:5px;" onchange="pesquisaCotaChamadao.pesquisarPorNumeroCota('#numeroCota', '#descricaoCota');" />
			    </td>
			    <td width="37">Nome:</td>
			    <td width="211">
			    	<input name="descricaoCota" id="descricaoCota" type="text"
		      		 	   class="nome_jornaleiro" maxlength="255" style="width:130px;"
		      		 	   onkeyup="pesquisaCotaChamadao.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaChamadao.pesquisarPorNomeCota('#numeroCota', '#descricaoCota');" />
		      	</td>
			    <td width="94">Data Chamadão:</td>
			    <td width="220">
			    	<input type="text" name="dataChamadao" id="dataChamadao" style="width:70px; float:left; margin-right:5px;" />
			    </td>
				<td width="104">&nbsp;</td>
  			</tr>
  			<tr>
  				<td>Fornecedor:</td>
			    <td>
			   		<select name="idFornecedor" id="idFornecedor" style="width:190px;">
      					<option selected="selected" value="">Todos</option>
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
    				</select>
    			</td>
			    <td align="right">
			   		<input type="checkbox" name="checkbox" id="checkbox" />
			   	</td>
			    <td>Com CE</td>
			    <td>Editor:</td>
			    <td>
			    	<select name="idEditor" id="idEditor" style="width:190px;">
			      		<option selected="selected" value="">Todos</option>
			      		<c:forEach var="editor" items="${listaEditores}">
							<option value="${editor.key}">${editor.value}</option>
						</c:forEach>
			    	</select>
			    </td>
			    <td>
    				<span class="bt_pesquisar" title="Pesquisar">
    					<a href="javascript:;" onclick="chamadaoController.pesquisar();">Pesquisar</a>
    				</span>
    			</td>
		  </tr>
  			
		</table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display:none;">
		<fieldset class="classFieldset">
			<legend>Chamadão</legend>
	    
			<table class="chamadaoGrid"></table>
	        
	        <table width="949" border="0" cellspacing="1" cellpadding="1">
	   			<tr>
	   				<td width="318" valign="top">
	    				<span class="bt_confirmar_novo" title="Confirmar"><a onclick="chamadaoController.confirmar();" href="javascript:;"><img
							  border="0" hspace="5"
							  src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a>
						</span>
	
	      				<span class="bt_novos" title="Gerar Arquivo">
	      					<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
							</a>
	      				</span>
	
						<span class="bt_novos" title="Imprimir">
							<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
								Imprimir
							</a>
						</span>
					</td>
	      				
	      			<td width="458">
				        <fieldset class="box_field" style="width:320px;">
				        	<legend>Chamadão</legend>
				        	<div class="box_resumo" style="width:308px;">
				            	<table width="309" border="0" cellspacing="1" cellpadding="1">
				                	<tr class="header_table">
				                    	<td height="23" align="right">&nbsp;</td>
				                      	<td align="center"><strong>Produtos</strong></td>
				                      	<td align="center"><strong>Exemplares</strong></td>
				                      	<td align="center"><strong>Total R$</strong></td>
				                    </tr>
				                    <tr class="class_linha_1">
				                    	<td width="52" height="23"><strong>Parcial:</strong></td>
				                      	<td width="72" align="center">
				                      		<input id="qtdProdutosParcial" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td width="82" align="center">
				                      		<input id="qtdExemplaresParcial" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td width="90" align="center">
				                      		<input id="valorParcial" type="text" style="width:80px; text-align:right;" disabled="disabled"/>
				                      	</td>
				                    </tr>
				                    <tr class="class_linha_2">
				                      	<td height="23"><strong>Total:</strong></td>
				                      	<td align="center">
				                      		<input id="qtdProdutosTotal" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td align="center">
				                      		<input id="qtdExemplaresTotal" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td align="center">
				                      		<input id="valorTotal" type="text" style="width:80px; text-align:right;" disabled="disabled"/>
				                      	</td>
				                    </tr>
				          		</table>
				          	</div>
				      	</fieldset>
	       			</td>
	       			<td width="163" valign="top">
	       				<span class="bt_sellAll">
	       					<label for="sel" style="float:left;">Selecionar Todos</label>
	       					
	       					<input type="checkbox" name="checkAll" id="checkAll"
	       						   onclick="chamadaoController.selecionarTodos(this);" style="float:left;"/>
	       				</span>
	       			</td>
	      		</tr>
	 		</table>
		</fieldset>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>