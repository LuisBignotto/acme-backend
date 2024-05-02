package br.com.acmeairlines.auth.service;

import br.com.acmeairlines.auth.feignclients.UserFeignClient;
import br.com.acmeairlines.auth.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserFeignClient userFeignClient;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserModel findByEmail(String email){
        UserModel user = userFeignClient.getUserByEmail(email).getBody();
        if(user == null){
            logger.error("Email not found: " + email);
            throw new IllegalArgumentException("Email not found");
        }
        logger.info("Email found: " + email);
        return user;
    }

}
