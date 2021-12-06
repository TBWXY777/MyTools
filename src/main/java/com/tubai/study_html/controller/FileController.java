package com.tubai.study_html.controller;

import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.tubai.study_html.constant.SysConstant;
import com.tubai.study_html.entity.Files;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.exception.ServiceException;
import com.tubai.study_html.pojo.AddFileDto;
import com.tubai.study_html.pojo.GetFileDto;
import com.tubai.study_html.service.FileService;
import com.tubai.study_html.utils.EmptyUtils;
import com.tubai.study_html.utils.EncodingUtils;
import com.tubai.study_html.utils.InputStreamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/file")
@Api(tags = "文件相关接口")
@RequiredArgsConstructor//我们把需要注入的属性，修改成 final 类型的
public class FileController {
    private final FileService fileService;
    /**
     * 文件列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @RequiresRoles("admin")
    public JsonResult<List<GetFileDto>> getFileList(@RequestParam Integer pageNo, @RequestParam Integer pageSize) throws IOException {
        return fileService.getFileList(pageNo, pageSize);
    }

    /**
     * 添加文件
     * 断点续传完成后上传文件信息进行入库操作
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @RequiresRoles("admin")
    public JsonResult<String> addFile(@RequestBody AddFileDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.create(StatusCode.PARAM_ERROR, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return fileService.addFile(dto);
    }

    /**
     * 检查文件MD5（文件MD5若已存在进行秒传）
     *
     * @param md5
     * @param fileName
     * @return
     */
    @GetMapping(value = "/check-file")
    @RequiresRoles("admin")
    public JsonResult<Object> checkFileMd5(String md5, String fileName) {
        return fileService.checkFileMd5(md5, fileName);
    }

    /**
     * 断点续传方式上传文件：用于大文件上传
     *
     * @param param
     * @param request
     * @return
     */
    @PostMapping(value = "/breakpoint-upload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;charset=UTF-8")
    @RequiresRoles("admin")
    public JsonResult<Object> breakpointResumeUpload(UploadFileParam param, HttpServletRequest request) {
        return fileService.breakpointResumeUpload(param, request);
    }

    /**
     * 图片/PDF查看
     * @param id
     * @return
     */
    @GetMapping(value = "/view/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public void viewFilesImage(@PathVariable String id,HttpServletResponse response){
        JsonResult<Files> fileDetails = fileService.getFileDetails(id);
        String suffix = null;
        if (fileDetails.getCode().equals(StatusCode.OK)) {
            suffix = fileDetails.getData().getSuffix();
            if (!ObjectUtils.isEmpty(suffix)){
                if((!SysConstant.IMAGE_TYPE.contains(suffix) && !SysConstant.VIDEO_TYPE.contains(suffix)))
                    throw new ServiceException("非图片/PDF/mp4类型请先下载",StatusCode.PARAM_ERROR);
            }
        }else{
            throw new ServiceException(fileDetails.getDescription(),fileDetails.getCode());
        }
        if(SysConstant.IMAGE_TYPE.contains(suffix)){
            fileService.playImgAndPDF(id,response);
        }
        else
            fileService.playMp4(id,response);
    }
    /**
     * 文件下载
     * @param id
     * @param isSource
     * @param request
     * @param response
     */
    @GetMapping(value = "/download/{id}")
    public void viewFilesImage(@PathVariable String id, @RequestParam(required = false) Boolean isSource, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            JsonResult<Files> fileDetails = fileService.getFileDetails(id);
            if (!fileDetails.getCode().equals(StatusCode.OK)) {
                throw new ServiceException(fileDetails.getDescription(),fileDetails.getCode());
            }
            String filename = (!EmptyUtils.basicIsEmpty(isSource) && isSource) ? fileDetails.getData().getFileName() : fileDetails.getData().getFilePath();
            inputStream = fileService.getFileInputStream(id);
            response.setHeader("Content-Disposition", "attachment;filename=" + EncodingUtils.convertToFileName(request, filename));
            // 获取输出流
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("文件下载出错", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
