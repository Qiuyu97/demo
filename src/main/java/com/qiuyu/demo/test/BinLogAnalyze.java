package com.qiuyu.demo.test;

import java.io.*;

public class BinLogAnalyze {



    /**
     * 找到binlog中原始停用的栏目
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Long id = null;
        Integer statusBegin= null;
        Integer statusEnd = null;
        boolean isWhere = true;
        int tyCount = 0;
        File file = new File("C:\\Users\\11631\\Desktop\\a3");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        File outFile = new File("C:\\Users\\11631\\Desktop\\out.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        File tingyongFile = new File("C:\\Users\\11631\\Desktop\\tingyong.txt");
        BufferedWriter tingyongWriter = new BufferedWriter(new FileWriter(tingyongFile));
        String line;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if (line.contains("### UPDATE `tmy_product`.`tenant_channel`")) {
                System.out.println();
                if (id == null) continue;
                String format = String.format("更新tenant_channel, id:%d , statusBegin:%d, statusEnd:%d ", id, statusBegin, statusEnd);
                System.out.print(format);
                writer.write(format);
                writer.newLine();
                // 历史停用id
                if (statusBegin == 1 && statusEnd == 1) {
                    tingyongWriter.write(id + ",");
                    tyCount ++;
                }
                continue;
            }
            if (line.contains("@1=")) {
                // 抽取id
                int i = line.indexOf("@1=");
                int j = line.indexOf(" /*");
                id = Long.parseLong(line.substring(i + 3, j));
//                System.out.print(" id: " + Long.parseLong(line.substring(i + 3, j)) + " ");
                continue;
            }
            if (line.contains("### WHERE")) {
//                System.out.print(" 初始值：");
                continue;
            }
            if (line.contains("@10=")) {
                if (isWhere) {
                    // 抽取初始值
                    int i = line.indexOf("@10=");
                    statusBegin = Integer.parseInt(String.valueOf(line.charAt(i + 4)));
//                System.out.print(line.charAt(i + 4));
                    isWhere = false;
                    continue;
                }else {
                    int i = line.indexOf("@10=");
                    statusEnd = Integer.parseInt(String.valueOf(line.charAt(i + 4)));
                    isWhere = true;
                    continue;
                }
            }
        }
        reader.close();
        writer.close();
        tingyongWriter.close();
        System.out.print("历史停用个数：" + tyCount);
    }

    private BufferedWriter writeLine(String line) throws IOException {
        File file = new File("C:\\Users\\11631\\Desktop\\out.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(line);
        writer.newLine();
        return writer;
    }



    private static void dealData(Long id, Integer beginValue, Integer endValue) {
        if (id == null || beginValue == null || endValue == null ){
            return;
        }
        System.out.println(String.format("id:%s,初始值：%s，设置值：%s", id, beginValue, endValue));
    }


    public static void main2(String[] args) {
        String line = "###   @1=207348 /* LONGINT meta=0 nullable=0 is_null=0 */";
        int i = line.indexOf("@1=");
        int j = line.indexOf(" /*");
        System.out.println(Long.parseLong(line.substring(i + 3, j)));

        String line2 = "###   @10=1 /* TINYINT meta=0 nullable=0 is_null=0 */";
        int i1 = line2.indexOf("@10=");
        System.out.println(line2.charAt(i1 + 4));
    }

}
