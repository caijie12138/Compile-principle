import java.util.ArrayList;


/**
 * Created by Jason_Cai on 2017/4/9.
 */
class Input{//arraylist 类型
    int num;
    String symbol;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}


public class Firstcifa {

    public String[] remain = {"void", "int", "main", "float", "double", "if", "else", "for", "do", "while","return"};//保留字
    public char[] sigle = {'+', '-', '*', '/', ';', ',', '(', ')', '{', '}', '>', '<', '!', '='};//单字符
    public String[] dou = {">=", "<=", ">>", "<>", "!=", "==", "/*", "++", "--", "<<", "+=", "-=", "*=", "/=", "&&", "||", "//", "*/"};//双字符
    public StringBuffer token = new StringBuffer();//读取的单个单词
    public Input input = new Input();
    public ArrayList<Input> result = new ArrayList<Input>();


    //判断字母
    public Boolean isLetter(int ch) {

        if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122)
            return true;
        else
            return false;
    }

    //判断数字
    public Boolean isNumber(int ch) {
        if (ch >= 48 && ch <= 57)
            return true;
        else
            return false;
    }

    //判断单字符
    public Boolean isSingle(int ch) {
        for (int i = 0; i < sigle.length; i++) {
            if (ch == sigle[i])
                return true;//4表示单字符类型
        }
        return false;
    }

    //判断双字符
    public Boolean isDou() {
        for (int i = 0; i < dou.length; i++) {
            if (token.toString().toLowerCase().equals(dou[i]))
                return true;//5表示双字符类型
        }
        return false;
    }

    //判断保留字
    public Boolean isRemain() {
        for (int i = 0; i < remain.length; i++) {
            if (token.toString().toLowerCase().equals(remain[i]))
                return true;//1表示保留字类型
        }
        return false;
    }

    //判断类型
    public int isWhat() {
        if (isRemain())
            return 1;//1表示保留字类型

        if (isDou())
            return 5;//5表示双字符类型

        if (token.length() != 0) {
            int num1 = 0;//表示数字的个数
            //int num2 = 0;//表示字符的个数
            for (int i = 0; i < token.length(); i++) {
                if (token.charAt(i) >= 48 && token.charAt(i) <= 57)
                    num1++;//数字的个数
            }
            if (num1 == token.length())//全是数字表示是数字
                return 3;//表示数字类型
//            if (token.charAt(0) >= 122 || token.charAt(0) <= 97 && token.charAt(0) >= 91 || token.charAt(0) <= 65)
//                return 6;//单字符
            else
                return 6;
        }
        return 0;
    }

    public void output(int code){
        System.out.println("(" + code + "," + token + ")");
        Input input = new Input();
        input.setNum(code);
        input.setSymbol(token.toString());
        result.add(input);
    }

    public void retract() {//重新抽取
        int code = isWhat();
        if (code == 1) {
           output(code);
        }
        else if (code == 2){
            output(code);
        }
        else if (code == 3) {
            output(code);
        }
        else if (code == 4) {
            for (int i = 0; i < token.length(); i++) {//单个字符还要进行数字和单字符的判断
                if (token.charAt(i) >= 48 && token.charAt(i) <= 57){
                    System.out.println("(" + 3 + "," + token.charAt(i) + ")");
                    Input input = new Input();
                    input.setNum(3);
                    input.setSymbol(token.toString());
                    result.add(input);
                }
                else if (isSingle(token.charAt(i))) {
                    System.out.println("(" + 2 + "," + token.charAt(i) + ")");
                    Input input = new Input();
                    input.setNum(2);
                    input.setSymbol(String.valueOf(token.charAt(i)));
                    result.add(input);
                } else {
                    System.out.println("(" + 6 + "," + token.charAt(i) + ")");
                    Input input = new Input();
                    input.setNum(6);
                    input.setSymbol(String.valueOf(token.charAt(i)));
                    result.add(input);
                }
            }
        }
        else if (code == 5) {
            output(code);
        }
        else if (code == 6) {
            if (isSingle(token.charAt(0))){
                System.out.println("(" + 2 + "," + token.charAt(0) + ")");
                Input input = new Input();
                input.setNum(2);
                input.setSymbol(String.valueOf(token.charAt(0)));
                result.add(input);
            }
            else{
                System.out.println("(" + 6 + "," + token + ")");
                Input input = new Input();
                input.setNum(6);
                input.setSymbol(token.toString());
                result.add(input);
            }
        }
//        else
//            System.out.print("error");
        delete(0);
    }


    public void concat(char ch) {
        token.append(ch);
    }//拼接函数

    public void delete(int i)//删除到指定位置
    {
        token.delete(i, token.length());
    }

    //判断空格
    public Boolean isSpace(int ch) {
        if (ch == 32)
            return true;
        else
            return false;
    }

    public void scanner(int ch) {

        if (!isSpace(ch) && ch != '\n') {//如果不是空格而且不是换行符
            if (isLetter(ch)) {//是字母
                if(token.length()==1&&isSingle(token.charAt(0))) {
                    retract();
                    concat((char) ch);//拼接
                }
                else{
                    concat((char) ch);
                }
            }
            else if (isNumber(ch)) {//是数字
                if (token.length() == 0)
                    concat((char) ch);//拼接
                else{
                    retract();//将之前的输出
                    concat((char) ch);//再拼接
                }
            }
            else if (isSingle(ch)) {//如果是单个字符集中的元素
                if (token.length() != 0 &&
                        (token.toString().equals(">") || token.toString().equals("<") || token.toString().equals("+") ||
                                token.toString().equals("=") || token.toString().equals("-") || token.toString().equals("*")
                                || token.toString().equals("/") || token.toString().equals("!"))
                        && (ch == 60/*<*/ || ch == 62/*>*/ || ch == 61/*=*/ || ch == 43/*+*/ || ch == 45/*-*/ || ch == 47/*/*/ || ch == 42/***/)) {
                    concat((char) ch);
                    retract();
                } else if (ch == 60/*<*/ || ch == 62/*>*/ || ch == 61/*=*/ || ch == 43/*+*/ || ch == 45/*-*/ || ch == 47/*</*/ || ch == 42/***/ || ch == 33/*!*/) {
                    retract();
                    concat((char) ch);
                } else {
                    retract();
                    System.out.println("(" + 2 + "," + (char) ch + ")");//2表示单字符
                    Input input = new Input();
                    input.setNum(2);
                    input.setSymbol(String.valueOf((char) ch));
                    if(!input.getSymbol().equals(";"))
                        result.add(input);
                }
            }
        } else
            retract();

    }
}
