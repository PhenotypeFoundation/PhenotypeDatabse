<%@ page import="dbnp.query.*" %>
<ul id="criteria">
	<g:each in="${criteria}" var="criterion" status="j">
		<li>
			<span class="entityfield">${criterion.entityField()}</span>
			<span class="operator">${criterion.operator}</span>
			<span class="value">
				<g:if test="${criterion.value != null && criterion.value instanceof Search}">
					<g:link action="show" id="${criterion.value.id}">${criterion.value}</g:link>
				</g:if>
				<g:else>
					${criterion.value}
				</g:else>
			</span>
			<g:if test="${j < criteria.size() -1}">
				<g:if test="${search.searchMode == SearchMode.and}">and</g:if>
				<g:if test="${search.searchMode == SearchMode.or}">or</g:if>
			</g:if>
		</li>
	</g:each>
</ul>