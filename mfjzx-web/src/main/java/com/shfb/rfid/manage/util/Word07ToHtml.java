package com.shfb.rfid.manage.util;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;
import com.shfb.rfid.manage.dto.UploadFileEntity;
import com.shfb.rfid.manage.service.FtpService;
import com.shfb.rfid.manage.service.impl.FtpServiceImpl;


public class Word07ToHtml {
    private String title;
    private String introduce;
    private List<String> imgs;
    private String html;
    private String cover;
    public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	private static String baseDir = "mfjzx";
    public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

//	public static String imgpath = "G:\\WebTest\\";
	//public static String imgpath = "D:/Developer/tomcat1/webapps/mfjzx-web/WebTest/";
	public static String imgpath = "/usr/WebTest/";

    public Word07ToHtml() {
        imgs = new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void byte2image(byte[] data,boolean isCover) {
        if (data.length < 3) return;
        try {
           // FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            FtpService ftpService = new FtpServiceImpl();
    		String dir = String.format("%s/case", baseDir);
    		String fileName="";
    		String srcImgPath =imgpath+ "tmp.jpg";
    	    String iconPath = imgpath+"logo.jpg";
    	    String targerPath =imgpath+ "target.jpg";
    	    UploadFileEntity uploadFileEntity=null;
    		if(isCover){
    			fileName = String.format("cov_%s.%s", new Date().getTime(), "jpg");
    			uploadFileEntity = new UploadFileEntity(fileName, new ByteArrayInputStream(data), dir);
    		}else{
    			FileImageOutputStream imageOutput = new FileImageOutputStream(new File(srcImgPath));
    			imageOutput.write(data, 0, data.length);
    			imageOutput.close();
    			WaterMarkUtil.waterMarkImageByIcon(iconPath, srcImgPath, targerPath, 0.5f);  			
    			FileInputStream fileInputStream = new FileInputStream(new File(targerPath));
    			fileName = String.format("pic_%s.%s", new Date().getTime(), "jpg");
    			uploadFileEntity = new UploadFileEntity(fileName, fileInputStream, dir);
    		}
    		ftpService.uploadFile(uploadFileEntity);
    		String picFile = FtpService.READ_URL+"data/"+dir + "/" + fileName;
    		if(isCover){
    			cover=picFile;
    		}else{
    			imgs.add(picFile);
    		}
    		
            //imageOutput.write(data, 0, data.length);
            //imageOutput.close();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    @Override
	public String toString() {
		return "Word07ToHtml [title=" + title + ", introduce=" + introduce
				+ ", imgs=" + imgs + ", html=" + html + "]";
	}

	public void parserHtml() {
        try {
            File input = new File(imgpath + "tmp.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            doc.select("body div p").first().remove();
            doc.select("body div p br").first().remove();
//          删去空行  
            doc.select("body div").first().removeAttr("style");
            Elements imgsources = doc.select("img[src]");
            for (int i = 0; i < imgsources.size(); i++) {
                imgsources.get(i).attr("src", imgs.get(i));
            }
            html=doc.toString();
            
//            System.out.println(doc.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getWordInfo(MultipartFile file) {
    	 String regEx="[^0-9]";
         Pattern p = Pattern.compile(regEx);
         Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        try {
            XWPFDocument docx = new XWPFDocument(file.getInputStream());
            int pages = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
            int characters = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getCharacters();
            List<XWPFParagraph> paras = docx.getParagraphs();
            String intro = "";
            int whiteline=0;
            for (int i = 0; i < paras.size(); i++) {
                if (i == whiteline) {
                    title = paras.get(i).getText();
                    if(title.equals(" ")||title.equals("\n")){
                    	whiteline++;
                    }
                } else {
                    if (paras.get(i).getText() != null) {
                        intro += paras.get(i).getText();
                    }
                }
            }
            introduce = intro.length() >= 40 ? intro.substring(0, 40) : intro;
            List<XWPFPictureData> pics = docx.getAllPictures();
            for (int i = 0; i < pics.size(); i++) {

                String filename=pics.get(i).getFileName();
                Matcher m = p.matcher(filename);
                int trueNum=Integer.parseInt(m.replaceAll("").trim());
                map.put(trueNum-1,i);

            }
            
            for (int i = 0; i < pics.size(); i++) {
            	int index=map.get(i);
            	if(i==0){
            		byte2image(pics.get(index).getData(),true);
            	}
                byte2image(pics.get(index).getData(),false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convertToHtml(MultipartFile file) {
        try {
               // if (file.getName().endsWith(".docx") || file.getName().endsWith(".DOCX")) {
                    InputStream in = file.getInputStream();
                    XWPFDocument document = new XWPFDocument(in);
                    File imageFolderFile = new File(imgpath);
                    XHTMLOptions options = XHTMLOptions.create().URIResolver(
                            new FileURIResolver(imageFolderFile));
                    options.setExtractor(new FileImageExtractor(imageFolderFile));
                    OutputStream out = new FileOutputStream(new File(
                            imgpath + "tmp.html"));
                    XHTMLConverter.getInstance().convert(document, out, null);
               /* } else {
                    System.out.println("Enter only MS Office 2007+ files");
                }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
