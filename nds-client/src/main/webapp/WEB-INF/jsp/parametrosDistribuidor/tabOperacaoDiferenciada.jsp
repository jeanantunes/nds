<div id="tabDiferenciada">
   <br />
   <fieldset style="width:940px; margin-left:10px;">
   	<legend>Grupos</legend>
   		

       <table class="gruposGrid"></table>
       
       <span class="bt_novos"><a isEdicao="true" href="javascript:;" onclick="OperacaoDiferenciadaController.novoGrupo();" rel="tipsy" title="Incluir Novo Grupo"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" /></a></span>
       
   </fieldset>
   <br clear="all" />
   
   
   
	<div id="dialog-confirm-grupo" title="Excluir Grupo" style="display:none;">
		<fieldset style="width:350px!important;">
	  		<legend>Confirmação</legend>
	  		 <p>Aten&ccedil;&atilde;o: A exclus&atilde;o deste grupo somente ser&aacute; efetiva em <span id="dataEfetivacao"/>.</p>
	        <p>Confirma a exclusão deste Grupo?</p>
	    </fieldset>
	</div>


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
				<p>Aten&ccedil;&atilde;o: a inclus&atilde;o/edi&ccedil;&atilde;o de grupo somente ser&aacute; efetiva apenas para a pr&oacute;xima senama sem Chamada de Encalhe.</p>
			</fieldset>
		</div>

<jsp:include page="detalheOperacaoDiferenciada.jsp"></jsp:include>
   
            
</div>
          
