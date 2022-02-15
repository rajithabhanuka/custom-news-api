package org.comppress.customnewsapi.config;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfiguration {

    static String API_KEY = "3OcPF9Kg5bsCR1eYtMz4hixM1";
    static String API_SECRET = "Q2LZCxGna2ckmMNtJW0f3eKhBRmtkMRhPOv50mVSBsGZKZGBb7";
    static String ACCESS_TOKEN = "1444941818047700993-9PGTpStoWYUJDy5YIE1Sz1g9GKWDLc";
    static String ACCESS_TOKEN_SECRET = "cwsR23PMMqa5XS3mzlU0G2oTtGzqIDps4V6tsjBn3kbpO";

    @Bean
    public Twitter getTwitterApi(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(API_KEY)
                .setOAuthConsumerSecret(API_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        return new TwitterFactory(cb.build()).getInstance();
    }

    @Bean
    public TwitterClient getTwitterApiRedouane59(){
        return new TwitterClient(TwitterCredentials.builder()
                .accessToken(ACCESS_TOKEN)
                .accessTokenSecret(ACCESS_TOKEN_SECRET)
                .apiKey(API_KEY)
                .apiSecretKey(API_SECRET)
                .build());
    }
}
