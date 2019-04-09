package com.lp;

import java.util.HashMap;
import java.util.Map;

import static com.lp.Calculate.evalString;

public class CalService {

    // 简单的四则运算
    static Map<String,String> SimpleArithmetic(int flagNumber,int lowLimit,int highLimit) {
        char[] p = new char[] { '*', '+', '÷', '-' };
        char[] q = new char[] { '*', '+', '/', '-' };
        String temp1 = "";
        String temp2 = "";
        int j;
        int m = flagNumber; // 符号数
        int[] num = new int[m + 1]; // 数字
        int[] key = new int[m]; // 符号所在的下标
        for (j = 0; j <= m; j++) {
            num[j] = (int) (Math.random() * (highLimit - lowLimit) + lowLimit);
        }
        for (j = 0; j < m; j++) {
            if (j > 0 && key[j - 1] == 3) { // 减号后仅允许加号，防止负数出现
                key[j] = 1;
            } else if (j > 0 && key[j - 1] == 2) {
                key[j] = (int) (Math.random() * 2); // 除号后仅允许乘号与加号，防止负数
            } else {
                key[j] = (int) (Math.random() * 4); // 随机符号
            }
            temp1 += String.valueOf(num[j]) + String.valueOf(p[key[j]]);
            temp2 += String.valueOf(num[j]) + String.valueOf(q[key[j]]);
            if (key[j] == 3) {
                num[j + 1] = Calculate.judgeSub(num[j], num[j + 1],lowLimit,highLimit); // 选定小于被减数的减数
            } else if (key[j] == 2) {
                num[j + 1] = Calculate.judgeDiv(num[j], num[j + 1],lowLimit,highLimit); // 确保能够整除
            }
        }
        j = 0;
        while (j < (m - 1) && key[j] == key[j + 1])
            j++; // 与第一个符号相同数
        if (j == (m - 1) )
            return SimpleArithmetic(flagNumber,lowLimit,highLimit); // 若所有符号相同，该式子不算，保证有两种运算符
        else {

            temp1 += String.valueOf(num[m]);
            temp2 += String.valueOf(num[m]);
            String afterEval = String.valueOf(Calculate.evalString(temp2));
            if (Calculate.judgeDecimal(afterEval) || afterEval.contains("Infinity") || afterEval.equals("NaN")) {
                return SimpleArithmetic(flagNumber,lowLimit,highLimit);// 小数处理
            } else {
                if (!Calculate.ifAnswerLegal(afterEval, lowLimit, highLimit)) //判断答案是否满足限定范围
                    return SimpleArithmetic(flagNumber, lowLimit, highLimit);
                Map<String, String> map = new HashMap<>();
                map.put(temp1, afterEval);
                System.out.println(temp1 + "=" + afterEval);
                return map;
            }
        }


    }


