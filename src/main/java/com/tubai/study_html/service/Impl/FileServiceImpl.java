package com.tubai.study_html.service.Impl;

import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tubai.study_html.entity.Files;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.exception.ServiceException;
import com.tubai.study_html.mapper.FilesMapper;
import com.tubai.study_html.pojo.AddFileDto;
import com.tubai.study_html.pojo.GetFileDto;
import com.tubai.study_html.service.FileService;
import com.tubai.study_html.utils.NovelWebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final String RESOURCES_PATH = FileServiceImpl.class.getClassLoader().getResource("").getPath();

    private final FilesMapper filesMapper;

    private String savePath = RESOURCES_PATH + "public/file-manager/files";

    private String confFilePath = RESOURCES_PATH + "public/file-manager/conf";;

    @Override
    public JsonResult<List<GetFileDto>> getFileList(Integer pageNo, Integer pageSize) {
        try {
            PageHelper.startPage(pageNo, pageSize);
            List<GetFileDto> result = filesMapper.selectFileList();
            PageInfo<GetFileDto> pageInfo = new PageInfo<>(result);
            return JsonResult.create(StatusCode.OK, "查询成功", pageInfo.getList(),pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取文件列表出错", e);
        }
        return JsonResult.create(StatusCode.ERROR, "查询失败");
    }

    @Override
    public JsonResult<Object> checkFileMd5(String md5, String fileName) {
        try {
            cn.novelweb.tool.http.Result result = LocalUpload.checkFileMd5(md5, fileName, confFilePath, savePath);
            return NovelWebUtils.forReturn(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return JsonResult.create(StatusCode.ERROR, "上传失败");
    }

    @Override
    public JsonResult breakpointResumeUpload(UploadFileParam param, HttpServletRequest request) {
        try {
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(param, confFilePath, savePath, 5242880L, request);
            return NovelWebUtils.forReturn(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return JsonResult.create(StatusCode.ERROR, "上传失败");
    }

    @Override
    public JsonResult<String> addFile(AddFileDto dto) {
        try {
            Files file = new Files();
            BeanUtils.copyProperties(dto, file);
            if (filesMapper.fileIsExist(dto.getFileName())) {
                return JsonResult.create(StatusCode.OK, "添加成功");
            } else if (filesMapper.insert(file.setFilePath(dto.getFileName())) == 1) {
                return JsonResult.create(StatusCode.OK, "添加成功");
            }
        } catch (Exception e) {
            log.error("添加文件出错", e);
        }
        return JsonResult.create(StatusCode.ERROR, "添加失败");
    }

    @Override
    public InputStream getFileInputStream(String id) {
        try {
            Files files = filesMapper.selectById(id);
            File file = new File(savePath + File.separator + files.getFilePath());
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("获取文件输入流出错", e);
        }
        return null;
    }

    @Override
    public void playImgAndPDF(String id, HttpServletResponse response) {
        try(FileInputStream fis = (FileInputStream)getFileInputStream(id)){
            byte[] data = new byte[fis.available()];
            fis.read(data);
            response.getOutputStream().write(data);
        }
        catch (IOException e){
            throw new ServiceException(e.getMessage(),StatusCode.ERROR);
        }
    }

    @Override
    public void playMp4(String id,HttpServletResponse response) {
        try(FileInputStream fis = (FileInputStream)getFileInputStream(id)){
            String fileName = getFileDetails(id).getData().getFileName();
            byte[] data = new byte[fis.available()];
            fis.read(data);
            response.setContentType("video/mp4");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();
            os.write(data);
        }
        catch (IOException e){
            throw new ServiceException(e.getMessage(),StatusCode.ERROR);
        }
    }

    @Override
    public JsonResult<Files> getFileDetails(String id) {
        try {
            Files files = filesMapper.selectById(id);
            return JsonResult.create(StatusCode.OK ,"查询成功",files);
        } catch (Exception e) {
            log.error("获取文件详情出错", e);
        }
        return JsonResult.create(StatusCode.ERROR, "查询失败");
    }
}
