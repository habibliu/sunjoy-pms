package com.sunjoy.parkctrl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.config.SignatureConfig;
import com.sunjoy.parkctrl.pojo.WeChatPayNotify;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.parkctrl.service.IPmsParkPaymentService;
import com.sunjoy.parkctrl.service.impl.PmsParkDeviceCommunicator;
import com.sunjoy.parkctrl.utils.SignUtils;
import com.sunjoy.parking.entity.PmsParkOrder;
import com.sunjoy.parking.entity.PmsParkPayment;
import com.sunjoy.parking.enums.ParkOrderStatusEnum;
import com.sunjoy.parking.enums.ParkPaymentChannelEnum;
import com.sunjoy.parking.enums.ParkPaymentMethods;
import com.sunjoy.parking.enums.ParkPaymentStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

/**
 * 微信支付结果通知处理类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatNotifyController {
    @Autowired
    private IPmsParkPaymentService pmsParkPaymentService;

    @Autowired
    private IPmsParkOrderService pmsParkOrderService;


    @Autowired
    private RedisService redisService;
    @Autowired
    private PmsParkDeviceCommunicator parkDeviceCommunicator;

    @Autowired
    private SignatureConfig signatureConfig;

    @PostMapping(value = "/notify", consumes = "application/xml")
    @Transactional
    public ResponseEntity<String> handlePaymentNotify(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 1. 解析 XML 数据
            TreeMap<String, String> params = parseNotifyToMap(request);
            // 3. 在处理业务逻辑之彰，需要验证签名
            String merchantKey = getMerchantKey(params.get("merchant_id"));
            if (signatureConfig.isWechatValid() && !verifySignature(params, merchantKey)) {
                log.error("验证签名失败！");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }
            log.info("接收到微信支付结果通知{}", objectMapper.writeValueAsString(params));
            // 2. 处理业务逻辑
            if ("SUCCESS".equals(params.get("result_code"))) {

                String orderId = params.get("out_trade_no");

                //从数据库中取出订单信息
                log.info("根据订单ID{}取出订单！", orderId);
                PmsParkOrder parkOrder = pmsParkOrderService.pickParkOrder(Long.parseLong(orderId));

                //生成支付凭证，更新
                createPaymentAndUpdateOrder(parkOrder, params);
                log.info("订单支付成功！");
                //TODO  如果全部金额支付完毕，通知通道开闸放行
                parkDeviceCommunicator.notifyToReleaseVehicle(parkOrder.getTransId());
                log.info("通知开闸放行成功!");
            }

            // 4. 返回成功响应
            return ResponseEntity.ok("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
        } catch (Exception e) {
            e.printStackTrace();
            //TODO 存起来，通过人工干预处理
            log.error("微信支付结果通知处理失败！{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing notification");
        }
    }


    /**
     * 根据订单ID构造支付凭证相关信息，并更新订单状态
     *
     * @param parkOrder
     * @parma params
     */
    protected void createPaymentAndUpdateOrder(PmsParkOrder parkOrder, TreeMap<String, String> params) {

        //构造支付凭证信息
        PmsParkPayment parkPayment = new PmsParkPayment();
        BeanUtils.copyBeanProp(parkPayment, parkOrder);
        parkPayment.setOrderAmount(parkOrder.getRealAmount());
        parkPayment.setPaymentAmount(parkOrder.getRealAmount());
        parkPayment.setPaymentMethods(ParkPaymentMethods.ONLINE.getCode());
        parkPayment.setPaymentChannel(ParkPaymentChannelEnum.WECHAT.getCode());
        parkPayment.setDelFlag("0");
        parkPayment.setStatus(ParkPaymentStatusEnum.COMPLETED.getCode());
        parkPayment.setTransactionId(params.get("transaction_id"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        parkPayment.setPaymentTime(LocalDateTime.parse(params.get("time_end"), formatter));
        parkPayment.setRemark(null);
        //保存支付凭证
        this.pmsParkPaymentService.createPayment(parkPayment);

        //更新订单状态,已支付
        parkOrder.setStatus(ParkOrderStatusEnum.Paid.getCode());
        this.pmsParkOrderService.updateParkOrder(parkOrder);
    }

    /**
     * 根据商户Id找出其签名字符串
     *
     * @param merchantId
     * @return
     */
    private String getMerchantKey(String merchantId) {
        //todo 从支付渠道表或者缓存中取出来
        return "";
    }

    private boolean verifySignature(TreeMap<String, String> params, String merchantKey) {

        // 获取微信返回的签名
        String wechatSign = params.remove("sign");

        return verifySignature(params, merchantKey, wechatSign);


    }

    private boolean verifySignature(TreeMap<String, String> params, String merchantKey, String wechatSign) {
        String calculatedSign = SignUtils.createSign(params, merchantKey);
        return calculatedSign.equals(wechatSign);
    }

    private TreeMap<String, String> parseNotifyToMap(HttpServletRequest request) throws Exception {
        TreeMap<String, String> params = new TreeMap<>();

        // 1. 从 HttpServletRequest 中读取请求体
        StringBuilder xmlBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            xmlBuilder.append(line);
        }
        String xml = xmlBuilder.toString();

        // 2. 使用 DocumentBuilder 解析 XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(xml)));

        // 3. 提取所有内容并放入 TreeMap
        org.w3c.dom.Element root = document.getDocumentElement();
        org.w3c.dom.NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                String key = nodeList.item(i).getNodeName();
                String value = nodeList.item(i).getTextContent();
                params.put(key, value);
            }
        }

        return params;


    }

    @Deprecated
    private WeChatPayNotify parseNotify(HttpServletRequest request) throws Exception {
        StringBuilder notification = new StringBuilder();


        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            notification.append(line);
        }
        log.info("从微信接收到的订单支付结果：{}", notification.toString());

        // 解析 XML 数据
        WeChatPayNotify notify = unmarshalXml(notification.toString());
        log.info("Parsed Notification: " + notify.getReturnCode());
        // 处理业务逻辑，例如更新订单状态

        return notify;
    }

    private WeChatPayNotify unmarshalXml(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(WeChatPayNotify.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (WeChatPayNotify) unmarshaller.unmarshal(new StringReader(xml));
    }
}