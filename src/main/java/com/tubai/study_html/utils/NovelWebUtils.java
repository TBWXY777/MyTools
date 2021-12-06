package com.tubai.study_html.utils;

import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;


public class NovelWebUtils {
    /**
     * 上传文件结果转换为本系统的结果
     * @param result
     * @return
     */
    public static JsonResult<Object> forReturn(cn.novelweb.tool.http.Result<Object> result) {
        if ("200".equals(result.getCode()) || "201".equals(result.getCode())) {
            return JsonResult.create(StatusCode.OK, result.getMessage());
        } else if ("206".equals(result.getCode())) {
            return JsonResult.create(StatusCode.ERROR, result.getMessage(),result.getData());
        } else {
            return JsonResult.create(StatusCode.ERROR, result.getMessage());
        }
    }

}
