<c:if test="${not empty errors}">
	<!-- MENSAGEM -->
	<table width="550" border="0" cellspacing="0" cellpadding="0"
		class="cx_mensagens">
		<tr>
			<td class="titulo">Erro</td>
		</tr>

		<tr class="corpo">
			<td><c:forEach items="${errors}" var="error">
					<p>
						<fmt:message key="${error.category}" /> -
						<fmt:message key="${error.message}" />
					</p>
				</c:forEach></td>
		</tr>
	</table>
	<!-- FIM MENSAGEM -->

</c:if>