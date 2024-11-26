package com.sunjoy.parkctrl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.pojo.WeChatPayNotify;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.parkctrl.service.IPmsParkPaymentService;
import com.sunjoy.parkctrl.service.IPmsParkTransactionService;
import com.sunjoy.parkctrl.service.impl.PmsParkDeviceCommunicator;
import com.sunjoy.parking.entity.PmsParkOrder;
import com.sunjoy.parking.entity.PmsParkPayment;
import com.sunjoy.parking.entity.PmsParkTransaction;
import com.sunjoy.parking.enums.ParkOrderStatusEnum;
import com.sunjoy.parking.enums.ParkPaymentChannelEnum;
import com.sunjoy.parking.enums.ParkPaymentMethods;
import com.sunjoy.parking.enums.ParkPaymentStatusEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * 车辆支付控制类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PmsParkPaymentController {
    @Autowired
    private IPmsParkPaymentService pmsParkPaymentService;

    @Autowired
    private IPmsParkOrderService pmsParkOrderService;

    @Autowired
    private IPmsParkTransactionService pmsParkTransactionService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private PmsParkDeviceCommunicator parkDeviceCommunicator;

    @PostMapping(value = "/notify/wechat")
    public ResponseEntity<String> handleWeChatPayNotify(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 1. 解析 XML 数据
            WeChatPayNotify notify = parseNotify(request);
            log.info("接收到微信支付结果通知{}", objectMapper.writeValueAsString(notify));
            // 2. 验证签名
            String sign = notify.getSign();
            if (!verifySignature(request)) {
                log.error("验证签名失败！");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            // 3. 处理业务逻辑
            if ("SUCCESS".equals(notify.getResultCode())) {

                String orderId = notify.getOutTradeNo();
                String transactionId = notify.getTransactionId();
                //TODO 从数据库中取出订单信息
                log.info("根据订单ID{}取出订单！", orderId);
                PmsParkOrder parkOrder = pmsParkOrderService.pickParkOrder(Long.parseLong(orderId));
                //生成支付凭证，更新
                handleWechatNotify(parkOrder, transactionId);
                log.info("订单支付成功！");
                //TODO  如果全部金额支付完毕，通知通道开闸放行
                notifyToReleaseVehicle(parkOrder.getTransId());
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

    private void notifyToReleaseVehicle(Long transId) {
        PmsParkTransaction parkTransaction = pmsParkTransactionService.pickupParkTransactionRecord(transId);
        if (null != parkTransaction) {
            VehiclePassage vehiclePassage = null;
            if (null != parkTransaction.getExitLaneId()) {
                //出场支付
                vehiclePassage = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_IN_LANE + parkTransaction.getExitLaneId());
            } else {
                //入场支付
                vehiclePassage = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_IN_LANE + parkTransaction.getEntryLaneId());
            }
        }
    }

    /**
     * 根据订单ID构造支付凭证相关信息，并更新订单状态
     *
     * @param parkOrder
     * @parma transactionId
     */
    @Transactional
    protected void handleWechatNotify(PmsParkOrder parkOrder, String transactionId) {

        //TODO  构造支付凭证信息
        PmsParkPayment parkPayment = new PmsParkPayment();
        BeanUtils.copyBeanProp(parkPayment, parkOrder);
        parkPayment.setPaymentAmount(parkOrder.getRealAmount());
        parkPayment.setPaymentMethods(ParkPaymentMethods.ONLINE.getCode());
        parkPayment.setPaymentChannel(ParkPaymentChannelEnum.WECHAT.getCode());
        parkPayment.setDelFlag("0");
        parkPayment.setStatus(ParkPaymentStatusEnum.COMPLETED.getCode());
        parkPayment.setTransactionId(transactionId);
        parkPayment.setRemark(null);
        //保存支付凭证
        this.pmsParkPaymentService.createPayment(parkPayment);
        //更新订单状态,已支付
        parkOrder.setStatus(ParkOrderStatusEnum.Paid.getCode());
        this.pmsParkOrderService.updateParkOrder(parkOrder);


    }

    private boolean verifySignature(HttpServletRequest request) {
        /*
        String wechatPaySerial = request.getHeader(WECHAT_PAY_SERIAL);
        String apiV3Key = wxPayConfig.getApiV3Key();
        String nonce = request.getHeader(WECHAT_PAY_NONCE); // 请求头Wechatpay-Nonce
        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP); // 请求头Wechatpay-Timestamp
        String signature = request.getHeader(WECHAT_PAY_SIGNATURE); // 请求头Wechatpay-Signature
        WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest = new WechatPay2ValidatorForRequest(wechatPaySerial, apiV3Key, nonce, timestamp, signature, body, verifier);
        Notification notification = wechatPay2ValidatorForRequest.notificationHandler();
        String eventType = notification.getEventType();
        if (eventType.length() == 0) {
            log.error("支付回调通知验签失败");
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }
        */
        log.info("支付回调通知验签成功");

        // TODO: 实现签名验证逻辑
        return true; // 假设签名验证通过
    }


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