package org.akhil.authorizationserver.federated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akhil.authorizationserver.model.AppUser;
import org.akhil.authorizationserver.model.GoogleUser;
import org.akhil.authorizationserver.repository.AppUserRepo;
import org.akhil.authorizationserver.repository.GoogleUserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
@Slf4j
@RequiredArgsConstructor
public final class UserRepositoryOAuth2UserHandler implements Consumer<OAuth2User> {

/*
    private final GoogleUserRepository googleUserRepository;
*/

    private final AppUserRepo appUserRepo;

    @Override
    public void accept(OAuth2User user) {
        // Capture user in a local data store on first authentication
      /*  if (!this.googleUserRepository.findByEmail(user.getName()).isPresent()) {
            GoogleUser googleUser = GoogleUser.fromOauth2User(user);
            log.info(googleUser.toString());
            this.googleUserRepository.save(googleUser);
        } else {
            log.info("username :  {}", user.getAttributes().get("given_name"));
        }*/
        if (!this.appUserRepo.findByEmail(user.getName()).isPresent()) {
            AppUser googleUser = AppUser.fromOauth2User(user);
            log.info(googleUser.toString());
            this.appUserRepo.save(googleUser);
        } else {
            log.info("username :  {}", user.getAttributes().get("given_name"));
        }
    }

//    /*static class UserRepository {
//
//        private final Map<String, OAuth2User> userCache = new ConcurrentHashMap<>();
//
//        public OAuth2User findByName(String name) {
//            return this.userCache.get(name);
//        }
//
//        public void save(OAuth2User oauth2User) {
//            System.out.println(oauth2User);
//            this.userCache.put(oauth2User.getName(), oauth2User);
//        }
//
//    }*/

}