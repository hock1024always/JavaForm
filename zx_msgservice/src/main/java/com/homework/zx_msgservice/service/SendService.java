package com.homework.zx_msgservice.service;


import com.homework.zx_msgservice.domain.dto.SendDTO;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author uuy
 * @since 2025-04-29
 */
public interface SendService{

    void instantSend(SendDTO sendDTO);

    void timing(SendDTO sendDTO);

    void periodSend(SendDTO sendDTO);
}
