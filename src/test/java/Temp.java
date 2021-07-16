import com.csvreader.CsvReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Temp {
    public static void main(String[] args) throws IOException {


        CsvReader reader = new CsvReader("C:\\Users\\Administrator\\Desktop\\test-save\\机框.csv", ',', StandardCharsets.UTF_8);
        boolean isFirst = true;
        //reader.readHeaders();
        // 读取每行的内容
        while (reader.readRecord()) {
            if (isFirst) {
                isFirst = false;
                int i = 0;
                while (i < reader.getValues().length) {

                    System.out.println(reader.get(i));
                    i++;
                }
            }
        }
        reader.close();
    }
}
