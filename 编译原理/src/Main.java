import java.io.*;

/**
 * Created by Jason_Cai on 2017/4/9.
 */
public class Main {
    static int ch;


    public static void main(String[] args)
    {
        Firstcifa firstcifa = new Firstcifa();
        SecondGrammer secondGrammer = new SecondGrammer();
        ThirdLL1 thirdLL1 = new ThirdLL1();
        FourthOG fourthOG = new FourthOG();
        FifthSLR fifthSLR = new FifthSLR();
        Input end1 = new Input();
        end1.setSymbol("#");
        end1.setNum(2);
       try{
           BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/caijie/Desktop/test1.txt"));
           while ((ch = bufferedReader.read())!=-1)
           {
               firstcifa.scanner(ch);
           }
           firstcifa.result.add(end1);
           //secondGrammer.start(firstcifa.result);
           //thirdLL1.startLL1(firstcifa.result);
           //fourthOG.startOG(firstcifa.result);
           fifthSLR.startSLR(firstcifa.result);

       }catch (FileNotFoundException e)
       {
           e.printStackTrace();
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
    }
}
