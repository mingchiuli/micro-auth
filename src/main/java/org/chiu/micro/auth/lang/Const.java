package org.chiu.micro.auth.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mingchiuli
 * @create 2021-12-14 11:58 AM
 */
@Getter
@AllArgsConstructor
public enum Const {

    TEMP_EDIT_BLOG("temp_edit_blog:"),


    EMAIL_CODE("email_code"),

    PHONE_CODE("phone_code"),
    
    SMS_CODE("sms_code"),

    DAY_VISIT("{visit_record}_day"),

    WEEK_VISIT("{visit_record}_week"),

    MONTH_VISIT("{visit_record}_month"),

    YEAR_VISIT("{visit_record}_year"),

    EMAIL_KEY("email_validation:"),

    PHONE_KEY("phone_validation:"),

    PASSWORD_KEY("password_validation:"),
    
    TOKEN_PREFIX("Bearer "),

    ROLE_PREFIX("ROLE_"),
    
    BLOCK_USER("block_user:");



    private final String info;

}

