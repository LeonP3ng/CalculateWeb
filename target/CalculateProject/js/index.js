function start() {
        // 一些初始化的dom操作
        document.getElementById("ansTips").style.display = "block";
        document.getElementById("rights").innerHTML = "";
        document.getElementById("faults").innerHTML = "";
        document.getElementById("rating").innerHTML = "";
        document.getElementById("spending").innerHTML = "";
        // var qtNum = document.getElementById("qtNum").value;
        // var minNum = document.getElementById("minNum").value;
        // var maxNum = document.getElementById("maxNum").value;
        // var optNum = document.getElementById("optNum").value;
        // var opt1 = document.getElementById("opt1").value;
        // var opt2 = document.getElementById("opt2").value;
        
        
        // MAX_NUM = Number(round);
        var quesitons;
        var answers;
        quesitonsArr = [];
        answersArr = [];
        var quesitonsHtmlArr = "";
        var answersHtmlArr = "";
        // 用来存没化简的答案，并且根据正负来改变决定循环走向
        $("#makeqt").click(function(){
            var qtNum = $.trim($("#qtNum").val()),
                minNum = $.trim($("#minNum").val()),
                maxNum = $.trim($("#maxNum").val()),
                optNum = $.trim($("#optNum").val()),
                opt1 = $.trim($("#opt1").val()),
                opt2 = $.trim($("#opt2").val());
            if (qtNum <= 0 || optNum <= 0) {
            alert('请输入正确的题目数及运算符数！');
            return;
            }
            var data = {
                // qtNum: qtNum,
                // minNum: minNum,
                // maxNum: maxNum,
                optNum: optNum,
                opt1: opt1,
                opt2: opt2
            }; 
            $.ajax({
                type: "POST",
                url: "http://152.136.68.17:8080/CalculateProject/hello",
                data: data, 
                dataType: 'json', 
                success: function(status){
                    if(status == true){
                        console.log(Object.keys(quesitons));
                        // quesitonsHtmlArr += ('<li>' + '第' + i + '题题目： ' + quesitons.toString() + ' = ' + "<input class='answer' type='text'/>"  + '</li>' + '<br>');
                    }else{
                        alert("出题失败！");
                    }
                },
                error:function(){
                    alert("请求失败！");
                }

            });
        });
    //     var flag;
    //     for (i = 1; i <= problemNum; i++) {
            
    //         quesitons = createProblem();
    //         answers = simpleFenshu(evalRpn(dal2Rpn(quesitons)));
    //         flag = evalRpn(dal2Rpn(quesitons)).toString();
    //         if(flag.indexOf('-') != -1) {
    //             i--;
    //         } else {
    //             quesitonsArr.push(quesitons.toString());
    //             answersArr.push(answers.toString());
    //             quesitonsHtmlArr += ('<li>' + '第' + i + '题题目： ' + quesitons.toString() + ' = ' + "<input class='answer' type='text'/>"  + '</li>' + '<br>');
    //             answersHtmlArr += ('<li>' + '第' + i + '题答案： ' + answers.toString() + '</li>' + '<br>');
    //         }
    //     }
    //     console.log(quesitonsArr);
    //     console.log(answersArr);
        
    //     document.getElementById("text").innerHTML=quesitonsHtmlArr;
    //     document.getElementById("text2").innerHTML=answersHtmlArr;
    // }
    // 生成下载文件
    function downFile(name, content) {
        document.getElementById("rights2").innerHTML = "";
        document.getElementById("faults2").innerHTML = "";
        // 先做必要判断
        content = content.toString();
        //console.log(content);
        if (name == 'quesitons.txt') {
            var str = content.split(',').join(' = \r\n');
            str = str + " = "
        } else {
            var str = content.split(',').join('\r\n');
        }
        download(name, str);
    }
    
    // // 查对错
    function check() {
        var answer = document.getElementsByClassName("answer");
        res = [];
        for(var i = 0;i<answer.length;i++) {
            res[i] = answer[i].value;
        }
        //console.log(res)
        //console.log(answersArr)
    }
    function checkRight() {
        check();
        var right = "正确题目编号：";
        var fault = "错误题目编号：";
        for(var i = 0;i<res.length;i++) {
            if(res[i] == answersArr[i]) {
                right+= i+1 + ",";
            } else {
                fault+= i+1 + ",";
            }
        }
        //console.log(right);
        //console.log(fault);
        document.getElementById("rights").innerHTML = right;
        document.getElementById("faults").innerHTML = fault;
        //document.getElementById("r-box-map").style.display = "none";
    }
    function checkRightFile () {
        if(FileAnswerArr.length == 0 || FileQuestionArr.length == 0) {
            alert('请先选择文件。')
        } else {
            var right2 = "正确题目编号：";
            var fault2 = "错误题目编号：";
            for(var i = 0;i<FileAnswerArr.length;i++) {
                var ans = FileQuestionArr[i].split('=');
                if (ans[1].trim() == FileAnswerArr[i].trim()) {
                    right2+= i+1 + ",";
                } else {
                    fault2+= i+1 + ",";
                }
            }
            document.getElementById("rights2").innerHTML = right2;
            document.getElementById("faults2").innerHTML = fault2;
        }
    }
}  