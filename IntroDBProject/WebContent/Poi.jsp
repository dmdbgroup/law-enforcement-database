<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Person of interest Details</h1>

<%= session.getAttribute("poiTable") %>

<h2>Notes</h2>

<%= session.getAttribute("notesTable") %>

<%@ include file="Footer.jsp"%>