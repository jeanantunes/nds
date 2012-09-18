<head>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptCadastroCalendario.js"></script>

	<script type="text/javascript">
		$(function() {
			CadastroCalendario.init();
		});
	</script>
<style type="text/css">


	#dialog-feriado-mes {
		display:none;
	}x''

	label {
		vertical-align: super;
	}
	
	.ui-datepicker-inline {
		width: 950px !important;
	}
	

	.ui-state-default {
		font-size: 13px !important;
	}
	
	.ui-datepicker-group {
	    height: 220px;
		margin-left: 4px;
		margin: 0 !important;
		padding: 5px !important;
	}
	.ui-datepicker-inline{height: 740px;}

    td.highlight { 
    		border: none !important;
    		padding: 1px 0 1px 1px !important;
    		background: none !important;
    		overflow:hidden;
    		line-height:22px!important;
    }
    
	td.highlight a {
			background: #fff !important;  
			border: 2px #0F0 solid !important;
			color:#060 !important;
	}

</style>

</head>

<body>

	<input type="hidden" id="alternate" />

	<form id="form-excluir">
	<div id="dialog-excluir" title="Excluir Feriado">
		<p>Confirma a exclusão deste Feriado?</p>
	</div>
	</form>

	<form id="form-novo">
	<div id="dialog-novo" title="Novo Feriado">
	
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialog-novo" name="messageDialog"/>
		</jsp:include>
	
		<fieldset style="width: 380px;">
			<legend>Dados do Feriado</legend>

			<table width="365" border="0" cellpadding="2" cellspacing="1">
			
				<tr>
					
					<td width="114">Data:</td>
					
					<td width="240">
						<input type="text" name="dtFeriadoNovo" id="dtFeriadoNovo" style="width: 122px; margin-right:5px;" />
					</td>
					
				</tr>
				
				<tr>
					<td>Tipo:</td>
					
					<td>
					
					<select name="tipos_feriado_dialog_novo" style="width:150px;" id="tipos_feriado_dialog_novo" onchange="CadastroCalendario.bloquearComboMunicipio();">
						
						<option value="">Selecione...</option>
						
						<c:forEach var="item" items="${tiposFeriado}">
		       				<option value="${item}">${item}</option>
		    			</c:forEach>
		    			
	    			</select>
					
					</td>
				</tr>
				
				<tr>
				
					<td>Cidade:</td>
					
					<td>

					<select name="cidades_dialog_novo" style="width:239px;" id="cidades_dialog_novo">
					
						<option value="">Selecione...</option>
						
						<c:forEach var="item" items="${listaLocalidade}">
		       				<option value="${item.id}">${item.nome}</option>
		    			</c:forEach>
		    			
	    			</select>
					
					</td>
				</tr>
				<tr>
					<td>Descrição:</td>
					<td>
						<input type="text" name="descricaoNovo" id="descricaoNovo" style="width: 230px;" />
					</td>
				</tr>
				
				<tr>
					<td>Opera?</td>
					<td>
						<input name="indOperaNovo" type="checkbox" value="" id="indOperaNovo" />
					</td>
				</tr>
				<tr>
					<td>Efetua Cobrança?</td>
					<td>
						<input name="indEfetuaCobrancaNovo" type="checkbox" value="" id="indEfetuaCobrancaNovo" />
					</td>
				</tr>
				<tr>
					<td>Repete Anualmente?</td>
					<td>
						<input name="indRepeteAnualmenteNovo" type="checkbox" value="" id="indRepeteAnualmenteNovo" />
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
	</form>

	<form id="form-feriado-mes">
	<div id="dialog-feriado-mes" title="Feriado do mês">
	
		<fieldset style="width: 650px; margin-top: 10px;">
		
			<legend>Feriados do mês</legend>
	
			<table class="mesFeriadoGrid"></table>
		
		</fieldset>
	
		<span class="bt_novos">
			<a href="javascript:;" onclick="CadastroCalendario.popupNovoCadastroFeriado();" rel="tipsy" Title="Incluir Novo Feriado">
			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" /></a>
		</span>
		
		<span class="bt_novos">
			<a href="${pageContext.request.contextPath}/administracao/cadastroCalendario/gerarRelatorioCalendario?fileType=PDF&tipoPesquisaFeriado=FERIADO_MENSAL" rel="tipsy" Title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
			</a>
		</span>
	
	</div>
	</form>

