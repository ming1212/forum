package com.yuanfang.forum.mapper;

import com.yuanfang.forum.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface LoginTicketMapper {

    /**
     * 插入一条登录凭证
     * @param loginTicket
     * @return
     */
    @Insert({
            "insert into login_ticket (user_id, ticket, status, expired) ",
            "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据凭证 查询一条凭证记录
     * @param ticket
     * @return
     */
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket getLoginTicket(String ticket);

    /**
     * 更新凭证的状态
     * @param ticket
     * @param status
     * @return
     */
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    int updateStatus(String ticket, int status);

}
