<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <meta name="renderer" content="webkit" />
    <style>
        #div{
            width: 1500px;
        }
       .table1{
            width: 1200px;
            height: 50px;
           margin-left: 150px;
        }
        table tr{
            height: 50px;
        }
        table tr td{
            line-height: 20px;
            text-align: center;
            vertical-align: middle;
        }
        .table2{
            width: 1200px;
            height: 20px;
            margin-left:150px;
        }
        .modal-footer{
            border-top: 1px  #ffffff !important;
        }
    </style>
</head>
<body>
<div id="div">
    <h3 style="text-align: center">异常巡检报告</h3>

    <table  border="1" cellspacing="0" class="table2">
        <caption><h4>异常详情</h4></caption>
        <tr>
            <th>报告编号</th>
            <th>执行时间</th>
            <th>工人</th>
            <th>区域</th>
            <th>设备</th>
            <th>异常项</th>
            <th>描述</th>
            <th>异常记录链接</th>
        </tr>

            <tr>
                <td>${taskreportinfo.taskcode}</td>
                <td>${reportcontent.operationtime}</td>
                <td>${taskreportinfo.worker}</td>
                <td>${reportcontent.areaname}</td>
                <td>${reportcontent.equipname}</td>
                <td>${reportcontent.checkname}</td>
                <td>${reportcontent.errcontent}</td>
                <td>
                    <c:if test="${reportcontent.img != 'null' or reportcontent.audio != 'null' or reportcontent.video != 'null'}">
                        <a href='http://${ip}/toException?img=${reportcontent.img}&audio=${reportcontent.audio}&video=${reportcontent.video}'  target="_Blank">查看异常详情</a>
                    </c:if>
                </td>
            </tr>
    </table>
</div>
</body>
</html>
