public class Temp {
    public static void main(String[] args) {
        for (int i = 0; i <= 272; i++) {
            //System.out.println("                case \"PHY.ULMeanNL.PRB"+i+"\":\n" +
            //       "                    PHY_ULMeanNL_PRB"+i+" = i;\n" +
            //       "                    break;");
            //System.out.println("            case "+i+":\n" +
            //        "                return PHY_ULMeanNL_PRB"+i+";");
           // System.out.println(" private static int PHY_ULMeanNL_PRB"+i+" = 0;");


            int min1 = 113, max1 = 117;
            int rd1 = 0 - (min1 + (int) (Math.random() * ((max1 - min1) + 1)));

            System.out.println(rd1);

        }

    }
}
