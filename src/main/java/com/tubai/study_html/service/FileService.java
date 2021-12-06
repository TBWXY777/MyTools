package com.tubai.study_html.service;

import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.tubai.study_html.entity.Files;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.pojo.AddFileDto;
import com.tubai.study_html.pojo.GetFileDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    /**
     * 获取文件输入流
     *
     * @param id
     * @return
     */
    InputStream getFileInputStream(String id);

    void playImgAndPDF(String id, HttpServletResponse response);

    void playMp4(String id,HttpServletResponse response);

    /**
     * 获取指定文件详情
     *
     * @param id
     * @return
     */
    JsonResult<Files> getFileDetails(String id);

    /**
     * 分页获取文件信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    JsonResult<List<GetFileDto>> getFileList(Integer pageNo, Integer pageSize);

    /**
     * 检查文件MD5
     *
     * @param md5
     * @param fileName
     * @return
     */
    JsonResult<Object> checkFileMd5(String md5, String fileName);

    /**
     * 断点续传
     *
     * @param param
     * @param request
     * @return
     */
    JsonResult<Object> breakpointResumeUpload(UploadFileParam param, HttpServletRequest request);

    /**
     * 添加文件
     *
     * @param dto
     * @return
     */
    JsonResult<String> addFile(AddFileDto dto);
}
