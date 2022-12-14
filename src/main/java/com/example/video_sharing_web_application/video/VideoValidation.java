package com.example.video_sharing_web_application.video;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class VideoValidation implements Predicate<String> {
    /**
     * Evaluates this predicate on the given argument.
     *
     * @param s the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(String s) {
        return patternMatches(s,"^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+");
    }

    public boolean patternMatches(String url, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(url)
                .matches();
    }
}
