package com.maxqiu.demo.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.bean.notify.OriginNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayApplyFundFlowBillV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayApplyTradeBillV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayApplyBillV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.GsonBuilder;
import com.maxqiu.demo.properties.WxPayProperties;
import com.maxqiu.demo.utils.WeixinNotifyUtils;
import com.maxqiu.demo.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Max_Qiu
 */
@RestController
@RequestMapping("/pay/native")
@Slf4j
public class NativePayController {
    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WxPayProperties wxPayProperties;

    /**
     * 商户订单号，即用户下单时后端生成的订单号
     */
    private String orderNo = "ORDER_20220414235512423";

    /**
     * 商户退款单号，即用户发起退款时记录的退款单号
     */
    private String refundNo = "REFUND_20220414235512423";

    /**
     * Native下单（即创建支付二维码）
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_1.shtml
     *
     * 当订单创建完成后，后端调用Native下单接口获取支付二维码地址
     *
     * 前端将二维码地址转换为图片进行显示
     */
    @GetMapping("order-create")
    public Result<String> createOrder() throws WxPayException {
        // 构建请求对象，appId、mchid、notify_url会在执行时中自动添加，无需手动设置
        // 其他非必填信息参考官方文档并设置
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        // 商品描述
        request.setDescription("测试商品111");
        // 商户订单号
        request.setOutTradeNo(orderNo);
        // 订单金额
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        // 总金额（单位为分，只能为整数，即元*100）
        amount.setTotal(1);
        request.setAmount(amount);
        // 调用接口，并接收返回的支付二维码地址
        String codeUrl = wxPayService.createOrderV3(TradeTypeEnum.NATIVE, request);
        // TODO 支付二维码需要存储在订单中（或缓存），防止重复创建
        return Result.success(codeUrl);
    }

    /**
     * 查询订单（即查询支付状态）
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml
     *
     * 当前端展示之后二维码之后，定时任务向后端查询订单支付状态，后端向微信端查询订单状态，如果支付完成，则更新数据库并返回前端支付成功
     */
    @GetMapping("order-query")
    public Result<WxPayOrderQueryV3Result> queryOrder() throws WxPayException {
        // 传入 微信支付系统生成的订单号 或 商户订单号 ，此处使用商户订单号查询，mchid会在执行时中自动添加，无需手动设置
        WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(null, orderNo);
        // 也可以创建请求对象，mchid会在执行时中自动添加，无需手动设置（太麻烦）
        // WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
        // request.setOutTradeNo(orderNo);
        // WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(request);
        // TODO 根据查询结果处理订单
        log.debug("查询订单 ---> orderNo：" + result.getOutTradeNo() + "\t 订单状态：" + result.getTradeState());
        return Result.success(result);
    }

    /**
     * 关闭订单（即关闭支付二维码）
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_3.shtml
     *
     * 当订单超时未支付时，关闭系统订单同时关闭支付二维码，使其无效
     */
    @GetMapping("order-close")
    public Result<?> closeOrder() throws WxPayException {
        // 创建请求对象，mchid会在执行时中自动添加，无需手动设置
        WxPayOrderCloseV3Request request = new WxPayOrderCloseV3Request();
        // 传入商户订单号号
        request.setOutTradeNo(orderNo);
        // 执行关闭，无返回结果
        wxPayService.closeOrderV3(request);
        // 也可以直接传入商户订单号关闭
        // wxPayService.closeOrderV3(orderNo);
        return Result.success();
    }

    private final ReentrantLock orderLock = new ReentrantLock();
    private final ReentrantLock refundLock = new ReentrantLock();

    /**
     * 1. 支付通知（必须）（即用户支付完成后，微信端的回调接口）
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_5.shtml
     *
     * 当收到支付时，修改本地订单为支付成功状态，并返回给微信处理成功的消息
     *
     * 2. 退款通知（可选）（即调用申请退款接口后的回调通知）
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_11.shtml
     *
     * 当收到退款通知时，根据退款完成情况修改系统订单状态，比如退款中改为已退款
     **/
    @PostMapping("/notify")
    public Map<String, String> nativeNotify(HttpServletRequest request, HttpServletResponse response, @RequestBody String notifyData) {
        log.debug("======= 接收到通知 =========");
        // 获取请求头信息
        SignatureHeader signatureHeader = WeixinNotifyUtils.getSignatureHeader(request);
        // 将请求体json字符串转换为实体
        OriginNotifyResponse notifyResponse = new GsonBuilder().create().fromJson(notifyData, OriginNotifyResponse.class);
        // 支付成功通知
        if ("TRANSACTION.SUCCESS".equals(notifyResponse.getEventType())) {
            // 获取锁
            if (orderLock.tryLock()) {
                try {
                    // 解析支付结果通知
                    WxPayOrderNotifyV3Result result = wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
                    // TODO 此处根据返回结果处理订单信息
                    log.debug("支付成功 ---> orderNo：" + result.getResult().getOutTradeNo());
                    // 返回成功（无需数据，系统状态码为200即可）
                    return new HashMap<>();
                } catch (Exception e) {
                    // 支付结果解析异常/订单处理异常
                    log.error("支付通知处理异常：", e);
                    response.setStatus(500);// 支付成功通知处理失败时需要将状态码修改为5xx/4xx，微信才会重新发送回调
                    Map<String, String> map = new HashMap<>();
                    map.put("code", "ERROR");
                    map.put("message", "系统异常");
                    return map;
                } finally {
                    // 释放锁
                    orderLock.unlock();
                }
            } else {
                // 锁获取失败，返回异常，等待下次消息
                response.setStatus(503);// 支付成功通知处理失败时需要将状态码修改为5xx/4xx，微信才会重新发送回调
                Map<String, String> map = new HashMap<>();
                map.put("code", "FAIL");
                map.put("message", "系统繁忙");
                return map;
            }
        }
        // 退款通知
        else {
            // 获取锁
            if (refundLock.tryLock()) {
                try {
                    // 解析退款结果通知
                    WxPayRefundNotifyV3Result result = wxPayService.parseRefundNotifyV3Result(notifyData, signatureHeader);
                    // TODO 此处根据返回结果处理退款信息
                    log.debug("退款结果 ---> orderNo：" + result.getResult().getOutTradeNo() + " \t 退款状态：" + result.getResult().getRefundStatus());
                    // 返回成功标识
                    Map<String, String> map = new HashMap<>();
                    map.put("code", "SUCCESS");
                    return map;
                } catch (WxPayException e) {
                    log.error(e.getMessage());
                    Map<String, String> map = new HashMap<>();
                    map.put("code", "ERROR");
                    map.put("message", "系统异常");
                    return map;
                } finally {
                    refundLock.unlock();
                }
            } else {
                // 锁获取失败，返回异常，等待下次消息
                Map<String, String> map = new HashMap<>();
                map.put("code", "FAIL");
                map.put("message", "系统繁忙");
                return map;
            }
        }
    }

