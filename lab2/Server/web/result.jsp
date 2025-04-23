<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="data.RequestData" %>
<html>
<head>
    <title>Результаты</title>
    <style>
        body {
            background-image: url('icons/дыра.gif');
            background-size: cover;
            background-repeat: no-repeat;
            background-position: center;
            height: 100vh;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            color: white;
        }


        body::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1;
        }

        .content {
            position: relative;
            z-index: 2;
            background-color: rgba(0, 0, 0, 0.7);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: rgba(255, 255, 255, 0.2);
        }
    </style>
</head>
<body>
<div class="content">
    <h2>История проверок</h2>
    <table border="1">
        <tr>
            <th>X</th>
            <th>Y</th>
            <th>R</th>
            <th>Результат</th>
        </tr>
        <%
            List<RequestData> collection = (List<RequestData>) request.getServletContext().getAttribute("collection");
            if (collection != null) {
                for (RequestData data : collection) {
        %>
        <tr>
            <td><%= data.getX() %></td>
            <td><%= data.getY() %></td>
            <td><%= data.getR() %></td>
            <td><%= data.getFlag() ? "Попадает" : "Не попадает" %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="4">Нет данных для отображения.</td>
        </tr>
        <%
            }
        %>
    </table>

    <br>
    <a href="/controller">дамой</a>
</div>
</body>
</html>

