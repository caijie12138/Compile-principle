import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Created by Jason_Cai on 2017/6/4.
 */
public class FifthSLR {
    /*
    G[A]:
    S→A[0]
    A→V=E(1)
    E→E+T(2)∣E-T(3)∣T(4)
    T→T*F(5)∣T/F(6)∣F(7)
    F→(E)(8)∣i(9)
    V→i(10)
    */
    //            ACTION                      GOTO
    //    0  1  2  3  4  5  6  7  8    9 10 11 12 13
    //    i  (  )  +  -  *  /  =  #    A  E  T  F  V
    //0   3                            1           2
    //1                          acc
    //2                        4
    //3                        r10
    //4   9  8                            5  6  7
    //5            10 11          r1
    //6         r4 r4 r4 12 13    r4
    //7         r7 r7 r7 r7 r7    r7
    //8   9  8                            14 6  7
    //9         r9 r9 r9 r9 r9    r9
    //10  9  8                               15 7
    //11  9  8                               16 7
    //12  9  8                                  17
    //13  9  8                                  18
    //14        19 10 11
    //15        r2 r2 r2 12 13    r2
    //16        r3 r3 r3 12 13    r3
    //17        r5 r5 r5 r5 r5    r5
    //18        r6 r6 r6 r6 r6    r6
    //19        r8 r8 r8 r8 r8    r8
    String product[] = new String[11];//产生式集合
    int productnum[] = new int[11];//每个产生式右侧的元素个数
    String rules[][] = new String[21][14];//分析表
    String[] terminateSymbol;
    String[] stacksymbol;//符号栈
    String[] stackstate;//状态栈
    int temp = 1;//临时变量的个数
    int index = -1; //记录归约所使用的产生式


    public void Init() {//分析表的初始化
        product[0] = "S";
        product[1] = "A";
        product[2] = "E";
        product[3] = "E";
        product[4] = "E";
        product[5] = "T";
        product[6] = "T";
        product[7] = "T";
        product[8] = "F";
        product[9] = "F";
        product[10] = "V";

        productnum[0] = 1;
        productnum[1] = 3;
        productnum[2] = 3;
        productnum[3] = 3;
        productnum[4] = 1;
        productnum[5] = 3;
        productnum[6] = 3;
        productnum[7] = 1;
        productnum[8] = 3;
        productnum[9] = 1;
        productnum[10] = 1;


        rules[0][0] = "S3";
        rules[0][9] = "1";
        rules[0][13] = "2";

        rules[1][8] = "acc";

        rules[2][7] = "S4";

        rules[3][7] = "r10";

        rules[4][0] = "S9";
        rules[4][1] = "S8";
        rules[4][10] = "5";
        rules[4][11] = "6";
        rules[4][12] = "7";

        rules[5][3] = "S10";
        rules[5][4] = "S11";
        rules[5][8] = "r1";

        rules[6][2] = "r4";
        rules[6][3] = "r4";
        rules[6][4] = "r4";
        rules[6][5] = "S12";
        rules[6][6] = "S13";
        rules[6][8] = "r4";

        rules[7][2] = "r7";
        rules[7][3] = "r7";
        rules[7][4] = "r7";
        rules[7][5] = "r7";
        rules[7][6] = "r7";
        rules[7][8] = "r7";

        rules[8][0] = "S9";
        rules[8][1] = "S8";
        rules[8][10] = "14";
        rules[8][11] = "6";
        rules[8][12] = "7";

        rules[9][2] = "r9";
        rules[9][3] = "r9";
        rules[9][4] = "r9";
        rules[9][5] = "r9";
        rules[9][6] = "r9";
        rules[9][8] = "r9";

        rules[10][0] = "S9";
        rules[10][1] = "S8";
        rules[10][11] = "15";
        rules[10][12] = "7";

        rules[11][0] = "S9";
        rules[11][1] = "S8";
        rules[11][11] = "16";
        rules[11][12] = "7";

        rules[12][0] = "S9";
        rules[12][1] = "S8";
        rules[12][12] = "17";

        rules[13][0] = "S9";
        rules[13][1] = "S8";
        rules[13][12] = "18";

        rules[14][2] = "S19";
        rules[14][3] = "S10";
        rules[14][4] = "S11";

        rules[15][2] = "r2";
        rules[15][3] = "r2";
        rules[15][4] = "r2";
        rules[15][5] = "S12";
        rules[15][6] = "S13";
        rules[15][8] = "r2";

        rules[16][2] = "r3";
        rules[16][3] = "r3";
        rules[16][4] = "r3";
        rules[16][5] = "S12";
        rules[16][6] = "S13";
        rules[16][8] = "r3";

        rules[17][2] = "r5";
        rules[17][3] = "r5";
        rules[17][4] = "r5";
        rules[17][5] = "r5";
        rules[17][6] = "r5";
        rules[17][8] = "r5";

        rules[18][2] = "r6";
        rules[18][3] = "r6";
        rules[18][4] = "r6";
        rules[18][5] = "r6";
        rules[18][6] = "r6";
        rules[18][8] = "r6";

        rules[19][2] = "r8";
        rules[19][3] = "r8";
        rules[19][4] = "r8";
        rules[19][5] = "r8";
        rules[19][6] = "r8";
        rules[19][8] = "r8";

        rules[20][0] = "i";
        rules[20][1] = "(";
        rules[20][2] = ")";
        rules[20][3] = "+";
        rules[20][4] = "-";
        rules[20][5] = "*";
        rules[20][6] = "/";
        rules[20][7] = "=";
        rules[20][8] = "#";
        rules[20][9] = "A";
        rules[20][10] = "E";
        rules[20][11] = "T";
        rules[20][12] = "F";
        rules[20][13] = "V";
    }

