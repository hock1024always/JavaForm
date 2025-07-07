package com.zxzy.zx_userservice.mapper;



import com.github.yulichang.base.MPJBaseMapper;
import com.zxzy.zx_userservice.domain.po.User;
import com.zxzy.zx_userservice.domain.vo.ResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author uuy
 * @since 2025-04-08
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
    public User LoginWithNameAndPassword(/*@Param("name")*/ String name, /*@Param("password")*/ String password);
    public User FindUserByName(String name);
    //public User LoginWithEmailAndVerificationCode(String email, String VerificationCode);
    public User FindUserByEmail(String email);
    //public User LoginWithPhoneAndVerificationCode(String phone);
    public User LoginWithEmailAndPassword(String email, String password);
    public void AlterUserStatusUpName(String username);
    public void AlterUserStatusUpEmail(String email);
    public void Logout(String username);

    public void InsertNameAndPassword(String id, String email, String username, String password);

    public String getMaxId();

    public void deleteuser(String id);

    User FindUserById(String id);
}