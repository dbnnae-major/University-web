<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="data.RequestData" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Лабораторная работа 2</title>
    <link rel="icon" type="image/jpg" href="icons/ИконкаСайта.jpg">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsxgraph/1.4.1/jsxgraph.css">
    <style>
        body {
            margin: 0;
            font-family: monospace;
            background-image: url('icons/кот.gif');
            color: #ffffff;
            font-size: 40px;
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
        }

        header {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100px;
            background: rgba(0, 0, 0, 0.82);
            gap: 20px;
            padding: 10px;
        }

        .content {
            display: flex;
            justify-content: space-around;
            align-items: flex-start;
            margin: 20px;
        }

        .coordinates_table, .graphic {
            background-color: rgba(0, 0, 0, 0.82);
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            color: #ffffff;
        }

        .coordinates_table {
            width: 20%;
            font-size: 30px;
            padding: 10px;
        }

        .graphic {
            width: 40%;
            height: 630px;
            font-size: 30px;
            position: relative;
        }

        input[type="text"] {
            width: 80%;
            padding: 10px;
            margin: 20px 0;
            border-radius: 5px;
            border: none;
            font-size: 30px;
            text-align: center;
        }

        .radio-group label {
            display: block;
            font-size: 20px;
            margin: 5px 0;
        }

        input[type="radio"] {
            width: 20px;
            height: 20px;
        }

        .button-group button {
            font-size: 30px;
            margin: 10px;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            background-color: #333;
            color: white;
            cursor: pointer;
        }

        .submit-button {
            margin-top: 20px;
            padding: 10px 30px;
            font-size: 35px;
            background-color: rgba(0, 0, 0, 0.82);
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jsxgraph/1.4.1/jsxgraphcore.js"></script>
    <script>
        function submitForm() {
            const x = document.querySelector('input[name="xRadio"]:checked')?.value;
            const y = document.getElementById('yInput').value;
            const r = document.querySelector('input[name="rValue"]')?.value;

            let errorMessage = '';

            if (!x || isNaN(x) || x < -4 || x > 4) {
                errorMessage += 'Значение X должно быть от -4 до 4.\n';
            }
            if (!y || isNaN(y) || y < -5 || y > 5) {
                errorMessage += 'Значение Y должно быть от -5 до 5.\n';
            }
            if (!r || ![1, 2, 3, 4, 5].includes(Number(r))) {
                errorMessage += 'Значение R должно быть 1, 2, 3, 4 или 5.\n';
            }

            if (errorMessage) {
                alert(errorMessage);
                return false;
            }
            return true;
        }

        let pointsData = [];

        <%
            List<RequestData> collection = (List<RequestData>) request.getServletContext().getAttribute("collection");
            if (collection != null) {
                for (RequestData data : collection) {
                    boolean flag = data.getFlag();
        %>
        pointsData.push({
            x: <%= data.getX() %>,
            y: <%= data.getY() %>,
            r: <%= data.getR() %>,
            color: '<%= (flag) ? "green" : "red" %>'
        });
        <%
                }
            }
        %>

        $(document).ready(function () {
            let radio = 1;
            let pointsByR = {};
            let board = JXG.JSXGraph.initBoard("jxgbox", {
                boundingbox: [-6, 6, 6, -6],
                axis: true,
                defaultAxes: {
                    x: { ticks: { color: 'red', drawZero: true, label: { color: 'red' } } },
                    y: { ticks: { color: 'red', label: { color: 'red' } } }
                }
            });

            function addPoint(x, y, r, color) {
                let point = board.create('point', [x, y], { name: '', size: 2, color: color });
                if (!pointsByR[r]) pointsByR[r] = [];
                pointsByR[r].push(point);
            }

            pointsData.forEach(point => {
                addPoint(point.x, point.y, point.r, point.color);
            });

            $(".button-group button").on("click", function () {
                radio = parseFloat($(this).text().split('=')[1]);
            });

            let graph1, graph2, graph3, i1, i2, i3;


            function redrawGraphs() {
                if (graph1) board.removeObject(graph1);
                if (i1) board.removeObject(i1);
                if (graph2) board.removeObject(graph2);
                if (i2) board.removeObject(i2);
                if (graph3) board.removeObject(graph3);
                if (i3) board.removeObject(i3);

                graph1 = board.create('functiongraph', [function (x) {
                    return -x / 2 + (radio / 2);
                }, 0, radio]);

                i1 = board.create('integral', [[0, radio], graph1], {
                    label: {visible: false},
                    curveLeft: {
                        visible: false
                    },
                    curveRight: {
                        visible: false
                    },
                    baseRight: {
                        visible: false
                    },
                    baseLeft: {
                        visible: false
                    },
                    fillColor: 'white',
                });


                graph2 = board.create('functiongraph', [function (x) {
                    return radio / 2;
                }, -radio, 0]);
                i2 = board.create('integral', [[-radio, 0], graph2], {
                    label: {visible: false},
                    curveLeft: {
                        visible: false
                    },
                    curveRight: {
                        visible: false
                    },
                    baseRight: {
                        visible: false
                    },
                    baseLeft: {
                        visible: false
                    },
                    fillColor: 'white',
                });


                graph3 = board.create('functiongraph', [function (x) {
                    return -Math.sqrt(radio * radio / 4 - x * x);
                }, -radio / 2, 0]);
                i3 = board.create('integral', [[-radio / 2, 0], graph3], {
                    label: {visible: false},
                    curveLeft: {
                        visible: false
                    },
                    curveRight: {
                        visible: false
                    },
                    baseRight: {
                        visible: false
                    },
                    baseLeft: {
                        visible: false
                    },
                    fillColor: 'white',
                });


                for (let r in pointsByR) {
                    pointsByR[r].forEach(point => point.setAttribute({visible: false}));
                }


                if (pointsByR[radio]) {
                    pointsByR[radio].forEach(point => point.setAttribute({visible: true}));
                }
            }

            redrawGraphs();
            $(".button-group button").on("click", function () {
                radio = parseFloat($(this).text().split('=')[1]);
                redrawGraphs();
            });

            var getMouseCoords = function (e, i) {
                    var pos = board.getMousePosition(e, i);
                    return new JXG.Coords(JXG.COORDS_BY_SCREEN, pos, board);
                },

                handleDown = function (e) {
                    var canCreate = true,
                        i, coords, el;

                    if (e[JXG.touchProperty]) {

                        i = 0;
                    }
                    coords = getMouseCoords(e, i);

                    for (el in board.objects) {
                        if (JXG.isPoint(board.objects[el]) && board.objects[el].hasPoint(coords.scrCoords[1], coords.scrCoords[2])) {
                            canCreate = false;
                            break;
                        }
                    }

                    if (canCreate) {
                        let point = board.create('point', [coords.usrCoords[1], coords.usrCoords[2]]);

                        if (!pointsByR[radio]) pointsByR[radio] = [];
                        pointsByR[radio].push(point);

                        document.getElementById('xHidden').value = coords.usrCoords[1].toFixed(2);
                        document.getElementById('yHidden').value = coords.usrCoords[2].toFixed(2);
                        document.getElementById('rHidden').value = radio;
                        document.getElementById('flagHidden').value = 1;

                        document.getElementById('hiddenForm').submit();
                    }
                };

            board.on('down', handleDown);
        });
    </script>
</head>
<body>
<header>
    <p>Дубинин Артём Сергеевич</p>
    <p>Номер варианта: 986</p>
    <p>Номер группы: Р3215</p>
</header>
<div class="content">
    <div class="coordinates_table">
        <form action="/controller" method="post" onsubmit="return submitForm();">
            <p>Выберите X:</p>
            <div class="radio-group">
                <label><input type="radio" name="xRadio" value="-4"> -4</label>
                <label><input type="radio" name="xRadio" value="-3"> -3</label>
                <label><input type="radio" name="xRadio" value="-2"> -2</label>
                <label><input type="radio" name="xRadio" value="-1"> -1</label>
                <label><input type="radio" name="xRadio" value="0"> 0</label>
                <label><input type="radio" name="xRadio" value="1"> 1</label>
                <label><input type="radio" name="xRadio" value="2"> 2</label>
                <label><input type="radio" name="xRadio" value="3"> 3</label>
                <label><input type="radio" name="xRadio" value="4"> 4</label>
            </div>

            <p>Введите Y:</p>
            <input id="yInput" name="yInput" type="text" placeholder="Введите Y">

            <p>Выберите R:</p>
            <div class="button-group">
                <button type="button" onclick="document.querySelector('input[name=\'rValue\']').value = 1">R = 1
                </button>
                <button type="button" onclick="document.querySelector('input[name=\'rValue\']').value = 2">R = 2
                </button>
                <button type="button" onclick="document.querySelector('input[name=\'rValue\']').value = 3">R = 3
                </button>
                <button type="button" onclick="document.querySelector('input[name=\'rValue\']').value = 4">R = 4
                </button>
                <button type="button" onclick="document.querySelector('input[name=\'rValue\']').value = 5">R = 5
                </button>
                <input type="hidden" name="rValue" value=""/>
            </div>

            <div class="submit-container">
                <button type="submit" class="submit-button">Отправить</button>
            </div>
        </form>
    </div>
    <div class="graphic" id="jxgbox"></div>
</div>
<form id="hiddenForm" action="/controller" method="post">
    <input type="hidden" id="xHidden" name="xRadio">
    <input type="hidden" id="yHidden" name="yInput">
    <input type="hidden" id="rHidden" name="rValue">
    <input type="hidden" id="flagHidden" name="flagValue">
</form>
</body>
</html>