    public void startSLR(ArrayList<Input> result) {
        Init();
        terminateSymbol = new String[result.size()];//初始化归约的终结符集合大小
//        for (int i = 0; i < 19; i++) {
//            for (int j = 0; j < 14; j++)
//                System.out.print(rules[i][j] + " ");
//            System.out.println();
//        }
        int count = 0;//记录当前输入串的位置
        stacksymbol = new String[result.size() + 2];//初始化符号栈
        stackstate = new String[result.size() + 2];//初始化分析栈
        stacksymbol[0] = "#";
        stackstate[0] = "0";
        while (true) {
            getStack();
            for (int i = count; i < result.size(); i++) {
                System.out.print(result.get(i).getSymbol());
            }
            System.out.print("  ");
            if (judgement((stackstate[getIndex(stackstate) - 1]), count, result)) {
                count++;
            }

        }
    }

    public boolean judgement(String symbol, int count, ArrayList<Input> result) {//判断应该进行归约还是移进
        if (!(symbol.equals("1") || symbol.equals("3") || symbol.equals("5") || symbol.equals("6") || symbol.equals("7")
                || symbol.equals("9") || symbol.equals("15") || symbol.equals("16") || symbol.equals("17") || symbol.equals("18") ||
                symbol.equals("19")))//说明栈正处于移进状态
        {
            for (int i = 0; i < 9; i++) {
                if (result.get(count).getSymbol().equals(rules[20][i]))//找到相同的两个符号
                {
                    if (rules[Integer.parseInt(symbol)][i] != null)//在判断对应的0行i列是否有值
                    {
                        System.out.print(rules[Integer.parseInt(symbol)][i]);
                        stackstate[getIndex(stackstate)] = rules[Integer.parseInt(symbol)][i].substring(1);//两个栈都分别添加元素
                        stacksymbol[getIndex(stacksymbol)] = result.get(count).getSymbol();
                        System.out.println();
                        return true;

                    } else { //
                        System.out.print("error");
                        return false;
                    }
                }
            }
        } else //说明栈正处于归约状态
        {
            int i;
            for (i = 0; i < 9; i++) {
                if (result.get(count).getSymbol().equals(rules[20][i]))//找到相同的两个符号
                {
                    if (rules[Integer.parseInt(symbol)][i] != null && rules[Integer.parseInt(symbol)][i].substring(0, 1).equals("r"))//在判断对应的0行i列是否有值
                    {
                        System.out.print(rules[Integer.parseInt(symbol)][i]);//打印归约或者移进所使用的方法
                        System.out.print("  ");
                        //归约的时候需要将终结符记录，方便产生四元式
                        if (rules[Integer.parseInt(symbol)][i].substring(0, 1).equals("r")) {//记录归约过的终结符号集合
                            if (stacksymbol[getIndex(stacksymbol) - 1].equals("i")) {
                                terminateSymbol[getIndex(terminateSymbol)] = stacksymbol[getIndex(stacksymbol) - 1];
                            }
                            index = Integer.parseInt(rules[Integer.parseInt(symbol)][i].substring(1));//记录归约所使用的产生式
                        }

                        for (int k = 0; k < productnum[Integer.parseInt(rules[Integer.parseInt(symbol)][i].substring(1))]; k++) {//弹栈的次数根据产生式右侧的符号数量决定
                            stackstate[getIndex(stackstate) - 1] = null;//弹栈
                            stacksymbol[getIndex(stacksymbol) - 1] = null;//弹栈
                        }
                        stacksymbol[getIndex(stacksymbol)] = product[Integer.parseInt(rules[Integer.parseInt(symbol)][i].substring(1))];//产生式左侧进栈
                        //查询goto表
                        for (int j = 9; j < 14; j++)
                            if (rules[20][j].equals(product[Integer.parseInt(rules[Integer.parseInt(symbol)][i].substring(1))]))//产生式集合对应的终结符
                            {
                                System.out.print(rules[Integer.parseInt(stackstate[getIndex(stackstate) - 1])][j]);//打印goto值
                                stackstate[getIndex(stackstate)] = rules[Integer.parseInt(stackstate[getIndex(stackstate) - 1])][j];//goto的值进栈
                                //产生四元式
                                produceFour(index, temp);
                                System.out.println();
                                return false;
                            }
                    } else if (rules[Integer.parseInt(symbol)][i] != null) {
                        System.out.print(rules[Integer.parseInt(symbol)][i]);
                        if (rules[Integer.parseInt(symbol)][i].equals("acc")) {//归约情况出现acc就说明成功
                            System.out.println();
                            System.out.print("分析成功！！");
                            exit(0);
                        }
                        stackstate[getIndex(stackstate)] = rules[Integer.parseInt(symbol)][i].substring(1);//两个栈都分别添加元素
                        stacksymbol[getIndex(stacksymbol)] = result.get(count).getSymbol();
                        System.out.println();
                        return true;
                    } else {//
                        System.out.print("error");
                        exit(0);
                    }
                }
            }
        }
        System.out.print("error");
        exit(0);
        return false;
    }

