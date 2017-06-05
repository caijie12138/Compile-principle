import java.util.ArrayList;

/**
 * Created by Jason_Cai on 2017/5/23.
 */
public class FourthOG {
    /*文法
    G[E]:E→E+T∣E-T∣T
         T→T*F∣T/F∣F
         F→(E)∣i
     */
    String[] EFirstVt = {"+", "-", "*", "/", "(", "i"};
    String[] ELastVt = {"+", "-", "*", "/", ")", "i"};
    String[] TFirstVt = {"*", "/", "(", "i"};
    String[] TLastVt = {"*", "/", ")", "i"};
    String[] FFirstVt = {"(", "i"};
    String[] FLastVt = {")", "i"};

    String line[] = {"+", "-", "*", "/", "(", ")", "i", "#"};
    String rank[] = {"+", "-", "*", "/", "(", ")", "i", "#"};
    /*
    + - * / ( ) i #
    0 1 2 3 4 5 6 7
     */

    /*
   小于关系 # < FirstVt(E) + < FirstVt(T) - < FirstVt(T) * < FirstVt(F) / < FirstVt(F) ( < FirstVt(E)
   大于关系 LastVt(E) > # LastVt(E) > + LastVt(E) > - LastVt(T) > * LastVt(T) > / LastVt(E) > )
     */
    String[][] rules = new String[8][8];//优先矩阵的声明
    String[] stack;//分析栈
    String symbol;


    public void Init() {
        rules[4][5] = "=";//(=)
        rules[7][7] = "=";// #=#
        //# < FirstVt(E)
        rules[7][0] = "<";//# < + # < FirstVt(E)
        rules[7][1] = "<";//# < - # < FirstVt(E)
        rules[7][2] = "<";//# < * # < FirstVt(E)
        rules[7][3] = "<";//# < / # < FirstVt(E)
        rules[7][4] = "<";//# < ( # < FirstVt(E)
        rules[7][6] = "<";//# < i # < FirstVt(E)
        //( < FirstVt(E)
        rules[4][0] = "<";//( < + ( < FirstVt(E)
        rules[4][1] = "<";//( < - ( < FirstVt(E)
        rules[4][2] = "<";//( < * ( < FirstVt(E)
        rules[4][3] = "<";//( < / ( < FirstVt(E)
        rules[4][4] = "<";//( < ( ( < FirstVt(E)
        rules[4][6] = "<";//( < i ( < FirstVt(E)
        //+ < FirstVt(T)
        rules[0][2] = "<";//+ < * + < FirstVt(T)
        rules[0][3] = "<";//+ < / + < FirstVt(T)
        rules[0][4] = "<";//+ < ( + < FirstVt(T)
        rules[0][6] = "<";//+ < i + < FirstVt(T)
        //- < FirstVt(T)
        rules[1][2] = "<";//- < * - < FirstVt(T)
        rules[1][3] = "<";//- < / - < FirstVt(T)
        rules[1][4] = "<";//- < ( - < FirstVt(T)
        rules[1][6] = "<";//- < i - < FirstVt(T)
        //* < FirstVt(F)
        rules[2][4] = "<";//* < ( * < FirstVt(F)
        rules[2][6] = "<";//* < i * < FirstVt(F)
        /// < FirstVt(F)
        rules[3][4] = "<";/// < ( / < FirstVt(F)
        rules[3][6] = "<";/// < i / < FirstVt(F)

        //LastVt(E) > #
        rules[0][7] = ">";
        rules[1][7] = ">";
        rules[2][7] = ">";
        rules[3][7] = ">";
        rules[5][7] = ">";
        rules[6][7] = ">";
        //LastVt(E) > +
        rules[0][0] = ">";
        rules[1][0] = ">";
        rules[2][0] = ">";
        rules[3][0] = ">";
        rules[5][0] = ">";
        rules[6][0] = ">";
        //LastVt(E) > -
        rules[0][1] = ">";
        rules[1][1] = ">";
        rules[2][1] = ">";
        rules[3][1] = ">";
        rules[5][1] = ">";
        rules[6][1] = ">";
        //LastVt(T) > *
        rules[2][2] = ">";
        rules[3][2] = ">";
        rules[5][2] = ">";
        rules[6][2] = ">";
        //LastVt(T) > /
        rules[2][3] = ">";
        rules[3][3] = ">";
        rules[5][3] = ">";
        rules[6][3] = ">";
        // LastVt(E) > )
        rules[0][5] = ">";
        rules[1][5] = ">";
        rules[2][5] = ">";
        rules[3][5] = ">";
        rules[5][5] = ">";
        rules[6][5] = ">";
    }

    public void startOG(ArrayList<Input> result) {
        Init();
        for(int i=0;i<8;i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(rules[i][j] + " ");
            System.out.println();
        }
        stack = new String[result.size() + 2];
        stack[0] = "#";
        int count = 0;
        while (true) {
            getStack();
            relation(result.get(count));
            if(symbol.equals("<")) {
                push(result.get(count));//进栈
                getString(result, count);
                count++;
            }
            else if(symbol.equals(">")){
                change();
                getString(result, count);
                //System.out.println();
            }
            else if(symbol.equals("=")){
                push(result.get(count));//进栈
                if(stack[0].equals("#") && stack[1].equals("N") && stack[2].equals("#")) {
                    System.out.println("分析成功！！");
                    break;
                }
                change();
                change();
                getString(result, count);
                count++;
                //System.out.println();
            }
        }
    }

    public void push(Input input){//进栈
        for(int i = 0;i<stack.length;i++)
        {
            if (stack[i]==null) {
                stack[i] = input.getSymbol();
                break;
            }
        }
    }


    public void change(){//归约 总是把不为N的栈顶元素归约
        int index = 0;
        for(int i=0;i<stack.length;i++)
        {
            if(i!=0 && stack[i]!=null && !stack[i].equals("N") && i!=stack.length)
                index = i;
        }
        if(index!=0&&stack[index-1].equals("N"))
        {
            pop(index);
        }
        else
            stack[index] = "N";
    }

//    public void change2(){//归约 总是把不为N的栈顶元素归约 "="的情况
//        int index1 = 0;
//        for(int i=0;i<stack.length;i++)
//        {
//            if(i!=0 && stack[i]!=null && !stack[i].equals("N") && i!=stack.length)
//                index1 = i;
//        }
//        if(stack[index-1].equals("N"))
//        {
//            pop(index);
//        }
//        else
//            stack[index] = "N";
//    }

    public void pop(int index){

        for(int i =index;i<stack.length;i++)
        {
            stack[i] = null;
        }
    }

    public void getStack() {
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] != null)
                System.out.print(stack[i]);
        }
        System.out.print("  ");
    }

    public void getString(ArrayList<Input> result, int count) {//余留输入串
        for (; count < result.size(); count++) {
            System.out.print(result.get(count).getSymbol());
        }
        System.out.println();
    }

    public void relation(Input input) {
        int position = 0;//栈内不是N的最右边的元素
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] != "N" && stack[i] != null)
                position = i;
        }
        for (int i = 0; i < line.length; i++)
            if (input.getSymbol().equals(line[i])) {
                for (int k = 0; k < rank.length; k++) {
                    if (stack[position].equals(rank[k])) {
                        symbol = rules[k][i];
                        System.out.print(stack[position] + rules[k][i] + input.getSymbol()+"    ");
                    }
                }
            }
    }

}
