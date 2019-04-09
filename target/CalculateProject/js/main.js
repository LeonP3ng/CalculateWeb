var ansList = new Array(); 
var results = new Array();
var answersHtmlArr = "";
var c = 0;
var t; 
 $("#makeqt").click(function(){
            $("#ansText").hide();
            var stuId = $.trim($("#stuId").val()),
                minNum = $.trim($("#minNum").val()),
                maxNum = $.trim($("#maxNum").val()),
                qtNum = $.trim($("#qtNum").val()),
                optNum = $.trim($("#optNum").val()),
                opt1 = $.trim($("#opt1").val()),
                opt2 = $.trim($("#opt2").val());
            
            var quesitonsHtmlArr = "";
           

            if (stuId == '' || minNum == '' || maxNum == '' || minNum >= maxNum || qtNum <= 0 || optNum <= 0) {
            alert('请输入正确的信息！');
            return;
            }
            if(stuId == ''){
                alert("学号不能为空！");
            }
            var data = {
                studentId: stuId,
                n: qtNum,
                lowLimit: minNum,
                highLimit: maxNum,
                flagNumber: optNum,
                ifBrack: opt1,
                ifScore: opt2
            }; 
            $.ajax({
                type: "POST",
                url: "http://152.136.68.17:8080/CalculateProject/hello",
                data: data, 
                dataType: 'JSON', 
                 success: function(data){
                    console.log(data);
                    console.log(data.questions);
                    var list = data.questions; 
                    for(var i in list){
                        for(var key in list[i]){
                            //console.log(key);
                            //console.log(list[i][key]);
                            ansList[i] = list[i][key];
                            //console.log(ansList);
                            var idx = Number(i) + 1; 
                            quesitonsHtmlArr += ('<li>' + '题' + idx + ":  " + key.toString() + ' = ' + "<input class='answer' type='text'/>"  + '</li>' + '<br>');
                            answersHtmlArr += ('<li>' + '题' + idx + '答案： ' + list[i][key].toString() + '</li>' + '<br>');
                        }
                    }
                    document.getElementById("quesText").innerHTML = quesitonsHtmlArr;
                },
                error:function(){
                    alert("error!");
                }

            });
        });
$("#Ans").click(function(){
    $("#ansText").show();
    $(".ansTips").hide();
     var classes = document.getElementsByClassName("answer");
        for(var i = 0; i < classes.length; i++) {
            results[i] = classes[i].value;
            //console.log(results[i]);
        }
        
        var right = "正确题目编号：";
        var fault = "错误题目编号：";
        var rating = "正确率为： ";
        var time = "用时： ";
        var rNum = 0; 
        var tNum = ansList.length;
        for(var i = 0;i<results.length;i++) {
            //console.log(results[i]);
            //console.log(ansList[i]);
             if(results[i] == ansList[i]) {
                right += i+1 + ",";
                rNum += 1;
            } else {
                fault+= i+1 + ",";
            }
        }
        time += $.trim($("#Ctime").val()) + "s";
        //console.log(rNum);
        //console.log(ansList.length);
        rating += Math.round(rNum/tNum*10000)/100.00+"%";
        document.getElementById("ansText").innerHTML = answersHtmlArr;
        document.getElementById("rights").innerHTML = right;
        document.getElementById("faults").innerHTML = fault;
        document.getElementById("rating").innerHTML = rating;
        document.getElementById("time").innerHTML = time;
});
                               
                         
function downLoad(){
    var url = 'http://152.136.68.17:8080/CalculateProject/download';
    window.open(url);
}

function timedCount(){
    document.getElementById('Ctime').value = c;
    c = c+1;
    t = setTimeout("timedCount()",1000);
}

function stopCount(){
    
     clearTimeout(t);
    
 }