package com.tubai.study_html.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tubai.study_html.entity.Files;
import com.tubai.study_html.pojo.GetFileDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LEON
 * @since 2020-06-09
 */
public interface FilesMapper extends BaseMapper<Files> {
    /**
     * 获取文件列表
     *
     * @return
     */
    List<GetFileDto> selectFileList();

    /**
     * 判断文件是否已存在
     *
     * @param fileName
     * @return
     */
    boolean fileIsExist(@Param("fileName") String fileName);
}
