<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ch.ethz.inf.dbproject.model.DatastoreInterface"%>
<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@page import="ch.ethz.inf.dbproject.model.Category"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	
	<head>
	    <link href="style.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Law Enforcement Project</title>
	</head>

	<body>

		<!-- Header -->
		
		<table id="masterTable" cellpadding="0" cellspacing="0">
			<tr>
				<th id="masterHeader" colspan="2">
					<h1>Law Enforcement Project</h1>
					Project by Cyrill Kr&auml;henb&uuml;hl, Alexander Peiker und Andreas Hess; 
				</th>
			</tr>
			<tr id="masterContent">
			
				<td id="masterContentMenu">
					
					<div class="menuDiv1"></div>
					<div class="menuDiv1"><a href="Home">Home</a></div>
					<div class="menuDiv1"><a href="Cases">All cases</a></div>
					<div class="menuDiv2"><a href="Cases?filter=open">Open</a></div>
					<div class="menuDiv2"><a href="Cases?filter=closed">Closed</a></div>
					<div class="menuDiv2"><a href="Cases?filter=recent">Recent Open</a></div>
					<div class="menuDiv2"><a href="Cases?filter=oldest">Oldest Unsolved</a></div>
					<div class="menuDiv1">Categories</div>
					<% if (session.getAttribute("catmenu") != null) { %>
					<%=session.getAttribute("catmenu")%>
					<% } %>
					<div class="menuDiv1"><a href="Pois">Persons of interest</a></div>
					<div class="menuDiv1"><a href="Search">Search</a></div>
					<% if ((Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN)) { %>
					<div class="menuDiv1"><a href="User">User Profile</a></div>
					<% } else { %>
					<div class="menuDiv1"><a href="User">Login</a></div>
					<% } %>
					
				</td>
				
				<td id="masterContentPlaceholder">
				
				<% if (session.getAttribute("message") != null) {%>
					<p><%=session.getAttribute("message")%></p>
				<% } %>