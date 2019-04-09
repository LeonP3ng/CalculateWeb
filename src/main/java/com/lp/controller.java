package com.lp;

import net.sf.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet(name = "MyController")
public class controller extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            getResult(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            doPost(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResult(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int n = Integer.parseInt(request.getParameter("n"));
        int ifBrack = Integer.parseInt(request.getParameter("ifBrack"));
        int ifScore = Integer.parseInt(request.getParameter("ifScore"));
        int flagNumber = Integer.parseInt(request.getParameter("flagNumber"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int lowLimit = Integer.parseInt(request.getParameter("lowLimit"));
        int highLimit = Integer.parseInt(request.getParameter("highLimit"));
        JSONObject jsonObject = new JSONObject();

        int level = 1;
        if (n > 1000 || n < 1) {
            jsonObject.put("status",false);
            jsonObject.put("falseMsg","请输入1-1000的正整数！");
            response.getWriter().write(jsonObject.toString());
            return;
        }
        if (ifBrack == 1) {
            if (ifScore == 1) {
                level = 4;  //普通，带括号，
            }else{
                level = 2;  //普通，带括号
            }
        }else{
            if (ifScore == 1){
                level = 3;  //普通，分数
            }
        }
        //若都不符合 只生成普通的

        try {
            File file = new File("../result.txt");
            if (file.exists()) {// 检查File.txt是否存在
                file.delete();
            }
            file.createNewFile();//

            PrintStream ps = new PrintStream(file);// 创建一个打印输出流
            System.setOut(ps);// 把创建的打印输出流赋给系统。即系统下次向 ps输出
            System.out.println(studentId);

            List list = judgeQuestion(n,level,flagNumber,lowLimit,highLimit);
            jsonObject.put("questions",list);
            ps.flush();
            ps.close();

            jsonObject.put("file",file.getAbsolutePath());
           // System.out.println(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonObject.put("status",true);
        response.getWriter().write(jsonObject.toString());

    }


    private static List judgeQuestion(int n, int flag,int flagNumber,int lowLimit,int highLimit){
        List<Map<String,String>> list = new ArrayList<>();
        switch (flag){
            //只有简单的四则运算
            case 1: {
                for (int i = 0; i < n; i++) {
                    list.add(CalService.SimpleArithmetic(flagNumber,lowLimit,highLimit));
                }
                break;
            }

            //有带括号的四则运算
            case 2:{
                for (int i = 0; i < n; i++) {
                    if ((int)(Math.random()*2) ==  1) {
                        list.add(CalService.SimpleArithmetic(flagNumber,lowLimit,highLimit));
                    }else {
                        list.add(CalService.bracketArithmetic(flagNumber,lowLimit,highLimit));
                    }
                }
                break;
            }

            //有分数的四则运算
            case 3: {
                for (int i = 0; i < n; i++) {
                    if ((int)(Math.random()*2) ==  1) {
                        list.add(CalService.SimpleArithmetic(flagNumber,lowLimit,highLimit));
                    }else {
                        list.add(CalService.score(flagNumber));
                    }
                }
                break;
            }


            //分数括号都有的四则运算

            case 4: {
                for (int i = 0; i < n; i++) {
                    int type = (int) (Math.random()*3);
                    if (type ==  1) {
                        list.add(CalService.bracketArithmetic(flagNumber,lowLimit,highLimit));
                    }else if (type == 2){
                        list.add(CalService.score(flagNumber));
                    }else {
                        list.add(CalService.SimpleArithmetic(flagNumber,lowLimit,highLimit));
                    }
                }
                break;
            }

        }
        return list;

    }


}
