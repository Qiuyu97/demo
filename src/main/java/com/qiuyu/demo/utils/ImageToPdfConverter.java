package com.qiuyu.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片转PDF
 */
@Slf4j
public class ImageToPdfConverter {

    public static void main(String[] args) throws IOException {
        String imgFilePath = "E:\\STUDY\\1111.jpg";
        String pdfFilePath = "E:\\STUDY\\1111.pdf";
        downloadPdf(imgFilePath, pdfFilePath);
    }

    /**
     * 下载
     *
     * @param imgFilePath img文件路径
     * @param filePath    文件路径
     * @throws IOException ioexception
     */
    private static void downloadPdf(String imgFilePath, String filePath) {
        try {
            // 1.将本地的图片转成流形式
            File imgFile = new File(imgFilePath);
            byte[] imageBytes = readBytesFromFile(imgFile);

            // 2. 生成一页 PDF document
            PDDocument document = new PDDocument();
            PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, "image");
            // 这里是你生成PDF自适应图片大小，不设置会默认为A4
            PDRectangle pageSize = new PDRectangle(image.getWidth(), image.getHeight());
            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            // 3.将 图片 添加进PDF document
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            float imageWidth = image.getWidth();
            float imageHeight = image.getHeight();
            float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
            float scaledWidth = imageWidth * scale;
            float scaledHeight = imageHeight * scale;
            float x = (pageWidth - scaledWidth) / 2;
            float y = (pageHeight - scaledHeight) / 2;
            // 这里是将你的图片填充入pdf页
            contentStream.drawImage(image, x, y, scaledWidth, scaledHeight);
            contentStream.close();

            // 4. 保存PDF
            File outputFile = new File(filePath);
            File parentFolder = outputFile.getParentFile();
            if (parentFolder != null && !parentFolder.exists()) {
                parentFolder.mkdirs();
            }
            document.save(outputFile);
            document.close();
        } catch (Exception e) {
            log.error("pdf下载失败,imgFilePath:{},filePath:{}", imgFilePath, filePath, e);
        }

    }

    /**
     * 从文件读取字节
     *
     * @param file 文件
     * @return {@link byte[]}
     * @throws IOException ioexception
     */
    private static byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return bytes;
    }


    private static byte[] downloadImage(String imageUrl) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(imageUrl);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return imageBytes;
    }

    /**
     * 得到img字节列表
     *
     * @param filePath 文件路径
     * @return {@link List}<{@link byte[]}>
     */
    private static List<byte[]> getImgByteList(String filePath) throws IOException {
        // 转换
        List<byte[]> imageBytesList = new ArrayList<>();
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && isImage(file)) {
                byte[] imageBytes = convertImageToBytes(file);
                imageBytesList.add(imageBytes);
            }
        }
        return  imageBytesList;
    }

    private static boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    private static byte[] convertImageToBytes(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        return outputStream.toByteArray();
    }


}
