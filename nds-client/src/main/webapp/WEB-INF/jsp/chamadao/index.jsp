<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/chamadao.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script>

	var pesquisaCotaChamadao = new PesquisaCota(chamadaoController.workspace);

	$(function(){
		chamadaoController.init();

		bloquearItensEdicao(chamadaoController.workspace);
	});
		
</script>

</head>

<body>
<input type="hidden" value="${numeroCotaFollowUp}" id="chamadao-numeroCotaFollowUp" name="chamadao-numeroCotaFollowUp">
<input type="hidden" value="${dataCotaFollowUp}" id="dataCotaFollowUp" name="dataCotaFollowUp">
	
	<form id="formUsoMatrizRecolhimentoConfirmada">
		<div id="dialogUsoMatrizRecolhimentoConfirmada" title="Atenção" style="display: none">
			<p>A data escolhida já possui matriz de recolhimento confirmada. Deseja prosseguir mesmo assim?</p>
		</div>
	</form>
	
	<form id="form-confirm">
		<div id="dialog-confirm" title="Chamadão">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<br/>
			
			<strong>Confirma a Programação do Chamadão?</strong>
			
			<br/>
			
			<div id="divNovaDataChamadao">
			
				<br/>
			
				<p>
				    <strong>Nova Data Chamadão:</strong>
				    <input name="novaDataChamadao" id="novaDataChamadao" type="text"
				    	   style="width:70px; margin-right:5px;" />
			    </p>
			    
			    <br/>
			    
		    </div>
		    
		</div>
	</form>
	
	<div class="areaBts">
		<div class="area">
			
			<div id="divBotaoConfirmarChamadao" style="display: none">
	    			
   				<span class="bt_novos">
   					<a isEdicao="true" onclick="chamadaoController.confirmar('CONFIRMAR');" href="javascript:;" rel="tipsy"  title="Confirmar" >
   						<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
   					</a>
				</span>
						
			</div>
				
			<div id="divBotoesChamadaEncalhe" style="display: none">
							
				<span class="bt_novos">
					<a onclick="chamadaoController.confirmar('REPROGRAMAR');" href="javascript:;" rel="tipsy" title="Reprogramar">
						<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" />
					</a>
				</span>
				
				<span class="bt_novos" >
					<a href="javascript:;" onclick="chamadaoController.cancelarChamadao();" rel="tipsy" title="Cancelar">
						<img src="${pageContext.request.contextPath}/images/ico_bloquear.gif" hspace="5" border="0" />
					</a>
				</span>
				
			</div>
			
    		<span class="bt_arq" >
  					<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
    		</span>

			<span class="bt_arq" >
				<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0"/>
				</a>
			</span>
	
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend>Pesquisar</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  			<tr>
			    <td width="73">Cota:</td>
			    <td width="175">
			    	<input type="text" name="numeroCotaChamadao" id="numeroCotaChamadao" style="width:70px; float:left;
						   margin-right:5px;" onchange="pesquisaCotaChamadao.pesquisarPorNumeroCota('#numeroCotaChamadao', '#descricaoCota');" />
			    </td>
			    <td width="37">Nome:</td>
			    <td width="211">
			    	<input name="descricaoCota" id="descricaoCota" type="text"
		      		 	   class="nome_jornaleiro" maxlength="255" style="width:130px;"
		      		 	   onkeyup="pesquisaCotaChamadao.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaChamadao.pesquisarPorNomeCota('#numeroCotaChamadao', '#descricaoCota');" />
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
			   		<input type="checkbox" name="comChamadaEncalhe" id="comChamadaEncalhe" />
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
    					<a href="javascript:;" onclick="chamadaoController.validarMatrizRecolhimentoConfirmada();">Pesquisar</a>
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
	   				<td width="242" valign="top"></td>
	      				
	      			<td width="458">
				        <fieldset class="box_field" style="width:420px;">
				        	<legend>Chamadão</legend>
				        	<div class="box_resumo" style="width:408px;">
				            	<table width="409" border="0" cellspacing="1" cellpadding="1">
				                	<tr class="header_table">
				                    	<td height="23" align="right">&nbsp;</td>
				                      	<td align="center"><strong>Produtos</strong></td>
				                      	<td align="center"><strong>Exemplares</strong></td>
				                      	<td align="center"><strong>Total R$ (com desconto)</strong></td>
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