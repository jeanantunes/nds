<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/impressaoBoletos.js"></script>
	<script language="javascript" type="text/javascript">

	$(function() {
		var pesquisaCotaImpressaoBoletos = new PesquisaCota();
		impressaoBoletosController.init();
	});

	</script>
</head>

<body>
	<form id="formAguarde">
		<div style="display: none;" id="aguarde">Aguarde...</div>
	</form>
	<form id="formPesquisaInvalida">
		<div style="display: none;heigth:200px;  min-height:0px;" id="pesquisaInvalida">
			Não foi feita a Geração de Dívidas para esta data.
		</div>	
	</form>
		<form id="pesquisaDividasForm"
				name="pesquisaDividasForm" 
				method="post">
			<fieldset class="classFieldset">
		   	    <legend> Gerar Dívida</legend>
		   	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	  					<tr>
						    <td width="29">Data:</td>
						    <td width="125"><input type="text" name="dataMovimento" id="dataMovimento" onchange="impressaoBoletosController.habilitarAcaoGeracaoDivida(this.value);" style="width:70px; float:left; margin-right:5px;" /></td>
						    <td width="49">Box</td>
						    <td width="169">
						    	<select name="box" id="box" style="width:70px; float:left; margin-right:5px;" onchange="impressaoBoletosController.recarregarComboRoteiroRotas(this.value)">
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="box" items="${listaBoxes}">
											<option value="${box.key}">${box.value}</option>
								  </c:forEach>
							    </select>
						    </td>
						    <td width="43">Roteiro</td>
						    <td width="198">
						    	<select name="roteiro" id="roteiro" style="width:160px; float:left; margin-right:5px;" onchange="impressaoBoletosController.recarregarComboRotas(this.value)" >
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="roteiro" items="${listaRoteiros}">
											<option value="${roteiro.key}">${roteiro.value}</option>
								  </c:forEach>
							    </select>
						    </td>
						    <td width="54">Rota</td>
						    <td width="242">
						    	<select name="rota" id="rota" style="width:160px; float:left; margin-right:5px;"  >
							      <option selected="selected" value="">Todos</option>
							      <c:forEach var="rota" items="${listaRotas}">
											<option value="${rota.key}">${rota.value}</option>
								  </c:forEach>
							    </select>
						    </td>
					    </tr>
	 		 			<tr>
	    					<td>Cota:</td>
	    					<td>
	    						<input name="numCota" 
				              		   id="numCota" 
				              		   type="text"
				              		   maxlength="11"
				              		   style="width:70px; 
				              		   float:left; margin-right:5px;"
				              		   onchange="pesquisaCotaImpressaoBoletos.pesquisarPorNumeroCota('#numCota', '#descricaoCota',false,
				              	  											impressaoBoletosController.pesquisarCotaSuccessCallBack, 
				              	  											impressaoBoletosController.pesquisarCotaErrorCallBack);" />
	      					</td>
	    					<td>Nome:</td>
	    					<td>
	    						 <input  name="descricaoCota" 
							      		 id="descricaoCota" 
							      		 type="text" 
							      		 class="nome_jornaleiro" 
							      		 maxlength="255"
							      		 style="width:130px;"
							      		 onkeyup="pesquisaCotaImpressaoBoletos.autoCompletarPorNome('#descricaoCota');" 
							      		 onblur="pesquisaCotaImpressaoBoletos.pesquisarPorNomeCota('#numCota', '#descricaoCota',false,
							      		 									impressaoBoletosController.pesquisarCotaSuccessCallBack,
							      		 									impressaoBoletosController.pesquisarCotaErrorCallBack);" />
	    					</td>
						    <td>Tipo</td>
						    <td>
						    	<select name="tipoCobranca" id="tipoCobranca" style="width: 200px;">
									<option selected="selected" value="">Todos</option>
									<c:forEach var="tipo" items="${listaTipoCobranca}">
										<option value="${tipo.key}">${tipo.value}</option>
									</c:forEach>
								</select>
								
						    </td>
						    <td>&nbsp;</td>
						    <td>
						    	<span class="bt_pesquisar">
						    		<a href="javascript:impressaoBoletosController.validarPesquisa();">Pesquisar</a>
						    	</span>
	    						<div id="divGerarDivida" style="display: none">
		    						<span class="bt_novos" title="Gerar Dívida" style="margin-left:20px;">
		    							<a href="javascript:impressaoBoletosController.gerarDivida();" id="btnGerarDivida">
		    								<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
		    								Gerar Dívida
		    							</a>
		    						</span>
	    					   </div>
	   						</td>
	    				</tr>
		  		</table>
		    </fieldset>
		 </form>
		 
		<div class="linha_separa_fields">&nbsp;</div>
			
		<fieldset class="classFieldset">
			<legend>Dividas Geradas</legend>
				<div class="grids" id="grids" style="display:none;">
					<table class="impressosGrid" id="impressosGrid"></table>
          			<span class="bt_novos" title="Gerar Arquivo">
          				<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=XLS">
          					<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
          					Arquivo
          				</a>
          			</span>
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir
						</a>
					</span>
					
					<span class="bt_novos" title="Imprimir Boletos">
						<a href="javascript:impressaoBoletosController.imprimirDividas('BOLETO')">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir Boletos
						</a>
					</span>
					
					<span class="bt_novos" title="Imprimir Dividas">
						<a href="javascript:impressaoBoletosController.imprimirDividas('DIVIDA')">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir Dívidas
						</a>
					</span>
					
				</div>
      	</fieldset>
			
		<div class="linha_separa_fields">&nbsp;</div>
</body>
