package org.chiu.micro.gateway.user;

import org.chiu.micro.gateway.dto.UserEntityDto;
import org.chiu.micro.gateway.rpc.wrapper.UserHttpServiceWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public final class UserDetailsServiceImpl implements UserDetailsService {

	private final UserHttpServiceWrapper userHttpServiceWrapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loadUserByUsernameFromDb(username);
	}

	private LoginUser loadUserByUsernameFromDb(String username) {

		UserEntityDto user = userHttpServiceWrapper.findByUsernameOrEmailOrPhone(username);

		Long userId = user.getId();
		List<String> roleCodes = userHttpServiceWrapper.findRoleCodesDecorByUserId(userId);

		//通过User去自动比较用户名和密码
		return new LoginUser(username,
				user.getPassword(),
				true,
				true,
				true,
				user.getStatus() == 0,
				AuthorityUtils.createAuthorityList(roleCodes),
				userId);
	}
}
