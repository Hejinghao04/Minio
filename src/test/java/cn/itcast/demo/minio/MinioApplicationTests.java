package cn.itcast.demo.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class MinioApplicationTests {

    @Autowired
    private MinioClient minioClient;

    @Test
    void contextLoads() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

    }
    @Test
    void Makeminio() throws Exception{
        //String bu = "yaoce";
        String bu = "guidao";
        boolean b = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bu).build());
        if(!b){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bu)
                    .build());
        }else {
            System.out.println("buket已经存在");
        }


    }
    @Test
    void ListMinio() throws Exception{
        List<Bucket> buckets = minioClient.listBuckets();
        buckets.forEach(bucket -> {
            try {
                minioClient.removeBucket(RemoveBucketArgs.builder()
                                .bucket(bucket.name())
                        .build());
            } catch (ErrorResponseException e) {
                throw new RuntimeException(e);
            } catch (InsufficientDataException e) {
                throw new RuntimeException(e);
            } catch (InternalException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (InvalidResponseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (ServerException e) {
                throw new RuntimeException(e);
            } catch (XmlParserException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void DelMinio() throws Exception{
        minioClient.removeBucket(RemoveBucketArgs.builder()
                        .bucket("1001")

                .build());
    }
    //-------------------------------------------------------------------------------------------------------
    @Test
    void PutObject() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File file = new File("C:\\Users\\贺敬豪\\Pictures\\Screenshots\\屏幕截图 2026-04-21 173639.png");
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket("buket")
                .object("text1.png")
                .stream(new FileInputStream(file),file.length(),-1)
                .build());
    }

    @Test
    void getUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("buket")
                .object("text1.png")
                .method(Method.GET)
                .expiry(3, TimeUnit.MINUTES)
                .build());
        System.out.println(presignedObjectUrl);
    }

    @Test
    void policy() throws Exception{
        String name = "buket";
        boolean b = minioClient.bucketExists(BucketExistsArgs
                .builder()
                .bucket(name)
                .build());
        if(!b){
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(name)
                    .build());
        }
        String policyJson = "{\"version\" : \"2012-10-17\",\"Statement\":[{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::"+name+"/*\"]}]}";
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(name)
                        .config(policyJson)

                .build());


    }
    @Test
    void getObject() throws Exception{
        File file = new File("C:\\Users\\贺敬豪\\Pictures\\Screenshots\\屏幕截图 2026-04-21 173639.png");

        minioClient.putObject(PutObjectArgs.builder()
                .bucket("weixing")
                .object("a/b/c.png")
                        .stream(new FileInputStream(file),file.length(),-1)
                .build());

    }

}