<form id="form-editar">
	<div id="dialog-editar" title="Editar Feriado">
		
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialog-editar" name="messageDialog"/>
		</jsp:include>

		<fieldset style="width: 650px;">
			<legend>Dados do Feriado</legend>

			<table width="568" border="0" cellpadding="2" cellspacing="2">
				<tr>
					
					<td width="45">Data:</td>
					
					<td width="210">
						<input type="text" disabled="disabled" name="dtFeriado" id="dtFeriado" style="width: 80px;" />
					</td>
					<td width="52">Tipo:</td>
					<td width="235"><select name="tipos_feriado_dialog_editar" style="width:239px;" id="tipos_feriado_dialog_editar" onchange="CadastroCalendario.bloquearComboMunicipio();">
					  <option value="">Selecione...</option>
					  <c:forEach var="item" items="${tiposFeriado}">
					    <option value="${item}">${item}</option>
				      </c:forEach>
				    </select></td>
					
				</tr>
				
				<tr>
					<td>Cidade:</td>
					<td>
					
					<select name="cidades_dialog_editar" style="width:200px;" id="cidades_dialog_editar">
					
						<option value="">Selecione...</option>
						
						<c:forEach var="item" items="${listaLocalidade}">
		       				<option value="${item.id}">${item.nome}</option>
		    			</c:forEach>
		    			
	    			</select>					
					
					</td>
					<td>Descrição:</td>
					<td><input type="text" name="descricao" id="descricao" style="width: 230px;" /></td>
				</tr>
				<tr>
					<td align="right"><input name="indOpera" type="checkbox" value="" id="indOpera" /></td>
					<td>Opera? </td>
					<td align="right"><input name="indEfetuaCobranca" type="checkbox" value="" id="indEfetuaCobranca" /></td>
					<td>Efetua Cobrança?</td>
				</tr>
				<tr>
					<td align="right"><input name="indRepeteAnualmente" type="checkbox" value="" id="indRepeteAnualmente" /></td>
					<td>Repete Anualmente? </td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset style="width: 650px; margin-top: 10px;">
			<legend>Dados dos Feriados</legend>

			<table class="diaFeriadoGrid"></table>
		
		</fieldset>
		
	</div>
	</form>



			<div class="areaBts">
				<div class="area">
					<span class="bt_novos" title="Novo">
						<a href="javascript:;" onclick="CadastroCalendario.popupNovoCadastroFeriado();" rel="tipsy" title="Incluir Novo Feriado">
							<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
						</a>
					</span>
					
					<span class="bt_arq" title="Imprimir">
						<a href="${pageContext.request.contextPath}/administracao/cadastroCalendario/gerarRelatorioCalendario?fileType=PDF&tipoPesquisaFeriado=FERIADO_ANUAL" rel="tipsy" title="Imprimir">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
						</a>
					</span>				
				</div>
			</div>
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset class="fieldFiltro"> 
				
				<legend>Pesquisar</legend>
				
				<table>
					<tr>
						<td><strong>Digite o Ano:</strong>&nbsp;</td>
						<td><input type="text" id="anoVigenciaPesquisa" value="${anoCorrente}" style="width:30px;"/></td>
						<td>
							<span class="bt_novos"><a href="javascript:;"
							onclick="CadastroCalendario.recarregarPainelCalendarioFeriado();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
							</span>
						</td>
					</tr>
				</table>
				
				
				 
				
				 
			</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>


			<fieldset class="fieldGrid">
			
				<legend>Calendário de Feriados</legend>
				
				<div id="feriadosWrapper">
					
					<div id="feriados"></div>
				</div>

				<div class="linha_separa_fields">&nbsp;</div>


			</fieldset>

</body>