    // 括号四则运算
    static Map<String,String> bracketArithmetic(int flagNumber,int lowLimit,int highLimit) {
        int div = 0; // 用来防止出现除0错误
        int brack_left = 0; // 记录未匹配的左括号个数
        int brack = 0; // 括号个数
        int op;
        char Op[] = { '+', '-', '*', '/' };
        char p[] = { '+', '-', '*', '÷' };
        String temp1 = "";
        String temp2 = "";
        int n = flagNumber+1;// 数字个数
        int[] num = new int[n];
        for (int j = 0; j < n; j++) {
            num[j] = (int) (Math.random() * (highLimit-lowLimit) + lowLimit); // 数字
        }
        int i;
        for (i = 0; i < (n - 2); i++) { // 循环生成算式
            if (div == 1) { // 若此时需要生成的数字前的符号是'/'，则需要特判此次生成的数字不能为0
                div = 0;
                num[i] = (int) (Math.random() * 100 + 1);
            } else if (div == 2) {
                div = 0;
                num[i] = Calculate.judgeSub(num[i - 1], num[i],lowLimit,highLimit); // 确保能够相减
            }
            temp1 += num[i];
            temp2 += num[i];
            int tmpcnt = brack_left;
            for (int j = 0; j < tmpcnt; j++) { // 若当前有未匹配的左括号，则对每一个未匹配的左括号，都有一定概率生成相应右括号。
                if ((int) (Math.random() * 5) > 1) { // 生成右括号概率为0.6
                    brack_left--;
                    temp1 += ")";
                    temp2 += ")";
                }
            }
            op = (int) (Math.random() * 4); // 生成运算符
            temp1 += Op[op];
            temp2 += p[op];
            if (op == 3) // 若生成了除号，则需要置相应标志位
                div = 1;
            else if (op == 1) // 若生成了减号，则需要置相应标志位
                div = 2;
            if (((brack * 2) <= (n - 1)) && (((int) (Math.random() * 2)) == 0)) { //括号数不得超过运算符个数，以一定概率生成左括号，概率为1/2
                temp1 += "(";
                temp2 += "(";
                brack++;
                brack_left++;
                temp1 += num[++i]; // 生成左括号后必须生成一个数字和运算符，不然可能出现(15)这样的错误
                temp2 += num[i];
                op = (int) (Math.random() * 4);
                temp1 += Op[op];
                temp2 += p[op];
                if (op == 3)
                    div = 1;
                else if (op == 1)
                    div = 2;
            }
        }
        while (i != (n - 1)) { // 判断是否为最后一个数
            if (div == 1) {
                div = 0;
                num[i + 1] = (int) (Math.random() * 100 + 1);
            } else if (div == 2) {
                div = 0;
                num[i + 1] = Calculate.judgeSub(num[i], num[i + 1],lowLimit,highLimit);
            }
            temp1 += num[i];
            temp2 += num[i];
            op = (int) (Math.random() * 4);
            temp1 += Op[op];
            temp2 += p[op];
            i++;
        }
        if (div == 1) { // 最后不生出运算符
            div = 0;
            num[n - 1] = (int) (Math.random() * 100 + 1);
        } else if (div == 2) {
            div = 0;
            num[n - 1] = Calculate.judgeSub(num[n - 2], num[n - 1],lowLimit,highLimit);
        }
        temp1 += num[n - 1];
        temp2 += num[n - 1];
        while ((brack_left) != 0) { // 补全右括号
            temp1 += ")";
            temp2 += ")";
            brack_left--;
        }
        //temp2 += "=";
        String result = String.valueOf(evalString(temp1));
        if (Calculate.judgeDecimal(result) || result.contains("Infinity") || result.equals("NaN")) {
            return bracketArithmetic(flagNumber,lowLimit,highLimit);// 小数处理
        } else {
            int result0 = Integer.valueOf(result);
            if (result0 < 0) {
                return bracketArithmetic(flagNumber,lowLimit,highLimit);// 负数处理
            } else {
                if (!Calculate.ifAnswerLegal(result,lowLimit,highLimit))
                    return SimpleArithmetic(flagNumber,lowLimit,highLimit);
                Map<String,String> map = new HashMap<>();
                System.out.println(temp1 + "=" + result);
                map.put(temp2,result);
                return map;
            }
        }
    }

    // 真分数分式
    static Map<String,String> score(int flagNumber) {
        char[] p = new char[] { '+', '-' };
        int j;
        String temp1 = "";
        String temp2 = "";
        int m = flagNumber;
        int[] key = new int[m]; // 运算符
        int[] x = new int[m + 1]; // 分子
        int[] y = new int[m + 1]; // 分母
        int[] sum = new int[2];// 中途运算结果
        for (j = 0; j <= m; j++) {
            x[j] = (int) (Math.random() *20 +1);
            y[j] = Calculate.judgeSimpleFrac(x[j]);
        }
        sum[0] = x[0];
        sum[1] = y[0];
        for (j = 0; j < m; j++) {
            key[j] = (int) (Math.random() * 2);
            if (key[j] == 0) { // 结果小于1
                int[] num = new int[2];
                num = Calculate.fracAdd(sum[0], sum[1], x[j + 1], y[j + 1]);
                if (num[0] >= num[1]) {
                    key[j] = 1;
                } else {
                    sum = num;
                }
            }
            if (key[j] == 1) { // 结果不为负数
                int[] num = new int[2];
                num = Calculate.fracSub(sum[0], sum[1], x[j + 1], y[j + 1]);
                if (num[0] < 0) { //如果减完分子小于0，重新减
                    x[j + 1] = Calculate.judgeFenziSub(sum[0], sum[1]);
                    y[j + 1] = sum[1];
                    num = Calculate.fracSub(sum[0], sum[1], x[j + 1], y[j + 1]);
                }
                sum = num;
            }
            temp1 += String.valueOf(x[j]) + "/" + String.valueOf(y[j]) + String.valueOf(p[key[j]]);
        }
        j = 0;
        while (j < (m - 1) && key[j] == key[j + 1])
            j++; // 与第一个符号相同数
        if (j == (m - 1))
            return score(flagNumber); // 若所有符号相同，该式子不算，保证有两种运算符
        else {
            temp1 += String.valueOf(x[m]) + "/" + String.valueOf(y[m]);
//            return temp1 + "=" + sum[0] + "/" + sum[1];

            temp2 = sum[0] + "/" + sum[1];
            String result = String.valueOf(sum[0]/sum[1]);
            Map<String,String> map = new HashMap<>();
            map.put(temp1,temp2);
            System.out.println(temp1 + "=" + temp2);
            return map;
        }
    }

}
