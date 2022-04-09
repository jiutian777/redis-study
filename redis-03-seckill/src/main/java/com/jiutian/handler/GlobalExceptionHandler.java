package com.jiutian.handler;

import com.jiutian.handler.error.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * ClassName:GlobalExceptionHandler
 * Package:com.jiutian.handler
 * Description:
 *
 * @Date: 2021/12/11 11:01
 * @Author: jiutian
 */
@ControllerAdvice
public class GlobalExceptionHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param e  自定义的业务异常
     * @return  结果体
     */
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResultBody<T> myExceptionHandler(MyException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param e    空指针异常
     * @return      结果体
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

    /**
     * @param e  重复键值异常
     * @return  结果体
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(DuplicateKeyException e) {
        logger.error("发生重复键值异常！原因是:", e);
        return ResultBody.error("键值不能重复");
    }

    /**
     * @param e   文件写入异常
     * @return  结果体
     */
    @ExceptionHandler(value = IOException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(IOException e) {
        logger.error("文件写入异常！原因是:", e);
        return ResultBody.error("文件写入异常");
    }

    /**
     * 处理其他异常
     *
     * @param e     未知异常
     * @return      结果体
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
}
