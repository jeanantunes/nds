<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/operacaoDiferenciada.js"></script>

<div id="tabDiferenciada">
            

   <br clear="all" />
   <fieldset style="width:800px;">
   	<legend>Grupos</legend>

       <table class="gruposGrid"></table>
       
       <span class="bt_novos" title="Incluir Novo"><a href="javascript:;" onclick="OperacaoDiferenciadaController.novoGrupo();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir Novo</a></span>
       
   </fieldset>
   <br clear="all" />
            
</div>
          

<form id="idFormExcluiGrupo">
	<div id="dialog-confirm-grupo" title="Excluir Grupo" style="display:none;">
		<fieldset style="width:350px!important;">
	  		<legend>Confirmação</legend>
	        <p>Confirma a exclusão deste Grupo?</p>
	        
	    </fieldset>
	</div>
</form>

<form id="idFormConfirmarGrupo">
	<div id="dialog-salvar" title="Salvar" style="display: none;">
			<fieldset style="width: 300px;">
				<legend>Digite o Nome</legend>
				<table width="287" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="65">Nome:</td>
						<td width="215">
							<input id="nomeDiferenca" type="text" style="width: 200px;" />
						</td>
					</tr>
					<tr>
						<td valign="top">Recolhimento:</td>
						<td>
							<select name="select3" size="5" multiple="multiple" 
								id="diaSemana" style="width: 130px; height: 100px">
								
								<option value="DOMINGO">Domingo</option>
								<option value="SEGUNDA_FEIRA">Segunda-feira</option>
								<option value="TERCA_FEIRA">Terça-feira</option>
								<option value="QUARTA_FEIRA">Quarta-feira</option>
								<option value="QUINTA_FEIRA">Quinta-feira</option>
								<option value="SEXTA_FEIRA">Sexta-feira</option>
								<option value="SABADO">Sábado</option>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
</form>

<jsp:include page="detalheOperacaoDiferenciada.jsp"></jsp:include>
<jsp:include page="../messagesDialog.jsp"></jsp:include>
