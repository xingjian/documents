package com.dhcc.traffic.wmts;

/**
 * Describe class WMTSService here.
 *
 *
 * Created: Tue Jun 19 10:17:29 2012
 *
 * @author <a href="mailto:lei.alf1981@gmail.com"> chenglei </a>
 * @version 1.0
 */

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

public class WMTSService  extends HttpServlet{
    
    private String params[]= {"REQUEST","FORMAT","TILEROW","TILECOL","TILEMATRIX","SERVICE","VERSION","TILEMATRIXSET"};
    private String filedir="/home/aden/dhccwork/jxw/cache/v/";

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        String request = req.getParameter("REQUEST");
        if("GetTile".equals(request)){
            getTile(req,res);
        }
    }

    private void response(HttpServletResponse res,byte[] data){
        try {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/vnd.ogc.wms_xml");
            //res.setCharacterEncoding("UTF-8");
            res.setContentLength(data.length);
            res.getOutputStream().write(data);
        } catch (Exception e) {
            e.printStackTrace();
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
           
            File file = new File(filedir+z+"/"+x+"/"+y+".jpg");
            if (file.exists()) {
                BufferedInputStream i = new BufferedInputStream(new FileInputStream(file));
                int c=0;
                while((c=i.read())!=-1){
                    res.getOutputStream().write(c);
                }
                i.close();
            }
            /*
            String urlstr = "";
            urlstr = "http://www.beijingmap.egov.cn/service/ImageEngine/EzServer/EzMap?Service=getImage&Type=RGB&ZoomOffset=-2&Col="+x+"&Row="+y+"&Zoom="+z+"&V=0.3&user=sjtw01&password=sjtwxxzx01";

            URL url = new URL(urlstr);
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            int c = 0;
            while ((c=input.read())!=-1){
                res.getOutputStream().write(c);
            }
            input.close();
            */
            
        } catch (Exception e) {
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
