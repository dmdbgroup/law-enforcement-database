<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Poi Details</h1>

<%=session.getAttribute("poiTable")%>

<br/>

<h2>Notes</h2>

<%=session.getAttribute("notesTable")%>

<%
if (user != null) {
	// User is logged in. He can add a comment
%>
<br/>
	<form action="Poi" method="get">
		<input type="hidden" name="action" value="add_comment" />
		<input type="hidden" name="poi_id" value="<%= user.getName() %>" />
		<input type="hidden" name="id" value=<%=session.getAttribute("poi_id")%> />
		Add Comment
		<br />
		<textarea rows="4" cols="50" name="comment"></textarea>
		<br />
		<input type="submit" value="Submit" />
	</form>
<%
}
%>

<h2>Convictions</h2>

<%=session.getAttribute("consTable")%>

<%@ include file="Footer.jsp"%>