package com.homework.zx_msgservice.mapper;

import com.homework.zx_msgservice.domain.po.MailCarrier;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface MailCarrierMapper {
//    @Select("select * from mailcarrier where MCid=#{id}")
    List<MailCarrier> selectCarrier(MailCarrier mailCarrier);
    @Update("update mailcarrier set host=#{host},Username=#{Username},Password=#{Password},encoding=#{encoding},port=#{port},SmtpState=#{SmtpState},StarttlsEnable=#{StarttlsEnable} Where MCid=#{MCid}")
    void updateCarrier(MailCarrier mailCarrier);
    @Insert("insert into mailcarrier (host, Username, Password, encoding, port,SmtpState,StarttlsEnable) VALUES (#{host}, #{Username}, #{Password}, #{encoding}, #{port},#{SmtpState},#{StarttlsEnable})")
    void insertCarrier(MailCarrier mailCarrier);
    @Delete("delete from mailcarrier where MCid =#{id}")
    void deleteCarrier(String id);
}
