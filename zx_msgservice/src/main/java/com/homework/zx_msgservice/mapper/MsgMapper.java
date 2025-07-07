package com.homework.zx_msgservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.zx_msgservice.domain.po.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MsgMapper extends BaseMapper<Template> {
    public String getTemplateById(String templateId);

    public Template selectTemplateById(String templateId);

//    public void save(Template template);

    public int updateTemplateContent(String id, String content);

    public int createTemplatesql(@Param("content") String content, @Param("templateId") String templateId, @Param("templateName") String templateName);

    public int deletetemplate(String templateId);
}
