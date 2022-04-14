package com.maxqiu.demo.utils;

import javax.servlet.http.HttpServletRequest;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信通知处理工具
 */
@Slf4j
public class WeixinNotifyUtils {
    /**
     * 获取请求头签名
     */
    public static SignatureHeader getSignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(request.getHeader("Wechatpay-Signature"));
        signatureHeader.setNonce(request.getHeader("Wechatpay-Nonce"));
        signatureHeader.setSerial(request.getHeader("Wechatpay-Serial"));
        signatureHeader.setTimeStamp(request.getHeader("Wechatpay-TimeStamp"));
        return signatureHeader;
    }
}
