package com.user.identity.constant;

import lombok.Getter;

@Getter
public enum BucketConstants {
    BUCKET_NAME("datpt-ce669.appspot.com"),
    AVATAR_FOLDER("avatar/"),

    URL_FIREBASE_API("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media");
    private final String value;

    BucketConstants(String value) {
        this.value = value;
    }
}
