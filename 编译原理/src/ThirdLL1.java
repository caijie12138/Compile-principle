import java.util.ArrayList;

/**
 * Created by Jason_Cai on 2017/5/14.
 */
public class ThirdLL1 {
    /*文法：G[E]:
           E→TE′
           E′→ATE′|ε
           T→FT′
           T′→MFT′|ε
           F→ (E)|i
           A→+|-
           M→*|/
   */

    String[] firstE = {"(","i"};     String[] followE = {"#",")"};
    String[] firstE2 = {"+","-","ε"};String[] followE2 = {"#",")"};
    String[] firstT = {"(","i"};     String[] followT = {"#",")","+","-"};
    String[] firstT2 = {"*","/","ε"};String[] followT2 = {"#",")","+","-"};
    String[] firstF = {"(","i"};     String[] followF = {"#","+","-","*","/"};
    String[] firstA = {"+","-"};     String[] followA = {"(","i"};
    String[] firstM = {"*","/"};     String[] followM = {"(","i"};

    /*
    i:0 +:1  -:2 *:3  /:4 (:5 ):6 $:7
    E:0 E`:1 T:2 T`:3 F:4 A:5 M:6
    */
    String[] line = {"i","+","-","*","/","(",")","#"};
    String[] rank = {"E","E`","T","T`","F","A","M"};
    String[][] rules = new String[7][8];//分析表的声明
    String[] stack;//分析栈
    int index;
    public void Init(){
        rules[0][0] = "0->21";
        rules[0][5] = "0->21";

        rules[1][1] = "1->521";
        rules[1][2] = "1->521";
        rules[1][6] = "1->ε";
        rules[1][7] = "1->ε";

        rules[2][0] = "2->43";
        rules[2][5] = "2->43";

        rules[3][1] = "3->ε";
        rules[3][2] = "3->ε";
        rules[3][3] = "3->643";
        rules[3][4] = "3->643";
        rules[3][6] = "3->ε";
        rules[3][7] = "3->ε";

        rules[4][0] = "4->i";
        rules[4][5] = "4->(0)";

        rules[5][1] = "5->+";
        rules[5][2] = "5->-";

        rules[6][3] = "6->*";
        rules[6][4] = "6->/";
    }

    public void startLL1(ArrayList<Input> result){
        Init();//分析表的初始化
        index = 0;
        int count = 0;
        stack = new String[result.size()+2];
        stack[0] = "#";
        stack[1] = "0";
        while (count<result.size()-1) {
            getStack();//获取当前栈中的元素
            getString(result,count);
            //System.out.print(count);
            if(compareTopOfStack(stack) == 1) {
                pop(stack);
                count++;
            }
            else if(compareTopOfStack(stack) == 2){
                pop(stack);
            }
            else if(compareTopOfStack(stack) == 3){
                System.out.println("分析成功！！");
                break;
            }
            else if(compareTopOfStack(stack) == 4){
                System.out.println("分析失败！！");
                break;
            }
            String sentence = getSentence(result.get(count));
            if (sentence != null) {
                pop(stack);
                push(sentence, stack);
            }
            System.out.println(sentence);
        }

    }
    public void push(String sentence,String[] stack){//进栈
        for(int i=0;i<sentence.length();i++)
            if(sentence.charAt(i)=='>')
                for(int j=sentence.length()-1;j>i;j--)
                    for(int k=0;k<stack.length;k++)
                        if(stack[k]==null) {
                            stack[k] = String.valueOf(sentence.charAt(j));
                            break;
                        }
    }

    public int compareTopOfStack(String[] stack){//对比栈顶元素和终结符
        for(int i=0;i<stack.length;i++)
        {
            if(stack[i]==null){
                for(int j=0;j<line.length;j++) {
                    if((i>0 && stack[i - 1].equals(line[j]))) {
                        index++;
                        return 1;
                    }
                    if((i>0 && stack[i - 1].equals("ε")))
                    {
                        return 2;
                    }
                    if((i>0 && stack[i - 1].equals("#")))
                    {
                        return 3;
                    }
                }
                break;
            }
        }
        return 0;
    }

    public void pop(String[] stack){//栈顶元素出栈
        for(int i=0;i<stack.length;i++)
        {
            if(stack[i]==null){
                stack[i-1]=null;
                break;
            }
        }
    }

    public String getSentence(Input input)//获取对应的产生式
    {
        for(int i=0;i<line.length;i++)
            if(input.getSymbol().equals(line[i]))
                for(int j=0;j<stack.length;j++)
                    if(stack[j]==null) {
                        for (int k = 0; k < rank.length; k++) {
                            if (j > 0 && stack[j - 1].equals(String.valueOf(k)))
                                return rules[k][i];
                        }
                        break;
                    }

        return null;
    }

     public void getString(ArrayList<Input> result,int count){//余留输入串
        for(;count<result.size();count++)
        {
            System.out.print(result.get(count).getSymbol());
        }
        System.out.print("   ");
     }

     public void getStack(){//获取当前栈中的元素
         for(int i=0;i<stack.length;i++)
         {
             if(stack[i]!=null)
                System.out.print(stack[i]);
         }
         System.out.print("   ");
     }

}
