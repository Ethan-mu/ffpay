package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PayDigestUtil;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.shop.demo.PayOrderDemo;
import org.xxpay.shop.util.MatrixToImageWriter;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    private static final MyLog _log = MyLog.getLog(PayOrderDemo.class);

    // 商户ID
//    static final String mchId = "20001223";//20001223,20001245
    //sxbctv商户ID
    //微信 20001225通付    20001224张雄
    //#支付宝#
    //zhangx 20001226
    //通付WAP 20001227
    static final String mchId = "VODSGS"; // 通付


//    static final String mchId = "20001225"; // 通付

    // 加签key
    static final String reqKey = "M86l522AV6q613Ii4W6u8K48uW8vM1N6bFgyv769220MdYe9u37N4y7rI5mQ";
    // 验签key
    static final String repKey = "Hpcl522AV6q613KIi46u6g6XuW8vM1N8bFgyv769770MdYe9u37M4y7rIpl8";

    //static final String baseUrl = "http://api.xxpay.org/api";
    static final String baseUrl = "http://localhost:3000/api";
    //static final String notifyUrl = "http://www.baidu.com"; // 本地环境测试,可到ngrok.cc网站注册
//    static final String notifyUrl = "http://shop.xxpay.org/goods/payNotify";
//    static final String notifyUrl = "http://10.43.124.84:8081/goods/payNotify";
//    static final String notifyUrl = "http://124.47.34.11:8081/goods/payNotify";
//    static final String notifyUrl = "http://47.105.136.113:8081/goods/payNotify";
//    static final String notifyUrl = "http://47.105.136.113:3020/notify/pay/wxPayNotifyRes.htm";

    //    static final String notifyUrl = "http://124.47.34.204/wxPaynotify";
    static final String notifyUrl = "http://10.43.112.151/notifyTest";
    @RequestMapping(value="/showQrcode",method={RequestMethod.POST,RequestMethod.GET} )
    public  void showQrcode() {
        payOrderTest();
        //quryPayOrderTest("1494774484058", "P0020170910211048000001");
    }

    // 统一下单
    static Map payOrderTest() {
        System.currentTimeMillis();
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                               // 商户ID
        paramMap.put("mchOrderNo", System.currentTimeMillis());     // 商户订单号
        // 支付渠道ID, WX_NATIVE(微信扫码),WX_JSAPI(微信公众号或微信小程序),WX_APP(微信APP),WX_MWEB(微信H5),ALIPAY_WAP(支付宝手机支付),ALIPAY_PC(支付宝网站支付),ALIPAY_MOBILE(支付宝移动支付)
//        paramMap.put("channelId", "WX_NATIVE");
        paramMap.put("channelId", "ALIPAY_QR");
        paramMap.put("amount", 1);                                  // 支付金额,单位分
        paramMap.put("currency", "cny");                            // 币种, cny-人民币
        paramMap.put("clientIp", "211.94.116.218");                 // 用户地址,微信H5支付时要真实的
        paramMap.put("device", "WEB");                              // 设备
        paramMap.put("subject", "通付账户测试");
        paramMap.put("body", "通付账户测试");
        paramMap.put("notifyUrl", notifyUrl);                       // 回调URL
//        paramMap.put("param1", "");                                 // 扩展参数1
//        paramMap.put("param2", "");                                 // 扩展参数2
//        paramMap.put("extra", "{\"productId\":\"120989823\",\"openId\":\"o2RvowBf7sOVJf8kJksUEMceaDqo\"}");  // 附加参数
//        paramMap.put("extra", "{\"productId\":\"1209898231\",\"openId\":\"0f8acb7bc720636f773b4355ca2948b8\"}");  // 附加参数

        paramMap.put("extra","{'productId':'1209898231'}");
//        paramMap.put("extra", "{\n" +
//                "  \"productId\": \"120989823\",\n" +
//                "  \"openId\": \"oIkQuwhPgPUgl-TvQ48_UUpZUwMs\",\n" +
//                "  \"sceneInfo\": {\n" +
//                "    \"h5_info\": {\n" +
//                "      \"type\": \"Wap\",\n" +
//                "      \"wap_url\": \"http://shop.xxpay.org\",\n" +
//                "      \"wap_name\": \"sxbctv-Test充值\"\n" +
//                "    }\n" +
//                "  }\n" +
//                " ,\"discountable_amount\":\"0.00\"," + //面对面支付扫码参数：可打折金额 可打折金额+不可打折金额=总金额
//                "  \"undiscountable_amount\":\"0.00\"," + //面对面支付扫码参数：不可打折金额
//                "}");  // 附加参数

        //{"h5_info": {"type":"Wap","wap_url": "https://pay.qq.com","wap_name": "腾讯充值"}}
        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);                              // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        System.out.println();
        String url = baseUrl + "/pay/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
        Map retMap = JSON.parseObject(result);
        if ("SUCCESS".equals(retMap.get("retCode")) && "SUCCESS".equalsIgnoreCase(retMap.get("resCode").toString())) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap,repKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                _log.info("=========支付中心下单验签成功=========");
                return retMap;
            } else {
                _log.error("=========支付中心下单验签失败=========");
                return null;
            }
        }
        _log.error("=========支付中心请求支付失败=========");
        return retMap;
    }

    static String quryPayOrderTest(String mchOrderNo, String payOrderId) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                               // 商户ID
        paramMap.put("mchOrderNo", mchOrderNo);                     // 商户订单号
        paramMap.put("payOrderId", payOrderId);                     // 支付订单号
        paramMap.put("executeNotify", "true");                      // 是否执行回调,true或false,如果为true当订单状态为支付成功(2)时,支付中心会再次回调一次业务系统

        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);                              // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心查单接口,请求数据:" + reqData);
        String url = baseUrl + "/pay/query_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心查单接口,响应数据:" + result);
        Map retMap = JSON.parseObject(result);
        if("SUCCESS".equals(retMap.get("retCode")) && "SUCCESS".equalsIgnoreCase(retMap.get("resCode").toString())) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, repKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if(checkSign.equals(retSign)) {
                System.out.println("=========支付中心查单验签成功=========");
            }else {
                System.err.println("=========支付中心查单验签失败=========");
                return null;
            }
        }

        return retMap.get("payOrderId")+"";
    }
    /**
     * 生成二维码，返回到页面上
     * @param response
     */
    @RequestMapping(value="/getErWeiCode",method={RequestMethod.POST,RequestMethod.GET} )
    public void getErWeiCode(HttpServletResponse response,Map map) throws IOException {
//        Map Result = payOrderTest();
////       String url = Result.get("codeUrl") + "";
//       String url = "http:qweqwrqtqttqetrewttew";
//        if (!"".equals(url)) {
//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] b = decoder.decodeBuffer(url);
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(b));
//            ServletOutputStream outputStream = response.getOutputStream();
//            ImageIO.write(image, "png", outputStream);
//            outputStream.flush();
//            outputStream.close();
//        }
        String url="www.baidu.com";
        OutputStream stream =null;
        if(url!=null&&!"".equals(url)){
            try {
                stream=  response.getOutputStream();
                MatrixToImageWriter.writeToStream(url, stream);
                stream.flush();
                stream.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace(); }
                finally{
                if(stream!=null){
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            }
        }
    }
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
