package com.softserve.mosquito.controllers.social;//package com.softserve.mosquito.controllers.social;

import com.softserve.mosquito.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;



@Service
public class GoogleProvider   {

	private static final String REDIRECT_CONNECT_GOOGLE = "redirect:/login";
	private static final String GOOGLE = "google";

   	@Autowired
    	BaseProvider socialLoginBean ;


	public String getGoogleUserData(Model model, User userForm) {

		ConnectionRepository connectionRepository = socialLoginBean.getConnectionRepository();
		if (connectionRepository.findPrimaryConnection(Google.class) == null) {
			return REDIRECT_CONNECT_GOOGLE;
		}

		populateUserDetailsFromGoogle(userForm);
		model.addAttribute("loggedInUser",userForm);
		return "user";
	}


	protected void populateUserDetailsFromGoogle(User userform) {
		Google google = socialLoginBean.getGoogle();
		Person googleUser = google.plusOperations().getGoogleProfile();
		userform.setEmail(googleUser.getAccountEmail());
		userform.setFirstName(googleUser.getGivenName());
		userform.setLastName(googleUser.getFamilyName());
	}

}
