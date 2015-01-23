package com.dhcc.traffic.wmts;

import org.apache.commons.lang.StringUtils;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Map;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.io.File;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.UnknownHostException;

/**
 * Describe class AgentService here.
 *
 *
 * Created: Sun Jul  8 16:54:02 2012
 *
 * @author <a href="mailto:lei.alf1981@gmail.com"> chenglei </a>
 * @version 1.0
 */
public class AgentService extends HttpServlet{

    private Logger log = LoggerFactory.getLogger(AgentService.class);
    
    private String params[]= {"REQUEST","FORMAT","TILEROW","TILECOL","TILEMATRIX","SERVICE","VERSION","TILEMATRIXSET"};
    private String cacheDir = "/home/aden/dhccwork/jxw/cache/v/";
    private String baseUrl = "http://www.beijingmap.egov.cn/service/ImageEngine/EzServer/EzMap";
    private String useCache = "true";
    
    public void init() throws ServletException{
        if (StringUtils.isNotBlank(getInitParameter("cacheDir"))) {
            cacheDir = getInitParameter("cacheDir");
        }
        if (StringUtils.isNotBlank(getInitParameter("baseUrl"))) {
            baseUrl = getInitParameter("baseUrl");
        }
        if (StringUtils.isNotBlank(getInitParameter("useCache"))) {
            useCache = getInitParameter("useCache");
        }
    }

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        String request = req.getParameter("REQUEST");
        if("GetTile".equals(request)){
            getTile(req,res);
        }
    }

    private void getTile(HttpServletRequest req,HttpServletResponse res){
        try {
        	Map<String,String> map = parseParam(req);
            String tileMatrix = map.get("TILEMATRIX");
            String tileRow = map.get("TILEROW");
            String tileCol = map.get("TILECOL");
            int y = Integer.parseInt(tileRow);
            int x = Integer.parseInt(tileCol);
            int z = Integer.parseInt(tileMatrix.substring(tileMatrix.lastIndexOf(":")+1));
            
            switch (z) {
            case 12:
                x+=1;
                y+=1;
                break;
            case 11:
                x+=2;
                y+=2;
                y=3+2-y;
                break;
            case 10:
                x+=4;
                y+=4;
                y=7+4-y;
                break;
            case 9:
            	x+=8;
                y+=8;
                y=15+8-y;
                
                break;
            case 8:
            	x+=16;
                y+=16;
                y=31+16-y;
                
                break;
            case 7:
            	x+=32;
            	y+=32;
            	y=63+32-y;
            	
            	break;
            case 6:
            	x+=64;
            	y+=64;
            	y=127+64-y;
            	
            	break;
            case 5:
            	x+=128;
            	y+=128;
            	y=255+128-y;
            	
            	break;
            case 4:
            	x+=256;
            	y+=256;
            	y=511+256-y;
            	
            	break;
            case 3:
            	x+=512;
            	y+=512;
            	y=1023+512-y;
            	
            	break;
            case 2:
            	x+=1024;
            	y+=1024;
            	y=2047+1024-y;
            	
            	break;
            case 1:
            	x+=2048;
            	y+=2048;
            	y=4095+2048-y;
            	
            	break;
            case 0:
            	x+=4096;
            	y+=4096;
            	y=8191+4096-y;
            	
            	break;
            default:
                break;
            }
            if (useCache.equals("true")) {
                File file = new File(cacheDir+z+File.separator+x+File.separator+y+".jpg");
                if(file.exists() && file.length()>0){
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream fb = new BufferedInputStream(fis);
                    byte[] data = new byte[1024];
                    int c = 0;
                    while ((c=fb.read(data))!=-1){
                        res.getOutputStream().write(data,0,c);
                    }
                    fb.close();
                    res.getOutputStream().flush();
                    res.getOutputStream().close();
                }else {
                    File f = new File(cacheDir+z+File.separator+x);
                    if(!f.exists()){
                        f.mkdirs();
                    }
                    File f1 = new File(cacheDir+z+File.separator+x+File.separator+y+".jpg");
                    FileOutputStream fos = new FileOutputStream(f1);
                    BufferedOutputStream fbo = new BufferedOutputStream(fos);
                    String urlstr = "";
                    urlstr = baseUrl+"&Col="+x+"&Row="+y+"&Zoom="+z;
                    URL url = new URL(urlstr);
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.setReadTimeout(100000);
                    res.setContentType(urlc.getContentType());
                
                    byte[] data = new byte[1024];

                    BufferedInputStream input = new BufferedInputStream(urlc.getInputStream());
                
                    int c = -1;
                    while ((c=input.read(data))!=-1){
                        res.getOutputStream().write(data,0,c);
                        fbo.write(data,0,c);
                    }
                    fbo.flush();
                    fbo.close();
                    input.close();
                    res.getOutputStream().flush();
                    res.getOutputStream().close();
                }
            }else {
                String urlstr = "";
                urlstr = baseUrl+"&Col="+x+"&Row="+y+"&Zoom="+z;
                URL url = new URL(urlstr);
                URLConnection urlc = url.openConnection();
                urlc.setConnectTimeout(3000);
                urlc.setReadTimeout(100000);
                res.setContentType(urlc.getContentType());
                
                byte[] data = new byte[1024];
                
                BufferedInputStream input = new BufferedInputStream(urlc.getInputStream());
                int c = -1;
                while ((c=input.read(data))!=-1){
                    res.getOutputStream().write(data,0,c);
                }
                res.getOutputStream().flush();
                res.getOutputStream().close();
                input.close();
            }
            
        }catch (UnknownHostException ex) {
            log.error("无法连接jxwGIS服务器!");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map parseParam(HttpServletRequest req){
        Map<String,String> map = new HashMap<String,String>();
        for (String p : params) {
            map.put(p,req.getParameter(p));
        }
        return map;
    }


}
