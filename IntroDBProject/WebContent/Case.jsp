<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Case Details</h1>

<%=session.getAttribute("caseTable")%>

<br/>

<% if (user != null) { %>
<form method="get" action="Case">
<div>
	<input type="hidden" name="action" value="toggleOpen" />
	<input type="hidden" name="id" value=<%=session.getAttribute("case_id")%> />
	<input type="submit" value="Toggle open/closed" title="Toggle open/closed" />
</div>
</form>
<% } %>

<h2>Notes</h2>

<%=session.getAttribute("notesTable")%>

<%
if (user != null) {
	// User is logged in. He can add a comment
%>
<br/>
	<form action="Case" method="get">
		<input type="hidden" name="action" value="add_comment" />
		<input type="hidden" name="user_id" value="<%= user.getName() %>" />
		<input type="hidden" name="id" value=<%=session.getAttribute("case_id")%> />
		Add Comment
		<br />
		<textarea rows="4" cols="50" name="comment"></textarea>
		<br />
		<input type="submit" value="Submit" />
	</form>
<%
}
%>


<% if (!(Boolean) session.getAttribute("case_open")) { %>
	<h2>Convictions</h2>
<% } else { %>
	<h2>Suspects</h2>
<% } %>

<form method="get" action="Case">
<input type="hidden" name="action" value="update_date" />
<input type="hidden" name="id" value="<%=session.getAttribute("case_id")%>" />
<%=session.getAttribute("convictionsTable")%>

<% if (user != null && !(Boolean) session.getAttribute("case_open")) { %>
<br />
<input type="submit" value="update end dates" /><br />
<% } %>

<% if (user != null && (Boolean) session.getAttribute("case_open")) { %>
<br />
<a href="Link?case_id=<%=session.getAttribute("case_id")%>">Link Persons of Interest to this case</a>
<% } %>
</form>

<%@ include file="Footer.jsp"%>