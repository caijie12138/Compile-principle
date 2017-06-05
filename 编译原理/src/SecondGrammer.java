import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;
import static java.lang.System.in;

/**
 * Created by Jason_Cai on 2017/5/9.
 */

public class SecondGrammer {
    /* G[E]:
            E→TE′
            E′→ATE′|ε
            T→FT′
            T′→MFT′|ε
            F→ (E)|i
            A→+|-
            M→*|/
    */

    public int index = 0;//下标
    public Input[] stack;

    public void start(ArrayList<Input> result) {
//        while (i < result.size()) {
        if (result.get(index).getNum() == 1) {
            index++;
            //  continue;
        } else if (result.get(index).getNum() == 6) {
            if (result.get(index).getSymbol().charAt(0) >= 48 &&
                    result.get(index).getSymbol().charAt(0) <= 57) {
                System.out.println("分析失败！");
                exit(0);
            }
        }
        stack = new Input[result.size()];

        for (int i = 0; i < result.size(); i++) {
            stack[i] = result.get(i);
        }
        EType();
        System.out.println("正确语句！");
        //index = 0;
        // i++;
        // if(i==100)
        //    System.out.print(result.get(i).getSymbol());
        // }

    }

    public void EType() {
        TType();
        EType2();
    }

    public void TType() {
        FType();
        TType2();
    }

    public void TType2() {
        MType();
    }

    public void EType2() {
        AType();
    }

    public void FType() {
        if (stack[index].getSymbol().equals("(")) {
            index++;
            EType();
            if (stack[index].getSymbol().equals(")")) {
                index++;
            } else {
                System.out.println("分析失败！缺少了')'");
                exit(0);
            }
        } else {
            index++;
        }
    }

    public void MType() {
        if (stack[index].getSymbol().equals("*")) {
            index++;
            FType();
            TType2();
        } else if (stack[index].getSymbol().equals("/")) {
            index++;
            FType();
            TType2();
        }
    }

    public void AType() {
        if (stack[index].getSymbol().equals("+")) {
            index++;
            TType();
            EType2();
        } else if (stack[index].getSymbol().equals("-")) {
            index++;
            TType();
            EType2();
        }
    }
}
