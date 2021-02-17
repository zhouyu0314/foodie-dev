package com.zy.controller.center;

import com.zy.controller.BaseController;
import com.zy.pojo.Users;
import com.zy.pojo.bo.center.CenterUserBO;
import com.zy.resource.FileUpload;
import com.zy.service.center.CenterUserService;
import com.zy.utils.CookieUtils;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息接口相关的api接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterUserController.class);

    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@ApiParam(name = "userId", value = "用户id", required = true)
                                  @RequestParam String userId,
                                  @RequestBody @Valid CenterUserBO centerUserBO,//@Valid通过此注解开启Hibernate验证，错误信息将保存到BindingResult
                                  BindingResult bindingResult,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            //判断BindingResult是否保存错误的验证信息，如果有则直接return
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = this.getErrors(bindingResult);
                return IMOOCJSONResult.errorMap(errors);
            }
            Users result = centerUserService.updateUserInfo(userId, centerUserBO);
            result = this.setNull(result);

            //更新cookie
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);//是否加密);
            // TODO: 2021/2/16 后续增加token 整合到redis 分布式会话 
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("修改用户信息失败！");
        }
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(@ApiParam(name = "userId", value = "用户id", required = true)
                                      @RequestParam String userId,
                                      @ApiParam(name = "file", value = "用户头像", required = true)
                                              MultipartFile file,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        //定义头像保存的地址
        //String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每一个用户增加一个userId，用于区分不同用户上传
        String uploadPathPrefix = "/" + userId + "/";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            //开始文件上传
            if (file != null) {
                //获得文件上传的文件原始名称
                String fileName = file.getOriginalFilename();
                if (!StringUtils.isBlank(fileName)) {
                    //文件重命名asd.png -> ["asd","png"]
                    String[] fileNameArr = fileName.split("\\.");
                    //获取后缀
                    String suffix = fileNameArr[fileNameArr.length - 1];
                    //判断后缀
                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg")) {
                        return IMOOCJSONResult.errorMsg("图片格式不正确！");
                    }
                    //文件名称重组(覆盖式，若要增量式，则需要不重名)
                    String newFileName = "face-" + userId + "-" + System.currentTimeMillis() + "." + suffix;

                    //上传的头像最终保存的位置  C:/workspaces/images/foodie/faces/userId/face-userId.png
                    String finalFacePath = fileSpace + uploadPathPrefix + newFileName;

                    File outFile = new File(finalFacePath);

                    //C:/workspaces/images/foodie/faces/userId 不为空
                    if (outFile.getParentFile() != null) {
                        //吹昂见文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    //将输入流中的信息拷贝到输入流
                    IOUtils.copy(inputStream, fileOutputStream);
                    //更新用户头像到数据库
                    Users result = centerUserService.updateUserFace(userId, fileUpload.getImageServerUrl() + uploadPathPrefix + newFileName);
                    result = this.setNull(result);

                    //更新cookie
                    CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);//是否加密);
                    // TODO: 2021/2/16 后续增加token 整合到redis 分布式会话
                    return IMOOCJSONResult.ok(result);
                } else {
                    return IMOOCJSONResult.errorMsg("上传文件的名字不可为空！");
                }
            } else {
                return IMOOCJSONResult.errorMsg("上传文件不能为空！");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("头像上传异常！");
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * 将错误信息取出放入到Map
     *
     * @param bindingResult
     * @return
     */
    private Map<String, String> getErrors(BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : errorList) {
            //发生验证错误所对应的某一个属性
            String errorField = fieldError.getField();
            //发生验证错误所对应的信息
            String errorMessage = fieldError.getDefaultMessage();
            map.put(errorField, errorMessage);
        }
        return map;
    }

    private Users setNull(Users result) {
        result.setPassword(null);
        result.setBirthday(null);
        result.setCreatedTime(null);
        result.setUpdatedTime(null);
        result.setMobile(null);
        result.setRealname(null);
        return result;
    }
}
