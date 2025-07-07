package com.homework.zx_msgservice.service.impl;

import com.homework.zx_msgservice.domain.vo.ResultVO;
import com.homework.zx_msgservice.service.TemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homework.zx_msgservice.domain.po.Template;
import com.homework.zx_msgservice.mapper.MsgMapper;
import com.homework.zx_msgservice.mapper.MessageMapper;
import com.homework.zx_msgservice.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private MsgMapper msgMapper;

    @Override
    public ResultVO getTemplateById(String templateId) {
        Template template=msgMapper.selectTemplateById(templateId);
        return ResultVO.success(template);
    }
    
    public ResultVO<List<Template>> getAllTemplates() {
        try {
            QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
            List<Template> templates = msgMapper.selectList(queryWrapper);
            return ResultVO.success(templates);
        } catch (Exception e) {
            // 这里可以根据实际情况记录日志
            return ResultVO.error("获取模板列表失败：" + e.getMessage());
        }
    }

    public ResultVO updateTemplate(String id, String newTemplateContent) {
        Template template=null;
        try{template = msgMapper.selectTemplateById(id);}catch(Exception e){
            e.printStackTrace();
            return ResultVO.error("null templateId");
        }
        if (template != null) {
            int rows = msgMapper.updateTemplateContent(id, newTemplateContent);
            if (rows > 0) {
                return ResultVO.success("成功");
            } else {
                return ResultVO.error("失败");
            }
        }
        return ResultVO.error("失败");
    }

    public ResultVO createTemplate(String content, String templateName) {
        if(content==null||templateName==null){
            return ResultVO.error("空值");
        }
        String templateId= UUID.randomUUID().toString();
        int rows = msgMapper.createTemplatesql(content, templateId, templateName);
        if(rows>0){

            return ResultVO.success("成功");
        }else{
            return ResultVO.error("失败");
        }
    }

    @Override
    public ResultVO deleteTemplate(String templateId) {
        int rows = msgMapper.deletetemplate(templateId);
        if (rows > 0) {
            return ResultVO.success("成功");
        } else {
            return ResultVO.error("失败");
        }
    }
}
