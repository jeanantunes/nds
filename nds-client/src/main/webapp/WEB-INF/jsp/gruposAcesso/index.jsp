<head>
<script type="text/javascript">
	$(function() {
		gruposPermissaoController.init("${pageContext.request.contextPath}");
		usuariosPermissaoController.init("${pageContext.request.contextPath}");
		regrasPermissaoController.init("${pageContext.request.contextPath}");
	});
</script>
<style type="text/css">
	#dialog-novo fieldset{ width: 250px!important;}
</style>
</head>
<body>
	<form action="/administracao/gruposAcesso" id="excluir_regra_form">
	<input type="hidden" value="grupoPermissao.id" />
	<div id="dialog-excluir_grupo" title="Excluir Grupo" style="display:none;">
	  <p>Confirma a exclusão deste Grupo?</p>
	</div>
	</form>
	<div id="dialog-novo-grupo" title="Grupo" style="display:none;">
		<form action="/administracao/gruposAcesso" id="novo_grupo_form">
        <input type="hidden" name="grupoPermissaoDTO.id" id="grupoPermissaoId" />
     	<table width="379" border="0" cellpadding="2" cellspacing="1" class="filtro">
	        <tr>
	          <td width="70">Nome:</td>
	          <td width="298"><input type="text" name="grupoPermissaoDTO.nome" id="grupoPermissaonome" style="width:280px;"/></td>
	        </tr>
      	</table>
         <table width="582" border="0" align="center" cellpadding="2" cellspacing="2">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Regras Disponíveis:</legend>
					    <select name="grupoPermissaoDTO.permissoesGrupo" size="10" multiple="multiple" id="permissoesGrupo" style="height:170px; width:245px;">
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<a href="javascript:;" onclick="gruposPermissaoController.adicionaGruposSelecionados();"><img src="images/seta_vai_todos.png" width="39" height="30" /></a><br />
			      	<br /><a href="javascript:;" onclick="gruposPermissaoController.removeGruposSelecionados();"><img src="images/seta_volta_todos.png" width="39" height="30" /></a><br />
			    </td>
			    <td width="264" valign="top">
				    <fieldset>
				    	<legend>Regras Selecionadas</legend>
					    <select name="grupoPermissaoDTO.permissoes" size="10" multiple="multiple" id="permissoesGrupoSelecionadas" style="height:170px; width:245px;">
						</select>
					</fieldset>
				</td>
			 </tr>
		</table>      	
		</form>
	</div>
	<form action="/administracao/gruposAcesso" id="excluir_usuario_form">
	<div id="dialog-excluir-usuario" title="Excluir Usuário" style="display:none;">
	  <p>Confirma a exclusão deste usuário?</p>
	</div>
	</form>
	<div id="dialog-alterar-senha" title="Alterar Senha" style="display:none;">
		<form action="/administracao/gruposAcesso" id="alterar_senha_form">
			<input type="hidden" name="usuarioDTO.id" id="usuarioId" />
          	<table width="248" border="0" cellpadding="2" cellspacing="1" class="filtro">
            	<tr>
              		<td width="118">Senha:</td>
              		<td width="130"><input type="password" name="usuarioDTO.senha" id="usuarioSenha" style="width:120px;"/></td>
              	</tr>
              	<tr>
              		<td width="118">Confirmar Senha:</td>
              		<td width="130"><input type="password" name="usuarioDTO.confirmaSenha" id="usuarioConfirmaSenha" style="width:120px;"/></td>
           		</tr>
           		<tr>
	            	<td width="118">Lembrete Senha:</td>
	            	<td width="130"><input type="text" name="usuarioDTO.lembreteSenha" id="usuarioLembreteSenha" style="width:120px;"/></td>
	            </tr>
           	</table>
		</form>
	</div>
	<div id="dialog-novo-usuario" title="Usuário" style="display:none;">
		<form action="/administracao/gruposAcesso" id="novo_usuario_form">
		  <input type="hidden" name="usuarioDTO.id" id="usuarioId" />
          <table width="700" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="118">Primeiro Nome:</td>
              <td width="229"><input type="text" name="usuarioDTO.nome" id="usuarioNome" style="width:220px;"/></td>
              <td width="109">Último  Nome:</td>
              <td width="223"><input type="text" name="usuarioDTO.sobrenome" id="usuarioSobreNome" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>Username:</td>
              <td><input type="text" name="usuarioDTO.login" id="usuarioLogin" style="width:220px;"/></td>
              <td>E-mail:</td>
              <td><input type="text" name="usuarioDTO.email" id="usuarioEmail" style="width:220px;"/></td>
            </tr>
            <tr id="trInsercaoSenhas">
              <td>Nova Senha:</td>
              <td><input type="password" name="usuarioDTO.senha" id="usuariosenha" style="width:220px;"/></td>
              <td>Confirma nova Senha:</td>
              <td><input type="password" name="usuarioDTO.confirmaSenha" id="usuarioSenhaConfirma" style="width:220px;"/></td>
            </tr>
            <tr id="trLembreteSenha">
              <td>Lembrete Senha:</td>
              <td><input type="text" name="usuarioDTO.lembreteSenha" id="usuarioLembreteSenha" style="width:220px;"/></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Telefone:</td>
              <td>(
                <input type="text" name="usuarioDTO.ddd" id="usuarioDdd" style="width:50px;"/>
              )
              <input type="text" name="usuarioDTO.telefone" id="usuarioTelefone" style="width:105px;"/></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><input type="radio" name="usuarioDTOContaAtiva" id="usuarioAtivaTrue" checked value="ativa"/></td>
              <td>Ativa</td>
              <td align="right">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right"><input type="radio" name="usuarioDTOContaAtiva" id="usuarioAtivaFalse" value=""/></td>
              <td>Bloqueada</td>
              <td align="right">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Endereço:</td>
              <td><input type="text" name="usuarioDTO.endereco" id="usuarioEndereco" style="width:220px;"/></td>
              <td>Cidade:</td>
              <td><input type="text" name="usuarioDTO.cidade" id="usuarioCidade" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>País:</td>
              <td><input type="text" name="usuarioDTO.pais" id="usuarioPais" style="width:220px;"/></td>
              <td>CEP:</td>
              <td><input type="text" name="usuarioDTO.cep" id="usuarioCep" style="width:220px;"/></td>
            </tr>
          </table>
         <br clear="all" />
         <br />
         <table width="582" border="0" align="center" cellpadding="2" cellspacing="2">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Grupos Disponíveis:</legend>
					    <select name="gruposUsuario" size="10" multiple="multiple" id="gruposUsuario" style="height:170px; width:245px;">
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<a href="javascript:;" onclick="usuariosPermissaoController.adicionaGruposSelecionados();"><img src="images/seta_vai_todos.png" width="39" height="30" /></a><br />
			      	<br /><a href="javascript:;" onclick="usuariosPermissaoController.removeGruposSelecionados();"><img src="images/seta_volta_todos.png" width="39" height="30" /></a><br />
			    </td>
			    <td width="264" valign="top">
				    <fieldset>
				    	<legend>Grupos Selecionados</legend>
					    <select name="gruposSelecionadosUsuario" size="10" multiple="multiple" id="gruposSelecionadosUsuario" style="height:170px; width:245px;">
						</select>
					</fieldset>
				</td>
			 </tr>
		</table>
         <table width="582" border="0" align="center" cellpadding="2" cellspacing="2">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Regras Disponíveis:</legend>
					    <select name="permissoesUsuario" size="10" multiple="multiple" id="permissoesUsuario" style="height:170px; width:245px;">
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<a href="javascript:;" onclick="usuariosPermissaoController.adicionaPermissoesSelecionadas();"><img src="images/seta_vai_todos.png" width="39" height="30" /></a><br />
			      	<br /><a href="javascript:;" onclick="usuariosPermissaoController.removePermissoesSelecionadas();"><img src="images/seta_volta_todos.png" width="39" height="30" /></a><br />
			    </td>
			    <td width="264" valign="top">
				    <fieldset>
				    	<legend>Regras Selecionadas</legend>
					    <select name="permissoesSelecionadasUsuario" size="10" multiple="multiple" id="permissoesSelecionadasUsuario" style="height:170px; width:245px;">
						</select>
					</fieldset>
				</td>
			 </tr>
		</table>
	</form>
	</div>
	<div class="corpo">
		<div class="container">
			<jsp:include page="../messagesDialog.jsp" />	
        	<div id="tabs-grupos">
	            <ul>
	                <li><a href="#tabs-grupos">Grupos</a></li>
	                <li><a href="#tabs-usuarios">Usuários</a></li>
	                <li><a href="#tabs-regras">Regras</a></li>
	            </ul>
	            <form action="/administracao/gruposAcesso" id="pesquisar_grupos_form">
	            <div id="tabs-grupos">
			        <fieldset style="width:925px!important;">
			   	    	<legend> Pesquisar Grupo</legend>
			        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
				            <tr>
				              <td width="83">Nome:</td>
				              <td width="647"><input type="text" name="filtroConsultaGrupoDTO.nome" id="filtroConsultaGrupoDTO.nome" style="width:250px;"/></td>
				              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="gruposPermissaoController.mostrarGrupo();">Pesquisar</a></span></td>
				            </tr>
			          	</table>
			      	</fieldset>
			      <div class="linha_separa_fields">&nbsp;</div>
			      <fieldset style="width:925px!important;">
			       	<legend>Grupos Cadastrados</legend>
			      	<div class="gridsGrupos" style="display:none;">
			       		<table class="gruposGrid"></table>
			        </div>
			        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="gruposPermissaoController.popup_novo_grupo();"><img src="images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
			      </fieldset>
			      <br clear="all" />
	            </div>
	            </form>
	            <form action="/administracao/gruposAcesso" id="pesquisar_usuario_form">
	            <div id="tabs-usuarios">
			        <fieldset style="width:925px!important;">
			   	    	<legend> Pesquisar Usuário</legend>
			        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
				            <tr>
				              <td width="83">Nome / Login:</td>
				              <td width="647"><input type="text" name="usuario.nome" id="usuarioNome" style="width:250px;"/></td>
				              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="usuariosPermissaoController.mostrarUsuario();">Pesquisar</a></span></td>
				            </tr>
			          	</table>
			      	</fieldset>
			      <div class="linha_separa_fields">&nbsp;</div>
			      <fieldset style="width:925px!important;">
			       	<legend>Usuários  Cadastrados</legend>
			      	<div class="gridsUsuario" style="display:none;">
			       		<table class="usuariosGrid"></table>
			        </div>
			        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="usuariosPermissaoController.popup_novo_usuario();"><img src="images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
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
			              		<td width="184"><input type="text" name="filtroConsultaPermissaoDTO.nome" style="width:160px;"/></td>
			              		<td width="65">Descrição:</td>
			              		<td width="430"><input type="text" name="filtroConsultaPermissaoDTO.descricao" style="width:200px;"/></td>
			              		<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="regrasPermissaoController.mostrarRegra();">Pesquisar</a></span></td>
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
