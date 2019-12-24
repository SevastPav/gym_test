package org.xtremebiker.jsfspring.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import java.util.Collections;

@Component
@SessionScope
public class ExtraInfoBean {

	private final UserProfileRepository userProfileRepository;

	public ExtraInfoBean(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	public int getAdminCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.ADMIN)).size();
	}

	public int getClientCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.USER)).size();
	}

	public int getTrainerCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.TRAINER)).size();
	}

	public String getName() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

}
