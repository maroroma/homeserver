package maroroma.homemusicplayer.config;

import maroroma.homemusicplayer.model.files.SmbFileAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SambaConfig {

//    static {
//        Config.setProperty("jcifs.resolverOrder", "DNS");
//    }

    @Bean
    public SmbFileAdapter.SambaUserSupplier sambaUserSupplier(
            @Value("${musicplayer.security.smb.username}") String userName,
            @Value("${musicplayer.security.smb.password}") String password) {
        return () -> new SmbFileAdapter.SmbUser(userName, password);
    }


}
