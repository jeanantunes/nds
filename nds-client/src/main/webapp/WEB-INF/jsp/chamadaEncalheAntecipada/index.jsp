<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptChamdaEncalheAntecipada.js"></script>
	
	<script type="text/javascript">
	
		var pesquisaCotaChamadaAntecipada = new PesquisaCota(chamdaEncalheAnteipadaController.workspace);
		
		var pesquisaProdutoChamadaAntecipada = new PesquisaProduto(chamdaEncalheAnteipadaController.workspace);
	
		chamdaEncalheAnteipadaController.init(pesquisaCotaChamadaAntecipada);
		
		bloquearItensEdicao(chamdaEncalheAnteipadaController.workspace);
		
	</script>
			
</head>

<body>
	
	<form action="/devolucao/chamadaEncalheAntecipada/" id="dialog_confirmacao_chamdaEncalhe_form">
		
		<div id="dialog-novo" title="CE Antecipada">
		
			<jsp:include page="../messagesDialog.jsp" />
			
			<p>Data Antecipada:<input name="dataAntecipacao" type="text" id="dataAntecipacao" style="width:80px;"/></p>
			<p>Confirma a gravação dessas informações? </p>      
		</div>
	
	</form>
	
	<form action="/devolucao/chamadaEncalheAntecipada/" id="dialog_cancelar_chamdaEncalhe_form">
		
		<div id="dialog-cancelamentoCE" title="CE Antecipada" style="display: none;">
			<p>Deseja cancelar a Programação da CE Antecipada deste Produto? </p>      
		</div>
	
	</form>
	
	<form action="/devolucao/chamadaEncalheAntecipada/" id="dialog_consulta_chamdaEncalhe_form">
		<div class="areaBts">
			<div class="area">
				<span class="bt_novos" id="bt_confirmar_novo">
			    	<a isEdicao="true" onclick="chamdaEncalheAnteipadaController.exibirDialogData('Novo');" href="javascript:;" rel="tipsy" title="Gravar">
			    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
			    	</a>
			    </span>
			    <span class="bt_novos" id="bt_reprogramar_CE">
			    	<a isEdicao="true" onclick="chamdaEncalheAnteipadaController.exibirDialogData('');" href="javascript:;" rel="tipsy" title="Reprogramar">
			    		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/bt_devolucao.png">
			    	</a>
			    </span>   
				<span class="bt_novos" id="bt_cancelar_programacao">
					<a isEdicao="true" onclick="chamdaEncalheAnteipadaController.exibirDialogCancelamentoCE();" href="javascript:;" rel="tipsy" title="Cancelar Programação">
						<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_bloquear.gif">
					</a>
				</span>
			    <span class="bt_arq">
			    	<a href="javascript:;" onclick="chamdaEncalheAnteipadaController.exportar('XLS')" rel="tipsy" title="Gerar Arquivo">
			    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			    	</a>
			    </span>
			
				<span class="bt_arq">
					<a href="javascript:;" onclick="chamdaEncalheAnteipadaController.exportar('PDF')" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
				
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	   	   
	   	   <legend> CE Produto</legend>
	   	   
	   	   <input type="hidden" id="codProdutoHidden" name="codProdutoHidden" value=""/>
	   	   <input type="hidden" id="numeroEdicaoHidden" name="numeroEdicaoHidden" value=""/>
	   	   <input type="hidden" id="codFornecedorHidden" name="codFornecedorHidden" value=""/>
	   	   <input type="hidden" id="codMunicipioHidden" name="codMunicipioHidden" value=""/>
	   	   <input type="hidden" id="codTipoPontoPdvHidden" name="codTipoPontoPdvHidden" value=""/>
	   	   
	   	   <table width="950" border="0" cellpadding="2" cellspacing="1">
			
			<tbody>		
			
			 <tr>
			    <td width="54">Código:</td>
			    
			    <td colspan="3">
			    	
			    	<input type="text" name="codigoProduto" id="codigoProduto" class="campoDePesquisa"
							   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
							   onchange="pesquisaProdutoChamadaAntecipada.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
									   									   chamdaEncalheAnteipadaController.pesquisarProdutosSuccessCallBack,
									   									   chamdaEncalheAnteipadaController.pesquisarProdutosErrorCallBack);" />
			    	
			    </td>
			    
			    <td width="54">Produto:</td>
			    <td width="264">
			    	<input type="text" name="produto" id="produto" style="width: 213px;" maxlength="255" class="campoDePesquisa"
						       onkeyup="pesquisaProdutoChamadaAntecipada.autoCompletarPorNomeProduto('#produto', false);"
						       onblur="pesquisaProdutoChamadaAntecipada.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
						    	   chamdaEncalheAnteipadaController.pesquisarProdutosSuccessCallBack,
						    	   chamdaEncalheAnteipadaController.pesquisarProdutosErrorCallBack);"/>
			    </td>
			    
			    <td width="42">Edição:</td>
			    <td width="165">
			    	
			    	<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled" class="campoDePesquisa"
							   onchange="pesquisaProdutoChamadaAntecipada.validarNumEdicao('#codigoProduto', '#edicao', false,
							   										chamdaEncalheAnteipadaController.validarEdicaoSuccessCallBack,
						    	   									chamdaEncalheAnteipadaController.validarEdicaoErrorCallBack);"/>
			    	
			    </td>
			    <td width="112">Data Programada:</td>
			    <td width="106">
			    	<input name="dataProgramada" type="text" id="dataProgramada" style="width:95px; text-align:center;" disabled="disabled" class="campoDePesquisa" />
			    </td>
			  </tr>
			  
			  </tbody>
			</table>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tbody>
			  <tr>
			    <td width="58">Box:</td>
			    <td colspan="3">
				    <select name="box" id="box" style="width:150px;" onchange="chamdaEncalheAnteipadaController.recarregarComboRoteiroRotas(this.value)" class="campoDePesquisa">
				      <option selected="selected" value="-1">Selecione</option>
				      <option value="">Todos</option>
				      <c:forEach var="box" items="${listaBoxes}">
								<option value="${box.key}">${box.value}</option>
					  </c:forEach>
				    </select>
				</td>
				
				<td width="81">Roteiro:</td>
			    <td width="187">
			    	<select class="campoDePesquisa" name="roteiro" id="roteiro" style="width:150px; float:left; margin-right:5px;" onchange="chamdaEncalheAnteipadaController.recarregarComboRotas(this.value)" >
	
				      <option selected="selected" value="">Todos</option>
				      <c:forEach var="roteiro" items="${listaRoteiros}">
								<option value="${roteiro.key}">${roteiro.value}</option>
					  </c:forEach>
				    </select>
			    </td>
			    <td width="37">Rota:</td>
			    <td width="155">
			    	<select class="campoDePesquisa" name="rota" id="rota" style="width:150px; float:left; margin-right:5px;"  >
	
				      <option selected="selected" value="">Todos</option>
				      <c:forEach var="rota" items="${listaRotas}">
								<option value="${rota.key}">${rota.value}</option>
					  </c:forEach>
				    </select>
			    </td>
			    <td width="82">Fornecedor:</td>
			    <td>
			    	<select class="campoDePesquisa" name="fornecedor" id="fornecedor" style="width:130px;">
	
			      		<option selected="selected">Todos</option>
			      		<c:forEach var="fornecedor" items="${listaFornecedores}">
								<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
			    	</select>
			    </td>
			  </tr>
			 
			  <tr>
				
				<td>Munic&iacute;pio:</td>
				
			  	<td colspan="3">
					
					<select class="campoDePesquisa" name="municipio" id="municipio" style="width:150px;">
			      		<option selected="selected" value="">Todos</option>
			      		<c:forEach var="municipio" items="${listaMunicipios}">
								<option value="${municipio.key}">${municipio.value}</option>
						</c:forEach>
			    	</select>
					
				</td>
				
				<td>Tipo de Ponto:</td>
	
				<td>
					<select class="campoDePesquisa" name="tipoPonto" id="tipoPontoPDV" style="width:150px;">
			      		<option selected="selected" value="">Todos</option>
			      		<c:forEach var="tpPonto" items="${listaTipoPonto}">
								<option value="${tpPonto.key}">${tpPonto.value}</option>
						</c:forEach>
			    	</select>
					
				</td>		  	
			  	
				<td align="right">
					<input class="campoDePesquisa" type="checkbox" id="checkCE" name="checkCE">
				</td>
	
			  	<td>Com CE</td>
			  	
			  	<td align="right">
					<input class="campoDePesquisa" type="checkbox" id="checkRecolhimentoFinal" name="checkRecolhimentoFinal">
				</td>
			    
			    <td>Recolhimento Final</td>
			  	
				 <td>&nbsp;</td>
				 
			    <td width="136" colspan="2">
			    	<span class="bt_novos">
			    		<a href="javascript:;" onclick="chamdaEncalheAnteipadaController.pesquisar();" id="btn_pesquisa_ce" class="botaoPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
			    	</span>
			    </td>
			  </tr>
			  </tbody>
	        </table>
	
		 </fieldset>
	     
	     <div class="linha_separa_fields">&nbsp;</div>
	      
	          <div class="grids" id="gridsCEAntecipada" style="display:none;">
				
				 <fieldset class="fieldGrid">
	       	  		<legend> CE Produto</legend>	  
					  
					  <div class="gridAntecipada" id="gridAntecipada" style="display:none;">
					  	<table class="ceAntecipadaGrid" id="ceAntecipadaGrid"></table>
					  </div>	  
					  
					  <div class="gridPesquisaCota" id="gridPesquisaCota" style="display:none;">
					  	<table class="ceAntecipadaCotaGrid" id="ceAntecipadaCotaGrid"></table>
					  </div>
		          
			          <table width="950" border="0" cellspacing="1" cellpadding="1">
						  
					  		<tr>
							   	<td width="468" valign="top">&nbsp;
								    
									
							    </td>
							      
							    <td width="295">
							    
							    	<fieldset class="box_field">
							          <legend>Totais</legend>
							          <table width="200" cellspacing="2" cellpadding="2" border="0">
							            <tbody><tr>
							              <td width="8">&nbsp;</td>
							              <td width="178">
							                <div class="box_resumo">
							                  <table width="150" cellspacing="1" cellpadding="1" border="0">
							                    <tbody><tr>
							                      <td width="83" height="23"><strong>Cotas:</strong></td>
							                      <td width="60"><input type="text" id="idTotalCotas" style="width:60px; text-align:center;" value="0" disabled="disabled"/></td>
							                      </tr>
							                    <tr>
							                      <td><strong>Exemplares:</strong></td>
							                      <td><input type="text" id="idTotalExemplares" style="width:60px; text-align:center;" value="0"  disabled="disabled"/></td>
							                      </tr>
							                    </tbody></table>
							                  </div>
							                </td>
							              </tr>
							            </tbody></table>
							          
							          </fieldset>
									
				          		</td>
				          		
				      			<td width="177" valign="top">
					        		<span class="bt_sellAll">
					        			<label for="sel">Selecionar Todos</label>
					        			<input isEdicao="true" type="checkbox" name="Todos" id="sel" onclick="chamdaEncalheAnteipadaController.checkAll(this);" style="float:left;"/>
					        		</span>
				    		     </td>
			      			</tr>
			 		</table>
				</fieldset>
				</div>
	</form>
</body>