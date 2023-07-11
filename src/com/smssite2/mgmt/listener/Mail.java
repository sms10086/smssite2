package com.smssite2.mgmt.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.misc.BASE64Encoder;
import com.note.common.ConfigUtil;

public class Mail {    
    private static final String LINE_END = "\r\n";
    
    private boolean isDebug = false;
    
    private boolean isAllowReadSocketInfo = true;
    
    private String host;
    
    private String from;
    
    private List<String> to;
    
    private List<String> cc;
    
    private List<String> bcc;
    
    private String subject;
    
    private String user;
    
    private String password;
    
    private String contentType;
    
    private String boundary;
    
    private String boundaryNextPart;
    
    private String contentTransferEncoding;
    
    private String charset;
    
    private String contentDisposition;
    
    private String content;
    
    private String simpleDatePattern;
    
    private String defaultAttachmentContentType;
    
    private List<MailPart> partSet;
    
    private static Map<String, String> contentTypeMap;
    
    static {
        contentTypeMap = new HashMap<String, String>();
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("xlsx", "application/vnd.ms-excel");
        contentTypeMap.put("xlsm", "application/vnd.ms-excel");
        contentTypeMap.put("xlsb", "application/vnd.ms-excel");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("dot", "application/msword");
        contentTypeMap.put("docx", "application/msword");
        contentTypeMap.put("docm", "application/msword");
        contentTypeMap.put("dotm", "application/msword");
    }
    
    private class MailPart extends Mail {
        public MailPart() {
        }
    }
    
    public Mail() {
        defaultAttachmentContentType = "application/octet-stream";
        simpleDatePattern = "yyyy-MM-dd HH:mm:ss";
        boundary = "--=_NextPart_zlz_3907_" + System.currentTimeMillis();
        boundaryNextPart = "--" + boundary;
        contentTransferEncoding = "base64";
        contentType = "multipart/alternative";
        charset = Charset.defaultCharset().name();
        partSet = new ArrayList<MailPart>();
        to = new ArrayList<String>();
        cc = new ArrayList<String>();
        bcc = new ArrayList<String>();
    }
    
    private String getPartContentType(String fileName) {
        String ret = null;
        if (null != fileName) {
            int flag = fileName.lastIndexOf(".");
            if (0 <= flag && flag < fileName.length() - 1) {
                fileName = fileName.substring(flag + 1);
            }
            ret = contentTypeMap.get(fileName);
        }
        
        if (null == ret) {
            ret = defaultAttachmentContentType;
        }
        return ret;
    }
    
