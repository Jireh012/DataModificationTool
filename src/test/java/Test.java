public class Test {
    public static void main(String[] args) {
        String str1= "瑞安鲍田前北汇聚CRAN瑞安汀田小微园_3";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str1.length());
        stringBuilder.append("5");
        stringBuilder.append("20210617");
        stringBuilder.append(30/15);
        System.out.println(Integer.valueOf(stringBuilder.toString())%5);
        System.out.println(stringBuilder);
    }
}
