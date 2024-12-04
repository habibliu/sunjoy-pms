package com.sunjoy.parkctrl.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.parkctrl.config.SignatureConfig;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.parkctrl.service.IPmsParkPaymentService;
import com.sunjoy.parkctrl.service.impl.PmsParkDeviceCommunicator;
import com.sunjoy.parking.entity.PmsParkOrder;
import com.sunjoy.parking.entity.PmsParkPayment;
import com.sunjoy.parking.enums.ParkOrderStatusEnum;
import com.sunjoy.parking.enums.ParkPaymentChannelEnum;
import com.sunjoy.parking.enums.ParkPaymentMethods;
import com.sunjoy.parking.enums.ParkPaymentStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

/**
 * 支付宝结果通知处理类
 *
 * @author Habib
 * @date 2024/11/26
 */
@Slf4j
@RestController
@RequestMapping("/alipay")
public class AlipayNotifyController {
    @Autowired
    private IPmsParkPaymentService pmsParkPaymentService;

    @Autowired
    private IPmsParkOrderService pmsParkOrderService;

    @Autowired
    private SignatureConfig signatureConfig;

    @Autowired
    private PmsParkDeviceCommunicator parkDeviceCommunicator;

    @PostMapping("/notify")
    @Transactional
    public String handlePaymentNotify(HttpServletRequest request) {
        try {
            // 1. 获取请求参数
            TreeMap<String, String> params = new TreeMap<>();
            request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));

            // 2. 验签
            boolean isVerified = AlipaySignature.rsaCheckV1(params, getAlipayPublicKey(params.get("seller_id")), "UTF-8", "RSA2");
            if (signatureConfig.isAlipayValid() && !isVerified) {
                // 验签失败
                return "fail";
            }

            // 3. 根据业务逻辑处理支付结果
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                // 处理成功的支付结果，例如更新订单状态

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
            // 返回成功响应给支付宝
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时返回失败
            return "fail";
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
        parkPayment.setTransactionId(params.get("trade_no"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        parkPayment.setPaymentTime(LocalDateTime.parse(params.get("notify_time"), formatter));
        parkPayment.setRemark(null);
        //保存支付凭证
        this.pmsParkPaymentService.createPayment(parkPayment);

        //更新订单状态,已支付
        parkOrder.setStatus(ParkOrderStatusEnum.Paid.getCode());
        this.pmsParkOrderService.updateParkOrder(parkOrder);
    }

    /**
     * 根据商户号取得该商户的公钥
     *
     * @param sellerId
     * @return
     */
    private String getAlipayPublicKey(String sellerId) {
        return "";
    }
}