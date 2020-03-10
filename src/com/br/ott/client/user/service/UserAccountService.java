package com.br.ott.client.user.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.user.entity.UserAccount;
import com.br.ott.client.user.mapper.UserAccountMapper;
import com.br.ott.common.util.Constants;
import com.br.ott.common.util.PagedModelList;

/**
 * 用户管理service
 * @author 陈登民
 *
 */
@Service
public class UserAccountService {

	@Autowired
	private UserAccountMapper userMapper;

	/**
	 * 查询用户列表
	 * @param pageNo
	 * @param user
	 * @return
	 */
	public PagedModelList<UserAccount> findUsers(int pageNo, UserAccount user) {
		if(user != null && user.getNickName() != null){
			user.setNickName("%"+user.getNickName()+"%");
		}
		int count = userMapper.findCountUsers(user);

		RowBounds rowBounds = new RowBounds((pageNo - 1) * Constants.PAGE_DATA, Constants.PAGE_DATA);

		List<UserAccount> users = userMapper.findUsers(user, rowBounds);

		PagedModelList<UserAccount> modelList = new PagedModelList<UserAccount>(pageNo, Constants.PAGE_DATA, count);
		modelList.addModels(users);
		return modelList;
	}

	/**
	 * 添加用户信息
	 * @param user
	 */
	public void addUser(UserAccount user) {
		userMapper.addUser(user);
	}

	/**
	 * 删除用户
	 * @param id
	 */
	public void delUser(String id) {
		userMapper.delUser(id);
	}

	public UserAccount getUserAccount(String id) {
		return userMapper.getUserAccount(id);
	}

	public void modifyUserAccount(UserAccount account) {
		userMapper.modifyUserAccount(account);
	}

}