    /**
     * 申请退款
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_9.shtml
     *
     * 用户发起退款申请，后端向微信端发起退款申请
     */
    @GetMapping("/refund-create")
    public Result<WxPayRefundV3Result> createRefund() throws WxPayException {
        // 构建请求对象
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        // 微信支付系统生成的订单号 和 商户订单号 二选一，此处使用商户订单号查询
        request.setOutTradeNo(orderNo);
        // 商户退款单号
        request.setOutRefundNo(refundNo);
        // 退款结果回调url，需要从配置类中手动传入，否则微信端不会发送回调信息
        request.setNotifyUrl(wxPayProperties.getNotifyUrl());
        // 金额信息
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        // 退款金额（单位为分，只能为整数。常识：不大于订单总金额）
        amount.setRefund(1);
        // 原订单金额（单位为分，只能为整数。）
        amount.setTotal(1);
        // 退款币种（必填，只支持CNY）
        amount.setCurrency("CNY");
        request.setAmount(amount);
        // 发起退款申请
        WxPayRefundV3Result result = wxPayService.refundV3(request);
        // TODO 处理返回接口，例如存储微信支付退款单号
        log.debug("退款已发起 ---> 退款单号：" + result.getRefundId());
        return Result.success(result);
    }

    /**
     * 查询退款
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_10.shtml
     *
     * 后端向微信端发起退款申请后，若不使用微信的回调消息，则可以手动通过查询退款接口查询进度
     */
    @GetMapping("/refund-query")
    public Result<WxPayRefundQueryV3Result> queryRefund() throws WxPayException {
        // 传入商户退款单号查询
        WxPayRefundQueryV3Result result = wxPayService.refundQueryV3(refundNo);
        // 也可构建request（太麻烦）
        // WxPayRefundQueryV3Request request = new WxPayRefundQueryV3Request();
        // request.setOutRefundNo(refundNo);
        // WxPayRefundQueryV3Result result = wxPayService.refundQueryV3(request);
        // TODO 处理结果，将退款是否成功更新到退款记录中
        log.debug("已查询退款结果 ---> 退款单号：" + result.getRefundId() + "\t 退款结果：" + result.getStatus());
        return Result.success(result);
    }

    /**
     * 申请 + 下载交易账单
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_6.shtml
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_8.shtml
     */
    @GetMapping("bill-trade")
    public void tradeBill(HttpServletResponse response) throws WxPayException {
        // 构建请求对象
        WxPayApplyTradeBillV3Request request = new WxPayApplyTradeBillV3Request();
        // 账单日期（格式yyyy-MM-dd）
        request.setBillDate("2022-04-13");
        // 账单类型
        request.setBillType(WxPayConstants.BillType.ALL);
        // 发起申请，获取下载链接
        WxPayApplyBillV3Result result = wxPayService.applyTradeBill(request);
        // 发起下载，获取流
        InputStream inputStream = wxPayService.downloadBill(result.getDownloadUrl());
        // 返回文件流
        returnFile(inputStream, response, "bill-trade.csv");
    }

    /**
     * 申请 + 下载资金账单
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_7.shtml
     *
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_8.shtml
     */
    @GetMapping("fund-trade")
    public void tradeFund(HttpServletResponse response) throws WxPayException {
        // 构建请求对象
        WxPayApplyFundFlowBillV3Request request = new WxPayApplyFundFlowBillV3Request();
        // 账单日期（格式yyyy-MM-dd）
        request.setBillDate("2022-04-13");
        // 发起申请，获取下载链接
        WxPayApplyBillV3Result result = wxPayService.applyFundFlowBill(request);
        // 发起下载，获取流
        InputStream inputStream = wxPayService.downloadBill(result.getDownloadUrl());
        // 返回文件流
        returnFile(inputStream, response, "fund-trade.csv");
    }

    /**
     * 返回文件流
     */
    private void returnFile(InputStream inputStream, HttpServletResponse response, String filename) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            System.out.println("os异常");
        }
    }
}
