package com.lp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculate {

    // 计算结果
    static Object evalString(String temp) {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("js");
        Object last = 0;
        try {
            last = se.eval(temp);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return last;
    }

    // 减数比被减数小
    static int judgeSub(int x, int y,int lowLimit,int highLimit) {
        if (x < y) {
            y = (int) (Math.random() * (highLimit -lowLimit) + lowLimit);
            return judgeSub(x, y,lowLimit,highLimit);
        } else {
            return y;
        }
    }

    // 被除数能被除数整除
    static int judgeDiv(int x, int y,int lowLimit,int highLimit) {
        if (y == 0 || x % y != 0) {
            y = (int) (Math.random() * (highLimit - lowLimit) + lowLimit);
            return judgeDiv(x, y,lowLimit,highLimit);
        } else {
            return y;
        }
    }

    // 最简真分数
    static int judgeSimpleFrac(int x) {
        int y = (int) (Math.random() * 39 +2); // 分母不为0
        if ((y > x) && (gcd(x, y)) == 1) {
            return y;
        } else
            return judgeSimpleFrac(x);
    }

    //分数减法减完后是否大于零
    static int judgeFenziSub(int x, int y) {
        int n = (int) (Math.random() * 20); // 重新生成分子
        if ((n <= x) && (y > n) && (gcd(n, y)) == 1) { // 分子小于结果分子
            return n;
        } else
            return judgeFenziSub(x,y);
    }


    //判断是否为小数
    static boolean judgeDecimal(String num) {
        boolean isdecimal = false;
        if (num.contains(".")) {
            isdecimal = true;
        }
        return isdecimal;
    }
    // 求最大公因数
    static int gcd(int x, int x2) {
        int s = 1;
        x = Math.abs(x);
        x2 = Math.abs(x2);
        //辗转相除
        while (x2 != 0) {
            s = x % x2;
            x = x2;
            x2 = s;
        }
        return x;
    }

    // 求最小公倍数
    static int lcm(int sum2, int y) {
        int b = (sum2 / gcd(sum2, y)) * y;
        return b;
    }


    // 分数相加
    static int[] fracAdd(int sum, int sum2, int x2, int y) {
        int a = lcm(sum2, y);
        int[] x = new int[2];
        x[0] = sum * (a / sum2) + x2 * (a / y);
        x[1] = a;
        int b = gcd(x[0], x[1]);
        x[0] = x[0] / b;
        x[1] = x[1] / b;
        return x;
    }

    // 分数相减
    static int[] fracSub(int sum, int sum2, int x2, int y) {
        int a = lcm(sum2, y);
        int[] x = new int[2];
        x[0] = sum * (a / sum2) - x2 * (a / y);
        x[1] = a;
        int b = gcd(x[0], x[1]);
        x[0] = x[0] / b;
        x[1] = x[1] / b;
        return x;
    }

    static boolean ifAnswerLegal(String answer,int lowLimit,int highLimit){
        int value = Integer.parseInt(answer);
        return value <= highLimit && value >= lowLimit;
    }
}