    private String toBase64(String str, String charset) {
        if (null != str) {
            try {
                return toBase64(str.getBytes(charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    
    private String toBase64(byte[] bs) {
        return new BASE64Encoder().encode(bs);
    }
    
    private String toBase64(String str) {
        return toBase64(str, Charset.defaultCharset().name());
    }
    
    private String getAllParts() {
        int partCount = partSet.size();
        StringBuilder sbd = new StringBuilder(LINE_END);
        for (int i = partCount - 1; i >= 0; i--) {
            Mail attachment = partSet.get(i);
            String attachmentContent = attachment.getContent();
            if (null != attachmentContent && 0 < attachmentContent.length()) {
                sbd.append(getBoundaryNextPart()).append(LINE_END);
                sbd.append("Content-Type: ");
                sbd.append(attachment.getContentType());
                sbd.append(LINE_END);
                sbd.append("Content-Transfer-Encoding: ");
                sbd.append(attachment.getContentTransferEncoding());
                sbd.append(LINE_END);
                if (i != partCount - 1) {
                    sbd.append("Content-Disposition: ");
                    sbd.append(attachment.getContentDisposition());
                    sbd.append(LINE_END);
                }
                
                sbd.append(LINE_END);
                sbd.append(attachment.getContent());
                sbd.append(LINE_END);
            }
        }
        
        sbd.append(LINE_END);
        sbd.append(LINE_END);
        partSet.clear();
        return sbd.toString();
    }
    
    private void addContent() {
        if (null != content) {
            MailPart part = new MailPart();
            part.setContent(toBase64(content));
            part.setContentType("text/plain;charset=\"" + charset + "\"");
            partSet.add(part);
        }
    }
    
    private String listToMailString(List<String> mailAddressList) {
        StringBuilder sbd = new StringBuilder();
        if (null != mailAddressList) {
            int listSize = mailAddressList.size();
            for (int i = 0; i < listSize; i++) {
                if (0 != i) {
                    sbd.append(";");
                }
                sbd.append("<").append(mailAddressList.get(i)).append(">");
            }
        }
        return sbd.toString();
    }
    
    private List<String> getrecipient() {
        List<String> list = new ArrayList<String>();
        list.addAll(to);
        list.addAll(cc);
        list.addAll(bcc);
        return list;
    }
    
    public void addAttachment(String filePath) {
        addAttachment(filePath, null);
    }
    
    public void addTo(String mailAddress) {
        this.to.add(mailAddress);
    }
    
    public void addCc(String mailAddress) {
        this.cc.add(mailAddress);
    }
    
    public void addBcc(String mailAddress) {
        this.bcc.add(mailAddress);
    }
    
    public void addAttachment(String filePath, String charset) {
        if (null != filePath && filePath.length() > 0) {
            File file = new File(filePath);
            try {
                addAttachment(file.getName(), new FileInputStream(file),
                        charset);
            } catch (FileNotFoundException e) {
              
                System.exit(1);
            }
        }
    }
    
    public void addAttachment(String fileName, InputStream attachmentStream,
            String charset) {
        try {
            
            byte[] bs = null;
            if (null != attachmentStream) {
                int buffSize = 1024;
                byte[] buff = new byte[buffSize];
                byte[] temp;
                bs = new byte[0];
                int readTotal = 0;
                while (-1 != (readTotal = attachmentStream.read(buff))) {
                    temp = new byte[bs.length];
                    System.arraycopy(bs, 0, temp, 0, bs.length);
                    bs = new byte[temp.length + readTotal];
                    System.arraycopy(temp, 0, bs, 0, temp.length);
                    System.arraycopy(buff, 0, bs, temp.length, readTotal);
                }
            }
            
            if (null != bs) {
                MailPart attachmentPart = new MailPart();
                charset = null != charset ? charset : Charset.defaultCharset()
                        .name();
                String contentType = getPartContentType(fileName)
                        + ";name=\"=?" + charset + "?B?" + toBase64(fileName)
                        + "?=\"";
                attachmentPart.setCharset(charset);
                attachmentPart.setContentType(contentType);
                attachmentPart.setContentDisposition("attachment;filename=\"=?"
                        + charset + "?B?" + toBase64(fileName) + "?=\"");
                attachmentPart.setContent(toBase64(bs));
                partSet.add(attachmentPart);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != attachmentStream) {
                try {
                    attachmentStream.close();
                    attachmentStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
        }
    }
    
    public String send() {
        
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        
        try {
            socket = new Socket(host, 25);
            pw = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            
            StringBuilder infoBuilder = new StringBuilder(
                    "\nServer info: \n------------\n");
            pw.write("HELO ".concat(host).concat(LINE_END)); 
            if (!readResponse(pw, br, infoBuilder, "220"))
                return infoBuilder.toString();
            
            pw.write("AUTH LOGIN".concat(LINE_END)); 
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();
            
            pw.write(toBase64(user).concat(LINE_END)); 
            if (!readResponse(pw, br, infoBuilder, "334"))
                return infoBuilder.toString();
            
            pw.write(toBase64(password).concat(LINE_END)); 
            if (!readResponse(pw, br, infoBuilder, "334"))
                return infoBuilder.toString();
            
            pw.write("MAIL FROM:<" + from + ">" + LINE_END); 
            if (!readResponse(pw, br, infoBuilder, "235"))
                return infoBuilder.toString();
            
            List<String> recipientList = getrecipient();
            for (int i = 0; i < recipientList.size(); i++) {
                pw.write("RCPT TO:<" + recipientList.get(i) + ">" + LINE_END);
                if (!readResponse(pw, br, infoBuilder, "250"))
                    return infoBuilder.toString();
            }
            
            
            pw.write("DATA" + LINE_END); 
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();
            
            flush(pw);
            
            StringBuffer sbf = new StringBuffer("From: <" + from + ">"
                    + LINE_END); 
            sbf.append("To: " + listToMailString(to) + LINE_END);
            sbf.append("Cc: " + listToMailString(cc) + LINE_END);
            sbf.append("Bcc: " + listToMailString(bcc) + LINE_END);
            sbf.append("Subject: " + subject + LINE_END);
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDatePattern);
            sbf.append("Date: ").append(sdf.format(new Date()));
            sbf.append(LINE_END); 
            sbf.append("Content-Type: ");
            sbf.append(contentType);
            sbf.append(";");
            sbf.append("boundary=\"");
            sbf.append(boundary).append("\""); 
            sbf.append(LINE_END);
            sbf.append("This is a multi-part message in MIME format.");
            sbf.append(LINE_END);
            
            addContent();
            
            sbf.append(getAllParts());
            
            sbf.append(LINE_END).append(".").append(LINE_END);
            pw.write(sbf.toString());
            readResponse(pw, br, infoBuilder, "354");
            flush(pw);
            
            pw.write("QUIT" + LINE_END);
            if (!readResponse(pw, br, infoBuilder, "250"))
                return infoBuilder.toString();
            flush(pw);
            
            return infoBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception:>" + e.getMessage();
        } finally {
            try {
                if (null != socket)
                    socket.close();
                if (null != pw)
                    pw.close();
                if (null != br)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            this.partSet.clear();
        }
        
    }
    
    private void flush(PrintWriter pw) {
        if (!isAllowReadSocketInfo) {
            pw.flush();
        }
    }
    
    private boolean readResponse(PrintWriter pw, BufferedReader br,
            StringBuilder infoBuilder, String msgCode) throws IOException {
        if (isAllowReadSocketInfo) {
            pw.flush();
            String message = br.readLine();
            infoBuilder.append("SERVER:/>");
            infoBuilder.append(message).append(LINE_END);
            if (null == message || 0 > message.indexOf(msgCode)) {
                
                pw.write("QUIT".concat(LINE_END));
                pw.flush();
                return false;
            }
            if (isDebug) {
                System.out.println("DEBUG:/>" + msgCode + "/" + message);
            }
        }
        return true;
    }
    
    public String getBoundaryNextPart() {
        return boundaryNextPart;
    }
    
    public void setBoundaryNextPart(String boundaryNextPart) {
        this.boundaryNextPart = boundaryNextPart;
    }
    
    public String getDefaultAttachmentContentType() {
        return defaultAttachmentContentType;
    }
    
    public void setDefaultAttachmentContentType(
            String defaultAttachmentContentType) {
        this.defaultAttachmentContentType = defaultAttachmentContentType;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public List<String> getTo() {
        return to;
    }
    
    public void setTo(List<String> to) {
        this.to = to;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getBoundary() {
        return boundary;
    }
    
    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }
    
    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }
    
    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }
    
    public String getCharset() {
        return charset;
    }
    
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    public String getContentDisposition() {
        return contentDisposition;
    }
    
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
    
    public String getSimpleDatePattern() {
        return simpleDatePattern;
    }
    
    public void setSimpleDatePattern(String simpleDatePattern) {
        this.simpleDatePattern = simpleDatePattern;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isAllowReadSocketInfo() {
        return isAllowReadSocketInfo;
    }
    
    public void setAllowReadSocketInfo(boolean isAllowReadSocketInfo) {
        this.isAllowReadSocketInfo = isAllowReadSocketInfo;
    }
    
    public void init(){
    	host=ConfigUtil.getProperties("host");
    	from=ConfigUtil.getProperties("from");
    	user=ConfigUtil.getProperties("user");
    	password=ConfigUtil.getProperties("password");
    	subject="天气预报异常！";
    	String receiver=ConfigUtil.getProperties("receiver");
    	
    	if(host==null||host.trim().length()==0)host="61.152.183.135";
    	if(receiver!=null&&receiver.trim().length()>0)
    	{String[]receivers=receiver.split(";");
	    	for(String s:receivers){
	    		to.add(s);
	    	}
    	}
    }
    public  void sendEmail(Mail mail,String content){
    	mail.init();
    	mail.setHost(host);
    	mail.setFrom(from);
    	mail.setTo(to);
    	mail.setSubject(subject);
    	mail.setUser(user);
    	mail.setPassword(password);
    	mail.setContent(content);
    	mail.send();
    }
    
}

