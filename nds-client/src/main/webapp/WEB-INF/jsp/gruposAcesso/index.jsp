<head>
<script type="text/javascript" src="scripts/gruposAcesso.js"></script>
<script type="text/javascript">
	$(function() {
		gruposAcessoController.init("${pageContext.request.contextPath}");
	});
</script>
<style type="text/css">
	#dialog-novo fieldset{ width: 250px!important;}
</style>
</head>
<body>
	<form action="/administracao/gruposAcesso" id="excluir_regra_form">
	<div id="dialog-excluir_regra" title="Excluir Regra" style="display:none;">
	  <p>Confirma a exclusão desta Regra?</p>
	</div>
	</form>
	<form action="/administracao/gruposAcesso" id="nova_regra_form">
	<div id="dialog-novo_regra" title="Regra" style="display:none;">
     	<table width="379" border="0" cellpadding="2" cellspacing="1" class="filtro">
	        <tr>
	          <td width="70">Nome:</td>
	          <td width="298"><input type="text" name="textfield" id="textfield" style="width:280px;"/></td>
	        </tr>
	        <tr>
	          <td>Descrição:</td>
	          <td><textarea name="textfield7" rows="3" id="textfield7" style="width:280px;"></textarea></td>
	        </tr>
      	</table>
	</div>
	<form action="/administracao/gruposAcesso" id="excluir_usuario_form">
	<div id="dialog-excluir-usuario" title="Excluir Usuário" style="display:none;">
	  <p>Confirma a exclusão deste usuário?</p>
	</div>
	</form>
	<form action="/administracao/gruposAcesso" id="novo_usuario_form">
	<div id="dialog-novo-usuario" title="Usuário" style="display:none;">
          <table width="700" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="118">Primeiro Nome:</td>
              <td width="229"><input type="text" name="textfield" id="textfield" style="width:220px;"/></td>
              <td width="109">Último  Nome:</td>
              <td width="223"><input type="text" name="textfield2" id="textfield2" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>Username::</td>
              <td><input type="text" name="textfield14" id="textfield14" style="width:220px;"/></td>
              <td>E-mail:</td>
              <td><input type="text" name="textfield3" id="textfield3" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>Senha:</td>
              <td><input type="text" name="textfield6" id="textfield6" style="width:220px;"/></td>
              <td>Confirma Senha:</td>
              <td><input type="text" name="textfield9" id="textfield9" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>Lembrete Senha:</td>
              <td><input type="text" name="textfield8" id="textfield8" style="width:220px;"/></td>
              <td>Telefone:</td>
              <td>(
                <input type="text" name="textfield4" id="textfield4" style="width:50px;"/>
              ) 
              <input type="text" name="textfield5" id="textfield5" style="width:105px;"/></td>
            </tr>
            <tr>
              <td align="right"><input type="checkbox" name="checkbox" id="checkbox" /></td>
              <td>Ativa</td>
              <td align="right"><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
              <td>Conta Expira</td>
            </tr>
            <tr>
              <td align="right"><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
              <td>Bloqueada</td>
              <td align="right"><input type="checkbox" name="checkbox4" id="checkbox4" /></td>
              <td>Senha Expira</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Endereço:</td>
              <td><input type="text" name="textfield10" id="textfield10" style="width:220px;"/></td>
              <td>Cidade:</td>
              <td><input type="text" name="textfield11" id="textfield11" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>País:</td>
              <td><input type="text" name="textfield12" id="textfield12" style="width:220px;"/></td>
              <td>CEP:</td>
              <td><input type="text" name="textfield13" id="textfield13" style="width:220px;"/></td>
            </tr>
          </table>
         <br clear="all" />
         <br />
         <table width="582" border="0" align="center" cellpadding="2" cellspacing="2">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Regras Disponíveis:</legend>
					    <select name="select4" size="10" multiple="multiple" id="select3" style="height:170px; width:245px;">
					      <option>ACERTO_CONSULTOR_MANAGER</option>
					      <option>ROLE_ADMIN</option>
					      <option>ROLE_AGENDA_FINANCEIRA</option>
					      <option>ROLE_ALTERAR_SENHA</option>
					      <option>ROLE_ALTERAR_TEMA</option>
					      <option>ROLE_ATENDIMENTO</option>
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<img src="images/seta_vai_todos.png" width="39" height="30" /><br />
			      	<br /><img src="images/seta_volta_todos.png" width="39" height="30" /><br />
			    </td>
			    <td width="264" valign="top">
				    <fieldset>
				    	<legend>Regras Selecionadas</legend>
					    <select name="select5" size="10" multiple="multiple" id="select4" style="height:170px; width:245px;">
						</select>
					</fieldset>
				</td>
			 </tr>
		</table>
	</div>
	</form>
	<div class="corpo">
		<div class="container">
	     	<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Usuário < evento > com < status >.</b></p>
			</div>
        	<div id="tabs-grupos">
	            <ul>
	                <li><a href="#tabs-grupos">Grupos</a></li>
	                <li><a href="#tabs-usuarios">Usuários</a></li>
	                <li><a href="#tabs-regras">Regras</a></li>
	            </ul>
	            <form action="/administracao/gruposAcesso" id="pesquisar_grupos_form">
	            <div id="tabs-grupos">
	            	1
	            </div>
	            </form>
	            <form action="/administracao/gruposAcesso" id="pesquisar_usuario_form">
	            <div id="tabs-usuarios">
			        <fieldset style="width:925px!important;">
			   	    	<legend> Pesquisar Usuário</legend>
			        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
				            <tr>
				              <td width="83">Nome / Login:</td>
				              <td width="647"><input type="text" name="textfield3" id="textfield3" style="width:250px;"/></td>
				              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="gruposAcessoController.mostrarUsuario();">Pesquisar</a></span></td>
				            </tr>
			          	</table>
			      	</fieldset>
			      <div class="linha_separa_fields">&nbsp;</div>
			      <fieldset style="width:925px!important;">
			       	<legend>Usuários  Cadastrados</legend>
			      	<div class="gridsUsuario" style="display:none;">
			       		<table class="usuariosGrid"></table>
			        </div>
			        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="gruposAcessoController.popup_usuario();"><img src="images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
			      </fieldset>
			      <br clear="all" />
	            </div>
	            </form>
	            <form action="/administracao/gruposAcesso" id="pesquisar_regras_form">
	            <div id="tabs-regras">
	            	<fieldset style="width:925px!important;">
			   	    <legend> Pesquisar Regras</legend>
			        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
			            	<tr>
			              		<td width="41">Nome:</td>
			              		<td width="184"><input type="text" name="filtro.nome" style="width:160px;"/></td>
			              		<td width="65">Descrição:</td>
			              		<td width="430"><input type="text" name="filtro.descricao" style="width:200px;"/></td>
			              		<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="gruposAcessoController.mostrarRegra();">Pesquisar</a></span></td>
			            	</tr>
			          	</table>
			      	</fieldset>
			      <div class="linha_separa_fields">&nbsp;</div>
			      <fieldset style="width:925px!important;">
			       	  <legend>Regras Cadastradas</legend>
			          <div class="gridsRegra" style="display:none;">
			       	  	<table class="regrasGrid"></table>
			          </div>
			          <%--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="gruposAcessoController.popup_nova_regra();"><img src="images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span--%>
			      </fieldset>
			      <br clear="all" />
	            </div>
	            </form>
	    	</div>
		</div>
	</div> 
</body>
</html>
