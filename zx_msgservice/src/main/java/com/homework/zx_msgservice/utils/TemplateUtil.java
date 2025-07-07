package com.homework.zx_msgservice.utils;

import com.homework.zx_msgservice.mapper.MsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TemplateUtil {
    /**
     * 模板填充
     * @param templateId 模板id
     * @param fillContent 模板填充内容
     * @return  返回填充后的模板 把Object改了哦，改了之后记得把Object改成对应的类型
     */
    @Autowired
    private MsgMapper msgMapper;

    public String fill(String templateId, String... fillContents) {
        String template = msgMapper.getTemplateById(templateId);
        if (template == null) {
            return null;
        }
        for (int i = 0; i < fillContents.length; i++) {                    /**有几个占位符，就替换几个*/
            String placeholder = "{content" + (i + 1) + "}";
            template = template.replace(placeholder, fillContents[i]);
        }
        return template;                                                   /**返回字符串*/
    }
}
