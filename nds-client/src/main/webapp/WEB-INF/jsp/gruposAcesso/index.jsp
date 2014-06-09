<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}" />
<head>
<script type="text/javascript" src="scripts/gruposPermissao.js"></script>
<script type="text/javascript" src="scripts/usuariosPermissao.js"></script>
<script type="text/javascript" src="scripts/regrasPermissao.js"></script>
<script type="text/javascript">
	$(function() {
		gruposPermissaoController.init("${pageContext.request.contextPath}");
		usuariosPermissaoController.init("${pageContext.request.contextPath}");
		regrasPermissaoController.init("${pageContext.request.contextPath}");
		
		bloquearItensEdicao(gruposPermissaoController.workspace.workspace);
		bloquearItensEdicao(usuariosPermissaoController.workspace.workspace);
		bloquearItensEdicao(regrasPermissaoController.workspace.workspace);
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
	<form action="/administracao/gruposAcesso" id="novo_grupo_form">
	<div id="dialog-novo-grupo" title="Grupo" style="display:none;">
        <input type="hidden" name="grupoPermissaoDTO.id" id="grupoPermissaoId" />
     	<fieldset style="width:638px;margin-bottom: 20px;">
	   		<legend>Nome do Grupo</legend>
			<table width="641" border="0" cellpadding="2" cellspacing="2" class="filtro">
		        <tr>
		          <td width="41">Nome:</td>
		          <td width="586"><input type="text" name="grupoPermissaoDTO.nome" id="grupoPermissaonome" style="width:280px;"/></td>
		        </tr>
	      	</table>
        </fieldset>
         
         <!-- RULES DO USUÁRIO -->
         <div id="localPermissaoGridGrupo">
         	<div id="permissaoGridConteudo">
         	 <table id="permissaoGrid" class="permissaoGrid"> </table>
         	</div>
         </div>
        
		
	</div>
	</form>
	<form action="/administracao/gruposAcesso" id="excluir_usuario_form">
	<div id="dialog-excluir-usuario" title="Excluir Usuário" style="display:none;">
	  <p>Confirma a exclusão deste usuário?</p>
	</div>
	</form>
	<form action="/administracao/gruposAcesso" id="alterar_senha_form">
	<div id="dialog-alterar-senha" title="Alterar Senha" style="display:none;">
	<fieldset>
    	<legend>Alterar Senha</legend>
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
         </fieldset>
	</div>
	</form>
	<form action="/administracao/gruposAcesso" id="novo_usuario_form">
	<input name="failAutoFill" style="display: none;" type="password" />
	<div id="dialog-novo-usuario" title="Usuário" style="display:none;">
		<fieldset style="width:710px;">
    	<legend>Dados do Usuário</legend>
		  <input type="hidden" name="usuarioDTO.id" id="usuarioId" />
          <table width="700" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="95">Primeiro Nome:</td>
              <td width="241"><input type="text" name="usuarioDTO.nome" id="nome" style="width:220px;"/></td>
              <td width="120">Último  Nome:</td>
              <td width="223"><input type="text" name="usuarioDTO.sobrenome" id="sobrenome" style="width:220px;"/></td>
            </tr>
            <tr>
              <td>Username:</td>
              <td><input type="text" name="usuarioDTO.login" id="login" autocomplete="off" style="width:220px;"/></td>
              <td>E-mail:</td>
              <td><input type="text" name="usuarioDTO.email" id="email" autocomplete="off" style="width:220px;"/></td>
            </tr>
            <tr id="trInsercaoSenhas">
              <td>Nova Senha:</td>
              <td><input type="password" name="usuarioDTO.senha" id="senha" autocomplete="off" style="width:220px;"/></td>
              <td>Confirma nova Senha:</td>
              <td><input type="password" name="usuarioDTO.confirmaSenha" id="usuarioSenhaConfirma" autocomplete="off" style="width:220px;"/></td>
            </tr>
            <tr id="trLembreteSenha">
              <td>Lembrete Senha:</td>
              <td><input type="text" name="usuarioDTO.lembreteSenha" id="lembreteSenha" autocomplete="off" style="width:220px;"/></td>
              <td>Status:</td>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="9%"><input type="radio" name="usuarioDTO.contaAtiva" id="usuarioAtivaTrue" checked="checked" value="ativa" /></td>
                  <td width="28%">Ativa</td>
                  <td width="6%"><input type="radio" name="usuarioDTO.contaAtiva" id="usuarioAtivaFalse" value="" /></td>
                  <td width="57%">Bloqueada</td>
                </tr>
              </table></td>
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
              <td align="right">&nbsp;</td>
              <td>&nbsp;</td>
              <td align="right">&nbsp;</td>
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
            <tr>
            	<td>Supervisor</td>
            	<td><input type="checkbox" name="usuarioDTO.supervisor" id="supervisor"/></td>
            </tr>
          </table>
          </fieldset>
         <br clear="all" />
         
         <fieldset style="width:710px; margin-top:5px;">
    	<legend>Grupos</legend>
         <table width="582" border="0" align="center" cellpadding="2" cellspacing="2" style="margin-left:20px;">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Grupos Disponíveis:</legend>
					    <select name="gruposUsuario" size="10" multiple="multiple" id="gruposUsuario" style="height:170px; width:245px;">
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<a href="javascript:;" onclick="usuariosPermissaoController.adicionaGruposSelecionados();"><img src="images/seta_vai_todos.png" width="39" height="30" border="0" /></a><br />
			      	<br /><a href="javascript:;" onclick="usuariosPermissaoController.removeGruposSelecionados();"><img src="images/seta_volta_todos.png" width="39" height="30" border="0" /></a><br />
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
		</fieldset>
		<fieldset style="width:710px; margin-top:5px;">
    	<legend>Regras</legend>
         <div id="localPermissaoGridUsuario" style="margin-left: 30px;"></div>
         <table  width="582" border="0" align="center" cellpadding="2" cellspacing="2" style="margin-left:20px;display:none">
			 <tr class="especialidades">
			    <td width="264" valign="top">
			    	<fieldset>
			    		<legend>Regras Disponíveis:</legend>
					    <select name="permissoesUsuario" size="10" multiple="multiple" id="permissoesUsuario" style="height:170px; width:245px;">
					    </select>
			    	</fieldset>
			    </td>
			    <td width="34" align="center">
			    	<a href="javascript:;" onclick="usuariosPermissaoController.adicionaPermissoesSelecionadas();"><img src="images/seta_vai_todos.png" width="39" height="30" border="0" /></a><br />
			      	<br /><a href="javascript:;" onclick="usuariosPermissaoController.removePermissoesSelecionadas();"><img src="images/seta_volta_todos.png" width="39" height="30" border="0" /></a><br />
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
		</fieldset>
	</div>
	</form>
	
	<style>
		.fixo{
			height: 440px !important;
			overflow-y: visible !important;
		}
	</style>

	<jsp:include page="../messagesDialog.jsp" />	
      	<div id="tabs-acesso" style="margin-top: 10px; width: 920px;">
           <ul>
               <li><a href="#tabs-grupos">Grupos</a></li>
               <li><a href="#tabs-usuarios">Usuários</a></li>
               <li><a href="#tabs-regras">Regras</a></li>
           </ul>
           <form action="/administracao/gruposAcesso" id="pesquisar_grupos_form">
           <div id="tabs-grupos" class="fixo" style="height: 300px" >
           <div class="areaBts">
           		<div class="area">
           			<span class="bt_novos"><a isEdicao="true" href="javascript:;" onclick="gruposPermissaoController.popup_novo_grupo();" rel="tipsy" title="Novo"><img src="images/ico_salvar.gif" hspace="5" border="0"/></a></span>
           		</div>
           </div>
           <div  class="linha_separa_fields">&nbsp;</div>
	        <fieldset class="fieldFiltro" style="width:905px!important;">
	   	    	<legend> Pesquisar Grupo</legend>
	        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
		            <tr>
		              <td width="36">Nome:</td>
		              <td width="257"><input type="text" name="filtroConsultaGrupoDTO.nome" id="filtroConsultaGrupoDTO.nome" style="width:250px;"/></td>
		              <td width="541"><span class="bt_novos"><a href="javascript:;" onclick="gruposPermissaoController.mostrarGrupo();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
		            </tr>
	          	</table>
	      	</fieldset>
	      <div class="linha_separa_fields">&nbsp;</div>
	      <fieldset class="fieldGrid" style="width:905px!important;">
	       	<legend>Grupos Cadastrados</legend>
	      	<div class="gridsGrupos" style="display:none;">
	       		<table class="gruposGrid"></table>
	        </div>
	        
	      </fieldset>
           </div>
           </form>
          
           <div id="tabs-usuarios" class="fixo">
           <div class="areaBts">
           		<div class="area">
           			<span class="bt_novos"><a isEdicao="true" href="javascript:;" onclick="usuariosPermissaoController.popup_novo_usuario();" rel="tipsy" title="Novo"><img src="images/ico_salvar.gif" hspace="5" border="0"/></a></span>
           		</div>
           </div>
           <div class="linha_separa_fields">&nbsp;</div>
            <form action="/administracao/gruposAcesso" id="pesquisar_usuario_form">
	        <fieldset class="fieldFiltro" style="width:905px!important;">
	   	    	<legend> Pesquisar Usuário</legend>
	        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
		            <tr>
		              <td width="82">Nome / Login:</td>
		              <td width="255"><input type="text" name="usuario.nome" id="usuarioNome" style="width:250px;"/></td>
		              <td width="497"><span class="bt_novos"><a href="javascript:;" onclick="usuariosPermissaoController.mostrarUsuario();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
		            </tr>
	          	</table>
	      	</fieldset>
	      	 </form>
	      <div class="linha_separa_fields">&nbsp;</div>
	      <fieldset class="fieldGrid" style="width:905px!important;">
	       	<legend>Usuários  Cadastrados</legend>
	      	<div class="gridsUsuario" style="display:none;">
	       		<table class="usuariosGrid"></table>
	        </div>
	        
	      </fieldset>
	      <br clear="all" />
           </div>
          
           <form action="/administracao/gruposAcesso" id="pesquisar_regras_form">
           <div id="tabs-regras" class="fixo">
           <div class="areaBts">
           		<div class="area">
           			&nbsp;
           		</div>
           </div>
           <div class="linha_separa_fields">&nbsp;</div>
           	<fieldset class="fieldFiltro" style="width:905px!important;">
	   	    <legend> Pesquisar Regras</legend>
	        	<table width="850" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            	<tr>
	              		<td width="32">Nome:</td>
	              		<td width="178"><input type="text" name="filtroConsultaPermissaoDTO.nome" style="width:160px;"/></td>
	              		<td width="52">Descrição:</td>
	              		<td width="313"><input type="text" name="filtroConsultaPermissaoDTO.descricao" style="width:300px;"/></td>
	              		<td width="249"><span class="bt_novosr"><a href="javascript:;" onclick="regrasPermissaoController.mostrarRegra();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
	            	</tr>
	          	</table>
	      	</fieldset>
	      <div class="linha_separa_fields">&nbsp;</div>
	      <fieldset class="field"Grid style="width:905px!important;">
	       	  <legend>Regras Cadastradas</legend>
	          <div class="gridsRegra" style="display:none;height: 300px;overflow: scroll;">
	       	  	<table class="regrasGrid"></table>
	          </div>
	          <%--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="gruposAcessoController.popup_nova_regra();"><img src="images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span--%>
	      </fieldset>
	      <br clear="all" />
           </div>
           </form>
   	</div>

	<div id="ok-falta-de-permissoes" title="Falta de permissões" style="display:none;">
	  <p>Não foi escolhido as permissões para esse grupo</p>
	</div>
</body>
