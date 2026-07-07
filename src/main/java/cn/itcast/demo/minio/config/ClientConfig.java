package cn.itcast.demo.minio.config;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Value("${minio.ipaddr}")
    private String ipaddr;

    @Value("${minio.public-ipaddr}")
    private String publicIpaddr;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(ipaddr)
                .credentials(username,password)
                .build();
    }

    @Bean("minioPublicClient")
    public MinioClient minioPublicClient(){
        return MinioClient.builder()
                .endpoint(publicIpaddr)
                .credentials(username,password)
                .build();
    }
}
