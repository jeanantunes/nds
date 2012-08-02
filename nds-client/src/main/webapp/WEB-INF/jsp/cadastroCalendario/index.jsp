<head>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptCadastroCalendario.js"></script>

<style type="text/css">


	#dialog-feriado-mes {
		display:none;
	}

	label {
		vertical-align: super;
	}
	
	.ui-datepicker-inline {
		width: 950px !important;
	}
	
	.ui-datepicker-group {
		margin-left: 4px;
	}
	
	.ui-state-default {
		font-size: 13px !important;
	}
	
	.ui-datepicker-group {
		margin: 0px !important;
		padding: 5px !important;
	}

    td.highlight { 
    		border: none !important;
    		padding: 1px 0 1px 1px !important;
    		background: none !important;
    		overflow:hidden;
    }
    
	td.highlight a {
			background: #99dd73 url(${pageContext.request.contextPath}/images/highlightDate.png) 50% 50% repeat-x !important;  
			border: 1px #88a276 solid !important;
	}



</style>

</head>

<body>

	<input type="hidden" id="alternate" />

	<div id="dialog-excluir" title="Excluir Feriado">
		<p>Confirma a exclusão deste Feriado?</p>
	</div>


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
						<input type="text" name="dtFeriadoNovo" id="dtFeriadoNovo" style="width: 110px;" />
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

					<select name="cidades_dialog_novo" style="width:150px;" id="cidades_dialog_novo">
					
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

	<div id="dialog-feriado-mes" title="Feriado do mês">
	
		<fieldset style="width: 650px; margin-top: 10px;">
		
			<legend>Feriados do mês</legend>
	
			<table class="mesFeriadoGrid"></table>
		
		</fieldset>
	
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="CadastroCalendario.popupNovoCadastroFeriado();">
			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo</a>
		</span>
		
		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/administracao/cadastroCalendario/gerarRelatorioCalendario?fileType=PDF&tipoPesquisaFeriado=FERIADO_MENSAL">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir
			</a>
		</span>
	
	</div>

	<div id="dialog-editar" title="Editar Feriado">

		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialog-editar" name="messageDialog"/>
		</jsp:include>

		<fieldset style="width: 650px;">
			<legend>Dados do Feriado</legend>

			<table width="365" border="0" cellpadding="2" cellspacing="1">
				<tr>
					
					<td width="114">Data:</td>
					
					<td width="240">
						<input type="text" disabled="disabled" name="dtFeriado" id="dtFeriado" style="width: 110px;" />
					</td>
					
				</tr>
				
				<tr>
					<td>Tipo:</td>
					<td>
					
					<select name="tipos_feriado_dialog_editar" style="width:150px;" id="tipos_feriado_dialog_editar" onchange="CadastroCalendario.bloquearComboMunicipio();">
						
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
					
					<select name="cidades_dialog_editar" style="width:150px;" id="cidades_dialog_editar">
					
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
						<input type="text" name="descricao" id="descricao" style="width: 230px;" />
					</td>
				</tr>
				
				<tr>
					<td>Opera?</td>
					<td>
						<input name="indOpera" type="checkbox" value="" id="indOpera" />
					</td>
				</tr>
				<tr>
					<td>Efetua Cobrança?</td>
					<td>
						<input name="indEfetuaCobranca" type="checkbox" value="" id="indEfetuaCobranca" />
					</td>
				</tr>
				<tr>
					<td>Repete Anualmente?</td>
					<td>
						<input name="indRepeteAnualmente" type="checkbox" value="" id="indRepeteAnualmente" />
					</td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset style="width: 650px; margin-top: 10px;">
			<legend>Dados dos Feriados</legend>

			<table class="diaFeriadoGrid"></table>
		
		</fieldset>

	</div>

	<div class="corpo">
	
		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em; z-index: 1;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Feriado < evento > com
						< status >.</b>
				</p>
			</div>

			<fieldset class="classFieldset"> 
				
				<legend>Pesquisar</legend>
				
				<table>
					<tr>
						<td><input type="text" id="anoVigenciaPesquisa"/></td>
						<td>
							<span class="bt_pesquisar"><a href="javascript:;"
							onclick="CadastroCalendario.recarregarPainelCalendarioFeriado();">Pesquisar</a>
							</span>
						</td>
						<td></td>
						<td></td>
					</tr>
				</table>
				
				
				 
				
				 
			</fieldset>
		


			<fieldset class="classFieldset">
			
				<legend>Calendário de Feriados</legend>
				
				
				<br clear="all" /> <br />
				
				<div id="feriadosWrapper">
					
					<div id="feriados"></div>
				
				</div>

				<div class="linha_separa_fields">&nbsp;</div>

				<div class="linha_separa_fields">&nbsp;</div>

					<span class="bt_novos" title="Novo">
						<a href="javascript:;" onclick="CadastroCalendario.popupNovoCadastroFeriado();">
						<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo</a>
					</span>
					
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/administracao/cadastroCalendario/gerarRelatorioCalendario?fileType=PDF&tipoPesquisaFeriado=FERIADO_ANUAL">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
							Imprimir
						</a>
					</span>
				
				<div class="linha_separa_fields">&nbsp;</div>


			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="linha_separa_fields">&nbsp;</div>

		</div>


	</div>

</body>
