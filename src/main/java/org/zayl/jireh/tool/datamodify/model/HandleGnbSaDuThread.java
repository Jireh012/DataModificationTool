package org.zayl.jireh.tool.datamodify.model;

import ch.ethz.ssh2.Connection;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;
import org.zayl.jireh.tool.datamodify.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.zayl.jireh.tool.datamodify.util.Const.*;
import static org.zayl.jireh.tool.datamodify.util.FileUtil.isChartPathExist;
import static org.zayl.jireh.tool.datamodify.util.Mathematical.StringToInt;

/**
 * @author last_
 */
public class HandleGnbSaDuThread implements Runnable {
    private final CountDownLatch threadsSignal;
    private final Map.Entry<String, List<String>> sourceData;

    private final Logger logger = Logger.getLogger(HandleGnbSaDuThread.class);
    private String ymd;
    private String nms;

    public HandleGnbSaDuThread(CountDownLatch threadsSignal, Map.Entry<String, List<String>> sourceData) {
        this.threadsSignal = threadsSignal;
        this.sourceData = sourceData;
    }

    private void initDataPosition(CsvReader reader) throws IOException {
        int i = 0;
        //初始化相关列位置
        while (i < reader.getValues().length) {
            switch (reader.get(i)) {
                case "PHY.ULMeanNL.PRB0":
                    PHY_ULMeanNL_PRB0 = i;
                    break;
                case "PHY.ULMeanNL.PRB1":
                    PHY_ULMeanNL_PRB1 = i;
                    break;
                case "PHY.ULMeanNL.PRB2":
                    PHY_ULMeanNL_PRB2 = i;
                    break;
                case "PHY.ULMeanNL.PRB3":
                    PHY_ULMeanNL_PRB3 = i;
                    break;
                case "PHY.ULMeanNL.PRB4":
                    PHY_ULMeanNL_PRB4 = i;
                    break;
                case "PHY.ULMeanNL.PRB5":
                    PHY_ULMeanNL_PRB5 = i;
                    break;
                case "PHY.ULMeanNL.PRB6":
                    PHY_ULMeanNL_PRB6 = i;
                    break;
                case "PHY.ULMeanNL.PRB7":
                    PHY_ULMeanNL_PRB7 = i;
                    break;
                case "PHY.ULMeanNL.PRB8":
                    PHY_ULMeanNL_PRB8 = i;
                    break;
                case "PHY.ULMeanNL.PRB9":
                    PHY_ULMeanNL_PRB9 = i;
                    break;
                case "PHY.ULMeanNL.PRB10":
                    PHY_ULMeanNL_PRB10 = i;
                    break;
                case "PHY.ULMeanNL.PRB11":
                    PHY_ULMeanNL_PRB11 = i;
                    break;
                case "PHY.ULMeanNL.PRB12":
                    PHY_ULMeanNL_PRB12 = i;
                    break;
                case "PHY.ULMeanNL.PRB13":
                    PHY_ULMeanNL_PRB13 = i;
                    break;
                case "PHY.ULMeanNL.PRB14":
                    PHY_ULMeanNL_PRB14 = i;
                    break;
                case "PHY.ULMeanNL.PRB15":
                    PHY_ULMeanNL_PRB15 = i;
                    break;
                case "PHY.ULMeanNL.PRB16":
                    PHY_ULMeanNL_PRB16 = i;
                    break;
                case "PHY.ULMeanNL.PRB17":
                    PHY_ULMeanNL_PRB17 = i;
                    break;
                case "PHY.ULMeanNL.PRB18":
                    PHY_ULMeanNL_PRB18 = i;
                    break;
                case "PHY.ULMeanNL.PRB19":
                    PHY_ULMeanNL_PRB19 = i;
                    break;
                case "PHY.ULMeanNL.PRB20":
                    PHY_ULMeanNL_PRB20 = i;
                    break;
                case "PHY.ULMeanNL.PRB21":
                    PHY_ULMeanNL_PRB21 = i;
                    break;
                case "PHY.ULMeanNL.PRB22":
                    PHY_ULMeanNL_PRB22 = i;
                    break;
                case "PHY.ULMeanNL.PRB23":
                    PHY_ULMeanNL_PRB23 = i;
                    break;
                case "PHY.ULMeanNL.PRB24":
                    PHY_ULMeanNL_PRB24 = i;
                    break;
                case "PHY.ULMeanNL.PRB25":
                    PHY_ULMeanNL_PRB25 = i;
                    break;
                case "PHY.ULMeanNL.PRB26":
                    PHY_ULMeanNL_PRB26 = i;
                    break;
                case "PHY.ULMeanNL.PRB27":
                    PHY_ULMeanNL_PRB27 = i;
                    break;
                case "PHY.ULMeanNL.PRB28":
                    PHY_ULMeanNL_PRB28 = i;
                    break;
                case "PHY.ULMeanNL.PRB29":
                    PHY_ULMeanNL_PRB29 = i;
                    break;
                case "PHY.ULMeanNL.PRB30":
                    PHY_ULMeanNL_PRB30 = i;
                    break;
                case "PHY.ULMeanNL.PRB31":
                    PHY_ULMeanNL_PRB31 = i;
                    break;
                case "PHY.ULMeanNL.PRB32":
                    PHY_ULMeanNL_PRB32 = i;
                    break;
                case "PHY.ULMeanNL.PRB33":
                    PHY_ULMeanNL_PRB33 = i;
                    break;
                case "PHY.ULMeanNL.PRB34":
                    PHY_ULMeanNL_PRB34 = i;
                    break;
                case "PHY.ULMeanNL.PRB35":
                    PHY_ULMeanNL_PRB35 = i;
                    break;
                case "PHY.ULMeanNL.PRB36":
                    PHY_ULMeanNL_PRB36 = i;
                    break;
                case "PHY.ULMeanNL.PRB37":
                    PHY_ULMeanNL_PRB37 = i;
                    break;
                case "PHY.ULMeanNL.PRB38":
                    PHY_ULMeanNL_PRB38 = i;
                    break;
                case "PHY.ULMeanNL.PRB39":
                    PHY_ULMeanNL_PRB39 = i;
                    break;
                case "PHY.ULMeanNL.PRB40":
                    PHY_ULMeanNL_PRB40 = i;
                    break;
                case "PHY.ULMeanNL.PRB41":
                    PHY_ULMeanNL_PRB41 = i;
                    break;
                case "PHY.ULMeanNL.PRB42":
                    PHY_ULMeanNL_PRB42 = i;
                    break;
                case "PHY.ULMeanNL.PRB43":
                    PHY_ULMeanNL_PRB43 = i;
                    break;
                case "PHY.ULMeanNL.PRB44":
                    PHY_ULMeanNL_PRB44 = i;
                    break;
                case "PHY.ULMeanNL.PRB45":
                    PHY_ULMeanNL_PRB45 = i;
                    break;
                case "PHY.ULMeanNL.PRB46":
                    PHY_ULMeanNL_PRB46 = i;
                    break;
                case "PHY.ULMeanNL.PRB47":
                    PHY_ULMeanNL_PRB47 = i;
                    break;
                case "PHY.ULMeanNL.PRB48":
                    PHY_ULMeanNL_PRB48 = i;
                    break;
                case "PHY.ULMeanNL.PRB49":
                    PHY_ULMeanNL_PRB49 = i;
                    break;
                case "PHY.ULMeanNL.PRB50":
                    PHY_ULMeanNL_PRB50 = i;
                    break;
                case "PHY.ULMeanNL.PRB51":
                    PHY_ULMeanNL_PRB51 = i;
                    break;
                case "PHY.ULMeanNL.PRB52":
                    PHY_ULMeanNL_PRB52 = i;
                    break;
                case "PHY.ULMeanNL.PRB53":
                    PHY_ULMeanNL_PRB53 = i;
                    break;
                case "PHY.ULMeanNL.PRB54":
                    PHY_ULMeanNL_PRB54 = i;
                    break;
                case "PHY.ULMeanNL.PRB55":
                    PHY_ULMeanNL_PRB55 = i;
                    break;
                case "PHY.ULMeanNL.PRB56":
                    PHY_ULMeanNL_PRB56 = i;
                    break;
                case "PHY.ULMeanNL.PRB57":
                    PHY_ULMeanNL_PRB57 = i;
                    break;
                case "PHY.ULMeanNL.PRB58":
                    PHY_ULMeanNL_PRB58 = i;
                    break;
                case "PHY.ULMeanNL.PRB59":
                    PHY_ULMeanNL_PRB59 = i;
                    break;
                case "PHY.ULMeanNL.PRB60":
                    PHY_ULMeanNL_PRB60 = i;
                    break;
                case "PHY.ULMeanNL.PRB61":
                    PHY_ULMeanNL_PRB61 = i;
                    break;
                case "PHY.ULMeanNL.PRB62":
                    PHY_ULMeanNL_PRB62 = i;
                    break;
                case "PHY.ULMeanNL.PRB63":
                    PHY_ULMeanNL_PRB63 = i;
                    break;
                case "PHY.ULMeanNL.PRB64":
                    PHY_ULMeanNL_PRB64 = i;
                    break;
                case "PHY.ULMeanNL.PRB65":
                    PHY_ULMeanNL_PRB65 = i;
                    break;
                case "PHY.ULMeanNL.PRB66":
                    PHY_ULMeanNL_PRB66 = i;
                    break;
                case "PHY.ULMeanNL.PRB67":
                    PHY_ULMeanNL_PRB67 = i;
                    break;
                case "PHY.ULMeanNL.PRB68":
                    PHY_ULMeanNL_PRB68 = i;
                    break;
                case "PHY.ULMeanNL.PRB69":
                    PHY_ULMeanNL_PRB69 = i;
                    break;
                case "PHY.ULMeanNL.PRB70":
                    PHY_ULMeanNL_PRB70 = i;
                    break;
                case "PHY.ULMeanNL.PRB71":
                    PHY_ULMeanNL_PRB71 = i;
                    break;
                case "PHY.ULMeanNL.PRB72":
                    PHY_ULMeanNL_PRB72 = i;
                    break;
                case "PHY.ULMeanNL.PRB73":
                    PHY_ULMeanNL_PRB73 = i;
                    break;
                case "PHY.ULMeanNL.PRB74":
                    PHY_ULMeanNL_PRB74 = i;
                    break;
                case "PHY.ULMeanNL.PRB75":
                    PHY_ULMeanNL_PRB75 = i;
                    break;
                case "PHY.ULMeanNL.PRB76":
                    PHY_ULMeanNL_PRB76 = i;
                    break;
                case "PHY.ULMeanNL.PRB77":
                    PHY_ULMeanNL_PRB77 = i;
                    break;
                case "PHY.ULMeanNL.PRB78":
                    PHY_ULMeanNL_PRB78 = i;
                    break;
                case "PHY.ULMeanNL.PRB79":
                    PHY_ULMeanNL_PRB79 = i;
                    break;
                case "PHY.ULMeanNL.PRB80":
                    PHY_ULMeanNL_PRB80 = i;
                    break;
                case "PHY.ULMeanNL.PRB81":
                    PHY_ULMeanNL_PRB81 = i;
                    break;
                case "PHY.ULMeanNL.PRB82":
                    PHY_ULMeanNL_PRB82 = i;
                    break;
                case "PHY.ULMeanNL.PRB83":
                    PHY_ULMeanNL_PRB83 = i;
                    break;
                case "PHY.ULMeanNL.PRB84":
                    PHY_ULMeanNL_PRB84 = i;
                    break;
                case "PHY.ULMeanNL.PRB85":
                    PHY_ULMeanNL_PRB85 = i;
                    break;
                case "PHY.ULMeanNL.PRB86":
                    PHY_ULMeanNL_PRB86 = i;
                    break;
                case "PHY.ULMeanNL.PRB87":
                    PHY_ULMeanNL_PRB87 = i;
                    break;
                case "PHY.ULMeanNL.PRB88":
                    PHY_ULMeanNL_PRB88 = i;
                    break;
                case "PHY.ULMeanNL.PRB89":
                    PHY_ULMeanNL_PRB89 = i;
                    break;
                case "PHY.ULMeanNL.PRB90":
                    PHY_ULMeanNL_PRB90 = i;
                    break;
                case "PHY.ULMeanNL.PRB91":
                    PHY_ULMeanNL_PRB91 = i;
                    break;
                case "PHY.ULMeanNL.PRB92":
                    PHY_ULMeanNL_PRB92 = i;
                    break;
                case "PHY.ULMeanNL.PRB93":
                    PHY_ULMeanNL_PRB93 = i;
                    break;
                case "PHY.ULMeanNL.PRB94":
                    PHY_ULMeanNL_PRB94 = i;
                    break;
                case "PHY.ULMeanNL.PRB95":
                    PHY_ULMeanNL_PRB95 = i;
                    break;
                case "PHY.ULMeanNL.PRB96":
                    PHY_ULMeanNL_PRB96 = i;
                    break;
                case "PHY.ULMeanNL.PRB97":
                    PHY_ULMeanNL_PRB97 = i;
                    break;
                case "PHY.ULMeanNL.PRB98":
                    PHY_ULMeanNL_PRB98 = i;
                    break;
                case "PHY.ULMeanNL.PRB99":
                    PHY_ULMeanNL_PRB99 = i;
                    break;
                case "PHY.ULMeanNL.PRB100":
                    PHY_ULMeanNL_PRB100 = i;
                    break;
                case "PHY.ULMeanNL.PRB101":
                    PHY_ULMeanNL_PRB101 = i;
                    break;
                case "PHY.ULMeanNL.PRB102":
                    PHY_ULMeanNL_PRB102 = i;
                    break;
                case "PHY.ULMeanNL.PRB103":
                    PHY_ULMeanNL_PRB103 = i;
                    break;
                case "PHY.ULMeanNL.PRB104":
                    PHY_ULMeanNL_PRB104 = i;
                    break;
                case "PHY.ULMeanNL.PRB105":
                    PHY_ULMeanNL_PRB105 = i;
                    break;
                case "PHY.ULMeanNL.PRB106":
                    PHY_ULMeanNL_PRB106 = i;
                    break;
                case "PHY.ULMeanNL.PRB107":
                    PHY_ULMeanNL_PRB107 = i;
                    break;
                case "PHY.ULMeanNL.PRB108":
                    PHY_ULMeanNL_PRB108 = i;
                    break;
                case "PHY.ULMeanNL.PRB109":
                    PHY_ULMeanNL_PRB109 = i;
                    break;
                case "PHY.ULMeanNL.PRB110":
                    PHY_ULMeanNL_PRB110 = i;
                    break;
                case "PHY.ULMeanNL.PRB111":
                    PHY_ULMeanNL_PRB111 = i;
                    break;
                case "PHY.ULMeanNL.PRB112":
                    PHY_ULMeanNL_PRB112 = i;
                    break;
                case "PHY.ULMeanNL.PRB113":
                    PHY_ULMeanNL_PRB113 = i;
                    break;
                case "PHY.ULMeanNL.PRB114":
                    PHY_ULMeanNL_PRB114 = i;
                    break;
                case "PHY.ULMeanNL.PRB115":
                    PHY_ULMeanNL_PRB115 = i;
                    break;
                case "PHY.ULMeanNL.PRB116":
                    PHY_ULMeanNL_PRB116 = i;
                    break;
                case "PHY.ULMeanNL.PRB117":
                    PHY_ULMeanNL_PRB117 = i;
                    break;
                case "PHY.ULMeanNL.PRB118":
                    PHY_ULMeanNL_PRB118 = i;
                    break;
                case "PHY.ULMeanNL.PRB119":
                    PHY_ULMeanNL_PRB119 = i;
                    break;
                case "PHY.ULMeanNL.PRB120":
                    PHY_ULMeanNL_PRB120 = i;
                    break;
                case "PHY.ULMeanNL.PRB121":
                    PHY_ULMeanNL_PRB121 = i;
                    break;
                case "PHY.ULMeanNL.PRB122":
                    PHY_ULMeanNL_PRB122 = i;
                    break;
                case "PHY.ULMeanNL.PRB123":
                    PHY_ULMeanNL_PRB123 = i;
                    break;
                case "PHY.ULMeanNL.PRB124":
                    PHY_ULMeanNL_PRB124 = i;
                    break;
                case "PHY.ULMeanNL.PRB125":
                    PHY_ULMeanNL_PRB125 = i;
                    break;
                case "PHY.ULMeanNL.PRB126":
                    PHY_ULMeanNL_PRB126 = i;
                    break;
                case "PHY.ULMeanNL.PRB127":
                    PHY_ULMeanNL_PRB127 = i;
                    break;
                case "PHY.ULMeanNL.PRB128":
                    PHY_ULMeanNL_PRB128 = i;
                    break;
                case "PHY.ULMeanNL.PRB129":
                    PHY_ULMeanNL_PRB129 = i;
                    break;
                case "PHY.ULMeanNL.PRB130":
                    PHY_ULMeanNL_PRB130 = i;
                    break;
                case "PHY.ULMeanNL.PRB131":
                    PHY_ULMeanNL_PRB131 = i;
                    break;
                case "PHY.ULMeanNL.PRB132":
                    PHY_ULMeanNL_PRB132 = i;
                    break;
                case "PHY.ULMeanNL.PRB133":
                    PHY_ULMeanNL_PRB133 = i;
                    break;
                case "PHY.ULMeanNL.PRB134":
                    PHY_ULMeanNL_PRB134 = i;
                    break;
                case "PHY.ULMeanNL.PRB135":
                    PHY_ULMeanNL_PRB135 = i;
                    break;
                case "PHY.ULMeanNL.PRB136":
                    PHY_ULMeanNL_PRB136 = i;
                    break;
                case "PHY.ULMeanNL.PRB137":
                    PHY_ULMeanNL_PRB137 = i;
                    break;
                case "PHY.ULMeanNL.PRB138":
                    PHY_ULMeanNL_PRB138 = i;
                    break;
                case "PHY.ULMeanNL.PRB139":
                    PHY_ULMeanNL_PRB139 = i;
                    break;
                case "PHY.ULMeanNL.PRB140":
                    PHY_ULMeanNL_PRB140 = i;
                    break;
                case "PHY.ULMeanNL.PRB141":
                    PHY_ULMeanNL_PRB141 = i;
                    break;
                case "PHY.ULMeanNL.PRB142":
                    PHY_ULMeanNL_PRB142 = i;
                    break;
                case "PHY.ULMeanNL.PRB143":
                    PHY_ULMeanNL_PRB143 = i;
                    break;
                case "PHY.ULMeanNL.PRB144":
                    PHY_ULMeanNL_PRB144 = i;
                    break;
                case "PHY.ULMeanNL.PRB145":
                    PHY_ULMeanNL_PRB145 = i;
                    break;
                case "PHY.ULMeanNL.PRB146":
                    PHY_ULMeanNL_PRB146 = i;
                    break;
                case "PHY.ULMeanNL.PRB147":
                    PHY_ULMeanNL_PRB147 = i;
                    break;
                case "PHY.ULMeanNL.PRB148":
                    PHY_ULMeanNL_PRB148 = i;
                    break;
                case "PHY.ULMeanNL.PRB149":
                    PHY_ULMeanNL_PRB149 = i;
                    break;
                case "PHY.ULMeanNL.PRB150":
                    PHY_ULMeanNL_PRB150 = i;
                    break;
                case "PHY.ULMeanNL.PRB151":
                    PHY_ULMeanNL_PRB151 = i;
                    break;
                case "PHY.ULMeanNL.PRB152":
                    PHY_ULMeanNL_PRB152 = i;
                    break;
                case "PHY.ULMeanNL.PRB153":
                    PHY_ULMeanNL_PRB153 = i;
                    break;
                case "PHY.ULMeanNL.PRB154":
                    PHY_ULMeanNL_PRB154 = i;
                    break;
                case "PHY.ULMeanNL.PRB155":
                    PHY_ULMeanNL_PRB155 = i;
                    break;
                case "PHY.ULMeanNL.PRB156":
                    PHY_ULMeanNL_PRB156 = i;
                    break;
                case "PHY.ULMeanNL.PRB157":
                    PHY_ULMeanNL_PRB157 = i;
                    break;
                case "PHY.ULMeanNL.PRB158":
                    PHY_ULMeanNL_PRB158 = i;
                    break;
                case "PHY.ULMeanNL.PRB159":
                    PHY_ULMeanNL_PRB159 = i;
                    break;
                case "PHY.ULMeanNL.PRB160":
                    PHY_ULMeanNL_PRB160 = i;
                    break;
                case "PHY.ULMeanNL.PRB161":
                    PHY_ULMeanNL_PRB161 = i;
                    break;
                case "PHY.ULMeanNL.PRB162":
                    PHY_ULMeanNL_PRB162 = i;
                    break;
                case "PHY.ULMeanNL.PRB163":
                    PHY_ULMeanNL_PRB163 = i;
                    break;
                case "PHY.ULMeanNL.PRB164":
                    PHY_ULMeanNL_PRB164 = i;
                    break;
                case "PHY.ULMeanNL.PRB165":
                    PHY_ULMeanNL_PRB165 = i;
                    break;
                case "PHY.ULMeanNL.PRB166":
                    PHY_ULMeanNL_PRB166 = i;
                    break;
                case "PHY.ULMeanNL.PRB167":
                    PHY_ULMeanNL_PRB167 = i;
                    break;
                case "PHY.ULMeanNL.PRB168":
                    PHY_ULMeanNL_PRB168 = i;
                    break;
                case "PHY.ULMeanNL.PRB169":
                    PHY_ULMeanNL_PRB169 = i;
                    break;
                case "PHY.ULMeanNL.PRB170":
                    PHY_ULMeanNL_PRB170 = i;
                    break;
                case "PHY.ULMeanNL.PRB171":
                    PHY_ULMeanNL_PRB171 = i;
                    break;
                case "PHY.ULMeanNL.PRB172":
                    PHY_ULMeanNL_PRB172 = i;
                    break;
                case "PHY.ULMeanNL.PRB173":
                    PHY_ULMeanNL_PRB173 = i;
                    break;
                case "PHY.ULMeanNL.PRB174":
                    PHY_ULMeanNL_PRB174 = i;
                    break;
                case "PHY.ULMeanNL.PRB175":
                    PHY_ULMeanNL_PRB175 = i;
                    break;
                case "PHY.ULMeanNL.PRB176":
                    PHY_ULMeanNL_PRB176 = i;
                    break;
                case "PHY.ULMeanNL.PRB177":
                    PHY_ULMeanNL_PRB177 = i;
                    break;
                case "PHY.ULMeanNL.PRB178":
                    PHY_ULMeanNL_PRB178 = i;
                    break;
                case "PHY.ULMeanNL.PRB179":
                    PHY_ULMeanNL_PRB179 = i;
                    break;
                case "PHY.ULMeanNL.PRB180":
                    PHY_ULMeanNL_PRB180 = i;
                    break;
                case "PHY.ULMeanNL.PRB181":
                    PHY_ULMeanNL_PRB181 = i;
                    break;
                case "PHY.ULMeanNL.PRB182":
                    PHY_ULMeanNL_PRB182 = i;
                    break;
                case "PHY.ULMeanNL.PRB183":
                    PHY_ULMeanNL_PRB183 = i;
                    break;
                case "PHY.ULMeanNL.PRB184":
                    PHY_ULMeanNL_PRB184 = i;
                    break;
                case "PHY.ULMeanNL.PRB185":
                    PHY_ULMeanNL_PRB185 = i;
                    break;
                case "PHY.ULMeanNL.PRB186":
                    PHY_ULMeanNL_PRB186 = i;
                    break;
                case "PHY.ULMeanNL.PRB187":
                    PHY_ULMeanNL_PRB187 = i;
                    break;
                case "PHY.ULMeanNL.PRB188":
                    PHY_ULMeanNL_PRB188 = i;
                    break;
                case "PHY.ULMeanNL.PRB189":
                    PHY_ULMeanNL_PRB189 = i;
                    break;
                case "PHY.ULMeanNL.PRB190":
                    PHY_ULMeanNL_PRB190 = i;
                    break;
                case "PHY.ULMeanNL.PRB191":
                    PHY_ULMeanNL_PRB191 = i;
                    break;
                case "PHY.ULMeanNL.PRB192":
                    PHY_ULMeanNL_PRB192 = i;
                    break;
                case "PHY.ULMeanNL.PRB193":
                    PHY_ULMeanNL_PRB193 = i;
                    break;
                case "PHY.ULMeanNL.PRB194":
                    PHY_ULMeanNL_PRB194 = i;
                    break;
                case "PHY.ULMeanNL.PRB195":
                    PHY_ULMeanNL_PRB195 = i;
                    break;
                case "PHY.ULMeanNL.PRB196":
                    PHY_ULMeanNL_PRB196 = i;
                    break;
                case "PHY.ULMeanNL.PRB197":
                    PHY_ULMeanNL_PRB197 = i;
                    break;
                case "PHY.ULMeanNL.PRB198":
                    PHY_ULMeanNL_PRB198 = i;
                    break;
                case "PHY.ULMeanNL.PRB199":
                    PHY_ULMeanNL_PRB199 = i;
                    break;
                case "PHY.ULMeanNL.PRB200":
                    PHY_ULMeanNL_PRB200 = i;
                    break;
                case "PHY.ULMeanNL.PRB201":
                    PHY_ULMeanNL_PRB201 = i;
                    break;
                case "PHY.ULMeanNL.PRB202":
                    PHY_ULMeanNL_PRB202 = i;
                    break;
                case "PHY.ULMeanNL.PRB203":
                    PHY_ULMeanNL_PRB203 = i;
                    break;
                case "PHY.ULMeanNL.PRB204":
                    PHY_ULMeanNL_PRB204 = i;
                    break;
                case "PHY.ULMeanNL.PRB205":
                    PHY_ULMeanNL_PRB205 = i;
                    break;
                case "PHY.ULMeanNL.PRB206":
                    PHY_ULMeanNL_PRB206 = i;
                    break;
                case "PHY.ULMeanNL.PRB207":
                    PHY_ULMeanNL_PRB207 = i;
                    break;
                case "PHY.ULMeanNL.PRB208":
                    PHY_ULMeanNL_PRB208 = i;
                    break;
                case "PHY.ULMeanNL.PRB209":
                    PHY_ULMeanNL_PRB209 = i;
                    break;
                case "PHY.ULMeanNL.PRB210":
                    PHY_ULMeanNL_PRB210 = i;
                    break;
                case "PHY.ULMeanNL.PRB211":
                    PHY_ULMeanNL_PRB211 = i;
                    break;
                case "PHY.ULMeanNL.PRB212":
                    PHY_ULMeanNL_PRB212 = i;
                    break;
                case "PHY.ULMeanNL.PRB213":
                    PHY_ULMeanNL_PRB213 = i;
                    break;
                case "PHY.ULMeanNL.PRB214":
                    PHY_ULMeanNL_PRB214 = i;
                    break;
                case "PHY.ULMeanNL.PRB215":
                    PHY_ULMeanNL_PRB215 = i;
                    break;
                case "PHY.ULMeanNL.PRB216":
                    PHY_ULMeanNL_PRB216 = i;
                    break;
                case "PHY.ULMeanNL.PRB217":
                    PHY_ULMeanNL_PRB217 = i;
                    break;
                case "PHY.ULMeanNL.PRB218":
                    PHY_ULMeanNL_PRB218 = i;
                    break;
                case "PHY.ULMeanNL.PRB219":
                    PHY_ULMeanNL_PRB219 = i;
                    break;
                case "PHY.ULMeanNL.PRB220":
                    PHY_ULMeanNL_PRB220 = i;
                    break;
                case "PHY.ULMeanNL.PRB221":
                    PHY_ULMeanNL_PRB221 = i;
                    break;
                case "PHY.ULMeanNL.PRB222":
                    PHY_ULMeanNL_PRB222 = i;
                    break;
                case "PHY.ULMeanNL.PRB223":
                    PHY_ULMeanNL_PRB223 = i;
                    break;
                case "PHY.ULMeanNL.PRB224":
                    PHY_ULMeanNL_PRB224 = i;
                    break;
                case "PHY.ULMeanNL.PRB225":
                    PHY_ULMeanNL_PRB225 = i;
                    break;
                case "PHY.ULMeanNL.PRB226":
                    PHY_ULMeanNL_PRB226 = i;
                    break;
                case "PHY.ULMeanNL.PRB227":
                    PHY_ULMeanNL_PRB227 = i;
                    break;
                case "PHY.ULMeanNL.PRB228":
                    PHY_ULMeanNL_PRB228 = i;
                    break;
                case "PHY.ULMeanNL.PRB229":
                    PHY_ULMeanNL_PRB229 = i;
                    break;
                case "PHY.ULMeanNL.PRB230":
                    PHY_ULMeanNL_PRB230 = i;
                    break;
                case "PHY.ULMeanNL.PRB231":
                    PHY_ULMeanNL_PRB231 = i;
                    break;
                case "PHY.ULMeanNL.PRB232":
                    PHY_ULMeanNL_PRB232 = i;
                    break;
                case "PHY.ULMeanNL.PRB233":
                    PHY_ULMeanNL_PRB233 = i;
                    break;
                case "PHY.ULMeanNL.PRB234":
                    PHY_ULMeanNL_PRB234 = i;
                    break;
                case "PHY.ULMeanNL.PRB235":
                    PHY_ULMeanNL_PRB235 = i;
                    break;
                case "PHY.ULMeanNL.PRB236":
                    PHY_ULMeanNL_PRB236 = i;
                    break;
                case "PHY.ULMeanNL.PRB237":
                    PHY_ULMeanNL_PRB237 = i;
                    break;
                case "PHY.ULMeanNL.PRB238":
                    PHY_ULMeanNL_PRB238 = i;
                    break;
                case "PHY.ULMeanNL.PRB239":
                    PHY_ULMeanNL_PRB239 = i;
                    break;
                case "PHY.ULMeanNL.PRB240":
                    PHY_ULMeanNL_PRB240 = i;
                    break;
                case "PHY.ULMeanNL.PRB241":
                    PHY_ULMeanNL_PRB241 = i;
                    break;
                case "PHY.ULMeanNL.PRB242":
                    PHY_ULMeanNL_PRB242 = i;
                    break;
                case "PHY.ULMeanNL.PRB243":
                    PHY_ULMeanNL_PRB243 = i;
                    break;
                case "PHY.ULMeanNL.PRB244":
                    PHY_ULMeanNL_PRB244 = i;
                    break;
                case "PHY.ULMeanNL.PRB245":
                    PHY_ULMeanNL_PRB245 = i;
                    break;
                case "PHY.ULMeanNL.PRB246":
                    PHY_ULMeanNL_PRB246 = i;
                    break;
                case "PHY.ULMeanNL.PRB247":
                    PHY_ULMeanNL_PRB247 = i;
                    break;
                case "PHY.ULMeanNL.PRB248":
                    PHY_ULMeanNL_PRB248 = i;
                    break;
                case "PHY.ULMeanNL.PRB249":
                    PHY_ULMeanNL_PRB249 = i;
                    break;
                case "PHY.ULMeanNL.PRB250":
                    PHY_ULMeanNL_PRB250 = i;
                    break;
                case "PHY.ULMeanNL.PRB251":
                    PHY_ULMeanNL_PRB251 = i;
                    break;
                case "PHY.ULMeanNL.PRB252":
                    PHY_ULMeanNL_PRB252 = i;
                    break;
                case "PHY.ULMeanNL.PRB253":
                    PHY_ULMeanNL_PRB253 = i;
                    break;
                case "PHY.ULMeanNL.PRB254":
                    PHY_ULMeanNL_PRB254 = i;
                    break;
                case "PHY.ULMeanNL.PRB255":
                    PHY_ULMeanNL_PRB255 = i;
                    break;
                case "PHY.ULMeanNL.PRB256":
                    PHY_ULMeanNL_PRB256 = i;
                    break;
                case "PHY.ULMeanNL.PRB257":
                    PHY_ULMeanNL_PRB257 = i;
                    break;
                case "PHY.ULMeanNL.PRB258":
                    PHY_ULMeanNL_PRB258 = i;
                    break;
                case "PHY.ULMeanNL.PRB259":
                    PHY_ULMeanNL_PRB259 = i;
                    break;
                case "PHY.ULMeanNL.PRB260":
                    PHY_ULMeanNL_PRB260 = i;
                    break;
                case "PHY.ULMeanNL.PRB261":
                    PHY_ULMeanNL_PRB261 = i;
                    break;
                case "PHY.ULMeanNL.PRB262":
                    PHY_ULMeanNL_PRB262 = i;
                    break;
                case "PHY.ULMeanNL.PRB263":
                    PHY_ULMeanNL_PRB263 = i;
                    break;
                case "PHY.ULMeanNL.PRB264":
                    PHY_ULMeanNL_PRB264 = i;
                    break;
                case "PHY.ULMeanNL.PRB265":
                    PHY_ULMeanNL_PRB265 = i;
                    break;
                case "PHY.ULMeanNL.PRB266":
                    PHY_ULMeanNL_PRB266 = i;
                    break;
                case "PHY.ULMeanNL.PRB267":
                    PHY_ULMeanNL_PRB267 = i;
                    break;
                case "PHY.ULMeanNL.PRB268":
                    PHY_ULMeanNL_PRB268 = i;
                    break;
                case "PHY.ULMeanNL.PRB269":
                    PHY_ULMeanNL_PRB269 = i;
                    break;
                case "PHY.ULMeanNL.PRB270":
                    PHY_ULMeanNL_PRB270 = i;
                    break;
                case "PHY.ULMeanNL.PRB271":
                    PHY_ULMeanNL_PRB271 = i;
                    break;
                case "PHY.ULMeanNL.PRB272":
                    PHY_ULMeanNL_PRB272 = i;
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName() + "...开始...");
        Properties properties = SOURCE_PRO;
        String source = sourceData.getKey().split("￥")[0];
        if (properties.get(source + ".user") == null) {
            logger.warn("SFTP连接参数错误,请检查sourceConfig配置文件!!!");
            threadsSignal.countDown();
            logger.info(Thread.currentThread().getName() + "结束. 还有"
                    + threadsSignal.getCount() + " 个线程");
            return;
        }
        ChannelSftp sftp = SftpUtilM.login(properties.get(source + ".user").toString(),
                properties.get(source + ".password").toString(), source, 22);
        if (sftp == null) {
            logger.warn("SFTP登陆失败，请检查网络和连接参数");
        } else {
            logger.info("SFTP登陆成功耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String path;
            String fileName;
            String str = null;
            long tttt = System.currentTimeMillis();
            if ("1".equals(TestModel)) {
                ymd=TestDirNameYmDH;
                nms=TestFileNameMMss;
                logger.info("获取：" + ymd + "/PM-GNB-SA-NRCELLDU-" +
                        properties.get(source + ".id") + "-*-" + ymd + nms + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                ymd + "/PM-GNB-SA-NRCELLDU-" +
                                properties.get(source + ".id") + "-*-" + ymd + nms + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }
                }
                if ("[]".equals(str) || str == null) {
                    logger.warn("文件不存在,exit");
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-GNB-SA-NRCELLDU-" + properties.get(source + ".id") +
                            "-" + verSion + "-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv";
                    logger.info("测试模式 文件名：" + fileName);
                    path = saveFilePath + TestDirNameYmDH + TestFileNameMMss + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + TestDirNameYmDH,
                            fileName + ".gz", path + fileName + ".gz");
                }
            } else {
                ymd=nowTime;
                nms=TimeMm;
                logger.info("获取：" + ymd + "/PM-GNB-SA-NRCELLDU-" +
                        properties.get(source + ".id") + "-*-" + ymd + nms + "-15.csv.gz");
                for (int t1 = 1; t1 <= ForCount; t1++) {
                    try {
                        str = SftpUtilM.listFiles(sftp, properties.get(source + ".path") + "/" +
                                ymd + "/PM-GNB-SA-NRCELLDU-" +
                                properties.get(source + ".id") + "-*-" + ymd + nms + "-15.csv.gz").toString();
                    } catch (SftpException e) {
                        e.printStackTrace();
                        logger.error("SFTP操作异常：" + e.getMessage());
                    }
                    if (!"[]".equals(str) && str != null) {
                        break;
                    } else {
                        logger.info(Thread.currentThread().getName() + "获取不到该时段文件，" +
                                SleepTime + " (秒) 后继续查询");
                        try {
                            Thread.sleep(SleepTime * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("线程异常：" + e.getMessage());
                        }
                    }

                }

                if ("[]".equals(str) || str == null) {
                    logger.warn("文件不存在,exit");
                    threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
                    logger.info(Thread.currentThread().getName() + "结束. 还有"
                            + threadsSignal.getCount() + " 个线程");
                    return;
                } else {
                    String verSion = str.substring(str.indexOf("V"), str.indexOf("V") + 6);
                    fileName = "PM-GNB-SA-NRCELLDU-" + properties.get(source + ".id") + "-" +
                            verSion + "-" + nowTime + TimeMm + "-15.csv";
                    logger.info("正常模式 文件名：" + fileName);
                    path = saveFilePath + nowTime + TimeMm + "_" + source + "_" + tttt + File.separator;
                    isChartPathExist(path);
                    logger.info("========开始下载文件========");
                    SftpUtilM.download(sftp, properties.get(source + ".path") + "/" + nowTime,
                            fileName + ".gz", path + fileName + ".gz");
                    logger.info("========开始删除文件========");
                    SftpUtilM.delete(sftp, properties.get(source + ".path") + "/" + nowTime, fileName + ".gz");
                }
            }
            UnCompressFileGZIP.doUncompressFile(path + fileName + ".gz");
            isChartPathExist(path + "Write" + File.separator);
            File y1 = new File(path + fileName + ".gz");
            logger.info(fileName + ".gz " + "原文件大小：" + y1.length());
            csvRun(path + fileName, path + "Write" + File.separator + fileName,
                    sourceData.getValue());
            logger.info("文件处理耗时：" + (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
            String fileLocal = path + "Write" + File.separator + fileName;
            CompressFileGZIP.doCompressFile(fileLocal);
            File file = new File(fileLocal + ".gz");
            logger.info(fileName + ".gz " + "修改后本地文件大小：" + file.length());
            try {
                InputStream is = new FileInputStream(file);
                String uploadPath;
                if ("1".equals(TestModel)) {
                    uploadPath = properties.get(source + ".path") + "/" + TestDirNameYmDH;
                } else {
                    uploadPath = properties.get(source + ".path") + "/" + nowTime;
                }
                SftpUtilM.upload(sftp, "/", uploadPath, fileName + ".gz", is);
                logger.info("=======" + source + "上传成功=======");

                Connection connection = RemoteShellExecutor.login(source, properties.get(source + ".user").toString(),
                        properties.get(source + ".password").toString());

                if (connection == null) {
                    logger.error("=======" + source + "SSH connection error=======");
                } else {
                    try {
                        RemoteShellExecutor.exec(connection, "ls -l " + uploadPath + "/" + fileName + ".gz");
                        logger.info("执行 chmod 750 " + uploadPath + "/" + fileName + ".gz");
                        RemoteShellExecutor.exec(connection, "chmod 750 " + uploadPath + "/" + fileName + ".gz");
                        logger.info("================alert===============");
                        RemoteShellExecutor.exec(connection, "ls -l " + uploadPath + "/" + fileName + ".gz");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                long dalen;
                if ("1".equals(TestModel)) {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            TestDirNameYmDH + "/PM-GNB-SA-NRCELLDU-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz").getSize();
                    logger.info("PM-GNB-SA-NRCELLDU-" +
                            properties.get(source + ".id") + "-*-" + TestDirNameYmDH + TestFileNameMMss + "-15.csv.gz  修改后FTP文件大小为：" + dalen);
                } else {
                    dalen = SftpUtilM.listFiles1(sftp, properties.get(source + ".path") + "/" +
                            nowTime + "/PM-GNB-SA-NRCELLDU-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz").getSize();
                    logger.info("PM-GNB-SA-NRCELLDU-" +
                            properties.get(source + ".id") + "-*-" + nowTime + TimeMm + "-15.csv.gz  修改后FTP文件大小为：" + dalen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件操作异常：" + e.getMessage());
            } catch (SftpException e) {
                e.printStackTrace();
                logger.error("SFTP操作异常：" + e.getMessage());
            }
        }

        // 线程结束时计数器减1
        threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
        logger.info(Thread.currentThread().getName() + "结束. 还有"
                + threadsSignal.getCount() + " 个线程 耗时：" +
                (System.currentTimeMillis() - startTime) / 1000 + " (秒)");
    }

    private void csvRun(String readerPath, String writePath, List<String> value) {
        try {
            CsvReader reader = new CsvReader(readerPath, '|', StandardCharsets.UTF_8);
            boolean isFirst = true;
            String[] stringList;
            reader.readHeaders();
            //读取表头 TimeStamp
            String[] title = reader.getHeaders();
            CsvWriter writerTemp = new CsvWriter(writePath, '|', StandardCharsets.UTF_8);
            writerTemp.setUseTextQualifier(false);
            writerTemp.writeRecord(title);
            // 读取每行的内容
            while (reader.readRecord()) {
                if (isFirst) {
                    isFirst = false;
                    //读取列头 rmUID
                    writerTemp.writeRecord(reader.getValues());
                    initDataPosition(reader);
                } else {
                    //每一行的值
                    stringList = reader.getValues();
                    stringList[2] = "\"" + stringList[2] + "\"";
                    if (value.toString().contains(reader.get(Integer.parseInt(Const.aimsType)))) {
                        for (String li : value) {
                            if (reader.get(Integer.parseInt(Const.aimsType)).equals(li.split("￥")[1])) {
                                int on4 = Integer.parseInt(li.split("￥")[5]);
                                try {
                                    if (on4 == 1) {
                                        for (int i = 0; i <= 272; i++) {
                                            if (!reader.get(getPHY_ULMeanNL_PRBPosition(i)).isEmpty() && Integer.parseInt(reader.get(getPHY_ULMeanNL_PRBPosition(i))) > -110) {
                                                logger.info("PHY_ULMeanNL_PRB指标修正 当前:" + reader.get(2));
                                                logger.info("PHY_ULMeanNL_PRB指标 当前:" + reader.get(getPHY_ULMeanNL_PRBPosition(i)));
                                                stringList[getPHY_ULMeanNL_PRBPosition(i)] = String.valueOf((reader.get(2).length()+i+Integer.parseInt(ymd)+(Integer.parseInt(nms.substring(0,2))/15))% 5-117);
                                                logger.info("PHY_ULMeanNL_PRB指标 修改后:" + stringList[getPHY_ULMeanNL_PRBPosition(i)]);
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("aims: " + reader.get(2) + "\n源数据异常: " + e.getMessage());
                                }
                                break;
                            }
                        }
                    }
                    writerTemp.writeRecord(stringList);
                }
            }
            reader.close();
            // 关闭Writer
            writerTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("操作异常：" + e.getMessage());
        }
    }

    private int getPHY_ULMeanNL_PRBPosition(int index) {
        switch (index) {
            case 0:
                return PHY_ULMeanNL_PRB0;
            case 1:
                return PHY_ULMeanNL_PRB1;
            case 2:
                return PHY_ULMeanNL_PRB2;
            case 3:
                return PHY_ULMeanNL_PRB3;
            case 4:
                return PHY_ULMeanNL_PRB4;
            case 5:
                return PHY_ULMeanNL_PRB5;
            case 6:
                return PHY_ULMeanNL_PRB6;
            case 7:
                return PHY_ULMeanNL_PRB7;
            case 8:
                return PHY_ULMeanNL_PRB8;
            case 9:
                return PHY_ULMeanNL_PRB9;
            case 10:
                return PHY_ULMeanNL_PRB10;
            case 11:
                return PHY_ULMeanNL_PRB11;
            case 12:
                return PHY_ULMeanNL_PRB12;
            case 13:
                return PHY_ULMeanNL_PRB13;
            case 14:
                return PHY_ULMeanNL_PRB14;
            case 15:
                return PHY_ULMeanNL_PRB15;
            case 16:
                return PHY_ULMeanNL_PRB16;
            case 17:
                return PHY_ULMeanNL_PRB17;
            case 18:
                return PHY_ULMeanNL_PRB18;
            case 19:
                return PHY_ULMeanNL_PRB19;
            case 20:
                return PHY_ULMeanNL_PRB20;
            case 21:
                return PHY_ULMeanNL_PRB21;
            case 22:
                return PHY_ULMeanNL_PRB22;
            case 23:
                return PHY_ULMeanNL_PRB23;
            case 24:
                return PHY_ULMeanNL_PRB24;
            case 25:
                return PHY_ULMeanNL_PRB25;
            case 26:
                return PHY_ULMeanNL_PRB26;
            case 27:
                return PHY_ULMeanNL_PRB27;
            case 28:
                return PHY_ULMeanNL_PRB28;
            case 29:
                return PHY_ULMeanNL_PRB29;
            case 30:
                return PHY_ULMeanNL_PRB30;
            case 31:
                return PHY_ULMeanNL_PRB31;
            case 32:
                return PHY_ULMeanNL_PRB32;
            case 33:
                return PHY_ULMeanNL_PRB33;
            case 34:
                return PHY_ULMeanNL_PRB34;
            case 35:
                return PHY_ULMeanNL_PRB35;
            case 36:
                return PHY_ULMeanNL_PRB36;
            case 37:
                return PHY_ULMeanNL_PRB37;
            case 38:
                return PHY_ULMeanNL_PRB38;
            case 39:
                return PHY_ULMeanNL_PRB39;
            case 40:
                return PHY_ULMeanNL_PRB40;
            case 41:
                return PHY_ULMeanNL_PRB41;
            case 42:
                return PHY_ULMeanNL_PRB42;
            case 43:
                return PHY_ULMeanNL_PRB43;
            case 44:
                return PHY_ULMeanNL_PRB44;
            case 45:
                return PHY_ULMeanNL_PRB45;
            case 46:
                return PHY_ULMeanNL_PRB46;
            case 47:
                return PHY_ULMeanNL_PRB47;
            case 48:
                return PHY_ULMeanNL_PRB48;
            case 49:
                return PHY_ULMeanNL_PRB49;
            case 50:
                return PHY_ULMeanNL_PRB50;
            case 51:
                return PHY_ULMeanNL_PRB51;
            case 52:
                return PHY_ULMeanNL_PRB52;
            case 53:
                return PHY_ULMeanNL_PRB53;
            case 54:
                return PHY_ULMeanNL_PRB54;
            case 55:
                return PHY_ULMeanNL_PRB55;
            case 56:
                return PHY_ULMeanNL_PRB56;
            case 57:
                return PHY_ULMeanNL_PRB57;
            case 58:
                return PHY_ULMeanNL_PRB58;
            case 59:
                return PHY_ULMeanNL_PRB59;
            case 60:
                return PHY_ULMeanNL_PRB60;
            case 61:
                return PHY_ULMeanNL_PRB61;
            case 62:
                return PHY_ULMeanNL_PRB62;
            case 63:
                return PHY_ULMeanNL_PRB63;
            case 64:
                return PHY_ULMeanNL_PRB64;
            case 65:
                return PHY_ULMeanNL_PRB65;
            case 66:
                return PHY_ULMeanNL_PRB66;
            case 67:
                return PHY_ULMeanNL_PRB67;
            case 68:
                return PHY_ULMeanNL_PRB68;
            case 69:
                return PHY_ULMeanNL_PRB69;
            case 70:
                return PHY_ULMeanNL_PRB70;
            case 71:
                return PHY_ULMeanNL_PRB71;
            case 72:
                return PHY_ULMeanNL_PRB72;
            case 73:
                return PHY_ULMeanNL_PRB73;
            case 74:
                return PHY_ULMeanNL_PRB74;
            case 75:
                return PHY_ULMeanNL_PRB75;
            case 76:
                return PHY_ULMeanNL_PRB76;
            case 77:
                return PHY_ULMeanNL_PRB77;
            case 78:
                return PHY_ULMeanNL_PRB78;
            case 79:
                return PHY_ULMeanNL_PRB79;
            case 80:
                return PHY_ULMeanNL_PRB80;
            case 81:
                return PHY_ULMeanNL_PRB81;
            case 82:
                return PHY_ULMeanNL_PRB82;
            case 83:
                return PHY_ULMeanNL_PRB83;
            case 84:
                return PHY_ULMeanNL_PRB84;
            case 85:
                return PHY_ULMeanNL_PRB85;
            case 86:
                return PHY_ULMeanNL_PRB86;
            case 87:
                return PHY_ULMeanNL_PRB87;
            case 88:
                return PHY_ULMeanNL_PRB88;
            case 89:
                return PHY_ULMeanNL_PRB89;
            case 90:
                return PHY_ULMeanNL_PRB90;
            case 91:
                return PHY_ULMeanNL_PRB91;
            case 92:
                return PHY_ULMeanNL_PRB92;
            case 93:
                return PHY_ULMeanNL_PRB93;
            case 94:
                return PHY_ULMeanNL_PRB94;
            case 95:
                return PHY_ULMeanNL_PRB95;
            case 96:
                return PHY_ULMeanNL_PRB96;
            case 97:
                return PHY_ULMeanNL_PRB97;
            case 98:
                return PHY_ULMeanNL_PRB98;
            case 99:
                return PHY_ULMeanNL_PRB99;
            case 100:
                return PHY_ULMeanNL_PRB100;
            case 101:
                return PHY_ULMeanNL_PRB101;
            case 102:
                return PHY_ULMeanNL_PRB102;
            case 103:
                return PHY_ULMeanNL_PRB103;
            case 104:
                return PHY_ULMeanNL_PRB104;
            case 105:
                return PHY_ULMeanNL_PRB105;
            case 106:
                return PHY_ULMeanNL_PRB106;
            case 107:
                return PHY_ULMeanNL_PRB107;
            case 108:
                return PHY_ULMeanNL_PRB108;
            case 109:
                return PHY_ULMeanNL_PRB109;
            case 110:
                return PHY_ULMeanNL_PRB110;
            case 111:
                return PHY_ULMeanNL_PRB111;
            case 112:
                return PHY_ULMeanNL_PRB112;
            case 113:
                return PHY_ULMeanNL_PRB113;
            case 114:
                return PHY_ULMeanNL_PRB114;
            case 115:
                return PHY_ULMeanNL_PRB115;
            case 116:
                return PHY_ULMeanNL_PRB116;
            case 117:
                return PHY_ULMeanNL_PRB117;
            case 118:
                return PHY_ULMeanNL_PRB118;
            case 119:
                return PHY_ULMeanNL_PRB119;
            case 120:
                return PHY_ULMeanNL_PRB120;
            case 121:
                return PHY_ULMeanNL_PRB121;
            case 122:
                return PHY_ULMeanNL_PRB122;
            case 123:
                return PHY_ULMeanNL_PRB123;
            case 124:
                return PHY_ULMeanNL_PRB124;
            case 125:
                return PHY_ULMeanNL_PRB125;
            case 126:
                return PHY_ULMeanNL_PRB126;
            case 127:
                return PHY_ULMeanNL_PRB127;
            case 128:
                return PHY_ULMeanNL_PRB128;
            case 129:
                return PHY_ULMeanNL_PRB129;
            case 130:
                return PHY_ULMeanNL_PRB130;
            case 131:
                return PHY_ULMeanNL_PRB131;
            case 132:
                return PHY_ULMeanNL_PRB132;
            case 133:
                return PHY_ULMeanNL_PRB133;
            case 134:
                return PHY_ULMeanNL_PRB134;
            case 135:
                return PHY_ULMeanNL_PRB135;
            case 136:
                return PHY_ULMeanNL_PRB136;
            case 137:
                return PHY_ULMeanNL_PRB137;
            case 138:
                return PHY_ULMeanNL_PRB138;
            case 139:
                return PHY_ULMeanNL_PRB139;
            case 140:
                return PHY_ULMeanNL_PRB140;
            case 141:
                return PHY_ULMeanNL_PRB141;
            case 142:
                return PHY_ULMeanNL_PRB142;
            case 143:
                return PHY_ULMeanNL_PRB143;
            case 144:
                return PHY_ULMeanNL_PRB144;
            case 145:
                return PHY_ULMeanNL_PRB145;
            case 146:
                return PHY_ULMeanNL_PRB146;
            case 147:
                return PHY_ULMeanNL_PRB147;
            case 148:
                return PHY_ULMeanNL_PRB148;
            case 149:
                return PHY_ULMeanNL_PRB149;
            case 150:
                return PHY_ULMeanNL_PRB150;
            case 151:
                return PHY_ULMeanNL_PRB151;
            case 152:
                return PHY_ULMeanNL_PRB152;
            case 153:
                return PHY_ULMeanNL_PRB153;
            case 154:
                return PHY_ULMeanNL_PRB154;
            case 155:
                return PHY_ULMeanNL_PRB155;
            case 156:
                return PHY_ULMeanNL_PRB156;
            case 157:
                return PHY_ULMeanNL_PRB157;
            case 158:
                return PHY_ULMeanNL_PRB158;
            case 159:
                return PHY_ULMeanNL_PRB159;
            case 160:
                return PHY_ULMeanNL_PRB160;
            case 161:
                return PHY_ULMeanNL_PRB161;
            case 162:
                return PHY_ULMeanNL_PRB162;
            case 163:
                return PHY_ULMeanNL_PRB163;
            case 164:
                return PHY_ULMeanNL_PRB164;
            case 165:
                return PHY_ULMeanNL_PRB165;
            case 166:
                return PHY_ULMeanNL_PRB166;
            case 167:
                return PHY_ULMeanNL_PRB167;
            case 168:
                return PHY_ULMeanNL_PRB168;
            case 169:
                return PHY_ULMeanNL_PRB169;
            case 170:
                return PHY_ULMeanNL_PRB170;
            case 171:
                return PHY_ULMeanNL_PRB171;
            case 172:
                return PHY_ULMeanNL_PRB172;
            case 173:
                return PHY_ULMeanNL_PRB173;
            case 174:
                return PHY_ULMeanNL_PRB174;
            case 175:
                return PHY_ULMeanNL_PRB175;
            case 176:
                return PHY_ULMeanNL_PRB176;
            case 177:
                return PHY_ULMeanNL_PRB177;
            case 178:
                return PHY_ULMeanNL_PRB178;
            case 179:
                return PHY_ULMeanNL_PRB179;
            case 180:
                return PHY_ULMeanNL_PRB180;
            case 181:
                return PHY_ULMeanNL_PRB181;
            case 182:
                return PHY_ULMeanNL_PRB182;
            case 183:
                return PHY_ULMeanNL_PRB183;
            case 184:
                return PHY_ULMeanNL_PRB184;
            case 185:
                return PHY_ULMeanNL_PRB185;
            case 186:
                return PHY_ULMeanNL_PRB186;
            case 187:
                return PHY_ULMeanNL_PRB187;
            case 188:
                return PHY_ULMeanNL_PRB188;
            case 189:
                return PHY_ULMeanNL_PRB189;
            case 190:
                return PHY_ULMeanNL_PRB190;
            case 191:
                return PHY_ULMeanNL_PRB191;
            case 192:
                return PHY_ULMeanNL_PRB192;
            case 193:
                return PHY_ULMeanNL_PRB193;
            case 194:
                return PHY_ULMeanNL_PRB194;
            case 195:
                return PHY_ULMeanNL_PRB195;
            case 196:
                return PHY_ULMeanNL_PRB196;
            case 197:
                return PHY_ULMeanNL_PRB197;
            case 198:
                return PHY_ULMeanNL_PRB198;
            case 199:
                return PHY_ULMeanNL_PRB199;
            case 200:
                return PHY_ULMeanNL_PRB200;
            case 201:
                return PHY_ULMeanNL_PRB201;
            case 202:
                return PHY_ULMeanNL_PRB202;
            case 203:
                return PHY_ULMeanNL_PRB203;
            case 204:
                return PHY_ULMeanNL_PRB204;
            case 205:
                return PHY_ULMeanNL_PRB205;
            case 206:
                return PHY_ULMeanNL_PRB206;
            case 207:
                return PHY_ULMeanNL_PRB207;
            case 208:
                return PHY_ULMeanNL_PRB208;
            case 209:
                return PHY_ULMeanNL_PRB209;
            case 210:
                return PHY_ULMeanNL_PRB210;
            case 211:
                return PHY_ULMeanNL_PRB211;
            case 212:
                return PHY_ULMeanNL_PRB212;
            case 213:
                return PHY_ULMeanNL_PRB213;
            case 214:
                return PHY_ULMeanNL_PRB214;
            case 215:
                return PHY_ULMeanNL_PRB215;
            case 216:
                return PHY_ULMeanNL_PRB216;
            case 217:
                return PHY_ULMeanNL_PRB217;
            case 218:
                return PHY_ULMeanNL_PRB218;
            case 219:
                return PHY_ULMeanNL_PRB219;
            case 220:
                return PHY_ULMeanNL_PRB220;
            case 221:
                return PHY_ULMeanNL_PRB221;
            case 222:
                return PHY_ULMeanNL_PRB222;
            case 223:
                return PHY_ULMeanNL_PRB223;
            case 224:
                return PHY_ULMeanNL_PRB224;
            case 225:
                return PHY_ULMeanNL_PRB225;
            case 226:
                return PHY_ULMeanNL_PRB226;
            case 227:
                return PHY_ULMeanNL_PRB227;
            case 228:
                return PHY_ULMeanNL_PRB228;
            case 229:
                return PHY_ULMeanNL_PRB229;
            case 230:
                return PHY_ULMeanNL_PRB230;
            case 231:
                return PHY_ULMeanNL_PRB231;
            case 232:
                return PHY_ULMeanNL_PRB232;
            case 233:
                return PHY_ULMeanNL_PRB233;
            case 234:
                return PHY_ULMeanNL_PRB234;
            case 235:
                return PHY_ULMeanNL_PRB235;
            case 236:
                return PHY_ULMeanNL_PRB236;
            case 237:
                return PHY_ULMeanNL_PRB237;
            case 238:
                return PHY_ULMeanNL_PRB238;
            case 239:
                return PHY_ULMeanNL_PRB239;
            case 240:
                return PHY_ULMeanNL_PRB240;
            case 241:
                return PHY_ULMeanNL_PRB241;
            case 242:
                return PHY_ULMeanNL_PRB242;
            case 243:
                return PHY_ULMeanNL_PRB243;
            case 244:
                return PHY_ULMeanNL_PRB244;
            case 245:
                return PHY_ULMeanNL_PRB245;
            case 246:
                return PHY_ULMeanNL_PRB246;
            case 247:
                return PHY_ULMeanNL_PRB247;
            case 248:
                return PHY_ULMeanNL_PRB248;
            case 249:
                return PHY_ULMeanNL_PRB249;
            case 250:
                return PHY_ULMeanNL_PRB250;
            case 251:
                return PHY_ULMeanNL_PRB251;
            case 252:
                return PHY_ULMeanNL_PRB252;
            case 253:
                return PHY_ULMeanNL_PRB253;
            case 254:
                return PHY_ULMeanNL_PRB254;
            case 255:
                return PHY_ULMeanNL_PRB255;
            case 256:
                return PHY_ULMeanNL_PRB256;
            case 257:
                return PHY_ULMeanNL_PRB257;
            case 258:
                return PHY_ULMeanNL_PRB258;
            case 259:
                return PHY_ULMeanNL_PRB259;
            case 260:
                return PHY_ULMeanNL_PRB260;
            case 261:
                return PHY_ULMeanNL_PRB261;
            case 262:
                return PHY_ULMeanNL_PRB262;
            case 263:
                return PHY_ULMeanNL_PRB263;
            case 264:
                return PHY_ULMeanNL_PRB264;
            case 265:
                return PHY_ULMeanNL_PRB265;
            case 266:
                return PHY_ULMeanNL_PRB266;
            case 267:
                return PHY_ULMeanNL_PRB267;
            case 268:
                return PHY_ULMeanNL_PRB268;
            case 269:
                return PHY_ULMeanNL_PRB269;
            case 270:
                return PHY_ULMeanNL_PRB270;
            case 271:
                return PHY_ULMeanNL_PRB271;
            case 272:
                return PHY_ULMeanNL_PRB272;
            default:
                return 0;
        }
    }

    private static int PHY_ULMeanNL_PRB0 = 0;
    private static int PHY_ULMeanNL_PRB1 = 0;
    private static int PHY_ULMeanNL_PRB2 = 0;
    private static int PHY_ULMeanNL_PRB3 = 0;
    private static int PHY_ULMeanNL_PRB4 = 0;
    private static int PHY_ULMeanNL_PRB5 = 0;
    private static int PHY_ULMeanNL_PRB6 = 0;
    private static int PHY_ULMeanNL_PRB7 = 0;
    private static int PHY_ULMeanNL_PRB8 = 0;
    private static int PHY_ULMeanNL_PRB9 = 0;
    private static int PHY_ULMeanNL_PRB10 = 0;
    private static int PHY_ULMeanNL_PRB11 = 0;
    private static int PHY_ULMeanNL_PRB12 = 0;
    private static int PHY_ULMeanNL_PRB13 = 0;
    private static int PHY_ULMeanNL_PRB14 = 0;
    private static int PHY_ULMeanNL_PRB15 = 0;
    private static int PHY_ULMeanNL_PRB16 = 0;
    private static int PHY_ULMeanNL_PRB17 = 0;
    private static int PHY_ULMeanNL_PRB18 = 0;
    private static int PHY_ULMeanNL_PRB19 = 0;
    private static int PHY_ULMeanNL_PRB20 = 0;
    private static int PHY_ULMeanNL_PRB21 = 0;
    private static int PHY_ULMeanNL_PRB22 = 0;
    private static int PHY_ULMeanNL_PRB23 = 0;
    private static int PHY_ULMeanNL_PRB24 = 0;
    private static int PHY_ULMeanNL_PRB25 = 0;
    private static int PHY_ULMeanNL_PRB26 = 0;
    private static int PHY_ULMeanNL_PRB27 = 0;
    private static int PHY_ULMeanNL_PRB28 = 0;
    private static int PHY_ULMeanNL_PRB29 = 0;
    private static int PHY_ULMeanNL_PRB30 = 0;
    private static int PHY_ULMeanNL_PRB31 = 0;
    private static int PHY_ULMeanNL_PRB32 = 0;
    private static int PHY_ULMeanNL_PRB33 = 0;
    private static int PHY_ULMeanNL_PRB34 = 0;
    private static int PHY_ULMeanNL_PRB35 = 0;
    private static int PHY_ULMeanNL_PRB36 = 0;
    private static int PHY_ULMeanNL_PRB37 = 0;
    private static int PHY_ULMeanNL_PRB38 = 0;
    private static int PHY_ULMeanNL_PRB39 = 0;
    private static int PHY_ULMeanNL_PRB40 = 0;
    private static int PHY_ULMeanNL_PRB41 = 0;
    private static int PHY_ULMeanNL_PRB42 = 0;
    private static int PHY_ULMeanNL_PRB43 = 0;
    private static int PHY_ULMeanNL_PRB44 = 0;
    private static int PHY_ULMeanNL_PRB45 = 0;
    private static int PHY_ULMeanNL_PRB46 = 0;
    private static int PHY_ULMeanNL_PRB47 = 0;
    private static int PHY_ULMeanNL_PRB48 = 0;
    private static int PHY_ULMeanNL_PRB49 = 0;
    private static int PHY_ULMeanNL_PRB50 = 0;
    private static int PHY_ULMeanNL_PRB51 = 0;
    private static int PHY_ULMeanNL_PRB52 = 0;
    private static int PHY_ULMeanNL_PRB53 = 0;
    private static int PHY_ULMeanNL_PRB54 = 0;
    private static int PHY_ULMeanNL_PRB55 = 0;
    private static int PHY_ULMeanNL_PRB56 = 0;
    private static int PHY_ULMeanNL_PRB57 = 0;
    private static int PHY_ULMeanNL_PRB58 = 0;
    private static int PHY_ULMeanNL_PRB59 = 0;
    private static int PHY_ULMeanNL_PRB60 = 0;
    private static int PHY_ULMeanNL_PRB61 = 0;
    private static int PHY_ULMeanNL_PRB62 = 0;
    private static int PHY_ULMeanNL_PRB63 = 0;
    private static int PHY_ULMeanNL_PRB64 = 0;
    private static int PHY_ULMeanNL_PRB65 = 0;
    private static int PHY_ULMeanNL_PRB66 = 0;
    private static int PHY_ULMeanNL_PRB67 = 0;
    private static int PHY_ULMeanNL_PRB68 = 0;
    private static int PHY_ULMeanNL_PRB69 = 0;
    private static int PHY_ULMeanNL_PRB70 = 0;
    private static int PHY_ULMeanNL_PRB71 = 0;
    private static int PHY_ULMeanNL_PRB72 = 0;
    private static int PHY_ULMeanNL_PRB73 = 0;
    private static int PHY_ULMeanNL_PRB74 = 0;
    private static int PHY_ULMeanNL_PRB75 = 0;
    private static int PHY_ULMeanNL_PRB76 = 0;
    private static int PHY_ULMeanNL_PRB77 = 0;
    private static int PHY_ULMeanNL_PRB78 = 0;
    private static int PHY_ULMeanNL_PRB79 = 0;
    private static int PHY_ULMeanNL_PRB80 = 0;
    private static int PHY_ULMeanNL_PRB81 = 0;
    private static int PHY_ULMeanNL_PRB82 = 0;
    private static int PHY_ULMeanNL_PRB83 = 0;
    private static int PHY_ULMeanNL_PRB84 = 0;
    private static int PHY_ULMeanNL_PRB85 = 0;
    private static int PHY_ULMeanNL_PRB86 = 0;
    private static int PHY_ULMeanNL_PRB87 = 0;
    private static int PHY_ULMeanNL_PRB88 = 0;
    private static int PHY_ULMeanNL_PRB89 = 0;
    private static int PHY_ULMeanNL_PRB90 = 0;
    private static int PHY_ULMeanNL_PRB91 = 0;
    private static int PHY_ULMeanNL_PRB92 = 0;
    private static int PHY_ULMeanNL_PRB93 = 0;
    private static int PHY_ULMeanNL_PRB94 = 0;
    private static int PHY_ULMeanNL_PRB95 = 0;
    private static int PHY_ULMeanNL_PRB96 = 0;
    private static int PHY_ULMeanNL_PRB97 = 0;
    private static int PHY_ULMeanNL_PRB98 = 0;
    private static int PHY_ULMeanNL_PRB99 = 0;
    private static int PHY_ULMeanNL_PRB100 = 0;
    private static int PHY_ULMeanNL_PRB101 = 0;
    private static int PHY_ULMeanNL_PRB102 = 0;
    private static int PHY_ULMeanNL_PRB103 = 0;
    private static int PHY_ULMeanNL_PRB104 = 0;
    private static int PHY_ULMeanNL_PRB105 = 0;
    private static int PHY_ULMeanNL_PRB106 = 0;
    private static int PHY_ULMeanNL_PRB107 = 0;
    private static int PHY_ULMeanNL_PRB108 = 0;
    private static int PHY_ULMeanNL_PRB109 = 0;
    private static int PHY_ULMeanNL_PRB110 = 0;
    private static int PHY_ULMeanNL_PRB111 = 0;
    private static int PHY_ULMeanNL_PRB112 = 0;
    private static int PHY_ULMeanNL_PRB113 = 0;
    private static int PHY_ULMeanNL_PRB114 = 0;
    private static int PHY_ULMeanNL_PRB115 = 0;
    private static int PHY_ULMeanNL_PRB116 = 0;
    private static int PHY_ULMeanNL_PRB117 = 0;
    private static int PHY_ULMeanNL_PRB118 = 0;
    private static int PHY_ULMeanNL_PRB119 = 0;
    private static int PHY_ULMeanNL_PRB120 = 0;
    private static int PHY_ULMeanNL_PRB121 = 0;
    private static int PHY_ULMeanNL_PRB122 = 0;
    private static int PHY_ULMeanNL_PRB123 = 0;
    private static int PHY_ULMeanNL_PRB124 = 0;
    private static int PHY_ULMeanNL_PRB125 = 0;
    private static int PHY_ULMeanNL_PRB126 = 0;
    private static int PHY_ULMeanNL_PRB127 = 0;
    private static int PHY_ULMeanNL_PRB128 = 0;
    private static int PHY_ULMeanNL_PRB129 = 0;
    private static int PHY_ULMeanNL_PRB130 = 0;
    private static int PHY_ULMeanNL_PRB131 = 0;
    private static int PHY_ULMeanNL_PRB132 = 0;
    private static int PHY_ULMeanNL_PRB133 = 0;
    private static int PHY_ULMeanNL_PRB134 = 0;
    private static int PHY_ULMeanNL_PRB135 = 0;
    private static int PHY_ULMeanNL_PRB136 = 0;
    private static int PHY_ULMeanNL_PRB137 = 0;
    private static int PHY_ULMeanNL_PRB138 = 0;
    private static int PHY_ULMeanNL_PRB139 = 0;
    private static int PHY_ULMeanNL_PRB140 = 0;
    private static int PHY_ULMeanNL_PRB141 = 0;
    private static int PHY_ULMeanNL_PRB142 = 0;
    private static int PHY_ULMeanNL_PRB143 = 0;
    private static int PHY_ULMeanNL_PRB144 = 0;
    private static int PHY_ULMeanNL_PRB145 = 0;
    private static int PHY_ULMeanNL_PRB146 = 0;
    private static int PHY_ULMeanNL_PRB147 = 0;
    private static int PHY_ULMeanNL_PRB148 = 0;
    private static int PHY_ULMeanNL_PRB149 = 0;
    private static int PHY_ULMeanNL_PRB150 = 0;
    private static int PHY_ULMeanNL_PRB151 = 0;
    private static int PHY_ULMeanNL_PRB152 = 0;
    private static int PHY_ULMeanNL_PRB153 = 0;
    private static int PHY_ULMeanNL_PRB154 = 0;
    private static int PHY_ULMeanNL_PRB155 = 0;
    private static int PHY_ULMeanNL_PRB156 = 0;
    private static int PHY_ULMeanNL_PRB157 = 0;
    private static int PHY_ULMeanNL_PRB158 = 0;
    private static int PHY_ULMeanNL_PRB159 = 0;
    private static int PHY_ULMeanNL_PRB160 = 0;
    private static int PHY_ULMeanNL_PRB161 = 0;
    private static int PHY_ULMeanNL_PRB162 = 0;
    private static int PHY_ULMeanNL_PRB163 = 0;
    private static int PHY_ULMeanNL_PRB164 = 0;
    private static int PHY_ULMeanNL_PRB165 = 0;
    private static int PHY_ULMeanNL_PRB166 = 0;
    private static int PHY_ULMeanNL_PRB167 = 0;
    private static int PHY_ULMeanNL_PRB168 = 0;
    private static int PHY_ULMeanNL_PRB169 = 0;
    private static int PHY_ULMeanNL_PRB170 = 0;
    private static int PHY_ULMeanNL_PRB171 = 0;
    private static int PHY_ULMeanNL_PRB172 = 0;
    private static int PHY_ULMeanNL_PRB173 = 0;
    private static int PHY_ULMeanNL_PRB174 = 0;
    private static int PHY_ULMeanNL_PRB175 = 0;
    private static int PHY_ULMeanNL_PRB176 = 0;
    private static int PHY_ULMeanNL_PRB177 = 0;
    private static int PHY_ULMeanNL_PRB178 = 0;
    private static int PHY_ULMeanNL_PRB179 = 0;
    private static int PHY_ULMeanNL_PRB180 = 0;
    private static int PHY_ULMeanNL_PRB181 = 0;
    private static int PHY_ULMeanNL_PRB182 = 0;
    private static int PHY_ULMeanNL_PRB183 = 0;
    private static int PHY_ULMeanNL_PRB184 = 0;
    private static int PHY_ULMeanNL_PRB185 = 0;
    private static int PHY_ULMeanNL_PRB186 = 0;
    private static int PHY_ULMeanNL_PRB187 = 0;
    private static int PHY_ULMeanNL_PRB188 = 0;
    private static int PHY_ULMeanNL_PRB189 = 0;
    private static int PHY_ULMeanNL_PRB190 = 0;
    private static int PHY_ULMeanNL_PRB191 = 0;
    private static int PHY_ULMeanNL_PRB192 = 0;
    private static int PHY_ULMeanNL_PRB193 = 0;
    private static int PHY_ULMeanNL_PRB194 = 0;
    private static int PHY_ULMeanNL_PRB195 = 0;
    private static int PHY_ULMeanNL_PRB196 = 0;
    private static int PHY_ULMeanNL_PRB197 = 0;
    private static int PHY_ULMeanNL_PRB198 = 0;
    private static int PHY_ULMeanNL_PRB199 = 0;
    private static int PHY_ULMeanNL_PRB200 = 0;
    private static int PHY_ULMeanNL_PRB201 = 0;
    private static int PHY_ULMeanNL_PRB202 = 0;
    private static int PHY_ULMeanNL_PRB203 = 0;
    private static int PHY_ULMeanNL_PRB204 = 0;
    private static int PHY_ULMeanNL_PRB205 = 0;
    private static int PHY_ULMeanNL_PRB206 = 0;
    private static int PHY_ULMeanNL_PRB207 = 0;
    private static int PHY_ULMeanNL_PRB208 = 0;
    private static int PHY_ULMeanNL_PRB209 = 0;
    private static int PHY_ULMeanNL_PRB210 = 0;
    private static int PHY_ULMeanNL_PRB211 = 0;
    private static int PHY_ULMeanNL_PRB212 = 0;
    private static int PHY_ULMeanNL_PRB213 = 0;
    private static int PHY_ULMeanNL_PRB214 = 0;
    private static int PHY_ULMeanNL_PRB215 = 0;
    private static int PHY_ULMeanNL_PRB216 = 0;
    private static int PHY_ULMeanNL_PRB217 = 0;
    private static int PHY_ULMeanNL_PRB218 = 0;
    private static int PHY_ULMeanNL_PRB219 = 0;
    private static int PHY_ULMeanNL_PRB220 = 0;
    private static int PHY_ULMeanNL_PRB221 = 0;
    private static int PHY_ULMeanNL_PRB222 = 0;
    private static int PHY_ULMeanNL_PRB223 = 0;
    private static int PHY_ULMeanNL_PRB224 = 0;
    private static int PHY_ULMeanNL_PRB225 = 0;
    private static int PHY_ULMeanNL_PRB226 = 0;
    private static int PHY_ULMeanNL_PRB227 = 0;
    private static int PHY_ULMeanNL_PRB228 = 0;
    private static int PHY_ULMeanNL_PRB229 = 0;
    private static int PHY_ULMeanNL_PRB230 = 0;
    private static int PHY_ULMeanNL_PRB231 = 0;
    private static int PHY_ULMeanNL_PRB232 = 0;
    private static int PHY_ULMeanNL_PRB233 = 0;
    private static int PHY_ULMeanNL_PRB234 = 0;
    private static int PHY_ULMeanNL_PRB235 = 0;
    private static int PHY_ULMeanNL_PRB236 = 0;
    private static int PHY_ULMeanNL_PRB237 = 0;
    private static int PHY_ULMeanNL_PRB238 = 0;
    private static int PHY_ULMeanNL_PRB239 = 0;
    private static int PHY_ULMeanNL_PRB240 = 0;
    private static int PHY_ULMeanNL_PRB241 = 0;
    private static int PHY_ULMeanNL_PRB242 = 0;
    private static int PHY_ULMeanNL_PRB243 = 0;
    private static int PHY_ULMeanNL_PRB244 = 0;
    private static int PHY_ULMeanNL_PRB245 = 0;
    private static int PHY_ULMeanNL_PRB246 = 0;
    private static int PHY_ULMeanNL_PRB247 = 0;
    private static int PHY_ULMeanNL_PRB248 = 0;
    private static int PHY_ULMeanNL_PRB249 = 0;
    private static int PHY_ULMeanNL_PRB250 = 0;
    private static int PHY_ULMeanNL_PRB251 = 0;
    private static int PHY_ULMeanNL_PRB252 = 0;
    private static int PHY_ULMeanNL_PRB253 = 0;
    private static int PHY_ULMeanNL_PRB254 = 0;
    private static int PHY_ULMeanNL_PRB255 = 0;
    private static int PHY_ULMeanNL_PRB256 = 0;
    private static int PHY_ULMeanNL_PRB257 = 0;
    private static int PHY_ULMeanNL_PRB258 = 0;
    private static int PHY_ULMeanNL_PRB259 = 0;
    private static int PHY_ULMeanNL_PRB260 = 0;
    private static int PHY_ULMeanNL_PRB261 = 0;
    private static int PHY_ULMeanNL_PRB262 = 0;
    private static int PHY_ULMeanNL_PRB263 = 0;
    private static int PHY_ULMeanNL_PRB264 = 0;
    private static int PHY_ULMeanNL_PRB265 = 0;
    private static int PHY_ULMeanNL_PRB266 = 0;
    private static int PHY_ULMeanNL_PRB267 = 0;
    private static int PHY_ULMeanNL_PRB268 = 0;
    private static int PHY_ULMeanNL_PRB269 = 0;
    private static int PHY_ULMeanNL_PRB270 = 0;
    private static int PHY_ULMeanNL_PRB271 = 0;
    private static int PHY_ULMeanNL_PRB272 = 0;

}
