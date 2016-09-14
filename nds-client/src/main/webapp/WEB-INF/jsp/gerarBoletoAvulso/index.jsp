<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/boletoAvulso.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
	
	<script language="javascript" type="text/javascript">
	$(function() {
		GerarBoletoAvulsoController.init();
		// bloquearItensEdicao(GerarBoletoAvulsoController.workspace);
	});
	</script>
</head>
<body>
	<!-- DIALOG  UPLOAD -->		
	 <form id="formUploadBoletoAvulsoManual"  method="post" enctype="multipart/form-data">
		<div id="modalUploadArquivo-BoletoAvulsoManual" title="Adicionar em Lote" style="display:none;">
			<p>Utilize o modelo de exemplo para fazer upload para o sistema: </p>
			<p ><span class="bt_novos"><a href="${pageContext.request.contextPath}/modelos/modelo_boleto_avulso.xls" rel="tipsy" title="Download Modelo">
			<img align="center" src="images/ico_excel.png" hspace="5" border="0" /> Modelo de exemplo</a></span></p>
			<br><br><br>
			<hr>
			<p>Selecione um arquivo para upload:</p>
			<br>
			<p align="center"><input type="file" id="excelFileBoletoAvulso" name="excelFileBoletoAvulso" style="width:300px"/></p>
			
		</div>		
	  </form>


	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
          		<a isEdicao="true" id="boleto-avulso-Confirmar" onclick="GerarBoletoAvulsoController.confirmar();" rel="tipsy" title="Confirmar Gera&ccedil;&atilde;o do Boleto Avulso">
         		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif"></a>
            </span>
			<span class="bt_novos">
				<a href="javascript:;" id="boletoAvulsoImportacao" onclick="GerarBoletoAvulsoController.boletoAvulsoLote();">
					<img src="${pageContext.request.contextPath}/images/ico_integrar.png"
					alt="Confirmar" hspace="5" border="0" /></a>
			</span>
		</div>
		
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		<legend>Boleto Avulso</legend>
		
		<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialogMensagemNovo" name="messageDialog" />
			</jsp:include>
		
		<table width="950px" border="0" cellpadding="2" cellspacing="1" class="filtro">
		    <tbody>
		    	<tr>
		    	
		    		<td width="100">
		    			Cota:
		    		</td>
		    		<td>
    					<input maxlength="11" type="text" style="width:80px; float:left; margin-right:5px;" name="boleto-avulso-cota" id="boletoAvulsoEdicaoNumeroCota" onChange="GerarBoletoAvulsoController.pesquisarCota(true);"/>
    				</td>
    
    				<td width="100">
    					Nome:
    				</td>
    				<td width="310">
    					<input maxlength="80" type="text" style="width:300px;" name="boletoAvulso.nomeCota" id="edicaoNomeCota" />
    				</td>
		    		
				    <td>Regi&atilde;o:</td>
					<td colspan="3">
						<select name="boleto-avulso-idRegiao" id="boleto-avulso-idRegiao" style="width: 150px;">
							<option selected="selected">Selecione...</option>
							<c:forEach items="${listaRegiao}" var="regiao">
								<option value="${regiao.key}" label="${regiao.value}" />
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>Box:</td>
	    			<td>
						<select id="boleto-avulso-idBox" style="width:150px;"  onchange="GerarBoletoAvulsoController.changeBox();">
							<option selected="selected">Selecione...</option>	    
							<c:forEach items="${listaBox}" var="box">
								<option value="${box.key}" >${box.value}</option>
							</c:forEach>
						</select>
					</td>
    					
	   				<td>Rota:</td>
	   				<td>
						<select id="boleto-avulso-idRota" onchange="GerarBoletoAvulsoController.changeRota();" style="width:150px; font-size:11px!important">
	    					<option value="">Selecione...</option>
	    					<c:forEach items="${rotas}" var="rota">
								<option value="${rota.key }">${rota.value }</option>
							</c:forEach>
	   					</select>
	    			</td>
    			
	    			<td>Roteiro:</td>
					<td colspan="2">
	 	 					<select id="boleto-avulso-idRoteiro" onchange="GerarBoletoAvulsoController.changeRoteiro();" style="width:150x; font-size:11px!important">
	     						<option value="">Selecione...</option>
	    						<c:forEach items="${roteiros}" var="roteiro">
									<option value="${roteiro.key }">${roteiro.value }</option>
								</c:forEach>
	   						</select>
	   					</td>
	    			</td>
	    			
				</tr>
		    	<tr>
		    		<td width="91">Banco:</td>
					<td>
 	 					<select id="boleto-avulso-idBanco" style="width:150px; font-size:11px!important">
     						<option value="">Selecione...</option>
    						<c:forEach items="${bancos}" var="roteiro">
								<option value="${roteiro.key }">${roteiro.value }</option>
							</c:forEach>
   						</select>
   					</td>
	    			</td>
	    			<td>Data Vencimento:</td>
	    			<td>
	    				<input type="text" name="boleto-avulso-dataVencimento" id="boleto-avulso-dataVencimento" style="width:100px;" />
	    			</td>
	    			<td>Valor(R$):</td>
	    			<td colspan="2">
	    				<input maxlength="16" type="text" style="width:70px;" name="boleto-avulso-valor" id="boleto-avulso-valor" />
	    			</td>
		    	</tr>
		    	<tr>		    		
			    	<td>Observa&ccedil;&atilde;o: </td>
					<td colspan="4">
						<input maxlength="350" type="text" style="width:350px;" name="boleto-avulso-observacao" id="boleto-avulso-observacao" />
					</td>
			    	<td>
						<a href="javascript:;" onclick="GerarBoletoAvulsoController.obterInformacoesParaBoleto();" style="width:20px;">
		        			<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" hspace="5" border="0" />
		    			</a>
					</td>
		    	</tr>
		    	
		    	
		    </tbody>
		</table> 
   		<br />
		<br clear="all" />
		<form name="listaCotasBoletos" id="listaCotasBoletos">
			<table  width="950" class="boleto-avulso-Grid_1" id="boleto-avulso-Grid_1"></table>
			
			<table  width="950" border="0" cellspacing="2" cellpadding="2">
			    <tr>
				    <td width="65%"></td>    
				    
					<td width="35%">    
				        <span class="checar" style="float:right; margin-right:43px;">
				            <label for="boleto-avulso-textoSelTodos" id="boleto-avulso-textoSelTodos" style="float:left;">Marcar Todos</label>
				            <input title="Selecionar todos os itens" type="checkbox" id="boleto-avulso-selTodos" name="selTodos" onclick="GerarBoletoAvulsoController.selecionarTodos(this.checked);" style="float:left; margin-top:8px;"/>
				        </span>
				    </td>
	            </tr> 
		    </table>
		
		</form>
	</fieldset>
</div>		
</form>
</body>