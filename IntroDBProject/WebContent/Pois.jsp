<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Persons Of Interest</h1>

<hr/>
<%= session.getAttribute("pois") %> 
<hr/>

<% if (user != null) { %>
<h2>Add Person Of Interest</h2>

<form method="get" action="Pois">
<div>
	<table>
		<tr>
			<td>Firstname:</td>
			<td><input type="text" name="firstname" /></td>
		</tr>
		<tr>
			<td>Surname:</td>
			<td><input type="text" name="surname" /></td>
		</tr>
		<tr>
			<td>Birthday (yyyy-mm-dd):</td>
			<td><input type="text" name="birthday" /></td>
		</tr>
	</table>
	<input type="hidden" name="action" value="add_poi" />
	
	<input type="submit" value="Add" title="addpoi" />
</div>
</form>
<hr/>
<% } %>

<%@ include file="Footer.jsp" %>