    public int getIndex(String[] stackstate) {
        for (int i = 0; i < stackstate.length; i++) {
            if (stackstate[i] == null) {
                return i;
            }
        }
        return 0;
    }

    public void produceFour(int index, int temp) {
        int position;
        switch (index) {
            case 1:
                System.out.print("  ");
                System.out.print("(" + "=," + terminateSymbol[getIndex(terminateSymbol) - 1] + ",_," + terminateSymbol[getIndex(terminateSymbol) - 2] + ")");
                position = getIndex(terminateSymbol) - 1;
                terminateSymbol[position] = null;
                terminateSymbol[position - 1] = null;
                terminateSymbol[position - 1] = "T" + this.temp;
                break;
            case 2:
                System.out.print("  ");
                System.out.print("(" + "+," + terminateSymbol[getIndex(terminateSymbol) - 2] + "," + terminateSymbol[getIndex(terminateSymbol) - 1] + "," + "T" + this.temp + ")");
                position = getIndex(terminateSymbol) - 1;
                terminateSymbol[position] = null;
                terminateSymbol[position - 1] = null;
                terminateSymbol[position - 1] = "T" + this.temp;
                this.temp++;
                break;
            case 3:
                System.out.print("  ");
                System.out.print("(" + "-," + terminateSymbol[getIndex(terminateSymbol) - 2] + "," + terminateSymbol[getIndex(terminateSymbol) - 1] + "," + "T" + this.temp + ")");
                position = getIndex(terminateSymbol) - 1;
                terminateSymbol[position] = null;
                terminateSymbol[position - 1] = null;
                terminateSymbol[position - 1] = "T" + this.temp;
                this.temp++;
                break;
            case 5:
                System.out.print("  ");
                System.out.print("(" + "*," + terminateSymbol[getIndex(terminateSymbol) - 2] + "," + terminateSymbol[getIndex(terminateSymbol) - 1] + "," + "T" + this.temp + ")");
                position = getIndex(terminateSymbol) - 1;
                terminateSymbol[position] = null;
                terminateSymbol[position - 1] = null;
                terminateSymbol[position - 1] = "T" + this.temp;
                this.temp++;
                break;
            case 6:
                System.out.print("  ");
                System.out.print("(" + "/," + terminateSymbol[getIndex(terminateSymbol) - 2] + "," + terminateSymbol[getIndex(terminateSymbol) - 1] + "," + "T" + this.temp + ")");
                position = getIndex(terminateSymbol) - 1;
                terminateSymbol[position] = null;
                terminateSymbol[position - 1] = null;
                terminateSymbol[position - 1] = "T" + this.temp;
                this.temp++;
                break;
        }
    }

    public void getStack() {
        for (int i = 0; i < stackstate.length; i++) {
            if (stackstate[i] != null)
                System.out.print(stackstate[i]);
        }
        System.out.print("  ");
        for (int i = 0; i < stacksymbol.length; i++) {
            if (stackstate[i] != null)
                System.out.print(stacksymbol[i]);
        }
        System.out.print("  ");
    }
}
