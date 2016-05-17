<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/impressaoBoletos.js"></script>
	<script language="javascript" type="text/javascript">

	var pesquisaCotaImpressaoBoletos = new PesquisaCota();
	
	$(function() {
		impressaoBoletosController.init();
		bloquearItensEdicao(impressaoBoletosController.workspace);
	});

	</script>
</head>

<body>

	
<div id="impressao-dialog-banco" title="Lista de Bancos">
	<form>
		<fieldset>
			<div id="impressao-dialog-lista">
				<legend>Bancos Cadastrados</legend>
				<table width="300" border="0" cellpadding="2" cellspacing="1" class="filtro">
					<tr>
						<td>
							Banco:
						</td>
						<td>
							<select name="impressao-boleto-banco" id="impressao-boleto-banco" style="width:70px; float:left; margin-right:5px;" onchange="impressaoBoletosController.recarregarComboRoteiroRotas(this.value)">
						    	<option selected="selected" value="-1">Todos</option>
						      		<c:forEach var="banco" items="${listaBancos}">
										<option value="${banco.key}">${banco.value}</option>
							  		</c:forEach>
						    </select>						
						</td>
					</tr>
				</table>
							
			</div>
		</fieldset>
	</form>
</div>
	
	<form id="formAguarde">
		<div style="display: none;" id="aguarde">Aguarde...</div>
	</form>
	
	<form id="form-confirmar-regerar-cobranca">
		<div id="dialog-confirmar-regerar-cobranca" title="Regerar Cobrança" style="display: none;">
			<fieldset>
				<legend>Regerar cobrança?</legend>
				<p id="msgRegerarCobranca"></p>
			</fieldset>
		</div>
	</form>
	<form id="pesquisaDividasForm"
			name="pesquisaDividasForm" 
			method="post">
		<div class="areaBts">
			<div class="area">
				<div id="divImpressoes" style="display: none">
					<span class="bt_novos">
						<a isEdicao="true" href="javascript:impressaoBoletosController.imprimirDividas('BOLETO')" rel="tipsy" title="Imprimir Boletos">
							<img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0" />
						</a>
					</span>
					
					<span class="bt_novos">
						<a isEdicao="true" href="javascript:impressaoBoletosController.imprimirDividas('DIVIDA')" rel="tipsy" title="Imprimir Dividas">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						</a>
					</span>
					
					<span class="bt_novos">
						<a isEdicao="true" href="javascript:impressaoBoletosController.imprimirDividas('BOLETO_SLIP')" rel="tipsy" title="Imprimir Boletos e Slips">
							<img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" hspace="5" border="0" />
						</a>
					</span>
				</div>
 					
				<span class="bt_arq">
       				<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
       					<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
       				</a>
       			</span>
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/financeiro/impressaoBoletos/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
 				<span class="bt_arq">
       				<a href="javascript:impressaoBoletosController.gerarArquivo()" rel="tipsy" title="Gerar Cobran�a Registrada">
       					<img src="${pageContext.request.contextPath}/images/ico_soma_estudos.gif"  hspace="5" border="0" />
       				</a>
       			</span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	   	    <legend> Gerar Dívida</legend>
	   	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  					<tr>
					    <td width="29">Data:</td>
					    <td width="125">
					    	<input value="${dataOperacao}" type="text" name="dataMovimento" id="dataMovimento"
					    		   style="width:70px; float:left; margin-right:5px;" />
					    </td>
					    <td width="49">Box</td>
					    <td width="169">
					    	<select name="impressao-boleto-box" id="impressao-boleto-box" style="width:70px; float:left; margin-right:5px;" onchange="impressaoBoletosController.recarregarComboRoteiroRotas(this.value)">
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
    						<input name="impressao-boleto-numCota" 
			              		   id="impressao-boleto-numCota" 
			              		   type="text"
			              		   maxlength="11"
			              		   style="width:70px; 
			              		   float:left; margin-right:5px;"
			              		   onchange="pesquisaCotaImpressaoBoletos.pesquisarPorNumeroCota('#impressao-boleto-numCota', '#descricaoCota',false,
			              	  											null, 
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
						      		 									null,
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
					    	<span class="bt_novos">
					    		<a href="javascript:impressaoBoletosController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					    	</span>
    						
   						</td>
    				</tr>
	  		</table>
	    </fieldset>
	 </form>
	 
	<div class="linha_separa_fields">&nbsp;</div>
		
	<fieldset class="fieldGrid">
		<legend>Dividas Geradas</legend>
			<div class="grids" id="grids" style="display:none;">
				<table class="impressosGrid" id="impressosGrid"></table>
         			
				
			</div>
     	</fieldset>
		
	<div class="linha_separa_fields">&nbsp;</div>
	
	<iframe src="" id="download-iframe" style="display:none;"></iframe>
</body>
