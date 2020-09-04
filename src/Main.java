import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static String Colorize(String message, byte foregroundNum)
    {
        if(foregroundNum>=0 && foregroundNum<16) {

            String color = "\u001b[3";

            int firstColorNum = foregroundNum / 2;
            int secondColorNum = foregroundNum % 2;

            color= color + firstColorNum;
            if(secondColorNum!=0)
                color = color + ";1";
            color = color + "m";
            String clearer = "\u001b[0m";

            return color + message + clearer;
        }
        return  message;
    }
    public static String Colorize(String message, byte foregroundNum, byte backgroundNum)
    {
        if(backgroundNum>=0 && backgroundNum<16) {

            String color = "\u001b[4";

            int firstColorNum = backgroundNum / 2;
            int secondColorNum = backgroundNum % 2;

            color= color + firstColorNum;
            if(secondColorNum!=0)
                color = color + ";1";
            color = color + "m";

            return color + Colorize(message, foregroundNum);
        }
        return  message;
    }



    public static void main(String[] args) {
        //System.out.printf("Hello world");
        Random rand = new Random();
        ColoredPrinter Out = new ColoredPrinter.Builder(1, false).build();

        int width = 11;
        int height = 11;
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                matrix[i][j] = rand.nextInt(100);


        int[][] foundVal = new int[2][3];
        boolean firstEncounter = true;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
            {
                if(i > j)
                {
                    if(firstEncounter)
                    {
                        foundVal[0][0] = matrix[i][j];
                        foundVal[0][1] = i;
                        foundVal[0][2] = j;
                        firstEncounter = false;
                    }
                    else
                    {
                        if(matrix[i][j] <= foundVal[0][0])
                        {
                            foundVal[0][0] = matrix[i][j];
                            foundVal[0][1] = i;
                            foundVal[0][2] = j;
                        }
                    }
                }
                //===============================
                if(i+j < width-1)
                {
                    if(matrix[i][j] >= foundVal[1][0])
                    {
                        foundVal[1][0] = matrix[i][j];
                        foundVal[1][1] = i;
                        foundVal[1][2] = j;
                    }
                }
            }

        Runnable DrawMatrix = ()->
        {
            String splitter = " \t";

            for (int i = 0; i< height; i++)
            {
                for (int j = 0; j< width; j++)
                {
                    if(i == foundVal[0][1] && j == foundVal[0][2])
                        System.out.printf(Colorize(String.valueOf(matrix[i][j]),(byte) 13,(byte) 3) + splitter);
                    else
                    if(i == foundVal[1][1] && j == foundVal[1][2])
                        System.out.printf(Colorize(String.valueOf(matrix[i][j]),(byte) 13,(byte) 9) + splitter);
                    else
                    if(i == j && i+j == width-1)
                        Out.print(matrix[i][j] + splitter, Ansi.Attribute.BOLD, Ansi.FColor.MAGENTA, Ansi.BColor.NONE);
                    else
                    if(i == j)
                        Out.print(matrix[i][j] + splitter, Ansi.Attribute.BOLD, Ansi.FColor.RED, Ansi.BColor.NONE);
                    else
                    if(i+j == width-1)
                        Out.print(matrix[i][j] + splitter, Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.NONE);
                        //else
                        //if(i > j && i+j < width-1)
                        //System.out.printf(Colorize(matrix[i][j] + " ",(byte) rand.nextInt(16)));
                        //Out.print(matrix[i][j] + " ", Ansi.Attribute.BOLD, Ansi.FColor.CYAN, Ansi.BColor.NONE);
                    else
                        System.out.printf(matrix[i][j] + splitter);
                }
                System.out.print('\n');
            }
        };

        Runnable DrawMatrixWResults = ()->
        {
            DrawMatrix.run();
            System.out.printf("min["+ (foundVal[0][2] +1) +"]["+(foundVal[0][1] + 1)+"] = "+foundVal[0][0] + "\n");
            System.out.printf("max["+ (foundVal[1][2] +1) +"]["+(foundVal[1][1] + 1)+"] = "+foundVal[1][0] + "\n");
            System.out.printf("\n");
        };

        System.out.printf("Generated matrix\n");
        DrawMatrixWResults.run();

        matrix[foundVal[0][1]][foundVal[0][2]] = foundVal[1][0];
        matrix[foundVal[1][1]][foundVal[1][2]] = foundVal[0][0];

        int temp = foundVal[0][1];
        foundVal[0][1] = foundVal[1][1];
        foundVal[1][1] = temp;

        temp = foundVal[0][2];
        foundVal[0][2] = foundVal[1][2];
        foundVal[1][2] = temp;

        DrawMatrixWResults.run();

      //  System.out.print('\n');
       // for (byte i = 0; i< 16; i++) {
       ///     for (byte j = 0; j < 16; j++) {
       //       System.out.print(Colorize(String.valueOf(j+i*16), i,j)+ " ");
       //     }
      //      System.out.print('\n');
     //   }
    }
}
