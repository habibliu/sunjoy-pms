package com.sunjoy.parkctrl.pojo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;


/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/26
 */
@Setter
@XmlRootElement(name = "xml")
public class WeChatPayNotify {
    private String returnCode;
    private String resultCode;
    private String outTradeNo;
    private String transactionId;
    private String totalFee;
    private String timeEnd;
    private String sign;

    @XmlElement(name = "return_code")
    public String getReturnCode() {
        return returnCode;
    }

    @XmlElement(name = "result_code")
    public String getResultCode() {
        return resultCode;
    }

    @XmlElement(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    @XmlElement(name = "out_trade_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }


    @XmlElement(name = "total_fee")
    public String getTotalFee() {
        return totalFee;
    }

    @XmlElement(name = "time_end")
    public String getTimeEnd() {
        return timeEnd;
    }

    @XmlElement(name = "sign")
    public String getSign() {
        return sign;
    }

}