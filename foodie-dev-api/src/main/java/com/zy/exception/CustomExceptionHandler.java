package com.zy.exception;

import com.zy.utils.IMOOCJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 异常拦截 会拦截所有加了controller注解的类
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 只要有controller抛出此MaxUploadSizeExceededException，就会到此方法
     * @param e
     * @return
     */
    //MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        LOGGER.error(e.getMessage());
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过500kb！");
    }
}
